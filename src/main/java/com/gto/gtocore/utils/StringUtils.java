package com.gto.gtocore.utils;

import net.minecraft.ChatFormatting;

import java.util.regex.Pattern;

import static net.minecraft.ChatFormatting.*;

public final class StringUtils {

    private StringUtils() {}

    private static final String[] CHINESE_NUMBERS = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };

    public static String numberToChinese(int number) {
        if (number < 0 || number > 9) {
            throw new IllegalArgumentException("Number must be between 0 and 9");
        }
        return CHINESE_NUMBERS[number];
    }

    public static String[] decompose(String location) {
        return decompose(':', location);
    }

    public static String[] decompose(char spot, String string) {
        String[] astring = { string, "" };
        int i = string.indexOf(spot);
        if (i >= 0) {
            astring[1] = string.substring(i + 1);
            if (i >= 1) {
                astring[0] = string.substring(0, i);
            }
        }
        return astring;
    }

    public static String[] lastDecompose(char spot, String string) {
        String[] result = { string, "" };
        int lastIndex = string.lastIndexOf(spot);
        if (lastIndex >= 0) {
            result[1] = string.substring(lastIndex + 1);
            result[0] = string.substring(0, lastIndex);
        }
        return result;
    }

    /**
     * 查询某个内容是否出现在数组中
     *
     * @param array  要查询的数组，支持通配符
     * @param target 要查询的内容
     * @return 是否存在
     */
    public static boolean containsWithWildcard(String[] array, String target) {
        for (String element : array) {
            if (Pattern.matches(element, target)) {
                return true;
            }
        }
        return false;
    }

    public static String full_color(String input) {
        return formatting(input, new ChatFormatting[] { RED, GOLD, YELLOW, GREEN, AQUA, BLUE, LIGHT_PURPLE }, 80.0D);
    }

    public static String dark_purplish_red(String input) {
        return formatting(input, new ChatFormatting[] { DARK_PURPLE, DARK_RED }, 160.0D);
    }

    public static String white_blue(String input) {
        return formatting(input, new ChatFormatting[] { BLUE, BLUE, BLUE, BLUE, WHITE, BLUE, WHITE, WHITE, BLUE,
                WHITE, WHITE, BLUE, RED, WHITE }, 80.0D);
    }

    public static String purplish_red(String input) {
        return formatting(input, new ChatFormatting[] { LIGHT_PURPLE, DARK_PURPLE }, 160.0D);
    }

    public static String golden(String input) {
        return formatting(input, new ChatFormatting[] { YELLOW, GOLD }, 160.0D);
    }

    public static String dark_green(String input) {
        return formatting(input, new ChatFormatting[] { GREEN, DARK_GREEN }, 160.0D);
    }

    private static String formatting(String input, ChatFormatting[] colours, double delay) {
        StringBuilder sb = new StringBuilder(input.length() * 3);
        if (delay <= 0.0D)
            delay = 0.001D;
        int offset = (int) Math.floor((System.currentTimeMillis() & 0x3FFFL) / delay) % colours.length;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            sb.append(colours[(colours.length + i - offset) % colours.length].toString());
            sb.append(c);
        }
        return sb.toString();
    }
}
