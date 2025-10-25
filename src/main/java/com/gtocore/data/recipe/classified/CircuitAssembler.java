package com.gtocore.data.recipe.classified;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import com.enderio.base.common.init.EIOItems;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.CIRCUIT_ASSEMBLER_RECIPES;

final class CircuitAssembler {

    public static void init() {
        int outputAmount = ConfigHolder.INSTANCE.recipes.harderCircuitRecipes ? 1 : 2;
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("magneto_resonatic_circuit_uiv")
                .inputItems(GTOItems.SMD_DIODE_SUPRACAUSAL.asItem(), 16)
                .inputItems(GTOItems.SMD_CAPACITOR_SUPRACAUSAL.asItem(), 16)
                .inputItems(GTOItems.SMD_TRANSISTOR_SUPRACAUSAL.asItem(), 16)
                .inputItems(GTOItems.IMPRINTED_RESONATIC_CIRCUIT_BOARD.asItem(), 8)
                .inputItems(TagPrefix.gemExquisite, GTOMaterials.MagnetoResonatic)
                .inputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.UEV].get())
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(432))
                .outputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.UIV].asStack(outputAmount << 1))
                .EUt(125829120)
                .duration(790)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("magneto_resonatic_circuit_uhv")
                .inputItems(GTOItems.SMD_DIODE_EXOTIC.asItem(), 16)
                .inputItems(GTOItems.SMD_CAPACITOR_EXOTIC.asItem(), 16)
                .inputItems(GTOItems.SMD_TRANSISTOR_EXOTIC.asItem(), 16)
                .inputItems(GTOItems.IMPRINTED_RESONATIC_CIRCUIT_BOARD.asItem(), 8)
                .inputItems(TagPrefix.gemExquisite, GTOMaterials.MagnetoResonatic)
                .inputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.UV].get())
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(432))
                .outputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.UHV].asStack(outputAmount << 1))
                .EUt(7864320)
                .duration(750)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("magneto_resonatic_circuit_ulv")
                .inputItems(GTItems.BATTERY_ULV_TANTALUM.asItem(), 4)
                .inputItems(GTItems.RESISTOR.asItem(), 4)
                .inputItems(GTItems.INDUCTOR.asItem(), 4)
                .inputItems(GTOItems.IMPRINTED_RESONATIC_CIRCUIT_BOARD.asItem())
                .inputItems(TagPrefix.gem, GTOMaterials.MagnetoResonatic)
                .inputItems(GTItems.VACUUM_TUBE.asItem())
                .inputFluids(GTMaterials.Tin.getFluid(144))
                .outputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.ULV].asStack(outputAmount << 1))
                .EUt(30)
                .duration(50)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("magneto_resonatic_circuit_uev")
                .inputItems(GTOItems.SMD_DIODE_COSMIC.asItem(), 16)
                .inputItems(GTOItems.SMD_CAPACITOR_COSMIC.asItem(), 16)
                .inputItems(GTOItems.SMD_TRANSISTOR_COSMIC.asItem(), 16)
                .inputItems(GTOItems.IMPRINTED_RESONATIC_CIRCUIT_BOARD.asItem(), 8)
                .inputItems(TagPrefix.gemExquisite, GTOMaterials.MagnetoResonatic)
                .inputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.UHV].get())
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(288))
                .outputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.UEV].asStack(outputAmount << 1))
                .EUt(31457280)
                .duration(770)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("magneto_resonatic_circuit_zpm")
                .inputItems(GTOItems.SMD_DIODE_BIOWARE.asItem(), 16)
                .inputItems(GTOItems.SMD_CAPACITOR_BIOWARE.asItem(), 16)
                .inputItems(GTOItems.SMD_TRANSISTOR_BIOWARE.asItem(), 16)
                .inputItems(GTOItems.IMPRINTED_RESONATIC_CIRCUIT_BOARD.asItem(), 8)
                .inputItems(TagPrefix.gemExquisite, GTOMaterials.MagnetoResonatic)
                .inputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.LuV].get())
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(144))
                .outputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.ZPM].asStack(outputAmount << 1))
                .EUt(491520)
                .duration(710)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("exotic_processor")
                .inputItems(GTOItems.EXOTIC_PROCESSING_CORE.asItem())
                .inputItems(GTOItems.EXOTIC_RAM_CHIP.asItem(), 4)
                .inputItems(GTItems.HIGHLY_ADVANCED_SOC.asItem())
                .inputItems(GTOItems.SMD_CAPACITOR_EXOTIC.asItem(), 8)
                .inputItems(GTOItems.SMD_TRANSISTOR_EXOTIC.asItem(), 8)
                .inputItems(TagPrefix.wireFine, GTOMaterials.Cinobite, 16)
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(432))
                .outputItems(GTOItems.EXOTIC_PROCESSOR.asStack(outputAmount))
                .EUt(7864320)
                .duration(200)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("bioware_processor")
                .inputItems(GTOItems.BIOWARE_PROCESSING_CORE.asItem())
                .inputItems(GTItems.NEURO_PROCESSOR.asItem())
                .inputItems(GTItems.HIGHLY_ADVANCED_SOC.asItem())
                .inputItems(GTOItems.SMD_CAPACITOR_BIOWARE.asItem(), 8)
                .inputItems(GTOItems.SMD_TRANSISTOR_BIOWARE.asItem(), 8)
                .inputItems(TagPrefix.wireFine, GTMaterials.Naquadah, 16)
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(144))
                .outputItems(GTOItems.BIOWARE_PROCESSOR.asItem())
                .EUt(491520)
                .duration(200)
                .cleanroom(CleanroomType.STERILE_CLEANROOM)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("magneto_resonatic_circuit_luv")
                .inputItems(GTItems.ADVANCED_SMD_DIODE.asItem(), 8)
                .inputItems(GTItems.ADVANCED_SMD_CAPACITOR.asItem(), 8)
                .inputItems(GTItems.ADVANCED_SMD_TRANSISTOR.asItem(), 8)
                .inputItems(GTOItems.IMPRINTED_RESONATIC_CIRCUIT_BOARD.asItem(), 4)
                .inputItems(TagPrefix.gemFlawless, GTOMaterials.MagnetoResonatic)
                .inputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.IV].get())
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.LuV].asStack(outputAmount << 1))
                .EUt(122880)
                .duration(570)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("supracausal_processor")
                .inputItems(GTOItems.SUPRACAUSAL_PROCESSING_CORE.asItem())
                .inputItems(GTOItems.SUPRACAUSAL_RAM_CHIP.asItem(), 4)
                .inputItems(GTOItems.COSMIC_PROCESSING_UNIT_CORE.asItem(), 16)
                .inputItems(GTOItems.MICROWORMHOLE_GENERATOR.asItem())
                .inputItems(GTOItems.MANIFOLD_OSCILLATORY_POWER_CELL.asItem())
                .inputItems(TagPrefix.plate, GTOMaterials.CrystalMatrix)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(432))
                .outputItems(GTOItems.SUPRACAUSAL_PROCESSOR.asStack(outputAmount))
                .EUt(125829120)
                .duration(100)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("magneto_resonatic_circuit_mv")
                .inputItems(GTItems.DIODE.asItem(), 8)
                .inputItems(GTItems.CAPACITOR.asItem(), 8)
                .inputItems(GTItems.TRANSISTOR.asItem(), 8)
                .inputItems(GTOItems.IMPRINTED_RESONATIC_CIRCUIT_BOARD.asItem())
                .inputItems(TagPrefix.gem, GTOMaterials.MagnetoResonatic)
                .inputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.LV].get())
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.MV].asStack(outputAmount << 1))
                .EUt(480)
                .duration(150)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("magneto_resonatic_circuit_lv")
                .inputItems(GTItems.DIODE.asItem(), 4)
                .inputItems(GTItems.CAPACITOR.asItem(), 4)
                .inputItems(GTItems.TRANSISTOR.asItem(), 4)
                .inputItems(GTOItems.IMPRINTED_RESONATIC_CIRCUIT_BOARD.asItem())
                .inputItems(TagPrefix.gem, GTOMaterials.MagnetoResonatic)
                .inputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.ULV].get())
                .inputFluids(GTMaterials.Tin.getFluid(144))
                .outputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.LV].asStack(outputAmount << 1))
                .EUt(120)
                .duration(90)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("magneto_resonatic_circuit_uv")
                .inputItems(GTOItems.SMD_DIODE_OPTICAL.asItem(), 16)
                .inputItems(GTOItems.SMD_CAPACITOR_OPTICAL.asItem(), 16)
                .inputItems(GTOItems.SMD_TRANSISTOR_OPTICAL.asItem(), 16)
                .inputItems(GTOItems.IMPRINTED_RESONATIC_CIRCUIT_BOARD.asItem(), 8)
                .inputItems(TagPrefix.gemExquisite, GTOMaterials.MagnetoResonatic)
                .inputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.ZPM].get())
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(288))
                .outputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.UV].asStack(outputAmount << 1))
                .EUt(1966080)
                .duration(730)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("magneto_resonatic_circuit_ev")
                .inputItems(GTItems.SMD_DIODE.asItem(), 8)
                .inputItems(GTItems.SMD_CAPACITOR.asItem(), 8)
                .inputItems(GTItems.SMD_TRANSISTOR.asItem(), 8)
                .inputItems(GTOItems.IMPRINTED_RESONATIC_CIRCUIT_BOARD.asItem(), 4)
                .inputItems(TagPrefix.gemFlawless, GTOMaterials.MagnetoResonatic)
                .inputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.HV].get())
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.EV].asStack(outputAmount << 1))
                .EUt(7680)
                .duration(330)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("magneto_resonatic_circuit_iv")
                .inputItems(GTItems.ADVANCED_SMD_DIODE.asItem(), 4)
                .inputItems(GTItems.ADVANCED_SMD_CAPACITOR.asItem(), 4)
                .inputItems(GTItems.ADVANCED_SMD_TRANSISTOR.asItem(), 4)
                .inputItems(GTOItems.IMPRINTED_RESONATIC_CIRCUIT_BOARD.asItem(), 4)
                .inputItems(TagPrefix.gemFlawless, GTOMaterials.MagnetoResonatic)
                .inputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.EV].get())
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.IV].asStack(outputAmount << 1))
                .EUt(30720)
                .duration(450)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("magneto_resonatic_circuit_hv")
                .inputItems(GTItems.SMD_DIODE.asItem(), 4)
                .inputItems(GTItems.SMD_CAPACITOR.asItem(), 4)
                .inputItems(GTItems.SMD_TRANSISTOR.asItem(), 4)
                .inputItems(GTOItems.IMPRINTED_RESONATIC_CIRCUIT_BOARD.asItem(), 2)
                .inputItems(TagPrefix.gemFlawless, GTOMaterials.MagnetoResonatic)
                .inputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.MV].get())
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.HV].asStack(outputAmount << 1))
                .EUt(1920)
                .duration(230)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("hui_circuit_1")
                .inputItems(GTItems.EXTREME_CIRCUIT_BOARD.asItem())
                .inputItems(TagPrefix.plate, GTMaterials.GarnetYellow, 16)
                .inputItems(GTItems.SYSTEM_ON_CHIP.asItem(), 8)
                .inputItems(GTItems.NOR_MEMORY_CHIP.asItem(), 32)
                .inputItems(TagPrefix.wireGtSingle, GTMaterials.Aluminium, 8)
                .inputItems(EIOItems.GUARDIAN_DIODE.asItem())
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(GTOItems.HUI_CIRCUIT_1.asItem())
                .EUt(7680)
                .duration(320)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("bioware_circuit_board")
                .inputItems(GTItems.WETWARE_BOARD.asItem(), 32)
                .inputItems(GTOItems.ELECTRICALY_WIRED_PETRI_DISH.asItem(), 8)
                .inputItems(GTItems.ELECTRIC_PUMP_UV.asItem())
                .inputItems(GTItems.SENSOR_LuV.asItem(), 2)
                .inputItems(CustomTags.LuV_CIRCUITS)
                .inputItems(TagPrefix.foil, GTMaterials.VanadiumGallium, 32)
                .inputFluids(GTOMaterials.BiohmediumSterilized.getFluid(1000))
                .outputItems(GTOItems.BIOWARE_CIRCUIT_BOARD.asItem(), 32)
                .EUt(122880)
                .duration(2400)
                .cleanroom(CleanroomType.STERILE_CLEANROOM)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("cosmic_processor")
                .inputItems(GTOItems.COSMIC_PROCESSING_CORE.asItem())
                .inputItems(GTOItems.COSMIC_RAM_CHIP.asItem(), 4)
                .inputItems(GTItems.HIGHLY_ADVANCED_SOC.asItem())
                .inputItems(GTOItems.SMD_CAPACITOR_COSMIC.asItem(), 16)
                .inputItems(GTOItems.SMD_TRANSISTOR_COSMIC.asItem(), 16)
                .inputItems(TagPrefix.wireFine, GTOMaterials.Cinobite, 16)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(288))
                .outputItems(GTOItems.COSMIC_PROCESSOR.asStack(outputAmount))
                .EUt(31457280)
                .duration(200)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("optical_processor")
                .inputItems(GTOItems.OPTICAL_PROCESSING_CORE.asItem())
                .inputItems(GTOItems.OPTICAL_RAM_CHIP.asItem(), 4)
                .inputItems(GTItems.HIGHLY_ADVANCED_SOC.asItem())
                .inputItems(GTOItems.SMD_CAPACITOR_OPTICAL.asItem(), 8)
                .inputItems(GTOItems.SMD_TRANSISTOR_OPTICAL.asItem(), 8)
                .inputItems(TagPrefix.wireFine, GTMaterials.Dubnium, 16)
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(288))
                .outputItems(GTOItems.OPTICAL_PROCESSOR.asStack(outputAmount))
                .EUt(1966080)
                .duration(200)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("planet_data_chip")
                .inputItems(GTItems.TOOL_DATA_STICK.asItem(), 2)
                .inputItems(GTItems.NOR_MEMORY_CHIP.asItem(), 16)
                .inputItems(GTItems.SIMPLE_SYSTEM_ON_CHIP.asItem(), 12)
                .inputItems(TagPrefix.screw, GTMaterials.BlueAlloy, 32)
                .inputItems(TagPrefix.wireFine, GTMaterials.Cupronickel, 32)
                .inputItems(TagPrefix.plate, GTMaterials.StainlessSteel, 4)
                .outputItems(GTOItems.PLANET_DATA_CHIP.asItem())
                .EUt(120)
                .duration(400)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.builder("integrated_circuit_hv").EUt(VA[LV]).duration(600)
                .inputItems(GTOTagPrefix.FLAKES, GTOMaterials.AluminaCeramic, 1)
                .inputItems(INTEGRATED_CIRCUIT_MV, 2)
                .inputItems(INTEGRATED_LOGIC_CIRCUIT, 2)
                .inputItems(RANDOM_ACCESS_MEMORY, 2)
                .inputItems(CustomTags.TRANSISTORS, 4)
                .inputItems(screw, AnnealedCopper, 8)
                .outputItems(INTEGRATED_CIRCUIT_HV)
                .solderMultiplier(2)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.builder("electronic_circuit_lv").EUt(16).duration(200)
                .inputItems(BASIC_CIRCUIT_BOARD)
                .inputItems(CustomTags.RESISTORS, 2)
                .inputItems(wireGtSingle, RedAlloy, 2)
                .inputItems(CustomTags.ULV_CIRCUITS, 2)
                .outputItems(ELECTRONIC_CIRCUIT_LV, outputAmount << 1)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.builder("wetware_processor_luv_soc").EUt(150000).duration(100)
                .inputItems(GTItems.WETWARE_CIRCUIT_BOARD)
                .inputItems(GTOItems.WETWARE_SOC)
                .inputItems(wireFine, YttriumBariumCuprate, 8)
                .inputItems(bolt, Naquadah, 8)
                .outputItems(WETWARE_PROCESSOR_LuV, outputAmount << 1)
                .cleanroom(CleanroomType.STERILE_CLEANROOM)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.builder("wetware_board").duration(1200).EUt(VA[LuV])
                .inputItems(MULTILAYER_FIBER_BOARD, 16)
                .inputItems(GTOItems.STERILIZED_PETRI_DISH)
                .inputItems(ELECTRIC_PUMP_LuV)
                .inputItems(SENSOR_IV)
                .inputItems(CustomTags.IV_CIRCUITS)
                .inputItems(foil, NiobiumTitanium, 16)
                .inputFluids(GTOMaterials.Indalloy140.getFluid(2304))
                .outputItems(WETWARE_BOARD, 16)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();
    }
}
