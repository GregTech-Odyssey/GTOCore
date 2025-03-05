package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.ItemStack;

import appeng.core.definitions.AEItems;

interface MassFabricator {

    static void init() {
        GTORecipeTypes.MASS_FABRICATOR_RECIPES.recipeBuilder(GTOCore.id("uu_matter"))
                .inputItems(new ItemStack(AEItems.MATTER_BALL.asItem()))
                .inputFluids(GTOMaterials.UuAmplifier.getFluid(10))
                .outputFluids(GTMaterials.UUMatter.getFluid(10))
                .EUt(31457280)
                .duration(20)
                .save();

        GTORecipeTypes.MASS_FABRICATOR_RECIPES.recipeBuilder(GTOCore.id("quasifissioning_plasma"))
                .inputItems(TagPrefix.ingot, GTMaterials.Uranium238)
                .inputFluids(GTMaterials.Uranium238.getFluid(144))
                .outputFluids(GTOMaterials.Quasifissioning.getFluid(FluidStorageKeys.PLASMA, 144))
                .EUt(7864320)
                .duration(200)
                .save();
    }
}
