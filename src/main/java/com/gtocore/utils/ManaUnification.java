package com.gtocore.utils;

public class ManaUnification {

    public static long manaToSource(long mana) {
        return mana >> 2;
    }

    public static long sourceToMana(long source) {
        return source << 2;
    }
}
