package com.gto.gtocore.common.machine.multiblock.noenergy;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.machine.multiblock.NoEnergyMultiblockMachine;
import com.gto.gtocore.common.data.GTORecipeModifiers;
import com.gto.gtocore.init.GTOMaterials;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

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

    private static final Fluid SupercriticalSteam = GTOMaterials.SupercriticalSteam.getFluid();
    private static final Fluid DistilledWater = GTMaterials.DistilledWater.getFluid();

    public HeatExchangerMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Persisted
    private int count;

    @Nullable
    @Override
    protected GTRecipe getRealRecipe(@NotNull GTRecipe recipe) {
        if (FluidRecipeCapability.CAP.of(recipe.inputs.get(FluidRecipeCapability.CAP)
                .get(1).getContent()).getStacks()[0].getFluid() == Fluids.WATER) {
            return GTORecipeModifiers.accurateParallel(this, recipe, Integer.MAX_VALUE);
        }
        GTRecipe result = GTORecipeModifiers.accurateParallel(this, new GTRecipeBuilder(GTOCore.id("heat_exchanger"), GTRecipeTypes.DUMMY_RECIPES)
                .inputFluids(FluidRecipeCapability.CAP.of(recipe.inputs
                        .get(FluidRecipeCapability.CAP).get(0).getContent()))
                .outputFluids(FluidRecipeCapability.CAP.of(recipe.outputs
                        .get(FluidRecipeCapability.CAP).get(0).getContent()))
                .duration(200)
                .buildRawRecipe(), Integer.MAX_VALUE);
        long count = result.parallels * recipe.data.getLong("eu");
        if (MachineUtils.inputFluid(this, DistilledWater, (int) (count / 160))) {
            this.count = (int) (count / 16);
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
            MachineUtils.outputFluid(this, SupercriticalSteam, count);
        }
        count = 0;
    }
}
