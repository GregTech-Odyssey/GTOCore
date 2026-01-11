package com.gtocore.integration.jech;

import com.gregtechceu.gtceu.GTCEu;

import me.towdium.jecharacters.utils.Match;

public class PinYinUtils {

    public static boolean match(String candidate, CharSequence search) {
        if (GTCEu.isModLoaded("jecharacters")) {
            return JechAdapter.gto$jech$match(candidate, search);
        } else {
            return candidate.contains(search);
        }
    }

    private static class JechAdapter {

        private static boolean gto$jech$match(String candidate, CharSequence search) {
            return Match.contains(candidate, search);
        }
    }
}
