package com.gto.gtocore.integration.jade.provider;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.machine.feature.IUpgradeMachine;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.integration.jade.provider.CapabilityBlockProvider;
import com.gregtechceu.gtceu.utils.FormattingUtil;

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

public final class UpgradeModuleProvider extends CapabilityBlockProvider<IUpgradeMachine> {

    public UpgradeModuleProvider() {
        super(GTOCore.id("upgrade_module_provider"));
    }

    @Nullable
    @Override
    protected IUpgradeMachine getCapability(Level level, BlockPos pos, @Nullable Direction side) {
        if (MetaMachine.getMachine(level, pos) instanceof IUpgradeMachine upgradeMachine) return upgradeMachine;
        return null;
    }

    @Override
    protected void write(CompoundTag data, IUpgradeMachine capability) {
        if (capability != null) {
            data.putDouble("speed", capability.gtocore$getSpeed());
            data.putDouble("energy", capability.gtocore$getEnergy());
        }
    }

    @Override
    protected void addTooltip(CompoundTag capData, ITooltip tooltip, Player player, BlockAccessor block, BlockEntity blockEntity, IPluginConfig config) {
        double speed = capData.getDouble("speed");
        if (speed > 0) {
            tooltip.add(Component.translatable("item.gtocore.speed_upgrade_module").append(": x").append(FormattingUtil.formatNumbers(speed)));
        }
        double energy = capData.getDouble("energy");
        if (energy > 0) {
            tooltip.add(Component.translatable("item.gtocore.energy_upgrade_module").append(": x").append(FormattingUtil.formatNumbers(energy)));
        }
    }
}
