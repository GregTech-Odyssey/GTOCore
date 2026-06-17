package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.PIGMENT_MIXING_RECIPES;

final class PigmentMixing {

    public static void init() {
        int i = 1;

        PIGMENT_MIXING_RECIPES.builder("brown_dye")
                .inputItems(GTOItems.MAGENTA_DYE_MASTERBATCH)
                .inputItems(GTOItems.YELLOW_DYE_MASTERBATCH, 2)
                .inputItems(GTOItems.BLACK_DYE_MASTERBATCH, 3)
                .inputFluids(GTOMaterials.DyeCarrierResin, 16000)
                .outputFluids(GTMaterials.DyeBrown, 24000)
                .EUt(400)
                .duration(60)
                .circuitMeta(i++)
                .save();
        PIGMENT_MIXING_RECIPES.builder("magenta_dye")
                .inputItems(GTOItems.MAGENTA_DYE_MASTERBATCH, 6)
                .inputFluids(GTOMaterials.DyeCarrierResin, 16000)
                .outputFluids(GTMaterials.DyeMagenta, 24000)
                .EUt(400)
                .duration(60)
                .circuitMeta(i++)
                .save();

        PIGMENT_MIXING_RECIPES.builder("blue_dye")
                .inputItems(GTOItems.CYAN_DYE_MASTERBATCH, 3)
                .inputItems(GTOItems.MAGENTA_DYE_MASTERBATCH, 2)
                .inputItems(GTOItems.YELLOW_DYE_MASTERBATCH)
                .inputFluids(GTOMaterials.DyeCarrierResin, 16000)
                .outputFluids(GTMaterials.DyeBlue, 24000)
                .EUt(400)
                .duration(60)
                .circuitMeta(i++)
                .save();
        PIGMENT_MIXING_RECIPES.builder("pink_dye")
                .inputItems(GTOItems.MAGENTA_DYE_MASTERBATCH, 4)
                .inputItems(GTOItems.YELLOW_DYE_MASTERBATCH, 2)
                .inputFluids(GTOMaterials.DyeCarrierResin, 16000)
                .outputFluids(GTMaterials.DyePink, 24000)
                .EUt(400)
                .duration(60)
                .circuitMeta(i++)
                .save();
        PIGMENT_MIXING_RECIPES.builder("black_dye")
                .inputItems(GTOItems.BLACK_DYE_MASTERBATCH, 6)
                .inputFluids(GTOMaterials.DyeCarrierResin, 16000)
                .outputFluids(GTMaterials.DyeBlack, 24000)
                .EUt(400)
                .duration(60)
                .circuitMeta(i++)
                .save();
        PIGMENT_MIXING_RECIPES.builder("red_dye")
                .inputItems(GTOItems.MAGENTA_DYE_MASTERBATCH, 3)
                .inputItems(GTOItems.YELLOW_DYE_MASTERBATCH, 3)
                .inputFluids(GTOMaterials.DyeCarrierResin, 16000)
                .outputFluids(GTMaterials.DyeRed, 24000)
                .EUt(400)
                .duration(60)
                .circuitMeta(i++)
                .save();
        PIGMENT_MIXING_RECIPES.builder("orange_dye")
                .inputItems(GTOItems.MAGENTA_DYE_MASTERBATCH, 2)
                .inputItems(GTOItems.YELLOW_DYE_MASTERBATCH, 4)
                .inputFluids(GTOMaterials.DyeCarrierResin, 16000)
                .outputFluids(GTMaterials.DyeOrange, 24000)
                .EUt(400)
                .duration(60)
                .circuitMeta(i++)
                .save();
        PIGMENT_MIXING_RECIPES.builder("light_blue_dye")
                .inputItems(GTOItems.MAGENTA_DYE_MASTERBATCH, 2)
                .inputItems(GTOItems.CYAN_DYE_MASTERBATCH, 4)
                .inputFluids(GTOMaterials.DyeCarrierResin, 16000)
                .outputFluids(GTMaterials.DyeLightBlue, 24000)
                .EUt(400)
                .duration(60)
                .circuitMeta(i++)
                .save();
        PIGMENT_MIXING_RECIPES.builder("yellow_dye")
                .inputItems(GTOItems.YELLOW_DYE_MASTERBATCH, 6)
                .inputFluids(GTOMaterials.DyeCarrierResin, 16000)
                .outputFluids(GTMaterials.DyeYellow, 24000)
                .EUt(400)
                .duration(60)
                .circuitMeta(i++)
                .save();
        PIGMENT_MIXING_RECIPES.builder("green_dye")
                .inputItems(GTOItems.YELLOW_DYE_MASTERBATCH, 3)
                .inputItems(GTOItems.MAGENTA_DYE_MASTERBATCH)
                .inputItems(GTOItems.CYAN_DYE_MASTERBATCH, 2)
                .inputFluids(GTOMaterials.DyeCarrierResin, 16000)
                .outputFluids(GTMaterials.DyeGreen, 24000)
                .EUt(400)
                .duration(60)
                .circuitMeta(i++)
                .save();
        PIGMENT_MIXING_RECIPES.builder("cyan_dye")
                .inputItems(GTOItems.CYAN_DYE_MASTERBATCH, 6)
                .inputFluids(GTOMaterials.DyeCarrierResin, 16000)
                .outputFluids(GTMaterials.DyeCyan, 24000)
                .EUt(400)
                .duration(60)
                .circuitMeta(i++)
                .save();
        PIGMENT_MIXING_RECIPES.builder("light_gray_dye")
                .inputItems(GTOItems.BLACK_DYE_MASTERBATCH, 2)
                .inputItems(GTOItems.WHITE_DYE_MASTERBATCH, 4)
                .inputFluids(GTOMaterials.DyeCarrierResin, 16000)
                .outputFluids(GTMaterials.DyeLightGray, 24000)
                .EUt(400)
                .duration(60)
                .circuitMeta(i++)
                .save();
        PIGMENT_MIXING_RECIPES.builder("gray_dye")
                .inputItems(GTOItems.BLACK_DYE_MASTERBATCH, 4)
                .inputItems(GTOItems.WHITE_DYE_MASTERBATCH, 2)
                .inputFluids(GTOMaterials.DyeCarrierResin, 16000)
                .outputFluids(GTMaterials.DyeGray, 24000)
                .EUt(400)
                .duration(60)
                .circuitMeta(i++)
                .save();
        PIGMENT_MIXING_RECIPES.builder("white_dye")
                .inputItems(GTOItems.WHITE_DYE_MASTERBATCH, 6)
                .inputFluids(GTOMaterials.DyeCarrierResin, 16000)
                .outputFluids(GTMaterials.DyeWhite, 24000)
                .EUt(400)
                .duration(60)
                .circuitMeta(i++)
                .save();
        PIGMENT_MIXING_RECIPES.builder("purple_dye")
                .inputItems(GTOItems.CYAN_DYE_MASTERBATCH, 2)
                .inputItems(GTOItems.MAGENTA_DYE_MASTERBATCH, 3)
                .inputItems(GTOItems.YELLOW_DYE_MASTERBATCH)
                .inputFluids(GTOMaterials.DyeCarrierResin, 16000)
                .outputFluids(GTMaterials.DyePurple, 24000)
                .EUt(400)
                .duration(60)
                .circuitMeta(i++)
                .save();
        PIGMENT_MIXING_RECIPES.builder("lime_dye")
                .inputItems(GTOItems.CYAN_DYE_MASTERBATCH, 2)
                .inputItems(GTOItems.YELLOW_DYE_MASTERBATCH, 4)
                .inputFluids(GTOMaterials.DyeCarrierResin, 16000)
                .outputFluids(GTMaterials.DyeLime, 24000)
                .EUt(400)
                .duration(60)
                .circuitMeta(i++)
                .save();
    }
}
