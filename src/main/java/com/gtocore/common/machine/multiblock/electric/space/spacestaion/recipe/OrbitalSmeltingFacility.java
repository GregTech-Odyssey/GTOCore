package com.gtocore.common.machine.multiblock.electric.space.spacestaion.recipe;

import com.gtocore.api.machine.ILargeSpaceStationMachine;
import com.gtocore.common.data.GTORecipeTypes;
import com.gtocore.common.machine.multiblock.electric.space.spacestaion.RecipeExtension;
import com.gtocore.common.recipe.condition.GravityCondition;

import com.gtolib.api.machine.trait.CoilTrait;

import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.ICoilMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;

import net.minecraft.network.chat.Component;

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
    public boolean checkConditions(@NotNull RecipeHandlerUnit unit, @NotNull GTRecipeDefinition recipe) {
        if (recipe.recipeType == GTORecipeTypes.BLAST_RECIPES) {
            for (var c : recipe.conditions) {
                if (c instanceof GravityCondition condition && condition.zero) return super.checkConditions(unit, recipe);
            }
            setIdleReason(Component.translatable("config.gtceu.option.recipes").append(" ").append(Component.translatable("gtocore.trade_group.unsatisfied")).append(": ").append(Component.translatable("gtocore.condition.zero_gravity")));
            return false;
        }
        return super.checkConditions(unit, recipe);
    }

    @Override
    public GTRecipe getRealRecipe(@NotNull RecipeHandlerUnit unit, @NotNull GTRecipe recipe) {
        if (recipe.definition.recipeType == GTORecipeTypes.BLAST_RECIPES) {
            recipe.durationMultiplier(0.25);
        }
        return super.getRealRecipe(unit, RecipeModifier.multiplier(recipe, 0.8, 0.6));
    }
}
