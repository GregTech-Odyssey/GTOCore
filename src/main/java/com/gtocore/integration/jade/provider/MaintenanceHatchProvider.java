package com.gtocore.integration.jade.provider;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMaintenanceMachine;
import com.gregtechceu.gtceu.integration.jade.provider.CapabilityBlockProvider;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gtolib.GTOCore;
import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.machine.feature.IGravityPartMachine;
import net.minecraft.ChatFormatting;
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
public class MaintenanceHatchProvider extends CapabilityBlockProvider<IMaintenanceMachine> {
    private static final String KEY_DURATION_MULTIPLIER = "durationMultiplier";
    private static final String KEY_GRAVITY = "gravity";

    private static final String DURATION_MULTIPLIER = "gtocore.machine.duration_multiplier.tooltip";
    @RegisterLanguage(cn = "当前重力: %s", en = "Current Gravity: %s")
    private static final String GRAVITY = "tooltip.jade.current_gravity";

    public MaintenanceHatchProvider() {
        super(GTOCore.id("maintenance_hatch_provider"));
    }

    @Override
    protected @Nullable IMaintenanceMachine getCapability(Level level, BlockPos blockPos, @Nullable BlockEntity blockEntity, @Nullable Direction direction) {
        var machine = MetaMachine.getMachine(blockEntity);
        if (machine instanceof IMaintenanceMachine mm) {
            return mm;
        }
        return null;
    }

    @Override
    protected void write(CompoundTag tag, IMaintenanceMachine machine) {
        tag.putFloat(KEY_DURATION_MULTIPLIER, machine.getDurationMultiplier());
        if (machine instanceof IGravityPartMachine gm) {
            tag.putInt(KEY_GRAVITY, gm.getCurrentGravity());
        }
    }

    @Override
    protected void addTooltip(CompoundTag capData, ITooltip tooltip, Player player, BlockAccessor accessor, BlockEntity blockEntity, IPluginConfig config) {
        if (capData.contains(KEY_DURATION_MULTIPLIER)) {
            float multiplier = capData.getFloat(KEY_DURATION_MULTIPLIER);
            if (multiplier != 1.0F) {
                Component multiplierText = Component.literal(FormattingUtil.formatNumbers(multiplier)).withStyle(multiplier > 1.0F ? ChatFormatting.RED : ChatFormatting.GREEN);
                tooltip.add(Component.translatable(DURATION_MULTIPLIER, multiplierText));
            }
        }
        if (capData.contains(KEY_GRAVITY)) {
            tooltip.add(Component.translatable(GRAVITY, capData.getInt(KEY_GRAVITY)));
        }
    }
}