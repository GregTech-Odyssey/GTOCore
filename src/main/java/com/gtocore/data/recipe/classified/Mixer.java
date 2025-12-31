package com.gtocore.data.recipe.classified;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOFluidStorageKey;
import com.gtocore.common.data.GTOFluids;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.recipe.condition.GravityCondition;

import com.gtolib.api.machine.GTOCleanroomType;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fluids.FluidStack;

import earth.terrarium.adastra.common.registry.ModFluids;

import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.MIXER_RECIPES;

final class Mixer {

    public static void init() {
        MIXER_RECIPES.builder("mica_based_pulp")
                .inputItems(TagPrefix.dust, GTMaterials.Mica, 3)
                .inputItems(TagPrefix.dust, GTMaterials.RawRubber, 2)
                .outputItems(GTOItems.MICA_BASED_PULP, 5)
                .EUt(8)
                .duration(300)
                .save();

        MIXER_RECIPES.recipeBuilder("absolute_ethanol")
                .inputFluids(Ethanol, 1000)
                .inputItems(TagPrefix.dust, ZeoliteSievingPellets)
                .outputFluids(AbsoluteEthanol.getFluid(1000))
                .outputItems(TagPrefix.dust, WetZeoliteSievingPellets)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(100).EUt(120).save();

        MIXER_RECIPES.recipeBuilder("piranha_solution")
                .inputFluids(HydrogenPeroxide, 1000)
                .inputFluids(SulfuricAcid, 1000)
                .outputFluids(PiranhaSolution.getFluid(2000))
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(50).EUt(30).save();

        MIXER_RECIPES.recipeBuilder("potassium_pyrosulfate_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Potassium, 2)
                .inputItems(TagPrefix.dust, GTMaterials.Sulfur, 2)
                .inputFluids(GTMaterials.Oxygen, 7000)
                .outputItems(TagPrefix.dust, GTOMaterials.PotassiumPyrosulfate, 11)
                .EUt(120)
                .duration(120)
                .save();

        MIXER_RECIPES.recipeBuilder("actinoids_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.ActinoidsMix1)
                .inputItems(TagPrefix.dust, GTOMaterials.ActinoidsMix2)
                .outputItems(TagPrefix.dust, GTOMaterials.ActinoidsMix, 2)
                .EUt(31457280)
                .duration(400)
                .save();

        MIXER_RECIPES.recipeBuilder("znfealcl_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Zinc)
                .inputItems(TagPrefix.dust, GTMaterials.Iron)
                .inputItems(TagPrefix.dust, GTMaterials.Aluminium)
                .inputFluids(GTMaterials.Chlorine, 1000)
                .outputItems(TagPrefix.dust, GTOMaterials.ZnFeAlClCatalyst, 4)
                .EUt(15360)
                .duration(250)
                .save();

        MIXER_RECIPES.recipeBuilder("concentration_mixing_hyper_fuel_1")
                .inputItems(TagPrefix.dust, GTMaterials.Hassium)
                .inputItems(TagPrefix.dust, GTMaterials.Oganesson)
                .inputFluids(GTOMaterials.HyperFuel4, 1000)
                .inputFluids(GTOMaterials.DimensionallyTranscendentCrudeCatalyst, 1000)
                .outputFluids(GTOMaterials.ConcentrationMixingHyperFuel1, 1000)
                .EUt(134217728)
                .duration(800)
                .save();

        MIXER_RECIPES.recipeBuilder("sunnarium")
                .notConsumable(GTItems.FIELD_GENERATOR_UIV.asItem())
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(new FluidStack(GTOFluids.LIQUID_SUNSHINE.getSource(), 1000))
                .outputFluids(GTOMaterials.Sunnarium, 1000)
                .EUt(125829120)
                .duration(400)
                .save();

        MIXER_RECIPES.recipeBuilder("iridium_trichloride_solution")
                .inputItems(TagPrefix.dust, GTMaterials.IridiumChloride, 4)
                .inputFluids(GTMaterials.HypochlorousAcid, 1000)
                .inputFluids(GTMaterials.NitricAcid, 1000)
                .outputFluids(GTOMaterials.IridiumTrichlorideSolution, 1000)
                .EUt(1920)
                .duration(360)
                .save();

        MIXER_RECIPES.recipeBuilder("magneto_resonatic_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.Prasiolite, 3)
                .inputItems(TagPrefix.dust, GTOMaterials.BismuthTellurite, 6)
                .inputItems(TagPrefix.dust, GTOMaterials.CubicZirconia)
                .inputItems(TagPrefix.dust, GTMaterials.SteelMagnetic)
                .outputItems(TagPrefix.dust, GTOMaterials.MagnetoResonatic, 9)
                .EUt(30)
                .duration(80)
                .addCondition(new GravityCondition(true))
                .save();

        MIXER_RECIPES.recipeBuilder("mixed_astatide_salts_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Holmium)
                .inputItems(TagPrefix.dust, GTMaterials.Thulium)
                .inputItems(TagPrefix.dust, GTMaterials.Copernicium)
                .inputItems(TagPrefix.dust, GTMaterials.Flerovium)
                .inputFluids(GTOMaterials.AstatideSolution, 3000)
                .inputFluids(GTMaterials.DistilledWater, 3000)
                .outputItems(TagPrefix.dust, GTOMaterials.MixedAstatideSalts, 7)
                .outputFluids(GTMaterials.DilutedSulfuricAcid, 6000)
                .EUt(122880)
                .duration(400)
                .save();

        MIXER_RECIPES.recipeBuilder("redstone_alloy_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Redstone)
                .inputItems(TagPrefix.dust, GTMaterials.Silicon)
                .circuitMeta(2)
                .outputItems(TagPrefix.dust, GTOMaterials.RedstoneAlloy, 2)
                .EUt(30)
                .duration(320)
                .save();

        MIXER_RECIPES.recipeBuilder("rocket_fuel_h8n4c2o4")
                .inputFluids(GTMaterials.Dimethylhydrazine, 1000)
                .inputFluids(GTMaterials.DinitrogenTetroxide, 1000)
                .outputFluids(GTOMaterials.RocketFuelH8n4c2o4, 1000)
                .EUt(1920)
                .duration(480)
                .save();

        MIXER_RECIPES.recipeBuilder("circuit_compound_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.DibismuthHydroborate, 3)
                .inputItems(TagPrefix.dust, GTOMaterials.BismuthTellurite, 2)
                .inputItems(TagPrefix.dust, GTMaterials.IndiumGalliumPhosphide)
                .outputItems(TagPrefix.dust, GTOMaterials.CircuitCompound, 6)
                .EUt(15)
                .duration(890)
                .save();

        MIXER_RECIPES.recipeBuilder("viscoelastic_polyurethane")
                .inputItems(TagPrefix.dust, GTMaterials.Calcite, 5)
                .inputFluids(GTOMaterials.Polyurethane, 1000)
                .inputFluids(GTOMaterials.EthyleneGlycol, 1000)
                .outputFluids(GTOMaterials.ViscoelasticPolyurethane, 2000)
                .EUt(120)
                .duration(110)
                .save();

        MIXER_RECIPES.recipeBuilder("astatide_solution")
                .inputItems(TagPrefix.dust, GTMaterials.Astatine)
                .inputFluids(GTMaterials.SulfuricAcid, 1000)
                .outputFluids(GTOMaterials.AstatideSolution, 1000)
                .EUt(1920)
                .duration(200)
                .save();

        MIXER_RECIPES.recipeBuilder("silica_alumina_gel_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.Alumina, 5)
                .inputItems(TagPrefix.dust, GTOMaterials.SilicaGel, 3)
                .outputItems(TagPrefix.dust, GTOMaterials.SilicaAluminaGel)
                .EUt(120)
                .duration(60)
                .save();

        MIXER_RECIPES.recipeBuilder("high_energy_mixture_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Glowstone, 4)
                .inputItems(TagPrefix.dust, GTMaterials.Redstone, 2)
                .inputItems(TagPrefix.dust, GTMaterials.Aluminium)
                .circuitMeta(3)
                .outputItems(TagPrefix.dust, GTOMaterials.HighEnergyMixture, 4)
                .EUt(480)
                .duration(600)
                .save();

        MIXER_RECIPES.recipeBuilder("dimensionallytranscendentcrudecatalyst")
                .inputItems(GTOItems.RESONATING_GEM)
                .inputFluids(GTMaterials.Nitrogen.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputFluids(GTOMaterials.DimensionallyTranscendentCrudeCatalyst, 1000)
                .EUt(503316480)
                .duration(200)
                .save();

        MIXER_RECIPES.recipeBuilder("sodium_nitrate_solution")
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumNitrate, 5)
                .inputFluids(GTMaterials.Water, 1000)
                .outputFluids(GTOMaterials.SodiumNitrateSolution, 1000)
                .EUt(120)
                .duration(80)
                .save();

        MIXER_RECIPES.recipeBuilder("fertilizer_")
                .inputItems(GTItems.FERTILIZER)
                .inputItems(GTOItems.SCRAP, 2)
                .outputItems(GTItems.FERTILIZER, 2)
                .EUt(480)
                .duration(40)
                .save();

        MIXER_RECIPES.recipeBuilder("copper_alloy_ingot_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Copper)
                .inputItems(TagPrefix.dust, GTMaterials.Silicon)
                .circuitMeta(2)
                .outputItems(TagPrefix.dust, GTOMaterials.CopperAlloy, 2)
                .EUt(30)
                .duration(120)
                .save();

        MIXER_RECIPES.recipeBuilder("molten_calcium_salts")
                .inputItems(TagPrefix.dust, GTMaterials.Calcium)
                .inputFluids(GTOMaterials.Fluorite, 432)
                .outputFluids(GTOMaterials.MoltenCalciumSalts, 1000)
                .EUt(30)
                .duration(160)
                .save();

        MIXER_RECIPES.recipeBuilder("glucose_iron_solution")
                .inputItems(TagPrefix.dust, GTOMaterials.Glucose, 24)
                .inputFluids(GTMaterials.Iron3Chloride, 1000)
                .outputFluids(GTOMaterials.GlucoseIronSolution, 1000)
                .EUt(30)
                .duration(80)
                .save();

        MIXER_RECIPES.recipeBuilder("sodium_ethylate_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Sodium)
                .inputFluids(GTMaterials.Ethanol, 1000)
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumEthylate, 9)
                .outputFluids(GTMaterials.Hydrogen, 1000)
                .EUt(120)
                .duration(100)
                .save();

        MIXER_RECIPES.recipeBuilder("essence_seed")
                .inputItems(Blocks.BEETROOTS.asItem(), 16)
                .inputItems(GTOItems.ESSENCE)
                .inputFluids(GTMaterials.DistilledWater, 1000)
                .inputFluids(GTMaterials.CarbonDioxide, 1000)
                .outputItems(GTOItems.ESSENCE_SEED, 16)
                .EUt(120)
                .duration(400)
                .save();

        MIXER_RECIPES.recipeBuilder("silica_gel_base")
                .inputItems(TagPrefix.dust, GTMaterials.SiliconDioxide, 3)
                .inputItems(TagPrefix.dust, GTMaterials.SodiumHydroxide, 3)
                .inputFluids(GTMaterials.DistilledWater, 1000)
                .outputFluids(GTOMaterials.SilicaGelBase, 1000)
                .EUt(120)
                .duration(80)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        MIXER_RECIPES.recipeBuilder("enriched_xenoxene")
                .inputItems(TagPrefix.dust, GTOMaterials.Taranium)
                .inputFluids(GTOMaterials.PurifiedXenoxene, 10000)
                .inputFluids(GTOMaterials.RadoxGas, 100)
                .outputFluids(GTOMaterials.EnrichedXenoxene, 10000)
                .EUt(491520)
                .duration(600)
                .save();

        MIXER_RECIPES.recipeBuilder("kelp_slurry")
                .inputItems(Items.DRIED_KELP.asItem(), 8)
                .inputFluids(GTMaterials.Water, 1000)
                .outputFluids(GTOMaterials.KelpSlurry, 1000)
                .EUt(30)
                .duration(600)
                .save();

        MIXER_RECIPES.recipeBuilder("rocket_fuel_cn3h7o3")
                .inputFluids(GTOMaterials.Monomethylhydrazine, 1000)
                .inputFluids(GTMaterials.NitricAcid, 1000)
                .outputFluids(GTOMaterials.RocketFuelCn3h7o3, 1000)
                .EUt(1920)
                .duration(200)
                .save();

        MIXER_RECIPES.recipeBuilder("conductive_alloy_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.CopperAlloy)
                .inputItems(TagPrefix.dust, GTMaterials.Iron)
                .inputItems(TagPrefix.dust, GTMaterials.Redstone)
                .circuitMeta(3)
                .outputItems(TagPrefix.dust, GTOMaterials.ConductiveAlloy, 3)
                .EUt(30)
                .duration(240)
                .save();

        MIXER_RECIPES.recipeBuilder("tin_alloy")
                .inputItems(TagPrefix.dust, GTMaterials.Iron)
                .inputFluids(GTMaterials.Tin, 144)
                .outputFluids(GTMaterials.TinAlloy, 288)
                .EUt(30)
                .duration(200)
                .heat(800)
                .save();

        MIXER_RECIPES.recipeBuilder("pulsating_alloy_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Iron)
                .inputItems(TagPrefix.dust, GTMaterials.EnderPearl)
                .circuitMeta(2)
                .outputItems(TagPrefix.dust, GTOMaterials.PulsatingAlloy, 2)
                .EUt(30)
                .duration(160)
                .save();

        MIXER_RECIPES.recipeBuilder("rooted_dirt")
                .chancedInput(new ItemStack(Blocks.MOSS_CARPET.asItem()), 1000, 0)
                .inputItems(Blocks.DIRT.asItem())
                .outputItems(Blocks.ROOTED_DIRT.asItem())
                .EUt(16)
                .duration(200)
                .save();

        MIXER_RECIPES.recipeBuilder("gamma_rays_photoresist")
                .inputItems(TagPrefix.dust, GTOMaterials.Borocarbide, 29)
                .inputItems(TagPrefix.dust, GTOMaterials.LanthanumEmbeddedFullerene, 4)
                .inputFluids(GTOMaterials.EuvPhotoresist, 1000)
                .inputFluids(GTOMaterials.Trichloroflerane, 1000)
                .outputFluids(GTOMaterials.GammaRaysPhotoresist, 1000)
                .EUt(1966080)
                .duration(800)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        MIXER_RECIPES.recipeBuilder("germanium_containing_precipitate_dust")
                .inputFluids(GTOMaterials.AshLeachingSolution, 1000)
                .inputFluids(GTOMaterials.Tannic, 1000)
                .outputItems(TagPrefix.dust, GTOMaterials.GermaniumContainingPrecipitate)
                .EUt(120)
                .duration(200)
                .save();

        MIXER_RECIPES.recipeBuilder("perlite_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Obsidian, 2)
                .inputFluids(GTMaterials.Water, 1000)
                .outputItems(TagPrefix.dust, GTMaterials.Perlite, 3)
                .EUt(480)
                .duration(300)
                .save();

        MIXER_RECIPES.recipeBuilder("rocket_fuel_rp_1")
                .inputFluids(GTOMaterials.Rp1, 1000)
                .inputFluids(GTMaterials.Oxygen.getFluid(FluidStorageKeys.LIQUID, 1000))
                .outputFluids(GTOMaterials.RocketFuelRp1, 1000)
                .EUt(1920)
                .duration(16)
                .save();

        MIXER_RECIPES.recipeBuilder("euv_photoresist")
                .inputItems(TagPrefix.dust, GTOMaterials.BisethylenedithiotetraselenafulvalenePerrhenate, 31)
                .inputFluids(GTOMaterials.Photoresist, 1000)
                .inputFluids(GTOMaterials.PolyurethaneResin, 1000)
                .outputFluids(GTOMaterials.EuvPhotoresist, 1000)
                .EUt(524288)
                .duration(400)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        MIXER_RECIPES.recipeBuilder("hastelloy_n_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Iridium, 2)
                .inputItems(TagPrefix.dust, GTMaterials.Molybdenum, 4)
                .inputItems(TagPrefix.dust, GTMaterials.Chromium, 2)
                .inputItems(TagPrefix.dust, GTMaterials.Titanium, 2)
                .inputItems(TagPrefix.dust, GTMaterials.Nickel, 15)
                .circuitMeta(5)
                .outputItems(TagPrefix.dust, GTOMaterials.HastelloyN, 25)
                .EUt(1920)
                .duration(1000)
                .save();

        MIXER_RECIPES.recipeBuilder("turbid_dragon_blood")
                .inputItems(TagPrefix.dust, GTOMaterials.SilicaGel)
                .inputFluids(GTOMaterials.DragonBlood, 1000)
                .inputFluids(GTMaterials.GelatinMixture, 1000)
                .outputFluids(GTOMaterials.TurbidDragonBlood, 1000)
                .EUt(1920)
                .duration(800)
                .cleanroom(GTOCleanroomType.LAW_CLEANROOM)
                .save();

        MIXER_RECIPES.recipeBuilder("xenoxene_mixture")
                .inputItems(TagPrefix.dustTiny, GTOMaterials.Radox)
                .inputItems(TagPrefix.dust, GTMaterials.Antimony)
                .inputItems(TagPrefix.dust, GTMaterials.Osmium)
                .inputItems(TagPrefix.dust, GTMaterials.EnderEye)
                .inputFluids(GTOMaterials.Xenoxene, 1000)
                .inputFluids(GTMaterials.Xenon, 9000)
                .outputFluids(GTOMaterials.XenoxeneMixture, 10000)
                .EUt(1966080)
                .duration(800)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        MIXER_RECIPES.recipeBuilder("actinium_trinium_hydroxides_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.ActiniumTriniumHydroxides, 58)
                .inputItems(TagPrefix.dust, GTMaterials.Radium, 6)
                .inputItems(GTOItems.PROTONATED_FULLERENE_SIEVING_MATRIX)
                .inputFluids(GTMaterials.Water, 2000)
                .outputItems(GTOItems.SATURATED_FULLERENE_SIEVING_MATRIX)
                .outputFluids(GTOMaterials.ActiniumRadiumHydroxideSolution, 2000)
                .EUt(245760)
                .duration(210)
                .save();

        MIXER_RECIPES.recipeBuilder("cryo_fuel")
                .inputItems("ad_astra:ice_shard", 16)
                .inputFluids(GTMaterials.CetaneBoostedDiesel, 1000)
                .inputFluids(GTOMaterials.ExplosiveHydrazine, 1000)
                .outputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 1000))
                .EUt(7680)
                .duration(320)
                .save();

        MIXER_RECIPES.recipeBuilder("dense_hydrazine_fuel_mixture")
                .inputFluids(GTOMaterials.Hydrazine, 1000)
                .inputFluids(GTMaterials.Methanol, 1000)
                .outputFluids(GTOMaterials.DenseHydrazineFuelMixture, 1000)
                .EUt(240)
                .duration(320)
                .save();

        MIXER_RECIPES.recipeBuilder("sodium_aluminium_hydride_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.AluminiumHydride, 4)
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumHydride, 2)
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumAluminiumHydride, 6)
                .EUt(30)
                .duration(190)
                .save();

        MIXER_RECIPES.recipeBuilder("heavy_quark_enriched_mixture")
                .inputFluids(GTOMaterials.HeavyQuarks, 750)
                .inputFluids(GTOMaterials.LightQuarks, 250)
                .outputFluids(GTOMaterials.HeavyQuarkEnrichedMixture, 1000)
                .EUt(32500000)
                .duration(100)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        MIXER_RECIPES.recipeBuilder("grass_block")
                .inputItems(Blocks.DIRT.asItem())
                .inputItems(Blocks.GRASS.asItem())
                .outputItems(Blocks.GRASS_BLOCK.asItem())
                .EUt(16)
                .duration(200)
                .save();

        MIXER_RECIPES.recipeBuilder("graphene_gel_suspension_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.GrapheneOxide, 3)
                .inputFluids(GTOMaterials.Resorcinol, 1000)
                .inputFluids(GTMaterials.Formaldehyde, 1000)
                .outputItems(TagPrefix.dust, GTOMaterials.GrapheneGelSuspension)
                .EUt(120)
                .duration(100)
                .save();

        MIXER_RECIPES.recipeBuilder("lanthanum_fullerene_mix_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Lanthanum)
                .inputItems(TagPrefix.dust, GTOMaterials.UnfoldedFullerene)
                .outputItems(TagPrefix.dust, GTOMaterials.LanthanumFullereneMix, 2)
                .EUt(30720)
                .duration(200)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        MIXER_RECIPES.recipeBuilder("scandium_titanium_50_mixture")
                .inputFluids(GTOMaterials.Titanium50, 144)
                .inputFluids(GTMaterials.Scandium, 144)
                .outputFluids(GTOMaterials.ScandiumTitanium50Mixture, 288)
                .EUt(7680)
                .duration(120)
                .save();

        MIXER_RECIPES.recipeBuilder("antihydrogen")
                .notConsumable(GTItems.FIELD_GENERATOR_UV.asItem())
                .inputFluids(GTOMaterials.PositiveElectron, 200)
                .inputFluids(GTOMaterials.Antiproton, 200)
                .outputFluids(GTOMaterials.Antihydrogen, 200)
                .EUt(491520)
                .duration(400)
                .save();

        MIXER_RECIPES.recipeBuilder("oganesson_breeding_base")
                .inputFluids(GTOMaterials.Titanium50, 1440)
                .inputFluids(GTMaterials.Californium, 576)
                .outputFluids(GTOMaterials.OganessonBreedingBase, 2016)
                .EUt(7680)
                .duration(480)
                .save();

        MIXER_RECIPES.recipeBuilder("aluminium_bronze_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Aluminium)
                .inputItems(TagPrefix.dust, GTMaterials.Bronze, 6)
                .circuitMeta(1)
                .outputItems(TagPrefix.dust, GTOMaterials.AluminiumBronze, 7)
                .EUt(30)
                .duration(400)
                .save();

        MIXER_RECIPES.recipeBuilder("rhodium_rhenium_naquadah_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Rhenium)
                .inputItems(TagPrefix.dust, GTMaterials.Rhodium)
                .inputItems(TagPrefix.dust, GTMaterials.Naquadah)
                .outputItems(TagPrefix.dust, GTOMaterials.RhodiumRheniumNaquadahCatalyst)
                .EUt(84500)
                .duration(260)
                .save();

        MIXER_RECIPES.recipeBuilder("polycyclic_aromatic_mixture_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.Tetracene, 2)
                .inputFluids(GTMaterials.Naphthalene, 1000)
                .outputItems(TagPrefix.dust, GTOMaterials.PolycyclicAromaticMixture, 3)
                .EUt(7680)
                .duration(240)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        MIXER_RECIPES.recipeBuilder("boron_trifluoride_acetate")
                .inputFluids(GTOMaterials.DiethylEther, 1000)
                .inputFluids(GTOMaterials.BoronFluoride, 1000)
                .outputFluids(GTOMaterials.BoronTrifluorideAcetate, 1000)
                .EUt(125)
                .duration(150)
                .save();

        MIXER_RECIPES.recipeBuilder("end_steel_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Endstone)
                .inputItems(TagPrefix.dust, GTOMaterials.DarkSteel)
                .inputItems(TagPrefix.dust, GTMaterials.Obsidian)
                .circuitMeta(3)
                .outputItems(TagPrefix.dust, GTOMaterials.EndSteel, 3)
                .EUt(480)
                .duration(360)
                .save();

        MIXER_RECIPES.recipeBuilder("dark_steel_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Iron)
                .inputItems(TagPrefix.dust, GTMaterials.Coal)
                .inputItems(TagPrefix.dust, GTMaterials.Obsidian)
                .circuitMeta(3)
                .outputItems(TagPrefix.dust, GTOMaterials.DarkSteel, 3)
                .EUt(30)
                .duration(300)
                .save();

        MIXER_RECIPES.recipeBuilder("fluix_dust")
                .inputItems(TagPrefix.dust, GTMaterials.NetherQuartz)
                .inputItems(TagPrefix.dust, GTMaterials.CertusQuartz)
                .inputItems(TagPrefix.dust, GTMaterials.Redstone)
                .outputItems(TagPrefix.dust, GTOMaterials.Fluix, 3)
                .EUt(16)
                .duration(200)
                .save();

        MIXER_RECIPES.recipeBuilder("explosivehydrazine")
                .notConsumable(GTItems.FIELD_GENERATOR_LuV.asItem())
                .inputItems(GTItems.GELLED_TOLUENE, 16)
                .inputItems(Items.FIRE_CHARGE.asItem(), 8)
                .inputFluids(GTMaterials.GlycerylTrinitrate, 1000)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture, 3000)
                .outputFluids(GTOMaterials.ExplosiveHydrazine, 4000)
                .EUt(1920)
                .duration(480)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        MIXER_RECIPES.recipeBuilder("charged_caesium_cerium_cobalt_indium_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Indium, 10)
                .inputItems(TagPrefix.dust, GTMaterials.Cobalt, 2)
                .inputItems(TagPrefix.dust, GTMaterials.Cerium)
                .inputItems(TagPrefix.dust, GTMaterials.Caesium)
                .inputFluids(GTOMaterials.CosmicComputingMixture, 1000)
                .outputItems(TagPrefix.dust, GTOMaterials.ChargedCaesiumCeriumCobaltIndium, 14)
                .EUt(31457280)
                .duration(400)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        MIXER_RECIPES.recipeBuilder("vibrant_alloy_dust")
                .circuitMeta(2)
                .inputItems(TagPrefix.dust, GTOMaterials.EnergeticAlloy)
                .inputItems(TagPrefix.dust, GTMaterials.EnderPearl)
                .outputItems(TagPrefix.dust, GTOMaterials.VibrantAlloy, 2)
                .EUt(120)
                .duration(400)
                .save();

        MIXER_RECIPES.recipeBuilder("eglin_steel_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Iron, 4)
                .inputItems(TagPrefix.dust, GTMaterials.Kanthal)
                .inputItems(TagPrefix.dust, GTMaterials.Invar, 5)
                .inputItems(TagPrefix.dust, GTMaterials.Sulfur)
                .inputItems(TagPrefix.dust, GTMaterials.Silicon)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon)
                .outputItems(TagPrefix.dust, GTOMaterials.EglinSteel, 13)
                .EUt(120)
                .duration(600)
                .save();

        MIXER_RECIPES.recipeBuilder("viscoelastic_polyurethane_foam")
                .inputFluids(GTOMaterials.ViscoelasticPolyurethane, 1000)
                .inputFluids(GTMaterials.Air, 1000)
                .outputFluids(GTOMaterials.ViscoelasticPolyurethaneFoam, 2000)
                .EUt(120)
                .duration(150)
                .save();

        MIXER_RECIPES.recipeBuilder("naquadah_solution")
                .inputItems(TagPrefix.dust, GTMaterials.Naquadah, 2)
                .inputFluids(GTOMaterials.AmmoniumNitrateSolution, 1000)
                .outputFluids(GTOMaterials.NaquadahSolution, 1000)
                .EUt(30720)
                .duration(400)
                .save();

        MIXER_RECIPES.recipeBuilder("ti_al_chloride")
                .inputItems(TagPrefix.dust, GTOMaterials.AluminiumChloride, 4)
                .inputFluids(GTMaterials.TitaniumTetrachloride, 1000)
                .outputItems(TagPrefix.dust, GTOMaterials.TiAlChloride)
                .EUt(120)
                .duration(400)
                .save();

        MIXER_RECIPES.recipeBuilder("aluminium_hydroxide_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumAluminate, 4)
                .inputFluids(GTMaterials.Water, 3000)
                .outputItems(TagPrefix.dust, GTOMaterials.AluminiumHydroxide, 7)
                .outputFluids(SodiumHydroxideSolution.getFluid(1000))
                .EUt(30)
                .duration(120)
                .save();

        MIXER_RECIPES.recipeBuilder("tungsten_boron_mixture_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Tungsten)
                .inputItems(TagPrefix.dust, GTMaterials.Boron, 4)
                .circuitMeta(2)
                .outputItems(TagPrefix.dust, GTOMaterials.TungstenBoronMixture)
                .EUt(480)
                .duration(100)
                .save();

        MIXER_RECIPES.recipeBuilder("coolant_liquid")
                .inputItems(TagPrefix.dust, GTMaterials.Lazurite, 2)
                .inputFluids(GTMaterials.DistilledWater, 1000)
                .outputFluids(GTOMaterials.CoolantLiquid, 1000)
                .EUt(30)
                .duration(200)
                .save();

        MIXER_RECIPES.recipeBuilder("dew_of_the_void")
                .inputItems(GTOItems.PULSATING_CRYSTAL)
                .inputItems(GTOItems.GREEN_ALGAE, 8)
                .inputItems(GTOItems.BROWN_ALGAE, 8)
                .inputFluids(new FluidStack(GTOFluids.NUTRIENT_DISTILLATION.getSource(), 4000))
                .outputFluids(new FluidStack(GTOFluids.DEW_OF_THE_VOID.getSource(), 4000))
                .EUt(120)
                .duration(400)
                .save();

        MIXER_RECIPES.recipeBuilder("bacterial_growth_medium")
                .inputFluids(GTMaterials.DistilledWater, 1000)
                .inputFluids(GTOMaterials.BloodCells, 1000)
                .outputFluids(GTOMaterials.BacterialGrowthMedium, 1000)
                .EUt(120)
                .duration(100)
                .cleanroom(CleanroomType.STERILE_CLEANROOM)
                .save();

        MIXER_RECIPES.recipeBuilder("animal_cells")
                .inputItems(TagPrefix.dust, GTMaterials.Meat, 2)
                .inputFluids(GTMaterials.DistilledWater, 1000)
                .outputFluids(GTOMaterials.AnimalCells, 1000)
                .EUt(480)
                .duration(100)
                .cleanroom(CleanroomType.STERILE_CLEANROOM)
                .save();

        MIXER_RECIPES.recipeBuilder("pluripotency_induction_gene_therapy_fluid")
                .inputFluids(GTOMaterials.PluripotencyInductionGenePlasmids, 1000)
                .inputFluids(GTOMaterials.Chitosan, 1000)
                .outputFluids(GTOMaterials.PluripotencyInductionGeneTherapyFluid, 1000)
                .EUt(7680)
                .duration(24)
                .cleanroom(CleanroomType.STERILE_CLEANROOM)
                .save();

        MIXER_RECIPES.recipeBuilder("clear_ammonia_solution")
                .inputFluids(GTMaterials.Ammonia, 1000)
                .inputFluids(GTMaterials.DistilledWater, 1000)
                .outputFluids(GTOMaterials.ClearAmmoniaSolution, 1000)
                .EUt(480)
                .duration(100)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        MIXER_RECIPES.builder("nitinol_a_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Nickel, 2)
                .inputItems(TagPrefix.dust, GTMaterials.Titanium, 3)
                .outputItems(TagPrefix.dust, GTOMaterials.NitinolA, 5)
                .circuitMeta(10)
                .EUt(120)
                .duration(500)
                .save();

        MIXER_RECIPES.recipeBuilder("potin")
                .inputItems(TagPrefix.dust, GTMaterials.Copper, 6)
                .inputItems(TagPrefix.dust, GTMaterials.Tin, 2)
                .inputItems(TagPrefix.dust, GTMaterials.Lead)
                .circuitMeta(3)
                .outputItems(TagPrefix.dust, GTMaterials.Potin, 9)
                .duration(400)
                .EUt(7)
                .save();

        MIXER_RECIPES.builder("trinium_compound_front")
                .inputFluids(GTOMaterials.ActiniumRadiumHydroxideSolution, 1000)
                .inputFluids(GTOMaterials.ResidualTriniiteSolution, 2000)
                .outputFluids(GTOMaterials.TriniumCompoundFront, 3000)
                .EUt(1920)
                .duration(800)
                .save();

        MIXER_RECIPES.builder("dna_extraction_solution")
                .inputItems(TagPrefix.dust, CTAB)
                .inputItems(TagPrefix.dust, EDTA)
                .inputFluids(DistilledWater, 12000)
                .inputItems(TagPrefix.dust, GTMaterials.SodiumHydroxide, 4)
                .inputFluids(TrisHydrochlorideSolution, 4000)
                .outputFluids(DNAExtractionBuffer.getFluid(16000))
                .EUt(7680)
                .duration(200)
                .cleanroom(CleanroomType.STERILE_CLEANROOM)
                .save();
        MIXER_RECIPES.builder("iron_chromium_redox_flow_battery_electrolyte_energy_release_cathode")
                .inputItems(TagPrefix.dust, GTOMaterials.IronSulfate, 4)
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumSulfate)
                .inputFluids(GTMaterials.SulfuricAcid, 4000)
                .inputFluids(GTMaterials.Oxygen, 1000)
                .outputFluids(IronChromiumRedoxFlowBatteryElectrolyte.getFluid(GTOFluidStorageKey.ENERGY_RELEASE_CATHODE, 16000))
                .EUt(1920)
                .duration(220)
                .save();
        MIXER_RECIPES.builder("cerium_4_sulfate")
                .inputItems(TagPrefix.dust, GTMaterials.Cerium)
                .inputFluids(GTMaterials.SulfuricAcid, 1000)
                .inputFluids(GTMaterials.Oxygen, 2000)
                .outputFluids(GTOMaterials.Cerium4Sulfate, 1000)
                .EUt(30)
                .duration(30)
                .save();

        MIXER_RECIPES.builder("titanium_tb6_dust")
                .inputItems(GTOTagPrefix.dust, GTMaterials.Titanium, 85)
                .inputItems(GTOTagPrefix.dust, GTMaterials.Vanadium, 10)
                .inputItems(GTOTagPrefix.dust, GTMaterials.Steel, 2)
                .inputItems(GTOTagPrefix.dust, GTMaterials.Aluminium, 3)
                .outputItems(GTOTagPrefix.dust, GTOMaterials.TitaniumTB6, 100)
                .circuitMeta(14)
                .EUt(480)
                .duration(6500)
                .save();
    }
}
