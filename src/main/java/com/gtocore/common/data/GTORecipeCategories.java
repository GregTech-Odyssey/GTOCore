package com.gtocore.common.data;

import com.gregtechceu.gtceu.api.recipe.category.GTRecipeCategory;
import com.gregtechceu.gtceu.common.data.GTRecipeCategories;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

public final class GTORecipeCategories {

    public static void init() {}

    // 魔力组装机
    public static final GTRecipeCategory MANA_ASSEMBLER = GTRecipeCategories.register("mana_assembler", GTRecipeTypes.ASSEMBLER_RECIPES);
    // 发射探测卫星
    public static final GTRecipeCategory SATELLITE_LAUNCHER = GTRecipeCategories.register("satellite_launcher", GTRecipeTypes.DUMMY_RECIPES);
}
