package com.gtocore.integration.emi;

import dev.ftb.mods.ftbxmodcompat.ftbquests.jei.FTBQuestsJEIIntegration;
import mezz.jei.api.IModPlugin;

import java.util.Collection;
import java.util.function.Supplier;

public class NotDevCompat {

    private static final Supplier<IModPlugin> questPlugin = FTBQuestsJEIIntegration::new;

    public static void addPlugin(Collection<IModPlugin> list) {
        list.add(questPlugin.get());
    }
}
