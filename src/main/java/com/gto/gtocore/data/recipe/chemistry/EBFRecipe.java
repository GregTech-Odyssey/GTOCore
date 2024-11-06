package com.gto.gtocore.data.recipe.chemistry;

import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;

public class EBFRecipe {

    public static void init(Consumer<FinishedRecipe> provider) {
        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder("chalcopyrite_metallurgy").duration(120).EUt(VA[MV]).blastFurnaceTemp(1200)
                .inputItems(dust, Chalcopyrite)
                .inputItems(dust, SiliconDioxide)
                .inputFluids(Oxygen.getFluid(3000))
                .outputItems(dust, CupricOxide)
                .outputItems(dust, Ferrosilite)
                .outputFluids(SulfurDioxide.getFluid(2000))
                .save(provider);
        registerBlastFurnaceRecipes(provider);
    }

    private static void registerBlastFurnaceRecipes(Consumer<FinishedRecipe> provider) {
        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder("aluminium_from_ruby_dust").duration(400).EUt(100).inputItems(dust, Ruby)
                .outputItems(nugget, Aluminium, 3).chancedOutput(dust, Ash, "1/9", 0).blastFurnaceTemp(1200)
                .save(provider);
        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder("aluminium_from_ruby_gem").duration(320).EUt(100).inputItems(gem, Ruby)
                .outputItems(nugget, Aluminium, 3).chancedOutput(dust, Ash, "1/9", 0).blastFurnaceTemp(1200)
                .save(provider);
        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder("aluminium_from_green_sapphire_dust").duration(400).EUt(100)
                .inputItems(dust, GreenSapphire).outputItems(nugget, Aluminium, 3).chancedOutput(dust, Ash, "1/9", 0)
                .blastFurnaceTemp(1200).save(provider);
        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder("aluminium_from_green_sapphire_gem").duration(320).EUt(100)
                .inputItems(gem, GreenSapphire).outputItems(nugget, Aluminium, 3).chancedOutput(dust, Ash, "1/9", 0)
                .blastFurnaceTemp(1200).save(provider);
        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder("aluminium_from_sapphire_dust").duration(400).EUt(100).inputItems(dust, Sapphire)
                .outputItems(nugget, Aluminium, 3).blastFurnaceTemp(1200).save(provider);
        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder("aluminium_from_sapphire_gem").duration(320).EUt(100).inputItems(gem, Sapphire)
                .outputItems(nugget, Aluminium, 3).blastFurnaceTemp(1200).save(provider);
        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder("titanium_from_tetrachloride").duration(600).EUt(VA[HV])
                .inputItems(dust, Magnesium, 2).inputFluids(TitaniumTetrachloride.getFluid(1000))
                .outputItems(ingotHot, Titanium).outputItems(dust, MagnesiumChloride, 6)
                .blastFurnaceTemp(Titanium.getBlastTemperature() + 200).save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder("rutile_from_ilmenite")
                .inputItems(dust, Ilmenite, 10)
                .inputItems(dust, Carbon, 4)
                .outputItems(ingot, WroughtIron, 2)
                .outputItems(dust, Rutile, 4)
                .outputFluids(CarbonDioxide.getFluid(2000))
                .blastFurnaceTemp(1700)
                .duration(1600).EUt(VA[HV]).save(provider);

        registerBlastFurnaceMetallurgyRecipes(provider);
    }

    private static void registerBlastFurnaceMetallurgyRecipes(Consumer<FinishedRecipe> provider) {
        createSulfurDioxideRecipe(provider, Stibnite, AntimonyTrioxide, 1500);
        createSulfurDioxideRecipe(provider, Sphalerite, Zincite, 1000);
        createSulfurDioxideRecipe(provider, Pyrite, Hematite, 2000);
        createSulfurDioxideRecipe(provider, Pentlandite, Garnierite, 1000);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder("tetrahedrite_metallurgy").duration(120).EUt(VA[MV]).blastFurnaceTemp(1200)
                .inputItems(dust, Tetrahedrite)
                .inputFluids(Oxygen.getFluid(3000))
                .outputItems(dust, CupricOxide)
                .outputItems(dustTiny, AntimonyTrioxide, 3)
                .outputFluids(SulfurDioxide.getFluid(2000))
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder("cobaltite_metallurgy").duration(120).EUt(VA[MV]).blastFurnaceTemp(1200)
                .inputItems(dust, Cobaltite)
                .inputFluids(Oxygen.getFluid(3000))
                .outputItems(dust, CobaltOxide)
                .outputItems(dust, ArsenicTrioxide)
                .outputFluids(SulfurDioxide.getFluid(1000))
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder("galena_metallurgy").duration(120).EUt(VA[MV]).blastFurnaceTemp(1200)
                .inputItems(dust, Galena)
                .inputFluids(Oxygen.getFluid(3000))
                .outputItems(dust, Massicot)
                .outputItems(nugget, Silver, 6)
                .outputFluids(SulfurDioxide.getFluid(1000))
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder("chalcopyrite_metallurgy").duration(120).EUt(VA[MV]).blastFurnaceTemp(1200)
                .inputItems(dust, Chalcopyrite)
                .inputItems(dust, SiliconDioxide)
                .inputFluids(Oxygen.getFluid(3000))
                .outputItems(dust, CupricOxide)
                .outputItems(dust, Ferrosilite)
                .outputFluids(SulfurDioxide.getFluid(2000))
                .save(provider);

        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder("blast_silicon_dioxide").duration(240).EUt(VA[MV]).blastFurnaceTemp(2273)
                .inputItems(dust, SiliconDioxide, 3)
                .inputItems(dust, Carbon, 2)
                .outputItems(ingotHot, Silicon)
                .chancedOutput(dust, Ash, "1/9", 0)
                .outputFluids(CarbonMonoxide.getFluid(2000))
                .save(provider);
    }

    private static void createSulfurDioxideRecipe(Consumer<FinishedRecipe> provider, Material inputMaterial,
                                                  Material outputMaterial, int sulfurDioxideAmount) {
        GTORecipeTypes.REACTION_FURNACE_RECIPES.recipeBuilder(inputMaterial.getName() + "_metallurgy").duration(120).EUt(VA[MV])
                .blastFurnaceTemp(1200)
                .inputItems(dust, inputMaterial)
                .inputFluids(Oxygen.getFluid(3000))
                .outputItems(dust, outputMaterial)
                .chancedOutput(dust, Ash, "1/9", 0)
                .outputFluids(SulfurDioxide.getFluid(sulfurDioxideAmount))
                .save(provider);
    }
}
