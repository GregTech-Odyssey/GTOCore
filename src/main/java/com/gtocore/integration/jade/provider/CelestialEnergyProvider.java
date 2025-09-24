package com.gtocore.integration.jade.provider;

import com.gtocore.common.machine.mana.CelestialCondenser;

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

public final class CelestialEnergyProvider extends CapabilityBlockProvider<CelestialCondenser> {

    public CelestialEnergyProvider() {
        super(GTOCore.id("celestial_energy_provider"));
    }

    @Nullable
    @Override
    protected CelestialCondenser getCapability(Level level, BlockPos pos, BlockEntity blockEntity, @Nullable Direction side) {
        if (MetaMachine.getMachine(blockEntity) instanceof CelestialCondenser machine) {
            return machine;
        }
        return null;
    }

    @Override
    protected void write(CompoundTag data, CelestialCondenser capability) {
        if (capability != null) {
            data.putInt("solaris", capability.getSolaris());
            data.putInt("lunara", capability.getLunara());
            data.putInt("voidflux", capability.getVoidflux());
            data.putInt("max_capacity", capability.getMaxMapacity());
        }
    }

    @Override
    protected void addTooltip(CompoundTag capData, ITooltip tooltip, Player player, BlockAccessor block, BlockEntity blockEntity, IPluginConfig config) {
        int solaris = capData.getInt("solaris");
        int lunara = capData.getInt("lunara");
        int voidflux = capData.getInt("voidflux");
        int maxCapacity = capData.getInt("max_capacity");

        if (solaris > 0) {
            tooltip.add(Component.translatable("gtocore.celestial_condenser.solaris", (solaris + "/" + maxCapacity)));
        }
        if (lunara > 0) {
            tooltip.add(Component.translatable("gtocore.celestial_condenser.lunara", (lunara + "/" + maxCapacity)));
        }
        if (voidflux > 0) {
            tooltip.add(Component.translatable("gtocore.celestial_condenser.voidflux", (voidflux + "/" + maxCapacity)));
        }
    }
}
