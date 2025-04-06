package com.gto.gtocore.utils;

import it.unimi.dsi.fastutil.chars.Char2CharOpenHashMap;

import java.util.Map;
import java.util.ResourceBundle;

public final class ChineseConverter {

    private ChineseConverter() {}

    private static final Map<Character, Character> mappingTable = new Char2CharOpenHashMap();

    static {
        ResourceBundle bundle = ResourceBundle.getBundle("SimplifiedToTraditional");
        for (String key : bundle.keySet()) {
            mappingTable.put(key.charAt(0), bundle.getString(key).charAt(0));
        }
    }

    public static String convert(String text) {
        StringBuilder outputTextBuilder = new StringBuilder();
        for (char character : text.toCharArray()) {
            Character convertedChar = mappingTable.get(character);
            if (convertedChar == null) {
                outputTextBuilder.append(character);
            } else {
                outputTextBuilder.append(convertedChar);
            }
        }
        return outputTextBuilder.toString();
    }
}
