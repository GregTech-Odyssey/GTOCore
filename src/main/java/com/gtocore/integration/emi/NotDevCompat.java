package com.gtocore.integration.emi;

import dev.ftb.mods.ftbxmodcompat.ftbquests.jei.FTBQuestsJEIIntegration;
import mezz.jei.api.IModPlugin;

import java.util.List;
import java.util.function.Supplier;

public class NotDevCompat {

    private static final Supplier<IModPlugin> questPlugin = FTBQuestsJEIIntegration::new;

    public static void addPlugin(List<IModPlugin> list) {
        list.add(questPlugin.get());
    }
}
