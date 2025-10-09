package com.gtocore.common.data.material;

import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtocore.data.recipe.processing.CompositeMaterialsProcessing.registerFiberExtrusionTemperature;

public class FiberExtruderTempDefinitions {

    public static void init() {
        registerFiberExtrusionTemperature(Quartzite, 3000);
        registerFiberExtrusionTemperature(Alumina, 3000);
        registerFiberExtrusionTemperature(Titanium, 3800);
        registerFiberExtrusionTemperature(SiliconCarbide, 5000);
        registerFiberExtrusionTemperature(Tungsten, 5000);
        registerFiberExtrusionTemperature(StainlessSteel155Ph, 3000);
        registerFiberExtrusionTemperature(Aluminium, 1600);
        registerFiberExtrusionTemperature(Tantalum, 3300);
        registerFiberExtrusionTemperature(Copper, 1600);
        registerFiberExtrusionTemperature(Kevlar, 800);
        registerFiberExtrusionTemperature(TungstenCarbide, 4500);
        registerFiberExtrusionTemperature(TitaniumCarbide, 4000);
    }
}
