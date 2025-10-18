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
                .inputFluids(GTOMaterials.Turpentine.getFluid(8000))
                .outputFluids(GTOMaterials.PyropeFront.getFluid(1000))
                .EUt(7680)
                .duration(4800)
                .save();

        FLOTATING_BENEFICIATION_RECIPES.recipeBuilder("redstone_front")
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumEthylxanthate, 32)
                .inputItems(GTOTagPrefix.MILLED, GTMaterials.Redstone, 64)
                .inputFluids(GTOMaterials.Turpentine.getFluid(13000))
                .outputFluids(GTOMaterials.RedstoneFront.getFluid(1000))
                .EUt(7680)
                .duration(4800)
                .save();

        FLOTATING_BENEFICIATION_RECIPES.recipeBuilder("chalcopyrite_front")
                .inputItems(TagPrefix.dust, GTOMaterials.PotassiumEthylxanthate, 32)
                .inputItems(GTOTagPrefix.MILLED, GTMaterials.Chalcopyrite, 64)
                .inputFluids(GTOMaterials.Turpentine.getFluid(12000))
                .outputFluids(GTOMaterials.ChalcopyriteFront.getFluid(1000))
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
                .inputFluids(GTOMaterials.Turpentine.getFluid(30000))
                .outputFluids(GTOMaterials.MonaziteFront.getFluid(1000))
                .EUt(30720)
                .duration(4800)
                .save();

        FLOTATING_BENEFICIATION_RECIPES.recipeBuilder("enriched_naquadah_front")
                .inputItems(TagPrefix.dust, GTOMaterials.PotassiumEthylxanthate, 64)
                .inputItems(GTOTagPrefix.MILLED, GTMaterials.NaquadahEnriched, 64)
                .inputFluids(GTOMaterials.Turpentine.getFluid(280000))
                .outputFluids(GTOMaterials.EnrichedNaquadahFront.getFluid(1000))
                .EUt(491520)
                .duration(2400)
                .save();

        FLOTATING_BENEFICIATION_RECIPES.recipeBuilder("grossular_front")
                .inputItems(TagPrefix.dust, GTOMaterials.PotassiumEthylxanthate, 32)
                .inputItems(GTOTagPrefix.MILLED, GTMaterials.Grossular, 64)
                .inputFluids(GTOMaterials.Turpentine.getFluid(28000))
                .outputFluids(GTOMaterials.GrossularFront.getFluid(1000))
                .EUt(30720)
                .duration(4800)
                .save();

        FLOTATING_BENEFICIATION_RECIPES.recipeBuilder("nickel_front")
                .inputItems(TagPrefix.dust, GTOMaterials.PotassiumEthylxanthate, 32)
                .inputItems(GTOTagPrefix.MILLED, GTMaterials.Nickel, 64)
                .inputFluids(GTOMaterials.Turpentine.getFluid(25000))
                .outputFluids(GTOMaterials.NickelFront.getFluid(1000))
                .EUt(7680)
                .duration(4800)
                .save();

        FLOTATING_BENEFICIATION_RECIPES.recipeBuilder("almandine_front")
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumEthylxanthate, 32)
                .inputItems(GTOTagPrefix.MILLED, GTMaterials.Almandine, 64)
                .inputFluids(GTOMaterials.Turpentine.getFluid(18000))
                .outputFluids(GTOMaterials.AlmandineFront.getFluid(1000))
                .EUt(7680)
                .duration(4800)
                .save();

        FLOTATING_BENEFICIATION_RECIPES.recipeBuilder("platinum_front")
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumEthylxanthate, 32)
                .inputItems(GTOTagPrefix.MILLED, GTMaterials.Platinum, 64)
                .inputFluids(GTOMaterials.Turpentine.getFluid(35000))
                .outputFluids(GTOMaterials.PlatinumFront.getFluid(1000))
                .EUt(30720)
                .duration(4800)
                .save();

        FLOTATING_BENEFICIATION_RECIPES.recipeBuilder("pentlandite_front")
                .inputItems(TagPrefix.dust, GTOMaterials.PotassiumEthylxanthate, 32)
                .inputItems(GTOTagPrefix.MILLED, GTMaterials.Pentlandite, 64)
                .inputFluids(GTOMaterials.Turpentine.getFluid(14000))
                .outputFluids(GTOMaterials.PentlanditeFront.getFluid(1000))
                .EUt(30720)
                .duration(4800)
                .save();

        FLOTATING_BENEFICIATION_RECIPES.recipeBuilder("spessartine_front")
                .inputItems(TagPrefix.dust, GTOMaterials.PotassiumEthylxanthate, 32)
                .inputItems(GTOTagPrefix.MILLED, GTMaterials.Spessartine, 64)
                .inputFluids(GTOMaterials.Turpentine.getFluid(35000))
                .outputFluids(GTOMaterials.SpessartineFront.getFluid(1000))
                .EUt(30720)
                .duration(4800)
                .save();

        FLOTATING_BENEFICIATION_RECIPES.recipeBuilder("sphalerite_front")
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumEthylxanthate, 32)
                .inputItems(GTOTagPrefix.MILLED, GTMaterials.Sphalerite, 64)
                .inputFluids(GTOMaterials.Turpentine.getFluid(14000))
                .outputFluids(GTOMaterials.SphaleriteFront.getFluid(1000))
                .EUt(30720)
                .duration(4800)
                .save();
    }
}
