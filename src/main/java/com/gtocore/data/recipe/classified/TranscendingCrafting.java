package com.gtocore.data.recipe.classified;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import static com.gtocore.common.data.GTORecipeTypes.TRANSCENDING_CRAFTING_RECIPES;

final class TranscendingCrafting {

    public static void init() {
        TRANSCENDING_CRAFTING_RECIPES.recipeBuilder("draconium_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Netherrack)
                .inputItems(TagPrefix.dust, GTMaterials.Endstone)
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(1000))
                .inputFluids(GTOMaterials.DragonBreath.getFluid(10))
                .outputItems(GTOItems.DRACONIUM_DIRT.asItem())
                .EUt(7864320)
                .duration(400)
                .save();

        TRANSCENDING_CRAFTING_RECIPES.recipeBuilder("magnetohydrodynamically_constrained_star_matternugget")
                .inputItems(TagPrefix.nugget, GTOMaterials.Eternity)
                .inputItems(GTOItems.SOLAR_LIGHT_SPLITTER.asItem())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(10000))
                .inputFluids(GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter.getFluid(16))
                .inputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(1000))
                .outputItems(TagPrefix.nugget, GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter)
                .EUt(8053063680L)
                .duration(200)
                .save();

        TRANSCENDING_CRAFTING_RECIPES.recipeBuilder("small_magnetohydrodynamically_constrained_star_matter_dust")
                .inputItems(TagPrefix.dustSmall, GTOMaterials.Eternity)
                .inputItems(GTOItems.SOLAR_LIGHT_SPLITTER.asItem())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(10000))
                .inputFluids(GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter.getFluid(36))
                .inputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(1000))
                .outputItems(TagPrefix.dustSmall, GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter)
                .EUt(8053063680L)
                .duration(200)
                .save();

        TRANSCENDING_CRAFTING_RECIPES.recipeBuilder("liquid_starlight")
                .inputItems(Blocks.BLUE_ICE.asItem(), 64)
                .inputItems(Blocks.BLUE_ICE.asItem(), 64)
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(10000))
                .inputFluids(GTOMaterials.Starlight.getFluid(10000))
                .outputFluids(GTOMaterials.LiquidStarlight.getFluid(10000))
                .EUt(7864320)
                .duration(9600)
                .save();

        TRANSCENDING_CRAFTING_RECIPES.recipeBuilder("magnetohydrodynamically_constrained_star_matteringot")
                .inputItems(TagPrefix.ingot, GTOMaterials.Eternity)
                .inputItems(GTOItems.SOLAR_LIGHT_SPLITTER.asItem())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(10000))
                .inputFluids(GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter.getFluid(144))
                .inputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(1000))
                .outputItems(TagPrefix.ingot, GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter)
                .EUt(8053063680L)
                .duration(200)
                .save();

        TRANSCENDING_CRAFTING_RECIPES.recipeBuilder("cosmic_fabric")
                .notConsumable(GTOTagPrefix.NANITES, GTOMaterials.CosmicNeutronium)
                .inputItems(TagPrefix.foil, GTMaterials.Rubber)
                .inputItems(GTOItems.AMORPHOUS_MATTER.asItem())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(1000))
                .inputFluids(GTOMaterials.EnrichedXenoxene.getFluid(1000))
                .inputFluids(GTOMaterials.CosmicMesh.getFluid(FluidStorageKeys.LIQUID, 100))
                .outputItems(GTOItems.COSMIC_FABRIC.asItem())
                .EUt(503316480)
                .duration(200)
                .save();

        TRANSCENDING_CRAFTING_RECIPES.recipeBuilder("magnetohydrodynamically_constrained_star_matterrod")
                .inputItems(TagPrefix.rod, GTOMaterials.Eternity)
                .inputItems(GTOItems.SOLAR_LIGHT_SPLITTER.asItem())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(10000))
                .inputFluids(GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter.getFluid(72))
                .inputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(1000))
                .outputItems(TagPrefix.rod, GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter)
                .EUt(8053063680L)
                .duration(200)
                .save();

        TRANSCENDING_CRAFTING_RECIPES.recipeBuilder("magnetohydrodynamically_constrained_star_matterplate")
                .inputItems(TagPrefix.plate, GTOMaterials.Eternity)
                .inputItems(GTOItems.SOLAR_LIGHT_SPLITTER.asItem())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(10000))
                .inputFluids(GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter.getFluid(144))
                .inputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(1000))
                .outputItems(TagPrefix.plate, GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter)
                .EUt(8053063680L)
                .duration(200)
                .save();

        TRANSCENDING_CRAFTING_RECIPES.recipeBuilder("echoite_plasma")
                .inputItems(TagPrefix.gemExquisite, GTMaterials.EchoShard, 16)
                .inputItems(GTOItems.RHENIUM_PLASMA_CONTAINMENT_CELL.asItem())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(16000))
                .inputFluids(GTOMaterials.Enderium.getFluid(1152))
                .inputFluids(GTOMaterials.Infuscolium.getFluid(1152))
                .outputItems(GTOItems.PLASMA_CONTAINMENT_CELL.asItem())
                .outputFluids(GTOMaterials.Echoite.getFluid(FluidStorageKeys.PLASMA, 2304))
                .EUt(31457280)
                .duration(400)
                .save();

        TRANSCENDING_CRAFTING_RECIPES.recipeBuilder("magnetohydrodynamically_constrained_star_matterfoil")
                .inputItems(TagPrefix.foil, GTOMaterials.Eternity)
                .inputItems(GTOItems.SOLAR_LIGHT_SPLITTER.asItem())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(10000))
                .inputFluids(GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter.getFluid(36))
                .inputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(1000))
                .outputItems(TagPrefix.foil, GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter)
                .EUt(8053063680L)
                .duration(200)
                .save();

        TRANSCENDING_CRAFTING_RECIPES.recipeBuilder("sculk_catalyst")
                .inputItems(Blocks.TERRACOTTA.asItem())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(1000))
                .inputFluids(GTMaterials.EchoShard.getFluid(100))
                .outputItems(Blocks.SCULK_CATALYST.asItem())
                .EUt(7864320)
                .duration(20)
                .save();

        TRANSCENDING_CRAFTING_RECIPES.recipeBuilder("magnetohydrodynamically_constrained_star_matterframe")
                .inputItems(TagPrefix.frameGt, GTOMaterials.Eternity)
                .inputItems(GTOItems.SOLAR_LIGHT_SPLITTER.asItem())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(10000))
                .inputFluids(GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter.getFluid(288))
                .inputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(1000))
                .outputItems(TagPrefix.frameGt, GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter)
                .EUt(8053063680L)
                .duration(200)
                .save();

        TRANSCENDING_CRAFTING_RECIPES.recipeBuilder("magmatter_ingot")
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asItem())
                .circuitMeta(1)
                .inputItems(TagPrefix.ingot, GTOMaterials.EnergeticNetherite)
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(100000))
                .inputFluids(GTOMaterials.Magmatter.getFluid(100))
                .outputItems(TagPrefix.ingot, GTOMaterials.Magmatter)
                .EUt(8053063680L)
                .duration(400)
                .save();

        TRANSCENDING_CRAFTING_RECIPES.recipeBuilder("magnetohydrodynamically_constrained_star_matterblock")
                .inputItems(TagPrefix.block, GTOMaterials.Eternity)
                .inputItems(GTOItems.SOLAR_LIGHT_SPLITTER.asItem())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(10000))
                .inputFluids(GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter.getFluid(1296))
                .inputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(1000))
                .outputItems(TagPrefix.block, GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter)
                .EUt(8053063680L)
                .duration(200)
                .save();

        TRANSCENDING_CRAFTING_RECIPES.recipeBuilder("magmatter_ingot_d")
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asItem())
                .inputItems(TagPrefix.dust, GTOMaterials.Magmatter)
                .inputItems(TagPrefix.ingot, GTOMaterials.EnergeticNetherite)
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(10000))
                .inputFluids(GTOMaterials.Magmatter.getFluid(10))
                .outputItems(TagPrefix.ingot, GTOMaterials.Magmatter)
                .EUt(8053063680L)
                .duration(200)
                .save();

        TRANSCENDING_CRAFTING_RECIPES.recipeBuilder("magnetohydrodynamically_constrained_star_matterdust")
                .inputItems(TagPrefix.dust, GTOMaterials.Eternity)
                .inputItems(GTOItems.SOLAR_LIGHT_SPLITTER.asItem())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(10000))
                .inputFluids(GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter.getFluid(144))
                .inputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter)
                .EUt(8053063680L)
                .duration(200)
                .save();

        TRANSCENDING_CRAFTING_RECIPES.recipeBuilder("attuned_tengam_ingot")
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asItem())
                .inputItems(TagPrefix.dust, GTOMaterials.AttunedTengam)
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(1000))
                .outputItems(TagPrefix.ingot, GTOMaterials.AttunedTengam)
                .EUt(31457280)
                .duration(400)
                .save();

        TRANSCENDING_CRAFTING_RECIPES.recipeBuilder("photocoated_hassium_boule")
                .inputItems(GTOItems.HIGH_PURITY_SINGLE_CRYSTAL_SILICON.asItem())
                .inputItems(TagPrefix.ingot, GTMaterials.Hassium, 2)
                .inputItems(GTOItems.HASSIUM_SEED_CRYSTAL.asItem())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(10000))
                .inputFluids(GTOMaterials.WoodsGlass.getFluid(2304))
                .inputFluids(GTOMaterials.Photopolymer.getFluid(2000))
                .outputItems(GTOItems.PHOTOCOATED_HASSIUM_BOULE.asItem())
                .EUt(7864320)
                .duration(1000)
                .save();

        TRANSCENDING_CRAFTING_RECIPES.recipeBuilder("bedrock_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.CompressedStone, 64)
                .inputItems(TagPrefix.dust, GTOMaterials.CompressedStone, 64)
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(100000))
                .inputFluids(GTMaterials.Thulium.getFluid(9216))
                .inputFluids(GTMaterials.Copernicium.getFluid(9216))
                .outputItems(TagPrefix.dust, GTOMaterials.Bedrockium)
                .EUt(31457280)
                .duration(1200)
                .save();

        TRANSCENDING_CRAFTING_RECIPES.recipeBuilder("dragon_stem_cells")
                .inputItems(GTItems.STEM_CELLS.asItem())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(1000))
                .inputFluids(GTMaterials.Mutagen.getFluid(100))
                .inputFluids(GTOMaterials.EnrichedDragonBreath.getFluid(100))
                .outputItems(GTOItems.DRAGON_STEM_CELLS.asItem())
                .EUt(7864320)
                .duration(400)
                .save();

        TRANSCENDING_CRAFTING_RECIPES.recipeBuilder("tiny_magnetohydrodynamically_constrained_star_matter_dust")
                .inputItems(TagPrefix.dustTiny, GTOMaterials.Eternity)
                .inputItems(GTOItems.SOLAR_LIGHT_SPLITTER.asItem())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(10000))
                .inputFluids(GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter.getFluid(16))
                .inputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(1000))
                .outputItems(TagPrefix.dustTiny, GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter)
                .EUt(8053063680L)
                .duration(200)
                .save();

        TRANSCENDING_CRAFTING_RECIPES.recipeBuilder("dragon_breath")
                .inputItems(Items.GLASS_BOTTLE.asItem())
                .inputItems(Blocks.DRAGON_EGG.asItem())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(1000))
                .inputFluids(GTOMaterials.DragonBlood.getFluid(10))
                .outputItems(Items.DRAGON_BREATH.asItem())
                .EUt(31457280)
                .duration(80)
                .save();

        TRANSCENDING_CRAFTING_RECIPES.recipeBuilder("double_magnetohydrodynamically_constrained_star_matter_plate")
                .inputItems(TagPrefix.plateDouble, GTOMaterials.Eternity)
                .inputItems(GTOItems.SOLAR_LIGHT_SPLITTER.asItem())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(10000))
                .inputFluids(GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter.getFluid(288))
                .inputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(1000))
                .outputItems(TagPrefix.plateDouble, GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter)
                .EUt(8053063680L)
                .duration(200)
                .save();

        TRANSCENDING_CRAFTING_RECIPES.recipeBuilder("dust_blizz")
                .inputItems(TagPrefix.dust, GTMaterials.Blaze)
                .inputItems(TagPrefix.dust, GTMaterials.AluminiumSulfite)
                .inputItems(Items.SNOWBALL.asItem(), 16)
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(1000))
                .inputFluids(GTMaterials.Ice.getFluid(1000))
                .outputItems(GTOItems.DUST_BLIZZ.asItem(), 2)
                .EUt(7864320)
                .duration(200)
                .save();
    }
}
