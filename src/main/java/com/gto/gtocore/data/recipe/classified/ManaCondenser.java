package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.Items;

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
                .inputFluids(GTOMaterials.Aether.getFluid(1000))
                .duration(80)
                .MANAt(2048)
                .save();

        MANA_CONDENSER_RECIPES.builder("ruthenium_trinium_americium_neutronate")
                .inputItems(GTOTagPrefix.SUPERCONDUCTOR_BASE, GTMaterials.RutheniumTriniumAmericiumNeutronate, 4)
                .outputItems(TagPrefix.wireGtSingle, GTMaterials.RutheniumTriniumAmericiumNeutronate, 4)
                .inputFluids(GTOMaterials.Aether.getFluid(1000))
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

        MANA_CONDENSER_RECIPES.builder("black_eye")
                .inputItems(GTItems.QUANTUM_EYE.asStack())
                .inputItems(Items.SCULK_CATALYST.asItem())
                .outputItems("endrem:black_eye")
                .duration(200)
                .MANAt(128)
                .save();

        MANA_CONDENSER_RECIPES.builder("cold_eye")
                .inputItems(GTItems.QUANTUM_EYE.asStack())
                .inputItems("ad_astra:ice_shard")
                .outputItems("endrem:cold_eye")
                .duration(200)
                .MANAt(128)
                .save();

        MANA_CONDENSER_RECIPES.builder("corrupted_eye")
                .inputItems(GTItems.QUANTUM_EYE.asStack())
                .inputItems("enderio:plant_matter_brown")
                .outputItems("endrem:corrupted_eye")
                .duration(200)
                .MANAt(128)
                .save();

        MANA_CONDENSER_RECIPES.builder("lost_eye")
                .inputItems(GTItems.QUANTUM_EYE.asStack())
                .inputItems("enderio:redstone_alloy_grinding_ball")
                .outputItems("endrem:lost_eye")
                .duration(200)
                .MANAt(128)
                .save();

        MANA_CONDENSER_RECIPES.builder("nether_eye")
                .inputItems(GTItems.QUANTUM_EYE.asStack())
                .inputItems("botania:quartz_blaze")
                .outputItems("endrem:nether_eye")
                .duration(200)
                .MANAt(128)
                .save();

        MANA_CONDENSER_RECIPES.builder("old_eye")
                .inputItems(GTItems.QUANTUM_EYE.asStack())
                .inputItems("botania:forest_eye")
                .outputItems("endrem:old_eye")
                .duration(200)
                .MANAt(128)
                .save();

        MANA_CONDENSER_RECIPES.builder("rogue_eye")
                .inputItems(GTItems.QUANTUM_EYE.asStack())
                .inputItems("botania:redstone_root")
                .outputItems("endrem:rogue_eye")
                .duration(200)
                .MANAt(128)
                .save();

        MANA_CONDENSER_RECIPES.builder("cursed_eye")
                .inputItems(GTItems.QUANTUM_EYE.asStack())
                .inputItems("botania:life_essence")
                .outputItems("endrem:cursed_eye")
                .duration(200)
                .MANAt(128)
                .save();

        MANA_CONDENSER_RECIPES.builder("guardian_eye")
                .inputItems(GTItems.QUANTUM_EYE.asStack())
                .inputItems(Items.PRISMARINE_CRYSTALS.asItem())
                .outputItems("endrem:guardian_eye")
                .duration(200)
                .MANAt(128)
                .save();

        MANA_CONDENSER_RECIPES.builder("magical_eye")
                .inputItems(GTItems.QUANTUM_EYE.asStack())
                .inputItems("botania:mana_bottle")
                .outputItems("endrem:magical_eye")
                .duration(200)
                .MANAt(128)
                .save();

        MANA_CONDENSER_RECIPES.builder("wither_eye")
                .inputItems(GTItems.QUANTUM_EYE.asStack())
                .inputItems("enderio:withering_powder")
                .outputItems("endrem:wither_eye")
                .duration(200)
                .MANAt(128)
                .save();
    }
}
