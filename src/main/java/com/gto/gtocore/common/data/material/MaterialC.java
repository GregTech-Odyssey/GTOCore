package com.gto.gtocore.common.data.material;

import com.gregtechceu.gtceu.api.fluids.FluidBuilder;

import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags.DISABLE_DECOMPOSITION;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gto.gtocore.common.data.GTOMaterials.*;
import static com.gto.gtocore.utils.register.MaterialsRegisterUtils.material;

public interface MaterialC {

    static void init() {
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
    }
}
