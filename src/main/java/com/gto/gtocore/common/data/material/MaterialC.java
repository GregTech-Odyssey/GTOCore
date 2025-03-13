package com.gto.gtocore.common.data.material;

import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet.DULL;
import static com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet.SAND;
import static com.gto.gtocore.common.data.GTOMaterials.*;
import static com.gto.gtocore.utils.register.MaterialsRegisterUtils.material;

public interface MaterialC {

    static void init() {
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

        Chitin = material("chitin", "甲壳质")
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
                .color(0x800C4F)
                .iconSet(DULL)
                .buildAndRegister();

        EschericiaColi = material("eschericia_coli", "大肠杆菌")
                .dust()
                .color(0x800C4F)
                .iconSet(DULL)
                .buildAndRegister();

        BifidobacteriumBreve = material("bifidobacterium_breve", "短双歧杆菌")
                .dust()
                .color(0x800C4F)
                .iconSet(DULL)
                .buildAndRegister();

        BrevibacteriumFlavium = material("brevibacterium_flavium", "黄色短杆菌")
                .dust()
                .color(0x800C4F)
                .iconSet(DULL)
                .buildAndRegister();

        CupriavidusNecator = material("cupriavidus_necator", "钩虫贪铜菌")
                .dust()
                .color(0x800C4F)
                .iconSet(DULL)
                .buildAndRegister();

        BasicMFPC = material("basic_mfpc", "多功能相变微粒（MFPC）")
                .dust()
                .color(0xC0C0C0)
                .iconSet(SAND)
                // 后面画.iconSet(new MaterialIconSet("basic_mfpc"))
                .buildAndRegister();

        CascadeMFPC = material("cascade_mfpc", "串级相变MFPC微粒（Cascade-MFPC）")
                .dust()
                .color(0x303030)
                .iconSet(SAND)
                // 后面画.iconSet(new MaterialIconSet("cascade_mfpc"))
                .buildAndRegister();

        InvalidationBasicMFPC = material("invalidation_basic_mfpc", "失效的多功能相变微粒（MFPC")
                .dust()
                .color(0xC0C0C0)
                .iconSet(SAND)
                // 后面画.iconSet(new MaterialIconSet("basic_mfpc"))
                .buildAndRegister();

        InvalidationCascadeMFPC = material("invalidation_cascade_mfpc", "失效的串级相变MFPC微粒（Cascade-MFPC）")
                .dust()
                .color(0x303030)
                .iconSet(SAND)
                // 后面画.iconSet(new MaterialIconSet("cascade_mfpc"))
                .buildAndRegister();
    }
}
