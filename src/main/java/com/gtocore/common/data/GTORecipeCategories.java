package com.gtocore.common.data;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.recipe.category.GTRecipeCategory;
import com.gregtechceu.gtceu.common.data.GTRecipeCategories;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gtolib.api.gui.GTOGuiTextures;

public final class GTORecipeCategories {

    public static void init() {}

    // 魔力组装机
    public static final GTRecipeCategory MANA_ASSEMBLER = GTRecipeCategories.register("mana_assembler", GTRecipeTypes.ASSEMBLER_RECIPES);
    public static final GTRecipeCategory ROTOR_PLATING = GTRecipeCategories.register("rotor_plating", GTORecipeTypes.ELECTROPLATING_RECIPES)
            .setIcon(GTOGuiTextures.HIGH_SPEED_MODE.getSubTexture(0, 0.5, 1, 0.5));
    public static final GTRecipeCategory ESSENCE_RECOVERY = GTRecipeCategories.register("essence_recovery", GTRecipeTypes.CHEMICAL_BATH_RECIPES)
            .setIcon(GuiTextures.PROGRESS_BAR_BATH.getSubTexture(0, 0.5, 1, 0.5));
    public static final GTRecipeCategory THREE_DIMENSIONAL_PRINTER_RECIPES_DISPOSABLE = GTRecipeCategories.register("three_dimensional_printer_recipes_disposable", GTORecipeTypes.THREE_DIMENSIONAL_PRINTER_RECIPES);
}
