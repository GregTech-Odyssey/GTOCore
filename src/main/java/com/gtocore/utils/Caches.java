package com.gtocore.utils;

import com.glodblock.github.extendedae.common.me.taglist.TagPriorityList;
import com.gto.fastcollection.cache.WeakValueHashCache;

import java.util.function.Function;

public final class Caches {

    private static final TagPriorityList EMPTY_TAG_FILTER = new TagPriorityList("", "");

    private static final Function<BiString, TagPriorityList> FUNCTION = s -> {
        if (s.isBlank()) return EMPTY_TAG_FILTER;
        return new TagPriorityList(s.a, s.b);
    };

    private static final WeakValueHashCache<BiString, TagPriorityList> TAG_FILTER_CACHE = new WeakValueHashCache<>();

    public static TagPriorityList getTagPriorityList(String white, String black) {
        return TAG_FILTER_CACHE.getCache(new BiString(white, black), FUNCTION);
    }

    private record BiString(String a, String b) {

        private boolean isBlank() {
            return a.isBlank() && b.isBlank();
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof BiString(String a1, String b1) && a1.equals(a) && b1.equals(b);
        }
    }
}
