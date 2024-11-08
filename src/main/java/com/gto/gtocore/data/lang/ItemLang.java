package com.gto.gtocore.data.lang;

import com.gto.gtocore.client.Tooltips;

import net.minecraftforge.common.data.LanguageProvider;

public class ItemLang {

    public static void init(LanguageProvider provider) {
        Tooltips.TOOL_TIPS_MAP.forEach((i, s) -> {
            for (int j = 0; j < s.length; j++) {
                provider.add("gtocore.tooltip.item." + i.kjs$getIdLocation().getPath() + "." + j, s[j]);
            }
        });

        provider.add("gtocore.tooltip.unknown", "Unknown");

        provider.add("item.gtceu.tool.vajra", "%s Vajra");
        provider.add("gtocore.tooltip.item.kinetic_rotor.max", "Max Wind Speed: %s");
        provider.add("gtocore.tooltip.item.kinetic_rotor.min", "Min Wind Speed: %s");

        provider.add("gtocore.tooltip.item.really_max_battery", "Filling it up can allow you to complete GregTechCEu Modern");
        provider.add("gtocore.tooltip.item.transcendent_max_battery", "Filling it up can allow you to complete GregTech Leisure");
        provider.add("gtocore.tooltip.item.extremely_max_battery", "Fill it up within your lifetime");
        provider.add("gtocore.tooltip.item.insanely_max_battery", "Filling it up is just for fun");
        provider.add("gtocore.tooltip.item.mega_max_battery", "Fill up the battery for mechanical ascension");
        provider.add("gtocore.tooltip.item.tier_circuit", "%s Tier Circuit");
        provider.add("gtocore.tooltip.item.suprachronal_circuit", "Running outside of known spacetime");
        provider.add("gtocore.tooltip.item.magneto_resonatic_circuit", "Utilize the powerful magnetic field generated by magnetic resonance instruments to operate");
    }
}
