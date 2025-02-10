package com.gto.gtocore.common.data.material;

import com.gto.gtocore.api.data.chemical.material.info.GTOMaterialFlags;

import com.gregtechceu.gtceu.api.GTValues;

import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.DISABLE_DECOMPOSITION;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet.*;
import static com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty.GasTier.HIGHEST;
import static com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty.GasTier.LOW;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gto.gtocore.api.data.chemical.material.info.GTOMaterialIconSet.LIMPID;
import static com.gto.gtocore.common.data.GTOMaterials.*;

public final class MaterialB {

    public static void init() {
        LanthanumExtractionNanoResin = material("lanthanum_extraction_nano_resin", "镧纳米萃取树脂")
                .fluid()
                .color(0x808080)
                .iconSet(LIMPID)
                .buildAndRegister();

        CeriumExtractionNanoResin = material("cerium_extraction_nano_resin", "铈纳米萃取树脂")
                .fluid()
                .color(0x969696)
                .iconSet(LIMPID)
                .buildAndRegister();

        PraseodymiumExtractionNanoResin = material("praseodymium_extraction_nano_resin", "镨纳米萃取树脂")
                .fluid()
                .color(0xA0A0A0)
                .iconSet(LIMPID)
                .buildAndRegister();

        NeodymiumExtractionNanoResin = material("neodymium_extraction_nano_resin", "钕纳米萃取树脂")
                .fluid()
                .color(0xB0B0B0)
                .iconSet(LIMPID)
                .buildAndRegister();

        PromethiumExtractionNanoResin = material("promethium_extraction_nano_resin", "钷纳米萃取树脂")
                .fluid()
                .color(0xC0C0C0)
                .iconSet(LIMPID)
                .buildAndRegister();

        SamariumExtractionNanoResin = material("samarium_extraction_nano_resin", "钐纳米萃取树脂")
                .fluid()
                .color(0xD0D0D0)
                .iconSet(LIMPID)
                .buildAndRegister();

        EuropiumExtractionNanoResin = material("europium_extraction_nano_resin", "铕纳米萃取树脂")
                .fluid()
                .color(0xE0E0E0)
                .iconSet(LIMPID)
                .buildAndRegister();

        GadoliniumExtractionNanoResin = material("gadolinium_extraction_nano_resin", "钆纳米萃取树脂")
                .fluid()
                .color(0xF0F0F0)
                .iconSet(LIMPID)
                .buildAndRegister();

        TerbiumExtractionNanoResin = material("terbium_extraction_nano_resin", "铽纳米萃取树脂")
                .fluid()
                .color(0x0080FF)
                .iconSet(LIMPID)
                .buildAndRegister();

        DysprosiumExtractionNanoResin = material("dysprosium_extraction_nano_resin", "镝纳米萃取树脂")
                .fluid()
                .color(0x00FFFF)
                .iconSet(LIMPID)
                .buildAndRegister();

        HolmiumExtractionNanoResin = material("holmium_extraction_nano_resin", "钬纳米萃取树脂")
                .fluid()
                .color(0x00FF00)
                .iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ErbiumExtractionNanoResin = material("erbium_extraction_nano_resin", "铒纳米萃取树脂")
                .fluid()
                .color(0xFF00FF)
                .iconSet(LIMPID)
                .buildAndRegister();

        ThuliumExtractionNanoResin = material("thulium_extraction_nano_resin", "铥纳米萃取树脂")
                .fluid()
                .color(0xFF0000)
                .iconSet(LIMPID)
                .buildAndRegister();

        YtterbiumExtractionNanoResin = material("ytterbium_extraction_nano_resin", "镱纳米萃取树脂")
                .fluid()
                .color(0xFFFF00)
                .iconSet(LIMPID)
                .buildAndRegister();

        LutetiumExtractionNanoResin = material("lutetium_extraction_nano_resin", "镥纳米萃取树脂")
                .fluid()
                .color(0x800080)
                .iconSet(LIMPID)
                .buildAndRegister();

        ScandiumExtractionNanoResin = material("scandium_extraction_nano_resin", "钪纳米萃取树脂")
                .fluid()
                .color(0x808000)
                .iconSet(LIMPID)
                .buildAndRegister();

        YttriumExtractionNanoResin = material("yttrium_extraction_nano_resin", "钇纳米萃取树脂")
                .fluid()
                .color(0x008000)
                .iconSet(LIMPID)
                .buildAndRegister();

        LanthanumExtractedNanoResin = material("lanthanum_extracted_nano_resin", "溢满的镧纳米萃取树脂")
                .fluid()
                .color(0x808080)
                .iconSet(LIMPID)
                .buildAndRegister();

        CeriumExtractedNanoResin = material("cerium_extracted_nano_resin", "溢满的铈纳米萃取树脂")
                .fluid()
                .color(0x969696)
                .iconSet(LIMPID)
                .buildAndRegister();

        PraseodymiumExtractedNanoResin = material("praseodymium_extracted_nano_resin", "溢满的镨纳米萃取树脂")
                .fluid()
                .color(0xA0A0A0)
                .iconSet(LIMPID)
                .buildAndRegister();

        NeodymiumExtractedNanoResin = material("neodymium_extracted_nano_resin", "溢满的钕纳米萃取树脂")
                .fluid()
                .color(0xB0B0B0)
                .iconSet(LIMPID)
                .buildAndRegister();

        PromethiumExtractedNanoResin = material("promethium_extracted_nano_resin", "溢满的钷纳米萃取树脂")
                .fluid()
                .color(0xC0C0C0)
                .iconSet(LIMPID)
                .buildAndRegister();

        SamariumExtractedNanoResin = material("samarium_extracted_nano_resin", "溢满的钐纳米萃取树脂")
                .fluid()
                .color(0xD0D0D0)
                .iconSet(LIMPID)
                .buildAndRegister();

        EuropiumExtractedNanoResin = material("europium_extracted_nano_resin", "溢满的铕纳米萃取树脂")
                .fluid()
                .color(0xE0E0E0)
                .iconSet(LIMPID)
                .buildAndRegister();

        GadoliniumExtractedNanoResin = material("gadolinium_extracted_nano_resin", "溢满的钆纳米萃取树脂")
                .fluid()
                .color(0xF0F0F0)
                .iconSet(LIMPID)
                .buildAndRegister();

        TerbiumExtractedNanoResin = material("terbium_extracted_nano_resin", "溢满的铽纳米萃取树脂")
                .fluid()
                .color(0x0080FF)
                .iconSet(LIMPID)
                .buildAndRegister();

        DysprosiumExtractedNanoResin = material("dysprosium_extracted_nano_resin", "溢满的镝纳米萃取树脂")
                .fluid()
                .color(0x00FFFF)
                .iconSet(LIMPID)
                .buildAndRegister();

        HolmiumExtractedNanoResin = material("holmium_extracted_nano_resin", "溢满的钬纳米萃取树脂")
                .fluid()
                .color(0x00FF00)
                .iconSet(LIMPID)
                .buildAndRegister();

        ErbiumExtractedNanoResin = material("erbium_extracted_nano_resin", "溢满的铒纳米萃取树脂")
                .fluid()
                .color(0xFF00FF)
                .iconSet(LIMPID)
                .buildAndRegister();

        ThuliumExtractedNanoResin = material("thulium_extracted_nano_resin", "溢满的铥纳米萃取树脂")
                .fluid()
                .color(0xFF0000)
                .iconSet(LIMPID)
                .buildAndRegister();

        YtterbiumExtractedNanoResin = material("ytterbium_extracted_nano_resin", "溢满的镱纳米萃取树脂")
                .fluid()
                .color(0xFFFF00)
                .iconSet(LIMPID)
                .buildAndRegister();

        LutetiumExtractedNanoResin = material("lutetium_extracted_nano_resin", "溢满的镥纳米萃取树脂")
                .fluid()
                .color(0x800080)
                .iconSet(LIMPID)
                .buildAndRegister();

        ScandiumExtractedNanoResin = material("scandium_extracted_nano_resin", "溢满的钪纳米萃取树脂")
                .fluid()
                .color(0x808000)
                .iconSet(LIMPID)
                .buildAndRegister();

        YttriumExtractedNanoResin = material("yttrium_extracted_nano_resin", "溢满的钇纳米萃取树脂")
                .fluid()
                .color(0x008000)
                .iconSet(LIMPID)
                .buildAndRegister();

        AluminaCeramic = material("alumina_ceramic", "氧化铝陶瓷")
                .dust()
                .color(0xe8f0fe)
                .iconSet(BRIGHT)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC)
                .buildAndRegister().setFormula("Al2O3");

        BariumTitanateCeramic = material("barium_titanate_ceramic", "钛酸钡陶瓷")
                .dust()
                .color(0xffcbe2)
                .iconSet(BRIGHT)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC)
                .buildAndRegister().setFormula("BaTiO3");

        TungstenTetraborideCeramics = material("tungsten_tetraboride_ceramics", "四硼化钨陶瓷")
                .dust()
                .color(0x1b1b1b)
                .iconSet(BRIGHT)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC)
                .buildAndRegister().setFormula("WB4");

        SilicaCeramic = material("silica_ceramic", "氧化硅岩陶瓷")
                .dust()
                .color(0x393941)
                .iconSet(BRIGHT)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC)
                .buildAndRegister().setFormula("SiO2");

        HydroxyapatiteCeramic = material("hydroxyapatite_ceramic", "羟基磷灰石陶瓷")
                .dust()
                .color(0x76c1da)
                .iconSet(BRIGHT)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC)
                .buildAndRegister().setFormula("Ca5(PO4)3(OH)");

        TellurateCeramics = material("tellurate_ceramics", "亚碲酸盐陶瓷")
                .dust()
                .color(0xffea9e)
                .iconSet(BRIGHT)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC)
                .buildAndRegister().setFormula("TeO3");

        ThuliumHexaborideCeramics = material("thulium_hexaboride_ceramics", "六硼化铥陶瓷")
                .dust()
                .color(0x2a2a2a)
                .iconSet(BRIGHT)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC)
                .buildAndRegister().setFormula("TmB6");

        SiliconNitrideCeramic = material("silicon_nitride_ceramic", "氮化硅陶瓷")
                .dust()
                .color(0x292824)
                .iconSet(BRIGHT)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC)
                .buildAndRegister().setFormula("Si3N4");

        CobaltOxideCeramic = material("cobalt_oxide_ceramic", "氧化钴陶瓷")
                .dust()
                .color(0x222534)
                .iconSet(BRIGHT)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC)
                .buildAndRegister().setFormula("CoO");

        CalciumOxideCeramic = material("calcium_oxide_ceramic", "氧化钙陶瓷")
                .dust()
                .color(0xf1f3ff)
                .iconSet(BRIGHT)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC)
                .buildAndRegister().setFormula("CaO");

        LithiumOxideCeramics = material("lithium_oxide_ceramics", "氧化锂陶瓷")
                .dust()
                .color(0xf3fff3)
                .iconSet(BRIGHT)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC)
                .buildAndRegister().setFormula("Li2O");

        MagnesiumOxideCeramic = material("magnesium_oxide_ceramic", "氧化镁陶瓷")
                .dust()
                .color(0xffe198)
                .iconSet(BRIGHT)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC)
                .buildAndRegister().setFormula("MgO");

        TitaniumNitrideCeramic = material("titanium_nitride_ceramic", "氮化钛陶瓷")
                .dust()
                .color(0xd4ac4b)
                .iconSet(BRIGHT)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC)
                .buildAndRegister().setFormula("TiN");

        BoronCarbideCeramics = material("boron_carbide_ceramics", "碳化硼陶瓷")
                .dust()
                .color(0x2b2b31)
                .iconSet(BRIGHT)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC)
                .buildAndRegister().setFormula("B4C");

        StrontiumCarbonateCeramic = material("strontium_carbonate_ceramic", "碳酸锶陶瓷")
                .dust()
                .color(0xc5c5c5)
                .iconSet(BRIGHT)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC)
                .buildAndRegister().setFormula("SrCO3");

        ZirconiaCeramic = material("zirconia_ceramic", "氧化锆陶瓷")
                .dust()
                .color(0xeaeaea)
                .iconSet(BRIGHT)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC)
                .buildAndRegister().setFormula("ZrO2");

        TricalciumPhosphateCeramic = material("tricalcium_phosphate_ceramic", "磷酸三钙陶瓷")
                .dust()
                .color(0xe9f4f4)
                .iconSet(BRIGHT)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC)
                .buildAndRegister().setFormula("Ca3(PO4)2");

        SodiumAluminate = material("sodium_aluminate", "铝酸钠")
                .dust()
                .color(0xdee8ea)
                .iconSet(DULL)
                .buildAndRegister().setFormula("NaAlO2");

        AluminiumHydroxide = material("aluminium_hydroxide", "氢氧化铝")
                .dust()
                .color(0xebeef0)
                .iconSet(DULL)
                .buildAndRegister().setFormula("Al(OH)3");

        BariumHydroxide = material("barium_hydroxide", "氢氧化钡")
                .dust()
                .color(0xf6feff)
                .iconSet(DULL)
                .buildAndRegister().setFormula("Ba(OH)2");

        TitaniumDioxide = material("titanium_dioxide", "二氧化钛")
                .dust()
                .color(0xfcf6fc)
                .iconSet(DULL)
                .buildAndRegister().setFormula("TiO2");

        TungstenBoronMixture = material("tungsten_boron_mixture", "钨-硼混合物")
                .dust()
                .color(0x2d2d2d)
                .iconSet(DULL)
                .buildAndRegister();

        UndriedHydroxyapatite = material("undried_hydroxyapatite", "待干燥羟基磷灰石")
                .dust()
                .color(0xf7f7f7)
                .iconSet(DULL)
                .buildAndRegister().setFormula("Ca10(PO4)6(OH)2");

        StrontiumSulfate = material("strontium_sulfate", "硫酸锶")
                .dust()
                .color(0x6a67f1)
                .iconSet(DULL)
                .buildAndRegister().setFormula("SrSO4");

        ZincChloride = material("zinc_chloride", "氯化锌")
                .dust()
                .color(0x6d9cee)
                .components(Zinc, 1, Chlorine, 2)
                .iconSet(METALLIC)
                .buildAndRegister();

        ElectronicGradeSilicon = material("electronic_grade_silicon", "电子级硅")
                .dust()
                .ingot()
                .blastTemp(2273,LOW)
                .color(0x414151)
                .components(Silicon, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister().setFormula("Si(6N)");

        UltraHighPuritySilicon = material("ultra_high_purity_silicon", "超高纯硅")
                .dust()
                .ingot()
                .blastTemp(10000, HIGHEST, GTValues.VA[GTValues.UV], 1200)
                .color(0x414151)
                .components(Silicon, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister().setFormula("Si(N12)");

        Trichlorosilane = material("trichlorosilane", "三氯硅烷")
                .fluid()
                .color(0xb7b7b7)
                .components(Helium, 1, Silicon, 1, Chlorine, 3)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Tetrachlorosilane = material("tetrachlorosilane", "四氯硅烷")
                .fluid()
                .color(0x9e9e9e)
                .components(Silicon, 1, Chlorine, 4)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Silane = material("silane", "硅烷")
                .gas()
                .color(0xb5b5b5)
                .components(Helium, 4, Silicon, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        PickledElectronicGradeSilicon = material("pickled_electronic_grade_silicon", "酸洗后的电子级硅")
                .dust()
                .color(0x414151)
                .components(Silicon, 1)
                .iconSet(SAND)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister().setFormula("Si(N6)");

        PureTrichlorosilane = material("pure_trichlorosilane", "高纯三氯硅烷")
                .fluid()
                .color(0xb7b7b7)
                .components(Helium, 1, Silicon, 1, Chlorine, 3)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        PureSilane = material("pure_silane", "高纯硅烷")
                .gas()
                .color(0xb5b5b5)
                .components(Silicon, 1, Helium, 4)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TetrafluorosilaneSolution = material("tetrafluorosilane_solution", "四氟硅烷溶液")
                .fluid()
                .color(0xf6f7fb)
                .components(Silicon, 1, Fluorine, 4)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister().setFormula("SiF4·(HF)n");
    }
}
