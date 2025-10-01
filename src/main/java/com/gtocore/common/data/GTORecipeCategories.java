package com.gtocore.common.data;

import com.gtolib.GTOCore;
import com.gtolib.api.gui.GTOGuiTextures;
import com.gtolib.api.lang.CNEN;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.category.GTRecipeCategory;
import com.gregtechceu.gtceu.common.data.GTRecipeCategories;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.collection.O2OOpenCacheHashMap;

import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;

import java.util.Map;

public final class GTORecipeCategories {

    public static final Map<String, CNEN> LANG = GTCEu.isDataGen() ? new O2OOpenCacheHashMap<>() : null;

    public static void init() {}

    // 魔力组装机
    public static final GTRecipeCategory MANA_ASSEMBLER = register("mana_assembler", "魔力组装", GTRecipeTypes.ASSEMBLER_RECIPES);
    public static final GTRecipeCategory ROTOR_PLATING = register("rotor_plating", "转子镀膜", GTORecipeTypes.ELECTROPLATING_RECIPES)
            .setIcon(GTOGuiTextures.HIGH_SPEED_MODE.getSubTexture(0, 0.5, 1, 0.5));
    public static final GTRecipeCategory ESSENCE_RECOVERY = register("essence_recovery", "精粹回收", GTRecipeTypes.CHEMICAL_BATH_RECIPES)
            .setIcon(GuiTextures.PROGRESS_BAR_BATH.getSubTexture(0, 0.5, 1, 0.5));
    public static final GTRecipeCategory THREE_DIMENSIONAL_PRINTER_RECIPES_DISPOSABLE = register("three_dimensional_printer_recipes_disposable", new CNEN("3D打印：一次性工具", "3D Printer: Disposable"), GTORecipeTypes.THREE_DIMENSIONAL_PRINTER_RECIPES);

    public static final GTRecipeCategory CONDENSE_FLUID_TO_DUST = register("condense_fluid_to_dust", new CNEN("雾化冷凝：液态", "Atomizing Condensation: Fluid"),
            GTORecipeTypes.ATOMIZATION_CONDENSATION_RECIPES)
            .setIcon(new ResourceTexture(GTOCore.id("textures/gui/condense_from_fluid.png")));
    public static final GTRecipeCategory CONDENSE_PLASMA_TO_DUST = register("condense_plasma_to_dust", new CNEN("雾化冷凝：等离子态", "Atomizing Condensation: Plasma"),
            GTORecipeTypes.ATOMIZATION_CONDENSATION_RECIPES)
            .setIcon(new ResourceTexture(GTOCore.id("textures/gui/condense_from_plasma.png")));
    public static final GTRecipeCategory CONDENSE_MOLTEN_TO_DUST = register("condense_molten_to_dust", new CNEN("雾化冷凝：熔融态", "Atomizing Condensation: Molten"),
            GTORecipeTypes.ATOMIZATION_CONDENSATION_RECIPES)
            .setIcon(new ResourceTexture(GTOCore.id("textures/gui/condense_from_molten.png")));

    private static GTRecipeCategory register(String name, String cn, GTRecipeType type) {
        return register(name, new CNEN(cn, FormattingUtil.toEnglishName(name)), type);
    }

    private static GTRecipeCategory register(String name, CNEN cnen, GTRecipeType type) {
        if (LANG != null) LANG.put(name, cnen);
        return GTRecipeCategories.register(name, type);
    }
}
