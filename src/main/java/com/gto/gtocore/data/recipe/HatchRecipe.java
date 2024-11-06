package com.gto.gtocore.data.recipe;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOMachines;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.data.CraftingComponentAddition;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.data.tag.TagUtil;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLER_RECIPES;
import static com.gregtechceu.gtceu.data.recipe.CraftingComponent.*;
import static com.gregtechceu.gtceu.data.recipe.misc.MetaTileEntityLoader.registerMachineRecipe;

public class HatchRecipe {

    public static void init(Consumer<FinishedRecipe> provider) {
        registerMachineRecipe(provider, GTMachines.FLUID_IMPORT_HATCH, " G", " M", 'M', HULL, 'G', GLASS);
        registerMachineRecipe(provider, GTMachines.FLUID_EXPORT_HATCH, " M", " G", 'M', HULL, 'G', GLASS);

        registerMachineRecipe(provider, GTMachines.ITEM_IMPORT_BUS, " C", " M", 'M', HULL, 'C',
                TagUtil.createItemTag("chests/wooden"));
        registerMachineRecipe(provider, GTMachines.ITEM_EXPORT_BUS, " M", " C", 'M', HULL, 'C',
                TagUtil.createItemTag("chests/wooden"));

        registerMachineRecipe(provider, GTMachines.DUAL_IMPORT_HATCH, "PG", "CM", 'P', PIPE_NONUPLE, 'M', HULL,
                'G', GLASS, 'C', CraftingComponentAddition.BUFFER);
        registerMachineRecipe(provider, GTMachines.DUAL_EXPORT_HATCH, "MG", "CP", 'P', PIPE_NONUPLE, 'M', HULL,
                'G', GLASS, 'C', CraftingComponentAddition.BUFFER);

        Material[] multiHatchMaterials = new Material[] {
                GTMaterials.Neutronium, GTOMaterials.Enderium, GTOMaterials.Enderium,
                GTOMaterials.HeavyQuarkDegenerateMatter,
                GTOMaterials.HeavyQuarkDegenerateMatter,
        };
        for (int i = 0; i < multiHatchMaterials.length; i++) {
            var tier = GTMachines.MULTI_HATCH_TIERS[i + 6];
            var tierName = VN[tier].toLowerCase();

            var material = multiHatchMaterials[i];

            var importHatch = GTMachines.FLUID_IMPORT_HATCH[tier];
            var exportHatch = GTMachines.FLUID_EXPORT_HATCH[tier];

            var importHatch4x = GTMachines.FLUID_IMPORT_HATCH_4X[tier];
            var exportHatch4x = GTMachines.FLUID_EXPORT_HATCH_4X[tier];
            var importHatch9x = GTMachines.FLUID_IMPORT_HATCH_9X[tier];
            var exportHatch9x = GTMachines.FLUID_EXPORT_HATCH_9X[tier];

            VanillaRecipeHelper.addShapedRecipe(
                    provider, true, GTOCore.id("fluid_import_hatch_4x_" + tierName),
                    importHatch4x.asStack(), "P", "M",
                    'M', importHatch.asStack(),
                    'P', new UnificationEntry(TagPrefix.pipeQuadrupleFluid, material));
            VanillaRecipeHelper.addShapedRecipe(
                    provider, true, GTOCore.id("fluid_export_hatch_4x_" + tierName),
                    exportHatch4x.asStack(), "M", "P",
                    'M', exportHatch.asStack(),
                    'P', new UnificationEntry(TagPrefix.pipeQuadrupleFluid, material));
            VanillaRecipeHelper.addShapedRecipe(
                    provider, true, GTOCore.id("fluid_import_hatch_9x_" + tierName),
                    importHatch9x.asStack(), "P", "M",
                    'M', importHatch.asStack(),
                    'P', new UnificationEntry(TagPrefix.pipeNonupleFluid, material));
            VanillaRecipeHelper.addShapedRecipe(
                    provider, true, GTOCore.id("fluid_export_hatch_9x_" + tierName),
                    exportHatch9x.asStack(), "M", "P",
                    'M', exportHatch.asStack(),
                    'P', new UnificationEntry(TagPrefix.pipeNonupleFluid, material));
        }

        for (int tier = 1; tier < 4; tier++) {
            var hatch = GTOMachines.ENERGY_INPUT_HATCH_4A[tier];

            ASSEMBLER_RECIPES.recipeBuilder("energy_hatch_4a_" + GTValues.VN[tier].toLowerCase())
                    .inputItems(GTMachines.ENERGY_INPUT_HATCH[tier])
                    .inputItems(WIRE_QUAD.getIngredient(tier), 2)
                    .inputItems(PLATE.getIngredient(tier), 2)
                    .outputItems(hatch)
                    .duration(100).EUt(VA[tier]).save(provider);
        }

        for (int tier = 1; tier < 4; tier++) {
            MachineDefinition hatch = GTOMachines.ENERGY_INPUT_HATCH_16A[tier];
            MachineDefinition transformer;
            transformer = GTMachines.TRANSFORMER[tier];
            ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("energy_hatch_16a_" + GTValues.VN[tier].toLowerCase()))
                    .inputItems(transformer)
                    .inputItems(GTOMachines.ENERGY_INPUT_HATCH_4A[tier])
                    .inputItems(WIRE_OCT.getIngredient(tier), 2)
                    .inputItems(PLATE.getIngredient(tier), 4)
                    .outputItems(hatch)
                    .duration(200).EUt(VA[tier]).save(provider);
        }

        for (int tier = 1; tier < 4; tier++) {
            var hatch = GTOMachines.ENERGY_OUTPUT_HATCH_4A[tier];
            ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("dynamo_hatch_4a_" + GTValues.VN[tier].toLowerCase()))
                    .inputItems(GTMachines.ENERGY_OUTPUT_HATCH[tier])
                    .inputItems(WIRE_QUAD.getIngredient(tier), 2)
                    .inputItems(PLATE.getIngredient(tier), 2)
                    .outputItems(hatch)
                    .duration(100)
                    .EUt(VA[tier])
                    .save(provider);
        }

        for (int tier = 1; tier < 4; tier++) {
            MachineDefinition hatch = GTOMachines.ENERGY_OUTPUT_HATCH_16A[tier];

            MachineDefinition transformer;
            transformer = GTMachines.TRANSFORMER[tier];

            ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("dynamo_hatch_16a_" + GTValues.VN[tier].toLowerCase()))
                    .inputItems(transformer)
                    .inputItems(GTOMachines.ENERGY_OUTPUT_HATCH_4A[tier])
                    .inputItems(WIRE_OCT.getIngredient(tier), 2)
                    .inputItems(PLATE.getIngredient(tier), 4)
                    .outputItems(hatch)
                    .duration(200).EUt(VA[tier]).save(provider);
        }
    }
}
