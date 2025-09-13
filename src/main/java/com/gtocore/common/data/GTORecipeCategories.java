package com.gtocore.common.data;

import com.gtolib.api.gui.GTOGuiTextures;

import com.gregtechceu.gtceu.api.recipe.category.GTRecipeCategory;
import com.gregtechceu.gtceu.common.data.GTRecipeCategories;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

public final class GTORecipeCategories {

    public static void init() {}

    // 魔力组装机
    public static final GTRecipeCategory MANA_ASSEMBLER = GTRecipeCategories.register("mana_assembler", GTRecipeTypes.ASSEMBLER_RECIPES);
    public static final GTRecipeCategory ROTOR_PLATING = GTRecipeCategories.register("rotor_plating", GTORecipeTypes.ELECTROPLATING_RECIPES)
            .setIcon(GTOGuiTextures.HIGH_SPEED_MODE.getSubTexture(0, 0.5, 1, 0.5));
}
