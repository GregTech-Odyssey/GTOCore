package com.gtocore.common.data.material;

import com.gtocore.api.data.material.GTOMaterialFlags;
import com.gtocore.common.data.GTOElements;

import com.gtolib.api.item.tool.GTOToolType;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.ToolProperty;
import com.gregtechceu.gtceu.api.fluids.FluidBuilder;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Rarity;

import committee.nova.mods.avaritia.init.registry.ModRarities;

import java.awt.*;

import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty.GasTier.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.api.data.material.GTOMaterialFlags.GENERATE_CRYSTAL_SEED;
import static com.gtocore.api.data.material.GTOMaterialFlags.GENERATE_CURVED_PLATE;
import static com.gtocore.api.data.material.GTOMaterialIconSet.*;
import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtolib.utils.register.MaterialsRegisterUtils.material;

public final class MaterialA {

    public static void init() {
        // ae
        Fluix = material("fluix", "福鲁伊克斯")
                .gem()
                .components(NetherQuartz, 1, CertusQuartz, 1, Redstone, 1)
                .color(0x8f5ccb)
                .iconSet(QUARTZ)
                .flags(NO_SMASHING, NO_SMELTING, DISABLE_DECOMPOSITION, GENERATE_PLATE)
                .buildAndRegister();

        // ad
        Desh = material("desh", "戴斯")
                .ingot()
                .ore()
                .element(GTOElements.DESH)
                .color(0xf2a057)
                .iconSet(METALLIC)
                .flags(NO_SMELTING, NO_ORE_SMELTING, GENERATE_DENSE)
                .buildAndRegister();

        Ostrum = material("ostrum", "紫金")
                .ingot()
                .ore()
                .element(GTOElements.OSTRUM)
                .color(0xe5939b)
                .iconSet(METALLIC)
                .flags(NO_SMELTING, NO_ORE_SMELTING, GENERATE_DENSE)
                .toolStats(ToolProperty.Builder.of(60, 80, 12288, 6, GTOToolType.VAJRA_EV).magnetic().build())
                .buildAndRegister();

        Calorite = material("calorite", "耐热金属")
                .ingot()
                .ore()
                .element(GTOElements.CALORITE)
                .color(0xe65757)
                .iconSet(METALLIC)
                .flags(NO_SMELTING, NO_ORE_SMELTING, GENERATE_DENSE)
                .buildAndRegister();

        // eio
        CopperAlloy = material("copper_alloy", "铜合金")
                .ingot()
                .color(0xc79738)
                .iconSet(SHINY)
                .flags(DISABLE_DECOMPOSITION)
                .components(Copper, 1, Silicon, 1)
                .buildAndRegister();

        EnergeticAlloy = material("energetic_alloy", "充能合金")
                .ingot()
                .color(0xffb545)
                .iconSet(SHINY)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FRAME)
                .blastTemp(1650, LOW, GTValues.VA[GTValues.MV], 700)
                .components(Redstone, 1, Gold, 1, Glowstone, 1)
                .cableProperties(128, 1, 0, true)
                .buildAndRegister();

        VibrantAlloy = material("vibrant_alloy", "振动合金")
                .ingot()
                .fluid()
                .color(0xa4ff70)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FRAME)
                .blastTemp(2450, LOW, GTValues.VA[GTValues.MV], 900)
                .components(EnergeticAlloy, 1, EnderPearl, 1)
                .cableProperties(512, 1, 0, true)
                .buildAndRegister();

        RedstoneAlloy = material("redstone_alloy", "红石合金")
                .ingot()
                .fluid()
                .color(0xf66565)
                .iconSet(SHINY)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FRAME)
                .components(Redstone, 1, Silicon, 1)
                .buildAndRegister();

        ConductiveAlloy = material("conductive_alloy", "导电合金")
                .ingot()
                .fluid()
                .color(0xf7b29b)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .components(CopperAlloy, 1, Iron, 1, Redstone, 1)
                .cableProperties(32, 1, 0, true)
                .buildAndRegister();

        PulsatingAlloy = material("pulsating_alloy", "脉冲合金")
                .ingot()
                .fluid()
                .color(0x6ae26e)
                .iconSet(SHINY)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FRAME)
                .components(Iron, 1, EnderPearl, 1)
                .cableProperties(8, 1, 0, true)
                .buildAndRegister();

        DarkSteel = material("dark_steel", "玄钢")
                .ingot()
                .color(0x414751)
                .iconSet(DULL)
                .flags(GENERATE_PLATE, DISABLE_DECOMPOSITION)
                .blastTemp(1450, LOW, GTValues.VA[GTValues.MV], 600)
                .components(Iron, 1, Coal, 1, Obsidian, 1)
                .toolStats(ToolProperty.Builder.of(30, 16, 8192, 5, GTOToolType.VAJRA_HV).magnetic().build())
                .buildAndRegister();

        Soularium = material("soularium", "魂金")
                .ingot()
                .color(0x7c674d)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .components(Gold, 1, Concrete, 1)
                .buildAndRegister();

        EndSteel = material("end_steel", "末地钢")
                .ingot()
                .color(0xd6d980)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .blastTemp(3250, LOW, GTValues.VA[GTValues.HV], 900)
                .cableProperties(2048, 1, 0, true)
                .components(Endstone, 1, DarkSteel, 1, Obsidian, 1)
                .buildAndRegister();

        BarnardaAir = material("barnarda_air", "巴纳德C空气")
                .gas()
                .color(0x563a24)
                .iconSet(DULL)
                .buildAndRegister();

        AlienAlgae = material("alien_algae", "异星藻类渣")
                .ore()
                .addOreByproducts(Paper, Agar)
                .color(0x808000)
                .iconSet(WOOD)
                .buildAndRegister();

        Bloodstone = material("bloodstone", "血石")
                .ore()
                .addOreByproducts(Deepslate, Redstone)
                .color(0xd80036)
                .iconSet(QUARTZ)
                .buildAndRegister();

        HighEnergyMixture = material("high_energy_mixture", "高能混合物")
                .dust()
                .color(0xdbd69c)
                .iconSet(SAND)
                .buildAndRegister();

        LuminEssence = material("lumin_essence", "流明精华")
                .dust()
                .components(HighEnergyMixture, 1, PhosphoricAcid, 1)
                .color(0x838914)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Sunnarium = material("sunnarium", "阳光化合物")
                .ingot()
                .fluid()
                .color(0xfcfc00)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?", false);

        UuAmplifier = material("uu_amplifier", "UU增幅液")
                .fluid()
                .color(0xaa2b9f)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?", false);

        Celestine = material("celestine", "天青石")
                .ore()
                .color(0x3c4899)
                .components(Strontium, 1, Sulfur, 1, Oxygen, 4)
                .iconSet(EMERALD)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Zircon = material("zircon", "锆石")
                .ore()
                .color(0xde953c)
                .iconSet(EMERALD)
                .buildAndRegister().setFormula("ZrSiO₄", false);

        Jasper = material("jasper", "碧玉")
                .gem()
                .ore()
                .addOreByproducts(Talc, Boron)
                .color(0xc85050)
                .iconSet(EMERALD)
                .buildAndRegister().setFormula("?", false);

        BismuthTellurite = material("bismuth_tellurite", "亚碲酸铋")
                .dust()
                .components(Bismuth, 2, Tellurium, 3)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x004222)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Prasiolite = material("prasiolite", "堇云石")
                .dust()
                .components(Silicon, 5, Oxygen, 10, Iron, 1)
                .color(0x9eB749)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CubicZirconia = material("cubic_zirconia", "立方氧化锆")
                .gem()
                .color(0xf3d5d7)
                .components(Zirconium, 1, Oxygen, 2)
                .iconSet(EMERALD)
                .flags(GENERATE_PLATE, NO_SMASHING, NO_SMELTING, EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        MagnetoResonatic = material("magneto_resonatic", "共振紫晶")
                .gem()
                .color(0xff97ff)
                .components(Prasiolite, 3, BismuthTellurite, 6, CubicZirconia, 1, SteelMagnetic, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(MAGNETIC)
                .buildAndRegister();

        Adamantium = material("adamantium", "艾德曼合金")
                .ingot()
                .fluid()
                .plasma()
                .blastTemp(17700, HIGHER)
                .element(GTOElements.ADAMANTIUM)
                .color(0xefbe35)
                .iconSet(METALLIC)
                .flags(GENERATE_ROTOR, GENERATE_FRAME)
                .buildAndRegister();

        Quantanium = material("quantanium", "量子素")
                .rarity(Rarity.UNCOMMON)
                .ingot()
                .fluid()
                .blastTemp(12500, HIGHER)
                .element(GTOElements.QUANTANIUM)
                .color(0x0dff02)
                .iconSet(METALLIC)
                .flags(GENERATE_ROTOR, GENERATE_SMALL_GEAR, GENERATE_FRAME)
                .buildAndRegister();

        Vibranium = material("vibranium", "振金")
                .ingot()
                .fluid()
                .plasma()
                .ore()
                .addOreByproducts(Plutonium239, Plutonium241)
                .blastTemp(18500, HIGHER)
                .element(GTOElements.VIBRANIUM)
                .color(0xff0000)
                .iconSet(METALLIC)
                .flags(GTOMaterialFlags.GENERATE_NANITES, GENERATE_ROTOR, GENERATE_FRAME)
                .buildAndRegister();

        Indalloy140 = material("indalloy_140", "铋铅合金-140")
                .ingot()
                .fluid()
                .color(0x6a5acd)
                .blastTemp(2600, LOW, GTValues.VA[GTValues.EV])
                .components(Bismuth, 47, Lead, 25, Tin, 13, Cadmium, 10, Indium, 5)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ArtheriumTin = material("artherium_tin", "阿瑟锡")
                .ingot()
                .fluid()
                .color(0x551a8b)
                .blastTemp(9800, HIGHER, GTValues.VA[GTValues.IV])
                .components(Tin, 12, Actinium, 7, EnrichedNaquadahTriniumEuropiumDuranide, 5, Caesium, 4, Osmiridium, 3)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Tairitsu = material("tairitsu", "对立合金")
                .ingot()
                .fluid()
                .color(0x1c1c1c)
                .blastTemp(12100, HIGHER, GTValues.VA[GTValues.ZPM])
                .components(Tungsten, 8, Naquadria, 7, Trinium, 4, Carbon, 4, Vanadium, 3,
                        Plutonium239, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Draconium = material("draconium", "龙")
                .rarity(Rarity.UNCOMMON)
                .ingot()
                .fluid()
                .blastTemp(19200)
                .element(GTOElements.DRACONIUM)
                .color(0xa300cc)
                .iconSet(RADIOACTIVE)
                .flags(GTOMaterialFlags.GENERATE_NANITES, GENERATE_ROTOR, GENERATE_FRAME, NO_SMELTING)
                .buildAndRegister();

        Chaos = material("chaos", "混沌物质")
                .rarity(ModRarities.COSMIC)
                .ingot()
                .radioactiveHazard(50)
                .liquid(new FluidBuilder().temperature(1000000).customStill())
                .plasma()
                .blastTemp(28000, HIGHEST)
                .element(GTOElements.CHAOS)
                .iconSet(CHAOS)
                .color(0x000000)
                .flags(GENERATE_FOIL)
                .buildAndRegister();

        Cosmic = material("cosmic", "宇宙")
                .rarity(ModRarities.COSMIC)
                .ingot()
                .color(0x2d3e5e)
                .iconSet(COSMIC)
                .flags(GENERATE_FINE_WIRE, GENERATE_LONG_ROD)
                .buildAndRegister();

        Hypogen = material("hypogen", "海珀珍")
                .rarity(ModRarities.COSMIC)
                .ingot()
                .fluid()
                .color(0xda916b)
                .secondaryColor(0x8f993b)
                .blastTemp(34000, HIGHEST)
                .element(GTOElements.HYPogen)
                .iconSet(RADIOACTIVE)
                .flags(GENERATE_PLATE)
                .cableProperties(Integer.MAX_VALUE, 32768, 0, true)
                .buildAndRegister();

        Shirabon = material("shirabon", "调律源金")
                .rarity(ModRarities.COSMIC)
                .ingot()
                .fluid()
                .color(0xc61361)
                .blastTemp(64000, HIGHEST, GTValues.VA[GTValues.OpV], 1200)
                .element(GTOElements.SHIRABON)
                .flags(GENERATE_FINE_WIRE)
                .iconSet(METALLIC)
                .buildAndRegister();

        Mithril = material("mithril", "秘银")
                .ingot()
                .fluid()
                .plasma()
                .ore()
                .addOreByproducts(Actinium, Technetium)
                .blastTemp(14800, HIGHER)
                .element(GTOElements.MITHRIL)
                .color(0x4da6ff)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, GENERATE_SPRING, GENERATE_FRAME, GENERATE_SPRING_SMALL)
                .cableProperties(GTValues.V[GTValues.UEV], 2, 64)
                .buildAndRegister();

        Taranium = material("taranium", "塔兰")
                .ingot()
                .fluid()
                .radioactiveHazard(5)
                .blastTemp(16200, HIGHEST, GTValues.VA[GTValues.UIV], 1440)
                .element(GTOElements.TARANIUM)
                .color(0x000033)
                .iconSet(RADIOACTIVE)
                .flags(GENERATE_SPRING, GENERATE_SPRING_SMALL, GENERATE_FRAME)
                .cableProperties(GTValues.V[GTValues.UXV], 2, 64)
                .buildAndRegister();

        CrystalMatrix = material("crystal_matrix", "水晶矩阵")
                .rarity(Rarity.RARE)
                .ingot()
                .fluid()
                .plasma()
                .radioactiveHazard(40)
                .blastTemp(19600, HIGHEST)
                .element(GTOElements.CRYSTALMATRIX)
                .color(0x33ffff)
                .iconSet(RADIOACTIVE)
                .flags(GENERATE_SPRING, GENERATE_SPRING_SMALL)
                .cableProperties(GTValues.V[GTValues.OpV], 2, 128)
                .buildAndRegister();

        CosmicNeutronium = material("cosmic_neutronium", "宇宙中子素")
                .rarity(ModRarities.COSMIC)
                .ingot()
                .radioactiveHazard(50)
                .liquid(new FluidBuilder().temperature(1000000).customStill())
                .blastTemp(24800, HIGHEST)
                .element(GTOElements.COSMICNEUTRONIUM)
                .color(0x000d1a)
                .iconSet(COSMIC_NEUTRONIUM)
                .flags(GTOMaterialFlags.GENERATE_NANITES, GENERATE_SPRING, GENERATE_FINE_WIRE,
                        GENERATE_SPRING_SMALL)
                .cableProperties(Integer.MAX_VALUE, 2, 128)
                .buildAndRegister();

        Echoite = material("echoite", "回响合金")
                .rarity(Rarity.RARE)
                .ingot()
                .fluid()
                .plasma()
                .radioactiveHazard(20)
                .blastTemp(17300, HIGHER)
                .element(GTOElements.ECHOITE)
                .color(0x26734d)
                .iconSet(METALLIC)
                .flags(GENERATE_ROD, GENERATE_FINE_WIRE)
                .cableProperties(GTValues.V[GTValues.UIV], 32, 0, true)
                .buildAndRegister();

        Legendarium = material("legendarium", "传奇合金")
                .rarity(Rarity.RARE)
                .ingot()
                .fluid()
                .plasma()
                .radioactiveHazard(40)
                .blastTemp(21400, HIGHEST)
                .element(GTOElements.LEGENDARIUM)
                .color(0x00ffff)
                .iconSet(RADIOACTIVE)
                .flags(GENERATE_FINE_WIRE)
                .cableProperties(GTValues.V[GTValues.UXV], 64, 0, true)
                .buildAndRegister();

        AwakenedDraconium = material("awakened_draconium", "觉醒龙")
                .rarity(ModRarities.LEGEND)
                .ingot()
                .fluid()
                .plasma()
                .radioactiveHazard(60)
                .blastTemp(22600, HIGHEST)
                .element(GTOElements.AWAKENEDDRACONIUM)
                .color(0xcc6600)
                .iconSet(METALLIC)
                .flags(GENERATE_FINE_WIRE)
                .cableProperties(GTValues.V[GTValues.OpV], 64, 0, true)
                .buildAndRegister();

        SpaceTime = material("spacetime", "时空")
                .ingot()
                .liquid(new FluidBuilder().temperature(1).customStill())
                .fluidPipeProperties(2147483647, 33554431, true, true, true)
                .element(GTOElements.SPACETIME)
                .iconSet(new MaterialIconSet("spacetime"))
                .flags(GTOMaterialFlags.GENERATE_NANITES, NO_UNIFICATION)
                .cableProperties(Integer.MAX_VALUE, 524288, 0, true)
                .buildAndRegister();

        Infinity = material("infinity", "无尽")
                .rarity(ModRarities.COSMIC)
                .ingot()
                .radioactiveHazard(80)
                .liquid(new FluidBuilder().temperature(1000000).customStill())
                .blastTemp(32000, HIGHEST)
                .element(GTOElements.INFINITY)
                .iconSet(INFINITY)
                .flags(GENERATE_FRAME, GENERATE_GEAR, GENERATE_BOLT_SCREW, GTOMaterialFlags.GENERATE_COIN)
                .cableProperties(Integer.MAX_VALUE, 8192, 0, true)
                .buildAndRegister();

        BlazeCube = material("blazecube", "炽骨立方")
                .ingot()
                .fluid()
                .plasma()
                .blastTemp(11000, HIGHER)
                .element(GTOElements.BLAZECUBE)
                .color(0xff6600)
                .iconSet(METALLIC)
                .flags(GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .cableProperties(GTValues.V[GTValues.IV], 128, 32)
                .buildAndRegister();

        Etrium = material("etrium", "埃忒恩")
                .ingot()
                .fluid()
                .blastTemp(11800, HIGHER)
                .element(GTOElements.ETRIUM)
                .color(new Color(123, 252, 215).getRGB())
                .iconSet(SHINY)
                .flags(GENERATE_FRAME, GENERATE_PLATE, GENERATE_FOIL, GENERATE_ROD)
                .buildAndRegister();

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
                        GENERATE_BOLT_SCREW, GENERATE_RING)
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
                        GENERATE_BOLT_SCREW, GENERATE_RING)
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
                        GENERATE_BOLT_SCREW, GENERATE_RING)
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
                        GENERATE_BOLT_SCREW, GENERATE_RING)
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
                        GENERATE_BOLT_SCREW, GENERATE_RING)
                .buildAndRegister();
        ThermalConductiveAlloy = material("thermal_conductive_alloy", "高热导合金")
                .ingot()
                .fluid()
                .color(0xffe278)
                .secondaryColor(0xc2a664)
                .blastTemp(8964, HIGHER, GTValues.VA[GTValues.ZPM], 400)
                .components(Copper, 57, Silver, 40, Zirconium, 27)
                .iconSet(BRIGHT)
                .flags(GENERATE_GEAR, GENERATE_CURVED_PLATE,
                        GENERATE_SMALL_GEAR, GENERATE_LONG_ROD,
                        GENERATE_FRAME, GENERATE_FOIL,
                        GENERATE_BOLT_SCREW, GENERATE_RING)
                .buildAndRegister();
        Titanium5553 = material("titanium_5553", "钛合金-5553")
                .ingot()
                .fluid()
                .color(0xc380c6)
                .secondaryColor(0x8b5e8b)
                .blastTemp(8660, HIGHER, GTValues.VA[GTValues.LuV], 900)
                .components(Titanium, 82, Aluminium, 5, Vanadium, 5, Molybdenum, 5, Chromium, 3)
                .iconSet(METALLIC)
                .flags(GENERATE_GEAR, GENERATE_CURVED_PLATE,
                        GENERATE_SMALL_GEAR, GENERATE_LONG_ROD,
                        GENERATE_FRAME, GENERATE_FOIL,
                        GENERATE_BOLT_SCREW, GENERATE_RING)
                .buildAndRegister();
        UltraLightweightCompositeSteel = material("ultra_lightweight_composite_steel", "超轻复合钢")
                .ingot()
                .fluid()
                .color(0x8b8b8b)
                .secondaryColor(0x4c4c4c)
                .blastTemp(10450, HIGHER, GTValues.VA[GTValues.IV], 600)
                .components(Steel, 56, Manganese, 29, Aluminium, 12, Silicon, 1, Chromium, 5)
                .iconSet(SHINY)
                .flags(GENERATE_GEAR, GENERATE_CURVED_PLATE,
                        GENERATE_SMALL_GEAR, GENERATE_LONG_ROD,
                        GENERATE_FRAME, GENERATE_FOIL,
                        GENERATE_BOLT_SCREW, GENERATE_RING)
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
                        GENERATE_BOLT_SCREW, GENERATE_RING)
                .buildAndRegister();
        MoonGoddessTitanium = material("moon_goddess_titanium", "月神钛")
                .ingot()
                .fluid()
                .color(0xd3b8d2)
                .secondaryColor(0xa899a8)
                .blastTemp(11660, HIGHER, GTValues.VA[GTValues.UHV], 900)
                .components(Titanium, 25, Aluminium, 1, Vanadium, 5, Tin, 1, Chromium, 1, AstralSilver, 3, Etrium, 6)
                .iconSet(METALLIC)
                .flags(GENERATE_GEAR, GENERATE_CURVED_PLATE,
                        GENERATE_SMALL_GEAR, GENERATE_LONG_ROD,
                        GENERATE_FRAME, GENERATE_FOIL,
                        GENERATE_BOLT_SCREW, GENERATE_RING)
                .buildAndRegister();

        Adamantine = material("adamantine", "精金")
                .ingot()
                .fluid()
                .blastTemp(14400, HIGHER)
                .element(GTOElements.ADAMANTINE)
                .color(0xe6e600)
                .iconSet(METALLIC)
                .flags(GENERATE_FINE_WIRE, GTOMaterialFlags.GENERATE_COIN)
                .cableProperties(GTValues.V[GTValues.UIV], 4, 128)
                .buildAndRegister();

        Starmetal = material("starmetal", "星辉")
                .rarity(Rarity.EPIC)
                .ingot()
                .fluid()
                .plasma()
                .radioactiveHazard(30)
                .addOreByproducts(Sapphire, Polonium)
                .blastTemp(21800, HIGHEST)
                .element(GTOElements.STARMETAL)
                .color(0x0000e6)
                .iconSet(METALLIC)
                .flags(GTOMaterialFlags.GENERATE_NANITES, GENERATE_FINE_WIRE)
                .cableProperties(GTValues.V[GTValues.OpV], 4, 256)
                .buildAndRegister();

        Orichalcum = material("orichalcum", "山铜")
                .ingot()
                .fluid()
                .plasma()
                .ore()
                .blastTemp(15300, HIGHER)
                .element(GTOElements.ORICHALCUM)
                .color(0xff78c9)
                .iconSet(METALLIC)
                .flags(GTOMaterialFlags.GENERATE_COMPONENT, GTOMaterialFlags.GENERATE_NANITES, GENERATE_ROUND, GENERATE_ROTOR, GENERATE_GEAR,
                        GENERATE_SMALL_GEAR, GENERATE_LONG_ROD)
                .buildAndRegister();

        Infuscolium = material("infuscolium", "魔金")
                .ingot()
                .fluid()
                .plasma()
                .radioactiveHazard(20)
                .blastTemp(17500, HIGHER)
                .element(GTOElements.INFUSCOLIUM)
                .color(0xff77ff)
                .iconSet(RADIOACTIVE)
                .flags(GTOMaterialFlags.GENERATE_COMPONENT, GTOMaterialFlags.GENERATE_NANITES, GENERATE_ROUND, GENERATE_ROTOR, GENERATE_GEAR,
                        GENERATE_SMALL_GEAR, GENERATE_LONG_ROD)
                .buildAndRegister();

        Enderium = material("enderium", "末影素")
                .rarity(Rarity.RARE)
                .ingot()
                .fluid()
                .plasma()
                .ore()
                .radioactiveHazard(10)
                .fluidPipeProperties(100000, 100000, true, true, true)
                .addOreByproducts(Endstone, EnderPearl)
                .blastTemp(16800, HIGHER)
                .element(GTOElements.ENDERIUM)
                .color(0x75ede2)
                .iconSet(METALLIC)
                .flags(GTOMaterialFlags.GENERATE_NANITES, GENERATE_FINE_WIRE)
                .toolStats(ToolProperty.Builder.of(1000, 3000, 100, 6, GTOToolType.VAJRA_IV).magnetic().unbreakable().build())
                .buildAndRegister();

        Eternity = material("eternity", "永恒")
                .rarity(ModRarities.COSMIC)
                .ingot()
                .radioactiveHazard(100)
                .liquid(new FluidBuilder().customStill())
                .blastTemp(36000, null, GTValues.VA[GTValues.MAX], 3600)
                .element(GTOElements.ETERNITY)
                .iconSet(ETERNITY)
                .flags(GTOMaterialFlags.GENERATE_NANITES, GENERATE_FOIL, GENERATE_FRAME)
                .buildAndRegister();

        Magmatter = material("magmatter", "磁物质")
                .rarity(ModRarities.COSMIC)
                .ingot()
                .liquid(new FluidBuilder().customStill())
                .element(GTOElements.MAGMATTER)
                .iconSet(MAGMATTER)
                .flags(GENERATE_LONG_ROD, NO_UNIFICATION)
                .buildAndRegister();

        DegenerateRhenium = material("degenerate_rhenium", "简并态铼")
                .rarity(Rarity.RARE)
                .dust()
                .plasma()
                .fluid()
                .radioactiveHazard(10)
                .color(0x4646ff)
                .element(GTOElements.DEGENERATE_REHENIUM)
                .iconSet(RADIOACTIVE)
                .flags(GENERATE_PLATE, NO_UNIFICATION)
                .buildAndRegister();

        HeavyQuarkDegenerateMatter = material("heavy_quark_degenerate_matter", "重夸克简并物质")
                .rarity(Rarity.UNCOMMON)
                .ingot()
                .fluid()
                .plasma()
                .radioactiveHazard(20)
                .fluidPipeProperties(1000000, 1000000, true, true, true)
                .element(GTOElements.HEAVY_QUARK_DEGENERATE_MATTER)
                .blastTemp(178000, HIGHER)
                .color(0x52a733)
                .iconSet(BRIGHT)
                .flags(GENERATE_PLATE, GENERATE_FINE_WIRE)
                .buildAndRegister();

        MetastableHassium = material("metastable_hassium", "亚稳态\ud872\udf76")
                .plasma()
                .fluid()
                .radioactiveHazard(2)
                .components(Hassium, 1)
                .color(0x78766f)
                .iconSet(RADIOACTIVE)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Inconel625 = material("inconel_625", "镍铬基合金-625")
                .ingot()
                .fluid()
                .color(0x00cd66)
                .blastTemp(4850, HIGH, GTValues.VA[GTValues.IV])
                .components(Nickel, 8, Chromium, 6, Molybdenum, 4, Niobium, 4, Titanium, 3, Iron, 2,
                        Aluminium, 2)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION, GENERATE_PLATE, GENERATE_GEAR, GENERATE_SMALL_GEAR,
                        GENERATE_BOLT_SCREW)
                .buildAndRegister();

        HastelloyN75 = material("hastelloy_n_75", "哈斯特洛依合金-N75")
                .ingot()
                .fluid()
                .color(0x8b6914)
                .blastTemp(4550, HIGH, GTValues.VA[GTValues.EV])
                .components(Nickel, 15, Molybdenum, 9, Chromium, 4, Titanium, 2, Erbium, 2)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION, GENERATE_BOLT_SCREW, GENERATE_GEAR, GENERATE_SMALL_GEAR,
                        GENERATE_PLATE)
                .buildAndRegister();

        MetastableOganesson = material("metastable_oganesson", "亚稳态鿫")
                .fluid()
                .radioactiveHazard(2)
                .color(0x8b000e)
                .components(Oganesson, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        QuantumChromoDynamicallyConfinedMatter = material("quantum_chromo_dynamically_confined_matter", "量子色动力学封闭物质")
                .rarity(Rarity.UNCOMMON)
                .ingot()
                .fluid()
                .plasma()
                .radioactiveHazard(10)
                .element(GTOElements.QUANTUM_CHROMO_DYNAMICALLY_CONFINED_MATTER)
                .blastTemp(13100, HIGHER)
                .color(0xd08c38)
                .iconSet(QUANTUM_CHROMO_DYNAMICALLY)
                .flags(GENERATE_FRAME, GENERATE_PLATE)
                .buildAndRegister();

        TranscendentMetal = material("transcendent_metal", "超时空金属")
                .rarity(Rarity.UNCOMMON)
                .ingot()
                .fluid()
                .element(GTOElements.TRANSCENDENTMETAL)
                .color(0xffffff)
                .iconSet(TRANSCENDENT)
                .flags(GTOMaterialFlags.GENERATE_COMPONENT, GTOMaterialFlags.GENERATE_NANITES, GENERATE_ROUND, GENERATE_ROTOR, GENERATE_GEAR,
                        GENERATE_SMALL_GEAR, GENERATE_LONG_ROD)
                .buildAndRegister();

        Uruium = material("uruium", "乌鲁")
                .ingot()
                .fluid()
                .ore()
                .radioactiveHazard(10)
                .addOreByproducts(Europium)
                .blastTemp(14600, HIGHER, GTValues.VA[GTValues.UIV], 1200)
                .element(GTOElements.URUIUM)
                .color(0x87ceeb)
                .flags(GTOMaterialFlags.GENERATE_NANITES)
                .iconSet(METALLIC)
                .cableProperties(Integer.MAX_VALUE, 16, 536870912)
                .buildAndRegister();

        MagnetohydrodynamicallyConstrainedStarMatter = material("magnetohydrodynamically_constrained_star_matter", "磁流体动力学约束恒星物质")
                .rarity(ModRarities.COSMIC)
                .ingot()
                .radioactiveHazard(100)
                .liquid(new FluidBuilder().temperature(100).customStill())
                .element(GTOElements.RAW_STAR_MATTER)
                .iconSet(MAGNETOHYDRODYNAMICALLY_CONSTRAINED_STAR_MATTER)
                .flags(GENERATE_FRAME, GENERATE_FOIL, NO_UNIFICATION)
                .buildAndRegister();

        WhiteDwarfMatter = material("white_dwarf_mtter", "白矮星物质")
                .rarity(Rarity.UNCOMMON)
                .ingot()
                .fluid()
                .radioactiveHazard(10)
                .element(GTOElements.STAR_MATTER)
                .color(0xffffff)
                .flags(GTOMaterialFlags.GENERATE_NANITES, GENERATE_FINE_WIRE)
                .iconSet(BRIGHT)
                .buildAndRegister();

        BlackDwarfMatter = material("black_dwarf_mtter", "黑矮星物质")
                .rarity(Rarity.UNCOMMON)
                .ingot()
                .fluid()
                .radioactiveHazard(10)
                .element(GTOElements.STAR_MATTER)
                .color(0x000000)
                .flags(GTOMaterialFlags.GENERATE_NANITES, GENERATE_FINE_WIRE)
                .iconSet(BRIGHT)
                .buildAndRegister();

        AstralTitanium = material("astral_titanium", "星体钛")
                .rarity(Rarity.UNCOMMON)
                .ingot()
                .fluid()
                .plasma()
                .radioactiveHazard(10)
                .element(GTOElements.ASTRALTITANIUM)
                .color(0xf6cbf6)
                .flags(GENERATE_GEAR)
                .iconSet(BRIGHT)
                .buildAndRegister();

        CelestialTungsten = material("celestial_tungsten", "天体钨")
                .rarity(Rarity.UNCOMMON)
                .ingot()
                .fluid()
                .plasma()
                .radioactiveHazard(10)
                .element(GTOElements.CELESTIALTUNGSTEN)
                .color(0x303030)
                .flags(GENERATE_GEAR)
                .iconSet(BRIGHT)
                .buildAndRegister();

        HexaphaseCopper = material("hexaphasecopper", "六相铜")
                .rarity(Rarity.UNCOMMON)
                .ingot()
                .fluid()
                .plasma()
                .radioactiveHazard(100)
                .color(0xec7916)
                .element(GTOElements.HEXAPHASECOPPER)
                .blastTemp(75000, HIGHER)
                .iconSet(METALLIC)
                .flags(GTOMaterialFlags.GENERATE_NANITES, GENERATE_ROTOR, GENERATE_FRAME, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_LONG_ROD)
                .buildAndRegister();

        ChromaticGlass = material("chromatic_glass", "彩色玻璃")
                .rarity(Rarity.UNCOMMON)
                .gem()
                .fluid()
                .plasma()
                .radioactiveHazard(100)
                .element(GTOElements.CHROMATICGLASS)
                .flags(GENERATE_LENS)
                .iconSet(GLASS)
                .buildAndRegister();

        Bedrockium = material("bedrockium", "基岩素")
                .ingot()
                .flags(GENERATE_PLATE)
                .color(0x808080)
                .element(GTOElements.BEDROCKIUM)
                .iconSet(new MaterialIconSet("bedrockium"))
                .buildAndRegister();

        ChaosInfinityAlloy = material("chaos_infinity_alloy", "混沌无尽合金")
                .rarity(ModRarities.COSMIC)
                .ingot()
                .radioactiveHazard(100)
                .liquid(new FluidBuilder().customStill())
                .color(0x000000)
                .blastTemp(32000, HIGHEST)
                .element(GTOElements.INFINITY)
                .iconSet(INFINITY_CHAOS)
                .flags(GENERATE_FRAME, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .buildAndRegister()
                .setFormula("§8§kc§r§8∞§r§8§kc", false);

        Enderite = material("enderite", "末影素合金")
                .ingot()
                .fluid()
                .blastTemp(14400, HIGHER, GTValues.VA[GTValues.UEV], 600)
                .components(Enderium, 3, EnderPearl, 2, ManganesePhosphide, 1, MagnesiumDiboride, 1,
                        MercuryBariumCalciumCuprate, 1, UraniumTriplatinum, 1,
                        SamariumIronArsenicOxide, 1, IndiumTinBariumTitaniumCuprate, 1)
                .color(0x006699)
                .iconSet(METALLIC)
                .flags(GENERATE_FINE_WIRE, DISABLE_DECOMPOSITION)
                .cableProperties(GTValues.V[GTValues.UEV], 32, 0, true)
                .buildAndRegister();

        NaquadriaticTaranium = material("naquadriatictaranium", "超能硅岩-塔兰金属合金")
                .ingot()
                .fluid()
                .blastTemp(16200, HIGHEST, GTValues.VA[GTValues.UXV], 1400)
                .components(Naquadria, 1, Taranium, 1)
                .color(0x000d1a)
                .iconSet(RADIOACTIVE)
                .flags(GENERATE_ROD, GENERATE_FINE_WIRE, DISABLE_DECOMPOSITION, GENERATE_FRAME)
                .cableProperties(GTValues.V[GTValues.UXV], 4, 128)
                .buildAndRegister();

        AbyssalAlloy = material("abyssalalloy", "渊狱合金")
                .ingot()
                .fluid()
                .radioactiveHazard(10)
                .blastTemp(10800, HIGHER, GTValues.VA[GTValues.UV], 1800)
                .components(StainlessSteel, 5, TungstenCarbide, 5, Nichrome, 5, Bronze, 5,
                        IncoloyMA956, 5, Iodine, 1, Germanium, 1, Radon, 1, Hafnium, 1,
                        BarnardaAir, 1)
                .color(0x9e706a)
                .iconSet(METALLIC)
                .flags(GENERATE_FINE_WIRE, DISABLE_DECOMPOSITION)
                .cableProperties(GTValues.V[GTValues.UHV], 4, 64)
                .buildAndRegister();

        TitanSteel = material("titansteel", "泰坦钢")
                .ingot()
                .fluid()
                .radioactiveHazard(10)
                .blastTemp(12600, HIGHER, GTValues.VA[GTValues.UHV], 1200)
                .components(TitaniumTungstenCarbide, 4, Plutonium241, 1, Einsteinium, 2, Rhenium, 1, Erbium, 1, Jasper, 3, UuAmplifier, 1)
                .color(0xe60000)
                .iconSet(METALLIC)
                .flags(GENERATE_FINE_WIRE, DISABLE_DECOMPOSITION)
                .cableProperties(GTValues.V[GTValues.UEV], 4, 64)
                .buildAndRegister();

        HighDurabilityCompoundSteel = material("high_durability_compound_steel", "高强度复合钢")
                .ingot()
                .fluid()
                .blastTemp(12600, HIGHER, GTValues.VA[GTValues.UV], 1600)
                .components(TungstenSteel, 12, HSSS, 9, HSSG, 6, Ruridit, 3, MagnetoResonatic, 2, Plutonium239, 1)
                .color(0x2d3c2d)
                .iconSet(METALLIC)
                .flags(GENERATE_BOLT_SCREW, GENERATE_PLATE, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GermaniumTungstenNitride = material("germanium_tungsten_nitride", "锗-钨氮化物")
                .ingot()
                .fluid()
                .blastTemp(8200, HIGHER, GTValues.VA[GTValues.LuV], 800)
                .components(Germanium, 3, Tungsten, 3, Nitrogen, 10)
                .color(0x7070a2)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        BlackTitanium = material("black_titanium", "黑钛合金")
                .ingot()
                .fluid()
                .blastTemp(18900, HIGHEST, GTValues.VA[GTValues.UXV], 600)
                .components(Titanium, 26, Lanthanum, 6, Tungsten, 4, Cobalt, 3, Manganese, 2,
                        Phosphorus, 2, Palladium, 2, Niobium, 1, Argon, 5)
                .color(0x3d0021)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, GENERATE_FRAME, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TriniumTitanium = material("trinium_titanium", "凯金钛合金")
                .ingot()
                .fluid()
                .blastTemp(14400, HIGHER, GTValues.VA[GTValues.UIV], 800)
                .components(Trinium, 2, Titanium, 1)
                .color(0x856f91)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, GENERATE_FINE_WIRE, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Cinobite = material("cinobite", "西诺柏合金")
                .ingot()
                .fluid()
                .blastTemp(15400, HIGHER, GTValues.VA[GTValues.UIV], 1300)
                .components(Zeron100, 8, Naquadria, 4, Terbium, 3, Aluminium, 2, Mercury, 1, Tin, 1, Titanium, 6, Osmiridium, 1)
                .color(0x000000)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, GENERATE_FINE_WIRE, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HastelloyX78 = material("hastelloyx_78", "哈斯特洛依合金-X78")
                .ingot()
                .fluid()
                .blastTemp(14400, HIGHER, GTValues.VA[GTValues.UEV], 1200)
                .components(NaquadahAlloy, 10, Rhenium, 5, Naquadria, 4, Tritanium, 4, TungstenCarbide, 1,
                        Promethium, 1, Mendelevium, 1, Praseodymium, 1)
                .color(0x3c5b7f)
                .iconSet(METALLIC)
                .flags(GTOMaterialFlags.GENERATE_COMPONENT, GENERATE_ROUND, GENERATE_ROTOR, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_LONG_ROD,
                        GENERATE_FINE_WIRE, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HastelloyK243 = material("hastelloyk_243", "哈斯特洛依合金-K243")
                .ingot()
                .fluid()
                .blastTemp(17200, HIGHEST, GTValues.VA[GTValues.UXV], 1500)
                .components(HastelloyX78, 5, NiobiumNitride, 2, Tritanium, 4, TungstenCarbide, 4,
                        Promethium, 1, Mendelevium, 1, Praseodymium, 1, Holmium, 1)
                .color(0x92d959)
                .iconSet(METALLIC)
                .flags(GTOMaterialFlags.GENERATE_COMPONENT, GENERATE_ROUND, GENERATE_ROTOR, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_LONG_ROD,
                        DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Vibramantium = material("vibramantium", "艾德曼振金")
                .ingot()
                .fluid()
                .blastTemp(18800, HIGHER, GTValues.VA[GTValues.UXV], 1800)
                .components(Vibranium, 1, Adamantium, 3)
                .color(0xff009c)
                .iconSet(METALLIC)
                .flags(GTOMaterialFlags.GENERATE_COMPONENT, GENERATE_ROUND, GENERATE_ROTOR, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_LONG_ROD, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        EglinSteel = material("eglin_steel", "埃格林钢")
                .ingot()
                .fluid()
                .blastTemp(1048, LOW)
                .components(Iron, 4, Kanthal, 1, Invar, 5, Sulfur, 1, Silicon, 1, Carbon, 1)
                .color(0x4e270b)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, GENERATE_GEAR, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Inconel792 = material("inconel_792", "镍铬基合金-792")
                .ingot()
                .fluid()
                .blastTemp(5200, HIGH)
                .components(Nickel, 2, Niobium, 1, Aluminium, 2, Nichrome, 1)
                .color(0x44974a)
                .iconSet(METALLIC)
                .flags(GENERATE_BOLT_SCREW, GENERATE_FRAME, GENERATE_GEAR, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Pikyonium = material("pikyonium", "皮卡优合金")
                .ingot()
                .fluid()
                .blastTemp(10400, HIGHER, GTValues.VA[GTValues.ZPM], 800)
                .components(Inconel792, 8, EglinSteel, 5, NaquadahEnriched, 4, Cerium, 3,
                        Antimony, 2, Platinum, 2, Ytterbium, 1, TungstenSteel, 4)
                .color(0x244780)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, DISABLE_DECOMPOSITION, GENERATE_FRAME)
                .buildAndRegister();

        HastelloyN = material("hastelloy_n", "哈斯特洛依合金-N")
                .ingot()
                .fluid()
                .blastTemp(4350, HIGHER, 1920)
                .components(Iridium, 2, Molybdenum, 4, Chromium, 2, Titanium, 2, Nickel, 15)
                .color(0xaaaaaa)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, GENERATE_FRAME, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AluminiumBronze = material("aluminium_bronze", "铝青铜")
                .ingot()
                .fluid()
                .blastTemp(1200, LOW)
                .components(Aluminium, 1, Bronze, 6)
                .color(0xffdead)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, GENERATE_FRAME, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Lafium = material("lafium", "路菲恩")
                .ingot()
                .fluid()
                .blastTemp(9865, HIGHER, 1920)
                .components(HastelloyN, 8, Naquadah, 4, Samarium, 2, Tungsten, 4,
                        Aluminium, 6, Nickel, 2, Carbon, 2)
                .color(0x235497)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Grisium = material("grisium", "灰钛合金")
                .ingot()
                .fluid()
                .blastTemp(4850, HIGH)
                .components(Titanium, 9, Carbon, 9, Potassium, 9, Lithium, 9, Sulfur, 9,
                        Hydrogen, 5)
                .color(0x355d6a)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Stellite = material("stellite", "铬钴锰钛合金")
                .ingot()
                .fluid()
                .blastTemp(4310, HIGH, 1920)
                .components(Cobalt, 9, Chromium, 9, Manganese, 5, Titanium, 2)
                .color(0x888192)
                .iconSet(METALLIC)
                .flags(GENERATE_GEAR, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SiliconCarbide = material("silicon_carbide", "碳化硅")
                .ingot()
                .fluid()
                .blastTemp(4800, HIGH, 480)
                .components(Silicon, 1, Carbon, 1)
                .color(0x34adb6)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        QuantumMetal = material("quantum_metal", "量子金属")
                .ingot()
                .fluid()
                .blastTemp(11400, HIGHER, 1920)
                .components(Stellite, 15, Quantanium, 3, Jasper, 2, Gallium, 5, Americium, 5,
                        Palladium, 5, Germanium, 5, SiliconCarbide, 5)
                .color(0x0d0d0d)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FOIL, GENERATE_FRAME)
                .buildAndRegister();

        FluxedElectrum = material("fluxed_electrum", "通流琥珀金")
                .ingot()
                .fluid()
                .blastTemp(10400, HIGHER, 1920)
                .components(SolderingAlloy, 1, InfusedGold, 1, Naquadah, 1, AstralSilver, 1,
                        RedSteel, 1, BlueSteel, 1, SterlingSilver, 1, RoseGold, 1)
                .color(0xf8f8d6)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FOIL)
                .buildAndRegister();

        Tanmolyium = material("tanmolyium", "钛钼合金β-C")
                .ingot()
                .fluid()
                .blastTemp(4300, HIGH, 1920)
                .components(Titanium, 5, Molybdenum, 5, Vanadium, 2, Chromium, 3, Aluminium, 1)
                .color(0x97249a)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION, GENERATE_PLATE)
                .buildAndRegister();

        Dalisenite = material("dalisenite", "大力合金")
                .ingot()
                .fluid()
                .blastTemp(12400, HIGHER, 7680)
                .components(Erbium, 3, Tungsten, 10, Naquadah, 1, NiobiumTitanium, 9, Quantanium, 7,
                        RhodiumPlatedPalladium, 14, Tanmolyium, 1)
                .color(0xa4ac11)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FOIL)
                .buildAndRegister();

        ArceusAlloy2B = material("arceusalloy2b", "阿尔宙斯合金2B")
                .ingot()
                .fluid()
                .blastTemp(14200, HIGHER, 122880)
                .components(Trinium, 3, MaragingSteel300, 4, Orichalcum, 1, NetherStar, 2,
                        TungstenSteel, 2, Osmiridium, 1, Strontium, 2)
                .color(0x79740e)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FOIL)
                .buildAndRegister();

        TitanPrecisionSteel = material("titan_precision_steel", "泰坦精钢")
                .ingot()
                .fluid()
                .blastTemp(16000, HIGHER, 491520)
                .components(TitanSteel, 3, Ytterbium, 1, PerditioCrystal, 1, GnomeCrystal, 1,
                        SalamanderCrystal, 1)
                .color(0x595137)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FOIL)
                .buildAndRegister();

        Lumiium = material("lumiium", "流明")
                .ingot()
                .fluid()
                .blastTemp(5400, HIGH)
                .components(SterlingSilver, 2, TinAlloy, 4, LuminEssence, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xd9e222)
                .iconSet(METALLIC)
                .buildAndRegister();

        Hikarium = material("hikarium", "光素")
                .ingot()
                .fluid()
                .blastTemp(17800, HIGHER, GTValues.VA[GTValues.UHV])
                .components(Lumiium, 18, Silver, 8, Sunnarium, 4)
                .color(0xe2bede)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FOIL)
                .buildAndRegister();

        SuperheavyLAlloy = material("superheavy_l_alloy", "超重元素-轻合金")
                .ingot()
                .fluid()
                .blastTemp(10600, HIGHER)
                .components(Rutherfordium, 1, Dubnium, 1, Seaborgium, 1, Bohrium, 1, Hassium, 1,
                        Meitnerium, 1, Darmstadtium, 1, Roentgenium, 1)
                .color(0x2b45df)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SuperheavyHAlloy = material("superheavy_h_alloy", "超重元素-重合金")
                .ingot()
                .fluid()
                .blastTemp(10600, HIGHER)
                .components(Copernicium, 1, Nihonium, 1, Flerovium, 1, Moscovium, 1, Livermorium, 1,
                        Tennessine, 1, Oganesson, 1)
                .color(0xe84b36)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ZirconiumCarbide = material("zirconium_carbide", "碳化锆")
                .ingot()
                .fluid()
                .blastTemp(6800, HIGH, 1920, 1200)
                .components(Zirconium, 1, Carbon, 1)
                .color(0xd2bfaa)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, DISABLE_DECOMPOSITION, GTOMaterialFlags.GENERATE_TARGET_BASE)
                .buildAndRegister();

        MarM200Steel = material("mar_m_200_steel", "MAR-M200特种钢")
                .ingot()
                .fluid()
                .blastTemp(4600, HIGH, GTValues.VA[GTValues.IV], 300)
                .components(Niobium, 2, Chromium, 9, Aluminium, 5, Titanium, 2, Cobalt, 10,
                        Tungsten, 13, Nickel, 18)
                .color(0x515151)
                .iconSet(METALLIC)
                .flags(GENERATE_GEAR, DISABLE_DECOMPOSITION, GENERATE_FRAME)
                .buildAndRegister();

        Tantalloy61 = material("tantalloy_61", "钽钨合金-61")
                .ingot()
                .fluid()
                .blastTemp(6900, HIGHER, GTValues.VA[GTValues.IV], 500)
                .components(Tantalum, 13, Tungsten, 12, Titanium, 6, Yttrium, 4)
                .color(0x363636)
                .iconSet(METALLIC)
                .flags(GENERATE_BOLT_SCREW, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ReactorSteel = material("reactor_steel", "反应堆专用钒钢")
                .ingot()
                .fluid()
                .blastTemp(3800, HIGH, GTValues.VA[GTValues.HV], 700)
                .components(Iron, 15, Niobium, 1, Vanadium, 4, Carbon, 2)
                .color(0xb4b3b0)
                .iconSet(SHINY)
                .flags(GENERATE_DENSE, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        FallKing = material("fall_king", "耐摔合金")
                .ingot()
                .fluid()
                .blastTemp(5400, HIGH)
                .components(Helium, 1, Lithium, 1, Cobalt, 1, Platinum, 1, Erbium, 1)
                .color(0xffcf6b)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        WoodsGlass = material("woods_glass", "伍兹玻璃")
                .ingot()
                .fluid()
                .blastTemp(3600)
                .components(SodaAsh, 6, SiliconDioxide, 3, Garnierite, 3, BariumSulfide, 1)
                .color(0x730099)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AttunedTengam = material("attuned_tengam", "谐镃")
                .ingot()
                .element(GTOElements.TENGAM)
                .color(0x819a4e)
                .iconSet(MAGNETIC)
                .flags(IS_MAGNETIC, GENERATE_LONG_ROD)
                .buildAndRegister();

        Titanium50 = material("titanium_50", "钛-50")
                .ingot()
                .fluid()
                .element(GTOElements.TITANIUM50)
                .blastTemp(1942)
                .color(0xd58eed)
                .iconSet(METALLIC)
                .flags(GTOMaterialFlags.GENERATE_PARTICLE_SOURCE)
                .buildAndRegister();

        Laurenium = material("laurenium", "劳伦姆合金")
                .ingot()
                .fluid()
                .blastTemp(7100, HIGH, GTValues.VA[GTValues.ZPM], 1250)
                .components(EglinSteel, 8, Indium, 2, Chromium, 4, Dysprosium, 1, Rhenium, 1)
                .color(0xffb7ef)
                .iconSet(METALLIC)
                .flags(GENERATE_GEAR, GENERATE_FRAME, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        BabbittAlloy = material("babbitt_alloy", "巴氏合金")
                .ingot()
                .fluid()
                .color(0xa19ca4)
                .iconSet(METALLIC)
                .components(Tin, 5, Lead, 36, Antimony, 8, Astatine, 1)
                .blastTemp(737, MID, GTValues.VA[GTValues.MV], 230)
                .flags(GENERATE_GEAR, GENERATE_FRAME, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        DepletedUraniumAlloy = material("depleted_uranium_alloy", "贫铀合金")
                .ingot()
                .fluid()
                .color(0x444b42)
                .iconSet(METALLIC)
                .blastTemp(3450, HIGH, GTValues.VA[GTValues.EV], 462)
                .flags(GENERATE_PLATE, GENERATE_FRAME, DISABLE_DECOMPOSITION)
                .components(Uranium238, 9, Titanium, 1)
                .buildAndRegister();

        Trinaquadalloy = material("trinaquadalloy", "碳化凯金硅岩合金")
                .ingot()
                .fluid()
                .color(0x281832)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, GENERATE_FRAME, DISABLE_DECOMPOSITION)
                .components(Trinium, 6, Naquadah, 2, Carbon, 1)
                .blastTemp(8747, HIGHER, GTValues.VA[GTValues.ZPM], 1200)
                .buildAndRegister();

        NitinolA = material("nitinol_a", "Nitinol 60", "镍钛诺60")
                .ingot()
                .fluid()
                .color(0xccb0ec)
                .iconSet(METALLIC)
                .flags(GENERATE_GEAR)
                .components(Nickel, 2, Titanium, 3)
                .blastTemp(1941, MID, GTValues.VA[GTValues.HV], 450)
                .buildAndRegister();

        ElectronicGradeSilicon = material("electronic_grade_silicon", "电子级硅")
                .dust()
                .ingot()
                .fluid()
                .blastTemp(2273, LOW)
                .color(0x414151)
                .components(Silicon, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION, GENERATE_CRYSTAL_SEED)
                .buildAndRegister().setFormula("Si(6N)");

        UltraHighPuritySilicon = material("ultra_high_purity_silicon", "超高纯硅")
                .dust()
                .ingot()
                .fluid()
                .blastTemp(10000, HIGHEST, GTValues.VA[GTValues.UV], 1200)
                .color(0x414151)
                .components(Silicon, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION, GENERATE_CRYSTAL_SEED)
                .buildAndRegister().setFormula("Si(N12)");

        Pedot = material("pedot", "PEDOT", "PEDOT")
                .polymer()
                .fluid()
                .components(Carbon, 6, Hydrogen, 6, Oxygen, 2, Sulfur, 1)
                .color(0x5cef20)
                .iconSet(DULL)
                .flags(GENERATE_FOIL, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Polyimide = material("polyimide", "聚酰亚胺")
                .fluid()
                .polymer()
                .components(Carbon, 22, Hydrogen, 12, Nitrogen, 2, Oxygen, 6)
                .flags(GENERATE_PLATE, DISABLE_DECOMPOSITION)
                .color(0xff6730)
                .iconSet(DULL)
                .buildAndRegister();

        Polyetheretherketone = material("polyetheretherketone", "聚醚醚酮")
                .polymer()
                .fluid()
                .components(Carbon, 20, Hydrogen, 12, Oxygen, 3)
                .color(0x33334d)
                .iconSet(DULL)
                .flags(GENERATE_FINE_WIRE)
                .buildAndRegister();

        CarbonNanotubes = material("carbon_nanotubes", "碳纳米管")
                .polymer()
                .fluid()
                .color(0x000000)
                .iconSet(DULL)
                .flags(GENERATE_FOIL, GENERATE_FINE_WIRE)
                .buildAndRegister();

        TitaniumDioxideNanotubes = material("titanium_dioxide_nanotubes", "二氧化钛纳米管")
                .polymer()
                .fluid()
                .color(0x491506)
                .secondaryColor(0x130101)
                .components(Titanium, 1, Oxygen, 2)
                .iconSet(DULL)
                .flags(GENERATE_FOIL, GENERATE_FINE_WIRE, GTOMaterialFlags.GENERATE_CATALYST)
                .buildAndRegister();

        FullerenePolymerMatrixPulp = material("fullerene_polymer_matrix_pulp", "富勒烯聚合物基体")
                .polymer()
                .fluid()
                .fluidPipeProperties(5000, 5000, true, true, true)
                .color(0x23221e)
                .iconSet(DULL)
                .flags(GENERATE_FOIL)
                .buildAndRegister();

        Zylon = material("zylon", "柴隆纤维")
                .polymer()
                .fluid()
                .components(Carbon, 14, Hydrogen, 6, Nitrogen, 2, Oxygen, 2)
                .color(0xd2b800)
                .iconSet(DULL)
                .flags(GENERATE_FOIL)
                .buildAndRegister();

        Kevlar = material("kevlar", "凯芙拉")
                .polymer()
                .fluid()
                .color(0x9f9f53)
                .iconSet(DULL)
                .flags(GENERATE_FOIL)
                .buildAndRegister();

        Radox = material("radox", "拉多X聚合物")
                .polymer()
                .fluid()
                .components(Carbon, 14, Osmium, 11, Oxygen, 7, Silver, 3, Concrete, 1, Water, 1)
                .color(0x680064)
                .iconSet(DULL)
                .flags(GENERATE_FOIL, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AdamantineCompounds = material("adamantine_compounds", "精金化合物")
                .ore()
                .color(0xdaa520)
                .components(Adamantine, 1, Concrete, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TriniumCompound = material("trinium_compound", "凯金化合物")
                .dust()
                .ore()
                .components(Trinium, 3, Actinium, 3, Selenium, 4, Astatine, 4)
                .color(0x675989).iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Force = material("force", "力量")
                .dust()
                .fluid()
                .ore()
                .addOreByproducts(Lanthanum)
                .color(0xdede00)
                .iconSet(BRIGHT)
                .buildAndRegister();

        Tartarite = material("tartarite", "溶火之石")
                .dust()
                .fluid()
                .ore()
                .addOreByproducts(Americium)
                .color(0xd36232)
                .iconSet(BRIGHT)
                .buildAndRegister();

        RareEarthMetal = material("rare_earth_metal", "稀土金属")
                .dust()
                .ore()
                .addOreByproducts(RareEarth, Monazite)
                .color(0x737373)
                .iconSet(METALLIC)
                .buildAndRegister().setFormula("?", false);

        LanthanoidsMix1 = material("lanthanoids_mix_1", "轻镧系元素混合物")
                .dust()
                .color(0xef1133)
                .components(Lanthanum, 1, Cerium, 1, Praseodymium, 1, Neodymium, 1, Promethium, 1,
                        Samarium, 1, Europium, 1, Gadolinium, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        LanthanoidsMix2 = material("lanthanoids_mix_2", "重镧系元素混合物")
                .dust()
                .color(0xef1133)
                .components(Terbium, 1, Dysprosium, 1, Holmium, 1, Erbium, 1, Thulium, 1,
                        Ytterbium, 1, Lutetium, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        RareearthMix = material("rareearth_mix", "稀土元素合金")
                .fluid()
                .ingot()
                .color(0xa52a2a)
                .blastTemp(12400, HIGHER, GTValues.VA[GTValues.UHV], 800)
                .components(Scandium, 1, Yttrium, 1, LanthanoidsMix1, 1, LanthanoidsMix2, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ActinoidsMix1 = material("actinoids_mix_1", "轻锕系元素混合物")
                .dust()
                .color(0x80eb33)
                .components(Actinium, 1, Thorium, 1, Protactinium, 1, Uranium238, 1, Neptunium, 1,
                        Plutonium239, 1, Americium, 1, Curium, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ActinoidsMix2 = material("actinoids_mix_2", "重锕系元素混合物")
                .dust()
                .color(0x80eb33)
                .components(Berkelium, 1, Californium, 1, Einsteinium, 1, Fermium, 1, Mendelevium, 1,
                        Nobelium, 1, Lawrencium, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ActinoidsMix = material("actinoids_mix", "锕系元素混合物")
                .dust()
                .color(0x72d22e)
                .components(ActinoidsMix1, 1, ActinoidsMix2, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AlkalineMix = material("alkaline_mix", "碱金属元素混合物")
                .dust()
                .color(0xdd186b)
                .components(Lithium, 1, Sodium, 1, Potassium, 1, Rubidium, 1, Caesium, 1,
                        Francium, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AlkalineEarthMix = material("alkaline_earth_mix", "碱土金属元素混合物")
                .dust()
                .color(0xc1155e)
                .components(Beryllium, 1, Magnesium, 1, Calcium, 1, Strontium, 1, Barium, 1,
                        Radium, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TransitionMix1 = material("transition_mix_1", "前过渡金属元素混合物")
                .dust()
                .color(0xa19e9d)
                .components(Titanium, 1, Vanadium, 1, Chromium, 1, Manganese, 1, Iron, 1, Cobalt, 1,
                        Nickel, 1, Copper, 1, Zinc, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TransitionMix2 = material("transition_mix_2", "中过渡金属元素混合物")
                .dust()
                .color(0x908d8c)
                .components(Zirconium, 1, Niobium, 1, Molybdenum, 1, Technetium, 1, Ruthenium, 1,
                        Rhodium, 1, Palladium, 1, Silver, 1, Cadmium, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TransitionMix3 = material("transition_mix_3", "后过渡金属元素混合物")
                .dust()
                .color(0x838180)
                .components(Hafnium, 1, Tantalum, 1, Tungsten, 1, Rhenium, 1, Osmium, 1, Iridium, 1,
                        Platinum, 1, Gold, 1, Mercury, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TransitionMix = material("transition_mix", "过渡元素合金")
                .fluid()
                .ingot()
                .color(0x989594)
                .blastTemp(13600, HIGHER, GTValues.VA[GTValues.UHV], 900)
                .components(TransitionMix1, 1, TransitionMix2, 1, TransitionMix3, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        PoorMix = material("poor_mix", "贫金属元素混合物")
                .dust()
                .color(0x916d12)
                .components(Aluminium, 1, Gallium, 1, Indium, 1, Tin, 1, Thallium, 1, Lead, 1,
                        Bismuth, 1, Polonium, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        MetalloidMix = material("metalloid_mix", "类金属元素混合物")
                .dust()
                .color(0x916d12)
                .components(Boron, 1, Silicon, 1, Germanium, 1, Arsenic, 1, Antimony, 1,
                        Tellurium, 1, Astatine, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        NotFoundMix = material("not_found_mix", "非金属元素混合物")
                .fluid()
                .color(0x1e0ebe)
                .components(Hydrogen, 1, Carbon, 1, Nitrogen, 1, Oxygen, 1, Fluorine, 1,
                        Phosphorus, 1, Sulfur, 1, Chlorine, 1, Selenium, 1, Bromine, 1,
                        Iodine, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        NobleGasMix = material("noble_gas_mix", "稀有气体元素混合物")
                .fluid()
                .color(0xed8dea)
                .components(Helium, 1, Neon, 1, Argon, 1, Krypton, 1, Xenon, 1, Radon, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SuperheavyMix = material("superheavy_mix", "超重元素混合物")
                .dust()
                .liquid(new FluidBuilder().temperature(12000))
                .color(0x3E2C2B)
                .iconSet(METALLIC)
                .components(SuperheavyLAlloy, 1, SuperheavyHAlloy, 1)
                .flags(NO_UNIFICATION, FORCE_GENERATE_BLOCK)
                .buildAndRegister();

        Periodicium = material("periodicium", "錭錤錶")
                .ingot()
                .fluid()
                .blastTemp(15200, HIGHEST, GTValues.VA[GTValues.UEV], 1200)
                .components(NotFoundMix, 1, NobleGasMix, 1, MetalloidMix, 1, PoorMix, 1, TransitionMix, 1,
                        AlkalineEarthMix, 1, RareearthMix, 1, AlkalineMix, 1, ActinoidsMix, 1, SuperheavyMix, 1)
                .color(0x3d4bf6)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        EnergeticNetherite = material("energetic_netherite", "充能下界合金")
                .ingot()
                .glow(true)
                .color(0x4b4042)
                .blastTemp(10200, HIGHEST, GTValues.VA[GTValues.ZPM], 600)
                .flags(IS_MAGNETIC, GENERATE_LONG_ROD)
                .iconSet(MAGNETIC)
                .buildAndRegister();

        DuraniumAlloy = material("duranium_alloy", "铿铀合金")
                .ingot()
                .fluid()
                .color(0x378D94)
                .blastTemp(7600, HIGH, GTValues.VA[GTValues.LuV], 540)
                .components(Duranium, 7, Magnesium, 1)
                .flags(GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .iconSet(METALLIC)
                .buildAndRegister();

        SpacetimeContinuum = material("spacetime_continuum", "时空连续体")
                .liquid(new FluidBuilder().customStill())
                .iconSet(DULL)
                .buildAndRegister();

        DimensionAllyshiftedSuperFluid = material("dimension_allyshifted_super_fluid", "维度偏移超流体")
                .liquid(new FluidBuilder().customStill())
                .iconSet(DULL)
                .buildAndRegister();

        StableBaryonicMatter = material("stable_baryonic_matter", "稳定重子物质")
                .liquid(new FluidBuilder().customStill())
                .iconSet(DULL)
                .buildAndRegister();

        TranscendingMatter = material("transcending_matter", "Transcending Matter", "超然物质")
                .liquid(new FluidBuilder().customStill())
                .iconSet(DULL)
                .buildAndRegister()
                .setFormula(ChatFormatting.BLUE + "༒", false);
    }
}
