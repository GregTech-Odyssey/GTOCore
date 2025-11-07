package com.gtocore.data.recipe.research;

import com.gregtechceu.gtceu.GTCEu;

public final class ResearchRecipes {

    public static void init() {
        if (GTCEu.isDev()) {
            ScanningRecipes.init();
            AnalyzeData.INSTANCE.init();
            if (true) {
                AnalyzeRecipes.init();
                DataGenerateRecipe.init();
            }
        }
    }
}
