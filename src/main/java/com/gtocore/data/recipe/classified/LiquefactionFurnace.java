package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOFluids;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import net.minecraftforge.fluids.FluidStack;

import static com.gtocore.common.data.GTORecipeTypes.LIQUEFACTION_FURNACE_RECIPES;

final class LiquefactionFurnace {

    public static void init() {
        LIQUEFACTION_FURNACE_RECIPES.recipeBuilder("gelid_cryotheum")
                .inputItems(GTOItems.DUST_CRYOTHEUM)
                .outputFluids(new FluidStack(GTOFluids.GELID_CRYOTHEUM.get(), 144))
                .EUt(491520)
                .duration(80)
                .blastFurnaceTemp(300)
                .save();

        LIQUEFACTION_FURNACE_RECIPES.recipeBuilder("antimatter")
                .inputItems(GTOItems.PELLET_ANTIMATTER)
                .outputFluids(GTOMaterials.Antimatter, 1000)
                .EUt(480)
                .duration(2000)
                .blastFurnaceTemp(19999)
                .save();
    }
}
