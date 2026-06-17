package com.gtocore.integration.construction_wand;

import thetadev.constructionwand.ConstructionWand;

public class ConstructionWandRegistrar {

    public static void register() {
        ConstructionWand.instance.containerManager.register(new AEHandler());
    }
}
