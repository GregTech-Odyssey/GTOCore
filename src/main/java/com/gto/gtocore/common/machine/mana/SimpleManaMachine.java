package com.gto.gtocore.common.machine.mana;

import com.gto.gtocore.api.GTOValues;
import com.gto.gtocore.api.capability.IManaContainer;
import com.gto.gtocore.api.machine.SimpleNoEnergyMachine;
import com.gto.gtocore.api.machine.mana.feature.IManaEnergyMachine;
import com.gto.gtocore.api.machine.mana.feature.IManaMachine;
import com.gto.gtocore.api.machine.mana.trait.NotifiableManaContainer;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import org.jetbrains.annotations.NotNull;

public class SimpleManaMachine extends SimpleNoEnergyMachine implements IManaMachine, IManaEnergyMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            SimpleManaMachine.class, SimpleNoEnergyMachine.MANAGED_FIELD_HOLDER);

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Persisted
    @DescSynced
    private final NotifiableManaContainer manaContainer;

    public SimpleManaMachine(IMachineBlockEntity holder, int tier, Int2IntFunction tankScalingFunction, Object... args) {
        super(holder, tier, tankScalingFunction, args);
        int tierMana = GTOValues.MANA[tier];
        manaContainer = new NotifiableManaContainer(this, IO.IN, 64 * tierMana, tierMana);
        manaContainer.setAcceptDistributor(true);
        IManaEnergyMachine.addProxy(capabilitiesProxy, tierMana, manaContainer);
    }

    @Override
    public @NotNull IManaContainer getManaContainer() {
        return manaContainer;
    }

    @Override
    public boolean canReceiveManaFromBursts() {
        return true;
    }
}
