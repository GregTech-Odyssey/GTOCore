package com.gtocore.integration.jade.provider;

import com.gtolib.GTOCore;
import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMaintenanceMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.integration.jade.provider.CapabilityBlockProvider;
import com.gregtechceu.gtceu.utils.FormattingUtil;

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
public class MaintenanceParamProvider extends CapabilityBlockProvider<IMaintenanceMachine> {

    private static final String KEY_DAMAGE_MULTIPLIER = "damageMultiplier";
    private static final String KEY_DAMAGE_CHANCE = "damageChance";

    @RegisterLanguage(cn = "损坏倍率: %s", en = "Damage Multiplier: %s")
    private static final String DAMAGE_MULTIPLIER = "gtocore.machine.maintenance_damage_multiplier.tooltip";
    @RegisterLanguage(cn = "损坏概率: %s", en = "Damage Chance: %s")
    private static final String DAMAGE_CHANCE = "gtocore.machine.maintenance_damage_chance.tooltip";
    @RegisterLanguage(cn = "维护仓损坏概率：%s", en = "Maintenance Damage Chance: %s")
    private static final String HATCH_DAMAGE_CHANCE = "gtocore.machine.maintenance_hatch_damage_chance.tooltip";
    @RegisterLanguage(cn = "0%% (%s%% 阈值)", en = "0%% (%s%% threshold)")
    private static final String DAMAGE_CHANCE_THRESHOLD = "gtocore.machine.maintenance_damage_chance.threshold";

    public MaintenanceParamProvider() {
        super(GTOCore.id("maintenance_param_provider"));
    }

    @Override
    protected @Nullable IMaintenanceMachine getCapability(Level level, BlockPos pos, @Nullable BlockEntity blockEntity, @Nullable Direction side) {
        if (!(blockEntity instanceof MetaMachineBlockEntity)) {
            return null;
        }
        MetaMachine machine = MetaMachine.getMachine(blockEntity);
        if (machine instanceof IMaintenanceMachine maintenanceMachine) {
            return maintenanceMachine;
        }
        if (machine instanceof IMultiController controller) {
            for (var part : controller.getParts()) {
                if (part instanceof IMaintenanceMachine maintenanceMachine) {
                    return maintenanceMachine;
                }
            }
        }
        return null;
    }

    @Override
    protected void write(CompoundTag tag, IMaintenanceMachine machine) {
        if (machine.isFullAuto() || machine.getController() == null) {
            return;
        }
        tag.putFloat(KEY_DAMAGE_MULTIPLIER, getDamageMultiplier(machine));
        tag.putFloat(KEY_DAMAGE_CHANCE, getDamageChance(machine));
    }

    @Override
    protected void addTooltip(CompoundTag data, ITooltip tooltip, Player player, BlockAccessor accessor, BlockEntity blockEntity, IPluginConfig config) {
        if (data.isEmpty()) {
            return;
        }
        boolean isPart = blockEntity instanceof MetaMachineBlockEntity machineBlockEntity && MetaMachine.getMachine(machineBlockEntity) instanceof IMaintenanceMachine;
        var damageChance = Component.literal(formatDamageChance(data.getFloat(KEY_DAMAGE_CHANCE))).withStyle(ChatFormatting.GOLD);
        if (isPart) {
            tooltip.add(Component.translatable(DAMAGE_MULTIPLIER,
                    Component.literal(formatMultiplier(data.getFloat(KEY_DAMAGE_MULTIPLIER))).withStyle(ChatFormatting.GOLD)));
            tooltip.add(Component.translatable(DAMAGE_CHANCE, damageChance));
        } else {
            tooltip.add(Component.translatable(HATCH_DAMAGE_CHANCE, damageChance));
        }
    }

    static float getDamageMultiplier(IMaintenanceMachine machine) {
        int pa = machine.getController().getParts().length;
        return machine.getTimeMultiplier() * GTOCore.difficulty * pa;
    }

    static float getDamageChance(IMaintenanceMachine machine) {
        return ((float) machine.getTimeActive() / IMaintenanceMachine.MINIMUM_MAINTENANCE_TIME) - 0.7F;
    }

    static String formatMultiplier(float value) {
        String number = value == Math.round(value) ? FormattingUtil.formatNumbers(Math.round(value)) : FormattingUtil.formatNumber2Places(value);
        return number + "x";
    }

    static String formatDamageChance(float damageChance) {
        if (damageChance < 0) {
            float threshold = (damageChance + 0.7F) / 0.7F * 100.0F;
            return Component.translatable(DAMAGE_CHANCE_THRESHOLD, FormattingUtil.formatNumber2Places(threshold)).getString();
        }
        return Math.round(damageChance * 100.0F) + "%";
    }
}
