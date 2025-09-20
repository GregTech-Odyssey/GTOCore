package com.gtocore.data.recipe.classified;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.recipe.condition.GravityCondition;
import com.gtocore.common.recipe.condition.VacuumCondition;

import com.gtolib.api.machine.GTOCleanroomType;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import com.enderio.base.common.init.EIOItems;
import committee.nova.mods.avaritia.init.registry.ModItems;

import static com.gtocore.common.data.GTORecipeTypes.AUTOCLAVE_RECIPES;

final class Autoclave {

    public static void init() {
        AUTOCLAVE_RECIPES.recipeBuilder("sterilized_petri_dish")
                .inputItems(GTItems.PETRI_DISH)
                .inputFluids(GTOMaterials.AbsoluteEthanol.getFluid(100))
                .outputItems(GTOItems.STERILIZED_PETRI_DISH)
                .cleanroom(CleanroomType.STERILE_CLEANROOM)
                .duration(25).EUt(7680).save();

        AUTOCLAVE_RECIPES.recipeBuilder("spacetime_catalyst")
                .inputItems(ModItems.infinity_catalyst.get())
                .inputFluids(GTOMaterials.SpaceTime.getFluid(1000))
                .outputItems(GTOItems.SPACETIME_CATALYST.asItem())
                .EUt(8053063680L)
                .duration(1200)
                .save();

        AUTOCLAVE_RECIPES.recipeBuilder("dry_graphene_gel_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.GrapheneGelSuspension)
                .inputFluids(GTMaterials.Acetone.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.DryGrapheneGel)
                .EUt(480)
                .duration(260)
                .save();

        AUTOCLAVE_RECIPES.recipeBuilder("soul_soil")
                .inputItems(EIOItems.FILLED_SOUL_VIAL.asItem())
                .inputItems(new ItemStack(Blocks.ROOTED_DIRT.asItem()))
                .inputFluids(GTMaterials.LiquidNetherAir.getFluid(100))
                .outputItems(EIOItems.EMPTY_SOUL_VIAL.asItem())
                .outputItems(new ItemStack(Blocks.SOUL_SOIL.asItem()))
                .EUt(480)
                .duration(240)
                .save();

        AUTOCLAVE_RECIPES.recipeBuilder("ender_crystal")
                .inputItems(EIOItems.VIBRANT_CRYSTAL.asItem())
                .inputFluids(GTOMaterials.Enderium.getFluid(8))
                .outputItems(EIOItems.ENDER_CRYSTAL.asItem())
                .EUt(30)
                .duration(200)
                .addCondition(new VacuumCondition(4))
                .save();

        AUTOCLAVE_RECIPES.recipeBuilder("hassium_seed_crystal")
                .inputItems(TagPrefix.dustTiny, GTMaterials.Hassium)
                .inputFluids(GTMaterials.Nitrogen.getFluid(10000))
                .outputItems(GTOItems.HASSIUM_SEED_CRYSTAL.asItem())
                .EUt(31457280)
                .duration(200)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        AUTOCLAVE_RECIPES.recipeBuilder("unstable_star")
                .inputItems(GTOTagPrefix.NANITES, GTOMaterials.Orichalcum)
                .inputItems(GTItems.GRAVI_STAR.asItem())
                .inputFluids(GTOMaterials.Adamantine.getFluid(288))
                .outputItems(GTOTagPrefix.CONTAMINABLE_NANITES, GTOMaterials.Orichalcum)
                .outputItems(GTOItems.UNSTABLE_STAR.asItem())
                .EUt(491520)
                .duration(480)
                .addCondition(new GravityCondition(true))
                .save();

        AUTOCLAVE_RECIPES.recipeBuilder("nuclear_star")
                .inputItems(GTOTagPrefix.NANITES, GTOMaterials.CosmicNeutronium)
                .inputItems(GTOItems.UNSTABLE_STAR.asItem())
                .inputFluids(GTOMaterials.Infinity.getFluid(288))
                .outputItems(GTOTagPrefix.CONTAMINABLE_NANITES, GTOMaterials.CosmicNeutronium)
                .outputItems(GTOItems.NUCLEAR_STAR.asItem())
                .EUt(31457280)
                .duration(480)
                .addCondition(new GravityCondition(true))
                .save();

        AUTOCLAVE_RECIPES.recipeBuilder("super_mutated_living_solder")
                .inputItems(GTOItems.SPACE_ESSENCE.asStack(64))
                .inputItems(GTOItems.DRACONIUM_DIRT.asStack(64))
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(10000))
                .outputItems(GTOBlocks.ESSENCE_BLOCK.asItem())
                .outputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(10000))
                .EUt(7864320)
                .duration(2400)
                .save();

        AUTOCLAVE_RECIPES.recipeBuilder("contained_kerr_singularity")
                .notConsumable(GTOTagPrefix.NANITES, GTOMaterials.Vibranium)
                .inputItems(GTOItems.CONTAINED_KERR_NEWMANN_SINGULARITY.asItem())
                .inputFluids(GTOMaterials.FreeElectronGas.getFluid(1000))
                .outputItems(GTOItems.CONTAINED_KERR_SINGULARITY.asItem())
                .EUt(1966080)
                .duration(400)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        AUTOCLAVE_RECIPES.recipeBuilder("draconium_dust")
                .notConsumable(GTOTagPrefix.NANITES, GTOMaterials.Enderium, 64)
                .inputItems(GTOItems.DRACONIUM_DIRT.asItem())
                .inputFluids(GTOMaterials.DragonElement.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.Draconium)
                .EUt(125829120)
                .duration(200)
                .cleanroom(GTOCleanroomType.LAW_CLEANROOM)
                .save();

        AUTOCLAVE_RECIPES.recipeBuilder("prescient_crystal")
                .inputItems(EIOItems.VIBRANT_CRYSTAL.asItem())
                .inputFluids(GTOMaterials.Mithril.getFluid(8))
                .outputItems(EIOItems.PRESCIENT_CRYSTAL.asItem())
                .EUt(30)
                .duration(200)
                .addCondition(new VacuumCondition(4))
                .save();

        AUTOCLAVE_RECIPES.recipeBuilder("vibrant_crystal")
                .inputItems(TagPrefix.gem, GTMaterials.Emerald)
                .inputFluids(GTOMaterials.PulsatingAlloy.getFluid(72))
                .outputItems(EIOItems.VIBRANT_CRYSTAL.asItem())
                .EUt(30)
                .duration(160)
                .addCondition(new VacuumCondition(2))
                .save();

        AUTOCLAVE_RECIPES.recipeBuilder("imprinted_resonatic_circuit_board")
                .inputItems(GTOItems.RAW_IMPRINTED_RESONATIC_CIRCUIT_BOARD.asItem())
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(432))
                .outputItems(GTOItems.IMPRINTED_RESONATIC_CIRCUIT_BOARD.asItem())
                .EUt(1920)
                .duration(300)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        AUTOCLAVE_RECIPES.recipeBuilder("pulsating_crystal")
                .inputItems(TagPrefix.gem, GTMaterials.Diamond)
                .inputFluids(GTOMaterials.PulsatingAlloy.getFluid(72))
                .outputItems(EIOItems.PULSATING_CRYSTAL.asItem())
                .EUt(30)
                .duration(100)
                .addCondition(new VacuumCondition(2))
                .save();
        AUTOCLAVE_RECIPES.builder("impregnated_alkane_filled_mfpc_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.BasicMFPC)
                .inputItems(TagPrefix.dustSmall, GTOMaterials.Cetane)
                .outputItems(TagPrefix.dust, GTOMaterials.ImpregnatedAlkaneFilledMFPC)
                .inputFluids(GTOMaterials.LiquidNitrogen, 2000)
                .outputFluids(GTMaterials.Nitrogen, 2000)
                .EUt(9000)
                .duration(1600)
                .addCondition(new VacuumCondition(4))
                .save();
        AUTOCLAVE_RECIPES.builder("impregnated_alkane_filled_recycled_mfpc_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.RecycleBasicMFPC)
                .inputItems(TagPrefix.dustSmall, GTOMaterials.Cetane)
                .outputItems(TagPrefix.dust, GTOMaterials.ImpregnatedAlkaneFilledMFPC)
                .inputFluids(GTOMaterials.LiquidNitrogen, 2000)
                .outputFluids(GTMaterials.Nitrogen, 2000)
                .EUt(9000)
                .duration(160)
                .addCondition(new VacuumCondition(4))
                .save();
        AUTOCLAVE_RECIPES.builder("etched_carbon_nanotube_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.CarbonNanotubes)
                .inputItems(TagPrefix.dustTiny, GTMaterials.RockSalt)
                .outputItems(TagPrefix.dust, GTOMaterials.EtchedCarbonNanotube)
                .inputFluids(GTOMaterials.SodiumHydroxideSolution, 1000)
                .EUt(1222)
                .duration(1222)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();
    }
}
