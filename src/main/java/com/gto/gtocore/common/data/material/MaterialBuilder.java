package com.gto.gtocore.common.data.material;

import com.gto.gtocore.api.data.chemical.material.info.GTOMaterialFlags;
import com.gto.gtocore.api.data.chemical.material.info.GTOMaterialIconSet;
import com.gto.gtocore.api.item.tool.GTOToolType;
import com.gto.gtocore.common.data.GTOElements;
import com.gto.gtocore.config.GTOConfigHolder;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.ToolProperty;
import com.gregtechceu.gtceu.api.fluids.FluidBuilder;
import com.gregtechceu.gtceu.api.fluids.attribute.FluidAttributes;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;

import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty.GasTier.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gto.gtocore.common.data.GTOMaterials.*;

public class MaterialBuilder {

    public static void init() {
        // ae
        Fluix = builderMaterial("fluix")
                .gem()
                .components(NetherQuartz, 1, CertusQuartz, 1, Redstone, 1)
                .color(0x8f5ccb)
                .iconSet(QUARTZ)
                .flags(NO_SMASHING, NO_SMELTING, DISABLE_DECOMPOSITION, GENERATE_PLATE)
                .buildAndRegister();

        // ad
        Desh = builderMaterial("desh")
                .ingot()
                .ore()
                .element(GTOElements.DESH)
                .color(0xf2a057)
                .iconSet(METALLIC)
                .flags(NO_SMELTING, NO_ORE_SMELTING)
                .buildAndRegister();

        Ostrum = builderMaterial("ostrum")
                .ingot()
                .ore()
                .element(GTOElements.OSTRUM)
                .color(0xe5939b)
                .iconSet(METALLIC)
                .flags(NO_SMELTING, NO_ORE_SMELTING)
                .buildAndRegister();

        Calorite = builderMaterial("calorite")
                .ingot()
                .ore()
                .element(GTOElements.CALORITE)
                .color(0xe65757)
                .iconSet(METALLIC)
                .flags(NO_SMELTING, NO_ORE_SMELTING)
                .buildAndRegister();

        // eio
        CopperAlloy = builderMaterial("copper_alloy_ingot")
                .ingot()
                .color(0xc79738)
                .iconSet(SHINY)
                .flags(DISABLE_DECOMPOSITION)
                .components(Copper, 1, Silicon, 1)
                .buildAndRegister();

        EnergeticAlloy = builderMaterial("energetic_alloy")
                .ingot()
                .color(0xffb545)
                .iconSet(SHINY)
                .flags(DISABLE_DECOMPOSITION)
                .blastTemp(1650, LOW, GTValues.VA[GTValues.MV], 700)
                .components(Redstone, 1, Gold, 1, Glowstone, 1)
                .cableProperties(128, 1, 0, true)
                .buildAndRegister();

        VibrantAlloy = builderMaterial("vibrant_alloy")
                .ingot()
                .fluid()
                .color(0xa4ff70)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .blastTemp(2450, LOW, GTValues.VA[GTValues.MV], 900)
                .components(EnergeticAlloy, 1, EnderPearl, 1)
                .cableProperties(512, 1, 0, true)
                .buildAndRegister();

        RedstoneAlloy = builderMaterial("redstone_alloy")
                .ingot()
                .fluid()
                .color(0xf66565)
                .iconSet(SHINY)
                .flags(DISABLE_DECOMPOSITION)
                .components(Redstone, 1, Silicon, 1)
                .buildAndRegister();

        ConductiveAlloy = builderMaterial("conductive_alloy")
                .ingot()
                .fluid()
                .color(0xf7b29b)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .components(CopperAlloy, 1, Iron, 1, Redstone, 1)
                .cableProperties(32, 1, 0, true)
                .buildAndRegister();

        PulsatingAlloy = builderMaterial("pulsating_alloy")
                .ingot()
                .fluid()
                .color(0x6ae26e)
                .iconSet(SHINY)
                .flags(DISABLE_DECOMPOSITION)
                .components(Iron, 1, EnderPearl, 1)
                .cableProperties(8, 1, 0, true)
                .buildAndRegister();

        DarkSteel = builderMaterial("dark_steel")
                .ingot()
                .color(0x414751)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .blastTemp(1450, LOW, GTValues.VA[GTValues.MV], 600)
                .components(Iron, 1, Coal, 1, Obsidian, 1)
                .buildAndRegister();

        Soularium = builderMaterial("soularium")
                .ingot()
                .color(0x7c674d)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .components(Gold, 1, Concrete, 1)
                .buildAndRegister();

        EndSteel = builderMaterial("end_steel")
                .ingot()
                .color(0xd6d980)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .blastTemp(3250, LOW, GTValues.VA[GTValues.HV], 900)
                .cableProperties(2048, 1, 0, true)
                .components(Endstone, 1, DarkSteel, 1, Obsidian, 1)
                .buildAndRegister();

        BarnardaAir = builderMaterial("barnarda_air")
                .gas()
                .color(0x563a24)
                .iconSet(DULL)
                .buildAndRegister();

        AlienAlgae = builderMaterial("alien_algae")
                .ore()
                .addOreByproducts(Paper, Agar)
                .color(0x808000)
                .iconSet(WOOD)
                .buildAndRegister();

        Bloodstone = builderMaterial("bloodstone")
                .ore()
                .addOreByproducts(Deepslate, Redstone)
                .color(0xd80036)
                .iconSet(QUARTZ)
                .buildAndRegister();

        PerditioCrystal = builderMaterial("perditio_crystal")
                .dust()
                .color(0x656565)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?");

        EarthCrystal = builderMaterial("earth_crystal")
                .dust()
                .ore()
                .addOreByproducts(PerditioCrystal)
                .color(0x00f100)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?");

        IgnisCrystal = builderMaterial("ignis_crystal")
                .dust()
                .ore()
                .addOreByproducts(PerditioCrystal)
                .color(0xd90000)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?");

        InfusedGold = builderMaterial("infused_gold")
                .dust()
                .ore()
                .color(0xc99614)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("Au?");

        Thaumium = builderMaterial("thaumium")
                .ingot()
                .components(InfusedGold, 1)
                .color(0x54537e)
                .iconSet(DULL)
                .blastTemp(1000, LOW, GTValues.VA[GTValues.LV], 100)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FRAME, GENERATE_PLATE, GENERATE_ROD)
                .buildAndRegister();

        AstralSilver = builderMaterial("astral_silver")
                .dust()
                .fluid()
                .components(Silver, 2, Thaumium, 1)
                .color(0xd9d9f1)
                .iconSet(BRIGHT)
                .buildAndRegister();

        HighEnergyMixture = builderMaterial("high_energy_mixture")
                .dust()
                .color(0xdbd69c)
                .iconSet(SAND)
                .buildAndRegister();

        LuminEssence = builderMaterial("lumin_essence")
                .dust()
                .components(HighEnergyMixture, 1, PhosphoricAcid, 1)
                .color(0x838914)
                .iconSet(DULL)
                .buildAndRegister();

        Sunnarium = builderMaterial("sunnarium")
                .ingot()
                .fluid()
                .color(0xfcfc00)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?");

        UuAmplifier = builderMaterial("uu_amplifier")
                .fluid()
                .color(0xaa2b9f)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?");

        Celestine = builderMaterial("celestine")
                .ore()
                .color(0x3c4899)
                .components(Strontium, 1, Sulfur, 1, Oxygen, 4)
                .iconSet(EMERALD)
                .buildAndRegister();

        Zircon = builderMaterial("zircon")
                .ore()
                .color(0xde953c)
                .iconSet(EMERALD)
                .buildAndRegister().setFormula("ZrSiO₄");

        Jasper = builderMaterial("jasper")
                .gem()
                .ore()
                .addOreByproducts(Talc, Boron)
                .color(0xc85050)
                .iconSet(EMERALD)
                .buildAndRegister().setFormula("?");

        BismuthTellurite = builderMaterial("bismuth_tellurite")
                .dust()
                .components(Bismuth, 2, Tellurium, 3)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x004222)
                .iconSet(BRIGHT)
                .buildAndRegister();

        Prasiolite = builderMaterial("prasiolite")
                .dust()
                .components(Silicon, 5, Oxygen, 10, Iron, 1)
                .color(0x9eB749)
                .iconSet(BRIGHT)
                .buildAndRegister();

        CubicZirconia = builderMaterial("cubic_zirconia")
                .gem()
                .color(0xf3d5d7)
                .components(Zirconium, 1, Oxygen, 2)
                .iconSet(EMERALD)
                .flags(GENERATE_PLATE, NO_SMASHING, NO_SMELTING, EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES)
                .buildAndRegister();

        MagnetoResonatic = builderMaterial("magneto_resonatic")
                .gem()
                .color(0xff97ff)
                .components(Prasiolite, 3, BismuthTellurite, 6, CubicZirconia, 1, SteelMagnetic, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(MAGNETIC)
                .buildAndRegister();

        Adamantium = builderMaterial("adamantium")
                .ingot()
                .fluid()
                .plasma()
                .blastTemp(17700, HIGHER)
                .element(GTOElements.ADAMANTIUM)
                .color(0xefbe35)
                .iconSet(METALLIC)
                .flags(GENERATE_ROTOR, GENERATE_FRAME)
                .buildAndRegister();

        Quantanium = builderMaterial("quantanium")
                .ingot()
                .fluid()
                .blastTemp(12500, HIGHER)
                .element(GTOElements.QUANTANIUM)
                .color(0x0dff02)
                .iconSet(METALLIC)
                .flags(GENERATE_ROTOR, GENERATE_SMALL_GEAR, GENERATE_FRAME)
                .buildAndRegister();

        Vibranium = builderMaterial("vibranium")
                .ingot()
                .fluid()
                .plasma()
                .ore()
                .addOreByproducts(Plutonium239, Plutonium241)
                .blastTemp(18500, HIGHER)
                .element(GTOElements.VIBRANIUM)
                .color(0xff0000)
                .iconSet(METALLIC)
                .flags(GTOMaterialFlags.GENERATE_NANOSWARM, GENERATE_ROTOR, GENERATE_FRAME)
                .buildAndRegister();

        Indalloy140 = builderMaterial("indalloy_140")
                .ingot()
                .fluid()
                .color(0x6a5acd)
                .blastTemp(2600, LOW, GTValues.VA[GTValues.EV])
                .components(Bismuth, 47, Lead, 25, Tin, 13, Cadmium, 10, Indium, 5)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ArtheriumTin = builderMaterial("artherium_tin")
                .ingot()
                .fluid()
                .color(0x551a8b)
                .blastTemp(9800, HIGHER, GTValues.VA[GTValues.IV])
                .components(Tin, 12, Actinium, 7, EnrichedNaquadahTriniumEuropiumDuranide, 5, Caesium, 4, Osmiridium, 3)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Tairitsu = builderMaterial("tairitsu")
                .ingot()
                .fluid()
                .color(0x1c1c1c)
                .blastTemp(12100, HIGHER, GTValues.VA[GTValues.ZPM])
                .components(Tungsten, 8, Naquadria, 7, Trinium, 4, Carbon, 4, Vanadium, 3,
                        Plutonium239, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Draconium = builderMaterial("draconium")
                .ingot()
                .fluid()
                .blastTemp(19200)
                .element(GTOElements.DRACONIUM)
                .color(0xa300cc)
                .iconSet(RADIOACTIVE)
                .flags(GTOMaterialFlags.GENERATE_NANOSWARM, GENERATE_ROTOR, GENERATE_FRAME, NO_SMELTING)
                .buildAndRegister();

        Chaos = builderMaterial("chaos")
                .ingot()
                .liquid(new FluidBuilder().temperature(1000000).customStill())
                .plasma()
                .blastTemp(28000, HIGHEST)
                .element(GTOElements.CHAOS)
                .iconSet(DULL)
                .color(0x000000)
                .flags(GENERATE_FOIL)
                .buildAndRegister();

        Cosmic = builderMaterial("cosmic")
                .ingot()
                .color(0x2d3e5e)
                .iconSet(new MaterialIconSet("cosmic"))
                .flags(GENERATE_FINE_WIRE, GENERATE_LONG_ROD)
                .buildAndRegister();

        Hypogen = builderMaterial("hypogen")
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

        Shirabon = builderMaterial("shirabon")
                .ingot()
                .fluid()
                .color(0xc61361)
                .blastTemp(64000, HIGHEST, GTValues.VA[GTValues.OpV], 1200)
                .element(GTOElements.SHIRABON)
                .flags(GENERATE_FINE_WIRE)
                .iconSet(METALLIC)
                .buildAndRegister();

        Mithril = builderMaterial("mithril")
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

        Taranium = builderMaterial("taranium")
                .ingot()
                .fluid()
                .blastTemp(16200, HIGHEST, GTValues.VA[GTValues.UIV], 1440)
                .element(GTOElements.TARANIUM)
                .color(0x000033)
                .iconSet(RADIOACTIVE)
                .flags(GENERATE_SPRING, GENERATE_SPRING_SMALL)
                .cableProperties(GTValues.V[GTValues.UXV], 2, 64)
                .buildAndRegister();

        CrystalMatrix = builderMaterial("crystal_matrix")
                .ingot()
                .fluid()
                .plasma()
                .blastTemp(19600, HIGHEST)
                .element(GTOElements.CRYSTALMATRIX)
                .color(0x33ffff)
                .iconSet(RADIOACTIVE)
                .flags(GENERATE_SPRING, GENERATE_SPRING_SMALL)
                .cableProperties(GTValues.V[GTValues.OpV], 2, 128)
                .buildAndRegister();

        CosmicNeutronium = builderMaterial("cosmic_neutronium")
                .ingot()
                .liquid(new FluidBuilder().temperature(1000000).customStill())
                .blastTemp(24800, HIGHEST)
                .element(GTOElements.COSMICNEUTRONIUM)
                .color(0x000d1a)
                .iconSet(new MaterialIconSet("cosmic_neutronium", BRIGHT))
                .flags(GTOMaterialFlags.GENERATE_NANOSWARM, GENERATE_SPRING, GENERATE_FINE_WIRE,
                        GENERATE_SPRING_SMALL)
                .cableProperties(Integer.MAX_VALUE, 2, 128)
                .buildAndRegister();

        Echoite = builderMaterial("echoite")
                .ingot()
                .fluid()
                .plasma()
                .blastTemp(17300, HIGHER)
                .element(GTOElements.ECHOITE)
                .color(0x26734d)
                .iconSet(METALLIC)
                .flags(GENERATE_ROD, GENERATE_FINE_WIRE)
                .cableProperties(GTValues.V[GTValues.UIV], 32, 0, true)
                .toolStats(ToolProperty.Builder.of(6.0F, 100.0F, 64, 6, GTOToolType.VAJRA).magnetic()
                        .unbreakable().build())
                .buildAndRegister();

        Legendarium = builderMaterial("legendarium")
                .ingot()
                .fluid()
                .plasma()
                .blastTemp(21400, HIGHEST)
                .element(GTOElements.LEGENDARIUM)
                .color(0x00ffff)
                .iconSet(RADIOACTIVE)
                .flags(GENERATE_FINE_WIRE)
                .cableProperties(GTValues.V[GTValues.UXV], 64, 0, true)
                .buildAndRegister();

        AwakenedDraconium = builderMaterial("awakened_draconium")
                .ingot()
                .fluid()
                .plasma()
                .blastTemp(22600, HIGHEST)
                .element(GTOElements.AWAKENEDDRACONIUM)
                .color(0xcc6600)
                .iconSet(RADIOACTIVE)
                .flags(GENERATE_FINE_WIRE)
                .cableProperties(GTValues.V[GTValues.OpV], 64, 0, true)
                .buildAndRegister();

        Adamantine = builderMaterial("adamantine")
                .ingot()
                .fluid()
                .blastTemp(14400, HIGHER)
                .element(GTOElements.ADAMANTINE)
                .color(0xe6e600)
                .iconSet(METALLIC)
                .flags(GENERATE_FINE_WIRE)
                .cableProperties(GTValues.V[GTValues.UIV], 4, 128)
                .buildAndRegister();

        Starmetal = builderMaterial("starmetal")
                .ingot()
                .fluid()
                .plasma()
                .addOreByproducts(Sapphire, Polonium)
                .blastTemp(21800, HIGHEST)
                .element(GTOElements.STARMETAL)
                .color(0x0000e6)
                .iconSet(METALLIC)
                .flags(GTOMaterialFlags.GENERATE_NANOSWARM, GENERATE_FINE_WIRE)
                .cableProperties(GTValues.V[GTValues.OpV], 4, 256)
                .buildAndRegister();

        Orichalcum = builderMaterial("orichalcum")
                .ingot()
                .fluid()
                .plasma()
                .ore()
                .blastTemp(15300, HIGHER)
                .element(GTOElements.ORICHALCUM)
                .color(0xff78c9)
                .iconSet(METALLIC)
                .flags(GTOMaterialFlags.GENERATE_NANOSWARM, GENERATE_ROUND, GENERATE_ROTOR, GENERATE_GEAR,
                        GENERATE_SMALL_GEAR, GENERATE_LONG_ROD)
                .buildAndRegister();

        Infuscolium = builderMaterial("infuscolium")
                .ingot()
                .fluid()
                .plasma()
                .blastTemp(17500, HIGHER)
                .element(GTOElements.INFUSCOLIUM)
                .color(0xff77ff)
                .iconSet(RADIOACTIVE)
                .flags(GTOMaterialFlags.GENERATE_NANOSWARM, GENERATE_ROUND, GENERATE_ROTOR, GENERATE_GEAR,
                        GENERATE_SMALL_GEAR, GENERATE_LONG_ROD)
                .buildAndRegister();

        Enderium = builderMaterial("enderium")
                .ingot()
                .fluid()
                .plasma()
                .ore()
                .fluidPipeProperties(100000, 100000, true, true, true, true)
                .addOreByproducts(Endstone, EnderPearl)
                .blastTemp(16800, HIGHER)
                .element(GTOElements.ENDERIUM)
                .color(0x75ede2)
                .iconSet(METALLIC)
                .flags(GTOMaterialFlags.GENERATE_NANOSWARM, GENERATE_FINE_WIRE)
                .buildAndRegister();

        Eternity = builderMaterial("eternity")
                .ingot()
                .liquid(new FluidBuilder().customStill())
                .blastTemp(36000, null, GTValues.VA[GTValues.MAX], 3600)
                .element(GTOElements.ETERNITY)
                .iconSet(new MaterialIconSet("eternity"))
                .flags(GTOMaterialFlags.GENERATE_NANOSWARM, GENERATE_FOIL, GENERATE_FRAME)
                .buildAndRegister();

        Magmatter = builderMaterial("magmatter")
                .ingot()
                .liquid(new FluidBuilder().customStill())
                .element(GTOElements.MAGMATTER)
                .iconSet(new MaterialIconSet("magmatter"))
                .flags(GENERATE_LONG_ROD, NO_UNIFICATION)
                .buildAndRegister();

        DegenerateRhenium = builderMaterial("degenerate_rhenium")
                .dust()
                .plasma()
                .fluid()
                .color(0x4646ff)
                .element(GTOElements.DEGENERATE_REHENIUM)
                .iconSet(RADIOACTIVE)
                .flags(GENERATE_PLATE, NO_UNIFICATION)
                .buildAndRegister();

        HeavyQuarkDegenerateMatter = builderMaterial("heavy_quark_degenerate_matter")
                .ingot()
                .fluid()
                .plasma()
                .fluidPipeProperties(1000000, 1000000, true, true, true, true)
                .element(GTOElements.HEAVY_QUARK_DEGENERATE_MATTER)
                .blastTemp(178000, HIGHER)
                .color(0x52a733)
                .iconSet(BRIGHT)
                .flags(GENERATE_PLATE, GENERATE_FINE_WIRE)
                .buildAndRegister();

        MetastableHassium = builderMaterial("metastable_hassium")
                .plasma()
                .fluid()
                .components(Hassium, 1)
                .color(0x78766f)
                .iconSet(RADIOACTIVE)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Inconel625 = builderMaterial("inconel_625")
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

        HastelloyN75 = builderMaterial("hastelloy_n_75")
                .ingot()
                .fluid()
                .color(0x8b6914)
                .blastTemp(4550, HIGH, GTValues.VA[GTValues.EV])
                .components(Nickel, 15, Molybdenum, 9, Chromium, 4, Titanium, 2, Erbium, 2)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION, GENERATE_BOLT_SCREW, GENERATE_GEAR, GENERATE_SMALL_GEAR,
                        GENERATE_PLATE)
                .buildAndRegister();

        MetastableOganesson = builderMaterial("metastable_oganesson")
                .fluid()
                .color(0x8b000e)
                .components(Oganesson, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        QuantumChromoDynamicallyConfinedMatter = builderMaterial("quantum_chromo_dynamically_confined_matter")
                .ingot()
                .fluid()
                .plasma()
                .element(GTOElements.QUANTUM_CHROMO_DYNAMICALLY_CONFINED_MATTER)
                .blastTemp(13100, HIGHER)
                .color(0xd08c38)
                .iconSet(RADIOACTIVE)
                .flags(GENERATE_FRAME, GENERATE_PLATE)
                .buildAndRegister();

        TranscendentMetal = builderMaterial("transcendent_metal")
                .ingot()
                .fluid()
                .element(GTOElements.TRANSCENDENTMETAL)
                .color(0xffffff)
                .iconSet(GTOMaterialIconSet.CUSTOM_TRANSCENDENT_MENTAL)
                .flags(GTOMaterialFlags.GENERATE_NANOSWARM, GENERATE_ROUND, GENERATE_ROTOR, GENERATE_GEAR,
                        GENERATE_SMALL_GEAR, GENERATE_LONG_ROD)
                .buildAndRegister();

        Uruium = builderMaterial("uruium")
                .ingot()
                .fluid()
                .ore()
                .addOreByproducts(Europium)
                .blastTemp(14600, HIGHER, GTValues.VA[GTValues.UIV], 1200)
                .element(GTOElements.URUIUM)
                .color(0x87ceeb)
                .flags(GTOMaterialFlags.GENERATE_NANOSWARM)
                .iconSet(METALLIC)
                .cableProperties(Integer.MAX_VALUE, 16, 536870912)
                .buildAndRegister();

        MagnetohydrodynamicallyConstrainedStarMatter = builderMaterial("magneto_hydro_dynamically_con_strained_star_matter")
                .ingot()
                .liquid(new FluidBuilder().temperature(100).customStill())
                .element(GTOElements.RAW_STAR_MATTER)
                .iconSet(new MaterialIconSet("magneto_hydro_dynamically_con_strained_star_matter"))
                .flags(GENERATE_FRAME, GENERATE_FOIL, NO_UNIFICATION)
                .buildAndRegister();

        WhiteDwarfMatter = builderMaterial("white_dwarf_mtter")
                .ingot()
                .fluid()
                .element(GTOElements.STAR_MATTER)
                .color(0xffffff)
                .flags(GTOMaterialFlags.GENERATE_NANOSWARM, GENERATE_FINE_WIRE)
                .iconSet(BRIGHT)
                .buildAndRegister();

        BlackDwarfMatter = builderMaterial("black_dwarf_mtter")
                .ingot()
                .fluid()
                .element(GTOElements.STAR_MATTER)
                .color(0x000000)
                .flags(GTOMaterialFlags.GENERATE_NANOSWARM, GENERATE_FINE_WIRE)
                .iconSet(BRIGHT)
                .buildAndRegister();

        AstralTitanium = builderMaterial("astral_titanium")
                .ingot()
                .fluid()
                .plasma()
                .element(GTOElements.ASTRALTITANIUM)
                .color(0xf6cbf6)
                .flags(GENERATE_GEAR)
                .iconSet(BRIGHT)
                .buildAndRegister();

        CelestialTungsten = builderMaterial("celestial_tungsten")
                .ingot()
                .fluid()
                .plasma()
                .element(GTOElements.CELESTIALTUNGSTEN)
                .color(0x303030)
                .flags(GENERATE_GEAR)
                .iconSet(BRIGHT)
                .buildAndRegister();

        HexaphaseCopper = builderMaterial("hexaphasecopper")
                .ingot()
                .fluid()
                .plasma()
                .color(0xec7916)
                .element(GTOElements.HEXAPHASECOPPER)
                .blastTemp(75000, HIGHER)
                .iconSet(METALLIC)
                .flags(GTOMaterialFlags.GENERATE_NANOSWARM, GENERATE_ROTOR, GENERATE_FRAME, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_LONG_ROD)
                .buildAndRegister();

        Mercury194 = builderMaterial("mercury194")
                .dust()
                .element(GTOElements.MERCURY194)
                .color(0xb3b1b5)
                .iconSet(BRIGHT)
                .buildAndRegister();

        ChromaticGlass = builderMaterial("chromatic_glass")
                .ingot()
                .fluid()
                .plasma()
                .element(GTOElements.CHROMATICGLASS)
                .flags(GENERATE_LENS)
                .iconSet(GLASS)
                .buildAndRegister();

        Enderite = builderMaterial("enderite")
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

        NaquadriaticTaranium = builderMaterial("naquadriatictaranium")
                .ingot()
                .fluid()
                .blastTemp(16200, HIGHEST, GTValues.VA[GTValues.UXV], 1400)
                .components(Naquadria, 1, Taranium, 1)
                .color(0x000d1a)
                .iconSet(RADIOACTIVE)
                .flags(GENERATE_ROD, GENERATE_FINE_WIRE, DISABLE_DECOMPOSITION)
                .cableProperties(GTValues.V[GTValues.UXV], 4, 128)
                .buildAndRegister();

        AbyssalAlloy = builderMaterial("abyssalalloy")
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

        TitanSteel = builderMaterial("titansteel")
                .ingot()
                .fluid()
                .blastTemp(12600, HIGHER, GTValues.VA[GTValues.UHV], 1200)
                .components(TitaniumTungstenCarbide, 4, Plutonium241, 1, Einsteinium, 2, Rhenium, 1, Erbium, 1, Jasper, 3, UuAmplifier, 1)
                .color(0xe60000)
                .iconSet(METALLIC)
                .flags(GENERATE_FINE_WIRE, DISABLE_DECOMPOSITION)
                .cableProperties(GTValues.V[GTValues.UEV], 4, 64)
                .buildAndRegister();

        HighDurabilityCompoundSteel = builderMaterial("high_durability_compound_steel")
                .ingot()
                .fluid()
                .blastTemp(12600, HIGHER, GTValues.VA[GTValues.UV], 1600)
                .components(TungstenSteel, 12, HSSS, 9, HSSG, 6, Ruridit, 3, MagnetoResonatic, 2, Plutonium239, 1)
                .color(0x2d3c2d)
                .iconSet(METALLIC)
                .flags(GENERATE_BOLT_SCREW, GENERATE_PLATE, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GermaniumTungstenNitride = builderMaterial("germanium_tungsten_nitride")
                .ingot()
                .fluid()
                .blastTemp(8200, HIGHER, GTValues.VA[GTValues.LuV], 800)
                .components(Germanium, 3, Tungsten, 3, Nitrogen, 10)
                .color(0x7070a2)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        BlackTitanium = builderMaterial("black_titanium")
                .ingot()
                .fluid()
                .blastTemp(18900, HIGHEST, GTValues.VA[GTValues.UXV], 600)
                .components(Titanium, 26, Lanthanum, 6, Tungsten, 4, Cobalt, 3, Manganese, 2,
                        Phosphorus, 2, Palladium, 2, Niobium, 1, Argon, 5)
                .color(0x3d0021)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, GENERATE_FRAME, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TriniumTitanium = builderMaterial("trinium_titanium")
                .ingot()
                .fluid()
                .blastTemp(14400, HIGHER, GTValues.VA[GTValues.UIV], 800)
                .components(Trinium, 2, Titanium, 1)
                .color(0x856f91)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, GENERATE_FINE_WIRE, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Cinobite = builderMaterial("cinobite")
                .ingot()
                .fluid()
                .blastTemp(15400, HIGHER, GTValues.VA[GTValues.UIV], 1300)
                .components(Zeron100, 8, Naquadria, 4, Terbium, 3, Aluminium, 2, Mercury, 1, Tin, 1, Titanium, 6, Osmiridium, 1)
                .color(0x000000)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, GENERATE_FINE_WIRE, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HastelloyX78 = builderMaterial("hastelloyx_78")
                .ingot()
                .fluid()
                .blastTemp(14400, HIGHER, GTValues.VA[GTValues.UEV], 1200)
                .components(NaquadahAlloy, 10, Rhenium, 5, Naquadria, 4, Tritanium, 4, TungstenCarbide, 1,
                        Promethium, 1, Mendelevium, 1, Praseodymium, 1)
                .color(0x3c5b7f)
                .iconSet(METALLIC)
                .flags(GENERATE_ROUND, GENERATE_ROTOR, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_LONG_ROD,
                        GENERATE_FINE_WIRE, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HastelloyK243 = builderMaterial("hastelloyk_243")
                .ingot()
                .fluid()
                .blastTemp(17200, HIGHEST, GTValues.VA[GTValues.UXV], 1500)
                .components(HastelloyX78, 5, NiobiumNitride, 2, Tritanium, 4, TungstenCarbide, 4,
                        Promethium, 1, Mendelevium, 1, Praseodymium, 1, Holmium, 1)
                .color(0x92d959)
                .iconSet(METALLIC)
                .flags(GENERATE_ROUND, GENERATE_ROTOR, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_LONG_ROD,
                        DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Vibramantium = builderMaterial("vibramantium")
                .ingot()
                .fluid()
                .blastTemp(18800, HIGHER, GTValues.VA[GTValues.UXV], 1800)
                .components(Vibranium, 1, Adamantium, 3)
                .color(0xff009c)
                .iconSet(METALLIC)
                .flags(GENERATE_ROUND, GENERATE_ROTOR, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_LONG_ROD)
                .buildAndRegister();

        EglinSteel = builderMaterial("eglin_steel")
                .ingot()
                .fluid()
                .blastTemp(1048, LOW)
                .components(Iron, 4, Kanthal, 1, Invar, 5, Sulfur, 1, Silicon, 1, Carbon, 1)
                .color(0x4e270b)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, GENERATE_GEAR, DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();

        Inconel792 = builderMaterial("inconel_792")
                .ingot()
                .fluid()
                .blastTemp(5200, HIGH)
                .components(Nickel, 2, Niobium, 1, Aluminium, 2, Nichrome, 1)
                .color(0x44974a)
                .iconSet(METALLIC)
                .flags(GENERATE_BOLT_SCREW, GENERATE_FRAME, GENERATE_GEAR, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Pikyonium = builderMaterial("pikyonium")
                .ingot()
                .fluid()
                .blastTemp(10400, HIGHER, GTValues.VA[GTValues.ZPM], 800)
                .components(Inconel792, 8, EglinSteel, 5, NaquadahEnriched, 4, Cerium, 3,
                        Antimony, 2, Platinum, 2, Ytterbium, 1, TungstenSteel, 4)
                .color(0x244780)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HastelloyN = builderMaterial("hastelloy_n")
                .ingot()
                .fluid()
                .blastTemp(4350, HIGHER, 1920)
                .components(Iridium, 2, Molybdenum, 4, Chromium, 2, Titanium, 2, Nickel, 15)
                .color(0xaaaaaa)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, GENERATE_FRAME, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AluminiumBronze = builderMaterial("aluminium_bronze")
                .ingot()
                .fluid()
                .blastTemp(1200, LOW)
                .components(Aluminium, 1, Bronze, 6)
                .color(0xffdead)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, GENERATE_FRAME)
                .buildAndRegister();

        Lafium = builderMaterial("lafium")
                .ingot()
                .fluid()
                .blastTemp(9865, HIGHER, 1920)
                .components(HastelloyN, 8, Naquadah, 4, Samarium, 2, Tungsten, 4,
                        Aluminium, 6, Nickel, 2, Carbon, 2)
                .color(0x235497)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Grisium = builderMaterial("grisium")
                .ingot()
                .fluid()
                .blastTemp(4850, HIGH)
                .components(Titanium, 9, Carbon, 9, Potassium, 9, Lithium, 9, Sulfur, 9,
                        Hydrogen, 5)
                .color(0x355d6a)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE)
                .buildAndRegister();

        Stellite = builderMaterial("stellite")
                .ingot()
                .fluid()
                .blastTemp(4310, HIGH, 1920)
                .components(Cobalt, 9, Chromium, 9, Manganese, 5, Titanium, 2)
                .color(0x888192)
                .iconSet(METALLIC)
                .flags(GENERATE_GEAR)
                .buildAndRegister();

        SiliconCarbide = builderMaterial("silicon_carbide")
                .ingot()
                .fluid()
                .blastTemp(4800, HIGH, 480)
                .components(Silicon, 1, Carbon, 1)
                .color(0x34adb6)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE)
                .buildAndRegister();

        Quantum = builderMaterial("quantum")
                .ingot()
                .fluid()
                .blastTemp(11400, HIGHER, 1920)
                .components(Stellite, 15, Quantanium, 3, Jasper, 2, Gallium, 5, Americium, 5,
                        Palladium, 5, Germanium, 5, SiliconCarbide, 5)
                .color(0x0d0d0d)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FOIL)
                .buildAndRegister();

        FluxedElectrum = builderMaterial("fluxed_electrum")
                .ingot()
                .fluid()
                .blastTemp(10400, HIGHER, 1920)
                .components(SolderingAlloy, 1, InfusedGold, 1, Naquadah, 1, AstralSilver, 1,
                        RedSteel, 1, BlueSteel, 1, SterlingSilver, 1, RoseGold, 1)
                .color(0xf8f8d6)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FOIL)
                .buildAndRegister();

        Tanmolyium = builderMaterial("tanmolyium")
                .ingot()
                .fluid()
                .blastTemp(4300, HIGH, 1920)
                .components(Titanium, 5, Molybdenum, 5, Vanadium, 2, Chromium, 3, Aluminium, 1)
                .color(0x97249a)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION, GENERATE_PLATE)
                .buildAndRegister();

        Dalisenite = builderMaterial("dalisenite")
                .ingot()
                .fluid()
                .blastTemp(12400, HIGHER, 7680)
                .components(Erbium, 3, Tungsten, 10, Naquadah, 1, NiobiumTitanium, 9, Quantanium, 7,
                        RhodiumPlatedPalladium, 14, Tanmolyium, 1)
                .color(0xa4ac11)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FOIL)
                .buildAndRegister();

        ArceusAlloy2B = builderMaterial("arceusalloy2b")
                .ingot()
                .fluid()
                .blastTemp(14200, HIGHER, 122880)
                .components(Trinium, 3, MaragingSteel300, 4, Orichalcum, 1, NetherStar, 2,
                        TungstenSteel, 2, Osmiridium, 1, Strontium, 2)
                .color(0x79740e)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FOIL)
                .buildAndRegister();

        TitanPrecisionSteel = builderMaterial("titan_precision_steel")
                .ingot()
                .fluid()
                .blastTemp(16000, HIGHER, 491520)
                .components(TitanSteel, 3, Ytterbium, 1, PerditioCrystal, 1, EarthCrystal, 1,
                        IgnisCrystal, 1)
                .color(0x595137)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FOIL)
                .buildAndRegister();

        Lumiium = builderMaterial("lumiium")
                .ingot()
                .fluid()
                .blastTemp(5400, HIGH)
                .components(SterlingSilver, 2, TinAlloy, 4, LuminEssence, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xd9e222)
                .iconSet(METALLIC)
                .buildAndRegister();

        Hikarium = builderMaterial("hikarium")
                .ingot()
                .fluid()
                .blastTemp(17800, HIGHER, GTValues.VA[GTValues.UHV])
                .components(Lumiium, 18, Silver, 8, Sunnarium, 4)
                .color(0xe2bede)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FOIL)
                .buildAndRegister();

        SuperheavyLAlloy = builderMaterial("superheavy_l_alloy")
                .ingot()
                .fluid()
                .blastTemp(10600, HIGHER)
                .components(Rutherfordium, 1, Dubnium, 1, Seaborgium, 1, Bohrium, 1, Hassium, 1,
                        Meitnerium, 1, Darmstadtium, 1, Roentgenium, 1)
                .color(0x2b45df)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SuperheavyHAlloy = builderMaterial("superheavy_h_alloy")
                .ingot()
                .fluid()
                .blastTemp(10600, HIGHER)
                .components(Copernicium, 1, Nihonium, 1, Flerovium, 1, Moscovium, 1, Livermorium, 1,
                        Tennessine, 1, Oganesson, 1)
                .color(0xe84b36)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ZirconiumCarbide = builderMaterial("zirconium_carbide")
                .ingot()
                .fluid()
                .blastTemp(6800, HIGH, 1920, 1200)
                .components(Zirconium, 1, Carbon, 1)
                .color(0xd2bfaa)
                .iconSet(METALLIC)
                .flags(GENERATE_PLATE)
                .buildAndRegister();

        MarM200Steel = builderMaterial("mar_m_200_steel")
                .ingot()
                .fluid()
                .blastTemp(4600, HIGH, GTValues.VA[GTValues.IV], 100)
                .components(Niobium, 2, Chromium, 9, Aluminium, 5, Titanium, 2, Cobalt, 10,
                        Tungsten, 13, Nickel, 18)
                .color(0x515151)
                .iconSet(METALLIC)
                .flags(GENERATE_GEAR, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Tantalloy61 = builderMaterial("tantalloy_61")
                .ingot()
                .fluid()
                .blastTemp(6900, HIGHER, GTValues.VA[GTValues.LuV])
                .components(Tantalum, 13, Tungsten, 12, Titanium, 6, Yttrium, 4)
                .color(0x363636)
                .iconSet(METALLIC)
                .flags(GENERATE_BOLT_SCREW, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ReactorSteel = builderMaterial("reactor_steel")
                .ingot()
                .fluid()
                .blastTemp(3800, HIGH, GTValues.VA[GTValues.HV], 700)
                .components(Iron, 15, Niobium, 1, Vanadium, 4, Carbon, 2)
                .color(0xb4b3b0)
                .iconSet(SHINY)
                .flags(GENERATE_DENSE, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Lanthanoids1 = builderMaterial("lanthanoids_1")
                .dust()
                .color(0xef1133)
                .components(Lanthanum, 1, Cerium, 1, Praseodymium, 1, Neodymium, 1, Promethium, 1,
                        Samarium, 1, Europium, 1, Gadolinium, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Lanthanoids2 = builderMaterial("lanthanoids_2")
                .dust()
                .color(0xef1133)
                .components(Terbium, 1, Dysprosium, 1, Holmium, 1, Erbium, 1, Thulium, 1,
                        Ytterbium, 1, Lutetium, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        RAREEARTH = builderMaterial("rareearth")
                .fluid()
                .ingot()
                .color(0xa52a2a)
                .blastTemp(12400, HIGHER, GTValues.VA[GTValues.UHV], 800)
                .components(Scandium, 1, Yttrium, 1, Lanthanoids1, 1, Lanthanoids2, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Actinoids1 = builderMaterial("actinoids_1")
                .dust()
                .color(0x80eb33)
                .components(Actinium, 1, Thorium, 1, Protactinium, 1, Uranium238, 1, Neptunium, 1,
                        Plutonium239, 1, Americium, 1, Curium, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Actinoids2 = builderMaterial("actinoids_2")
                .dust()
                .color(0x80eb33)
                .components(Berkelium, 1, Californium, 1, Einsteinium, 1, Fermium, 1, Mendelevium, 1,
                        Nobelium, 1, Lawrencium, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Actinoids = builderMaterial("actinoids")
                .dust()
                .color(0x72d22e)
                .components(Actinoids1, 1, Actinoids2, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Alkaline = builderMaterial("alkaline")
                .dust()
                .color(0xdd186b)
                .components(Lithium, 1, Sodium, 1, Potassium, 1, Rubidium, 1, Caesium, 1,
                        Francium, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AlkalineEarth = builderMaterial("alkaline_earth")
                .dust()
                .color(0xc1155e)
                .components(Beryllium, 1, Magnesium, 1, Calcium, 1, Strontium, 1, Barium, 1,
                        Radium, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Transition1 = builderMaterial("transition_1")
                .dust()
                .color(0xa19e9d)
                .components(Titanium, 1, Vanadium, 1, Chromium, 1, Manganese, 1, Iron, 1, Cobalt, 1,
                        Nickel, 1, Copper, 1, Zinc, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Transition2 = builderMaterial("transition_2")
                .dust()
                .color(0x908d8c)
                .components(Zirconium, 1, Niobium, 1, Molybdenum, 1, Technetium, 1, Ruthenium, 1,
                        Rhodium, 1, Palladium, 1, Silver, 1, Cadmium, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Transition3 = builderMaterial("transition_3")
                .dust()
                .color(0x838180)
                .components(Hafnium, 1, Tantalum, 1, Tungsten, 1, Rhenium, 1, Osmium, 1, Iridium, 1,
                        Platinum, 1, Gold, 1, Mercury, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Transition = builderMaterial("transition")
                .fluid()
                .ingot()
                .color(0x989594)
                .blastTemp(13600, HIGHER, GTValues.VA[GTValues.UHV], 900)
                .components(Transition1, 1, Transition2, 1, Transition3, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Poor = builderMaterial("poor")
                .dust()
                .color(0x916d12)
                .components(Aluminium, 1, Gallium, 1, Indium, 1, Tin, 1, Thallium, 1, Lead, 1,
                        Bismuth, 1, Polonium, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Metalloid = builderMaterial("metalloid")
                .dust()
                .color(0x916d12)
                .components(Boron, 1, Silicon, 1, Germanium, 1, Arsenic, 1, Antimony, 1,
                        Tellurium, 1, Astatine, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        NotFound = builderMaterial("not_found")
                .fluid()
                .color(0x1e0ebe)
                .components(Hydrogen, 1, Carbon, 1, Nitrogen, 1, Oxygen, 1, Fluorine, 1,
                        Phosphorus, 1, Sulfur, 1, Chlorine, 1, Selenium, 1, Bromine, 1,
                        Iodine, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        NobleGas = builderMaterial("noble_gas")
                .fluid()
                .color(0xed8dea)
                .components(Helium, 1, Neon, 1, Argon, 1, Krypton, 1, Xenon, 1, Radon, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Periodicium = builderMaterial("periodicium")
                .ingot()
                .fluid()
                .blastTemp(15200, HIGHEST, GTValues.VA[GTValues.UEV], 1200)
                .components(NotFound, 1, NobleGas, 1, Metalloid, 1, Poor, 1, Transition, 1,
                        AlkalineEarth, 1, RAREEARTH, 1, Alkaline, 1, Actinoids, 1)
                .color(0x3d4bf6)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        FallKing = builderMaterial("fall_king")
                .ingot()
                .fluid()
                .blastTemp(5400, HIGH)
                .components(Helium, 1, Lithium, 1, Cobalt, 1, Platinum, 1, Erbium, 1)
                .color(0xffcf6b)
                .iconSet(BRIGHT)
                .buildAndRegister();

        WoodsGlass = builderMaterial("woods_glass")
                .ingot()
                .fluid()
                .blastTemp(3600)
                .components(SodaAsh, 6, SiliconDioxide, 3, Garnierite, 3, BariumSulfide, 1)
                .color(0x730099)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Polyetheretherketone = builderMaterial("polyetheretherketone")
                .polymer()
                .fluid()
                .components(Carbon, 20, Hydrogen, 12, Oxygen, 3)
                .color(0x33334d)
                .iconSet(DULL)
                .flags(GENERATE_FINE_WIRE)
                .buildAndRegister();

        CarbonNanotubes = builderMaterial("carbon_nanotubes")
                .polymer()
                .fluid()
                .color(0x000000)
                .iconSet(DULL)
                .flags(GENERATE_FOIL, GENERATE_FINE_WIRE)
                .buildAndRegister();

        FullerenePolymerMatrixPulp = builderMaterial("fullerene_polymer_matrix_pulp")
                .polymer()
                .fluid()
                .fluidPipeProperties(5000, 5000, true, true, true, true)
                .color(0x23221e)
                .iconSet(DULL)
                .flags(GENERATE_FOIL)
                .buildAndRegister();

        Zylon = builderMaterial("zylon")
                .polymer()
                .fluid()
                .components(Carbon, 14, Hydrogen, 6, Nitrogen, 2, Oxygen, 2)
                .color(0xd2b800)
                .iconSet(DULL)
                .flags(GENERATE_FOIL)
                .buildAndRegister();

        Kevlar = builderMaterial("kevlar")
                .polymer()
                .fluid()
                .color(0x9f9f53)
                .iconSet(DULL)
                .flags(GENERATE_FOIL)
                .buildAndRegister();

        Radox = builderMaterial("radox")
                .polymer()
                .fluid()
                .components(Carbon, 14, Osmium, 11, Oxygen, 7, Silver, 3, Concrete, 1, Water, 1)
                .color(0x680064)
                .iconSet(DULL)
                .flags(GENERATE_FOIL, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AdamantineCompounds = builderMaterial("adamantine_compounds")
                .ore()
                .color(0xdaa520)
                .components(Adamantine, 1, Concrete, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        // Dust
        RawTengam = builderMaterial("raw_tengam")
                .dust()
                .color(0x819a4e)
                .iconSet(DULL)
                .buildAndRegister();

        CleanRawTengam = builderMaterial("clean_raw_tengam")
                .dust()
                .color(0x819a4e)
                .iconSet(SHINY)
                .buildAndRegister();

        PurifiedTengam = builderMaterial("purified_tengam")
                .dust()
                .element(GTOElements.TENGAM)
                .color(0x819a4e)
                .iconSet(METALLIC)
                .buildAndRegister();

        AttunedTengam = builderMaterial("attuned_tengam")
                .ingot()
                .element(GTOElements.TENGAM)
                .color(0x819a4e)
                .iconSet(MAGNETIC)
                .flags(IS_MAGNETIC, GENERATE_LONG_ROD)
                .buildAndRegister();

        PreZylon = builderMaterial("pre_zylon")
                .dust()
                .color(0x562050)
                .components(Carbon, 20, Hydrogen, 22, Nitrogen, 2, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Terephthalaldehyde = builderMaterial("terephthalaldehyde")
                .dust()
                .color(0x4a7454)
                .components(Carbon, 8, Hydrogen, 6, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumOxide = builderMaterial("sodium_oxide")
                .dust()
                .color(0x036dee)
                .components(Sodium, 2, Oxygen, 1)
                .iconSet(DULL)
                .buildAndRegister();

        Bedrock = builderMaterial("bedrock")
                .dust()
                .color(0x808080)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?");

        CompressedStone = builderMaterial("compressed_stone")
                .dust()
                .color(0x696969)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?");

        GermaniumContainingPrecipitate = builderMaterial("germanium_containing_precipitate")
                .dust()
                .color(0x666699)
                .components(Germanium, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GermaniumAsh = builderMaterial("germanium_ash")
                .dust()
                .color(0x706699)
                .components(Germanium, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GermaniumDioxide = builderMaterial("germanium_dioxide")
                .dust()
                .color(0xffffff)
                .components(Germanium, 1, Oxygen, 2)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Durene = builderMaterial("durene")
                .dust()
                .components(Carbon, 10, Hydrogen, 14)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x708090)
                .iconSet(DULL)
                .buildAndRegister();

        PyromelliticDianhydride = builderMaterial("pyromellitic_dianhydride")
                .dust()
                .components(Carbon, 10, Hydrogen, 2, Oxygen, 6)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x708090)
                .iconSet(DULL)
                .buildAndRegister();

        CoAcAbCatalyst = builderMaterial("co_ac_ab_catalyst")
                .dust()
                .color(0x544422)
                .iconSet(DULL)
                .buildAndRegister();

        ZnFeAlClCatalyst = builderMaterial("znfealcl_catalyst")
                .dust()
                .color(0xcf51aa)
                .iconSet(DULL)
                .buildAndRegister();

        CalciumCarbide = builderMaterial("calcium_carbide")
                .dust()
                .components(Calcium, 1, Carbon, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x47443e)
                .iconSet(DULL)
                .buildAndRegister();

        Difluorobenzophenone = builderMaterial("difluorobenzophenone")
                .dust()
                .components(Fluorine, 2, Carbon, 13, Hydrogen, 8, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xcf51ae)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumFluoride = builderMaterial("sodium_fluoride")
                .dust()
                .components(Sodium, 1, Fluorine, 1)
                .color(0x460011)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumSeaborgate = builderMaterial("sodium_seaborgate")
                .dust()
                .components(Sodium, 2, Seaborgium, 1, Oxygen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x55bbd4)
                .iconSet(DULL)
                .buildAndRegister();

        GoldDepletedMolybdenite = builderMaterial("gold_depleted_molybdenite")
                .dust()
                .color(0x757587)
                .components(Molybdenum, 1, Sulfur, 2)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        MolybdenumTrioxide = builderMaterial("molybdenum_trioxide")
                .dust()
                .components(Molybdenum, 1, Oxygen, 3)
                .color(0x757587)
                .iconSet(DULL)
                .buildAndRegister();

        Dichlorocyclooctadieneplatinum = builderMaterial("dichlorocyclooctadieneplatinium")
                .dust()
                .color(0xd4e982)
                .components(Carbon, 8, Hydrogen, 12, Chlorine, 2, Platinum, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Diiodobiphenyl = builderMaterial("diiodobiphenyl")
                .dust()
                .color(0x000a42)
                .components(Carbon, 12, Hydrogen, 8, Iodine, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        MolybdenumConcentrate = builderMaterial("molybdenum_concentrate")
                .dust()
                .color(0x47443e)
                .components(Molybdenum, 1, Sulfur, 2, Concrete, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        BoronTrioxide = builderMaterial("boron_trioxide")
                .dust()
                .components(Boron, 2, Oxygen, 3)
                .color(0x8fa6b5)
                .iconSet(DULL)
                .buildAndRegister();

        LithiumNiobateNanoparticles = builderMaterial("lithium_niobate_nanoparticles")
                .dust()
                .color(0xc1c12d)
                .components(Lithium, 2, Niobium, 1, Oxygen, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Hexanitrohexaaxaisowurtzitane = builderMaterial("hexanitrohexaaxaisowurtzitane")
                .dust()
                .components(Carbon, 6, Hydrogen, 6, Nitrogen, 12, Oxygen, 12)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x3d464b)
                .iconSet(DULL)
                .buildAndRegister();

        SilicaGel = builderMaterial("silica_gel")
                .dust()
                .color(0x57c3e4)
                .iconSet(DULL)
                .buildAndRegister();

        CrudeHexanitrohexaaxaisowurtzitane = builderMaterial("crude_hexanitrohexaaxaisowurtzitane")
                .dust()
                .color(0x19586d)
                .components(Carbon, 6, Hydrogen, 6, Nitrogen, 12, Oxygen, 12)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Tetraacetyldinitrosohexaazaisowurtzitane = builderMaterial("tetraacetyldinitrosohexaazaisowurtzitane")
                .dust()
                .color(0x500449)
                .components(Carbon, 14, Hydrogen, 18, Nitrogen, 8, Oxygen, 6)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        NitroniumTetrafluoroborate = builderMaterial("nitronium_tetrafluoroborate")
                .dust()
                .color(0x3c3f40)
                .components(Nitrogen, 1, Oxygen, 2, Boron, 1, Fluorine, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        NitrosoniumTetrafluoroborate = builderMaterial("nitrosonium_tetrafluoroborate")
                .dust()
                .color(0x485054)
                .components(Nitrogen, 1, Oxygen, 1, Boron, 1, Fluorine, 4)
                .iconSet(DULL)
                .buildAndRegister();

        Dibenzyltetraacetylhexaazaisowurtzitane = builderMaterial("dibenzyltetraacetylhexaazaisowurtzitane")
                .dust()
                .color(0x64704d)
                .components(Carbon, 28, Hydrogen, 32, Nitrogen, 6, Oxygen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SuccinamidylAcetate = builderMaterial("succinimidyl_acetate")
                .dust()
                .color(0x64704d)
                .components(Carbon, 6, Hydrogen, 7, Nitrogen, 1, Oxygen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Hexabenzylhexaazaisowurtzitane = builderMaterial("hexabenzylhexaazaisowurtzitane")
                .dust()
                .color(0x64704d)
                .components(Carbon, 48, Hydrogen, 48, Nitrogen, 6)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        NHydroxysuccinimide = builderMaterial("n_hydroxysuccinimide")
                .dust()
                .color(0x7b717f)
                .components(Carbon, 4, Hydrogen, 5, Nitrogen, 1, Oxygen, 3)
                .iconSet(DULL)
                .buildAndRegister();

        SuccinicAnhydride = builderMaterial("succinic_anhydride")
                .dust()
                .color(0x401116)
                .components(Carbon, 4, Hydrogen, 4, Oxygen, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SuccinicAcid = builderMaterial("succinic_acid")
                .dust()
                .color(0x104e5c)
                .components(Carbon, 4, Hydrogen, 6, Oxygen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Acetonitrile = builderMaterial("acetonitrile")
                .dust()
                .color(0x5a6161)
                .components(Carbon, 2, Hydrogen, 3, Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Hexamethylenetetramine = builderMaterial("hexamethylenetetramine")
                .dust()
                .color(0x5a6261)
                .components(Carbon, 6, Hydrogen, 12, Nitrogen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        RareEarthOxide = builderMaterial("rare_earth_oxide")
                .dust()
                .color(0x808000)
                .components(Concrete, 1, Oxygen, 1)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        RareEarthMetal = builderMaterial("rare_earth_metal")
                .dust()
                .ore()
                .addOreByproducts(RareEarth, Monazite)
                .color(0x737373)
                .iconSet(METALLIC)
                .buildAndRegister().setFormula("?");

        NaquadahContainRareEarth = builderMaterial("naquadah_contain_rare_earth")
                .dust()
                .color(0xffd700)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?");

        NaquadahContainRareEarthFluoride = builderMaterial("naquadah_contain_rare_earth_fluoride")
                .dust()
                .color(0xb3b300)
                .components(Concrete, 1, Fluorine, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        MetalResidue = builderMaterial("metal_residue")
                .dust()
                .color(0x652118)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?");

        PalladiumFullereneMatrix = builderMaterial("palladium_fullerene_matrix")
                .dust()
                .color(0x96b4b4)
                .components(Palladium, 1, Carbon, 73, Hydrogen, 15, Nitrogen, 1, Iron, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(BRIGHT)
                .buildAndRegister();

        Fullerene = builderMaterial("fullerene")
                .dust()
                .color(0x86c2b8)
                .components(Carbon, 60)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        UnfoldedFullerene = builderMaterial("unfolded_fullerene")
                .dust()
                .color(0x587f83)
                .components(Carbon, 60, Hydrogen, 30)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Methylbenzophenanthrene = builderMaterial("methylbenzophenanthrene")
                .dust()
                .color(0x79260c)
                .components(Carbon, 19, Hydrogen, 14)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Sarcosine = builderMaterial("sarcosine")
                .dust()
                .color(0x809324)
                .components(Carbon, 3, Hydrogen, 7, Nitrogen, 1, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        DiphenylmethaneDiisocyanate = builderMaterial("diphenylmethane_diisocyanate")
                .dust()
                .color(0x8e801c)
                .components(Carbon, 15, Hydrogen, 10, Nitrogen, 2, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Pentaerythritol = builderMaterial("pentaerythritol")
                .dust()
                .color(0xacacac)
                .components(Carbon, 5, Hydrogen, 12, Oxygen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        KelpSlurry = builderMaterial("kelp_slurry")
                .fluid()
                .color(0x336600)
                .iconSet(DULL)
                .buildAndRegister();

        Quasifissioning = builderMaterial("quasifissioning")
                .plasma()
                .color(0xcdbe35)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?");

        ExcitedDtec = builderMaterial("exciteddtec")
                .fluid()
                .color(0xb36a6b)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?");

        ExcitedDtsc = builderMaterial("exciteddtsc")
                .fluid()
                .color(0xb36a6b)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?");

        DimensionallyTranscendentResplendentCatalyst = builderMaterial("dimensionallytranscendentresplendentcatalyst")
                .fluid()
                .color(0x081010)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?");

        DimensionallyTranscendentProsaicCatalyst = builderMaterial("dimensionallytranscendentprosaiccatalyst")
                .fluid()
                .color(0x081010)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?");

        DimensionallyTranscendentExoticCatalyst = builderMaterial("dimensionallytranscendentexoticcatalyst")
                .fluid()
                .color(0x081010)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?");

        DimensionallyTranscendentStellarCatalyst = builderMaterial("dimensionallytranscendentstellarcatalyst")
                .fluid()
                .color(0x081010)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?");

        DimensionallyTranscendentCrudeCatalyst = builderMaterial("dimensionallytranscendentcrudecatalyst")
                .fluid()
                .color(0x081010)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?");

        Ytterbium178 = builderMaterial("ytterbium_178")
                .fluid()
                .element(GTOElements.YITTERBIUM178)
                .color(0xffffff)
                .iconSet(DULL)
                .buildAndRegister();

        Flyb = builderMaterial("flyb")
                .plasma()
                .components(Flerovium, 1, Ytterbium178, 1)
                .color(0x890a95)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        EnrichedPotassiumIodideSlurry = builderMaterial("enriched_potassium_iodide_slurry")
                .fluid()
                .color(0x00ffcc)
                .components(Potassium, 1, Iodine, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        IodineContainingSlurry = builderMaterial("iodine_containing_slurry")
                .fluid()
                .color(0x666633)
                .components(Iodine, 1, RockSalt, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AshLeachingSolution = builderMaterial("ash_leaching_solution")
                .fluid()
                .color(0x666699)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?");

        Tannic = builderMaterial("tannic")
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .components(Carbon, 76, Hydrogen, 52, Oxygen, 46)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xffff66)
                .iconSet(DULL)
                .buildAndRegister();

        GermaniumTetrachlorideSolution = builderMaterial("germanium_tetrachloride_solution")
                .fluid()
                .color(0x66ffcc)
                .components(Germanium, 1, Chlorine, 4)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Polyimide = builderMaterial("polyimide")
                .fluid()
                .components(Carbon, 22, Hydrogen, 12, Nitrogen, 2, Oxygen, 6)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xff6730)
                .iconSet(DULL)
                .buildAndRegister();

        Aniline = builderMaterial("aniline")
                .fluid()
                .components(Carbon, 6, Hydrogen, 7, Nitrogen, 1)
                .color(0x3d7517)
                .iconSet(DULL)
                .buildAndRegister();

        Oxydianiline = builderMaterial("oxydianiline")
                .fluid()
                .components(Carbon, 12, Hydrogen, 12, Nitrogen, 2, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xffd700)
                .iconSet(DULL)
                .buildAndRegister();

        BoricAcid = builderMaterial("boric_acide")
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .components(Hydrogen, 3, Boron, 1, Oxygen, 3)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x8fbc8f)
                .iconSet(DULL)
                .buildAndRegister();

        FluoroboricAcid = builderMaterial("fluoroboric_acide")
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .components(Hydrogen, 1, Boron, 1, Fluorine, 4)
                .color(0x8fbc8f)
                .iconSet(DULL)
                .buildAndRegister();

        BenzenediazoniumTetrafluoroborate = builderMaterial("benzenediazonium_tetrafluoroborate")
                .fluid()
                .components(Carbon, 6, Hydrogen, 5, Boron, 1, Fluorine, 4, Nitrogen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x8fbc8f)
                .iconSet(DULL)
                .buildAndRegister();

        FluoroBenzene = builderMaterial("fluoro_benzene")
                .fluid()
                .components(Carbon, 6, Hydrogen, 5, Fluorine, 1)
                .color(0x8fbc8f)
                .iconSet(DULL)
                .buildAndRegister();

        Fluorotoluene = builderMaterial("fluorotoluene")
                .fluid()
                .components(Carbon, 7, Hydrogen, 7, Fluorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xdad386)
                .iconSet(DULL)
                .buildAndRegister();

        Hydroquinone = builderMaterial("hydroquinone")
                .fluid()
                .components(Carbon, 6, Hydrogen, 6, Oxygen, 2)
                .color(0x8e2518)
                .iconSet(DULL)
                .buildAndRegister();

        Resorcinol = builderMaterial("resorcinol")
                .fluid()
                .components(Carbon, 6, Hydrogen, 6, Oxygen, 2)
                .color(0x8e2518)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumNitrate = builderMaterial("sodium_nitrate")
                .dust()
                .color(0x4e2a40).iconSet(SAND)
                .components(Sodium, 1, Nitrogen, 1, Oxygen, 3)
                .buildAndRegister();

        SodiumNitrateSolution = builderMaterial("sodium_nitrate_solution")
                .fluid()
                .components(SodiumNitrate, 1, Water, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x2b387e)
                .iconSet(DULL)
                .buildAndRegister();

        Acetylene = builderMaterial("acetylene")
                .fluid()
                .components(Carbon, 2, Hydrogen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x7f8552)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumCyanide = builderMaterial("sodium_cyanide")
                .fluid()
                .components(Sodium, 1, Carbon, 1, Nitrogen, 1)
                .color(0x4f6774)
                .iconSet(DULL)
                .buildAndRegister();

        GoldCyanide = builderMaterial("gold_cyanide")
                .fluid()
                .components(Gold, 1, Carbon, 1, Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x736f50)
                .iconSet(DULL)
                .buildAndRegister();

        MolybdenumFlue = builderMaterial("molybdenum_flue")
                .gas()
                .color(0x4b626f)
                .components(Sulfur, 2, Concrete, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        RheniumSulfuricSolution = builderMaterial("rhenium_sulfuric_solution")
                .fluid()
                .color(0xc0c0ea)
                .components(Rhenium, 1, Sulfur, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AmmoniumPerrhenate = builderMaterial("ammonium_perrhenate")
                .fluid()
                .color(0x161637)
                .components(Ammonia, 1, Rhenium, 1, Oxygen, 4)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Cycloparaphenylene = builderMaterial("cycloparaphenylene")
                .fluid()
                .color(0x000000)
                .iconSet(DULL)
                .buildAndRegister();

        TrimethylTinChloride = builderMaterial("trimethyltin_chloride")
                .fluid()
                .color(0x72685f)
                .components(Tin, 1, Carbon, 3, Hydrogen, 9, Chlorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SilverTetrafluoroborate = builderMaterial("silver_tetrafluoroborate")
                .fluid()
                .color(0x76750f)
                .components(Silver, 1, Boron, 1, Fluorine, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        BoronFluoride = builderMaterial("boron_fluoride")
                .fluid()
                .components(Boron, 1, Fluorine, 3)
                .color(0xcecad0)
                .iconSet(DULL)
                .buildAndRegister();

        OneOctene = builderMaterial("1_octene")
                .fluid()
                .components(Carbon, 8, Hydrogen, 16)
                .color(0x666d61)
                .iconSet(DULL)
                .buildAndRegister();

        Pyridine = builderMaterial("pyridine")
                .fluid()
                .color(0x555642)
                .components(Carbon, 5, Hydrogen, 5, Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Acetaldehyde = builderMaterial("acetaldehyde")
                .fluid()
                .components(Carbon, 2, Hydrogen, 4, Oxygen, 1)
                .color(0x666d61)
                .iconSet(DULL)
                .buildAndRegister();

        Cyclooctadiene = builderMaterial("cyclooctadiene")
                .fluid()
                .color(0xd4e982)
                .components(Carbon, 8, Hydrogen, 12)
                .iconSet(DULL)
                .buildAndRegister();

        SeaborgiumDopedNanotubes = builderMaterial("seaborgium_doped_nanotubes")
                .fluid()
                .color(0x242473)
                .iconSet(DULL)
                .buildAndRegister();

        Ethylenediamine = builderMaterial("ethylenediamine")
                .fluid()
                .color(0x1b5d74)
                .components(Carbon, 2, Hydrogen, 8, Nitrogen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Ethanolamine = builderMaterial("ethanolamine")
                .fluid()
                .color(0x1b5d74)
                .components(Carbon, 2, Hydrogen, 7, Nitrogen, 1, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        EthyleneOxide = builderMaterial("ethylene_oxide")
                .fluid()
                .color(0x8eb7d8)
                .components(Carbon, 2, Hydrogen, 4, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Benzaldehyde = builderMaterial("benzaldehyde")
                .fluid()
                .color(0x905a1b)
                .components(Carbon, 7, Hydrogen, 6, Oxygen, 1)
                .iconSet(DULL)
                .buildAndRegister();

        HydroxylamineHydrochloride = builderMaterial("hydroxylamine_hydrochloride")
                .fluid()
                .color(0x433217)
                .components(Hydrogen, 4, Nitrogen, 1, Oxygen, 1, Chlorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        MaleicAnhydride = builderMaterial("maleic_anhydride")
                .fluid()
                .color(0x321b90)
                .components(Carbon, 4, Hydrogen, 2, Oxygen, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Benzylamine = builderMaterial("benzylamine")
                .fluid()
                .color(0x5b6363)
                .components(Carbon, 7, Hydrogen, 9, Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Glyoxal = builderMaterial("glyoxal")
                .fluid()
                .color(0xf0ed4d)
                .components(Carbon, 2, Hydrogen, 2, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        BenzylChloride = builderMaterial("benzyl_chloride")
                .fluid()
                .color(0x9ff6fb)
                .components(Carbon, 7, Hydrogen, 7, Chlorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Mana = builderMaterial("mana")
                .gas()
                .liquid()
                .color(0x9400d3)
                .element(GTOElements.MANA)
                .iconSet(DULL)
                .buildAndRegister();

        RareEarthHydroxides = builderMaterial("rare_earth_hydroxides")
                .fluid()
                .color(0x808000)
                .components(Concrete, 1, Oxygen, 1, Hydrogen, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        QuantumDots = builderMaterial("quantum_dots")
                .fluid()
                .color(0xda0000)
                .iconSet(DULL)
                .buildAndRegister();

        StearicAcid = builderMaterial("stearic_acid")
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0x239791)
                .components(Carbon, 18, Hydrogen, 36, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Soap = builderMaterial("soap")
                .fluid()
                .color(0xff9d1b)
                .iconSet(DULL)
                .buildAndRegister();

        Tricotylphosphine = builderMaterial("tricotylphosphine")
                .fluid()
                .color(0xe7d510)
                .components(Carbon, 24, Hydrogen, 51, Phosphorus, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        IridiumTrichlorideSolution = builderMaterial("iridium_trichloride_solution")
                .fluid()
                .color(0x776715)
                .components(Iridium, 1, Chlorine, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        LiquidHydrogen = builderMaterial("liquid_hydrogen")
                .fluid()
                .color(0x4fc4a2)
                .components(Hydrogen, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        BedrockSmoke = builderMaterial("bedrock_smoke")
                .gas()
                .color(0xa9a9a9)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?");

        BedrockSootSolution = builderMaterial("bedrock_soot_solution")
                .fluid()
                .color(0x191970)
                .iconSet(DULL)
                .buildAndRegister().setFormula("Nq?");

        CleanBedrockSolution = builderMaterial("clean_bedrock_solution")
                .fluid()
                .color(0x778899)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister().setFormula("?");

        BedrockGas = builderMaterial("bedrock_gas")
                .gas()
                .color(0xc0c0c0)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?");

        VibraniumUnstable = builderMaterial("vibranium_unstable")
                .fluid()
                .color(0xfa8072)
                .components(Vibranium, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TaraniumEnrichedLiquidHelium3 = builderMaterial("taranium_enriched_liquid_helium_3")
                .fluid()
                .color(0x57f26d)
                .components(Taranium, 1, Helium3, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TaraniumRichLiquidHelium4 = builderMaterial("taranium_rich_liquid_helium_4")
                .fluid()
                .plasma()
                .color(0x57f26d)
                .components(Taranium, 1, Helium, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        FreeElectronGas = builderMaterial("free_electron_gas")
                .gas()
                .color(0x033c3c)
                .element(GTOElements.ELECTRON)
                .iconSet(DULL)
                .buildAndRegister();

        FreeAlphaGas = builderMaterial("free_alpha_gas")
                .gas()
                .color(0xb5ab06)
                .element(GTOElements.ALPHA)
                .iconSet(DULL)
                .buildAndRegister();

        FreeProtonGas = builderMaterial("free_proton_gas")
                .gas()
                .color(0x2089aa)
                .element(GTOElements.PROTON)
                .iconSet(DULL)
                .buildAndRegister();

        QuarkGluon = builderMaterial("quark_gluon")
                .plasma()
                .color(0x7a00da)
                .element(GTOElements.QUARK_GLUON)
                .iconSet(DULL)
                .buildAndRegister();

        HeavyQuarks = builderMaterial("heavy_quarks")
                .gas()
                .color(0x007000)
                .element(GTOElements.HEAVY_QUARKS)
                .iconSet(DULL)
                .buildAndRegister();

        LightQuarks = builderMaterial("light_quarks")
                .gas()
                .color(0x0000ce)
                .element(GTOElements.LIGHT_QUARKS)
                .iconSet(DULL)
                .buildAndRegister();

        Gluons = builderMaterial("gluons")
                .gas()
                .color(0xfbfbf9)
                .element(GTOElements.GLUONS)
                .iconSet(DULL)
                .buildAndRegister();

        OganessonBreedingBase = builderMaterial("oganesson_breeding_base")
                .fluid()
                .color(0xb8676c)
                .iconSet(DULL)
                .buildAndRegister();

        TitaniumTetrafluoride = builderMaterial("titanium_tetrafluoride")
                .fluid()
                .color(0xd68fed)
                .components(Titanium, 1, Fluorine, 4)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Titanium50 = builderMaterial("titanium_50")
                .ingot()
                .fluid()
                .element(GTOElements.TITANIUM50)
                .blastTemp(1942)
                .color(0xd58eed)
                .iconSet(METALLIC)
                .buildAndRegister();

        Titanium50Tetrafluoride = builderMaterial("titanium_50_tetrafluoride")
                .fluid()
                .color(0xd68fed)
                .components(Titanium50, 1, Fluorine, 4)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Titanium50Tetrachloride = builderMaterial("titanium_50_tetrachloride")
                .fluid()
                .color(0xafeeee)
                .components(Titanium50, 1, Chlorine, 4)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HotOganesson = builderMaterial("hot_oganesson")
                .fluid()
                .color(0x42145d)
                .components(Oganesson, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Ferrocene = builderMaterial("ferrocene")
                .fluid()
                .color(0x7373c9)
                .components(Carbon, 10, Hydrogen, 10, Iron, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        ScandiumTitanium50Mixture = builderMaterial("scandium_titanium_50_mixture")
                .fluid()
                .color(0xeb315f)
                .components(Scandium, 1, Titanium50, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        DragonBreath = builderMaterial("dragon_breath")
                .fluid()
                .color(0xff00ff)
                .components(Draconium, 1, Concrete, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        EnrichedDragonBreath = builderMaterial("enriched_dragon_breath")
                .fluid()
                .color(0xff00ff)
                .components(Draconium, 1, Concrete, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        DragonBlood = builderMaterial("dragon_blood")
                .fluid()
                .color(0x9932cc)
                .components(Draconium, 1, Concrete, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TurbidDragonBlood = builderMaterial("turbid_dragon_blood")
                .fluid()
                .color(0x4d0099)
                .components(Draconium, 1, Concrete, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        DragonElement = builderMaterial("dragon_element")
                .fluid()
                .color(0x9933ff)
                .iconSet(DULL)
                .components(Draconium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HeavyLeptonMixture = builderMaterial("heavy_lepton_mixture")
                .gas()
                .color(0x3ad931)
                .element(GTOElements.HEAVY_LEPTON_MIXTURE)
                .iconSet(DULL)
                .buildAndRegister();

        HeavyQuarkEnrichedMixture = builderMaterial("heavy_quark_enriched_mixture")
                .gas()
                .color(0xececec)
                .element(GTOElements.HEAVY_QUARK_ENRICHED_MIXTURE)
                .iconSet(DULL)
                .buildAndRegister();

        CosmicComputingMixture = builderMaterial("cosmic_computing_mixture")
                .gas()
                .color(0x8b8925)
                .components(Gluons, 1, HeavyQuarks, 1, HeavyLeptonMixture, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        LiquidStarlight = builderMaterial("liquid_starlight")
                .liquid(new FluidBuilder().customStill())
                .element(GTOElements.STARLIGHT)
                .buildAndRegister();

        Starlight = builderMaterial("starlight")
                .liquid(new FluidBuilder().customStill())
                .element(GTOElements.STARLIGHT)
                .buildAndRegister();

        DenseNeutron = builderMaterial("dense_neutron")
                .plasma()
                .color(0x9ce89c)
                .element(GTOElements.DENSE_NEUTRON)
                .iconSet(DULL)
                .buildAndRegister();

        HighEnergyQuarkGluon = builderMaterial("high_energy_quark_gluon")
                .plasma()
                .color(0x7400ce)
                .element(GTOElements.HIGH_ENERGY_QUARK_GLUON)
                .iconSet(BRIGHT)
                .buildAndRegister();

        NeutroniumDopedNanotubes = builderMaterial("neutronium_doped_nanotubes")
                .fluid()
                .color(0x5bf5f5)
                .iconSet(DULL)
                .buildAndRegister();

        AmmoniumNitrateSolution = builderMaterial("ammonium_nitrate_solution")
                .fluid()
                .color(0xe1ffff)
                .components(Nitrogen, 2, Hydrogen, 4, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        NaquadahSolution = builderMaterial("naquadah_solution")
                .fluid()
                .color(0x00ff00)
                .iconSet(DULL)
                .buildAndRegister();

        FluorineCrackedAquadah = builderMaterial("fluorine_cracked_aquadah")
                .fluid()
                .color(0x424d4b)
                .iconSet(DULL)
                .buildAndRegister();

        RadonCrackedEnrichedAquadah = builderMaterial("radon_cracked_enriched_aquadah")
                .fluid()
                .color(0x424d4b)
                .iconSet(DULL)
                .buildAndRegister();

        NaquadahFuel = builderMaterial("naquadah_fuel")
                .fluid()
                .color(0x292929)
                .components(Naquadah, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        EnrichedNaquadahFuel = builderMaterial("enriched_naquadah_fuel")
                .fluid()
                .color(0x292929)
                .components(NaquadahEnriched, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HyperFuel1 = builderMaterial("hyper_fuel_1")
                .fluid()
                .color(0xf9ff3d)
                .components(Naquadah, 1, NaquadahEnriched, 1, Naquadria, 1, Thorium, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HyperFuel2 = builderMaterial("hyper_fuel_2")
                .fluid()
                .color(0xd1d54d)
                .components(HyperFuel1, 1, Uranium235, 1, Dubnium, 1, Fermium, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HyperFuel3 = builderMaterial("hyper_fuel_3")
                .fluid()
                .color(0x7a7c3c)
                .components(HyperFuel2, 1, Plutonium241, 1, Adamantine, 1, Lawrencium, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HyperFuel4 = builderMaterial("hyper_fuel_4")
                .fluid()
                .color(0x3f4028)
                .components(HyperFuel3, 1, Nobelium, 1, Neutronium, 1, Taranium, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ConcentrationMixingHyperFuel1 = builderMaterial("concentration_mixing_hyper_fuel_1")
                .fluid()
                .color(0x000000)
                .components(HyperFuel4, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ConcentrationMixingHyperFuel2 = builderMaterial("concentration_mixing_hyper_fuel_2")
                .fluid()
                .color(0x000000)
                .components(HyperFuel4, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CosmicElement = builderMaterial("cosmic_element")
                .gas()
                .color(0xa366ff)
                .components(FreeElectronGas, 1, FreeAlphaGas, 1, FreeProtonGas, 1, QuarkGluon, 1,
                        HeavyQuarks, 1, LightQuarks, 1, Gluons, 1, HeavyLeptonMixture, 1,
                        HeavyQuarkEnrichedMixture, 1, DenseNeutron, 1,
                        HighEnergyQuarkGluon, 1)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        DimensionallyTranscendentResidue = builderMaterial("dimensionallytranscendentresidue")
                .liquid(new FluidBuilder().customStill())
                .buildAndRegister().setFormula("?");

        PrimordialMatter = builderMaterial("primordialmatter")
                .liquid(new FluidBuilder().customStill())
                .buildAndRegister().setFormula("?");

        SpatialFluid = builderMaterial("spatialfluid")
                .liquid(new FluidBuilder().customStill())
                .element(GTOElements.TIME)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TemporalFluid = builderMaterial("temporalfluid")
                .liquid(new FluidBuilder().customStill())
                .element(GTOElements.TIME)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Isochloropropane = builderMaterial("isochloropropane")
                .fluid()
                .color(0xcdd681)
                .components(Carbon, 3, Hydrogen, 7, Chlorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Dinitrodipropanyloxybenzene = builderMaterial("dinitrodipropanyloxybenzene")
                .fluid()
                .color(0x6a784d)
                .components(Carbon, 12, Hydrogen, 16, Nitrogen, 2, Oxygen, 6)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        RadoxGas = builderMaterial("radox_gas")
                .gas()
                .components(Carbon, 14, Osmium, 11, Silver, 3, Concrete, 1, Water, 1)
                .color(0xab57ab)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CrackedRadox = builderMaterial("crackedradox")
                .fluid()
                .components(Carbon, 14, Osmium, 11, Silver, 3, Concrete, 1, Water, 1)
                .color(0xab57ab)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SuperLightRadox = builderMaterial("superlightradox")
                .fluid()
                .components(Carbon, 14, Osmium, 11, Silver, 2, Concrete, 1, Water, 1)
                .color(0x6c006c)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        LightRadox = builderMaterial("lightradox")
                .fluid()
                .components(Carbon, 14, Osmium, 11, Silver, 1, Concrete, 1, Water, 1)
                .color(0x6c006c)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SuperHeavyRadox = builderMaterial("superheavyradox")
                .fluid()
                .components(Carbon, 14, Osmium, 11, Concrete, 1, Water, 1)
                .color(0x6c006c)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HeavyRadox = builderMaterial("heavyradox")
                .fluid()
                .components(Carbon, 14, Osmium, 11, Concrete, 1, Water, 1)
                .color(0x6c006c)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        RawRadox = builderMaterial("rawradox")
                .fluid()
                .color(0x391539)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?");

        Xenoxene = builderMaterial("xenoxene")
                .fluid()
                .color(0x5c5a58)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?");

        XenoxeneCrystal = builderMaterial("xenoxene_crystal")
                .dust()
                .color(0x5c5a58)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?");

        XenoxeneMixture = builderMaterial("xenoxene_mixture")
                .fluid()
                .color(0x5c7a58)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?");

        EnrichedXenoxene = builderMaterial("enriched_xenoxene")
                .fluid()
                .color(0x5c5a58)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?");

        PurifiedXenoxene = builderMaterial("purified_xenoxene")
                .fluid()
                .color(0x5c7a58)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?");

        DilutedXenoxene = builderMaterial("dilutedxenoxene")
                .fluid()
                .color(0x8b8784)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?");

        Dibromomethylbenzene = builderMaterial("dibromomethylbenzene")
                .fluid()
                .components(Carbon, 7, Hydrogen, 6, Bromine, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x6b584)
                .iconSet(DULL)
                .buildAndRegister();

        RawStarMatter = builderMaterial("raw_star_matter")
                .element(GTOElements.RAW_STAR_MATTER)
                .plasma(new FluidBuilder().customStill())
                .buildAndRegister();

        BiomediumRaw = builderMaterial("biomediumraw")
                .fluid()
                .color(0x42641f)
                .iconSet(DULL)
                .buildAndRegister();

        BiohmediumSterilized = builderMaterial("biohmediumsterilized")
                .fluid()
                .color(0x72b125)
                .iconSet(DULL)
                .buildAndRegister();

        UnknowWater = builderMaterial("unknowwater")
                .fluid()
                .color(0x3322aa)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?");

        UnknownNutrientAgar = builderMaterial("unknownnutrientagar")
                .fluid()
                .color(0x916e00)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?");

        SeaweedBroth = builderMaterial("seaweedbroth")
                .fluid()
                .color(0x2c9400)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?");

        LiquidCrystalKevlar = builderMaterial("liquidcrystalkevlar")
                .fluid()
                .color(0x9f9f50)
                .iconSet(DULL)
                .buildAndRegister();

        PolyurethaneResin = builderMaterial("polyurethaneresin")
                .fluid()
                .color(0x9a9a51)
                .iconSet(DULL)
                .buildAndRegister();

        DiphenylmethanediisocyanateMixture = builderMaterial("diphenylmethanediisocyanatemixture")
                .fluid()
                .color(0x94851d)
                .iconSet(DULL)
                .buildAndRegister();

        Phosgene = builderMaterial("phosgene")
                .fluid()
                .components(Carbon, 1, Oxygen, 1, Chlorine, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x0e6c11)
                .iconSet(DULL)
                .buildAndRegister();

        DiaminodiphenylmethanMixture = builderMaterial("diaminodiphenylmethanmixture")
                .fluid()
                .color(0x94851d)
                .iconSet(DULL)
                .buildAndRegister();

        SiliconOil = builderMaterial("siliconoil")
                .fluid()
                .color(0xadadad)
                .iconSet(DULL)
                .buildAndRegister();

        EthyleneGlycol = builderMaterial("ethyleneglycol")
                .fluid()
                .color(0xadadad)
                .components(Carbon, 2, Hydrogen, 6, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        PCBs = builderMaterial("pcbs")
                .fluid()
                .color(0x758a61)
                .components(Carbon, 80, Hydrogen, 21, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        DMAP = builderMaterial("dmap")
                .dust()
                .color(0x758a61)
                .components(Carbon, 7, Hydrogen, 10, Nitrogen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        PhenylPentanoicAcid = builderMaterial("phenylpentanoic_acid")
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0x4d3833)
                .components(Carbon, 11, Hydrogen, 14, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Dichloromethane = builderMaterial("dichloromethane")
                .fluid()
                .color(0x832663)
                .components(Carbon, 1, Hydrogen, 2, Chlorine, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        DimethylSulfide = builderMaterial("dimethyl_sulfide")
                .fluid()
                .color(0xa02e06)
                .components(Carbon, 2, Hydrogen, 6, Sulfur, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        CosmicMesh = builderMaterial("cosmic_mesh")
                .plasma()
                .fluid()
                .color(0x181878)
                .element(GTOElements.COSMIC_MESH)
                .iconSet(DULL)
                .buildAndRegister();

        HydrobromicAcid = builderMaterial("hydrobromic_acid")
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .components(Hydrogen, 1, Bromine, 1)
                .color(0xa2573f)
                .iconSet(DULL)
                .buildAndRegister();

        BenzophenanthrenylAcetonitrile = builderMaterial("benzophenanthrenylacetonitrile")
                .dust()
                .components(Carbon, 20, Hydrogen, 13, Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x9222c7)
                .iconSet(DULL)
                .buildAndRegister();

        BromoSuccinamide = builderMaterial("bromo_succinimide")
                .dust()
                .components(Carbon, 4, Hydrogen, 4, Bromine, 1, Nitrogen, 1, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x546a21)
                .iconSet(DULL)
                .buildAndRegister();

        PotassiumBromide = builderMaterial("potassium_bromide")
                .dust()
                .components(Potassium, 1, Bromine, 1)
                .color(0xa34a76)
                .iconSet(GEM_HORIZONTAL)
                .buildAndRegister();

        Succinimide = builderMaterial("succinimide")
                .dust()
                .components(Carbon, 4, Hydrogen, 5, Nitrogen, 1, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x21a7c5)
                .iconSet(DULL)
                .buildAndRegister();

        FissionedUranium235 = builderMaterial("fissioned_uranium_235")
                .dust()
                .color(0x39c9b7)
                .iconSet(METALLIC)
                .buildAndRegister();

        FranciumCaesiumCadmiumBromide = builderMaterial("francium_caesium_cadmium_bromide")
                .dust()
                .components(Francium, 1, Caesium, 1, Cadmium, 2, Bromine, 6)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xa34d1a)
                .iconSet(BRIGHT)
                .buildAndRegister();

        StrontiumEuropiumAluminate = builderMaterial("strontium_europium_aluminate")
                .dust()
                .components(Strontium, 1, Europium, 1, Aluminium, 2, Oxygen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xb62d78)
                .iconSet(BRIGHT)
                .buildAndRegister();

        UraniumSulfateWasteSolution = builderMaterial("uranium_sulfate_waste_solution")
                .fluid()
                .color(0xb1b113)
                .iconSet(DULL)
                .buildAndRegister();

        DibismuthHydroborate = builderMaterial("dibismuthhydroborat")
                .dust()
                .components(Bismuth, 2, Hydrogen, 1, Boron, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x007d32)
                .iconSet(BRIGHT)
                .buildAndRegister();

        CircuitCompound = builderMaterial("circuit_compound")
                .dust()
                .components(IndiumGalliumPhosphide, 1, DibismuthHydroborate, 3, BismuthTellurite, 2)
                .color(0x00210e)
                .iconSet(BRIGHT)
                .buildAndRegister();

        CaesiumIodide = builderMaterial("caesium_iodide")
                .dust()
                .components(Caesium, 1, Iodine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xeeeee2)
                .iconSet(DULL)
                .buildAndRegister();

        ThalliumThuliumDopedCaesiumIodide = builderMaterial("thallium_thulium_doped_caesium_iodide")
                .dust()
                .color(0xe8b97f)
                .iconSet(BRIGHT)
                .buildAndRegister();

        Photoresist = builderMaterial("photoresist")
                .fluid()
                .color(0x2f4f4f)
                .iconSet(BRIGHT)
                .buildAndRegister();

        EuvPhotoresist = builderMaterial("euv_photoresist")
                .fluid()
                .color(0x4b0082)
                .iconSet(BRIGHT)
                .buildAndRegister();

        GammaRaysPhotoresist = builderMaterial("gamma_rays_photoresist")
                .fluid()
                .color(0x556b2f)
                .iconSet(BRIGHT)
                .buildAndRegister();

        AcrylicAcid = builderMaterial("acrylic_acid")
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0xb41558)
                .components(Carbon, 3, Hydrogen, 4, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        EthylAcrylate = builderMaterial("ethyl_acrylate")
                .fluid()
                .color(0x947d15)
                .components(Carbon, 5, Hydrogen, 8, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Trichloroflerane = builderMaterial("trichloroflerane")
                .fluid()
                .color(0x42145d)
                .components(Flerovium, 1, Chlorine, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        BisethylenedithiotetraselenafulvalenePerrhenate = builderMaterial("bisethylenedithiotetraselenafulvalene_perrhenate")
                .dust()
                .color(0x72cb00)
                .components(Rhenium, 1, Carbon, 10, Hydrogen, 8, Sulfur, 4, Selenium, 4, Oxygen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(BRIGHT)
                .buildAndRegister();

        Bisethylenedithiotetraselenafulvalene = builderMaterial("bisethylenedithiotetraselenafulvalene")
                .dust()
                .color(0x72cb00)
                .components(Carbon, 10, Hydrogen, 8, Sulfur, 4, Selenium, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(BRIGHT)
                .buildAndRegister();

        LithiumThiinediselenide = builderMaterial("lithiumthiinediselenide")
                .dust()
                .color(0x72cb00)
                .components(Carbon, 4, Hydrogen, 4, Sulfur, 2, Lithium, 2, Selenium, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        CyclopentadienylTitaniumTrichloride = builderMaterial("cyclopentadienyl_titanium_trichloride")
                .dust()
                .color(0xb22db2)
                .components(Carbon, 10, Hydrogen, 10, Chlorine, 2, Titanium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(BRIGHT)
                .buildAndRegister();

        ButylLithium = builderMaterial("butyl_lithium")
                .fluid()
                .color(0x9f6af6)
                .components(Carbon, 4, Hydrogen, 9, Lithium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Bromodihydrothiine = builderMaterial("bromodihydrothiine")
                .fluid()
                .color(0x50c44d)
                .components(Carbon, 4, Hydrogen, 4, Sulfur, 2, Bromine, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Dibromoacrolein = builderMaterial("dibromoacrolein")
                .fluid()
                .color(0x3e3e3e)
                .components(Carbon, 2, Hydrogen, 2, Bromine, 2, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumThiosulfate = builderMaterial("sodium_thiosulfate")
                .dust()
                .color(0x145a9d)
                .components(Sodium, 2, Sulfur, 2, Oxygen, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        LithiumFluoride = builderMaterial("lithium_fluoride")
                .dust()
                .color(0x6d6d6d)
                .components(Lithium, 1, Fluorine, 1)
                .iconSet(DULL)
                .buildAndRegister();

        HighPurityCalciumCarbonate = builderMaterial("high_purity_calcium_carbonate")
                .dust()
                .color(0xeeeee0)
                .components(Calcium, 1, Carbon, 1, Oxygen, 3)
                .iconSet(DULL)
                .buildAndRegister();

        Bromobutane = builderMaterial("bromobutane")
                .fluid()
                .color(0xff0c0c)
                .components(Carbon, 4, Hydrogen, 9, Bromine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Propadiene = builderMaterial("propadiene")
                .fluid()
                .color(0x323b0a)
                .components(Carbon, 3, Hydrogen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        AstatideSolution = builderMaterial("astatide_solution")
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0x4ff417)
                .components(Astatine, 1, SulfuricAcid, 1)
                .iconSet(DULL)
                .buildAndRegister();

        MixedAstatideSalts = builderMaterial("mixed_astatide_salts")
                .dust()
                .color(0x67e83c)
                .components(Holmium, 1, Thulium, 1, Copernicium, 1, Flerovium, 1, Astatine, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        BoronFranciumCarbide = builderMaterial("boron_francium_carbide")
                .dust()
                .color(0x797979)
                .components(Francium, 4, Boron, 4, Carbon, 7)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Borocarbide = builderMaterial("borocarbide")
                .dust()
                .color(0x7e7e22)
                .components(MixedAstatideSalts, 1, BoronFranciumCarbide, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        FranciumCarbide = builderMaterial("francium_carbide")
                .dust()
                .color(0xa1a1a1)
                .components(Francium, 2, Carbon, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        BoronCarbide = builderMaterial("boron_carbide")
                .dust()
                .color(0x1e1e1e)
                .components(Boron, 4, Carbon, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        LanthanumEmbeddedFullerene = builderMaterial("lanthanum_embedded_fullerene")
                .dust()
                .color(0x91c100)
                .components(Lanthanum, 1, Fullerene, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        LanthanumFullereneMix = builderMaterial("lanthanum_fullerene_mix")
                .dust()
                .color(0xd3bfec)
                .components(Lanthanum, 1, UnfoldedFullerene, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        CaliforniumTrichloride = builderMaterial("californium_trichloride")
                .dust()
                .color(0x286224)
                .components(Californium, 1, Chlorine, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        FullereneDopedNanotubes = builderMaterial("fullerene_doped_nanotubes")
                .fluid()
                .color(0x562356)
                .iconSet(DULL)
                .buildAndRegister();

        CaliforniumCyclopentadienide = builderMaterial("californium_cyclopentadienide")
                .fluid()
                .color(0x78374a)
                .components(Carbon, 15, Hydrogen, 15, Californium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Cyclopentadiene = builderMaterial("cyclopentadiene")
                .fluid()
                .color(0x2aa62a)
                .components(Carbon, 5, Hydrogen, 6)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        LithiumCyclopentadienide = builderMaterial("lithium_cyclopentadienide")
                .fluid()
                .color(0x7b4657)
                .components(Lithium, 1, Carbon, 5, Hydrogen, 5)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Dimethylether = builderMaterial("dimethylether")
                .fluid()
                .color(0xbda90e)
                .components(Carbon, 2, Hydrogen, 6, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Dimethoxyethane = builderMaterial("dimethoxyethane")
                .fluid()
                .color(0x23a996)
                .components(Carbon, 4, Hydrogen, 10, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Photopolymer = builderMaterial("photopolymer")
                .fluid()
                .color(0x73445b)
                .components(Carbon, 149, Hydrogen, 97, Nitrogen, 10, Oxygen, 2, Titanium, 1,
                        Boron, 1, Fluorine, 20)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SilverPerchlorate = builderMaterial("silver_perchlorate")
                .dust()
                .color(0xededed)
                .components(Silver, 1, Chlorine, 1, Oxygen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SilverChloride = builderMaterial("silver_chloride")
                .dust()
                .color(0x8d8d8d)
                .components(Silver, 1, Chlorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumBromide = builderMaterial("sodium_bromide")
                .dust()
                .color(0xc588c4)
                .components(Sodium, 1, Bromine, 1)
                .iconSet(DULL)
                .buildAndRegister();

        SilverOxide = builderMaterial("silver_oxide")
                .dust()
                .color(0x2b2b2b)
                .components(Silver, 2, Oxygen, 1)
                .iconSet(DULL)
                .buildAndRegister();

        PhthalicAnhydride = builderMaterial("phthalic_anhydride")
                .dust()
                .color(0x8c8c8c)
                .components(Carbon, 8, Hydrogen, 4, Oxygen, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumHypochlorite = builderMaterial("sodium_hypochlorite")
                .dust()
                .color(0x66f14c)
                .components(Sodium, 1, Chlorine, 1, Oxygen, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Ethylanthraquinone = builderMaterial("ethylanthraquinone")
                .fluid()
                .color(0xffff24)
                .components(Carbon, 16, Hydrogen, 12, Oxygen, 2)
                .iconSet(DULL)
                .buildAndRegister();

        Ethylanthrahydroquinone = builderMaterial("ethylanthrahydroquinone")
                .fluid()
                .color(0xcece00)
                .components(Ethylanthraquinone, 1, Hydrogen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Anthracene = builderMaterial("anthracene")
                .fluid()
                .color(0x929e92)
                .components(Carbon, 14, Hydrogen, 10)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Phenylsodium = builderMaterial("phenylsodium")
                .fluid()
                .color(0x2626ab)
                .components(Carbon, 6, Hydrogen, 5, Sodium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        NDifluorophenylpyrrole = builderMaterial("n_difluorophenylpyrrole")
                .fluid()
                .color(0x2f7e8a)
                .components(Carbon, 10, Hydrogen, 7, Fluorine, 2, Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Difluoroaniline = builderMaterial("difluoroaniline")
                .fluid()
                .color(0x348f3e)
                .components(Carbon, 6, Hydrogen, 5, Fluorine, 2, Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Succinaldehyde = builderMaterial("succinaldehyde")
                .fluid()
                .color(0x63577d)
                .components(Carbon, 4, Hydrogen, 6, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        TetraethylammoniumBromide = builderMaterial("tetraethylammonium_bromide")
                .fluid()
                .color(0xc20cff)
                .components(Carbon, 8, Hydrogen, 20, Nitrogen, 1, Bromine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        RhodiumRheniumNaquadahCatalyst = builderMaterial("rhodium_rhenium_naquadah_catalyst")
                .dust()
                .color(0x944648)
                .components(Rhodium, 1, Rhenium, 1, Naquadah, 1)
                .iconSet(DULL)
                .buildAndRegister();

        IodineMonochloride = builderMaterial("iodine_monochloride")
                .fluid()
                .color(0x004141)
                .components(Iodine, 1, Chlorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Dimethylnaphthalene = builderMaterial("dimethylnaphthalene")
                .fluid()
                .color(0xde2da1)
                .components(Carbon, 12, Hydrogen, 12)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Dihydroiodotetracene = builderMaterial("dihydroiodotetracene")
                .fluid()
                .color(0xde2da1)
                .components(Carbon, 18, Hydrogen, 13, Iodine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        AcetylatingReagent = builderMaterial("acetylating_reagent")
                .fluid()
                .color(0x724c50)
                .components(Carbon, 9, Hydrogen, 12, Silicon, 1, Magnesium, 2, Bromine, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        MagnesiumChlorideBromide = builderMaterial("magnesium_chloride_bromide")
                .dust()
                .color(0x99234d)
                .components(Magnesium, 1, Chlorine, 1, Bromine, 1)
                .iconSet(DULL)
                .buildAndRegister();

        IsopropylAlcohol = builderMaterial("isopropyl_alcohol")
                .fluid()
                .color(0x51b31f)
                .components(Carbon, 3, Hydrogen, 8, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Dichlorodicyanobenzoquinone = builderMaterial("dichlorodicyanobenzoquinone")
                .fluid()
                .color(0x302399)
                .components(Carbon, 8, Chlorine, 2, Nitrogen, 2, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Dichlorodicyanohydroquinone = builderMaterial("dichlorodicyanohydroquinone")
                .fluid()
                .color(0x302399)
                .components(Carbon, 8, Chlorine, 2, Nitrogen, 2, Oxygen, 2, Hydrogen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Tetracene = builderMaterial("tetracene")
                .dust()
                .color(0x8f7718)
                .components(Carbon, 18, Hydrogen, 12)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        PolycyclicAromaticMixture = builderMaterial("polycyclic_aromatic_mixture")
                .dust()
                .color(0x8f7718)
                .components(Carbon, 18, Hydrogen, 12)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        RheniumHassiumThalliumIsophtaloylbisdiethylthioureaHexaf = builderMaterial("rhenium_hassium_thallium_isophtaloylbisdiethylthiourea_hexaf")
                .dust()
                .color(0x8f7718)
                .components(Rhenium, 1, Hassium, 1, Thallium, 1, Carbon, 60, Phosphorus, 1,
                        Nitrogen, 12, Hydrogen, 84, Sulfur, 6, Oxygen, 12, Fluorine, 6)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        ThalliumChloride = builderMaterial("thallium_chloride")
                .dust()
                .color(0xcc5350)
                .components(Thallium, 1, Chlorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        HassiumChloride = builderMaterial("hassium_chloride")
                .dust()
                .color(0x5828b2)
                .components(Hassium, 1, Chlorine, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        RheniumChloride = builderMaterial("rhenium_chloride")
                .dust()
                .color(0x392857)
                .components(Rhenium, 1, Chlorine, 5)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        IsophthaloylBis = builderMaterial("isophthaloylbis")
                .fluid()
                .color(0x76677e)
                .components(Carbon, 18, Hydrogen, 26, Nitrogen, 4, Oxygen, 2, Sulfur, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        HexafluorophosphoricAcid = builderMaterial("hexafluorophosphoric_acid")
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0xd5d54b)
                .components(Hydrogen, 1, Phosphorus, 1, Fluorine, 6)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Diethylthiourea = builderMaterial("diethylthiourea")
                .fluid()
                .color(0x23a687)
                .components(Carbon, 5, Hydrogen, 12, Nitrogen, 2, Sulfur, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        ThionylChloride = builderMaterial("thionyl_chloride")
                .fluid()
                .color(0xf8f5e0)
                .components(Sulfur, 1, Oxygen, 1, Chlorine, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        PhenylenedioxydiaceticAcid = builderMaterial("phenylenedioxydiacetic_acid")
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0x7c4456)
                .components(Carbon, 10, Hydrogen, 10, Oxygen, 6)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumThiocyanate = builderMaterial("sodium_thiocyanate")
                .fluid()
                .color(0x7c4456)
                .components(Sodium, 1, Sulfur, 1, Carbon, 1, Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        PhosphorusTrichloride = builderMaterial("phosphorus_trichloride")
                .fluid()
                .color(0xd5d54b)
                .components(Phosphorus, 1, Chlorine, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        AntimonyPentafluoride = builderMaterial("antimony_pentafluoride")
                .fluid()
                .color(0xd5d5bd)
                .components(Antimony, 1, Fluorine, 5)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        AntimonyTrichloride = builderMaterial("antimony_trichloride")
                .dust()
                .color(0xbcbcbc)
                .components(Antimony, 1, Chlorine, 3)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ChargedCaesiumCeriumCobaltIndium = builderMaterial("charged_caesium_cerium_cobalt_indium")
                .dust()
                .color(0x4da323)
                .components(Caesium, 1, Cerium, 1, Cobalt, 2, Indium, 10, CosmicComputingMixture, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ActiniumSuperhydride = builderMaterial("actinium_superhydride")
                .plasma()
                .dust()
                .color(0x52b051)
                .components(Actinium, 1, Hydrogen, 12)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CosmicSuperconductor = builderMaterial("cosmic_superconductor")
                .fluid()
                .color(0x00ffff)
                .components(RheniumHassiumThalliumIsophtaloylbisdiethylthioureaHexaf, 1,
                        ActiniumSuperhydride, 1, ChargedCaesiumCeriumCobaltIndium, 1)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Ethylamine = builderMaterial("ethylamine")
                .fluid()
                .color(0x5a656d)
                .components(Carbon, 2, Hydrogen, 5, Nitrogen, 1, Hydrogen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        TolueneDiisocyanate = builderMaterial("toluene_diisocyanate")
                .fluid()
                .color(0xacf4bf)
                .components(Carbon, 9, Hydrogen, 6, Nitrogen, 2, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Polyurethane = builderMaterial("polyurethane")
                .fluid()
                .color(0xacf4bf)
                .components(Carbon, 17, Hydrogen, 16, Nitrogen, 2, Oxygen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        ViscoelasticPolyurethane = builderMaterial("viscoelastic_polyurethane")
                .fluid()
                .color(0xecfbec)
                .components(Polyurethane, 1, EthyleneGlycol, 1, Calcite, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ViscoelasticPolyurethaneFoam = builderMaterial("viscoelastic_polyurethane_foam")
                .fluid()
                .color(0xecfbec)
                .components(ViscoelasticPolyurethane, 1, Air, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Glucose = builderMaterial("glucose")
                .dust()
                .color(0xcacace)
                .components(Carbon, 6, Hydrogen, 12, Oxygen, 6)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        GlucoseIronSolution = builderMaterial("glucose_iron_solution")
                .fluid()
                .color(0xc9c9c9)
                .components(Glucose, 1, Iron3Chloride, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GrapheneOxide = builderMaterial("graphene_oxide")
                .dust()
                .color(0x535353)
                .components(Carbon, 1, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        GrapheneGelSuspension = builderMaterial("graphene_gel_suspension")
                .dust()
                .color(0x535353)
                .components(Carbon, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        DryGrapheneGel = builderMaterial("dry_graphene_gel")
                .dust()
                .color(0x202079)
                .components(Carbon, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SupercriticalCarbonDioxide = builderMaterial("supercritical_carbon_dioxide")
                .fluid()
                .color(0x9ac8f3)
                .components(Carbon, 1, Oxygen, 2)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        PotassiumBisulfite = builderMaterial("potassium_bisulfite")
                .dust()
                .color(0x807d72)
                .components(Potassium, 1, Sulfur, 1, Hydrogen, 1, Oxygen, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        PotassiumHydroxylaminedisulfonate = builderMaterial("potassium_hydroxylaminedisulfonate")
                .dust()
                .color(0x4c6226)
                .components(Potassium, 2, Nitrogen, 1, Hydrogen, 1, Sulfur, 2, Oxygen, 7)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        HydroxylammoniumSulfate = builderMaterial("hydroxylammonium_sulfate")
                .dust()
                .color(0x848075)
                .components(Nitrogen, 2, Hydrogen, 8, Sulfur, 1, Oxygen, 6)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        BariumChloride = builderMaterial("barium_chloride")
                .dust()
                .color(0xe9705f)
                .components(Barium, 1, Chlorine, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        NitrousAcid = builderMaterial("nitrous_acid")
                .fluid()
                .color(0xa0c8fd)
                .components(Hydrogen, 1, Nitrogen, 1, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        ActiniumHydride = builderMaterial("atinium_hydride")
                .dust()
                .color(0xb8c6f1)
                .components(Actinium, 1, Hydrogen, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        GradePurifiedWater1 = builderMaterial("grade_1_purified_water")
                .fluid()
                .components(Water, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GradePurifiedWater2 = builderMaterial("grade_2_purified_water")
                .fluid()
                .components(Water, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GradePurifiedWater3 = builderMaterial("grade_3_purified_water")
                .fluid()
                .components(Water, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GradePurifiedWater4 = builderMaterial("grade_4_purified_water")
                .fluid()
                .components(Water, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GradePurifiedWater5 = builderMaterial("grade_5_purified_water")
                .fluid()
                .components(Water, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GradePurifiedWater6 = builderMaterial("grade_6_purified_water")
                .fluid()
                .components(Water, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GradePurifiedWater7 = builderMaterial("grade_7_purified_water")
                .fluid()
                .components(Water, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GradePurifiedWater8 = builderMaterial("grade_8_purified_water")
                .fluid()
                .components(Water, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GradePurifiedWater9 = builderMaterial("grade_9_purified_water")
                .fluid()
                .components(Water, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GradePurifiedWater10 = builderMaterial("grade_10_purified_water")
                .fluid()
                .components(Water, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GradePurifiedWater11 = builderMaterial("grade_11_purified_water")
                .fluid()
                .components(Water, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GradePurifiedWater12 = builderMaterial("grade_12_purified_water")
                .fluid()
                .components(Water, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GradePurifiedWater13 = builderMaterial("grade_13_purified_water")
                .fluid()
                .components(Water, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GradePurifiedWater14 = builderMaterial("grade_14_purified_water")
                .fluid()
                .components(Water, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GradePurifiedWater15 = builderMaterial("grade_15_purified_water")
                .fluid()
                .components(Water, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GradePurifiedWater16 = builderMaterial("grade_16_purified_water")
                .fluid()
                .components(Water, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Antimatter = builderMaterial("antimatter")
                .fluid()
                .color(0x9932cc)
                .iconSet(DULL)
                .buildAndRegister();

        PositiveElectron = builderMaterial("positive_electron")
                .fluid()
                .color(0x59764c)
                .iconSet(DULL)
                .buildAndRegister().setFormula("e+");

        Antiproton = builderMaterial("antiproton")
                .fluid()
                .color(0x4945af)
                .iconSet(DULL)
                .buildAndRegister().setFormula("p-");

        Antineutron = builderMaterial("antineutron")
                .fluid()
                .color(0xe6e6fa)
                .iconSet(DULL)
                .buildAndRegister().setFormula("n-bar");

        Antihydrogen = builderMaterial("antihydrogen")
                .fluid()
                .color(0x6a5acd)
                .iconSet(DULL)
                .buildAndRegister().setFormula("Ah");

        Kerosene = builderMaterial("kerosene")
                .fluid()
                .components(Carbon, 12, Hydrogen, 24)
                .color(0xce57ce)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Rp1 = builderMaterial("rp_1")
                .fluid()
                .color(0xff523e)
                .iconSet(DULL)
                .buildAndRegister();

        RocketFuelRp1 = builderMaterial("rocket_fuel_rp_1")
                .fluid()
                .color(0xff321b)
                .iconSet(DULL)
                .buildAndRegister();

        Hydrazine = builderMaterial("hydrazine")
                .fluid()
                .color(0xffffff)
                .components(Nitrogen, 2, Hydrogen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        DenseHydrazineFuelMixture = builderMaterial("dense_hydrazine_fuel_mixture")
                .fluid()
                .color(0x4a223b)
                .iconSet(DULL)
                .buildAndRegister();

        Monomethylhydrazine = builderMaterial("monomethylhydrazine")
                .fluid()
                .color(0xffffff)
                .components(Carbon, 1, Hydrogen, 6, Nitrogen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        RocketFuelCn3h7o3 = builderMaterial("rocket_fuel_cn3h7o3")
                .fluid()
                .color(0xa636ac)
                .iconSet(DULL)
                .buildAndRegister();

        RocketFuelH8n4c2o4 = builderMaterial("rocket_fuel_h8n4c2o4")
                .fluid()
                .color(0x4aa11b)
                .iconSet(DULL)
                .buildAndRegister();

        StellarEnergyRocketFuel = builderMaterial("stellar_energy_rocket_fuel")
                .fluid()
                .color(0xdf362d)
                .iconSet(DULL)
                .buildAndRegister();

        ExplosiveHydrazine = builderMaterial("explosivehydrazine")
                .fluid()
                .color(0x3b0c5c)
                .iconSet(DULL)
                .buildAndRegister();

        HmxExplosive = builderMaterial("hmxexplosive")
                .dust()
                .color(0xf3ffdb)
                .iconSet(BRIGHT)
                .buildAndRegister();

        LaNdOxidesSolution = builderMaterial("la_nd_oxides_solution")
                .fluid()
                .color(0x9ce3db)
                .iconSet(DULL)
                .buildAndRegister().setFormula("(La2O3)(Pr2O3)(Nd2O3)(Ce2O3)");

        SmGdOxidesSolution = builderMaterial("sm_gd_oxides_solution")
                .fluid()
                .color(0xffff99)
                .iconSet(DULL)
                .buildAndRegister().setFormula("(Sc2O3)(Eu2O3)(Gd2O3)(Sm2O3)");

        TbHoOxidesSolution = builderMaterial("tb_ho_oxides_solution")
                .fluid()
                .color(0x99ff99)
                .iconSet(DULL)
                .buildAndRegister().setFormula("(Y2O3)(Tb2O3)(Dy2O3)(Ho2O3)");

        ErLuOxidesSolution = builderMaterial("er_lu_oxides_solution")
                .fluid()
                .color(0xffb3ff)
                .iconSet(DULL)
                .buildAndRegister().setFormula("(Er2O3)(Tm2O3)(Yb2O3)(Lu2O3)");

        LanthanumOxide = builderMaterial("lanthanum_oxide")
                .dust()
                .color(0xcfcfcf)
                .components(Lanthanum, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        PraseodymiumOxide = builderMaterial("praseodymium_oxide")
                .dust()
                .color(0xcfcfcf)
                .components(Praseodymium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        NeodymiumOxide = builderMaterial("neodymium_oxide")
                .dust()
                .color(0xcfcfcf)
                .components(Neodymium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CeriumOxide = builderMaterial("cerium_oxide")
                .dust()
                .color(0xcfcfcf)
                .components(Cerium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        EuropiumOxide = builderMaterial("europium_oxide")
                .dust()
                .color(0xcfcfcf)
                .components(Europium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GadoliniumOxide = builderMaterial("gadolinium_oxide")
                .dust()
                .color(0xcfcfcf)
                .components(Gadolinium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SamariumOxide = builderMaterial("samarium_oxide")
                .dust()
                .color(0xcfcfcf)
                .components(Samarium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TerbiumOxide = builderMaterial("terbium_oxide")
                .dust()
                .color(0xcfcfcf)
                .components(Terbium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        DysprosiumOxide = builderMaterial("dysprosium_oxide")
                .dust()
                .color(0xcfcfcf)
                .components(Dysprosium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HolmiumOxide = builderMaterial("holmium_oxide")
                .dust()
                .color(0xcfcfcf)
                .components(Holmium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ErbiumOxide = builderMaterial("erbium_oxide")
                .dust()
                .color(0xcfcfcf)
                .components(Erbium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ThuliumOxide = builderMaterial("thulium_oxide")
                .dust()
                .color(0xcfcfcf)
                .components(Thulium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        YtterbiumOxide = builderMaterial("ytterbium_oxide")
                .dust()
                .color(0xcfcfcf)
                .components(Ytterbium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        LutetiumOxide = builderMaterial("lutetium_oxide")
                .dust()
                .color(0xcfcfcf)
                .components(Lutetium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ScandiumOxide = builderMaterial("scandium_oxide")
                .dust()
                .color(0xcfcfcf)
                .components(Scandium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        YttriumOxide = builderMaterial("yttrium_oxide")
                .dust()
                .color(0xcfcfcf)
                .components(Yttrium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ZirconChlorinatingResidue = builderMaterial("zircon_chlorinating_residue")
                .fluid()
                .color(0x33ca33)
                .components(Silicon, 1, Chlorine, 4)
                .iconSet(DULL)
                .buildAndRegister();

        ZirconiumHafniumChloride = builderMaterial("zirconium_hafnium_chloride")
                .fluid()
                .color(0x33ca33)
                .iconSet(DULL)
                .buildAndRegister().setFormula("ZrHfCl₄");

        ZirconiumHafniumOxychloride = builderMaterial("zirconiu_hafnium_oxychloride")
                .fluid()
                .color(0x33ca33)
                .iconSet(DULL)
                .buildAndRegister().setFormula("Cl₂HfOZr");

        HafniumOxide = builderMaterial("hafnium_oxide")
                .dust()
                .color(0x3c3c3c)
                .components(Hafnium, 1, Oxygen, 2)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ZirconiumOxide = builderMaterial("zirconium_oxide")
                .dust()
                .color(0x3c3c3c)
                .components(Zirconium, 1, Oxygen, 2)
                .iconSet(DULL)
                .buildAndRegister();

        HafniumChloride = builderMaterial("hafnium_chloride")
                .dust()
                .color(0x5c5c69)
                .components(Hafnium, 1, Chlorine, 4)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TelluriumOxide = builderMaterial("tellurium_oxide")
                .dust()
                .color(0xd0d0d0)
                .components(Tellurium, 1, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(GLASS)
                .buildAndRegister();

        SodiumEthylate = builderMaterial("sodium_ethylate")
                .dust()
                .color(0x9bcd9b)
                .components(Carbon, 2, Hydrogen, 5, Oxygen, 1, Sodium, 1)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumEthylxanthate = builderMaterial("sodium_ethylxanthate")
                .dust()
                .color(0xcdad00)
                .components(Carbon, 3, Hydrogen, 5, Sodium, 1, Oxygen, 1, Sulfur, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        PotassiumEthylxanthate = builderMaterial("potassium_ethylxanthate")
                .dust()
                .color(0xcdc8b1)
                .components(Carbon, 3, Hydrogen, 5, Potassium, 1, Oxygen, 1, Sulfur, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        PotassiumEthylate = builderMaterial("potassium_ethylate")
                .dust()
                .color(0xcd661d)
                .components(Carbon, 2, Hydrogen, 5, Oxygen, 1, Potassium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        NMPyrolidone = builderMaterial("nmethylpyrolidone")
                .fluid()
                .color(0xd0d0d0)
                .components(Carbon, 5, Hydrogen, 9, Nitrogen, 1, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        GammaButyrolactone = builderMaterial("gammabutyrolactone")
                .fluid()
                .color(0xcccca1)
                .components(Carbon, 4, Hydrogen, 6, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Butane14Diol = builderMaterial("butane_1_4_diol")
                .fluid()
                .color(0xc4534c)
                .components(Carbon, 4, Hydrogen, 10, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Methylamine = builderMaterial("methylamine")
                .fluid()
                .color(0x45486f)
                .components(Carbon, 1, Hydrogen, 5, Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        PPhenylenediamine = builderMaterial("p_phenylenediamine")
                .dust()
                .color(0xdccf52)
                .components(Carbon, 6, Hydrogen, 8, Nitrogen, 2)
                .iconSet(DULL)
                .buildAndRegister();

        PNitroaniline = builderMaterial("p_nitroaniline")
                .fluid()
                .color(0xcc9037)
                .components(Carbon, 6, Hydrogen, 8, Nitrogen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        TerephthalicAcid = builderMaterial("terephthalicacid")
                .fluid()
                .color(0xd6d6d6)
                .components(Carbon, 8, Hydrogen, 6, Hydrogen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        DimethylTerephthalate = builderMaterial("dimethylterephthalate")
                .fluid()
                .color(0xd1d1d1)
                .components(Carbon, 10, Hydrogen, 10, Hydrogen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        TerephthaloylChloride = builderMaterial("terephthaloyl_chloride")
                .dust()
                .color(0x00e60b)
                .components(Carbon, 8, Hydrogen, 4, Chlorine, 2, Nitrogen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Rhugnor = builderMaterial("rhugnor")
                .fluid()
                .color(0xa800e2)
                .element(GTOElements.RHUGNOR)
                .iconSet(METALLIC)
                .buildAndRegister();

        Force = builderMaterial("force")
                .dust()
                .fluid()
                .ore()
                .addOreByproducts(Lanthanum)
                .color(0xdede00)
                .iconSet(BRIGHT)
                .buildAndRegister();

        Tartarite = builderMaterial("tartarite")
                .dust()
                .fluid()
                .ore()
                .addOreByproducts(Americium)
                .color(0xd36232)
                .iconSet(BRIGHT)
                .buildAndRegister();

        HotSodiumPotassium = builderMaterial("hot_sodium_potassium")
                .fluid()
                .color(0x64fcb4)
                .components(Sodium, 1, Potassium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(SAND)
                .buildAndRegister();

        SupercriticalSodiumPotassium = builderMaterial("supercritical_sodium_potassium")
                .fluid()
                .color(0x64fcb4)
                .components(Sodium, 1, Potassium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(LAPIS)
                .buildAndRegister();

        Copper76 = builderMaterial("copper76")
                .dust()
                .element(GTOElements.COPPER76)
                .color(0xe77c56)
                .iconSet(BRIGHT)
                .buildAndRegister();

        CadmiumSulfide = builderMaterial("cadmium_sulfide")
                .dust()
                .components(Cadmium, 1, Sulfur, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xd4ba19)
                .iconSet(DULL)
                .buildAndRegister();

        CadmiumTungstate = builderMaterial("cadmium_tungstate")
                .dust()
                .components(Cadmium, 1, Tungsten, 1, Oxygen, 4)
                .color(0x757770)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        BismuthGermanate = builderMaterial("bismuth_germanate")
                .dust()
                .components(Bismuth, 12, Germanium, 1, Oxygen, 20)
                .color(0x4ea839)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        BismuthNitrateSolution = builderMaterial("bismuth_nitrate_solution")
                .fluid()
                .components(Water, 1, Bismuth, 1, Nitrogen, 3, Oxygen, 9)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xa4a7a8)
                .iconSet(DULL)
                .buildAndRegister();

        Paa = builderMaterial("paa")
                .fluid()
                .components(Carbon, 22, Hydrogen, 14, Nitrogen, 2, Oxygen, 7)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xead05e)
                .iconSet(DULL)
                .buildAndRegister();

        SilicaGelBase = builderMaterial("silica_gel_base")
                .fluid()
                .color(0x39967a)
                .iconSet(DULL)
                .buildAndRegister();

        DeglyceratedSoap = builderMaterial("deglycerated_soap")
                .fluid()
                .color(0xffb000)
                .iconSet(DULL)
                .buildAndRegister();

        Turpentine = builderMaterial("turpentine")
                .fluid()
                .components(Carbon, 10, Hydrogen, 16)
                .color(0x9acd32)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        SteamCrackedTurpentine = builderMaterial("steam_cracked_turpentine")
                .fluid()
                .color(0x8b6914)
                .iconSet(FLUID)
                .buildAndRegister();

        LeachedTurpentine = builderMaterial("leached_turpentine")
                .fluid()
                .components(Carbon, 10, Hydrogen, 16, Concrete, 1)
                .color(0xcd9b1d)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        AlmandineFront = builderMaterial("almandine_front")
                .fluid()
                .components(Almandine, 1)
                .color(0xb22222)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        ChalcopyriteFront = builderMaterial("chalcopyrite_front")
                .fluid()
                .components(Chalcopyrite, 1)
                .color(0xcdaa7d)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        GrossularFront = builderMaterial("grossular_front")
                .fluid()
                .components(Grossular, 1)
                .color(0xd2691e)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        MonaziteFront = builderMaterial("monazite_front")
                .fluid()
                .components(Monazite, 1)
                .color(0x838b83)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        NickelFront = builderMaterial("nickel_front")
                .fluid()
                .components(Nickel, 1)
                .color(0xc1cdcd)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        PlatinumFront = builderMaterial("platinum_front")
                .fluid()
                .components(Platinum, 1)
                .color(0xcdc9a5)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        PyropeFront = builderMaterial("pyrope_front")
                .fluid()
                .components(Pyrope, 1)
                .color(0x8b0000)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        RedstoneFront = builderMaterial("redstone_front")
                .fluid()
                .components(Redstone, 1)
                .color(0xee0000)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        SpessartineFront = builderMaterial("spessartine_front")
                .fluid()
                .components(Spessartine, 1)
                .color(0xee5c42)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        SphaleriteFront = builderMaterial("sphalerite_front")
                .fluid()
                .components(Sphalerite, 1)
                .color(0xeee9e9)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        PentlanditeFront = builderMaterial("pentlandite_front")
                .fluid()
                .components(Pentlandite, 1)
                .color(0xcdaa7d)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        EnrichedNaquadahFront = builderMaterial("enriched_naquadah_front")
                .fluid()
                .components(NaquadahEnriched, 1)
                .color(0x58d00f)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        CarbonDisulfide = builderMaterial("carbon_disulfide")
                .fluid()
                .components(Carbon, 1, Sulfur, 2)
                .color(0x104e8b)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        SpecialCeramics = builderMaterial("special_ceramics")
                .dust()
                .color(0x5c5909)
                .iconSet(DULL)
                .buildAndRegister();

        HydroiodicAcid = builderMaterial("hydroiodic_acid")
                .fluid()
                .components(Hydrogen, 1, Iodine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x0382e2)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        Acrylonitrile = builderMaterial("acrylonitrile")
                .fluid()
                .components(Carbon, 3, Hydrogen, 3, Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xa4a4e1)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        LithiumIodide = builderMaterial("lithium_iodide")
                .dust()
                .components(Lithium, 1, Iodine, 1)
                .color(0xc10014)
                .iconSet(DULL)
                .buildAndRegister();

        SilicaAluminaGel = builderMaterial("silica_alumina_gel")
                .dust()
                .color(0x0c849f)
                .iconSet(DULL)
                .buildAndRegister();

        ZeoliteSievingPellets = builderMaterial("zeolite_sieving_pellets")
                .dust()
                .color(0x4d3e9f)
                .iconSet(DULL)
                .buildAndRegister();

        WetZeoliteSievingPellets = builderMaterial("wet_zeolite_sieving_pellets")
                .dust()
                .color(0x1d173c)
                .iconSet(DULL)
                .buildAndRegister();

        TertButanol = builderMaterial("tert_butanol")
                .fluid()
                .components(Carbon, 4, Hydrogen, 10, Oxygen, 1)
                .color(0xacb500)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        DitertbutylDicarbonate = builderMaterial("ditertbutyl_dicarbonate")
                .dust()
                .components(Carbon, 10, Hydrogen, 18, Oxygen, 5)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x7e96b5)
                .iconSet(DULL)
                .buildAndRegister();

        Tertbuthylcarbonylazide = builderMaterial("tertbuthylcarbonylazide")
                .fluid()
                .components(Carbon, 5, Hydrogen, 9, Nitrogen, 3, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xacb500)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        SodiumToluenesulfonate = builderMaterial("sodium_toluenesulfonate")
                .fluid()
                .components(Carbon, 7, Hydrogen, 7, Sulfur, 3, Oxygen, 3, Sodium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xb5b41d)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        SodiumAzide = builderMaterial("sodium_azide")
                .dust()
                .components(Sodium, 1, Nitrogen, 3)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x1018f0)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumAzanide = builderMaterial("sodium_azanide")
                .dust()
                .components(Sodium, 1, Nitrogen, 1, Hydrogen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x2381b3)
                .iconSet(DULL)
                .buildAndRegister();

        NitrogenPentoxide = builderMaterial("nitrogen_pentoxide")
                .fluid()
                .components(Nitrogen, 2, Oxygen, 5)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x162bb3)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        AminatedFullerene = builderMaterial("aminated_fullerene")
                .fluid()
                .components(Carbon, 60, Hydrogen, 12, Nitrogen, 12)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x3842f0)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        Azafullerene = builderMaterial("azafullerene")
                .fluid()
                .components(Carbon, 60, Hydrogen, 12, Nitrogen, 12)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xb3a500)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        AbsoluteEthanol = builderMaterial("absolute_ethanol")
                .fluid()
                .color(0xff4500)
                .components(Carbon, 2, Hydrogen, 6, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        PiranhaSolution = builderMaterial("piranha_solution")
                .fluid()
                .color(0xac2fff)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        PolyAluminiumChloride = builderMaterial("poly_aluminium_chloride")
                .fluid()
                .color(0xf3ffe5)
                .iconSet(DULL)
                .buildAndRegister();

        FlocculationWasteSolution = builderMaterial("flocculation_waste_solution")
                .fluid()
                .color(0xc7cac1)
                .iconSet(DULL)
                .buildAndRegister();

        PotassiumPyrosulfate = builderMaterial("potassium_pyrosulfate")
                .dust()
                .components(Potassium, 2, Sulfur, 2, Oxygen, 7)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xff9900).iconSet(METALLIC)
                .buildAndRegister();

        PlatinumSlag = builderMaterial("platinum_slag")
                .dust()
                .color(0x343318).iconSet(DULL)
                .buildAndRegister().setFormula("IrOsRhRu??");

        LeachResidue = builderMaterial("leach_residue")
                .dust()
                .color(0x644629).iconSet(DULL)
                .buildAndRegister().setFormula("IrOsRhRu?");

        ZincSulfate = builderMaterial("zinc_sulfate")
                .dust()
                .components(Zinc, 1, Sulfur, 1, Oxygen, 4)
                .color(0x533c1b).iconSet(SAND)
                .buildAndRegister();

        RhodiumNitrate = builderMaterial("rhodium_nitrate")
                .dust()
                .color(0x8C5A0C).iconSet(SAND)
                .flags(DISABLE_DECOMPOSITION)
                .components(Rhodium, 1, Nitrogen, 1, Oxygen, 3, Concrete, 1)
                .buildAndRegister();

        RoughlyRhodiumMetal = builderMaterial("roughly_rhodium_metal")
                .dust()
                .color(0x594C1A).iconSet(SAND)
                .buildAndRegister().setFormula("?Rh?");

        ReprecipitatedRhodium = builderMaterial("reprecipitated_rhodium")
                .dust()
                .color(0xD40849).iconSet(SAND)
                .flags(DISABLE_DECOMPOSITION)
                .components(Rhodium, 1, Nitrogen, 1, Hydrogen, 4)
                .buildAndRegister();

        RhodiumSalt = builderMaterial("rhodium_salt")
                .dust()
                .color(0x61200A).iconSet(SAND)
                .buildAndRegister().setFormula("NaRhCl?");

        RhodiumSaltSolution = builderMaterial("rhodium_salt_solution")
                .fluid()
                .color(0x61200A).iconSet(SAND)
                .buildAndRegister().setFormula("Rh(NaCl)2Cl");

        RhodiumFilterCake = builderMaterial("rhodium_filter_cake")
                .dust()
                .color(0x87350C).iconSet(ROUGH)
                .buildAndRegister().setFormula("?Rh?");

        RhodiumFilterCakeSolution = builderMaterial("rhodium_filter_cake_solution")
                .fluid()
                .color(0x87350C).iconSet(ROUGH)
                .buildAndRegister().setFormula("?Rh?");

        SodiumRutheniate = builderMaterial("sodium_rutheniate")
                .dust()
                .color(0x282C8C).iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .components(Sodium, 2, Oxygen, 4, Ruthenium, 1)
                .buildAndRegister();

        IridiumDioxide = builderMaterial("iridium_dioxide")
                .dust()
                .color(0xA2BFFF).iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .components(Iridium, 1, Oxygen, 2)
                .buildAndRegister();

        RutheniumTetroxideLQ = builderMaterial("ruthenium_tetroxide_lq")
                .fluid()
                .color(0xA8A8A8).iconSet(ROUGH)
                .buildAndRegister();

        SodiumFormate = builderMaterial("sodium_formate")
                .fluid()
                .color(0xf1939c).iconSet(ROUGH)
                .components(Sodium, 1, Carbon, 1, Oxygen, 2, Hydrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        RhodiumSulfateGas = builderMaterial("rhodium_sulfate_gas")
                .gas()
                .color(0xBD8743).iconSet(ROUGH)
                .buildAndRegister();

        AcidicIridium = builderMaterial("acidic_iridium")
                .gas()
                .color(0x634E3A).iconSet(ROUGH)
                .buildAndRegister().setFormula("???");

        RutheniumTetroxideHot = builderMaterial("ruthenium_tetroxide_hot")
                .gas()
                .color(0x9B9B9B).iconSet(ROUGH)
                .buildAndRegister();

        SodiumSulfate = builderMaterial("sodium_sulfate")
                .dust()
                .components(Sodium, 2, Sulfur, 1, Oxygen, 4)
                .color(0xF9F6CF).iconSet(SAND)
                .buildAndRegister();

        MutatedLivingSolder = builderMaterial("mutated_living_solder")
                .fluid()
                .color(0xC18FCC).iconSet(METALLIC)
                .radioactiveHazard(1)
                .buildAndRegister();

        SuperMutatedLivingSolder = builderMaterial("super_mutated_living_solder")
                .fluid()
                .color(0xB662FF).iconSet(METALLIC)
                .radioactiveHazard(2)
                .buildAndRegister();

        HexafluorideEnrichedNaquadahSolution = builderMaterial("hexafluoride_enriched_naquadah_solution")
                .fluid()
                .color(0x868D7F)
                .components(NaquadahEnriched, 1, Fluorine, 6)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        XenonHexafluoroEnrichedNaquadate = builderMaterial("xenon_hexafluoro_enriched_naquadate")
                .fluid()
                .color(0x255A55)
                .components(Xenon, 1, NaquadahEnriched, 1, Fluorine, 6)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GoldTrifluoride = builderMaterial("gold_trifluoride")
                .dust()
                .color(0xE8C478)
                .iconSet(BRIGHT)
                .components(Gold, 1, Fluorine, 3)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        XenoauricFluoroantimonicAcid = builderMaterial("xenoauric_fluoroantimonic_acid")
                .fluid(FluidStorageKeys.LIQUID, new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0xE0BD74)
                .components(Xenon, 1, Gold, 1, Antimony, 1, Fluorine, 6)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GoldChloride = builderMaterial("gold_chloride")
                .fluid()
                .color(0xCCCC66)
                .components(Gold, 2, Chlorine, 6)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        BromineTrifluoride = builderMaterial("bromine_trifluoride")
                .fluid()
                .color(0xA88E57)
                .components(Bromine, 1, Fluorine, 3)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HexafluorideNaquadriaSolution = builderMaterial("hexafluoride_naquadria_solution")
                .fluid()
                .color(0x25C213)
                .components(Naquadria, 1, Fluorine, 6)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        RadonDifluoride = builderMaterial("radon_difluoride")
                .fluid()
                .color(0x8B7EFF)
                .components(Radon, 1, Fluorine, 2)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        RadonNaquadriaOctafluoride = builderMaterial("radon_naquadria_octafluoride")
                .fluid()
                .color(0x85F378)
                .components(Radon, 1, Naquadria, 1, Fluorine, 8)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CaesiumFluoride = builderMaterial("caesium_fluoride")
                .fluid()
                .color(0xFF7A5F)
                .components(Caesium, 1, Fluorine, 1)
                .buildAndRegister();

        XenonTrioxide = builderMaterial("xenon_trioxide")
                .fluid()
                .color(0x359FC3)
                .components(Xenon, 1, Oxygen, 3)
                .buildAndRegister();

        CaesiumXenontrioxideFluoride = builderMaterial("caesium_xenontrioxide_fluoride")
                .fluid()
                .color(0x5067D7)
                .components(Caesium, 1, Xenon, 1, Oxygen, 3, Fluorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        NaquadriaCaesiumXenonnonfluoride = builderMaterial("naquadria_caesium_xenonnonfluoride")
                .fluid()
                .color(0x54C248)
                .components(Naquadria, 1, Caesium, 1, Xenon, 1, Fluorine, 9)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        RadonTrioxide = builderMaterial("radon_trioxide")
                .fluid()
                .color(0x9A6DD7)
                .components(Radon, 1, Oxygen, 3)
                .buildAndRegister();

        NaquadriaCaesiumfluoride = builderMaterial("naquadria_caesiumfluoride")
                .fluid()
                .color(0xAAEB69)
                .components(Naquadria, 1, Fluorine, 3, Caesium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister().setFormula("*Nq*F2CsF", true);

        NitrosoniumOctafluoroxenate = builderMaterial("nitrosonium_octafluoroxenate")
                .fluid()
                .color(0x953D9F)
                .components(NitrogenDioxide, 2, Xenon, 1, Fluorine, 8)
                .buildAndRegister().setFormula("(NO2)2XeF8", true);

        NitrylFluoride = builderMaterial("nitryl_fluoride")
                .fluid()
                .color(0x8B7EFF)
                .components(Nitrogen, 1, Oxygen, 2, Fluorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AcidicNaquadriaCaesiumfluoride = builderMaterial("acidic_naquadria_caesiumfluoride")
                .fluid()
                .color(0x75EB00)
                .components(Naquadria, 1, Fluorine, 3, Caesium, 1, Sulfur, 2, Oxygen, 8)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister().setFormula("*Nq*F2CsF(SO4)2", true);

        SupercriticalSteam = builderMaterial("supercritical_steam")
                .gas(new FluidBuilder().temperature(1000))
                .color(0xC4C4C4)
                .components(Water, 1)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        WaterAgarMix = builderMaterial("water_agar_mix")
                .fluid()
                .color(0x88FFC0)
                .buildAndRegister();

        TungstenTrioxide = builderMaterial("tungsten_trioxide")
                .dust()
                .components(Tungsten, 1, Oxygen, 3)
                .color(0x86FF75).iconSet(ROUGH)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SpaceTime = builderMaterial("spacetime")
                .ingot()
                .liquid(new FluidBuilder().temperature(1).customStill())
                .fluidPipeProperties(2147483647, GTOConfigHolder.INSTANCE.spacetimePip, true, true, true, true)
                .element(GTOElements.SPACETIME)
                .iconSet(new MaterialIconSet("spacetime"))
                .flags(GTOMaterialFlags.GENERATE_NANOSWARM, NO_UNIFICATION)
                .cableProperties(Integer.MAX_VALUE, 524288, 0, true)
                .buildAndRegister();

        Infinity = builderMaterial("infinity")
                .ingot()
                .liquid(new FluidBuilder().temperature(1000000).customStill())
                .blastTemp(32000, HIGHEST)
                .element(GTOElements.INFINITY)
                .iconSet(new MaterialIconSet("infinity"))
                .flags(GENERATE_FRAME)
                .cableProperties(Integer.MAX_VALUE, 8192, 0, true)
                .buildAndRegister();

        CompoundTriniite = builderMaterial("trinium_compound")
                .dust()
                .ore()
                .components(Trinium, 3, Actinium, 3, Selenium, 4, Astatine, 4)
                .color(0x675989).iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CrystallineNitricAcid = builderMaterial("crystalline_nitric_acid")
                .dust()
                .components(Hydrogen, 1, Nitrogen, 1, Oxygen, 3)
                .color(0xCDBD14)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SodiumChlorate = builderMaterial("sodium_chlorate")
                .dust()
                .components(Sodium, 1, Chlorine, 1, Oxygen, 3)
                .color(0xA5A5A5)
                .buildAndRegister();

        SodiumPerchlorate = builderMaterial("sodium_perchlorate")
                .dust()
                .components(Sodium, 1, Chlorine, 1, Oxygen, 4)
                .color(0xF0F0F0)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ActiniumTriniumHydroxides = builderMaterial("actinium_trinium_hydroxides")
                .dust()
                .color(0xAD47CF).iconSet(ROUGH)
                .buildAndRegister().setFormula("Ke3Ac2(OH)12");

        SeleniumOxide = builderMaterial("selenium_oxide")
                .dust()
                .components(Selenium, 1, Oxygen, 2)
                .color(0xFFF71C).iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TriniumTetrafluoride = builderMaterial("trinium_tetrafluoride")
                .dust()
                .color(0x529E57)
                .components(Trinium, 1, Fluorine, 4)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Fluorocarborane = builderMaterial("fluorocarborane")
                .dust()
                .components(Hydrogen, 1, Carbon, 1, Hydrogen, 1, Boron, 11, Fluorine, 11)
                .color(0x00EC80).iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CaesiumNitrate = builderMaterial("caesium_nitrate")
                .dust()
                .components(Caesium, 1, Nitrogen, 1, Oxygen, 3)
                .color(0x821EC7).iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CesiumCarborane = builderMaterial("cesium_carborane")
                .dust()
                .components(Caesium, 1, Carbon, 1, Boron, 11, Hydrogen, 12)
                .color(0xAE6EDA).iconSet(SAND)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SilverIodide = builderMaterial("silver_iodide")
                .dust()
                .components(Silver, 1, Iodine, 1)
                .color(0xACA56A).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SilverNitrate = builderMaterial("silver_nitrate")
                .dust()
                .components(Silver, 1, Nitrogen, 1, Oxygen, 3)
                .color(0xFFFCE0).iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TrifluoroaceticPhosphateEster = builderMaterial("trifluoroacetic_phosphate_ester")
                .dust()
                .components(Carbon, 8, Hydrogen, 5, Fluorine, 3, Oxygen, 2, Sulfur, 1)
                .color(0xA99F45).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        RadiumNitrate = builderMaterial("radium_nitrate")
                .dust()
                .color(0xCD40DA).iconSet(SAND)
                .buildAndRegister().setFormula("Ra(NO3)2");

        ActiniumNitrate = builderMaterial("actinium_nitrate")
                .dust()
                .color(0xDAE0EE).iconSet(DULL)
                .buildAndRegister().setFormula("Ac(NO3)3");

        PotassiumFluoride = builderMaterial("potassium_fluoride")
                .dust()
                .components(Potassium, 1, Fluorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xB9B9B9).iconSet(BRIGHT)
                .buildAndRegister();

        SodiumHydride = builderMaterial("sodium_hydride")
                .dust()
                .components(Sodium, 1, Hydrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x757475).iconSet(DULL)
                .buildAndRegister();

        CesiumCarboranePrecursor = builderMaterial("cesium_carborane_precursor")
                .dust()
                .color(0xBA5C69).iconSet(DULL)
                .buildAndRegister().setFormula("CsB10H12CN(CH3)3Cl");

        LithiumAluminiumHydride = builderMaterial("lithium_aluminium_hydride")
                .dust()
                .components(Lithium, 1, Aluminium, 1, Hydrogen, 1)
                .color(0xABD7DF).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        LithiumAluminiumFluoride = builderMaterial("lithium_aluminium_fluoride")
                .dust()
                .components(Aluminium, 1, Fluorine, 4, Lithium, 1)
                .color(0xAD1F58).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AluminiumTrifluoride = builderMaterial("aluminium_trifluoride")
                .dust()
                .components(Aluminium, 1, Fluorine, 3)
                .color(0xB8601A).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SodiumAluminiumHydride = builderMaterial("sodium_aluminium_hydride")
                .dust()
                .components(Sodium, 1, Aluminium, 1, Hydrogen, 4)
                .color(0x87BFBF).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AluminiumHydride = builderMaterial("aluminium_hydride")
                .dust()
                .components(Aluminium, 1, Hydrogen, 3)
                .color(0x315C6E).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Alumina = builderMaterial("alumina")
                .dust()
                .components(Aluminium, 2, Oxygen, 3)
                .color(0x1D4759).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CaesiumHydroxide = builderMaterial("caesium_hydroxide")
                .dust()
                .components(Caesium, 1, Oxygen, 1, Hydrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xD7D7D7).iconSet(DULL)
                .buildAndRegister();

        Decaborane = builderMaterial("decaborane")
                .dust()
                .components(Boron, 10, Hydrogen, 14)
                .color(0x95B78F).iconSet(SAND)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SodiumTetrafluoroborate = builderMaterial("sodium_tetrafluoroborate")
                .dust()
                .components(Sodium, 1, Boron, 1, Fluorine, 4)
                .color(0xA6640F).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SodiumBorohydride = builderMaterial("sodium_borohydride")
                .dust()
                .components(Sodium, 1, Boron, 1, Hydrogen, 4)
                .color(0x9AB0B2).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        PhosphorusPentasulfide = builderMaterial("phosphorus_pentasulfide")
                .dust()
                .components(Phosphorus, 4, Sulfur, 10)
                .color(0xE7A123).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        StoneDustResidue = builderMaterial("stone_dust_residue")
                .dust()
                .color(0x585858).iconSet(DULL)
                .buildAndRegister();

        AmmoniumBifluoride = builderMaterial("ammonium_bifluoride")
                .dust()
                .components(Nitrogen, 1, Hydrogen, 4, Hydrogen, 1, Fluorine, 2)
                .color(0x26C6BB).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        UncommonResidues = builderMaterial("uncommon_residues")
                .dust()
                .color(0x3F46BD).iconSet(SAND)
                .buildAndRegister();

        PartiallyOxidizedResidues = builderMaterial("partially_oxidized_residues")
                .dust()
                .color(0x8F1515).iconSet(SAND)
                .buildAndRegister();

        InertResidues = builderMaterial("inert_residues")
                .dust()
                .color(0x4F4863).iconSet(BRIGHT)
                .buildAndRegister();

        OxidizedResidues = builderMaterial("oxidized_residues")
                .dust()
                .color(0x330D4A).iconSet(SAND)
                .buildAndRegister();

        HeavyOxidizedResidues = builderMaterial("heavy_oxidized_residues")
                .dust()
                .color(0x310D48).iconSet(SAND)
                .buildAndRegister();

        CleanInertResidues = builderMaterial("clean_inert_residues")
                .dust()
                .color(0x187F4D).iconSet(BRIGHT)
                .buildAndRegister();

        MetallicResidues = builderMaterial("metallic_residues")
                .dust()
                .color(0x205A53).iconSet(SAND)
                .buildAndRegister();

        HeavyMetallicResidues = builderMaterial("heavy_metallic_residues")
                .dust()
                .color(0x1C0986).iconSet(SAND)
                .buildAndRegister();

        DiamagneticResidues = builderMaterial("diamagnetic_residues")
                .dust()
                .color(0x30845E).iconSet(SAND)
                .buildAndRegister();

        ParamagneticResidues = builderMaterial("paramagnetic_residues")
                .dust()
                .color(0x25788B).iconSet(SAND)
                .buildAndRegister();

        FerromagneticResidues = builderMaterial("ferromagnetic_residues")
                .dust()
                .color(0x1F5D46).iconSet(SAND)
                .buildAndRegister();

        HeavyDiamagneticResidues = builderMaterial("heavy_diamagnetic_residues")
                .dust()
                .color(0x22316A).iconSet(SAND)
                .buildAndRegister();

        HeavyParamagneticResidues = builderMaterial("heavy_paramagnetic_residues")
                .dust()
                .color(0x1A8E2F).iconSet(SAND)
                .buildAndRegister();

        HeavyFerromagneticResidues = builderMaterial("heavy_ferromagnetic_residues")
                .dust()
                .color(0x26743E).iconSet(SAND)
                .buildAndRegister();

        ExoticHeavyResidues = builderMaterial("exotic_heavy_residues")
                .dust()
                .color(0x3E8496).iconSet(BRIGHT)
                .buildAndRegister();

        FumingNitricAcid = builderMaterial("fuming_nitric_acid")
                .fluid()
                .components(Hydrogen, 1, Nitrogen, 1, Oxygen, 3)
                .color(0xB46800).iconSet(GTOMaterialIconSet.LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Perfluorobenzene = builderMaterial("perfluorobenzene")
                .fluid()
                .components(Carbon, 6, Fluorine, 6)
                .color(0x15752B).iconSet(GTOMaterialIconSet.LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Trimethylsilane = builderMaterial("trimethylsilane")
                .fluid()
                .components(Carbon, 3, Hydrogen, 10, Silicon, 1)
                .color(0x7D2FC3).iconSet(GTOMaterialIconSet.LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Trimethylchlorosilane = builderMaterial("trimethylchlorosilane")
                .fluid()
                .color(0x591399).iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister().setFormula("(CH3)3SiCl");

        NitratedTriniiteCompoundSolution = builderMaterial("nitrated_triniite_compound_solution")
                .fluid()
                .color(0x5E9699).iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        ResidualTriniiteSolution = builderMaterial("residual_triniite_solution")
                .fluid()
                .color(0x68B59).iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        ActiniumRadiumHydroxideSolution = builderMaterial("actinium_radium_hydroxide_solution")
                .fluid()
                .color(0xC3E0DC).iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        ActiniumRadiumNitrateSolution = builderMaterial("actinium_radium_nitrate_solution")
                .fluid()
                .color(0x89C0B3).iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        HeavilyFluorinatedTriniumSolution = builderMaterial("heavily_fluorinated_trinium_solution")
                .fluid()
                .color(0x169A33).iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        EthyleneSulfide = builderMaterial("ethylene_sulfide")
                .fluid()
                .components(Carbon, 6, Hydrogen, 6, Sulfur, 1, Oxygen, 1)
                .color(0x868544).iconSet(GTOMaterialIconSet.LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        EthylTrifluoroacetate = builderMaterial("ethyl_trifluoroacetate")
                .fluid()
                .components(Carbon, 4, Hydrogen, 5, Fluorine, 3, Oxygen, 2)
                .color(0x93A658).iconSet(GTOMaterialIconSet.LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AcetylChloride = builderMaterial("acetyl_chloride")
                .fluid()
                .components(Carbon, 2, Hydrogen, 3, Oxygen, 1, Chlorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xD1B117)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        KryptonDifluoride = builderMaterial("krypton_difluoride")
                .fluid()
                .components(Krypton, 1, Fluorine, 2)
                .color(0x3FF12B).iconSet(GTOMaterialIconSet.LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        MoltenCalciumSalts = builderMaterial("molten_calcium_salts")
                .fluid()
                .color(0x7F478B)
                .iconSet(DULL)
                .buildAndRegister();

        Trimethylamine = builderMaterial("trimethylamine")
                .fluid()
                .color(0xCEA67D).iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister().setFormula("(CH3)3N");

        BoraneDimethylSulfide = builderMaterial("borane_dimethyl_sulfide")
                .fluid()
                .color(0xCEA67D).iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister().setFormula("(BH3)(CH3)2S");

        Tetrahydrofuran = builderMaterial("tetrahydrofuran")
                .fluid()
                .color(0x86E4CE).iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister().setFormula("(CH2)4O");

        Diborane = builderMaterial("diborane")
                .fluid()
                .color(0xBEEBCD).iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister().setFormula("(BH3)2");

        DiethylEther = builderMaterial("diethyl_ether")
                .fluid()
                .color(0x9AB0B2).iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister().setFormula("(C2H5)2O");

        BoronTrifluorideAcetate = builderMaterial("boron_trifluoride_acetate")
                .fluid()
                .color(0x991062).iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister().setFormula("(BF3)(C2H5)2O");

        SodiumHexafluoroaluminate = builderMaterial("sodium_hexafluoroaluminate")
                .fluid()
                .components(Sodium, 3, Aluminium, 1, Fluorine, 6)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xA47732).iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        DirtyHexafluorosilicicAcid = builderMaterial("dirty_hexafluorosilicic_acid")
                .fluid()
                .color(0xDA387D).iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister().setFormula("H2SiF6?");

        DiluteHexafluorosilicicAcid = builderMaterial("dilute_hexafluorosilicic_acid")
                .fluid()
                .color(0x49DF68).iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister().setFormula("(H2O)2(H2SiF6)");

        FluorosilicicAcid = builderMaterial("fluorosilicic_acid")
                .fluid()
                .components(Hydrogen, 2, Silicon, 1, Fluorine, 6)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x49BF61).iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        AmmoniumFluoride = builderMaterial("ammonium_fluoride")
                .fluid()
                .components(Nitrogen, 1, Hydrogen, 4, Fluorine, 1)
                .color(0xB80926).iconSet(GTOMaterialIconSet.LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AmmoniumBifluorideSolution = builderMaterial("ammonium_bifluoride_solution")
                .fluid()
                .color(0xCBC5B7).iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister().setFormula("(H2O)NH4FHF");

        SodiumHydroxideSolution = builderMaterial("sodium_hydroxide_solution")
                .fluid()
                .color(0x000791).iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister().setFormula("(H2O)NaOH");

        RedMud = builderMaterial("red_mud")
                .fluid()
                .color(0x972903).iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister().setFormula("HCl?");

        NeutralisedRedMud = builderMaterial("neutralised_red_mud")
                .fluid()
                .color(0x972903).iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister().setFormula("Fe??");

        RedSlurry = builderMaterial("red_slurry")
                .fluid()
                .color(0x982902).iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister().setFormula("TiO2?");

        FerricReeChloride = builderMaterial("ferric_ree_chloride")
                .fluid()
                .color(0x68680D).iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister().setFormula("Fe?");

        TitanylSulfate = builderMaterial("titanyl_sulfate")
                .fluid()
                .color(0xF82296).iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister().setFormula("TiO(SO4)");

        DioxygenDifluoride = builderMaterial("dioxygen_difluoride")
                .fluid()
                .components(Fluorine, 1, Oxygen, 1, Oxygen, 1, Fluorine, 1)
                .color(0x18BFB9).iconSet(GTOMaterialIconSet.LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        OxidizedResidualSolution = builderMaterial("oxidized_residual_solution")
                .fluid()
                .color(0x2CD3CB).iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        HeliumIIIHydride = builderMaterial("helium_iii_hydride")
                .fluid()
                .color(0x9F9F02).iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister().setFormula("He_3H");

        UltraacidicResidueSolution = builderMaterial("ultraacidic_residue_solution")
                .fluid()
                .color(0x848C9A).iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        XenicAcid = builderMaterial("xenic_acid")
                .fluid()
                .components(Hydrogen, 2, Xenon, 1, Oxygen, 4)
                .color(0x443A76).iconSet(GTOMaterialIconSet.LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        DiluteHydrofluoricAcid = builderMaterial("dilute_hydrofluoric_acid")
                .fluid()
                .color(0x049821).iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister().setFormula("(H2O)(HF)");

        TritiumHydride = builderMaterial("tritium_hydride")
                .fluid()
                .components(Hydrogen, 1, Tritium, 1)
                .color(0xBA0202).iconSet(GTOMaterialIconSet.LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        DustyLiquidHeliumIII = builderMaterial("dusty_liquid_helium_iii")
                .fluid()
                .components(Concrete, 1, Helium3, 1)
                .color(0x774060).iconSet(GTOMaterialIconSet.LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Ozone = builderMaterial("ozone")
                .fluid()
                .components(Oxygen, 3)
                .color(0x0176C4).iconSet(GTOMaterialIconSet.LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HydrogenPeroxide = builderMaterial("hydrogen_peroxide")
                .fluid()
                .components(Hydrogen, 2, Oxygen, 2)
                .color(0xC8FFFF).iconSet(GTOMaterialIconSet.LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        RareEarthChlorides = builderMaterial("rare_earth_chlorides")
                .fluid()
                .components(Concrete, 1, Chlorine, 1)
                .color(0xBDB76B).iconSet(GTOMaterialIconSet.LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Fluorite = builderMaterial("fluorite")
                .fluid()
                .dust()
                .components(Calcium, 1, Fluorine, 2)
                .color(0x18B400).iconSet(DULL)
                .buildAndRegister();

        // wanggugu'sLanthanidetreatment
        SamariumRefinedPowder = builderMaterial("samarium_refined_powder")
                .dust()
                .color(0x8a6d7d)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("??Sm??");

        SamariumRrareEearthTurbidLiquid = builderMaterial("samarium_rare_earth_turbid_liquid")
                .fluid()
                .color(0xcfc883)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        MonaziteRareEarthTurbidLiquid = builderMaterial("monazite_rare_earth_turbid_liquid")
                .fluid()
                .color(0x81624c)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        ThoritePowder = builderMaterial("thorite_powder")
                .dust()
                .components(Thorium, 1, Silicon, 1, Oxygen, 4)
                .color(0x7D7D7D)
                .iconSet(SAND)
                .buildAndRegister();

        DilutedMonaziteSlurry = builderMaterial("diluted_monazite_slurry")
                .fluid()
                .color(0xBDBDBD)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        RedZirconPowder = builderMaterial("red_zircon_powder")
                .dust()
                .components(Zirconium, 1, Silicon, 1, Oxygen, 4)
                .color(0xFF4500)
                .iconSet(SAND)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        MonaziteSulfatePowder = builderMaterial("monazite_sulfate_powder")
                .dust()
                .color(0xCCCCCC)
                .iconSet(SAND)
                .buildAndRegister();

        DilutedMonaziteSulfateSolution = builderMaterial("diluted_monazite_sulfate_solution")
                .fluid()
                .color(0xADD8E6)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        AcidicMonazitePowder = builderMaterial("acidic_monazite_powder")
                .dust()
                .color(0x9ACD32)
                .iconSet(SAND)
                .buildAndRegister();

        ThoriumPhosphateFilterCakePowder = builderMaterial("thorium_phosphate_filter_cake_powder")
                .dust()
                .color(0x808080)
                .iconSet(SAND)
                .buildAndRegister();

        ThoriumPhosphateRefinedPowder = builderMaterial("thorium_phosphate_refined_powder")
                .dust()
                .color(0x545454)
                .iconSet(SAND)
                .buildAndRegister().setFormula("??ThP??", true);

        MonaziteRareEarthFilterResiduePowder = builderMaterial("monazite_rare_earth_filter_residue_powder")
                .dust()
                .color(0x7F7F7F)
                .iconSet(SAND)
                .buildAndRegister();

        NeutralizedMonaziteRareEarthFilterResiduePowder = builderMaterial("neutralized_monazite_rare_earth_filter_residue_powder")
                .dust()
                .color(0x969696)
                .iconSet(SAND)
                .buildAndRegister();

        UraniumFilterResiduePowder = builderMaterial("uranium_filter_residue_powder")
                .dust()
                .color(0x545454)
                .iconSet(SAND)
                .buildAndRegister();

        NeutralizedUraniumFilterResiduePowder = builderMaterial("neutralized_uranium_filter_residue_powder")
                .dust()
                .color(0x7F7F7F)
                .iconSet(SAND)
                .buildAndRegister();

        ConcentratedMonaziteRareEarthHydroxidePowder = builderMaterial("concentrated_monazite_rare_earth_hydroxide_powder")
                .dust()
                .color(0xC0C0C0)
                .iconSet(SAND)
                .buildAndRegister();

        DriedConcentratedNitricMonaziteRareEarthPowder = builderMaterial("dried_concentrated_nitric_monazite_rare_earth_powder")
                .dust()
                .color(0x969696)
                .iconSet(SAND)
                .buildAndRegister();

        ConcentratedNitrideMonaziteRareEarthSolution = builderMaterial("concentrated_nitride_monazite_rare_earth_solution")
                .fluid()
                .color(0xADD8E6)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        CeriumRichMixturePowder = builderMaterial("cerium_rich_mixture_powder")
                .dust()
                .color(0x808080)
                .iconSet(SAND)
                .buildAndRegister().setFormula("??Ce??");

        CeriumChloridePowder = builderMaterial("cerium_chloride_powder")
                .dust()
                .components(Cerium, 1, Chlorine, 3)
                .color(0x808080)
                .iconSet(SAND)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        OxalicAcid = builderMaterial("oxalic_acid")
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .components(Carbon, 2, Hydrogen, 2, Oxygen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xA0FFA0)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        VanadiumPentoxidePowder = builderMaterial("vanadium_pentoxide_powder")
                .dust()
                .components(Vanadium, 2, Oxygen, 5)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x8B0000)
                .iconSet(SAND)
                .buildAndRegister();

        CeriumOxalatePowder = builderMaterial("cerium_oxalate_powder")
                .dust()
                .components(Cerium, 1, Carbon, 2, Hydrogen, 2, Oxygen, 4)
                .color(0x969696)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(SAND)
                .buildAndRegister();

        ConcentratedCeriumChlorideSolution = builderMaterial("concentrated_cerium_chloride_solution")
                .fluid()
                .components(Cerium, 1, Chlorine, 3)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x00FFFF)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        NitricLeachateFromMonazite = builderMaterial("nitric_leachate_from_monazite")
                .fluid()
                .color(0xADD8E6)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        ConcentratedNitricLeachateFromMonazite = builderMaterial("concentrated_nitric_leachate_from_monazite")
                .fluid()
                .color(0x00FFFF)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        CoolingConcentratedNitricMonaziteRareEarthPowder = builderMaterial("cooling_concentrated_nitric_monazite_rare_earth_powder")
                .dust()
                .color(0xC0C0C0)
                .iconSet(SAND)
                .buildAndRegister();

        MonaziteRareEarthPrecipitatePowder = builderMaterial("monazite_rare_earth_precipitate_powder")
                .dust()
                .color(0x808080)
                .iconSet(SAND)
                .buildAndRegister();

        HeterogeneousHalideMonaziteRareEarthMixturePowder = builderMaterial("heterogeneous_halide_monazite_rare_earth_mixture_powder")
                .dust()
                .color(0x808080)
                .iconSet(SAND)
                .buildAndRegister();

        SaturatedMonaziteRareEarthPowder = builderMaterial("saturated_monazite_rare_earth_powder")
                .dust()
                .color(0xFFFFFF)
                .iconSet(SAND)
                .buildAndRegister();

        SamariumPrecipitatePowder = builderMaterial("samarium_precipitate_powder")
                .dust()
                .components(Samarium, 2, Gadolinium, 1)
                .color(0x969696)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(SAND)
                .buildAndRegister();

        ConcentratedRareEarthChlorideSolution = builderMaterial("concentrated_rare_earth_chloride_solution")
                .fluid()
                .color(0x00BFFF)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        EnrichedRareEarthChlorideSolution = builderMaterial("enriched_rare_earth_chloride_solution")
                .fluid()
                .color(0x87CEFA)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        DilutedRareEarthChlorideSolution = builderMaterial("diluted_rare_earth_chloride_solution")
                .fluid()
                .color(0xADD8E6)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        SamariumRareEarthSlurry = builderMaterial("samarium_rare_earth_slurry")
                .fluid()
                .color(0x808080)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        NeodymiumRareEarthConcentratePowder = builderMaterial("neodymium_rare_earth_concentrate_powder")
                .dust()
                .color(0x969696)
                .iconSet(SAND)
                .buildAndRegister();

        SamariumRareEarthDilutedSolution = builderMaterial("samarium_rare_earth_diluted_solution")
                .fluid()
                .components(Samarium, 1, Chlorine, 1, Water, 2)
                .color(0xA9A9A9)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        SamariumOxalateWithImpurities = builderMaterial("samarium_oxalate_with_impurities")
                .dust()
                .components(Samarium, 1, Carbon, 2, Oxygen, 6)
                .color(0x808080)
                .iconSet(SAND)
                .buildAndRegister();

        SamariumChlorideWithImpurities = builderMaterial("samarium_chloride_with_impurities")
                .dust()
                .fluid()
                .components(Samarium, 1, Chlorine, 3)
                .color(0xB0B0B0)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SamariumChlorideSodiumChlorideMixturePowder = builderMaterial("samarium_chloride_sodium_chloride_mixture_powder")
                .dust()
                .components(Samarium, 1, Chlorine, 3, Sodium, 1, Chlorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xC0C0C0)
                .iconSet(SAND)
                .buildAndRegister();

        PhosphorusFreeSamariumConcentratePowder = builderMaterial("phosphorus_free_samarium_concentrate_powder")
                .dust()
                .components(Samarium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xD3D3D3)
                .iconSet(SAND)
                .buildAndRegister();

        SamariumChlorideConcentrateSolution = builderMaterial("samarium_chloride_concentrate_solution")
                .fluid()
                .components(Samarium, 1, Chlorine, 3, Water, 5)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xB0C4DE)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        LanthanumChloride = builderMaterial("lanthanum_chloride")
                .dust()
                .components(Lanthanum, 1, Chlorine, 3)
                .color(0xFFFFFF)
                .iconSet(SAND)
                .buildAndRegister();

        LanthanumChlorideWithImpurities = builderMaterial("lanthanum_chloride_with_impurities")
                .dust()
                .color(0xE0E0E0)
                .iconSet(SAND)
                .buildAndRegister();

        FluoroCarbonLanthanideCeriumSolution = builderMaterial("fluoro_carbon_lanthanide_cerium_solution")
                .fluid()
                .color(0x3888d2)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        SteamCrackedFluoroCarbonLanthanideSlurry = builderMaterial("steam_cracked_fluoro_carbon_lanthanide_slurry")
                .fluid()
                .color(0x7F7FFF)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        ModulatedFluoroCarbonLanthanideSlurry = builderMaterial("modulated_fluoro_carbon_lanthanide_slurry")
                .fluid()
                .color(0x5A5AFF)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        DilutedFluoroCarbonLanthanideSlurry = builderMaterial("diluted_fluoro_carbon_lanthanide_slurry")
                .fluid()
                .color(0x9696FF)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        FilteredFluoroCarbonLanthanideSlurry = builderMaterial("filtered_fluoro_carbon_lanthanide_slurry")
                .fluid()
                .color(0xA0A0FF)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        FluoroCarbonLanthanideCeriumOxidePowder = builderMaterial("fluoro_carbon_lanthanide_cerium_oxide_powder")
                .dust()
                .color(0x7F7F7F)
                .iconSet(GTOMaterialIconSet.BRIGHT)
                .buildAndRegister();

        AcidLeachedFluoroCarbonLanthanideCeriumOxidePowder = builderMaterial("acid_leached_fluoro_carbon_lanthanide_cerium_oxide_powder")
                .dust()
                .color(0x555555)
                .iconSet(GTOMaterialIconSet.BRIGHT)
                .buildAndRegister();

        CalcinedRareEarthOxidePowder = builderMaterial("calcined_rare_earth_oxide_powder")
                .dust()
                .color(0x969696)
                .iconSet(GTOMaterialIconSet.BRIGHT)
                .buildAndRegister();

        WetRareEarthOxidePowder = builderMaterial("wet_rare_earth_oxide_powder")
                .dust()
                .color(0x6699CC)
                .iconSet(GTOMaterialIconSet.BRIGHT)
                .buildAndRegister();

        CeriumOxideRareEarthOxidePowder = builderMaterial("cerium_oxide_rare_earth_oxide_powder")
                .dust()
                .components(Cerium, 1, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xD2D2D2)
                .iconSet(GTOMaterialIconSet.BRIGHT)
                .buildAndRegister();

        FluoroCarbonLanthanideCeriumRareEarthOxidePowder = builderMaterial("fluoro_carbon_lanthanide_cerium_rare_earth_oxide_powder")
                .dust()
                .color(0x7F7F7F)
                .iconSet(GTOMaterialIconSet.BRIGHT)
                .buildAndRegister();

        NitridedFluoroCarbonLanthanideCeriumRareEarthOxideSolution = builderMaterial("nitrided_fluoro_carbon_lanthanide_cerium_rare_earth_oxide_solution")
                .fluid()
                .color(0x9999FF)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        FluoroCarbonLanthanideCeriumRareEarthSuspension = builderMaterial("fluoro_carbon_lanthanide_cerium_rare_earth_suspension")
                .fluid()
                .color(0xBEBEBE)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        SamariumRareEarthConcentratePowder = builderMaterial("samarium_rare_earth_concentrate_powder")
                .dust()
                .color(0x808080)
                .iconSet(GTOMaterialIconSet.BRIGHT)
                .buildAndRegister();

        FluorinatedSamariumConcentratePowder = builderMaterial("fluorinated_samarium_concentrate_powder")
                .dust()
                .color(0x969696)
                .iconSet(GTOMaterialIconSet.BRIGHT)
                .buildAndRegister();

        SamariumTerbiumMixturePowder = builderMaterial("samarium_terbium_mixture_powder")
                .dust()
                .color(0x969696)
                .iconSet(GTOMaterialIconSet.BRIGHT)
                .buildAndRegister();

        NitridedSamariumTerbiumMixturePowder = builderMaterial("nitrided_samarium_terbium_mixture_powder")
                .dust()
                .color(0x7F7F7F)
                .iconSet(GTOMaterialIconSet.BRIGHT)
                .buildAndRegister();

        TerbiumNitratePowder = builderMaterial("terbium_nitrate_powder")
                .dust()
                .color(0x0080FF)
                .components(Terbium, 1, Nitrogen, 1, Oxygen, 3)
                .iconSet(GTOMaterialIconSet.BRIGHT)
                .buildAndRegister();

        PromethiumOxide = builderMaterial("promethium_oxide")
                .dust()
                .color(0xcfcfcf)
                .components(Promethium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CarbonTetrachloride = builderMaterial("carbon_tetrachloride")
                .fluid()
                .color(0x0E9000)
                .components(Carbon, 1, Chlorine, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        ActiniumOxalate = builderMaterial("actinium_oxalate")
                .dust()
                .color(0xcfcfcf)
                .iconSet(GLASS)
                .buildAndRegister().setFormula("Ac(CO₂)₄");

        EthylHexanol = builderMaterial("ethyl_hexanol")
                .fluid()
                .components(Carbon, 8, Hydrogen, 18, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xFFFFCC)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        P507 = builderMaterial("p507")
                .fluid()
                .components(Carbon, 18, Hydrogen, 36, Phosphorus, 1, Oxygen, 3)
                .color(0xFFFF00)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        LanthanumExtractionNanoResin = builderMaterial("lanthanum_extraction_nano_resin")
                .fluid()
                .color(0x808080)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        CeriumExtractionNanoResin = builderMaterial("cerium_extraction_nano_resin")
                .fluid()
                .color(0x969696)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        PraseodymiumExtractionNanoResin = builderMaterial("praseodymium_extraction_nano_resin")
                .fluid()
                .color(0xA0A0A0)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        NeodymiumExtractionNanoResin = builderMaterial("neodymium_extraction_nano_resin")
                .fluid()
                .color(0xB0B0B0)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        PromethiumExtractionNanoResin = builderMaterial("promethium_extraction_nano_resin")
                .fluid()
                .color(0xC0C0C0)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        SamariumExtractionNanoResin = builderMaterial("samarium_extraction_nano_resin")
                .fluid()
                .color(0xD0D0D0)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        EuropiumExtractionNanoResin = builderMaterial("europium_extraction_nano_resin")
                .fluid()
                .color(0xE0E0E0)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        GadoliniumExtractionNanoResin = builderMaterial("gadolinium_extraction_nano_resin")
                .fluid()
                .color(0xF0F0F0)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        TerbiumExtractionNanoResin = builderMaterial("terbium_extraction_nano_resin")
                .fluid()
                .color(0x0080FF)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        DysprosiumExtractionNanoResin = builderMaterial("dysprosium_extraction_nano_resin")
                .fluid()
                .color(0x00FFFF)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        HolmiumExtractionNanoResin = builderMaterial("holmium_extraction_nano_resin")
                .fluid()
                .color(0x00FF00)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ErbiumExtractionNanoResin = builderMaterial("erbium_extraction_nano_resin")
                .fluid()
                .color(0xFF00FF)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        ThuliumExtractionNanoResin = builderMaterial("thulium_extraction_nano_resin")
                .fluid()
                .color(0xFF0000)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        YtterbiumExtractionNanoResin = builderMaterial("ytterbium_extraction_nano_resin")
                .fluid()
                .color(0xFFFF00)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        LutetiumExtractionNanoResin = builderMaterial("lutetium_extraction_nano_resin")
                .fluid()
                .color(0x800080)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        ScandiumExtractionNanoResin = builderMaterial("scandium_extraction_nano_resin")
                .fluid()
                .color(0x808000)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        YttriumExtractionNanoResin = builderMaterial("yttrium_extraction_nano_resin")
                .fluid()
                .color(0x008000)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        LanthanumExtractedNanoResin = builderMaterial("lanthanum_extracted_nano_resin")
                .fluid()
                .color(0x808080)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        CeriumExtractedNanoResin = builderMaterial("cerium_extracted_nano_resin")
                .fluid()
                .color(0x969696)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        PraseodymiumExtractedNanoResin = builderMaterial("praseodymium_extracted_nano_resin")
                .fluid()
                .color(0xA0A0A0)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        NeodymiumExtractedNanoResin = builderMaterial("neodymium_extracted_nano_resin")
                .fluid()
                .color(0xB0B0B0)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        PromethiumExtractedNanoResin = builderMaterial("promethium_extracted_nano_resin")
                .fluid()
                .color(0xC0C0C0)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        SamariumExtractedNanoResin = builderMaterial("samarium_extracted_nano_resin")
                .fluid()
                .color(0xD0D0D0)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        EuropiumExtractedNanoResin = builderMaterial("europium_extracted_nano_resin")
                .fluid()
                .color(0xE0E0E0)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        GadoliniumExtractedNanoResin = builderMaterial("gadolinium_extracted_nano_resin")
                .fluid()
                .color(0xF0F0F0)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        TerbiumExtractedNanoResin = builderMaterial("terbium_extracted_nano_resin")
                .fluid()
                .color(0x0080FF)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        DysprosiumExtractedNanoResin = builderMaterial("dysprosium_extracted_nano_resin")
                .fluid()
                .color(0x00FFFF)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        HolmiumExtractedNanoResin = builderMaterial("holmium_extracted_nano_resin")
                .fluid()
                .color(0x00FF00)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        ErbiumExtractedNanoResin = builderMaterial("erbium_extracted_nano_resin")
                .fluid()
                .color(0xFF00FF)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        ThuliumExtractedNanoResin = builderMaterial("thulium_extracted_nano_resin")
                .fluid()
                .color(0xFF0000)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        YtterbiumExtractedNanoResin = builderMaterial("ytterbium_extracted_nano_resin")
                .fluid()
                .color(0xFFFF00)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        LutetiumExtractedNanoResin = builderMaterial("lutetium_extracted_nano_resin")
                .fluid()
                .color(0x800080)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        ScandiumExtractedNanoResin = builderMaterial("scandium_extracted_nano_resin")
                .fluid()
                .color(0x808000)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        YttriumExtractedNanoResin = builderMaterial("yttrium_extracted_nano_resin")
                .fluid()
                .color(0x008000)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        FluorosulfuricAcid = builderMaterial("fluorosulfuric_acid")
                .fluid()
                .color(0xffffff)
                .components(Fluorine, 1, Hydrogen, 1, Oxygen, 3, Sulfur, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister();

        MagicAcid = builderMaterial("magic_acid")
                .fluid()
                .color(0xffffff)
                .iconSet(GTOMaterialIconSet.LIMPID)
                .buildAndRegister().setFormula("SbF₅·HSO₃F");
    }
}
