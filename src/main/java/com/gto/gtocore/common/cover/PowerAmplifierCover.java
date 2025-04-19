package com.gto.gtocore.common.cover;

import com.gto.gtocore.api.machine.feature.IPowerAmplifierMachine;

import com.gregtechceu.gtceu.api.capability.ICoverable;
import com.gregtechceu.gtceu.api.cover.CoverBehavior;
import com.gregtechceu.gtceu.api.cover.CoverDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;

import net.minecraft.core.Direction;
import net.minecraft.server.TickTask;
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
        return 1 + tier * 0.25;
    }

    private MetaMachine machine;

    @Override
    public boolean canAttach() {
        return super.canAttach() && getMachine() instanceof IPowerAmplifierMachine powerAmplifierMachine && powerAmplifierMachine.gtocore$noPowerAmplifier();
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
            amplifierMachine.gtocore$setHasPowerAmplifier(false);
            amplifierMachine.gtocore$setPowerAmplifier(1);
            amplifierMachine.getRecipeLogic().markLastRecipeDirty();
        }
        this.machine = null;
    }

    private void updateCoverSub() {
        if (coverHolder.getLevel() instanceof ServerLevel level) {
            level.getServer().tell(new TickTask(1, () -> {
                MetaMachine machine = getMachine();
                if (machine instanceof IPowerAmplifierMachine amplifierMachine && amplifierMachine.gtocore$noPowerAmplifier()) {
                    amplifierMachine.gtocore$setHasPowerAmplifier(true);
                    amplifierMachine.gtocore$setPowerAmplifier(multiplier);
                    amplifierMachine.getRecipeLogic().markLastRecipeDirty();
                }
            }));
        }
    }

    @Nullable
    private MetaMachine getMachine() {
        if (machine == null) machine = MetaMachine.getMachine(coverHolder.getLevel(), coverHolder.getPos());
        return machine;
    }
}
