package com.gtocore.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Soft dependency on JustEnoughCharacters (JEC). When JEC is present its pinyin matcher is used so a
 * Chinese ore-vein name matches a pinyin query; otherwise this degrades to a plain case-insensitive
 * substring test. JEC is only a compile-time dependency of this mod, so it may be absent at runtime —
 * all access goes through {@link #AVAILABLE} and a lazily-loaded bridge class to avoid a hard link.
 */
@OnlyIn(Dist.CLIENT)
public final class PinyinMatch {

    private static final boolean AVAILABLE;

    static {
        boolean ok;
        try {
            Class.forName("me.towdium.jecharacters.utils.Match");
            ok = true;
        } catch (Throwable t) {
            ok = false;
        }
        AVAILABLE = ok;
    }

    private PinyinMatch() {}

    /** True if {@code query} matches {@code text}: pinyin-aware when JEC is loaded, plain substring otherwise. */
    public static boolean matches(String text, String query) {
        if (query == null || query.isEmpty()) return true;
        if (AVAILABLE) {
            try {
                return Bridge.contains(text, query);
            } catch (Throwable ignored) {
                // JEC threw or could not link; fall back to plain matching below
            }
        }
        return text.toLowerCase().contains(query.toLowerCase());
    }

    /** Loaded only when JEC is actually present, so the reference to JEC classes never links otherwise. */
    private static final class Bridge {
        static boolean contains(String text, String query) {
            return me.towdium.jecharacters.utils.Match.contains(text, query)
                    || text.toLowerCase().contains(query.toLowerCase());
        }
    }
}
