package com.gtocore.integration.ae;

import com.gtocore.integration.ae.hooks.IMoreLangCache;

import appeng.api.stacks.AEKey;
import appeng.menu.me.common.GridInventoryEntry;
import com.ref.moremorelang.MoremorelangConfig;

import java.util.Objects;
import java.util.function.Predicate;

public class MultiLangNameSearchPredicate implements Predicate<GridInventoryEntry> {

    private final String term;

    public MultiLangNameSearchPredicate(String term) {
        this.term = term.toLowerCase();
    }

    @Override
    public boolean test(GridInventoryEntry gridInventoryEntry) {
        AEKey entryInfo = Objects.requireNonNull(gridInventoryEntry.getWhat());

        String displayName = entryInfo.getDisplayName().getString().toLowerCase();
        if (displayName.contains(this.term)) {
            return true;
        }

        if (entryInfo instanceof IMoreLangCache cache) {
            for (String langCode : MoremorelangConfig.moreLanguages) {
                String translated = cache.gtocore$getTranslatedLower(langCode);
                if (translated.contains(this.term)) {
                    return true;
                }
            }
        }

        return false;
    }
}
