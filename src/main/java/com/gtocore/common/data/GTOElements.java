package com.gtocore.common.data;

import com.gtolib.utils.StringUtils;

import com.gregtechceu.gtceu.api.data.chemical.Element;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public final class GTOElements {

    public static void init() {}

    public static final Element SPACETIME = new Element(0, 1000, -1, "spacetime", () -> Component.translatable("gtocore.spacetime.element").withStyle(ChatFormatting.GRAY));
    public static final Element INFINITY = new Element(100000, 100000, -1, "infinity", () -> Component.literal(StringUtils.full_color("Infinity")));
    public static final Element ORICHALCUM = new Element(320, 320, -1, "orichalcum", "Or");
    public static final Element ADAMANTIUM = new Element(380, 380, -1, "adamantium", "An");
    public static final Element VIBRANIUM = new Element(550, 550, -1, "vibranium", "Vi");
    public static final Element DRACONIUM = new Element(8000, 8000, -1, "draconium", "Dc");
    public static final Element CHAOS = new Element(20000, 20000, -1, "chaos", "§8§kchaos");
    public static final Element MITHRIL = new Element(330, 330, -1, "mithril", "Mi");
    public static final Element INFUSCOLIUM = new Element(600, 600, -1, "infuscolium", "If");
    public static final Element TARANIUM = new Element(440, 440, -1, "taranium", "Tn");
    public static final Element CRYSTALMATRIX = new Element(10000, 0, -1, "crystal_matrix", "§b§ke§r§b✧§ke");
    public static final Element COSMICNEUTRONIUM = new Element(0, 20000, -1, "cosmic_neutronium", "Cnt");
    public static final Element ENDERIUM = new Element(350, 350, -1, "enderium", "En");
    public static final Element ADAMANTINE = new Element(600, 600, -1, "adamantine", "Ad");
    public static final Element LEGENDARIUM = new Element(640, 640, -1, "legendarium", "Le");
    public static final Element STARMETAL = new Element(530, 530, -1, "starmetal", "St");
    public static final Element AWAKENEDDRACONIUM = new Element(16000, 16000, -1, "awakened_draconium", "✵Dc✵");
    public static final Element ECHOITE = new Element(570, 570, -1, "echoite", "Ec");
    public static final Element ETERNITY = new Element(1000000, 1000000, -1, "eternity", "Et❃");
    public static final Element DEGENERATE_REHENIUM = new Element(1000, 1000, -1, "degenerate_rhenium", "§bRe");
    public static final Element HEAVY_QUARK_DEGENERATE_MATTER = new Element(800, 1500, -1, "heavy_quark_degenerate_matter", "§b§ke§r§b(u₂)d§ke");
    public static final Element QUANTUM_CHROMO_DYNAMICALLY_CONFINED_MATTER = new Element(1000, 2000, -1, "quantum_chromo_dynamically_confined_matter", "§b§ke§r§b(u₂)d(c₂)s(t₂)bg§ke");
    public static final Element TRANSCENDENTMETAL = new Element(200, 500, -1, "transcendent_metal", "§kmetal");
    public static final Element COSMIC_MESH = new Element(10000, 10000, -1, "cosmic_mesh", "§kcm");
    public static final Element TITANIUM50 = new Element(22, 28, -1, "titanium50", "Ti⁵⁰");
    public static final Element TENGAM = new Element(200, 0, -1, "tengam", "M");
    public static final Element YITTERBIUM178 = new Element(70, 108, -1, "ytterbium178", "Yb¹⁷⁸");
    public static final Element COPPER76 = new Element(29, 47, -1, "copper76", "Cu⁷⁶");
    public static final Element URUIUM = new Element(480, 480, -1, "uruium", "Ur");
    public static final Element STARLIGHT = new Element(1000, 0, -1, "starlight", "§9Sl");
    public static final Element ELECTRON = new Element(0, 0, -1, "electron", "§ke§re§ke");
    public static final Element ALPHA = new Element(2, 2, -1, "alpha", "§ke§rα§ke");
    public static final Element PROTON = new Element(1, 0, -1, "proton", "§ke§rp§ke");
    public static final Element GLUONS = new Element(10, 10, -1, "gluons", "§ke§rg§ke");
    public static final Element QUARK_GLUON = new Element(10, 10, -1, "quark_gluon", "§ke§r(u2)d(c2)s(t2)bg§ke");
    public static final Element HEAVY_QUARKS = new Element(10, 10, -1, "heavy_quarks", "§ke§r(u₂)d§ke");
    public static final Element LIGHT_QUARKS = new Element(10, 10, -1, "light_quarks", "§ke§r(c₂)(t₂)b§ke");
    public static final Element HEAVY_LEPTON_MIXTURE = new Element(100, 100, -1, "heavy_lepton_mixture", "§ke§r(t₂)u§ke");
    public static final Element HEAVY_QUARK_ENRICHED_MIXTURE = new Element(400, 400, -1, "heavy_quark_enriched_mixture", "§ke§r(u₂)d(c₂)s(t₂)b§ke");
    public static final Element DENSE_NEUTRON = new Element(5000, 5000, -1, "dense_neutron", "§ke§rn§ke");
    public static final Element HIGH_ENERGY_QUARK_GLUON = new Element(1000, 1000, -1, "high_energy_quark_gluon", "§ke§r(u₂)d(c₂)s(t₂)bg§ke");
    public static final Element TIME = new Element(1000, 1000, -1, "time", "§ketime");
    public static final Element STAR_MATTER = new Element(10000, 10000, -1, "star_matter", "§kestar_matter");
    public static final Element RAW_STAR_MATTER = new Element(10000, 10000, -1, "raw_star_matter", "§6§kestar_matter");
    public static final Element RHUGNOR = new Element(100000, 100000, -1, "rhugnor", "Fs⚶");
    public static final Element HYPogen = new Element(100000, 100000, -1, "hypogen", "Hy⚶");
    public static final Element SHIRABON = new Element(5000, 5000, -1, "shirabon", "Sh⏧");
    public static final Element MAGMATTER = new Element(100000, 100000, -1, "magmatter", "M⎋");
    public static final Element ASTRALTITANIUM = new Element(1000, 1000, -1, "astral_titanium", "✧◇✧");
    public static final Element CELESTIALTUNGSTEN = new Element(10000, 10000, -1, "celestial_tungsten", "✦◆✦");
    public static final Element QUANTANIUM = new Element(800, 800, -1, "quantanium", "Qt");
    public static final Element HEXAPHASECOPPER = new Element(1536, 5246, -1, "hexaphasecopper", "✢");
    public static final Element DESH = new Element(200, 300, -1, "desh", "De");
    public static final Element OSTRUM = new Element(300, 300, -1, "ostrum", "Ot");
    public static final Element CALORITE = new Element(300, 400, -1, "calorite", "Ci");
    public static final Element CHROMATICGLASS = new Element(300, 400, -1, "chromatic_glass", "⌘☯☯⌘");
    public static final Element BEDROCKIUM = new Element(1000, 1000, -1, "bedrockium", "?");
    public static final Element BLAZECUBE = new Element(500, 500, -1, "blazecube", "Bc");
    public static final Element ETRIUM = new Element(400, 400, -1, "etrium", "Et⚡");
    public static final Element RESONARIUM = new Element(600, 600, -1, "resonarium", "\uD83D\uDCE2Rs\uD83D\uDCE2");
    public static final Element ASTRIUM = new Element(800, 800, -1, "astrium", "＊");
    public static final Element UELIBRIUM = new Element(400, 400, -1, "uelibrium", "✿");
    public static final Element CRUPTIX = new Element(400, 400, -1, "cruptix", "☄");
    public static final Element PHOTOKRYSTAL = new Element(400, 400, -1, "photokrystal", "✧");

    public static final Element MANASTEEL = new Element(26, 30, -1, "manasteel", "*Ma*");
    public static final Element TERRASTEEL = new Element(56, 66, -1, "terrasteel", "*Tr*");
    public static final Element ELEMENTIUM = new Element(66, 66, -1, "elementium", "*Em*");
    public static final Element ALFSTEEL = new Element(66, 76, -1, "alfsteel", "*Af*"); // Mystical Botany
    public static final Element GAIASTEEL = new Element(76, 86, -1, "gaiasteel", "*Ga*");
    public static final Element GAIA = new Element(76, 88, -1, "gaia", "*TR*Gs*");
    public static final Element AERIALITE = new Element(66, 76, -1, "aerialite", "*Ae*"); // Extra Botany
    public static final Element SHADOWIUM = new Element(76, 86, -1, "shadowium", "*Sh*"); // Extra Botany
    public static final Element PHOTONIUM = new Element(86, 96, -1, "photonium", "*Ph*"); // Extra Botany
    public static final Element OHRICHALOS = new Element(96, 106, -1, "ohrichalos", "*Oh*"); // Extra
                                                                                             // Botany
}
