package com.gtocore.data.recipe.classified;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.data.machines.MultiBlockH;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import static com.gtocore.common.data.GTORecipeTypes.*;

class FastNeutronBreeder {

    public static void init() {
        FAST_NEUTRON_BREEDER_REACTOR_RECIPES.builder("thorium_depleted_breeder_rod")// 钍增殖棒
                .inputItems(GTOTagPrefix.BREEDER_ROD, GTMaterials.Thorium)
                .inputItems(TagPrefix.dust, GTMaterials.Protactinium)
                .outputItems(GTOTagPrefix.DEPLETED_BREEDER_ROD, GTMaterials.Thorium)
                .EUt(30)
                .duration(128000)
                .addData("neutron_flux", 5.04f)
                .addData("neutron_flux_change", 0.01f)
                .addData("heat", 0.01f)
                .save();
        // 最低中子通量：5.04 keV
        // 中子通量变化：0.01 keV
        // 温度变化：0.01 K

        FAST_NEUTRON_BREEDER_REACTOR_RECIPES.builder("protactinium_depleted_breeder_rod_1")// 镤增殖棒-1
                .inputItems(GTOTagPrefix.BREEDER_ROD, GTMaterials.Protactinium)
                .inputItems(TagPrefix.dust, GTMaterials.Uranium238)
                .outputItems(GTOTagPrefix.DEPLETED_BREEDER_ROD, GTMaterials.Protactinium)
                .EUt(30)
                .duration(128000)
                .addData("neutron_flux", 100.08f)
                .addData("neutron_flux_change", 0.03f)
                .addData("heat", 0.06f)
                .save();
        // 最低中子通量：100.08 keV
        // 中子通量变化：0.03 keV
        // 温度变化：0.06 K

        FAST_NEUTRON_BREEDER_REACTOR_RECIPES.builder("protactinium_depleted_breeder_rod_2")// 镤增殖棒-2
                .inputItems(GTOTagPrefix.BREEDER_ROD, GTMaterials.Protactinium)
                .inputItems(TagPrefix.dust, GTMaterials.Uranium235)
                .outputItems(GTOTagPrefix.DEPLETED_BREEDER_ROD, GTMaterials.Protactinium)
                .EUt(30)
                .duration(128000)
                .addData("neutron_flux", 100.08f)
                .addData("neutron_flux_change", 0.03f)
                .addData("heat", 0.06f)
                .save();
        // 最低中子通量：100.08 keV
        // 中子通量变化：0.03 keV
        // 温度变化：0.06 K

        FAST_NEUTRON_BREEDER_REACTOR_RECIPES.builder("uranium_depleted_breeder_rod")// 铀增殖棒
                .inputItems(GTOTagPrefix.BREEDER_ROD, GTMaterials.Uranium238)
                .inputItems(TagPrefix.dust, GTMaterials.Neptunium)
                .outputItems(GTOTagPrefix.DEPLETED_BREEDER_ROD, GTMaterials.Uranium238)
                .EUt(30)
                .duration(128000)
                .addData("neutron_flux", 49.92f)
                .addData("neutron_flux_change", 0.01f)
                .addData("heat", 0.03f)
                .save();
        // 最低中子通量：49.92 keV
        // 中子通量变化：0.01 keV
        // 温度变化：0.03 K

        FAST_NEUTRON_BREEDER_REACTOR_RECIPES.builder("uranium_235_depleted_breeder_rod")// 铀235增殖棒
                .inputItems(GTOTagPrefix.BREEDER_ROD, GTMaterials.Uranium235)
                .inputItems(TagPrefix.dust, GTMaterials.Neptunium)
                .outputItems(GTOTagPrefix.DEPLETED_BREEDER_ROD, GTMaterials.Uranium235)
                .EUt(30)
                .duration(192000)
                .addData("neutron_flux", 49.92f)
                .addData("neutron_flux_change", 0.05f)
                .addData("heat", 0.04f)
                .save();
        // 最低中子通量：0.48 keV
        // 中子通量变化：-0.05 keV
        // 温度变化：0.04 K

        FAST_NEUTRON_BREEDER_REACTOR_RECIPES.builder("neptunium_depleted_breeder_rod_1")// 镎增殖棒-1
                .inputItems(GTOTagPrefix.BREEDER_ROD, GTMaterials.Neptunium)
                .inputItems(TagPrefix.dust, GTMaterials.Plutonium239)
                .outputItems(GTOTagPrefix.DEPLETED_BREEDER_ROD, GTMaterials.Neptunium)
                .EUt(30)
                .duration(128000)
                .addData("neutron_flux", 400.08f)
                .addData("neutron_flux_change", 0.04f)
                .addData("heat", 0.08f)
                .save();
        // 最低中子通量：400.08 keV
        // 中子通量变化：0.04 keV
        // 温度变化：0.08 K

        FAST_NEUTRON_BREEDER_REACTOR_RECIPES.builder("neptunium_depleted_breeder_rod_2")// 镎增殖棒-2
                .inputItems(GTOTagPrefix.BREEDER_ROD, GTMaterials.Neptunium)
                .inputItems(TagPrefix.dust, GTMaterials.Plutonium241)
                .outputItems(GTOTagPrefix.DEPLETED_BREEDER_ROD, GTMaterials.Neptunium)
                .EUt(30)
                .duration(64000)
                .addData("neutron_flux", 400.08f)
                .addData("neutron_flux_change", 0.08f)
                .addData("heat", 0.12f)
                .save();
        // 最低中子通量：400.08 keV
        // 中子通量变化：0.08 keV
        // 温度变化：0.12 K

        FAST_NEUTRON_BREEDER_REACTOR_RECIPES.builder("plutonium_depleted_breeder_rod")// 钚增殖棒
                .inputItems(GTOTagPrefix.BREEDER_ROD, GTMaterials.Plutonium239)
                .inputItems(TagPrefix.dust, GTMaterials.Americium)
                .outputItems(GTOTagPrefix.DEPLETED_BREEDER_ROD, GTMaterials.Plutonium239)
                .EUt(30)
                .duration(128000)
                .addData("neutron_flux", 199.92f)
                .addData("neutron_flux_change", 0.08f)
                .addData("heat", 0.06f)
                .save();
        // 最低中子通量：199.92 keV
        // 中子通量变化：0.08 keV
        // 温度变化：0.06 K

        FAST_NEUTRON_BREEDER_REACTOR_RECIPES.builder("plutonium_241_depleted_breeder_rod")// 钚241增殖棒
                .inputItems(GTOTagPrefix.BREEDER_ROD, GTMaterials.Plutonium241)
                .inputItems(TagPrefix.dust, GTMaterials.Americium)
                .outputItems(GTOTagPrefix.DEPLETED_BREEDER_ROD, GTMaterials.Plutonium241)
                .EUt(30)
                .duration(64000)
                .addData("neutron_flux", 300f)
                .addData("neutron_flux_change", 0.08f)
                .addData("heat", 0.12f)
                .save();
        // 最低中子通量：300 keV
        // 中子通量变化：0.08 keV
        // 温度变化：0.12 K

        FAST_NEUTRON_BREEDER_REACTOR_RECIPES.builder("americium_depleted_breeder_rod")// 镅增殖棒
                .inputItems(GTOTagPrefix.BREEDER_ROD, GTMaterials.Americium)
                .inputItems(TagPrefix.dust, GTMaterials.Curium)
                .outputItems(GTOTagPrefix.DEPLETED_BREEDER_ROD, GTMaterials.Americium)
                .EUt(30)
                .duration(128000)
                .addData("neutron_flux", 600f)
                .addData("neutron_flux_change", 0.06f)
                .addData("heat", 0.15f)
                .save();
        // 最低中子通量：600 keV
        // 中子通量变化：0.06 keV
        // 温度变化：0.15 K

        FAST_NEUTRON_BREEDER_REACTOR_RECIPES.builder("curium_depleted_breeder_rod")// 锔增殖棒
                .inputItems(GTOTagPrefix.BREEDER_ROD, GTMaterials.Curium)
                .inputItems(TagPrefix.dust, GTMaterials.Berkelium)
                .outputItems(GTOTagPrefix.DEPLETED_BREEDER_ROD, GTMaterials.Curium)
                .EUt(30)
                .duration(128000)
                .addData("neutron_flux", 1200f)
                .addData("neutron_flux_change", 0.1f)
                .addData("heat", 0.2f)
                .save();
        // 最低中子通量：1200 keV
        // 中子通量变化：0.1 keV
        // 温度变化：0.2 K

        FAST_NEUTRON_BREEDER_REACTOR_RECIPES.builder("berkelium_depleted_breeder_rod_1")// 锫增殖棒-1
                .inputItems(GTOTagPrefix.BREEDER_ROD, GTMaterials.Berkelium)
                .inputItems(TagPrefix.dust, GTMaterials.Californium)
                .outputItems(GTOTagPrefix.DEPLETED_BREEDER_ROD, GTMaterials.Berkelium)
                .EUt(30)
                .duration(128000)
                .addData("neutron_flux", 2400f)
                .addData("neutron_flux_change", 0.15f)
                .addData("heat", 0.3f)
                .save();
        // 最低中子通量：2400 keV
        // 中子通量变化：0.15 keV
        // 温度变化：0.3 K

        FAST_NEUTRON_BREEDER_REACTOR_RECIPES.builder("berkelium_depleted_breeder_rod_2")// 锫增殖棒-2
                .inputItems(GTOTagPrefix.BREEDER_ROD, GTMaterials.Berkelium)
                .inputItems(TagPrefix.dust, GTOMaterials.Californium252Source)
                .outputItems(GTOTagPrefix.DEPLETED_BREEDER_ROD, GTMaterials.Berkelium)
                .EUt(30)
                .duration(64000)
                .addData("neutron_flux", 2400f)
                .addData("neutron_flux_change", 0.19f)
                .addData("heat", 0.4f)
                .save();
        // 最低中子通量：2400 keV
        // 中子通量变化：0.19 keV
        // 温度变化：0.4 K

        CANNER_RECIPES.builder("antimony_beryllium_particle_source")// 锑-铍粒子源
                .inputItems(GTItems.FLUID_CELL.asItem(), 4)
                .inputItems(TagPrefix.dust, GTMaterials.Antimony)
                .outputItems(GTOTagPrefix.PARTICLE_SOURCE, GTOMaterials.AntinomyBerylliumSource)
                .inputFluids(GTMaterials.Beryllium, 1000)
                .EUt(7680)
                .duration(200)
                .save();

        CANNER_RECIPES.builder("plutonium_beryllium_particle_source")// 钚-铍粒子源
                .inputItems(GTItems.FLUID_CELL.asItem(), 4)
                .inputItems(TagPrefix.dust, GTMaterials.Plutonium239)
                .outputItems(GTOTagPrefix.PARTICLE_SOURCE, GTOMaterials.PlutoniumBerylliumSource)
                .inputFluids(GTMaterials.Beryllium, 1000)
                .EUt(30720)
                .duration(200)
                .save();

        CANNER_RECIPES.builder("californium_252_particle_source")// 锎252粒子源
                .inputItems(GTItems.FLUID_CELL.asItem(), 4)
                .inputItems(TagPrefix.dust, GTOMaterials.Californium252Source)
                .outputItems(GTOTagPrefix.PARTICLE_SOURCE, GTOMaterials.Californium252Source)
                .EUt(30720)
                .duration(200)
                .save();
        ASSEMBLER_RECIPES.builder("fast_neutron_breeder_reactor")// 快中子增殖堆
                .inputItems(TagPrefix.frameGt, GTOMaterials.BabbittAlloy)
                .inputItems(GTItems.ROBOT_ARM_IV.asItem(), 16)
                .inputItems(GTItems.SENSOR_IV.asItem(), 4)
                .inputItems(GTItems.ADVANCED_SYSTEM_ON_CHIP.asItem(), 4)
                .inputItems(TagPrefix.cableGtQuadruple, GTMaterials.NiobiumTitanium, 4)
                .inputItems(TagPrefix.plate, GTMaterials.Trinium, 8)
                .inputItems(TagPrefix.plateDense, GTMaterials.NaquadahAlloy, 4)
                .inputItems(CustomTags.ZPM_CIRCUITS, 4)                                // 任意ZPM电路
                .inputItems(GTItems.NEUTRON_REFLECTOR.asItem(), 4)
                .outputItems(MultiBlockH.FAST_NEUTRON_BREEDER_REACTOR.asItem())
                .inputFluids(GTMaterials.PolyphenyleneSulfide, 1296)
                .EUt(30720)
                .duration(400)
                .save();
    }
}
