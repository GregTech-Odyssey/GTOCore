package com.gto.gtocore.api.machine.trait;

import com.gto.gtocore.api.machine.feature.IParallelMachine;
import com.gto.gtocore.common.data.GTORecipeModifiers;

import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.util.Mth;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class CustomParallelTrait extends MultiblockTrait {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            CustomParallelTrait.class, MultiblockTrait.MANAGED_FIELD_HOLDER);

    @Persisted
    private int parallelNumber;
    private final boolean defaultParallel;
    private final Function<IParallelMachine, Integer> parallel;

    public CustomParallelTrait(WorkableMultiblockMachine machine, boolean defaultParallel, @NotNull Function<IParallelMachine, Integer> parallel) {
        super(machine);
        this.defaultParallel = defaultParallel;
        this.parallel = parallel;
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public boolean alwaysTryModifyRecipe() {
        return true;
    }

    @Override
    public GTRecipe modifyRecipe(@NotNull GTRecipe recipe) {
        if (defaultParallel) {
            recipe = GTORecipeModifiers.accurateParallel(getMachine(), recipe, getParallel());
        }
        return super.modifyRecipe(recipe);
    }

    @Override
    public void onStructureInvalid() {
        parallelNumber = 0;
    }

    public int getMaxParallel() {
        if (getMachine().isFormed()) {
            return parallel.apply((IParallelMachine) getMachine());
        }
        return 0;
    }

    public int getParallel() {
        if (parallelNumber == 0) parallelNumber = getMaxParallel();
        return Math.max(1, parallelNumber);
    }

    public void setParallel(int number) {
        parallelNumber = Mth.clamp(number, 1, getMaxParallel());
    }
}
