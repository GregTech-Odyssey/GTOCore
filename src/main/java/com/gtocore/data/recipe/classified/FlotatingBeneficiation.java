package com.gtocore.data.recipe.classified;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.FLOTATING_BENEFICIATION_RECIPES;

final class FlotatingBeneficiation {

    public static void init() {
        FLOTATING_BENEFICIATION_RECIPES.recipeBuilder("pyrope_front")
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumEthylxanthate, 32)
                .inputItems(GTOTagPrefix.MILLED, GTMaterials.Pyrope, 64)
                .inputFluids(GTOMaterials.Turpentine, 8000)
                .outputFluids(GTOMaterials.PyropeFront, 1000)
                .EUt(7680)
                .duration(4800)
                .save();

        FLOTATING_BENEFICIATION_RECIPES.recipeBuilder("redstone_front")
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumEthylxanthate, 32)
                .inputItems(GTOTagPrefix.MILLED, GTMaterials.Redstone, 64)
                .inputFluids(GTOMaterials.Turpentine, 13000)
                .outputFluids(GTOMaterials.RedstoneFront, 1000)
                .EUt(7680)
                .duration(4800)
                .save();

        FLOTATING_BENEFICIATION_RECIPES.recipeBuilder("chalcopyrite_front")
                .inputItems(TagPrefix.dust, GTOMaterials.PotassiumEthylxanthate, 32)
                .inputItems(GTOTagPrefix.MILLED, GTMaterials.Chalcopyrite, 64)
                .inputFluids(GTOMaterials.Turpentine, 12000)
                .outputFluids(GTOMaterials.ChalcopyriteFront, 1000)
                .EUt(7680)
                .duration(4800)
                .save();

        FLOTATING_BENEFICIATION_RECIPES.builder("metal_compound_particle_front")
                .inputItems(GTOTagPrefix.dust, GTOMaterials.SodiumEthylxanthate, 32)
                .inputItems(GTOTagPrefix.dust, GTOMaterials.MetalCompoundParticles, 64)
                .inputFluids(GTOMaterials.Turpentine, 12000)
                .outputFluids(GTOMaterials.MetalCompoundParticleFront, 1000)
                .EUt(7680)
                .duration(4800)
                .save();

        FLOTATING_BENEFICIATION_RECIPES.recipeBuilder("monazite_front")
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumEthylxanthate, 32)
                .inputItems(GTOTagPrefix.MILLED, GTMaterials.Monazite, 64)
                .inputFluids(GTOMaterials.Turpentine, 30000)
                .outputFluids(GTOMaterials.MonaziteFront, 1000)
                .EUt(30720)
                .duration(4800)
                .save();

        FLOTATING_BENEFICIATION_RECIPES.recipeBuilder("enriched_naquadah_front")
                .inputItems(TagPrefix.dust, GTOMaterials.PotassiumEthylxanthate, 64)
                .inputItems(GTOTagPrefix.MILLED, GTMaterials.NaquadahEnriched, 64)
                .inputFluids(GTOMaterials.Turpentine, 280000)
                .outputFluids(GTOMaterials.EnrichedNaquadahFront, 1000)
                .EUt(491520)
                .duration(2400)
                .save();

        FLOTATING_BENEFICIATION_RECIPES.recipeBuilder("grossular_front")
                .inputItems(TagPrefix.dust, GTOMaterials.PotassiumEthylxanthate, 32)
                .inputItems(GTOTagPrefix.MILLED, GTMaterials.Grossular, 64)
                .inputFluids(GTOMaterials.Turpentine, 28000)
                .outputFluids(GTOMaterials.GrossularFront, 1000)
                .EUt(30720)
                .duration(4800)
                .save();

        FLOTATING_BENEFICIATION_RECIPES.recipeBuilder("nickel_front")
                .inputItems(TagPrefix.dust, GTOMaterials.PotassiumEthylxanthate, 32)
                .inputItems(GTOTagPrefix.MILLED, GTMaterials.Nickel, 64)
                .inputFluids(GTOMaterials.Turpentine, 25000)
                .outputFluids(GTOMaterials.NickelFront, 1000)
                .EUt(7680)
                .duration(4800)
                .save();

        FLOTATING_BENEFICIATION_RECIPES.recipeBuilder("almandine_front")
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumEthylxanthate, 32)
                .inputItems(GTOTagPrefix.MILLED, GTMaterials.Almandine, 64)
                .inputFluids(GTOMaterials.Turpentine, 18000)
                .outputFluids(GTOMaterials.AlmandineFront, 1000)
                .EUt(7680)
                .duration(4800)
                .save();

        FLOTATING_BENEFICIATION_RECIPES.recipeBuilder("platinum_front")
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumEthylxanthate, 32)
                .inputItems(GTOTagPrefix.MILLED, GTMaterials.Platinum, 64)
                .inputFluids(GTOMaterials.Turpentine, 35000)
                .outputFluids(GTOMaterials.PlatinumFront, 1000)
                .EUt(30720)
                .duration(4800)
                .save();

        FLOTATING_BENEFICIATION_RECIPES.recipeBuilder("pentlandite_front")
                .inputItems(TagPrefix.dust, GTOMaterials.PotassiumEthylxanthate, 32)
                .inputItems(GTOTagPrefix.MILLED, GTMaterials.Pentlandite, 64)
                .inputFluids(GTOMaterials.Turpentine, 14000)
                .outputFluids(GTOMaterials.PentlanditeFront, 1000)
                .EUt(30720)
                .duration(4800)
                .save();

        FLOTATING_BENEFICIATION_RECIPES.recipeBuilder("spessartine_front")
                .inputItems(TagPrefix.dust, GTOMaterials.PotassiumEthylxanthate, 32)
                .inputItems(GTOTagPrefix.MILLED, GTMaterials.Spessartine, 64)
                .inputFluids(GTOMaterials.Turpentine, 35000)
                .outputFluids(GTOMaterials.SpessartineFront, 1000)
                .EUt(30720)
                .duration(4800)
                .save();

        FLOTATING_BENEFICIATION_RECIPES.recipeBuilder("sphalerite_front")
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumEthylxanthate, 32)
                .inputItems(GTOTagPrefix.MILLED, GTMaterials.Sphalerite, 64)
                .inputFluids(GTOMaterials.Turpentine, 14000)
                .outputFluids(GTOMaterials.SphaleriteFront, 1000)
                .EUt(30720)
                .duration(4800)
                .save();
    }
}
