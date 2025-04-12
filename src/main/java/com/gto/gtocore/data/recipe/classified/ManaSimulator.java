package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.utils.RegistriesUtils;

import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import static com.gto.gtocore.common.data.GTORecipeTypes.MANA_SIMULATOR_RECIPES;

interface ManaSimulator {
    int BUFF_FACTOR = 8;
    static void init() {
        MANA_SIMULATOR_RECIPES.builder("thermalily")
                .inputFluids(new FluidStack(Fluids.LAVA, 1000))
                .notConsumable(RegistriesUtils.getItem("botania", "thermalily"))
                .duration(900)
                .EUt(120)
                .MANAt(-20 * BUFF_FACTOR)
                .save();
    }
}
