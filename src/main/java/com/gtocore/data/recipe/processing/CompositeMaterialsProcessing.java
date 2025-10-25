package com.gtocore.data.recipe.processing;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import org.jetbrains.annotations.Nullable;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.dust;
import static com.gregtechceu.gtceu.common.data.GTMaterials.Carbon;
import static com.gregtechceu.gtceu.common.data.GTMaterials.Polyethylene;
import static com.gtocore.common.data.GTORecipeTypes.*;

public class CompositeMaterialsProcessing {

    private static @Nullable Reference2IntOpenHashMap<Material> fiber_extrusion_temperatures = new Reference2IntOpenHashMap<>();

    public static void registerFiberExtrusionTemperature(Material material, int temperature) {
        if (fiber_extrusion_temperatures == null) fiber_extrusion_temperatures = new Reference2IntOpenHashMap<>();
        fiber_extrusion_temperatures.put(material, temperature);
    }

    public static int getFiberExtrusionTemperature(Material material) {
        if (fiber_extrusion_temperatures == null) return 0;
        return fiber_extrusion_temperatures.getOrDefault(material, 0);
    }

    public static void init() {
        POLYMERIZATION_REACTOR_RECIPES.builder("polyacrylonitrile")
                .inputItems(dust, GTMaterials.Sodium)
                .inputFluids(GTOMaterials.Acrylonitrile, 12000)
                .inputFluids(GTMaterials.Naphthalene, 1000)
                .outputFluids(GTOMaterials.Polyacrylonitrile, 11000)
                .EUt(480)
                .duration(200)
                .save();
        REACTION_FURNACE_RECIPES.builder("titanium3_carbide_ceramic_dust")
                .inputItems(GTOTagPrefix.dust, GTMaterials.TitaniumCarbide, 4)
                .outputItems(GTOTagPrefix.dust, GTOMaterials.Titanium3Carbide, 4)
                .inputFluids(GTMaterials.Oxygen, 1000)
                .outputFluids(GTMaterials.CarbonMonoxide, 1000)
                .EUt(555)
                .blastFurnaceTemp(3555)
                .duration(555)
                .save();

        processFiber();
        processCompositeMaterials();
    }

    private static void processFiber() {
        FIBER_EXTRUSION_RECIPES.builder("micron_pan_fiber")
                .inputItems(dust, GTOMaterials.Polyacrylonitrile, 2)
                .outputItems(GTOItems.MICRON_PAN_FIBER.asItem(), 2)
                .inputFluids(GTOMaterials.Soap, 1000)
                .circuitMeta(1)
                .EUt(120)
                .blastFurnaceTemp(3000)
                .duration(500)
                .save();
        FIBER_EXTRUSION_RECIPES.builder("nano_pan_fiber")
                .inputItems(dust, GTOMaterials.Polyacrylonitrile, 2)
                .inputItems(dust, GTOMaterials.CTAB)
                .outputItems(GTOItems.NANO_PAN_FIBER.asItem(), 2)
                .inputFluids(GTOMaterials.KH550SilaneCouplingAgent, 1000)
                .circuitMeta(2)
                .EUt(30720)
                .blastFurnaceTemp(5000)
                .duration(500)
                .save();
        FIBER_EXTRUSION_RECIPES.builder("atomic_pan_fiber")
                .inputItems(dust, GTOMaterials.Polyacrylonitrile, 2)
                .inputItems(dust, GTOMaterials.CTAB)
                .notConsumable(GTOTagPrefix.NANITES, GTMaterials.Gold)
                .outputItems(GTOItems.ATOMIC_PAN_FIBER.asItem(), 2)
                .inputFluids(GTOMaterials.KH550SilaneCouplingAgent, 1000)
                .inputFluids(GTOMaterials.Polyvinylpyrrolidone, 1000)
                .circuitMeta(3)
                .EUt(520000)
                .blastFurnaceTemp(7000)
                .duration(500)
                .save();
        CHEMICAL_RECIPES.builder("preoxidized_micron_pan_fiber")
                .inputItems(GTOItems.MICRON_PAN_FIBER.asItem(), 2)
                .outputItems(GTOItems.PREOXIDIZED_MICRON_PAN_FIBER.asItem(), 2)
                .inputFluids(GTMaterials.Oxygen, 8000)
                .EUt(240)
                .duration(200)
                .save();
        ARC_GENERATOR_RECIPES.builder("preoxidized_nano_pan_fiber")
                .circuitMeta(1)
                .inputItems(GTOItems.NANO_PAN_FIBER.asItem(), 2)
                .inputItems(GTOTagPrefix.CATALYST, GTMaterials.Rhenium)
                .outputItems(GTOItems.PREOXIDIZED_NANO_PAN_FIBER.asItem(), 2)
                .inputFluids(GTMaterials.Oxygen, 3000)
                .EUt(7680)
                .duration(200)
                .save();

        ELECTROPLATING_RECIPES.builder("preoxidized_atomic_pan_fiber")
                .inputItems(GTOItems.ATOMIC_PAN_FIBER.asItem(), 2)
                .notConsumable(GTOTagPrefix.plateDouble, GTMaterials.EnrichedNaquadahTriniumEuropiumDuranide, 8)
                .outputItems(GTOItems.PREOXIDIZED_ATOMIC_PAN_FIBER.asItem(), 2)
                .inputFluids(GTMaterials.Oxygen, FluidStorageKeys.PLASMA, 1000)
                .inputFluids(GTOMaterials.SupercriticalCarbonDioxide, 1000)
                .EUt(500000)
                .duration(200)
                .save();
        ARC_GENERATOR_RECIPES.builder("graphitized_atomic_pan_fiber")
                .inputItems(GTOItems.PREOXIDIZED_ATOMIC_PAN_FIBER.asItem(), 2)
                .inputItems(dust, Carbon, 16)
                .inputItems(GTOTagPrefix.CATALYST, GTOMaterials.RhodiumRheniumNaquadahCatalyst)
                .outputItems(GTOItems.GRAPHITIZED_ATOMIC_PAN_FIBER.asItem(), 2)
                .inputFluids(GTMaterials.Krypton, 3000)
                .EUt(7680)
                .duration(2000)
                .save();
        REACTION_FURNACE_RECIPES.builder("t300_carbon_fiber_tow_carbon_fibres")
                .inputItems(GTOItems.PREOXIDIZED_MICRON_PAN_FIBER.asItem())
                .inputItems(dust, Carbon, 16)
                .outputItems(GTOTagPrefix.FIBER_TOW, GTOMaterials.T300CarbonFiber)
                .inputFluids(GTOMaterials.HighPressureNitrogen, 6000)
                .outputFluids(GTMaterials.Nitrogen, 2000)
                .EUt(222)
                .blastFurnaceTemp(2222)
                .duration(222)
                .save();
        REACTION_FURNACE_RECIPES.builder("t700_carbon_fiber_tow_carbon_fibres")
                .inputItems(GTOItems.PREOXIDIZED_NANO_PAN_FIBER.asItem())
                .inputItems(dust, Carbon, 16)
                .inputItems(dust, GTMaterials.PolyvinylButyral, 4)
                .outputItems(GTOTagPrefix.FIBER_TOW, GTOMaterials.T700CarbonFiber)
                .inputFluids(GTOMaterials.HighPressureKrypton, 6000)
                .outputFluids(GTMaterials.Krypton, 2000)
                .EUt(5555)
                .blastFurnaceTemp(5355)
                .duration(355)
                .save();
        CHEMICAL_VAPOR_DEPOSITION_RECIPES.builder("t1200_carbon_fiber_tow_carbon_fibres")
                .inputItems(GTOItems.GRAPHITIZED_ATOMIC_PAN_FIBER.asItem())
                .inputItems(dust, Carbon, 64)
                .outputItems(GTOTagPrefix.FIBER_TOW, GTOMaterials.T1200CarbonFiber)
                .inputFluids(GTOMaterials.HighPressureNeon, 24000)
                .inputFluids(GTOMaterials.Polyetheretherketone, 4000)
                .outputFluids(GTMaterials.Neon, 6000)
                .EUt(524288)
                .duration(200)
                .save();
        CHEMICAL_VAPOR_DEPOSITION_RECIPES.builder("t800_carbon_fiber_tow_carbon_fibres")
                .inputItems(GTOTagPrefix.FIBER_TOW, GTOMaterials.T700CarbonFiber)
                .inputItems(dust, Carbon, 16)
                .outputItems(GTOTagPrefix.FIBER_TOW, GTOMaterials.T800CarbonFiber)
                .inputFluids(GTOMaterials.HighPressureArgon, 12000)
                .inputFluids(GTOMaterials.PolyurethaneResin, 4000)
                .outputFluids(GTMaterials.Argon, 8000)
                .EUt(5242)
                .duration(200)
                .save();
        BLAST_RECIPES.builder("t400_carbon_fiber_tow_carbon_fibres")
                .inputItems(GTOTagPrefix.FIBER_TOW, GTOMaterials.T300CarbonFiber)
                .inputItems(dust, Carbon, 8)
                .outputItems(GTOTagPrefix.FIBER_TOW, GTOMaterials.T400CarbonFiber)
                .inputFluids(GTMaterials.Argon, 100)
                .EUt(1900)
                .blastFurnaceTemp(3588)
                .duration(190)
                .save();
        PHYSICAL_VAPOR_DEPOSITION_RECIPES.builder("t600_carbon_fiber_tow_carbon_fibres")
                .inputItems(GTOTagPrefix.FIBER_TOW, GTOMaterials.T400CarbonFiber)
                .inputItems(dust, Carbon, 8)
                .outputItems(GTOTagPrefix.FIBER_TOW, GTOMaterials.T600CarbonFiber)
                .inputFluids(GTMaterials.ReinforcedEpoxyResin, 1000)
                .EUt(1900)
                .duration(200)
                .save();
        ELECTROPLATING_RECIPES.builder("t900_carbon_fiber_tow_carbon_fibres")
                .inputItems(GTOTagPrefix.FIBER_TOW, GTOMaterials.T800CarbonFiber, 4)
                .notConsumable(GTOTagPrefix.plateDouble, GTMaterials.YttriumBariumCuprate, 8)
                .outputItems(GTOTagPrefix.FIBER_TOW, GTOMaterials.T900CarbonFiber, 4)
                .inputFluids(GTMaterials.Neon, FluidStorageKeys.PLASMA, 1000)
                .inputFluids(GTOMaterials.SupercriticalCarbonDioxide, 20)
                .EUt(5000)
                .duration(200)
                .save();
        ELECTROPLATING_RECIPES.builder("t1000_carbon_fiber_tow_carbon_fibres")
                .inputItems(GTOTagPrefix.FIBER_TOW, GTOMaterials.T800CarbonFiber, 2)
                .notConsumable(GTOTagPrefix.plateDouble, GTMaterials.IndiumTinBariumTitaniumCuprate, 8)
                .outputItems(GTOTagPrefix.FIBER_TOW, GTOMaterials.T1000CarbonFiber, 2)
                .inputFluids(GTMaterials.Argon, FluidStorageKeys.PLASMA, 1000)
                .inputFluids(GTOMaterials.SupercriticalCarbonDioxide, 100)
                .EUt(5000)
                .duration(200)
                .save();
        ELECTROPLATING_RECIPES.builder("t1500_carbon_fiber_tow_carbon_fibres")
                .inputItems(GTOTagPrefix.FIBER_TOW, GTOMaterials.T1200CarbonFiber, 2)
                .notConsumable(GTOTagPrefix.plateDouble, GTMaterials.EnrichedNaquadahTriniumEuropiumDuranide, 8)
                .outputItems(GTOTagPrefix.FIBER_TOW, GTOMaterials.T1500CarbonFiber, 2)
                .inputFluids(GTOMaterials.Orichalcum, FluidStorageKeys.PLASMA, 1000)
                .inputFluids(GTOMaterials.SupercriticalCarbonDioxide, 4000)
                .EUt(500000)
                .duration(200)
                .save();

        Material[] carbonFibers = new Material[] {
                Carbon,
                GTOMaterials.T300CarbonFiber,
                GTOMaterials.T400CarbonFiber,
                GTOMaterials.T600CarbonFiber,
                GTOMaterials.T700CarbonFiber,
                GTOMaterials.T800CarbonFiber,
                GTOMaterials.T900CarbonFiber,
                GTOMaterials.T1000CarbonFiber,
                GTOMaterials.T1200CarbonFiber,
                GTOMaterials.T1500CarbonFiber
        };

        Material[] resins = new Material[] {
                Polyethylene,
                GTOMaterials.PhenolicResin,
                GTMaterials.Epoxy,
                GTMaterials.ReinforcedEpoxyResin,
                GTMaterials.Polybenzimidazole,
                GTOMaterials.PolyurethaneResin,
                GTOMaterials.Polyurethane,
                GTOMaterials.Polyimide,
                GTOMaterials.Polyetheretherketone,
                GTOMaterials.Paa
        };
        for (int i = 0; i < carbonFibers.length; i++) {
            CHEMICAL_BATH_RECIPES.builder("chemical_bath_" + carbonFibers[i].getName())
                    .inputItems(GTOTagPrefix.FIBER_TOW, carbonFibers[i], 4)
                    .outputItems(GTOTagPrefix.FIBER, carbonFibers[i], 4)
                    .inputFluids(resins[i], 576)
                    .EUt(95 + ((1 << i) * 25L))
                    .duration(200)
                    .save();
        }
    }

    private static void processCompositeMaterials() {
        ISOSTATIC_PRESSING_RECIPES.builder("oxide_dispersion_strengthened_nickel_based_alloy_rough_blank")
                .inputItems(GTOTagPrefix.dust, GTOMaterials.Inconel617, 7)
                .inputItems(GTOTagPrefix.dust, GTOMaterials.YttriumOxide, 2)
                .inputItems(GTOTagPrefix.FIBER_MESH, GTMaterials.Titanium, 2)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.OxideDispersionStrengthenedNickelBasedAlloy)
                .inputFluids(GTMaterials.Nickel, 288)
                .EUt(8000)
                .duration(2000)
                .save();

        SINTERING_FURNACE_RECIPES.builder("oxide_dispersion_strengthened_nickel_based_alloy_block")
                .inputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.OxideDispersionStrengthenedNickelBasedAlloy)
                .outputItems(GTOTagPrefix.block, GTOMaterials.OxideDispersionStrengthenedNickelBasedAlloy)
                .EUt(2000)
                .blastFurnaceTemp(4700)
                .duration(200)
                .save();

        ISOSTATIC_PRESSING_RECIPES.builder("titanium_steel_composite_rough_blank")
                .inputItems(GTOTagPrefix.foil, GTOMaterials.TitaniumTi64, 9)
                .inputItems(GTOTagPrefix.foil, GTOMaterials.StainlessSteelGC4, 9)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.TitaniumSteelComposite)
                .inputFluids(GTMaterials.Copper, 288)
                .EUt(8000)
                .duration(2000)
                .save();

        THERMO_PRESSING_RECIPES.builder("titanium_steel_composite_block")
                .inputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.TitaniumSteelComposite)
                .outputItems(GTOTagPrefix.block, GTOMaterials.TitaniumSteelComposite)
                .inputFluids(GTMaterials.Nickel, 288)
                .EUt(2000)
                .duration(400)
                .save();

        CHEMICAL_BATH_RECIPES.builder("fiberglass_reinforced_plastic")
                .inputItems(GTOTagPrefix.FIBER_MESH, GTMaterials.BorosilicateGlass, 2)
                .inputFluids(GTOMaterials.PhenolicResin, 288)
                .inputFluids(GTMaterials.Epoxy, 144)
                .outputFluids(GTOMaterials.FiberglassReinforcedPlastic, 576)
                .EUt(500)
                .duration(200)
                .save();

        CHEMICAL_BATH_RECIPES.builder("quartz_fiber_reinforced_silica_rough_blank")
                .inputItems(GTOTagPrefix.FIBER_MESH, GTMaterials.Quartzite, 2)
                .inputItems(GTOTagPrefix.dust, GTMaterials.SiliconDioxide, 7)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.QuartzFiberReinforcedSilica)
                .inputFluids(GTOMaterials.SilicaSol, 288)
                .EUt(2000)
                .duration(200)
                .save();

        THERMO_PRESSING_RECIPES.builder("quartz_fiber_reinforced_silica_block")
                .inputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.QuartzFiberReinforcedSilica)
                .outputItems(GTOTagPrefix.block, GTOMaterials.QuartzFiberReinforcedSilica)
                .EUt(500)
                .duration(200)
                .save();

        MIXER_RECIPES.builder("aluminum_reinforced_with_silicon_carbide_particles_pre_dust")
                .inputItems(GTOTagPrefix.dust, GTOMaterials.AluminumAlloy2090, 2)
                .inputItems(GTOTagPrefix.dust, GTOMaterials.SiliconCarbide, 7)
                .outputItems(GTOTagPrefix.dust, GTOMaterials.AluminumReinforcedWithSiliconCarbideParticlesPre, 9)
                .circuitMeta(2)
                .EUt(2000)
                .duration(200)
                .save();

        SINTERING_FURNACE_RECIPES.builder("aluminum_reinforced_with_silicon_carbide_particles_block")
                .inputItems(GTOTagPrefix.dust, GTOMaterials.AluminumReinforcedWithSiliconCarbideParticlesPre, 9)
                .outputItems(GTOTagPrefix.block, GTOMaterials.AluminumReinforcedWithSiliconCarbideParticles)
                .inputFluids(GTOMaterials.PhenolicResin, 288)
                .EUt(2000)
                .blastFurnaceTemp(4400)
                .duration(500)
                .save();

        MIXER_RECIPES.builder("graphite_copper_composite_pre_dust")
                .inputItems(GTOTagPrefix.dust, GTMaterials.Bronze, 7)
                .inputItems(GTOTagPrefix.dust, GTMaterials.Graphite, 2)
                .outputItems(GTOTagPrefix.dust, GTOMaterials.GraphiteCopperCompositePre, 9)
                .circuitMeta(2)
                .EUt(2000)
                .duration(200)
                .save();

        SINTERING_FURNACE_RECIPES.builder("graphite_copper_composite_block")
                .inputItems(GTOTagPrefix.dust, GTOMaterials.GraphiteCopperCompositePre, 9)
                .outputItems(GTOTagPrefix.block, GTOMaterials.GraphiteCopperComposite)
                .inputFluids(GTMaterials.Copper, 288)
                .EUt(2000)
                .blastFurnaceTemp(4400)
                .duration(500)
                .save();

        PHYSICAL_VAPOR_DEPOSITION_RECIPES.builder("borosilicate_fiber_reinforced_aluminum_matrix_composite_rough_blank")
                .inputItems(GTOTagPrefix.FIBER_MESH, GTMaterials.BorosilicateGlass, 2)
                .inputItems(GTOTagPrefix.foil, GTOMaterials.AluminumAlloy8090, 18)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.BorosilicateFiberReinforcedAluminumMatrixComposite)
                .inputFluids(GTOMaterials.PhenolicResin, 288)
                .EUt(2000)
                .duration(200)
                .save();

        THERMO_PRESSING_RECIPES.builder("borosilicate_fiber_reinforced_aluminum_matrix_composite_block")
                .inputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.BorosilicateFiberReinforcedAluminumMatrixComposite)
                .outputItems(GTOTagPrefix.block, GTOMaterials.BorosilicateFiberReinforcedAluminumMatrixComposite)
                .EUt(2000)
                .duration(200)
                .save();

        MIXER_RECIPES.builder("dispersion_strengthened_copper_pre_dust")
                .inputItems(GTOTagPrefix.dust, GTMaterials.Copper, 7)
                .inputItems(GTOTagPrefix.dust, GTOMaterials.Alumina, 2)
                .outputItems(GTOTagPrefix.dust, GTOMaterials.DispersionStrengthenedCopperPre, 9)
                .circuitMeta(2)
                .EUt(8000)
                .duration(200)
                .save();

        THERMO_PRESSING_RECIPES.builder("dispersion_strengthened_copper_block")
                .inputItems(GTOTagPrefix.dust, GTOMaterials.DispersionStrengthenedCopperPre, 9)
                .outputItems(GTOTagPrefix.block, GTOMaterials.DispersionStrengthenedCopper)
                .inputFluids(GTOMaterials.PhenolicResin, 288)
                .EUt(2000)
                .duration(200)
                .save();

        ISOSTATIC_PRESSING_RECIPES.builder("silica_carbon_composite_rough_blank")
                .inputItems(GTOTagPrefix.dust, GTOMaterials.HighPuritySilica, 6)
                .inputItems(GTOTagPrefix.dust, GTMaterials.Graphite, 3)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.SilicaCarbonComposite)
                .inputFluids(GTOMaterials.PhenolicResin, 288)
                .EUt(8000)
                .duration(200)
                .save();

        SINTERING_FURNACE_RECIPES.builder("silica_carbon_composite_block")
                .inputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.SilicaCarbonComposite)
                .outputItems(GTOTagPrefix.block, GTOMaterials.SilicaCarbonComposite)
                .EUt(2000)
                .blastFurnaceTemp(4400)
                .duration(500)
                .save();

        CHEMICAL_BATH_RECIPES.builder("carbon_fiber_epoxy_composite_rough_blank")
                .inputItems(GTOTagPrefix.FIBER_MESH, GTOMaterials.T300CarbonFiber, 10)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.CarbonFiberEpoxyComposite)
                .inputFluids(GTMaterials.Epoxy, 576)
                .EUt(8000)
                .duration(200)
                .save();

        THERMO_PRESSING_RECIPES.builder("carbon_fiber_epoxy_composite_block")
                .inputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.CarbonFiberEpoxyComposite)
                .outputItems(GTOTagPrefix.block, GTOMaterials.CarbonFiberEpoxyComposite)
                .EUt(2000)
                .duration(200)
                .save();

        CHEMICAL_BATH_RECIPES.builder("carbon_fiber_reinforced_epoxy_composite_rough_blank")
                .inputItems(GTOTagPrefix.FIBER_MESH, GTOMaterials.T400CarbonFiber, 10)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.CarbonFiberReinforcedEpoxyComposite)
                .inputFluids(GTMaterials.ReinforcedEpoxyResin, 576)
                .EUt(8000)
                .duration(200)
                .save();

        THERMO_PRESSING_RECIPES.builder("carbon_fiber_reinforced_epoxy_composite_block")
                .inputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.CarbonFiberReinforcedEpoxyComposite)
                .outputItems(GTOTagPrefix.block, GTOMaterials.CarbonFiberReinforcedEpoxyComposite)
                .EUt(2000)
                .duration(200)
                .save();

        CHEMICAL_BATH_RECIPES.builder("carbon_fiber_phenolic_resin_composite_rough_blank")
                .inputItems(GTOTagPrefix.FIBER_MESH, GTOMaterials.T700CarbonFiber, 10)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.CarbonFiberPhenolicResinComposite)
                .inputFluids(GTOMaterials.PhenolicResin, 576)
                .EUt(8000)
                .duration(200)
                .save();

        THERMO_PRESSING_RECIPES.builder("carbon_fiber_phenolic_resin_composite_block")
                .inputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.CarbonFiberPhenolicResinComposite)
                .outputItems(GTOTagPrefix.block, GTOMaterials.CarbonFiberPhenolicResinComposite)
                .EUt(2000)
                .duration(200)
                .save();

        CHEMICAL_BATH_RECIPES.builder("carbon_fiber_polyphenylene_sulfide_composite_rough_blank")
                .inputItems(GTOTagPrefix.FIBER_MESH, GTOMaterials.T600CarbonFiber, 10)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.CarbonFiberPolyphenyleneSulfideComposite)
                .inputFluids(GTMaterials.PolyphenyleneSulfide, 576)
                .EUt(8000)
                .duration(200)
                .save();

        THERMO_PRESSING_RECIPES.builder("carbon_fiber_polyphenylene_sulfide_composite_block")
                .inputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.CarbonFiberPolyphenyleneSulfideComposite)
                .outputItems(GTOTagPrefix.block, GTOMaterials.CarbonFiberPolyphenyleneSulfideComposite)
                .EUt(2000)
                .duration(200)
                .save();

        MIXER_RECIPES.builder("silicon_carbide_fiber_reinforced_nickel_based_composite_rough_blank")
                .inputItems(GTOTagPrefix.FIBER_MESH, GTOMaterials.SiliconCarbide, 4)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.SiliconCarbideFiberReinforcedNickelBasedComposite)
                .inputFluids(GTOMaterials.InconelX750, FluidStorageKeys.MOLTEN, 1296)
                .circuitMeta(2)
                .EUt(32700)
                .duration(200)
                .save();

        SINTERING_FURNACE_RECIPES.builder("silicon_carbide_fiber_reinforced_nickel_based_composite_block")
                .inputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.SiliconCarbideFiberReinforcedNickelBasedComposite)
                .outputItems(GTOTagPrefix.block, GTOMaterials.SiliconCarbideFiberReinforcedNickelBasedComposite)
                .inputFluids(GTMaterials.Nickel, 288)
                .EUt(8000)
                .blastFurnaceTemp(7000)
                .duration(500)
                .save();

        ISOSTATIC_PRESSING_RECIPES.builder("silicon_carbide_fiber_reinforced_titanium_matrix_composite_rough_blank")
                .inputItems(GTOTagPrefix.foil, GTOMaterials.TitaniumTB6, 18)
                .inputItems(GTOTagPrefix.FIBER_MESH, GTOMaterials.SiliconCarbide, 4)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.SiliconCarbideFiberReinforcedTitaniumMatrixComposite)
                .EUt(32700)
                .duration(200)
                .save();

        THERMO_PRESSING_RECIPES.builder("silicon_carbide_fiber_reinforced_titanium_matrix_composite_block")
                .inputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.SiliconCarbideFiberReinforcedTitaniumMatrixComposite)
                .outputItems(GTOTagPrefix.block, GTOMaterials.SiliconCarbideFiberReinforcedTitaniumMatrixComposite)
                .inputFluids(GTMaterials.Titanium, 288)
                .EUt(8000)
                .duration(200)
                .save();

        ISOSTATIC_PRESSING_RECIPES.builder("tungsten_fiber_reinforced_cobalt_based_composite_rough_blank")
                .inputItems(GTOTagPrefix.foil, GTOMaterials.Stellite, 18)
                .inputItems(GTOTagPrefix.FIBER_MESH, GTMaterials.Tungsten, 4)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.TungstenFiberReinforcedCobaltBasedComposite)
                .EUt(32700)
                .duration(200)
                .save();

        THERMO_PRESSING_RECIPES.builder("tungsten_fiber_reinforced_cobalt_based_composite_block")
                .inputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.TungstenFiberReinforcedCobaltBasedComposite)
                .outputItems(GTOTagPrefix.block, GTOMaterials.TungstenFiberReinforcedCobaltBasedComposite)
                .inputFluids(GTMaterials.Cobalt, 288)
                .EUt(8000)
                .duration(200)
                .save();

        CHEMICAL_BATH_RECIPES.builder("carbon_fiber_polyimide_composite_rough_blank")
                .inputItems(GTOTagPrefix.FIBER_MESH, GTOMaterials.T800CarbonFiber, 10)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.CarbonFiberPolyimideComposite)
                .inputFluids(GTOMaterials.Polyimide, 288)
                .EUt(32700)
                .duration(200)
                .save();

        THERMO_PRESSING_RECIPES.builder("carbon_fiber_polyimide_composite_block")
                .inputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.CarbonFiberPolyimideComposite)
                .outputItems(GTOTagPrefix.block, GTOMaterials.CarbonFiberPolyimideComposite)
                .EUt(32700)
                .duration(200)
                .save();

        REACTION_FURNACE_RECIPES.builder("alumina_dust0")
                .inputItems(GTOTagPrefix.dust, GTOMaterials.Alumina, 2)
                .inputItems(GTOTagPrefix.dust, GTMaterials.Aluminium, 2)
                .outputItems(GTOTagPrefix.dust, GTOMaterials.Alumina, 6)
                .inputFluids(GTMaterials.Nitrogen, 432)
                .EUt(8000)
                .blastFurnaceTemp(7100)
                .duration(200)
                .save();

        LARGE_CHEMICAL_RECIPES.builder("silicasol")
                .inputItems(GTOTagPrefix.dust, GTMaterials.SodiumHydroxide, 2)
                .inputItems(GTOTagPrefix.dust, GTMaterials.Silicon)
                .inputFluids(GTMaterials.Water, 288)
                .outputFluids(GTMaterials.Hydrogen, 288)
                .outputFluids(GTOMaterials.SilicaSol, 144)
                .EUt(8000)
                .duration(100)
                .save();

        CHEMICAL_BATH_RECIPES.builder("alumina_fiber_reinforced_aluminum_matrix_composite_rough_blank")
                .inputItems(GTOTagPrefix.dust, GTOMaterials.AluminumAlloy5A06, 9)
                .inputItems(GTOTagPrefix.FIBER_MESH, GTOMaterials.Alumina, 2)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.AluminaFiberReinforcedAluminumMatrixComposite)
                .inputFluids(GTMaterials.ReinforcedEpoxyResin, 288)
                .EUt(2000)
                .duration(200)
                .save();

        THERMO_PRESSING_RECIPES.builder("alumina_fiber_reinforced_aluminum_matrix_composite_block")
                .inputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.AluminaFiberReinforcedAluminumMatrixComposite)
                .outputItems(GTOTagPrefix.block, GTOMaterials.AluminaFiberReinforcedAluminumMatrixComposite)
                .EUt(500)
                .duration(100)
                .save();

        CHEMICAL_BATH_RECIPES.builder("carbon_fiber_peek_composite_rough_blank")
                .inputItems(GTOTagPrefix.FIBER_MESH, GTOMaterials.T900CarbonFiber, 10)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.CarbonFiberPolyetheretherketoneComposite)
                .inputFluids(GTOMaterials.Polyetheretherketone, 432)
                .EUt(131000)
                .duration(200)
                .save();

        THERMO_PRESSING_RECIPES.builder("carbon_fiber_peek_composite_block")
                .inputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.CarbonFiberPolyetheretherketoneComposite)
                .outputItems(GTOTagPrefix.block, GTOMaterials.CarbonFiberPolyetheretherketoneComposite)
                .EUt(131000)
                .duration(200)
                .save();

        SUPERMATERIAL_FORGING_RECIPES.builder("uhtc_x1000_ultra_high_temperature_composite_rough_blank")
                .inputItems(GTOTagPrefix.plate, GTOMaterials.ZirconiumCarbide, 7)
                .inputItems(GTOTagPrefix.dust, GTMaterials.Hafnium, 5)
                .inputItems(GTOTagPrefix.dust, GTOMaterials.TungstenAlloyYG10, 3)
                .inputItems(GTOTagPrefix.FIBER_MESH, GTOMaterials.SiliconCarbide, 4)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.UHTCX1000UltraHighTemperatureComposite, 2)
                .inputFluids(GTOMaterials.SilicaSol, 576)
                .EUt(131000)
                .duration(1500)
                .save();

        SINTERING_FURNACE_RECIPES.builder("uhtc_x1000_ultra_high_temperature_composite_block")
                .inputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.UHTCX1000UltraHighTemperatureComposite)
                .outputItems(GTOTagPrefix.block, GTOMaterials.UHTCX1000UltraHighTemperatureComposite)
                .EUt(131000)
                .blastFurnaceTemp(9600)
                .duration(500)
                .save();

        SUPERMATERIAL_FORGING_RECIPES.builder("zirconia_nickel_base_graded_composite_rough_blank")
                .inputItems(GTOTagPrefix.dust, GTOMaterials.Inconel242, 12)
                .inputItems(GTOTagPrefix.dust, GTOMaterials.ZirconiumOxide, 5)
                .inputItems(GTOTagPrefix.FIBER_MESH, GTOMaterials.StainlessSteel155Ph, 4)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.ZirconiaNickelBaseGradedComposite, 2)
                .inputFluids(GTMaterials.Nickel, 576)
                .EUt(131000)
                .duration(1500)
                .save();

        SINTERING_FURNACE_RECIPES.builder("zirconia_nickel_base_graded_composite_block")
                .inputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.ZirconiaNickelBaseGradedComposite)
                .outputItems(GTOTagPrefix.block, GTOMaterials.ZirconiaNickelBaseGradedComposite)
                .EUt(131000)
                .blastFurnaceTemp(9600)
                .duration(500)
                .save();

        SUPERMATERIAL_FORGING_RECIPES.builder("zirconia_nickel_base_graded_composite_rough_blank1")
                .inputItems(GTOTagPrefix.plate, GTOMaterials.BoronCarbideCompositeProtectiveCeramic, 9)
                .inputItems(GTOTagPrefix.plate, GTMaterials.TungstenCarbide, 5)
                .inputItems(GTOTagPrefix.plate, GTOMaterials.DepletedUraniumAlloy, 3)
                .inputItems(GTOTagPrefix.FIBER_MESH, GTOMaterials.Kevlar, 5)
                .inputItems(GTOTagPrefix.FIBER_MESH, GTOMaterials.SiliconCarbide, 5)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.ZirconiaNickelBaseGradedComposite, 3)
                .inputFluids(GTOMaterials.PhenolicResin, 576)
                .EUt(524200)
                .duration(1500)
                .save();

        SINTERING_FURNACE_RECIPES.builder("boron_carbide_composite_protective_ceramic_block2")
                .inputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.BoronCarbideCompositeProtectiveCeramic)
                .outputItems(GTOTagPrefix.block, GTOMaterials.BoronCarbideCompositeProtectiveCeramic)
                .EUt(131000)
                .blastFurnaceTemp(11000)
                .duration(500)
                .save();

        SUPERMATERIAL_FORGING_RECIPES.builder("carbon_nanotube_reinforced_nickel_matrix_composite_rough_blank3")
                .inputItems(GTOTagPrefix.foil, GTOMaterials.Inconel242, 16)
                .inputItems(GTOTagPrefix.dust, GTMaterials.Graphene, 5)
                .inputItems(GTOTagPrefix.dust, GTOMaterials.CarbonNanotubes, 3)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.CarbonNanotubeReinforcedNickelMatrixComposite)
                .inputFluids(GTMaterials.Nickel, 288)
                .EUt(524200)
                .duration(500)
                .save();

        THERMO_PRESSING_RECIPES.builder("carbon_nanotube_reinforced_nickel_matrix_composite_block4")
                .inputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.CarbonNanotubeReinforcedNickelMatrixComposite)
                .outputItems(GTOTagPrefix.block, GTOMaterials.CarbonNanotubeReinforcedNickelMatrixComposite)
                .EUt(131000)
                .duration(200)
                .save();

        SUPERMATERIAL_FORGING_RECIPES.builder("carbon_nanotube_reinforced_aluminum_matrix_composite_rough_blank5")
                .inputItems(GTOTagPrefix.dust, GTOMaterials.NanoAluminum, 6)
                .inputItems(GTOTagPrefix.dust, GTMaterials.Graphene, 3)
                .inputItems(GTOTagPrefix.dust, GTOMaterials.CarbonNanotubes, 2)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.CarbonNanotubeReinforcedAluminumMatrixComposite)
                .inputFluids(GTOMaterials.PolyurethaneResin, 1000)
                .EUt(524200)
                .duration(500)
                .save();

        THERMO_PRESSING_RECIPES.builder("carbon_nanotube_reinforced_aluminum_matrix_composite_rough_blank6")
                .inputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.CarbonNanotubeReinforcedAluminumMatrixComposite)
                .outputItems(GTOTagPrefix.block, GTOMaterials.CarbonNanotubeReinforcedAluminumMatrixComposite)
                .EUt(131000)
                .duration(200)
                .save();

        SUPERMATERIAL_FORGING_RECIPES.builder("fgm_tps10_thermal_protection_composite_rough_blank7")
                .inputItems(GTOTagPrefix.FLAKES, GTOMaterials.ZirconiumDiboride, 10)
                .inputItems(GTOTagPrefix.plate, GTOMaterials.SiliconCarbide, 8)
                .inputItems(GTOTagPrefix.dust, GTOMaterials.NanoScaleRhenium, 3)
                .inputItems(GTOTagPrefix.dust, GTOMaterials.NanoScaleMolybdenum, 7)
                .inputItems(GTOTagPrefix.FIBER_MESH, GTOMaterials.NanoScaleTungsten, 5)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.FGMTPSThermalProtectionComposite, 3)
                .inputFluids(GTOMaterials.PhosphoricAcidMagnesiumPhosphateCement, 576)
                .EUt(2097100)
                .duration(1500)
                .save();

        SINTERING_FURNACE_RECIPES.builder("fgm_tps10_thermal_protection_composite_block8")
                .inputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.FGMTPSThermalProtectionComposite)
                .outputItems(GTOTagPrefix.block, GTOMaterials.FGMTPSThermalProtectionComposite)
                .EUt(528200)
                .blastFurnaceTemp(13800)
                .duration(500)
                .save();

        SUPERMATERIAL_FORGING_RECIPES.builder("cfc_sic1500_carbon_fiber_reinforced_composite_rough_blan9k")
                .inputItems(GTOTagPrefix.plate, GTOMaterials.SiliconCarbide, 8)
                .inputItems(GTOTagPrefix.FIBER_MESH, GTOMaterials.T1500CarbonFiber, 4)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.CFCSIC1500CarbonFiberReinforcedComposite)
                .inputFluids(GTOMaterials.Paa, 288)
                .EUt(2097100)
                .duration(500)
                .save();

        THERMO_PRESSING_RECIPES.builder("cfc_sic1500_carbon_fiber_reinforced_composite_bloc7k")
                .inputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.CFCSIC1500CarbonFiberReinforcedComposite)
                .outputItems(GTOTagPrefix.block, GTOMaterials.CFCSIC1500CarbonFiberReinforcedComposite)
                .EUt(524200)
                .duration(200)
                .save();

        SUPERMATERIAL_FORGING_RECIPES.builder("uhtc_x2000_ultra_high_temperature_composite5_rough_blank")
                .inputItems(GTOTagPrefix.FLAKES, GTOMaterials.ZirconiumDiboride, 12)
                .inputItems(GTOTagPrefix.dust, GTMaterials.TantalumCarbide, 10)
                .inputItems(GTOTagPrefix.dust, GTMaterials.Hafnium, 5)
                .inputItems(GTOTagPrefix.FIBER_MESH, GTOMaterials.NanoScaleSiliconCarbide, 5)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.UHTCX2000UltraHighTemperatureComposite, 3)
                .inputFluids(GTOMaterials.PhosphoricAcidChromiumPhosphateCement, 576)
                .EUt(2097100)
                .duration(1500)
                .save();

        SINTERING_FURNACE_RECIPES.builder("uhtc_x2000_ultra_high_temperature_com3posite_block")
                .inputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.UHTCX2000UltraHighTemperatureComposite)
                .outputItems(GTOTagPrefix.block, GTOMaterials.UHTCX2000UltraHighTemperatureComposite)
                .EUt(528200)
                .blastFurnaceTemp(14200)
                .duration(500)
                .save();

        SUPERMATERIAL_FORGING_RECIPES.builder("titanium_dioxide_nanotube_reinforce2d_titanium_matrix_composite_rough_blank")
                .inputItems(GTOTagPrefix.dust, GTOMaterials.TitaniumTC21, 12)
                .inputItems(GTOTagPrefix.dust, GTOMaterials.TitaniumDioxideNanotubes, 6)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.TitaniumDioxideNanotubeReinforcedTitaniumMatrixComposite, 2)
                .inputFluids(GTMaterials.Titanium, 288)
                .EUt(83888600)
                .duration(1000)
                .save();

        SINTERING_FURNACE_RECIPES.builder("titanium_dioxide_nanotube_reinforce1d_titanium_matrix_composite_block")
                .inputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.TitaniumDioxideNanotubeReinforcedTitaniumMatrixComposite)
                .outputItems(GTOTagPrefix.block, GTOMaterials.TitaniumDioxideNanotubeReinforcedTitaniumMatrixComposite)
                .EUt(528200)
                .blastFurnaceTemp(14200)
                .duration(500)
                .save();

        SUPERMATERIAL_FORGING_RECIPES.builder("oxide_dispersion_strengthen2ed_nickel_base_superalloy_rough_blank")
                .inputItems(GTOTagPrefix.dust, GTOMaterials.Inconel242, 16)
                .inputItems(GTOTagPrefix.dust, GTOMaterials.DegenerateRhenium, 6)
                .inputItems(GTOTagPrefix.dust, GTOMaterials.YttriumOxide, 3)
                .inputItems(GTOTagPrefix.dust, GTMaterials.Ruthenium, 5)
                .inputItems(GTOTagPrefix.FIBER_MESH, GTMaterials.TungstenCarbide, 8)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.OxideDispersionStrengthenedNickelBaseSuperalloy, 5)
                .inputFluids(GTMaterials.Nickel, 1440)
                .EUt(8388608)
                .duration(2500)
                .save();
        SINTERING_FURNACE_RECIPES.builder("titanium_dioxide_nanotube_reinfor3ce1d_titanium_matrix_composite_block")
                .inputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.OxideDispersionStrengthenedNickelBaseSuperalloy)
                .outputItems(GTOTagPrefix.block, GTOMaterials.OxideDispersionStrengthenedNickelBaseSuperalloy)
                .EUt(528200)
                .blastFurnaceTemp(14200)
                .duration(500)
                .save();

        REACTION_FURNACE_RECIPES.builder("aluminum_nitride_dust")
                .inputItems(GTOTagPrefix.dust, GTOMaterials.Alumina)
                .inputItems(GTOTagPrefix.dust, GTMaterials.Carbon)
                .outputItems(GTOTagPrefix.dust, GTOMaterials.AluminumNitride)
                .inputFluids(GTMaterials.Nitrogen, 1000)
                .outputFluids(GTMaterials.CarbonMonoxide, 1000)
                .EUt(2040)
                .blastFurnaceTemp(1900)
                .duration(100)
                .save();

        ISOSTATIC_PRESSING_RECIPES.builder("aluminum_oxynitride_glass_ceramic_rough_blank")
                .inputItems(GTOTagPrefix.dust, GTOMaterials.AluminumNitride, 9)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.AluminumOxynitrideGlassCeramic)
                .inputFluids(GTMaterials.Epoxy, 1000)
                .EUt(32700)
                .duration(500)
                .save();

        SINTERING_FURNACE_RECIPES.builder("aluminum_oxynitride_glass_ceramic_block")
                .inputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.AluminumOxynitrideGlassCeramic)
                .outputItems(GTOTagPrefix.block, GTOMaterials.AluminumOxynitrideGlassCeramic)
                .inputFluids(GTMaterials.Neon, 1000)
                .EUt(8100)
                .blastFurnaceTemp(7900)
                .duration(500)
                .save();

        REACTION_FURNACE_RECIPES.builder("lead_oxide_dust")
                .inputItems(GTOTagPrefix.dust, GTMaterials.Lead)
                .outputItems(GTOTagPrefix.dust, GTOMaterials.LeadOxide)
                .inputFluids(GTMaterials.Oxygen, 1000)
                .EUt(510)
                .blastFurnaceTemp(2600)
                .duration(100)
                .save();

        REACTION_FURNACE_RECIPES.builder("lead_zirconate_titanate_ceramic_dust")
                .inputItems(GTOTagPrefix.dust, GTOMaterials.LeadOxide)
                .inputItems(GTOTagPrefix.dust, GTOMaterials.ZirconiumOxide, 4)
                .inputItems(GTOTagPrefix.dust, GTOMaterials.TitaniumDioxide, 3)
                .outputItems(GTOTagPrefix.dust, GTOMaterials.LeadZirconateTitanateCeramic, 6)
                .EUt(8100)
                .blastFurnaceTemp(2600)
                .duration(100)
                .save();

        THERMO_PRESSING_RECIPES.builder("lead_zirconate_titanate_shape_piezoelectric_ceramic_rough_blank")
                .inputItems(GTOTagPrefix.dust, GTOMaterials.LeadZirconateTitanateCeramic, 9)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.LeadZirconateTitanateShapePiezoelectricCeramic)
                .inputFluids(GTMaterials.Epoxy, 1000)
                .EUt(32700)
                .duration(500)
                .save();

        SINTERING_FURNACE_RECIPES.builder("lead_zirconate_titanate_shape_piezoelectric_ceramic_block")
                .inputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.LeadZirconateTitanateShapePiezoelectricCeramic)
                .outputItems(GTOTagPrefix.block, GTOMaterials.LeadZirconateTitanateShapePiezoelectricCeramic)
                .inputFluids(GTMaterials.Argon, 1000)
                .EUt(32760)
                .blastFurnaceTemp(11800)
                .duration(500)
                .save();

        SINTERING_FURNACE_RECIPES.builder("lead_magnesium_niobate_shape_piezoelectric_ceramic_block")
                .inputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.LeadMagnesiumNiobateShapePiezoelectricCeramic)
                .outputItems(GTOTagPrefix.block, GTOMaterials.LeadMagnesiumNiobateShapePiezoelectricCeramic)
                .inputFluids(GTMaterials.Argon, 1000)
                .EUt(32760)
                .blastFurnaceTemp(11800)
                .duration(500)
                .save();

        REACTION_FURNACE_RECIPES.builder("lead_niobate_magnesium_niobate_ceramic_dust")
                .inputItems(GTOTagPrefix.dust, GTOMaterials.LeadOxide, 3)
                .inputItems(GTOTagPrefix.dust, GTOMaterials.NiobiumOxide)
                .inputItems(GTOTagPrefix.dust, GTMaterials.Magnesia)
                .outputItems(GTOTagPrefix.dust, GTOMaterials.LeadNiobateMagnesiumNiobateCeramic, 3)
                .EUt(8100)
                .blastFurnaceTemp(2600)
                .duration(100)
                .save();

        REACTION_FURNACE_RECIPES.builder("niobium_oxide_dust")
                .inputItems(GTOTagPrefix.dust, GTMaterials.Niobium, 2)
                .outputItems(GTOTagPrefix.dust, GTOMaterials.NiobiumOxide)
                .inputFluids(GTMaterials.Oxygen, 5000)
                .EUt(500)
                .blastFurnaceTemp(1800)
                .duration(100)
                .save();

        THERMO_PRESSING_RECIPES.builder("lead_magnesium_niobate_shape_piezoelectric_ceramic_rough_blank")
                .inputItems(GTOTagPrefix.dust, GTOMaterials.LeadNiobateMagnesiumNiobateCeramic, 9)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.LeadMagnesiumNiobateShapePiezoelectricCeramic)
                .inputFluids(GTMaterials.ReinforcedEpoxyResin, 1000)
                .EUt(32700)
                .duration(500)
                .save();
    }
}
