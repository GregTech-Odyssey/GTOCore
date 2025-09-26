package com.gtocore.data.recipe.classified;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.*;
import com.gtocore.common.data.machines.GTAEMachines;
import com.gtocore.common.data.machines.GeneratorMultiblock;
import com.gtocore.common.data.machines.MultiBlockG;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.*;
import com.gregtechceu.gtceu.common.data.machines.GTResearchMachines;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import com.glodblock.github.extendedae.common.EPPItemAndBlock;
import com.hepdd.gtmthings.data.WirelessMachines;
import vazkii.botania.common.item.BotaniaItems;

import java.util.Locale;

import static com.gregtechceu.gtceu.api.GTValues.LuV;
import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.ASSEMBLER_RECIPES;

final class AssemblerA {

    public static void init() {
        ASSEMBLER_RECIPES.builder("control_hatch")
                .inputItems(GTMachines.HULL[GTValues.MV].asStack())
                .inputItems(GTItems.COVER_MACHINE_CONTROLLER.asStack())
                .inputItems("enderio:redstone_sensor_filter")
                .inputFluids(GTMaterials.RedAlloy, 288)
                .outputItems(GTMachines.CONTROL_HATCH.getItem())
                .EUt(30)
                .duration(200)
                .save();

        ASSEMBLER_RECIPES.builder("heat_vacuum_interface")
                .inputItems(GTMachines.HULL[GTValues.LV].asStack())
                .inputItems(GTOItems.AIR_VENT)
                .inputItems(TagPrefix.rotor, GTMaterials.Copper, 2)
                .inputItems(TagPrefix.pipeSmallFluid, GTMaterials.Steel, 2)
                .outputItems(GTOMachines.TEMP_VACUUM_INTERFACE)
                .inputFluids(GTMaterials.TinAlloy, 288)
                .EUt(7)
                .duration(100)
                .save();

        ASSEMBLER_RECIPES.builder("wireless_charger_cover")
                .inputItems("gtmthings:mv_wireless_energy_receive_cover")
                .inputItems(GTMachines.CHARGER_4[GTValues.MV].asStack())
                .inputItems(GTItems.EMITTER_MV.asStack())
                .inputItems(CustomTags.MV_CIRCUITS, 2)
                .outputItems(GTOItems.WIRELESS_CHARGER_COVER.asItem())
                .inputFluids(GTMaterials.SolderingAlloy, 288)
                .EUt(120)
                .duration(200)
                .save();

        ASSEMBLER_RECIPES.builder("thermal_conductor_hatch")
                .inputItems(GTMachines.HULL[GTValues.LuV].asStack())
                .inputItems(TagPrefix.ingot, GTOMaterials.BasicMFPC, 16)
                .inputItems(TagPrefix.pipeHugeRestrictive, GTMaterials.SterlingSilver, 8)
                .inputItems(GTItems.ROBOT_ARM_LuV.asStack(8))
                .outputItems(GTOMachines.THERMAL_CONDUCTOR_HATCH.asStack())
                .inputFluids(GTMaterials.SodiumPotassium, 2880)
                .EUt(VA[LuV])
                .duration(200)
                .save();

        ASSEMBLER_RECIPES.builder("basic_monitor")
                .inputItems(GTBlocks.CASING_STEEL_SOLID)
                .inputItems(GTItems.COVER_SCREEN)
                .inputItems(GTItems.SENSOR_LV.asStack(2))
                .outputItems(GTOMachines.BASIC_MONITOR)
                .EUt(15)
                .duration(100)
                .save();

        ASSEMBLER_RECIPES.builder("monitor_elec")
                .inputItems(GTOMachines.BASIC_MONITOR)
                .inputItems(WirelessMachines.WIRELESS_ENERGY_MONITOR)
                .outputItems(GTOMachines.MONITOR_MACHINE_ELECTRICITY)
                .EUt(15)
                .duration(100)
                .save();

        ASSEMBLER_RECIPES.builder("monitor_mana")
                .inputItems(GTOMachines.BASIC_MONITOR)
                .inputItems(BotaniaItems.manaMirror)
                .outputItems(GTOMachines.MONITOR_MACHINE_MANA)
                .EUt(15)
                .duration(100)
                .save();

        ASSEMBLER_RECIPES.builder("monitor_cwu")
                .inputItems(GTOMachines.BASIC_MONITOR)
                .inputItems(GTResearchMachines.HPCA_BRIDGE_COMPONENT)
                .outputItems(GTOMachines.MONITOR_MACHINE_CWU)
                .EUt(15)
                .duration(100)
                .save();

        ASSEMBLER_RECIPES.builder("monitor_custom")
                .inputItems(GTOMachines.BASIC_MONITOR)
                .inputItems(Items.NAME_TAG)
                .outputItems(GTOMachines.MONITOR_MACHINE_CUSTOM)
                .EUt(15)
                .duration(100)
                .save();

        ASSEMBLER_RECIPES.builder("monitor_ae_throughput")
                .inputItems(GTOMachines.BASIC_MONITOR)
                .inputItems(GTOAEParts.INSTANCE.getEXCHANGE_STORAGE_MONITOR().get().asItem())
                .outputItems(GTOMachines.MONITOR_AE_THROUGHPUT)
                .EUt(15)
                .duration(100)
                .save();
        ASSEMBLER_RECIPES.builder("monitor_ae_cpu")
                .inputItems(GTOMachines.BASIC_MONITOR)
                .inputItems(AEBlocks.CRAFTING_MONITOR.asItem())
                .outputItems(GTOMachines.MONITOR_AE_CPU)
                .EUt(15)
                .duration(100)
                .save();

        ASSEMBLER_RECIPES.builder("monitor_machine")
                .inputItems(GTOMachines.BASIC_MONITOR)
                .inputItems(GTItems.PORTABLE_SCANNER)
                .outputItems(GTOMachines.MONITOR_MACHINE)
                .EUt(15)
                .duration(100)
                .save();

        ASSEMBLER_RECIPES.builder("wood_frame_fix")
                .inputItems(Items.STICK, 4)
                .circuitMeta(8)
                .outputItems(frameGt, Wood, 1)
                .EUt(7)
                .duration(64)
                .save();

        ASSEMBLER_RECIPES.builder("advanced_tesseract_generator")
                .inputItems(GTOMachines.TESSERACT_GENERATOR.asStack(4))
                .inputItems(GTItems.FIELD_GENERATOR_HV.asStack(2))
                .inputItems(GTItems.QUANTUM_STAR.asStack(1))
                .outputItems(GTOMachines.ADVANCED_TESSERACT_GENERATOR.asStack())
                .EUt(1920)
                .duration(200)
                .save();

        ASSEMBLER_RECIPES.builder("me_big_storage_access_hatch")
                .inputItems(GTAEMachines.ME_STORAGE_ACCESS_HATCH.asStack())
                .inputItems("expatternprovider:oversize_interface", 16)
                .inputItems(new ItemStack(AEItems.CAPACITY_CARD.asItem(), 32))
                .inputItems(new ItemStack(AEItems.SPEED_CARD.asItem(), 32))
                .inputItems(GTItems.FIELD_GENERATOR_IV.asStack())
                .outputItems(GTAEMachines.ME_BIG_STORAGE_ACCESS_HATCH.asStack())
                .EUt(7680)
                .duration(400)
                .save();

        ASSEMBLER_RECIPES.builder("me_io_storage_access_hatch")
                .inputItems(GTAEMachines.ME_STORAGE_ACCESS_HATCH.asStack())
                .inputItems(EPPItemAndBlock.EX_IO_PORT, 32)
                .inputItems(new ItemStack(AEItems.SPEED_CARD.asItem(), 64))
                .inputItems(GTItems.FIELD_GENERATOR_IV.asStack(4))
                .inputItems(GTItems.FIELD_GENERATOR_LuV.asStack(4))
                .outputItems(GTAEMachines.ME_IO_STORAGE_ACCESS_HATCH.asStack())
                .EUt(VA[LuV])
                .duration(400)
                .save();

        ASSEMBLER_RECIPES.builder("crafting_cpu_interface")
                .inputItems(GTMachines.HULL[GTValues.HV].asStack())
                .inputItems("expatternprovider:oversize_interface")
                .inputItems("ae2:storage_bus", 4)
                .inputItems(GTItems.SENSOR_HV.asStack(4))
                .outputItems(GTAEMachines.CRAFTING_CPU_INTERFACE.asStack())
                .EUt(480)
                .duration(400)
                .save();

        ASSEMBLER_RECIPES.builder("me_cpu")
                .inputItems(new ItemStack(AEBlocks.CRAFTING_MONITOR.block().asItem()))
                .inputItems(GTItems.QUBIT_CENTRAL_PROCESSING_UNIT.asStack(32))
                .inputItems(GTItems.TOOL_DATA_STICK.asStack(16))
                .inputItems(GTItems.EMITTER_EV.asStack(8))
                .inputItems(GTItems.SENSOR_EV.asStack(8))
                .inputItems(CustomTags.IV_CIRCUITS, 16)
                .inputItems(TagPrefix.plateDouble, GTMaterials.Titanium, 32)
                .outputItems(MultiBlockG.ME_CPU.asStack())
                .EUt(1920)
                .duration(800)
                .save();

        ASSEMBLER_RECIPES.builder("t1_crafting_storage_core")
                .inputItems(new ItemStack(AEBlocks.CRAFTING_UNIT.block().asItem()))
                .inputItems(GTOBlocks.T1_ME_STORAGE_CORE.asStack())
                .inputItems(new ItemStack(AEBlocks.CRAFTING_ACCELERATOR.block().asItem()))
                .inputItems(new ItemStack(AEItems.ADVANCED_CARD.asItem(), 4))
                .outputItems(GTOBlocks.T1_CRAFTING_STORAGE_CORE.asStack())
                .EUt(1920)
                .duration(200)
                .save();

        ASSEMBLER_RECIPES.builder("t2_crafting_storage_core")
                .inputItems(new ItemStack(AEBlocks.CRAFTING_UNIT.block().asItem()))
                .inputItems(GTOBlocks.T2_ME_STORAGE_CORE.asStack())
                .inputItems(new ItemStack(AEBlocks.CRAFTING_ACCELERATOR.block().asItem()))
                .inputItems(new ItemStack(AEItems.ADVANCED_CARD.asItem(), 4))
                .outputItems(GTOBlocks.T2_CRAFTING_STORAGE_CORE.asStack())
                .EUt(7680)
                .duration(200)
                .save();

        ASSEMBLER_RECIPES.builder("t3_crafting_storage_core")
                .inputItems(new ItemStack(AEBlocks.CRAFTING_UNIT.block().asItem()))
                .inputItems(GTOBlocks.T3_ME_STORAGE_CORE.asStack())
                .inputItems(new ItemStack(AEBlocks.CRAFTING_ACCELERATOR.block().asItem()))
                .inputItems(new ItemStack(AEItems.ADVANCED_CARD.asItem(), 4))
                .outputItems(GTOBlocks.T3_CRAFTING_STORAGE_CORE.asStack())
                .EUt(30720)
                .duration(200)
                .save();

        ASSEMBLER_RECIPES.builder("t4_crafting_storage_core")
                .inputItems(new ItemStack(AEBlocks.CRAFTING_UNIT.block().asItem()))
                .inputItems(GTOBlocks.T4_ME_STORAGE_CORE.asStack())
                .inputItems(new ItemStack(AEBlocks.CRAFTING_ACCELERATOR.block().asItem()))
                .inputItems(new ItemStack(AEItems.ADVANCED_CARD.asItem(), 4))
                .outputItems(GTOBlocks.T4_CRAFTING_STORAGE_CORE.asStack())
                .EUt(122880)
                .duration(200)
                .save();

        ASSEMBLER_RECIPES.builder("t5_crafting_storage_core")
                .inputItems(new ItemStack(AEBlocks.CRAFTING_UNIT.block().asItem()))
                .inputItems(GTOBlocks.T5_ME_STORAGE_CORE.asStack())
                .inputItems(new ItemStack(AEBlocks.CRAFTING_ACCELERATOR.block().asItem()))
                .inputItems(new ItemStack(AEItems.ADVANCED_CARD.asItem(), 4))
                .outputItems(GTOBlocks.T5_CRAFTING_STORAGE_CORE.asStack())
                .EUt(491520)
                .duration(200)
                .save();

        ASSEMBLER_RECIPES.builder("high_pressure_pipe_casing")
                .inputItems(frameGt, Titanium)
                .inputItems(TagPrefix.pipeNormalFluid, Titanium, 4)
                .inputItems(TagPrefix.plate, TungstenSteel, 4)
                .outputItems(GTOBlocks.HIGH_PRESSURE_PIPE_CASING.asStack())
                .inputFluids(GTOMaterials.HighPressureNitrogen, 576)
                .EUt(480)
                .duration(200)
                .save();

        ASSEMBLER_RECIPES.builder("chemical_corrosion_resistant_pipe_casing")
                .inputItems(TagPrefix.frameGt, StainlessSteel)
                .inputItems(TagPrefix.pipeNormalFluid, StainlessSteel, 4)
                .inputItems(TagPrefix.plate, Polytetrafluoroethylene, 4)
                .inputItems(TagPrefix.plate, StainlessSteel, 4)
                .outputItems(GTOBlocks.CHEMICAL_CORROSION_RESISTANT_PIPE_CASING.asStack())
                .inputFluids(Polytetrafluoroethylene, 576)
                .EUt(480)
                .duration(200)
                .save();

        ASSEMBLER_RECIPES.builder("oil_gas_transportation_pipe_casing")
                .inputItems(TagPrefix.frameGt, HSSG)
                .inputItems(TagPrefix.pipeNormalFluid, NiobiumTitanium, 4)
                .inputItems(TagPrefix.plate, RhodiumPlatedPalladium, 4)
                .inputItems(TagPrefix.plate, GTOMaterials.HastelloyN75, 4)
                .outputItems(GTOBlocks.OIL_GAS_TRANSPORTATION_PIPE_CASING.asStack())
                .inputFluids(GTOMaterials.MarM200Steel, 576)
                .EUt(480)
                .duration(200)
                .save();

        ASSEMBLER_RECIPES.builder("high_strength_support_mechanical_casing")
                .inputItems(TagPrefix.frameGt, BlackSteel)
                .inputItems(plateDouble, HastelloyX, 8)
                .inputItems(TagPrefix.plate, RhodiumPlatedPalladium, 8)
                .inputItems(rod, GTOMaterials.Inconel625, 8)
                .outputItems(GTOBlocks.HIGH_STRENGTH_SUPPORT_MECHANICAL_CASING.asStack())
                .inputFluids(Polybenzimidazole, 576)
                .EUt(480)
                .duration(200)
                .save();

        ASSEMBLER_RECIPES.builder("high_strength_support_spindle")
                .inputItems(TagPrefix.frameGt, BlackSteel)
                .inputItems(plateDouble, RhodiumPlatedPalladium, 8)
                .inputItems(rodLong, Naquadah, 8)
                .outputItems(GTOBlocks.HIGH_STRENGTH_SUPPORT_SPINDLE.asStack())
                .inputFluids(Concrete, 576 * 4)
                .EUt(480)
                .duration(200)
                .save();

        ASSEMBLER_RECIPES.builder("high_strength_space_elevator_cable")
                .inputItems(wireGtQuadruple, BlackSteel, 4)
                .inputItems(wireGtQuadruple, GTOMaterials.Amprosium, 4)
                .inputItems(foil, NaquadahAlloy, 8)
                .outputItems(GTOBlocks.HIGH_STRENGTH_SPACE_ELEVATOR_CABLE.asStack())
                .inputFluids(GTOMaterials.Kevlar, 288)
                .EUt(480)
                .duration(200)
                .save();

        ASSEMBLER_RECIPES.builder("high_energy_laser_emitter")
                .inputItems(TagPrefix.frameGt, GTMaterials.Trinium)
                .inputItems(GCYMBlocks.CASING_LASER_SAFE_ENGRAVING.asStack())
                .inputItems(GTItems.EMITTER_UHV.asStack(4))
                .inputItems(TagPrefix.lens, GTMaterials.Ruby)
                .inputItems(GTOItems.LASER_DIODE.asItem())
                .inputItems(GTOItems.UHV_VOLTAGE_COIL.asItem())
                .inputItems(TagPrefix.plate, GTOMaterials.ZirconiumCarbide, 6)
                .inputItems(TagPrefix.plateDouble, GTOMaterials.FluxedElectrum, 2)
                .inputItems(TagPrefix.plateDouble, GTMaterials.Zeron100, 4)
                .outputItems(GTOBlocks.HIGH_ENERGY_LASER_EMITTER.asStack())
                .inputFluids(GTMaterials.SolderingAlloy, 576)
                .EUt(480)
                .duration(2222)
                .save();

        ASSEMBLER_RECIPES.builder("COSMIC_DETECTION_RECEIVER_MATERIAL_RAY_ABSORBING_ARRAYasStack".toLowerCase(Locale.ROOT))
                .inputItems(frameGt, GTOMaterials.BabbittAlloy)
                .inputItems(dust, GalliumArsenide, 8)
                .inputItems(GTOTagPrefix.FLAKES, GTOMaterials.StrontiumCarbonateCeramic, 16)
                .outputItems(GTOBlocks.COSMIC_DETECTION_RECEIVER_MATERIAL_RAY_ABSORBING_ARRAY.asStack())
                .inputFluids(Osmiridium, 2000)
                .EUt(480)
                .duration(200)
                .save();

        ASSEMBLER_RECIPES.builder("OPTICAL_RESONANCE_CHAMBERasStack".toLowerCase(Locale.ROOT))
                .inputItems(GTBlocks.FUSION_GLASS.asStack())
                .inputItems(TagPrefix.lens, GTMaterials.Ruby, 2)
                .inputItems(TagPrefix.plate, GTMaterials.UraniumRhodiumDinaquadide, 6)
                .inputItems("gtceu:normal_optical_pipe", 6)
                .inputItems(GTOTagPrefix.FLAKES, GTOMaterials.TungstenTetraborideCeramics, 8)
                .outputItems(GTOBlocks.OPTICAL_RESONANCE_CHAMBER.asStack())
                .inputFluids(GTMaterials.HSLASteel, 1000)
                .EUt(480)
                .duration(200)
                .save();

        ASSEMBLER_RECIPES.builder("PRECISION_PROCESSING_MECHANICAL_CASING".toLowerCase(Locale.ROOT))
                .inputItems(TagPrefix.frameGt, GTOMaterials.HastelloyN)
                .inputItems(TagPrefix.ingot, GTOMaterials.HastelloyN, 6)
                .inputItems(TagPrefix.gearSmall, GTMaterials.RhodiumPlatedPalladium, 4)
                .inputItems(TagPrefix.gearSmall, GTMaterials.HSSS, 4)
                .inputItems(TagPrefix.gearSmall, GTMaterials.Osmiridium, 4)
                .inputItems(TagPrefix.rod, GTOMaterials.HastelloyN, 6)
                .outputItems(GTOBlocks.PRECISION_PROCESSING_MECHANICAL_CASING.asStack())
                .inputFluids(GTMaterials.Scandium, 1000)
                .EUt(480)
                .duration(200)
                .save();

        ASSEMBLER_RECIPES.builder("ENGINEERING_MECHANICAL_CASING".toLowerCase(Locale.ROOT))
                .inputItems(TagPrefix.frameGt, GTMaterials.Ruridit)
                .inputItems(TagPrefix.plateDense, GTMaterials.Steel, 4)
                .outputItems(GTOBlocks.ENGINEERING_MECHANICAL_CASING.asStack())
                .inputFluids(GTMaterials.ReinforcedEpoxyResin, 2000)
                .EUt(480)
                .duration(200)
                .save();

        ASSEMBLER_RECIPES.builder("transcendentally_amplified_magnetic_confinement_casing")
                .inputItems(TagPrefix.frameGt, GTOMaterials.Laurenium)
                .inputItems(GTOBlocks.ACCELERATOR_MAGNETIC_CONSTRAINED_RAIL_CASING.asStack(4))
                .inputItems(TagPrefix.plateDouble, GTOMaterials.Ostrum, 8)
                .inputItems(TagPrefix.plateDouble, GTOMaterials.Vibranium, 8)
                .outputItems(GTOBlocks.TRANSCENDENTALLY_AMPLIFIED_MAGNETIC_CONFINEMENT_CASING.asStack())
                .circuitMeta(6)
                .EUt(122800)
                .duration(200)
                .save();
        ASSEMBLER_RECIPES.builder("polytetrafluoroethylene_membrane_electrode")
                .inputItems(TagPrefix.foil, GTMaterials.Polytetrafluoroethylene, 16)
                .inputItems(GTOTagPrefix.CATALYST, Nickel, 4)
                .inputItems(new ItemStack(Items.SPONGE.asItem()))
                .outputItems(GTOTagPrefix.MEMBRANE_ELECTRODE, GTMaterials.Polytetrafluoroethylene)
                .inputFluids(GTMaterials.Polycaprolactam, 144)
                .EUt(8168)
                .duration(600)
                .save();
        ASSEMBLER_RECIPES.builder("graphene_membrane_electrode")
                .inputItems(TagPrefix.foil, GTMaterials.Graphene, 16)
                .inputItems(GTOTagPrefix.CATALYST, Platinum, 4)
                .inputItems(new ItemStack(Items.SPONGE.asItem()))
                .outputItems(GTOTagPrefix.MEMBRANE_ELECTRODE, GTMaterials.Graphene)
                .inputFluids(GTMaterials.Polycaprolactam, 144)
                .EUt(8168)
                .duration(600)
                .save();
        ASSEMBLER_RECIPES.builder("polous_polyolefin_sulfonate_membrane_electrode")
                .inputItems(TagPrefix.foil, GTOMaterials.PolousPolyolefinSulfonate, 16)
                .inputItems(GTOTagPrefix.CATALYST, GTOMaterials.TitaniumDioxideNanotubes, 4)
                .inputItems(new ItemStack(Items.SPONGE.asItem()))
                .outputItems(GTOTagPrefix.MEMBRANE_ELECTRODE, GTOMaterials.PolousPolyolefinSulfonate)
                .inputFluids(GTMaterials.Polycaprolactam, 144)
                .EUt(8168)
                .duration(600)
                .save();
        ASSEMBLER_RECIPES.builder("perfluorosulfonic_acid_polytetrafluoroethylene_copolymer_membrane_electrode")
                .inputItems(TagPrefix.foil, GTOMaterials.PerfluorosulfonicAcidPolytetrafluoroethyleneCopolymer, 16)
                .inputItems(GTOTagPrefix.CATALYST, GTOMaterials.RhodiumRheniumNaquadahCatalyst, 4)
                .inputItems(new ItemStack(Items.SPONGE.asItem()))
                .outputItems(GTOTagPrefix.MEMBRANE_ELECTRODE, GTOMaterials.PerfluorosulfonicAcidPolytetrafluoroethyleneCopolymer)
                .inputFluids(GTMaterials.Polycaprolactam, 144)
                .EUt(8168)
                .duration(600)
                .save();
        ASSEMBLER_RECIPES.builder("ce_ox_poly_dopamine_reinforced_polytetrafluoroethylene_membrane_electrode")
                .inputItems(TagPrefix.foil, GTOMaterials.CeOxPolyDopamineReinforcedPolytetrafluoroethylene, 16)
                .inputItems(GTOTagPrefix.CATALYST, Hafnium, 4)
                .inputItems(new ItemStack(Items.SPONGE.asItem()))
                .outputItems(GTOTagPrefix.MEMBRANE_ELECTRODE, GTOMaterials.CeOxPolyDopamineReinforcedPolytetrafluoroethylene)
                .inputFluids(GTMaterials.Polycaprolactam, 144)
                .EUt(8168)
                .duration(600)
                .save();
        ASSEMBLER_RECIPES.builder("nanocrack_regulated_self_humidifying_composite_material_membrane_electrode")
                .inputItems(TagPrefix.foil, GTOMaterials.NanocrackRegulatedSelfHumidifyingCompositeMaterial, 16)
                .inputItems(GTOTagPrefix.CATALYST, GTOMaterials.RhodiumTriphenylphosphineChloride, 4)
                .inputItems(new ItemStack(Items.SPONGE.asItem()))
                .outputItems(GTOTagPrefix.MEMBRANE_ELECTRODE, GTOMaterials.NanocrackRegulatedSelfHumidifyingCompositeMaterial)
                .inputFluids(GTMaterials.Polycaprolactam, 144)
                .EUt(8168)
                .duration(600)
                .save();
        ASSEMBLER_RECIPES.builder("fuel_cell_generator")
                .inputItems(GTOBlocks.IRIDIUM_CASING.asStack(4))
                .inputItems(GeneratorMultiblock.CHEMICAL_ENERGY_DEVOURER.asStack())
                .inputItems(GTOTagPrefix.MEMBRANE_ELECTRODE, GTMaterials.Polytetrafluoroethylene, 16)
                .inputItems(GTItems.ENGRAVED_LAPOTRON_CHIP.asStack(8))
                .inputItems(GTBlocks.HERMETIC_CASING_EV.asStack(4))
                .inputItems(GTBlocks.HERMETIC_CASING_IV.asStack(4))
                .inputItems(CustomTags.LuV_CIRCUITS, 4)
                .inputItems(GTItems.ELECTRIC_PUMP_IV.asStack(8))
                .inputItems(TagPrefix.wireGtQuadruple, GTMaterials.SamariumIronArsenicOxide, 4)
                .outputItems(GeneratorMultiblock.FUEL_CELL_GENERATOR.asStack())
                .inputFluids(GTMaterials.SolderingAlloy, 4000)
                .EUt(7680)
                .duration(1200)
                .save();
    }
}
