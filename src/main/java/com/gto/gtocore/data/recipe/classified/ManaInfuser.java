package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.utils.TagUtils;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.Items;

import static com.gto.gtocore.common.data.GTORecipeTypes.MANA_INFUSER_RECIPES;

interface ManaInfuser {

    static void init() {
        MANA_INFUSER_RECIPES.builder("mana_dust")
                .inputItems(TagUtils.createTGTag("dusts"))
                .outputItems(TagPrefix.dust, GTOMaterials.Mana)
                .duration(20)
                .MANAt(1)
                .save();

        MANA_INFUSER_RECIPES.builder("mana_string")
                .inputItems(Items.STRING)
                .outputItems("botania:mana_string")
                .duration(20)
                .MANAt(1)
                .save();

        MANA_INFUSER_RECIPES.builder("managlass")
                .inputItems(TagPrefix.block, GTMaterials.Glass)
                .outputItems(TagPrefix.block, GTOMaterials.ManaGlass)
                .duration(20)
                .MANAt(1)
                .save();

        MANA_INFUSER_RECIPES.builder("manasteel")
                .inputItems(TagPrefix.ingot, GTMaterials.Steel)
                .outputItems(TagPrefix.ingot, GTOMaterials.Manasteel)
                .duration(20)
                .MANAt(2)
                .save();

        MANA_INFUSER_RECIPES.builder("mana_pearl")
                .inputItems("torchmaster:frozen_pearl")
                .outputItems("botania:mana_pearl")
                .duration(20)
                .MANAt(8)
                .save();

        MANA_INFUSER_RECIPES.builder("mana_diamond")
                .inputItems(TagPrefix.gem, GTMaterials.Diamond)
                .outputItems(TagPrefix.gem, GTOMaterials.ManaDiamond)
                .duration(20)
                .MANAt(16)
                .save();

        MANA_INFUSER_RECIPES.builder("pulsatingalloy")
                .inputItems(GTOTagPrefix.SUPERCONDUCTOR_BASE, GTOMaterials.PulsatingAlloy)
                .outputItems(TagPrefix.wireGtSingle, GTOMaterials.PulsatingAlloy)
                .duration(20)
                .MANAt(8)
                .save();

        MANA_INFUSER_RECIPES.builder("conductivealloy")
                .inputItems(GTOTagPrefix.SUPERCONDUCTOR_BASE, GTOMaterials.ConductiveAlloy)
                .outputItems(TagPrefix.wireGtSingle, GTOMaterials.ConductiveAlloy)
                .duration(20)
                .MANAt(32)
                .save();

        MANA_INFUSER_RECIPES.builder("energeticalloy")
                .inputItems(GTOTagPrefix.SUPERCONDUCTOR_BASE, GTOMaterials.EnergeticAlloy)
                .outputItems(TagPrefix.wireGtSingle, GTOMaterials.EnergeticAlloy)
                .duration(20)
                .MANAt(128)
                .save();

        MANA_INFUSER_RECIPES.builder("vibrantalloy")
                .inputItems(GTOTagPrefix.SUPERCONDUCTOR_BASE, GTOMaterials.VibrantAlloy)
                .outputItems(TagPrefix.wireGtSingle, GTOMaterials.VibrantAlloy)
                .duration(20)
                .MANAt(512)
                .save();

        MANA_INFUSER_RECIPES.builder("endsteel")
                .inputItems(GTOTagPrefix.SUPERCONDUCTOR_BASE, GTOMaterials.EndSteel)
                .outputItems(TagPrefix.wireGtSingle, GTOMaterials.EndSteel)
                .duration(20)
                .MANAt(2048)
                .save();
    }
}
