package com.gto.gtocore.data.recipe.pack;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.common.data.GTORecipeTypes;
import com.gto.gtocore.utils.TagUtils;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

final class IsaMill {

    static void init(Consumer<FinishedRecipe> provider) {
        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_grossular_bgs"))
                .circuitMeta(1)
                .inputItems(TagUtils.createTGTag("ores/grossular"))
                .inputFluids(GTMaterials.DistilledWater.getFluid(100))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Grossular, 96)
                .EUt(1920)
                .duration(4800)
                .addData("grindball", 1)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_grossular_bal"))
                .circuitMeta(10)
                .inputItems(TagUtils.createTGTag("ores/grossular"))
                .inputFluids(GTMaterials.DistilledWater.getFluid(100))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Grossular, 72)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 2)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_almandine_bal"))
                .circuitMeta(10)
                .inputItems(TagUtils.createTGTag("ores/almandine"))
                .inputFluids(GTMaterials.DistilledWater.getFluid(100))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Almandine, 72)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 2)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_enriched_naquadah_ral"))
                .circuitMeta(10)
                .inputItems(TagPrefix.rawOre, GTMaterials.NaquadahEnriched, 16)
                .inputFluids(GTMaterials.DistilledWater.getFluid(50))
                .outputItems(GTOTagPrefix.milled, GTMaterials.NaquadahEnriched, 36)
                .EUt(1920)
                .duration(1200)
                .addData("grindball", 2)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_enriched_naquadah_rgs"))
                .circuitMeta(1)
                .inputItems(TagPrefix.rawOre, GTMaterials.NaquadahEnriched, 16)
                .inputFluids(GTMaterials.DistilledWater.getFluid(50))
                .outputItems(GTOTagPrefix.milled, GTMaterials.NaquadahEnriched, 48)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 1)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_almandine_bgs"))
                .circuitMeta(1)
                .inputItems(TagUtils.createTGTag("ores/almandine"))
                .inputFluids(GTMaterials.DistilledWater.getFluid(100))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Almandine, 96)
                .EUt(1920)
                .duration(4800)
                .addData("grindball", 1)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_chalcopyrite_bgs"))
                .circuitMeta(1)
                .inputItems(TagUtils.createTGTag("ores/chalcopyrite"))
                .inputFluids(GTMaterials.DistilledWater.getFluid(100))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Chalcopyrite, 96)
                .EUt(1920)
                .duration(4800)
                .addData("grindball", 1)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_chalcopyrite_bal"))
                .circuitMeta(10)
                .inputItems(TagUtils.createTGTag("ores/chalcopyrite"))
                .inputFluids(GTMaterials.DistilledWater.getFluid(100))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Chalcopyrite, 72)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 2)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_platinum_rgs"))
                .circuitMeta(1)
                .inputItems(TagPrefix.rawOre, GTMaterials.Platinum, 16)
                .inputFluids(GTMaterials.DistilledWater.getFluid(50))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Platinum, 48)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 1)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_platinum_ral"))
                .circuitMeta(10)
                .inputItems(TagPrefix.rawOre, GTMaterials.Platinum, 16)
                .inputFluids(GTMaterials.DistilledWater.getFluid(50))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Platinum, 36)
                .EUt(1920)
                .duration(1200)
                .addData("grindball", 2)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_redstone_ral"))
                .circuitMeta(10)
                .inputItems(TagPrefix.rawOre, GTMaterials.Redstone, 16)
                .inputFluids(GTMaterials.DistilledWater.getFluid(50))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Redstone, 36)
                .EUt(1920)
                .duration(1200)
                .addData("grindball", 2)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_redstone_rgs"))
                .circuitMeta(1)
                .inputItems(TagPrefix.rawOre, GTMaterials.Redstone, 16)
                .inputFluids(GTMaterials.DistilledWater.getFluid(50))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Redstone, 48)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 1)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_enriched_naquadah_bal"))
                .circuitMeta(10)
                .inputItems(TagUtils.createTGTag("ores/enriched_naquadah"))
                .inputFluids(GTMaterials.DistilledWater.getFluid(100))
                .outputItems(GTOTagPrefix.milled, GTMaterials.NaquadahEnriched, 72)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 2)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_enriched_naquadah_bgs"))
                .circuitMeta(1)
                .inputItems(TagUtils.createTGTag("ores/enriched_naquadah"))
                .inputFluids(GTMaterials.DistilledWater.getFluid(100))
                .outputItems(GTOTagPrefix.milled, GTMaterials.NaquadahEnriched, 96)
                .EUt(1920)
                .duration(4800)
                .addData("grindball", 1)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_chalcopyrite_rgs"))
                .circuitMeta(1)
                .inputItems(TagPrefix.rawOre, GTMaterials.Chalcopyrite, 16)
                .inputFluids(GTMaterials.DistilledWater.getFluid(50))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Chalcopyrite, 48)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 1)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_chalcopyrite_ral"))
                .circuitMeta(10)
                .inputItems(TagPrefix.rawOre, GTMaterials.Chalcopyrite, 16)
                .inputFluids(GTMaterials.DistilledWater.getFluid(50))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Chalcopyrite, 36)
                .EUt(1920)
                .duration(1200)
                .addData("grindball", 2)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_platinum_bgs"))
                .circuitMeta(1)
                .inputItems(TagUtils.createTGTag("ores/platinum"))
                .inputFluids(GTMaterials.DistilledWater.getFluid(100))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Platinum, 96)
                .EUt(1920)
                .duration(4800)
                .addData("grindball", 1)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_platinum_bal"))
                .circuitMeta(10)
                .inputItems(TagUtils.createTGTag("ores/platinum"))
                .inputFluids(GTMaterials.DistilledWater.getFluid(100))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Platinum, 72)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 2)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_monazite_rgs"))
                .circuitMeta(1)
                .inputItems(TagPrefix.rawOre, GTMaterials.Monazite, 16)
                .inputFluids(GTMaterials.DistilledWater.getFluid(50))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Monazite, 48)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 1)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_monazite_ral"))
                .circuitMeta(10)
                .inputItems(TagPrefix.rawOre, GTMaterials.Monazite, 16)
                .inputFluids(GTMaterials.DistilledWater.getFluid(50))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Monazite, 36)
                .EUt(1920)
                .duration(1200)
                .addData("grindball", 2)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_pentlandite_bgs"))
                .circuitMeta(1)
                .inputItems(TagUtils.createTGTag("ores/pentlandite"))
                .inputFluids(GTMaterials.DistilledWater.getFluid(100))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Pentlandite, 96)
                .EUt(1920)
                .duration(4800)
                .addData("grindball", 1)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_pentlandite_bal"))
                .circuitMeta(10)
                .inputItems(TagUtils.createTGTag("ores/pentlandite"))
                .inputFluids(GTMaterials.DistilledWater.getFluid(100))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Pentlandite, 72)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 2)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_nickel_bgs"))
                .circuitMeta(1)
                .inputItems(TagUtils.createTGTag("ores/nickel"))
                .inputFluids(GTMaterials.DistilledWater.getFluid(100))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Nickel, 96)
                .EUt(1920)
                .duration(4800)
                .addData("grindball", 1)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_nickel_bal"))
                .circuitMeta(10)
                .inputItems(TagUtils.createTGTag("ores/nickel"))
                .inputFluids(GTMaterials.DistilledWater.getFluid(100))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Nickel, 72)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 2)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_spessartine_rgs"))
                .circuitMeta(1)
                .inputItems(TagPrefix.rawOre, GTMaterials.Spessartine, 16)
                .inputFluids(GTMaterials.DistilledWater.getFluid(50))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Spessartine, 48)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 1)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_spessartine_ral"))
                .circuitMeta(10)
                .inputItems(TagPrefix.rawOre, GTMaterials.Spessartine, 16)
                .inputFluids(GTMaterials.DistilledWater.getFluid(50))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Spessartine, 36)
                .EUt(1920)
                .duration(1200)
                .addData("grindball", 2)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_redstone_bal"))
                .circuitMeta(10)
                .inputItems(TagUtils.createTGTag("ores/redstone"))
                .inputFluids(GTMaterials.DistilledWater.getFluid(100))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Redstone, 72)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 2)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_redstone_bgs"))
                .circuitMeta(1)
                .inputItems(TagUtils.createTGTag("ores/redstone"))
                .inputFluids(GTMaterials.DistilledWater.getFluid(100))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Redstone, 96)
                .EUt(1920)
                .duration(4800)
                .addData("grindball", 1)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_pyrope_bal"))
                .circuitMeta(10)
                .inputItems(TagUtils.createTGTag("ores/pyrope"))
                .inputFluids(GTMaterials.DistilledWater.getFluid(100))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Pyrope, 72)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 2)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_pyrope_bgs"))
                .circuitMeta(1)
                .inputItems(TagUtils.createTGTag("ores/pyrope"))
                .inputFluids(GTMaterials.DistilledWater.getFluid(100))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Pyrope, 96)
                .EUt(1920)
                .duration(4800)
                .addData("grindball", 1)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_sphalerite_rgs"))
                .circuitMeta(1)
                .inputItems(TagPrefix.rawOre, GTMaterials.Sphalerite, 16)
                .inputFluids(GTMaterials.DistilledWater.getFluid(50))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Sphalerite, 48)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 1)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_sphalerite_ral"))
                .circuitMeta(10)
                .inputItems(TagPrefix.rawOre, GTMaterials.Sphalerite, 16)
                .inputFluids(GTMaterials.DistilledWater.getFluid(50))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Sphalerite, 36)
                .EUt(1920)
                .duration(1200)
                .addData("grindball", 2)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_monazite_bgs"))
                .circuitMeta(1)
                .inputItems(TagUtils.createTGTag("ores/monazite"))
                .inputFluids(GTMaterials.DistilledWater.getFluid(100))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Monazite, 96)
                .EUt(1920)
                .duration(4800)
                .addData("grindball", 1)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_monazite_bal"))
                .circuitMeta(10)
                .inputItems(TagUtils.createTGTag("ores/monazite"))
                .inputFluids(GTMaterials.DistilledWater.getFluid(100))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Monazite, 72)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 2)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_pentlandite_rgs"))
                .circuitMeta(1)
                .inputItems(TagPrefix.rawOre, GTMaterials.Pentlandite, 16)
                .inputFluids(GTMaterials.DistilledWater.getFluid(50))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Pentlandite, 48)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 1)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_pentlandite_ral"))
                .circuitMeta(10)
                .inputItems(TagPrefix.rawOre, GTMaterials.Pentlandite, 16)
                .inputFluids(GTMaterials.DistilledWater.getFluid(50))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Pentlandite, 36)
                .EUt(1920)
                .duration(1200)
                .addData("grindball", 2)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_spessartine_bal"))
                .circuitMeta(10)
                .inputItems(TagUtils.createTGTag("ores/spessartine"))
                .inputFluids(GTMaterials.DistilledWater.getFluid(100))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Spessartine, 72)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 2)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_spessartine_bgs"))
                .circuitMeta(1)
                .inputItems(TagUtils.createTGTag("ores/spessartine"))
                .inputFluids(GTMaterials.DistilledWater.getFluid(100))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Spessartine, 96)
                .EUt(1920)
                .duration(4800)
                .addData("grindball", 1)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_grossular_rgs"))
                .circuitMeta(1)
                .inputItems(TagPrefix.rawOre, GTMaterials.Grossular, 16)
                .inputFluids(GTMaterials.DistilledWater.getFluid(50))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Grossular, 48)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 1)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_grossular_ral"))
                .circuitMeta(10)
                .inputItems(TagPrefix.rawOre, GTMaterials.Grossular, 16)
                .inputFluids(GTMaterials.DistilledWater.getFluid(50))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Grossular, 36)
                .EUt(1920)
                .duration(1200)
                .addData("grindball", 2)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_nickel_rgs"))
                .circuitMeta(1)
                .inputItems(TagPrefix.rawOre, GTMaterials.Nickel, 16)
                .inputFluids(GTMaterials.DistilledWater.getFluid(50))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Nickel, 48)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 1)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_almandine_ral"))
                .circuitMeta(10)
                .inputItems(TagPrefix.rawOre, GTMaterials.Almandine, 16)
                .inputFluids(GTMaterials.DistilledWater.getFluid(50))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Almandine, 36)
                .EUt(1920)
                .duration(1200)
                .addData("grindball", 2)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_nickel_ral"))
                .circuitMeta(10)
                .inputItems(TagPrefix.rawOre, GTMaterials.Nickel, 16)
                .inputFluids(GTMaterials.DistilledWater.getFluid(50))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Nickel, 36)
                .EUt(1920)
                .duration(1200)
                .addData("grindball", 2)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_almandine_rgs"))
                .circuitMeta(1)
                .inputItems(TagPrefix.rawOre, GTMaterials.Almandine, 16)
                .inputFluids(GTMaterials.DistilledWater.getFluid(50))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Almandine, 48)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 1)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_pyrope_rgs"))
                .circuitMeta(1)
                .inputItems(TagPrefix.rawOre, GTMaterials.Pyrope, 16)
                .inputFluids(GTMaterials.DistilledWater.getFluid(50))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Pyrope, 48)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 1)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_pyrope_ral"))
                .circuitMeta(10)
                .inputItems(TagPrefix.rawOre, GTMaterials.Pyrope, 16)
                .inputFluids(GTMaterials.DistilledWater.getFluid(50))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Pyrope, 36)
                .EUt(1920)
                .duration(1200)
                .addData("grindball", 2)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_sphalerite_bgs"))
                .circuitMeta(1)
                .inputItems(TagUtils.createTGTag("ores/sphalerite"))
                .inputFluids(GTMaterials.DistilledWater.getFluid(100))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Sphalerite, 96)
                .EUt(1920)
                .duration(4800)
                .addData("grindball", 1)
                .save(provider);

        GTORecipeTypes.ISA_MILL_RECIPES.recipeBuilder(GTOCore.id("milled_sphalerite_bal"))
                .circuitMeta(10)
                .inputItems(TagUtils.createTGTag("ores/sphalerite"))
                .inputFluids(GTMaterials.DistilledWater.getFluid(100))
                .outputItems(GTOTagPrefix.milled, GTMaterials.Sphalerite, 72)
                .EUt(1920)
                .duration(2400)
                .addData("grindball", 2)
                .save(provider);
    }
}
