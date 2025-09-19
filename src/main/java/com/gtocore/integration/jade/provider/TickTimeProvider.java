package com.gtocore.integration.jade.provider;

import com.gtolib.GTOCore;
import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;

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

@DataGeneratorScanned
public class TickTimeProvider extends CapabilityBlockProvider<MetaMachine> {

    @RegisterLanguage(cn = "延迟：%s", en = "Latency: %s")
    public static final String LATENCY = "gtocore.jade.latency";

    public TickTimeProvider() {
        super(GTOCore.id("tick_time_provider"));
    }

    @Nullable
    @Override
    protected MetaMachine getCapability(Level level, BlockPos pos, BlockEntity blockEntity, @Nullable Direction side) {
        var machine = MetaMachine.getMachine(blockEntity);
        if (machine != null) {
            machine.observe();
            return machine;
        }
        return null;
    }

    @Override
    protected void write(CompoundTag data, MetaMachine capability) {
        if (capability != null) data.putInt("latency", capability.getTickTime());
    }

    @Override
    protected void addTooltip(CompoundTag capData, ITooltip tooltip, Player player, BlockAccessor block, BlockEntity blockEntity, IPluginConfig config) {
        long latency = capData.getInt("latency");
        if (latency == 0) return;
        tooltip.add(Component.translatable(LATENCY, latency).append(" μs"));
    }
}
