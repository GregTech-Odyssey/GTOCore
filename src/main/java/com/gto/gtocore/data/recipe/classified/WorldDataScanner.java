package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.GTOWorldGenLayers;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

interface WorldDataScanner {

    static void init(Consumer<FinishedRecipe> provider) {
        GTORecipeTypes.WORLD_DATA_SCANNER_RECIPES.recipeBuilder(GTOCore.id("end_data"))
                .inputItems(GTItems.TOOL_DATA_STICK.asStack())
                .inputItems(TagPrefix.dust, GTMaterials.Endstone, 64)
                .inputFluids(GTMaterials.PCBCoolant.getFluid(400))
                .inputFluids(GTMaterials.EnderAir.getFluid(64000))
                .outputItems(GTOItems.DIMENSION_DATA.get().getDimensionData(GTOWorldGenLayers.THE_END))
                .EUt(2048)
                .duration(4000)
                .dimension(GTOWorldGenLayers.PLUTO)
                .save(provider);

        GTORecipeTypes.WORLD_DATA_SCANNER_RECIPES.recipeBuilder(GTOCore.id("nether_data"))
                .inputItems(GTItems.TOOL_DATA_STICK.asStack())
                .inputItems(TagPrefix.dust, GTMaterials.Netherrack, 64)
                .inputFluids(GTMaterials.PCBCoolant.getFluid(200))
                .inputFluids(GTMaterials.NetherAir.getFluid(64000))
                .outputItems(GTOItems.DIMENSION_DATA.get().getDimensionData(GTOWorldGenLayers.THE_NETHER))
                .EUt(512)
                .duration(4000)
                .dimension(GTOWorldGenLayers.VENUS)
                .save(provider);

        GTORecipeTypes.WORLD_DATA_SCANNER_RECIPES.recipeBuilder(GTOCore.id("overworld_data"))
                .inputItems(GTItems.TOOL_DATA_STICK.asStack())
                .inputItems(TagPrefix.dust, GTMaterials.Stone, 64)
                .inputFluids(GTMaterials.PCBCoolant.getFluid(100))
                .inputFluids(GTMaterials.Air.getFluid(64000))
                .outputItems(GTOItems.DIMENSION_DATA.get().getDimensionData(GTOWorldGenLayers.OVERWORLD))
                .EUt(128)
                .duration(4000)
                .dimension(GTOWorldGenLayers.OVERWORLD)
                .save(provider);
    }
}
