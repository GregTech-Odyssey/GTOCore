package com.gtocore.client;

import com.gtocore.integration.ae.hooks.ICraftAmountMenu;

import com.gtolib.api.network.NetworkPack;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IUIMachine;

import net.minecraft.client.Minecraft;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import appeng.api.stacks.GenericStack;
import appeng.api.stacks.KeyCounter;
import appeng.menu.me.common.MEStorageMenu;

public final class Message {

    public static void init() {}

    public static final NetworkPack OPEN_CONTAINER_C2S = NetworkPack.registerC2S("openContainerC2S", (p, b) -> tryOpenMetaMachineUI(p, b.readGlobalPos()));

    private static void tryOpenMetaMachineUI(ServerPlayer p, GlobalPos globalPos) {
        Level level = p.getServer().getLevel(globalPos.dimension());
        if (level != null && level.isLoaded(globalPos.pos())) {
            var be = level.getBlockEntity(globalPos.pos());
            if (be instanceof MetaMachineBlockEntity mbe && mbe.getMetaMachine() instanceof IUIMachine uiMachine) {
                uiMachine.tryToOpenUI(p, InteractionHand.MAIN_HAND,
                        new BlockHitResult(globalPos.pos().getCenter(), p.getDirection(), globalPos.pos(), false));
            }
        }
    }

    public static final NetworkPack ORDER_ITEM_C2S = NetworkPack.registerC2S("orderItemC2S", (p, b) -> {
        var containerId = b.readInt();
        if (p.containerMenu.containerId != containerId) return;
        if (!(p.containerMenu instanceof MEStorageMenu menu)) return;
        var keyCounter = new KeyCounter();
        var size = b.readVarInt();
        for (int i = 0; i < size; i++) {
            var stack = GenericStack.readBuffer(b);
            if (stack == null) return;
            keyCounter.add(stack.what(), stack.amount());
        }
        ICraftAmountMenu.open(p, menu.getLocator(), keyCounter, b.readLong());
    });

    public static class Client {

        public static void orderItem(KeyCounter whatToCraft, long initialAmount) {
            ORDER_ITEM_C2S.send(b -> {
                b.writeInt(Minecraft.getInstance().player.containerMenu.containerId);
                b.writeVarInt(whatToCraft.size());
                for (var entry : whatToCraft.entrySet()) {
                    GenericStack.writeBuffer(entry, b);
                }
                b.writeLong(initialAmount);
            });
        }
    }
}
