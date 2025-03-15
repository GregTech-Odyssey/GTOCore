package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.data.TagsHandler;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.Items;

import static com.gto.gtocore.common.data.GTORecipeTypes.BIOCHEMICAL_EXTRACTION_RECIPES;

interface BiochemicalExtraction {

    static void init() {
        BIOCHEMICAL_EXTRACTION_RECIPES.recipeBuilder(GTOCore.id("cow_spawn_egg"))
                .notConsumable(Items.COW_SPAWN_EGG.asItem())
                .outputFluids(GTMaterials.Milk.getFluid(1000))
                .EUt(2)
                .duration(200)
                .save();

        BIOCHEMICAL_EXTRACTION_RECIPES.recipeBuilder(GTOCore.id("cerebrum"))
                .notConsumable(TagsHandler.HUMAN_EGG)
                .outputItems(GTOItems.CEREBRUM)
                .duration(200)
                .EUt(30)
                .save();

        BIOCHEMICAL_EXTRACTION_RECIPES.recipeBuilder(GTOCore.id("mycgene"))
                .notConsumable("ad_astra:pygro_spawn_egg")
                .inputFluids(GTOMaterials.RapidlyReplicatingAnimalCells.getFluid(1000))
                .outputFluids(GTOMaterials.MycGene.getFluid(100))
                .EUt(480)
                .duration(200)
                .save();

        BIOCHEMICAL_EXTRACTION_RECIPES.recipeBuilder(GTOCore.id("oct4gene"))
                .notConsumable("ad_astra:pygro_brute_spawn_egg")
                .inputFluids(GTOMaterials.RapidlyReplicatingAnimalCells.getFluid(1000))
                .outputFluids(GTOMaterials.Oct4Gene.getFluid(100))
                .EUt(480)
                .duration(200)
                .save();

        BIOCHEMICAL_EXTRACTION_RECIPES.recipeBuilder(GTOCore.id("sox2gene"))
                .notConsumable("ad_astra:mogler_spawn_egg")
                .inputFluids(GTOMaterials.RapidlyReplicatingAnimalCells.getFluid(1000))
                .outputFluids(GTOMaterials.Sox2Gene.getFluid(100))
                .EUt(480)
                .duration(200)
                .save();

        BIOCHEMICAL_EXTRACTION_RECIPES.recipeBuilder(GTOCore.id("kfl4gene"))
                .notConsumable("ad_astra:sulfur_creeper_spawn_egg")
                .inputFluids(GTOMaterials.RapidlyReplicatingAnimalCells.getFluid(1000))
                .outputFluids(GTOMaterials.Kfl4Gene.getFluid(100))
                .EUt(480)
                .duration(200)
                .save();
    }
}
