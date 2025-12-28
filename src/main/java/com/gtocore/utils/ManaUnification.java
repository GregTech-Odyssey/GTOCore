package com.gtocore.utils;

public class ManaUnification {

    public static final double MANA2SOURCE_MULTIPLIER = 0.25;

    public static long manaToSource(long mana) {
        return (long) (mana * MANA2SOURCE_MULTIPLIER);
    }

    public static long sourceToMana(long source) {
        return (long) (source / MANA2SOURCE_MULTIPLIER);
    }
}
