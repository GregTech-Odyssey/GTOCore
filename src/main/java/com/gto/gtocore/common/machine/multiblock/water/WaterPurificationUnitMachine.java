package com.gto.gtocore.common.machine.multiblock.water;

import com.gto.gtocore.api.machine.multiblock.NoEnergyMultiblockMachine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;

@MethodsReturnNonnullByDefault
public abstract class WaterPurificationUnitMachine extends NoEnergyMultiblockMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            WaterPurificationUnitMachine.class, NoEnergyMultiblockMachine.MANAGED_FIELD_HOLDER);

    abstract long before();

    GTRecipe recipe;

    @Persisted
    long eut;

    WaterPurificationUnitMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    void setWorking(boolean isWorkingAllowed) {
        super.setWorkingEnabled(isWorkingAllowed);
    }

    @Override
    public void setWorkingEnabled(boolean isWorkingAllowed) {}

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
}
