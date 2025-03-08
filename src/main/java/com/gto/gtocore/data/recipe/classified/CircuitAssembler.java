package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import com.enderio.base.common.init.EIOItems;

import static com.gto.gtocore.common.data.GTORecipeTypes.CIRCUIT_ASSEMBLER_RECIPES;

interface CircuitAssembler {

    static void init() {
        int outputAmount = ConfigHolder.INSTANCE.recipes.harderCircuitRecipes ? 1 : 2;
        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("magneto_resonatic_circuit_uiv"))
                .inputItems(GTOItems.SMD_DIODE_SUPRACAUSAL.asStack(16))
                .inputItems(GTOItems.SMD_CAPACITOR_SUPRACAUSAL.asStack(16))
                .inputItems(GTOItems.SMD_TRANSISTOR_SUPRACAUSAL.asStack(16))
                .inputItems(GTOItems.IMPRINTED_RESONATIC_CIRCUIT_BOARD.asStack(8))
                .inputItems(TagPrefix.gemExquisite, GTOMaterials.MagnetoResonatic)
                .inputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.UEV].asStack())
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(432))
                .outputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.UIV].asStack(outputAmount << 1))
                .EUt(125829120)
                .duration(790)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("magneto_resonatic_circuit_uhv"))
                .inputItems(GTOItems.SMD_DIODE_EXOTIC.asStack(16))
                .inputItems(GTOItems.SMD_CAPACITOR_EXOTIC.asStack(16))
                .inputItems(GTOItems.SMD_TRANSISTOR_EXOTIC.asStack(16))
                .inputItems(GTOItems.IMPRINTED_RESONATIC_CIRCUIT_BOARD.asStack(8))
                .inputItems(TagPrefix.gemExquisite, GTOMaterials.MagnetoResonatic)
                .inputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.UV].asStack())
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(432))
                .outputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.UHV].asStack(outputAmount << 1))
                .EUt(7864320)
                .duration(750)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("magneto_resonatic_circuit_ulv"))
                .inputItems(GTItems.BATTERY_ULV_TANTALUM.asStack(4))
                .inputItems(GTItems.RESISTOR.asStack(4))
                .inputItems(GTItems.INDUCTOR.asStack(4))
                .inputItems(GTOItems.IMPRINTED_RESONATIC_CIRCUIT_BOARD.asStack())
                .inputItems(TagPrefix.gem, GTOMaterials.MagnetoResonatic)
                .inputItems(GTItems.VACUUM_TUBE.asStack())
                .inputFluids(GTMaterials.Tin.getFluid(144))
                .outputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.ULV].asStack(outputAmount << 1))
                .EUt(30)
                .duration(50)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("magneto_resonatic_circuit_uev"))
                .inputItems(GTOItems.SMD_DIODE_COSMIC.asStack(16))
                .inputItems(GTOItems.SMD_CAPACITOR_COSMIC.asStack(16))
                .inputItems(GTOItems.SMD_TRANSISTOR_COSMIC.asStack(16))
                .inputItems(GTOItems.IMPRINTED_RESONATIC_CIRCUIT_BOARD.asStack(8))
                .inputItems(TagPrefix.gemExquisite, GTOMaterials.MagnetoResonatic)
                .inputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.UHV].asStack())
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(288))
                .outputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.UEV].asStack(outputAmount << 1))
                .EUt(31457280)
                .duration(770)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("magneto_resonatic_circuit_zpm"))
                .inputItems(GTOItems.SMD_DIODE_BIOWARE.asStack(16))
                .inputItems(GTOItems.SMD_CAPACITOR_BIOWARE.asStack(16))
                .inputItems(GTOItems.SMD_TRANSISTOR_BIOWARE.asStack(16))
                .inputItems(GTOItems.IMPRINTED_RESONATIC_CIRCUIT_BOARD.asStack(8))
                .inputItems(TagPrefix.gemExquisite, GTOMaterials.MagnetoResonatic)
                .inputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.LuV].asStack())
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(144))
                .outputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.ZPM].asStack(outputAmount << 1))
                .EUt(491520)
                .duration(710)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("exotic_processor"))
                .inputItems(GTOItems.EXOTIC_PROCESSING_CORE.asStack())
                .inputItems(GTOItems.EXOTIC_RAM_CHIP.asStack(4))
                .inputItems(GTItems.HIGHLY_ADVANCED_SOC.asStack())
                .inputItems(GTOItems.SMD_CAPACITOR_EXOTIC.asStack(8))
                .inputItems(GTOItems.SMD_TRANSISTOR_EXOTIC.asStack(8))
                .inputItems(TagPrefix.wireFine, GTOMaterials.Cinobite, 16)
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(432))
                .outputItems(GTOItems.EXOTIC_PROCESSOR.asStack(outputAmount))
                .EUt(7864320)
                .duration(200)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("bioware_processor"))
                .inputItems(GTOItems.BIOWARE_PROCESSING_CORE.asStack())
                .inputItems(GTItems.QUBIT_CENTRAL_PROCESSING_UNIT.asStack(4))
                .inputItems(GTItems.HIGHLY_ADVANCED_SOC.asStack())
                .inputItems(GTOItems.SMD_CAPACITOR_BIOWARE.asStack(8))
                .inputItems(GTOItems.SMD_TRANSISTOR_BIOWARE.asStack(8))
                .inputItems(TagPrefix.wireFine, GTMaterials.Naquadah, 16)
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(144))
                .outputItems(GTOItems.BIOWARE_PROCESSOR.asStack())
                .EUt(491520)
                .duration(200)
                .cleanroom(CleanroomType.STERILE_CLEANROOM)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("magneto_resonatic_circuit_luv"))
                .inputItems(GTItems.ADVANCED_SMD_DIODE.asStack(8))
                .inputItems(GTItems.ADVANCED_SMD_CAPACITOR.asStack(8))
                .inputItems(GTItems.ADVANCED_SMD_TRANSISTOR.asStack(8))
                .inputItems(GTOItems.IMPRINTED_RESONATIC_CIRCUIT_BOARD.asStack(4))
                .inputItems(TagPrefix.gemFlawless, GTOMaterials.MagnetoResonatic)
                .inputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.IV].asStack())
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.LuV].asStack(outputAmount << 1))
                .EUt(122880)
                .duration(570)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("supracausal_processor"))
                .inputItems(GTOItems.SUPRACAUSAL_PROCESSING_CORE.asStack())
                .inputItems(GTOItems.SUPRACAUSAL_RAM_CHIP.asStack(4))
                .inputItems(GTOItems.COSMIC_PROCESSING_UNIT_CORE.asStack(16))
                .inputItems(GTOItems.MICROWORMHOLE_GENERATOR.asStack())
                .inputItems(GTOItems.MANIFOLD_OSCILLATORY_POWER_CELL.asStack())
                .inputItems(TagPrefix.plate, GTOMaterials.CrystalMatrix)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(432))
                .outputItems(GTOItems.SUPRACAUSAL_PROCESSOR.asStack(outputAmount))
                .EUt(125829120)
                .duration(100)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("magneto_resonatic_circuit_mv"))
                .inputItems(GTItems.DIODE.asStack(8))
                .inputItems(GTItems.CAPACITOR.asStack(8))
                .inputItems(GTItems.TRANSISTOR.asStack(8))
                .inputItems(GTOItems.IMPRINTED_RESONATIC_CIRCUIT_BOARD.asStack())
                .inputItems(TagPrefix.gem, GTOMaterials.MagnetoResonatic)
                .inputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.LV].asStack())
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.MV].asStack(outputAmount << 1))
                .EUt(480)
                .duration(150)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("magneto_resonatic_circuit_lv"))
                .inputItems(GTItems.DIODE.asStack(4))
                .inputItems(GTItems.CAPACITOR.asStack(4))
                .inputItems(GTItems.TRANSISTOR.asStack(4))
                .inputItems(GTOItems.IMPRINTED_RESONATIC_CIRCUIT_BOARD.asStack())
                .inputItems(TagPrefix.gem, GTOMaterials.MagnetoResonatic)
                .inputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.ULV].asStack())
                .inputFluids(GTMaterials.Tin.getFluid(144))
                .outputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.LV].asStack(outputAmount << 1))
                .EUt(120)
                .duration(90)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("magneto_resonatic_circuit_uv"))
                .inputItems(GTOItems.SMD_DIODE_OPTICAL.asStack(16))
                .inputItems(GTOItems.SMD_CAPACITOR_OPTICAL.asStack(16))
                .inputItems(GTOItems.SMD_TRANSISTOR_OPTICAL.asStack(16))
                .inputItems(GTOItems.IMPRINTED_RESONATIC_CIRCUIT_BOARD.asStack(8))
                .inputItems(TagPrefix.gemExquisite, GTOMaterials.MagnetoResonatic)
                .inputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.ZPM].asStack())
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(288))
                .outputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.UV].asStack(outputAmount << 1))
                .EUt(1966080)
                .duration(730)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("magneto_resonatic_circuit_ev"))
                .inputItems(GTItems.SMD_DIODE.asStack(8))
                .inputItems(GTItems.SMD_CAPACITOR.asStack(8))
                .inputItems(GTItems.SMD_TRANSISTOR.asStack(8))
                .inputItems(GTOItems.IMPRINTED_RESONATIC_CIRCUIT_BOARD.asStack(4))
                .inputItems(TagPrefix.gemFlawless, GTOMaterials.MagnetoResonatic)
                .inputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.HV].asStack())
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.EV].asStack(outputAmount << 1))
                .EUt(7680)
                .duration(330)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("magneto_resonatic_circuit_iv"))
                .inputItems(GTItems.ADVANCED_SMD_DIODE.asStack(4))
                .inputItems(GTItems.ADVANCED_SMD_CAPACITOR.asStack(4))
                .inputItems(GTItems.ADVANCED_SMD_TRANSISTOR.asStack(4))
                .inputItems(GTOItems.IMPRINTED_RESONATIC_CIRCUIT_BOARD.asStack(4))
                .inputItems(TagPrefix.gemFlawless, GTOMaterials.MagnetoResonatic)
                .inputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.EV].asStack())
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.IV].asStack(outputAmount << 1))
                .EUt(30720)
                .duration(450)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("magneto_resonatic_circuit_hv"))
                .inputItems(GTItems.SMD_DIODE.asStack(4))
                .inputItems(GTItems.SMD_CAPACITOR.asStack(4))
                .inputItems(GTItems.SMD_TRANSISTOR.asStack(4))
                .inputItems(GTOItems.IMPRINTED_RESONATIC_CIRCUIT_BOARD.asStack(2))
                .inputItems(TagPrefix.gemFlawless, GTOMaterials.MagnetoResonatic)
                .inputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.MV].asStack())
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(GTOItems.MAGNETO_RESONATIC_CIRCUIT[GTValues.HV].asStack(outputAmount << 1))
                .EUt(1920)
                .duration(230)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("hui_circuit_1"))
                .inputItems(GTItems.EXTREME_CIRCUIT_BOARD.asStack())
                .inputItems(TagPrefix.plate, GTMaterials.GarnetYellow, 16)
                .inputItems(GTItems.SYSTEM_ON_CHIP.asStack(8))
                .inputItems(GTItems.NOR_MEMORY_CHIP.asStack(32))
                .inputItems(TagPrefix.wireGtSingle, GTMaterials.Aluminium, 8)
                .inputItems(EIOItems.GUARDIAN_DIODE.asStack())
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                .outputItems(GTOItems.HUI_CIRCUIT_1.asStack())
                .EUt(7680)
                .duration(320)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("bioware_circuit_board"))
                .inputItems(GTItems.WETWARE_BOARD.asStack(32))
                .inputItems(GTOItems.ELECTRICALY_WIRED_PETRI_DISH.asStack(8))
                .inputItems(GTItems.ELECTRIC_PUMP_UV.asStack())
                .inputItems(GTItems.SENSOR_LuV.asStack(2))
                .inputItems(CustomTags.LuV_CIRCUITS)
                .inputItems(TagPrefix.foil, GTMaterials.VanadiumGallium, 32)
                .inputFluids(GTOMaterials.BiohmediumSterilized.getFluid(1000))
                .outputItems(GTOItems.BIOWARE_CIRCUIT_BOARD.asStack(32))
                .EUt(122880)
                .duration(2400)
                .cleanroom(CleanroomType.STERILE_CLEANROOM)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("cosmic_processor"))
                .inputItems(GTOItems.COSMIC_PROCESSING_CORE.asStack())
                .inputItems(GTOItems.COSMIC_RAM_CHIP.asStack(4))
                .inputItems(GTItems.HIGHLY_ADVANCED_SOC.asStack())
                .inputItems(GTOItems.SMD_CAPACITOR_COSMIC.asStack(16))
                .inputItems(GTOItems.SMD_TRANSISTOR_COSMIC.asStack(16))
                .inputItems(TagPrefix.wireFine, GTOMaterials.Cinobite, 16)
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(288))
                .outputItems(GTOItems.COSMIC_PROCESSOR.asStack(outputAmount))
                .EUt(31457280)
                .duration(200)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("optical_processor"))
                .inputItems(GTOItems.OPTICAL_PROCESSING_CORE.asStack())
                .inputItems(GTOItems.OPTICAL_RAM_CHIP.asStack(4))
                .inputItems(GTItems.HIGHLY_ADVANCED_SOC.asStack())
                .inputItems(GTOItems.SMD_CAPACITOR_OPTICAL.asStack(8))
                .inputItems(GTOItems.SMD_TRANSISTOR_OPTICAL.asStack(8))
                .inputItems(TagPrefix.wireFine, GTMaterials.Dubnium, 16)
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(288))
                .outputItems(GTOItems.OPTICAL_PROCESSOR.asStack(outputAmount))
                .EUt(1966080)
                .duration(200)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("planet_data_chip"))
                .inputItems(GTItems.TOOL_DATA_STICK.asStack(2))
                .inputItems(GTItems.NOR_MEMORY_CHIP.asStack(16))
                .inputItems(GTItems.SIMPLE_SYSTEM_ON_CHIP.asStack(12))
                .inputItems(TagPrefix.screw, GTMaterials.BlueAlloy, 32)
                .inputItems(TagPrefix.wireFine, GTMaterials.Cupronickel, 32)
                .inputItems(TagPrefix.plate, GTMaterials.StainlessSteel, 4)
                .outputItems(GTOItems.PLANET_DATA_CHIP.asStack())
                .EUt(120)
                .duration(400)
                .save();
    }
}
