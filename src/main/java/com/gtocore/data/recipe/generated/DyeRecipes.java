package com.gtocore.data.recipe.generated;

import com.gtocore.common.block.ColorBlockMap;
import com.gtocore.common.data.GTOBlocks;
import com.gtocore.integration.Mods;

import com.gtolib.utils.RLUtils;
import com.gtolib.utils.TagUtils;

import com.gregtechceu.gtceu.common.data.GTRecipeCategories;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;

import static com.gregtechceu.gtceu.common.data.GTMaterials.CHEMICAL_DYES;
import static com.gtocore.common.data.GTORecipeTypes.CHEMICAL_BATH_RECIPES;
import static com.gtocore.common.data.GTORecipeTypes.EXTRACTOR_RECIPES;

public final class DyeRecipes {

    public static final Object2BooleanOpenHashMap<String> BWG = new Object2BooleanOpenHashMap<>();

    public static void init() {
        for (int i = 0; i < CHEMICAL_DYES.length; i++) {
            DyeColor color = DyeColor.values()[i];
            String colorName = color.getName();
            Block abs = ColorBlockMap.ABS_MAP.get(color);
            if (abs != null && color != DyeColor.WHITE) {
                CHEMICAL_BATH_RECIPES.recipeBuilder("abs_%s".formatted(colorName))
                        .inputItems(GTOBlocks.ABS_WHITE_CASING.asItem())
                        .inputFluids(CHEMICAL_DYES[i].getFluid(144))
                        .outputItems(abs.asItem())
                        .EUt(7).duration(200)
                        .category(GTRecipeCategories.CHEM_DYES)
                        .save();
            }
        }

        EXTRACTOR_RECIPES.builder("farmersrespite_red_dye")
                .inputItems("farmersrespite:coffee_berries")
                .outputItems("minecraft:red_dye")
                .duration(400)
                .EUt(2)
                .save();

        if (Mods.biomeswevegone()) {
            BWG.put("white", true);
            BWG.put("orange", false);
            BWG.put("magenta", false);
            BWG.put("light_blue", false);
            BWG.put("yellow", false);
            BWG.put("lime", false);
            BWG.put("pink", true);
            BWG.put("cyan", true);
            BWG.put("purple", true);
            BWG.put("blue", true);
            BWG.put("green", false);
            BWG.put("red", false);
            BWG.put("black", false);
            DyeRecipes.BWG.forEach((k, v) -> {
                EXTRACTOR_RECIPES.recipeBuilder(k + "_bwg_dye")
                        .inputItems(TagUtils.createTag(RLUtils.fromNamespaceAndPath("biomeswevegone", "dye/makes_" + k)))
                        .outputItems("minecraft:" + k + "_dye")
                        .duration(400)
                        .EUt(2)
                        .save();
                if (v) EXTRACTOR_RECIPES.recipeBuilder(k + "_bwg_2_dye")
                        .inputItems(TagUtils.createTag(RLUtils.fromNamespaceAndPath("biomeswevegone", "dye/makes_2_" + k)))
                        .outputItems("minecraft:" + k + "_dye")
                        .duration(400)
                        .EUt(2)
                        .save();
            });
        }

        addBotDye("white");
        addBotDye("light_gray");
        addBotDye("gray");
        addBotDye("black");
        addBotDye("brown");
        addBotDye("red");
        addBotDye("orange");
        addBotDye("yellow");
        addBotDye("lime");
        addBotDye("green");
        addBotDye("cyan");
        addBotDye("light_blue");
        addBotDye("blue");
        addBotDye("purple");
        addBotDye("magenta");
        addBotDye("pink");
    }

    private static void addBotDye(String dye) {
        EXTRACTOR_RECIPES.recipeBuilder(dye + "_petals")
                .inputItems(TagUtils.createTag(RLUtils.bot("petals/" + dye)))
                .outputItems("minecraft:" + dye + "_dye")
                .duration(400)
                .EUt(2)
                .save();
    }
}
