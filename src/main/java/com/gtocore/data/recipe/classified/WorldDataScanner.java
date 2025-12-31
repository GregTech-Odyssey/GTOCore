package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;

import com.gtolib.api.data.GTODimensions;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.WORLD_DATA_SCANNER_RECIPES;

final class WorldDataScanner {

    public static void init() {
        WORLD_DATA_SCANNER_RECIPES.recipeBuilder("end_data")
                .inputItems(GTItems.TOOL_DATA_STICK)
                .inputItems(TagPrefix.dust, GTMaterials.Endstone, 64)
                .inputFluids(GTMaterials.PCBCoolant, 1000)
                .inputFluids(GTMaterials.EnderAir, 64000)
                .outputItems(GTOItems.DIMENSION_DATA.get().getDimensionData(GTODimensions.THE_END))
                .EUt(480)
                .duration(4000)
                .dimension(GTODimensions.VOID)
                .save();

        WORLD_DATA_SCANNER_RECIPES.recipeBuilder("nether_data")
                .inputItems(GTItems.TOOL_DATA_STICK)
                .inputItems(TagPrefix.dust, GTMaterials.Netherrack, 64)
                .inputFluids(GTMaterials.PCBCoolant, 1000)
                .inputFluids(GTMaterials.NetherAir, 64000)
                .outputItems(GTOItems.DIMENSION_DATA.get().getDimensionData(GTODimensions.THE_NETHER))
                .EUt(120)
                .duration(4000)
                .dimension(GTODimensions.FLAT)
                .save();

        WORLD_DATA_SCANNER_RECIPES.recipeBuilder("otherside_data")
                .inputItems(GTItems.TOOL_DATA_STICK)
                .inputItems(TagPrefix.dust, GTMaterials.EchoShard, 64)
                .inputFluids(GTMaterials.PCBCoolant, 64000)
                .outputItems(GTOItems.DIMENSION_DATA.get().getDimensionData(GTODimensions.OTHERSIDE))
                .EUt(122880)
                .duration(4000)
                .dimension(GTODimensions.OTHERSIDE)
                .save();
    }
}
