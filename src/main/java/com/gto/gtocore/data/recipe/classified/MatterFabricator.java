package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import net.minecraft.world.item.ItemStack;

import appeng.core.definitions.AEItems;

import static com.gto.gtocore.common.data.GTORecipeTypes.MATTER_FABRICATOR_RECIPES;

interface MatterFabricator {

    static void init() {
        MATTER_FABRICATOR_RECIPES.recipeBuilder(GTOCore.id("uu_amplifier"))
                .inputItems(GTOItems.SCRAP.asStack())
                .circuitMeta(1)
                .outputFluids(GTOMaterials.UuAmplifier.getFluid(1))
                .EUt(491520)
                .duration(200)
                .save();

        MATTER_FABRICATOR_RECIPES.recipeBuilder(GTOCore.id("uu_amplifier_1"))
                .inputItems(GTOItems.SCRAP.asStack())
                .circuitMeta(2)
                .outputItems(new ItemStack(AEItems.MATTER_BALL.asItem(), 64))
                .EUt(491520)
                .duration(1)
                .save();

        MATTER_FABRICATOR_RECIPES.recipeBuilder(GTOCore.id("uu_amplifier_2"))
                .inputItems(GTOItems.SCRAP_BOX.asStack())
                .circuitMeta(2)
                .outputItems(new ItemStack(AEItems.MATTER_BALL.asItem(), 576))
                .EUt(1966080)
                .duration(1)
                .save();

        MATTER_FABRICATOR_RECIPES.recipeBuilder(GTOCore.id("uu_amplifier_a"))
                .inputItems(GTOItems.SCRAP_BOX.asStack())
                .circuitMeta(1)
                .outputFluids(GTOMaterials.UuAmplifier.getFluid(9))
                .EUt(1966080)
                .duration(200)
                .save();
    }
}
