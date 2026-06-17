package com.gtocore.common.cover;

import com.gtolib.api.machine.feature.IPowerAmplifierMachine;

import com.gregtechceu.gtceu.api.capability.ICoverable;
import com.gregtechceu.gtceu.api.cover.CoverBehavior;
import com.gregtechceu.gtceu.api.cover.CoverDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.utils.TaskHandler;

import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public final class PowerAmplifierCover extends CoverBehavior {

    private final double multiplier;

    public PowerAmplifierCover(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide, int tier) {
        super(definition, coverHolder, attachedSide);
        this.multiplier = getMultiplier(tier);
    }

    public static double getMultiplier(int tier) {
        return 1 + tier * 0.5;
    }

    @Override
    public boolean canAttach() {
        return super.canAttach() && getMachine() instanceof IPowerAmplifierMachine powerAmplifierMachine && powerAmplifierMachine.gtolib$noPowerAmplifier();
    }

    @Override
    public void onAttached(@NotNull ItemStack itemStack, @NotNull ServerPlayer player) {
        super.onAttached(itemStack, player);
        updateCoverSub();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        updateCoverSub();
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        MetaMachine machine = getMachine();
        if (machine instanceof IPowerAmplifierMachine amplifierMachine) {
            amplifierMachine.gtolib$setHasPowerAmplifier(false);
            amplifierMachine.gtolib$setPowerAmplifier(1);
        }
    }

    private void updateCoverSub() {
        if (coverHolder.getLevel() instanceof ServerLevel level) {
            TaskHandler.enqueueTask(level, () -> {
                MetaMachine machine = getMachine();
                if (machine instanceof IPowerAmplifierMachine amplifierMachine && amplifierMachine.gtolib$noPowerAmplifier()) {
                    amplifierMachine.gtolib$setHasPowerAmplifier(true);
                    amplifierMachine.gtolib$setPowerAmplifier(multiplier);
                }
            });
        }
    }

    @Nullable
    private MetaMachine getMachine() {
        return MetaMachine.getMachine(coverHolder.holder());
    }
}
