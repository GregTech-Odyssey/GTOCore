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
    }
}
