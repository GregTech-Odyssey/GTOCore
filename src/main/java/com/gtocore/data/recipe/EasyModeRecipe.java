package com.gtocore.data.recipe;

import com.gtocore.common.data.machines.OptionalMachine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;

import static com.gtocore.common.data.GTORecipeTypes.ASSEMBLER_RECIPES;

public final class EasyModeRecipe {

    public static void init() {
        VanillaRecipeHelper.addShapelessNBTClearingRecipe("me_simple_pattern_buffer", OptionalMachine.ME_SIMPLE_PATTERN_BUFFER.asStack(),
                OptionalMachine.ME_SIMPLE_PATTERN_BUFFER.asItem());

        ASSEMBLER_RECIPES.builder("me_simple_pattern_buffer")
                .inputItems(GTMachines.DUAL_IMPORT_HATCH[GTValues.MV].asItem())
                .inputItems(AEBlocks.PATTERN_PROVIDER.block().asItem())
                .inputItems(AEItems.CAPACITY_CARD.asItem())
                .inputItems(AEItems.SPEED_CARD.asItem())
                .outputItems(OptionalMachine.ME_SIMPLE_PATTERN_BUFFER.asItem())
                .inputFluids(GTMaterials.SolderingAlloy, 72)
                .EUt(120)
                .duration(200)
                .save();
    }
}
