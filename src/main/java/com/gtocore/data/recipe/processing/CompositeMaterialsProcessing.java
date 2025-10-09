package com.gtocore.data.recipe.processing;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gtolib.api.recipe.RecipeType;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import org.jetbrains.annotations.Nullable;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.api.data.tag.GTOTagPrefix.FIBER_MESH;
import static com.gtocore.api.data.tag.GTOTagPrefix.MXene;
import static com.gtocore.common.data.GTOMaterials.*;
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
                .inputItems(GTOTagPrefix.plateDouble, GTMaterials.EnrichedNaquadahTriniumEuropiumDuranide, 8)
                .outputItems(GTOTagPrefix.FIBER_TOW, GTOMaterials.T1500CarbonFiber, 2)
                .inputFluids(GTOMaterials.Orichalcum, FluidStorageKeys.PLASMA, 1000)
                .inputFluids(GTOMaterials.SupercriticalCarbonDioxide, 4000)
                .EUt(500000)
                .duration(200)
                .save();

        Material[] carbonFibers = new Material[] {
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
                    .inputFluids(resins[i], 1000)
                    .EUt(280)
                    .duration(2000)
                    .save();
            FIBER_EXTRUSION_RECIPES.builder("fiber_mesh_" + carbonFibers[i].getName())
                    .inputItems(GTOTagPrefix.FIBER, carbonFibers[i], 4)
                    .outputItems(FIBER_MESH, carbonFibers[i])
                    .circuitMeta(4)
                    .EUt(520)
                    .blastFurnaceTemp(3600)
                    .duration(500)
                    .save();
        }
    }

    private static void processCompositeMaterials() {
        // 玻璃钢 fiberglass_reinforced_plastic 液态 默认 #e8efe9 EV 酚醛树脂（液态） 环氧树脂 硼硅玻璃纤维 化学浸洗，固化
        CHEMICAL_BATH_RECIPES.builder("fiberglass_reinforced_plastic")
                .inputItems(FIBER_MESH, GTMaterials.BorosilicateGlass)
                .inputFluids(GTMaterials.Epoxy, 144)
                .outputFluids(GTOMaterials.FiberglassReinforcedPlastic, 576)
                .EUt(280)
                .duration(200)
                .save();
        // 名称 ID 预制体形态 零件/材料 色号 电压 基体材料 增强材料（粉） 粘合剂（液态） 增强纤维 配方类型（第一为预制体，第二为成品）
        // 石英纤维增强二氧化硅复合材料 quartz_fiber_reinforced_silica 粗矿块 弯曲板，双层板 #eff3f3 EV 二氧化硅（粉） 硅溶胶 石英纤维 化学浸洗，热压成型
        ProcessBuilder.builder("quartz_fiber_reinforced_silica")
                .base(GTMaterials.SiliconDioxide, dust)
                .reinforcement(GTMaterials.Quartzite)
                .adhesive(GTOMaterials.SilicaSol)
                .fiber(GTMaterials.Quartzite)
                .precursor(QuartzFiberReinforcedSilica, GTOTagPrefix.ROUGH_BLANK)
                .step1(CHEMICAL_BATH_RECIPES)
                .step2(THERMO_PRESSING_RECIPES)
                .voltage(VA[EV])
                .duration(400)
                .result(QuartzFiberReinforcedSilica)
                .process();
        // 碳化硅颗粒增强铝基复合材料 aluminum_reinforced_with_silicon_carbide_particles 粉 箔，杆 #133e5c EV 铝合金-2090（粉） 碳化硅 酚醛树脂 搅拌，烧结炉
        ProcessBuilder.builder("aluminum_reinforced_with_silicon_carbide_particles")
                .base(GTMaterials.Aluminium, dust)
                .reinforcement(GTOMaterials.SiliconCarbide)
                .adhesive(GTOMaterials.PhenolicResin)
                .fiber(Carbon)
                .precursor(GTOMaterials.AluminumReinforcedWithSiliconCarbideParticlesPre)
                .step1(MIXER_RECIPES)
                .step2(SINTERING_FURNACE_RECIPES)
                .voltage(VA[EV])
                .duration(300)
                .result(GTOMaterials.AluminumReinforcedWithSiliconCarbideParticles)
                .process();
        // 氧化铝纤维增强铝基复合材料 alumina_fiber_reinforced_aluminum_matrix_composite 粗矿块 杆，弯曲板 #e6f5ff EV 铝合金-5A06（液态） 强化环氧树脂
        // 氧化铝纤维 化学浸洗，热压成型
        ProcessBuilder.builder("alumina_fiber_reinforced_aluminum_matrix_composite")
                .base(GTMaterials.Aluminium)
                .reinforcement(GTOMaterials.Alumina)
                .adhesive(GTMaterials.ReinforcedEpoxyResin)
                .fiber(GTOMaterials.Alumina)
                .precursor(GTOMaterials.AluminaFiberReinforcedAluminumMatrixComposite, GTOTagPrefix.ROUGH_BLANK)
                .step1(CHEMICAL_BATH_RECIPES)
                .step2(THERMO_PRESSING_RECIPES)
                .voltage(VA[EV])
                .duration(400)
                .result(GTOMaterials.AluminaFiberReinforcedAluminumMatrixComposite)
                .process();
        // 石墨-铜复合材料 graphite_copper_composite 粉 箔，环，杆，管道 #534f17 EV 青铜（粉） 石墨 铜 搅拌机，烧结炉
        ProcessBuilder.builder("graphite_copper_composite")
                .base(GTMaterials.Bronze, dust)
                .reinforcement(GTMaterials.Graphite)
                .adhesive(Copper)
                .fiber(Carbon)
                .precursor(GTOMaterials.GraphiteCopperCompositePre)
                .step1(MIXER_RECIPES)
                .step2(SINTERING_FURNACE_RECIPES)
                .voltage(VA[EV])
                .duration(300)
                .result(GTOMaterials.GraphiteCopperComposite)
                .process();
        // 硼硅纤维增强铝基复合材料 borosilicate_fiber_reinforced_aluminum_matrix_composite 粗矿块 杆，框架，双层板 #f6ffff IV 铝合金-8090（箔） 酚醛树脂
        // 硼硅玻璃纤维 物理气相沉积，热压成型
        ProcessBuilder.builder("borosilicate_fiber_reinforced_aluminum_matrix_composite")
                .base(AluminumAlloy8090, foil)
                .reinforcement(GTMaterials.BorosilicateGlass)
                .adhesive(GTOMaterials.PhenolicResin)
                .fiber(GTMaterials.BorosilicateGlass)
                .precursor(GTOMaterials.BorosilicateFiberReinforcedAluminumMatrixComposite, GTOTagPrefix.ROUGH_BLANK)
                .step1(PHYSICAL_VAPOR_DEPOSITION_RECIPES)
                .step2(THERMO_PRESSING_RECIPES)
                .voltage(VA[IV])
                .duration(400)
                .result(GTOMaterials.BorosilicateFiberReinforcedAluminumMatrixComposite)
                .process();
        // 弥散强化铜 dispersion_strengthened_copper 粉 箔，杆 #c87d0a IV 纯铜（粉） 氧化铝 铜 搅拌，热压成型
        ProcessBuilder.builder("dispersion_strengthened_copper")
                .base(Copper, dust)
                .reinforcement(GTOMaterials.Alumina)
                .adhesive(Copper)
                .fiber(Carbon)
                .precursor(GTOMaterials.DispersionStrengthenedCopperPre)
                .step1(MIXER_RECIPES)
                .step2(THERMO_PRESSING_RECIPES)
                .voltage(VA[IV])
                .duration(300)
                .result(GTOMaterials.DispersionStrengthenedCopper)
                .process();
        // 氧化物弥散强化镍基合金 oxide_dispersion_strengthened_nickel_based_alloy 粗矿块 杆，框架，双层板 #106643 IV 镍铬基合金-617（粉） 氧化钇 镍 钛纤维
        // 搅拌机，烧结炉
        ProcessBuilder.builder("oxide_dispersion_strengthened_nickel_based_alloy")
                .base(Inconel617, dust)
                .reinforcement(GTOMaterials.YttriumOxide)
                .adhesive(GTMaterials.Nickel)
                .fiber(Titanium)
                .precursor(GTOMaterials.OxideDispersionStrengthenedNickelBasedAlloy, GTOTagPrefix.ROUGH_BLANK)
                .step1(MIXER_RECIPES)
                .step2(SINTERING_FURNACE_RECIPES)
                .voltage(VA[IV])
                .duration(400)
                .result(GTOMaterials.OxideDispersionStrengthenedNickelBasedAlloy)
                .process();
        // 钛-钢复合材料 titanium_Steel_composite 粗矿块 弯曲板，双层板 #bb7390 IV 钛合金-TI64（箔） 不锈钢GC4（箔） 铜，镍 等静压成型，热压成型
        ProcessBuilder.builder("titanium_steel_composite")
                .base(TitaniumTi64, foil)
                .reinforcement(StainlessSteel155Ph, foil, 8)
                .adhesive(Copper)
                .fiber(StainlessSteel155Ph)
                .precursor(TitaniumSteelComposite, GTOTagPrefix.ROUGH_BLANK)
                .step1(CHEMICAL_BATH_RECIPES)
                .step2(THERMO_PRESSING_RECIPES)
                .voltage(VA[IV])
                .duration(300)
                .result(GTOMaterials.TitaniumSteelComposite)
                .process();
        // 二氧化硅-碳复合材料 silica_carbon_composite 粗矿块 弯曲板，双层板 #141417 IV 二氧化硅（粉） 石墨 酚醛树脂 等静压成型，烧结炉
        ProcessBuilder.builder("silica_carbon_composite")
                .base(GTMaterials.SiliconDioxide, dust)
                .reinforcement(GTMaterials.Graphite)
                .adhesive(GTOMaterials.PhenolicResin)
                .fiber(Carbon)
                .precursor(GTOMaterials.SilicaCarbonComposite, GTOTagPrefix.ROUGH_BLANK)
                .step1(CHEMICAL_BATH_RECIPES)
                .step2(SINTERING_FURNACE_RECIPES)
                .voltage(VA[IV])
                .duration(300)
                .result(GTOMaterials.SilicaCarbonComposite)
                .process();
        // 碳纤维-环氧树脂复合材料 carbon_fiber_epoxy_composite 粗矿块 杆，框架，双层板 #8d8c31 IV 环氧树脂 碳纤维T300 化学浸洗，热压成型
        ProcessBuilder.builder("carbon_fiber_epoxy_composite")
                .base(GTMaterials.Epoxy)
                .reinforcement(Titanium3Carbide, MXene, 2)
                .adhesive(GTMaterials.Epoxy)
                .fiber(GTOMaterials.T300CarbonFiber)
                .precursor(GTOMaterials.CarbonFiberEpoxyComposite, GTOTagPrefix.ROUGH_BLANK)
                .step1(CHEMICAL_BATH_RECIPES)
                .step2(THERMO_PRESSING_RECIPES)
                .voltage(VA[IV])
                .duration(300)
                .result(GTOMaterials.CarbonFiberEpoxyComposite)
                .process();
        // 碳纤维-强化环氧树脂复合材料 carbon_fiber_reinforced_epoxy_composite 粗矿块 杆，框架，双层板 #225b27 IV 强化环氧树脂 碳纤维T400 化学浸洗，热压成型
        ProcessBuilder.builder("carbon_fiber_reinforced_epoxy_composite")
                .base(GTMaterials.ReinforcedEpoxyResin)
                .reinforcement(SiliconNitrideCeramic, MXene, 2)
                .adhesive(GTMaterials.ReinforcedEpoxyResin)
                .fiber(T400CarbonFiber)
                .precursor(GTOMaterials.CarbonFiberReinforcedEpoxyComposite, GTOTagPrefix.ROUGH_BLANK)
                .step1(CHEMICAL_BATH_RECIPES)
                .step2(THERMO_PRESSING_RECIPES)
                .voltage(VA[IV])
                .duration(300)
                .result(GTOMaterials.CarbonFiberReinforcedEpoxyComposite)
                .process();
        // 碳钎维-酚醛树脂复合材料 carbon_fiber_phenolic_resin_composite 粗矿块 杆，框架，双层板 #531e1e IV 酚醛树脂 碳纤维T700 化学浸洗，热压成型
        ProcessBuilder.builder("carbon_fiber_phenolic_resin_composite")
                .base(GTOMaterials.PhenolicResin)
                .reinforcement(TantalumCarbide, MXene, 2)
                .adhesive(GTOMaterials.PhenolicResin)
                .fiber(GTOMaterials.T700CarbonFiber)
                .precursor(GTOMaterials.CarbonFiberPhenolicResinComposite, GTOTagPrefix.ROUGH_BLANK)
                .step1(CHEMICAL_BATH_RECIPES)
                .step2(THERMO_PRESSING_RECIPES)
                .voltage(VA[IV])
                .duration(300)
                .result(GTOMaterials.CarbonFiberPhenolicResinComposite)
                .process();
        // 碳纤维-聚苯硫醚复合材料 carbon_fiber_polyphenylene_sulfide_composite 粗矿块 杆，框架，双层板 #24221d IV 聚苯硫醚 碳纤维T600 化学浸洗，热压成型
        ProcessBuilder.builder("carbon_fiber_polyphenylene_sulfide_composite")
                .base(PolyphenyleneSulfide)
                .reinforcement(NiobiumNitride, MXene, 2)
                .adhesive(PolyphenyleneSulfide)
                .fiber(GTOMaterials.T600CarbonFiber)
                .precursor(GTOMaterials.CarbonFiberPolyphenyleneSulfideComposite, GTOTagPrefix.ROUGH_BLANK)
                .step1(CHEMICAL_BATH_RECIPES)
                .step2(THERMO_PRESSING_RECIPES)
                .voltage(VA[IV])
                .duration(400)
                .result(GTOMaterials.CarbonFiberPolyphenyleneSulfideComposite)
                .process();
        // 氮氧化铝玻璃陶瓷 aluminum_oxynitride_glass_ceramic 粗矿块 同陶瓷 #dedede LUV 氮氧化铝（粉） 环氧树脂 等静压成型，烧结炉
        ProcessBuilder.builder("aluminum_oxynitride_glass_ceramic")
                .base(AluminumNitride, dust)
                .reinforcement(ThuliumHexaborideCeramics, MXene, 2)
                .adhesive(GTMaterials.Epoxy)
                .fiber(Alumina)
                .precursor(GTOMaterials.AluminumOxynitrideGlassCeramic, GTOTagPrefix.ROUGH_BLANK)
                .step1(CHEMICAL_BATH_RECIPES)
                .step2(SINTERING_FURNACE_RECIPES)
                .voltage(VA[LuV])
                .duration(500)
                .result(GTOMaterials.AluminumOxynitrideGlassCeramic)
                .process();
        // 碳化硅纤维强化镍基复合材料 silicon_carbide_fiber_reinforced_nickel_based_composite 粗矿块 弯曲板，双层板 #1d4a2f LUV 镍铬基合金-X750（熔融）
        // 镍 碳化硅纤维 搅拌机，热压成型
        ProcessBuilder.builder("silicon_carbide_fiber_reinforced_nickel_based_composite")
                .base(InconelX750)
                .reinforcement(ZirconiumCarbide, MXene, 2)
                .adhesive(GTMaterials.Nickel)
                .fiber(GTOMaterials.SiliconCarbide)
                .precursor(GTOMaterials.SiliconCarbideFiberReinforcedNickelBasedComposite, GTOTagPrefix.ROUGH_BLANK)
                .step1(MIXER_RECIPES)
                .step2(THERMO_PRESSING_RECIPES)
                .voltage(VA[LuV])
                .duration(300)
                .result(GTOMaterials.SiliconCarbideFiberReinforcedNickelBasedComposite)
                .process();
        // 碳化硅纤维增强钛基复合材料 silicon_carbide_fiber_reinforced_titanium_matrix_composite 粗矿块 转子 #6d4041 LUV 钛合金TB6（箔） 钛 碳化硅纤维
        // 等静压成型，热压成型
        ProcessBuilder.builder("silicon_carbide_fiber_reinforced_titanium_matrix_composite")
                .base(TitaniumTB6, foil)
                .reinforcement(ZirconiumCarbide, MXene, 2)
                .adhesive(Titanium)
                .fiber(GTOMaterials.SiliconCarbide)
                .precursor(GTOMaterials.SiliconCarbideFiberReinforcedTitaniumMatrixComposite, GTOTagPrefix.ROUGH_BLANK)
                .step1(CHEMICAL_BATH_RECIPES)
                .step2(THERMO_PRESSING_RECIPES)
                .voltage(VA[LuV])
                .duration(400)
                .result(GTOMaterials.SiliconCarbideFiberReinforcedTitaniumMatrixComposite)
                .process();
        // 钨纤维强化钴基复合材料 tungsten_fiber_reinforced_cobalt_based_composite 粗矿块 弯曲板，双层板 #171a3c LUV 铬钴锰钛合金（箔） 钴 钨纤维
        // 等静压成型，热压成型
        ProcessBuilder.builder("tungsten_fiber_reinforced_cobalt_based_composite")
                .base(Stellite, foil)
                .reinforcement(TungstenCarbide, MXene, 2)
                .adhesive(Cobalt)
                .fiber(Tungsten)
                .precursor(GTOMaterials.TungstenFiberReinforcedCobaltBasedComposite, GTOTagPrefix.ROUGH_BLANK)
                .step1(CHEMICAL_BATH_RECIPES)
                .step2(THERMO_PRESSING_RECIPES)
                .voltage(VA[LuV])
                .duration(300)
                .result(GTOMaterials.TungstenFiberReinforcedCobaltBasedComposite)
                .process();
        // 碳纤维-聚酰亚胺复合材料 carbon_fiber_polyimide_composite 粗矿块 箔，杆 #c68430 LUV 聚酰亚胺 碳纤维T800 化学浸洗，热压成型
        ProcessBuilder.builder("carbon_fiber_polyimide_composite")
                .base(GTOMaterials.Polyimide)
                .reinforcement(TitaniumNitrideCeramic, MXene, 2)
                .adhesive(GTOMaterials.Polyimide)
                .fiber(GTOMaterials.T800CarbonFiber)
                .precursor(GTOMaterials.CarbonFiberPolyimideComposite, GTOTagPrefix.ROUGH_BLANK)
                .step1(CHEMICAL_BATH_RECIPES)
                .step2(THERMO_PRESSING_RECIPES)
                .voltage(VA[LuV])
                .duration(400)
                .result(GTOMaterials.CarbonFiberPolyimideComposite)
                .process();
    }

    record Process(Object base, ItemStack reinforcement, Material adhesive, Material fiber, ItemStack precursor, RecipeType step1, RecipeType step2, long voltage, int duration, Material result, String id) {

        boolean isBaseFluid() {
            return base instanceof FluidStack;
        }

        void processStep1() {
            if (isBaseFluid()) {
                step1.builder("material_synthesis_p1_" + id)
                        .inputItems(FIBER_MESH, fiber)
                        .inputFluids((FluidStack) base)
                        .outputItems(precursor)
                        .EUt(voltage)
                        .duration(duration)
                        .save();
            } else {
                step1.builder("material_synthesis_p1_" + id)
                        .inputItems(FIBER_MESH, fiber)
                        .inputItems((ItemStack) base)
                        .outputItems(precursor)
                        .EUt(voltage)
                        .duration(duration)
                        .save();
            }
        }

        void processStep2() {
            step2.builder("material_synthesis_p2_" + id)
                    .inputItems(precursor)
                    .outputItems(plate, result, 9)
                    .inputFluids(adhesive, 1000)
                    .inputItems(reinforcement)
                    .EUt(voltage)
                    .duration(duration)
                    .save();
        }
    }

    static class ProcessBuilder {

        private Object base;
        private ItemStack reinforcement;
        private Material adhesive;
        private Material fiber;
        private ItemStack precursor;
        private RecipeType step1;
        private RecipeType step2;
        private long voltage;
        private int duration;
        private Material result;
        private String id;

        public static ProcessBuilder builder(String id) {
            return new ProcessBuilder().id(id);
        }

        private ProcessBuilder id(String id) {
            this.id = id;
            return this;
        }

        public ProcessBuilder base(Material base) {
            this.base = base.getFluid(4 * L);
            return this;
        }

        public ProcessBuilder base(Material base, TagPrefix prefix) {
            this.base = ChemicalHelper.get(prefix, base, 4);
            return this;
        }

        public ProcessBuilder reinforcement(Material reinforcement) {
            this.reinforcement = ChemicalHelper.get(dust, reinforcement, 2);
            return this;
        }

        public ProcessBuilder reinforcement(Material reinforcement, TagPrefix prefix, int amount) {
            this.reinforcement = ChemicalHelper.get(prefix, reinforcement, amount);
            return this;
        }

        public ProcessBuilder adhesive(Material adhesive) {
            this.adhesive = adhesive;
            return this;
        }

        public ProcessBuilder fiber(Material fiber) {
            this.fiber = fiber;
            return this;
        }

        public ProcessBuilder precursor(Material precursor, TagPrefix prefix) {
            this.precursor = ChemicalHelper.get(prefix, precursor, prefix == dust ? 9 : 1);
            return this;
        }

        public ProcessBuilder precursor(Material precursor) {
            this.precursor = ChemicalHelper.get(dust, precursor, 9);
            return this;
        }

        public ProcessBuilder step1(RecipeType step1) {
            this.step1 = step1;
            return this;
        }

        public ProcessBuilder step2(RecipeType step2) {
            this.step2 = step2;
            return this;
        }

        public ProcessBuilder voltage(long voltage) {
            this.voltage = voltage;
            return this;
        }

        public ProcessBuilder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public ProcessBuilder result(Material result) {
            this.result = result;
            return this;
        }

        public void process() {
            Process resultProcess = new Process(base, reinforcement, adhesive, fiber, precursor, step1, step2, voltage, duration, result, id);
            resultProcess.processStep1();
            resultProcess.processStep2();
        }
    }
}
