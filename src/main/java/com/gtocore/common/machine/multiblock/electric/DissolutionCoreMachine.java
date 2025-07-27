package com.gtocore.common.machine.multiblock.electric;

import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.machine.trait.CoilTrait;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.modifier.ParallelLogic;
import com.gtolib.api.recipe.modifier.RecipeModifierFunction;

import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.ICoilMachine;
import com.gregtechceu.gtceu.api.recipe.content.Content;

import net.minecraftforge.fluids.FluidStack;

import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DissolutionCoreMachine extends ElectricMultiblockMachine implements ICoilMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(DissolutionCoreMachine.class, ElectricMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    @NotNull
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private int parallels = 1;

    public DissolutionCoreMachine(IMachineBlockEntity holder) {
        super(holder);
        coilTrait = new CoilTrait(this, false, true);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        parallels = 1 << (getTemperature() - 5000) / 900;
    }

    @Nullable
    @Override
    protected Recipe getRealRecipe(@NotNull Recipe recipe) {
        List<Content> fluidList = recipe.inputs.getOrDefault(FluidRecipeCapability.CAP, null);
        FluidStack fluidStack1 = FluidRecipeCapability.CAP.of(fluidList.get(0).getContent()).getStacks()[0];
        FluidStack fluidStack2 = FluidRecipeCapability.CAP.of(fluidList.get(1).getContent()).getStacks()[0];
        long[] a = getFluidAmount(fluidStack1.getFluid(), fluidStack2.getFluid());
        if (a[1] > 0) {
            recipe = RecipeModifierFunction.overclocking(this, RecipeModifierFunction.hatchParallel(this, recipe));
            if (recipe != null) {
                if ((double) a[0] / a[1] != ((double) fluidStack1.getAmount()) / fluidStack2.getAmount()) {
                    recipe.outputs.clear();
                }
                return ParallelLogic.accurateParallel(this, recipe, parallels);
            }
        }
        return null;
    }

    private final CoilTrait coilTrait;

    @Override
    public int getTemperature() {
        return coilTrait.getTemperature();
    }

    @Override
    public ICoilType getCoilType() {
        return coilTrait.getCoilType();
    }
}
