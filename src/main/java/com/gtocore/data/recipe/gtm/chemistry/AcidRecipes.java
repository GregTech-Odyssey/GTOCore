package com.gtocore.data.recipe.gtm.chemistry;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.dust;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.CHEMICAL_RECIPES;
import static com.gtocore.common.data.GTORecipeTypes.LARGE_CHEMICAL_RECIPES;

final class AcidRecipes {

    public static void init() {
        sulfuricAcidRecipes();
        nitricAcidRecipes();
        phosphoricAcidRecipes();
    }

    private static void sulfuricAcidRecipes() {
        CHEMICAL_RECIPES.recipeBuilder("sulfur_dioxide_from_sulfur")
                .circuitMeta(2)
                .inputItems(dust, Sulfur)
                .inputFluids(Oxygen, 2000)
                .outputFluids(SulfurDioxide.getFluid(1000))
                .duration(60).EUt(VA[ULV]).save();

        CHEMICAL_RECIPES.recipeBuilder("sulfur_dioxide_from_sulfide")
                .circuitMeta(1)
                .inputFluids(Oxygen, 3000)
                .inputFluids(HydrogenSulfide, 1000)
                .outputFluids(Water.getFluid(1000))
                .outputFluids(SulfurDioxide.getFluid(1000))
                .duration(120).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.recipeBuilder("sulfur_trioxide")
                .inputFluids(SulfurDioxide, 1000)
                .inputFluids(Oxygen, 1000)
                .outputFluids(SulfurTrioxide.getFluid(1000))
                .duration(200).EUt(VA[ULV]).save();

        CHEMICAL_RECIPES.recipeBuilder("sulfuric_acid_from_trioxide")
                .inputFluids(SulfurTrioxide, 1000)
                .inputFluids(Water, 1000)
                .outputFluids(SulfuricAcid.getFluid(1000))
                .displayPriority(2)
                .duration(160).EUt(VA[ULV]).save();

        CHEMICAL_RECIPES.recipeBuilder("sulfuric_acid_from_sulfide")
                .circuitMeta(2)
                .inputFluids(HydrogenSulfide, 1000)
                .inputFluids(Oxygen, 4000)
                .outputFluids(SulfuricAcid.getFluid(1000))
                .displayPriority(1)
                .duration(320).EUt(VA[LV]).save();
    }

    private static void nitricAcidRecipes() {
        CHEMICAL_RECIPES.recipeBuilder("ammonia_from_elements")
                .circuitMeta(1)
                .inputFluids(Hydrogen, 3000)
                .inputFluids(Nitrogen, 1000)
                .outputFluids(Ammonia.getFluid(1000))
                .duration(320).EUt(384).save();

        CHEMICAL_RECIPES.recipeBuilder("nitric_oxide_from_ammonia")
                .circuitMeta(1)
                .inputFluids(Oxygen, 5000)
                .inputFluids(Ammonia, 2000)
                .outputFluids(NitricOxide.getFluid(2000))
                .outputFluids(Water.getFluid(3000))
                .duration(160).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.recipeBuilder("nitrogen_dioxide_from_oxide")
                .circuitMeta(1)
                .inputFluids(Oxygen, 1000)
                .inputFluids(NitricOxide, 1000)
                .outputFluids(NitrogenDioxide.getFluid(1000))
                .duration(160).EUt(VA[LV]).save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("nitrogen_dioxide_from_elements")
                .circuitMeta(3)
                .inputFluids(Nitrogen, 1000)
                .inputFluids(Oxygen, 2000)
                .outputFluids(NitrogenDioxide.getFluid(1000))
                .duration(160).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.recipeBuilder("nitric_acid_from_dioxide_1")
                .circuitMeta(1)
                .inputFluids(NitrogenDioxide, 3000)
                .inputFluids(Water, 1000)
                .outputFluids(NitricAcid.getFluid(2000))
                .outputFluids(NitricOxide.getFluid(1000))
                .displayPriority(1)
                .duration(240).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.recipeBuilder("nitric_acid_from_dioxide_2")
                .circuitMeta(3)
                .inputFluids(Water, 1000)
                .inputFluids(Oxygen, 1000)
                .inputFluids(NitrogenDioxide, 2000)
                .outputFluids(NitricAcid.getFluid(2000))
                .displayPriority(2)
                .duration(240).EUt(VA[LV]).save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("nitric_acid_from_ammonia")
                .circuitMeta(24)
                .inputFluids(Oxygen, 4000)
                .inputFluids(Ammonia, 1000)
                .outputFluids(NitricAcid.getFluid(1000))
                .outputFluids(Water.getFluid(1000))
                .duration(320).EUt(VA[LV]).save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("nitric_acid_from_elements")
                .circuitMeta(24)
                .inputFluids(Nitrogen, 1000)
                .inputFluids(Hydrogen, 3000)
                .inputFluids(Oxygen, 4000)
                .outputFluids(NitricAcid.getFluid(1000))
                .outputFluids(Water.getFluid(1000))
                .duration(320).EUt(VA[HV]).save();
    }

    private static void phosphoricAcidRecipes() {
        CHEMICAL_RECIPES.recipeBuilder("phosphorus_pentoxide_from_elements")
                .circuitMeta(1)
                .inputItems(dust, Phosphorus, 4)
                .inputFluids(Oxygen, 10000)
                .outputItems(dust, PhosphorusPentoxide, 14)
                .duration(40).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.recipeBuilder("phosphoric_acid_from_pentoxide")
                .inputItems(dust, PhosphorusPentoxide, 14)
                .inputFluids(Water, 6000)
                .outputFluids(PhosphoricAcid.getFluid(4000))
                .duration(40).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.recipeBuilder("phosphoric_acid_from_apatite")
                .inputItems(dust, Apatite, 9)
                .inputFluids(SulfuricAcid, 5000)
                .inputFluids(Water, 10000)
                .outputItems(dust, Gypsum, 40)
                .outputFluids(HydrochloricAcid.getFluid(1000))
                .outputFluids(PhosphoricAcid.getFluid(3000))
                .duration(320).EUt(VA[LV]).save();
    }
}
