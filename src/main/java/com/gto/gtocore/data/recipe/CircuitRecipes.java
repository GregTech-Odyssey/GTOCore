package com.gto.gtocore.data.recipe;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;
import static com.gto.gtocore.common.data.GTOMaterials.AbsoluteEthanol;
import static com.gto.gtocore.common.data.GTOMaterials.PiranhaSolution;

public class CircuitRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        AUTOCLAVE_RECIPES.recipeBuilder(GTOCore.id("sterilized_petri_dish"))
                .inputItems(GTItems.PETRI_DISH)
                .inputFluids(AbsoluteEthanol.getFluid(100))
                .outputItems(GTOItems.STERILIZED_PETRI_DISH)
                .cleanroom(CleanroomType.STERILE_CLEANROOM)
                .duration(25).EUt(7680).save(provider);

        ASSEMBLER_RECIPES.recipeBuilder(GTOCore.id("electricaly_wired_petri_dish"))
                .inputItems(GTOItems.STERILIZED_PETRI_DISH)
                .inputItems(TagPrefix.wireFine, GTMaterials.Titanium)
                .inputFluids(GTMaterials.Polyethylene.getFluid(1296))
                .outputItems(GTOItems.ELECTRICALY_WIRED_PETRI_DISH)
                .cleanroom(CleanroomType.STERILE_CLEANROOM)
                .duration(100).EUt(7680).save(provider);

        CHEMICAL_BATH_RECIPES.recipeBuilder(GTOCore.id("petri_dish"))
                .inputItems(GTOItems.CONTAMINATED_PETRI_DISH)
                .outputItems(GTItems.PETRI_DISH)
                .inputFluids(PiranhaSolution.getFluid(100))
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(25).EUt(30).save(provider);
    }
}
