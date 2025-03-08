package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.api.machine.multiblock.TierCasingMultiblockMachine;
import com.gto.gtocore.common.data.GTORecipeModifiers;
import com.gto.gtocore.utils.SphereExplosion;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.core.BlockPos;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.gto.gtocore.api.GTOValues.STELLAR_CONTAINMENT_TIER;

public final class StellarForgeMachine extends TierCasingMultiblockMachine implements IExplosionMachine {

    public StellarForgeMachine(IMachineBlockEntity holder) {
        super(holder, STELLAR_CONTAINMENT_TIER);
    }

    @Nullable
    @Override
    protected GTRecipe getRealRecipe(@NotNull GTRecipe recipe) {
        recipe = GTORecipeModifiers.overclocking(this, recipe);
        if (getRecipeLogic().getConsecutiveRecipes() > 1) {
            recipe.duration = Math.max(recipe.duration / 2, 1);
        }
        return recipe;
    }

    @Override
    public void doDamping(RecipeLogic recipeLogic) {
        recipeLogic.interruptRecipe();
        doExplosion(1);
    }

    @Override
    public void doExplosion(BlockPos pos, float explosionPower) {
        var machine = self();
        var level = machine.getLevel();
        if (level != null) {
            level.removeBlock(pos, false);
            SphereExplosion.explosion(pos, level, 100, true, true);
        }
    }
}
