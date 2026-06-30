package com.gtocore.integration.jade.provider;

import com.gtolib.GTOCore;
import com.gtolib.api.capability.IHeatContainer;

import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
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

public final class TemperatureProvider extends CapabilityBlockProvider<IHeatContainer> {

    public TemperatureProvider() {
        super(GTOCore.id("temperature_provider"));
    }

    @Nullable
    @Override
    protected IHeatContainer getCapability(Level level, BlockPos pos, BlockEntity blockEntity, @Nullable Direction side) {
        return GTCapabilityHelper.getBlockEntityGTCapability(IHeatContainer.class, blockEntity, side);
    }

    @Override
    protected void write(CompoundTag data, IHeatContainer capability) {
        if (capability != null) {
            data.putDouble("temperature", capability.getTemperature());
            data.putLong("max_temperature", capability.getMaxTemperature());
        }
    }

    @Override
    protected void addTooltip(CompoundTag capData, ITooltip tooltip, Player player, BlockAccessor block, BlockEntity blockEntity, IPluginConfig config) {
        var max_temperature = capData.getLong("max_temperature");
        if (max_temperature == 0) return;
        tooltip.add(Component.translatable("gtocore.machine.current_temperature", (long) capData.getDouble("temperature") + " / " + max_temperature));
    }
}
