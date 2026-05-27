package com.gtocore.common.machine.multiblock.electric.space.spacestaion.recipe;

import com.gtocore.api.machine.ILargeSpaceStationMachine;
import com.gtocore.common.machine.multiblock.electric.space.spacestaion.RecipeExtension;

import com.gtolib.api.machine.trait.CoilTrait;

import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.ICoilMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;

import org.jetbrains.annotations.NotNull;

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
    public GTRecipe getRealRecipe(@NotNull RecipeHandlerUnit unit, @NotNull GTRecipe recipe) {
        return super.getRealRecipe(unit, RecipeModifier.multiplier(recipe, 0.8, 0.6));
    }
}
