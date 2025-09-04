package com.gtocore.mixin.ae2.menu;

import com.gtocore.api.ae2.crafting.OptimizedCraftingCpuLogic;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import appeng.api.stacks.GenericStack;
import appeng.me.cluster.implementations.CraftingCPUCluster;
import appeng.menu.AEBaseMenu;
import appeng.menu.me.crafting.CraftingCPUMenu;
import com.glodblock.github.extendedae.network.EPPNetworkHandler;
import com.glodblock.github.glodium.network.packet.SGenericPacket;
import com.glodblock.github.glodium.network.packet.sync.IActionHolder;
import com.glodblock.github.glodium.network.packet.sync.Paras;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Mixin(CraftingCPUMenu.class)
public class CraftingCPUMenuMixin extends AEBaseMenu implements IActionHolder {

    @Shadow(remap = false)
    private CraftingCPUCluster cpu;

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
}
