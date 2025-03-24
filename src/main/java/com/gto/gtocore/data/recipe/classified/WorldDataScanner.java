package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.GTODimensions;
import com.gto.gtocore.common.data.GTOItems;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gto.gtocore.common.data.GTORecipeTypes.WORLD_DATA_SCANNER_RECIPES;

interface WorldDataScanner {

    static void init() {
        WORLD_DATA_SCANNER_RECIPES.recipeBuilder(GTOCore.id("end_data"))
                .inputItems(GTItems.TOOL_DATA_STICK.asItem())
                .inputItems(TagPrefix.dust, GTMaterials.Endstone, 64)
                .inputFluids(GTMaterials.PCBCoolant.getFluid(1000))
                .inputFluids(GTMaterials.EnderAir.getFluid(64000))
                .outputItems(GTOItems.DIMENSION_DATA.get().getDimensionData(GTODimensions.THE_END))
                .EUt(480)
                .duration(4000)
                .dimension(GTODimensions.VOID)
                .save();

        WORLD_DATA_SCANNER_RECIPES.recipeBuilder(GTOCore.id("nether_data"))
                .inputItems(GTItems.TOOL_DATA_STICK.asItem())
                .inputItems(TagPrefix.dust, GTMaterials.Netherrack, 64)
                .inputFluids(GTMaterials.PCBCoolant.getFluid(1000))
                .inputFluids(GTMaterials.NetherAir.getFluid(64000))
                .outputItems(GTOItems.DIMENSION_DATA.get().getDimensionData(GTODimensions.THE_NETHER))
                .EUt(120)
                .duration(4000)
                .dimension(GTODimensions.FLAT)
                .save();

        WORLD_DATA_SCANNER_RECIPES.recipeBuilder(GTOCore.id("otherside_data"))
                .inputItems(GTItems.TOOL_DATA_STICK.asItem())
                .inputItems(TagPrefix.dust, GTMaterials.EchoShard, 64)
                .inputFluids(GTMaterials.PCBCoolant.getFluid(64000))
                .outputItems(GTOItems.DIMENSION_DATA.get().getDimensionData(GTODimensions.OTHERSIDE))
                .EUt(122880)
                .duration(4000)
                .dimension(GTODimensions.OTHERSIDE)
                .save();
    }
}
