package com.gtocore.common.machine.multiblock.electric;

import com.gtolib.api.machine.multiblock.CoilMultiblockMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;

import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class LargeChemicalReactorMachine extends CoilMultiblockMachine {

    public LargeChemicalReactorMachine(MetaMachineBlockEntity holder) {
        super(holder, false, false);
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        textList.add(Component.translatable("gtceu.multiblock.multi_furnace.heating_coil_level", getCoilTier() + 1));
    }

    @Nullable
    @Override
    public GTRecipe getRealRecipe(@NotNull RecipeHandlerUnit unit, @NotNull GTRecipe recipe) {
        return RecipeModifier.overclocking(this, unit, recipe, false, 1, 1, (getCoilTier() + 2 > recipe.tier) ? 0.25 : 0.5);
    }
}
