package com.gto.gtocore.common.data.material;

import com.gto.gtocore.api.data.chemical.material.info.GTOMaterialFlags;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.*;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;
import com.gregtechceu.gtceu.api.fluids.FluidBuilder;
import com.gregtechceu.gtceu.api.fluids.FluidState;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;

import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;

public final class GTMaterialAdd {

    public static void init() {
        Iron.setProperty(PropertyKey.TOOL, ToolProperty.Builder.of(2.0F, 2.0F, 256, 2, GTToolType.MINING_HAMMER, GTToolType.SPADE, GTToolType.SAW, GTToolType.HARD_HAMMER, GTToolType.WRENCH, GTToolType.FILE, GTToolType.CROWBAR, GTToolType.SCREWDRIVER, GTToolType.WIRE_CUTTER, GTToolType.SCYTHE, GTToolType.KNIFE, GTToolType.BUTCHERY_KNIFE, GTToolType.DRILL_LV, GTToolType.DRILL_MV, GTToolType.DRILL_HV, GTToolType.DRILL_EV, GTToolType.DRILL_IV, GTToolType.CHAINSAW_LV, GTToolType.WRENCH_LV, GTToolType.WRENCH_HV, GTToolType.WRENCH_IV, GTToolType.BUZZSAW, GTToolType.SCREWDRIVER_LV, GTToolType.WIRE_CUTTER_LV, GTToolType.WIRE_CUTTER_HV, GTToolType.WIRE_CUTTER_IV).enchantability(14).addTypes(GTToolType.MORTAR).build());
        Platinum.setProperty(BLAST, new BlastProperty(1810));
        RedAlloy.addFlags(GENERATE_SPRING_SMALL);
        PhosphorusPentoxide.addFlags(GTOMaterialFlags.GENERATE_CATALYST);
        Iodine.addFlags(GTOMaterialFlags.GENERATE_CATALYST);
        Magnesium.addFlags(GTOMaterialFlags.GENERATE_CATALYST);
        Hafnium.addFlags(GTOMaterialFlags.GENERATE_CATALYST);
        Bismuth.addFlags(GTOMaterialFlags.GENERATE_CATALYST);
        Tin.addFlags(GTOMaterialFlags.GENERATE_CATALYST);
        Rhenium.addFlags(GTOMaterialFlags.GENERATE_CATALYST);
        Zinc.addFlags(GTOMaterialFlags.GENERATE_CATALYST);
        Nickel.addFlags(GTOMaterialFlags.GENERATE_CATALYST);
        Copper.addFlags(GTOMaterialFlags.GENERATE_CATALYST);
        OsmiumTetroxide.addFlags(GTOMaterialFlags.GENERATE_CATALYST);
        Ferrosilite.addFlags(GTOMaterialFlags.GENERATE_CATALYST);
        Silver.addFlags(GTOMaterialFlags.GENERATE_CATALYST);
        Platinum.addFlags(GTOMaterialFlags.GENERATE_CATALYST);
        Palladium.addFlags(GTOMaterialFlags.GENERATE_CATALYST);
        RhodiumPlatedPalladium.addFlags(GTOMaterialFlags.GENERATE_CATALYST);
        HastelloyC276.addFlags(GENERATE_ROTOR);
        Steel.addFlags(GTOMaterialFlags.GENERATE_COMPONENT);
        Aluminium.addFlags(GTOMaterialFlags.GENERATE_COMPONENT);
        StainlessSteel.addFlags(GTOMaterialFlags.GENERATE_COMPONENT);
        Titanium.addFlags(GTOMaterialFlags.GENERATE_COMPONENT);
        TungstenSteel.addFlags(GTOMaterialFlags.GENERATE_COMPONENT);
        HSSS.addFlags(GTOMaterialFlags.GENERATE_COMPONENT);
        Osmiridium.addFlags(GTOMaterialFlags.GENERATE_COMPONENT);
        Tritanium.addFlags(GTOMaterialFlags.GENERATE_COMPONENT, GENERATE_ROTOR);
        Monazite.addFlags(DISABLE_DECOMPOSITION);
        Bastnasite.addFlags(DISABLE_DECOMPOSITION);
        PotassiumSulfate.setComponents(new MaterialStack(Potassium, 2), new MaterialStack(Sulfur, 1), new MaterialStack(Oxygen, 4));
        AmmoniumChloride.setComponents(new MaterialStack(Nitrogen, 1), new MaterialStack(Hydrogen, 4), new MaterialStack(Chlorine, 1));
        Duranium.addFlags(GENERATE_FRAME);
        Naquadria.addFlags(GENERATE_FRAME);
        Trinium.addFlags(GENERATE_FRAME);
        Copper.addFlags(GENERATE_SMALL_GEAR);
        Europium.getProperty(BLAST).setBlastTemperature(9602);
        Neutronium.addFlags(GENERATE_ROTOR, GENERATE_SPRING, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_SPRING_SMALL);
        Neutronium.setProperty(WIRE, new WireProperties((int) GTValues.V[GTValues.UIV], 2, 64));
        Mendelevium.setProperty(WIRE, new WireProperties((int) GTValues.V[GTValues.UHV], 4, 16));
        RutheniumTetroxide.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        RutheniumTetroxide.addFlags(NO_UNIFICATION);
        Actinium.setProperty(DUST, new DustProperty());
        Actinium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Selenium.setProperty(DUST, new DustProperty());
        Aluminium.addFlags(GENERATE_ROTOR);
        Neutronium.addFlags(GTOMaterialFlags.GENERATE_NANITES);
        Carbon.setProperty(INGOT, new IngotProperty());
        RadAway.addFlags(DISABLE_DECOMPOSITION);
        Platinum.addFlags(GENERATE_SPRING);
        Chromium.addFlags(GENERATE_GEAR);
        Naquadah.addFlags(GTOMaterialFlags.GENERATE_NANITES, GENERATE_FRAME);
        NaquadahAlloy.addFlags(GENERATE_FINE_WIRE);
        RutheniumTriniumAmericiumNeutronate.addFlags(GENERATE_FINE_WIRE);
        TitaniumTungstenCarbide.addFlags(GENERATE_GEAR);
        Europium.addFlags(GENERATE_SPRING_SMALL);
        Germanium.setProperty(INGOT, new IngotProperty());
        Germanium.addFlags(GENERATE_PLATE);
        Germanium.setProperty(BLAST, new BlastProperty(3800, BlastProperty.GasTier.MID, GTValues.VA[GTValues.HV], 700, 120, 140));
        Sodium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Seaborgium.setProperty(INGOT, new IngotProperty());
        Seaborgium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Seaborgium.addFlags(GENERATE_PLATE);
        Dubnium.setProperty(INGOT, new IngotProperty());
        Dubnium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Dubnium.addFlags(GENERATE_FINE_WIRE);
        Rhenium.setProperty(INGOT, new IngotProperty());
        Rhenium.addFlags(GENERATE_PLATE);
        Rhenium.setProperty(BLAST, new BlastProperty(7400, BlastProperty.GasTier.MID, GTValues.VA[GTValues.EV], 900, 480, 240));
        Calcium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Calcium.setProperty(INGOT, new IngotProperty());
        Praseodymium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Praseodymium.setProperty(DUST, new DustProperty());
        Gadolinium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Gadolinium.setProperty(DUST, new DustProperty());
        Terbium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Terbium.setProperty(DUST, new DustProperty());
        Dysprosium.setProperty(DUST, new DustProperty());
        Dysprosium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Holmium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Holmium.setProperty(DUST, new DustProperty());
        Erbium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Erbium.setProperty(DUST, new DustProperty());
        Thulium.setProperty(DUST, new DustProperty());
        Thulium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Ytterbium.setProperty(DUST, new DustProperty());
        Ytterbium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Scandium.setProperty(DUST, new DustProperty());
        Scandium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        NaquadahEnriched.addFlags(GENERATE_FRAME);
        NickelZincFerrite.addFlags(GENERATE_FOIL);
        Promethium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Promethium.setProperty(DUST, new DustProperty());
        Polonium.setProperty(INGOT, new IngotProperty());
        Polonium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Polonium.setProperty(BLAST, new BlastProperty(3700, BlastProperty.GasTier.MID, GTValues.VA[GTValues.IV], 400, 120, 160));
        Hafnium.setProperty(INGOT, new IngotProperty());
        Hafnium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Hafnium.setProperty(BLAST, new BlastProperty(6800, BlastProperty.GasTier.MID, GTValues.VA[GTValues.HV], 1100, 480, 180));
        EnderEye.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        EnderPearl.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Protactinium.setProperty(DUST, new DustProperty());
        Protactinium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Neptunium.setProperty(DUST, new DustProperty());
        Neptunium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Curium.setProperty(INGOT, new IngotProperty());
        Curium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Berkelium.setProperty(INGOT, new IngotProperty());
        Berkelium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Einsteinium.setProperty(INGOT, new IngotProperty());
        Einsteinium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Californium.setProperty(INGOT, new IngotProperty());
        Californium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Fermium.setProperty(INGOT, new IngotProperty());
        Fermium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Mendelevium.setProperty(INGOT, new IngotProperty());
        Mendelevium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Oganesson.setProperty(INGOT, new IngotProperty());
        Oganesson.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Oganesson.addFlags(GENERATE_PLATE);
        Flerovium.setProperty(INGOT, new IngotProperty());
        Flerovium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Nobelium.setProperty(INGOT, new IngotProperty());
        Lawrencium.setProperty(INGOT, new IngotProperty());
        Nobelium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Lawrencium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Hassium.setProperty(INGOT, new IngotProperty());
        Hassium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Hassium.addFlags(GENERATE_PLATE);
        Rubidium.setProperty(DUST, new DustProperty());
        Rubidium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Technetium.setProperty(INGOT, new IngotProperty());
        Technetium.setProperty(BLAST, new BlastProperty(5600, BlastProperty.GasTier.MID, GTValues.VA[GTValues.HV], 800, 120, 240));
        Technetium.addFlags(GENERATE_PLATE);
        Francium.setProperty(DUST, new DustProperty());
        Strontium.setProperty(DUST, new DustProperty());
        Zirconium.setProperty(DUST, new DustProperty());
        Radium.setProperty(DUST, new DustProperty());
        Radium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Tellurium.setProperty(DUST, new DustProperty());
        Tellurium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Rutherfordium.setProperty(DUST, new DustProperty());
        Astatine.setProperty(INGOT, new IngotProperty());
        Astatine.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Bohrium.setProperty(INGOT, new IngotProperty());
        Bohrium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Thallium.setProperty(DUST, new DustProperty());
        Meitnerium.setProperty(DUST, new DustProperty());
        Meitnerium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Roentgenium.setProperty(INGOT, new IngotProperty());
        Copernicium.setProperty(INGOT, new IngotProperty());
        Nihonium.setProperty(INGOT, new IngotProperty());
        Moscovium.setProperty(INGOT, new IngotProperty());
        Livermorium.setProperty(INGOT, new IngotProperty());
        Tennessine.setProperty(INGOT, new IngotProperty());
        Roentgenium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Copernicium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Nihonium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Moscovium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Livermorium.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Tennessine.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        ReinforcedEpoxyResin.addFlags(GENERATE_ROD);
        Phosphorus.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Tellurium.setProperty(ORE, new OreProperty());
        Rubidium.setProperty(ORE, new OreProperty());
        Rubidium.getProperty(ORE).setOreByProducts(Caesium, Potassium, Sodium);
        Titanium.setProperty(ORE, new OreProperty());
        Titanium.getProperty(ORE).setOreByProducts(Aluminium, Iron);
        Tungsten.setProperty(ORE, new OreProperty());
        Tungsten.getProperty(ORE).setOreByProducts(Lithium, Calcium);
        Indium.setProperty(ORE, new OreProperty());
        Indium.getProperty(ORE).setOreByProducts(Aluminium, Zinc);
        NaquadahEnriched.setProperty(ORE, new OreProperty());
        NaquadahEnriched.getProperty(ORE).setOreByProducts(Naquadah, Sulfur);
        EchoShard.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Graphite.setProperty(INGOT, new IngotProperty());
        VanadiumSteel.addFlags(GENERATE_FRAME);
        Lutetium.setProperty(INGOT, new IngotProperty());
        Carbon.addFlags(GTOMaterialFlags.GENERATE_NANITES);
        Iron.addFlags(GTOMaterialFlags.GENERATE_NANITES);
        Glowstone.addFlags(GTOMaterialFlags.GENERATE_NANITES);
        Copper.addFlags(GTOMaterialFlags.GENERATE_NANITES);
        Silver.addFlags(GTOMaterialFlags.GENERATE_NANITES);
        Gold.addFlags(GTOMaterialFlags.GENERATE_NANITES);
        Iridium.addFlags(GTOMaterialFlags.GENERATE_NANITES);
        Osmium.addFlags(GTOMaterialFlags.GENERATE_NANITES);
        Rhenium.addFlags(GTOMaterialFlags.GENERATE_NANITES);
        Nickel.addFlags(GTOMaterialFlags.GENERATE_MILLED);
        Platinum.addFlags(GTOMaterialFlags.GENERATE_MILLED);
        Almandine.addFlags(GTOMaterialFlags.GENERATE_MILLED);
        Grossular.addFlags(GTOMaterialFlags.GENERATE_MILLED);
        Pyrope.addFlags(GTOMaterialFlags.GENERATE_MILLED);
        Spessartine.addFlags(GTOMaterialFlags.GENERATE_MILLED);
        Chalcopyrite.addFlags(GTOMaterialFlags.GENERATE_MILLED);
        Pentlandite.addFlags(GTOMaterialFlags.GENERATE_MILLED);
        Sphalerite.addFlags(GTOMaterialFlags.GENERATE_MILLED);
        Monazite.addFlags(GTOMaterialFlags.GENERATE_MILLED);
        Redstone.addFlags(GTOMaterialFlags.GENERATE_MILLED);
        NaquadahEnriched.addFlags(GTOMaterialFlags.GENERATE_MILLED);
        Titanium.addFlags(GENERATE_FINE_WIRE);
        Titanium.getProperty(FLUID).enqueueRegistration(FluidStorageKeys.PLASMA, (new FluidBuilder()).state(FluidState.PLASMA));
        Boron.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Boron.getProperty(FLUID).enqueueRegistration(FluidStorageKeys.PLASMA, (new FluidBuilder()).state(FluidState.PLASMA));
        Sulfur.setProperty(FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
        Sulfur.getProperty(FLUID).enqueueRegistration(FluidStorageKeys.PLASMA, (new FluidBuilder()).state(FluidState.PLASMA));
        Calcium.getProperty(FLUID).enqueueRegistration(FluidStorageKeys.PLASMA, (new FluidBuilder()).state(FluidState.PLASMA));
        Zinc.getProperty(FLUID).enqueueRegistration(FluidStorageKeys.PLASMA, (new FluidBuilder()).state(FluidState.PLASMA));
        Niobium.getProperty(FLUID).enqueueRegistration(FluidStorageKeys.PLASMA, (new FluidBuilder()).state(FluidState.PLASMA));
        Tin.getProperty(FLUID).enqueueRegistration(FluidStorageKeys.PLASMA, (new FluidBuilder()).state(FluidState.PLASMA));
        Lead.getProperty(FLUID).enqueueRegistration(FluidStorageKeys.PLASMA, (new FluidBuilder()).state(FluidState.PLASMA));
        Silver.getProperty(FLUID).enqueueRegistration(FluidStorageKeys.PLASMA, (new FluidBuilder()).state(FluidState.PLASMA));
        Thorium.getProperty(FLUID).enqueueRegistration(FluidStorageKeys.PLASMA, (new FluidBuilder()).state(FluidState.PLASMA));
        Xenon.getProperty(FLUID).enqueueRegistration(FluidStorageKeys.PLASMA, (new FluidBuilder()).state(FluidState.PLASMA));
        Neon.getProperty(FLUID).enqueueRegistration(FluidStorageKeys.PLASMA, (new FluidBuilder()).state(FluidState.PLASMA));
        // Naquadah.setProperty(HAZARD, new HazardProperty(HazardProperty.HazardTrigger.ANY,
        // GTMedicalConditions.CARCINOGEN, 1, true));
        // NaquadahEnriched.setProperty(HAZARD, new HazardProperty(HazardProperty.HazardTrigger.ANY,
        // GTMedicalConditions.CARCINOGEN, 2, true));
        // Seaborgium.setProperty(HAZARD, new HazardProperty(HazardProperty.HazardTrigger.ANY,
        // GTMedicalConditions.CARCINOGEN, 2, true));
        // Dubnium.setProperty(HAZARD, new HazardProperty(HazardProperty.HazardTrigger.ANY,
        // GTMedicalConditions.CARCINOGEN, 1, true));
        // Praseodymium.setProperty(HAZARD, new HazardProperty(HazardProperty.HazardTrigger.ANY,
        // GTMedicalConditions.CARCINOGEN, 1, true));
        // Promethium.setProperty(HAZARD, new HazardProperty(HazardProperty.HazardTrigger.ANY,
        // GTMedicalConditions.CARCINOGEN, 1, true));
        // Polonium.setProperty(HAZARD, new HazardProperty(HazardProperty.HazardTrigger.ANY,
        // GTMedicalConditions.CARCINOGEN, 4, true));
        // Protactinium.setProperty(HAZARD, new HazardProperty(HazardProperty.HazardTrigger.ANY,
        // GTMedicalConditions.CARCINOGEN, 1, true));
        // Neptunium.setProperty(HAZARD, new HazardProperty(HazardProperty.HazardTrigger.ANY,
        // GTMedicalConditions.CARCINOGEN, 1, true));
        // Curium.setProperty(HAZARD, new HazardProperty(HazardProperty.HazardTrigger.ANY,
        // GTMedicalConditions.CARCINOGEN, 1, true));
        // Berkelium.setProperty(HAZARD, new HazardProperty(HazardProperty.HazardTrigger.ANY,
        // GTMedicalConditions.CARCINOGEN, 1, true));
        // Einsteinium.setProperty(HAZARD, new HazardProperty(HazardProperty.HazardTrigger.ANY,
        // GTMedicalConditions.CARCINOGEN, 1, true));
        // Californium.setProperty(HAZARD, new HazardProperty(HazardProperty.HazardTrigger.ANY,
        // GTMedicalConditions.CARCINOGEN, 1, true));
        // Fermium.setProperty(HAZARD, new HazardProperty(HazardProperty.HazardTrigger.ANY,
        // GTMedicalConditions.CARCINOGEN, 1, true));
        // Mendelevium.setProperty(HAZARD, new HazardProperty(HazardProperty.HazardTrigger.ANY,
        // GTMedicalConditions.CARCINOGEN, 1, true));
        // Oganesson.setProperty(HAZARD, new HazardProperty(HazardProperty.HazardTrigger.ANY,
        // GTMedicalConditions.CARCINOGEN, 2, true));
        // Flerovium.setProperty(HAZARD, new HazardProperty(HazardProperty.HazardTrigger.ANY,
        // GTMedicalConditions.CARCINOGEN, 2, true));
        // Nobelium.setProperty(HAZARD, new HazardProperty(HazardProperty.HazardTrigger.ANY,
        // GTMedicalConditions.CARCINOGEN, 2, true));
        // Lawrencium.setProperty(HAZARD, new HazardProperty(HazardProperty.HazardTrigger.ANY,
        // GTMedicalConditions.CARCINOGEN, 2, true));
        // Hassium.setProperty(HAZARD, new HazardProperty(HazardProperty.HazardTrigger.ANY,
        // GTMedicalConditions.CARCINOGEN, 2, true));
        // Rubidium.setProperty(HAZARD, new HazardProperty(HazardProperty.HazardTrigger.ANY,
        // GTMedicalConditions.CARCINOGEN, 1, true));
        // Thallium.setProperty(HAZARD, new HazardProperty(HazardProperty.HazardTrigger.INHALATION,
        // GTMedicalConditions.CARCINOGEN, 2, true));
        // Meitnerium.setProperty(HAZARD, new HazardProperty(HazardProperty.HazardTrigger.ANY,
        // GTMedicalConditions.CARCINOGEN, 2, true));
        // Roentgenium.setProperty(HAZARD, new HazardProperty(HazardProperty.HazardTrigger.ANY,
        // GTMedicalConditions.CARCINOGEN, 2, true));
        // Copernicium.setProperty(HAZARD, new HazardProperty(HazardProperty.HazardTrigger.ANY,
        // GTMedicalConditions.CARCINOGEN, 2, true));
        // Nihonium.setProperty(HAZARD, new HazardProperty(HazardProperty.HazardTrigger.ANY,
        // GTMedicalConditions.CARCINOGEN, 2, true));
        // Moscovium.setProperty(HAZARD, new HazardProperty(HazardProperty.HazardTrigger.ANY,
        // GTMedicalConditions.CARCINOGEN, 2, true));
        // Livermorium.setProperty(HAZARD, new HazardProperty(HazardProperty.HazardTrigger.ANY,
        // GTMedicalConditions.CARCINOGEN, 2, true));
        // Tennessine.setProperty(HAZARD, new HazardProperty(HazardProperty.HazardTrigger.ANY,
        // GTMedicalConditions.CARCINOGEN, 2, true));
    }
}
