package com.gtocore.data.recipe.classified;

import com.gtocore.api.data.tag.GTOTagPrefix;

import com.gtolib.utils.TagUtils;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.ISA_MILL_RECIPES;

final class IsaMill {

    public static void init() {
        ISA_MILL_RECIPES.recipeBuilder("milled_grossular_bgs")
                .circuitMeta(1)
                .inputItems(TagUtils.createTGItemTag("ores/grossular"))
                .inputFluids(GTMaterials.DistilledWater, 100)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Grossular, 96)
                .EUt(1920)
                .duration(4800)
                .addData("grindball", 1)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_grossular_bal")
                .circuitMeta(10)
                .inputItems(TagUtils.createTGItemTag("ores/grossular"))
                .inputFluids(GTMaterials.DistilledWater, 100)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Grossular, 72)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 2)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_almandine_bal")
                .circuitMeta(10)
                .inputItems(TagUtils.createTGItemTag("ores/almandine"))
                .inputFluids(GTMaterials.DistilledWater, 100)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Almandine, 72)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 2)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_enriched_naquadah_ral")
                .circuitMeta(10)
                .inputItems(TagPrefix.rawOre, GTMaterials.NaquadahEnriched, 16)
                .inputFluids(GTMaterials.DistilledWater, 50)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.NaquadahEnriched, 36)
                .EUt(1920)
                .duration(1200)
                .addData("grindball", 2)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_enriched_naquadah_rgs")
                .circuitMeta(1)
                .inputItems(TagPrefix.rawOre, GTMaterials.NaquadahEnriched, 16)
                .inputFluids(GTMaterials.DistilledWater, 50)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.NaquadahEnriched, 48)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 1)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_almandine_bgs")
                .circuitMeta(1)
                .inputItems(TagUtils.createTGItemTag("ores/almandine"))
                .inputFluids(GTMaterials.DistilledWater, 100)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Almandine, 96)
                .EUt(1920)
                .duration(4800)
                .addData("grindball", 1)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_chalcopyrite_bgs")
                .circuitMeta(1)
                .inputItems(TagUtils.createTGItemTag("ores/chalcopyrite"))
                .inputFluids(GTMaterials.DistilledWater, 100)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Chalcopyrite, 96)
                .EUt(1920)
                .duration(4800)
                .addData("grindball", 1)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_chalcopyrite_bal")
                .circuitMeta(10)
                .inputItems(TagUtils.createTGItemTag("ores/chalcopyrite"))
                .inputFluids(GTMaterials.DistilledWater, 100)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Chalcopyrite, 72)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 2)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_platinum_rgs")
                .circuitMeta(1)
                .inputItems(TagPrefix.rawOre, GTMaterials.Platinum, 16)
                .inputFluids(GTMaterials.DistilledWater, 50)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Platinum, 48)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 1)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_platinum_ral")
                .circuitMeta(10)
                .inputItems(TagPrefix.rawOre, GTMaterials.Platinum, 16)
                .inputFluids(GTMaterials.DistilledWater, 50)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Platinum, 36)
                .EUt(1920)
                .duration(1200)
                .addData("grindball", 2)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_redstone_ral")
                .circuitMeta(10)
                .inputItems(TagPrefix.rawOre, GTMaterials.Redstone, 16)
                .inputFluids(GTMaterials.DistilledWater, 50)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Redstone, 36)
                .EUt(1920)
                .duration(1200)
                .addData("grindball", 2)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_redstone_rgs")
                .circuitMeta(1)
                .inputItems(TagPrefix.rawOre, GTMaterials.Redstone, 16)
                .inputFluids(GTMaterials.DistilledWater, 50)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Redstone, 48)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 1)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_enriched_naquadah_bal")
                .circuitMeta(10)
                .inputItems(TagUtils.createTGItemTag("ores/enriched_naquadah"))
                .inputFluids(GTMaterials.DistilledWater, 100)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.NaquadahEnriched, 72)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 2)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_enriched_naquadah_bgs")
                .circuitMeta(1)
                .inputItems(TagUtils.createTGItemTag("ores/enriched_naquadah"))
                .inputFluids(GTMaterials.DistilledWater, 100)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.NaquadahEnriched, 96)
                .EUt(1920)
                .duration(4800)
                .addData("grindball", 1)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_chalcopyrite_rgs")
                .circuitMeta(1)
                .inputItems(TagPrefix.rawOre, GTMaterials.Chalcopyrite, 16)
                .inputFluids(GTMaterials.DistilledWater, 50)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Chalcopyrite, 48)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 1)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_chalcopyrite_ral")
                .circuitMeta(10)
                .inputItems(TagPrefix.rawOre, GTMaterials.Chalcopyrite, 16)
                .inputFluids(GTMaterials.DistilledWater, 50)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Chalcopyrite, 36)
                .EUt(1920)
                .duration(1200)
                .addData("grindball", 2)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_platinum_bgs")
                .circuitMeta(1)
                .inputItems(TagUtils.createTGItemTag("ores/platinum"))
                .inputFluids(GTMaterials.DistilledWater, 100)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Platinum, 96)
                .EUt(1920)
                .duration(4800)
                .addData("grindball", 1)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_platinum_bal")
                .circuitMeta(10)
                .inputItems(TagUtils.createTGItemTag("ores/platinum"))
                .inputFluids(GTMaterials.DistilledWater, 100)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Platinum, 72)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 2)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_monazite_rgs")
                .circuitMeta(1)
                .inputItems(TagPrefix.rawOre, GTMaterials.Monazite, 16)
                .inputFluids(GTMaterials.DistilledWater, 50)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Monazite, 48)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 1)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_monazite_ral")
                .circuitMeta(10)
                .inputItems(TagPrefix.rawOre, GTMaterials.Monazite, 16)
                .inputFluids(GTMaterials.DistilledWater, 50)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Monazite, 36)
                .EUt(1920)
                .duration(1200)
                .addData("grindball", 2)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_pentlandite_bgs")
                .circuitMeta(1)
                .inputItems(TagUtils.createTGItemTag("ores/pentlandite"))
                .inputFluids(GTMaterials.DistilledWater, 100)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Pentlandite, 96)
                .EUt(1920)
                .duration(4800)
                .addData("grindball", 1)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_pentlandite_bal")
                .circuitMeta(10)
                .inputItems(TagUtils.createTGItemTag("ores/pentlandite"))
                .inputFluids(GTMaterials.DistilledWater, 100)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Pentlandite, 72)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 2)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_nickel_bgs")
                .circuitMeta(1)
                .inputItems(TagUtils.createTGItemTag("ores/nickel"))
                .inputFluids(GTMaterials.DistilledWater, 100)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Nickel, 96)
                .EUt(1920)
                .duration(4800)
                .addData("grindball", 1)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_nickel_bal")
                .circuitMeta(10)
                .inputItems(TagUtils.createTGItemTag("ores/nickel"))
                .inputFluids(GTMaterials.DistilledWater, 100)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Nickel, 72)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 2)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_spessartine_rgs")
                .circuitMeta(1)
                .inputItems(TagPrefix.rawOre, GTMaterials.Spessartine, 16)
                .inputFluids(GTMaterials.DistilledWater, 50)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Spessartine, 48)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 1)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_spessartine_ral")
                .circuitMeta(10)
                .inputItems(TagPrefix.rawOre, GTMaterials.Spessartine, 16)
                .inputFluids(GTMaterials.DistilledWater, 50)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Spessartine, 36)
                .EUt(1920)
                .duration(1200)
                .addData("grindball", 2)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_redstone_bal")
                .circuitMeta(10)
                .inputItems(TagUtils.createTGItemTag("ores/redstone"))
                .inputFluids(GTMaterials.DistilledWater, 100)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Redstone, 72)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 2)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_redstone_bgs")
                .circuitMeta(1)
                .inputItems(TagUtils.createTGItemTag("ores/redstone"))
                .inputFluids(GTMaterials.DistilledWater, 100)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Redstone, 96)
                .EUt(1920)
                .duration(4800)
                .addData("grindball", 1)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_pyrope_bal")
                .circuitMeta(10)
                .inputItems(TagUtils.createTGItemTag("ores/pyrope"))
                .inputFluids(GTMaterials.DistilledWater, 100)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Pyrope, 72)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 2)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_pyrope_bgs")
                .circuitMeta(1)
                .inputItems(TagUtils.createTGItemTag("ores/pyrope"))
                .inputFluids(GTMaterials.DistilledWater, 100)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Pyrope, 96)
                .EUt(1920)
                .duration(4800)
                .addData("grindball", 1)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_sphalerite_rgs")
                .circuitMeta(1)
                .inputItems(TagPrefix.rawOre, GTMaterials.Sphalerite, 16)
                .inputFluids(GTMaterials.DistilledWater, 50)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Sphalerite, 48)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 1)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_sphalerite_ral")
                .circuitMeta(10)
                .inputItems(TagPrefix.rawOre, GTMaterials.Sphalerite, 16)
                .inputFluids(GTMaterials.DistilledWater, 50)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Sphalerite, 36)
                .EUt(1920)
                .duration(1200)
                .addData("grindball", 2)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_monazite_bgs")
                .circuitMeta(1)
                .inputItems(TagUtils.createTGItemTag("ores/monazite"))
                .inputFluids(GTMaterials.DistilledWater, 100)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Monazite, 96)
                .EUt(1920)
                .duration(4800)
                .addData("grindball", 1)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_monazite_bal")
                .circuitMeta(10)
                .inputItems(TagUtils.createTGItemTag("ores/monazite"))
                .inputFluids(GTMaterials.DistilledWater, 100)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Monazite, 72)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 2)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_pentlandite_rgs")
                .circuitMeta(1)
                .inputItems(TagPrefix.rawOre, GTMaterials.Pentlandite, 16)
                .inputFluids(GTMaterials.DistilledWater, 50)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Pentlandite, 48)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 1)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_pentlandite_ral")
                .circuitMeta(10)
                .inputItems(TagPrefix.rawOre, GTMaterials.Pentlandite, 16)
                .inputFluids(GTMaterials.DistilledWater, 50)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Pentlandite, 36)
                .EUt(1920)
                .duration(1200)
                .addData("grindball", 2)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_spessartine_bal")
                .circuitMeta(10)
                .inputItems(TagUtils.createTGItemTag("ores/spessartine"))
                .inputFluids(GTMaterials.DistilledWater, 100)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Spessartine, 72)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 2)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_spessartine_bgs")
                .circuitMeta(1)
                .inputItems(TagUtils.createTGItemTag("ores/spessartine"))
                .inputFluids(GTMaterials.DistilledWater, 100)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Spessartine, 96)
                .EUt(1920)
                .duration(4800)
                .addData("grindball", 1)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_grossular_rgs")
                .circuitMeta(1)
                .inputItems(TagPrefix.rawOre, GTMaterials.Grossular, 16)
                .inputFluids(GTMaterials.DistilledWater, 50)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Grossular, 48)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 1)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_grossular_ral")
                .circuitMeta(10)
                .inputItems(TagPrefix.rawOre, GTMaterials.Grossular, 16)
                .inputFluids(GTMaterials.DistilledWater, 50)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Grossular, 36)
                .EUt(1920)
                .duration(1200)
                .addData("grindball", 2)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_nickel_rgs")
                .circuitMeta(1)
                .inputItems(TagPrefix.rawOre, GTMaterials.Nickel, 16)
                .inputFluids(GTMaterials.DistilledWater, 50)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Nickel, 48)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 1)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_almandine_ral")
                .circuitMeta(10)
                .inputItems(TagPrefix.rawOre, GTMaterials.Almandine, 16)
                .inputFluids(GTMaterials.DistilledWater, 50)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Almandine, 36)
                .EUt(1920)
                .duration(1200)
                .addData("grindball", 2)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_nickel_ral")
                .circuitMeta(10)
                .inputItems(TagPrefix.rawOre, GTMaterials.Nickel, 16)
                .inputFluids(GTMaterials.DistilledWater, 50)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Nickel, 36)
                .EUt(1920)
                .duration(1200)
                .addData("grindball", 2)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_almandine_rgs")
                .circuitMeta(1)
                .inputItems(TagPrefix.rawOre, GTMaterials.Almandine, 16)
                .inputFluids(GTMaterials.DistilledWater, 50)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Almandine, 48)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 1)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_pyrope_rgs")
                .circuitMeta(1)
                .inputItems(TagPrefix.rawOre, GTMaterials.Pyrope, 16)
                .inputFluids(GTMaterials.DistilledWater, 50)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Pyrope, 48)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 1)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_pyrope_ral")
                .circuitMeta(10)
                .inputItems(TagPrefix.rawOre, GTMaterials.Pyrope, 16)
                .inputFluids(GTMaterials.DistilledWater, 50)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Pyrope, 36)
                .EUt(1920)
                .duration(1200)
                .addData("grindball", 2)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_sphalerite_bgs")
                .circuitMeta(1)
                .inputItems(TagUtils.createTGItemTag("ores/sphalerite"))
                .inputFluids(GTMaterials.DistilledWater, 100)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Sphalerite, 96)
                .EUt(1920)
                .duration(4800)
                .addData("grindball", 1)
                .save();

        ISA_MILL_RECIPES.recipeBuilder("milled_sphalerite_bal")
                .circuitMeta(10)
                .inputItems(TagUtils.createTGItemTag("ores/sphalerite"))
                .inputFluids(GTMaterials.DistilledWater, 100)
                .outputItems(GTOTagPrefix.MILLED, GTMaterials.Sphalerite, 72)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 2)
                .save();
    }
}
