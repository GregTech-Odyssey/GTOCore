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

    /// 第零阶段 -
    /// 小扫描仪 - 小差分机 - 小组装机
    ///
    ///
    ///

    /// 第一阶段 -
    /// 基元扫描站 - 分析推演中心 - 合成数据组装厂
    ///
}
