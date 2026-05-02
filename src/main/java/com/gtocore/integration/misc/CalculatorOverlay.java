package com.gtocore.integration.misc;

import com.ref.calculatoroverlay.JEIPlugin;
import mezz.jei.api.IModPlugin;

import java.util.function.Consumer;

public class CalculatorOverlay {

    public static void initJEI(Consumer<IModPlugin> jei) {
        jei.accept(new JEIPlugin());
    }
}
