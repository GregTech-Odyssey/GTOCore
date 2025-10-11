package com.gtocore.common.data.material;

import com.gregtechceu.gtceu.api.GTValues;

import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty.GasTier.HIGH;
import static com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty.GasTier.LOW;
import static com.gtocore.api.data.material.GTOMaterialFlags.*;
import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtocore.common.data.GTOMaterials.CarbonFiberPolyetheretherketoneComposite;
import static com.gtolib.utils.register.MaterialsRegisterUtils.material;

public class MaterialComposite {

    public static void init() {
        // 玻璃钢 fiberglass_reinforced_plastic 默认 #e8efe9 EV
        // 石英纤维增强二氧化硅复合材料 quartz_fiber_reinforced_silica 弯曲板，双层板 #eff3f3 EV
        // 碳化硅颗粒增强铝基复合材料 aluminum_reinforced_with_silicon_carbide_particles 箔，杆 #133e5c EV
        // 氧化铝纤维增强铝基复合材料 alumina_fiber_reinforced_aluminum_matrix_composite 杆，弯曲板 #e6f5ff EV
        // 石墨-铜复合材料 graphite_copper_composite 箔，环，杆，管道 #534f17 EV
        // 硼硅纤维增强铝基复合材料 borosilicate_fiber_reinforced_aluminum_matrix_composite 杆，框架，双层板 #f6ffff IV
        // 弥散强化铜 dispersion_strengthened_copper 箔，杆 #c87d0a IV
        // 氧化物弥散强化镍基合金 oxide_dispersion_strengthened_nickel_based_alloy 杆，框架，双层板 #106643 IV
        // 钛-钢复合材料 titanium_steel_composite 弯曲板，双层板 #bb7390 IV
        // 二氧化硅-碳复合材料 silica_carbon_composite 弯曲板，双层板 #141417 IV
        // 碳纤维-环氧树脂复合材料 carbon_fiber_epoxy_composite 杆，框架，双层板 #8d8c31 IV
        // 碳纤维-强化环氧树脂复合材料 carbon_fiber_reinforced_epoxy_composite 杆，框架，双层板 #225b27 IV
        // 碳钎维-酚醛树脂复合材料 carbon_fiber_phenolic_resin_composite 杆，框架，双层板 #531e1e IV
        // 碳纤维-聚苯硫醚复合材料 carbon_fiber_polyphenylene_sulfide_composite 杆，框架，双层板 #24221d IV
        // 氮氧化铝玻璃陶瓷 aluminum_oxynitride_glass_ceramic 同陶瓷 #dedede LUV
        // 碳化硅纤维强化镍基复合材料 silicon_carbide_fiber_reinforced_nickel_based_composite 弯曲板，双层板 #1d4a2f LUV
        // 碳化硅纤维增强钛基复合材料 silicon_carbide_fiber_reinforced_titanium_matrix_composite 转子 #6d4041 LUV
        // 钨纤维强化钴基复合材料 tungsten_fiber_reinforced_cobalt_based_composite 弯曲板，双层板 #171a3c LUV
        // 碳纤维-聚酰亚胺复合材料 carbon_fiber_polyimide_composite 箔，杆 #c68430 LUV
        // 碳纤维-PEEK复合材料 carbon_fiber_peek_composite 箔，杆 #3a3a3a LUV
        //

        FiberglassReinforcedPlastic = material("fiberglass_reinforced_plastic", "玻璃钢")
                .ingot().fluid()
                .color(0xe8efe9)
                .secondaryColor(0xb7c6b7)
                .blastTemp(1650, LOW, GTValues.VA[GTValues.EV], 600)
                .iconSet(GLASS)
                .flags(GENERATE_PLATE)
                .buildAndRegister();
        QuartzFiberReinforcedSilica = material("quartz_fiber_reinforced_silica", "石英纤维增强二氧化硅复合材料")
                .ingot().fluid()
                .color(0xeff3f3)
                .secondaryColor(0xc7d3d3)
                .blastTemp(1720, LOW, GTValues.VA[GTValues.EV], 600)
                .iconSet(GLASS)
                .flags(GENERATE_PLATE, GENERATE_CURVED_PLATE, COMPOSITE_MATERIAL)
                .buildAndRegister();
        AluminumReinforcedWithSiliconCarbideParticles = material("aluminum_reinforced_with_silicon_carbide_particles", "碳化硅颗粒增强铝基复合材料")
                .ingot().fluid()
                .color(0x133e5c)
                .secondaryColor(0x0d2a3f)
                .blastTemp(2020, LOW, GTValues.VA[GTValues.EV], 600)
                .iconSet(BRIGHT)
                .flags(GENERATE_PLATE, COMPOSITE_MATERIAL)
                .flags(GENERATE_FOIL, GENERATE_LONG_ROD)
                .buildAndRegister();
        AluminumReinforcedWithSiliconCarbideParticlesPre = material("aluminum_reinforced_with_silicon_carbide_particles_pre", "碳化硅颗粒增强铝基复合材料(预)")
                .dust()
                .color(0x133e5c)
                .secondaryColor(0x0d2a3f)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();
        AluminaFiberReinforcedAluminumMatrixComposite = material("alumina_fiber_reinforced_aluminum_matrix_composite", "氧化铝纤维增强铝基复合材料")
                .ingot().fluid()
                .color(0xe6f5ff)
                .secondaryColor(0xb7d9ff)
                .blastTemp(2080, LOW, GTValues.VA[GTValues.EV], 600)
                .iconSet(SHINY)
                .flags(GENERATE_LONG_ROD, GENERATE_PLATE, GENERATE_CURVED_PLATE, COMPOSITE_MATERIAL)
                .buildAndRegister();
        GraphiteCopperComposite = material("graphite_copper_composite", "石墨-铜复合材料")
                .ingot().fluid()
                .color(0x534f17)
                .secondaryColor(0x3a3611)
                .blastTemp(2920, LOW, GTValues.VA[GTValues.EV], 600)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE)
                .flags(GENERATE_FOIL, GENERATE_RING, GENERATE_LONG_ROD, COMPOSITE_MATERIAL)
                .fluidPipeProperties(500, 3500, true, true, true)
                .buildAndRegister();
        GraphiteCopperCompositePre = material("graphite_copper_composite_pre", "石墨-铜复合材料(预)")
                .dust()
                .color(0x534f17)
                .secondaryColor(0x3a3611)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();
        BorosilicateFiberReinforcedAluminumMatrixComposite = material("borosilicate_fiber_reinforced_aluminum_matrix_composite", "硼硅纤维增强铝基复合材料")
                .ingot().fluid()
                .color(0xf6ffff)
                .secondaryColor(0xb7f1f1)
                .blastTemp(2310, HIGH, GTValues.VA[GTValues.IV], 600)
                .iconSet(BRIGHT)
                .flags(GENERATE_LONG_ROD, GENERATE_FRAME, GENERATE_PLATE, GENERATE_CURVED_PLATE, COMPOSITE_MATERIAL)
                .buildAndRegister();
        DispersionStrengthenedCopper = material("dispersion_strengthened_copper", "弥散强化铜")
                .ingot().fluid()
                .color(0xc87d0a)
                .secondaryColor(0x8b5307)
                .blastTemp(3240, HIGH, GTValues.VA[GTValues.IV], 600)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE)
                .flags(GENERATE_FOIL, GENERATE_LONG_ROD, COMPOSITE_MATERIAL)
                .buildAndRegister();
        DispersionStrengthenedCopperPre = material("dispersion_strengthened_copper_pre", "弥散强化铜(预)")
                .dust()
                .color(0xc87d0a)
                .secondaryColor(0x8b5307)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();
        OxideDispersionStrengthenedNickelBasedAlloy = material("oxide_dispersion_strengthened_nickel_based_alloy", "氧化物弥散强化镍基合金")
                .ingot().fluid()
                .color(0x106643)
                .secondaryColor(0x0a3a28)
                .blastTemp(3540, HIGH, GTValues.VA[GTValues.IV], 600)
                .iconSet(METALLIC)
                .flags(GENERATE_LONG_ROD, GENERATE_FRAME, GENERATE_PLATE, GENERATE_CURVED_PLATE, COMPOSITE_MATERIAL)
                .buildAndRegister();
        TitaniumSteelComposite = material("titanium_steel_composite", "钛-钢复合材料")
                .ingot().fluid()
                .color(0xbb7390)
                .secondaryColor(0x7a4c5e)
                .blastTemp(3680, HIGH, GTValues.VA[GTValues.IV], 600)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, GENERATE_CURVED_PLATE, GENERATE_LONG_ROD, COMPOSITE_MATERIAL)
                .buildAndRegister();
        SilicaCarbonComposite = material("silica_carbon_composite", "二氧化硅-碳复合材料")
                .ingot().fluid()
                .color(0x141417)
                .secondaryColor(0x0d0d0e)
                .blastTemp(3920, HIGH, GTValues.VA[GTValues.IV], 600)
                .iconSet(BRIGHT)
                .flags(GENERATE_PLATE, GENERATE_CURVED_PLATE, GENERATE_LONG_ROD, COMPOSITE_MATERIAL)
                .buildAndRegister();
        CarbonFiberEpoxyComposite = material("carbon_fiber_epoxy_composite", "碳纤维-环氧树脂复合材料")
                .ingot().fluid()
                .color(0x8d8c31)
                .secondaryColor(0x5e5d21)
                .blastTemp(4120, HIGH, GTValues.VA[GTValues.IV], 600)
                .iconSet(BRIGHT)
                .flags(GENERATE_PLATE, GENERATE_LONG_ROD, GENERATE_FRAME, GENERATE_CURVED_PLATE, COMPOSITE_MATERIAL)
                .buildAndRegister();
        CarbonFiberReinforcedEpoxyComposite = material("carbon_fiber_reinforced_epoxy_composite", "碳纤维-强化环氧树脂复合材料")
                .ingot().fluid()
                .color(0x225b27)
                .secondaryColor(0x163a18)
                .blastTemp(4280, HIGH, GTValues.VA[GTValues.IV], 600)
                .iconSet(BRIGHT)
                .flags(GENERATE_PLATE, GENERATE_LONG_ROD, GENERATE_FRAME, GENERATE_CURVED_PLATE, COMPOSITE_MATERIAL)
                .buildAndRegister();
        CarbonFiberPhenolicResinComposite = material("carbon_fiber_phenolic_resin_composite", "碳钎维-酚醛树脂复合材料")
                .ingot().fluid()
                .color(0x531e1e)
                .secondaryColor(0x361414)
                .blastTemp(4380, HIGH, GTValues.VA[GTValues.IV], 600)
                .iconSet(SHINY)
                .flags(GENERATE_PLATE, GENERATE_LONG_ROD, GENERATE_FRAME, GENERATE_CURVED_PLATE, COMPOSITE_MATERIAL)
                .buildAndRegister();
        CarbonFiberPolyphenyleneSulfideComposite = material("carbon_fiber_polyphenylene_sulfide_composite", "碳纤维-聚苯硫醚复合材料")
                .ingot().fluid()
                .color(0x24221d)
                .secondaryColor(0x171614)
                .blastTemp(4480, HIGH, GTValues.VA[GTValues.IV], 600)
                .iconSet(BRIGHT)
                .flags(GENERATE_PLATE, GENERATE_LONG_ROD, GENERATE_FRAME, GENERATE_CURVED_PLATE, COMPOSITE_MATERIAL)
                .buildAndRegister();
        AluminumOxynitrideGlassCeramic = material("aluminum_oxynitride_glass_ceramic", "氮氧化铝玻璃陶瓷")
                .ingot().fluid()
                .color(0xdedede)
                .secondaryColor(0xa1a1a1)
                .blastTemp(2050, LOW, GTValues.VA[GTValues.HV], 600)
                .iconSet(GLASS)
                .flags(GENERATE_PLATE, COMPOSITE_MATERIAL)
                .buildAndRegister();
        SiliconCarbideFiberReinforcedNickelBasedComposite = material("silicon_carbide_fiber_reinforced_nickel_based_composite", "碳化硅纤维强化镍基复合材料")
                .ingot().fluid()
                .color(0x1d4a2f)
                .secondaryColor(0x102d1a)
                .blastTemp(4680, HIGH, GTValues.VA[GTValues.LuV], 600)
                .iconSet(SHINY)
                .flags(GENERATE_PLATE, GENERATE_CURVED_PLATE, GENERATE_LONG_ROD, COMPOSITE_MATERIAL)
                .buildAndRegister();
        SiliconCarbideFiberReinforcedTitaniumMatrixComposite = material("silicon_carbide_fiber_reinforced_titanium_matrix_composite", "碳化硅纤维增强钛基复合材料")
                .ingot().fluid()
                .color(0x6d4041)
                .secondaryColor(0x4c292a)
                .blastTemp(4780, HIGH, GTValues.VA[GTValues.LuV], 600)
                .iconSet(SHINY)
                .flags(GENERATE_PLATE)
                .flags(GENERATE_ROTOR, COMPOSITE_MATERIAL)
                .buildAndRegister();
        TungstenFiberReinforcedCobaltBasedComposite = material("tungsten_fiber_reinforced_cobalt_based_composite", "钨纤维强化钴基复合材料")
                .ingot().fluid()
                .color(0x171a3c)
                .secondaryColor(0x0d0e22)
                .blastTemp(4840, HIGH, GTValues.VA[GTValues.LuV], 600)
                .iconSet(BRIGHT)
                .flags(GENERATE_PLATE, GENERATE_CURVED_PLATE, GENERATE_LONG_ROD, COMPOSITE_MATERIAL)
                .buildAndRegister();
        CarbonFiberPolyimideComposite = material("carbon_fiber_polyimide_composite", "碳纤维-聚酰亚胺复合材料")
                .ingot().fluid()
                .color(0xc68430)
                .secondaryColor(0x8b5420)
                .blastTemp(4980, HIGH, GTValues.VA[GTValues.LuV], 600)
                .iconSet(SHINY)
                .flags(GENERATE_PLATE)
                .flags(GENERATE_FOIL, GENERATE_LONG_ROD, COMPOSITE_MATERIAL)
                .buildAndRegister();
        CarbonFiberPolyetheretherketoneComposite = material("carbon_fiber_peek_composite", "碳纤维-PEEK复合材料")
                .ingot().fluid()
                .color(0x3a3a3a)
                .secondaryColor(0x262626)
                .blastTemp(5120, HIGH, GTValues.VA[GTValues.LuV], 600)
                .iconSet(GLASS)
                .flags(GENERATE_PLATE)
                .flags(GENERATE_FOIL, GENERATE_LONG_ROD, COMPOSITE_MATERIAL)
                .buildAndRegister();

        // 纳米碳化硅 nano_scale_silicon_carbide 默认 #76dfcf
        // 纳米钨 nano_scale_tungsten 默认 #a3bb51
        // T300碳纤维原丝
        // T400碳纤维原丝
        // T600碳纤维原丝
        // T700碳纤维原丝
        // T800碳纤维原丝
        // T900碳纤维原丝
        // T1000碳纤维原丝
        // T1200碳纤维原丝
        // T1500碳纤维原丝

        NanoScaleSiliconCarbide = material("nano_scale_silicon_carbide", "纳米碳化硅")
                .dust()
                .color(0x76dfcf)
                .secondaryColor(0x4accca)
                .iconSet(BRIGHT)
                .flags(GENERATE_FIBER)
                .blastTemp(5000, HIGH, GTValues.VA[GTValues.LuV], 600)
                .buildAndRegister();
        NanoScaleTungsten = material("nano_scale_tungsten", "纳米钨")
                .dust()
                .color(0xa3bb51)
                .secondaryColor(0x7d8c3a)
                .iconSet(BRIGHT)
                .flags(GENERATE_FIBER)
                .blastTemp(6000, HIGH, GTValues.VA[GTValues.LuV], 600)
                .buildAndRegister();
        T300CarbonFiber = material("t300_carbon_fiber_tow", "T300碳")
                .dust()
                .color(0x3a3a3a)
                .secondaryColor(0x262626)
                .iconSet(BRIGHT)
                .flags(GENERATE_FIBER, IS_CARBON_FIBER)
                .buildAndRegister();
        T400CarbonFiber = material("t400_carbon_fiber_tow", "T400碳")
                .dust()
                .color(0x4a4a4a)
                .secondaryColor(0x333333)
                .iconSet(BRIGHT)
                .flags(GENERATE_FIBER, IS_CARBON_FIBER)
                .buildAndRegister();
        T600CarbonFiber = material("t600_carbon_fiber_tow", "T600碳")
                .dust()
                .color(0x5a5a5a)
                .secondaryColor(0x3d3d3d)
                .iconSet(BRIGHT)
                .flags(GENERATE_FIBER, IS_CARBON_FIBER)
                .buildAndRegister();
        T700CarbonFiber = material("t700_carbon_fiber_tow", "T700碳")
                .dust()
                .color(0x6d6d6d)
                .secondaryColor(0x4a4a4a)
                .iconSet(BRIGHT)
                .flags(GENERATE_FIBER, IS_CARBON_FIBER)
                .buildAndRegister();
        T800CarbonFiber = material("t800_carbon_fiber_tow", "T800碳")
                .dust()
                .color(0x7d7d7d)
                .secondaryColor(0x555555)
                .iconSet(BRIGHT)
                .flags(GENERATE_FIBER, IS_CARBON_FIBER)
                .buildAndRegister();
        T900CarbonFiber = material("t900_carbon_fiber_tow", "T900碳")
                .dust()
                .color(0x8b8b8b)
                .secondaryColor(0x5e5e5e)
                .iconSet(BRIGHT)
                .flags(GENERATE_FIBER, IS_CARBON_FIBER)
                .buildAndRegister();
        T1000CarbonFiber = material("t1000_carbon_fiber_tow", "T1000碳")
                .dust()
                .color(0x9a9a9a)
                .secondaryColor(0x666666)
                .iconSet(BRIGHT)
                .flags(GENERATE_FIBER, IS_CARBON_FIBER)
                .buildAndRegister();
        T1200CarbonFiber = material("t1200_carbon_fiber_tow", "T1200碳")
                .dust()
                .color(0xaaaaaa)
                .secondaryColor(0x717171)
                .iconSet(BRIGHT)
                .flags(GENERATE_FIBER, IS_CARBON_FIBER)
                .buildAndRegister();
        T1500CarbonFiber = material("t1500_carbon_fiber_tow", "T1500碳")
                .dust()
                .color(0xb7b7b7)
                .secondaryColor(0x7d7d7d)
                .iconSet(BRIGHT)
                .flags(GENERATE_FIBER, IS_CARBON_FIBER)
                .buildAndRegister();
    }
}
