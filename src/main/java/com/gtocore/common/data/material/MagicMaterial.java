package com.gtocore.common.data.material;

import com.gtocore.api.data.material.GTOMaterialFlags;
import com.gtocore.common.data.GTOElements;

import com.gtolib.api.data.chemical.material.GTOMaterialBuilder;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.ToolProperty;
import com.gregtechceu.gtceu.api.fluids.FluidBuilder;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.common.data.GTElements;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Rarity;

import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty.GasTier.HIGHEST;
import static com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty.GasTier.LOW;
import static com.gregtechceu.gtceu.common.data.GTMaterials.Silver;
import static com.gtocore.api.data.material.GTOMaterialIconSet.LIMPID;
import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtolib.utils.register.MaterialsRegisterUtils.material;

public final class MagicMaterial {

    public static void init() {
        Mana = magicMaterial("mana", "魔力")
                .gas()
                .dust()
                .color(0x00A7F7)
                .iconSet(BRIGHT)
                .buildAndRegister()
                .setFormula(ChatFormatting.ITALIC + "*Ma*", false);

        Salamander = magicMaterial("salamander", "火元素")
                .gas()
                .fluid()
                .dust()
                .color(0xff0000)
                .iconSet(BRIGHT)
                .flags(NO_UNIFICATION)
                .buildAndRegister()
                .setFormula("*Sa*", false);

        Undine = magicMaterial("undine", "水元素")
                .gas()
                .fluid()
                .dust()
                .color(0x0099ff)
                .iconSet(BRIGHT)
                .flags(NO_UNIFICATION)
                .buildAndRegister()
                .setFormula("*Un*", false);

        Sylph = magicMaterial("sylph", "风元素")
                .gas()
                .fluid()
                .dust()
                .color(0x13e841)
                .iconSet(BRIGHT)
                .flags(NO_UNIFICATION)
                .buildAndRegister()
                .setFormula("*Sy*", false);

        Gnome = magicMaterial("gnome", "地元素")
                .gas()
                .fluid()
                .dust()
                .color(0xedc31c)
                .iconSet(BRIGHT)
                .flags(NO_UNIFICATION)
                .buildAndRegister()
                .setFormula("*Gn*", false);

        Aether = magicMaterial("aether", "以太")
                .gas()
                .fluid()
                .dust()
                .color(0xfbedff)
                .iconSet(BRIGHT)
                .flags(NO_UNIFICATION)
                .buildAndRegister()
                .setFormula("*Ae*", false);

        PerditioCrystal = magicMaterial("perditio_crystal", "混沌魔晶")
                .ore(true)
                .color(0x2b2d30)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?", false);

        GnomeCrystal = magicMaterial("gnome_crystal", "地之魔晶")
                .ore(true)
                .addOreByproducts(PerditioCrystal)
                .color(0xb7835b)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?", false);

        SylphCrystal = magicMaterial("sylph_crystal", "风之魔晶")
                .ore(true)
                .addOreByproducts(PerditioCrystal)
                .color(0x13e841)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?", false);

        UndineCrystal = magicMaterial("undine_crystal", "水之魔晶")
                .ore(true)
                .addOreByproducts(PerditioCrystal)
                .color(0x0099ff)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?", false);

        SalamanderCrystal = magicMaterial("salamander_crystal", "火之魔晶")
                .ore(true)
                .addOreByproducts(PerditioCrystal)
                .color(0xd90000)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?", false);

        FractalPetalSolvent = magicMaterial("fractal_petal_solvent", "碎蕊调和溶剂")
                .liquid(new FluidBuilder().temperature(275))
                .color(0xC7E8C9)
                .iconSet(LIMPID)
                .buildAndRegister();

        CycleofBlossomsSolvent = magicMaterial("cycle_of_blossoms_solvent", "群芳轮迴溶剂")
                .liquid(new FluidBuilder().temperature(275))
                .color(0x8AC4A0)
                .iconSet(LIMPID)
                .buildAndRegister();

        PhantomicElectrolyteBuffer = magicMaterial("phantomic_electrolyte_buffer", "幻影离子液")
                .liquid(new FluidBuilder().temperature(300).viscosity(1500))
                .color(0x7F00FF)
                .iconSet(LIMPID)
                .buildAndRegister();

        OmniFloraElixir = magicMaterial("omni_flora_elixir", "万华灵枢原液")
                .liquid(new FluidBuilder().temperature(275))
                .color(0x6A1B9A)
                .iconSet(LIMPID)
                .buildAndRegister();

        GaiaSolvent = magicMaterial("gaia_solvent", "盖亚魂溶剂")
                .liquid(new FluidBuilder().temperature(275))
                .color(0x82404a)
                .iconSet(LIMPID)
                .buildAndRegister();

        WildenEssence = magicMaterial("wilden_essence", "荒野精华")
                .liquid(new FluidBuilder().temperature(275))
                .color(0x6f3aa4)
                .iconSet(LIMPID)
                .buildAndRegister();

        HerosBrawlers = magicMaterial("heros_brawlers", "英雄之斗争")
                .liquid(new FluidBuilder().temperature(375))
                .color(0xfd4f7b)
                .iconSet(LIMPID)
                .buildAndRegister();

        HerosBreach = magicMaterial("heros_breach", "英雄之突破")
                .liquid(new FluidBuilder().temperature(375))
                .color(0xc7e6ff)
                .iconSet(LIMPID)
                .buildAndRegister();

        HerosSplendor = magicMaterial("heros_splendor", "英雄之辉煌")
                .liquid(new FluidBuilder().temperature(375))
                .color(0x1bbbb5)
                .iconSet(LIMPID)
                .buildAndRegister();

        Animium = magicMaterial("animium", "灵髓液")
                .liquid(new FluidBuilder().temperature(237))
                .color(0xfcd428)
                .buildAndRegister();

        TheWaterFromTheWellOfWisdom = magicMaterial("the_water_from_the_well_of_wisdom", "智慧之泉水")
                .liquid(new FluidBuilder().temperature(272))
                .color(0x0000FF)
                .buildAndRegister();

        FlowingCiphers = magicMaterial("flowing_ciphers", "液态符文")
                .liquid(new FluidBuilder().temperature(176))
                .color(0xbcf7e5)
                .iconSet(FLUID)
                .buildAndRegister();

        ManaDiamond = magicMaterial("mana_diamond", "魔力钻石")
                .gem()
                .components(GTMaterials.Diamond, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x00daef)
                .secondaryColor(0xa0f8ff)
                .iconSet(BRIGHT)
                .buildAndRegister()
                .setFormula("*Ma*C");

        Dragonstone = magicMaterial("dragonstone", "龙石")
                .gem()
                .components(GTMaterials.SiliconDioxide, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xbb0067)
                .iconSet(BRIGHT)
                .buildAndRegister()
                .setFormula("*Em*4(SiO2)2");

        SourceGem = magicMaterial("sourcegem", "魔源宝石")
                .gem()
                .components(GTMaterials.SiliconDioxide, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xca65fc)
                .iconSet(BRIGHT)
                .buildAndRegister()
                .setFormula("*Ma*2(SiO2)2");

        Livingclay = magicMaterial("livingclay", "活土")
                .ingot()
                .flags(MORTAR_GRINDABLE, GENERATE_ROD, GENERATE_PLATE)
                .color(0x391a12)
                .iconSet(ROUGH)
                .buildAndRegister();

        Livingwood = magicMaterial("livingwood", "活木")
                .wood()
                .flags(GENERATE_FRAME, GENERATE_PLATE)
                .color(0x391a12)
                .iconSet(WOOD)
                .toolStats(ToolProperty.Builder.of(2.0f, 1.0f, 256, 0, new GTToolType[] { GTToolType.SOFT_MALLET }).build())
                .buildAndRegister();

        Dreamwood = magicMaterial("dreamwood", "梦之木")
                .wood()
                .flags(GENERATE_FRAME, GENERATE_PLATE)
                .color(0xa6bcb6)
                .iconSet(WOOD)
                .toolStats(ToolProperty.Builder.of(4.0f, 2.0f, 1200, 0, new GTToolType[] { GTToolType.SOFT_MALLET }).build())
                .buildAndRegister();

        Shimmerwood = magicMaterial("shimmerwood", "微光木")
                .wood()
                .flags(GENERATE_FRAME, GENERATE_PLATE)
                .iconSet(WOOD)
                .toolStats(ToolProperty.Builder.of(6.0f, 3.0f, 4500, 0, new GTToolType[] { GTToolType.SOFT_MALLET }).build())
                .buildAndRegister();

        Livingrock = magicMaterial("livingrock", "活石")
                .dust()
                .flags(FORCE_GENERATE_BLOCK, GENERATE_PLATE)
                .color(0xcbcdbb)
                .iconSet(FINE)
                .toolStats(ToolProperty.Builder.of(1.0F, 1, 512, 0, new GTToolType[] { GTToolType.MORTAR }).build())
                .buildAndRegister();

        Runerock = magicMaterial("runerock", "符文石")
                .dust()
                .flags(FORCE_GENERATE_BLOCK, GENERATE_PLATE)
                .color(0xbcf7e5)
                .iconSet(FINE)
                .toolStats(ToolProperty.Builder.of(3.0F, 2, 18000, 0, new GTToolType[] { GTToolType.MORTAR }).build())
                .buildAndRegister();

        Shimmerrock = magicMaterial("shimmerrock", "微光石")
                .dust()
                .flags(FORCE_GENERATE_BLOCK, GENERATE_PLATE)
                .iconSet(FINE)
                .toolStats(ToolProperty.Builder.of(4.0F, 3, 4500, 0, new GTToolType[] { GTToolType.MORTAR }).build())
                .buildAndRegister();

        StarStone = magicMaterial("star_stone", "星辰石")
                .dust()
                .iconSet(FINE)
                .buildAndRegister();

        ManaGlass = magicMaterial("mana_glass", "魔力玻璃")
                .dust()
                .fluid()
                .flags(FORCE_GENERATE_BLOCK, GENERATE_PLATE, GENERATE_LENS)
                .color(0x00A7F7)
                .iconSet(GLASS)
                .buildAndRegister();

        ElfGlass = magicMaterial("elf_glass", "精灵玻璃")
                .dust()
                .fluid()
                .flags(FORCE_GENERATE_BLOCK, GENERATE_PLATE, GENERATE_LENS)
                .color(0x93a2a2)
                .iconSet(GLASS)
                .buildAndRegister();

        BifrostPerm = magicMaterial("bifrost_perm", "彩虹桥")
                .dust()
                .fluid()
                .flags(FORCE_GENERATE_BLOCK, GENERATE_PLATE, GENERATE_LENS)
                .iconSet(GLASS)
                .buildAndRegister();

        InfusedGold = magicMaterial("infused_gold", "注魔金")
                .ingot()
                .ore()
                .color(0xf9f328)
                .iconSet(DULL)
                .flags(GENERATE_PLATE, GENERATE_FRAME, GENERATE_BOLT_SCREW)
                .buildAndRegister().setFormula("Au?", false);

        Thaumium = magicMaterial("thaumium", "神秘")
                .ingot()
                .fluid()
                .components(InfusedGold, 1)
                .color(0x8153a9)
                .iconSet(DULL)
                .blastTemp(1000, LOW, GTValues.VA[GTValues.LV], 100)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FRAME, GENERATE_PLATE, GENERATE_ROD)
                .buildAndRegister();

        AstralSilver = magicMaterial("astral_silver", "星辰银")
                .ingot()
                .fluid()
                .components(Silver, 2, Thaumium, 1)
                .color(0xd9d9f1)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Livingsteel = magicMaterial("livingsteel", "活铁")
                .ingot()
                .fluid()
                .components(GTMaterials.Iron, 1)
                .flags(GTOMaterialFlags.GENERATE_CURVED_PLATE, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .color(0x2ae870)
                .iconSet(METALLIC)
                .buildAndRegister()
                .setFormula("?Fe", false);

        WhiteWax = magicMaterial("white_wax", "白腊")
                .ingot()
                .fluid()
                .color(0xd4d4d4)
                .flags(DISABLE_DECOMPOSITION, GENERATE_ROD, GENERATE_FOIL)
                .iconSet(DULL)
                .buildAndRegister();

        Herbs = magicMaterial("herbs", "药钢")
                .ingot()
                .fluid()
                .color(0xe6ffef)
                .blastTemp(1450, LOW)
                .components(GTMaterials.Steel, 1)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .iconSet(METALLIC)
                .buildAndRegister()
                .setFormula("*Gn*3*Sy*3*Un*3*Sa*3*Ma*4?");

        OriginalBronze = magicMaterial("original_bronze", "原始青铜")
                .ingot()
                .fluid()
                .color(0x562f20)
                .blastTemp(1395, LOW)
                .components(GTMaterials.Bronze, 2)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .iconSet(METALLIC)
                .buildAndRegister()
                .setFormula("*Ma*3(SnCu3)2");

        Manasteel = magicMaterial("manasteel", "魔力钢")
                .ingot()
                .fluid()
                .element(GTOElements.MANASTEEL)
                .flags(GTOMaterialFlags.GENERATE_CURVED_PLATE, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .color(0x3396fe)
                .blastTemp(1700, LOW)
                .secondaryColor(0x2e56d7)
                .iconSet(BRIGHT)
                .toolStats(ToolProperty.Builder.of(2.0F, 6, 2500, 3, GTToolType.SWORD, GTToolType.PICKAXE, GTToolType.SHOVEL, GTToolType.AXE, GTToolType.HOE, GTToolType.MINING_HAMMER, GTToolType.SPADE, GTToolType.SAW, GTToolType.HARD_HAMMER, GTToolType.WRENCH, GTToolType.FILE, GTToolType.CROWBAR, GTToolType.SCREWDRIVER, GTToolType.WIRE_CUTTER, GTToolType.SCYTHE, GTToolType.KNIFE, GTToolType.BUTCHERY_KNIFE, GTToolType.DRILL_LV, GTToolType.DRILL_MV, GTToolType.DRILL_HV, GTToolType.DRILL_EV, GTToolType.DRILL_IV, GTToolType.CHAINSAW_LV, GTToolType.WRENCH_LV, GTToolType.WRENCH_HV, GTToolType.WRENCH_IV, GTToolType.BUZZSAW, GTToolType.SCREWDRIVER_LV, GTToolType.WIRE_CUTTER_LV, GTToolType.WIRE_CUTTER_HV, GTToolType.WIRE_CUTTER_IV).build())
                .rotorStats(160, 130, 5.0f, 400)
                .buildAndRegister();

        Terrasteel = magicMaterial("terrasteel", "泰拉钢")
                .rarity(Rarity.UNCOMMON)
                .ingot()
                .fluid()
                .flags(GTOMaterialFlags.GENERATE_CURVED_PLATE, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .element(GTOElements.TERRASTEEL)
                .color(0x5cd12b)
                .blastTemp(2500, LOW)
                .secondaryColor(0x28b739)
                .iconSet(BRIGHT)
                .buildAndRegister();

        Elementium = magicMaterial("elementium", "源质钢")
                .ingot()
                .fluid()
                .flags(GTOMaterialFlags.GENERATE_CURVED_PLATE, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .element(GTOElements.ELEMENTIUM)
                .blastTemp(3400, LOW)
                .color(0xf766a7)
                .secondaryColor(0xf768d4)
                .iconSet(BRIGHT)
                .toolStats(ToolProperty.Builder.of(6.0F, 7, 8000, 5, GTToolType.SWORD, GTToolType.PICKAXE, GTToolType.SHOVEL, GTToolType.AXE, GTToolType.HOE, GTToolType.MINING_HAMMER, GTToolType.SPADE, GTToolType.SAW, GTToolType.HARD_HAMMER, GTToolType.WRENCH, GTToolType.FILE, GTToolType.CROWBAR, GTToolType.SCREWDRIVER, GTToolType.WIRE_CUTTER, GTToolType.SCYTHE, GTToolType.KNIFE, GTToolType.BUTCHERY_KNIFE, GTToolType.DRILL_LV, GTToolType.DRILL_MV, GTToolType.DRILL_HV, GTToolType.DRILL_EV, GTToolType.DRILL_IV, GTToolType.CHAINSAW_LV, GTToolType.WRENCH_LV, GTToolType.WRENCH_HV, GTToolType.WRENCH_IV, GTToolType.BUZZSAW, GTToolType.SCREWDRIVER_LV, GTToolType.WIRE_CUTTER_LV, GTToolType.WIRE_CUTTER_HV, GTToolType.WIRE_CUTTER_IV).build())
                .rotorStats(200, 150, 7.0f, 1600)
                .buildAndRegister();

        Alfsteel = magicMaterial("alfsteel", "精灵钢")
                .ingot()
                .fluid()
                .blastTemp(3400, LOW)
                .element(GTOElements.ALFSTEEL)
                .flags(GTOMaterialFlags.GENERATE_CURVED_PLATE, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .color(0xffb700)
                .iconSet(BRIGHT)
                .buildAndRegister();

        Gaiasteel = magicMaterial("gaiasteel", "盖亚钢")
                .rarity(Rarity.RARE)
                .ingot()
                .fluid()
                .radioactiveHazard(1)
                .element(GTOElements.GAIASTEEL)
                .blastTemp(4300, LOW)
                .flags(GTOMaterialFlags.GENERATE_CURVED_PLATE, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .color(0x660404)
                .iconSet(BRIGHT)
                .toolStats(ToolProperty.Builder.of(16.0F, 12, 32000, 7, GTToolType.SWORD, GTToolType.PICKAXE, GTToolType.SHOVEL, GTToolType.AXE, GTToolType.HOE, GTToolType.MINING_HAMMER, GTToolType.SPADE, GTToolType.SAW, GTToolType.HARD_HAMMER, GTToolType.WRENCH, GTToolType.FILE, GTToolType.CROWBAR, GTToolType.SCREWDRIVER, GTToolType.WIRE_CUTTER, GTToolType.SCYTHE, GTToolType.KNIFE, GTToolType.BUTCHERY_KNIFE, GTToolType.DRILL_LV, GTToolType.DRILL_MV, GTToolType.DRILL_HV, GTToolType.DRILL_EV, GTToolType.DRILL_IV, GTToolType.CHAINSAW_LV, GTToolType.WRENCH_LV, GTToolType.WRENCH_HV, GTToolType.WRENCH_IV, GTToolType.BUZZSAW, GTToolType.SCREWDRIVER_LV, GTToolType.WIRE_CUTTER_LV, GTToolType.WIRE_CUTTER_HV, GTToolType.WIRE_CUTTER_IV).build())
                .rotorStats(250, 180, 9.0f, 5000)
                .buildAndRegister();

        Gaia = magicMaterial("gaia", "盖亚魂")
                .rarity(Rarity.RARE)
                .ingot()
                .fluid()
                .element(GTOElements.GAIA)
                .blastTemp(5300, LOW)
                .flags(GTOMaterialFlags.GENERATE_CURVED_PLATE, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .iconSet(BRIGHT)
                .buildAndRegister();

        Orichalcos = magicMaterial("orichalcos", "奥利哈钢")
                .ingot()
                .fluid()
                .blastTemp(6700, LOW)
                .element(GTOElements.OHRICHALOS)
                .color(0x590aa9)
                .iconSet(METALLIC)
                .flags(GTOMaterialFlags.GENERATE_CURVED_PLATE, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .toolStats(ToolProperty.Builder.of(24.0F, 16, 128000, 16, GTToolType.SWORD, GTToolType.PICKAXE, GTToolType.SHOVEL, GTToolType.AXE, GTToolType.HOE, GTToolType.MINING_HAMMER, GTToolType.SPADE, GTToolType.SAW, GTToolType.HARD_HAMMER, GTToolType.WRENCH, GTToolType.FILE, GTToolType.CROWBAR, GTToolType.SCREWDRIVER, GTToolType.WIRE_CUTTER, GTToolType.SCYTHE, GTToolType.KNIFE, GTToolType.BUTCHERY_KNIFE, GTToolType.DRILL_LV, GTToolType.DRILL_MV, GTToolType.DRILL_HV, GTToolType.DRILL_EV, GTToolType.DRILL_IV, GTToolType.CHAINSAW_LV, GTToolType.WRENCH_LV, GTToolType.WRENCH_HV, GTToolType.WRENCH_IV, GTToolType.BUZZSAW, GTToolType.SCREWDRIVER_LV, GTToolType.WIRE_CUTTER_LV, GTToolType.WIRE_CUTTER_HV, GTToolType.WIRE_CUTTER_IV).build())
                .rotorStats(300, 220, 12.0f, 20000)
                .buildAndRegister();

        Photonium = magicMaterial("photonium", "光子")
                .ingot()
                .fluid()
                .blastTemp(2800, LOW)
                .element(GTOElements.PHOTONIUM)
                .color(0xb8b8b8)
                .iconSet(METALLIC)
                .flags(GTOMaterialFlags.GENERATE_CURVED_PLATE, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .buildAndRegister();

        Shadowium = magicMaterial("shadowium", "暗影")
                .ingot()
                .fluid()
                .blastTemp(2800, LOW)
                .element(GTOElements.SHADOWIUM)
                .color(0x636363)
                .iconSet(METALLIC)
                .flags(GTOMaterialFlags.GENERATE_CURVED_PLATE, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .buildAndRegister();

        Aerialite = magicMaterial("aerialite", "天空")
                .ingot()
                .fluid()
                .blastTemp(3400, LOW)
                .element(GTOElements.AERIALITE)
                .color(0x045b82)
                .iconSet(METALLIC)
                .flags(GTOMaterialFlags.GENERATE_CURVED_PLATE, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .buildAndRegister();

        Laureril = magicMaterial("laureril", "秘金")
                .rarity(Rarity.UNCOMMON)
                .ingot()
                .fluid()
                .color(0xf5e2b3)
                .blastTemp(1200, LOW)
                .element(GTElements.Au)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .iconSet(METALLIC)
                .buildAndRegister()
                .setFormula("*La*", false);

        Quicksilver = magicMaterial("quicksilver", "银钻")
                .rarity(Rarity.UNCOMMON)
                .ingot()
                .fluid()
                .blastTemp(10100, HIGHEST)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .element(GTElements.Ag)
                .color(0x58C8B6)
                .iconSet(METALLIC)
                .buildAndRegister()
                .setFormula("Qs", false);

        Ignatius = magicMaterial("ignatius", "伊格内休斯")
                .ingot()
                .fluid()
                .blastTemp(12300, HIGHEST)
                .element(GTOElements.ORICHALCUM)
                .color(0xFF9F34)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE)
                .buildAndRegister().setFormula("Ig", false);

        Ceruclase = magicMaterial("ceruclase", "暗影秘银")
                .ingot()
                .fluid()
                .blastTemp(12100, HIGHEST)
                .element(GTOElements.MITHRIL)
                .color(0x3680B6)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE)
                .buildAndRegister().setFormula("Cc", false);

        Lemurite = magicMaterial("lemurite", "利莫利亚")
                .ingot()
                .fluid()
                .blastTemp(12300, HIGHEST)
                .element(GTElements.Nq2)
                .color(0xC5CACB)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE)
                .buildAndRegister().setFormula("Lm", false);

        Alduorite = magicMaterial("alduorite", "神秘蓝金")
                .ingot()
                .fluid()
                .blastTemp(13300, HIGHEST)
                .element(GTOElements.ENDERIUM)
                .color(0x17B4CB)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, GENERATE_GEAR)
                .buildAndRegister().setFormula("Ao", false);

        Kalendrite = magicMaterial("kalendrite", "幽冥魂石")
                .ingot()
                .fluid()
                .blastTemp(13500, HIGHEST)
                .element(GTOElements.INFUSCOLIUM)
                .color(0x9A3AB3)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE)
                .buildAndRegister().setFormula("Kl", false);

        Celenegil = magicMaterial("celenegil", "幽冥毒晶")
                .ingot()
                .fluid()
                .blastTemp(13700, HIGHEST)
                .element(GTOElements.INFUSCOLIUM)
                .color(0x399936)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE)
                .buildAndRegister().setFormula("Cg", false);

        Haderoth = magicMaterial("haderoth", "幻铜")
                .ingot()
                .fluid()
                .blastTemp(14100, HIGHEST)
                .element(GTOElements.COPPER76)
                .color(0xB34616)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE)
                .buildAndRegister().setFormula("Hd", false);

        Sanguinite = magicMaterial("sanguinite", "狱炎")
                .ingot()
                .fluid()
                .blastTemp(14900, HIGHEST)
                .element(GTOElements.ADAMANTIUM)
                .color(0xC81B00)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, GENERATE_GEAR)
                .buildAndRegister().setFormula("Su", false);
    }

    public static GTOMaterialBuilder magicMaterial(String name, String cn) {
        return material(name, cn).flags(MaterialFlags.MAGICAL);
    }
}
