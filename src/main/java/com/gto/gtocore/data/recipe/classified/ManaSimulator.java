package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.utils.RegistriesUtils;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;

import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import com.enderio.base.common.init.EIOFluids;

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

        MANA_SIMULATOR_RECIPES.recipeBuilder(GTOCore.id("rosa_arcana"))
                .inputFluids(new FluidStack(EIOFluids.XP_JUICE.get().getSource(), 1000))
                .notConsumable(RegistriesUtils.getItem("botania", "rosa_arcana"))
                .EUt(VA[MV])
                .duration(1000)
                .MANAt(-50 * BUFF_FACTOR)
                .save();

        MANA_SIMULATOR_RECIPES.recipeBuilder(GTOCore.id("monchdew"))
                .inputItems(TagPrefix.getPrefix("#minecraft:leaves"), 4)
                .notConsumable(RegistriesUtils.getItem("botania", "monchdew"))
                .EUt(VA[MV])
                .duration(20)
                .MANAt(-32 * BUFF_FACTOR)
                .save();
    }
}
