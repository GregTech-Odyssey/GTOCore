package com.gtocore.common.data.material;

import com.gregtechceu.gtceu.api.GTValues;

import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.GENERATE_BOLT_SCREW;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.GENERATE_FOIL;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.GENERATE_FRAME;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.GENERATE_GEAR;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.GENERATE_LONG_ROD;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.GENERATE_RING;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.GENERATE_SMALL_GEAR;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet.METALLIC;
import static com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty.GasTier.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.api.data.material.GTOMaterialFlags.*;
import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtocore.common.data.GTOMaterials.Etrium;
import static com.gtolib.utils.register.MaterialsRegisterUtils.material;

/// 不锈钢-JBK75 LUV stainless_steel_jbk75 #f7faff 管道，杆，长杆，环，弯曲板，框架
/// 镍铬基合金-242 LUV inconel_242 #575762 齿轮，杆，长杆
/// 铂铑合金 LUV platinum_rhodium #edefe7 弯曲板，齿轮，箔
/// 斯卡马洛伊-S合金 LUV scalmalloy_s #d7fcfe 弯曲板
/// GRCop-84合金 LUV grcop_48 #e5d8aa 弯曲板
/// 导热特种合金 ZPM thermal_conductive_alloys #ffe278 弯曲板
/// Ti-5553合金 ZPM titanium_5553 #c380c6 弯曲板，杆，长杆，框架
/// 超轻特种钢 ZPM ultra_light_special_steel #8b8b8b 杆，长杆，框架
/// 特种铍铝合金 UV beryllium_aluminum #689d79 齿轮
/// 月神钛 UHV moon_goddess_titanium #d3b8d2 齿轮，杆，长杆，框架
/// 铝合金-2090 HV aluminum_alloy_2090 #95e3e2 仅板子 16铝，1锂，2铜
/// 铝合金-8090 HV aluminum_alloy_8090 #518fbe 仅板子 20铝，2铜，1镁，4锂
/// 铝合金-7050 HV aluminum_alloy_7050 #94d4ee 全套零件 20铝，1铜，1镁，3锌
/// 铝合金-5A06 HV aluminum_alloy_5a06 #78e2f2 仅板子 20铝，3镁，1锰，1硅
/// 结构钢-45 HV structural_steel_45 #dbdee0 全套零件 50钢，1铬，1镍，1锰
/// 不锈钢-420 HV stainless_steel_420 #c1c3c5 全套零件 80钢，12铬，1镍
/// 不锈钢-GC4 HV stainless_steel_gc4 #c5cbd0 全套零件 10钢，6铬，1锰，1硅，1钼，1钒
/// 钛合金-Ti64 HV titanium_ti64 #cbbcc5 全套零件 45钛，3铝，2钒
/// 不锈钢-15-5ph EV stainless_steel_15_5ph #d2d7dc 仅板子 60钢，4铜，5镍，15铬，1铌，1硅，1锰
/// 不锈钢-316 EV stainless_steel_316 #eae9e3 全套零件 64钢，1硅，2锰，18铬，10镍，3钼
/// 结构钢-Q690 EV structural_steel_q690 #898989 仅板子 80钢，1钒，2钛，1铌，1铝
/// 镍铬基合金-X750 EV inconel_x750 #59565a 仅板子 35镍，9铬，5钢，1钨，1钼，1钛
/// 钨合金-YW3 IV tungsten_alloy_yw3 #2d2d3c 仅板子 50钨，5镍，4钢，1钴
/// 钨合金-YG10 IV tungsten_alloy_yg10 #3b3b46 全套零件 9钨，1钴
/// 镍铬基合金-617 IV inconel_617 #2d3c2e 仅板子 11镍，6铬，4钴，2钼，1钢
/// 铍铝合金-Z IV beryllium_aluminum_z #47b191 全套零件 30铍，18铝，2镁
/// 钛合金-TB6 IV titanium_tb6 #b9a7ba 仅板子 85钛，10钒，2钢，3铝
/// 镍铬基合金-718 IV inconel_718 #1f4134 仅板子 50镍，20铬，4钼，6铌，19钢，2钛
/// 铍铝合金-F LUV beryllium_aluminum_f #0d8980 全套零件 20铍，12铝，1银，1钴
/// 钛合金-TC11 LUV titanium_tc11 #8c7a8b 全套零件 90钛，6铝，3钒，1铁
public class MaterialSpaceEra {

    public static void init() {
        StainlessSteelJbk75 = material("stainless_steel_jbk75", "不锈钢-JBK75")
                .ingot()
                .fluid()
                .color(0xf7faff)
                .secondaryColor(0x9fa1a3)
                .blastTemp(5450, HIGH, GTValues.VA[GTValues.LuV], 600)
                .components(Steel, 25, Chromium, 8, Nickel, 16, Niobium, 1, Titanium, 2, Molybdenum, 3)
                .iconSet(BRIGHT)
                .flags(GENERATE_GEAR,
                        GENERATE_SMALL_GEAR, GENERATE_LONG_ROD,
                        GENERATE_FRAME, GENERATE_FOIL,
                        GENERATE_BOLT_SCREW, GENERATE_RING, DECOMPOSITION_BY_CENTRIFUGING)
                .fluidPipeProperties(3500, 3500, true, true, true)
                .buildAndRegister();

        Inconel242 = material("inconel_242", "镍铬基合金-242")
                .ingot()
                .fluid()
                .color(0x575762)
                .secondaryColor(0x2e2e38)
                .blastTemp(4450, HIGH, GTValues.VA[GTValues.LuV], 600)
                .components(Nickel, 16, Chromium, 10, Naquadah, 2, Cobalt, 1, Molybdenum, 3)
                .iconSet(METALLIC)
                .flags(GENERATE_GEAR, GENERATE_CURVED_PLATE,
                        GENERATE_SMALL_GEAR, GENERATE_LONG_ROD,
                        GENERATE_FRAME, GENERATE_FOIL,
                        GENERATE_BOLT_SCREW, GENERATE_RING, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();
        PlatinumRhodiumAlloy = material("platinum_rhodium_alloy", "铂铑合金")
                .ingot()
                .fluid()
                .color(0xedefe7)
                .secondaryColor(0xe4e4e4)
                .blastTemp(4440, HIGH, GTValues.VA[GTValues.LuV], 600)
                .components(Platinum, 9, Rhodium, 10)
                .iconSet(BRIGHT)
                .flags(GENERATE_GEAR, GENERATE_CURVED_PLATE,
                        GENERATE_SMALL_GEAR, GENERATE_LONG_ROD,
                        GENERATE_FRAME, GENERATE_FOIL,
                        GENERATE_BOLT_SCREW, GENERATE_RING, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();
        ScalmAlloyS = material("scalm_alloy_s", "斯卡马洛伊-S合金")
                .ingot()
                .fluid()
                .color(0xd7fcfe)
                .secondaryColor(0x9ef1f5)
                .blastTemp(5580, HIGH, GTValues.VA[GTValues.IV], 600)
                .components(Aluminium, 45, Silicon, 7, Magnesium, 8, Scandium, 4, Zirconium, 4, Manganese, 5)
                .iconSet(METALLIC)
                .flags(GENERATE_GEAR, GENERATE_CURVED_PLATE,
                        GENERATE_SMALL_GEAR, GENERATE_LONG_ROD,
                        GENERATE_FRAME, GENERATE_FOIL,
                        GENERATE_BOLT_SCREW, GENERATE_RING, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();
        Grcop84 = material("grcop84", "GRCop-84合金")
                .ingot()
                .fluid()
                .color(0xe5d8aa)
                .secondaryColor(0xb7a97d)
                .blastTemp(5875, HIGH, GTValues.VA[GTValues.IV], 400)
                .components(Copper, 21, Nickel, 2, Niobium, 2)
                .iconSet(METALLIC)
                .flags(GENERATE_GEAR, GENERATE_CURVED_PLATE,
                        GENERATE_SMALL_GEAR, GENERATE_LONG_ROD,
                        GENERATE_FRAME, GENERATE_FOIL,
                        GENERATE_BOLT_SCREW, GENERATE_RING, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();
        ThermalConductiveAlloy = material("thermal_conductive_alloy", "高热导合金")
                .ingot()
                .fluid()
                .color(0xffe278)
                .secondaryColor(0xc2a664)
                .blastTemp(7111, HIGHER, GTValues.VA[GTValues.ZPM], 1400)
                .components(Copper, 80, Silver, 20, Zirconium, 8, SterlingSilver, 5, Thulium, 3)
                .iconSet(BRIGHT)
                .flags(GENERATE_GEAR, GENERATE_CURVED_PLATE,
                        GENERATE_SMALL_GEAR, GENERATE_LONG_ROD,
                        GENERATE_FRAME, GENERATE_FOIL,
                        GENERATE_BOLT_SCREW, GENERATE_RING, DECOMPOSITION_BY_CENTRIFUGING, NEED_BLAST_IN_SPACE)
                .buildAndRegister();
        Titanium5553 = material("titanium_5553", "钛合金-5553")
                .ingot()
                .fluid()
                .color(0xc380c6)
                .secondaryColor(0x8b5e8b)
                .blastTemp(8660, HIGHER, GTValues.VA[GTValues.LuV], 900)
                .components(Titanium, 82, Aluminium, 5, Vanadium, 5, Molybdenum, 5, Chromium, 3, Niobium, 1)
                .iconSet(METALLIC)
                .flags(GENERATE_GEAR, GENERATE_CURVED_PLATE,
                        GENERATE_SMALL_GEAR, GENERATE_LONG_ROD,
                        GENERATE_FRAME, GENERATE_FOIL,
                        GENERATE_BOLT_SCREW, GENERATE_RING, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();
        UltraLightweightCompositeSteel = material("ultra_lightweight_composite_steel", "超轻复合钢")
                .ingot()
                .fluid()
                .color(0x8b8b8b)
                .secondaryColor(0x4c4c4c)
                .blastTemp(7100, HIGHER, GTValues.VA[GTValues.UV], 1600)
                .components(Steel, 56 * 3, Aluminium, 12 * 3, Manganese, 29 * 3, Chromium, 5 * 3, Beryllium, 2, Naquadria, 1)
                .iconSet(SHINY)
                .flags(GENERATE_GEAR, GENERATE_CURVED_PLATE,
                        GENERATE_SMALL_GEAR, GENERATE_LONG_ROD,
                        GENERATE_FRAME, GENERATE_FOIL,
                        GENERATE_BOLT_SCREW, GENERATE_RING, DECOMPOSITION_BY_CENTRIFUGING, NEED_BLAST_IN_SPACE)
                .buildAndRegister();
        BerylliumAluminiumAlloy = material("beryllium_aluminium_alloy", "特种铍铝合金")
                .ingot()
                .fluid()
                .color(0x689d79)
                .secondaryColor(0x4b6f58)
                .components(Aluminium, 3, Beryllium, 6)
                .iconSet(METALLIC)
                .flags(GENERATE_GEAR, GENERATE_CURVED_PLATE,
                        GENERATE_SMALL_GEAR, GENERATE_LONG_ROD,
                        GENERATE_FRAME, GENERATE_FOIL,
                        GENERATE_BOLT_SCREW, GENERATE_RING, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();
        MoonGoddessTitanium = material("moon_goddess_titanium", "月神钛")
                .ingot()
                .fluid()
                .color(0xd3b8d2)
                .secondaryColor(0xa899a8)
                .blastTemp(12345, HIGHER, GTValues.VA[GTValues.UHV], 1900)
                .components(Titanium, 64 * 4, Vanadium, 10 * 4, Aluminium, 5 * 4, Chromium, 5 * 4, Tin, 5 * 4, Etrium, 8 * 4, InfusedGold, 5, AstralSilver, 3)
                .iconSet(METALLIC)
                .flags(GENERATE_GEAR, GENERATE_CURVED_PLATE,
                        GENERATE_SMALL_GEAR, GENERATE_LONG_ROD,
                        GENERATE_FRAME, GENERATE_FOIL,
                        GENERATE_BOLT_SCREW, GENERATE_RING, DECOMPOSITION_BY_CENTRIFUGING, NEED_BLAST_IN_SPACE)
                .buildAndRegister();

        AluminumAlloy2090 = material("aluminum_alloy_2090", "铝合金-2090")
                .ingot().fluid()
                .color(0x95e3e2)
                .secondaryColor(0x6abed1)
                .components(Aluminium, 16, Lithium, 1, Copper, 2)
                .blastTemp(1660, LOW, GTValues.VA[GTValues.HV], 1100)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();
        AluminumAlloy8090 = material("aluminum_alloy_8090", "铝合金-8090")
                .ingot().fluid()
                .color(0x518fbe)
                .secondaryColor(0x38648c)
                .components(Aluminium, 20, Copper, 2, Magnesium, 1, Lithium, 4)
                .blastTemp(1760, LOW, GTValues.VA[GTValues.HV], 1100)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, CAN_BE_COOLED_DOWN_BY_BATHING, GENERATE_FOIL, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();
        AluminumAlloy7050 = material("aluminum_alloy_7050", "铝合金-7050")
                .ingot().fluid()
                .color(0x94d4ee)
                .secondaryColor(0x6abed1)
                .components(Aluminium, 20, Copper, 1, Magnesium, 1, Zinc, 3)
                .blastTemp(1750, LOW, GTValues.VA[GTValues.HV], 1200)
                .iconSet(METALLIC)
                .flags(GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_LONG_ROD,
                        GENERATE_FRAME, GENERATE_FOIL,
                        GENERATE_BOLT_SCREW, GENERATE_RING, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();
        AluminumAlloy5A06 = material("aluminum_alloy_5a06", "铝合金-5A06")
                .ingot().fluid()
                .color(0x78e2f2)
                .secondaryColor(0x4cbfd1)
                .components(Aluminium, 20, Magnesium, 3, Manganese, 1, Silicon, 1)
                .blastTemp(2180, LOW, GTValues.VA[GTValues.EV], 1050)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, CAN_BE_COOLED_DOWN_BY_BATHING, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();
        StructuralSteel45 = material("structural_steel_45", "结构钢-45")
                .ingot().fluid()
                .color(0xdbdee0)
                .secondaryColor(0x9ea1a3)
                .components(Steel, 50, Chromium, 1, Nickel, 1, Manganese, 1)
                .blastTemp(2480, LOW, GTValues.VA[GTValues.EV], 900)
                .iconSet(METALLIC)
                .flags(GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_LONG_ROD,
                        GENERATE_FRAME, GENERATE_FOIL,
                        GENERATE_BOLT_SCREW, GENERATE_RING, CAN_BE_COOLED_DOWN_BY_BATHING, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();
        StainlessSteel420 = material("stainless_steel_420", "不锈钢-420")
                .ingot().fluid()
                .color(0xc1c3c5)
                .secondaryColor(0x8f9294)
                .components(Steel, 80, Chromium, 12, Nickel, 1)
                .blastTemp(2410, LOW, GTValues.VA[GTValues.HV], 1100)
                .iconSet(METALLIC)
                .flags(GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_LONG_ROD,
                        GENERATE_FRAME, GENERATE_FOIL,
                        GENERATE_BOLT_SCREW, GENERATE_RING, CAN_BE_COOLED_DOWN_BY_BATHING, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();
        StainlessSteelGC4 = material("stainless_steel_gc4", "不锈钢-GC4")
                .ingot().fluid()
                .color(0xc5cbd0)
                .secondaryColor(0x929aa1)
                .components(Steel, 10, Chromium, 6, Manganese, 1, Silicon, 1, Molybdenum, 1, Vanadium, 1)
                .blastTemp(2630, LOW, GTValues.VA[GTValues.HV], 1040)
                .iconSet(METALLIC)
                .flags(GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_LONG_ROD,
                        GENERATE_FRAME, GENERATE_FOIL,
                        GENERATE_BOLT_SCREW, GENERATE_RING, CAN_BE_COOLED_DOWN_BY_BATHING, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();
        TitaniumTi64 = material("titanium_ti64", "钛合金-Ti64")
                .ingot().fluid()
                .color(0xcbbcc5)
                .secondaryColor(0x8b7a8b)
                .components(Titanium, 45, Aluminium, 3, Vanadium, 2)
                .blastTemp(2630, LOW, GTValues.VA[GTValues.HV], 1060)
                .iconSet(METALLIC)
                .flags(GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_LONG_ROD,
                        GENERATE_FRAME, GENERATE_FOIL,
                        GENERATE_BOLT_SCREW, GENERATE_RING, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();
        StainlessSteel155Ph = material("stainless_steel_15_5ph", "不锈钢-15-5ph")
                .ingot().fluid()
                .color(0xd2d7dc)
                .secondaryColor(0xa1a6ac)
                .components(Steel, 60, Copper, 4, Nichrome, 5, StainlessSteel, 10, Niobium, 1, Silicon, 1)
                .blastTemp(3150, LOW, GTValues.VA[GTValues.EV], 1150)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, GENERATE_FIBER, GENERATE_FOIL, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();
        StainlessSteel316 = material("stainless_steel_316", "不锈钢-316")
                .ingot().fluid()
                .color(0xeae9e3)
                .secondaryColor(0xb7b8b1)
                .components(Steel, 64, Silicon, 1, Manganese, 2, Chromium, 18, Nickel, 10, Molybdenum, 3)
                .blastTemp(3250, LOW, GTValues.VA[GTValues.EV], 1200)
                .iconSet(METALLIC)
                .flags(GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_LONG_ROD,
                        GENERATE_FRAME, GENERATE_FOIL,
                        GENERATE_BOLT_SCREW, GENERATE_RING, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();
        StructuralSteelQ690 = material("structural_steel_q690", "结构钢-Q690")
                .ingot().fluid()
                .color(0x898989)
                .secondaryColor(0x5c5c5c)
                .components(Steel, 80, Vanadium, 1, Titanium, 2, Niobium, 1, Aluminium, 1)
                .blastTemp(3430, LOW, GTValues.VA[GTValues.EV], 950)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();
        InconelX750 = material("inconel_x750", "镍铬基合金-X750")
                .ingot().fluid()
                .color(0x59565a)
                .secondaryColor(0x3a373b)
                .components(Nickel, 35, Chromium, 9, Steel, 5, Tungsten, 1, Molybdenum, 1, Titanium, 1)
                .blastTemp(3350, LOW, GTValues.VA[GTValues.EV], 1150)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();
        TungstenAlloyYW3 = material("tungsten_alloy_yw3", "钨合金-YW3")
                .ingot().fluid()
                .color(0x2d2d3c)
                .secondaryColor(0x1f1f28)
                .components(Tungsten, 50, Nickel, 5, Steel, 4, Cobalt, 1)
                .blastTemp(4444, HIGH, GTValues.VA[GTValues.IV], 1300)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();
        TungstenAlloyYG10 = material("tungsten_alloy_yg10", "钨合金-YG10")
                .ingot().fluid()
                .color(0x3b3b46)
                .secondaryColor(0x262631)
                .components(Tungsten, 9, Cobalt, 1)
                .blastTemp(4373, HIGH, GTValues.VA[GTValues.IV], 1300)
                .iconSet(METALLIC)
                .flags(GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_LONG_ROD,
                        GENERATE_FRAME, GENERATE_FOIL,
                        GENERATE_BOLT_SCREW, GENERATE_RING, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();
        Inconel617 = material("inconel_617", "镍铬基合金-617")
                .ingot().fluid()
                .color(0x2d3c2e)
                .secondaryColor(0x1f281f)
                .components(Nickel, 11, Chromium, 6, Cobalt, 4, Molybdenum, 2, Steel, 1)
                .blastTemp(4270, HIGH, GTValues.VA[GTValues.IV], 1250)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();
        BerylliumAluminumZ = material("beryllium_aluminum_z", "铍铝合金-Z")
                .ingot().fluid()
                .color(0x47b191)
                .secondaryColor(0x2f7d61)
                .components(Aluminium, 18, Beryllium, 30, Magnesium, 2)
                .blastTemp(3644, HIGH, GTValues.VA[GTValues.IV], 900)
                .iconSet(METALLIC)
                .flags(GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_LONG_ROD,
                        GENERATE_FRAME, GENERATE_FOIL,
                        GENERATE_BOLT_SCREW, GENERATE_RING, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();
        TitaniumTB6 = material("titanium_tb6", "钛合金-TB6")
                .ingot().fluid()
                .color(0xb9a7ba)
                .secondaryColor(0x7a6a7a)
                .components(Titanium, 85, Vanadium, 10, Steel, 2, Aluminium, 3)
                .blastTemp(4320, HIGH, GTValues.VA[GTValues.IV], 1200)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, GENERATE_FOIL, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();
        Inconel718 = material("inconel_718", "镍铬基合金-718")
                .ingot().fluid()
                .color(0x1f4134)
                .secondaryColor(0x142822)
                .components(Nickel, 50, Chromium, 20, Molybdenum, 4, Niobium, 6, StainlessSteel, 19, Titanium, 2)
                .blastTemp(4175, HIGH, GTValues.VA[GTValues.IV], 1300)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();
        BerylliumAluminumF = material("beryllium_aluminum_f", "铍铝合金-F")
                .ingot().fluid()
                .color(0x0d8980)
                .secondaryColor(0x07544d)
                .components(Aluminium, 12, Beryllium, 20, Silver, 1, Cobalt, 1)
                .blastTemp(5560, HIGH, GTValues.VA[GTValues.LuV], 800)
                .iconSet(METALLIC)
                .flags(GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_LONG_ROD,
                        GENERATE_FRAME, GENERATE_FOIL,
                        GENERATE_BOLT_SCREW, GENERATE_RING, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();
        TitaniumTC11 = material("titanium_tc11", "钛合金-TC11")
                .ingot().fluid()
                .color(0x8c7a8b)
                .secondaryColor(0x5e4c5e)
                .components(Titanium, 90, Aluminium, 6, Vanadium, 3, Iron, 1)
                .blastTemp(5680, HIGH, GTValues.VA[GTValues.LuV], 1000)
                .iconSet(METALLIC)
                .flags(GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_LONG_ROD,
                        GENERATE_FRAME, GENERATE_FOIL,
                        GENERATE_BOLT_SCREW, GENERATE_RING, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();
    }
}
