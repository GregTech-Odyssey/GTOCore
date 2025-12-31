package com.gtocore.data.recipe.gtm.chemistry;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.crushed;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.dust;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.CENTRIFUGE_RECIPES;
import static com.gtocore.common.data.GTORecipeTypes.MIXER_RECIPES;

final class GemSlurryRecipes {

    public static void init() {
        // Ruby
        MIXER_RECIPES.recipeBuilder("ruby_slurry").duration(280).EUt(VA[EV])
                .inputItems(crushed, Ruby, 2)
                .inputFluids(AquaRegia, 3000)
                .outputFluids(RubySlurry.getFluid(3000))
                .save();

        CENTRIFUGE_RECIPES.recipeBuilder("ruby_slurry_centrifuging").duration(320).EUt(VA[HV])
                .inputFluids(RubySlurry, 3000)
                .outputItems(dust, Aluminium, 2)
                .outputItems(dust, Chromium)
                .chancedOutput(dust, Titanium, 200, 0)
                .chancedOutput(dust, Iron, 200, 0)
                .chancedOutput(dust, Vanadium, 200, 0)
                .outputFluids(DilutedHydrochloricAcid.getFluid(2000))
                .save();

        // Sapphire
        MIXER_RECIPES.recipeBuilder("sapphire_slurry").duration(280).EUt(VA[EV])
                .inputItems(crushed, Sapphire, 2)
                .inputFluids(AquaRegia, 3000)
                .outputFluids(SapphireSlurry.getFluid(3000))
                .save();

        CENTRIFUGE_RECIPES.recipeBuilder("sapphire_slurry_centrifuging").duration(320).EUt(VA[HV])
                .inputFluids(SapphireSlurry, 3000)
                .outputItems(dust, Aluminium, 2)
                .chancedOutput(dust, Titanium, 200, 0)
                .chancedOutput(dust, Iron, 200, 0)
                .chancedOutput(dust, Vanadium, 200, 0)
                .outputFluids(DilutedHydrochloricAcid.getFluid(2000))
                .save();

        // Green Sapphire
        MIXER_RECIPES.recipeBuilder("green_sapphire_slurry").duration(280).EUt(VA[EV])
                .inputItems(crushed, GreenSapphire, 2)
                .inputFluids(AquaRegia, 3000)
                .outputFluids(GreenSapphireSlurry.getFluid(3000))
                .save();

        CENTRIFUGE_RECIPES.recipeBuilder("green_sapphire_slurry_centrifuging").duration(320).EUt(VA[HV])
                .inputFluids(GreenSapphireSlurry, 3000)
                .outputItems(dust, Aluminium, 2)
                .chancedOutput(dust, Beryllium, 200, 0)
                .chancedOutput(dust, Titanium, 200, 0)
                .chancedOutput(dust, Iron, 200, 0)
                .chancedOutput(dust, Vanadium, 200, 0)
                .outputFluids(DilutedHydrochloricAcid.getFluid(2000))
                .save();
    }
}
