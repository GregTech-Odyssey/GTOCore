package com.gtocore.common.machine.multiblock.electric;

import com.gtolib.api.machine.multiblock.CoilMultiblockMachine;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.modifier.RecipeModifierFunction;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

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
    protected Recipe getRealRecipe(@NotNull Recipe recipe) {
        return RecipeModifierFunction.overclocking(this, recipe, false, 1, 1, (getCoilTier() + 2 > recipe.tier) ? 0.25 : 0.5);
    }
}
