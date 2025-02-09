package com.gto.gtocore.api.machine.mana.feature;

import com.gto.gtocore.api.capability.IManaContainer;

import com.gregtechceu.gtceu.api.machine.MetaMachine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.api.mana.ManaCollector;

public interface IManaMachine extends ManaCollector {

    default MetaMachine getMachine() {
        return (MetaMachine) this;
    }

    @NotNull
    IManaContainer getManaContainer();

    @Override
    default Level getManaReceiverLevel() {
        return getMachine().getLevel();
    }

    @Override
    default BlockPos getManaReceiverPos() {
        return getMachine().getPos();
    }

    @Override
    default void onClientDisplayTick() {}

    @Override
    default float getManaYieldMultiplier(ManaBurst burst) {
        return 1;
    }

    @Override
    default int getMaxMana() {
        return getManaContainer().getMaxMana();
    }

    @Override
    default int getCurrentMana() {
        return getManaContainer().getCurrentMana();
    }

    @Override
    default boolean isFull() {
        return getMaxMana() <= getCurrentMana();
    }

    @Override
    default void receiveMana(int mana) {
        getManaContainer().addMana(mana, 1);
    }
}
