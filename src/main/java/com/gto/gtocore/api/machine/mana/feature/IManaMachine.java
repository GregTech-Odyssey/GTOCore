package com.gto.gtocore.api.machine.mana.feature;

import com.gto.gtocore.api.capability.IManaContainer;

import com.gregtechceu.gtceu.api.machine.feature.IMachineFeature;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.api.mana.ManaCollector;

public interface IManaMachine extends ManaCollector, IMachineFeature {

    @NotNull
    IManaContainer getManaContainer();

    @Override
    default Level getManaReceiverLevel() {
        return self().getLevel();
    }

    @Override
    default BlockPos getManaReceiverPos() {
        return self().getPos();
    }

    @Override
    default void onClientDisplayTick() {}

    @Override
    default float getManaYieldMultiplier(ManaBurst burst) {
        return 1;
    }

    @Override
    default int getMaxMana() {
        return getManaContainer().getSaturatedMaxMana();
    }

    @Override
    default int getCurrentMana() {
        return getManaContainer().getSaturatedCurrentMana();
    }

    @Override
    default boolean isFull() {
        return getManaContainer().getMaxMana() <= getManaContainer().getCurrentMana();
    }

    @Override
    default void receiveMana(int mana) {
        if (mana > 0) {
            getManaContainer().addMana(mana, 1, false);
        } else if (mana < 0) {
            getManaContainer().removeMana(-mana, 1, false);
        }
    }
}
