package com.gtocore.client;

import com.gtolib.api.network.NetworkPack;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IUIMachine;

import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public final class Message {

    public static void init() {}

    public static final NetworkPack NETWORK_PACK = NetworkPack.registerC2S("openContainerC2S", (p, b) -> tryOpenMetaMachineUI(p, b.readGlobalPos()));

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
}
