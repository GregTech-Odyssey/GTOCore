package com.gtocore.api.ae2.pattern;

public interface IEncodingLogic {

    String gtocore$getRecipe();

    String gtocore$getRecipeType();

    void gtocore$setRecipe(String recipe);

    void gtocore$setRecipeType(String recipeType);

    void gtocore$clearExtraRecipeInfo();
}
