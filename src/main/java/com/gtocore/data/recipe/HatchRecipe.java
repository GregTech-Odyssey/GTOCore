package com.gtocore.data.recipe;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMachines;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.data.CraftingComponents;

import com.gtolib.GTOCore;
import com.gtolib.utils.RLUtils;
import com.gtolib.utils.RegistriesUtils;
import com.gtolib.utils.TagUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.machines.GTMachineUtils;
import com.gregtechceu.gtceu.common.data.machines.GTMultiMachines;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.world.item.Item;

import com.hepdd.gtmthings.data.CustomItems;
import com.tterrag.registrate.util.entry.ItemEntry;

import java.util.List;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.lens;
import static com.gregtechceu.gtceu.common.data.GTMachines.*;
import static com.gregtechceu.gtceu.common.data.GTMachines.HULL;
import static com.gregtechceu.gtceu.common.data.GTMaterials.Diamond;
import static com.gregtechceu.gtceu.data.recipe.GTCraftingComponents.*;
import static com.gregtechceu.gtceu.data.recipe.GTCraftingComponents.PUMP;
import static com.gtocore.common.data.GTORecipeTypes.ASSEMBLER_RECIPES;
import static com.gtocore.common.data.GTORecipeTypes.LASER_WELDER_RECIPES;

final class HatchRecipe {

    public static void init() {
        MaterialEntry lensDiamond = new MaterialEntry(lens, Diamond);
        var activeTransformer = GTMultiMachines.ACTIVE_TRANSFORMER.asItem();
        var coverEnergyDetector = GTItems.COVER_ENERGY_DETECTOR_ADVANCED.asItem();

        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("rotor_holder_uhv"), GTMachines.ROTOR_HOLDER[UHV].asItem(),
                "SGS", "GHG", "SGS", 'H', GTMachines.HULL[UHV].asItem(), 'G',
                new MaterialEntry(TagPrefix.gear, GTOMaterials.Orichalcum), 'S',
                new MaterialEntry(TagPrefix.gearSmall, GTMaterials.Neutronium));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("rotor_holder_uev"), GTMachines.ROTOR_HOLDER[UEV].asItem(),
                "SGS", "GHG", "SGS", 'H', GTMachines.HULL[UEV].asItem(), 'G',
                new MaterialEntry(TagPrefix.gear, GTOMaterials.AstralTitanium), 'S',
                new MaterialEntry(TagPrefix.gearSmall, GTOMaterials.Quantanium));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("rotor_holder_uiv"), GTMachines.ROTOR_HOLDER[UIV].asItem(),
                "SGS", "GHG", "SGS", 'H', GTMachines.HULL[UIV].asItem(), 'G',
                new MaterialEntry(TagPrefix.gear, GTOMaterials.CelestialTungsten), 'S',
                new MaterialEntry(TagPrefix.gearSmall, GTOMaterials.Infuscolium));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("rotor_holder_uxv"), GTMachines.ROTOR_HOLDER[UXV].asItem(),
                "SGS", "GHG", "SGS", 'H', GTMachines.HULL[UXV].asItem(), 'G',
                new MaterialEntry(TagPrefix.gear, GTOMaterials.Vibramantium), 'S',
                new MaterialEntry(TagPrefix.gearSmall, GTOMaterials.HastelloyK243));
        VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("rotor_holder_opv"), GTMachines.ROTOR_HOLDER[OpV].asItem(),
                "SGS", "GHG", "SGS", 'H', GTMachines.HULL[OpV].asItem(), 'G',
                new MaterialEntry(TagPrefix.gear, GTOMaterials.HexaphaseCopper), 'S',
                new MaterialEntry(TagPrefix.gearSmall, GTOMaterials.TranscendentMetal));
        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("primitive_blast_furnace_hatch"), GTOMachines.PRIMITIVE_BLAST_FURNACE_HATCH.asItem(),
                "ABA",
                'B', GTBlocks.CASING_PRIMITIVE_BRICKS.asItem(), 'A', TagUtils.createTag(RLUtils.forge("chests")));

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("steam_fluid_input_hatch"), GTOMachines.STEAM_FLUID_INPUT_HATCH.asItem(),
                " A ",
                " B ",
                "   ",
                'A', RegistriesUtils.getItemStack("gtceu:wood_drum"), 'B', GTBlocks.BRONZE_HULL.asItem());

        VanillaRecipeHelper.addShapedRecipe(GTOCore.id("steam_fluid_output_hatch"), GTOMachines.STEAM_FLUID_OUTPUT_HATCH.asItem(),
                "   ",
                " A ",
                " B ",
                'A', GTBlocks.BRONZE_HULL.asItem(), 'B', RegistriesUtils.getItemStack("gtceu:wood_drum"));

        for (int tier : tiersBetween(LV, MAX)) {
            ASSEMBLER_RECIPES.recipeBuilder("dual_import_bus_" + VN[tier].toLowerCase())
                    .inputItems(FRAME.get(tier))
                    .inputItems(ITEM_IMPORT_BUS[tier].asItem())
                    .inputItems(FLUID_IMPORT_HATCH[tier].asItem())
                    .inputItems(CraftingComponents.BUFFER.get(tier))
                    .inputItems(PIPE_NONUPLE.get(tier))
                    .inputItems(GLASS.get(tier))
                    .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                    .outputItems(DUAL_IMPORT_HATCH[tier].asItem())
                    .duration(300)
                    .EUt(VA[tier])
                    .save();

            ASSEMBLER_RECIPES.recipeBuilder("dual_export_bus_" + VN[tier].toLowerCase())
                    .inputItems(FRAME.get(tier))
                    .inputItems(ITEM_EXPORT_BUS[tier].asItem())
                    .inputItems(FLUID_EXPORT_HATCH[tier].asItem())
                    .inputItems(CraftingComponents.BUFFER.get(tier))
                    .inputItems(PIPE_NONUPLE.get(tier))
                    .inputItems(GLASS.get(tier))
                    .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                    .outputItems(DUAL_EXPORT_HATCH[tier].asItem())
                    .duration(300)
                    .EUt(VA[tier])
                    .save();
        }

        Material[] multiHatchMaterials = {
                GTMaterials.Neutronium, GTOMaterials.Enderium, GTOMaterials.Enderium,
                GTOMaterials.HeavyQuarkDegenerateMatter,
                GTOMaterials.HeavyQuarkDegenerateMatter,
        };
        for (int i = 0; i < multiHatchMaterials.length; i++) {
            var tier = GTMachineUtils.MULTI_HATCH_TIERS[i + 6];
            var tierName = VN[tier].toLowerCase();

            var material = multiHatchMaterials[i];

            var importHatch = GTMachines.FLUID_IMPORT_HATCH[tier];
            var exportHatch = GTMachines.FLUID_EXPORT_HATCH[tier];

            var importHatch4x = GTMachines.FLUID_IMPORT_HATCH_4X[tier];
            var exportHatch4x = GTMachines.FLUID_EXPORT_HATCH_4X[tier];
            var importHatch9x = GTMachines.FLUID_IMPORT_HATCH_9X[tier];
            var exportHatch9x = GTMachines.FLUID_EXPORT_HATCH_9X[tier];

            VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("fluid_import_hatch_4x_" + tierName),
                    importHatch4x.asItem(), "P", "M",
                    'M', importHatch.asItem(),
                    'P', new MaterialEntry(TagPrefix.pipeQuadrupleFluid, material));
            VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("fluid_export_hatch_4x_" + tierName),
                    exportHatch4x.asItem(), "M", "P",
                    'M', exportHatch.asItem(),
                    'P', new MaterialEntry(TagPrefix.pipeQuadrupleFluid, material));
            VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("fluid_import_hatch_9x_" + tierName),
                    importHatch9x.asItem(), "P", "M",
                    'M', importHatch.asItem(),
                    'P', new MaterialEntry(TagPrefix.pipeNonupleFluid, material));
            VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("fluid_export_hatch_9x_" + tierName),
                    exportHatch9x.asItem(), "M", "P",
                    'M', exportHatch.asItem(),
                    'P', new MaterialEntry(TagPrefix.pipeNonupleFluid, material));
        }

        for (int tier = 1; tier < 4; tier++) {
            var hatch = GTOMachines.ENERGY_INPUT_HATCH_4A[tier];

            ASSEMBLER_RECIPES.recipeBuilder("energy_hatch_4a_" + VN[tier].toLowerCase())
                    .inputItems(GTMachines.ENERGY_INPUT_HATCH[tier])
                    .inputItems(WIRE_QUAD.get(tier), 2)
                    .inputItems(PLATE.get(tier), 2)
                    .outputItems(hatch)
                    .duration(100).EUt(VA[tier]).save();
        }

        for (int tier = 1; tier < 4; tier++) {
            MachineDefinition hatch = GTOMachines.ENERGY_INPUT_HATCH_16A[tier];
            MachineDefinition transformer;
            transformer = GTMachines.TRANSFORMER[tier];
            ASSEMBLER_RECIPES.recipeBuilder("energy_hatch_16a_" + VN[tier].toLowerCase())
                    .inputItems(transformer)
                    .inputItems(GTOMachines.ENERGY_INPUT_HATCH_4A[tier])
                    .inputItems(WIRE_OCT.get(tier), 2)
                    .inputItems(PLATE.get(tier), 4)
                    .outputItems(hatch)
                    .duration(200).EUt(VA[tier]).save();
        }

        for (int tier = 1; tier < 4; tier++) {
            var hatch = GTOMachines.ENERGY_OUTPUT_HATCH_4A[tier];
            ASSEMBLER_RECIPES.recipeBuilder("dynamo_hatch_4a_" + VN[tier].toLowerCase())
                    .inputItems(GTMachines.ENERGY_OUTPUT_HATCH[tier])
                    .inputItems(WIRE_QUAD.get(tier), 2)
                    .inputItems(PLATE.get(tier), 2)
                    .outputItems(hatch)
                    .duration(100)
                    .EUt(VA[tier])
                    .save();
        }

        for (int tier = 1; tier < 4; tier++) {
            MachineDefinition hatch = GTOMachines.ENERGY_OUTPUT_HATCH_16A[tier];

            MachineDefinition transformer;
            transformer = GTMachines.TRANSFORMER[tier];

            ASSEMBLER_RECIPES.recipeBuilder("dynamo_hatch_16a_" + VN[tier].toLowerCase())
                    .inputItems(transformer)
                    .inputItems(GTOMachines.ENERGY_OUTPUT_HATCH_4A[tier])
                    .inputItems(WIRE_OCT.get(tier), 2)
                    .inputItems(PLATE.get(tier), 4)
                    .outputItems(hatch)
                    .duration(200).EUt(VA[tier]).save();
        }

        List<ItemEntry<ComponentItem>> WIRELESS_ENERGY_RECEIVE_COVER = List.of(
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LV,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_MV,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_HV,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_EV,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_IV,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LUV,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_ZPM,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UV,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UHV,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UEV,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UIV,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UXV,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_OPV,
                GTOItems.WIRELESS_ENERGY_RECEIVE_COVER_MAX);

        List<ItemEntry<ComponentItem>> WIRELESS_ENERGY_RECEIVE_COVER_4A = List.of(
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LV_4A,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_MV_4A,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_HV_4A,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_EV_4A,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_IV_4A,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LUV_4A,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_ZPM_4A,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UV_4A,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UHV_4A,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UEV_4A,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UIV_4A,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UXV_4A,
                CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_OPV_4A,
                GTOItems.WIRELESS_ENERGY_RECEIVE_COVER_MAX_4A);

        for (int tier : tiersBetween(LV, MAX)) {
            String tierName = VN[tier].toLowerCase();
            ASSEMBLER_RECIPES.recipeBuilder("programmablec_hatch_" + tierName + "_4a")
                    .inputItems(GTMachines.DUAL_IMPORT_HATCH[tier].asItem())
                    .inputItems(CustomItems.VIRTUAL_ITEM_PROVIDER.asItem())
                    .inputItems(ROBOT_ARM.get(tier))
                    .inputItems(CONVEYOR.get(tier))
                    .inputItems(CustomTags.CIRCUITS_ARRAY[tier], 4)
                    .inputFluids(GTMaterials.SolderingAlloy.getFluid(144))
                    .outputItems(GTOMachines.PROGRAMMABLEC_HATCH[tier].asItem())
                    .duration(400)
                    .EUt(GTValues.VA[tier])
                    .save();

            LASER_WELDER_RECIPES.recipeBuilder("wireless_energy_input_hatch_" + tierName)
                    .inputItems(GTMachines.ENERGY_INPUT_HATCH[tier].asItem())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER.get(tier - 1).asItem())
                    .inputItems(coverEnergyDetector)
                    .outputItems(GTOMachines.WIRELESS_INPUT_HATCH_2[tier].asItem())
                    .duration(200)
                    .EUt(VA[tier])
                    .save();

            LASER_WELDER_RECIPES.recipeBuilder("wireless_energy_output_hatch_" + tierName)
                    .inputItems(GTMachines.ENERGY_OUTPUT_HATCH[tier].asItem())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER.get(tier - 1).asItem())
                    .inputItems(coverEnergyDetector)
                    .outputItems(GTOMachines.WIRELESS_OUTPUT_HATCH_2[tier].asItem())
                    .duration(200)
                    .EUt(VA[tier])
                    .save();

            LASER_WELDER_RECIPES.recipeBuilder("wireless_energy_input_hatch_" + tierName + "_4a")
                    .inputItems(tier > HV ? GTMachines.ENERGY_INPUT_HATCH_4A[tier].asItem() : GTOMachines.ENERGY_INPUT_HATCH_4A[tier].asItem())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER.get(tier - 1).asItem(), 2)
                    .inputItems(coverEnergyDetector)
                    .outputItems(GTOMachines.WIRELESS_INPUT_HATCH_4[tier].asItem())
                    .duration(200)
                    .EUt(VA[tier])
                    .save();

            LASER_WELDER_RECIPES.recipeBuilder("wireless_energy_input_hatch_" + tierName + "_16a")
                    .inputItems(tier > HV ? GTMachines.ENERGY_INPUT_HATCH_16A[tier].asItem() : GTOMachines.ENERGY_INPUT_HATCH_16A[tier].asItem())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asItem())
                    .inputItems(coverEnergyDetector)
                    .outputItems(GTOMachines.WIRELESS_INPUT_HATCH_16[tier].asItem())
                    .duration(200)
                    .EUt(VA[tier])
                    .save();

            LASER_WELDER_RECIPES.recipeBuilder("wireless_energy_output_hatch_" + tierName + "_4a")
                    .inputItems(tier > HV ? GTMachines.ENERGY_OUTPUT_HATCH_4A[tier].asItem() : GTOMachines.ENERGY_OUTPUT_HATCH_4A[tier].asItem())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER.get(tier - 1).asItem(), 2)
                    .inputItems(coverEnergyDetector)
                    .outputItems(GTOMachines.WIRELESS_OUTPUT_HATCH_4[tier].asItem())
                    .duration(200)
                    .EUt(VA[tier])
                    .save();

            LASER_WELDER_RECIPES.recipeBuilder("wireless_energy_output_hatch_" + tierName + "_16a")
                    .inputItems(tier > HV ? GTMachines.ENERGY_OUTPUT_HATCH_16A[tier].asItem() : GTOMachines.ENERGY_OUTPUT_HATCH_16A[tier].asItem())
                    .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asItem())
                    .inputItems(coverEnergyDetector)
                    .outputItems(GTOMachines.WIRELESS_OUTPUT_HATCH_16[tier].asItem())
                    .duration(200)
                    .EUt(VA[tier])
                    .save();

            if (tier > HV) {
                LASER_WELDER_RECIPES.recipeBuilder("wireless_energy_input_hatch_" + tierName + "_64a")
                        .inputItems(GTMachines.SUBSTATION_ENERGY_INPUT_HATCH[tier].asItem())
                        .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asItem(), 2)
                        .inputItems(coverEnergyDetector)
                        .outputItems(GTOMachines.WIRELESS_INPUT_HATCH_64[tier].asItem())
                        .duration(200)
                        .EUt(VA[tier])
                        .save();

                LASER_WELDER_RECIPES.recipeBuilder("wireless_energy_output_hatch_" + tierName + "_64a")
                        .inputItems(GTMachines.SUBSTATION_ENERGY_OUTPUT_HATCH[tier].asItem())
                        .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asItem(), 2)
                        .inputItems(coverEnergyDetector)
                        .outputItems(GTOMachines.WIRELESS_OUTPUT_HATCH_64[tier].asItem())
                        .duration(200)
                        .EUt(VA[tier])
                        .save();

                if (tier > EV) {
                    LASER_WELDER_RECIPES.recipeBuilder("wireless_energy_input_hatch_" + tierName + "_256a")
                            .inputItems(GTMachines.LASER_INPUT_HATCH_256[tier].asItem())
                            .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asItem(), 2)
                            .inputItems(activeTransformer)
                            .outputItems(GTOMachines.WIRELESS_INPUT_HATCH_256[tier].asItem())
                            .duration(200)
                            .EUt(VA[tier])
                            .save();

                    LASER_WELDER_RECIPES.recipeBuilder("wireless_energy_input_hatch_" + tierName + "_1024a")
                            .inputItems(GTMachines.LASER_INPUT_HATCH_1024[tier].asItem())
                            .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asItem(), 4)
                            .inputItems(activeTransformer)
                            .outputItems(GTOMachines.WIRELESS_INPUT_HATCH_1024[tier].asItem())
                            .duration(200)
                            .EUt(VA[tier])
                            .save();

                    LASER_WELDER_RECIPES.recipeBuilder("wireless_energy_input_hatch_" + tierName + "_4096a")
                            .inputItems(GTMachines.LASER_INPUT_HATCH_4096[tier].asItem())
                            .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asItem(), 8)
                            .inputItems(activeTransformer)
                            .outputItems(GTOMachines.WIRELESS_INPUT_HATCH_4096[tier].asItem())
                            .duration(200)
                            .EUt(VA[tier])
                            .save();

                    LASER_WELDER_RECIPES.recipeBuilder("wireless_energy_input_hatch_" + tierName + "_16384a")
                            .inputItems(GTOMachines.LASER_INPUT_HATCH_16384[tier].asItem())
                            .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asItem(), 12)
                            .inputItems(activeTransformer)
                            .outputItems(GTOMachines.WIRELESS_INPUT_HATCH_16384[tier].asItem())
                            .duration(200)
                            .EUt(VA[tier])
                            .save();

                    LASER_WELDER_RECIPES.recipeBuilder("wireless_energy_input_hatch_" + tierName + "_65536a")
                            .inputItems(GTOMachines.LASER_INPUT_HATCH_65536[tier].asItem())
                            .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asItem(), 16)
                            .inputItems(activeTransformer)
                            .outputItems(GTOMachines.WIRELESS_INPUT_HATCH_65536[tier].asItem())
                            .duration(200)
                            .EUt(VA[tier])
                            .save();

                    LASER_WELDER_RECIPES.recipeBuilder("wireless_energy_input_hatch_" + tierName + "_262144a")
                            .inputItems(GTOMachines.LASER_INPUT_HATCH_262144[tier].asItem())
                            .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asItem(), 24)
                            .inputItems(activeTransformer)
                            .outputItems(GTOMachines.WIRELESS_INPUT_HATCH_262144[tier].asItem())
                            .duration(200)
                            .EUt(VA[tier])
                            .save();

                    LASER_WELDER_RECIPES.recipeBuilder("wireless_energy_input_hatch_" + tierName + "_1048576a")
                            .inputItems(GTOMachines.LASER_INPUT_HATCH_1048576[tier].asItem())
                            .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asItem(), 32)
                            .inputItems(activeTransformer)
                            .outputItems(GTOMachines.WIRELESS_INPUT_HATCH_1048576[tier].asItem())
                            .duration(200)
                            .EUt(VA[tier])
                            .save();

                    LASER_WELDER_RECIPES.recipeBuilder("wireless_energy_input_hatch_" + tierName + "_4194304a")
                            .inputItems(GTOMachines.LASER_INPUT_HATCH_4194304[tier].asItem())
                            .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asItem(), 64)
                            .inputItems(activeTransformer)
                            .outputItems(GTOMachines.WIRELESS_INPUT_HATCH_4194304[tier].asItem())
                            .duration(200)
                            .EUt(VA[tier])
                            .save();

                    LASER_WELDER_RECIPES.recipeBuilder("wireless_energy_input_hatch_" + tierName + "_16777216a")
                            .inputItems(GTOMachines.LASER_INPUT_HATCH_16777216[tier].asItem())
                            .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asItem(), 64)
                            .inputItems(activeTransformer)
                            .outputItems(GTOMachines.WIRELESS_INPUT_HATCH_16777216[tier].asItem())
                            .duration(200)
                            .EUt(VA[tier])
                            .save();

                    LASER_WELDER_RECIPES.recipeBuilder("wireless_energy_output_hatch_" + tierName + "_256a")
                            .inputItems(GTMachines.LASER_OUTPUT_HATCH_256[tier].asItem())
                            .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asItem(), 2)
                            .inputItems(activeTransformer)
                            .outputItems(GTOMachines.WIRELESS_OUTPUT_HATCH_256[tier].asItem())
                            .duration(200)
                            .EUt(VA[tier])
                            .save();

                    LASER_WELDER_RECIPES.recipeBuilder("wireless_energy_output_hatch_" + tierName + "_1024a")
                            .inputItems(GTMachines.LASER_OUTPUT_HATCH_1024[tier].asItem())
                            .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asItem(), 4)
                            .inputItems(activeTransformer)
                            .outputItems(GTOMachines.WIRELESS_OUTPUT_HATCH_1024[tier].asItem())
                            .duration(200)
                            .EUt(VA[tier])
                            .save();

                    LASER_WELDER_RECIPES.recipeBuilder("wireless_energy_output_hatch_" + tierName + "_4096a")
                            .inputItems(GTMachines.LASER_OUTPUT_HATCH_4096[tier].asItem())
                            .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asItem(), 8)
                            .inputItems(activeTransformer)
                            .outputItems(GTOMachines.WIRELESS_OUTPUT_HATCH_4096[tier].asItem())
                            .duration(200)
                            .EUt(VA[tier])
                            .save();

                    LASER_WELDER_RECIPES.recipeBuilder("wireless_energy_output_hatch_" + tierName + "_16384a")
                            .inputItems(GTOMachines.LASER_OUTPUT_HATCH_16384[tier].asItem())
                            .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asItem(), 12)
                            .inputItems(activeTransformer)
                            .outputItems(GTOMachines.WIRELESS_OUTPUT_HATCH_16384[tier].asItem())
                            .duration(200)
                            .EUt(VA[tier])
                            .save();

                    LASER_WELDER_RECIPES.recipeBuilder("wireless_energy_output_hatch_" + tierName + "_65536a")
                            .inputItems(GTOMachines.LASER_OUTPUT_HATCH_65536[tier].asItem())
                            .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asItem(), 16)
                            .inputItems(activeTransformer)
                            .outputItems(GTOMachines.WIRELESS_OUTPUT_HATCH_65536[tier].asItem())
                            .duration(200)
                            .EUt(VA[tier])
                            .save();

                    LASER_WELDER_RECIPES.recipeBuilder("wireless_energy_output_hatch_" + tierName + "_262144a")
                            .inputItems(GTOMachines.LASER_OUTPUT_HATCH_262144[tier].asItem())
                            .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asItem(), 24)
                            .inputItems(activeTransformer)
                            .outputItems(GTOMachines.WIRELESS_OUTPUT_HATCH_262144[tier].asItem())
                            .duration(200)
                            .EUt(VA[tier])
                            .save();

                    LASER_WELDER_RECIPES.recipeBuilder("wireless_energy_output_hatch_" + tierName + "_1048576a")
                            .inputItems(GTOMachines.LASER_OUTPUT_HATCH_1048576[tier].asItem())
                            .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asItem(), 32)
                            .inputItems(activeTransformer)
                            .outputItems(GTOMachines.WIRELESS_OUTPUT_HATCH_1048576[tier].asItem())
                            .duration(200)
                            .EUt(VA[tier])
                            .save();

                    LASER_WELDER_RECIPES.recipeBuilder("wireless_energy_output_hatch_" + tierName + "_4194304a")
                            .inputItems(GTOMachines.LASER_OUTPUT_HATCH_4194304[tier].asItem())
                            .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asItem(), 64)
                            .inputItems(activeTransformer)
                            .outputItems(GTOMachines.WIRELESS_OUTPUT_HATCH_4194304[tier].asItem())
                            .duration(200)
                            .EUt(VA[tier])
                            .save();

                    LASER_WELDER_RECIPES.recipeBuilder("wireless_energy_output_hatch_" + tierName + "16777216a")
                            .inputItems(GTOMachines.LASER_OUTPUT_HATCH_16777216[tier].asItem())
                            .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asItem(), 64)
                            .inputItems(activeTransformer)
                            .outputItems(GTOMachines.WIRELESS_OUTPUT_HATCH_16777216[tier].asItem())
                            .duration(200)
                            .EUt(VA[tier])
                            .save();

                    Item sensor = ((Item) SENSOR.get(tier));
                    Item emitter = ((Item) EMITTER.get(tier));
                    Item pump = ((Item) PUMP.get(tier));
                    MaterialEntry cable = (MaterialEntry) CABLE_OCT.get(tier);

                    ASSEMBLER_RECIPES.recipeBuilder(tierName + "16384_i")
                            .inputItems(HULL[tier])
                            .inputItems(lensDiamond, 8)
                            .inputItems(sensor, 8)
                            .inputItems(pump, 8)
                            .inputItems(cable, 4)
                            .circuitMeta(4)
                            .outputItems(GTOMachines.LASER_INPUT_HATCH_16384[tier].asItem())
                            .duration(2400)
                            .EUt(VA[tier])
                            .save();

                    ASSEMBLER_RECIPES.recipeBuilder(tierName + "65536_i")
                            .inputItems(HULL[tier])
                            .inputItems(lensDiamond, 16)
                            .inputItems(sensor, 16)
                            .inputItems(pump, 16)
                            .inputItems(cable, 8)
                            .circuitMeta(5)
                            .outputItems(GTOMachines.LASER_INPUT_HATCH_65536[tier].asItem())
                            .duration(3200)
                            .EUt(VA[tier])
                            .save();

                    ASSEMBLER_RECIPES.recipeBuilder(tierName + "262144_i")
                            .inputItems(HULL[tier])
                            .inputItems(lensDiamond, 32)
                            .inputItems(sensor, 32)
                            .inputItems(pump, 32)
                            .inputItems(cable, 16)
                            .circuitMeta(6)
                            .outputItems(GTOMachines.LASER_INPUT_HATCH_262144[tier].asItem())
                            .duration(4800)
                            .EUt(VA[tier])
                            .save();

                    ASSEMBLER_RECIPES.recipeBuilder(tierName + "1048576_i")
                            .inputItems(HULL[tier])
                            .inputItems(lensDiamond, 64)
                            .inputItems(sensor, 64)
                            .inputItems(pump, 64)
                            .inputItems(cable, 32)
                            .circuitMeta(7)
                            .outputItems(GTOMachines.LASER_INPUT_HATCH_1048576[tier].asItem())
                            .duration(6400)
                            .EUt(VA[tier])
                            .save();

                    ASSEMBLER_RECIPES.recipeBuilder(tierName + "4194304_i")
                            .inputItems(HULL[tier])
                            .inputItems(lensDiamond, 128)
                            .inputItems(sensor, 128)
                            .inputItems(pump, 128)
                            .inputItems(cable, 64)
                            .circuitMeta(8)
                            .outputItems(GTOMachines.LASER_INPUT_HATCH_4194304[tier].asItem())
                            .duration(9600)
                            .EUt(VA[tier])
                            .save();

                    ASSEMBLER_RECIPES.recipeBuilder(tierName + "16777216_i")
                            .inputItems(HULL[tier])
                            .inputItems(lensDiamond, 256)
                            .inputItems(sensor, 256)
                            .inputItems(pump, 256)
                            .inputItems(cable, 128)
                            .circuitMeta(9)
                            .outputItems(GTOMachines.LASER_INPUT_HATCH_16777216[tier].asItem())
                            .duration(14400)
                            .EUt(VA[tier])
                            .save();

                    ASSEMBLER_RECIPES.recipeBuilder(tierName + "16384_o")
                            .inputItems(HULL[tier])
                            .inputItems(lensDiamond, 8)
                            .inputItems(emitter, 8)
                            .inputItems(pump, 8)
                            .inputItems(cable, 4)
                            .circuitMeta(4)
                            .outputItems(GTOMachines.LASER_OUTPUT_HATCH_16384[tier].asItem())
                            .duration(2400)
                            .EUt(VA[tier])
                            .save();

                    ASSEMBLER_RECIPES.recipeBuilder(tierName + "65536_o")
                            .inputItems(HULL[tier])
                            .inputItems(lensDiamond, 16)
                            .inputItems(emitter, 16)
                            .inputItems(pump, 16)
                            .inputItems(cable, 8)
                            .circuitMeta(5)
                            .outputItems(GTOMachines.LASER_OUTPUT_HATCH_65536[tier].asItem())
                            .duration(3200)
                            .EUt(VA[tier])
                            .save();

                    ASSEMBLER_RECIPES.recipeBuilder(tierName + "262144_o")
                            .inputItems(HULL[tier])
                            .inputItems(lensDiamond, 32)
                            .inputItems(emitter, 32)
                            .inputItems(pump, 32)
                            .inputItems(cable, 16)
                            .circuitMeta(6)
                            .outputItems(GTOMachines.LASER_OUTPUT_HATCH_262144[tier].asItem())
                            .duration(4800)
                            .EUt(VA[tier])
                            .save();

                    ASSEMBLER_RECIPES.recipeBuilder(tierName + "1048576_o")
                            .inputItems(HULL[tier])
                            .inputItems(lensDiamond, 64)
                            .inputItems(emitter, 64)
                            .inputItems(pump, 64)
                            .inputItems(cable, 32)
                            .circuitMeta(7)
                            .outputItems(GTOMachines.LASER_OUTPUT_HATCH_1048576[tier].asItem())
                            .duration(6400)
                            .EUt(VA[tier])
                            .save();

                    ASSEMBLER_RECIPES.recipeBuilder(tierName + "4194304_o")
                            .inputItems(HULL[tier])
                            .inputItems(lensDiamond, 128)
                            .inputItems(emitter, 128)
                            .inputItems(pump, 128)
                            .inputItems(cable, 64)
                            .circuitMeta(8)
                            .outputItems(GTOMachines.LASER_OUTPUT_HATCH_4194304[tier].asItem())
                            .duration(9600)
                            .EUt(VA[tier])
                            .save();

                    ASSEMBLER_RECIPES.recipeBuilder(tierName + "16777216_o")
                            .inputItems(HULL[tier])
                            .inputItems(lensDiamond, 256)
                            .inputItems(emitter, 256)
                            .inputItems(pump, 256)
                            .inputItems(cable, 128)
                            .circuitMeta(9)
                            .outputItems(GTOMachines.LASER_OUTPUT_HATCH_16777216[tier].asItem())
                            .duration(14400)
                            .EUt(VA[tier])
                            .save();
                }
            }
        }
    }
}
