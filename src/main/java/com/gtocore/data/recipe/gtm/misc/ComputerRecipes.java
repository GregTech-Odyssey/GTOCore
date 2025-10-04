package com.gtocore.data.recipe.gtm.misc;

import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.machines.GTMultiMachines;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTBlocks.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMachines.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.machines.GTResearchMachines.*;
import static com.gtocore.common.data.GTORecipeTypes.ASSEMBLER_RECIPES;
import static com.gtocore.common.data.GTORecipeTypes.ASSEMBLY_LINE_RECIPES;

final class ComputerRecipes {

    public static void init() {
        ASSEMBLER_RECIPES.recipeBuilder("data_access_hatch")
                .inputItems(ITEM_IMPORT_BUS[EV])
                .inputItems(TOOL_DATA_STICK, 4)
                .inputItems(CustomTags.EV_CIRCUITS, 4)
                .outputItems(DATA_ACCESS_HATCH)
                .inputFluids(Polytetrafluoroethylene, L << 1)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(200).EUt(VA[EV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("high_power_casing")
                .inputItems(frameGt, Iridium)
                .inputItems(plate, Iridium, 6)
                .inputItems(CustomTags.IV_CIRCUITS)
                .inputItems(wireFine, Cobalt, 16)
                .inputItems(wireFine, Copper, 16)
                .inputItems(wireGtSingle, NiobiumTitanium, 2)
                .outputItems(HIGH_POWER_CASING,
                        ConfigHolder.INSTANCE.recipes.casingsPerCraft)
                .duration(100).EUt(VA[IV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("computer_casing")
                .inputItems(frameGt, Iridium)
                .inputItems(plate, Iridium, 6)
                .inputItems(CustomTags.LuV_CIRCUITS)
                .inputItems(wireFine, Cobalt, 32)
                .inputItems(wireFine, Copper, 32)
                .inputItems(wireGtSingle, VanadiumGallium, 2)
                .outputItems(COMPUTER_CASING,
                        ConfigHolder.INSTANCE.recipes.casingsPerCraft)
                .duration(200).EUt(VA[LuV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("advanced_computer_casing")
                .inputItems(COMPUTER_CASING.asItem())
                .inputItems(CustomTags.ZPM_CIRCUITS)
                .inputItems(wireFine, Cobalt, 64)
                .inputItems(wireFine, Electrum, 64)
                .inputItems(wireGtSingle, IndiumTinBariumTitaniumCuprate, 4)
                .outputItems(ADVANCED_COMPUTER_CASING)
                .duration(200).EUt(VA[LuV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("computer_heat_vent")
                .inputItems(frameGt, StainlessSteel)
                .inputItems(ELECTRIC_MOTOR_IV, 2)
                .inputItems(rotor, StainlessSteel, 2)
                .inputItems(pipeTinyFluid, StainlessSteel, 16)
                .inputItems(plate, Copper, 16)
                .inputItems(wireGtSingle, SamariumIronArsenicOxide)
                .outputItems(COMPUTER_HEAT_VENT,
                        ConfigHolder.INSTANCE.recipes.casingsPerCraft)
                .duration(100).EUt(VA[EV])
                .save();

        ASSEMBLY_LINE_RECIPES.recipeBuilder("data_bank")
                .inputItems(COMPUTER_CASING.asItem())
                .inputItems(CustomTags.LuV_CIRCUITS, 8)
                .inputItems(TOOL_DATA_ORB)
                .inputItems(wireFine, Cobalt, 64)
                .inputItems(wireFine, Copper, 64)
                .inputItems(OPTICAL_PIPES[0].asItem(), 4)
                .inputItems(wireGtDouble, IndiumTinBariumTitaniumCuprate, 16)
                .inputFluids(SolderingAlloy, L << 1)
                .inputFluids(Lubricant, 500)
                .outputItems(DATA_BANK)
                .scanner(b -> b
                        .researchStack(DATA_ACCESS_HATCH.asItem())
                        .duration(2400)
                        .EUt(VA[EV]))
                .duration(1200).EUt(6000)
                .save();

        ASSEMBLY_LINE_RECIPES.recipeBuilder("research_station")
                .inputItems(DATA_BANK)
                .inputItems(SENSOR_LuV, 8)
                .inputItems(CustomTags.ZPM_CIRCUITS, 8)
                .inputItems(FIELD_GENERATOR_LuV, 2)
                .inputItems(ELECTRIC_MOTOR_ZPM, 2)
                .inputItems(wireGtDouble, UraniumRhodiumDinaquadide, 32)
                .inputItems(foil, Trinium, 32)
                .inputItems(OPTICAL_PIPES[0].asItem(), 16)
                .inputFluids(SolderingAlloy, L << 3)
                .inputFluids(VanadiumGallium, L << 3)
                .outputItems(RESEARCH_STATION)
                .scanner(b -> b
                        .researchStack(SCANNER[LuV].asItem())
                        .duration(2400)
                        .EUt(VA[IV]))
                .duration(1200).EUt(100000)
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("hpca_empty_component")
                .inputItems(COMPUTER_CASING.asItem())
                .inputItems(CustomTags.IV_CIRCUITS)
                .inputItems(TOOL_DATA_STICK)
                .outputItems(HPCA_EMPTY_COMPONENT)
                .inputFluids(PCBCoolant, 1000)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(200).EUt(VA[IV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("hpca_heat_sink_component")
                .inputItems(HPCA_EMPTY_COMPONENT)
                .inputItems(plate, Aluminium, 32)
                .inputItems(screw, StainlessSteel, 8)
                .outputItems(HPCA_HEAT_SINK_COMPONENT)
                .inputFluids(PCBCoolant, 1000)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(200).EUt(VA[IV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("hpca_active_cooler_component")
                .inputItems(ADVANCED_COMPUTER_CASING.asItem())
                .inputItems(plate, Aluminium, 16)
                .inputItems(pipeTinyFluid, StainlessSteel, 16)
                .inputItems(screw, StainlessSteel, 8)
                .outputItems(HPCA_ACTIVE_COOLER_COMPONENT)
                .inputFluids(PCBCoolant, 1000)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(200).EUt(VA[IV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("hpca_bridge_component")
                .inputItems(ADVANCED_COMPUTER_CASING.asItem())
                .inputItems(CustomTags.UV_CIRCUITS)
                .inputItems(EMITTER_ZPM)
                .inputItems(OPTICAL_PIPES[0].asItem(), 2)
                .outputItems(HPCA_BRIDGE_COMPONENT)
                .inputFluids(PCBCoolant, 1000)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(200).EUt(VA[LuV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("hpca_computation_component")
                .inputItems(HPCA_EMPTY_COMPONENT)
                .inputItems(CustomTags.ZPM_CIRCUITS, 4)
                .inputItems(FIELD_GENERATOR_LuV)
                .outputItems(HPCA_COMPUTATION_COMPONENT)
                .inputFluids(PCBCoolant, 1000)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(200).EUt(VA[LuV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("hpca_advanced_computation_component")
                .inputItems(HPCA_COMPUTATION_COMPONENT)
                .inputItems(CustomTags.UV_CIRCUITS, 4)
                .inputItems(FIELD_GENERATOR_ZPM)
                .outputItems(HPCA_ADVANCED_COMPUTATION_COMPONENT)
                .inputFluids(PCBCoolant, 1000)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(200).EUt(VA[ZPM])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("data_receiver_hatch")
                .inputItems(COMPUTER_CASING.asItem())
                .inputItems(ITEM_IMPORT_BUS[LuV])
                .inputItems(CustomTags.LuV_CIRCUITS)
                .inputItems(SENSOR_IV)
                .inputItems(OPTICAL_PIPES[0].asItem(), 2)
                .inputFluids(Polybenzimidazole, L << 1)
                .outputItems(DATA_HATCH_RECEIVER)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(200).EUt(VA[LuV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("data_transmitter_hatch")
                .inputItems(COMPUTER_CASING.asItem())
                .inputItems(ITEM_EXPORT_BUS[LuV])
                .inputItems(CustomTags.LuV_CIRCUITS)
                .inputItems(EMITTER_IV)
                .inputItems(OPTICAL_PIPES[0].asItem(), 2)
                .inputFluids(Polybenzimidazole, L << 1)
                .outputItems(DATA_HATCH_TRANSMITTER)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(200).EUt(VA[LuV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("computation_receiver_hatch")
                .inputItems(DATA_HATCH_RECEIVER)
                .inputItems(CustomTags.ZPM_CIRCUITS)
                .inputItems(SENSOR_LuV)
                .inputFluids(Polybenzimidazole, L << 1)
                .outputItems(COMPUTATION_HATCH_RECEIVER)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(200).EUt(VA[LuV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("computation_transmitter_hatch")
                .inputItems(DATA_HATCH_TRANSMITTER)
                .inputItems(CustomTags.ZPM_CIRCUITS)
                .inputItems(EMITTER_LuV)
                .inputFluids(Polybenzimidazole, L << 1)
                .outputItems(COMPUTATION_HATCH_TRANSMITTER)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(200).EUt(VA[LuV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("active_transformer")
                .inputItems(POWER_TRANSFORMER[LuV])
                .inputItems(CustomTags.LuV_CIRCUITS, 2)
                .inputItems(wireGtSingle, IndiumTinBariumTitaniumCuprate, 8)
                .inputItems(ULTRA_HIGH_POWER_INTEGRATED_CIRCUIT, 2)
                .inputFluids(PCBCoolant, 1000)
                .outputItems(GTMultiMachines.ACTIVE_TRANSFORMER)
                .duration(300).EUt(VA[LuV])
                .save();

        ASSEMBLER_RECIPES.recipeBuilder("laser_cable")
                .inputItems(CASING_LAMINATED_GLASS.asItem(), 1)
                .inputItems(foil, Osmiridium, 2)
                .inputFluids(Polytetrafluoroethylene, L)
                .outputItems(LASER_PIPES[0])
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(100).EUt(VA[IV])
                .save();
    }
}
