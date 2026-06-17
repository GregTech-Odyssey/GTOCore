package com.gtocore.integration.lang;

import net.minecraft.network.chat.MutableComponent;

import com.google.common.collect.ImmutableList;
import com.ref.moremorelang.integration.jade.MoremorelangJadePlugin;
import com.ref.moremorelang.lang.ComponentTranslator;
import snownee.jade.api.IWailaPlugin;

public class LangAdaptor {

    public static String langCn(MutableComponent component) {
        return ComponentTranslator.translateComponent(component, "zh_cn").getString();
    }

    public static String langEn(MutableComponent component) {
        return ComponentTranslator.translateComponent(component, "en_us").getString();
    }

    public static void addPlugin(ImmutableList.Builder<IWailaPlugin> builder) {
        builder.add(new MoremorelangJadePlugin());
    }
}
