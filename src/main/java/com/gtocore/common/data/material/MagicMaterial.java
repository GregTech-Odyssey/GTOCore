package com.gtocore.common.data.material;

import com.gtocore.api.data.material.GTOMaterialFlags;
import com.gtocore.common.data.GTOElements;

import com.gregtechceu.gtceu.api.GTValues;
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
        Mana = material("mana", "魔力")
                .gas()
                .dust()
                .color(0x00A7F7)
                .iconSet(BRIGHT)
                .buildAndRegister()
                .setFormula(ChatFormatting.ITALIC + "*Ma*", false);

        Salamander = material("salamander", "火元素")
                .gas()
                .fluid()
                .dust()
                .color(0xff0000)
                .iconSet(BRIGHT)
                .flags(NO_UNIFICATION)
                .buildAndRegister()
                .setFormula("*Sa*", false);

        Undine = material("undine", "水元素")
                .gas()
                .fluid()
                .dust()
                .color(0x0099ff)
                .iconSet(BRIGHT)
                .flags(NO_UNIFICATION)
                .buildAndRegister()
                .setFormula("*Un*", false);

        Sylph = material("sylph", "风元素")
                .gas()
                .fluid()
                .dust()
                .color(0x13e841)
                .iconSet(BRIGHT)
                .flags(NO_UNIFICATION)
                .buildAndRegister()
                .setFormula("*Sy*", false);

        Gnome = material("gnome", "地元素")
                .gas()
                .fluid()
                .dust()
                .color(0xedc31c)
                .iconSet(BRIGHT)
                .flags(NO_UNIFICATION)
                .buildAndRegister()
                .setFormula("*Gn*", false);

        Aether = material("aether", "以太")
                .gas()
                .fluid()
                .dust()
                .color(0xfbedff)
                .iconSet(BRIGHT)
                .flags(NO_UNIFICATION)
                .buildAndRegister()
                .setFormula("*Ae*", false);

        PerditioCrystal = material("perditio_crystal", "混沌魔晶")
                .ore(true)
                .color(0x2b2d30)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?", false);

        GnomeCrystal = material("gnome_crystal", "地之魔晶")
                .ore(true)
                .addOreByproducts(PerditioCrystal)
                .color(0xb7835b)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?", false);

        SylphCrystal = material("sylph_crystal", "风之魔晶")
                .ore(true)
                .addOreByproducts(PerditioCrystal)
                .color(0x13e841)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?", false);

        UndineCrystal = material("undine_crystal", "水之魔晶")
                .ore(true)
                .addOreByproducts(PerditioCrystal)
                .color(0x0099ff)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?", false);

        SalamanderCrystal = material("salamander_crystal", "火之魔晶")
                .ore(true)
                .addOreByproducts(PerditioCrystal)
                .color(0xd90000)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?", false);

        FractalPetalSolvent = material("fractal_petal_solvent", "碎蕊调和溶剂")
                .liquid(new FluidBuilder().temperature(275))
                .color(0xC7E8C9)
                .iconSet(LIMPID)
                .buildAndRegister();

        CycleofBlossomsSolvent = material("cycle_of_blossoms_solvent", "群芳轮迴溶剂")
                .liquid(new FluidBuilder().temperature(275))
                .color(0x8AC4A0)
                .iconSet(LIMPID)
                .buildAndRegister();

        OmniFloraElixir = material("omni_flora_elixir", "万华灵枢原液")
                .liquid(new FluidBuilder().temperature(275))
                .color(0x6A1B9A)
                .iconSet(LIMPID)
                .buildAndRegister();

        GaiaSolvent = material("gaia_solvent", "盖亚魂溶剂")
                .liquid(new FluidBuilder().temperature(275))
                .color(0x82404a)
                .iconSet(LIMPID)
                .buildAndRegister();

        ManaDiamond = material("mana_diamond", "魔力钻石")
                .gem()
                .components(GTMaterials.Diamond, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x00daef)
                .secondaryColor(0xa0f8ff)
                .iconSet(BRIGHT)
                .buildAndRegister()
                .setFormula("*Ma*C");

        Dragonstone = material("dragonstone", "龙石")
                .gem()
                .components(GTMaterials.SiliconDioxide, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xbb0067)
                .iconSet(BRIGHT)
                .buildAndRegister()
                .setFormula("*Em*4(SiO2)2");

        SourceGem = material("sourcegem", "魔源宝石")
                .gem()
                .components(GTMaterials.SiliconDioxide, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xca65fc)
                .iconSet(BRIGHT)
                .buildAndRegister()
                .setFormula("*Ma*2(SiO2)2");

        Livingclay = material("livingclay", "活土")
                .ingot()
                .flags(MORTAR_GRINDABLE, GENERATE_ROD, GENERATE_PLATE)
                .color(0x391a12)
                .iconSet(ROUGH)
                .buildAndRegister();

        Livingwood = material("livingwood", "活木")
                .wood()
                .flags(GENERATE_FRAME, GENERATE_PLATE)
                .color(0x391a12)
                .iconSet(WOOD)
                .toolStats(ToolProperty.Builder.of(2.0f, 1.0f, 256, 0, new GTToolType[] { GTToolType.SOFT_MALLET }).build())
                .buildAndRegister();

        Dreamwood = material("dreamwood", "梦之木")
                .wood()
                .flags(GENERATE_FRAME, GENERATE_PLATE)
                .color(0xa6bcb6)
                .iconSet(WOOD)
                .toolStats(ToolProperty.Builder.of(4.0f, 2.0f, 1200, 0, new GTToolType[] { GTToolType.SOFT_MALLET }).build())
                .buildAndRegister();

        Shimmerwood = material("shimmerwood", "微光木")
                .wood()
                .flags(GENERATE_FRAME, GENERATE_PLATE)
                .iconSet(WOOD)
                .toolStats(ToolProperty.Builder.of(6.0f, 3.0f, 4500, 0, new GTToolType[] { GTToolType.SOFT_MALLET }).build())
                .buildAndRegister();

        Livingrock = material("livingrock", "活石")
                .dust()
                .flags(FORCE_GENERATE_BLOCK, GENERATE_PLATE)
                .color(0xcbcdbb)
                .iconSet(FINE)
                .toolStats(ToolProperty.Builder.of(1.0F, 1, 512, 0, new GTToolType[] { GTToolType.MORTAR }).build())
                .buildAndRegister();

        Runerock = material("runerock", "符文石")
                .dust()
                .flags(FORCE_GENERATE_BLOCK, GENERATE_PLATE)
                .color(0xbcf7e5)
                .iconSet(FINE)
                .toolStats(ToolProperty.Builder.of(3.0F, 2, 18000, 0, new GTToolType[] { GTToolType.MORTAR }).build())
                .buildAndRegister();

        Shimmerrock = material("shimmerrock", "微光石")
                .dust()
                .flags(FORCE_GENERATE_BLOCK, GENERATE_PLATE)
                .iconSet(FINE)
                .toolStats(ToolProperty.Builder.of(4.0F, 3, 4500, 0, new GTToolType[] { GTToolType.MORTAR }).build())
                .buildAndRegister();

        ManaGlass = material("mana_glass", "魔力玻璃")
                .dust()
                .fluid()
                .flags(FORCE_GENERATE_BLOCK, GENERATE_PLATE, GENERATE_LENS)
                .color(0x00A7F7)
                .iconSet(GLASS)
                .buildAndRegister();

        ElfGlass = material("elf_glass", "精灵玻璃")
                .dust()
                .fluid()
                .flags(FORCE_GENERATE_BLOCK, GENERATE_PLATE, GENERATE_LENS)
                .color(0x93a2a2)
                .iconSet(GLASS)
                .buildAndRegister();

        BifrostPerm = material("bifrost_perm", "彩虹桥")
                .dust()
                .fluid()
                .flags(FORCE_GENERATE_BLOCK, GENERATE_PLATE, GENERATE_LENS)
                .iconSet(GLASS)
                .buildAndRegister();

        InfusedGold = material("infused_gold", "注魔金")
                .ingot()
                .ore()
                .color(0xf9f328)
                .iconSet(DULL)
                .flags(GENERATE_PLATE, GENERATE_FRAME, GENERATE_BOLT_SCREW)
                .buildAndRegister().setFormula("Au?", false);

        Thaumium = material("thaumium", "神秘")
                .ingot()
                .fluid()
                .components(InfusedGold, 1)
                .color(0x8153a9)
                .iconSet(DULL)
                .blastTemp(1000, LOW, GTValues.VA[GTValues.LV], 100)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FRAME, GENERATE_PLATE, GENERATE_ROD)
                .buildAndRegister();

        AstralSilver = material("astral_silver", "星辰银")
                .ingot()
                .fluid()
                .components(Silver, 2, Thaumium, 1)
                .color(0xd9d9f1)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Livingsteel = material("livingsteel", "活铁")
                .ingot()
                .fluid()
                .components(GTMaterials.Iron, 1)
                .flags(GTOMaterialFlags.GENERATE_CURVED_PLATE, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .color(0x2ae870)
                .iconSet(METALLIC)
                .buildAndRegister()
                .setFormula("?Fe", false);

        WhiteWax = material("white_wax", "白腊")
                .ingot()
                .fluid()
                .color(0xd4d4d4)
                .flags(DISABLE_DECOMPOSITION, GENERATE_ROD, GENERATE_FOIL)
                .iconSet(DULL)
                .buildAndRegister();

        Herbs = material("herbs", "药钢")
                .ingot()
                .fluid()
                .color(0xe6ffef)
                .blastTemp(1450, LOW)
                .components(GTMaterials.Steel, 1)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .iconSet(METALLIC)
                .buildAndRegister()
                .setFormula("*Gn*3*Sy*3*Un*3*Sa*3*Ma*4?");

        OriginalBronze = material("original_bronze", "原始青铜")
                .ingot()
                .fluid()
                .color(0x562f20)
                .blastTemp(1395, LOW)
                .components(GTMaterials.Bronze, 2)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .iconSet(METALLIC)
                .buildAndRegister()
                .setFormula("*Ma*3(SnCu3)2");

        Manasteel = material("manasteel", "魔力钢")
                .ingot()
                .fluid()
                .element(GTOElements.MANASTEEL)
                .flags(GTOMaterialFlags.GENERATE_CURVED_PLATE, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .color(0x3396fe)
                .blastTemp(1700, LOW)
                .secondaryColor(0x2e56d7)
                .iconSet(BRIGHT)
                .toolStats(ToolProperty.Builder.of(2.0F, 6, 2500, 3, GTToolType.SWORD, GTToolType.PICKAXE, GTToolType.SHOVEL, GTToolType.AXE, GTToolType.HOE, GTToolType.MINING_HAMMER, GTToolType.SPADE, GTToolType.SAW, GTToolType.HARD_HAMMER, GTToolType.WRENCH, GTToolType.FILE, GTToolType.CROWBAR, GTToolType.SCREWDRIVER, GTToolType.WIRE_CUTTER, GTToolType.SCYTHE, GTToolType.KNIFE, GTToolType.BUTCHERY_KNIFE, GTToolType.DRILL_LV, GTToolType.DRILL_MV, GTToolType.DRILL_HV, GTToolType.DRILL_EV, GTToolType.DRILL_IV, GTToolType.CHAINSAW_LV, GTToolType.WRENCH_LV, GTToolType.WRENCH_HV, GTToolType.WRENCH_IV, GTToolType.BUZZSAW, GTToolType.SCREWDRIVER_LV, GTToolType.WIRE_CUTTER_LV, GTToolType.WIRE_CUTTER_HV, GTToolType.WIRE_CUTTER_IV).magnetic().build())
                .buildAndRegister();

        Terrasteel = material("terrasteel", "泰拉钢")
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

        Elementium = material("elementium", "源质钢")
                .ingot()
                .fluid()
                .flags(GTOMaterialFlags.GENERATE_CURVED_PLATE, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .element(GTOElements.ELEMENTIUM)
                .blastTemp(3400, LOW)
                .color(0xf766a7)
                .secondaryColor(0xf768d4)
                .iconSet(BRIGHT)
                .toolStats(ToolProperty.Builder.of(6.0F, 7, 8000, 5, GTToolType.SWORD, GTToolType.PICKAXE, GTToolType.SHOVEL, GTToolType.AXE, GTToolType.HOE, GTToolType.MINING_HAMMER, GTToolType.SPADE, GTToolType.SAW, GTToolType.HARD_HAMMER, GTToolType.WRENCH, GTToolType.FILE, GTToolType.CROWBAR, GTToolType.SCREWDRIVER, GTToolType.WIRE_CUTTER, GTToolType.SCYTHE, GTToolType.KNIFE, GTToolType.BUTCHERY_KNIFE, GTToolType.DRILL_LV, GTToolType.DRILL_MV, GTToolType.DRILL_HV, GTToolType.DRILL_EV, GTToolType.DRILL_IV, GTToolType.CHAINSAW_LV, GTToolType.WRENCH_LV, GTToolType.WRENCH_HV, GTToolType.WRENCH_IV, GTToolType.BUZZSAW, GTToolType.SCREWDRIVER_LV, GTToolType.WIRE_CUTTER_LV, GTToolType.WIRE_CUTTER_HV, GTToolType.WIRE_CUTTER_IV).magnetic().build())
                .buildAndRegister();

        Alfsteel = material("alfsteel", "精灵钢")
                .ingot()
                .fluid()
                .blastTemp(3400, LOW)
                .element(GTOElements.ALFSTEEL)
                .flags(GTOMaterialFlags.GENERATE_CURVED_PLATE, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .color(0xffb700)
                .iconSet(BRIGHT)
                .buildAndRegister();

        Gaiasteel = material("gaiasteel", "盖亚钢")
                .rarity(Rarity.RARE)
                .ingot()
                .fluid()
                .radioactiveHazard(1)
                .element(GTOElements.GAIASTEEL)
                .blastTemp(4300, LOW)
                .flags(GTOMaterialFlags.GENERATE_CURVED_PLATE, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .color(0x660404)
                .iconSet(BRIGHT)
                .toolStats(ToolProperty.Builder.of(16.0F, 12, 32000, 7, GTToolType.SWORD, GTToolType.PICKAXE, GTToolType.SHOVEL, GTToolType.AXE, GTToolType.HOE, GTToolType.MINING_HAMMER, GTToolType.SPADE, GTToolType.SAW, GTToolType.HARD_HAMMER, GTToolType.WRENCH, GTToolType.FILE, GTToolType.CROWBAR, GTToolType.SCREWDRIVER, GTToolType.WIRE_CUTTER, GTToolType.SCYTHE, GTToolType.KNIFE, GTToolType.BUTCHERY_KNIFE, GTToolType.DRILL_LV, GTToolType.DRILL_MV, GTToolType.DRILL_HV, GTToolType.DRILL_EV, GTToolType.DRILL_IV, GTToolType.CHAINSAW_LV, GTToolType.WRENCH_LV, GTToolType.WRENCH_HV, GTToolType.WRENCH_IV, GTToolType.BUZZSAW, GTToolType.SCREWDRIVER_LV, GTToolType.WIRE_CUTTER_LV, GTToolType.WIRE_CUTTER_HV, GTToolType.WIRE_CUTTER_IV).magnetic().build())
                .buildAndRegister();

        Gaia = material("gaia", "盖亚魂")
                .rarity(Rarity.RARE)
                .ingot()
                .fluid()
                .element(GTOElements.GAIA)
                .blastTemp(5300, LOW)
                .flags(GTOMaterialFlags.GENERATE_CURVED_PLATE, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .iconSet(BRIGHT)
                .buildAndRegister();

        Orichalcos = material("orichalcos", "奥利哈刚")
                .ingot()
                .fluid()
                .blastTemp(6700, LOW)
                .element(GTOElements.AERIALITE)
                .color(0x590aa9)
                .iconSet(METALLIC)
                .flags(GTOMaterialFlags.GENERATE_CURVED_PLATE, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .buildAndRegister();

        Photonium = material("photonium", "光子")
                .ingot()
                .fluid()
                .blastTemp(2800, LOW)
                .element(GTOElements.AERIALITE)
                .color(0xb8b8b8)
                .iconSet(METALLIC)
                .flags(GTOMaterialFlags.GENERATE_CURVED_PLATE, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .buildAndRegister();

        Shadowium = material("shadowium", "暗影")
                .ingot()
                .fluid()
                .blastTemp(2800, LOW)
                .element(GTOElements.AERIALITE)
                .color(0x636363)
                .iconSet(METALLIC)
                .flags(GTOMaterialFlags.GENERATE_CURVED_PLATE, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .buildAndRegister();

        Aerialite = material("aerialite", "天空")
                .ingot()
                .fluid()
                .blastTemp(3400, LOW)
                .element(GTOElements.AERIALITE)
                .color(0x045b82)
                .iconSet(METALLIC)
                .flags(GTOMaterialFlags.GENERATE_CURVED_PLATE, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .buildAndRegister();

        Laureril = material("laureril", "秘金")
                .rarity(Rarity.UNCOMMON)
                .ingot()
                .fluid()
                .color(0xf5e2b3)
                .blastTemp(1200, LOW)
                .components(GTMaterials.Gold, 1)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .iconSet(METALLIC)
                .buildAndRegister()
                .setFormula("*La*", false);

        Quicksilver = material("quicksilver", "银钻")
                .ingot()
                .fluid()
                .blastTemp(10100, HIGHEST)
                .element(GTElements.Ag)
                .color(0x58C8B6)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE)
                .buildAndRegister().setFormula("Qs", false);

        Ignatius = material("ignatius", "伊格内休斯")
                .ingot()
                .fluid()
                .blastTemp(12300, HIGHEST)
                .element(GTOElements.ORICHALCUM)
                .color(0xFF9F34)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE)
                .buildAndRegister().setFormula("Ig", false);

        Ceruclase = material("ceruclase", "暗影秘银")
                .ingot()
                .fluid()
                .blastTemp(12100, HIGHEST)
                .element(GTOElements.MITHRIL)
                .color(0x3680B6)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE)
                .buildAndRegister().setFormula("Cc", false);

        Lemurite = material("lemurite", "利莫利亚")
                .ingot()
                .fluid()
                .blastTemp(12300, HIGHEST)
                .element(GTElements.Nq2)
                .color(0xC5CACB)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE)
                .buildAndRegister().setFormula("Lm", false);

        Alduorite = material("alduorite", "神秘蓝金")
                .ingot()
                .fluid()
                .blastTemp(13300, HIGHEST)
                .element(GTOElements.ENDERIUM)
                .color(0x17B4CB)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, GENERATE_GEAR)
                .buildAndRegister().setFormula("Ao", false);

        Kalendrite = material("kalendrite", "幽冥魂石")
                .ingot()
                .fluid()
                .blastTemp(13500, HIGHEST)
                .element(GTOElements.INFUSCOLIUM)
                .color(0x9A3AB3)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE)
                .buildAndRegister().setFormula("Kl", false);

        Celenegil = material("celenegil", "幽冥毒晶")
                .ingot()
                .fluid()
                .blastTemp(13700, HIGHEST)
                .element(GTOElements.INFUSCOLIUM)
                .color(0x399936)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE)
                .buildAndRegister().setFormula("Cg", false);

        Haderoth = material("haderoth", "幻铜")
                .ingot()
                .fluid()
                .blastTemp(14100, HIGHEST)
                .element(GTOElements.COPPER76)
                .color(0xB34616)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE)
                .buildAndRegister().setFormula("Hd", false);

        Sanguinite = material("sanguinite", "狱炎")
                .ingot()
                .fluid()
                .blastTemp(14900, HIGHEST)
                .element(GTOElements.ADAMANTIUM)
                .color(0xC81B00)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, GENERATE_GEAR)
                .buildAndRegister().setFormula("Su", false);
    }
}
