package com.gto.gtocore.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import com.google.common.math.LongMath;

import java.text.DecimalFormat;
import java.util.Random;

public class NumberUtils {

    public static final String[] UNITS = { "", "K", "M", "G", "T", "P", "E", "Y", "Z", "R", "Q", "S", "O", "N" };

    public static String formatLong(long number) {
        DecimalFormat df = new DecimalFormat("#.##");
        double temp = number;
        int unitIndex = 0;
        while (temp >= 1000) {
            temp /= 1000;
            unitIndex++;
        }
        return df.format(temp) + UNITS[unitIndex];
    }

    public static MutableComponent numberText(long number) {
        return Component.literal(formatLong(number));
    }

    public static int getFakeVoltageTier(long voltage) {
        long a = voltage;
        int b = 0;
        while (a / 4L >= 8L) {
            b++;
            a /= 4L;
        }
        return b;
    }

    public static long getVoltageFromFakeTier(int tier) {
        return LongMath.pow(4L, tier + 1) * 2;
    }

    public static int chanceOccurrences(int count, int chance) {
        Random random = new Random();
        int occurrences = 0;
        for (int i = 0; i < count; i++) {
            if (random.nextInt(chance) == 0) {
                occurrences++;
            }
        }
        return occurrences;
    }

    public static double calculateDistance(BlockPos pos1, BlockPos pos2) {
        int deltaX = pos2.getX() - pos1.getX();
        int deltaY = pos2.getY() - pos1.getY();
        int deltaZ = pos2.getZ() - pos1.getZ();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
    }
}
