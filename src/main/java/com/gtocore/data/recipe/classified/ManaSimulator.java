package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOFluids;

import com.gtolib.utils.RegistriesUtils;

import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import static com.gregtechceu.gtceu.api.GTValues.MV;
import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gtocore.common.data.GTORecipeTypes.MANA_GARDEN_RECIPES;
import static net.minecraft.tags.ItemTags.LEAVES;

public final class ManaSimulator {

    public static final int BUFF_FACTOR = 8;

    public static void init() {
        MANA_GARDEN_RECIPES.builder("thermalily")
                .notConsumable(RegistriesUtils.getItem("botania", "thermalily"))
                .inputFluids(new FluidStack(Fluids.LAVA, 1000))
                .duration(900)
                .EUt(VA[MV])
                .MANAt(-20 * BUFF_FACTOR)
                .save();

        MANA_GARDEN_RECIPES.recipeBuilder("rosa_arcana")
                .notConsumable(RegistriesUtils.getItem("botania", "rosa_arcana"))
                .inputFluids(new FluidStack(GTOFluids.XP_JUICE.get().getSource(), 1000))
                .EUt(VA[MV])
                .duration(1000)
                .MANAt(-50 * BUFF_FACTOR)
                .save();

        MANA_GARDEN_RECIPES.recipeBuilder("munchdew")
                .notConsumable(RegistriesUtils.getItem("botania", "munchdew"))
                .inputItems(LEAVES, 4)
                .EUt(VA[MV])
                .duration(20)
                .MANAt(-32 * BUFF_FACTOR)
                .save();
    }
}
