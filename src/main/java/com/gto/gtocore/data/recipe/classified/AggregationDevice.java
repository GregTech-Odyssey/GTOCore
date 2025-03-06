package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gto.gtocore.common.data.GTORecipeTypes.AGGREGATION_DEVICE_RECIPES;

interface AggregationDevice {

    static void init() {
        AGGREGATION_DEVICE_RECIPES.recipeBuilder(GTOCore.id("chaotic_core"))
                .notConsumable(GTOItems.DRAGON_STABILIZER_CORE.asStack())
                .inputItems(GTOTagPrefix.nanites, GTOMaterials.Draconium)
                .inputItems(GTItems.FIELD_GENERATOR_OpV.asStack())
                .inputItems(GTOItems.CHAOS_SHARD.asStack())
                .inputItems(TagPrefix.block, GTOMaterials.Legendarium)
                .inputItems(GTOItems.AWAKENED_CORE.asStack())
                .inputItems(GTOItems.MAX_FIELD_GENERATOR.asStack())
                .inputItems(GTOItems.UNSTABLE_STAR.asStack())
                .inputItems(TagPrefix.block, GTOMaterials.AwakenedDraconium)
                .outputItems(GTOItems.CHAOTIC_CORE.asStack(2))
                .EUt(503316480)
                .duration(400)
                .save();

        AGGREGATION_DEVICE_RECIPES.recipeBuilder(GTOCore.id("awakened_core"))
                .notConsumable(GTOItems.DRAGON_STABILIZER_CORE.asStack())
                .inputItems(TagPrefix.dust, GTOMaterials.Draconium)
                .inputItems(GTItems.FIELD_GENERATOR_UIV.asStack())
                .inputItems(GTOItems.DRAGON_HEART.asStack())
                .inputItems(TagPrefix.block, GTOMaterials.Vibranium)
                .inputItems(GTOItems.WYVERN_CORE.asStack())
                .inputItems(GTItems.FIELD_GENERATOR_UXV.asStack())
                .inputItems(GTItems.GRAVI_STAR.asStack())
                .inputItems(TagPrefix.block, GTOMaterials.Taranium)
                .outputItems(GTOItems.AWAKENED_CORE.asStack(2))
                .EUt(125829120)
                .duration(400)
                .save();

        AGGREGATION_DEVICE_RECIPES.recipeBuilder(GTOCore.id("wyvern_core"))
                .notConsumable(GTOItems.STABILIZER_CORE.asStack())
                .inputItems(GTOItems.DRACONIUM_DIRT.asStack())
                .inputItems(GTItems.FIELD_GENERATOR_UHV.asStack())
                .inputItems(GTItems.QUANTUM_EYE.asStack())
                .inputItems(TagPrefix.block, GTOMaterials.Adamantine)
                .inputItems(GTOItems.DRACONIC_CORE.asStack())
                .inputItems(GTItems.FIELD_GENERATOR_UEV.asStack())
                .inputItems(GTItems.QUANTUM_STAR.asStack())
                .inputItems(TagPrefix.block, GTOMaterials.Orichalcum)
                .outputItems(GTOItems.WYVERN_CORE.asStack(2))
                .EUt(31457280)
                .duration(400)
                .save();

        AGGREGATION_DEVICE_RECIPES.recipeBuilder(GTOCore.id("draconic_core"))
                .notConsumable(GTOItems.STABILIZER_CORE.asStack())
                .inputItems(GTOItems.DRACONIUM_DIRT.asStack())
                .inputItems(GTItems.FIELD_GENERATOR_ZPM.asStack())
                .inputItems(GTItems.ENERGY_LAPOTRONIC_ORB.asStack())
                .inputItems(TagPrefix.block, GTOMaterials.Mithril)
                .inputItems(TagPrefix.dust, GTOMaterials.Hexanitrohexaaxaisowurtzitane)
                .inputItems(GTItems.FIELD_GENERATOR_UV.asStack())
                .inputItems(TagPrefix.gem, GTMaterials.NetherStar)
                .inputItems(TagPrefix.block, GTOMaterials.Enderium)
                .outputItems(GTOItems.DRACONIC_CORE.asStack(2))
                .EUt(7864320)
                .duration(400)
                .save();
    }
}
