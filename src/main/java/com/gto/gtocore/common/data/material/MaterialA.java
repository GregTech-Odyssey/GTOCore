package com.gto.gtocore.common.data.material;

import com.gto.gtocore.api.data.chemical.material.info.GTOMaterialFlags;
import com.gto.gtocore.api.item.tool.GTOToolType;
import com.gto.gtocore.common.data.GTOElements;
import com.gto.gtocore.config.GTOConfig;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.ToolProperty;
import com.gregtechceu.gtceu.api.fluids.FluidBuilder;
import com.gregtechceu.gtceu.api.fluids.attribute.FluidAttributes;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Rarity;

import committee.nova.mods.avaritia.init.registry.ModRarities;

import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty.GasTier.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gto.gtocore.api.data.chemical.material.info.GTOMaterialIconSet.*;
import static com.gto.gtocore.common.data.GTOMaterials.*;
import static com.gto.gtocore.utils.register.MaterialsRegisterUtils.material;

public interface MaterialA {

    static void init() {
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
                .flags(DISABLE_DECOMPOSITION)
                .blastTemp(1650, LOW, GTValues.VA[GTValues.MV], 700)
                .components(Redstone, 1, Gold, 1, Glowstone, 1)
                .cableProperties(128, 1, 0, true)
                .buildAndRegister();

        VibrantAlloy = material("vibrant_alloy", "振动合金")
                .ingot()
                .fluid()
                .color(0xa4ff70)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .blastTemp(2450, LOW, GTValues.VA[GTValues.MV], 900)
                .components(EnergeticAlloy, 1, EnderPearl, 1)
                .cableProperties(512, 1, 0, true)
                .buildAndRegister();

        RedstoneAlloy = material("redstone_alloy", "红石合金")
                .ingot()
                .fluid()
                .color(0xf66565)
                .iconSet(SHINY)
                .flags(DISABLE_DECOMPOSITION)
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
                .flags(DISABLE_DECOMPOSITION)
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

        PerditioCrystal = material("perditio_crystal", "混沌魔晶")
                .dust()
                .color(0x656565)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        EarthCrystal = material("earth_crystal", "地之魔晶")
                .dust()
                .ore()
                .addOreByproducts(PerditioCrystal)
                .color(0x00f100)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?", false);

        IgnisCrystal = material("ignis_crystal", "火之魔晶")
                .dust()
                .ore()
                .addOreByproducts(PerditioCrystal)
                .color(0xd90000)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?", false);

        InfusedGold = material("infused_gold", "注魔金")
                .dust()
                .ore()
                .color(0xc99614)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("Au?", false);

        Thaumium = material("thaumium", "神秘")
                .ingot()
                .components(InfusedGold, 1)
                .color(0x8153a9)
                .iconSet(DULL)
                .blastTemp(1000, LOW, GTValues.VA[GTValues.LV], 100)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FRAME, GENERATE_PLATE, GENERATE_ROD)
                .buildAndRegister();

        AstralSilver = material("astral_silver", "星辰银")
                .dust()
                .fluid()
                .components(Silver, 2, Thaumium, 1)
                .color(0xd9d9f1)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
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

        Quantanium = material("quantanium", "量子")
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
                .flags(GENERATE_SPRING, GENERATE_SPRING_SMALL)
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
                .fluidPipeProperties(2147483647, GTOConfig.INSTANCE.spacetimePip, true, true, true, true)
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
                .flags(GENERATE_FRAME, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .cableProperties(Integer.MAX_VALUE, 8192, 0, true)
                .buildAndRegister();

        Adamantine = material("adamantine", "精金")
                .ingot()
                .fluid()
                .blastTemp(14400, HIGHER)
                .element(GTOElements.ADAMANTINE)
                .color(0xe6e600)
                .iconSet(METALLIC)
                .flags(GENERATE_FINE_WIRE)
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
                .fluidPipeProperties(100000, 100000, true, true, true, true)
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
                .fluidPipeProperties(1000000, 1000000, true, true, true, true)
                .element(GTOElements.HEAVY_QUARK_DEGENERATE_MATTER)
                .blastTemp(178000, HIGHER)
                .color(0x52a733)
                .iconSet(BRIGHT)
                .flags(GENERATE_PLATE, GENERATE_FINE_WIRE)
                .buildAndRegister();

        MetastableHassium = material("metastable_hassium", "亚稳态𬭶")
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
                .flags(GENERATE_ROD, GENERATE_FINE_WIRE, DISABLE_DECOMPOSITION)
                .cableProperties(GTValues.V[GTValues.UXV], 4, 128)
                .buildAndRegister();

        AbyssalAlloy = material("abyssalalloy", "渊狱合金")
                .ingot()
                .fluid()
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
                .flags(GENERATE_PLATE, DISABLE_DECOMPOSITION)
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

        Quantum = material("quantum", "量子合金")
                .ingot()
                .fluid()
                .blastTemp(11400, HIGHER, 1920)
                .components(Stellite, 15, Quantanium, 3, Jasper, 2, Gallium, 5, Americium, 5,
                        Palladium, 5, Germanium, 5, SiliconCarbide, 5)
                .color(0x0d0d0d)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FOIL)
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
                .components(TitanSteel, 3, Ytterbium, 1, PerditioCrystal, 1, EarthCrystal, 1,
                        IgnisCrystal, 1)
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
                .flags(GENERATE_PLATE, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        MarM200Steel = material("mar_m_200_steel", "MAR-M200特种钢")
                .ingot()
                .fluid()
                .blastTemp(4600, HIGH, GTValues.VA[GTValues.IV], 300)
                .components(Niobium, 2, Chromium, 9, Aluminium, 5, Titanium, 2, Cobalt, 10,
                        Tungsten, 13, Nickel, 18)
                .color(0x515151)
                .iconSet(METALLIC)
                .flags(GENERATE_GEAR, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Tantalloy61 = material("tantalloy_61", "钽钨合金-61")
                .ingot()
                .fluid()
                .blastTemp(6900, HIGHER, GTValues.VA[GTValues.LuV], 500)
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

        Lanthanoids1 = material("lanthanoids_1", "轻镧系元素混合物")
                .dust()
                .color(0xef1133)
                .components(Lanthanum, 1, Cerium, 1, Praseodymium, 1, Neodymium, 1, Promethium, 1,
                        Samarium, 1, Europium, 1, Gadolinium, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Lanthanoids2 = material("lanthanoids_2", "重镧系元素混合物")
                .dust()
                .color(0xef1133)
                .components(Terbium, 1, Dysprosium, 1, Holmium, 1, Erbium, 1, Thulium, 1,
                        Ytterbium, 1, Lutetium, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        RAREEARTH = material("rareearth", "稀土元素合金")
                .fluid()
                .ingot()
                .color(0xa52a2a)
                .blastTemp(12400, HIGHER, GTValues.VA[GTValues.UHV], 800)
                .components(Scandium, 1, Yttrium, 1, Lanthanoids1, 1, Lanthanoids2, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Actinoids1 = material("actinoids_1", "轻锕系元素混合物")
                .dust()
                .color(0x80eb33)
                .components(Actinium, 1, Thorium, 1, Protactinium, 1, Uranium238, 1, Neptunium, 1,
                        Plutonium239, 1, Americium, 1, Curium, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Actinoids2 = material("actinoids_2", "重锕系元素混合物")
                .dust()
                .color(0x80eb33)
                .components(Berkelium, 1, Californium, 1, Einsteinium, 1, Fermium, 1, Mendelevium, 1,
                        Nobelium, 1, Lawrencium, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Actinoids = material("actinoids", "锕系元素混合物")
                .dust()
                .color(0x72d22e)
                .components(Actinoids1, 1, Actinoids2, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Alkaline = material("alkaline", "碱金属元素混合物")
                .dust()
                .color(0xdd186b)
                .components(Lithium, 1, Sodium, 1, Potassium, 1, Rubidium, 1, Caesium, 1,
                        Francium, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AlkalineEarth = material("alkaline_earth", "碱土金属元素混合物")
                .dust()
                .color(0xc1155e)
                .components(Beryllium, 1, Magnesium, 1, Calcium, 1, Strontium, 1, Barium, 1,
                        Radium, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Transition1 = material("transition_1", "前过渡金属元素混合物")
                .dust()
                .color(0xa19e9d)
                .components(Titanium, 1, Vanadium, 1, Chromium, 1, Manganese, 1, Iron, 1, Cobalt, 1,
                        Nickel, 1, Copper, 1, Zinc, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Transition2 = material("transition_2", "中过渡金属元素混合物")
                .dust()
                .color(0x908d8c)
                .components(Zirconium, 1, Niobium, 1, Molybdenum, 1, Technetium, 1, Ruthenium, 1,
                        Rhodium, 1, Palladium, 1, Silver, 1, Cadmium, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Transition3 = material("transition_3", "后过渡金属元素混合物")
                .dust()
                .color(0x838180)
                .components(Hafnium, 1, Tantalum, 1, Tungsten, 1, Rhenium, 1, Osmium, 1, Iridium, 1,
                        Platinum, 1, Gold, 1, Mercury, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Transition = material("transition", "过渡元素合金")
                .fluid()
                .ingot()
                .color(0x989594)
                .blastTemp(13600, HIGHER, GTValues.VA[GTValues.UHV], 900)
                .components(Transition1, 1, Transition2, 1, Transition3, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Poor = material("poor", "贫金属元素混合物")
                .dust()
                .color(0x916d12)
                .components(Aluminium, 1, Gallium, 1, Indium, 1, Tin, 1, Thallium, 1, Lead, 1,
                        Bismuth, 1, Polonium, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Metalloid = material("metalloid", "类金属元素混合物")
                .dust()
                .color(0x916d12)
                .components(Boron, 1, Silicon, 1, Germanium, 1, Arsenic, 1, Antimony, 1,
                        Tellurium, 1, Astatine, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        NotFound = material("not_found", "非金属元素混合物")
                .fluid()
                .color(0x1e0ebe)
                .components(Hydrogen, 1, Carbon, 1, Nitrogen, 1, Oxygen, 1, Fluorine, 1,
                        Phosphorus, 1, Sulfur, 1, Chlorine, 1, Selenium, 1, Bromine, 1,
                        Iodine, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        NobleGas = material("noble_gas", "稀有气体元素混合物")
                .fluid()
                .color(0xed8dea)
                .components(Helium, 1, Neon, 1, Argon, 1, Krypton, 1, Xenon, 1, Radon, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Periodicium = material("periodicium", "錭錤錶")
                .ingot()
                .fluid()
                .blastTemp(15200, HIGHEST, GTValues.VA[GTValues.UEV], 1200)
                .components(NotFound, 1, NobleGas, 1, Metalloid, 1, Poor, 1, Transition, 1,
                        AlkalineEarth, 1, RAREEARTH, 1, Alkaline, 1, Actinoids, 1)
                .color(0x3d4bf6)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
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
                .buildAndRegister();

        Laurenium = material("laurenium", "劳伦姆合金")
                .ingot()
                .fluid()
                .blastTemp(7100, HIGH, GTValues.VA[GTValues.ZPM], 1250)
                .components(EglinSteel, 8, Indium, 2, Chromium, 4, Dysprosium, 1, Rhenium, 1)
                .color(0xffb7ef)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, GENERATE_FRAME, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        BabbittAlloy = material("babbitt_alloy", "巴氏合金")
                .ingot()
                .fluid()
                .color(0xa19ca4)
                .iconSet(METALLIC)
                .components(Tin, 5, Lead, 36, Antimony, 8, Astatine, 1)
                .blastTemp(737, MID, GTValues.VA[GTValues.MV], 230)
                .flags(GENERATE_PLATE, GENERATE_FRAME, DISABLE_DECOMPOSITION)
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

        ChaosInfinityAlloy = material("chaos_infinity_alloy", "混沌无尽合金")
                .rarity(ModRarities.COSMIC)
                .ingot()
                .radioactiveHazard(100)
                .fluid()
                .color(0x000000)
                .blastTemp(32000, HIGHEST)
                .element(GTOElements.INFINITY)
                .iconSet(CHAOS_INFINITY)
                .flags(GENERATE_FRAME, GENERATE_GEAR, GENERATE_BOLT_SCREW)
                .buildAndRegister()
                .setFormula("§8§kc§r§8∞§r§8§kc", false);

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

        FullerenePolymerMatrixPulp = material("fullerene_polymer_matrix_pulp", "富勒烯聚合物基体")
                .polymer()
                .fluid()
                .fluidPipeProperties(5000, 5000, true, true, true, true)
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

        // Dust
        RawTengam = material("raw_tengam", "生镃")
                .dust()
                .color(0x819a4e)
                .iconSet(DULL)
                .buildAndRegister();

        CleanRawTengam = material("clean_raw_tengam", "洁净生镃")
                .dust()
                .color(0x819a4e)
                .iconSet(SHINY)
                .buildAndRegister();

        PurifiedTengam = material("purified_tengam", "纯镃")
                .dust()
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .element(GTOElements.TENGAM)
                .color(0x819a4e)
                .iconSet(METALLIC)
                .buildAndRegister();

        PreZylon = material("pre_zylon", "预处理柴隆纤维")
                .dust()
                .color(0x562050)
                .components(Carbon, 20, Hydrogen, 22, Nitrogen, 2, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Terephthalaldehyde = material("terephthalaldehyde", "对苯二甲醛")
                .dust()
                .color(0x4a7454)
                .components(Carbon, 8, Hydrogen, 6, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumOxide = material("sodium_oxide", "氧化钠")
                .dust()
                .color(0x036dee)
                .components(Sodium, 2, Oxygen, 1)
                .iconSet(DULL)
                .buildAndRegister();

        CompressedStone = material("compressed_stone", "压缩石头")
                .dust()
                .color(0x696969)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        GermaniumContainingPrecipitate = material("germanium_containing_precipitate", "含锗沉淀物")
                .dust()
                .color(0x666699)
                .components(Germanium, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GermaniumAsh = material("germanium_ash", "锗灰")
                .dust()
                .color(0x706699)
                .components(Germanium, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GermaniumDioxide = material("germanium_dioxide", "二氧化锗")
                .dust()
                .color(0xffffff)
                .components(Germanium, 1, Oxygen, 2)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Durene = material("durene", "杜烯")
                .dust()
                .components(Carbon, 10, Hydrogen, 14)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x708090)
                .iconSet(DULL)
                .buildAndRegister();

        PyromelliticDianhydride = material("pyromellitic_dianhydride", "均苯二甲酸酐")
                .dust()
                .components(Carbon, 10, Hydrogen, 2, Oxygen, 6)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x708090)
                .iconSet(DULL)
                .buildAndRegister();

        CoAcAbCatalyst = material("co_ac_ab", "Co/AC-AB")
                .dust()
                .color(0x544422)
                .iconSet(DULL)
                .flags(GTOMaterialFlags.GENERATE_CATALYST)
                .buildAndRegister();

        ZnFeAlClCatalyst = material("znfealcl", "锌-铁-铝-氯混合")
                .dust()
                .color(0xcf51aa)
                .iconSet(DULL)
                .flags(GTOMaterialFlags.GENERATE_CATALYST)
                .buildAndRegister();

        CalciumCarbide = material("calcium_carbide", "电石")
                .dust()
                .components(Calcium, 1, Carbon, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x47443e)
                .iconSet(DULL)
                .buildAndRegister();

        Difluorobenzophenone = material("difluorobenzophenone", "二氟二苯甲酮")
                .dust()
                .components(Fluorine, 2, Carbon, 13, Hydrogen, 8, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xcf51ae)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumFluoride = material("sodium_fluoride", "氟化钠")
                .dust()
                .components(Sodium, 1, Fluorine, 1)
                .color(0x460011)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumSeaborgate = material("sodium_seaborgate", "𬭳酸钠")
                .dust()
                .components(Sodium, 2, Seaborgium, 1, Oxygen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x55bbd4)
                .iconSet(DULL)
                .buildAndRegister();

        GoldDepletedMolybdenite = material("gold_depleted_molybdenite", "贫金辉钼矿")
                .dust()
                .color(0x757587)
                .components(Molybdenum, 1, Sulfur, 2)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        MolybdenumTrioxide = material("molybdenum_trioxide", "三氧化钼")
                .dust()
                .components(Molybdenum, 1, Oxygen, 3)
                .flags(GTOMaterialFlags.GENERATE_CATALYST)
                .color(0x757587)
                .iconSet(DULL)
                .buildAndRegister();

        Dichlorocyclooctadieneplatinum = material("dichlorocyclooctadieneplatinium", "二氯环辛二烯铂")
                .dust()
                .color(0xd4e982)
                .components(Carbon, 8, Hydrogen, 12, Chlorine, 2, Platinum, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Diiodobiphenyl = material("diiodobiphenyl", "二碘代联苯")
                .dust()
                .color(0x000a42)
                .components(Carbon, 12, Hydrogen, 8, Iodine, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        MolybdenumConcentrate = material("molybdenum_concentrate", "钼精")
                .dust()
                .color(0x47443e)
                .components(Molybdenum, 1, Sulfur, 2, Concrete, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        BoronTrioxide = material("boron_trioxide", "氧化硼")
                .dust()
                .components(Boron, 2, Oxygen, 3)
                .color(0x8fa6b5)
                .iconSet(DULL)
                .buildAndRegister();

        LithiumNiobateNanoparticles = material("lithium_niobate_nanoparticles", "铌酸锂纳米粒子")
                .dust()
                .color(0xc1c12d)
                .components(Lithium, 2, Niobium, 1, Oxygen, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Hexanitrohexaaxaisowurtzitane = material("hexanitrohexaaxaisowurtzitane", "六硝基六氮杂异伍兹烷")
                .dust()
                .flags(GTOMaterialFlags.GENERATE_TINY_DUST, GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Carbon, 6, Hydrogen, 6, Nitrogen, 12, Oxygen, 12)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x3d464b)
                .iconSet(DULL)
                .buildAndRegister();

        SilicaGel = material("silica_gel", "硅胶")
                .dust()
                .color(0x57c3e4)
                .iconSet(DULL)
                .buildAndRegister();

        CrudeHexanitrohexaaxaisowurtzitane = material("crude_hexanitrohexaaxaisowurtzitane", "粗制六硝基六氮杂异伍兹烷")
                .dust()
                .color(0x19586d)
                .components(Carbon, 6, Hydrogen, 6, Nitrogen, 12, Oxygen, 12)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Tetraacetyldinitrosohexaazaisowurtzitane = material("tetraacetyldinitrosohexaazaisowurtzitane", "四乙酰二硝基六氮杂异戊二烯")
                .dust()
                .color(0x500449)
                .components(Carbon, 14, Hydrogen, 18, Nitrogen, 8, Oxygen, 6)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        NitroniumTetrafluoroborate = material("nitronium_tetrafluoroborate", "四氟硝铵")
                .dust()
                .color(0x3c3f40)
                .components(Nitrogen, 1, Oxygen, 2, Boron, 1, Fluorine, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        NitrosoniumTetrafluoroborate = material("nitrosonium_tetrafluoroborate", "四氟硼酸亚硝铵")
                .dust()
                .color(0x485054)
                .components(Nitrogen, 1, Oxygen, 1, Boron, 1, Fluorine, 4)
                .iconSet(DULL)
                .buildAndRegister();

        Dibenzyltetraacetylhexaazaisowurtzitane = material("dibenzyltetraacetylhexaazaisowurtzitane", "二苄基四乙酰六氮杂异纤锌烷")
                .dust()
                .color(0x64704d)
                .components(Carbon, 28, Hydrogen, 32, Nitrogen, 6, Oxygen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SuccinamidylAcetate = material("succinimidyl_acetate", "琥珀酰亚胺醋酸酯")
                .dust()
                .color(0x64704d)
                .components(Carbon, 6, Hydrogen, 7, Nitrogen, 1, Oxygen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Hexabenzylhexaazaisowurtzitane = material("hexabenzylhexaazaisowurtzitane", "六苄基六氮杂异伍兹烷")
                .dust()
                .color(0x64704d)
                .components(Carbon, 48, Hydrogen, 48, Nitrogen, 6)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        NHydroxysuccinimide = material("n_hydroxysuccinimide", "羟基丁二酰亚胺")
                .dust()
                .color(0x7b717f)
                .components(Carbon, 4, Hydrogen, 5, Nitrogen, 1, Oxygen, 3)
                .iconSet(DULL)
                .buildAndRegister();

        SuccinicAnhydride = material("succinic_anhydride", "丁二酸酐")
                .dust()
                .color(0x401116)
                .components(Carbon, 4, Hydrogen, 4, Oxygen, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SuccinicAcid = material("succinic_acid", "琥珀酸")
                .dust()
                .color(0x104e5c)
                .components(Carbon, 4, Hydrogen, 6, Oxygen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Acetonitrile = material("acetonitrile", "乙腈")
                .dust()
                .color(0x5a6161)
                .components(Carbon, 2, Hydrogen, 3, Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Hexamethylenetetramine = material("hexamethylenetetramine", "环六亚甲基四胺")
                .dust()
                .color(0x5a6261)
                .components(Carbon, 6, Hydrogen, 12, Nitrogen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        RareEarthOxide = material("rare_earth_oxide", "稀土氧化物")
                .dust()
                .color(0x808000)
                .components(Concrete, 1, Oxygen, 1)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        RareEarthMetal = material("rare_earth_metal", "稀土金属")
                .dust()
                .ore()
                .addOreByproducts(RareEarth, Monazite)
                .color(0x737373)
                .iconSet(METALLIC)
                .buildAndRegister().setFormula("?", false);

        NaquadahContainRareEarth = material("naquadah_contain_rare_earth", "含硅岩的稀土")
                .dust()
                .color(0xffd700)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        NaquadahContainRareEarthFluoride = material("naquadah_contain_rare_earth_fluoride", "含硅岩的稀土氟化物")
                .dust()
                .color(0xb3b300)
                .components(Concrete, 1, Fluorine, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        MetalResidue = material("metal_residue", "金属残渣")
                .dust()
                .color(0x652118)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        PalladiumFullereneMatrix = material("palladium_fullerene_matrix", "钯-富勒烯基质")
                .dust()
                .color(0x96b4b4)
                .components(Palladium, 1, Carbon, 73, Hydrogen, 15, Nitrogen, 1, Iron, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(BRIGHT)
                .buildAndRegister();

        Fullerene = material("fullerene", "富勒烯")
                .dust()
                .color(0x86c2b8)
                .components(Carbon, 60)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        UnfoldedFullerene = material("unfolded_fullerene", "未折叠富勒烯")
                .dust()
                .color(0x587f83)
                .components(Carbon, 60, Hydrogen, 30)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Methylbenzophenanthrene = material("methylbenzophenanthrene", "甲基苯并菲")
                .dust()
                .color(0x79260c)
                .components(Carbon, 19, Hydrogen, 14)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Sarcosine = material("sarcosine", "肌氨酸")
                .dust()
                .color(0x809324)
                .components(Carbon, 3, Hydrogen, 7, Nitrogen, 1, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        DiphenylmethaneDiisocyanate = material("diphenylmethane_diisocyanate", "4,4'-二苯基甲烷二异氰酸酯")
                .dust()
                .color(0x8e801c)
                .components(Carbon, 15, Hydrogen, 10, Nitrogen, 2, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Pentaerythritol = material("pentaerythritol", "季戊四醇")
                .dust()
                .color(0xacacac)
                .components(Carbon, 5, Hydrogen, 12, Oxygen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        KelpSlurry = material("kelp_slurry", "海带浆液")
                .fluid()
                .color(0x336600)
                .iconSet(DULL)
                .buildAndRegister();

        Quasifissioning = material("quasifissioning", "拟裂变")
                .plasma()
                .color(0xcdbe35)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        ExcitedDtec = material("exciteddtec", "激发的异星超维度催化剂")
                .fluid()
                .color(0xb36a6b)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        ExcitedDtsc = material("exciteddtsc", "激发的恒星超维度催化剂")
                .fluid()
                .color(0xb36a6b)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        DimensionallyTranscendentResplendentCatalyst = material("dimensionallytranscendentresplendentcatalyst", "光辉超维度催化剂")
                .fluid()
                .color(0x081010)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        DimensionallyTranscendentProsaicCatalyst = material("dimensionallytranscendentprosaiccatalyst", "平凡超维度催化剂")
                .fluid()
                .color(0x081010)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        DimensionallyTranscendentExoticCatalyst = material("dimensionallytranscendentexoticcatalyst", "异星超维度催化剂")
                .fluid()
                .color(0x081010)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        DimensionallyTranscendentStellarCatalyst = material("dimensionallytranscendentstellarcatalyst", "恒星超维度催化剂")
                .fluid()
                .color(0x081010)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        DimensionallyTranscendentCrudeCatalyst = material("dimensionallytranscendentcrudecatalyst", "粗制超维度催化剂")
                .fluid()
                .color(0x081010)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        Ytterbium178 = material("ytterbium_178", "镱-178")
                .fluid()
                .element(GTOElements.YITTERBIUM178)
                .color(0xffffff)
                .iconSet(DULL)
                .buildAndRegister();

        Flyb = material("flyb", "𫓧-镱")
                .plasma()
                .components(Flerovium, 1, Ytterbium178, 1)
                .color(0x890a95)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        EnrichedPotassiumIodideSlurry = material("enriched_potassium_iodide_slurry", "富集碘化钾浆液")
                .fluid()
                .color(0x00ffcc)
                .components(Potassium, 1, Iodine, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        IodineContainingSlurry = material("iodine_containing_slurry", "含碘溶液")
                .fluid()
                .color(0x666633)
                .components(Iodine, 1, RockSalt, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AshLeachingSolution = material("ash_leaching_solution", "灰烬浸出液")
                .fluid()
                .color(0x666699)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        Tannic = material("tannic", "丹宁")
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .components(Carbon, 76, Hydrogen, 52, Oxygen, 46)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xffff66)
                .iconSet(DULL)
                .buildAndRegister();

        GermaniumTetrachlorideSolution = material("germanium_tetrachloride_solution", "四氯化锗")
                .fluid()
                .gas()
                .color(0x66ffcc)
                .components(Germanium, 1, Chlorine, 4)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Polyimide = material("polyimide", "聚酰亚胺")
                .fluid()
                .components(Carbon, 22, Hydrogen, 12, Nitrogen, 2, Oxygen, 6)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xff6730)
                .iconSet(DULL)
                .buildAndRegister();

        Aniline = material("aniline", "苯胺")
                .fluid()
                .components(Carbon, 6, Hydrogen, 7, Nitrogen, 1)
                .color(0x3d7517)
                .iconSet(DULL)
                .buildAndRegister();

        Oxydianiline = material("oxydianiline", "二氨基二苯醚")
                .fluid()
                .components(Carbon, 12, Hydrogen, 12, Nitrogen, 2, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xffd700)
                .iconSet(DULL)
                .buildAndRegister();

        BoricAcid = material("boric_acide", "硼酸")
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .components(Hydrogen, 3, Boron, 1, Oxygen, 3)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x8fbc8f)
                .iconSet(DULL)
                .buildAndRegister();

        FluoroboricAcid = material("fluoroboric_acide", "氟硼酸")
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .components(Hydrogen, 1, Boron, 1, Fluorine, 4)
                .color(0x8fbc8f)
                .iconSet(DULL)
                .buildAndRegister();

        BenzenediazoniumTetrafluoroborate = material("benzenediazonium_tetrafluoroborate", "四氟硼酸重氮苯")
                .fluid()
                .components(Carbon, 6, Hydrogen, 5, Boron, 1, Fluorine, 4, Nitrogen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x8fbc8f)
                .iconSet(DULL)
                .buildAndRegister();

        FluoroBenzene = material("fluoro_benzene", "氟苯")
                .fluid()
                .components(Carbon, 6, Hydrogen, 5, Fluorine, 1)
                .color(0x8fbc8f)
                .iconSet(DULL)
                .buildAndRegister();

        Fluorotoluene = material("fluorotoluene", "氟甲苯")
                .fluid()
                .components(Carbon, 7, Hydrogen, 7, Fluorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xdad386)
                .iconSet(DULL)
                .buildAndRegister();

        Hydroquinone = material("hydroquinone", "对苯二酚")
                .fluid()
                .components(Carbon, 6, Hydrogen, 6, Oxygen, 2)
                .color(0x8e2518)
                .iconSet(DULL)
                .buildAndRegister();

        Resorcinol = material("resorcinol", "间苯二酚")
                .fluid()
                .components(Carbon, 6, Hydrogen, 6, Oxygen, 2)
                .color(0x8e2518)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumNitrate = material("sodium_nitrate", "硝酸钠")
                .dust()
                .color(0x4e2a40).iconSet(SAND)
                .components(Sodium, 1, Nitrogen, 1, Oxygen, 3)
                .buildAndRegister();

        SodiumNitrateSolution = material("sodium_nitrate_solution", "硝酸钠溶液")
                .fluid()
                .components(SodiumNitrate, 1, Water, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x2b387e)
                .iconSet(DULL)
                .buildAndRegister();

        Acetylene = material("acetylene", "乙炔")
                .fluid()
                .components(Carbon, 2, Hydrogen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x7f8552)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumCyanide = material("sodium_cyanide", "氰化钠")
                .fluid()
                .components(Sodium, 1, Carbon, 1, Nitrogen, 1)
                .color(0x4f6774)
                .iconSet(DULL)
                .buildAndRegister();

        GoldCyanide = material("gold_cyanide", "氰化金")
                .fluid()
                .components(Gold, 1, Carbon, 1, Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x736f50)
                .iconSet(DULL)
                .buildAndRegister();

        MolybdenumFlue = material("molybdenum_flue", "钼烟气")
                .gas()
                .color(0x4b626f)
                .components(Sulfur, 2, Concrete, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        RheniumSulfuricSolution = material("rhenium_sulfuric_solution", "铼硫酸溶液")
                .fluid()
                .color(0xc0c0ea)
                .components(Rhenium, 1, Sulfur, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AmmoniumPerrhenate = material("ammonium_perrhenate", "高铼酸铵")
                .fluid()
                .color(0x161637)
                .components(Ammonia, 1, Rhenium, 1, Oxygen, 4)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Cycloparaphenylene = material("cycloparaphenylene", "环对苯撑")
                .fluid()
                .color(0x000000)
                .iconSet(DULL)
                .buildAndRegister();

        TrimethylTinChloride = material("trimethyltin_chloride", "三甲基氯化锡")
                .fluid()
                .color(0x72685f)
                .components(Tin, 1, Carbon, 3, Hydrogen, 9, Chlorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SilverTetrafluoroborate = material("silver_tetrafluoroborate", "四氟硼酸银")
                .fluid()
                .color(0x76750f)
                .components(Silver, 1, Boron, 1, Fluorine, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        BoronFluoride = material("boron_fluoride", "氟化硼")
                .fluid()
                .components(Boron, 1, Fluorine, 3)
                .color(0xcecad0)
                .iconSet(DULL)
                .buildAndRegister();

        OneOctene = material("1_octene", "1-辛烯")
                .fluid()
                .components(Carbon, 8, Hydrogen, 16)
                .color(0x666d61)
                .iconSet(DULL)
                .buildAndRegister();

        Pyridine = material("pyridine", "吡啶")
                .fluid()
                .color(0x555642)
                .components(Carbon, 5, Hydrogen, 5, Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Acetaldehyde = material("acetaldehyde", "乙醛")
                .fluid()
                .components(Carbon, 2, Hydrogen, 4, Oxygen, 1)
                .color(0x666d61)
                .iconSet(DULL)
                .buildAndRegister();

        Cyclooctadiene = material("cyclooctadiene", "环辛二烯")
                .fluid()
                .color(0xd4e982)
                .components(Carbon, 8, Hydrogen, 12)
                .iconSet(DULL)
                .buildAndRegister();

        SeaborgiumDopedNanotubes = material("seaborgium_doped_nanotubes", "𬭳掺杂的纳米管")
                .fluid()
                .color(0x242473)
                .iconSet(DULL)
                .buildAndRegister();

        Ethylenediamine = material("ethylenediamine", "乙二胺")
                .fluid()
                .color(0x1b5d74)
                .components(Carbon, 2, Hydrogen, 8, Nitrogen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Ethanolamine = material("ethanolamine", "乙醇胺")
                .fluid()
                .color(0x1b5d74)
                .components(Carbon, 2, Hydrogen, 7, Nitrogen, 1, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        EthyleneOxide = material("ethylene_oxide", "环氧乙烷")
                .fluid()
                .color(0x8eb7d8)
                .components(Carbon, 2, Hydrogen, 4, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Benzaldehyde = material("benzaldehyde", "苯甲醛")
                .fluid()
                .color(0x905a1b)
                .components(Carbon, 7, Hydrogen, 6, Oxygen, 1)
                .iconSet(DULL)
                .buildAndRegister();

        HydroxylamineHydrochloride = material("hydroxylamine_hydrochloride", "盐酸羟胺")
                .fluid()
                .color(0x433217)
                .components(Hydrogen, 4, Nitrogen, 1, Oxygen, 1, Chlorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        MaleicAnhydride = material("maleic_anhydride", "顺丁烯二酸酐")
                .fluid()
                .color(0x321b90)
                .components(Carbon, 4, Hydrogen, 2, Oxygen, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Benzylamine = material("benzylamine", "苄胺")
                .fluid()
                .color(0x5b6363)
                .components(Carbon, 7, Hydrogen, 9, Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Glyoxal = material("glyoxal", "乙二醛")
                .fluid()
                .color(0xf0ed4d)
                .components(Carbon, 2, Hydrogen, 2, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        BenzylChloride = material("benzyl_chloride", "氯化苄")
                .fluid()
                .color(0x9ff6fb)
                .components(Carbon, 7, Hydrogen, 7, Chlorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        TranscendingMatter = material("transcending_matter", "Transcending Matter", "超然物质")
                .fluid()
                .color(0x9400d3)
                .iconSet(DULL)
                .buildAndRegister()
                .setFormula(ChatFormatting.BLUE + "༒", false);

        RareEarthHydroxides = material("rare_earth_hydroxides", "稀土氢氧化物")
                .fluid()
                .color(0x808000)
                .components(Concrete, 1, Oxygen, 1, Hydrogen, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        QuantumDots = material("quantum_dots", "量子点")
                .fluid()
                .color(0xda0000)
                .iconSet(DULL)
                .buildAndRegister();

        StearicAcid = material("stearic_acid", "硬脂酸")
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0x239791)
                .components(Carbon, 18, Hydrogen, 36, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Soap = material("soap", "肥皂")
                .fluid()
                .color(0xff9d1b)
                .iconSet(DULL)
                .buildAndRegister();

        Tricotylphosphine = material("tricotylphosphine", "三辛基膦")
                .fluid()
                .color(0xe7d510)
                .components(Carbon, 24, Hydrogen, 51, Phosphorus, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        IridiumTrichlorideSolution = material("iridium_trichloride_solution", "三氯化铱溶液")
                .fluid()
                .color(0x776715)
                .components(Iridium, 1, Chlorine, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        LiquidHydrogen = material("liquid_hydrogen", "液态氢")
                .fluid()
                .color(0x4fc4a2)
                .components(Hydrogen, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        BedrockSmoke = material("bedrock_smoke", "基岩烟")
                .gas()
                .color(0xa9a9a9)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        BedrockSootSolution = material("bedrock_soot_solution", "基岩烟溶液")
                .fluid()
                .color(0x191970)
                .iconSet(DULL)
                .buildAndRegister().setFormula("Nq?", false);

        CleanBedrockSolution = material("clean_bedrock_solution", "洁净基岩烟溶液")
                .fluid()
                .color(0x778899)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister().setFormula("?", false);

        BedrockGas = material("bedrock_gas", "基岩气")
                .gas()
                .color(0xc0c0c0)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        VibraniumUnstable = material("vibranium_unstable", "不稳定振金")
                .fluid()
                .color(0xfa8072)
                .components(Vibranium, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TaraniumEnrichedLiquidHelium3 = material("taranium_enriched_liquid_helium_3", "浓缩塔兰金属的液氦-3")
                .fluid()
                .color(0x57f26d)
                .components(Taranium, 1, Helium3, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TaraniumRichLiquidHelium4 = material("taranium_rich_liquid_helium_4", "富塔兰金属的氦-4")
                .fluid()
                .plasma()
                .color(0x57f26d)
                .components(Taranium, 1, Helium, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        FreeElectronGas = material("free_electron_gas", "高密度自由电子气")
                .gas()
                .color(0x033c3c)
                .element(GTOElements.ELECTRON)
                .iconSet(DULL)
                .buildAndRegister();

        FreeAlphaGas = material("free_alpha_gas", "高密度自由α粒子气")
                .gas()
                .color(0xb5ab06)
                .element(GTOElements.ALPHA)
                .iconSet(DULL)
                .buildAndRegister();

        FreeProtonGas = material("free_proton_gas", "高密度自由质子气")
                .gas()
                .color(0x2089aa)
                .element(GTOElements.PROTON)
                .iconSet(DULL)
                .buildAndRegister();

        QuarkGluon = material("quark_gluon", "夸克-胶子")
                .plasma()
                .color(0x7a00da)
                .element(GTOElements.QUARK_GLUON)
                .iconSet(DULL)
                .buildAndRegister();

        HeavyQuarks = material("heavy_quarks", "重夸克")
                .gas()
                .color(0x007000)
                .element(GTOElements.HEAVY_QUARKS)
                .iconSet(DULL)
                .buildAndRegister();

        LightQuarks = material("light_quarks", "轻夸克")
                .gas()
                .color(0x0000ce)
                .element(GTOElements.LIGHT_QUARKS)
                .iconSet(DULL)
                .buildAndRegister();

        Gluons = material("gluons", "胶子")
                .gas()
                .color(0xfbfbf9)
                .element(GTOElements.GLUONS)
                .iconSet(DULL)
                .buildAndRegister();

        OganessonBreedingBase = material("oganesson_breeding_base", "鿫增殖基")
                .fluid()
                .color(0xb8676c)
                .iconSet(DULL)
                .buildAndRegister();

        TitaniumTetrafluoride = material("titanium_tetrafluoride", "四氟化钛")
                .fluid()
                .color(0xd68fed)
                .components(Titanium, 1, Fluorine, 4)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Titanium50Tetrafluoride = material("titanium_50_tetrafluoride", "四氟化钛-50")
                .fluid()
                .color(0xd68fed)
                .components(Titanium50, 1, Fluorine, 4)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Titanium50Tetrachloride = material("titanium_50_tetrachloride", "四氯化钛-50")
                .fluid()
                .color(0xafeeee)
                .components(Titanium50, 1, Chlorine, 4)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HotOganesson = material("hot_oganesson", "热鿫")
                .fluid()
                .color(0x42145d)
                .components(Oganesson, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Ferrocene = material("ferrocene", "二茂铁")
                .fluid()
                .color(0x7373c9)
                .components(Carbon, 10, Hydrogen, 10, Iron, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        ScandiumTitanium50Mixture = material("scandium_titanium_50_mixture", "钪-钛50混合物")
                .fluid()
                .color(0xeb315f)
                .components(Scandium, 1, Titanium50, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        DragonBreath = material("dragon_breath", "龙息")
                .fluid()
                .color(0xff00ff)
                .components(Draconium, 1, Concrete, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        EnrichedDragonBreath = material("enriched_dragon_breath", "富集龙息")
                .fluid()
                .color(0xff00ff)
                .components(Draconium, 1, Concrete, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        DragonBlood = material("dragon_blood", "龙血")
                .fluid()
                .color(0x9932cc)
                .components(Draconium, 1, Concrete, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TurbidDragonBlood = material("turbid_dragon_blood", "龙血浊液")
                .fluid()
                .color(0x4d0099)
                .components(Draconium, 1, Concrete, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        DragonElement = material("dragon_element", "龙素")
                .fluid()
                .color(0x9933ff)
                .iconSet(DULL)
                .components(Draconium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HeavyLeptonMixture = material("heavy_lepton_mixture", "重轻子混合物")
                .gas()
                .color(0x3ad931)
                .element(GTOElements.HEAVY_LEPTON_MIXTURE)
                .iconSet(DULL)
                .buildAndRegister();

        HeavyQuarkEnrichedMixture = material("heavy_quark_enriched_mixture", "富集重夸克混合物")
                .gas()
                .color(0xececec)
                .element(GTOElements.HEAVY_QUARK_ENRICHED_MIXTURE)
                .iconSet(DULL)
                .buildAndRegister();

        CosmicComputingMixture = material("cosmic_computing_mixture", "寰宇计算混合物")
                .gas()
                .color(0x8b8925)
                .components(Gluons, 1, HeavyQuarks, 1, HeavyLeptonMixture, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        LiquidStarlight = material("liquid_starlight", "星能液")
                .liquid(new FluidBuilder().customStill())
                .element(GTOElements.STARLIGHT)
                .buildAndRegister();

        Starlight = material("starlight", "星光")
                .liquid(new FluidBuilder().customStill())
                .element(GTOElements.STARLIGHT)
                .buildAndRegister();

        DenseNeutron = material("dense_neutron", "致密中子")
                .plasma()
                .color(0x9ce89c)
                .element(GTOElements.DENSE_NEUTRON)
                .iconSet(DULL)
                .buildAndRegister();

        HighEnergyQuarkGluon = material("high_energy_quark_gluon", "高能夸克-胶子")
                .plasma()
                .color(0x7400ce)
                .element(GTOElements.HIGH_ENERGY_QUARK_GLUON)
                .iconSet(BRIGHT)
                .buildAndRegister();

        NeutroniumDopedNanotubes = material("neutronium_doped_nanotubes", "掺中子素纳米管")
                .fluid()
                .color(0x5bf5f5)
                .iconSet(DULL)
                .buildAndRegister();

        AmmoniumNitrateSolution = material("ammonium_nitrate_solution", "硝酸铵溶液")
                .fluid()
                .color(0xe1ffff)
                .components(Nitrogen, 2, Hydrogen, 4, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(LIMPID)
                .buildAndRegister();

        NaquadahSolution = material("naquadah_solution", "硅岩溶液")
                .fluid()
                .color(0x00ff00)
                .iconSet(DULL)
                .buildAndRegister();

        FluorineCrackedAquadah = material("fluorine_cracked_aquadah", "加氟裂化硅岩")
                .fluid()
                .color(0x424d4b)
                .iconSet(DULL)
                .buildAndRegister();

        RadonCrackedEnrichedAquadah = material("radon_cracked_enriched_aquadah", "加氡裂化富集硅岩")
                .fluid()
                .color(0x424d4b)
                .iconSet(DULL)
                .buildAndRegister();

        NaquadahFuel = material("naquadah_fuel", "硅岩燃料")
                .fluid()
                .color(0x292929)
                .components(Naquadah, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        EnrichedNaquadahFuel = material("enriched_naquadah_fuel", "富集硅岩燃料")
                .fluid()
                .color(0x292929)
                .components(NaquadahEnriched, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HyperFuel1 = material("hyper_fuel_1", "超能燃料 I")
                .fluid()
                .color(0xf9ff3d)
                .components(Naquadah, 1, NaquadahEnriched, 1, Naquadria, 1, Thorium, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HyperFuel2 = material("hyper_fuel_2", "超能燃料 II")
                .fluid()
                .color(0xd1d54d)
                .components(HyperFuel1, 1, Uranium235, 1, Dubnium, 1, Fermium, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HyperFuel3 = material("hyper_fuel_3", "超能燃料 III")
                .fluid()
                .color(0x7a7c3c)
                .components(HyperFuel2, 1, Plutonium241, 1, Adamantine, 1, Lawrencium, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HyperFuel4 = material("hyper_fuel_4", "超能燃料 IV")
                .fluid()
                .color(0x3f4028)
                .components(HyperFuel3, 1, Nobelium, 1, Neutronium, 1, Taranium, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ConcentrationMixingHyperFuel1 = material("concentration_mixing_hyper_fuel_1", "浓缩混合超能燃料 I")
                .fluid()
                .color(0x000000)
                .components(HyperFuel4, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ConcentrationMixingHyperFuel2 = material("concentration_mixing_hyper_fuel_2", "浓缩混合超能燃料 II")
                .fluid()
                .color(0x000000)
                .components(HyperFuel4, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CosmicElement = material("cosmic_element", "宇宙素")
                .gas()
                .color(0xa366ff)
                .components(FreeElectronGas, 1, FreeAlphaGas, 1, FreeProtonGas, 1, QuarkGluon, 1,
                        HeavyQuarks, 1, LightQuarks, 1, Gluons, 1, HeavyLeptonMixture, 1,
                        HeavyQuarkEnrichedMixture, 1, DenseNeutron, 1,
                        HighEnergyQuarkGluon, 1)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        DimensionallyTranscendentResidue = material("dimensionallytranscendentresidue", "超维度残留")
                .liquid(new FluidBuilder().customStill())
                .buildAndRegister().setFormula("?", false);

        PrimordialMatter = material("primordialmatter", "流体本源物质")
                .liquid(new FluidBuilder().customStill())
                .buildAndRegister().setFormula("?", false);

        SpatialFluid = material("spatialfluid", "扩大化空间流体")
                .liquid(new FluidBuilder().customStill())
                .element(GTOElements.TIME)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TemporalFluid = material("temporalfluid", "富快子时间流体")
                .liquid(new FluidBuilder().customStill())
                .element(GTOElements.TIME)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Isochloropropane = material("isochloropropane", "氯丙烷")
                .fluid()
                .color(0xcdd681)
                .components(Carbon, 3, Hydrogen, 7, Chlorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Dinitrodipropanyloxybenzene = material("dinitrodipropanyloxybenzene", "二硝基二丙氧基苯")
                .fluid()
                .color(0x6a784d)
                .components(Carbon, 12, Hydrogen, 16, Nitrogen, 2, Oxygen, 6)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        RadoxGas = material("radox_gas", "拉多X气")
                .gas()
                .components(Carbon, 14, Osmium, 11, Silver, 3, Concrete, 1, Water, 1)
                .color(0xab57ab)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CrackedRadox = material("crackedradox", "裂化拉多X")
                .fluid()
                .components(Carbon, 14, Osmium, 11, Silver, 3, Concrete, 1, Water, 1)
                .color(0xab57ab)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SuperLightRadox = material("superlightradox", "超轻拉多X")
                .fluid()
                .components(Carbon, 14, Osmium, 11, Silver, 2, Concrete, 1, Water, 1)
                .color(0x6c006c)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        LightRadox = material("lightradox", "轻拉多X")
                .fluid()
                .components(Carbon, 14, Osmium, 11, Silver, 1, Concrete, 1, Water, 1)
                .color(0x6c006c)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SuperHeavyRadox = material("superheavyradox", "超重拉多X")
                .fluid()
                .components(Carbon, 14, Osmium, 11, Concrete, 1, Water, 1)
                .color(0x6c006c)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HeavyRadox = material("heavyradox", "重拉多X")
                .fluid()
                .components(Carbon, 14, Osmium, 11, Concrete, 1, Water, 1)
                .color(0x6c006c)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        RawRadox = material("rawradox", "粗制拉多X")
                .fluid()
                .color(0x391539)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?", false);

        Xenoxene = material("xenoxene", "异氙")
                .fluid()
                .color(0x5c5a58)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?", false);

        XenoxeneCrystal = material("xenoxene_crystal", "结晶异氙")
                .dust()
                .color(0x5c5a58)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?", false);

        XenoxeneMixture = material("xenoxene_mixture", "异氙混合物")
                .fluid()
                .color(0x5c7a58)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?", false);

        EnrichedXenoxene = material("enriched_xenoxene", "富集异氙")
                .fluid()
                .color(0x5c5a58)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        PurifiedXenoxene = material("purified_xenoxene", "纯净异氙")
                .fluid()
                .color(0x5c7a58)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        DilutedXenoxene = material("dilutedxenoxene", "钝化异氙")
                .fluid()
                .color(0x8b8784)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?", false);

        Dibromomethylbenzene = material("dibromomethylbenzene", "二溴甲苯")
                .fluid()
                .components(Carbon, 7, Hydrogen, 6, Bromine, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x6b584)
                .iconSet(DULL)
                .buildAndRegister();

        RawStarMatter = material("raw_star_matter", "原始恒星混合物")
                .element(GTOElements.RAW_STAR_MATTER)
                .plasma(new FluidBuilder().customStill())
                .buildAndRegister();

        BiomediumRaw = material("biomediumraw", "生物培养基原液")
                .fluid()
                .color(0x42641f)
                .iconSet(DULL)
                .buildAndRegister();

        BiohmediumSterilized = material("biohmediumsterilized", "灭菌生物培养基原液")
                .fluid()
                .color(0x72b125)
                .iconSet(DULL)
                .buildAndRegister();

        UnknowWater = material("unknowwater", "不明液体")
                .fluid()
                .color(0x3322aa)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        UnknownNutrientAgar = material("unknownnutrientagar", "未知营养琼脂")
                .fluid()
                .color(0x916e00)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        SeaweedBroth = material("seaweedbroth", "海藻基质")
                .fluid()
                .color(0x2c9400)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        LiquidCrystalKevlar = material("liquidcrystalkevlar", "液晶凯芙拉")
                .fluid()
                .color(0x9f9f50)
                .iconSet(DULL)
                .buildAndRegister();

        PolyurethaneResin = material("polyurethaneresin", "聚氨酯树脂")
                .fluid()
                .color(0x9a9a51)
                .iconSet(DULL)
                .buildAndRegister();

        DiphenylmethanediisocyanateMixture = material("diphenylmethanediisocyanatemixture", "二苯基甲烷二异氰酸酯混合物")
                .fluid()
                .color(0x94851d)
                .iconSet(DULL)
                .buildAndRegister();

        Phosgene = material("phosgene", "碳酰氯")
                .fluid()
                .components(Carbon, 1, Oxygen, 1, Chlorine, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x0e6c11)
                .iconSet(DULL)
                .buildAndRegister();

        DiaminodiphenylmethanMixture = material("diaminodiphenylmethanmixture", "二氨基二苯甲烷混合物")
                .fluid()
                .color(0x94851d)
                .iconSet(DULL)
                .buildAndRegister();

        SiliconOil = material("siliconoil", "硅油")
                .fluid()
                .color(0xadadad)
                .iconSet(DULL)
                .buildAndRegister();

        EthyleneGlycol = material("ethyleneglycol", "乙二醇")
                .fluid()
                .color(0xadadad)
                .components(Carbon, 2, Hydrogen, 6, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        PCBs = material("pcbs", "苯基-C61-丁酸苯乙烯")
                .fluid()
                .color(0x758a61)
                .components(Carbon, 80, Hydrogen, 21, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        DMAP = material("dmap", "二甲氨基吡啶")
                .dust()
                .color(0x758a61)
                .components(Carbon, 7, Hydrogen, 10, Nitrogen, 2)
                .flags(GTOMaterialFlags.GENERATE_CATALYST)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        AluminiumChloride = material("aluminium_chloride", "氯化铝")
                .dust()
                .color(0xa4c4c6)
                .components(Aluminium, 1, Chlorine, 3)
                .iconSet(DULL)
                .buildAndRegister();

        TiAlChloride = material("ti_al_chloride", "钛-铝氯化物")
                .dust()
                .color(0x6e1050)
                .flags(GTOMaterialFlags.GENERATE_CATALYST)
                .iconSet(DULL)
                .buildAndRegister();

        PhenylPentanoicAcid = material("phenylpentanoic_acid", "苯基戊酸")
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0x4d3833)
                .components(Carbon, 11, Hydrogen, 14, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Dichloromethane = material("dichloromethane", "二氯甲烷")
                .fluid()
                .color(0x832663)
                .components(Carbon, 1, Hydrogen, 2, Chlorine, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        DimethylSulfide = material("dimethyl_sulfide", "二甲硫醚")
                .fluid()
                .color(0xa02e06)
                .components(Carbon, 2, Hydrogen, 6, Sulfur, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        CosmicMesh = material("cosmic_mesh", "寰宇织网")
                .plasma()
                .fluid()
                .color(0x181878)
                .element(GTOElements.COSMIC_MESH)
                .iconSet(DULL)
                .buildAndRegister();

        HydrobromicAcid = material("hydrobromic_acid", "氢溴酸")
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .components(Hydrogen, 1, Bromine, 1)
                .color(0xa2573f)
                .iconSet(DULL)
                .buildAndRegister();

        BenzophenanthrenylAcetonitrile = material("benzophenanthrenylacetonitrile", "苯并菲乙腈")
                .dust()
                .components(Carbon, 20, Hydrogen, 13, Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x9222c7)
                .iconSet(DULL)
                .buildAndRegister();

        BromoSuccinamide = material("bromo_succinimide", "N-溴代琥珀酰亚胺")
                .dust()
                .components(Carbon, 4, Hydrogen, 4, Bromine, 1, Nitrogen, 1, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x546a21)
                .iconSet(DULL)
                .buildAndRegister();

        PotassiumBromide = material("potassium_bromide", "溴化钾")
                .dust()
                .components(Potassium, 1, Bromine, 1)
                .color(0xa34a76)
                .iconSet(GEM_HORIZONTAL)
                .buildAndRegister();

        Succinimide = material("succinimide", "琥珀酰亚胺")
                .dust()
                .components(Carbon, 4, Hydrogen, 5, Nitrogen, 1, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x21a7c5)
                .iconSet(DULL)
                .buildAndRegister();

        FissionedUranium235 = material("fissioned_uranium_235", "裂变铀-235")
                .dust()
                .color(0x39c9b7)
                .iconSet(METALLIC)
                .buildAndRegister();

        FranciumCaesiumCadmiumBromide = material("francium_caesium_cadmium_bromide", "溴化钫铯镉")
                .dust()
                .components(Francium, 1, Caesium, 1, Cadmium, 2, Bromine, 6)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xa34d1a)
                .iconSet(BRIGHT)
                .buildAndRegister();

        StrontiumEuropiumAluminate = material("strontium_europium_aluminate", "锶铕铝酸盐")
                .dust()
                .components(Strontium, 1, Europium, 1, Aluminium, 2, Oxygen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xb62d78)
                .iconSet(BRIGHT)
                .buildAndRegister();

        UraniumSulfateWasteSolution = material("uranium_sulfate_waste_solution", "硫酸铀废液")
                .fluid()
                .color(0xb1b113)
                .iconSet(DULL)
                .buildAndRegister();

        DibismuthHydroborate = material("dibismuthhydroborat", "硼氢二铋")
                .dust()
                .components(Bismuth, 2, Hydrogen, 1, Boron, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x007d32)
                .iconSet(BRIGHT)
                .buildAndRegister();

        CircuitCompound = material("circuit_compound", "电路化合物")
                .dust()
                .components(IndiumGalliumPhosphide, 1, DibismuthHydroborate, 3, BismuthTellurite, 2)
                .color(0x00210e)
                .iconSet(BRIGHT)
                .buildAndRegister();

        CaesiumIodide = material("caesium_iodide", "碘化铯")
                .dust()
                .components(Caesium, 1, Iodine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xeeeee2)
                .iconSet(DULL)
                .buildAndRegister();

        ThalliumThuliumDopedCaesiumIodide = material("thallium_thulium_doped_caesium_iodide", "铊铥掺杂的碘化铯")
                .dust()
                .color(0xe8b97f)
                .iconSet(BRIGHT)
                .buildAndRegister();

        Photoresist = material("photoresist", "光刻胶")
                .fluid()
                .color(0x2f4f4f)
                .iconSet(BRIGHT)
                .buildAndRegister();

        EuvPhotoresist = material("euv_photoresist", "EUV光刻胶")
                .fluid()
                .color(0x4b0082)
                .iconSet(BRIGHT)
                .buildAndRegister();

        GammaRaysPhotoresist = material("gamma_rays_photoresist", "γ射线光刻胶")
                .fluid()
                .color(0x556b2f)
                .iconSet(BRIGHT)
                .buildAndRegister();

        AcrylicAcid = material("acrylic_acid", "丙烯酸")
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0xb41558)
                .components(Carbon, 3, Hydrogen, 4, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        EthylAcrylate = material("ethyl_acrylate", "丙烯酸乙酯")
                .fluid()
                .color(0x947d15)
                .components(Carbon, 5, Hydrogen, 8, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Trichloroflerane = material("trichloroflerane", "三氯𫓧烷")
                .fluid()
                .color(0x42145d)
                .components(Flerovium, 1, Chlorine, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        BisethylenedithiotetraselenafulvalenePerrhenate = material("bisethylenedithiotetraselenafulvalene_perrhenate", "高铼酸双（乙烯二硫代）四硒富瓦烯")
                .dust()
                .color(0x72cb00)
                .components(Rhenium, 1, Carbon, 10, Hydrogen, 8, Sulfur, 4, Selenium, 4, Oxygen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(BRIGHT)
                .buildAndRegister();

        Bisethylenedithiotetraselenafulvalene = material("bisethylenedithiotetraselenafulvalene", "双（乙烯二硫代）四硒富瓦烯")
                .dust()
                .color(0x72cb00)
                .components(Carbon, 10, Hydrogen, 8, Sulfur, 4, Selenium, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(BRIGHT)
                .buildAndRegister();

        LithiumThiinediselenide = material("lithium_thiinediselenide", "二硒醚合硫锂")
                .dust()
                .color(0x72cb00)
                .components(Carbon, 4, Hydrogen, 4, Sulfur, 2, Lithium, 2, Selenium, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        CyclopentadienylTitaniumTrichloride = material("cyclopentadienyl_titanium_trichloride", "环戊二烯基三氯化钛")
                .dust()
                .color(0xb22db2)
                .components(Carbon, 10, Hydrogen, 10, Chlorine, 2, Titanium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(BRIGHT)
                .buildAndRegister();

        ButylLithium = material("butyl_lithium", "丁基锂")
                .fluid()
                .color(0x9f6af6)
                .components(Carbon, 4, Hydrogen, 9, Lithium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Bromodihydrothiine = material("bromodihydrothiine", "溴二氢硫醚")
                .fluid()
                .color(0x50c44d)
                .components(Carbon, 4, Hydrogen, 4, Sulfur, 2, Bromine, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Dibromoacrolein = material("dibromoacrolein", "二溴丙烯醛")
                .fluid()
                .color(0x3e3e3e)
                .components(Carbon, 2, Hydrogen, 2, Bromine, 2, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumThiosulfate = material("sodium_thiosulfate", "硫代硫酸钠")
                .dust()
                .color(0x145a9d)
                .components(Sodium, 2, Sulfur, 2, Oxygen, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        LithiumFluoride = material("lithium_fluoride", "氟化锂")
                .dust()
                .color(0x6d6d6d)
                .components(Lithium, 1, Fluorine, 1)
                .iconSet(DULL)
                .buildAndRegister();

        HighPurityCalciumCarbonate = material("high_purity_calcium_carbonate", "高纯度碳酸钙")
                .dust()
                .color(0xeeeee0)
                .components(Calcium, 1, Carbon, 1, Oxygen, 3)
                .iconSet(DULL)
                .buildAndRegister();

        Bromobutane = material("bromobutane", "溴丁烷")
                .fluid()
                .color(0xff0c0c)
                .components(Carbon, 4, Hydrogen, 9, Bromine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Propadiene = material("propadiene", "丙二烯")
                .fluid()
                .color(0x323b0a)
                .components(Carbon, 3, Hydrogen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        AstatideSolution = material("astatide_solution", "砹化物溶液")
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0x4ff417)
                .components(Astatine, 1, SulfuricAcid, 1)
                .iconSet(DULL)
                .buildAndRegister();

        MixedAstatideSalts = material("mixed_astatide_salts", "混合砹化盐")
                .dust()
                .color(0x67e83c)
                .components(Holmium, 1, Thulium, 1, Copernicium, 1, Flerovium, 1, Astatine, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        BoronFranciumCarbide = material("boron_francium_carbide", "硼-钫碳化物")
                .dust()
                .color(0x797979)
                .components(Francium, 4, Boron, 4, Carbon, 7)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Borocarbide = material("borocarbide", "碳化硼混合材料")
                .dust()
                .color(0x7e7e22)
                .components(MixedAstatideSalts, 1, BoronFranciumCarbide, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        FranciumCarbide = material("francium_carbide", "碳化钫")
                .dust()
                .color(0xa1a1a1)
                .components(Francium, 2, Carbon, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        BoronCarbide = material("boron_carbide", "碳化硼")
                .dust()
                .color(0x1e1e1e)
                .components(Boron, 4, Carbon, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        LanthanumEmbeddedFullerene = material("lanthanum_embedded_fullerene", "镧-富勒烯包合物")
                .dust()
                .color(0x91c100)
                .components(Lanthanum, 1, Fullerene, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        LanthanumFullereneMix = material("lanthanum_fullerene_mix", "镧-富勒烯混合物")
                .dust()
                .color(0xd3bfec)
                .components(Lanthanum, 1, UnfoldedFullerene, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        CaliforniumTrichloride = material("californium_trichloride", "三氯化锎")
                .dust()
                .color(0x286224)
                .components(Californium, 1, Chlorine, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        FullereneDopedNanotubes = material("fullerene_doped_nanotubes", "富勒烯掺杂的纳米管")
                .fluid()
                .color(0x562356)
                .iconSet(DULL)
                .buildAndRegister();

        CaliforniumCyclopentadienide = material("californium_cyclopentadienide", "环戊二烯化锎")
                .fluid()
                .color(0x78374a)
                .components(Carbon, 15, Hydrogen, 15, Californium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Cyclopentadiene = material("cyclopentadiene", "环戊二烯")
                .fluid()
                .color(0x2aa62a)
                .components(Carbon, 5, Hydrogen, 6)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        LithiumCyclopentadienide = material("lithium_cyclopentadienide", "环戊二烯化锂")
                .fluid()
                .color(0x7b4657)
                .components(Lithium, 1, Carbon, 5, Hydrogen, 5)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Dimethylether = material("dimethylether", "二甲基乙醚")
                .fluid()
                .color(0xbda90e)
                .components(Carbon, 2, Hydrogen, 6, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Dimethoxyethane = material("dimethoxyethane", "二甲氧基乙烷")
                .fluid()
                .color(0x23a996)
                .components(Carbon, 4, Hydrogen, 10, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Photopolymer = material("photopolymer", "光聚合物溶液")
                .fluid()
                .color(0x73445b)
                .components(Carbon, 149, Hydrogen, 97, Nitrogen, 10, Oxygen, 2, Titanium, 1,
                        Boron, 1, Fluorine, 20)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SilverPerchlorate = material("silver_perchlorate", "高氯酸银")
                .dust()
                .color(0xededed)
                .components(Silver, 1, Chlorine, 1, Oxygen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SilverChloride = material("silver_chloride", "氯化银")
                .dust()
                .color(0x8d8d8d)
                .components(Silver, 1, Chlorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumBromide = material("sodium_bromide", "溴化钠")
                .dust()
                .color(0xc588c4)
                .components(Sodium, 1, Bromine, 1)
                .iconSet(DULL)
                .buildAndRegister();

        SilverOxide = material("silver_oxide", "氧化银")
                .dust()
                .color(0x2b2b2b)
                .components(Silver, 2, Oxygen, 1)
                .iconSet(DULL)
                .buildAndRegister();

        PhthalicAnhydride = material("phthalic_anhydride", "邻苯二甲酸酐")
                .dust()
                .color(0x8c8c8c)
                .components(Carbon, 8, Hydrogen, 4, Oxygen, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumHypochlorite = material("sodium_hypochlorite", "次氯酸钠")
                .dust()
                .color(0x66f14c)
                .components(Sodium, 1, Chlorine, 1, Oxygen, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Ethylanthraquinone = material("ethylanthraquinone", "2-乙基蒽醌")
                .fluid()
                .color(0xffff24)
                .components(Carbon, 16, Hydrogen, 12, Oxygen, 2)
                .iconSet(DULL)
                .buildAndRegister();

        Ethylanthrahydroquinone = material("ethylanthrahydroquinone", "2-乙基蒽氢醌")
                .fluid()
                .color(0xcece00)
                .components(Ethylanthraquinone, 1, Hydrogen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Anthracene = material("anthracene", "蒽")
                .fluid()
                .color(0x929e92)
                .components(Carbon, 14, Hydrogen, 10)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Phenylsodium = material("phenylsodium", "苯基钠")
                .fluid()
                .color(0x2626ab)
                .components(Carbon, 6, Hydrogen, 5, Sodium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        NDifluorophenylpyrrole = material("n_difluorophenylpyrrole", "N-二氟苯基吡咯")
                .fluid()
                .color(0x2f7e8a)
                .components(Carbon, 10, Hydrogen, 7, Fluorine, 2, Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Difluoroaniline = material("difluoroaniline", "二氟苯胺")
                .fluid()
                .color(0x348f3e)
                .components(Carbon, 6, Hydrogen, 5, Fluorine, 2, Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Succinaldehyde = material("succinaldehyde", "琥珀醛")
                .fluid()
                .color(0x63577d)
                .components(Carbon, 4, Hydrogen, 6, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        TetraethylammoniumBromide = material("tetraethylammonium_bromide", "四乙基溴化铵")
                .fluid()
                .color(0xc20cff)
                .components(Carbon, 8, Hydrogen, 20, Nitrogen, 1, Bromine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        RhodiumRheniumNaquadahCatalyst = material("rhodium_rhenium_naquadah", "铑-铼-硅岩")
                .dust()
                .color(0x944648)
                .components(Rhodium, 1, Rhenium, 1, Naquadah, 1)
                .flags(GTOMaterialFlags.GENERATE_CATALYST)
                .iconSet(DULL)
                .buildAndRegister();

        IodineMonochloride = material("iodine_monochloride", "氯化碘")
                .fluid()
                .color(0x004141)
                .components(Iodine, 1, Chlorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Dimethylnaphthalene = material("dimethylnaphthalene", "二甲基萘")
                .fluid()
                .color(0xde2da1)
                .components(Carbon, 12, Hydrogen, 12)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Dihydroiodotetracene = material("dihydroiodotetracene", "二氢碘化四联苯")
                .fluid()
                .color(0xde2da1)
                .components(Carbon, 18, Hydrogen, 13, Iodine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        AcetylatingReagent = material("acetylating_reagent", "乙酰化试剂")
                .fluid()
                .color(0x724c50)
                .components(Carbon, 9, Hydrogen, 12, Silicon, 1, Magnesium, 2, Bromine, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        MagnesiumChlorideBromide = material("magnesium_chloride_bromide", "溴氯化镁")
                .dust()
                .color(0x99234d)
                .components(Magnesium, 1, Chlorine, 1, Bromine, 1)
                .iconSet(DULL)
                .buildAndRegister();

        IsopropylAlcohol = material("isopropyl_alcohol", "异丙醇")
                .fluid()
                .color(0x51b31f)
                .components(Carbon, 3, Hydrogen, 8, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Dichlorodicyanobenzoquinone = material("dichlorodicyanobenzoquinone", "二氯二氰苯醌")
                .fluid()
                .color(0x302399)
                .components(Carbon, 8, Chlorine, 2, Nitrogen, 2, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Dichlorodicyanohydroquinone = material("dichlorodicyanohydroquinone", "二氯二氰氢醌")
                .fluid()
                .color(0x302399)
                .components(Carbon, 8, Chlorine, 2, Nitrogen, 2, Oxygen, 2, Hydrogen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Tetracene = material("tetracene", "并四苯")
                .dust()
                .color(0x8f7718)
                .components(Carbon, 18, Hydrogen, 12)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        PolycyclicAromaticMixture = material("polycyclic_aromatic_mixture", "多环芳香烃混合物")
                .dust()
                .color(0x8f7718)
                .components(Carbon, 18, Hydrogen, 12)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        RheniumHassiumThalliumIsophtaloylbisdiethylthioureaHexaf = material("rhenium_hassium_thallium_isophtaloylbisdiethylthiourea_hexaf", "间苯二甲酰双（二乙基硫脲基）六氟磷酸铼-𬭶-铊")
                .dust()
                .color(0x8f7718)
                .components(Rhenium, 1, Hassium, 1, Thallium, 1, Carbon, 60, Phosphorus, 1,
                        Nitrogen, 12, Hydrogen, 84, Sulfur, 6, Oxygen, 12, Fluorine, 6)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        ThalliumChloride = material("thallium_chloride", "氯化铊")
                .dust()
                .color(0xcc5350)
                .components(Thallium, 1, Chlorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        HassiumChloride = material("hassium_chloride", "氯化𬭶")
                .dust()
                .color(0x5828b2)
                .components(Hassium, 1, Chlorine, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        RheniumChloride = material("rhenium_chloride", "氯化铼")
                .dust()
                .color(0x392857)
                .components(Rhenium, 1, Chlorine, 5)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        IsophthaloylBis = material("isophthaloylbis", "间苯二甲酰基二乙基硫脲")
                .fluid()
                .color(0x76677e)
                .components(Carbon, 18, Hydrogen, 26, Nitrogen, 4, Oxygen, 2, Sulfur, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        HexafluorophosphoricAcid = material("hexafluorophosphoric_acid", "单氟磷酸")
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0xd5d54b)
                .components(Hydrogen, 1, Phosphorus, 1, Fluorine, 6)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Diethylthiourea = material("diethylthiourea", "二乙基硫脲")
                .fluid()
                .color(0x23a687)
                .components(Carbon, 5, Hydrogen, 12, Nitrogen, 2, Sulfur, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        ThionylChloride = material("thionyl_chloride", "氯化亚砜")
                .fluid()
                .color(0xf8f5e0)
                .components(Sulfur, 1, Oxygen, 1, Chlorine, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        PhenylenedioxydiaceticAcid = material("phenylenedioxydiacetic_acid", "亚苯基二氧二乙酸")
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0x7c4456)
                .components(Carbon, 10, Hydrogen, 10, Oxygen, 6)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumThiocyanate = material("sodium_thiocyanate", "硫氰酸钠")
                .fluid()
                .color(0x7c4456)
                .components(Sodium, 1, Sulfur, 1, Carbon, 1, Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        PhosphorusTrichloride = material("phosphorus_trichloride", "三氯化磷")
                .fluid()
                .color(0xd5d54b)
                .components(Phosphorus, 1, Chlorine, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        AntimonyPentafluoride = material("antimony_pentafluoride", "五氟化锑")
                .fluid()
                .color(0xd5d5bd)
                .components(Antimony, 1, Fluorine, 5)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        AntimonyTrichloride = material("antimony_trichloride", "三氯化锑")
                .dust()
                .color(0xbcbcbc)
                .components(Antimony, 1, Chlorine, 3)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ChargedCaesiumCeriumCobaltIndium = material("charged_caesium_cerium_cobalt_indium", "带电的铯-铈-钴-铟")
                .dust()
                .color(0x4da323)
                .components(Caesium, 1, Cerium, 1, Cobalt, 2, Indium, 10, CosmicComputingMixture, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ActiniumSuperhydride = material("actinium_superhydride", "超氢化锕")
                .plasma()
                .dust()
                .color(0x52b051)
                .components(Actinium, 1, Hydrogen, 12)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CosmicSuperconductor = material("cosmic_superconductor", "寰宇超导液")
                .fluid()
                .color(0x00ffff)
                .components(RheniumHassiumThalliumIsophtaloylbisdiethylthioureaHexaf, 1,
                        ActiniumSuperhydride, 1, ChargedCaesiumCeriumCobaltIndium, 1)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Ethylamine = material("ethylamine", "乙胺")
                .fluid()
                .color(0x5a656d)
                .components(Carbon, 2, Hydrogen, 5, Nitrogen, 1, Hydrogen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        TolueneDiisocyanate = material("toluene_diisocyanate", "甲苯二异氰酸脂")
                .fluid()
                .color(0xacf4bf)
                .components(Carbon, 9, Hydrogen, 6, Nitrogen, 2, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Polyurethane = material("polyurethane", "聚氨基甲酸酯")
                .fluid()
                .color(0xacf4bf)
                .components(Carbon, 17, Hydrogen, 16, Nitrogen, 2, Oxygen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        ViscoelasticPolyurethane = material("viscoelastic_polyurethane", "粘弹性聚氨酯")
                .fluid()
                .color(0xecfbec)
                .components(Polyurethane, 1, EthyleneGlycol, 1, Calcite, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ViscoelasticPolyurethaneFoam = material("viscoelastic_polyurethane_foam", "粘弹性聚氨酯泡沫")
                .fluid()
                .color(0xecfbec)
                .components(ViscoelasticPolyurethane, 1, Air, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Glucose = material("glucose", "葡萄糖")
                .dust()
                .color(0xcacace)
                .components(Carbon, 6, Hydrogen, 12, Oxygen, 6)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        GlucoseIronSolution = material("glucose_iron_solution", "葡萄糖铁溶液")
                .fluid()
                .color(0xc9c9c9)
                .components(Glucose, 1, Iron3Chloride, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GrapheneOxide = material("graphene_oxide", "氧化石墨烯")
                .dust()
                .color(0x535353)
                .components(Carbon, 1, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        GrapheneGelSuspension = material("graphene_gel_suspension", "石墨烯浆料")
                .dust()
                .color(0x535353)
                .components(Carbon, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        DryGrapheneGel = material("dry_graphene_gel", "干石墨烯凝胶")
                .dust()
                .color(0x202079)
                .components(Carbon, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SupercriticalCarbonDioxide = material("supercritical_carbon_dioxide", "超临界二氧化碳")
                .fluid()
                .color(0x9ac8f3)
                .components(Carbon, 1, Oxygen, 2)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        PotassiumBisulfite = material("potassium_bisulfite", "亚硫酸氢钾")
                .dust()
                .color(0x807d72)
                .components(Potassium, 1, Sulfur, 1, Hydrogen, 1, Oxygen, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        PotassiumHydroxylaminedisulfonate = material("potassium_hydroxylaminedisulfonate", "羟胺二磺酸钾")
                .dust()
                .color(0x4c6226)
                .components(Potassium, 2, Nitrogen, 1, Hydrogen, 1, Sulfur, 2, Oxygen, 7)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        HydroxylammoniumSulfate = material("hydroxylammonium_sulfate", "羟铵硫酸盐")
                .dust()
                .color(0x848075)
                .components(Nitrogen, 2, Hydrogen, 8, Sulfur, 1, Oxygen, 6)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        BariumChloride = material("barium_chloride", "氯化钡")
                .dust()
                .color(0xe9705f)
                .flags(GTOMaterialFlags.GENERATE_TINY_DUST)
                .components(Barium, 1, Chlorine, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        NitrousAcid = material("nitrous_acid", "亚硝酸")
                .fluid()
                .color(0xa0c8fd)
                .components(Hydrogen, 1, Nitrogen, 1, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        ActiniumHydride = material("actinium_hydride", "氢化锕")
                .dust()
                .color(0xb8c6f1)
                .components(Actinium, 1, Hydrogen, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        FilteredSater = material("filtered_water", "过滤水")
                .fluid()
                .components(Hydrogen, 2, Oxygen, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        OzoneWater = material("ozone_water", "臭氧水")
                .fluid()
                .components(Hydrogen, 2, Oxygen, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        FlocculentWater = material("flocculent_water", "絮凝水")
                .fluid()
                .components(Hydrogen, 2, Oxygen, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        PHNeutralWater = material("ph_neutral_water", "ph中和水")
                .fluid()
                .components(Hydrogen, 2, Oxygen, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ExtremeTemperatureWater = material("extreme_temperature_water", "极端温度水")
                .fluid()
                .components(Hydrogen, 2, Oxygen, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ElectricEquilibriumWater = material("electric_equilibrium_water", "电平衡水")
                .fluid()
                .components(Hydrogen, 2, Oxygen, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        DegassedWater = material("degassed_water", "脱气水")
                .fluid()
                .components(Hydrogen, 2, Oxygen, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        BaryonicPerfectionWater = material("baryonic_perfection_water", "重子完美水")
                .fluid()
                .components(Hydrogen, 2, Oxygen, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Antimatter = material("antimatter", "离散反物质")
                .fluid()
                .color(0x9932cc)
                .iconSet(DULL)
                .buildAndRegister();

        PositiveElectron = material("positive_electron", "反电子")
                .fluid()
                .color(0x59764c)
                .iconSet(DULL)
                .buildAndRegister().setFormula("e+", false);

        Antiproton = material("antiproton", "反质子")
                .fluid()
                .color(0x4945af)
                .iconSet(DULL)
                .buildAndRegister().setFormula("p-", false);

        Antineutron = material("antineutron", "反中子")
                .fluid()
                .color(0xe6e6fa)
                .iconSet(DULL)
                .buildAndRegister().setFormula("n-bar", false);

        Antihydrogen = material("antihydrogen", "反氢")
                .fluid()
                .color(0x6a5acd)
                .iconSet(DULL)
                .buildAndRegister().setFormula("Ah", false);

        Kerosene = material("kerosene", "煤油")
                .fluid()
                .components(Carbon, 12, Hydrogen, 24)
                .color(0xce57ce)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Rp1 = material("rp_1", "RP-1混合燃料")
                .fluid()
                .color(0xff523e)
                .iconSet(DULL)
                .buildAndRegister();

        RocketFuelRp1 = material("rocket_fuel_rp_1", "火箭燃料 RP-1")
                .fluid()
                .color(0xff321b)
                .iconSet(DULL)
                .buildAndRegister();

        Hydrazine = material("hydrazine", "肼")
                .fluid()
                .color(0xffffff)
                .components(Nitrogen, 2, Hydrogen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        DenseHydrazineFuelMixture = material("dense_hydrazine_fuel_mixture", "浓缩肼混合燃料")
                .fluid()
                .color(0x4a223b)
                .iconSet(DULL)
                .buildAndRegister();

        Monomethylhydrazine = material("monomethylhydrazine", "甲肼")
                .fluid()
                .color(0xffffff)
                .components(Carbon, 1, Hydrogen, 6, Nitrogen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        RocketFuelCn3h7o3 = material("rocket_fuel_cn3h7o3", "火箭燃料（硝酸甲肼）")
                .fluid()
                .color(0xa636ac)
                .iconSet(DULL)
                .buildAndRegister();

        RocketFuelH8n4c2o4 = material("rocket_fuel_h8n4c2o4", "火箭燃料（偏二甲肼-四氧化二氮）")
                .fluid()
                .color(0x4aa11b)
                .iconSet(DULL)
                .buildAndRegister();

        StellarEnergyRocketFuel = material("stellar_energy_rocket_fuel", "星能火箭燃料")
                .fluid()
                .color(0xdf362d)
                .iconSet(DULL)
                .buildAndRegister();

        ExplosiveHydrazine = material("explosivehydrazine", "爆炸性肼燃料混合物")
                .fluid()
                .color(0x3b0c5c)
                .iconSet(DULL)
                .buildAndRegister();

        HmxExplosive = material("hmxexplosive", "HMX高能爆炸性化合物")
                .dust()
                .color(0xf3ffdb)
                .iconSet(BRIGHT)
                .buildAndRegister();

        LaNdOxidesSolution = material("la_nd_oxides_solution", "镧-钕氧化物溶液")
                .fluid()
                .color(0x9ce3db)
                .iconSet(DULL)
                .buildAndRegister().setFormula("(La2O3)(Pr2O3)(Nd2O3)(Ce2O3)");

        SmGdOxidesSolution = material("sm_gd_oxides_solution", "钐-钆氧化物溶液")
                .fluid()
                .color(0xffff99)
                .iconSet(DULL)
                .buildAndRegister().setFormula("(Sc2O3)(Eu2O3)(Gd2O3)(Sm2O3)");

        TbHoOxidesSolution = material("tb_ho_oxides_solution", "铽-钬氧化物溶液")
                .fluid()
                .color(0x99ff99)
                .iconSet(DULL)
                .buildAndRegister().setFormula("(Y2O3)(Tb2O3)(Dy2O3)(Ho2O3)");

        ErLuOxidesSolution = material("er_lu_oxides_solution", "铒-镥氧化物溶液")
                .fluid()
                .color(0xffb3ff)
                .iconSet(DULL)
                .buildAndRegister().setFormula("(Er2O3)(Tm2O3)(Yb2O3)(Lu2O3)");

        LanthanumOxide = material("lanthanum_oxide", "氧化镧")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Lanthanum, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        PraseodymiumOxide = material("praseodymium_oxide", "氧化镨")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Praseodymium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        NeodymiumOxide = material("neodymium_oxide", "氧化钕")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Neodymium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CeriumOxide = material("cerium_oxide", "氧化铈")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Cerium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        EuropiumOxide = material("europium_oxide", "氧化铕")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Europium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GadoliniumOxide = material("gadolinium_oxide", "氧化钆")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Gadolinium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SamariumOxide = material("samarium_oxide", "氧化钐")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Samarium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TerbiumOxide = material("terbium_oxide", "氧化铽")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Terbium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        DysprosiumOxide = material("dysprosium_oxide", "氧化镝")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Dysprosium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HolmiumOxide = material("holmium_oxide", "氧化钬")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Holmium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ErbiumOxide = material("erbium_oxide", "氧化铒")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Erbium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ThuliumOxide = material("thulium_oxide", "氧化铥")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Thulium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        YtterbiumOxide = material("ytterbium_oxide", "氧化镱")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Ytterbium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        LutetiumOxide = material("lutetium_oxide", "氧化镥")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Lutetium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ScandiumOxide = material("scandium_oxide", "氧化钪")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Scandium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        YttriumOxide = material("yttrium_oxide", "氧化钇")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Yttrium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ZirconChlorinatingResidue = material("zircon_chlorinating_residue", "锆氯化反应残渣")
                .fluid()
                .color(0x33ca33)
                .components(Silicon, 1, Chlorine, 4)
                .iconSet(DULL)
                .buildAndRegister();

        ZirconiumHafniumChloride = material("zirconium_hafnium_chloride", "锆-铪氯化物")
                .fluid()
                .color(0x33ca33)
                .iconSet(DULL)
                .buildAndRegister().setFormula("ZrHfCl₄", false);

        ZirconiumHafniumOxychloride = material("zirconiu_hafnium_oxychloride", "锆-铪氯氧化物")
                .fluid()
                .color(0x33ca33)
                .iconSet(DULL)
                .buildAndRegister().setFormula("Cl₂HfOZr", false);

        HafniumOxide = material("hafnium_oxide", "二氧化铪")
                .dust()
                .color(0x3c3c3c)
                .components(Hafnium, 1, Oxygen, 2)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ZirconiumOxide = material("zirconium_oxide", "二氧化锆")
                .dust()
                .color(0x3c3c3c)
                .components(Zirconium, 1, Oxygen, 2)
                .iconSet(DULL)
                .buildAndRegister();

        HafniumChloride = material("hafnium_chloride", "四氯化铪")
                .dust()
                .color(0x5c5c69)
                .components(Hafnium, 1, Chlorine, 4)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TelluriumOxide = material("tellurium_oxide", "二氧化碲")
                .dust()
                .color(0xd0d0d0)
                .components(Tellurium, 1, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(GLASS)
                .buildAndRegister();

        SodiumEthylate = material("sodium_ethylate", "乙醇钠")
                .dust()
                .color(0x9bcd9b)
                .components(Carbon, 2, Hydrogen, 5, Oxygen, 1, Sodium, 1)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumEthylxanthate = material("sodium_ethylxanthate", "乙基黄原酸钠")
                .dust()
                .color(0xcdad00)
                .components(Carbon, 3, Hydrogen, 5, Sodium, 1, Oxygen, 1, Sulfur, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        PotassiumEthylxanthate = material("potassium_ethylxanthate", "乙基黄原酸钾")
                .dust()
                .color(0xcdc8b1)
                .components(Carbon, 3, Hydrogen, 5, Potassium, 1, Oxygen, 1, Sulfur, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        PotassiumEthylate = material("potassium_ethylate", "乙醇钾")
                .dust()
                .color(0xcd661d)
                .components(Carbon, 2, Hydrogen, 5, Oxygen, 1, Potassium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        NMPyrolidone = material("nmethylpyrolidone", "N-甲基吡咯烷酮")
                .fluid()
                .color(0xd0d0d0)
                .components(Carbon, 5, Hydrogen, 9, Nitrogen, 1, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        GammaButyrolactone = material("gammabutyrolactone", "1,4-丁内酯")
                .fluid()
                .color(0xcccca1)
                .components(Carbon, 4, Hydrogen, 6, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Butane14Diol = material("butane_1_4_diol", "1,4-丁二醇")
                .fluid()
                .color(0xc4534c)
                .components(Carbon, 4, Hydrogen, 10, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Methylamine = material("methylamine", "甲胺")
                .fluid()
                .color(0x45486f)
                .components(Carbon, 1, Hydrogen, 5, Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        PPhenylenediamine = material("p_phenylenediamine", "对苯二胺")
                .dust()
                .color(0xdccf52)
                .components(Carbon, 6, Hydrogen, 8, Nitrogen, 2)
                .iconSet(DULL)
                .buildAndRegister();

        PNitroaniline = material("p_nitroaniline", "对硝基苯胺")
                .fluid()
                .color(0xcc9037)
                .components(Carbon, 6, Hydrogen, 8, Nitrogen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        TerephthalicAcid = material("terephthalicacid", "对苯二甲酸")
                .fluid()
                .color(0xd6d6d6)
                .components(Carbon, 8, Hydrogen, 6, Hydrogen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        DimethylTerephthalate = material("dimethylterephthalate", "对苯二甲酸二甲酯")
                .fluid()
                .color(0xd1d1d1)
                .components(Carbon, 10, Hydrogen, 10, Hydrogen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        TerephthaloylChloride = material("terephthaloyl_chloride", "对苯二甲酰氯")
                .dust()
                .color(0x00e60b)
                .components(Carbon, 8, Hydrogen, 4, Chlorine, 2, Nitrogen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Rhugnor = material("rhugnor", "鲁格诺")
                .fluid()
                .color(0xa800e2)
                .element(GTOElements.RHUGNOR)
                .iconSet(METALLIC)
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

        HotSodiumPotassium = material("hot_sodium_potassium", "热钠钾合金")
                .fluid()
                .color(0x64fcb4)
                .components(Sodium, 1, Potassium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(SAND)
                .buildAndRegister();

        SupercriticalSodiumPotassium = material("supercritical_sodium_potassium", "超临界钠钾合金")
                .fluid()
                .color(0x64fcb4)
                .components(Sodium, 1, Potassium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(LAPIS)
                .buildAndRegister();

        Copper76 = material("copper76", "铜-76")
                .dust()
                .element(GTOElements.COPPER76)
                .color(0xe77c56)
                .iconSet(BRIGHT)
                .buildAndRegister();

        CadmiumSulfide = material("cadmium_sulfide", "硫化镉")
                .dust()
                .components(Cadmium, 1, Sulfur, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xd4ba19)
                .iconSet(DULL)
                .buildAndRegister();

        CadmiumTungstate = material("cadmium_tungstate", "钨酸镉")
                .dust()
                .components(Cadmium, 1, Tungsten, 1, Oxygen, 4)
                .color(0x757770)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        BismuthGermanate = material("bismuth_germanate", "锗酸铋")
                .dust()
                .components(Bismuth, 12, Germanium, 1, Oxygen, 20)
                .color(0x4ea839)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        BismuthNitrateSolution = material("bismuth_nitrate_solution", "硝酸铋溶液")
                .fluid()
                .components(Water, 1, Bismuth, 1, Nitrogen, 3, Oxygen, 9)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xa4a7a8)
                .iconSet(DULL)
                .buildAndRegister();

        Paa = material("paa", "聚酰胺酸（PAA）")
                .fluid()
                .components(Carbon, 22, Hydrogen, 14, Nitrogen, 2, Oxygen, 7)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xead05e)
                .iconSet(DULL)
                .buildAndRegister();

        SilicaGelBase = material("silica_gel_base", "硅胶基质")
                .fluid()
                .color(0x39967a)
                .iconSet(DULL)
                .buildAndRegister();

        DeglyceratedSoap = material("deglycerated_soap", "脱糖肥皂")
                .fluid()
                .color(0xffb000)
                .iconSet(DULL)
                .buildAndRegister();

        Turpentine = material("turpentine", "松油")
                .fluid()
                .components(Carbon, 10, Hydrogen, 16)
                .color(0x9acd32)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        SteamCrackedTurpentine = material("steam_cracked_turpentine", "蒸汽裂化的松油浸出物")
                .fluid()
                .color(0x8b6914)
                .iconSet(FLUID)
                .buildAndRegister();

        LeachedTurpentine = material("leached_turpentine", "松油浸出物")
                .fluid()
                .components(Carbon, 10, Hydrogen, 16, Concrete, 1)
                .color(0xcd9b1d)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        AlmandineFront = material("almandine_front", "铁铝榴石矿石泡沫")
                .fluid()
                .components(Almandine, 1)
                .color(0xb22222)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        ChalcopyriteFront = material("chalcopyrite_front", "黄铜矿矿石泡沫")
                .fluid()
                .components(Chalcopyrite, 1)
                .color(0xcdaa7d)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        GrossularFront = material("grossular_front", "钙铝榴石矿石泡沫")
                .fluid()
                .components(Grossular, 1)
                .color(0xd2691e)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        MonaziteFront = material("monazite_front", "独居石矿石泡沫")
                .fluid()
                .components(Monazite, 1)
                .color(0x838b83)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        NickelFront = material("nickel_front", "镍矿石泡沫")
                .fluid()
                .components(Nickel, 1)
                .color(0xc1cdcd)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        PlatinumFront = material("platinum_front", "铂矿石泡沫")
                .fluid()
                .components(Platinum, 1)
                .color(0xcdc9a5)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        PyropeFront = material("pyrope_front", "镁铝榴石矿石泡沫")
                .fluid()
                .components(Pyrope, 1)
                .color(0x8b0000)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        RedstoneFront = material("redstone_front", "红石矿石泡沫")
                .fluid()
                .components(Redstone, 1)
                .color(0xee0000)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        SpessartineFront = material("spessartine_front", "锰铝榴石矿石泡沫")
                .fluid()
                .components(Spessartine, 1)
                .color(0xee5c42)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        SphaleriteFront = material("sphalerite_front", "闪锌矿矿石泡沫")
                .fluid()
                .components(Sphalerite, 1)
                .color(0xeee9e9)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        PentlanditeFront = material("pentlandite_front", "镍黄铁矿矿石泡沫")
                .fluid()
                .components(Pentlandite, 1)
                .color(0xcdaa7d)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        EnrichedNaquadahFront = material("enriched_naquadah_front", "富集硅岩矿石泡沫")
                .fluid()
                .components(NaquadahEnriched, 1)
                .color(0x58d00f)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        CarbonDisulfide = material("carbon_disulfide", "二硫化碳")
                .fluid()
                .components(Carbon, 1, Sulfur, 2)
                .color(0x104e8b)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        SpecialCeramics = material("special_ceramics", "特种陶瓷")
                .dust()
                .color(0x5c5909)
                .iconSet(DULL)
                .buildAndRegister();

        HydroiodicAcid = material("hydroiodic_acid", "氢碘酸")
                .fluid()
                .components(Hydrogen, 1, Iodine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x0382e2)
                .iconSet(LIMPID)
                .buildAndRegister();

        Acrylonitrile = material("acrylonitrile", "丙烯腈")
                .fluid()
                .components(Carbon, 3, Hydrogen, 3, Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xa4a4e1)
                .iconSet(LIMPID)
                .buildAndRegister();

        LithiumIodide = material("lithium_iodide", "碘化锂")
                .dust()
                .components(Lithium, 1, Iodine, 1)
                .color(0xc10014)
                .iconSet(DULL)
                .buildAndRegister();

        SilicaAluminaGel = material("silica_alumina_gel", "硅铝凝胶")
                .dust()
                .color(0x0c849f)
                .iconSet(DULL)
                .buildAndRegister();

        ZeoliteSievingPellets = material("zeolite_sieving_pellets", "过筛沸石颗粒")
                .dust()
                .color(0x4d3e9f)
                .iconSet(DULL)
                .buildAndRegister();

        WetZeoliteSievingPellets = material("wet_zeolite_sieving_pellets", "湿过筛沸石颗粒")
                .dust()
                .color(0x1d173c)
                .iconSet(DULL)
                .buildAndRegister();

        TertButanol = material("tert_butanol", "叔丁醇")
                .fluid()
                .components(Carbon, 4, Hydrogen, 10, Oxygen, 1)
                .color(0xacb500)
                .iconSet(LIMPID)
                .buildAndRegister();

        DitertbutylDicarbonate = material("ditertbutyl_dicarbonate", "二碳酸二叔丁酯")
                .dust()
                .components(Carbon, 10, Hydrogen, 18, Oxygen, 5)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x7e96b5)
                .iconSet(DULL)
                .buildAndRegister();

        Tertbuthylcarbonylazide = material("tertbuthylcarbonylazide", "叔丁基羰基叠氮")
                .fluid()
                .components(Carbon, 5, Hydrogen, 9, Nitrogen, 3, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xacb500)
                .iconSet(LIMPID)
                .buildAndRegister();

        SodiumToluenesulfonate = material("sodium_toluenesulfonate", "甲苯磺酸钠")
                .fluid()
                .components(Carbon, 7, Hydrogen, 7, Sulfur, 3, Oxygen, 3, Sodium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xb5b41d)
                .iconSet(LIMPID)
                .buildAndRegister();

        SodiumAzide = material("sodium_azide", "叠氮化钠")
                .dust()
                .components(Sodium, 1, Nitrogen, 3)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x1018f0)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumAzanide = material("sodium_azanide", "氨基钠")
                .dust()
                .components(Sodium, 1, Nitrogen, 1, Hydrogen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x2381b3)
                .iconSet(DULL)
                .buildAndRegister();

        NitrogenPentoxide = material("nitrogen_pentoxide", "五氧化二氮")
                .fluid()
                .components(Nitrogen, 2, Oxygen, 5)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x162bb3)
                .iconSet(LIMPID)
                .buildAndRegister();

        AminatedFullerene = material("aminated_fullerene", "胺化富勒烯")
                .fluid()
                .components(Carbon, 60, Hydrogen, 12, Nitrogen, 12)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x3842f0)
                .iconSet(LIMPID)
                .buildAndRegister();

        Azafullerene = material("azafullerene", "氮杂富勒烯")
                .fluid()
                .components(Carbon, 60, Hydrogen, 12, Nitrogen, 12)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xb3a500)
                .iconSet(LIMPID)
                .buildAndRegister();

        AbsoluteEthanol = material("absolute_ethanol", "绝对乙醇")
                .fluid()
                .color(0xff4500)
                .components(Carbon, 2, Hydrogen, 6, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(LIMPID)
                .buildAndRegister();

        PiranhaSolution = material("piranha_solution", "食人鱼洗液")
                .fluid()
                .color(0xac2fff)
                .iconSet(LIMPID)
                .buildAndRegister();

        PolyAluminiumChloride = material("poly_aluminium_chloride", "聚合氯化铝")
                .fluid()
                .color(0xf3ffe5)
                .iconSet(DULL)
                .buildAndRegister();

        FlocculationWasteSolution = material("flocculation_waste_solution", "絮凝废液")
                .fluid()
                .color(0xc7cac1)
                .iconSet(DULL)
                .buildAndRegister();

        PotassiumPyrosulfate = material("potassium_pyrosulfate", "焦硫酸钾")
                .dust()
                .components(Potassium, 2, Sulfur, 2, Oxygen, 7)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xff9900).iconSet(METALLIC)
                .buildAndRegister();

        PlatinumSlag = material("platinum_slag", "铂渣")
                .dust()
                .color(0x343318).iconSet(DULL)
                .buildAndRegister().setFormula("IrOsRhRu??", false);

        LeachResidue = material("leach_residue", "铂浸出渣")
                .dust()
                .color(0x644629).iconSet(DULL)
                .buildAndRegister().setFormula("IrOsRhRu?", false);

        ZincSulfate = material("zinc_sulfate", "硫酸锌")
                .dust()
                .components(Zinc, 1, Sulfur, 1, Oxygen, 4)
                .color(0x533c1b).iconSet(SAND)
                .buildAndRegister();

        RhodiumNitrate = material("rhodium_nitrate", "硝酸铑")
                .dust()
                .color(0x8c5a0c).iconSet(SAND)
                .flags(DISABLE_DECOMPOSITION)
                .components(Rhodium, 1, Nitrogen, 1, Oxygen, 3, Concrete, 1)
                .buildAndRegister();

        RoughlyRhodiumMetal = material("roughly_rhodium_metal", "粗制铑金属")
                .dust()
                .color(0x594c1a).iconSet(SAND)
                .buildAndRegister().setFormula("?Rh?", false);

        ReprecipitatedRhodium = material("reprecipitated_rhodium", "再沉淀铑")
                .dust()
                .color(0xd40849).iconSet(SAND)
                .flags(DISABLE_DECOMPOSITION)
                .components(Rhodium, 1, Nitrogen, 1, Hydrogen, 4)
                .buildAndRegister();

        RhodiumSalt = material("rhodium_salt", "铑盐")
                .dust()
                .color(0x61200a).iconSet(SAND)
                .buildAndRegister().setFormula("NaRhCl?", false);

        RhodiumSaltSolution = material("rhodium_salt_solution", "铑盐溶液")
                .fluid()
                .color(0x61200a).iconSet(SAND)
                .buildAndRegister().setFormula("Rh(NaCl)2Cl");

        RhodiumFilterCake = material("rhodium_filter_cake", "铑滤饼")
                .dust()
                .color(0x87350c).iconSet(ROUGH)
                .buildAndRegister().setFormula("?Rh?", false);

        RhodiumFilterCakeSolution = material("rhodium_filter_cake_solution", "铑滤饼溶液")
                .fluid()
                .color(0x87350c).iconSet(ROUGH)
                .buildAndRegister().setFormula("?Rh?", false);

        SodiumRutheniate = material("sodium_rutheniate", "钌酸钠")
                .dust()
                .color(0x282c8c).iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .components(Sodium, 2, Oxygen, 4, Ruthenium, 1)
                .buildAndRegister();

        IridiumDioxide = material("iridium_dioxide", "二氧化铱")
                .dust()
                .color(0xa2bfff).iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .components(Iridium, 1, Oxygen, 2)
                .buildAndRegister();

        RutheniumTetroxideLQ = material("ruthenium_tetroxide_lq", "四氧化钌溶液")
                .fluid()
                .color(0xa8a8a8).iconSet(ROUGH)
                .buildAndRegister();

        SodiumFormate = material("sodium_formate", "甲酸钠")
                .fluid()
                .color(0xf1939c).iconSet(ROUGH)
                .components(Sodium, 1, Carbon, 1, Oxygen, 2, Hydrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        RhodiumSulfateGas = material("rhodium_sulfate_gas", "气态硫酸铑")
                .gas()
                .color(0xbd8743).iconSet(ROUGH)
                .buildAndRegister();

        AcidicIridium = material("acidic_iridium", "酸性铱")
                .gas()
                .color(0x634e3a).iconSet(ROUGH)
                .buildAndRegister().setFormula("???", false);

        RutheniumTetroxideHot = material("ruthenium_tetroxide_hot", "热四氧化钌")
                .gas()
                .color(0x9b9b9b).iconSet(ROUGH)
                .buildAndRegister();

        SodiumSulfate = material("sodium_sulfate", "硫酸钠")
                .dust()
                .components(Sodium, 2, Sulfur, 1, Oxygen, 4)
                .color(0xf9f6cf).iconSet(SAND)
                .buildAndRegister();

        MutatedLivingSolder = material("mutated_living_solder", "突变活性焊料")
                .fluid()
                .color(0xc18fcc).iconSet(METALLIC)
                .radioactiveHazard(1)
                .buildAndRegister();

        SuperMutatedLivingSolder = material("super_mutated_living_solder", "超突变活性焊料")
                .fluid()
                .color(0xb662ff).iconSet(METALLIC)
                .radioactiveHazard(2)
                .buildAndRegister();

        HexafluorideEnrichedNaquadahSolution = material("hexafluoride_enriched_naquadah_solution", "六氟化富集硅岩溶液")
                .fluid()
                .color(0x868d7f)
                .components(NaquadahEnriched, 1, Fluorine, 6)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        XenonHexafluoroEnrichedNaquadate = material("xenon_hexafluoro_enriched_naquadate", "六氟氙酸富集硅岩")
                .fluid()
                .color(0x255a55)
                .components(Xenon, 1, NaquadahEnriched, 1, Fluorine, 6)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GoldTrifluoride = material("gold_trifluoride", "三氟化金")
                .dust()
                .color(0xe8c478)
                .iconSet(BRIGHT)
                .components(Gold, 1, Fluorine, 3)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        XenoauricFluoroantimonicAcid = material("xenoauric_fluoroantimonic_acid", "氟锑酸二氙")
                .fluid(FluidStorageKeys.LIQUID, new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0xe0bd74)
                .components(Xenon, 1, Gold, 1, Antimony, 1, Fluorine, 6)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GoldChloride = material("gold_chloride", "氯化金")
                .fluid()
                .color(0xcccc66)
                .components(Gold, 2, Chlorine, 6)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        BromineTrifluoride = material("bromine_trifluoride", "三氟化溴")
                .fluid()
                .color(0xa88e57)
                .components(Bromine, 1, Fluorine, 3)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HexafluorideNaquadriaSolution = material("hexafluoride_naquadria_solution", "六氟化超能硅岩溶液")
                .fluid()
                .color(0x25c213)
                .components(Naquadria, 1, Fluorine, 6)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        RadonDifluoride = material("radon_difluoride", "二氟化氡")
                .fluid()
                .color(0x8b7eff)
                .components(Radon, 1, Fluorine, 2)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        RadonNaquadriaOctafluoride = material("radon_naquadria_octafluoride", "八氟超能硅岩酸氡")
                .fluid()
                .color(0x85f378)
                .components(Radon, 1, Naquadria, 1, Fluorine, 8)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CaesiumFluoride = material("caesium_fluoride", "氟化铯")
                .fluid()
                .color(0xff7a5f)
                .components(Caesium, 1, Fluorine, 1)
                .buildAndRegister();

        XenonTrioxide = material("xenon_trioxide", "三氧化氙")
                .fluid()
                .color(0x359fc3)
                .components(Xenon, 1, Oxygen, 3)
                .buildAndRegister();

        CaesiumXenontrioxideFluoride = material("caesium_xenontrioxide_fluoride", "二氟三氧氙酸铯")
                .fluid()
                .color(0x5067d7)
                .components(Caesium, 1, Xenon, 1, Oxygen, 3, Fluorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        NaquadriaCaesiumXenonnonfluoride = material("naquadria_caesium_xenonnonfluoride", "九氟氙酸超能硅岩铯")
                .fluid()
                .color(0x54c248)
                .components(Naquadria, 1, Caesium, 1, Xenon, 1, Fluorine, 9)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        RadonTrioxide = material("radon_trioxide", "三氧化氡")
                .fluid()
                .color(0x9a6dd7)
                .components(Radon, 1, Oxygen, 3)
                .buildAndRegister();

        NaquadriaCaesiumfluoride = material("naquadria_caesiumfluoride", "二氟超能硅岩酸铯")
                .fluid()
                .color(0xaaeb69)
                .components(Naquadria, 1, Fluorine, 3, Caesium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister().setFormula("*Nq*F2CsF");

        NitrosoniumOctafluoroxenate = material("nitrosonium_octafluoroxenate", "八氟氙酸亚硝酰")
                .fluid()
                .color(0x953d9f)
                .components(NitrogenDioxide, 2, Xenon, 1, Fluorine, 8)
                .buildAndRegister().setFormula("(NO2)2XeF8");

        NitrylFluoride = material("nitryl_fluoride", "硝酰氟")
                .fluid()
                .color(0x8b7eff)
                .components(Nitrogen, 1, Oxygen, 2, Fluorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AcidicNaquadriaCaesiumfluoride = material("acidic_naquadria_caesiumfluoride", "硫酸二氟超能硅岩酸铯")
                .fluid()
                .color(0x75eb00)
                .components(Naquadria, 1, Fluorine, 3, Caesium, 1, Sulfur, 2, Oxygen, 8)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister().setFormula("*Nq*F2CsF(SO4)2");

        SupercriticalSteam = material("supercritical_steam", "超临界蒸汽")
                .gas(new FluidBuilder().temperature(1000))
                .color(0xc4c4c4)
                .components(Water, 1)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        WaterAgarMix = material("water_agar_mix", "琼脂水溶液")
                .fluid()
                .color(0x88ffc0)
                .buildAndRegister();

        TungstenTrioxide = material("tungsten_trioxide", "三氧化钨")
                .dust()
                .components(Tungsten, 1, Oxygen, 3)
                .color(0x86ff75).iconSet(ROUGH)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TriniumCompound = material("trinium_compound", "凯金化合物")
                .dust()
                .ore()
                .components(Trinium, 3, Actinium, 3, Selenium, 4, Astatine, 4)
                .color(0x675989).iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CrystallineNitricAcid = material("crystalline_nitric_acid", "结晶硝酸")
                .dust()
                .components(Hydrogen, 1, Nitrogen, 1, Oxygen, 3)
                .color(0xcdbd14)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SodiumChlorate = material("sodium_chlorate", "氯酸钠")
                .dust()
                .components(Sodium, 1, Chlorine, 1, Oxygen, 3)
                .color(0xa5a5a5)
                .buildAndRegister();

        SodiumPerchlorate = material("sodium_perchlorate", "高氯酸钠")
                .dust()
                .components(Sodium, 1, Chlorine, 1, Oxygen, 4)
                .color(0xf0f0f0)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ActiniumTriniumHydroxides = material("actinium_trinium_hydroxides", "氢氧化锕凯金")
                .dust()
                .color(0xad47cf).iconSet(ROUGH)
                .buildAndRegister().setFormula("Ke3Ac2(OH)12");

        SeleniumOxide = material("selenium_oxide", "二氧化硒")
                .dust()
                .components(Selenium, 1, Oxygen, 2)
                .color(0xfff71c).iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TriniumTetrafluoride = material("trinium_tetrafluoride", "四氟化凯金")
                .dust()
                .color(0x529e57)
                .components(Trinium, 1, Fluorine, 4)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Fluorocarborane = material("fluorocarborane", "碳氟化合物")
                .dust()
                .components(Hydrogen, 1, Carbon, 1, Hydrogen, 1, Boron, 11, Fluorine, 11)
                .color(0x00ec80).iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CaesiumNitrate = material("caesium_nitrate", "硝酸铯")
                .dust()
                .components(Caesium, 1, Nitrogen, 1, Oxygen, 3)
                .color(0x821ec7).iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CesiumCarborane = material("cesium_carborane", "碳酸铯")
                .dust()
                .components(Caesium, 1, Carbon, 1, Boron, 11, Hydrogen, 12)
                .color(0xae6eda).iconSet(SAND)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SilverIodide = material("silver_iodide", "碘化银")
                .dust()
                .components(Silver, 1, Iodine, 1)
                .color(0xaca56a).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SilverNitrate = material("silver_nitrate", "硝酸银")
                .dust()
                .components(Silver, 1, Nitrogen, 1, Oxygen, 3)
                .color(0xfffce0).iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TrifluoroaceticPhosphateEster = material("trifluoroacetic_phosphate_ester", "三氟乙酸对磷脂")
                .dust()
                .components(Carbon, 8, Hydrogen, 5, Fluorine, 3, Oxygen, 2, Sulfur, 1)
                .color(0xa99f45).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        RadiumNitrate = material("radium_nitrate", "硝酸镭")
                .dust()
                .color(0xcd40da).iconSet(SAND)
                .buildAndRegister().setFormula("Ra(NO3)2");

        ActiniumNitrate = material("actinium_nitrate", "硝酸锕")
                .dust()
                .color(0xdae0ee).iconSet(DULL)
                .buildAndRegister().setFormula("Ac(NO3)3");

        PotassiumFluoride = material("potassium_fluoride", "氟化钾")
                .dust()
                .components(Potassium, 1, Fluorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xb9b9b9).iconSet(BRIGHT)
                .buildAndRegister();

        SodiumHydride = material("sodium_hydride", "氢化钠")
                .dust()
                .components(Sodium, 1, Hydrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x757475).iconSet(DULL)
                .buildAndRegister();

        CesiumCarboranePrecursor = material("cesium_carborane_precursor", "铯烷预固化剂")
                .dust()
                .color(0xba5c69).iconSet(DULL)
                .buildAndRegister().setFormula("CsB10H12CN(CH3)3Cl");

        LithiumAluminiumHydride = material("lithium_aluminium_hydride", "氢化铝锂")
                .dust()
                .components(Lithium, 1, Aluminium, 1, Hydrogen, 1)
                .color(0xabd7df).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        LithiumAluminiumFluoride = material("lithium_aluminium_fluoride", "铝-锂氟化物")
                .dust()
                .components(Aluminium, 1, Fluorine, 4, Lithium, 1)
                .color(0xad1f58).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AluminiumTrifluoride = material("aluminium_trifluoride", "氟化铝")
                .dust()
                .components(Aluminium, 1, Fluorine, 3)
                .color(0xb8601a).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SodiumAluminiumHydride = material("sodium_aluminium_hydride", "氢化铝钠")
                .dust()
                .components(Sodium, 1, Aluminium, 1, Hydrogen, 4)
                .color(0x87bfbf).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AluminiumHydride = material("aluminium_hydride", "氢化铝")
                .dust()
                .components(Aluminium, 1, Hydrogen, 3)
                .color(0x315c6e).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Alumina = material("alumina", "氧化铝")
                .dust()
                .components(Aluminium, 2, Oxygen, 3)
                .color(0x1d4759).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CaesiumHydroxide = material("caesium_hydroxide", "氢氧化铯")
                .dust()
                .components(Caesium, 1, Oxygen, 1, Hydrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xd7d7d7).iconSet(DULL)
                .buildAndRegister();

        Decaborane = material("decaborane", "癸硼烷")
                .dust()
                .components(Boron, 10, Hydrogen, 14)
                .color(0x95b78f).iconSet(SAND)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SodiumTetrafluoroborate = material("sodium_tetrafluoroborate", "四氟硼酸钠")
                .dust()
                .components(Sodium, 1, Boron, 1, Fluorine, 4)
                .color(0xa6640f).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SodiumBorohydride = material("sodium_borohydride", "硼氢化钠")
                .dust()
                .components(Sodium, 1, Boron, 1, Hydrogen, 4)
                .color(0x9ab0b2).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        PhosphorusPentasulfide = material("phosphorus_pentasulfide", "五硫化磷")
                .dust()
                .components(Phosphorus, 4, Sulfur, 10)
                .color(0xe7a123).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        StoneDustResidue = material("stone_dust_residue", "石头粉残渣")
                .dust()
                .color(0x585858).iconSet(DULL)
                .buildAndRegister();

        AmmoniumBifluoride = material("ammonium_bifluoride", "二氟化铵")
                .dust()
                .components(Nitrogen, 1, Hydrogen, 4, Hydrogen, 1, Fluorine, 2)
                .color(0x26c6bb).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        UncommonResidues = material("uncommon_residues", "精良残渣")
                .dust()
                .color(0x3f46bd).iconSet(SAND)
                .buildAndRegister();

        PartiallyOxidizedResidues = material("partially_oxidized_residues", "待分离氧化金属残渣")
                .dust()
                .color(0x8f1515).iconSet(SAND)
                .buildAndRegister();

        InertResidues = material("inert_residues", "纯净残渣")
                .dust()
                .color(0x4f4863).iconSet(BRIGHT)
                .buildAndRegister();

        OxidizedResidues = material("oxidized_residues", "氧化残渣")
                .dust()
                .color(0x330d4a).iconSet(SAND)
                .buildAndRegister();

        HeavyOxidizedResidues = material("heavy_oxidized_residues", "重氧化残渣")
                .dust()
                .color(0x310d48).iconSet(SAND)
                .buildAndRegister();

        CleanInertResidues = material("clean_inert_residues", "纯净的惰性残渣")
                .dust()
                .color(0x187f4d).iconSet(BRIGHT)
                .buildAndRegister();

        MetallicResidues = material("metallic_residues", "金属残渣")
                .dust()
                .color(0x205a53).iconSet(SAND)
                .buildAndRegister();

        HeavyMetallicResidues = material("heavy_metallic_residues", "重金属残渣")
                .dust()
                .color(0x1c0986).iconSet(SAND)
                .buildAndRegister();

        DiamagneticResidues = material("diamagnetic_residues", "抗磁性残渣")
                .dust()
                .color(0x30845e).iconSet(SAND)
                .buildAndRegister();

        ParamagneticResidues = material("paramagnetic_residues", "顺磁残渣")
                .dust()
                .color(0x25788b).iconSet(SAND)
                .buildAndRegister();

        FerromagneticResidues = material("ferromagnetic_residues", "铁磁性残渣")
                .dust()
                .color(0x1f5d46).iconSet(SAND)
                .buildAndRegister();

        HeavyDiamagneticResidues = material("heavy_diamagnetic_residues", "重磁残渣")
                .dust()
                .color(0x22316a).iconSet(SAND)
                .buildAndRegister();

        HeavyParamagneticResidues = material("heavy_paramagnetic_residues", "重顺磁残渣")
                .dust()
                .color(0x1a8e2f).iconSet(SAND)
                .buildAndRegister();

        HeavyFerromagneticResidues = material("heavy_ferromagnetic_residues", "重铁磁性残渣")
                .dust()
                .color(0x26743e).iconSet(SAND)
                .buildAndRegister();

        ExoticHeavyResidues = material("exotic_heavy_residues", "重奇异残渣")
                .dust()
                .color(0x3e8496).iconSet(BRIGHT)
                .buildAndRegister();

        FumingNitricAcid = material("fuming_nitric_acid", "发烟硝酸")
                .fluid()
                .components(Hydrogen, 1, Nitrogen, 1, Oxygen, 3)
                .color(0xb46800).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Perfluorobenzene = material("perfluorobenzene", "全氟苯")
                .fluid()
                .components(Carbon, 6, Fluorine, 6)
                .color(0x15752B).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Trimethylsilane = material("trimethylsilane", "三甲基硅烷")
                .fluid()
                .components(Carbon, 3, Hydrogen, 10, Silicon, 1)
                .color(0x7d2fc3).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Trimethylchlorosilane = material("trimethylchlorosilane", "三甲基氯硅烷")
                .fluid()
                .color(0x591399).iconSet(LIMPID)
                .buildAndRegister().setFormula("(CH3)3SiCl");

        NitratedTriniiteCompoundSolution = material("nitrated_triniite_compound_solution", "硝化凯金化合物溶液")
                .fluid()
                .color(0x5e9699).iconSet(LIMPID)
                .buildAndRegister();

        ResidualTriniiteSolution = material("residual_triniite_solution", "残留凯金化合物溶液")
                .fluid()
                .color(0x68b59).iconSet(LIMPID)
                .buildAndRegister();

        ActiniumRadiumHydroxideSolution = material("actinium_radium_hydroxide_solution", "氢氧化锕镭溶液")
                .fluid()
                .color(0xc3e0dc).iconSet(LIMPID)
                .buildAndRegister();

        ActiniumRadiumNitrateSolution = material("actinium_radium_nitrate_solution", "硝酸锕镭溶液")
                .fluid()
                .color(0x89c0b3).iconSet(LIMPID)
                .buildAndRegister();

        HeavilyFluorinatedTriniumSolution = material("heavily_fluorinated_trinium_solution", "重氟化凯金化合物溶液")
                .fluid()
                .color(0x169a33).iconSet(LIMPID)
                .buildAndRegister();

        EthyleneSulfide = material("ethylene_sulfide", "乙硫酮")
                .fluid()
                .components(Carbon, 6, Hydrogen, 6, Sulfur, 1, Oxygen, 1)
                .color(0x868544).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        EthylTrifluoroacetate = material("ethyl_trifluoroacetate", "三氟乙酸乙酯")
                .fluid()
                .components(Carbon, 4, Hydrogen, 5, Fluorine, 3, Oxygen, 2)
                .color(0x93a658).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AcetylChloride = material("acetyl_chloride", "乙酰氯")
                .fluid()
                .components(Carbon, 2, Hydrogen, 3, Oxygen, 1, Chlorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xd1b117)
                .iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        KryptonDifluoride = material("krypton_difluoride", "二氟化氪")
                .fluid()
                .components(Krypton, 1, Fluorine, 2)
                .color(0x3ff12b).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        MoltenCalciumSalts = material("molten_calcium_salts", "熔融钙盐")
                .fluid()
                .color(0x7f478b)
                .iconSet(DULL)
                .buildAndRegister();

        Trimethylamine = material("trimethylamine", "三甲胺")
                .fluid()
                .color(0xcea67d).iconSet(LIMPID)
                .buildAndRegister().setFormula("(CH3)3N");

        BoraneDimethylSulfide = material("borane_dimethyl_sulfide", "硼烷二甲基硫醚")
                .fluid()
                .color(0xcea67d).iconSet(LIMPID)
                .buildAndRegister().setFormula("(BH3)(CH3)2S");

        Tetrahydrofuran = material("tetrahydrofuran", "四氢呋喃")
                .fluid()
                .color(0x86e4ce).iconSet(LIMPID)
                .buildAndRegister().setFormula("(CH2)4O");

        Diborane = material("diborane", "乙硼烷")
                .fluid()
                .color(0xbeebcd).iconSet(LIMPID)
                .buildAndRegister().setFormula("(BH3)2");

        DiethylEther = material("diethyl_ether", "二乙醚")
                .fluid()
                .color(0x9ab0b2).iconSet(LIMPID)
                .buildAndRegister().setFormula("(C2H5)2O");

        BoronTrifluorideAcetate = material("boron_trifluoride_acetate", "三氟化硼乙酸酯")
                .fluid()
                .color(0x991062).iconSet(LIMPID)
                .buildAndRegister().setFormula("(BF3)(C2H5)2O");

        SodiumHexafluoroaluminate = material("sodium_hexafluoroaluminate", "六氟铝酸钠")
                .fluid()
                .components(Sodium, 3, Aluminium, 1, Fluorine, 6)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xa47732).iconSet(LIMPID)
                .buildAndRegister();

        DirtyHexafluorosilicicAcid = material("dirty_hexafluorosilicic_acid", "污浊的六氟硅酸")
                .fluid()
                .color(0xda387d).iconSet(LIMPID)
                .buildAndRegister().setFormula("H2SiF6?");

        DiluteHexafluorosilicicAcid = material("dilute_hexafluorosilicic_acid", "稀六氟硅酸")
                .fluid()
                .color(0x49df68).iconSet(LIMPID)
                .buildAndRegister().setFormula("(H2O)2(H2SiF6)");

        FluorosilicicAcid = material("fluorosilicic_acid", "六氟硅酸")
                .fluid()
                .components(Hydrogen, 2, Silicon, 1, Fluorine, 6)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x49BF61).iconSet(LIMPID)
                .buildAndRegister();

        AmmoniumFluoride = material("ammonium_fluoride", "氟化铵")
                .fluid()
                .components(Nitrogen, 1, Hydrogen, 4, Fluorine, 1)
                .color(0xB80926).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AmmoniumBifluorideSolution = material("ammonium_bifluoride_solution", "氟氢化氨")
                .fluid()
                .color(0xCBC5B7).iconSet(LIMPID)
                .buildAndRegister().setFormula("(H2O)NH4FHF");

        SodiumHydroxideSolution = material("sodium_hydroxide_solution", "氢氧化钠溶液")
                .fluid()
                .color(0x000791).iconSet(LIMPID)
                .buildAndRegister().setFormula("(H2O)NaOH");

        RedMud = material("red_mud", "赤泥")
                .fluid()
                .color(0x972903).iconSet(LIMPID)
                .buildAndRegister().setFormula("HCl?", false);

        NeutralisedRedMud = material("neutralised_red_mud", "中和赤泥")
                .fluid()
                .color(0x972903).iconSet(LIMPID)
                .buildAndRegister().setFormula("Fe??", false);

        RedSlurry = material("red_slurry", "赤泥浆液")
                .fluid()
                .color(0x982902).iconSet(LIMPID)
                .buildAndRegister().setFormula("TiO2?");

        FerricReeChloride = material("ferric_ree_chloride", "含稀土氯化铁")
                .fluid()
                .color(0x68680d).iconSet(LIMPID)
                .buildAndRegister().setFormula("Fe?", false);

        TitanylSulfate = material("titanyl_sulfate", "硫酸钛酯")
                .fluid()
                .color(0xf82296).iconSet(LIMPID)
                .buildAndRegister().setFormula("TiO(SO4)");

        DioxygenDifluoride = material("dioxygen_difluoride", "二氟化二氧")
                .fluid()
                .components(Fluorine, 1, Oxygen, 1, Oxygen, 1, Fluorine, 1)
                .color(0x18bfb9).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        OxidizedResidualSolution = material("oxidized_residual_solution", "氧化残留溶液")
                .fluid()
                .color(0x2cd3cb).iconSet(LIMPID)
                .buildAndRegister();

        HeliumIIIHydride = material("helium_iii_hydride", "氢化氦-3")
                .fluid()
                .color(0x9f9f02).iconSet(LIMPID)
                .buildAndRegister().setFormula("He_3H");

        UltraacidicResidueSolution = material("ultraacidic_residue_solution", "超酸性残渣溶液")
                .fluid()
                .color(0x848c9a).iconSet(LIMPID)
                .buildAndRegister();

        XenicAcid = material("xenic_acid", "氙酸")
                .fluid()
                .components(Hydrogen, 2, Xenon, 1, Oxygen, 4)
                .color(0x443a76).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        DiluteHydrofluoricAcid = material("dilute_hydrofluoric_acid", "稀释氢氟酸")
                .fluid()
                .color(0x049821).iconSet(LIMPID)
                .buildAndRegister().setFormula("(H2O)(HF)");

        TritiumHydride = material("tritium_hydride", "氢化氚")
                .fluid()
                .components(Hydrogen, 1, Tritium, 1)
                .color(0xba0202).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        DustyLiquidHeliumIII = material("dusty_liquid_helium_iii", "污浊的液氦-3")
                .fluid()
                .components(Concrete, 1, Helium3, 1)
                .color(0x774060).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Ozone = material("ozone", "臭氧")
                .fluid()
                .components(Oxygen, 3)
                .color(0x0176c4).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HydrogenPeroxide = material("hydrogen_peroxide", "过氧化氢")
                .fluid()
                .components(Hydrogen, 2, Oxygen, 2)
                .color(0xc8ffff).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        RareEarthChlorides = material("rare_earth_chlorides", "稀土氯化物")
                .fluid()
                .components(Concrete, 1, Chlorine, 1)
                .color(0xbdb76b).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Fluorite = material("fluorite", "氟石")
                .fluid()
                .dust()
                .components(Calcium, 1, Fluorine, 2)
                .color(0x18b400).iconSet(DULL)
                .buildAndRegister();

        // wanggugu'sLanthanidetreatment
        SamariumRefinedPowder = material("samarium_refined_powder", "钐精")
                .dust()
                .color(0x8a6d7d)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("??Sm??", false);

        SamariumRrareEearthTurbidLiquid = material("samarium_rare_earth_turbid_liquid", "钐稀土浊液")
                .fluid()
                .color(0xcfc883)
                .iconSet(LIMPID)
                .buildAndRegister();

        MonaziteRareEarthTurbidLiquid = material("monazite_rare_earth_turbid_liquid", "独居石稀土混浊液")
                .fluid()
                .color(0x81624c)
                .iconSet(LIMPID)
                .buildAndRegister();

        ThoritePowder = material("thorite_powder", "方钍石")
                .dust()
                .components(Thorium, 1, Silicon, 1, Oxygen, 4)
                .color(0x7d7d7d)
                .iconSet(SAND)
                .buildAndRegister();

        DilutedMonaziteSlurry = material("diluted_monazite_slurry", "稀释独居石稀土泥浆")
                .fluid()
                .color(0xbdbdbd)
                .iconSet(LIMPID)
                .buildAndRegister();

        RedZirconPowder = material("red_zircon_powder", "红锆石")
                .dust()
                .components(Zirconium, 1, Silicon, 1, Oxygen, 4)
                .color(0xff4500)
                .iconSet(SAND)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        MonaziteSulfatePowder = material("monazite_sulfate_powder", "硫酸独居石")
                .dust()
                .color(0xcccccc)
                .iconSet(SAND)
                .buildAndRegister();

        AcidicMonazitePowder = material("acidic_monazite_powder", "酸性独居石")
                .dust()
                .color(0x9acd32)
                .iconSet(SAND)
                .buildAndRegister();

        ThoriumPhosphateRefinedPowder = material("thorium_phosphate_refined_powder", "磷酸钍精")
                .dust()
                .color(0x545454)
                .iconSet(SAND)
                .buildAndRegister().setFormula("??ThP??", false);

        MonaziteRareEarthFilterResiduePowder = material("monazite_rare_earth_filter_residue_powder", "独居石稀土滤渣")
                .dust()
                .color(0x7f7f7f)
                .iconSet(SAND)
                .buildAndRegister();

        NeutralizedMonaziteRareEarthFilterResiduePowder = material("neutralized_monazite_rare_earth_filter_residue_powder", "中和独居石稀土滤渣")
                .dust()
                .color(0x969696)
                .iconSet(SAND)
                .buildAndRegister();

        UraniumFilterResiduePowder = material("uranium_filter_residue_powder", "铀滤渣")
                .dust()
                .color(0x545454)
                .iconSet(SAND)
                .buildAndRegister();

        NeutralizedUraniumFilterResiduePowder = material("neutralized_uranium_filter_residue_powder", "中和铀滤渣")
                .dust()
                .color(0x7f7f7f)
                .iconSet(SAND)
                .buildAndRegister();

        ConcentratedMonaziteRareEarthHydroxidePowder = material("concentrated_monazite_rare_earth_hydroxide_powder", "浓缩独居石稀土氢氧化物")
                .dust()
                .color(0xc0c0c0)
                .iconSet(SAND)
                .buildAndRegister();

        ConcentratedNitrideMonaziteRareEarthSolution = material("concentrated_nitride_monazite_rare_earth_solution", "浓缩氮化独居石稀土溶液")
                .fluid()
                .color(0xadd8e6)
                .iconSet(LIMPID)
                .buildAndRegister();

        CeriumRichMixturePowder = material("cerium_rich_mixture_powder", "富铈混合物")
                .dust()
                .color(0x808080)
                .iconSet(SAND)
                .buildAndRegister().setFormula("??Ce??", false);

        CeriumChloridePowder = material("cerium_chloride_powder", "氯化铈")
                .dust()
                .components(Cerium, 1, Chlorine, 3)
                .color(0x808080)
                .iconSet(SAND)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        OxalicAcid = material("oxalic_acid", "草酸")
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .components(Carbon, 2, Hydrogen, 2, Oxygen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xa0ffa0)
                .iconSet(LIMPID)
                .buildAndRegister();

        VanadiumPentoxidePowder = material("vanadium_pentoxide_powder", "五氧化二钒")
                .dust()
                .components(Vanadium, 2, Oxygen, 5)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x8b0000)
                .iconSet(SAND)
                .buildAndRegister();

        CeriumOxalatePowder = material("cerium_oxalate_powder", "草酸铈")
                .dust()
                .components(Cerium, 1, Carbon, 2, Hydrogen, 2, Oxygen, 4)
                .color(0x969696)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(SAND)
                .buildAndRegister();

        ConcentratedCeriumChlorideSolution = material("concentrated_cerium_chloride_solution", "氯化铈浓缩液")
                .fluid()
                .components(Cerium, 1, Chlorine, 3)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x00ffff)
                .iconSet(LIMPID)
                .buildAndRegister();

        NitricLeachateFromMonazite = material("nitric_leachate_from_monazite", "硝酸独居石浸出混合物")
                .fluid()
                .color(0xadd8e6)
                .iconSet(LIMPID)
                .buildAndRegister();

        ConcentratedNitricLeachateFromMonazite = material("concentrated_nitric_leachate_from_monazite", "浓缩硝酸独居石浸出溶液")
                .fluid()
                .color(0x00ffff)
                .iconSet(LIMPID)
                .buildAndRegister();

        CoolingConcentratedNitricMonaziteRareEarthPowder = material("cooling_concentrated_nitric_monazite_rare_earth_powder", "冷却浓缩硝酸独居石稀土")
                .dust()
                .color(0xc0c0c0)
                .iconSet(SAND)
                .buildAndRegister();

        MonaziteRareEarthPrecipitatePowder = material("monazite_rare_earth_precipitate_powder", "独居石罕土沉淀")
                .dust()
                .color(0x808080)
                .iconSet(SAND)
                .buildAndRegister();

        HeterogeneousHalideMonaziteRareEarthMixturePowder = material("heterogeneous_halide_monazite_rare_earth_mixture_powder", "异质卤化独居石稀土混合物")
                .dust()
                .color(0x808080)
                .iconSet(SAND)
                .buildAndRegister();

        SaturatedMonaziteRareEarthPowder = material("saturated_monazite_rare_earth_powder", "饱和独居石稀土")
                .dust()
                .color(0xffffff)
                .iconSet(SAND)
                .buildAndRegister();

        SamariumPrecipitatePowder = material("samarium_precipitate_powder", "钐沉淀")
                .dust()
                .components(Samarium, 2, Gadolinium, 1)
                .color(0x969696)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(SAND)
                .buildAndRegister();

        ConcentratedRareEarthChlorideSolution = material("concentrated_rare_earth_chloride_solution", "氯化稀土浓缩液")
                .fluid()
                .color(0x00bfff)
                .iconSet(LIMPID)
                .buildAndRegister();

        EnrichedRareEarthChlorideSolution = material("enriched_rare_earth_chloride_solution", "氯化稀土富集液")
                .fluid()
                .color(0x87cefa)
                .iconSet(LIMPID)
                .buildAndRegister();

        DilutedRareEarthChlorideSolution = material("diluted_rare_earth_chloride_solution", "氯化稀土稀释液")
                .fluid()
                .color(0xadd8e6)
                .iconSet(LIMPID)
                .buildAndRegister();

        SamariumRareEarthSlurry = material("samarium_rare_earth_slurry", "钐稀土泥浆")
                .fluid()
                .color(0x808080)
                .iconSet(LIMPID)
                .buildAndRegister();

        NeodymiumRareEarthConcentratePowder = material("neodymium_rare_earth_concentrate_powder", "钕稀土精")
                .dust()
                .color(0x969696)
                .iconSet(SAND)
                .buildAndRegister();

        SamariumRareEarthDilutedSolution = material("samarium_rare_earth_diluted_solution", "钐稀土稀释液")
                .fluid()
                .components(Samarium, 1, Chlorine, 1, Water, 2)
                .color(0xa9a9a9)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(LIMPID)
                .buildAndRegister();

        SamariumOxalateWithImpurities = material("samarium_oxalate_with_impurities", "含杂草酸钐")
                .dust()
                .components(Samarium, 1, Carbon, 2, Oxygen, 6)
                .color(0x808080)
                .iconSet(SAND)
                .buildAndRegister();

        SamariumChlorideWithImpurities = material("samarium_chloride_with_impurities", "含杂氯化钐")
                .dust()
                .fluid()
                .components(Samarium, 1, Chlorine, 3)
                .color(0xb0b0b0)
                .iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SamariumChlorideSodiumChlorideMixturePowder = material("samarium_chloride_sodium_chloride_mixture_powder", "氯化钐-氯化钠混合物")
                .dust()
                .components(Samarium, 1, Chlorine, 3, Sodium, 1, Chlorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xc0c0c0)
                .iconSet(SAND)
                .buildAndRegister();

        PhosphorusFreeSamariumConcentratePowder = material("phosphorus_free_samarium_concentrate_powder", "脱磷钐精")
                .dust()
                .components(Samarium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xd3d3d3)
                .iconSet(SAND)
                .buildAndRegister();

        SamariumChlorideConcentrateSolution = material("samarium_chloride_concentrate_solution", "氯化钐浓缩液")
                .fluid()
                .components(Samarium, 1, Chlorine, 3, Water, 5)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xb0c4de)
                .iconSet(LIMPID)
                .buildAndRegister();

        LanthanumChloride = material("lanthanum_chloride", "氯化镧")
                .dust()
                .components(Lanthanum, 1, Chlorine, 3)
                .color(0xffffff)
                .iconSet(SAND)
                .buildAndRegister();

        LanthanumChlorideWithImpurities = material("lanthanum_chloride_with_impurities", "含杂氯化镧")
                .dust()
                .color(0xe0e0e0)
                .iconSet(SAND)
                .buildAndRegister();

        FluoroCarbonLanthanideCeriumSolution = material("fluoro_carbon_lanthanide_cerium_solution", "氟碳镧铈稀土浊溶液")
                .fluid()
                .color(0x3888d2)
                .iconSet(LIMPID)
                .buildAndRegister();

        SteamCrackedFluoroCarbonLanthanideSlurry = material("steam_cracked_fluoro_carbon_lanthanide_slurry", "蒸汽裂化氟碳镧铈泥浆")
                .fluid()
                .color(0x7f7fff)
                .iconSet(LIMPID)
                .buildAndRegister();

        ModulatedFluoroCarbonLanthanideSlurry = material("modulated_fluoro_carbon_lanthanide_slurry", "调制氟碳镧铈泥浆")
                .fluid()
                .color(0x5a5aff)
                .iconSet(LIMPID)
                .buildAndRegister();

        DilutedFluoroCarbonLanthanideSlurry = material("diluted_fluoro_carbon_lanthanide_slurry", "稀释氟碳镧铈泥浆")
                .fluid()
                .color(0x9696ff)
                .iconSet(LIMPID)
                .buildAndRegister();

        FluoroCarbonLanthanideCeriumOxidePowder = material("fluoro_carbon_lanthanide_cerium_oxide_powder", "氟碳镧铈稀土氧化物")
                .dust()
                .color(0x7f7f7f)
                .iconSet(BRIGHT)
                .buildAndRegister();

        AcidLeachedFluoroCarbonLanthanideCeriumOxidePowder = material("acid_leached_fluoro_carbon_lanthanide_cerium_oxide_powder", "酸浸氟碳镧铈稀土氧化物")
                .dust()
                .color(0x555555)
                .iconSet(BRIGHT)
                .buildAndRegister();

        CalcinedRareEarthOxidePowder = material("calcined_rare_earth_oxide_powder", "焙烧稀土氧化物")
                .dust()
                .color(0x969696)
                .iconSet(BRIGHT)
                .buildAndRegister();

        WetRareEarthOxidePowder = material("wet_rare_earth_oxide_powder", "湿稀土氧化物")
                .dust()
                .color(0x6699cc)
                .iconSet(BRIGHT)
                .buildAndRegister();

        CeriumOxideRareEarthOxidePowder = material("cerium_oxide_rare_earth_oxide_powder", "氧化铈稀土氧化物")
                .dust()
                .components(Cerium, 1, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xd2d2d2)
                .iconSet(BRIGHT)
                .buildAndRegister();

        FluoroCarbonLanthanideCeriumRareEarthOxidePowder = material("fluoro_carbon_lanthanide_cerium_rare_earth_oxide_powder", "氟碳镧铈罕土氧化物")
                .dust()
                .color(0x7f7f7f)
                .iconSet(BRIGHT)
                .buildAndRegister();

        NitridedFluoroCarbonLanthanideCeriumRareEarthOxideSolution = material("nitrided_fluoro_carbon_lanthanide_cerium_rare_earth_oxide_solution", "氮化氟碳镧铈罕土氧化物")
                .fluid()
                .color(0x9999ff)
                .iconSet(LIMPID)
                .buildAndRegister();

        FluoroCarbonLanthanideCeriumRareEarthSuspension = material("fluoro_carbon_lanthanide_cerium_rare_earth_suspension", "氟碳镧铈罕土氧化物悬浊液")
                .fluid()
                .color(0xbebebe)
                .iconSet(LIMPID)
                .buildAndRegister();

        SamariumRareEarthConcentratePowder = material("samarium_rare_earth_concentrate_powder", "钐稀土精")
                .dust()
                .color(0x808080)
                .iconSet(BRIGHT)
                .buildAndRegister();

        FluorinatedSamariumConcentratePowder = material("fluorinated_samarium_concentrate_powder", "氟化钐精")
                .dust()
                .color(0x969696)
                .iconSet(BRIGHT)
                .buildAndRegister();

        SamariumTerbiumMixturePowder = material("samarium_terbium_mixture_powder", "钐-铽混合物")
                .dust()
                .color(0x969696)
                .iconSet(BRIGHT)
                .buildAndRegister();

        NitridedSamariumTerbiumMixturePowder = material("nitrided_samarium_terbium_mixture_powder", "氮化钐-铽混合物")
                .dust()
                .color(0x7f7f7f)
                .iconSet(BRIGHT)
                .buildAndRegister();

        TerbiumNitratePowder = material("terbium_nitrate_powder", "硝酸铽")
                .dust()
                .color(0x0080ff)
                .components(Terbium, 1, Nitrogen, 1, Oxygen, 3)
                .iconSet(BRIGHT)
                .buildAndRegister();

        PromethiumOxide = material("promethium_oxide", "氧化钷")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Promethium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CarbonTetrachloride = material("carbon_tetrachloride", "四氯化碳")
                .fluid()
                .color(0x0e9000)
                .components(Carbon, 1, Chlorine, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(LIMPID)
                .buildAndRegister();

        ActiniumOxalate = material("actinium_oxalate", "草酸锕")
                .dust()
                .color(0xcfcfcf)
                .iconSet(GLASS)
                .buildAndRegister().setFormula("Ac(CO₂)₄", false);

        EthylHexanol = material("ethyl_hexanol", "2-乙基己醇")
                .fluid()
                .components(Carbon, 8, Hydrogen, 18, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xffffcc)
                .iconSet(LIMPID)
                .buildAndRegister();

        P507 = material("p507", "P-507（2-乙基己基膦酸单-2-乙基己基酯）")
                .fluid()
                .components(Carbon, 18, Hydrogen, 36, Phosphorus, 1, Oxygen, 3)
                .color(0xffff00)
                .iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();
    }
}
