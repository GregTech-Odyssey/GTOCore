package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOFluids;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Consumer;

interface LiquefactionFurnace {

    static void init(Consumer<FinishedRecipe> provider) {
        GTORecipeTypes.LIQUEFACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("gelid_cryotheum"))
                .inputItems(GTOItems.DUST_CRYOTHEUM.asStack())
                .outputFluids(new FluidStack(GTOFluids.GELID_CRYOTHEUM.get(), 144))
                .EUt(491520)
                .duration(80)
                .blastFurnaceTemp(300)
                .save(provider);

        GTORecipeTypes.LIQUEFACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("antimatter"))
                .inputItems(GTOItems.PELLET_ANTIMATTER.asStack())
                .outputFluids(GTOMaterials.Antimatter.getFluid(1000))
                .EUt(480)
                .duration(2000)
                .blastFurnaceTemp(19999)
                .save(provider);
    }
}
