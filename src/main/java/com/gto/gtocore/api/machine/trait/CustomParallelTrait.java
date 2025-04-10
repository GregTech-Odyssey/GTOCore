package com.gto.gtocore.api.machine.trait;

import com.gto.gtocore.api.machine.feature.multiblock.IMultiblockTraitHolder;
import com.gto.gtocore.api.machine.feature.multiblock.IParallelMachine;
import com.gto.gtocore.common.data.GTORecipeModifiers;

import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.util.Mth;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;

import java.util.function.ToIntFunction;

public class CustomParallelTrait extends MultiblockTrait {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            CustomParallelTrait.class, MultiblockTrait.MANAGED_FIELD_HOLDER);

    @Persisted
    private int parallelNumber;
    private final boolean defaultParallel;
    private final ToIntFunction<IParallelMachine> parallel;

    public CustomParallelTrait(IParallelMachine machine, boolean defaultParallel, @NotNull ToIntFunction<IParallelMachine> parallel) {
        super((IMultiblockTraitHolder) machine);
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
            return parallel.applyAsInt((IParallelMachine) getMachine());
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
