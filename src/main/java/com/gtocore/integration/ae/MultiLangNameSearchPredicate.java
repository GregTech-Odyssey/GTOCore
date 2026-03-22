package com.gtocore.integration.ae;

import com.gtocore.integration.ae.hooks.IMoreLangCache;

import appeng.api.stacks.AEKey;
import appeng.menu.me.common.GridInventoryEntry;

import com.ref.moremorelang.MoremorelangConfig;

import java.lang.reflect.Constructor;
import java.util.Objects;
import java.util.function.Predicate;

public class MultiLangNameSearchPredicate implements Predicate<GridInventoryEntry> {

    private final String term;
    private Predicate<GridInventoryEntry> originalPredicate;

    @SuppressWarnings("unchecked")
    public MultiLangNameSearchPredicate(String term) {
        this.term = term.toLowerCase();
        try {
            Class<?> clazz = Class.forName("appeng.client.gui.me.search.NameSearchPredicate");
            Constructor<?> constructor = clazz.getDeclaredConstructor(String.class);
            constructor.setAccessible(true);
            this.originalPredicate = (Predicate<GridInventoryEntry>) constructor.newInstance(term);
        } catch (Exception e) {
            this.originalPredicate = null;
        }
    }

    @Override
    public boolean test(GridInventoryEntry gridInventoryEntry) {
        // 1. 复用原始逻辑
        if (originalPredicate != null) {
            if (originalPredicate.test(gridInventoryEntry)) {
                return true;
            }
        } else {
            // 手动 fallback 逻辑 (如果你担心反射失败)
            AEKey entryInfo = gridInventoryEntry.getWhat();
            if (entryInfo != null) {
                String displayName = entryInfo.getDisplayName().getString().toLowerCase();
                if (displayName.contains(this.term)) return true;
            }
        }

        // 2. 你的多语言逻辑
        AEKey entryInfo = Objects.requireNonNull(gridInventoryEntry.getWhat());
        if (entryInfo instanceof IMoreLangCache cache) {
            for (String langCode : MoremorelangConfig.moreLanguages) {
                String translated = cache.gtocore$getTranslatedLower(langCode);
                if (translated != null && translated.contains(this.term)) {
                    return true;
                }
            }
        }

        return false;
    }
}
