package com.gtocore.data.recipe.misc;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.common.data.GTRecipes;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import commoble.jumbofurnace.JumboFurnace;
import commoble.jumbofurnace.recipes.JumboFurnaceRecipe;

import java.util.Arrays;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTOMaterials.*;

public class EIOAlternativeRecipes {

    public static void init() {
        jbRecipe("soularium", ChemicalHelper.get(ingot, Soularium), 1,
                ChemicalHelper.get(ingot, Gold), Blocks.SOUL_SAND.asItem().getDefaultInstance());
        jbRecipe("dark_steel", ChemicalHelper.get(ingot, DarkSteel), 1,
                ChemicalHelper.get(ingot, Iron), ChemicalHelper.get(gem, Coal), Blocks.OBSIDIAN.asItem().getDefaultInstance());
        jbRecipe("pulsating_alloy", ChemicalHelper.get(ingot, PulsatingAlloy, 2), 1,
                ChemicalHelper.get(ingot, Iron), ChemicalHelper.get(gem, EnderPearl));
        jbRecipe("copper_alloy", ChemicalHelper.get(ingot, CopperAlloy, 2), 1,
                ChemicalHelper.get(ingot, Copper), ChemicalHelper.get(ingot, Silicon));
        jbRecipe("conductive_alloy", ChemicalHelper.get(ingot, ConductiveAlloy, 2), 1,
                ChemicalHelper.get(ingot, CopperAlloy), ChemicalHelper.get(ingot, Iron), ChemicalHelper.get(ingot, Silver));
        jbRecipe("energetic_alloy", ChemicalHelper.get(ingot, EnergeticAlloy, 2), 1,
                ChemicalHelper.get(dust, Redstone), ChemicalHelper.get(ingot, Gold), ChemicalHelper.get(dust, Glowstone));
        jbRecipe("vibrant_alloy", ChemicalHelper.get(ingot, VibrantAlloy, 2), 1,
                ChemicalHelper.get(ingot, EnergeticAlloy), ChemicalHelper.get(gem, EnderPearl));
        jbRecipe("redstone_alloy", ChemicalHelper.get(ingot, RedstoneAlloy, 2), 1,
                ChemicalHelper.get(ingot, CopperAlloy), ChemicalHelper.get(dust, Redstone));
        jbRecipe("end_steel", ChemicalHelper.get(ingot, EndSteel, 2), 1,
                ChemicalHelper.get(ingot, DarkSteel), Blocks.OBSIDIAN.asItem().getDefaultInstance(), Blocks.END_STONE.asItem().getDefaultInstance());
    }

    private static void jbRecipe(String id, ItemStack result, float experience, ItemStack... ingredients) {
        NonNullList<Ingredient> ingredientList = NonNullList.create();
        ingredientList.addAll(Arrays.stream(ingredients).map(Ingredient::of).toList());
        GTRecipes.RECIPE_MAP.put(GTOCore.id(id),
                new JumboFurnaceRecipe(JumboFurnace.get().jumboSmeltingRecipeType.get(),
                        GTOCore.id(id), "smelting", ingredientList, result, experience));
    }
}
