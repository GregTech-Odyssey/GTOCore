package com.gtocore.common.machine.mana;

import com.gtolib.api.GTOValues;
import com.gtolib.api.capability.IManaContainer;
import com.gtolib.api.machine.SimpleNoEnergyMachine;
import com.gtolib.api.machine.mana.feature.IManaMachine;
import com.gtolib.api.machine.mana.trait.NotifiableManaContainer;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.handler.IO;

import com.gto.datasynclib.annotations.SaveToDisk;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import org.jetbrains.annotations.NotNull;

abstract class SimpleManaMachine extends SimpleNoEnergyMachine implements IManaMachine {

    @SaveToDisk
    private final NotifiableManaContainer manaContainer;
    private final int tierMana;

    SimpleManaMachine(MetaMachineBlockEntity holder, int tier, Int2IntFunction tankScalingFunction, Object... args) {
        super(holder, tier, tankScalingFunction, args);
        tierMana = GTOValues.MANA[tier];
        manaContainer = new NotifiableManaContainer(this, IO.IN, 256L * tierMana, GTOValues.MANA[tier]);
        manaContainer.setAcceptDistributor(true);
    }

    @Override
    @NotNull
    public IManaContainer getManaContainer() {
        return manaContainer;
    }

    @Override
    public boolean canReceiveManaFromBursts() {
        return true;
    }

    int getTierMana() {
        return this.tierMana;
    }
}
