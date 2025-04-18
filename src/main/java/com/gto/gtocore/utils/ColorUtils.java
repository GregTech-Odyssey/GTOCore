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

    /**
     * 根据最深和最浅的RGB整数值，生成指定数量的渐变色
     *
     * @param deepestColor  最深颜色的RGB整数值
     * @param lightestColor 最浅颜色的RGB整数值
     * @param steps         需要生成的颜色数量 (包括最深和最浅颜色)
     * @return 由深至浅排列的ARGB整数数组
     * @throws IllegalArgumentException 如果参数无效
     */
    public static int[] generateStepGradient(int deepestColor, int lightestColor, int alpha, int steps) {
        if (steps < 2) {
            throw new IllegalArgumentException("阶梯数必须至少为2");
        }

        int[] gradient = new int[steps];
        gradient[0] = deepestColor | (alpha << 24);
        gradient[steps - 1] = lightestColor | (alpha << 24);

        // 提取RGB分量
        int startRed = (deepestColor >> 16) & 0xFF;
        int startGreen = (deepestColor >> 8) & 0xFF;
        int startBlue = deepestColor & 0xFF;

        int endRed = (lightestColor >> 16) & 0xFF;
        int endGreen = (lightestColor >> 8) & 0xFF;
        int endBlue = lightestColor & 0xFF;

        for (int i = 1; i < steps - 1; i++) {
            float ratio = (float) i / (steps - 1);

            int red = calculateGradientComponent(startRed, endRed, ratio);
            int green = calculateGradientComponent(startGreen, endGreen, ratio);
            int blue = calculateGradientComponent(startBlue, endBlue, ratio);
            gradient[i] = (alpha << 24) | (red << 16) | (green << 8) | blue;
        }

        return gradient;
    }

    private static int calculateGradientComponent(int start, int end, float ratio) {
        return Math.round(start + (end - start) * ratio);
    }
}
