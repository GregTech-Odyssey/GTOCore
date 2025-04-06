package com.gto.gtocore.utils;

public final class ColorUtils {

    private ColorUtils() {}

    public static int getInterpolatedColor(int hexColor1, int hexColor2, float ratio) {
        int red1 = (hexColor1 >> 16) & 0xFF;
        int green1 = (hexColor1 >> 8) & 0xFF;
        int blue1 = hexColor1 & 0xFF;

        int red2 = (hexColor2 >> 16) & 0xFF;
        int green2 = (hexColor2 >> 8) & 0xFF;
        int blue2 = hexColor2 & 0xFF;

        int red = (int) (red1 + ratio * (red2 - red1));
        int green = (int) (green1 + ratio * (green2 - green1));
        int blue = (int) (blue1 + ratio * (blue2 - blue1));

        return (red << 16) | (green << 8) | blue;
    }

    public static int createARGBColor(int rgbColor, int alpha) {
        int red = (rgbColor >> 16) & 0xFF;
        int green = (rgbColor >> 8) & 0xFF;
        int blue = rgbColor & 0xFF;
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }
}
