package com.gto.gtocore.integration.jade.provider;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.machine.feature.ITemperatureMachine;

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

public final class TemperatureProvider extends CapabilityBlockProvider<ITemperatureMachine> {

    public TemperatureProvider() {
        super(GTOCore.id("temperature_provider"));
    }

    @Nullable
    @Override
    protected ITemperatureMachine getCapability(Level level, BlockPos pos, @Nullable Direction side) {
        if (MetaMachine.getMachine(level, pos) instanceof ITemperatureMachine machine) {
            return machine;
        }
        return null;
    }

    @Override
    protected void write(CompoundTag data, ITemperatureMachine capability) {
        if (capability != null) {
            data.putInt("temperature", capability.getTemperature());
            data.putInt("max_temperature", capability.getMaxTemperature());
        }
    }

    @Override
    protected void addTooltip(CompoundTag capData, ITooltip tooltip, Player player, BlockAccessor block, BlockEntity blockEntity, IPluginConfig config) {
        int max_temperature = capData.getInt("max_temperature");
        if (max_temperature == 0) return;
        tooltip.add(Component.translatable("gtocore.machine.current_temperature", capData.getInt("temperature") + " / " + max_temperature));
    }
}
