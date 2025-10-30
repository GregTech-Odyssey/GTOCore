package com.gtocore.common.network;

import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachine;

import com.gtolib.api.ae2.me2in1.Me2in1Menu;
import com.gtolib.api.network.NetworkPack;
import com.gtolib.api.player.IEnhancedPlayer;

import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import appeng.api.config.Actionable;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.api.storage.MEStorage;
import appeng.me.helpers.PlayerSource;
import appeng.menu.me.common.MEStorageMenu;
import appeng.menu.me.items.PatternEncodingTermMenu;
import com.lowdragmc.lowdraglib.gui.modular.ModularUIContainer;
import de.mari_023.ae2wtlib.wct.CraftingTerminalHandler;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

// todo 重构
@Deprecated
public final class ClientMessage {

    private static final Int2ObjectOpenHashMap<NetworkPack> PACKS = new Int2ObjectOpenHashMap<>();

    public static void sendData(String channel, @Nullable CompoundTag data) {
        send(channel, buf -> buf.writeNbt(data));
    }

    public static void send(String channel, @NotNull Consumer<FriendlyByteBuf> data) {
        PACKS.computeIfAbsent(channel.hashCode(), k -> NetworkPack.registerC2S(k, (p, b) -> handle(channel, p, b))).send(data);
    }

    private static void handle(String channel, @NotNull ServerPlayer serverPlayer, FriendlyByteBuf data) {
        switch (channel) {
            case "key" -> KeyMessage.pressAction(serverPlayer, data.readVarInt());
            case "shiftKeypress" -> IEnhancedPlayer.of(serverPlayer).getPlayerData().shiftState = data.readBoolean();
            case "pattern_buffer_index" -> {
                if (serverPlayer.containerMenu instanceof ModularUIContainer container) {
                    for (var widget : container.getModularUI().mainGroup.widgets) {
                        if (widget instanceof FancyMachineUIWidget uiWidget && uiWidget.getMainPage() instanceof MEPatternBufferPartMachine machine) {
                            machine.onMouseClicked(data.readVarInt());
                        }
                    }
                }
            }
            case "emiStackInteraction" -> {
                if (data.readVarInt() != serverPlayer.containerMenu.containerId) return;
                MEStorage ae = null;
                switch (serverPlayer.containerMenu) {
                    case PatternEncodingTermMenu ignored -> {
                        return;
                    }
                    case Me2in1Menu ignored -> {
                        return;
                    }
                    case MEStorageMenu menu -> ae = menu.getHost().getInventory();
                    default -> {}
                }

                if (ae == null) {
                    var cTHandler = CraftingTerminalHandler.getCraftingTerminalHandler(serverPlayer);

                    if (cTHandler.getLocator() == null || cTHandler.getTargetGrid() == null)
                        return;
                    if (cTHandler.getTargetGrid().getStorageService() == null)
                        return;
                    ae = cTHandler.getTargetGrid().getStorageService().getInventory();
                }
                if (ae == null) return;

                GenericStack stack = GenericStack.readBuffer(data);
                if (stack == null) return;
                AEKey what = stack.what();
                long amount = stack.amount();

                PlayerSource playerSource = new PlayerSource(serverPlayer, null);
                Inventory playerInv = serverPlayer.getInventory();
                AEItemKey itemKey;
                if (what instanceof AEItemKey aeItemKey) {
                    itemKey = aeItemKey;
                    amount = ae.extract(what, amount, Actionable.MODULATE, playerSource);
                } else if (what instanceof AEFluidKey fluidKey) {
                    // This costs no energy, but who cares...
                    long fluidCellRemaining = ae.extract(AEItemKey.of(GTItems.FLUID_CELL.asItem()), amount, Actionable.MODULATE, playerSource);
                    long fluidAmount = ae.extract(what, fluidCellRemaining * 1000, Actionable.SIMULATE, playerSource);
                    long exactFluidCells = fluidAmount / 1000;
                    if (exactFluidCells > 0) {
                        ItemStack is = GTItems.FLUID_CELL.asStack();
                        CompoundTag fluidTag = is.getOrCreateTag();
                        CompoundTag fluid = new CompoundTag();
                        fluid.putString("FluidName", fluidKey.getId().toString());
                        fluid.putInt("Amount", 1000);
                        fluidTag.put("Fluid", fluid);
                        is.setTag(fluidTag);
                        itemKey = AEItemKey.of(is);
                        amount = exactFluidCells;
                        ae.extract(what, exactFluidCells * 1000, Actionable.MODULATE, playerSource);
                        ae.insert(what, fluidCellRemaining - exactFluidCells, Actionable.MODULATE, playerSource);
                    } else {
                        return;
                    }
                } else {
                    return;
                }

                serverPlayer.playNotifySound(SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((serverPlayer.getRandom().nextFloat() - serverPlayer.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);

                int mode = data.readVarInt(); // 0: toInventory, 1: toCarried
                if (mode == 1) {
                    var carried = serverPlayer.containerMenu.getCarried();
                    if (carried.isEmpty()) {
                        ItemStack toCarried = itemKey.toStack((int) amount);
                        serverPlayer.containerMenu.setCarried(toCarried);
                    } else if (ItemStack.isSameItemSameTags(carried, itemKey.toStack(1))) {
                        long canAdd = carried.getMaxStackSize() - carried.getCount();
                        long toAdd = Math.min(canAdd, amount);
                        carried.grow((int) toAdd);
                        serverPlayer.containerMenu.setCarried(carried);
                        amount -= toAdd;
                    }
                    if (amount <= 0) return;
                }
                playerInv.add(itemKey.toStack((int) amount));
            }
        }
    }
}
