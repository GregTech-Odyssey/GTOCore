package com.gto.gtocore.common.machine.multiblock.noenergy;

import com.gto.gtocore.api.machine.multiblock.NoEnergyMultiblockMachine;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeModifiers;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class HeatExchangerMachine extends NoEnergyMultiblockMachine implements IExplosionMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            HeatExchangerMachine.class, NoEnergyMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private static final Fluid Steam = GTMaterials.Steam.getFluid();
    private static final Fluid HighPressureSteam = GTOMaterials.HighPressureSteam.getFluid();
    private static final Fluid SupercriticalSteam = GTOMaterials.SupercriticalSteam.getFluid();
    private static final Fluid DistilledWater = GTMaterials.DistilledWater.getFluid();

    public HeatExchangerMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Persisted
    private int count;

    @Persisted
    private boolean water;

    @Nullable
    @Override
    protected GTRecipe getRealRecipe(@NotNull GTRecipe recipe) {
        water = false;
        if (FluidRecipeCapability.CAP.of(recipe.inputs.get(FluidRecipeCapability.CAP).get(1).getContent()).getStacks()[0].getFluid() == Fluids.WATER) {
            water = true;
            recipe.outputs.clear();
            recipe = GTORecipeModifiers.accurateParallel(this, recipe, Integer.MAX_VALUE);
            count = 6400 * recipe.parallels;
            return recipe;
        }
        GTRecipe result = GTORecipeModifiers.accurateParallel(this, GTORecipeBuilder.ofRaw()
                .inputFluids(FluidRecipeCapability.CAP.of(recipe.inputs
                        .get(FluidRecipeCapability.CAP).get(0).getContent()))
                .outputFluids(FluidRecipeCapability.CAP.of(recipe.outputs
                        .get(FluidRecipeCapability.CAP).get(0).getContent()))
                .duration(200)
                .buildRawRecipe(), Integer.MAX_VALUE);
        long count = result.parallels * recipe.data.getLong("eu");
        if (inputFluid(DistilledWater, (int) (count / 160))) {
            this.count = (int) (count / 4);
            return result;
        } else {
            doExplosion(Math.min(10, count / 1000));
        }
        return null;
    }

    @Override
    public void onRecipeFinish() {
        super.onRecipeFinish();
        if (count != 0) {
            if (getRecipeLogic().getConsecutiveRecipes() > 4) {
                if (water) {
                    outputFluid(HighPressureSteam, count);
                } else {
                    outputFluid(SupercriticalSteam, count / 4);
                }
            } else {
                if (water) {
                    outputFluid(Steam, count << 2);
                } else {
                    outputFluid(HighPressureSteam, count);
                }
            }
        }
        count = 0;
    }
}
