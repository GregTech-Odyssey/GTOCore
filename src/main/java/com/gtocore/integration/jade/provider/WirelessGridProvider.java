package com.gtocore.integration.jade.provider;

import com.gtocore.common.saved.WirelessSavedData;
import com.gtocore.integration.ae.WirelessMachine;

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
            String id = capability.getWirelessMachinePersisted().getGridConnectedName();
            data.putString("grid", id);
            String nick = id;
            try {
                var grid = WirelessSavedData.Companion.findGridByName(id);
                if (grid != null) {
                    grid.getNickname();
                    if (!grid.getNickname().isBlank()) {
                        nick = grid.getNickname();
                    }
                }
            } catch (Throwable ignored) {}
            data.putString("grid_nick", nick);
        }
    }

    @Override
    protected void addTooltip(CompoundTag capData, ITooltip tooltip, Player player, BlockAccessor block, BlockEntity blockEntity, IPluginConfig config) {
        String nick = capData.getString("grid_nick");
        if (nick.isBlank()) nick = capData.getString("grid");
        if (nick.isBlank()) return;

        tooltip.add(Component.translatable("gtocore.integration.ae.WirelessMachine.currentlyConnectedTo", nick));
    }
}
