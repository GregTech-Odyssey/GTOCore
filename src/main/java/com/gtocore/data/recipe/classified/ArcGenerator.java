package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.recipe.condition.GravityCondition;
import com.gtocore.common.recipe.condition.VacuumCondition;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import appeng.core.definitions.AEItems;
import com.enderio.base.common.init.EIOBlocks;
import com.enderio.base.common.init.EIOFluids;
import com.enderio.base.common.init.EIOItems;
import com.kyanite.deeperdarker.content.DDItems;
import earth.terrarium.adastra.common.registry.ModItems;

import static com.gregtechceu.gtceu.api.GTValues.UV;
import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gtocore.common.data.GTOItems.ENDER_DIAMOND;
import static com.gtocore.common.data.GTOItems.HELIO_COAL;
import static com.gtocore.common.data.GTORecipeTypes.ARC_GENERATOR_RECIPES;

final class ArcGenerator {

    public static void init() {
        ARC_GENERATOR_RECIPES.recipeBuilder("ender_pearl_dust")
                .duration(400).EUt(30)
                .inputItems(TagPrefix.dust, GTMaterials.Beryllium)
                .inputItems(TagPrefix.dust, GTMaterials.Potassium, 4)
                .inputFluids(GTMaterials.Nitrogen.getFluid(5000))
                .circuitMeta(1)
                .outputItems(TagPrefix.dust, GTMaterials.EnderPearl, 10)
                .addCondition(new VacuumCondition(3))
                .save();

        ARC_GENERATOR_RECIPES.recipeBuilder("fluix_pearl")
                .inputItems(TagPrefix.gem, GTMaterials.EnderPearl)
                .inputItems(TagPrefix.gemFlawless, GTOMaterials.Fluix)
                .outputItems(new ItemStack(AEItems.FLUIX_PEARL.asItem()))
                .EUt(30)
                .duration(160)
                .save();

        ARC_GENERATOR_RECIPES.recipeBuilder("piglin_head")
                .notConsumable("minecraft:player_head")
                .inputItems(new ItemStack(Items.PORKCHOP.asItem(), 9))
                .inputItems(TagPrefix.nugget, GTMaterials.Gold)
                .outputItems(new ItemStack(Blocks.PIGLIN_HEAD.asItem()))
                .EUt(120)
                .duration(200)
                .save();

        ARC_GENERATOR_RECIPES.recipeBuilder("zombie_head")
                .notConsumable("minecraft:player_head")
                .inputItems(new ItemStack(Items.ROTTEN_FLESH.asItem(), 9))
                .outputItems(new ItemStack(Blocks.ZOMBIE_HEAD.asItem()))
                .EUt(120)
                .duration(100)
                .save();

        ARC_GENERATOR_RECIPES.recipeBuilder("adamantine_compounds_dust_a")
                .notConsumable(GTOItems.MICROWORMHOLE_GENERATOR.asItem())
                .inputItems(TagPrefix.dust, GTOMaterials.AdamantineCompounds, 4)
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.Adamantine)
                .EUt(125829120)
                .duration(20)
                .save();

        ARC_GENERATOR_RECIPES.recipeBuilder("skeleton_skull")
                .notConsumable("minecraft:player_head")
                .inputItems(TagPrefix.rod, GTMaterials.Bone, 9)
                .outputItems(new ItemStack(Blocks.SKELETON_SKULL.asItem()))
                .EUt(120)
                .duration(100)
                .save();

        ARC_GENERATOR_RECIPES.recipeBuilder("thaumium_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.InfusedGold, 8)
                .inputItems(TagPrefix.dust, GTOMaterials.Soularium, 4)
                .inputItems(TagPrefix.dust, GTMaterials.Iron, 4)
                .inputItems(TagPrefix.dust, GTMaterials.Lapis, 16)
                .inputFluids(GTMaterials.DistilledWater.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.Thaumium, 16)
                .EUt(7680)
                .duration(480)
                .save();

        ARC_GENERATOR_RECIPES.recipeBuilder("ender_air")
                .inputItems(TagPrefix.dust, GTMaterials.EnderPearl, 64)
                .inputFluids(GTOMaterials.FumingNitricAcid.getFluid(1000))
                .inputFluids(GTMaterials.NitrogenDioxide.getFluid(10000))
                .inputFluids(GTMaterials.Helium.getFluid(10000))
                .inputFluids(GTMaterials.Radon.getFluid(1000))
                .inputFluids(GTMaterials.Deuterium.getFluid(1000))
                .inputFluids(GTMaterials.Xenon.getFluid(1000))
                .outputFluids(GTMaterials.EnderAir.getFluid(1000))
                .EUt(480)
                .duration(800)
                .save();

        ARC_GENERATOR_RECIPES.recipeBuilder("nitric_oxide")
                .inputFluids(GTMaterials.Air.getFluid(10000))
                .outputFluids(GTMaterials.NitricOxide.getFluid(1000))
                .EUt(120)
                .duration(200)
                .save();

        ARC_GENERATOR_RECIPES.builder("wither_skeleton_skull")
                .inputItems(new ItemStack(Items.WITHER_SKELETON_SKULL.asItem()))
                .inputItems(TagPrefix.gemExquisite, GTMaterials.Coal)
                .inputItems(TagPrefix.dust, GTMaterials.Iridium)
                .inputItems(TagPrefix.dust, GTMaterials.Osmium)
                .inputItems(TagPrefix.dust, GTMaterials.Rhodium)
                .outputItems(TagPrefix.dust, GTMaterials.NetherStar, 4)
                .inputFluids(GTMaterials.NetherAir, 8000)
                .inputFluids(GTMaterials.RocketFuel, 1000)
                .EUt(480)
                .duration(200)
                .addCondition(new GravityCondition(true))
                .save();

        ARC_GENERATOR_RECIPES.recipeBuilder("netherite_scrap")
                .inputItems(new ItemStack(Blocks.ANCIENT_DEBRIS.asItem()))
                .inputItems(new ItemStack(Items.PRISMARINE_SHARD.asItem()))
                .inputFluids(GTMaterials.NetherAir.getFluid(100))
                .outputItems(new ItemStack(Items.NETHERITE_SCRAP.asItem()))
                .EUt(480)
                .duration(240)
                .save();

        ARC_GENERATOR_RECIPES.recipeBuilder("nether_air")
                .inputItems(TagPrefix.dust, GTMaterials.Gunpowder, 64)
                .inputFluids(GTMaterials.Blaze.getFluid(1000))
                .inputFluids(GTMaterials.HydrogenSulfide.getFluid(10000))
                .inputFluids(GTMaterials.SulfurDioxide.getFluid(10000))
                .inputFluids(GTMaterials.CarbonMonoxide.getFluid(10000))
                .inputFluids(GTMaterials.CoalGas.getFluid(1000))
                .inputFluids(GTMaterials.Helium.getFluid(1000))
                .outputFluids(GTMaterials.NetherAir.getFluid(1000))
                .EUt(120)
                .duration(400)
                .save();

        ARC_GENERATOR_RECIPES.recipeBuilder("dragon_egg")
                .inputItems(new ItemStack(Items.EGG.asItem()))
                .inputItems(GTOItems.WARPED_ENDER_PEARL.asStack(64))
                .inputFluids(GTOMaterials.Antimatter.getFluid(1000))
                .inputFluids(GTMaterials.EnderEye.getFluid(10000))
                .inputFluids(GTMaterials.SterileGrowthMedium.getFluid(10000))
                .chancedOutput(new ItemStack(Blocks.DRAGON_EGG.asItem()), 9000, 0)
                .EUt(491520)
                .duration(2000)
                .save();

        ARC_GENERATOR_RECIPES.recipeBuilder("grains_of_infinity")
                .chancedInput(ChemicalHelper.get(TagPrefix.gem, GTMaterials.Flint), 2000, 0)
                .inputItems(TagPrefix.dustTiny, GTMaterials.Obsidian)
                .outputItems(EIOItems.GRAINS_OF_INFINITY.asItem())
                .EUt(30)
                .duration(200)
                .save();

        ARC_GENERATOR_RECIPES.recipeBuilder("crying_obsidian")
                .inputItems(TagPrefix.rock, GTMaterials.Obsidian)
                .inputFluids(GTOMaterials.Antimatter.getFluid(10))
                .outputItems(new ItemStack(Blocks.CRYING_OBSIDIAN.asItem()))
                .EUt(480)
                .duration(200)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        ARC_GENERATOR_RECIPES.recipeBuilder("energetic_alloy_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Redstone)
                .inputItems(TagPrefix.dust, GTMaterials.Gold)
                .inputItems(TagPrefix.dust, GTMaterials.Glowstone)
                .inputFluids(GTMaterials.Water.getFluid(100))
                .outputItems(TagPrefix.dust, GTOMaterials.EnergeticAlloy, 3)
                .EUt(30)
                .duration(480)
                .save();

        ARC_GENERATOR_RECIPES.recipeBuilder("naquadah_contain_rare_earth_fluoride_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Alunite)
                .inputFluids(GTOMaterials.RareEarthChlorides.getFluid(6000))
                .inputFluids(GTMaterials.AcidicEnrichedNaquadahSolution.getFluid(3000))
                .inputFluids(GTMaterials.AcidicNaquadriaSolution.getFluid(3000))
                .inputFluids(GTMaterials.HydrofluoricAcid.getFluid(2000))
                .inputFluids(GTOMaterials.EnrichedNaquadahFront.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.NaquadahContainRareEarthFluoride)
                .outputFluids(GTMaterials.HydrochloricAcid.getFluid(6000))
                .EUt(491520)
                .duration(400)
                .save();

        ARC_GENERATOR_RECIPES.recipeBuilder("ghast_tear")
                .inputItems(TagPrefix.dustTiny, GTMaterials.Lithium)
                .inputItems(TagPrefix.dustTiny, GTMaterials.Potassium)
                .inputFluids(GTMaterials.SaltWater.getFluid(1000))
                .outputItems(new ItemStack(Items.GHAST_TEAR.asItem()))
                .EUt(30)
                .duration(400)
                .save();

        ARC_GENERATOR_RECIPES.recipeBuilder("glowstone_dust")
                .circuitMeta(1)
                .inputItems(TagPrefix.dust, GTMaterials.Gold)
                .inputItems(TagPrefix.dust, GTMaterials.Redstone)
                .outputItems(TagPrefix.dust, GTMaterials.Glowstone, 2)
                .EUt(30)
                .duration(200)
                .save();

        ARC_GENERATOR_RECIPES.recipeBuilder("creeper_head")
                .notConsumable("minecraft:player_head")
                .inputItems(TagPrefix.dust, GTMaterials.Gunpowder, 9)
                .outputItems(new ItemStack(Blocks.CREEPER_HEAD.asItem()))
                .EUt(120)
                .duration(100)
                .save();

        ARC_GENERATOR_RECIPES.recipeBuilder("fluix_crystal")
                .circuitMeta(2)
                .inputItems(TagPrefix.dust, GTMaterials.Redstone)
                .inputItems(TagPrefix.gem, GTMaterials.NetherQuartz)
                .inputItems(TagPrefix.gem, GTMaterials.CertusQuartz)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(TagPrefix.gem, GTOMaterials.Fluix, 2)
                .EUt(30)
                .duration(80)
                .save();

        ARC_GENERATOR_RECIPES.recipeBuilder("enderman_head")
                .notConsumable("minecraft:player_head")
                .inputItems(TagPrefix.gem, GTMaterials.EnderPearl, 9)
                .outputItems(EIOBlocks.ENDERMAN_HEAD.asItem())
                .EUt(480)
                .duration(100)
                .save();

        ARC_GENERATOR_RECIPES.recipeBuilder("charged_certus_quartz_crystal")
                .circuitMeta(1)
                .inputItems(TagPrefix.gem, GTMaterials.CertusQuartz)
                .outputItems(new ItemStack(AEItems.CERTUS_QUARTZ_CRYSTAL_CHARGED.asItem()))
                .EUt(30)
                .duration(60)
                .save();

        ARC_GENERATOR_RECIPES.recipeBuilder("echobone")
                .inputItems(TagPrefix.rod, GTMaterials.Bone)
                .inputFluids(GTMaterials.EchoShard.getFluid(144))
                .outputItems(DDItems.SCULK_BONE.get())
                .EUt(480)
                .duration(400)
                .save();

        ARC_GENERATOR_RECIPES.recipeBuilder("ice_shard")
                .inputItems(new ItemStack(Blocks.BLUE_ICE.asItem()))
                .outputItems(ModItems.ICE_SHARD.get())
                .EUt(30)
                .duration(200)
                .save();

        ARC_GENERATOR_RECIPES.recipeBuilder("prismarine")
                .inputItems(new ItemStack(Blocks.COBBLESTONE.asItem()))
                .inputFluids(GTMaterials.SaltWater.getFluid(10))
                .outputItems(new ItemStack(Blocks.PRISMARINE.asItem()))
                .EUt(30)
                .duration(200)
                .save();

        ARC_GENERATOR_RECIPES.recipeBuilder("reinforced_deepslate")
                .inputItems(TagPrefix.rock, GTMaterials.Deepslate)
                .inputItems(TagPrefix.block, GTMaterials.EchoShard)
                .inputItems(new ItemStack(DDItems.SCULK_BONE.get(), 4))
                .inputFluids(GTMaterials.EchoShard.getFluid(1440))
                .outputItems(new ItemStack(Blocks.REINFORCED_DEEPSLATE.asItem()))
                .EUt(480)
                .duration(200)
                .save();

        ARC_GENERATOR_RECIPES.builder("dust_blizz")
                .inputItems(Items.SNOW_BLOCK.asItem(), 64)
                .outputItems(GTOItems.DUST_BLIZZ.asItem())
                .inputFluids(GTOMaterials.TranscendingMatter, 2000)
                .inputFluids(EIOFluids.CLOUD_SEED_CONCENTRATED.getSource(), 2000)
                .inputFluids(GTMaterials.Ice, 10000)
                .EUt(1920)
                .duration(1600)
                .save();

        ARC_GENERATOR_RECIPES.builder("sanguinite_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.Adamantium)
                .inputItems("botania:quartz_blaze")
                .inputItems(TagPrefix.dust, GTMaterials.Netherrack)
                .inputItems(TagPrefix.dustTiny, GTOMaterials.EnergeticNetherite)
                .outputItems(TagPrefix.dust, GTOMaterials.Sanguinite)
                .inputFluids(GTOMaterials.Salamander, 10)
                .EUt(1966080)
                .duration(600)
                .save();

        ARC_GENERATOR_RECIPES.recipeBuilder("astral_silver")
                .inputItems(TagPrefix.ingot, GTMaterials.Silver)
                .inputItems(TagPrefix.gemExquisite, GTMaterials.Lapis)
                .inputFluids(GTOMaterials.TheWaterFromTheWellOfWisdom.getFluid(1000))
                .outputItems(TagPrefix.ingot, GTOMaterials.AstralSilver)
                .EUt(VA[UV])
                .duration(200)
                .save();

        ARC_GENERATOR_RECIPES.recipeBuilder("helio_coal")
                .inputItems(Items.COAL)
                .inputItems(TagPrefix.gemExquisite, GTMaterials.Lapis)
                .inputFluids(GTOMaterials.TheWaterFromTheWellOfWisdom.getFluid(1000))
                .outputItems(HELIO_COAL)
                .EUt(VA[UV])
                .duration(200)
                .save();

        ARC_GENERATOR_RECIPES.recipeBuilder("ender_diamond")
                .inputItems(Items.DIAMOND)
                .inputItems(TagPrefix.gemExquisite, GTMaterials.Lapis)
                .inputFluids(GTOMaterials.TheWaterFromTheWellOfWisdom.getFluid(1000))
                .outputItems(ENDER_DIAMOND)
                .EUt(VA[UV])
                .duration(200)
                .save();
    }
}
