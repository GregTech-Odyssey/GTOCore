package com.gto.gtocore.data.recipe.classified;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.data.tag.TagUtil;
import com.gto.gtocore.GTOCore;
import com.gto.gtocore.data.tag.Tags;
import com.gto.gtocore.utils.RegistriesUtils;
import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import com.enderio.base.common.init.EIOFluids;

import static com.gregtechceu.gtceu.api.GTValues.MV;
import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gto.gtocore.common.data.GTORecipeTypes.*;
import static net.minecraft.tags.ItemTags.LEAVES;

public interface ManaSimulator {

    int BUFF_FACTOR = 8;

    static void init() {
        MANA_SIMULATOR_RECIPES.builder("thermalily")
                .notConsumable(RegistriesUtils.getItem("botania", "thermalily"))
                .inputFluids(new FluidStack(Fluids.LAVA, 1000))
                .duration(900)
                .EUt(VA[MV])
                .MANAt(-20 * BUFF_FACTOR)
                .save();

        MANA_SIMULATOR_RECIPES.recipeBuilder(GTOCore.id("rosa_arcana"))
                .notConsumable(RegistriesUtils.getItem("botania", "rosa_arcana"))
                .inputFluids(new FluidStack(EIOFluids.XP_JUICE.get().getSource(), 1000))
                .EUt(VA[MV])
                .duration(1000)
                .MANAt(-50 * BUFF_FACTOR)
                .save();

        MANA_SIMULATOR_RECIPES.recipeBuilder(GTOCore.id("munchdew"))
                .notConsumable(RegistriesUtils.getItem("botania", "munchdew"))
                .inputItems(LEAVES, 4)
                .EUt(VA[MV])
                .duration(20)
                .MANAt(-32 * BUFF_FACTOR)
                .save();
    }
}
