package com.gtocore.mixin.ae2.menu;

import com.gtocore.api.ae2.crafting.OptimizedCraftingCpuLogic;
import com.gtocore.common.network.ServerMessage;
import com.gtocore.integration.ae.hooks.IPushResultsHandler;

import com.gtolib.api.ae2.IPatternProviderLogic;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.me.cluster.implementations.CraftingCPUCluster;
import appeng.menu.AEBaseMenu;
import appeng.menu.me.crafting.CraftingCPUMenu;
import com.glodblock.github.extendedae.network.EPPNetworkHandler;
import com.glodblock.github.glodium.network.packet.SGenericPacket;
import com.glodblock.github.glodium.network.packet.sync.IActionHolder;
import com.glodblock.github.glodium.network.packet.sync.Paras;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

@Mixin(CraftingCPUMenu.class)
public class CraftingCPUMenuMixin extends AEBaseMenu implements IActionHolder, IPushResultsHandler {

    @Shadow(remap = false)
    private CraftingCPUCluster cpu;

    @Unique
    private HashMultimap<AEKey, IPatternProviderLogic.PushResult> gto$lastCraftingResults;

    public CraftingCPUMenuMixin(MenuType<?> menuType, int id, Inventory playerInventory, Object host) {
        super(menuType, id, playerInventory, host);
    }

    @Override
    public @NotNull Map<String, Consumer<Paras>> getActionMap() {
        return Map.of("requestPendingBlocks", this::gto$requestPendingBlocks);
    }

    @Unique
    private void gto$requestPendingBlocks(Paras paras) {
        if (paras.get(0) instanceof ItemStack stack && cpu != null) {
            var gStack = GenericStack.fromItemStack(stack);
            if (gStack != null) {
                var aeKey = gStack.what();
                List<Object> toSend = new ObjectArrayList<>();
                ((OptimizedCraftingCpuLogic) cpu.craftingLogic).getPendingRequests(aeKey).forEach(gpos -> {
                    toSend.add(gpos.pos().asLong());
                    toSend.add(gpos.dimension().location().toString());
                });
                EPPNetworkHandler.INSTANCE.sendTo(
                        new SGenericPacket("hightlightBlock", toSend.toArray()),
                        (ServerPlayer) getPlayer());
            }
        }
    }

    @Inject(method = "broadcastChanges", at = @At(value = "INVOKE", target = "Lappeng/menu/me/crafting/CraftingCPUMenu;sendPacketToClient(Lappeng/core/sync/BasePacket;)V", shift = At.Shift.AFTER), remap = false)
    private void gto$onBroadcastChanges(CallbackInfo ci) {
        gto$lastCraftingResults = HashMultimap.create(((OptimizedCraftingCpuLogic) cpu.craftingLogic).getCraftingResults());
        ServerPlayer player = (ServerPlayer) getPlayer();
        MinecraftServer server = Objects.requireNonNull(player.getServer());
        ServerMessage.send(server, player, "craftMenuPushResults", (buf -> {
            buf.writeInt(containerId);
            buf.writeInt(gto$lastCraftingResults.keySet().size());
            gto$lastCraftingResults.asMap().forEach((key, results) -> {
                AEKey.writeKey(buf, key);
                buf.writeInt(results.size());
                results.forEach(r -> buf.writeInt(r.ordinal()));
            });
        }));
    }

    @Override
    public void gtocore$syncCraftingResults(FriendlyByteBuf buf) {
        var newMap = HashMultimap.<AEKey, IPatternProviderLogic.PushResult>create();
        int keySize = buf.readInt();
        for (int i = 0; i < keySize; i++) {
            var key = AEKey.readKey(buf);
            int resultSize = buf.readInt();
            for (int j = 0; j < resultSize; j++) {
                int ord = buf.readInt();
                newMap.put(key, IPatternProviderLogic.PushResult.values()[ord]);
            }
        }
        gto$lastCraftingResults = newMap;
    }

    @Override
    public Multimap<AEKey, IPatternProviderLogic.PushResult> gto$getLastCraftingResults() {
        return gto$lastCraftingResults;
    }
}
