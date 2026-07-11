package com.gtocore.client;

import com.gtocore.config.GTOConfig;

import com.gregtechceu.gtceu.api.data.worldgen.GTOreDefinition;
import com.gregtechceu.gtceu.integration.xei.widgets.GTOreVeinWidget;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Client-side state for the ore vein map filter. Holds the set of ore vein internal names that are
 * hidden on the Xaero world map. The set is loaded from {@link GTOConfig} on first access and kept
 * authoritative in memory afterwards; {@link #save()} writes it back to the config file.
 */
@OnlyIn(Dist.CLIENT)
public final class OreVeinFilter {

    private static Set<String> hidden;
    // Whether the filter panel was left open; remembered across map opens and restarts. Null until first read.
    private static Boolean panelOpen;

    private OreVeinFilter() {}

    private static Set<String> hidden() {
        if (hidden == null) {
            hidden = new LinkedHashSet<>();
            String[] stored = GTOConfig.INSTANCE.client.minimap.hiddenOreVeins;
            if (stored != null) {
                for (String s : stored) {
                    if (s != null && !s.isEmpty()) hidden.add(s);
                }
            }
        }
        return hidden;
    }

    public static boolean isPanelOpen() {
        if (panelOpen == null) panelOpen = GTOConfig.INSTANCE.client.minimap.oreVeinFilterPanelOpen;
        return panelOpen;
    }

    public static void setPanelOpen(boolean value) {
        panelOpen = value;
    }

    public static boolean isHidden(String oreName) {
        return hidden().contains(oreName);
    }

    public static boolean isHidden(GTOreDefinition definition) {
        return isHidden(GTOreVeinWidget.getOreName(definition));
    }

    public static void setHidden(String oreName, boolean value) {
        if (value) {
            hidden().add(oreName);
        } else {
            hidden().remove(oreName);
        }
    }

    public static void toggle(String oreName) {
        setHidden(oreName, !isHidden(oreName));
    }

    /** Persist the current hidden set and panel-open flag back to the config file. */
    public static void save() {
        GTOConfig.set("hiddenOreVeins", hidden().toArray(new String[0]), "client", "minimap");
        GTOConfig.set("oreVeinFilterPanelOpen", isPanelOpen(), "client", "minimap");
    }
}
