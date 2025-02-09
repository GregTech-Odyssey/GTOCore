package com.gto.gtocore.api;

import static net.minecraft.ChatFormatting.*;

public final class GTOValues {

    // Dyson Sphere Data
    public static final String GALAXY = "g";
    public static final String DYSON_LIST = "l";
    public static final String DYSON_COUNT = "c";
    public static final String DYSON_DAMAGE = "d";
    public static final String DYSON_USE = "u";

    // Infinity Cell Data
    public static final String INFINITY_CELL_LIST = "l";
    public static final String INFINITY_CELL_UUID = "u";
    public static final String INFINITY_CELL_DATA = "d";
    public static final String INFINITY_CELL_KEYS = "k";
    public static final String INFINITY_CELL_AMOUNTS = "a";

    // Wireless Energy Data
    public static final String WIRELESS_ENERGY_UUID = "u";
    public static final String WIRELESS_ENERGY_STORAGE = "s";
    public static final String WIRELESS_ENERGY_CAPACITY = "c";
    public static final String WIRELESS_ENERGY_RATE = "r";
    public static final String WIRELESS_ENERGY_LOSS = "l";
    public static final String WIRELESS_ENERGY_DIMENSION = "d";
    public static final String WIRELESS_ENERGY_POS = "p";

    // Tier Type
    public static final String STELLAR_CONTAINMENT_TIER = "sc";
    public static final String POWER_MODULE_TIER = "pm";
    public static final String COMPONENT_ASSEMBLY_CASING_TIER = "ca";
    public static final String GLASS_TIER = "gb";
    public static final String MACHINE_CASING_TIER = "mc";
    public static final String GRAVITON_FLOW_TIER = "gf";

    public static final String[] MANA = new String[] {
            "Primitive",
            "Basic",
            "Intermediate",
            "Advanced",
            "Expert",
            "Elite",
            "Master",
            "Champion",
            "Ultimate",
            "Supreme",
            "Epic",
            "Legendary",
            "Arcane",
            "Divine" };

    public static final String[] MANACN = new String[] {
            "原始",
            "基础",
            "中级",
            "高级",
            "专家",
            "精英",
            "大师",
            "冠军",
            "终极",
            "至高",
            "史诗",
            "传奇",
            "奥术",
            "神圣" };

    public static final String[] VLVHCN = new String[] {
            "原始",
            "基础",
            AQUA + "高级",
            GOLD + "高级",
            DARK_PURPLE + "高级",
            BLUE + "精英",
            LIGHT_PURPLE + "精英",
            RED + "精英",
            DARK_AQUA + "终极",
            DARK_RED + "史诗",
            GREEN + "史诗",
            DARK_GREEN + "史诗",
            YELLOW + "史诗",
            BLUE.toString() + BOLD + "传奇",
            RED.toString() + BOLD + "MAX" };

    public static final String[] VOLTAGE_NAMESCN = new String[] { "超低压", "低压", "中压", "高压", "超高压", "强导压", "剧差压", "零点压", "极限压", "极高压", "极超压", "极巨压", "超极限压", "过载压", "终压" };

    public static final String[] VNFR = new String[] {
            DARK_GRAY + "ULV" + RESET,
            GRAY + "LV" + RESET,
            AQUA + "MV" + RESET,
            GOLD + "HV" + RESET,
            DARK_PURPLE + "EV" + RESET,
            BLUE + "IV" + RESET,
            LIGHT_PURPLE + "LuV" + RESET,
            RED + "ZPM" + RESET,
            DARK_AQUA + "UV" + RESET,
            DARK_RED + "UHV" + RESET,
            GREEN + "UEV" + RESET,
            DARK_GREEN + "UIV" + RESET,
            YELLOW + "UXV" + RESET,
            BLUE.toString() + BOLD + "OpV" + RESET,
            RED.toString() + BOLD + "MAX" + RESET,
            RED.toString() + BOLD + "M" + GREEN + BOLD + "A" + BLUE + BOLD + "X" + YELLOW + BOLD + "+" + RED + BOLD + "1" + RESET,
            RED.toString() + BOLD + "M" + GREEN + BOLD + "A" + BLUE + BOLD + "X" + YELLOW + BOLD + "+" + RED + BOLD + "2" + RESET,
            RED.toString() + BOLD + "M" + GREEN + BOLD + "A" + BLUE + BOLD + "X" + YELLOW + BOLD + "+" + RED + BOLD + "3" + RESET,
            RED.toString() + BOLD + "M" + GREEN + BOLD + "A" + BLUE + BOLD + "X" + YELLOW + BOLD + "+" + RED + BOLD + "4" + RESET,
            RED.toString() + BOLD + "M" + GREEN + BOLD + "A" + BLUE + BOLD + "X" + YELLOW + BOLD + "+" + RED + BOLD + "5" + RESET,
            RED.toString() + BOLD + "M" + GREEN + BOLD + "A" + BLUE + BOLD + "X" + YELLOW + BOLD + "+" + RED + BOLD + "6" + RESET,
            RED.toString() + BOLD + "M" + GREEN + BOLD + "A" + BLUE + BOLD + "X" + YELLOW + BOLD + "+" + RED + BOLD + "7" + RESET,
            RED.toString() + BOLD + "M" + GREEN + BOLD + "A" + BLUE + BOLD + "X" + YELLOW + BOLD + "+" + RED + BOLD + "8" + RESET,
            RED.toString() + BOLD + "M" + GREEN + BOLD + "A" + BLUE + BOLD + "X" + YELLOW + BOLD + "+" + RED + BOLD + "9" + RESET,
            RED.toString() + BOLD + "M" + GREEN + BOLD + "A" + BLUE + BOLD + "X" + YELLOW + BOLD + "+" + RED + BOLD + "10" + RESET,
            RED.toString() + BOLD + "M" + GREEN + BOLD + "A" + BLUE + BOLD + "X" + YELLOW + BOLD + "+" + RED + BOLD + "11" + RESET,
            RED.toString() + BOLD + "M" + GREEN + BOLD + "A" + BLUE + BOLD + "X" + YELLOW + BOLD + "+" + RED + BOLD + "12" + RESET,
            RED.toString() + BOLD + "M" + GREEN + BOLD + "A" + BLUE + BOLD + "X" + YELLOW + BOLD + "+" + RED + BOLD + "13" + RESET,
            RED.toString() + BOLD + "M" + GREEN + BOLD + "A" + BLUE + BOLD + "X" + YELLOW + BOLD + "+" + RED + BOLD + "14" + RESET,
            RED.toString() + BOLD + "M" + GREEN + BOLD + "A" + BLUE + BOLD + "X" + YELLOW + BOLD + "+" + RED + BOLD + "15" + RESET,
            RED.toString() + BOLD + "M" + GREEN + BOLD + "A" + BLUE + BOLD + "X" + YELLOW + BOLD + "+" + RED + BOLD + "16" + RESET,
    };
}
