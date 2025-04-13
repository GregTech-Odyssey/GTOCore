package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.common.data.GTORecipeTypes;
import com.gto.gtocore.utils.RegistriesUtils;

import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import static com.gregtechceu.gtceu.api.GTValues.MV;
import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gto.gtocore.common.data.GTORecipeTypes.*;

public interface ManaSimulator {

    int BUFF_FACTOR = 8;

    static void init() {
        MANA_SIMULATOR_RECIPES.builder("thermalily")
                .inputFluids(new FluidStack(Fluids.LAVA, 1000))
                .notConsumable(RegistriesUtils.getItem("botania", "thermalily"))
                .duration(900)
                .EUt(VA[MV])
                .MANAt(-20 * BUFF_FACTOR)
                .save();
    }
}
