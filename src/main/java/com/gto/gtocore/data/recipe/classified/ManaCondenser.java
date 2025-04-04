package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gto.gtocore.common.data.GTORecipeTypes.MANA_CONDENSER_RECIPES;

interface ManaCondenser {

    static void init() {
        MANA_CONDENSER_RECIPES.builder("terrasteel")
                .inputItems(TagPrefix.ingot, GTOMaterials.Manasteel)
                .inputItems("botania:mana_pearl")
                .inputItems(TagPrefix.gem, GTOMaterials.ManaDiamond)
                .outputItems(TagPrefix.ingot, GTOMaterials.Terrasteel, 3)
                .inputFluids(GTOMaterials.Terrasteel, 144)
                .MANAt(512)
                .duration(200)
                .save();

        MANA_CONDENSER_RECIPES.builder("gaiasteel")
                .inputItems(TagPrefix.dust, GTOMaterials.Gaia)
                .inputItems(TagPrefix.dust, GTOMaterials.OriginalBronze)
                .outputItems(TagPrefix.dust, GTOMaterials.Gaiasteel, 2)
                .inputFluids(GTOMaterials.Elementium, 288)
                .duration(200)
                .MANAt(2048)
                .save();

        MANA_CONDENSER_RECIPES.builder("enriched_naquadah_trinium_europium_duranide")
                .inputItems(GTOTagPrefix.SUPERCONDUCTOR_BASE, GTMaterials.EnrichedNaquadahTriniumEuropiumDuranide, 4)
                .outputItems(TagPrefix.wireGtSingle, GTMaterials.EnrichedNaquadahTriniumEuropiumDuranide, 4)
                .inputFluids(GTOMaterials.Aether.getFluid(FluidStorageKeys.GAS, 1000))
                .duration(80)
                .MANAt(2048)
                .save();

        MANA_CONDENSER_RECIPES.builder("ruthenium_trinium_americium_neutronate")
                .inputItems(GTOTagPrefix.SUPERCONDUCTOR_BASE, GTMaterials.RutheniumTriniumAmericiumNeutronate, 4)
                .outputItems(TagPrefix.wireGtSingle, GTMaterials.RutheniumTriniumAmericiumNeutronate, 4)
                .inputFluids(GTOMaterials.Aether.getFluid(FluidStorageKeys.GAS, 1000))
                .duration(80)
                .MANAt(8192)
                .save();

        MANA_CONDENSER_RECIPES.builder("rune_water")
                .inputItems(TagPrefix.block, GTOMaterials.Livingrock, 16)
                .inputItems("botania:blue_petal")
                .inputItems(TagPrefix.dust, GTMaterials.Lapis)
                .outputItems("botania:rune_water")
                .inputFluids(GTOMaterials.Elementium, 288)
                .duration(200)
                .MANAt(256)
                .save();

        MANA_CONDENSER_RECIPES.builder("rune_fire")
                .inputItems(TagPrefix.block, GTOMaterials.Livingrock, 16)
                .inputItems("botania:orange_petal")
                .inputItems(TagPrefix.dust, GTMaterials.Copper)
                .outputItems("botania:rune_fire")
                .inputFluids(GTOMaterials.Elementium, 288)
                .duration(200)
                .MANAt(256)
                .save();

        MANA_CONDENSER_RECIPES.builder("rune_air")
                .inputItems(TagPrefix.block, GTOMaterials.Livingrock, 16)
                .inputItems("botania:light_blue_petal")
                .inputItems(TagPrefix.dust, GTMaterials.Diamond)
                .outputItems("botania:rune_air")
                .inputFluids(GTOMaterials.Elementium, 288)
                .duration(200)
                .MANAt(256)
                .save();

        MANA_CONDENSER_RECIPES.builder("rune_earth")
                .inputItems(TagPrefix.block, GTOMaterials.Livingrock, 16)
                .inputItems("botania:green_petal")
                .inputItems(TagPrefix.dust, GTMaterials.Emerald)
                .outputItems("botania:rune_earth")
                .inputFluids(GTOMaterials.Elementium, 288)
                .duration(200)
                .MANAt(256)
                .save();

        MANA_CONDENSER_RECIPES.builder("rune_spring")
                .inputItems(TagPrefix.block, GTOMaterials.Livingrock, 16)
                .inputItems("botania:rune_water")
                .inputItems("botania:rune_fire")
                .outputItems("botania:rune_spring")
                .inputFluids(GTOMaterials.Elementium, 288)
                .duration(200)
                .MANAt(256)
                .save();

        MANA_CONDENSER_RECIPES.builder("rune_summer")
                .inputItems(TagPrefix.block, GTOMaterials.Livingrock, 16)
                .inputItems("botania:rune_air")
                .inputItems("botania:rune_earth")
                .outputItems("botania:rune_summer")
                .inputFluids(GTOMaterials.Elementium, 288)
                .duration(200)
                .MANAt(256)
                .save();

        MANA_CONDENSER_RECIPES.builder("rune_autumn")
                .inputItems(TagPrefix.block, GTOMaterials.Livingrock, 16)
                .inputItems("botania:rune_air")
                .inputItems("botania:rune_fire")
                .outputItems("botania:rune_autumn")
                .inputFluids(GTOMaterials.Elementium, 288)
                .duration(200)
                .MANAt(256)
                .save();

        MANA_CONDENSER_RECIPES.builder("rune_winter")
                .inputItems(TagPrefix.block, GTOMaterials.Livingrock, 16)
                .inputItems("botania:rune_earth")
                .inputItems("botania:rune_water")
                .outputItems("botania:rune_winter")
                .inputFluids(GTOMaterials.Elementium, 288)
                .duration(200)
                .MANAt(256)
                .save();

        MANA_CONDENSER_RECIPES.builder("rune_mana")
                .inputItems(TagPrefix.block, GTOMaterials.Livingrock, 16)
                .inputItems(TagPrefix.gem, GTOMaterials.ManaDiamond, 2)
                .inputItems(TagPrefix.dust, GTOMaterials.Mana, 2)
                .outputItems("botania:rune_mana")
                .inputFluids(GTOMaterials.Elementium, 288)
                .duration(200)
                .MANAt(256)
                .save();

        MANA_CONDENSER_RECIPES.builder("rune_lust")
                .inputItems(TagPrefix.block, GTOMaterials.Livingrock, 16)
                .inputItems("botania:rune_air")
                .inputItems("botania:rune_summer")
                .outputItems("botania:rune_lust")
                .inputFluids(GTOMaterials.Elementium, 288)
                .duration(200)
                .MANAt(256)
                .save();

        MANA_CONDENSER_RECIPES.builder("rune_gluttony")
                .inputItems(TagPrefix.block, GTOMaterials.Livingrock, 16)
                .inputItems("botania:rune_winter")
                .inputItems("botania:rune_fire")
                .outputItems("botania:rune_gluttony")
                .inputFluids(GTOMaterials.Elementium, 288)
                .duration(200)
                .MANAt(256)
                .save();

        MANA_CONDENSER_RECIPES.builder("rune_greed")
                .inputItems(TagPrefix.block, GTOMaterials.Livingrock, 16)
                .inputItems("botania:rune_water")
                .inputItems("botania:rune_spring")
                .outputItems("botania:rune_greed")
                .inputFluids(GTOMaterials.Elementium, 288)
                .duration(200)
                .MANAt(256)
                .save();

        MANA_CONDENSER_RECIPES.builder("rune_sloth")
                .inputItems(TagPrefix.block, GTOMaterials.Livingrock, 16)
                .inputItems("botania:rune_air")
                .inputItems("botania:rune_autumn")
                .outputItems("botania:rune_sloth")
                .inputFluids(GTOMaterials.Elementium, 288)
                .duration(200)
                .MANAt(256)
                .save();

        MANA_CONDENSER_RECIPES.builder("rune_wrath")
                .inputItems(TagPrefix.block, GTOMaterials.Livingrock, 16)
                .inputItems("botania:rune_earth")
                .inputItems("botania:rune_winter")
                .outputItems("botania:rune_wrath")
                .inputFluids(GTOMaterials.Elementium, 288)
                .duration(200)
                .MANAt(256)
                .save();

        MANA_CONDENSER_RECIPES.builder("rune_envy")
                .inputItems(TagPrefix.block, GTOMaterials.Livingrock, 16)
                .inputItems("botania:rune_water")
                .inputItems("botania:rune_winter")
                .outputItems("botania:rune_envy")
                .inputFluids(GTOMaterials.Elementium, 288)
                .duration(200)
                .MANAt(256)
                .save();

        MANA_CONDENSER_RECIPES.builder("rune_pride")
                .inputItems(TagPrefix.block, GTOMaterials.Livingrock, 16)
                .inputItems("botania:rune_fire")
                .inputItems("botania:rune_summer")
                .outputItems("botania:rune_pride")
                .inputFluids(GTOMaterials.Elementium, 288)
                .duration(200)
                .MANAt(256)
                .save();
    }
}
