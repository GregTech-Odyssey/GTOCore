package com.gtocore.data.recipe.classified;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import appeng.core.definitions.AEItems;

import static com.gtocore.common.data.GTORecipeTypes.MASS_FABRICATOR_RECIPES;

final class MassFabricator {

    public static void init() {
        MASS_FABRICATOR_RECIPES.recipeBuilder("uu_matter")
                .inputItems(AEItems.MATTER_BALL.asItem())
                .inputFluids(GTOMaterials.UuAmplifier, 10)
                .outputFluids(GTMaterials.UUMatter, 10)
                .EUt(31457280)
                .duration(20)
                .save();

        MASS_FABRICATOR_RECIPES.recipeBuilder("quasifissioning_plasma")
                .inputItems(TagPrefix.ingot, GTMaterials.Uranium238)
                .inputFluids(GTMaterials.Uranium238, 144)
                .outputFluids(GTOMaterials.Quasifissioning.getFluid(FluidStorageKeys.PLASMA, 144))
                .EUt(7864320)
                .duration(200)
                .save();
        MASS_FABRICATOR_RECIPES.builder("celenegil")
                .inputItems(GTOTagPrefix.gemExquisite, GTOMaterials.SoulCrystal)
                .inputFluids(GTOMaterials.ChromaticGlass, FluidStorageKeys.PLASMA, 144)
                .outputFluids(GTOMaterials.Celenegil, 768)
                .EUt(7864320)
                .duration(480)
                .save();
    }
}
