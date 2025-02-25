package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.GTOWorldGenLayers;
import com.gto.gtocore.api.machine.GTOCleanroomType;
import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;
import com.gto.gtocore.common.data.machines.MultiBlockG;
import com.gto.gtocore.common.recipe.condition.GravityCondition;
import com.gto.gtocore.utils.TagUtils;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GCYMRecipeTypes;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fluids.FluidStack;

import appeng.core.definitions.AEItems;
import com.enderio.base.common.init.EIOFluids;
import dev.shadowsoffire.apotheosis.ench.Ench;

import java.util.function.Consumer;

public interface ClassifiedRecipe {

    static void init(Consumer<FinishedRecipe> provider) {
        Vanilla.init(provider);
        MolecularTransformer.init(provider);
        ReactionFurnace.init(provider);
        LiquefactionFurnace.init(provider);
        Desulfurizer.init(provider);
        DigestionTeatment.init(provider);
        DissolutionTreatment.init(provider);
        VacuumDrying.init(provider);
        FlotatingBeneficiation.init(provider);
        AlloySmelter.init(provider);
        AtomicEnergyExcitation.init(provider);
        FuelRefining.init(provider);
        NanoForge.init(provider);
        NanitesIntegratedProcessingCenter.init(provider);
        PetrochemicalPlant.init(provider);
        UltimateMaterialForge.init(provider);
        GravitationShockburst.init(provider);
        BedrockDrillingRig.init(provider);
        QuantumForceTransformer.init(provider);
        NeutronCompressor.init(provider);
        DimensionallyTranscendentMixer.init(provider);
        PrecisionLaserEngraver.init(provider);
        DimensionalFocusEngravingArray.init(provider);
        SuperParticleCollider.init(provider);
        AggregationDevice.init(provider);
        LargeGasCollector.init(provider);
        PCBFactory.init(provider);
        FishingGround.init(provider);
        DrillingModule.init(provider);
        MinerModule.init(provider);
        ArcFurnace.init(provider);
        Autoclave.init(provider);
        Macerator.init(provider);
        Canner.init(provider);
        Centrifuge.init(provider);
        ChemicalBath.init(provider);
        ChemicaRreactor.init(provider);
        Compressor.init(provider);
        Cutter.init(provider);
        Distillery.init(provider);
        Electrolyzer.init(provider);
        PrecisionAssembler.init(provider);
        CircuitAssemblyLine.init(provider);
        DecayHastener.init(provider);
        LargeVoidMiner.init(provider);
        TranscendingCrafting.init(provider);
        PlasmaCondenser.init(provider);
        DimensionallytranscendentPlasmaForge.init(provider);
        Greenhouse.init(provider);
        StellarForge.init(provider);
        FissionReactor.init(provider);
        NeutronActivator.init(provider);
        ImplosionCompressor.init(provider);
        Dehydrator.init(provider);
        ArcGenerator.init(provider);
        Vacuum.init(provider);
        Cracking.init(provider);
        FluidSolidfication.init(provider);
        FormingPress.init(provider);
        Fusion.init(provider);
        Mixer.init(provider);
        Packer.init(provider);
        LaserEngraver.init(provider);
        CircuitAssembler.init(provider);
        AssemblerModule.init(provider);
        Assembler.init(provider);
        AssemblyLine.init(provider);
        SuprachronalAssemblyLine.init(provider);
        Incubator.init(provider);
        IsaMill.init(provider);
        IsostaticPressing.init(provider);
        Sifter.init(provider);

        GTRecipeTypes.POLARIZER_RECIPES.recipeBuilder(GTOCore.id("attuned_tengam_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.PurifiedTengam)
                .outputItems(TagPrefix.dust, GTOMaterials.AttunedTengam)
                .EUt(125829120)
                .duration(400)
                .save(provider);

        GTRecipeTypes.POLARIZER_RECIPES.recipeBuilder(GTOCore.id("magnetic_long_netherite_rod"))
                .inputItems(GTOItems.LONG_NETHERITE_ROD.asStack())
                .outputItems(GTOItems.MAGNETIC_LONG_NETHERITE_ROD.asStack())
                .EUt(1966080)
                .duration(400)
                .save(provider);

        GTRecipeTypes.POLARIZER_RECIPES.recipeBuilder(GTOCore.id("magnetic_netherite_rod"))
                .inputItems(GTOItems.NETHERITE_ROD.asStack())
                .outputItems(GTOItems.MAGNETIC_NETHERITE_ROD.asStack())
                .EUt(1966080)
                .duration(200)
                .save(provider);

        GTRecipeTypes.POLARIZER_RECIPES.recipeBuilder(GTOCore.id("small_attuned_tengam_dust"))
                .inputItems(TagPrefix.dustSmall, GTOMaterials.PurifiedTengam)
                .outputItems(TagPrefix.dustSmall, GTOMaterials.AttunedTengam)
                .EUt(31457280)
                .duration(400)
                .save(provider);

        GTRecipeTypes.POLARIZER_RECIPES.recipeBuilder(GTOCore.id("triplet_neutronium_sphere"))
                .inputItems(GTOItems.NEUTRONIUM_SPHERE.asStack())
                .outputItems(GTOItems.TRIPLET_NEUTRONIUM_SPHERE.asStack())
                .EUt(5000000)
                .duration(200)
                .save(provider);

        GTRecipeTypes.ORE_WASHER_RECIPES.recipeBuilder(GTOCore.id("clean_inert_residues_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.InertResidues)
                .inputFluids(GTMaterials.AquaRegia.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.CleanInertResidues)
                .EUt(480)
                .duration(400)
                .save(provider);

        GTRecipeTypes.ORE_WASHER_RECIPES.recipeBuilder(GTOCore.id("clean_raw_tengam_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.RawTengam)
                .inputFluids(GTMaterials.DistilledWater.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.CleanRawTengam)
                .EUt(480)
                .duration(800)
                .save(provider);

        GTRecipeTypes.LATHE_RECIPES.recipeBuilder(GTOCore.id("non_linear_optical_lens"))
                .inputItems(GTOItems.PERIODICALLY_POLED_LITHIUM_NIOBATE_BOULE.asStack())
                .outputItems(GTOItems.NON_LINEAR_OPTICAL_LENS.asStack())
                .EUt(1966080)
                .duration(360)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        GTRecipeTypes.LATHE_RECIPES.recipeBuilder(GTOCore.id("magmatter_rod"))
                .inputItems(TagPrefix.ingot, GTOMaterials.Magmatter)
                .outputItems(TagPrefix.rod, GTOMaterials.Magmatter)
                .outputItems(TagPrefix.dustSmall, GTOMaterials.Magmatter)
                .EUt(2013265920)
                .duration(200)
                .save(provider);

        GTRecipeTypes.FORGE_HAMMER_RECIPES.recipeBuilder(GTOCore.id("long_netherite_rod"))
                .inputItems(GTOItems.NETHERITE_ROD.asStack(2))
                .outputItems(GTOItems.LONG_NETHERITE_ROD.asStack())
                .EUt(30)
                .duration(200)
                .save(provider);

        GTRecipeTypes.FORGE_HAMMER_RECIPES.recipeBuilder(GTOCore.id("prismarine_crystals"))
                .inputItems(new ItemStack(Items.PRISMARINE_SHARD.asItem()))
                .outputItems(new ItemStack(Items.PRISMARINE_CRYSTALS.asItem()))
                .EUt(16)
                .duration(20)
                .save(provider);

        GTRecipeTypes.FORGE_HAMMER_RECIPES.recipeBuilder(GTOCore.id("magnetic_long_netherite_rod"))
                .inputItems(GTOItems.MAGNETIC_NETHERITE_ROD.asStack(2))
                .outputItems(GTOItems.MAGNETIC_LONG_NETHERITE_ROD.asStack())
                .EUt(30)
                .duration(200)
                .save(provider);

        GTRecipeTypes.FORGE_HAMMER_RECIPES.recipeBuilder(GTOCore.id("long_magmatter_rod"))
                .inputItems(TagPrefix.rod, GTOMaterials.Magmatter, 2)
                .outputItems(TagPrefix.rodLong, GTOMaterials.Magmatter)
                .EUt(2013265920)
                .duration(300)
                .save(provider);

        GTRecipeTypes.FORGE_HAMMER_RECIPES.recipeBuilder(GTOCore.id("special_ceramics_dust"))
                .inputItems(new ItemStack(Blocks.BROWN_GLAZED_TERRACOTTA.asItem()))
                .outputItems(TagPrefix.dust, GTOMaterials.SpecialCeramics)
                .EUt(7680)
                .duration(20)
                .save(provider);

        GTRecipeTypes.PYROLYSE_RECIPES.recipeBuilder(GTOCore.id("rawradox1"))
                .inputItems(GTOBlocks.VARIATION_WOOD.asStack(16))
                .circuitMeta(1)
                .inputFluids(GTOMaterials.EnrichedXenoxene.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.Ash)
                .outputFluids(GTOMaterials.RawRadox.getFluid(10000))
                .EUt(7864320)
                .duration(600)
                .save(provider);

        GTRecipeTypes.PYROLYSE_RECIPES.recipeBuilder(GTOCore.id("rawradox"))
                .inputItems(GTOBlocks.VARIATION_WOOD.asStack(16))
                .circuitMeta(1)
                .inputFluids(GTOMaterials.Xenoxene.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.Ash)
                .outputFluids(GTOMaterials.RawRadox.getFluid(1000))
                .EUt(491520)
                .duration(900)
                .save(provider);

        GTRecipeTypes.EVAPORATION_RECIPES.recipeBuilder(GTOCore.id("salt_water"))
                .inputFluids(GTMaterials.Water.getFluid(50000))
                .outputFluids(GTMaterials.SaltWater.getFluid(1000))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTRecipeTypes.BLAST_RECIPES.recipeBuilder(GTOCore.id("hot_draconium_ingot_1"))
                .inputItems(TagPrefix.dust, GTOMaterials.Draconium)
                .inputFluids(GTMaterials.CetaneBoostedDiesel.getFluid(2000))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.Draconium)
                .EUt(125829120)
                .duration(800)
                .blastFurnaceTemp(21600)
                .save(provider);

        GTRecipeTypes.BLAST_RECIPES.recipeBuilder(GTOCore.id("hot_draconium_ingot_3"))
                .inputItems(TagPrefix.dust, GTOMaterials.Draconium)
                .inputFluids(GTMaterials.HighOctaneGasoline.getFluid(500))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.Draconium)
                .EUt(125829120)
                .duration(200)
                .blastFurnaceTemp(21600)
                .save(provider);

        GTRecipeTypes.BLAST_RECIPES.recipeBuilder(GTOCore.id("hot_draconium_ingot_2"))
                .inputItems(TagPrefix.dust, GTOMaterials.Draconium)
                .inputFluids(GTMaterials.Gasoline.getFluid(1000))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.Draconium)
                .EUt(125829120)
                .duration(400)
                .blastFurnaceTemp(21600)
                .save(provider);

        GTRecipeTypes.BLAST_RECIPES.recipeBuilder(GTOCore.id("netherite_ingot"))
                .inputItems(new ItemStack(Items.NETHERITE_SCRAP.asItem()))
                .inputItems(TagPrefix.ingot, GTOMaterials.Calorite)
                .inputFluids(GTMaterials.Neon.getFluid(100))
                .outputItems(TagPrefix.ingot, GTMaterials.Netherite)
                .EUt(1920)
                .duration(800)
                .blastFurnaceTemp(6470)
                .save(provider);

        GTRecipeTypes.BLAST_RECIPES.recipeBuilder(GTOCore.id("rutherfordium_neutronium_boule"))
                .inputItems(GTItems.NEUTRONIUM_BOULE.asStack())
                .inputItems(TagPrefix.dust, GTMaterials.Rutherfordium, 4)
                .inputFluids(GTMaterials.Radon.getFluid(8000))
                .outputItems(GTOItems.RUTHERFORDIUM_AMPROSIUM_BOULE.asStack())
                .EUt(30720)
                .duration(21000)
                .blastFurnaceTemp(8100)
                .save(provider);

        GTRecipeTypes.BLAST_RECIPES.recipeBuilder(GTOCore.id("taranium_boulea"))
                .inputItems(TagPrefix.block, GTOMaterials.UltraHighPuritySilicon, 64)
                .inputItems(TagPrefix.ingot, GTOMaterials.Taranium, 8)
                .inputItems(TagPrefix.dust, GTMaterials.GalliumArsenide, 4)
                .inputFluids(GTMaterials.Radon.getFluid(16000))
                .outputItems(GTOItems.TARANIUM_BOULE.asStack())
                .EUt(122880)
                .duration(24000)
                .blastFurnaceTemp(10500)
                .save(provider);

        GTRecipeTypes.BLAST_RECIPES.recipeBuilder(GTOCore.id("ostrum_ingot"))
                .inputItems(TagPrefix.dust, GTOMaterials.Ostrum)
                .inputItems(TagPrefix.dust, GTMaterials.TitaniumCarbide)
                .inputFluids(GTMaterials.SamariumIronArsenicOxide.getFluid(144))
                .outputItems(TagPrefix.ingot, GTOMaterials.Ostrum)
                .EUt(1920)
                .duration(2400)
                .blastFurnaceTemp(5200)
                .save(provider);

        GTRecipeTypes.BLAST_RECIPES.recipeBuilder(GTOCore.id("shining_obsidian"))
                .inputItems(TagPrefix.rock, GTMaterials.Obsidian)
                .inputItems(TagPrefix.dust, GTOMaterials.VibrantAlloy)
                .inputFluids(GTMaterials.Glowstone.getFluid(576))
                .outputItems(GTOBlocks.SHINING_OBSIDIAN.asStack())
                .EUt(480)
                .duration(600)
                .blastFurnaceTemp(2600)
                .save(provider);

        GTRecipeTypes.BLAST_RECIPES.recipeBuilder(GTOCore.id("calorite_ingot"))
                .inputItems(TagPrefix.ingot, GTMaterials.Naquadah)
                .inputItems(TagPrefix.dust, GTOMaterials.Calorite)
                .outputItems(TagPrefix.ingot, GTOMaterials.Calorite)
                .EUt(1920)
                .duration(3200)
                .blastFurnaceTemp(6100)
                .save(provider);

        GTRecipeTypes.BLAST_RECIPES.recipeBuilder(GTOCore.id("giga_chad"))
                .inputItems(GTItems.FIELD_GENERATOR_UIV.asStack(64))
                .inputItems(GTItems.FIELD_GENERATOR_UXV.asStack(64))
                .inputItems(GTItems.FIELD_GENERATOR_OpV.asStack(64))
                .inputFluids(GTOMaterials.ExcitedDtec.getFluid(10000000))
                .outputItems(GTOItems.GIGA_CHAD.asStack())
                .EUt(2013265920)
                .duration(4000)
                .blastFurnaceTemp(36000)
                .save(provider);

        GTRecipeTypes.BLAST_RECIPES.recipeBuilder(GTOCore.id("desh_ingot"))
                .inputItems(TagPrefix.dust, GTOMaterials.Desh)
                .inputItems(TagPrefix.dust, GTMaterials.Rhodium)
                .inputFluids(GTMaterials.BismuthBronze.getFluid(144))
                .outputItems(TagPrefix.ingot, GTOMaterials.Desh)
                .EUt(1920)
                .duration(1600)
                .blastFurnaceTemp(4300)
                .save(provider);

        GTRecipeTypes.BLAST_RECIPES.recipeBuilder(GTOCore.id("bedrock_smoke"))
                .inputItems(TagPrefix.dust, GTOMaterials.Bedrockium)
                .inputFluids(GTMaterials.Xenon.getFluid(100))
                .outputFluids(GTOMaterials.BedrockSmoke.getFluid(1000))
                .EUt(7864320)
                .duration(400)
                .blastFurnaceTemp(16200)
                .save(provider);

        GTRecipeTypes.BLAST_RECIPES.recipeBuilder(GTOCore.id("lepton_trap_crystal"))
                .inputItems(TagPrefix.dust, GTMaterials.Meitnerium)
                .inputItems(TagPrefix.dust, GTMaterials.Molybdenum)
                .inputItems(TagPrefix.dust, GTMaterials.Rhenium)
                .inputFluids(GTMaterials.NaquadahAlloy.getFluid(288))
                .outputItems(GTOItems.LEPTON_TRAP_CRYSTAL.asStack())
                .EUt(3450000)
                .duration(340)
                .blastFurnaceTemp(10900)
                .save(provider);

        GCYMRecipeTypes.ALLOY_BLAST_RECIPES.recipeBuilder(GTOCore.id("carbon_disulfide"))
                .circuitMeta(8)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon)
                .inputItems(TagPrefix.dust, GTMaterials.Sulfur, 2)
                .outputFluids(GTOMaterials.CarbonDisulfide.getFluid(1000))
                .EUt(120)
                .duration(350)
                .blastFurnaceTemp(1200)
                .save(provider);

        GTRecipeTypes.ELECTROMAGNETIC_SEPARATOR_RECIPES.recipeBuilder(GTOCore.id("graphene_oxide_dust"))
                .inputItems(GTOItems.GRAPHENE_IRON_PLATE.asStack())
                .outputItems(TagPrefix.dust, GTOMaterials.GrapheneOxide, 3)
                .outputItems(TagPrefix.dust, GTMaterials.Iron)
                .EUt(30)
                .duration(120)
                .save(provider);

        GTRecipeTypes.ELECTROMAGNETIC_SEPARATOR_RECIPES.recipeBuilder(GTOCore.id("raw_tengam_dust"))
                .inputItems(TagPrefix.dustPure, GTOMaterials.Jasper)
                .outputItems(TagPrefix.dust, GTOMaterials.Jasper)
                .chancedOutput(TagPrefix.dust, GTOMaterials.RawTengam, 1000, 0)
                .chancedOutput(TagPrefix.dust, GTOMaterials.RawTengam, 500, 0)
                .EUt(24)
                .duration(200)
                .save(provider);

        GTRecipeTypes.ELECTROMAGNETIC_SEPARATOR_RECIPES.recipeBuilder(GTOCore.id("purified_tengam_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.CleanRawTengam)
                .outputItems(TagPrefix.dust, GTOMaterials.PurifiedTengam)
                .chancedOutput(TagPrefix.dust, GTMaterials.NeodymiumMagnetic, 1000, 0)
                .chancedOutput(TagPrefix.dust, GTMaterials.SamariumMagnetic, 500, 0)
                .EUt(7864320)
                .duration(200)
                .save(provider);

        GTRecipeTypes.EXTRACTOR_RECIPES.recipeBuilder(GTOCore.id("tannic"))
                .inputItems(new ItemStack(Blocks.NETHER_WART_BLOCK.asItem()))
                .outputFluids(GTOMaterials.Tannic.getFluid(50))
                .EUt(30)
                .duration(200)
                .save(provider);

        GTRecipeTypes.EXTRACTOR_RECIPES.recipeBuilder(GTOCore.id("xpjuice"))
                .inputItems(TagPrefix.block, GTMaterials.Sculk)
                .outputFluids(new FluidStack(EIOFluids.XP_JUICE.get().getSource(), 100))
                .EUt(120)
                .duration(20)
                .save(provider);

        GTRecipeTypes.EXTRACTOR_RECIPES.recipeBuilder(GTOCore.id("milk"))
                .inputItems(new ItemStack(Items.MILK_BUCKET.asItem()))
                .outputItems(new ItemStack(Items.BUCKET.asItem()))
                .outputFluids(GTMaterials.Milk.getFluid(1000))
                .EUt(16)
                .duration(60)
                .save(provider);

        GTRecipeTypes.EXTRACTOR_RECIPES.recipeBuilder(GTOCore.id("tcetieseaweedextract"))
                .inputItems(GTOItems.TCETIEDANDELIONS.asStack(64))
                .outputItems(GTOItems.TCETIESEAWEEDEXTRACT.asStack())
                .EUt(16)
                .duration(200)
                .addCondition(new GravityCondition(false))
                .save(provider);

        GTRecipeTypes.EXTRACTOR_RECIPES.recipeBuilder(GTOCore.id("milk1"))
                .notConsumable(new ItemStack(Items.COW_SPAWN_EGG.asItem()))
                .outputFluids(GTMaterials.Milk.getFluid(100))
                .EUt(30)
                .duration(20)
                .save(provider);

        GTRecipeTypes.EXTRACTOR_RECIPES.recipeBuilder(GTOCore.id("bones"))
                .inputItems(new ItemStack(Blocks.DIRT.asItem()))
                .chancedOutput(TagPrefix.rod, GTMaterials.Bone, 25, 0)
                .EUt(16)
                .duration(100)
                .save(provider);

        GTRecipeTypes.EXTRACTOR_RECIPES.recipeBuilder(GTOCore.id("dragon_breath"))
                .inputItems(Ench.Items.INFUSED_BREATH.get(), 3)
                .outputItems(new ItemStack(Items.GLASS_BOTTLE.asItem()))
                .outputFluids(GTOMaterials.DragonBreath.getFluid(1000))
                .EUt(30)
                .duration(200)
                .cleanroom(GTOCleanroomType.LAW_CLEANROOM)
                .save(provider);

        GTRecipeTypes.EXTRUDER_RECIPES.recipeBuilder(GTOCore.id("special_ceramics"))
                .inputItems(TagPrefix.dust, GTOMaterials.SpecialCeramics, 2)
                .notConsumable(GTItems.SHAPE_EXTRUDER_PLATE.asStack())
                .outputItems(GTOItems.SPECIAL_CERAMICS.asStack())
                .EUt(480)
                .duration(20)
                .save(provider);

        GTRecipeTypes.FERMENTING_RECIPES.recipeBuilder(GTOCore.id("taranium_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.ActivatedCarbon)
                .inputFluids(GTOMaterials.TaraniumRichLiquidHelium4.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.Taranium)
                .EUt(2)
                .duration(200000)
                .save(provider);

        GTRecipeTypes.FERMENTING_RECIPES.recipeBuilder(GTOCore.id("poisonous_potato"))
                .inputItems(new ItemStack(Blocks.POTATOES.asItem()))
                .outputItems(new ItemStack(Items.POISONOUS_POTATO.asItem()))
                .EUt(30)
                .duration(400)
                .save(provider);

        GTRecipeTypes.FERMENTING_RECIPES.recipeBuilder(GTOCore.id("rotten_flesh"))
                .inputItems(TagPrefix.dust, GTMaterials.Meat)
                .outputItems(new ItemStack(Items.ROTTEN_FLESH.asItem()))
                .EUt(30)
                .duration(400)
                .save(provider);

        GTRecipeTypes.FLUID_HEATER_RECIPES.recipeBuilder(GTOCore.id("supercritical_carbon_dioxide"))
                .inputFluids(GTMaterials.CarbonDioxide.getFluid(1000))
                .outputFluids(GTOMaterials.SupercriticalCarbonDioxide.getFluid(1000))
                .EUt(480)
                .duration(200)
                .save(provider);

        GTRecipeTypes.FLUID_HEATER_RECIPES.recipeBuilder(GTOCore.id("azafullerene"))
                .notConsumable(TagPrefix.dustTiny, GTMaterials.Rhenium, 36)
                .inputFluids(GTOMaterials.AminatedFullerene.getFluid(100))
                .outputFluids(GTOMaterials.Azafullerene.getFluid(100))
                .EUt(480)
                .duration(120)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        GTRecipeTypes.FLUID_HEATER_RECIPES.recipeBuilder(GTOCore.id("biohmediumsterilized"))
                .inputFluids(GTOMaterials.BiomediumRaw.getFluid(100))
                .outputFluids(GTOMaterials.BiohmediumSterilized.getFluid(100))
                .EUt(480)
                .duration(400)
                .cleanroom(CleanroomType.STERILE_CLEANROOM)
                .save(provider);

        GTRecipeTypes.FLUID_HEATER_RECIPES.recipeBuilder(GTOCore.id("bedrock_gas"))
                .inputFluids(GTOMaterials.CleanBedrockSolution.getFluid(1000))
                .outputFluids(GTOMaterials.BedrockGas.getFluid(1000))
                .EUt(31457280)
                .duration(100)
                .save(provider);

        GTORecipeTypes.LOOM_RECIPES.recipeBuilder(GTOCore.id("string"))
                .inputItems(GTOItems.PLANT_FIBER.asStack(4))
                .outputItems(new ItemStack(Blocks.TRIPWIRE.asItem()))
                .EUt(1920)
                .duration(20)
                .save(provider);

        GTORecipeTypes.LOOM_RECIPES.recipeBuilder(GTOCore.id("plant_fiber"))
                .inputItems(GTItems.PLANT_BALL.asStack())
                .outputItems(GTOItems.PLANT_FIBER.asStack(2))
                .EUt(7)
                .duration(200)
                .save(provider);

        GTRecipeTypes.LARGE_BOILER_RECIPES.recipeBuilder(GTOCore.id("tin_bucket"))
                .inputItems(TagPrefix.ingot, GTMaterials.Tin)
                .inputFluids(GTMaterials.Lava.getFluid(100))
                .outputFluids(GTMaterials.Tin.getFluid(72))
                .duration(5)
                .save(provider);

        GTRecipeTypes.GAS_COLLECTOR_RECIPES.recipeBuilder(GTOCore.id("barnarda_c"))
                .circuitMeta(6)
                .outputFluids(GTOMaterials.BarnardaAir.getFluid(10000))
                .EUt(1024)
                .duration(200)
                .dimension(GTOWorldGenLayers.BARNARDA_C)
                .save(provider);

        GTRecipeTypes.GAS_COLLECTOR_RECIPES.recipeBuilder(GTOCore.id("flat"))
                .circuitMeta(5)
                .outputFluids(GTMaterials.Air.getFluid(10000))
                .EUt(16)
                .duration(200)
                .dimension(GTOWorldGenLayers.FLAT)
                .save(provider);

        GTRecipeTypes.GAS_COLLECTOR_RECIPES.recipeBuilder(GTOCore.id("void"))
                .circuitMeta(4)
                .outputFluids(GTMaterials.Air.getFluid(10000))
                .EUt(16)
                .duration(200)
                .dimension(GTOWorldGenLayers.VOID)
                .save(provider);

        GTORecipeTypes.WORLD_DATA_SCANNER_RECIPES.recipeBuilder(GTOCore.id("end_data"))
                .inputItems(GTItems.TOOL_DATA_STICK.asStack())
                .inputItems(TagPrefix.dust, GTMaterials.Endstone, 64)
                .inputFluids(GTMaterials.PCBCoolant.getFluid(400))
                .inputFluids(GTMaterials.EnderAir.getFluid(64000))
                .outputItems(GTOItems.DIMENSION_DATA.get().getDimensionData(GTOWorldGenLayers.THE_END))
                .EUt(2048)
                .duration(4000)
                .dimension(GTOWorldGenLayers.PLUTO)
                .save(provider);

        GTORecipeTypes.WORLD_DATA_SCANNER_RECIPES.recipeBuilder(GTOCore.id("nether_data"))
                .inputItems(GTItems.TOOL_DATA_STICK.asStack())
                .inputItems(TagPrefix.dust, GTMaterials.Netherrack, 64)
                .inputFluids(GTMaterials.PCBCoolant.getFluid(200))
                .inputFluids(GTMaterials.NetherAir.getFluid(64000))
                .outputItems(GTOItems.DIMENSION_DATA.get().getDimensionData(GTOWorldGenLayers.THE_NETHER))
                .EUt(512)
                .duration(4000)
                .dimension(GTOWorldGenLayers.VENUS)
                .save(provider);

        GTORecipeTypes.WORLD_DATA_SCANNER_RECIPES.recipeBuilder(GTOCore.id("overworld_data"))
                .inputItems(GTItems.TOOL_DATA_STICK.asStack())
                .inputItems(TagPrefix.dust, GTMaterials.Stone, 64)
                .inputFluids(GTMaterials.PCBCoolant.getFluid(100))
                .inputFluids(GTMaterials.Air.getFluid(64000))
                .outputItems(GTOItems.DIMENSION_DATA.get().getDimensionData(GTOWorldGenLayers.OVERWORLD))
                .EUt(128)
                .duration(4000)
                .dimension(GTOWorldGenLayers.OVERWORLD)
                .save(provider);

        GTRecipeTypes.BREWING_RECIPES.recipeBuilder(GTOCore.id("dragon_blood"))
                .inputItems(GTOItems.DRAGON_CELLS.asStack())
                .inputFluids(GTMaterials.SterileGrowthMedium.getFluid(1000))
                .outputFluids(GTOMaterials.DragonBlood.getFluid(1000))
                .EUt(480)
                .duration(6000)
                .cleanroom(GTOCleanroomType.LAW_CLEANROOM)
                .save(provider);

        GTORecipeTypes.WEATHER_CONTROL_RECIPES.recipeBuilder(GTOCore.id("3"))
                .circuitMeta(3)
                .EUt(30)
                .duration(200)
                .save(provider);

        GTORecipeTypes.WEATHER_CONTROL_RECIPES.recipeBuilder(GTOCore.id("1"))
                .circuitMeta(1)
                .EUt(30)
                .duration(200)
                .save(provider);

        GTORecipeTypes.WEATHER_CONTROL_RECIPES.recipeBuilder(GTOCore.id("2"))
                .circuitMeta(2)
                .EUt(30)
                .duration(200)
                .save(provider);

        GTORecipeTypes.CREATE_AGGREGATION_RECIPES.recipeBuilder(GTOCore.id("1"))
                .circuitMeta(1)
                .EUt(32212254720L)
                .duration(20)
                .dimension(GTOWorldGenLayers.CREATE)
                .save(provider);

        GTORecipeTypes.DOOR_OF_CREATE_RECIPES.recipeBuilder(GTOCore.id("1"))
                .circuitMeta(1)
                .EUt(8053063680L)
                .duration(20)
                .dimension(GTOWorldGenLayers.OVERWORLD)
                .save(provider);

        GTORecipeTypes.HEAT_EXCHANGER_RECIPES.recipeBuilder(GTOCore.id("hot_sodium_potassium"))
                .inputFluids(GTOMaterials.HotSodiumPotassium.getFluid(1))
                .inputFluids(GTMaterials.Water.getFluid(160))
                .outputFluids(GTMaterials.SodiumPotassium.getFluid(1))
                .outputFluids(GTMaterials.Steam.getFluid(25600))
                .duration(200)
                .save(provider);

        GTORecipeTypes.HEAT_EXCHANGER_RECIPES.recipeBuilder(GTOCore.id("supercritical_sodium_potassium"))
                .inputFluids(GTOMaterials.SupercriticalSodiumPotassium.getFluid(1))
                .inputFluids(GTMaterials.DistilledWater.getFluid(80))
                .outputFluids(GTMaterials.SodiumPotassium.getFluid(1))
                .outputFluids(GTMaterials.Steam.getFluid(12800))
                .outputFluids(GTOMaterials.SupercriticalSteam.getFluid(800))
                .duration(200)
                .save(provider);

        GTORecipeTypes.DRAGON_EGG_COPIER_RECIPES.recipeBuilder(GTOCore.id("dragon_egg_copier"))
                .inputItems(new ItemStack(Blocks.DRAGON_EGG.asItem()))
                .inputFluids(GTOMaterials.BiohmediumSterilized.getFluid(100))
                .outputItems(new ItemStack(Blocks.DRAGON_EGG.asItem()))
                .chancedOutput(new ItemStack(Blocks.DRAGON_EGG.asItem()), 2000, 1000)
                .EUt(122880)
                .duration(200)
                .cleanroom(GTOCleanroomType.LAW_CLEANROOM)
                .save(provider);

        GTORecipeTypes.LAVA_FURNACE_RECIPES.recipeBuilder(GTOCore.id("lava_furnace"))
                .inputItems(new ItemStack(Blocks.COBBLESTONE.asItem()))
                .outputFluids(GTMaterials.Lava.getFluid(1000))
                .EUt(16)
                .duration(200)
                .save(provider);

        GTORecipeTypes.LAVA_FURNACE_RECIPES.recipeBuilder(GTOCore.id("lava_furnace1"))
                .inputItems(new ItemStack(Blocks.ANDESITE.asItem()))
                .outputFluids(GTMaterials.Lava.getFluid(1000))
                .EUt(16)
                .duration(200)
                .save(provider);

        GTORecipeTypes.SPACE_PROBE_SURFACE_RECEPTION_RECIPES.recipeBuilder(GTOCore.id("heavy_lepton_mixture1"))
                .notConsumable(GTOItems.SPACE_PROBE_MK1.asStack())
                .circuitMeta(1)
                .outputFluids(GTOMaterials.HeavyLeptonMixture.getFluid(100))
                .EUt(31457280)
                .duration(200)
                .save(provider);

        GTORecipeTypes.SPACE_PROBE_SURFACE_RECEPTION_RECIPES.recipeBuilder(GTOCore.id("heavy_lepton_mixture2"))
                .notConsumable(GTOItems.SPACE_PROBE_MK2.asStack())
                .circuitMeta(1)
                .outputFluids(GTOMaterials.HeavyLeptonMixture.getFluid(1000))
                .EUt(125829120)
                .duration(200)
                .save(provider);

        GTORecipeTypes.SPACE_PROBE_SURFACE_RECEPTION_RECIPES.recipeBuilder(GTOCore.id("heavy_lepton_mixture3"))
                .notConsumable(GTOItems.SPACE_PROBE_MK3.asStack())
                .circuitMeta(1)
                .outputFluids(GTOMaterials.HeavyLeptonMixture.getFluid(10000))
                .EUt(503316480)
                .duration(200)
                .save(provider);

        GTORecipeTypes.SPACE_PROBE_SURFACE_RECEPTION_RECIPES.recipeBuilder(GTOCore.id("cosmic_element3"))
                .notConsumable(GTOItems.SPACE_PROBE_MK3.asStack())
                .circuitMeta(3)
                .outputFluids(GTOMaterials.CosmicElement.getFluid(10000))
                .EUt(503316480)
                .duration(200)
                .save(provider);

        GTORecipeTypes.SPACE_PROBE_SURFACE_RECEPTION_RECIPES.recipeBuilder(GTOCore.id("starlight3"))
                .notConsumable(GTOItems.SPACE_PROBE_MK3.asStack())
                .circuitMeta(2)
                .outputFluids(GTOMaterials.Starlight.getFluid(10000))
                .EUt(503316480)
                .duration(200)
                .save(provider);

        GTORecipeTypes.SPACE_PROBE_SURFACE_RECEPTION_RECIPES.recipeBuilder(GTOCore.id("starlight2"))
                .notConsumable(GTOItems.SPACE_PROBE_MK2.asStack())
                .circuitMeta(2)
                .outputFluids(GTOMaterials.Starlight.getFluid(1000))
                .EUt(125829120)
                .duration(200)
                .save(provider);

        GTORecipeTypes.COSMOS_SIMULATION_RECIPES.recipeBuilder(GTOCore.id("cosmos_simulation1"))
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asStack())
                .inputFluids(GTOMaterials.CosmicElement.getFluid(1024000))
                .outputItems(TagPrefix.dust, GTMaterials.Carbon, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Phosphorus, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Sulfur, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Selenium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Iodine, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Boron, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Silicon, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Germanium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Arsenic, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Antimony, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Tellurium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Astatine, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Aluminium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Gallium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Indium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Tin, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Thallium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Lead, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Bismuth, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Polonium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Titanium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Vanadium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Chromium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Manganese, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Iron, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Cobalt, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Nickel, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Copper, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Zinc, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Zirconium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Niobium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Molybdenum, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Technetium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Ruthenium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Rhodium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Palladium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Silver, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Cadmium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Hafnium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Tantalum, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Tungsten, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Rhenium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Osmium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Iridium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Platinum, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Gold, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Beryllium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Magnesium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Calcium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Strontium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Barium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Radium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Yttrium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Lithium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Sodium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Potassium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Rubidium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Caesium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Francium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Scandium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Actinium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Thorium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Protactinium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Uranium238, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Neptunium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Plutonium239, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Americium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Curium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Berkelium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Californium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Einsteinium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Fermium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Mendelevium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Nobelium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Lawrencium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Lanthanum, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Cerium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Praseodymium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Neodymium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Promethium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Samarium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Europium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Gadolinium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Terbium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Dysprosium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Holmium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Erbium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Thulium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Ytterbium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Lutetium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Rutherfordium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Dubnium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Seaborgium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Bohrium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Hassium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Meitnerium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Darmstadtium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Roentgenium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Copernicium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Nihonium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Flerovium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Moscovium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Livermorium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Tennessine, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Oganesson, 131072)
                .outputItems(TagPrefix.dust, GTOMaterials.Jasper, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Naquadah, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.NaquadahEnriched, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Naquadria, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Duranium, 131072)
                .outputItems(TagPrefix.dust, GTMaterials.Tritanium, 131072)
                .outputItems(TagPrefix.dust, GTOMaterials.Mithril, 131072)
                .outputItems(TagPrefix.dust, GTOMaterials.Orichalcum, 131072)
                .outputItems(TagPrefix.dust, GTOMaterials.Enderium, 131072)
                .outputItems(TagPrefix.dust, GTOMaterials.Adamantine, 131072)
                .outputItems(TagPrefix.dust, GTOMaterials.Vibranium, 131072)
                .outputItems(TagPrefix.dust, GTOMaterials.Infuscolium, 131072)
                .outputItems(TagPrefix.dust, GTOMaterials.Taranium, 131072)
                .outputItems(TagPrefix.dust, GTOMaterials.Draconium, 131072)
                .outputItems(TagPrefix.dust, GTOMaterials.Starmetal, 131072)
                .outputFluids(GTOMaterials.SpaceTime.getFluid(256))
                .outputFluids(GTOMaterials.RawStarMatter.getFluid(FluidStorageKeys.PLASMA, 1310720))
                .outputFluids(GTOMaterials.QuarkGluon.getFluid(FluidStorageKeys.PLASMA, 1310720))
                .outputFluids(GTOMaterials.HeavyQuarkDegenerateMatter.getFluid(FluidStorageKeys.PLASMA, 1310720))
                .outputFluids(GTMaterials.Neutronium.getFluid(13107200))
                .outputFluids(GTOMaterials.HeavyLeptonMixture.getFluid(13107200))
                .outputFluids(GTMaterials.Hydrogen.getFluid(131072000))
                .outputFluids(GTMaterials.Nitrogen.getFluid(131072000))
                .outputFluids(GTMaterials.Oxygen.getFluid(131072000))
                .outputFluids(GTMaterials.Fluorine.getFluid(131072000))
                .outputFluids(GTMaterials.Chlorine.getFluid(131072000))
                .outputFluids(GTMaterials.Bromine.getFluid(131072000))
                .outputFluids(GTMaterials.Helium.getFluid(131072000))
                .outputFluids(GTMaterials.Neon.getFluid(131072000))
                .outputFluids(GTMaterials.Argon.getFluid(131072000))
                .outputFluids(GTMaterials.Krypton.getFluid(131072000))
                .outputFluids(GTMaterials.Xenon.getFluid(131072000))
                .outputFluids(GTMaterials.Radon.getFluid(131072000))
                .outputFluids(GTMaterials.Mercury.getFluid(131072000))
                .outputFluids(GTMaterials.Deuterium.getFluid(131072000))
                .outputFluids(GTMaterials.Tritium.getFluid(131072000))
                .outputFluids(GTMaterials.Helium3.getFluid(131072000))
                .outputFluids(GTOMaterials.UnknowWater.getFluid(131072000))
                .outputFluids(GTMaterials.UUMatter.getFluid(131072000))
                .duration(1200)
                .save(provider);

        GTORecipeTypes.ANNIHILATE_GENERATOR_RECIPES.recipeBuilder(GTOCore.id("neutronium_antimatter_fuel_rod"))
                .inputItems(GTOItems.NEUTRONIUM_ANTIMATTER_FUEL_ROD.asStack())
                .chancedOutput(GTOItems.ANNIHILATION_CONSTRAINER.asStack(), 9000, 0)
                .EUt(-549755813888L)
                .duration(200)
                .save(provider);

        GTORecipeTypes.ANNIHILATE_GENERATOR_RECIPES.recipeBuilder(GTOCore.id("draconium_antimatter_fuel_rod"))
                .inputItems(GTOItems.DRACONIUM_ANTIMATTER_FUEL_ROD.asStack())
                .chancedOutput(GTOItems.ANNIHILATION_CONSTRAINER.asStack(), 8000, 0)
                .EUt(-8796093022208L)
                .duration(200)
                .save(provider);

        GTORecipeTypes.ANNIHILATE_GENERATOR_RECIPES.recipeBuilder(GTOCore.id("cosmic_neutronium_antimatter_fuel_rod"))
                .inputItems(GTOItems.COSMIC_NEUTRONIUM_ANTIMATTER_FUEL_ROD.asStack())
                .chancedOutput(GTOItems.ANNIHILATION_CONSTRAINER.asStack(), 7000, 0)
                .EUt(-140737488355328L)
                .duration(200)
                .save(provider);

        GTORecipeTypes.ANNIHILATE_GENERATOR_RECIPES.recipeBuilder(GTOCore.id("infinity_antimatter_fuel_rod"))
                .inputItems(GTOItems.INFINITY_ANTIMATTER_FUEL_ROD.asStack())
                .chancedOutput(GTOItems.ANNIHILATION_CONSTRAINER.asStack(), 6000, 0)
                .EUt(-2251799813685248L)
                .duration(200)
                .save(provider);

        GTORecipeTypes.MASS_FABRICATOR_RECIPES.recipeBuilder(GTOCore.id("uu_matter"))
                .inputItems(new ItemStack(AEItems.MATTER_BALL.asItem()))
                .inputFluids(GTOMaterials.UuAmplifier.getFluid(10))
                .outputFluids(GTMaterials.UUMatter.getFluid(10))
                .EUt(31457280)
                .duration(20)
                .save(provider);

        GTORecipeTypes.MASS_FABRICATOR_RECIPES.recipeBuilder(GTOCore.id("quasifissioning_plasma"))
                .inputItems(TagPrefix.ingot, GTMaterials.Uranium238)
                .inputFluids(GTMaterials.Uranium238.getFluid(144))
                .outputFluids(GTOMaterials.Quasifissioning.getFluid(FluidStorageKeys.PLASMA, 144))
                .EUt(7864320)
                .duration(200)
                .save(provider);

        GTORecipeTypes.MATTER_FABRICATOR_RECIPES.recipeBuilder(GTOCore.id("uu_amplifier"))
                .inputItems(GTOItems.SCRAP.asStack())
                .circuitMeta(1)
                .outputFluids(GTOMaterials.UuAmplifier.getFluid(1))
                .EUt(491520)
                .duration(200)
                .save(provider);

        GTORecipeTypes.MATTER_FABRICATOR_RECIPES.recipeBuilder(GTOCore.id("uu_amplifier_1"))
                .inputItems(GTOItems.SCRAP.asStack())
                .circuitMeta(2)
                .outputItems(new ItemStack(AEItems.MATTER_BALL.asItem(), 64))
                .EUt(491520)
                .duration(1)
                .save(provider);

        GTORecipeTypes.MATTER_FABRICATOR_RECIPES.recipeBuilder(GTOCore.id("uu_amplifier_2"))
                .inputItems(GTOItems.SCRAP_BOX.asStack())
                .circuitMeta(2)
                .outputItems(new ItemStack(AEItems.MATTER_BALL.asItem(), 576))
                .EUt(1966080)
                .duration(1)
                .save(provider);

        GTORecipeTypes.MATTER_FABRICATOR_RECIPES.recipeBuilder(GTOCore.id("uu_amplifier_a"))
                .inputItems(GTOItems.SCRAP_BOX.asStack())
                .circuitMeta(1)
                .outputFluids(GTOMaterials.UuAmplifier.getFluid(9))
                .EUt(1966080)
                .duration(200)
                .save(provider);

        GTORecipeTypes.RECYCLER_RECIPES.recipeBuilder(GTOCore.id("recycler_a"))
                .inputItems(TagUtils.createTGTag("ingots"))
                .outputItems(GTOItems.SCRAP.asStack())
                .EUt(30)
                .duration(200)
                .save(provider);

        GTORecipeTypes.RECYCLER_RECIPES.recipeBuilder(GTOCore.id("recycler_b"))
                .inputItems(TagUtils.createTGTag("storage_blocks"))
                .outputItems(GTOItems.SCRAP.asStack(9))
                .EUt(120)
                .duration(200)
                .save(provider);

        GTORecipeTypes.RECYCLER_RECIPES.recipeBuilder(GTOCore.id("recycler_c"))
                .inputItems(TagUtils.createTGTag("gems"))
                .outputItems(GTOItems.SCRAP.asStack())
                .EUt(30)
                .duration(200)
                .save(provider);

        GTRecipeTypes.SCANNER_RECIPES.recipeBuilder(GTOCore.id("nether_reactor_core"))
                .notConsumable(GTOItems.DIMENSION_DATA.get().getDimensionData(GTOWorldGenLayers.THE_NETHER))
                .inputItems(MultiBlockG.ANCIENT_REACTOR_CORE.asStack())
                .inputFluids(GTMaterials.Wax.getFluid(2304))
                .outputItems(MultiBlockG.NETHER_REACTOR_CORE.asStack())
                .EUt(480)
                .duration(1200)
                .save(provider);
    }
}
