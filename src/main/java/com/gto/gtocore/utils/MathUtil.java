package com.gto.gtocore.utils;

public final class MathUtil {

    private MathUtil() {}

    private static final int INITIAL_TABLE_SIZE = 4096;
    private static float[] trigTable;
    private static float radToIndex;

    private static void initialize() {
        trigTable = new float[MathUtil.INITIAL_TABLE_SIZE];
        radToIndex = MathUtil.INITIAL_TABLE_SIZE / (2.0f * (float) Math.PI);
        for (int j = 0; j < MathUtil.INITIAL_TABLE_SIZE; ++j) {
            trigTable[j] = toFloat(Math.sin(j * 2.0 * Math.PI / INITIAL_TABLE_SIZE));
        }
    }

    public static float sin(float radians) {
        if (trigTable == null) {
            initialize();
        }
        return trigTable[(int) (radians * radToIndex) & (trigTable.length - 1)];
    }

    public static float cos(float radians) {
        if (trigTable == null) {
            initialize();
        }
        int cosIndex = (int) ((radians * radToIndex + trigTable.length / 4.0) % trigTable.length);
        if (cosIndex < 0) {
            cosIndex += trigTable.length;
        }
        return trigTable[cosIndex];
    }

    private static float toFloat(double d) {
        return (float) (Math.round(d * 1.0E8D) / 1.0E8D);
    }
}
