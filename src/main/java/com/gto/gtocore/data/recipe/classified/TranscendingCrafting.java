package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

interface TranscendingCrafting {

    static void init(Consumer<FinishedRecipe> provider) {
        GTORecipeTypes.TRANSCENDING_CRAFTING_RECIPES.recipeBuilder(GTOCore.id("draconium_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.Netherrack)
                .inputItems(TagPrefix.dust, GTMaterials.Endstone)
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(1000))
                .inputFluids(GTOMaterials.DragonBreath.getFluid(10))
                .outputItems(GTOItems.DRACONIUM_DIRT.asStack())
                .EUt(7864320)
                .duration(400)
                .save(provider);

        GTORecipeTypes.TRANSCENDING_CRAFTING_RECIPES.recipeBuilder(GTOCore.id("magnetohydrodynamically_constrained_star_matternugget"))
                .inputItems(TagPrefix.nugget, GTOMaterials.Eternity)
                .inputItems(GTOItems.SOLAR_LIGHT_SPLITTER.asStack())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(10000))
                .inputFluids(GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter.getFluid(16))
                .inputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(1000))
                .outputItems(TagPrefix.nugget, GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter)
                .EUt(8053063680L)
                .duration(200)
                .save(provider);

        GTORecipeTypes.TRANSCENDING_CRAFTING_RECIPES.recipeBuilder(GTOCore.id("small_magnetohydrodynamically_constrained_star_matter_dust"))
                .inputItems(TagPrefix.dustSmall, GTOMaterials.Eternity)
                .inputItems(GTOItems.SOLAR_LIGHT_SPLITTER.asStack())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(10000))
                .inputFluids(GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter.getFluid(36))
                .inputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(1000))
                .outputItems(TagPrefix.dustSmall, GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter)
                .EUt(8053063680L)
                .duration(200)
                .save(provider);

        GTORecipeTypes.TRANSCENDING_CRAFTING_RECIPES.recipeBuilder(GTOCore.id("liquid_starlight"))
                .inputItems(new ItemStack(Blocks.BLUE_ICE.asItem(), 64))
                .inputItems(new ItemStack(Blocks.BLUE_ICE.asItem(), 64))
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(10000))
                .inputFluids(GTOMaterials.Starlight.getFluid(10000))
                .outputFluids(GTOMaterials.LiquidStarlight.getFluid(10000))
                .EUt(7864320)
                .duration(9600)
                .save(provider);

        GTORecipeTypes.TRANSCENDING_CRAFTING_RECIPES.recipeBuilder(GTOCore.id("magnetohydrodynamically_constrained_star_matteringot"))
                .inputItems(TagPrefix.ingot, GTOMaterials.Eternity)
                .inputItems(GTOItems.SOLAR_LIGHT_SPLITTER.asStack())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(10000))
                .inputFluids(GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter.getFluid(144))
                .inputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(1000))
                .outputItems(TagPrefix.ingot, GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter)
                .EUt(8053063680L)
                .duration(200)
                .save(provider);

        GTORecipeTypes.TRANSCENDING_CRAFTING_RECIPES.recipeBuilder(GTOCore.id("cosmic_fabric"))
                .notConsumable(GTOTagPrefix.nanites, GTOMaterials.CosmicNeutronium)
                .inputItems(TagPrefix.foil, GTMaterials.Rubber)
                .inputItems(GTOItems.AMORPHOUS_MATTER.asStack())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(1000))
                .inputFluids(GTOMaterials.EnrichedXenoxene.getFluid(1000))
                .inputFluids(GTOMaterials.CosmicMesh.getFluid(FluidStorageKeys.LIQUID, 100))
                .outputItems(GTOItems.COSMIC_FABRIC.asStack())
                .EUt(503316480)
                .duration(200)
                .save(provider);

        GTORecipeTypes.TRANSCENDING_CRAFTING_RECIPES.recipeBuilder(GTOCore.id("magnetohydrodynamically_constrained_star_matterrod"))
                .inputItems(TagPrefix.rod, GTOMaterials.Eternity)
                .inputItems(GTOItems.SOLAR_LIGHT_SPLITTER.asStack())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(10000))
                .inputFluids(GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter.getFluid(72))
                .inputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(1000))
                .outputItems(TagPrefix.rod, GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter)
                .EUt(8053063680L)
                .duration(200)
                .save(provider);

        GTORecipeTypes.TRANSCENDING_CRAFTING_RECIPES.recipeBuilder(GTOCore.id("magnetohydrodynamically_constrained_star_matterplate"))
                .inputItems(TagPrefix.plate, GTOMaterials.Eternity)
                .inputItems(GTOItems.SOLAR_LIGHT_SPLITTER.asStack())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(10000))
                .inputFluids(GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter.getFluid(144))
                .inputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(1000))
                .outputItems(TagPrefix.plate, GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter)
                .EUt(8053063680L)
                .duration(200)
                .save(provider);

        GTORecipeTypes.TRANSCENDING_CRAFTING_RECIPES.recipeBuilder(GTOCore.id("echoite_plasma"))
                .inputItems(TagPrefix.gemExquisite, GTMaterials.EchoShard, 16)
                .inputItems(GTOItems.RHENIUM_PLASMA_CONTAINMENT_CELL.asStack())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(16000))
                .inputFluids(GTOMaterials.Enderium.getFluid(1152))
                .inputFluids(GTOMaterials.Infuscolium.getFluid(1152))
                .outputItems(GTOItems.PLASMA_CONTAINMENT_CELL.asStack())
                .outputFluids(GTOMaterials.Echoite.getFluid(FluidStorageKeys.PLASMA, 2304))
                .EUt(31457280)
                .duration(400)
                .save(provider);

        GTORecipeTypes.TRANSCENDING_CRAFTING_RECIPES.recipeBuilder(GTOCore.id("magnetohydrodynamically_constrained_star_matterfoil"))
                .inputItems(TagPrefix.foil, GTOMaterials.Eternity)
                .inputItems(GTOItems.SOLAR_LIGHT_SPLITTER.asStack())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(10000))
                .inputFluids(GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter.getFluid(36))
                .inputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(1000))
                .outputItems(TagPrefix.foil, GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter)
                .EUt(8053063680L)
                .duration(200)
                .save(provider);

        GTORecipeTypes.TRANSCENDING_CRAFTING_RECIPES.recipeBuilder(GTOCore.id("sculk_catalyst"))
                .inputItems(new ItemStack(Blocks.TERRACOTTA.asItem()))
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(1000))
                .inputFluids(GTMaterials.EchoShard.getFluid(100))
                .outputItems(new ItemStack(Blocks.SCULK_CATALYST.asItem()))
                .EUt(7864320)
                .duration(20)
                .save(provider);

        GTORecipeTypes.TRANSCENDING_CRAFTING_RECIPES.recipeBuilder(GTOCore.id("magnetohydrodynamically_constrained_star_matterframe"))
                .inputItems(TagPrefix.frameGt, GTOMaterials.Eternity)
                .inputItems(GTOItems.SOLAR_LIGHT_SPLITTER.asStack())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(10000))
                .inputFluids(GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter.getFluid(288))
                .inputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(1000))
                .outputItems(TagPrefix.frameGt, GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter)
                .EUt(8053063680L)
                .duration(200)
                .save(provider);

        GTORecipeTypes.TRANSCENDING_CRAFTING_RECIPES.recipeBuilder(GTOCore.id("magmatter_ingot"))
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asStack())
                .circuitMeta(1)
                .inputItems(TagPrefix.ingot, GTMaterials.Netherite)
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(100000))
                .inputFluids(GTOMaterials.Magmatter.getFluid(100))
                .outputItems(TagPrefix.ingot, GTOMaterials.Magmatter)
                .EUt(8053063680L)
                .duration(400)
                .save(provider);

        GTORecipeTypes.TRANSCENDING_CRAFTING_RECIPES.recipeBuilder(GTOCore.id("magnetohydrodynamically_constrained_star_matterblock"))
                .inputItems(TagPrefix.block, GTOMaterials.Eternity)
                .inputItems(GTOItems.SOLAR_LIGHT_SPLITTER.asStack())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(10000))
                .inputFluids(GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter.getFluid(1296))
                .inputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(1000))
                .outputItems(TagPrefix.block, GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter)
                .EUt(8053063680L)
                .duration(200)
                .save(provider);

        GTORecipeTypes.TRANSCENDING_CRAFTING_RECIPES.recipeBuilder(GTOCore.id("magmatter_ingot_d"))
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asStack())
                .inputItems(TagPrefix.dust, GTOMaterials.Magmatter)
                .inputItems(TagPrefix.ingot, GTMaterials.Netherite)
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(10000))
                .inputFluids(GTOMaterials.Magmatter.getFluid(10))
                .outputItems(TagPrefix.ingot, GTOMaterials.Magmatter)
                .EUt(8053063680L)
                .duration(200)
                .save(provider);

        GTORecipeTypes.TRANSCENDING_CRAFTING_RECIPES.recipeBuilder(GTOCore.id("magnetohydrodynamically_constrained_star_matterdust"))
                .inputItems(TagPrefix.dust, GTOMaterials.Eternity)
                .inputItems(GTOItems.SOLAR_LIGHT_SPLITTER.asStack())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(10000))
                .inputFluids(GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter.getFluid(144))
                .inputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter)
                .EUt(8053063680L)
                .duration(200)
                .save(provider);

        GTORecipeTypes.TRANSCENDING_CRAFTING_RECIPES.recipeBuilder(GTOCore.id("attuned_tengam_ingot"))
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asStack())
                .inputItems(TagPrefix.dust, GTOMaterials.AttunedTengam)
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(1000))
                .outputItems(TagPrefix.ingot, GTOMaterials.AttunedTengam)
                .EUt(31457280)
                .duration(400)
                .save(provider);

        GTORecipeTypes.TRANSCENDING_CRAFTING_RECIPES.recipeBuilder(GTOCore.id("photocoated_hassium_boule"))
                .inputItems(GTOItems.HIGH_PURITY_SINGLE_CRYSTAL_SILICON.asStack())
                .inputItems(TagPrefix.ingot, GTMaterials.Hassium, 2)
                .inputItems(GTOItems.HASSIUM_SEED_CRYSTAL.asStack())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(10000))
                .inputFluids(GTOMaterials.WoodsGlass.getFluid(2304))
                .inputFluids(GTOMaterials.Photopolymer.getFluid(2000))
                .outputItems(GTOItems.PHOTOCOATED_HASSIUM_BOULE.asStack())
                .EUt(7864320)
                .duration(1000)
                .save(provider);

        GTORecipeTypes.TRANSCENDING_CRAFTING_RECIPES.recipeBuilder(GTOCore.id("bedrock_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.CompressedStone, 64)
                .inputItems(TagPrefix.dust, GTOMaterials.CompressedStone, 64)
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(100000))
                .inputFluids(GTMaterials.Thulium.getFluid(9216))
                .inputFluids(GTMaterials.Copernicium.getFluid(9216))
                .outputItems(TagPrefix.dust, GTOMaterials.Bedrockium)
                .EUt(31457280)
                .duration(1200)
                .save(provider);

        GTORecipeTypes.TRANSCENDING_CRAFTING_RECIPES.recipeBuilder(GTOCore.id("dragon_stem_cells"))
                .inputItems(GTItems.STEM_CELLS.asStack())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(1000))
                .inputFluids(GTMaterials.Mutagen.getFluid(100))
                .inputFluids(GTOMaterials.EnrichedDragonBreath.getFluid(100))
                .outputItems(GTOItems.DRAGON_STEM_CELLS.asStack())
                .EUt(7864320)
                .duration(400)
                .save(provider);

        GTORecipeTypes.TRANSCENDING_CRAFTING_RECIPES.recipeBuilder(GTOCore.id("tiny_magnetohydrodynamically_constrained_star_matter_dust"))
                .inputItems(TagPrefix.dustTiny, GTOMaterials.Eternity)
                .inputItems(GTOItems.SOLAR_LIGHT_SPLITTER.asStack())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(10000))
                .inputFluids(GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter.getFluid(16))
                .inputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(1000))
                .outputItems(TagPrefix.dustTiny, GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter)
                .EUt(8053063680L)
                .duration(200)
                .save(provider);

        GTORecipeTypes.TRANSCENDING_CRAFTING_RECIPES.recipeBuilder(GTOCore.id("dragon_breath"))
                .inputItems(new ItemStack(Items.GLASS_BOTTLE.asItem()))
                .inputItems(new ItemStack(Blocks.DRAGON_EGG.asItem()))
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(1000))
                .inputFluids(GTOMaterials.DragonBlood.getFluid(10))
                .outputItems(new ItemStack(Items.DRAGON_BREATH.asItem()))
                .EUt(31457280)
                .duration(80)
                .save(provider);

        GTORecipeTypes.TRANSCENDING_CRAFTING_RECIPES.recipeBuilder(GTOCore.id("double_magnetohydrodynamically_constrained_star_matter_plate"))
                .inputItems(TagPrefix.plateDouble, GTOMaterials.Eternity)
                .inputItems(GTOItems.SOLAR_LIGHT_SPLITTER.asStack())
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(10000))
                .inputFluids(GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter.getFluid(288))
                .inputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(1000))
                .outputItems(TagPrefix.plateDouble, GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter)
                .EUt(8053063680L)
                .duration(200)
                .save(provider);

        GTORecipeTypes.TRANSCENDING_CRAFTING_RECIPES.recipeBuilder(GTOCore.id("dust_blizz"))
                .inputItems(TagPrefix.dust, GTMaterials.Blaze)
                .inputItems(TagPrefix.dust, GTMaterials.AluminiumSulfite)
                .inputItems(new ItemStack(Items.SNOWBALL.asItem(), 16))
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(1000))
                .inputFluids(GTMaterials.Ice.getFluid(1000))
                .outputItems(GTOItems.DUST_BLIZZ.asStack(2))
                .EUt(7864320)
                .duration(200)
                .save(provider);

        GTORecipeBuilder.of(GTORecipeTypes.MAGIC_MANUFACTURER_RECIPES, GTOCore.id("mana_0"))
                .MANAt(-100)
                .notConsumable(GTItems.FIELD_GENERATOR_UEV.asStack())
                .circuitMeta(1)
                .EUt(7864320)
                .duration(20)
                .save(provider);

        GTORecipeBuilder.of(GTORecipeTypes.MAGIC_MANUFACTURER_RECIPES, GTOCore.id("mana_3"))
                .MANAt(-100000)
                .notConsumable(GTItems.FIELD_GENERATOR_OpV.asStack())
                .circuitMeta(1)
                .EUt(503316480)
                .duration(20)
                .save(provider);

        GTORecipeBuilder.of(GTORecipeTypes.MAGIC_MANUFACTURER_RECIPES, GTOCore.id("mana_4"))
                .MANAt(-400)
                .notConsumable(GTItems.FIELD_GENERATOR_UEV.asStack(4))
                .circuitMeta(2)
                .EUt(7864320)
                .duration(40)
                .save(provider);

        GTORecipeBuilder.of(GTORecipeTypes.MAGIC_MANUFACTURER_RECIPES, GTOCore.id("mana_1"))
                .MANAt(-1000)
                .notConsumable(GTItems.FIELD_GENERATOR_UIV.asStack())
                .circuitMeta(1)
                .EUt(31457280)
                .duration(20)
                .save(provider);

        GTORecipeBuilder.of(GTORecipeTypes.MAGIC_MANUFACTURER_RECIPES, GTOCore.id("mana_2"))
                .MANAt(-10000)
                .notConsumable(GTItems.FIELD_GENERATOR_UXV.asStack())
                .circuitMeta(1)
                .EUt(125829120)
                .duration(20)
                .save(provider);

        GTORecipeBuilder.of(GTORecipeTypes.MAGIC_MANUFACTURER_RECIPES, GTOCore.id("mana_7"))
                .MANAt(-400000)
                .notConsumable(GTItems.FIELD_GENERATOR_OpV.asStack(4))
                .circuitMeta(2)
                .EUt(503316480)
                .duration(40)
                .save(provider);

        GTORecipeBuilder.of(GTORecipeTypes.MAGIC_MANUFACTURER_RECIPES, GTOCore.id("mana_8"))
                .MANAt(-1600)
                .notConsumable(GTItems.FIELD_GENERATOR_UEV.asStack(16))
                .circuitMeta(3)
                .EUt(7864320)
                .duration(80)
                .save(provider);

        GTORecipeBuilder.of(GTORecipeTypes.MAGIC_MANUFACTURER_RECIPES, GTOCore.id("mana_5"))
                .MANAt(-4000)
                .notConsumable(GTItems.FIELD_GENERATOR_UIV.asStack(4))
                .circuitMeta(2)
                .EUt(31457280)
                .duration(40)
                .save(provider);

        GTORecipeBuilder.of(GTORecipeTypes.MAGIC_MANUFACTURER_RECIPES, GTOCore.id("mana_6"))
                .MANAt(-40000)
                .notConsumable(GTItems.FIELD_GENERATOR_UXV.asStack(4))
                .circuitMeta(2)
                .EUt(125829120)
                .duration(40)
                .save(provider);

        GTORecipeBuilder.of(GTORecipeTypes.MAGIC_MANUFACTURER_RECIPES, GTOCore.id("mana_9"))
                .MANAt(-16000)
                .notConsumable(GTItems.FIELD_GENERATOR_UIV.asStack(16))
                .circuitMeta(3)
                .EUt(31457280)
                .duration(80)
                .save(provider);

        GTORecipeBuilder.of(GTORecipeTypes.MAGIC_MANUFACTURER_RECIPES, GTOCore.id("mana_15"))
                .MANAt(-6400000)
                .notConsumable(GTItems.FIELD_GENERATOR_OpV.asStack(64))
                .circuitMeta(4)
                .EUt(503316480)
                .duration(160)
                .save(provider);

        GTORecipeBuilder.of(GTORecipeTypes.MAGIC_MANUFACTURER_RECIPES, GTOCore.id("mana_14"))
                .MANAt(-640000)
                .notConsumable(GTItems.FIELD_GENERATOR_UXV.asStack(64))
                .circuitMeta(4)
                .EUt(125829120)
                .duration(160)
                .save(provider);

        GTORecipeBuilder.of(GTORecipeTypes.MAGIC_MANUFACTURER_RECIPES, GTOCore.id("mana_13"))
                .MANAt(-64000)
                .notConsumable(GTItems.FIELD_GENERATOR_UIV.asStack(64))
                .circuitMeta(4)
                .EUt(31457280)
                .duration(160)
                .save(provider);

        GTORecipeBuilder.of(GTORecipeTypes.MAGIC_MANUFACTURER_RECIPES, GTOCore.id("mana_12"))
                .MANAt(-6400)
                .notConsumable(GTItems.FIELD_GENERATOR_UEV.asStack(64))
                .circuitMeta(4)
                .EUt(7864320)
                .duration(160)
                .save(provider);

        GTORecipeBuilder.of(GTORecipeTypes.MAGIC_MANUFACTURER_RECIPES, GTOCore.id("mana_11"))
                .MANAt(-1600000)
                .notConsumable(GTItems.FIELD_GENERATOR_OpV.asStack(16))
                .circuitMeta(3)
                .EUt(503316480)
                .duration(80)
                .save(provider);

        GTORecipeBuilder.of(GTORecipeTypes.MAGIC_MANUFACTURER_RECIPES, GTOCore.id("mana_10"))
                .MANAt(-160000)
                .notConsumable(GTItems.FIELD_GENERATOR_UXV.asStack(16))
                .circuitMeta(3)
                .EUt(125829120)
                .duration(80)
                .save(provider);
    }
}
