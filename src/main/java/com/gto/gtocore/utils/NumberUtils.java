package com.gto.gtocore.utils;

import com.gregtechceu.gtceu.api.GTValues;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.text.DecimalFormat;

public final class NumberUtils {

    private NumberUtils() {}

    private static final DecimalFormat DF = new DecimalFormat("#.##");

    public static String formatDouble(double number) {
        if (number < 1_000) {
            return DF.format(number);
        } else if (number < 1_000_000) {
            return DF.format(number / 1_000) + "K";
        } else if (number < 1_000_000_000) {
            return DF.format(number / 1_000_000) + "M";
        } else if (number < 1_000_000_000_000.0) {
            return DF.format(number / 1_000_000_000) + "G";
        } else if (number < 1_000_000_000_000_000.0) {
            return DF.format(number / 1_000_000_000_000.0) + "T";
        } else if (number < 1_000_000_000_000_000_000.0) {
            return DF.format(number / 1_000_000_000_000_000.0) + "P";
        } else if (number < 1_000_000_000_000_000_000_000.0) {
            return DF.format(number / 1_000_000_000_000_000_000.0) + "E";
        } else if (number < 1_000_000_000_000_000_000_000_000.0) {
            return DF.format(number / 1_000_000_000_000_000_000_000.0) + "Z";
        } else {
            return DF.format(number / 1_000_000_000_000_000_000_000_000.0) + "Y";
        }
    }

    public static String formatLong(long number) {
        if (number < 1_000) {
            return DF.format(number);
        } else if (number < 1_000_000) {
            return DF.format((double) number / 1_000.0) + "K";
        } else if (number < 1_000_000_000) {
            return DF.format((double) number / 1_000_000.0) + "M";
        } else if (number < 1_000_000_000_000L) {
            return DF.format((double) number / 1_000_000_000.0) + "G";
        } else if (number < 1_000_000_000_000_000L) {
            return DF.format((double) number / 1_000_000_000_000.0) + "T";
        } else {
            return DF.format((double) number / 1_000_000_000_000_000.0) + "P";
        }
    }

    public static MutableComponent numberText(long number) {
        return Component.literal(formatLong(number));
    }

    public static int chanceOccurrences(int count, int chance, int max) {
        int occurrences = 0;
        for (int i = 0; i < count; i++) {
            if (occurrences == max) return max;
            if (GTValues.RNG.nextInt(chance) == 0) {
                occurrences++;
            }
        }
        return occurrences;
    }
}
