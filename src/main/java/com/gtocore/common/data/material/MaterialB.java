package com.gtocore.common.data.material;

import com.gtocore.api.data.material.GTOMaterialFlags;
import com.gtocore.common.data.GTOElements;

import com.gregtechceu.gtceu.api.fluids.FluidBuilder;

import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.api.data.material.GTOMaterialIconSet.*;
import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtolib.utils.register.MaterialsRegisterUtils.material;

public final class MaterialB {

    public static void init() {
        Honey = material("honey", "蜂蜜")
                .liquid()
                .color(0xfcd12c)
                .iconSet(DULL)
                .buildAndRegister().setFormula("C6H12O6");

        RawTengam = material("raw_tengam", "生镃")
                .dust()
                .color(0x819a4e)
                .iconSet(DULL)
                .buildAndRegister();

        CleanRawTengam = material("clean_raw_tengam", "洁净生镃")
                .dust()
                .color(0x819a4e)
                .iconSet(SHINY)
                .buildAndRegister();

        PurifiedTengam = material("purified_tengam", "纯镃")
                .dust()
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .element(GTOElements.TENGAM)
                .color(0x819a4e)
                .iconSet(METALLIC)
                .buildAndRegister();

        PreZylon = material("pre_zylon", "预处理柴隆纤维")
                .dust()
                .color(0x562050)
                .components(Carbon, 20, Hydrogen, 22, Nitrogen, 2, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Terephthalaldehyde = material("terephthalaldehyde", "对苯二甲醛")
                .dust()
                .color(0x4a7454)
                .components(Carbon, 8, Hydrogen, 6, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumOxide = material("sodium_oxide", "氧化钠")
                .dust()
                .color(0x036dee)
                .components(Sodium, 2, Oxygen, 1)
                .iconSet(DULL)
                .buildAndRegister();

        CompressedStone = material("compressed_stone", "压缩石头")
                .dust()
                .color(0x696969)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        GermaniumContainingPrecipitate = material("germanium_containing_precipitate", "含锗沉淀物")
                .dust()
                .color(0x666699)
                .components(Germanium, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GermaniumAsh = material("germanium_ash", "锗灰")
                .dust()
                .color(0x706699)
                .components(Germanium, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GermaniumDioxide = material("germanium_dioxide", "二氧化锗")
                .dust()
                .color(0xffffff)
                .components(Germanium, 1, Oxygen, 2)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Durene = material("durene", "杜烯")
                .dust()
                .components(Carbon, 10, Hydrogen, 14)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x708090)
                .iconSet(DULL)
                .buildAndRegister();

        PyromelliticDianhydride = material("pyromellitic_dianhydride", "均苯二甲酸酐")
                .dust()
                .components(Carbon, 10, Hydrogen, 2, Oxygen, 6)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x708090)
                .iconSet(DULL)
                .buildAndRegister();

        CoAcAbCatalyst = material("co_ac_ab", "Co/AC-AB")
                .dust()
                .color(0x544422)
                .iconSet(DULL)
                .flags(GTOMaterialFlags.GENERATE_CATALYST)
                .buildAndRegister();

        ZnFeAlClCatalyst = material("znfealcl", "锌-铁-铝-氯混合")
                .dust()
                .color(0xcf51aa)
                .iconSet(DULL)
                .flags(GTOMaterialFlags.GENERATE_CATALYST)
                .buildAndRegister();

        CalciumCarbide = material("calcium_carbide", "电石")
                .dust()
                .components(Calcium, 1, Carbon, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x47443e)
                .iconSet(DULL)
                .buildAndRegister();

        Difluorobenzophenone = material("difluorobenzophenone", "二氟二苯甲酮")
                .dust()
                .components(Fluorine, 2, Carbon, 13, Hydrogen, 8, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xcf51ae)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumFluoride = material("sodium_fluoride", "氟化钠")
                .dust()
                .components(Sodium, 1, Fluorine, 1)
                .color(0x460011)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumSeaborgate = material("sodium_seaborgate", "\ud872\udf73酸钠")
                .dust()
                .components(Sodium, 2, Seaborgium, 1, Oxygen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x55bbd4)
                .iconSet(DULL)
                .buildAndRegister();

        GoldDepletedMolybdenite = material("gold_depleted_molybdenite", "贫金辉钼矿")
                .dust()
                .color(0x757587)
                .components(Molybdenum, 1, Sulfur, 2)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        MolybdenumTrioxide = material("molybdenum_trioxide", "三氧化钼")
                .dust()
                .components(Molybdenum, 1, Oxygen, 3)
                .flags(GTOMaterialFlags.GENERATE_CATALYST)
                .color(0x757587)
                .iconSet(DULL)
                .buildAndRegister();

        Dichlorocyclooctadieneplatinum = material("dichlorocyclooctadieneplatinium", "二氯环辛二烯铂")
                .dust()
                .color(0xd4e982)
                .components(Carbon, 8, Hydrogen, 12, Chlorine, 2, Platinum, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Diiodobiphenyl = material("diiodobiphenyl", "二碘代联苯")
                .dust()
                .color(0x000a42)
                .components(Carbon, 12, Hydrogen, 8, Iodine, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        MolybdenumConcentrate = material("molybdenum_concentrate", "钼精")
                .dust()
                .color(0x47443e)
                .components(Molybdenum, 1, Sulfur, 2, Concrete, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        BoronTrioxide = material("boron_trioxide", "氧化硼")
                .dust()
                .components(Boron, 2, Oxygen, 3)
                .color(0x8fa6b5)
                .iconSet(DULL)
                .buildAndRegister();

        LithiumNiobateNanoparticles = material("lithium_niobate_nanoparticles", "铌酸锂纳米粒子")
                .dust()
                .color(0xc1c12d)
                .components(Lithium, 2, Niobium, 1, Oxygen, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Hexanitrohexaaxaisowurtzitane = material("hexanitrohexaaxaisowurtzitane", "六硝基六氮杂异伍兹烷")
                .dust()
                .flags(GTOMaterialFlags.GENERATE_TINY_DUST, GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Carbon, 6, Hydrogen, 6, Nitrogen, 12, Oxygen, 12)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x3d464b)
                .iconSet(DULL)
                .buildAndRegister();

        SilicaGel = material("silica_gel", "硅胶")
                .dust()
                .color(0x57c3e4)
                .iconSet(DULL)
                .buildAndRegister();

        CrudeHexanitrohexaaxaisowurtzitane = material("crude_hexanitrohexaaxaisowurtzitane", "粗制六硝基六氮杂异伍兹烷")
                .dust()
                .color(0x19586d)
                .components(Carbon, 6, Hydrogen, 6, Nitrogen, 12, Oxygen, 12)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Tetraacetyldinitrosohexaazaisowurtzitane = material("tetraacetyldinitrosohexaazaisowurtzitane", "四乙酰二硝基六氮杂异戊二烯")
                .dust()
                .color(0x500449)
                .components(Carbon, 14, Hydrogen, 18, Nitrogen, 8, Oxygen, 6)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        NitroniumTetrafluoroborate = material("nitronium_tetrafluoroborate", "四氟硝铵")
                .dust()
                .color(0x3c3f40)
                .components(Nitrogen, 1, Oxygen, 2, Boron, 1, Fluorine, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        NitrosoniumTetrafluoroborate = material("nitrosonium_tetrafluoroborate", "四氟硼酸亚硝铵")
                .dust()
                .color(0x485054)
                .components(Nitrogen, 1, Oxygen, 1, Boron, 1, Fluorine, 4)
                .iconSet(DULL)
                .buildAndRegister();

        Dibenzyltetraacetylhexaazaisowurtzitane = material("dibenzyltetraacetylhexaazaisowurtzitane", "二苄基四乙酰六氮杂异纤锌烷")
                .dust()
                .color(0x64704d)
                .components(Carbon, 28, Hydrogen, 32, Nitrogen, 6, Oxygen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SuccinamidylAcetate = material("succinimidyl_acetate", "琥珀酰亚胺醋酸酯")
                .dust()
                .color(0x64704d)
                .components(Carbon, 6, Hydrogen, 7, Nitrogen, 1, Oxygen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Hexabenzylhexaazaisowurtzitane = material("hexabenzylhexaazaisowurtzitane", "六苄基六氮杂异伍兹烷")
                .dust()
                .color(0x64704d)
                .components(Carbon, 48, Hydrogen, 48, Nitrogen, 6)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        NHydroxysuccinimide = material("n_hydroxysuccinimide", "羟基丁二酰亚胺")
                .dust()
                .color(0x7b717f)
                .components(Carbon, 4, Hydrogen, 5, Nitrogen, 1, Oxygen, 3)
                .iconSet(DULL)
                .buildAndRegister();

        LipoicAcid = material("lipoic_acid", "硫辛酸")
                .liquid()
                .color(0x7b717f)
                .components(Carbon, 8, Hydrogen, 14, Oxygen, 2, Sulfur, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        ChloroauricAcid = material("chloroauric_acid", "氯金酸")
                .liquid()
                .color(0xffd700)
                .components(Gold, 1, Hydrogen, 1, Chlorine, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        NHydroxysuccinimideLipoicEster = material("n_hydroxysuccinimide_lipoic_ester", "硫辛酸N-羟基琥珀酰亚胺酯")
                .dust()
                .color(0x7b717f)
                .components(Carbon, 12, Hydrogen, 17, Nitrogen, 1, Oxygen, 5, Sulfur, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SuccinicAnhydride = material("succinic_anhydride", "丁二酸酐")
                .dust()
                .color(0x401116)
                .components(Carbon, 4, Hydrogen, 4, Oxygen, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SuccinicAcid = material("succinic_acid", "琥珀酸")
                .dust()
                .color(0x104e5c)
                .components(Carbon, 4, Hydrogen, 6, Oxygen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Acetonitrile = material("acetonitrile", "乙腈")
                .dust()
                .color(0x5a6161)
                .components(Carbon, 2, Hydrogen, 3, Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Hexamethylenetetramine = material("hexamethylenetetramine", "环六亚甲基四胺")
                .dust()
                .color(0x5a6261)
                .components(Carbon, 6, Hydrogen, 12, Nitrogen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        RareEarthOxide = material("rare_earth_oxide", "稀土氧化物")
                .dust()
                .color(0x808000)
                .components(Concrete, 1, Oxygen, 1)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        NaquadahContainRareEarth = material("naquadah_contain_rare_earth", "含硅岩的稀土")
                .dust()
                .color(0xffd700)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        NaquadahContainRareEarthFluoride = material("naquadah_contain_rare_earth_fluoride", "含硅岩的稀土氟化物")
                .dust()
                .color(0xb3b300)
                .components(Concrete, 1, Fluorine, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        MetalResidue = material("metal_residue", "金属残渣")
                .dust()
                .color(0x652118)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        PalladiumFullereneMatrix = material("palladium_fullerene_matrix", "钯-富勒烯基质")
                .dust()
                .color(0x96b4b4)
                .components(Palladium, 1, Carbon, 73, Hydrogen, 15, Nitrogen, 1, Iron, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(BRIGHT)
                .buildAndRegister();

        Fullerene = material("fullerene", "富勒烯")
                .dust()
                .color(0x86c2b8)
                .components(Carbon, 60)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        UnfoldedFullerene = material("unfolded_fullerene", "未折叠富勒烯")
                .dust()
                .color(0x587f83)
                .components(Carbon, 60, Hydrogen, 30)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Methylbenzophenanthrene = material("methylbenzophenanthrene", "甲基苯并菲")
                .dust()
                .color(0x79260c)
                .components(Carbon, 19, Hydrogen, 14)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Sarcosine = material("sarcosine", "肌氨酸")
                .dust()
                .color(0x809324)
                .components(Carbon, 3, Hydrogen, 7, Nitrogen, 1, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        DiphenylmethaneDiisocyanate = material("diphenylmethane_diisocyanate", "4,4'-二苯基甲烷二异氰酸酯")
                .dust()
                .color(0x8e801c)
                .components(Carbon, 15, Hydrogen, 10, Nitrogen, 2, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Pentaerythritol = material("pentaerythritol", "季戊四醇")
                .dust()
                .color(0xacacac)
                .components(Carbon, 5, Hydrogen, 12, Oxygen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        KelpSlurry = material("kelp_slurry", "海带浆液")
                .fluid()
                .color(0x336600)
                .iconSet(DULL)
                .buildAndRegister();

        Quasifissioning = material("quasifissioning", "拟裂变")
                .plasma()
                .color(0xcdbe35)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        ExcitedDtec = material("exciteddtec", "激发的异星超维度催化剂")
                .fluid()
                .color(0xb36a6b)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        ExcitedDtsc = material("exciteddtsc", "激发的恒星超维度催化剂")
                .fluid()
                .color(0xb36a6b)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        DimensionallyTranscendentResplendentCatalyst = material("dimensionallytranscendentresplendentcatalyst", "光辉超维度催化剂")
                .fluid()
                .color(0x081010)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        DimensionallyTranscendentProsaicCatalyst = material("dimensionallytranscendentprosaiccatalyst", "平凡超维度催化剂")
                .fluid()
                .color(0x081010)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        DimensionallyTranscendentExoticCatalyst = material("dimensionallytranscendentexoticcatalyst", "异星超维度催化剂")
                .fluid()
                .color(0x081010)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        DimensionallyTranscendentStellarCatalyst = material("dimensionallytranscendentstellarcatalyst", "恒星超维度催化剂")
                .fluid()
                .color(0x081010)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        DimensionallyTranscendentCrudeCatalyst = material("dimensionallytranscendentcrudecatalyst", "粗制超维度催化剂")
                .fluid()
                .color(0x081010)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        Ytterbium178 = material("ytterbium_178", "镱-178")
                .fluid()
                .element(GTOElements.YITTERBIUM178)
                .color(0xffffff)
                .iconSet(DULL)
                .buildAndRegister();

        Flyb = material("flyb", "\ud86d\udce7-镱")
                .plasma()
                .components(Flerovium, 1, Ytterbium178, 1)
                .color(0x890a95)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        EnrichedPotassiumIodideSlurry = material("enriched_potassium_iodide_slurry", "富集碘化钾浆液")
                .fluid()
                .color(0x00ffcc)
                .components(Potassium, 1, Iodine, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        IodineContainingSlurry = material("iodine_containing_slurry", "含碘溶液")
                .fluid()
                .color(0x666633)
                .components(Iodine, 1, RockSalt, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AshLeachingSolution = material("ash_leaching_solution", "灰烬浸出液")
                .fluid()
                .color(0x666699)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        Tannic = material("tannic", "丹宁")
                .liquid()
                .components(Carbon, 76, Hydrogen, 52, Oxygen, 46)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xffff66)
                .iconSet(DULL)
                .buildAndRegister();

        GermaniumTetrachlorideSolution = material("germanium_tetrachloride_solution", "四氯化锗")
                .fluid()
                .gas()
                .color(0x66ffcc)
                .components(Germanium, 1, Chlorine, 4)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Aniline = material("aniline", "苯胺")
                .fluid()
                .components(Carbon, 6, Hydrogen, 7, Nitrogen, 1)
                .color(0x3d7517)
                .iconSet(DULL)
                .buildAndRegister();

        Oxydianiline = material("oxydianiline", "二氨基二苯醚")
                .fluid()
                .components(Carbon, 12, Hydrogen, 12, Nitrogen, 2, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xffd700)
                .iconSet(DULL)
                .buildAndRegister();

        BoricAcid = material("boric_acide", "硼酸")
                .liquid()
                .components(Hydrogen, 3, Boron, 1, Oxygen, 3)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x8fbc8f)
                .iconSet(DULL)
                .buildAndRegister();

        FluoroboricAcid = material("fluoroboric_acide", "氟硼酸")
                .liquid()
                .components(Hydrogen, 1, Boron, 1, Fluorine, 4)
                .color(0x8fbc8f)
                .iconSet(DULL)
                .buildAndRegister();

        BenzenediazoniumTetrafluoroborate = material("benzenediazonium_tetrafluoroborate", "四氟硼酸重氮苯")
                .fluid()
                .components(Carbon, 6, Hydrogen, 5, Boron, 1, Fluorine, 4, Nitrogen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x8fbc8f)
                .iconSet(DULL)
                .buildAndRegister();

        FluoroBenzene = material("fluoro_benzene", "氟苯")
                .fluid()
                .components(Carbon, 6, Hydrogen, 5, Fluorine, 1)
                .color(0x8fbc8f)
                .iconSet(DULL)
                .buildAndRegister();

        Fluorotoluene = material("fluorotoluene", "氟甲苯")
                .fluid()
                .components(Carbon, 7, Hydrogen, 7, Fluorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xdad386)
                .iconSet(DULL)
                .buildAndRegister();

        Hydroquinone = material("hydroquinone", "对苯二酚")
                .fluid()
                .components(Carbon, 6, Hydrogen, 6, Oxygen, 2)
                .color(0x8e2518)
                .iconSet(DULL)
                .buildAndRegister();

        Resorcinol = material("resorcinol", "间苯二酚")
                .fluid()
                .components(Carbon, 6, Hydrogen, 6, Oxygen, 2)
                .color(0x8e2518)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumNitrate = material("sodium_nitrate", "硝酸钠")
                .dust()
                .color(0x4e2a40).iconSet(SAND)
                .components(Sodium, 1, Nitrogen, 1, Oxygen, 3)
                .buildAndRegister();

        SodiumNitrateSolution = material("sodium_nitrate_solution", "硝酸钠溶液")
                .fluid()
                .components(SodiumNitrate, 1, Water, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x2b387e)
                .iconSet(DULL)
                .buildAndRegister();

        Acetylene = material("acetylene", "乙炔")
                .fluid()
                .components(Carbon, 2, Hydrogen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x7f8552)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumCyanide = material("sodium_cyanide", "氰化钠")
                .fluid()
                .components(Sodium, 1, Carbon, 1, Nitrogen, 1)
                .color(0x4f6774)
                .iconSet(DULL)
                .buildAndRegister();

        GoldCyanide = material("gold_cyanide", "氰化金")
                .fluid()
                .components(Gold, 1, Carbon, 1, Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x736f50)
                .iconSet(DULL)
                .buildAndRegister();

        MolybdenumFlue = material("molybdenum_flue", "钼烟气")
                .gas()
                .color(0x4b626f)
                .components(Sulfur, 2, Concrete, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        RheniumSulfuricSolution = material("rhenium_sulfuric_solution", "铼硫酸溶液")
                .fluid()
                .color(0xc0c0ea)
                .components(Rhenium, 1, Sulfur, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AmmoniumPerrhenate = material("ammonium_perrhenate", "高铼酸铵")
                .fluid()
                .color(0x161637)
                .components(Ammonia, 1, Rhenium, 1, Oxygen, 4)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Cycloparaphenylene = material("cycloparaphenylene", "环对苯撑")
                .fluid()
                .color(0x000000)
                .iconSet(DULL)
                .buildAndRegister();

        TrimethylTinChloride = material("trimethyltin_chloride", "三甲基氯化锡")
                .fluid()
                .color(0x72685f)
                .components(Tin, 1, Carbon, 3, Hydrogen, 9, Chlorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SilverTetrafluoroborate = material("silver_tetrafluoroborate", "四氟硼酸银")
                .fluid()
                .color(0x76750f)
                .components(Silver, 1, Boron, 1, Fluorine, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        BoronFluoride = material("boron_fluoride", "氟化硼")
                .fluid()
                .components(Boron, 1, Fluorine, 3)
                .color(0xcecad0)
                .iconSet(DULL)
                .buildAndRegister();

        OneOctene = material("1_octene", "1-辛烯")
                .fluid()
                .components(Carbon, 8, Hydrogen, 16)
                .color(0x666d61)
                .iconSet(DULL)
                .buildAndRegister();

        Pyridine = material("pyridine", "吡啶")
                .fluid()
                .color(0x555642)
                .components(Carbon, 5, Hydrogen, 5, Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Acetaldehyde = material("acetaldehyde", "乙醛")
                .fluid()
                .components(Carbon, 2, Hydrogen, 4, Oxygen, 1)
                .color(0x666d61)
                .iconSet(DULL)
                .buildAndRegister();

        Cyclooctadiene = material("cyclooctadiene", "环辛二烯")
                .fluid()
                .color(0xd4e982)
                .components(Carbon, 8, Hydrogen, 12)
                .iconSet(DULL)
                .buildAndRegister();

        SeaborgiumDopedNanotubes = material("seaborgium_doped_nanotubes", "\ud872\udf73掺杂的纳米管")
                .fluid()
                .color(0x242473)
                .iconSet(DULL)
                .buildAndRegister();

        Ethylenediamine = material("ethylenediamine", "乙二胺")
                .fluid()
                .color(0x1b5d74)
                .components(Carbon, 2, Hydrogen, 8, Nitrogen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Triphenylphosphine = material("triphenylphosphine", "三苯基膦")
                .fluid()
                .color(0xe7d510)
                .components(Carbon, 18, Hydrogen, 15, Phosphorus, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        RhodiumTriphenylphosphineChloride = material("rhodium_triphenylphosphine_chloride", "三（三苯基膦）铑(I)氯化物")
                .dust()
                .color(0x776715)
                .components(Rhodium, 1, Chlorine, 1, Carbon, 18, Hydrogen, 15, Phosphorus, 1)
                .flags(DISABLE_DECOMPOSITION, GTOMaterialFlags.GENERATE_CATALYST)
                .iconSet(DULL)
                .buildAndRegister();

        Ethanolamine = material("ethanolamine", "乙醇胺")
                .fluid()
                .color(0x1b5d74)
                .components(Carbon, 2, Hydrogen, 7, Nitrogen, 1, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        EthyleneOxide = material("ethylene_oxide", "环氧乙烷")
                .fluid()
                .color(0x8eb7d8)
                .components(Carbon, 2, Hydrogen, 4, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Benzaldehyde = material("benzaldehyde", "苯甲醛")
                .fluid()
                .color(0x905a1b)
                .components(Carbon, 7, Hydrogen, 6, Oxygen, 1)
                .iconSet(DULL)
                .buildAndRegister();

        HydroxylamineHydrochloride = material("hydroxylamine_hydrochloride", "盐酸羟胺")
                .fluid()
                .color(0x433217)
                .components(Hydrogen, 4, Nitrogen, 1, Oxygen, 1, Chlorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        MaleicAnhydride = material("maleic_anhydride", "顺丁烯二酸酐")
                .fluid()
                .color(0x321b90)
                .components(Carbon, 4, Hydrogen, 2, Oxygen, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Benzylamine = material("benzylamine", "苄胺")
                .fluid()
                .color(0x5b6363)
                .components(Carbon, 7, Hydrogen, 9, Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Glyoxal = material("glyoxal", "乙二醛")
                .fluid()
                .color(0xf0ed4d)
                .components(Carbon, 2, Hydrogen, 2, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        BenzylChloride = material("benzyl_chloride", "氯化苄")
                .fluid()
                .color(0x9ff6fb)
                .components(Carbon, 7, Hydrogen, 7, Chlorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Tris = material("tris", "三羟甲基氨基甲烷")
                .fluid()
                .color(0x239791)
                .components(Carbon, 4, Hydrogen, 11, Nitrogen, 1, Oxygen, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        TrisHydrochlorideSolution = material("tris_hydrochloride_solution", "Tris-盐酸")
                .fluid()
                .color(0x239791)
                .components(Tris, 1, HydrochloricAcid, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        EDTA = material("edta", "乙二胺四乙酸")
                .dust()
                .color(0x239791)
                .components(Carbon, 10, Hydrogen, 16, Nitrogen, 2, Oxygen, 8)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        CTAB = material("ctab", "十六烷基三甲基溴化铵")
                .dust()
                .color(0x239791)
                .components(Carbon, 19, Hydrogen, 42, Nitrogen, 1, Bromine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        TitaniumNanotubePrecursor = material("titanium_nanotube_precursor", "钛纳米管前驱体")
                .dust()
                .color(0x808080)
                .components(Titanium, 1, Hydrogen, 4, Oxygen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Cetane = material("cetane", "十六烷")
                .polymer()
                .fluid()
                .color(0xfff68f)
                .components(Carbon, 16, Hydrogen, 34)
                .iconSet(DULL)
                .buildAndRegister();

        HexadecylBromide = material("hexadecyl_bromide", "1-溴代十六烷")
                .dust()
                .color(0xfff68f)
                .components(Carbon, 16, Hydrogen, 33, Bromine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        DNAExtractionBuffer = material("dna_extraction_buffer", "DNA提取缓冲液")
                .fluid()
                .color(0x808000)
                .components(Salt, 4, Tris, 4, EDTA, 1, CTAB, 1, Water, 12)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        ChloroaceticAcid = material("chloroacetic_acid", "氯乙酸")
                .liquid()
                .color(0x239791)
                .components(Carbon, 2, Hydrogen, 3, Chlorine, 1, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        RareEarthHydroxides = material("rare_earth_hydroxides", "稀土氢氧化物")
                .fluid()
                .color(0x808000)
                .components(Concrete, 1, Oxygen, 1, Hydrogen, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        QuantumDots = material("quantum_dots", "量子点")
                .fluid()
                .color(0xda0000)
                .iconSet(DULL)
                .buildAndRegister();

        StearicAcid = material("stearic_acid", "硬脂酸")
                .liquid()
                .color(0x239791)
                .components(Carbon, 18, Hydrogen, 36, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Soap = material("soap", "肥皂")
                .fluid()
                .color(0xff9d1b)
                .iconSet(DULL)
                .buildAndRegister();

        Tricotylphosphine = material("tricotylphosphine", "三辛基膦")
                .fluid()
                .color(0xe7d510)
                .components(Carbon, 24, Hydrogen, 51, Phosphorus, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        IridiumTrichlorideSolution = material("iridium_trichloride_solution", "三氯化铱溶液")
                .fluid()
                .color(0x776715)
                .components(Iridium, 1, Chlorine, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        BedrockSmoke = material("bedrock_smoke", "基岩烟")
                .gas()
                .color(0xa9a9a9)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        BedrockSootSolution = material("bedrock_soot_solution", "基岩烟溶液")
                .fluid()
                .color(0x191970)
                .iconSet(DULL)
                .buildAndRegister().setFormula("Nq?", false);

        CleanBedrockSolution = material("clean_bedrock_solution", "洁净基岩烟溶液")
                .fluid()
                .color(0x778899)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister().setFormula("?", false);

        BedrockGas = material("bedrock_gas", "基岩气")
                .gas()
                .color(0xc0c0c0)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        VibraniumUnstable = material("vibranium_unstable", "不稳定振金")
                .fluid()
                .color(0xfa8072)
                .components(Vibranium, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TaraniumEnrichedLiquidHelium3 = material("taranium_enriched_liquid_helium_3", "浓缩塔兰金属的液氦-3")
                .fluid()
                .color(0x57f26d)
                .components(Taranium, 1, Helium3, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TaraniumRichLiquidHelium4 = material("taranium_rich_liquid_helium_4", "富塔兰金属的氦-4")
                .fluid()
                .plasma()
                .color(0x57f26d)
                .components(Taranium, 1, Helium, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        FreeElectronGas = material("free_electron_gas", "高密度自由电子气")
                .gas()
                .color(0x033c3c)
                .element(GTOElements.ELECTRON)
                .iconSet(DULL)
                .buildAndRegister();

        FreeAlphaGas = material("free_alpha_gas", "高密度自由α粒子气")
                .gas()
                .color(0xb5ab06)
                .element(GTOElements.ALPHA)
                .iconSet(DULL)
                .buildAndRegister();

        FreeProtonGas = material("free_proton_gas", "高密度自由质子气")
                .gas()
                .color(0x2089aa)
                .element(GTOElements.PROTON)
                .iconSet(DULL)
                .buildAndRegister();

        QuarkGluon = material("quark_gluon", "夸克-胶子")
                .plasma()
                .color(0x7a00da)
                .element(GTOElements.QUARK_GLUON)
                .iconSet(DULL)
                .buildAndRegister();

        HeavyQuarks = material("heavy_quarks", "重夸克")
                .gas()
                .color(0x007000)
                .element(GTOElements.HEAVY_QUARKS)
                .iconSet(DULL)
                .buildAndRegister();

        LightQuarks = material("light_quarks", "轻夸克")
                .gas()
                .color(0x0000ce)
                .element(GTOElements.LIGHT_QUARKS)
                .iconSet(DULL)
                .buildAndRegister();

        Gluons = material("gluons", "胶子")
                .gas()
                .color(0xfbfbf9)
                .element(GTOElements.GLUONS)
                .iconSet(DULL)
                .buildAndRegister();

        OganessonBreedingBase = material("oganesson_breeding_base", "鿫增殖基")
                .fluid()
                .color(0xb8676c)
                .iconSet(DULL)
                .buildAndRegister();

        TitaniumTetrafluoride = material("titanium_tetrafluoride", "四氟化钛")
                .fluid()
                .color(0xd68fed)
                .components(Titanium, 1, Fluorine, 4)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Titanium50Tetrafluoride = material("titanium_50_tetrafluoride", "四氟化钛-50")
                .fluid()
                .color(0xd68fed)
                .components(Titanium50, 1, Fluorine, 4)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Titanium50Tetrachloride = material("titanium_50_tetrachloride", "四氯化钛-50")
                .fluid()
                .color(0xafeeee)
                .components(Titanium50, 1, Chlorine, 4)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HotOganesson = material("hot_oganesson", "热鿫")
                .fluid()
                .color(0x42145d)
                .components(Oganesson, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Ferrocene = material("ferrocene", "二茂铁")
                .fluid()
                .color(0x7373c9)
                .components(Carbon, 10, Hydrogen, 10, Iron, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        ScandiumTitanium50Mixture = material("scandium_titanium_50_mixture", "钪-钛50混合物")
                .fluid()
                .color(0xeb315f)
                .components(Scandium, 1, Titanium50, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        DragonBreath = material("dragon_breath", "龙息")
                .fluid()
                .color(0xff00ff)
                .components(Draconium, 1, Concrete, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        EnrichedDragonBreath = material("enriched_dragon_breath", "富集龙息")
                .fluid()
                .color(0xff00ff)
                .components(Draconium, 1, Concrete, 1)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        DragonBlood = material("dragon_blood", "龙血")
                .fluid()
                .color(0x9932cc)
                .components(Draconium, 1, Concrete, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TurbidDragonBlood = material("turbid_dragon_blood", "龙血浊液")
                .fluid()
                .color(0x4d0099)
                .components(Draconium, 1, Concrete, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        DragonElement = material("dragon_element", "龙素")
                .fluid()
                .color(0x9933ff)
                .iconSet(DULL)
                .components(Draconium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HeavyLeptonMixture = material("heavy_lepton_mixture", "重轻子混合物")
                .gas()
                .color(0x3ad931)
                .element(GTOElements.HEAVY_LEPTON_MIXTURE)
                .iconSet(DULL)
                .buildAndRegister();

        HeavyQuarkEnrichedMixture = material("heavy_quark_enriched_mixture", "富集重夸克混合物")
                .gas()
                .color(0xececec)
                .element(GTOElements.HEAVY_QUARK_ENRICHED_MIXTURE)
                .iconSet(DULL)
                .buildAndRegister();

        CosmicComputingMixture = material("cosmic_computing_mixture", "寰宇计算混合物")
                .gas()
                .color(0x8b8925)
                .components(Gluons, 1, HeavyQuarks, 1, HeavyLeptonMixture, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        LiquidStarlight = material("liquid_starlight", "星能液")
                .liquid(new FluidBuilder().customStill())
                .element(GTOElements.STARLIGHT)
                .buildAndRegister();

        Starlight = material("starlight", "星光")
                .liquid(new FluidBuilder().customStill())
                .element(GTOElements.STARLIGHT)
                .buildAndRegister();

        DenseNeutron = material("dense_neutron", "致密中子")
                .plasma()
                .color(0x9ce89c)
                .element(GTOElements.DENSE_NEUTRON)
                .iconSet(DULL)
                .buildAndRegister();

        HighEnergyQuarkGluon = material("high_energy_quark_gluon", "高能夸克-胶子")
                .plasma()
                .color(0x7400ce)
                .element(GTOElements.HIGH_ENERGY_QUARK_GLUON)
                .iconSet(BRIGHT)
                .buildAndRegister();

        NeutroniumDopedNanotubes = material("neutronium_doped_nanotubes", "掺中子素纳米管")
                .fluid()
                .color(0x5bf5f5)
                .iconSet(DULL)
                .buildAndRegister();

        AmmoniumNitrateSolution = material("ammonium_nitrate_solution", "硝酸铵溶液")
                .fluid()
                .color(0xe1ffff)
                .components(Nitrogen, 2, Hydrogen, 4, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(LIMPID)
                .buildAndRegister();

        NaquadahSolution = material("naquadah_solution", "硅岩溶液")
                .fluid()
                .color(0x00ff00)
                .iconSet(DULL)
                .buildAndRegister();

        FluorineCrackedAquadah = material("fluorine_cracked_aquadah", "加氟裂化硅岩")
                .fluid()
                .color(0x424d4b)
                .iconSet(DULL)
                .buildAndRegister();

        RadonCrackedEnrichedAquadah = material("radon_cracked_enriched_aquadah", "加氡裂化富集硅岩")
                .fluid()
                .color(0x424d4b)
                .iconSet(DULL)
                .buildAndRegister();

        NaquadahFuel = material("naquadah_fuel", "硅岩燃料")
                .fluid()
                .color(0x292929)
                .components(Naquadah, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        EnrichedNaquadahFuel = material("enriched_naquadah_fuel", "富集硅岩燃料")
                .fluid()
                .color(0x292929)
                .components(NaquadahEnriched, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HyperFuel1 = material("hyper_fuel_1", "超能燃料 I")
                .fluid()
                .color(0xf9ff3d)
                .components(Naquadah, 1, NaquadahEnriched, 1, Naquadria, 1, Thorium, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HyperFuel2 = material("hyper_fuel_2", "超能燃料 II")
                .fluid()
                .color(0xd1d54d)
                .components(HyperFuel1, 1, Uranium235, 1, Dubnium, 1, Fermium, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HyperFuel3 = material("hyper_fuel_3", "超能燃料 III")
                .fluid()
                .color(0x7a7c3c)
                .components(HyperFuel2, 1, Plutonium241, 1, Adamantine, 1, Lawrencium, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HyperFuel4 = material("hyper_fuel_4", "超能燃料 IV")
                .fluid()
                .color(0x3f4028)
                .components(HyperFuel3, 1, Nobelium, 1, Neutronium, 1, Taranium, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ConcentrationMixingHyperFuel1 = material("concentration_mixing_hyper_fuel_1", "浓缩混合超能燃料 I")
                .fluid()
                .color(0x000000)
                .components(HyperFuel4, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ConcentrationMixingHyperFuel2 = material("concentration_mixing_hyper_fuel_2", "浓缩混合超能燃料 II")
                .fluid()
                .color(0x000000)
                .components(HyperFuel4, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CosmicElement = material("cosmic_element", "宇宙素")
                .gas()
                .color(0xa366ff)
                .components(FreeElectronGas, 1, FreeAlphaGas, 1, FreeProtonGas, 1, QuarkGluon, 1,
                        HeavyQuarks, 1, LightQuarks, 1, Gluons, 1, HeavyLeptonMixture, 1,
                        HeavyQuarkEnrichedMixture, 1, DenseNeutron, 1,
                        HighEnergyQuarkGluon, 1)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        DimensionallyTranscendentResidue = material("dimensionallytranscendentresidue", "超维度残留")
                .liquid(new FluidBuilder().customStill())
                .buildAndRegister().setFormula("?", false);

        PrimordialMatter = material("primordialmatter", "流体本源物质")
                .liquid(new FluidBuilder().customStill())
                .buildAndRegister().setFormula("?", false);

        SpatialFluid = material("spatialfluid", "扩大化空间流体")
                .liquid(new FluidBuilder().customStill())
                .element(GTOElements.TIME)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TemporalFluid = material("temporalfluid", "富快子时间流体")
                .liquid(new FluidBuilder().customStill())
                .element(GTOElements.TIME)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Isochloropropane = material("isochloropropane", "氯丙烷")
                .fluid()
                .color(0xcdd681)
                .components(Carbon, 3, Hydrogen, 7, Chlorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Dinitrodipropanyloxybenzene = material("dinitrodipropanyloxybenzene", "二硝基二丙氧基苯")
                .fluid()
                .color(0x6a784d)
                .components(Carbon, 12, Hydrogen, 16, Nitrogen, 2, Oxygen, 6)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        RadoxGas = material("radox_gas", "拉多X气")
                .gas()
                .components(Carbon, 14, Osmium, 11, Silver, 3, Concrete, 1, Water, 1)
                .color(0xab57ab)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CrackedRadox = material("crackedradox", "裂化拉多X")
                .fluid()
                .components(Carbon, 14, Osmium, 11, Silver, 3, Concrete, 1, Water, 1)
                .color(0xab57ab)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SuperLightRadox = material("superlightradox", "超轻拉多X")
                .fluid()
                .components(Carbon, 14, Osmium, 11, Silver, 2, Concrete, 1, Water, 1)
                .color(0x6c006c)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        LightRadox = material("lightradox", "轻拉多X")
                .fluid()
                .components(Carbon, 14, Osmium, 11, Silver, 1, Concrete, 1, Water, 1)
                .color(0x6c006c)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SuperHeavyRadox = material("superheavyradox", "超重拉多X")
                .fluid()
                .components(Carbon, 14, Osmium, 11, Concrete, 1, Water, 1)
                .color(0x6c006c)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HeavyRadox = material("heavyradox", "重拉多X")
                .fluid()
                .components(Carbon, 14, Osmium, 11, Concrete, 1, Water, 1)
                .color(0x6c006c)
                .iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        RawRadox = material("rawradox", "粗制拉多X")
                .fluid()
                .color(0x391539)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?", false);

        Xenoxene = material("xenoxene", "异氙")
                .fluid()
                .color(0x5c5a58)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?", false);

        XenoxeneCrystal = material("xenoxene_crystal", "结晶异氙")
                .dust()
                .color(0x5c5a58)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?", false);

        XenoxeneMixture = material("xenoxene_mixture", "异氙混合物")
                .fluid()
                .color(0x5c7a58)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?", false);

        EnrichedXenoxene = material("enriched_xenoxene", "富集异氙")
                .fluid()
                .color(0x5c5a58)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        PurifiedXenoxene = material("purified_xenoxene", "纯净异氙")
                .fluid()
                .color(0x5c7a58)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        DilutedXenoxene = material("dilutedxenoxene", "钝化异氙")
                .fluid()
                .color(0x8b8784)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("?", false);

        Dibromomethylbenzene = material("dibromomethylbenzene", "二溴甲苯")
                .fluid()
                .components(Carbon, 7, Hydrogen, 6, Bromine, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x6b584)
                .iconSet(DULL)
                .buildAndRegister();

        RawStarMatter = material("raw_star_matter", "原始恒星混合物")
                .element(GTOElements.RAW_STAR_MATTER)
                .plasma(new FluidBuilder().customStill())
                .buildAndRegister();

        BiomediumRaw = material("biomediumraw", "生物培养基原液")
                .fluid()
                .color(0x42641f)
                .iconSet(DULL)
                .buildAndRegister();

        BiohmediumSterilized = material("biohmediumsterilized", "灭菌生物培养基原液")
                .fluid()
                .color(0x72b125)
                .iconSet(DULL)
                .buildAndRegister();

        UnknowWater = material("unknowwater", "不明液体")
                .fluid()
                .color(0x3322aa)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        UnknownNutrientAgar = material("unknownnutrientagar", "未知营养琼脂")
                .fluid()
                .color(0x916e00)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        SeaweedBroth = material("seaweedbroth", "海藻基质")
                .fluid()
                .color(0x2c9400)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?", false);

        LiquidCrystalKevlar = material("liquidcrystalkevlar", "液晶凯芙拉")
                .fluid()
                .color(0x9f9f50)
                .iconSet(DULL)
                .buildAndRegister();

        PolyurethaneResin = material("polyurethaneresin", "聚氨酯树脂")
                .fluid()
                .color(0x9a9a51)
                .iconSet(DULL)
                .buildAndRegister();

        DiphenylmethanediisocyanateMixture = material("diphenylmethanediisocyanatemixture", "二苯基甲烷二异氰酸酯混合物")
                .fluid()
                .color(0x94851d)
                .iconSet(DULL)
                .buildAndRegister();

        Phosgene = material("phosgene", "碳酰氯")
                .fluid()
                .components(Carbon, 1, Oxygen, 1, Chlorine, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x0e6c11)
                .iconSet(DULL)
                .buildAndRegister();

        DiaminodiphenylmethanMixture = material("diaminodiphenylmethanmixture", "二氨基二苯甲烷混合物")
                .fluid()
                .color(0x94851d)
                .iconSet(DULL)
                .buildAndRegister();

        SiliconOil = material("siliconoil", "硅油")
                .fluid()
                .color(0xadadad)
                .iconSet(DULL)
                .buildAndRegister();

        EthyleneGlycol = material("ethyleneglycol", "乙二醇")
                .fluid()
                .color(0xadadad)
                .components(Carbon, 2, Hydrogen, 6, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        DiethyleneGlycol = material("diethyleneglycol", "二乙二醇")
                .fluid()
                .color(0xadadad)
                .components(Carbon, 4, Hydrogen, 10, Oxygen, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        PCBs = material("pcbs", "苯基-C61-丁酸苯乙烯")
                .fluid()
                .color(0x758a61)
                .components(Carbon, 80, Hydrogen, 21, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        DMAP = material("dmap", "二甲氨基吡啶")
                .dust()
                .color(0x758a61)
                .components(Carbon, 7, Hydrogen, 10, Nitrogen, 2)
                .flags(GTOMaterialFlags.GENERATE_CATALYST)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        AluminiumChloride = material("aluminium_chloride", "氯化铝")
                .dust()
                .color(0xa4c4c6)
                .components(Aluminium, 1, Chlorine, 3)
                .iconSet(DULL)
                .buildAndRegister();

        TiAlChloride = material("ti_al_chloride", "钛-铝氯化物")
                .dust()
                .color(0x6e1050)
                .flags(GTOMaterialFlags.GENERATE_CATALYST)
                .iconSet(DULL)
                .buildAndRegister();

        PhenylPentanoicAcid = material("phenylpentanoic_acid", "苯基戊酸")
                .liquid()
                .color(0x4d3833)
                .components(Carbon, 11, Hydrogen, 14, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Dichloromethane = material("dichloromethane", "二氯甲烷")
                .fluid()
                .color(0x832663)
                .components(Carbon, 1, Hydrogen, 2, Chlorine, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        DimethylSulfide = material("dimethyl_sulfide", "二甲硫醚")
                .fluid()
                .color(0xa02e06)
                .components(Carbon, 2, Hydrogen, 6, Sulfur, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        CosmicMesh = material("cosmic_mesh", "寰宇织网")
                .plasma()
                .fluid()
                .color(0x181878)
                .element(GTOElements.COSMIC_MESH)
                .iconSet(DULL)
                .buildAndRegister();

        HydrobromicAcid = material("hydrobromic_acid", "氢溴酸")
                .liquid()
                .components(Hydrogen, 1, Bromine, 1)
                .color(0xa2573f)
                .iconSet(DULL)
                .buildAndRegister();

        BenzophenanthrenylAcetonitrile = material("benzophenanthrenylacetonitrile", "苯并菲乙腈")
                .dust()
                .components(Carbon, 20, Hydrogen, 13, Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x9222c7)
                .iconSet(DULL)
                .buildAndRegister();

        BromoSuccinamide = material("bromo_succinimide", "N-溴代琥珀酰亚胺")
                .dust()
                .components(Carbon, 4, Hydrogen, 4, Bromine, 1, Nitrogen, 1, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x546a21)
                .iconSet(DULL)
                .buildAndRegister();

        PotassiumBromide = material("potassium_bromide", "溴化钾")
                .dust()
                .components(Potassium, 1, Bromine, 1)
                .color(0xa34a76)
                .iconSet(GEM_HORIZONTAL)
                .buildAndRegister();

        Succinimide = material("succinimide", "琥珀酰亚胺")
                .dust()
                .components(Carbon, 4, Hydrogen, 5, Nitrogen, 1, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x21a7c5)
                .iconSet(DULL)
                .buildAndRegister();

        FissionedUranium235 = material("fissioned_uranium_235", "裂变铀-235")
                .dust()
                .color(0x39c9b7)
                .iconSet(METALLIC)
                .buildAndRegister();

        FranciumCaesiumCadmiumBromide = material("francium_caesium_cadmium_bromide", "溴化钫铯镉")
                .dust()
                .components(Francium, 1, Caesium, 1, Cadmium, 2, Bromine, 6)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xa34d1a)
                .iconSet(BRIGHT)
                .buildAndRegister();

        StrontiumEuropiumAluminate = material("strontium_europium_aluminate", "锶铕铝酸盐")
                .dust()
                .components(Strontium, 1, Europium, 1, Aluminium, 2, Oxygen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xb62d78)
                .iconSet(BRIGHT)
                .buildAndRegister();

        UraniumSulfateWasteSolution = material("uranium_sulfate_waste_solution", "硫酸铀废液")
                .fluid()
                .color(0xb1b113)
                .iconSet(DULL)
                .buildAndRegister();

        DibismuthHydroborate = material("dibismuthhydroborat", "硼氢二铋")
                .dust()
                .components(Bismuth, 2, Hydrogen, 1, Boron, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x007d32)
                .iconSet(BRIGHT)
                .buildAndRegister();

        CircuitCompound = material("circuit_compound", "电路化合物")
                .dust()
                .components(IndiumGalliumPhosphide, 1, DibismuthHydroborate, 3, BismuthTellurite, 2)
                .color(0x00210e)
                .iconSet(BRIGHT)
                .buildAndRegister();

        CaesiumIodide = material("caesium_iodide", "碘化铯")
                .dust()
                .components(Caesium, 1, Iodine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xeeeee2)
                .iconSet(DULL)
                .buildAndRegister();

        ThalliumThuliumDopedCaesiumIodide = material("thallium_thulium_doped_caesium_iodide", "铊铥掺杂的碘化铯")
                .dust()
                .color(0xe8b97f)
                .iconSet(BRIGHT)
                .buildAndRegister();

        Photoresist = material("photoresist", "光刻胶")
                .fluid()
                .color(0x2f4f4f)
                .iconSet(BRIGHT)
                .buildAndRegister();

        EuvPhotoresist = material("euv_photoresist", "EUV光刻胶")
                .fluid()
                .color(0x4b0082)
                .iconSet(BRIGHT)
                .buildAndRegister();

        GammaRaysPhotoresist = material("gamma_rays_photoresist", "γ射线光刻胶")
                .fluid()
                .color(0x556b2f)
                .iconSet(BRIGHT)
                .buildAndRegister();

        AcrylicAcid = material("acrylic_acid", "丙烯酸")
                .liquid()
                .color(0xb41558)
                .components(Carbon, 3, Hydrogen, 4, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        EthylAcrylate = material("ethyl_acrylate", "丙烯酸乙酯")
                .fluid()
                .color(0x947d15)
                .components(Carbon, 5, Hydrogen, 8, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Trichloroflerane = material("trichloroflerane", "三氯\ud86d\udce7烷")
                .fluid()
                .color(0x42145d)
                .components(Flerovium, 1, Chlorine, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        BisethylenedithiotetraselenafulvalenePerrhenate = material("bisethylenedithiotetraselenafulvalene_perrhenate", "高铼酸双（乙烯二硫代）四硒富瓦烯")
                .dust()
                .color(0x72cb00)
                .components(Rhenium, 1, Carbon, 10, Hydrogen, 8, Sulfur, 4, Selenium, 4, Oxygen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(BRIGHT)
                .buildAndRegister();

        Bisethylenedithiotetraselenafulvalene = material("bisethylenedithiotetraselenafulvalene", "双（乙烯二硫代）四硒富瓦烯")
                .dust()
                .color(0x72cb00)
                .components(Carbon, 10, Hydrogen, 8, Sulfur, 4, Selenium, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(BRIGHT)
                .buildAndRegister();

        LithiumThiinediselenide = material("lithium_thiinediselenide", "二硒醚合硫锂")
                .dust()
                .color(0x72cb00)
                .components(Carbon, 4, Hydrogen, 4, Sulfur, 2, Lithium, 2, Selenium, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        CyclopentadienylTitaniumTrichloride = material("cyclopentadienyl_titanium_trichloride", "环戊二烯基三氯化钛")
                .dust()
                .color(0xb22db2)
                .components(Carbon, 10, Hydrogen, 10, Chlorine, 2, Titanium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(BRIGHT)
                .buildAndRegister();

        ButylLithium = material("butyl_lithium", "丁基锂")
                .fluid()
                .color(0x9f6af6)
                .components(Carbon, 4, Hydrogen, 9, Lithium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Bromodihydrothiine = material("bromodihydrothiine", "溴二氢硫醚")
                .fluid()
                .color(0x50c44d)
                .components(Carbon, 4, Hydrogen, 4, Sulfur, 2, Bromine, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Dibromoacrolein = material("dibromoacrolein", "二溴丙烯醛")
                .fluid()
                .color(0x3e3e3e)
                .components(Carbon, 2, Hydrogen, 2, Bromine, 2, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumThiosulfate = material("sodium_thiosulfate", "硫代硫酸钠")
                .dust()
                .color(0x145a9d)
                .components(Sodium, 2, Sulfur, 2, Oxygen, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        LithiumFluoride = material("lithium_fluoride", "氟化锂")
                .dust()
                .color(0x6d6d6d)
                .components(Lithium, 1, Fluorine, 1)
                .iconSet(DULL)
                .buildAndRegister();

        HighPurityCalciumCarbonate = material("high_purity_calcium_carbonate", "高纯度碳酸钙")
                .dust()
                .color(0xeeeee0)
                .components(Calcium, 1, Carbon, 1, Oxygen, 3)
                .iconSet(DULL)
                .buildAndRegister();

        Bromobutane = material("bromobutane", "溴丁烷")
                .fluid()
                .color(0xff0c0c)
                .components(Carbon, 4, Hydrogen, 9, Bromine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Propadiene = material("propadiene", "丙二烯")
                .fluid()
                .color(0x323b0a)
                .components(Carbon, 3, Hydrogen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        AstatideSolution = material("astatide_solution", "砹化物溶液")
                .liquid()
                .color(0x4ff417)
                .components(Astatine, 1, SulfuricAcid, 1)
                .iconSet(DULL)
                .buildAndRegister();

        MixedAstatideSalts = material("mixed_astatide_salts", "混合砹化盐")
                .dust()
                .color(0x67e83c)
                .components(Holmium, 1, Thulium, 1, Copernicium, 1, Flerovium, 1, Astatine, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        BoronFranciumCarbide = material("boron_francium_carbide", "硼-钫碳化物")
                .dust()
                .color(0x797979)
                .components(Francium, 4, Boron, 4, Carbon, 7)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Borocarbide = material("borocarbide", "碳化硼混合材料")
                .dust()
                .color(0x7e7e22)
                .components(MixedAstatideSalts, 1, BoronFranciumCarbide, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        FranciumCarbide = material("francium_carbide", "碳化钫")
                .dust()
                .color(0xa1a1a1)
                .components(Francium, 2, Carbon, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        BoronCarbide = material("boron_carbide", "碳化硼")
                .dust()
                .color(0x1e1e1e)
                .components(Boron, 4, Carbon, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        LanthanumEmbeddedFullerene = material("lanthanum_embedded_fullerene", "镧-富勒烯包合物")
                .dust()
                .color(0x91c100)
                .components(Lanthanum, 1, Fullerene, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        LanthanumFullereneMix = material("lanthanum_fullerene_mix", "镧-富勒烯混合物")
                .dust()
                .color(0xd3bfec)
                .components(Lanthanum, 1, UnfoldedFullerene, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        CaliforniumTrichloride = material("californium_trichloride", "三氯化锎")
                .dust()
                .color(0x286224)
                .components(Californium, 1, Chlorine, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        FullereneDopedNanotubes = material("fullerene_doped_nanotubes", "富勒烯掺杂的纳米管")
                .fluid()
                .color(0x562356)
                .iconSet(DULL)
                .buildAndRegister();

        CaliforniumCyclopentadienide = material("californium_cyclopentadienide", "环戊二烯化锎")
                .fluid()
                .color(0x78374a)
                .components(Carbon, 15, Hydrogen, 15, Californium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Cyclopentadiene = material("cyclopentadiene", "环戊二烯")
                .fluid()
                .color(0x2aa62a)
                .components(Carbon, 5, Hydrogen, 6)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        LithiumCyclopentadienide = material("lithium_cyclopentadienide", "环戊二烯化锂")
                .fluid()
                .color(0x7b4657)
                .components(Lithium, 1, Carbon, 5, Hydrogen, 5)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Dimethylether = material("dimethylether", "二甲基乙醚")
                .fluid()
                .color(0xbda90e)
                .components(Carbon, 2, Hydrogen, 6, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Dimethoxyethane = material("dimethoxyethane", "二甲氧基乙烷")
                .fluid()
                .color(0x23a996)
                .components(Carbon, 4, Hydrogen, 10, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Photopolymer = material("photopolymer", "光聚合物溶液")
                .fluid()
                .color(0x73445b)
                .components(Carbon, 149, Hydrogen, 97, Nitrogen, 10, Oxygen, 2, Titanium, 1,
                        Boron, 1, Fluorine, 20)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SilverPerchlorate = material("silver_perchlorate", "高氯酸银")
                .dust()
                .color(0xededed)
                .components(Silver, 1, Chlorine, 1, Oxygen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SilverChloride = material("silver_chloride", "氯化银")
                .dust()
                .color(0x8d8d8d)
                .components(Silver, 1, Chlorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumBromide = material("sodium_bromide", "溴化钠")
                .dust()
                .color(0xc588c4)
                .components(Sodium, 1, Bromine, 1)
                .iconSet(DULL)
                .buildAndRegister();

        SilverOxide = material("silver_oxide", "氧化银")
                .dust()
                .color(0x2b2b2b)
                .components(Silver, 2, Oxygen, 1)
                .iconSet(DULL)
                .buildAndRegister();

        PhthalicAnhydride = material("phthalic_anhydride", "邻苯二甲酸酐")
                .dust()
                .color(0x8c8c8c)
                .components(Carbon, 8, Hydrogen, 4, Oxygen, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumHypochlorite = material("sodium_hypochlorite", "次氯酸钠")
                .dust()
                .color(0x66f14c)
                .components(Sodium, 1, Chlorine, 1, Oxygen, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Ethylanthraquinone = material("ethylanthraquinone", "2-乙基蒽醌")
                .fluid()
                .color(0xffff24)
                .components(Carbon, 16, Hydrogen, 12, Oxygen, 2)
                .iconSet(DULL)
                .buildAndRegister();

        Ethylanthrahydroquinone = material("ethylanthrahydroquinone", "2-乙基蒽氢醌")
                .fluid()
                .color(0xcece00)
                .components(Ethylanthraquinone, 1, Hydrogen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Anthracene = material("anthracene", "蒽")
                .fluid()
                .color(0x929e92)
                .components(Carbon, 14, Hydrogen, 10)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Phenylsodium = material("phenylsodium", "苯基钠")
                .fluid()
                .color(0x2626ab)
                .components(Carbon, 6, Hydrogen, 5, Sodium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        NDifluorophenylpyrrole = material("n_difluorophenylpyrrole", "N-二氟苯基吡咯")
                .fluid()
                .color(0x2f7e8a)
                .components(Carbon, 10, Hydrogen, 7, Fluorine, 2, Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Difluoroaniline = material("difluoroaniline", "二氟苯胺")
                .fluid()
                .color(0x348f3e)
                .components(Carbon, 6, Hydrogen, 5, Fluorine, 2, Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Succinaldehyde = material("succinaldehyde", "琥珀醛")
                .fluid()
                .color(0x63577d)
                .components(Carbon, 4, Hydrogen, 6, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        TetraethylammoniumBromide = material("tetraethylammonium_bromide", "四乙基溴化铵")
                .fluid()
                .color(0xc20cff)
                .components(Carbon, 8, Hydrogen, 20, Nitrogen, 1, Bromine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        RhodiumRheniumNaquadahCatalyst = material("rhodium_rhenium_naquadah", "铑-铼-硅岩")
                .dust()
                .color(0x944648)
                .components(Rhodium, 1, Rhenium, 1, Naquadah, 1)
                .flags(GTOMaterialFlags.GENERATE_CATALYST)
                .iconSet(DULL)
                .buildAndRegister();

        IodineMonochloride = material("iodine_monochloride", "氯化碘")
                .fluid()
                .color(0x004141)
                .components(Iodine, 1, Chlorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Dimethylnaphthalene = material("dimethylnaphthalene", "二甲基萘")
                .fluid()
                .color(0xde2da1)
                .components(Carbon, 12, Hydrogen, 12)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Dihydroiodotetracene = material("dihydroiodotetracene", "二氢碘化四联苯")
                .fluid()
                .color(0xde2da1)
                .components(Carbon, 18, Hydrogen, 13, Iodine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        AcetylatingReagent = material("acetylating_reagent", "乙酰化试剂")
                .fluid()
                .color(0x724c50)
                .components(Carbon, 9, Hydrogen, 12, Silicon, 1, Magnesium, 2, Bromine, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        MagnesiumChlorideBromide = material("magnesium_chloride_bromide", "溴氯化镁")
                .dust()
                .color(0x99234d)
                .components(Magnesium, 1, Chlorine, 1, Bromine, 1)
                .iconSet(DULL)
                .buildAndRegister();

        IsopropylAlcohol = material("isopropyl_alcohol", "异丙醇")
                .fluid()
                .color(0x51b31f)
                .components(Carbon, 3, Hydrogen, 8, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Dichlorodicyanobenzoquinone = material("dichlorodicyanobenzoquinone", "二氯二氰苯醌")
                .fluid()
                .color(0x302399)
                .components(Carbon, 8, Chlorine, 2, Nitrogen, 2, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Dichlorodicyanohydroquinone = material("dichlorodicyanohydroquinone", "二氯二氰氢醌")
                .fluid()
                .color(0x302399)
                .components(Carbon, 8, Chlorine, 2, Nitrogen, 2, Oxygen, 2, Hydrogen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Tetracene = material("tetracene", "并四苯")
                .dust()
                .color(0x8f7718)
                .components(Carbon, 18, Hydrogen, 12)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        PolycyclicAromaticMixture = material("polycyclic_aromatic_mixture", "多环芳香烃混合物")
                .dust()
                .color(0x8f7718)
                .components(Carbon, 18, Hydrogen, 12)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        RheniumHassiumThalliumIsophtaloylbisdiethylthioureaHexaf = material("rhenium_hassium_thallium_isophtaloylbisdiethylthiourea_hexaf", "间苯二甲酰双（二乙基硫脲基）六氟磷酸铼-\ud872\udf76-铊")
                .dust()
                .color(0x8f7718)
                .components(Rhenium, 1, Hassium, 1, Thallium, 1, Carbon, 60, Phosphorus, 1,
                        Nitrogen, 12, Hydrogen, 84, Sulfur, 6, Oxygen, 12, Fluorine, 6)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        ThalliumChloride = material("thallium_chloride", "氯化铊")
                .dust()
                .color(0xcc5350)
                .components(Thallium, 1, Chlorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        HassiumChloride = material("hassium_chloride", "氯化\ud872\udf76")
                .dust()
                .color(0x5828b2)
                .components(Hassium, 1, Chlorine, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        RheniumChloride = material("rhenium_chloride", "氯化铼")
                .dust()
                .color(0x392857)
                .components(Rhenium, 1, Chlorine, 5)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        IsophthaloylBis = material("isophthaloylbis", "间苯二甲酰基二乙基硫脲")
                .fluid()
                .color(0x76677e)
                .components(Carbon, 18, Hydrogen, 26, Nitrogen, 4, Oxygen, 2, Sulfur, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        HexafluorophosphoricAcid = material("hexafluorophosphoric_acid", "单氟磷酸")
                .liquid()
                .color(0xd5d54b)
                .components(Hydrogen, 1, Phosphorus, 1, Fluorine, 6)
                .iconSet(DULL)
                .buildAndRegister();

        Diethylthiourea = material("diethylthiourea", "二乙基硫脲")
                .fluid()
                .color(0x23a687)
                .components(Carbon, 5, Hydrogen, 12, Nitrogen, 2, Sulfur, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        ThionylChloride = material("thionyl_chloride", "氯化亚砜")
                .fluid()
                .color(0xf8f5e0)
                .components(Sulfur, 1, Oxygen, 1, Chlorine, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        PhenylenedioxydiaceticAcid = material("phenylenedioxydiacetic_acid", "亚苯基二氧二乙酸")
                .liquid()
                .color(0x7c4456)
                .components(Carbon, 10, Hydrogen, 10, Oxygen, 6)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumThiocyanate = material("sodium_thiocyanate", "硫氰酸钠")
                .fluid()
                .color(0x7c4456)
                .components(Sodium, 1, Sulfur, 1, Carbon, 1, Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        PhosphorusTrichloride = material("phosphorus_trichloride", "三氯化磷")
                .fluid()
                .color(0xd5d54b)
                .components(Phosphorus, 1, Chlorine, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        AntimonyPentafluoride = material("antimony_pentafluoride", "五氟化锑")
                .fluid()
                .color(0xd5d5bd)
                .components(Antimony, 1, Fluorine, 5)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        AntimonyTrichloride = material("antimony_trichloride", "三氯化锑")
                .dust()
                .color(0xbcbcbc)
                .components(Antimony, 1, Chlorine, 3)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ChargedCaesiumCeriumCobaltIndium = material("charged_caesium_cerium_cobalt_indium", "带电的铯-铈-钴-铟")
                .dust()
                .color(0x4da323)
                .components(Caesium, 1, Cerium, 1, Cobalt, 2, Indium, 10, CosmicComputingMixture, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ActiniumSuperhydride = material("actinium_superhydride", "超氢化锕")
                .plasma()
                .dust()
                .color(0x52b051)
                .components(Actinium, 1, Hydrogen, 12)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CosmicSuperconductor = material("cosmic_superconductor", "寰宇超导液")
                .fluid()
                .color(0x00ffff)
                .components(RheniumHassiumThalliumIsophtaloylbisdiethylthioureaHexaf, 1,
                        ActiniumSuperhydride, 1, ChargedCaesiumCeriumCobaltIndium, 1)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Ethylamine = material("ethylamine", "乙胺")
                .fluid()
                .color(0x5a656d)
                .components(Carbon, 2, Hydrogen, 5, Nitrogen, 1, Hydrogen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        TolueneDiisocyanate = material("toluene_diisocyanate", "甲苯二异氰酸脂")
                .fluid()
                .color(0xacf4bf)
                .components(Carbon, 9, Hydrogen, 6, Nitrogen, 2, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Polyurethane = material("polyurethane", "聚氨基甲酸酯")
                .fluid()
                .color(0xacf4bf)
                .components(Carbon, 17, Hydrogen, 16, Nitrogen, 2, Oxygen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        ViscoelasticPolyurethane = material("viscoelastic_polyurethane", "粘弹性聚氨酯")
                .fluid()
                .color(0xecfbec)
                .components(Polyurethane, 1, EthyleneGlycol, 1, Calcite, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ViscoelasticPolyurethaneFoam = material("viscoelastic_polyurethane_foam", "粘弹性聚氨酯泡沫")
                .fluid()
                .color(0xecfbec)
                .components(ViscoelasticPolyurethane, 1, Air, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Glucose = material("glucose", "葡萄糖")
                .dust()
                .color(0xcacace)
                .components(Carbon, 6, Hydrogen, 12, Oxygen, 6)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        GlucoseIronSolution = material("glucose_iron_solution", "葡萄糖铁溶液")
                .fluid()
                .color(0xc9c9c9)
                .components(Glucose, 1, Iron3Chloride, 1)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GrapheneOxide = material("graphene_oxide", "氧化石墨烯")
                .dust()
                .color(0x535353)
                .components(Carbon, 1, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        GrapheneGelSuspension = material("graphene_gel_suspension", "石墨烯浆料")
                .dust()
                .color(0x535353)
                .components(Carbon, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        DryGrapheneGel = material("dry_graphene_gel", "干石墨烯凝胶")
                .dust()
                .color(0x202079)
                .components(Carbon, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        SupercriticalCarbonDioxide = material("supercritical_carbon_dioxide", "超临界二氧化碳")
                .fluid()
                .color(0x9ac8f3)
                .components(Carbon, 1, Oxygen, 2)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        PotassiumBisulfite = material("potassium_bisulfite", "亚硫酸氢钾")
                .dust()
                .color(0x807d72)
                .components(Potassium, 1, Sulfur, 1, Hydrogen, 1, Oxygen, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        PotassiumHydroxylaminedisulfonate = material("potassium_hydroxylaminedisulfonate", "羟胺二磺酸钾")
                .dust()
                .color(0x4c6226)
                .components(Potassium, 2, Nitrogen, 1, Hydrogen, 1, Sulfur, 2, Oxygen, 7)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        HydroxylammoniumSulfate = material("hydroxylammonium_sulfate", "羟铵硫酸盐")
                .dust()
                .color(0x848075)
                .components(Nitrogen, 2, Hydrogen, 8, Sulfur, 1, Oxygen, 6)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        BariumChloride = material("barium_chloride", "氯化钡")
                .dust()
                .color(0xe9705f)
                .flags(GTOMaterialFlags.GENERATE_TINY_DUST)
                .components(Barium, 1, Chlorine, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        NitrousAcid = material("nitrous_acid", "亚硝酸")
                .fluid()
                .color(0xa0c8fd)
                .components(Hydrogen, 1, Nitrogen, 1, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        ActiniumHydride = material("actinium_hydride", "氢化锕")
                .dust()
                .color(0xb8c6f1)
                .components(Actinium, 1, Hydrogen, 3)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        FilteredSater = material("filtered_water", "过滤水")
                .fluid()
                .components(Hydrogen, 2, Oxygen, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        OzoneWater = material("ozone_water", "臭氧水")
                .fluid()
                .components(Hydrogen, 2, Oxygen, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        FlocculentWater = material("flocculent_water", "絮凝水")
                .fluid()
                .components(Hydrogen, 2, Oxygen, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        PHNeutralWater = material("ph_neutral_water", "pH中和水")
                .fluid()
                .components(Hydrogen, 2, Oxygen, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ExtremeTemperatureWater = material("extreme_temperature_water", "极端温度水")
                .fluid()
                .components(Hydrogen, 2, Oxygen, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ElectricEquilibriumWater = material("electric_equilibrium_water", "电平衡水")
                .fluid()
                .components(Hydrogen, 2, Oxygen, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        DegassedWater = material("degassed_water", "脱气水")
                .fluid()
                .components(Hydrogen, 2, Oxygen, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        BaryonicPerfectionWater = material("baryonic_perfection_water", "重子完美水")
                .fluid()
                .components(Hydrogen, 2, Oxygen, 1)
                .color(0x0058cd)
                .iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Antimatter = material("antimatter", "离散反物质")
                .fluid()
                .color(0x9932cc)
                .iconSet(DULL)
                .buildAndRegister();

        PositiveElectron = material("positive_electron", "反电子")
                .fluid()
                .color(0x59764c)
                .iconSet(DULL)
                .buildAndRegister().setFormula("e+", false);

        Antiproton = material("antiproton", "反质子")
                .fluid()
                .color(0x4945af)
                .iconSet(DULL)
                .buildAndRegister().setFormula("p-", false);

        Antineutron = material("antineutron", "反中子")
                .fluid()
                .color(0xe6e6fa)
                .iconSet(DULL)
                .buildAndRegister().setFormula("n-bar", false);

        Antihydrogen = material("antihydrogen", "反氢")
                .fluid()
                .color(0x6a5acd)
                .iconSet(DULL)
                .buildAndRegister().setFormula("Ah", false);

        Kerosene = material("kerosene", "煤油")
                .fluid()
                .components(Carbon, 12, Hydrogen, 24)
                .color(0xce57ce)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Rp1 = material("rp_1", "RP-1混合燃料")
                .fluid()
                .color(0xff523e)
                .iconSet(DULL)
                .buildAndRegister();

        RocketFuelRp1 = material("rocket_fuel_rp_1", "火箭燃料 RP-1")
                .fluid()
                .color(0xff321b)
                .iconSet(DULL)
                .buildAndRegister();

        Hydrazine = material("hydrazine", "肼")
                .fluid()
                .color(0xffffff)
                .components(Nitrogen, 2, Hydrogen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        DenseHydrazineFuelMixture = material("dense_hydrazine_fuel_mixture", "浓缩肼混合燃料")
                .fluid()
                .color(0x4a223b)
                .iconSet(DULL)
                .buildAndRegister();

        Monomethylhydrazine = material("monomethylhydrazine", "甲肼")
                .fluid()
                .color(0xffffff)
                .components(Carbon, 1, Hydrogen, 6, Nitrogen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        RocketFuelCn3h7o3 = material("rocket_fuel_cn3h7o3", "火箭燃料（硝酸甲肼）")
                .fluid()
                .color(0xa636ac)
                .iconSet(DULL)
                .buildAndRegister();

        RocketFuelH8n4c2o4 = material("rocket_fuel_h8n4c2o4", "火箭燃料（偏二甲肼-四氧化二氮）")
                .fluid()
                .color(0x4aa11b)
                .iconSet(DULL)
                .buildAndRegister();

        StellarEnergyRocketFuel = material("stellar_energy_rocket_fuel", "星能火箭燃料")
                .fluid()
                .color(0xdf362d)
                .iconSet(DULL)
                .buildAndRegister();

        ExplosiveHydrazine = material("explosivehydrazine", "爆炸性肼燃料混合物")
                .fluid()
                .color(0x3b0c5c)
                .iconSet(DULL)
                .buildAndRegister();

        HmxExplosive = material("hmxexplosive", "HMX高能爆炸性化合物")
                .dust()
                .color(0xf3ffdb)
                .iconSet(BRIGHT)
                .buildAndRegister();

        LaNdOxidesSolution = material("la_nd_oxides_solution", "镧-钕氧化物溶液")
                .fluid()
                .color(0x9ce3db)
                .iconSet(DULL)
                .buildAndRegister().setFormula("(La2O3)(Pr2O3)(Nd2O3)(Ce2O3)");

        SmGdOxidesSolution = material("sm_gd_oxides_solution", "钐-钆氧化物溶液")
                .fluid()
                .color(0xffff99)
                .iconSet(DULL)
                .buildAndRegister().setFormula("(Sc2O3)(Eu2O3)(Gd2O3)(Sm2O3)");

        TbHoOxidesSolution = material("tb_ho_oxides_solution", "铽-钬氧化物溶液")
                .fluid()
                .color(0x99ff99)
                .iconSet(DULL)
                .buildAndRegister().setFormula("(Y2O3)(Tb2O3)(Dy2O3)(Ho2O3)");

        ErLuOxidesSolution = material("er_lu_oxides_solution", "铒-镥氧化物溶液")
                .fluid()
                .color(0xffb3ff)
                .iconSet(DULL)
                .buildAndRegister().setFormula("(Er2O3)(Tm2O3)(Yb2O3)(Lu2O3)");

        LanthanumOxide = material("lanthanum_oxide", "氧化镧")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Lanthanum, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        PraseodymiumOxide = material("praseodymium_oxide", "氧化镨")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Praseodymium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        NeodymiumOxide = material("neodymium_oxide", "氧化钕")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Neodymium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CeriumOxide = material("cerium_oxide", "氧化铈")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Cerium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        EuropiumOxide = material("europium_oxide", "氧化铕")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Europium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GadoliniumOxide = material("gadolinium_oxide", "氧化钆")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Gadolinium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SamariumOxide = material("samarium_oxide", "氧化钐")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Samarium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TerbiumOxide = material("terbium_oxide", "氧化铽")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Terbium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        DysprosiumOxide = material("dysprosium_oxide", "氧化镝")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Dysprosium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HolmiumOxide = material("holmium_oxide", "氧化钬")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Holmium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ErbiumOxide = material("erbium_oxide", "氧化铒")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Erbium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ThuliumOxide = material("thulium_oxide", "氧化铥")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Thulium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        YtterbiumOxide = material("ytterbium_oxide", "氧化镱")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Ytterbium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        LutetiumOxide = material("lutetium_oxide", "氧化镥")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Lutetium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ScandiumOxide = material("scandium_oxide", "氧化钪")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Scandium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        YttriumOxide = material("yttrium_oxide", "氧化钇")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Yttrium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ZirconChlorinatingResidue = material("zircon_chlorinating_residue", "锆氯化反应残渣")
                .fluid()
                .color(0x33ca33)
                .components(Silicon, 1, Chlorine, 4)
                .iconSet(DULL)
                .buildAndRegister();

        ZirconiumHafniumChloride = material("zirconium_hafnium_chloride", "锆-铪氯化物")
                .fluid()
                .color(0x33ca33)
                .iconSet(DULL)
                .buildAndRegister().setFormula("ZrHfCl₄", false);

        ZirconiumHafniumOxychloride = material("zirconiu_hafnium_oxychloride", "锆-铪氯氧化物")
                .fluid()
                .color(0x33ca33)
                .iconSet(DULL)
                .buildAndRegister().setFormula("Cl₂HfOZr", false);

        HafniumOxide = material("hafnium_oxide", "二氧化铪")
                .dust()
                .color(0x3c3c3c)
                .components(Hafnium, 1, Oxygen, 2)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ZirconiumOxide = material("zirconium_oxide", "二氧化锆")
                .dust()
                .color(0x3c3c3c)
                .components(Zirconium, 1, Oxygen, 2)
                .iconSet(DULL)
                .buildAndRegister();

        HafniumChloride = material("hafnium_chloride", "四氯化铪")
                .dust()
                .color(0x5c5c69)
                .components(Hafnium, 1, Chlorine, 4)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TelluriumOxide = material("tellurium_oxide", "二氧化碲")
                .dust()
                .color(0xd0d0d0)
                .components(Tellurium, 1, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(GLASS)
                .buildAndRegister();

        SodiumEthylate = material("sodium_ethylate", "乙醇钠")
                .dust()
                .color(0x9bcd9b)
                .components(Carbon, 2, Hydrogen, 5, Oxygen, 1, Sodium, 1)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumEthylxanthate = material("sodium_ethylxanthate", "乙基黄原酸钠")
                .dust()
                .color(0xcdad00)
                .components(Carbon, 3, Hydrogen, 5, Sodium, 1, Oxygen, 1, Sulfur, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        PotassiumEthylxanthate = material("potassium_ethylxanthate", "乙基黄原酸钾")
                .dust()
                .color(0xcdc8b1)
                .components(Carbon, 3, Hydrogen, 5, Potassium, 1, Oxygen, 1, Sulfur, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        PotassiumEthylate = material("potassium_ethylate", "乙醇钾")
                .dust()
                .color(0xcd661d)
                .components(Carbon, 2, Hydrogen, 5, Oxygen, 1, Potassium, 1)
                .iconSet(DULL)
                .buildAndRegister();

        NMPyrolidone = material("nmethylpyrolidone", "N-甲基吡咯烷酮")
                .fluid()
                .color(0xd0d0d0)
                .components(Carbon, 5, Hydrogen, 9, Nitrogen, 1, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        GammaButyrolactone = material("gammabutyrolactone", "1,4-丁内酯")
                .fluid()
                .color(0xcccca1)
                .components(Carbon, 4, Hydrogen, 6, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Butane14Diol = material("butane_1_4_diol", "1,4-丁二醇")
                .fluid()
                .color(0xc4534c)
                .components(Carbon, 4, Hydrogen, 10, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Methylamine = material("methylamine", "甲胺")
                .fluid()
                .color(0x45486f)
                .components(Carbon, 1, Hydrogen, 5, Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        PPhenylenediamine = material("p_phenylenediamine", "对苯二胺")
                .dust()
                .color(0xdccf52)
                .components(Carbon, 6, Hydrogen, 8, Nitrogen, 2)
                .iconSet(DULL)
                .buildAndRegister();

        PNitroaniline = material("p_nitroaniline", "对硝基苯胺")
                .fluid()
                .color(0xcc9037)
                .components(Carbon, 6, Hydrogen, 8, Nitrogen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        TerephthalicAcid = material("terephthalicacid", "对苯二甲酸")
                .fluid()
                .color(0xd6d6d6)
                .components(Carbon, 8, Hydrogen, 6, Hydrogen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        DimethylTerephthalate = material("dimethylterephthalate", "对苯二甲酸二甲酯")
                .fluid()
                .color(0xd1d1d1)
                .components(Carbon, 10, Hydrogen, 10, Hydrogen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        TerephthaloylChloride = material("terephthaloyl_chloride", "对苯二甲酰氯")
                .dust()
                .color(0x00e60b)
                .components(Carbon, 8, Hydrogen, 4, Chlorine, 2, Nitrogen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        Rhugnor = material("rhugnor", "鲁格诺")
                .fluid()
                .color(0xa800e2)
                .element(GTOElements.RHUGNOR)
                .iconSet(METALLIC)
                .buildAndRegister();

        HotSodiumPotassium = material("hot_sodium_potassium", "热钠钾合金")
                .fluid()
                .color(0x64fcb4)
                .components(Sodium, 1, Potassium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(SAND)
                .buildAndRegister();

        SupercriticalSodiumPotassium = material("supercritical_sodium_potassium", "超临界钠钾合金")
                .fluid()
                .color(0x64fcb4)
                .components(Sodium, 1, Potassium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(LAPIS)
                .buildAndRegister();

        Copper76 = material("copper76", "铜-76")
                .dust()
                .element(GTOElements.COPPER76)
                .color(0xe77c56)
                .iconSet(BRIGHT)
                .buildAndRegister();

        CadmiumSulfide = material("cadmium_sulfide", "硫化镉")
                .dust()
                .components(Cadmium, 1, Sulfur, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xd4ba19)
                .iconSet(DULL)
                .buildAndRegister();

        CadmiumTungstate = material("cadmium_tungstate", "钨酸镉")
                .dust()
                .components(Cadmium, 1, Tungsten, 1, Oxygen, 4)
                .color(0x757770)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        BismuthGermanate = material("bismuth_germanate", "锗酸铋")
                .dust()
                .components(Bismuth, 12, Germanium, 1, Oxygen, 20)
                .color(0x4ea839)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(DULL)
                .buildAndRegister();

        BismuthNitrateSolution = material("bismuth_nitrate_solution", "硝酸铋溶液")
                .fluid()
                .components(Water, 1, Bismuth, 1, Nitrogen, 3, Oxygen, 9)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xa4a7a8)
                .iconSet(DULL)
                .buildAndRegister();

        Paa = material("paa", "聚酰胺酸（PAA）")
                .fluid()
                .components(Carbon, 22, Hydrogen, 14, Nitrogen, 2, Oxygen, 7)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xead05e)
                .iconSet(DULL)
                .buildAndRegister();

        SilicaGelBase = material("silica_gel_base", "硅胶基质")
                .fluid()
                .color(0x39967a)
                .iconSet(DULL)
                .buildAndRegister();

        DeglyceratedSoap = material("deglycerated_soap", "脱糖肥皂")
                .fluid()
                .color(0xffb000)
                .iconSet(DULL)
                .buildAndRegister();

        Turpentine = material("turpentine", "松油")
                .fluid()
                .components(Carbon, 10, Hydrogen, 16)
                .color(0x9acd32)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        SteamCrackedTurpentine = material("steam_cracked_turpentine", "蒸汽裂化的松油浸出物")
                .fluid()
                .color(0x8b6914)
                .iconSet(FLUID)
                .buildAndRegister();

        LeachedTurpentine = material("leached_turpentine", "松油浸出物")
                .fluid()
                .components(Carbon, 10, Hydrogen, 16, Concrete, 1)
                .color(0xcd9b1d)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        AlmandineFront = material("almandine_front", "铁铝榴石矿石泡沫")
                .fluid()
                .components(Almandine, 1)
                .color(0xb22222)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        ChalcopyriteFront = material("chalcopyrite_front", "黄铜矿矿石泡沫")
                .fluid()
                .components(Chalcopyrite, 1)
                .color(0xcdaa7d)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        GrossularFront = material("grossular_front", "钙铝榴石矿石泡沫")
                .fluid()
                .components(Grossular, 1)
                .color(0xd2691e)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        MonaziteFront = material("monazite_front", "独居石矿石泡沫")
                .fluid()
                .components(Monazite, 1)
                .color(0x838b83)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        NickelFront = material("nickel_front", "镍矿石泡沫")
                .fluid()
                .components(Nickel, 1)
                .color(0xc1cdcd)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        PlatinumFront = material("platinum_front", "铂矿石泡沫")
                .fluid()
                .components(Platinum, 1)
                .color(0xcdc9a5)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        PyropeFront = material("pyrope_front", "镁铝榴石矿石泡沫")
                .fluid()
                .components(Pyrope, 1)
                .color(0x8b0000)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        RedstoneFront = material("redstone_front", "红石矿石泡沫")
                .fluid()
                .components(Redstone, 1)
                .color(0xee0000)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        SpessartineFront = material("spessartine_front", "锰铝榴石矿石泡沫")
                .fluid()
                .components(Spessartine, 1)
                .color(0xee5c42)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        SphaleriteFront = material("sphalerite_front", "闪锌矿矿石泡沫")
                .fluid()
                .components(Sphalerite, 1)
                .color(0xeee9e9)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        PentlanditeFront = material("pentlandite_front", "镍黄铁矿矿石泡沫")
                .fluid()
                .components(Pentlandite, 1)
                .color(0xcdaa7d)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        EnrichedNaquadahFront = material("enriched_naquadah_front", "富集硅岩矿石泡沫")
                .fluid()
                .components(NaquadahEnriched, 1)
                .color(0x58d00f)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        TriniumCompoundFront = material("trinium_compound_front", "凯金化合物泡沫")
                .fluid()
                .components(TriniumCompound, 1)
                .color(0xbae8e6)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        CarbonDisulfide = material("carbon_disulfide", "二硫化碳")
                .fluid()
                .components(Carbon, 1, Sulfur, 2)
                .color(0x104e8b)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        SpecialCeramics = material("special_ceramics", "特种陶瓷")
                .dust()
                .color(0x5c5909)
                .iconSet(DULL)
                .buildAndRegister();

        HydroiodicAcid = material("hydroiodic_acid", "氢碘酸")
                .fluid()
                .components(Hydrogen, 1, Iodine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x0382e2)
                .iconSet(LIMPID)
                .buildAndRegister();

        Acrylonitrile = material("acrylonitrile", "丙烯腈")
                .fluid()
                .components(Carbon, 3, Hydrogen, 3, Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xa4a4e1)
                .iconSet(LIMPID)
                .buildAndRegister();

        Abs = material("abs", "ABS塑料")
                .polymer()
                .fluid()
                .components(Acrylonitrile, 1, Butadiene, 1, Styrene, 2)
                .color(0xE8E7E5)
                .iconSet(DULL)
                .flags(GENERATE_PLATE, DISABLE_DECOMPOSITION)
                .buildAndRegister();

        LithiumIodide = material("lithium_iodide", "碘化锂")
                .dust()
                .components(Lithium, 1, Iodine, 1)
                .color(0xc10014)
                .iconSet(DULL)
                .buildAndRegister();

        SilicaAluminaGel = material("silica_alumina_gel", "硅铝凝胶")
                .dust()
                .color(0x0c849f)
                .iconSet(DULL)
                .buildAndRegister();

        ZeoliteSievingPellets = material("zeolite_sieving_pellets", "过筛沸石颗粒")
                .dust()
                .color(0x4d3e9f)
                .iconSet(DULL)
                .buildAndRegister();

        WetZeoliteSievingPellets = material("wet_zeolite_sieving_pellets", "湿过筛沸石颗粒")
                .dust()
                .color(0x1d173c)
                .iconSet(DULL)
                .buildAndRegister();

        TertButanol = material("tert_butanol", "叔丁醇")
                .fluid()
                .components(Carbon, 4, Hydrogen, 10, Oxygen, 1)
                .color(0xacb500)
                .iconSet(LIMPID)
                .buildAndRegister();

        DitertbutylDicarbonate = material("ditertbutyl_dicarbonate", "二碳酸二叔丁酯")
                .dust()
                .components(Carbon, 10, Hydrogen, 18, Oxygen, 5)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x7e96b5)
                .iconSet(DULL)
                .buildAndRegister();

        Tertbuthylcarbonylazide = material("tertbuthylcarbonylazide", "叔丁基羰基叠氮")
                .fluid()
                .components(Carbon, 5, Hydrogen, 9, Nitrogen, 3, Oxygen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xacb500)
                .iconSet(LIMPID)
                .buildAndRegister();

        SodiumToluenesulfonate = material("sodium_toluenesulfonate", "甲苯磺酸钠")
                .fluid()
                .components(Carbon, 7, Hydrogen, 7, Sulfur, 3, Oxygen, 3, Sodium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xb5b41d)
                .iconSet(LIMPID)
                .buildAndRegister();

        SodiumAzide = material("sodium_azide", "叠氮化钠")
                .dust()
                .components(Sodium, 1, Nitrogen, 3)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x1018f0)
                .iconSet(DULL)
                .buildAndRegister();

        SodiumAzanide = material("sodium_azanide", "氨基钠")
                .dust()
                .components(Sodium, 1, Nitrogen, 1, Hydrogen, 2)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x2381b3)
                .iconSet(DULL)
                .buildAndRegister();

        NitrogenPentoxide = material("nitrogen_pentoxide", "五氧化二氮")
                .fluid()
                .components(Nitrogen, 2, Oxygen, 5)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x162bb3)
                .iconSet(LIMPID)
                .buildAndRegister();

        AminatedFullerene = material("aminated_fullerene", "胺化富勒烯")
                .fluid()
                .components(Carbon, 60, Hydrogen, 12, Nitrogen, 12)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x3842f0)
                .iconSet(LIMPID)
                .buildAndRegister();

        Azafullerene = material("azafullerene", "氮杂富勒烯")
                .gas()
                .components(Carbon, 60, Hydrogen, 12, Nitrogen, 12)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xb3a500)
                .iconSet(LIMPID)
                .buildAndRegister();

        AbsoluteEthanol = material("absolute_ethanol", "绝对乙醇")
                .fluid()
                .color(0xff4500)
                .components(Carbon, 2, Hydrogen, 6, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(LIMPID)
                .buildAndRegister();

        PiranhaSolution = material("piranha_solution", "食人鱼洗液")
                .fluid()
                .color(0xac2fff)
                .iconSet(LIMPID)
                .buildAndRegister();

        PolyAluminiumChloride = material("poly_aluminium_chloride", "聚合氯化铝")
                .fluid()
                .color(0xf3ffe5)
                .iconSet(DULL)
                .buildAndRegister();

        FlocculationWasteSolution = material("flocculation_waste_solution", "絮凝废液")
                .fluid()
                .color(0xc7cac1)
                .iconSet(DULL)
                .buildAndRegister();

        PotassiumPyrosulfate = material("potassium_pyrosulfate", "焦硫酸钾")
                .dust()
                .components(Potassium, 2, Sulfur, 2, Oxygen, 7)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xff9900).iconSet(METALLIC)
                .buildAndRegister();

        PlatinumSlag = material("platinum_slag", "铂渣")
                .dust()
                .color(0x343318).iconSet(DULL)
                .buildAndRegister().setFormula("IrOsRhRu??", false);

        LeachResidue = material("leach_residue", "铂浸出渣")
                .dust()
                .color(0x644629).iconSet(DULL)
                .buildAndRegister().setFormula("IrOsRhRu?", false);

        ZincSulfate = material("zinc_sulfate", "硫酸锌")
                .dust()
                .components(Zinc, 1, Sulfur, 1, Oxygen, 4)
                .color(0x533c1b).iconSet(SAND)
                .buildAndRegister();

        RhodiumNitrate = material("rhodium_nitrate", "硝酸铑")
                .dust()
                .color(0x8c5a0c).iconSet(SAND)
                .flags(DISABLE_DECOMPOSITION)
                .components(Rhodium, 1, Nitrogen, 1, Oxygen, 3, Concrete, 1)
                .buildAndRegister();

        RoughlyRhodiumMetal = material("roughly_rhodium_metal", "粗制铑金属")
                .dust()
                .color(0x594c1a).iconSet(SAND)
                .buildAndRegister().setFormula("?Rh?", false);

        ReprecipitatedRhodium = material("reprecipitated_rhodium", "再沉淀铑")
                .dust()
                .color(0xd40849).iconSet(SAND)
                .flags(DISABLE_DECOMPOSITION)
                .components(Rhodium, 1, Nitrogen, 1, Hydrogen, 4)
                .buildAndRegister();

        RhodiumSalt = material("rhodium_salt", "铑盐")
                .dust()
                .color(0x61200a).iconSet(SAND)
                .buildAndRegister().setFormula("NaRhCl?", false);

        RhodiumSaltSolution = material("rhodium_salt_solution", "铑盐溶液")
                .fluid()
                .color(0x61200a).iconSet(SAND)
                .buildAndRegister().setFormula("Rh(NaCl)2Cl");

        RhodiumFilterCake = material("rhodium_filter_cake", "铑滤饼")
                .dust()
                .color(0x87350c).iconSet(ROUGH)
                .buildAndRegister().setFormula("?Rh?", false);

        RhodiumFilterCakeSolution = material("rhodium_filter_cake_solution", "铑滤饼溶液")
                .fluid()
                .color(0x87350c).iconSet(ROUGH)
                .buildAndRegister().setFormula("?Rh?", false);

        SodiumRutheniate = material("sodium_rutheniate", "钌酸钠")
                .dust()
                .color(0x282c8c).iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .components(Sodium, 2, Oxygen, 4, Ruthenium, 1)
                .buildAndRegister();

        IridiumDioxide = material("iridium_dioxide", "二氧化铱")
                .dust()
                .color(0xa2bfff).iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .components(Iridium, 1, Oxygen, 2)
                .buildAndRegister();

        RutheniumTetroxideLQ = material("ruthenium_tetroxide_lq", "四氧化钌溶液")
                .fluid()
                .color(0xa8a8a8).iconSet(ROUGH)
                .buildAndRegister();

        SodiumFormate = material("sodium_formate", "甲酸钠")
                .fluid()
                .color(0xf1939c).iconSet(ROUGH)
                .components(Sodium, 1, Carbon, 1, Oxygen, 2, Hydrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        RhodiumSulfateGas = material("rhodium_sulfate_gas", "气态硫酸铑")
                .gas()
                .color(0xbd8743).iconSet(ROUGH)
                .buildAndRegister();

        AcidicIridium = material("acidic_iridium", "酸性铱")
                .gas()
                .color(0x634e3a).iconSet(ROUGH)
                .buildAndRegister().setFormula("???", false);

        RutheniumTetroxideHot = material("ruthenium_tetroxide_hot", "热四氧化钌")
                .gas()
                .color(0x9b9b9b).iconSet(ROUGH)
                .buildAndRegister();

        SodiumSulfate = material("sodium_sulfate", "硫酸钠")
                .dust()
                .components(Sodium, 2, Sulfur, 1, Oxygen, 4)
                .color(0xf9f6cf).iconSet(SAND)
                .buildAndRegister();

        MutatedLivingSolder = material("mutated_living_solder", "突变活性焊料")
                .fluid()
                .color(0xc18fcc).iconSet(METALLIC)
                .radioactiveHazard(1)
                .buildAndRegister();

        SuperMutatedLivingSolder = material("super_mutated_living_solder", "超突变活性焊料")
                .fluid()
                .color(0xb662ff).iconSet(METALLIC)
                .radioactiveHazard(2)
                .buildAndRegister();

        HexafluorideEnrichedNaquadahSolution = material("hexafluoride_enriched_naquadah_solution", "六氟化富集硅岩溶液")
                .fluid()
                .color(0x868d7f)
                .components(NaquadahEnriched, 1, Fluorine, 6)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        XenonHexafluoroEnrichedNaquadate = material("xenon_hexafluoro_enriched_naquadate", "六氟氙酸富集硅岩")
                .fluid()
                .color(0x255a55)
                .components(Xenon, 1, NaquadahEnriched, 1, Fluorine, 6)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GoldTrifluoride = material("gold_trifluoride", "三氟化金")
                .dust()
                .color(0xe8c478)
                .iconSet(BRIGHT)
                .components(Gold, 1, Fluorine, 3)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        XenoauricFluoroantimonicAcid = material("xenoauric_fluoroantimonic_acid", "氟锑酸二氙")
                .fluid()
                .color(0xe0bd74)
                .components(Xenon, 1, Gold, 1, Antimony, 1, Fluorine, 6)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        GoldChloride = material("gold_chloride", "氯化金")
                .fluid()
                .color(0xcccc66)
                .components(Gold, 2, Chlorine, 6)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        BromineTrifluoride = material("bromine_trifluoride", "三氟化溴")
                .fluid()
                .color(0xa88e57)
                .components(Bromine, 1, Fluorine, 3)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HexafluorideNaquadriaSolution = material("hexafluoride_naquadria_solution", "六氟化超能硅岩溶液")
                .fluid()
                .color(0x25c213)
                .components(Naquadria, 1, Fluorine, 6)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        RadonDifluoride = material("radon_difluoride", "二氟化氡")
                .fluid()
                .color(0x8b7eff)
                .components(Radon, 1, Fluorine, 2)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        RadonNaquadriaOctafluoride = material("radon_naquadria_octafluoride", "八氟超能硅岩酸氡")
                .fluid()
                .color(0x85f378)
                .components(Radon, 1, Naquadria, 1, Fluorine, 8)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CaesiumFluoride = material("caesium_fluoride", "氟化铯")
                .fluid()
                .color(0xff7a5f)
                .components(Caesium, 1, Fluorine, 1)
                .buildAndRegister();

        XenonTrioxide = material("xenon_trioxide", "三氧化氙")
                .fluid()
                .color(0x359fc3)
                .components(Xenon, 1, Oxygen, 3)
                .buildAndRegister();

        CaesiumXenontrioxideFluoride = material("caesium_xenontrioxide_fluoride", "二氟三氧氙酸铯")
                .fluid()
                .color(0x5067d7)
                .components(Caesium, 1, Xenon, 1, Oxygen, 3, Fluorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        NaquadriaCaesiumXenonnonfluoride = material("naquadria_caesium_xenonnonfluoride", "九氟氙酸超能硅岩铯")
                .fluid()
                .color(0x54c248)
                .components(Naquadria, 1, Caesium, 1, Xenon, 1, Fluorine, 9)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        RadonTrioxide = material("radon_trioxide", "三氧化氡")
                .fluid()
                .color(0x9a6dd7)
                .components(Radon, 1, Oxygen, 3)
                .buildAndRegister();

        NaquadriaCaesiumfluoride = material("naquadria_caesiumfluoride", "二氟超能硅岩酸铯")
                .fluid()
                .color(0xaaeb69)
                .components(Naquadria, 1, Fluorine, 3, Caesium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister().setFormula("*Nq*F2CsF");

        NitrosoniumOctafluoroxenate = material("nitrosonium_octafluoroxenate", "八氟氙酸亚硝酰")
                .fluid()
                .color(0x953d9f)
                .components(NitrogenDioxide, 2, Xenon, 1, Fluorine, 8)
                .buildAndRegister().setFormula("(NO2)2XeF8");

        NitrylFluoride = material("nitryl_fluoride", "硝酰氟")
                .fluid()
                .color(0x8b7eff)
                .components(Nitrogen, 1, Oxygen, 2, Fluorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AcidicNaquadriaCaesiumfluoride = material("acidic_naquadria_caesiumfluoride", "硫酸二氟超能硅岩酸铯")
                .fluid()
                .color(0x75eb00)
                .components(Naquadria, 1, Fluorine, 3, Caesium, 1, Sulfur, 2, Oxygen, 8)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister().setFormula("*Nq*F2CsF(SO4)2");

        SupercriticalSteam = material("supercritical_steam", "超临界蒸汽")
                .gas(new FluidBuilder().temperature(1000))
                .color(0xc4c4c4)
                .components(Water, 1)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        WaterAgarMix = material("water_agar_mix", "琼脂水溶液")
                .fluid()
                .color(0x88ffc0)
                .buildAndRegister();

        TungstenTrioxide = material("tungsten_trioxide", "三氧化钨")
                .dust()
                .components(Tungsten, 1, Oxygen, 3)
                .color(0x86ff75).iconSet(ROUGH)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CrystallineNitricAcid = material("crystalline_nitric_acid", "结晶硝酸")
                .dust()
                .components(Hydrogen, 1, Nitrogen, 1, Oxygen, 3)
                .color(0xcdbd14)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SodiumChlorate = material("sodium_chlorate", "氯酸钠")
                .dust()
                .components(Sodium, 1, Chlorine, 1, Oxygen, 3)
                .color(0xa5a5a5)
                .buildAndRegister();

        SodiumPerchlorate = material("sodium_perchlorate", "高氯酸钠")
                .dust()
                .components(Sodium, 1, Chlorine, 1, Oxygen, 4)
                .color(0xf0f0f0)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ActiniumTriniumHydroxides = material("actinium_trinium_hydroxides", "氢氧化锕凯金")
                .dust()
                .color(0xad47cf).iconSet(ROUGH)
                .buildAndRegister().setFormula("Ke3Ac2(OH)12");

        SeleniumOxide = material("selenium_oxide", "二氧化硒")
                .dust()
                .components(Selenium, 1, Oxygen, 2)
                .color(0xfff71c).iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TriniumTetrafluoride = material("trinium_tetrafluoride", "四氟化凯金")
                .dust()
                .color(0x529e57)
                .components(Trinium, 1, Fluorine, 4)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Fluorocarborane = material("fluorocarborane", "碳氟化合物")
                .dust()
                .components(Hydrogen, 1, Carbon, 1, Hydrogen, 1, Boron, 11, Fluorine, 11)
                .color(0x00ec80).iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CaesiumNitrate = material("caesium_nitrate", "硝酸铯")
                .dust()
                .components(Caesium, 1, Nitrogen, 1, Oxygen, 3)
                .color(0x821ec7).iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CesiumCarborane = material("cesium_carborane", "碳酸铯")
                .dust()
                .components(Caesium, 1, Carbon, 1, Boron, 11, Hydrogen, 12)
                .color(0xae6eda).iconSet(SAND)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SilverIodide = material("silver_iodide", "碘化银")
                .dust()
                .components(Silver, 1, Iodine, 1)
                .color(0xaca56a).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SilverNitrate = material("silver_nitrate", "硝酸银")
                .dust()
                .components(Silver, 1, Nitrogen, 1, Oxygen, 3)
                .color(0xfffce0).iconSet(BRIGHT)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SilverNitrateSolution = material("silver_nitrate_solution", "硝酸银溶液")
                .fluid()
                .components(SilverNitrate, 1, Water, 1)
                .color(0xfffce0).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        TrifluoroaceticPhosphateEster = material("trifluoroacetic_phosphate_ester", "三氟乙酸对磷脂")
                .dust()
                .components(Carbon, 8, Hydrogen, 5, Fluorine, 3, Oxygen, 2, Sulfur, 1)
                .color(0xa99f45).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        RadiumNitrate = material("radium_nitrate", "硝酸镭")
                .dust()
                .color(0xcd40da).iconSet(SAND)
                .buildAndRegister().setFormula("Ra(NO3)2");

        ActiniumNitrate = material("actinium_nitrate", "硝酸锕")
                .dust()
                .color(0xdae0ee).iconSet(DULL)
                .buildAndRegister().setFormula("Ac(NO3)3");

        PotassiumFluoride = material("potassium_fluoride", "氟化钾")
                .dust()
                .components(Potassium, 1, Fluorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xb9b9b9).iconSet(BRIGHT)
                .buildAndRegister();

        SodiumHydride = material("sodium_hydride", "氢化钠")
                .dust()
                .components(Sodium, 1, Hydrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x757475).iconSet(DULL)
                .buildAndRegister();

        CesiumCarboranePrecursor = material("cesium_carborane_precursor", "铯烷预固化剂")
                .dust()
                .color(0xba5c69).iconSet(DULL)
                .buildAndRegister().setFormula("CsB10H12CN(CH3)3Cl");

        LithiumAluminiumHydride = material("lithium_aluminium_hydride", "氢化铝锂")
                .dust()
                .components(Lithium, 1, Aluminium, 1, Hydrogen, 1)
                .color(0xabd7df).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        LithiumAluminiumFluoride = material("lithium_aluminium_fluoride", "铝-锂氟化物")
                .dust()
                .components(Aluminium, 1, Fluorine, 4, Lithium, 1)
                .color(0xad1f58).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AluminiumTrifluoride = material("aluminium_trifluoride", "氟化铝")
                .dust()
                .components(Aluminium, 1, Fluorine, 3)
                .color(0xb8601a).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SodiumAluminiumHydride = material("sodium_aluminium_hydride", "氢化铝钠")
                .dust()
                .components(Sodium, 1, Aluminium, 1, Hydrogen, 4)
                .color(0x87bfbf).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AluminiumHydride = material("aluminium_hydride", "氢化铝")
                .dust()
                .components(Aluminium, 1, Hydrogen, 3)
                .color(0x315c6e).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Alumina = material("alumina", "氧化铝")
                .dust()
                .components(Aluminium, 2, Oxygen, 3)
                .color(0x1d4759).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CaesiumHydroxide = material("caesium_hydroxide", "氢氧化铯")
                .dust()
                .components(Caesium, 1, Oxygen, 1, Hydrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xd7d7d7).iconSet(DULL)
                .buildAndRegister();

        Decaborane = material("decaborane", "癸硼烷")
                .dust()
                .components(Boron, 10, Hydrogen, 14)
                .color(0x95b78f).iconSet(SAND)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SodiumTetrafluoroborate = material("sodium_tetrafluoroborate", "四氟硼酸钠")
                .dust()
                .components(Sodium, 1, Boron, 1, Fluorine, 4)
                .color(0xa6640f).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SodiumBorohydride = material("sodium_borohydride", "硼氢化钠")
                .dust()
                .components(Sodium, 1, Boron, 1, Hydrogen, 4)
                .color(0x9ab0b2).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        PhosphorusPentasulfide = material("phosphorus_pentasulfide", "五硫化磷")
                .dust()
                .components(Phosphorus, 4, Sulfur, 10)
                .color(0xe7a123).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        StoneDustResidue = material("stone_dust_residue", "石头粉残渣")
                .dust()
                .color(0x585858).iconSet(DULL)
                .buildAndRegister();

        AmmoniumBifluoride = material("ammonium_bifluoride", "二氟化铵")
                .dust()
                .components(Nitrogen, 1, Hydrogen, 4, Hydrogen, 1, Fluorine, 2)
                .color(0x26c6bb).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        UncommonResidues = material("uncommon_residues", "精良残渣")
                .dust()
                .color(0x3f46bd).iconSet(SAND)
                .buildAndRegister();

        PartiallyOxidizedResidues = material("partially_oxidized_residues", "待分离氧化金属残渣")
                .dust()
                .color(0x8f1515).iconSet(SAND)
                .buildAndRegister();

        InertResidues = material("inert_residues", "纯净残渣")
                .dust()
                .color(0x4f4863).iconSet(BRIGHT)
                .buildAndRegister();

        OxidizedResidues = material("oxidized_residues", "氧化残渣")
                .dust()
                .color(0x330d4a).iconSet(SAND)
                .buildAndRegister();

        HeavyOxidizedResidues = material("heavy_oxidized_residues", "重氧化残渣")
                .dust()
                .color(0x310d48).iconSet(SAND)
                .buildAndRegister();

        CleanInertResidues = material("clean_inert_residues", "纯净的惰性残渣")
                .dust()
                .color(0x187f4d).iconSet(BRIGHT)
                .buildAndRegister();

        MetallicResidues = material("metallic_residues", "金属残渣")
                .dust()
                .color(0x205a53).iconSet(SAND)
                .buildAndRegister();

        HeavyMetallicResidues = material("heavy_metallic_residues", "重金属残渣")
                .dust()
                .color(0x1c0986).iconSet(SAND)
                .buildAndRegister();

        DiamagneticResidues = material("diamagnetic_residues", "抗磁性残渣")
                .dust()
                .color(0x30845e).iconSet(SAND)
                .buildAndRegister();

        ParamagneticResidues = material("paramagnetic_residues", "顺磁残渣")
                .dust()
                .color(0x25788b).iconSet(SAND)
                .buildAndRegister();

        FerromagneticResidues = material("ferromagnetic_residues", "铁磁性残渣")
                .dust()
                .color(0x1f5d46).iconSet(SAND)
                .buildAndRegister();

        HeavyDiamagneticResidues = material("heavy_diamagnetic_residues", "重磁残渣")
                .dust()
                .color(0x22316a).iconSet(SAND)
                .buildAndRegister();

        HeavyParamagneticResidues = material("heavy_paramagnetic_residues", "重顺磁残渣")
                .dust()
                .color(0x1a8e2f).iconSet(SAND)
                .buildAndRegister();

        HeavyFerromagneticResidues = material("heavy_ferromagnetic_residues", "重铁磁性残渣")
                .dust()
                .color(0x26743e).iconSet(SAND)
                .buildAndRegister();

        ExoticHeavyResidues = material("exotic_heavy_residues", "重奇异残渣")
                .dust()
                .color(0x3e8496).iconSet(BRIGHT)
                .buildAndRegister();

        FumingNitricAcid = material("fuming_nitric_acid", "发烟硝酸")
                .fluid()
                .components(Hydrogen, 1, Nitrogen, 1, Oxygen, 3)
                .color(0xb46800).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Perfluorobenzene = material("perfluorobenzene", "全氟苯")
                .fluid()
                .components(Carbon, 6, Fluorine, 6)
                .color(0x15752B).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Trimethylsilane = material("trimethylsilane", "三甲基硅烷")
                .fluid()
                .components(Carbon, 3, Hydrogen, 10, Silicon, 1)
                .color(0x7d2fc3).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Trimethylchlorosilane = material("trimethylchlorosilane", "三甲基氯硅烷")
                .fluid()
                .color(0x591399).iconSet(LIMPID)
                .buildAndRegister().setFormula("(CH3)3SiCl");

        NitratedTriniiteCompoundSolution = material("nitrated_triniite_compound_solution", "硝化凯金化合物溶液")
                .fluid()
                .color(0x5e9699).iconSet(LIMPID)
                .buildAndRegister();

        ResidualTriniiteSolution = material("residual_triniite_solution", "残留凯金化合物溶液")
                .fluid()
                .color(0x68b59).iconSet(LIMPID)
                .buildAndRegister();

        ActiniumRadiumHydroxideSolution = material("actinium_radium_hydroxide_solution", "氢氧化锕镭溶液")
                .fluid()
                .color(0xc3e0dc).iconSet(LIMPID)
                .buildAndRegister();

        ActiniumRadiumNitrateSolution = material("actinium_radium_nitrate_solution", "硝酸锕镭溶液")
                .fluid()
                .color(0x89c0b3).iconSet(LIMPID)
                .buildAndRegister();

        HeavilyFluorinatedTriniumSolution = material("heavily_fluorinated_trinium_solution", "重氟化凯金化合物溶液")
                .fluid()
                .color(0x169a33).iconSet(LIMPID)
                .buildAndRegister();

        EthyleneSulfide = material("ethylene_sulfide", "乙硫酮")
                .fluid()
                .components(Carbon, 6, Hydrogen, 6, Sulfur, 1, Oxygen, 1)
                .color(0x868544).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        EthylTrifluoroacetate = material("ethyl_trifluoroacetate", "三氟乙酸乙酯")
                .fluid()
                .components(Carbon, 4, Hydrogen, 5, Fluorine, 3, Oxygen, 2)
                .color(0x93a658).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AcetylChloride = material("acetyl_chloride", "乙酰氯")
                .fluid()
                .components(Carbon, 2, Hydrogen, 3, Oxygen, 1, Chlorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xd1b117)
                .iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        KryptonDifluoride = material("krypton_difluoride", "二氟化氪")
                .fluid()
                .components(Krypton, 1, Fluorine, 2)
                .color(0x3ff12b).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        MoltenCalciumSalts = material("molten_calcium_salts", "熔融钙盐")
                .fluid()
                .color(0x7f478b)
                .iconSet(DULL)
                .buildAndRegister();

        Trimethylamine = material("trimethylamine", "三甲胺")
                .fluid()
                .color(0xcea67d).iconSet(LIMPID)
                .buildAndRegister().setFormula("(CH3)3N");

        BoraneDimethylSulfide = material("borane_dimethyl_sulfide", "硼烷二甲基硫醚")
                .fluid()
                .color(0xcea67d).iconSet(LIMPID)
                .buildAndRegister().setFormula("(BH3)(CH3)2S");

        Tetrahydrofuran = material("tetrahydrofuran", "四氢呋喃")
                .fluid()
                .color(0x86e4ce).iconSet(LIMPID)
                .buildAndRegister().setFormula("(CH2)4O");

        Diborane = material("diborane", "乙硼烷")
                .fluid()
                .color(0xbeebcd).iconSet(LIMPID)
                .buildAndRegister().setFormula("(BH3)2");

        DiethylEther = material("diethyl_ether", "二乙醚")
                .fluid()
                .color(0x9ab0b2).iconSet(LIMPID)
                .buildAndRegister().setFormula("(C2H5)2O");

        BoronTrifluorideAcetate = material("boron_trifluoride_acetate", "三氟化硼乙酸酯")
                .fluid()
                .color(0x991062).iconSet(LIMPID)
                .buildAndRegister().setFormula("(BF3)(C2H5)2O");

        SodiumHexafluoroaluminate = material("sodium_hexafluoroaluminate", "六氟铝酸钠")
                .fluid()
                .components(Sodium, 3, Aluminium, 1, Fluorine, 6)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xa47732).iconSet(LIMPID)
                .buildAndRegister();

        DirtyHexafluorosilicicAcid = material("dirty_hexafluorosilicic_acid", "污浊的六氟硅酸")
                .fluid()
                .color(0xda387d).iconSet(LIMPID)
                .buildAndRegister().setFormula("H2SiF6?");

        DiluteHexafluorosilicicAcid = material("dilute_hexafluorosilicic_acid", "稀六氟硅酸")
                .fluid()
                .color(0x49df68).iconSet(LIMPID)
                .buildAndRegister().setFormula("(H2O)2(H2SiF6)");

        FluorosilicicAcid = material("fluorosilicic_acid", "六氟硅酸")
                .fluid()
                .components(Hydrogen, 2, Silicon, 1, Fluorine, 6)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x49BF61).iconSet(LIMPID)
                .buildAndRegister();

        AmmoniumFluoride = material("ammonium_fluoride", "氟化铵")
                .fluid()
                .components(Nitrogen, 1, Hydrogen, 4, Fluorine, 1)
                .color(0xB80926).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AmmoniumBifluorideSolution = material("ammonium_bifluoride_solution", "氟氢化氨")
                .fluid()
                .color(0xCBC5B7).iconSet(LIMPID)
                .buildAndRegister().setFormula("(H2O)NH4FHF");

        SodiumHydroxideSolution = material("sodium_hydroxide_solution", "氢氧化钠溶液")
                .fluid()
                .color(0x000791).iconSet(LIMPID)
                .buildAndRegister().setFormula("(H2O)NaOH");

        Tempo = material("tempo", "2,2,6,6-四甲基哌啶氧自由基")
                .fluid()
                .color(0xff0000).iconSet(LIMPID)
                .buildAndRegister().setFormula("C9H18NO");

        ChromiumSulfate = material("chromium_sulfate", "硫酸铬")
                .fluid()
                .color(0x1f1f1f).iconSet(LIMPID)
                .buildAndRegister().setFormula("Cr2(SO4)3");

        MercuryAcetate = material("mercury_acetate", "醋酸汞")
                .fluid()
                .color(0xdddd00).iconSet(LIMPID)
                .buildAndRegister().setFormula("Hg(CH3COO)2");

        Tetramethylpiperidine = material("tetramethylpiperidine", "2,2,6,6-四甲基哌啶")
                .fluid()
                .color(0xff00ff).iconSet(LIMPID)
                .buildAndRegister().setFormula("C9H19N");

        Polyvinylpyrrolidone = material("polyvinylpyrrolidone", "聚乙烯吡咯烷酮")
                .fluid()
                .color(0xe0f2cf).iconSet(LIMPID)
                .buildAndRegister().setFormula("(C6H9NO)n");

        AmmoniumBromide = material("ammonium_bromide", "溴化铵")
                .dust()
                .components(Nitrogen, 1, Hydrogen, 4, Bromine, 1)
                .color(0xff7700).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        DitertButyl1MethoxyMethoxyethoxyBenzene = material("ditert_butyl_1_methoxy_methoxyethoxy_benzene", "二叔丁基-1-甲氧基-甲氧基乙氧基苯")
                .fluid()
                .color(0xdeac34).iconSet(LIMPID)
                .buildAndRegister().setFormula("C15H24O3");

        TertButylChloride = material("tert_butyl_chloride", "叔丁基氯")
                .fluid()
                .color(0xf3ecc0).iconSet(LIMPID)
                .buildAndRegister().setFormula("C4H9Cl");
        ParaBenzoquinone = material("para_benzoquinone", "对苯醌")
                .fluid()
                .color(0xcf7ca0).iconSet(LIMPID)
                .buildAndRegister().setFormula("C6H4O2");
        Chloroethoxyethane = material("chloroethoxyethane", "氯乙氧基乙烷")
                .fluid()
                .color(0xfdaac0).iconSet(LIMPID)
                .buildAndRegister().setFormula("C4H9ClO");
        NMethylphthalimide = material("n_methylphthalimide", "N-甲基邻苯二甲酰亚胺")
                .fluid()
                .color(0xff7cc0).iconSet(LIMPID)
                .buildAndRegister().setFormula("C9H7NO2");
        LithiumBisTrifluoromethanesulfonylimide = material("lithium_bis_trifluoromethanesulfonylimide", "双(三氟甲磺酰)亚胺锂")
                .dust()
                .components(Lithium, 1, Nitrogen, 1, Sulfur, 2, Carbon, 2, Fluorine, 6, Oxygen, 4)
                .color(0x0ccfff).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();
        TetraethylammoniumTetrafluoroborate = material("tetraethylammonium_tetrafluoroborate", "四乙基氟硼酸铵")
                .dust()
                .components(Nitrogen, 1, Carbon, 8, Hydrogen, 20, Boron, 1, Fluorine, 4)
                .color(0x0cdddf).iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();
        RedMud = material("red_mud", "赤泥")
                .fluid()
                .color(0x972903).iconSet(LIMPID)
                .buildAndRegister().setFormula("HCl?", false);

        NeutralisedRedMud = material("neutralised_red_mud", "中和赤泥")
                .fluid()
                .color(0x972903).iconSet(LIMPID)
                .buildAndRegister().setFormula("Fe??", false);

        ChlorosulfonicAcid = material("chlorosulfonic_acid", "氯磺酸")
                .fluid()
                .components(Hydrogen, 1, Sulfur, 1, Oxygen, 3, Chlorine, 1)
                .color(0x682f0d).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();
        TrifluoromethanesulfonicAcid = material("trifluoromethanesulfonic_acid", "三氟甲磺酸")
                .fluid()
                .components(Hydrogen, 1, Carbon, 1, Sulfur, 1, Oxygen, 3, Fluorine, 3)
                .color(0x68680d).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();
        PhosphorusPentachloride = material("phosphorus_pentachloride", "五氯化磷")
                .fluid()
                .components(Phosphorus, 1, Chlorine, 5)
                .color(0xbe3f0d).iconSet(FLUID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();
        SodiumAllylsulfonate = material("sodium_allylsulfonate", "烯丙基磺酸钠")
                .fluid()
                .color(0x3f7f0d).iconSet(LIMPID)
                .buildAndRegister().setFormula("C3H5SO3Na");

        PerfluorooctanesulfonicAcid = material("perfluorooctanesulfonic_acid", "全氟辛烷磺酸")
                .fluid()
                .color(0x0d7f7f).iconSet(LIMPID)
                .buildAndRegister().setFormula("C8F17SO3H");

        PolyDopamine = material("poly_dopamine", "聚多巴胺")
                .polymer()
                .fluid()
                .flags(GENERATE_FOIL, GENERATE_PLATE)
                .color(0x0d7f0d).iconSet(DULL)
                .buildAndRegister().setFormula("(C8H11NO2)n");

        Dopamine = material("dopamine", "多巴胺")
                .fluid()
                .color(0x0d7f0d).iconSet(LIMPID)
                .buildAndRegister().setFormula("C8H11NO2");

        Cerium4Sulfate = material("cerium_4_sulfate", "硫酸铈(IV)溶液")
                .fluid()
                .color(0x0d7f7f).iconSet(LIMPID)
                .buildAndRegister().setFormula("Ce(SO4)2");

        SulfonatedPolyAryleneEtherSulfoneRandomCopolymer = material("sulfonated_poly_arylene_ether_sulfone_random_copolymer", "磺化聚芳醚砜随机共聚物")
                .dust()
                .color(0x7f7f7f).iconSet(DULL)
                .buildAndRegister().setFormula("(C18H12O3S)n");

        Dichlorodiphenylsulfone = material("dichlorodiphenylsulfone", "4,4'-二氯二苯砜")
                .fluid()
                .color(0x7acf7f).iconSet(LIMPID)
                .buildAndRegister().setFormula("C12H8Cl2O2S");
        Difluorodiphenylsulfone = material("difluorodiphenylsulfone", "4,4'-二氟二苯砜")
                .fluid()
                .color(0xcf04df).iconSet(LIMPID)
                .buildAndRegister().setFormula("C12H8F2O2S");
        Dihydroxybiphenyl = material("dihydroxybiphenyl", "二羟基联苯")
                .fluid()
                .color(0x7f04df).iconSet(LIMPID)
                .buildAndRegister().setFormula("C12H10O2");
        TetramethylBisphenolA = material("tetramethyl_bisphenol_a", "四甲基双酚A")
                .fluid()
                .color(0x7f04af).iconSet(LIMPID)
                .buildAndRegister().setFormula("C17H20O2");
        DisulfonateDichlorodiphenylsulfone = material("disulfonate_dichlorodiphenylsulfone", "3,3′-二磺酸-4,4′-二氯二苯砜")
                .fluid()
                .color(0x7f7f4f).iconSet(LIMPID)
                .buildAndRegister().setFormula("C12H6Cl2O6S3");
        Dimethylacetamide = material("dimethylacetamide", "二甲基乙酰胺")
                .fluid()
                .color(0x7f7f0f).iconSet(LIMPID)
                .buildAndRegister().setFormula("C4H9NO");
        Tetrachloroethane = material("tetrachloroethane", "1,1,2,2-四氯乙烷")
                .fluid()
                .color(0x3f7f0f).iconSet(LIMPID)
                .buildAndRegister().setFormula("C2H2Cl4");
        BenzoylPeroxide = material("benzoyl_peroxide", "过氧化苯甲酰")
                .dust()
                .color(0x3f7f7f).iconSet(DULL)
                .buildAndRegister().setFormula("C14H10O4");
        BenzoicAnhydride = material("benzoic_anhydride", "苯甲酸酐")
                .fluid()
                .color(0x3f4f7f).iconSet(LIMPID)
                .buildAndRegister().setFormula("C14H10O3");
        BenzoicAcid = material("benzoic_acid", "苯甲酸")
                .fluid()
                .color(0x3f0f7f).iconSet(LIMPID)
                .buildAndRegister().setFormula("C7H6O2");
        BenzenesulfonicAcid = material("benzenesulfonic_acid", "苯磺酸")
                .fluid()
                .color(0x7f0f7f).iconSet(LIMPID)
                .buildAndRegister().setFormula("C6H5SO3H");
        PropyleneCarbonate = material("propylene_carbonate", "碳酸丙烯酯")
                .fluid()
                .color(0xbf0f7f).iconSet(LIMPID)
                .buildAndRegister().setFormula("C4H6O3");
        UltraLowViscosityIonicCarrier = material("ultra_low_viscosity_ionic_carrier", "超低粘度离子载体")
                .liquid(new FluidBuilder().temperature(7000))
                .color(0x3c8d4f).iconSet(LIMPID)
                .buildAndRegister();

        RedSlurry = material("red_slurry", "赤泥浆液")
                .fluid()
                .color(0x982902).iconSet(LIMPID)
                .buildAndRegister().setFormula("TiO2?");

        TrifluoromethanesulfonylChloride = material("trifluoromethanesulfonyl_chloride", "三氟甲磺酰氯")
                .fluid()
                .color(0x68680d).iconSet(LIMPID)
                .buildAndRegister().setFormula("CF3SO2Cl");

        PolousPolyolefinSulfonate = material("polous_polyolefin_sulfonate", "多孔聚烯烃磺酸盐")
                .polymer()
                .flags(GENERATE_FOIL, GENERATE_PLATE)
                .flags(GTOMaterialFlags.GENERATE_MEMBRANE_ELECTRODE)
                .color(0xffff00).iconSet(DULL)
                .buildAndRegister().setFormula("C3H6SO3Na");

        PerfluorosulfonicAcidPolytetrafluoroethyleneCopolymer = material("perfluorosulfonic_acid_polytetrafluoroethylene_copolymer", "全氟磺酸-聚四氟乙烯共聚物")
                .polymer()
                .flags(GENERATE_FOIL, GENERATE_PLATE)
                .flags(GTOMaterialFlags.GENERATE_MEMBRANE_ELECTRODE)
                .color(0xfd0ddf).iconSet(DULL)
                .buildAndRegister().setFormula("(C2F4)n(CF2SO3H)m");

        CeOxPolyDopamineReinforcedPolytetrafluoroethylene = material("ce_ox_poly_dopamine_reinforced_polytetrafluoroethylene", "氧化铈-聚多巴胺增强聚四氟乙烯")
                .polymer()
                .flags(GENERATE_FOIL, GENERATE_PLATE)
                .flags(GTOMaterialFlags.GENERATE_MEMBRANE_ELECTRODE)
                .color(0x0d7f7f).iconSet(DULL)
                .buildAndRegister().setFormula("(C2F4)n(C8H11NO2)mCeOx");

        NanocrackRegulatedSelfHumidifyingCompositeMaterial = material("nanocrack_regulated_self_humidifying_composite_material", "纳米裂纹调节自加湿复合材料")
                .polymer()
                .flags(GENERATE_FOIL, GENERATE_PLATE)
                .flags(GTOMaterialFlags.GENERATE_MEMBRANE_ELECTRODE)
                .color(0x0d7f0d).iconSet(DULL)
                .buildAndRegister().setFormula("(C2F4)n(C8H11NO2)mCeOx?");

        FerricReeChloride = material("ferric_ree_chloride", "含稀土氯化铁")
                .fluid()
                .color(0x68680d).iconSet(LIMPID)
                .buildAndRegister().setFormula("Fe?", false);

        TitanylSulfate = material("titanyl_sulfate", "硫酸钛酯")
                .fluid()
                .color(0xf82296).iconSet(LIMPID)
                .buildAndRegister().setFormula("TiO(SO4)");

        DioxygenDifluoride = material("dioxygen_difluoride", "二氟化二氧")
                .fluid()
                .components(Fluorine, 1, Oxygen, 1, Oxygen, 1, Fluorine, 1)
                .color(0x18bfb9).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        OxidizedResidualSolution = material("oxidized_residual_solution", "氧化残留溶液")
                .fluid()
                .color(0x2cd3cb).iconSet(LIMPID)
                .buildAndRegister();

        HeliumIIIHydride = material("helium_iii_hydride", "氢化氦-3")
                .fluid()
                .color(0x9f9f02).iconSet(LIMPID)
                .buildAndRegister().setFormula("He_3H");

        UltraacidicResidueSolution = material("ultraacidic_residue_solution", "超酸性残渣溶液")
                .fluid()
                .color(0x848c9a).iconSet(LIMPID)
                .buildAndRegister();

        XenicAcid = material("xenic_acid", "氙酸")
                .fluid()
                .components(Hydrogen, 2, Xenon, 1, Oxygen, 4)
                .color(0x443a76).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        DiluteHydrofluoricAcid = material("dilute_hydrofluoric_acid", "稀释氢氟酸")
                .fluid()
                .color(0x049821).iconSet(LIMPID)
                .buildAndRegister().setFormula("(H2O)(HF)");

        TritiumHydride = material("tritium_hydride", "氢化氚")
                .fluid()
                .components(Hydrogen, 1, Tritium, 1)
                .color(0xba0202).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        DustyLiquidHeliumIII = material("dusty_liquid_helium_iii", "污浊的液氦-3")
                .fluid()
                .components(Concrete, 1, Helium3, 1)
                .color(0x774060).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Ozone = material("ozone", "臭氧")
                .fluid()
                .components(Oxygen, 3)
                .color(0x0176c4).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        RareEarthChlorides = material("rare_earth_chlorides", "稀土氯化物")
                .fluid()
                .components(Concrete, 1, Chlorine, 1)
                .color(0xbdb76b).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ChromicAcid = material("chromic_acid", "铬酸")
                .fluid()
                .components(Chromium, 2, Hydrogen, 2, Oxygen, 7)
                .color(0x9a0b0b).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ChromicAcidWaste = material("chromic_acid_waste", "铬酸废液")
                .fluid()
                .components(Chromium, 2, Hydrogen, 2, Oxygen, 7)
                .color(0xca4b4b).iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Fluorite = material("fluorite", "氟石")
                .fluid()
                .dust()
                .components(Calcium, 1, Fluorine, 2)
                .color(0x18b400).iconSet(DULL)
                .buildAndRegister();

        // wanggugu'sLanthanidetreatment
        SamariumRefinedPowder = material("samarium_refined", "钐精")
                .dust()
                .color(0x8a6d7d)
                .iconSet(BRIGHT)
                .buildAndRegister().setFormula("??Sm??", false);

        SamariumRrareEearthTurbidLiquid = material("samarium_rare_earth_turbid_liquid", "钐稀土浊液")
                .fluid()
                .color(0xcfc883)
                .iconSet(LIMPID)
                .buildAndRegister();

        MonaziteRareEarthTurbidLiquid = material("monazite_rare_earth_turbid_liquid", "独居石稀土混浊液")
                .fluid()
                .color(0x81624c)
                .iconSet(LIMPID)
                .buildAndRegister();

        ThoritePowder = material("thorite", "方钍石")
                .dust()
                .components(Thorium, 1, Silicon, 1, Oxygen, 4)
                .color(0x7d7d7d)
                .iconSet(SAND)
                .buildAndRegister();

        DilutedMonaziteSlurry = material("diluted_monazite_slurry", "稀释独居石稀土泥浆")
                .fluid()
                .color(0xbdbdbd)
                .iconSet(LIMPID)
                .buildAndRegister();

        RedZirconPowder = material("red_zircon", "红锆石")
                .dust()
                .components(Zirconium, 1, Silicon, 1, Oxygen, 4)
                .color(0xff4500)
                .iconSet(SAND)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AcidicMonazitePowder = material("acidic_monazite", "酸性独居石")
                .dust()
                .color(0x9acd32)
                .iconSet(SAND)
                .buildAndRegister();

        ThoriumPhosphateRefinedPowder = material("thorium_phosphate_refined", "磷酸钍精")
                .dust()
                .color(0x545454)
                .iconSet(SAND)
                .buildAndRegister().setFormula("??ThP??", false);

        UraniumFilterResiduePowder = material("uranium_filter_residue", "铀滤渣")
                .dust()
                .color(0x545454)
                .iconSet(SAND)
                .buildAndRegister();

        ConcentratedMonaziteRareEarthHydroxidePowder = material("concentrated_monazite_rare_earth_hydroxide", "浓缩独居石稀土氢氧化物")
                .dust()
                .color(0xc0c0c0)
                .iconSet(SAND)
                .buildAndRegister();

        CeriumRichMixturePowder = material("cerium_rich_mixture", "富铈混合物")
                .dust()
                .color(0x808080)
                .iconSet(SAND)
                .buildAndRegister().setFormula("??Ce??", false);

        CeriumChloridePowder = material("cerium_chloride", "氯化铈")
                .dust()
                .components(Cerium, 1, Chlorine, 3)
                .color(0x808080)
                .iconSet(SAND)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        OxalicAcid = material("oxalic_acid", "草酸")
                .liquid()
                .components(Carbon, 2, Hydrogen, 2, Oxygen, 4)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xa0ffa0)
                .iconSet(LIMPID)
                .buildAndRegister();

        VanadiumPentoxidePowder = material("vanadium_pentoxide", "五氧化二钒")
                .dust()
                .components(Vanadium, 2, Oxygen, 5)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x8b0000)
                .iconSet(SAND)
                .buildAndRegister();

        CeriumOxalatePowder = material("cerium_oxalate", "草酸铈")
                .dust()
                .components(Cerium, 1, Carbon, 2, Hydrogen, 2, Oxygen, 4)
                .color(0x969696)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(SAND)
                .buildAndRegister();

        ConcentratedCeriumChlorideSolution = material("concentrated_cerium_chloride_solution", "氯化铈浓缩液")
                .fluid()
                .components(Cerium, 1, Chlorine, 3)
                .flags(DISABLE_DECOMPOSITION)
                .color(0x00ffff)
                .iconSet(LIMPID)
                .buildAndRegister();

        NitricLeachateFromMonazite = material("nitric_leachate_from_monazite", "硝酸独居石浸出混合物")
                .fluid()
                .color(0xadd8e6)
                .iconSet(LIMPID)
                .buildAndRegister();

        MonaziteRareEarthPrecipitatePowder = material("monazite_rare_earth_precipitate", "独居石罕土沉淀")
                .dust()
                .color(0x808080)
                .iconSet(SAND)
                .buildAndRegister();

        SaturatedMonaziteRareEarthPowder = material("saturated_monazite_rare_earth", "饱和独居石稀土")
                .dust()
                .color(0xffffff)
                .iconSet(SAND)
                .buildAndRegister();

        SamariumPrecipitatePowder = material("samarium_precipitate", "钐沉淀")
                .dust()
                .components(Samarium, 2, Gadolinium, 1)
                .color(0x969696)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(SAND)
                .buildAndRegister();

        ConcentratedRareEarthChlorideSolution = material("concentrated_rare_earth_chloride_solution", "氯化稀土浓缩液")
                .fluid()
                .color(0x00bfff)
                .iconSet(LIMPID)
                .buildAndRegister();

        EnrichedRareEarthChlorideSolution = material("enriched_rare_earth_chloride_solution", "氯化稀土富集液")
                .fluid()
                .color(0x87cefa)
                .iconSet(LIMPID)
                .buildAndRegister();

        DilutedRareEarthChlorideSolution = material("diluted_rare_earth_chloride_solution", "氯化稀土稀释液")
                .fluid()
                .color(0xadd8e6)
                .iconSet(LIMPID)
                .buildAndRegister();

        SamariumRareEarthSlurry = material("samarium_rare_earth_slurry", "钐稀土泥浆")
                .fluid()
                .color(0x808080)
                .iconSet(LIMPID)
                .buildAndRegister();

        NeodymiumRareEarthConcentratePowder = material("neodymium_rare_earth_concentrate", "钕稀土精")
                .dust()
                .color(0x969696)
                .iconSet(SAND)
                .buildAndRegister();

        SamariumRareEarthDilutedSolution = material("samarium_rare_earth_diluted_solution", "钐稀土稀释液")
                .fluid()
                .components(Samarium, 1, Chlorine, 1, Water, 2)
                .color(0xa9a9a9)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(LIMPID)
                .buildAndRegister();

        SamariumOxalateWithImpurities = material("samarium_oxalate_with_impurities", "含杂草酸钐")
                .dust()
                .components(Samarium, 1, Carbon, 2, Oxygen, 6)
                .color(0x808080)
                .iconSet(SAND)
                .buildAndRegister();

        SamariumChlorideWithImpurities = material("samarium_chloride_with_impurities", "含杂氯化钐")
                .dust()
                .fluid()
                .components(Samarium, 1, Chlorine, 3)
                .color(0xb0b0b0)
                .iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SamariumChlorideSodiumChlorideMixturePowder = material("samarium_chloride_sodium_chloride_mixture", "氯化钐-氯化钠混合物")
                .dust()
                .components(Samarium, 1, Chlorine, 3, Sodium, 1, Chlorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xc0c0c0)
                .iconSet(SAND)
                .buildAndRegister();

        PhosphorusFreeSamariumConcentratePowder = material("phosphorus_free_samarium_concentrate", "脱磷钐精")
                .dust()
                .components(Samarium, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xd3d3d3)
                .iconSet(SAND)
                .buildAndRegister();

        SamariumChlorideConcentrateSolution = material("samarium_chloride_concentrate_solution", "氯化钐浓缩液")
                .fluid()
                .components(Samarium, 1, Chlorine, 3, Water, 5)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xb0c4de)
                .iconSet(LIMPID)
                .buildAndRegister();

        LanthanumChloride = material("lanthanum_chloride", "氯化镧")
                .dust()
                .components(Lanthanum, 1, Chlorine, 3)
                .color(0xffffff)
                .iconSet(SAND)
                .buildAndRegister();

        LanthanumChlorideWithImpurities = material("lanthanum_chloride_with_impurities", "含杂氯化镧")
                .dust()
                .color(0xe0e0e0)
                .iconSet(SAND)
                .buildAndRegister();

        FluoroCarbonLanthanideCeriumSolution = material("fluoro_carbon_lanthanide_cerium_solution", "氟碳镧铈稀土浊溶液")
                .fluid()
                .color(0x3888d2)
                .iconSet(LIMPID)
                .buildAndRegister();

        SteamCrackedFluoroCarbonLanthanideSlurry = material("steam_cracked_fluoro_carbon_lanthanide_slurry", "蒸汽裂化氟碳镧铈泥浆")
                .fluid()
                .color(0x7f7fff)
                .iconSet(LIMPID)
                .buildAndRegister();

        ModulatedFluoroCarbonLanthanideSlurry = material("modulated_fluoro_carbon_lanthanide_slurry", "调制氟碳镧铈泥浆")
                .fluid()
                .color(0x5a5aff)
                .iconSet(LIMPID)
                .buildAndRegister();

        DilutedFluoroCarbonLanthanideSlurry = material("diluted_fluoro_carbon_lanthanide_slurry", "稀释氟碳镧铈泥浆")
                .fluid()
                .color(0x9696ff)
                .iconSet(LIMPID)
                .buildAndRegister();

        FluoroCarbonLanthanideCeriumOxidePowder = material("fluoro_carbon_lanthanide_cerium_oxide", "氟碳镧铈稀土氧化物")
                .dust()
                .color(0x7f7f7f)
                .iconSet(BRIGHT)
                .buildAndRegister();

        FluoroCarbonLanthanideCeriumRareEarthSuspension = material("fluoro_carbon_lanthanide_cerium_rare_earth_suspension", "氟碳镧铈罕土氧化物悬浊液")
                .fluid()
                .color(0xbebebe)
                .iconSet(LIMPID)
                .buildAndRegister();

        SamariumRareEarthConcentratePowder = material("samarium_rare_earth_concentrate", "钐稀土精")
                .dust()
                .color(0x808080)
                .iconSet(BRIGHT)
                .buildAndRegister();

        FluorinatedSamariumConcentratePowder = material("fluorinated_samarium_concentrate", "氟化钐精")
                .dust()
                .color(0x969696)
                .iconSet(BRIGHT)
                .buildAndRegister();

        SamariumTerbiumMixturePowder = material("samarium_terbium_mixture", "钐-铽混合物")
                .dust()
                .color(0x969696)
                .iconSet(BRIGHT)
                .buildAndRegister();

        NitridedSamariumTerbiumMixturePowder = material("nitrided_samarium_terbium_mixture", "氮化钐-铽混合物")
                .dust()
                .color(0x7f7f7f)
                .iconSet(BRIGHT)
                .buildAndRegister();

        TerbiumNitratePowder = material("terbium_nitrate", "硝酸铽")
                .dust()
                .color(0x0080ff)
                .components(Terbium, 1, Nitrogen, 1, Oxygen, 3)
                .iconSet(BRIGHT)
                .buildAndRegister();

        PromethiumOxide = material("promethium_oxide", "氧化钷")
                .dust()
                .color(0xcfcfcf)
                .flags(GTOMaterialFlags.GENERATE_SMALL_DUST)
                .components(Promethium, 2, Oxygen, 3)
                .iconSet(GLASS)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CarbonTetrachloride = material("carbon_tetrachloride", "四氯化碳")
                .fluid()
                .color(0x0e9000)
                .components(Carbon, 1, Chlorine, 4)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(LIMPID)
                .buildAndRegister();

        ActiniumOxalate = material("actinium_oxalate", "草酸锕")
                .dust()
                .color(0xcfcfcf)
                .iconSet(GLASS)
                .buildAndRegister().setFormula("Ac(CO₂)₄", false);

        EthylHexanol = material("ethyl_hexanol", "2-乙基己醇")
                .fluid()
                .components(Carbon, 8, Hydrogen, 18, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .color(0xffffcc)
                .iconSet(LIMPID)
                .buildAndRegister();

        P507 = material("p507", "P-507（2-乙基己基膦酸单-2-乙基己基酯）")
                .fluid()
                .components(Carbon, 18, Hydrogen, 36, Phosphorus, 1, Oxygen, 3)
                .color(0xffff00)
                .iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        PhenolicResin = material("phenolic_resin", "酚醛树脂")
                .fluid()
                .color(0x7F4503)
                .iconSet(DULL)
                .buildAndRegister();

        OsmiumOxideMetal = material("osmium_oxide_metal", "氧化锇金属")
                .dust()
                .color(0x4E748F)
                .iconSet(LIGNITE)
                .buildAndRegister().setFormula("OsO4?");

        CoolantLiquid = material("coolant_liquid", "冷却液")
                .fluid()
                .color(0x4B59FF)
                .iconSet(LIMPID)
                .buildAndRegister();

        CoalSlurry = material("coal_slurry", "煤浆")
                .fluid()
                .color(0x3E3C3A)
                .iconSet(DULL)
                .buildAndRegister();

        WaterGas = material("water_gas", "水煤气")
                .gas()
                .color(0x5D5B57)
                .iconSet(DULL)
                .buildAndRegister();

        LowPurityNaquadahEmulsion = material("low_purity_naquadah_emulsion", "低纯硅岩乳液")
                .fluid()
                .color(0x363635)
                .iconSet(DULL)
                .buildAndRegister();

        LowPurityNaquadahSolution = material("low_purity_naquadah_solution", "低纯硅岩溶液")
                .fluid()
                .color(0x374939)
                .iconSet(DULL)
                .buildAndRegister();

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
                .temp(2700)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC)
                .buildAndRegister().setFormula("Al2O3");

        BariumTitanateCeramic = material("barium_titanate_ceramic", "钛酸钡陶瓷")
                .dust()
                .color(0xffcbe2)
                .iconSet(BRIGHT)
                .temp(3600)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC)
                .buildAndRegister().setFormula("BaTiO3");

        TungstenTetraborideCeramics = material("tungsten_tetraboride_ceramics", "四硼化钨陶瓷")
                .dust()
                .color(0xcee55a).secondaryColor(0x9cbf38)
                .iconSet(BRIGHT)
                .temp(4500)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC, GTOMaterialFlags.GENERATE_MXene)
                .buildAndRegister().setFormula("WB4");

        SilicaCeramic = material("silica_ceramic", "氧化硅岩陶瓷")
                .dust()
                .color(0x393941)
                .iconSet(BRIGHT)
                .temp(5400)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC)
                .buildAndRegister().setFormula("SiO2");

        HydroxyapatiteCeramic = material("hydroxyapatite_ceramic", "羟基磷灰石陶瓷")
                .dust()
                .color(0x76c1da)
                .iconSet(BRIGHT)
                .temp(7200)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC)
                .buildAndRegister().setFormula("Ca5(PO4)3(OH)");

        TellurateCeramics = material("tellurate_ceramics", "亚碲酸盐陶瓷")
                .dust()
                .color(0xffea9e)
                .iconSet(BRIGHT)
                .temp(9000)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC)
                .buildAndRegister().setFormula("TeO3");

        ThuliumHexaborideCeramics = material("thulium_hexaboride_ceramics", "六硼化铥陶瓷")
                .dust()
                .color(0x6cdcff).secondaryColor(0x3ab1ff)
                .iconSet(BRIGHT)
                .temp(12600)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC, GTOMaterialFlags.GENERATE_MXene)
                .buildAndRegister().setFormula("TmB6");

        SiliconNitrideCeramic = material("silicon_nitride_ceramic", "氮化硅陶瓷")
                .dust()
                .color(0x29c824).secondaryColor(0x1b7a1b)
                .temp(5400)
                .iconSet(BRIGHT)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC, GTOMaterialFlags.GENERATE_MXene)
                .buildAndRegister().setFormula("Si3N4");

        Titanium3Carbide = material("titanium3_carbide_ceramic", "碳化钛(III)陶瓷")
                .dust()
                .color(0x6b423b).secondaryColor(0x4e2f2a)
                .iconSet(BRIGHT)
                .temp(5400)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC, GTOMaterialFlags.GENERATE_MXene)
                .buildAndRegister().setFormula("Ti3C2");

        CobaltOxideCeramic = material("cobalt_oxide_ceramic", "氧化钴陶瓷")
                .dust()
                .color(0x222534)
                .iconSet(BRIGHT)
                .temp(1800)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC)
                .buildAndRegister().setFormula("CoO");

        CalciumOxideCeramic = material("calcium_oxide_ceramic", "氧化钙陶瓷")
                .dust()
                .color(0xf1f3ff)
                .iconSet(BRIGHT)
                .temp(2700)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC)
                .buildAndRegister().setFormula("CaO");

        LithiumOxideCeramics = material("lithium_oxide_ceramics", "氧化锂陶瓷")
                .dust()
                .color(0xf3fff3)
                .iconSet(BRIGHT)
                .temp(3300)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC)
                .buildAndRegister().setFormula("Li2O");

        MagnesiumOxideCeramic = material("magnesium_oxide_ceramic", "氧化镁陶瓷")
                .dust()
                .color(0xffe198)
                .iconSet(BRIGHT)
                .temp(3400)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC)
                .buildAndRegister().setFormula("MgO");

        TitaniumNitrideCeramic = material("titanium_nitride_ceramic", "氮化钛陶瓷")
                .dust()
                .color(0xd4ac4b).secondaryColor(0x9c7a34)
                .iconSet(BRIGHT)
                .temp(3600)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC, GTOMaterialFlags.GENERATE_MXene)
                .buildAndRegister().setFormula("TiN");

        BoronCarbideCeramics = material("boron_carbide_ceramics", "碳化硼陶瓷")
                .dust()
                .color(0x2b2b31)
                .iconSet(BRIGHT)
                .temp(4500)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC)
                .buildAndRegister().setFormula("B4C");

        StrontiumCarbonateCeramic = material("strontium_carbonate_ceramic", "碳酸锶陶瓷")
                .dust()
                .color(0xc5c5c5)
                .temp(5400)
                .iconSet(BRIGHT)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC)
                .buildAndRegister().setFormula("SrCO3");

        ZirconiaCeramic = material("zirconia_ceramic", "氧化锆陶瓷")
                .dust()
                .color(0xeaeaea)
                .temp(7200)
                .iconSet(BRIGHT)
                .flags(GTOMaterialFlags.GENERATE_CERAMIC)
                .buildAndRegister().setFormula("ZrO2");

        TricalciumPhosphateCeramic = material("tricalcium_phosphate_ceramic", "磷酸三钙陶瓷")
                .dust()
                .color(0xe9f4f4)
                .iconSet(BRIGHT)
                .temp(9000)
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

        Trichlorosilane = material("trichlorosilane", "三氯硅烷")
                .fluid()
                .color(0xb7b7b7)
                .components(Hydrogen, 1, Silicon, 1, Chlorine, 3)
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
                .components(Hydrogen, 4, Silicon, 1)
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
                .components(Hydrogen, 1, Silicon, 1, Chlorine, 3)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        PureSilane = material("pure_silane", "高纯硅烷")
                .gas()
                .color(0xb5b5b5)
                .components(Silicon, 1, Hydrogen, 4)
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

        Tetrafluorosilane = material("tetrafluorosilane", "四氟硅烷")
                .gas()
                .color(0xf6f7fb)
                .components(Silicon, 1, Fluorine, 4)
                .iconSet(DULL)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister().setFormula("SiF4");

        HighPuritySilica = material("high_purity_silica", "高纯度二氧化硅")
                .dust()
                .color(0xffffff)
                .iconSet(METALLIC)
                .components(Silicon, 1, Oxygen, 2)
                .iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister().setFormula("SiO2");

        CarbonTetrafluoride = material("carbon_tetrafluoride", "四氟化碳")
                .gas()
                .color(0xffffff)
                .components(Carbon, 1, Fluorine, 4)
                .iconSet(LIMPID)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AlgaeExtract = material("algae_extract", "藻类提取物")
                .dust()
                .color(0xA3CA00)
                .iconSet(DULL)
                .buildAndRegister();

        LightEmittingChargedSuspiciousWaste = material("light_emitting_charged_suspicious_waste", "发光充能可疑废料")
                .dust()
                .color(0x39ccF4)
                .iconSet(DULL)
                .buildAndRegister();

        EnrichedLightEmittingChargedSuspiciousWaste = material("enriched_light_emitting_charged_suspicious_waste", "富集发光充能可疑废料")
                .dust()
                .color(0x79EcF4)
                .iconSet(DULL)
                .buildAndRegister();

        CopperNitrate = material("copper_nitrate", "硝酸铜")
                .dust()
                .color(0x8EFAF4)
                .iconSet(DULL)
                .buildAndRegister().setFormula("Cu(NO3)2");

        BariumNitrate = material("barium_nitrate", "硝酸钡")
                .dust()
                .color(0xE464D6)
                .iconSet(DULL)
                .buildAndRegister().setFormula("Ba(NO3)2");

        YttriumNitrate = material("yttrium_nitrate", "硝酸钇")
                .dust()
                .color(0xAAC6E4)
                .iconSet(DULL)
                .buildAndRegister().setFormula("Y(NO3)3");

        CitricAcid = material("citric_acid", "柠檬酸")
                .liquid()
                .color(0xFADD0C)
                .iconSet(DULL)
                .buildAndRegister().setFormula("C6H8O7");

        WellMixedYbcOxides = material("well_mixed_ybc_oxides", "精致钇-钡-铜氧化混合物")
                .dust()
                .color(0x283301)
                .iconSet(DULL)
                .buildAndRegister().setFormula("YBa2Cu3O6");
    }
}
