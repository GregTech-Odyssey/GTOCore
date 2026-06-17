package com.gtocore.integration.jade.provider;

import com.gtocore.common.saved.WirelessNetworkSavedData;
import com.gtocore.integration.ae.wireless.WirelessMachine;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.integration.jade.provider.CapabilityBlockProvider;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import org.jetbrains.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public final class WirelessGridProvider extends CapabilityBlockProvider<WirelessMachine> {

    public WirelessGridProvider() {
        super(GTOCore.id("wireless_grid_provider"));
    }

    @Override
    protected @Nullable WirelessMachine getCapability(Level level, BlockPos pos, BlockEntity blockEntity, @Nullable Direction side) {
        var machine = MetaMachine.getMachine(blockEntity);
        if (machine instanceof WirelessMachine wm) {
            return wm;
        }
        return null;
    }

    @Override
    protected void write(CompoundTag data, WirelessMachine capability) {
        if (capability != null) {
            String id = capability.getConnectedNetworkId();
            data.putString("network", id);
            String nick = id;
            try {
                var net = WirelessNetworkSavedData.Companion.findNetworkById(id);
                if (net != null && !net.getNickname().isBlank()) {
                    nick = net.getNickname();
                }
            } catch (Throwable ignored) {}
            data.putString("network_nick", nick);
            data.putString("node_type", capability.getNodeType().name());
        }
    }

    @Override
    protected void addTooltip(CompoundTag capData, ITooltip tooltip, Player player, BlockAccessor block, BlockEntity blockEntity, IPluginConfig config) {
        String nick = capData.getString("network_nick");
        if (nick.isBlank()) nick = capData.getString("network");
        if (nick.isBlank()) return;

        String nodeType = capData.getString("node_type");
        tooltip.add(Component.translatable(WirelessMachine.KEY_CONNECTED, nick));
        if (!nodeType.isBlank()) {
            tooltip.add(Component.translatable(WirelessMachine.KEY_NODE_TYPE, nodeType));
        }
    }
}
