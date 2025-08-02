package com.gtocore.integration.jade.provider;

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

public class TickTimeProvider extends CapabilityBlockProvider<MetaMachine> {

    public TickTimeProvider() {
        super(GTOCore.id("tick_time_provider"));
    }

    @Nullable
    @Override
    protected MetaMachine getCapability(Level level, BlockPos pos, @Nullable Direction side) {
        var machine = MetaMachine.getMachine(level, pos);
        if (machine != null) {
            machine.observe();
            return machine;
        }
        return null;
    }

    @Override
    protected void write(CompoundTag data, MetaMachine capability) {
        if (capability != null) data.putInt("tick_time", capability.getTickTime());
    }

    @Override
    protected void addTooltip(CompoundTag capData, ITooltip tooltip, Player player, BlockAccessor block, BlockEntity blockEntity, IPluginConfig config) {
        long time = capData.getInt("tick_time");
        if (time == 0) return;
        tooltip.add(Component.translatable("tooltip.jade.delay", time).append(" μs"));
    }
}
