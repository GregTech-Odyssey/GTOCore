package com.gto.gtocore.data.recipe.chemistry;

import com.gto.gtocore.GTOCore;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.common.data.GTMaterials.Ethanol;
import static com.gregtechceu.gtceu.common.data.GTMaterials.SulfuricAcid;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.MIXER_RECIPES;
import static com.gto.gtocore.common.data.GTOMaterials.*;

public class MixerRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        MIXER_RECIPES.recipeBuilder(GTOCore.id("absolute_ethanol"))
                .inputFluids(Ethanol.getFluid(1000))
                .inputItems(TagPrefix.dust, ZeoliteSievingPellets)
                .outputFluids(AbsoluteEthanol.getFluid(1000))
                .outputItems(TagPrefix.dust, WetZeoliteSievingPellets)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(100).EUt(120).save(provider);

        MIXER_RECIPES.recipeBuilder(GTOCore.id("piranha_solution"))
                .inputFluids(HydrogenPeroxide.getFluid(1000))
                .inputFluids(SulfuricAcid.getFluid(1000))
                .outputFluids(PiranhaSolution.getFluid(2000))
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(50).EUt(30).save(provider);
    }
}
