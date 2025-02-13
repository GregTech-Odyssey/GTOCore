package com.gto.gtocore.init.material;

import com.gto.gtocore.api.data.chemical.material.info.GTOMaterialFlags;
import com.gto.gtocore.init.GTOElements;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Rarity;

import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty.GasTier.LOW;
import static com.gto.gtocore.init.GTOMaterials.*;
import static com.gto.gtocore.utils.register.MaterialsRegisterUtils.material;

public interface MagicMaterial {

    static void init() {
        Mana = material("mana", "魔力")
                .gas()
                .dust()
                .color(0x00A7F7)
                .iconSet(BRIGHT)
                .buildAndRegister()
                .setFormula(ChatFormatting.ITALIC + "*Ma*", false);

        Salamander = material("salamander", "火元素")
                .fluid()
                .gas()
                .dust()
                .color(0xff0000)
                .iconSet(BRIGHT)
                .buildAndRegister()
                .setFormula("*Sa*", false);

        Undine = material("undine", "水元素")
                .fluid()
                .gas()
                .dust()
                .color(0x0099ff)
                .iconSet(BRIGHT)
                .buildAndRegister()
                .setFormula("*Un*", false);

        Sylph = material("sylph", "风元素")
                .fluid()
                .gas()
                .dust()
                .color(0x13e841)
                .iconSet(BRIGHT)
                .buildAndRegister()
                .setFormula("*Sy*", false);

        Gnome = material("gnome", "地元素")
                .fluid()
                .gas()
                .dust()
                .color(0xedc31c)
                .iconSet(BRIGHT)
                .buildAndRegister()
                .setFormula("*Gn*", false);

        Aether = material("aether", "以太")
                .fluid()
                .gas()
                .dust()
                .color(0xfbedff)
                .iconSet(BRIGHT)
                .buildAndRegister()
                .setFormula("*Ae*", false);

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

        Manasteel = material("manasteel", "魔力钢")
                .ingot()
                .fluid()
                .element(GTOElements.MANASTEEL)
                .flags(GTOMaterialFlags.GENERATE_CURVED_PLATE, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .color(0x3396fe)
                .blastTemp(1700, LOW)
                .secondaryColor(0x2e56d7)
                .iconSet(BRIGHT)
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
                .buildAndRegister();

        Living = material("living", "活铁")
                .ingot()
                .fluid()
                .components(GTMaterials.Iron, 1)
                .flags(GENERATE_PLATE, DISABLE_DECOMPOSITION)
                .color(0x2ae870)
                .iconSet(METALLIC)
                .buildAndRegister()
                .setFormula("?Fe", false);

        Alfsteel = material("alfsteel", "精灵钢")
                .ingot()
                .fluid()
                .element(GTOElements.ALFSTEEL)
                .flags(GTOMaterialFlags.GENERATE_CURVED_PLATE, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .color(0xffb700)
                .iconSet(BRIGHT)
                .buildAndRegister();

        Gaiasteel = material("gaiasteel", "盖亚钢")
                .rarity(Rarity.RARE)
                .ingot()
                .fluid()
                .element(GTOElements.GAIASTEEL)
                .blastTemp(4300, LOW)
                .flags(GTOMaterialFlags.GENERATE_CURVED_PLATE, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .color(0x660404)
                .iconSet(BRIGHT)
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

        laureril = material("laureril", "秘金")
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
                .blastTemp(14500, LOW)
                .components(GTMaterials.Steel, 1)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .iconSet(METALLIC)
                .buildAndRegister()
                .setFormula("*Sy*3*Un*3*Ma*4?");

        OriginalBronze = material("original_bronze", "原始青铜")
                .ingot()
                .fluid()
                .color(0x302a4a)
                .blastTemp(1395, LOW)
                .components(GTMaterials.Bronze, 2)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FRAME, GENERATE_LONG_ROD, GENERATE_FOIL, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .iconSet(METALLIC)
                .buildAndRegister()
                .setFormula("*Ma*3(SnCu3)2");

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
                .buildAndRegister();

        Dreamwood = material("dreamwood", "梦之木")
                .wood()
                .flags(GENERATE_FRAME, GENERATE_PLATE)
                .color(0xa6bcb6)
                .iconSet(WOOD)
                .buildAndRegister();

        Shimmerwood = material("shimmerwood", "微光木")
                .wood()
                .flags(GENERATE_FRAME, GENERATE_PLATE)
                .iconSet(WOOD)
                .buildAndRegister();

        Livingrock = material("livingrock", "活石")
                .dust()
                .flags(FORCE_GENERATE_BLOCK, GENERATE_ROD, GENERATE_PLATE)
                .color(0xcbcdbb)
                .iconSet(FINE)
                .buildAndRegister();

        Runerock = material("runerock", "符文石")
                .dust()
                .flags(FORCE_GENERATE_BLOCK, GENERATE_ROD, GENERATE_PLATE)
                .color(0xbcf7e5)
                .iconSet(FINE)
                .buildAndRegister();

        Shimmerrock = material("shimmerrock", "微光石")
                .dust()
                .flags(FORCE_GENERATE_BLOCK, GENERATE_ROD, GENERATE_PLATE)
                .iconSet(FINE)
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
    }
}
