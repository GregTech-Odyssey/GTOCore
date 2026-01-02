package com.gtocore.common.machine.multiblock.electric.space.spacestaion.recipe;

import com.gtocore.api.machine.part.ILargeSpaceStationMachine;
import com.gtocore.common.machine.multiblock.electric.space.spacestaion.RecipeExtension;

import com.gtolib.api.machine.trait.CoilTrait;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.modifier.RecipeModifierFunction;

import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.ICoilMachine;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class OrbitalSmeltingFacility extends RecipeExtension implements ICoilMachine {

    private final CoilTrait coilTrait;

    public OrbitalSmeltingFacility(MetaMachineBlockEntity metaMachineBlockEntity) {
        super(metaMachineBlockEntity, ILargeSpaceStationMachine.twoWayPositionFunction(41));
        this.coilTrait = new CoilTrait(this, true, true);
    }

    @Override
    public ICoilType getCoilType() {
        return coilTrait.getCoilType();
    }

    @Override
    public Recipe getRealRecipe(@NotNull Recipe recipe) {
        return super.getRealRecipe(Objects.requireNonNull(RecipeModifierFunction.recipeReduction(0.8, 0.6).apply(this, recipe)));
    }
}
