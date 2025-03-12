package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.GTODimensions;
import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import appeng.core.definitions.AEBlocks;

import static com.gto.gtocore.common.data.GTORecipeTypes.CHEMICAL_BATH_RECIPES;

interface ChemicalBath {

    static void init() {
        CHEMICAL_BATH_RECIPES.recipeBuilder(GTOCore.id("petri_dish"))
                .inputItems(GTOItems.CONTAMINATED_PETRI_DISH)
                .outputItems(GTItems.PETRI_DISH)
                .inputFluids(GTOMaterials.PiranhaSolution.getFluid(100))
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(25).EUt(30).save();

        CHEMICAL_BATH_RECIPES.recipeBuilder(GTOCore.id("naquadria_sulfate_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.Sodium, 6)
                .inputFluids(GTOMaterials.AcidicNaquadriaCaesiumfluoride.getFluid(3000))
                .outputItems(TagPrefix.dust, GTMaterials.NaquadriaSulfate, 6)
                .outputItems(TagPrefix.dust, GTMaterials.TriniumSulfide, 2)
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumFluoride, 8)
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumSulfate, 7)
                .chancedOutput(TagPrefix.dust, GTMaterials.Caesium, 8000, 500)
                .EUt(120)
                .duration(200)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder(GTOCore.id("caesium_hydroxide_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.Caesium, 2)
                .inputFluids(GTOMaterials.HydrogenPeroxide.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.CaesiumHydroxide, 6)
                .EUt(120)
                .duration(180)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder(GTOCore.id("glucose"))
                .inputItems(TagPrefix.gem, GTMaterials.Sugar, 2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.Glucose, 24)
                .EUt(480)
                .duration(300)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder(GTOCore.id("kevlar_plate"))
                .inputItems(GTOItems.WOVEN_KEVLAR.asItem())
                .inputFluids(GTOMaterials.PolyurethaneResin.getFluid(1000))
                .outputItems(TagPrefix.plate, GTOMaterials.Kevlar)
                .EUt(480)
                .duration(400)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder(GTOCore.id("photon_carrying_wafer"))
                .inputItems(GTOItems.RAW_PHOTON_CARRYING_WAFER.asItem())
                .inputFluids(GTMaterials.Blaze.getFluid(288))
                .outputItems(GTOItems.PHOTON_CARRYING_WAFER.asItem())
                .EUt(1920)
                .duration(800)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder(GTOCore.id("high_strength_concrete"))
                .inputItems(TagPrefix.frameGt, GTMaterials.Steel)
                .inputFluids(GTMaterials.Concrete.getFluid(1152))
                .outputItems(GTOBlocks.HIGH_STRENGTH_CONCRETE.asItem())
                .EUt(480)
                .duration(200)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder(GTOCore.id("damascus_steel_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.Steel)
                .inputFluids(GTMaterials.Lubricant.getFluid(100))
                .outputItems(TagPrefix.dust, GTMaterials.DamascusSteel)
                .EUt(120)
                .duration(200)
                .dimension(GTODimensions.ANCIENT_WORLD)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder(GTOCore.id("end_stone"))
                .inputItems(new ItemStack(Blocks.ANDESITE.asItem()))
                .inputFluids(GTMaterials.LiquidEnderAir.getFluid(1000))
                .outputItems(TagPrefix.rock, GTMaterials.Endstone)
                .EUt(480)
                .duration(800)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder(GTOCore.id("resonating_gem"))
                .inputItems(TagPrefix.gemExquisite, GTMaterials.Sapphire)
                .inputFluids(GTOMaterials.LiquidStarlight.getFluid(1000))
                .outputItems(GTOItems.RESONATING_GEM.asItem())
                .EUt(31457280)
                .duration(400)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder(GTOCore.id("leached_turpentine"))
                .inputItems(new ItemStack(Blocks.DARK_OAK_LOG.asItem()))
                .inputFluids(GTMaterials.Naphtha.getFluid(1000))
                .outputFluids(GTOMaterials.LeachedTurpentine.getFluid(1000))
                .EUt(480)
                .duration(80)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder(GTOCore.id("vibrant_alloy_ingot"))
                .inputItems(TagPrefix.ingotHot, GTOMaterials.VibrantAlloy)
                .inputFluids(GTOMaterials.CoolantLiquid.getFluid(100))
                .outputItems(TagPrefix.ingot, GTOMaterials.VibrantAlloy)
                .EUt(120)
                .duration(280)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder(GTOCore.id("sculk_vein"))
                .inputItems(new ItemStack(Blocks.VINE.asItem()))
                .inputFluids(GTMaterials.EchoShard.getFluid(10))
                .outputItems(new ItemStack(Blocks.SCULK_VEIN.asItem()))
                .EUt(120)
                .duration(200)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder(GTOCore.id("x_ray_waveguide"))
                .inputItems(GTOItems.FULLERENE_POLYMER_MATRIX_FINE_TUBING.asItem())
                .inputFluids(GTOMaterials.IridiumTrichlorideSolution.getFluid(100))
                .outputItems(GTOItems.X_RAY_WAVEGUIDE.asItem())
                .EUt(8000000)
                .duration(240)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder(GTOCore.id("netherrack"))
                .inputItems(new ItemStack(Blocks.GRANITE.asItem()))
                .inputFluids(GTMaterials.LiquidNetherAir.getFluid(1000))
                .outputItems(TagPrefix.rock, GTMaterials.Netherrack)
                .EUt(120)
                .duration(800)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder(GTOCore.id("x_ray_mirror_plate"))
                .inputItems(TagPrefix.plate, GTMaterials.Graphene)
                .inputFluids(GTOMaterials.IridiumTrichlorideSolution.getFluid(100))
                .outputItems(GTOItems.X_RAY_MIRROR_PLATE.asItem())
                .EUt(2000000)
                .duration(240)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder(GTOCore.id("black_candle"))
                .inputItems(new ItemStack(Blocks.TRIPWIRE.asItem()))
                .inputFluids(GTMaterials.Oil.getFluid(100))
                .outputItems(new ItemStack(Blocks.BLACK_CANDLE.asItem()))
                .EUt(120)
                .duration(200)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder(GTOCore.id("ash_leaching_solution"))
                .inputItems(TagPrefix.dust, GTMaterials.Ash, 12)
                .inputFluids(GTMaterials.SulfuricAcid.getFluid(1000))
                .outputFluids(GTOMaterials.AshLeachingSolution.getFluid(1000))
                .EUt(120)
                .duration(400)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder(GTOCore.id("fullerene_polymer_matrix_pulp_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.PalladiumFullereneMatrix)
                .inputFluids(GTOMaterials.PCBs.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.FullerenePolymerMatrixPulp, 2)
                .EUt(8000000)
                .duration(40)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder(GTOCore.id("metal_residue_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.PartiallyOxidizedResidues)
                .inputFluids(GTOMaterials.BedrockGas.getFluid(100))
                .outputItems(TagPrefix.dust, GTOMaterials.MetalResidue)
                .EUt(122880)
                .duration(200)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder(GTOCore.id("ender_obsidian"))
                .inputItems(GTOBlocks.SHINING_OBSIDIAN.asItem())
                .inputFluids(GTMaterials.EnderEye.getFluid(1152))
                .outputItems(GTOBlocks.ENDER_OBSIDIAN.asItem())
                .EUt(480)
                .duration(200)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder(GTOCore.id("flawless_budding_quartz"))
                .inputItems(new ItemStack(AEBlocks.FLAWED_BUDDING_QUARTZ.block().asItem()))
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(AEBlocks.FLAWLESS_BUDDING_QUARTZ.block().asItem()))
                .EUt(30)
                .duration(400)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder(GTOCore.id("eternity_catalyst"))
                .inputItems(GTOItems.SPACETIME_CATALYST.asItem())
                .inputFluids(GTOMaterials.Eternity.getFluid(1000))
                .outputItems(GTOItems.ETERNITY_CATALYST.asItem())
                .EUt(8053063680L)
                .duration(1600)
                .save();
    }
}
