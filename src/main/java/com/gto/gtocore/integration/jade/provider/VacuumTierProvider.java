package com.gto.gtocore.integration.jade.provider;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.machine.feature.IVacuumMachine;

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

public final class VacuumTierProvider extends CapabilityBlockProvider<IVacuumMachine> {

    public VacuumTierProvider() {
        super(GTOCore.id("vacuum_tier_provider"));
    }

    @Nullable
    @Override
    protected IVacuumMachine getCapability(Level level, BlockPos pos, @Nullable Direction side) {
        if (MetaMachine.getMachine(level, pos) instanceof IVacuumMachine machine) {
            return machine;
        }
        return null;
    }

    @Override
    protected void write(CompoundTag data, IVacuumMachine capability) {
        if (capability != null) data.putInt("vacuum_tier", capability.getVacuumTier());
    }

    @Override
    protected void addTooltip(CompoundTag capData, ITooltip tooltip, Player player, BlockAccessor block,
                              BlockEntity blockEntity, IPluginConfig config) {
        int tier = capData.getInt("vacuum_tier");
        if (tier == 0) return;
        tooltip.add(Component.translatable("gtocore.recipe.vacuum.tier", tier));
    }
}
