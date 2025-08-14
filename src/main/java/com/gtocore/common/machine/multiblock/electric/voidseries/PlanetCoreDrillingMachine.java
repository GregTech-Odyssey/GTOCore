package com.gtocore.common.machine.multiblock.electric.voidseries;

import com.gtocore.common.data.GTOOres;

import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.api.recipe.RecipeRunner;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;

public final class PlanetCoreDrillingMachine extends ElectricMultiblockMachine {

    private Set<Material> materials;

    public PlanetCoreDrillingMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Nullable
    private Recipe getRecipe() {
        RecipeBuilder builder = getRecipeBuilder().duration(20).EUt(GTValues.VA[GTValues.MAX]);
        for (Material material : getMaterials()) {
            builder.outputItems(TagPrefix.ore, material, 65536);
        }
        Recipe recipe = builder.buildRawRecipe();
        if (RecipeRunner.matchTickRecipe(this, recipe) && RecipeRunner.matchRecipeOutput(this, recipe)) return recipe;
        return null;
    }

    private Set<Material> getMaterials() {
        if (materials == null) materials = GTOOres.ALL_ORES.get(Objects.requireNonNull(getLevel()).dimension().location()).keySet();
        return materials;
    }

    @Override
    public RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe, true);
    }
}
