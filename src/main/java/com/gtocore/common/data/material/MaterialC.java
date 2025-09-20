package com.gtocore.common.data.material;

import com.gtocore.api.data.material.GTOMaterialFlags;
import com.gtocore.common.data.GTOFluidStorageKey;

import com.gregtechceu.gtceu.api.fluids.FluidBuilder;
import com.gregtechceu.gtceu.api.fluids.FluidState;

import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.DISABLE_DECOMPOSITION;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.GENERATE_FOIL;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.api.data.material.GTOMaterialIconSet.LIMPID;
import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtolib.utils.register.MaterialsRegisterUtils.material;

public final class MaterialC {

    public static void init() {
        Ethylenedioxythiophene = material("ethylenedioxythiophene", "EDOT", "乙撑二氧噻吩")
                .fluid()
                .color(0x57acad)
                .iconSet(LIMPID)
                .buildAndRegister().setFormula("C₂H₄O₂C₄H₂S(C₆H₆O₂S)", false);

        Dietoxythiophene = material("dietoxythiophene", "Dietoxythiophene", "二乙氧基噻吩")
                .fluid()
                .color(0x67ff6a)
                .iconSet(LIMPID)
                .buildAndRegister().setFormula("C₄H₂(OC₂H₅)₂S", false);

        Perbromothiophene = material("perbromothiophene", "Perbromothiophene", "二溴噻吩")
                .fluid()
                .color(0x87B657)
                .iconSet(LIMPID)
                .buildAndRegister().setFormula("C₄Br₄S", false);

        IronSulfate = material("iron_sulfate", "硫酸亚铁")
                .dust()
                .color(0x8EB64F)
                .flags(GTOMaterialFlags.GENERATE_CATALYST)
                .iconSet(DULL)
                .buildAndRegister().setFormula("FeSO4");

        LiquidHydrogen = material("liquid_hydrogen", "液态氢")
                .liquid(new FluidBuilder().temperature(20))
                .color(0x4fc4a2)
                .components(Hydrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        HighPressureHydrogen = material("high_pressure_hydrogen", "高压氢气")
                .gas()
                .color(0x00009D)
                .iconSet(DULL)
                .components(Hydrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HighPressureOxygen = material("high_pressure_oxygen", "高压氧气")
                .gas()
                .color(0x3FA2D4)
                .iconSet(DULL)
                .components(Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        LiquidNitrogen = material("liquid_nitrogen", "液态氮")
                .liquid(new FluidBuilder().temperature(77))
                .color(0x057A99)
                .iconSet(FLUID)
                .components(Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HighPressureNitrogen = material("high_pressure_nitrogen", "高压氮气")
                .gas()
                .color(0x139099)
                .iconSet(DULL)
                .components(Nitrogen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HighPressureSteam = material("high_pressure_steam", "高压蒸汽")
                .gas()
                .color(0xEEF2E9)
                .iconSet(DULL)
                .components(Hydrogen, 2, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AcidicBromineSolution = material("acidic_bromine_solution", "酸性溴溶液")
                .liquid()
                .color(0xc49b52)
                .components(Chlorine, 1, Bromine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        ConcentratedBromineSolution = material("concentrated_bromine_solution", "浓缩溴溶液")
                .liquid()
                .color(0x91481e)
                .components(Bromine, 2, Chlorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HydrogenIodide = material("hydrogen_iodide", "碘化氢")
                .gas()
                .color(0x8187a6)
                .components(Hydrogen, 1, Iodine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HotBrine = material("hot_brine", "热卤水")
                .liquid(320)
                .color(0xbe6026)
                .buildAndRegister();

        HotChlorinatedBrominatedBrine = material("hot_chlorinated_brominated_brine", "热氯化溴化盐水")
                .liquid(320)
                .color(0xab765d)
                .components(HotBrine, 1, Chlorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        HotDebrominatedBrine = material("hot_debrominated_brine", "热脱溴盐水")
                .liquid(320)
                .color(0xab896d)
                .buildAndRegister();

        HotAlkalineDebrominatedBrine = material("hot_alkaline_debrominated_brine", "热碱性脱溴盐水")
                .liquid(320)
                .color(0xbe8938)
                .components(HotDebrominatedBrine, 2, Chlorine, 1)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        RawBrine = material("raw_brine", "粗盐水")
                .liquid()
                .color(0x9f6b26)
                .buildAndRegister();

        DebrominatedBrine = material("debrominated_brine", "脱溴化盐水")
                .liquid()
                .color(0xab8c6d)
                .buildAndRegister();

        BrominatedChlorineVapor = material("brominated_chlorine_vapor", "溴化氯气")
                .gas()
                .color(0xbb9b72)
                .components(Chlorine, 1, Bromine, 1, Steam, 1)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        AcidicBromineExhaust = material("acidic_bromine_exhaust", "酸性溴废气")
                .gas()
                .color(0x8f681e)
                .components(Steam, 3, Chlorine, 1)
                .buildAndRegister();

        Blood = material("blood", "血")
                .fluid()
                .color(0xA2000A)
                .iconSet(DULL)
                .buildAndRegister().setFormula("blood");

        BloodCells = material("blood_cells", "血细胞")
                .fluid()
                .color(0xAD1B00)
                .iconSet(DULL)
                .buildAndRegister().setFormula("???");

        BloodPlasma = material("blood_plasma", "血浆")
                .fluid()
                .color(0xA85B00)
                .iconSet(DULL)
                .buildAndRegister().setFormula("???");

        BacterialGrowthMedium = material("bacterial_growth_medium", "细菌生长培养基")
                .fluid()
                .color(0x004401)
                .iconSet(DULL)
                .buildAndRegister();

        AnimalCells = material("animal_cells", "动物细胞")
                .fluid()
                .color(0x8C8C43)
                .iconSet(DULL)
                .buildAndRegister().setFormula("???");

        RapidlyReplicatingAnimalCells = material("rapidly_replicating_animal_cells", "快速增殖动物细胞")
                .fluid()
                .color(0x8C104B)
                .iconSet(DULL)
                .buildAndRegister().setFormula("§k???");

        MycGene = material("myc_gene", "MYC Gene", "MYC 基因")
                .fluid()
                .color(0x055717)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?");

        Oct4Gene = material("oct_4_gene", "OCT-4-Gene", "OCT-4基因")
                .fluid()
                .color(0x425C0A)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?");

        Sox2Gene = material("sox_2_gene", "SOX-2-Gene", "SOX-2基因")
                .fluid()
                .color(0x2D6618)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?");

        Kfl4Gene = material("kfl_4_gene", "KFL-4-Gene", "KFL-4基因")
                .fluid()
                .color(0x316630)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?");

        Cas9Protein = material("cas_9_protein", "CAS-9-Protein", "CAS-9-蛋白")
                .fluid()
                .color(0x50664E)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?");

        PluripotencyInductionGenePlasmids = material("pluripotency_induction_gene_plasmids", "多能性诱导基因质粒")
                .fluid()
                .color(0x569907)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?");

        Chitosan = material("chitosan", "壳聚糖")
                .fluid()
                .color(0x96990E)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?");

        Chitin = material("chitin", "几丁质")
                .fluid()
                .color(0x998422)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?");

        PluripotencyInductionGeneTherapyFluid = material("pluripotency_induction_gene_therapy_fluid", "多能性诱导基因治疗液")
                .fluid()
                .color(0x800C4F)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?");

        Shewanella = material("shewanella", "希瓦氏菌")
                .dust()
                .color(0x800C4F)
                .iconSet(DULL)
                .buildAndRegister();

        StreptococcusPyogenes = material("streptococcus_pyogenes", "酿脓链球菌")
                .dust()
                .color(0x19520B)
                .iconSet(DULL)
                .buildAndRegister();

        EschericiaColi = material("eschericia_coli", "大肠杆菌")
                .dust()
                .color(0x546F2F)
                .iconSet(DULL)
                .buildAndRegister();

        BifidobacteriumBreve = material("bifidobacterium_breve", "短双歧杆菌")
                .dust()
                .color(0x4D7511)
                .iconSet(DULL)
                .buildAndRegister();

        BrevibacteriumFlavium = material("brevibacterium_flavium", "黄色短杆菌")
                .dust()
                .color(0x667532)
                .iconSet(DULL)
                .buildAndRegister();

        CupriavidusNecator = material("cupriavidus_necator", "贪铜钩虫菌")
                .dust()
                .color(0x008C07)
                .iconSet(DULL)
                .buildAndRegister();

        Yeast = material("yeast", "酵母")
                .dust()
                .color(0xAE9C00)
                .iconSet(LIGNITE)
                .buildAndRegister().setFormula("?");

        BetaPinene = material("beta_pinene", "β-蒎烯")
                .dust()
                .color(0x059600)
                .iconSet(DULL)
                .buildAndRegister().setFormula("C10H16");

        Glutamine = material("glutamine", "谷氨酰胺")
                .dust()
                .color(0x948635)
                .iconSet(DULL)
                .buildAndRegister().setFormula("C5H10N2O3");

        Catalase = material("catalase", "过氧化氢酶")
                .fluid()
                .color(0xBD577E)
                .iconSet(DULL)
                .buildAndRegister().setFormula("?");

        BasicFibroblastGrowthFactor = material("basic_fibroblast_growth_factor", "碱性成纤维细胞生长因子")
                .fluid()
                .color(0xBD03B4)
                .iconSet(DULL)
                .buildAndRegister().setFormula("bfgf");

        EpidermalGrowthFactor = material("epidermal_growth_factor", "表皮生长因子")
                .fluid()
                .color(0x772B61)
                .iconSet(DULL)
                .buildAndRegister().setFormula("C₂₅₇H₃₈₁N₇₃O₈₃S₇", false);

        PropargylChloride = material("propargyl_chloride", "炔丙基氯")
                .fluid()
                .color(0x776B00)
                .iconSet(DULL)
                .buildAndRegister().setFormula("HC2CH2Cl");

        BetaIonone = material("beta_ionone", "β-香堇酮")
                .fluid()
                .color(0xD60B7F)
                .iconSet(DULL)
                .buildAndRegister().setFormula("C₁₃H₂₀O", false);

        PropargylAlcohol = material("propargyl_alcohol", "炔丙醇")
                .fluid()
                .color(0xBCB600)
                .iconSet(DULL)
                .buildAndRegister().setFormula("CHCCH₂OH", false);

        VitaminA = material("vitamin_a", "维生素A")
                .fluid()
                .color(0x9D60AB)
                .iconSet(DULL)
                .buildAndRegister().setFormula("C₂₀H₃₀O", false);

        Citral = material("citral", "柠檬醛")
                .fluid()
                .color(0xC3DA00)
                .iconSet(DULL)
                .buildAndRegister().setFormula("C₁₀H₁₆O", false);

        ClearAmmoniaSolution = material("clear_ammonia_solution", "纯净氨水")
                .fluid()
                .color(0x48B7A5)
                .iconSet(DULL)
                .buildAndRegister().setFormula("NH₃(H₂O)", false);

        B27Supplement = material("b27_supplement", "B27 Supplement", "B27细胞培养添加剂")
                .fluid()
                .color(0x2C6D28)
                .iconSet(DULL)
                .buildAndRegister().setFormula("C₁₄₂H₂₃₀N₃₆O₄₄S", false);

        LinoleicAcid = material("linoleic_acid", "亚油酸")
                .fluid()
                .color(0x979744)
                .iconSet(DULL)
                .buildAndRegister().setFormula("C₁₈H₃₂O₂", false);

        Biotin = material("biotin", "生物素")
                .fluid()
                .color(0x3E9743)
                .iconSet(DULL)
                .buildAndRegister().setFormula("C₁₀H₁₆N₂O₃S", false);

        BasicMFPC = material("basic_mfpc", "多功能相变(MFPC)")
                .polymer()
                .color(0xC0C0C0)
                .iconSet(DULL)
                // 后面画.iconSet(new MaterialIconSet("basic_mfpc"))
                .buildAndRegister();

        CascadeMFPC = material("cascade_mfpc", "串级相变MFPC(Cascade-MFPC)")
                .polymer()
                .color(0x303030)
                .iconSet(DULL)
                // 后面画.iconSet(new MaterialIconSet("cascade_mfpc"))
                .buildAndRegister();

        InvalidationBasicMFPC = material("invalidation_basic_mfpc", "失效的多功能相变(MFPC)")
                .polymer()
                .color(0xC0C0C0)
                .iconSet(DULL)
                // 后面画.iconSet(new MaterialIconSet("basic_mfpc"))
                .buildAndRegister();

        InvalidationCascadeMFPC = material("invalidation_cascade_mfpc", "失效的串级相变MFPC(Cascade-MFPC)")
                .polymer()
                .color(0x303030)
                .iconSet(DULL)
                // 后面画.iconSet(new MaterialIconSet("cascade_mfpc"))
                .buildAndRegister();
        ImpregnatedAlkaneFilledMFPC = material("impregnated_alkane_filled_mfpc", "浸渍烷烃填充MFPC")
                .dust()
                .color(0xB2B0B0)
                .iconSet(DULL)
                .buildAndRegister();
        HighPressureStaticAdsorptionMFPC = material("high_pressure_static_adsorption_mfpc", "高压静态吸附MFPC")
                .dust()
                .color(0xA4A0A0)
                .iconSet(DULL)
                .buildAndRegister();
        SolGelQDInterfaceModifiedMFPC = material("sol_gel_qd_interface_modified_mfpc", "溶胶-凝胶量子点界面改性MFPC")
                .dust()
                .color(0x868080)
                .iconSet(DULL)
                .buildAndRegister();
        InterfaceSupramolecularSelfAssemblyMFPC = material("interface_supramolecular_self_assembly_mfpc", "界面超分子自组装MFPC")
                .dust()
                .color(0x646260)
                .iconSet(DULL)
                .buildAndRegister();
        ExtremeTemperatureInterfaceStabilizedMFPC = material("extreme_temperature_interface_stabilized_mfpc", "极端温度界面稳定MFPC")
                .dust()
                .color(0x404042)
                .iconSet(DULL)
                .buildAndRegister();
        EtchedCarbonNanotube = material("etched_carbon_nanotube", "蚀刻碳纳米管")
                .dust()
                .color(0x303030)
                .iconSet(DULL)
                .buildAndRegister();
        NanoGoldDepositedCarbonNanotube = material("nano_gold_deposited_carbon_nanotube", "纳米金沉积碳纳米管")
                .dust()
                .color(0x3cada8)
                .iconSet(DULL)
                .buildAndRegister();
        NanoGoldDepositedCarbonNanotubeModifiedNHSLipoicEsterQDot = material("nano_gold_deposited_carbon_nanotube_modified_nhs_lipoic_ester_q_dot", "纳米金沉积碳纳米管修饰的NHS-脂肪酰基酯量子点")
                .dust()
                .color(0x57acad)
                .iconSet(DULL)
                .buildAndRegister();
        RecycleBasicMFPC = material("recycle_basic_mfpc", "回收的多功能相变(MFPC)")
                .dust()
                .color(0xC0C0C0)
                .iconSet(DULL)
                .buildAndRegister();

        HollowCeramicMicrosphereRoughEmbryo = material("hollow_ceramic_microsphere_rough_embryo", "中空陶瓷微珠粗胚")
                .dust()
                .color(0x2b2d30)
                .iconSet(SAND)
                .buildAndRegister();

        HollowCeramicMicrospheres = material("hollow_ceramic_microspheres", "中空陶瓷微珠")
                .dust()
                .color(0x2b2d30)
                .iconSet(SAND)
                .buildAndRegister();

        SilverCoatedHollowCeramicMicrospheres = material("silver_coated_hollow_ceramic_microspheres", "镀银的中空陶瓷微珠")
                .dust()
                .color(0x2b2d30)
                .iconSet(SAND)
                .buildAndRegister();

        SilverCoatedOctaneCeramicBeads = material("silver_coated_octane_ceramic_beads", "镀银的辛烷陶瓷微珠")
                .dust()
                .color(0x2b2d30)
                .iconSet(SAND)
                .buildAndRegister();

        SealedPhaseChangeBeads = material("sealed_phase_change_beads", "密封相变微珠")
                .dust()
                .color(0x2b2d30)
                .iconSet(SAND)
                .buildAndRegister();

        CarbonNanotubeCoatedPhaseChangeMicrobeads = material("carbon_nanotube_coated_phase_change_microbeads", "碳纳米管包覆的相变微珠")
                .dust()
                .color(0x2b2d30)
                .iconSet(SAND)
                .buildAndRegister();

        MicrowaveAttenuatingCoatedPhaseChangeMicrobeads = material("microwave_attenuating_coated_phase_change_microbeads", "微波衰减涂覆的相变微珠")
                .dust()
                .color(0x2b2d30)
                .iconSet(SAND)
                .buildAndRegister();

        SurfaceFunctionalizedPhaseChangeMicrobeads = material("surface_functionalized_phase_change_microbeads", "表面功能化的相变微珠")
                .dust()
                .color(0x2b2d30)
                .iconSet(SAND)
                .buildAndRegister();

        RecycledPhaseChangeMicrobeads = material("recycled_phase_change_microbeads", "回收的相变微珠")
                .dust()
                .color(0x2b2d30)
                .iconSet(SAND)
                .buildAndRegister();

        OctaneLoadedPhaseChangeMicrobeads = material("octane_loaded_phase_change_microbeads", "重载辛烷的相变微珠")
                .dust()
                .color(0x2b2d30)
                .iconSet(SAND)
                .buildAndRegister();

        ReactivatedPhaseChangeMicrobeads = material("reactivated_phase_change_microbeads", "重新活化的相变微珠")
                .dust()
                .color(0x2b2d30)
                .iconSet(SAND)
                .buildAndRegister();

        EthylSilicate = material("ethyl_silicate", "硅酸乙酯(TEOS)")
                .fluid()
                .color(0xfcffc1)
                .iconSet(FLUID)
                .buildAndRegister().setFormula("(C₂H₅O)₄Si", false);

        SilicicAcid = material("silicic_acid", "硅酸")
                .fluid()
                .color(0xd3d2d5)
                .iconSet(DULL)
                .buildAndRegister().setFormula("Si(OH)₄", false);

        Polystyrene = material("polystyrene", "聚苯乙烯")
                .polymer(1)
                .liquid(new FluidBuilder().temperature(240))
                .flags(GENERATE_FOIL)
                .color(0xc6c6c6)
                .buildAndRegister().setFormula("(C₈H₈)n", false);

        Trimethylsilanol = material("trimethylsilanol", "三甲基硅醇")
                .fluid()
                .iconSet(DULL)
                .color(0x622998)
                .buildAndRegister().setFormula("(CH₃)₃SiOH", false);

        Hexamethyldisiloxane = material("hexamethyldisiloxane", "六甲基二硅氧烷")
                .fluid()
                .iconSet(DULL)
                .color(0xffffff)
                .buildAndRegister().setFormula("(CH₃)₆OSi₂", false);

        Hexamethyldisilazane = material("hexamethyldisilazane", "六甲基二硅氨烷")
                .fluid()
                .iconSet(DULL)
                .color(0xe0e0e0)
                .buildAndRegister().setFormula("(CH₃)₆NHSi₂", false);

        LiquidAmmonia = material("liquid_ammonia", "液态氨")
                .liquid(new FluidBuilder().temperature(240))
                .color(0x4fc4a2)
                .components(Ammonia, 1)
                .flags(DISABLE_DECOMPOSITION)
                .iconSet(FLUID)
                .buildAndRegister();

        Triethoxysilane = material("triethoxysilane", "三乙氧基氢硅烷")
                .fluid()
                .iconSet(DULL)
                .color(0xbcbec4)
                .buildAndRegister().setFormula("(CH₅O)₃SiH", false);

        Chloropropyltriethoxysilane = material("chloropropyltriethoxysilane", "氯丙基三乙氧基硅烷")
                .fluid()
                .iconSet(DULL)
                .color(0xb8bb90)
                .buildAndRegister().setFormula("(CH₅O)₃(C₃H₆Cl)Si", false);

        KH550SilaneCouplingAgent = material("kh550_silane_coupling_agent", "KH-550硅烷偶联剂")
                .fluid()
                .iconSet(DULL)
                .color(0xf5f5f5)
                .buildAndRegister().setFormula("(CH₅O)₃(C₃H₆NH₂)Si", false);

        AlphaParticleSource = material("alpha_particle", "α").dust()
                .flags(GTOMaterialFlags.GENERATE_PARTICLE_SOURCE)
                .color(0x378D94).iconSet(METALLIC).radioactiveHazard(75)
                .buildAndRegister().setFormula("[α]", false);

        Calcium48Source = material("calcium_48", "钙-48").dust()
                .flags(GTOMaterialFlags.GENERATE_PARTICLE_SOURCE)
                .color(0xFFF5DE).secondaryColor(0xa4a4a4).iconSet(METALLIC).radioactiveHazard(75)
                .buildAndRegister().setFormula("Ca⁴⁸", false);

        Chromium54Source = material("chromium_54", "铬-54").dust()
                .flags(GTOMaterialFlags.GENERATE_PARTICLE_SOURCE)
                .color(0xf3e0ea).secondaryColor(0x441f2e).iconSet(SHINY).radioactiveHazard(75)
                .buildAndRegister().setFormula("Cr⁵⁴", false);

        Iron58Source = material("iron_58", "铁-58").dust()
                .flags(GTOMaterialFlags.GENERATE_PARTICLE_SOURCE)
                .color(0xeeeeee).secondaryColor(0x979797).iconSet(METALLIC).radioactiveHazard(75)
                .buildAndRegister().setFormula("Fe⁵⁸", false);

        Nickel64Source = material("nickel_64", "镍-64").dust()
                .flags(GTOMaterialFlags.GENERATE_PARTICLE_SOURCE)
                .color(0xccdff5).secondaryColor(0x59563a).iconSet(METALLIC).radioactiveHazard(75)
                .buildAndRegister().setFormula("Ni⁶⁴", false);

        Zinc70Source = material("zinc_70", "锌-70").dust()
                .flags(GTOMaterialFlags.GENERATE_PARTICLE_SOURCE)
                .color(0xEBEBFA).secondaryColor(0x232c30).iconSet(METALLIC).radioactiveHazard(75)
                .buildAndRegister().setFormula("Zn⁷⁰", false);

        PlutoniumBerylliumSource = material("plutonium_beryllium", "钚-铍").dust()
                .flags(GTOMaterialFlags.GENERATE_PARTICLE_SOURCE)
                .color(0xff4c4c).secondaryColor(0x222730).iconSet(RADIOACTIVE).radioactiveHazard(75)
                .buildAndRegister().setFormula("Pu-Be", false);

        AntinomyBerylliumSource = material("antimony_beryllium", "锑-铍").dust()
                .flags(GTOMaterialFlags.GENERATE_PARTICLE_SOURCE)
                .color(0xeaeaff).secondaryColor(0x8181bd).iconSet(SHINY).radioactiveHazard(75)
                .buildAndRegister().setFormula("Sb-Be", false);

        Californium252Source = material("californium_252", "锎-252").dust()
                .flags(GTOMaterialFlags.GENERATE_PARTICLE_SOURCE)
                .color(0xA85A12).iconSet(RADIOACTIVE).radioactiveHazard(75)
                .buildAndRegister().setFormula("Cf²⁵²", false);

        IronChromiumRedoxFlowBatteryElectrolyte = material("iron_chromium", "铁铬液流")
                .fluid(GTOFluidStorageKey.ENERGY_STORAGE_ANODE, FluidState.LIQUID)
                .fluid(GTOFluidStorageKey.ENERGY_STORAGE_CATHODE, FluidState.LIQUID)
                .fluid(GTOFluidStorageKey.ENERGY_RELEASE_ANODE, FluidState.LIQUID)
                .fluid(GTOFluidStorageKey.ENERGY_RELEASE_CATHODE, FluidState.LIQUID)
                .color(0xB7410E)
                .iconSet(FLUID)
                .buildAndRegister();
        VanadiumRedoxFlowBatteryElectrolyte = material("all_vanadium", "全钒液流")
                .fluid(GTOFluidStorageKey.ENERGY_STORAGE_ANODE, FluidState.LIQUID)
                .fluid(GTOFluidStorageKey.ENERGY_STORAGE_CATHODE, FluidState.LIQUID)
                .fluid(GTOFluidStorageKey.ENERGY_RELEASE_ANODE, FluidState.LIQUID)
                .fluid(GTOFluidStorageKey.ENERGY_RELEASE_CATHODE, FluidState.LIQUID)
                .color(0xFF4D00)
                .iconSet(FLUID)
                .buildAndRegister();
        ZincIodideFlowBatteryElectrolyte = material("zinc_iodide", "锌碘液流")
                .fluid(GTOFluidStorageKey.ENERGY_STORAGE_ANODE, FluidState.LIQUID)
                .fluid(GTOFluidStorageKey.ENERGY_STORAGE_CATHODE, FluidState.LIQUID)
                .fluid(GTOFluidStorageKey.ENERGY_RELEASE_ANODE, FluidState.LIQUID)
                .fluid(GTOFluidStorageKey.ENERGY_RELEASE_CATHODE, FluidState.LIQUID)
                .color(0xFFFF00)
                .iconSet(FLUID)
                .buildAndRegister();
        OrganicMoleculeRedoxFlowBatteryElectrolyte = material("organic_molecule", "有机液流")
                .fluid(GTOFluidStorageKey.ENERGY_STORAGE_ANODE, FluidState.LIQUID)
                .fluid(GTOFluidStorageKey.ENERGY_STORAGE_CATHODE, FluidState.LIQUID)
                .fluid(GTOFluidStorageKey.ENERGY_RELEASE_ANODE, FluidState.LIQUID)
                .fluid(GTOFluidStorageKey.ENERGY_RELEASE_CATHODE, FluidState.LIQUID)
                .color(0x00FF00)
                .iconSet(FLUID)
                .buildAndRegister();
        SuperconductingIonRedoxFlowBatteryElectrolyte = material("superconducting_ion", "超导液流")
                .fluid(GTOFluidStorageKey.ENERGY_STORAGE_ANODE, FluidState.LIQUID)
                .fluid(GTOFluidStorageKey.ENERGY_STORAGE_CATHODE, FluidState.LIQUID)
                .fluid(GTOFluidStorageKey.ENERGY_RELEASE_ANODE, FluidState.LIQUID)
                .fluid(GTOFluidStorageKey.ENERGY_RELEASE_CATHODE, FluidState.LIQUID)
                .color(0x0000FF)
                .iconSet(FLUID)
                .buildAndRegister();
        AntimatterRedoxFlowBatteryElectrolyte = material("antimatter-ion", "反电荷载电")
                .fluid(GTOFluidStorageKey.ENERGY_STORAGE_ANODE, FluidState.LIQUID)
                .fluid(GTOFluidStorageKey.ENERGY_STORAGE_CATHODE, FluidState.LIQUID)
                .fluid(GTOFluidStorageKey.ENERGY_RELEASE_ANODE, FluidState.LIQUID)
                .fluid(GTOFluidStorageKey.ENERGY_RELEASE_CATHODE, FluidState.LIQUID)
                .color(0x8B00FF)
                .iconSet(FLUID)
                .buildAndRegister();
    }
}
