package com.gto.gtocore.data.recipe.generated;

import com.gto.gtocore.GTOCore;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.WireProperties;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.data.recipes.FinishedRecipe;

import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gto.gtocore.common.data.GTORecipeTypes.*;

interface GTOWireCombiningHandler {

    Map<TagPrefix, TagPrefix> cableToWireMap = ImmutableMap.of(
            cableGtSingle, wireGtSingle,
            cableGtDouble, wireGtDouble,
            cableGtQuadruple, wireGtQuadruple,
            cableGtOctal, wireGtOctal,
            cableGtHex, wireGtHex);

    TagPrefix[] WIRE_DOUBLING_ORDER = new TagPrefix[] {
            wireGtSingle, wireGtDouble, wireGtQuadruple, wireGtOctal, wireGtHex
    };

    static void run(@NotNull Consumer<FinishedRecipe> provider, @NotNull Material material) {
        WireProperties property = material.getProperty(PropertyKey.WIRE);
        if (property == null) {
            return;
        }
        processWireCompression(material, property, provider);

        for (TagPrefix cablePrefix : cableToWireMap.keySet()) {
            processCableStripping(cablePrefix, material, property, provider);
        }
    }

    private static void processWireCompression(Material material, WireProperties property,
                                               Consumer<FinishedRecipe> provider) {
        if (!wireGtSingle.shouldGenerateRecipes(material)) {
            return;
        }
        int mass = (int) material.getMass();
        for (int startTier = 0; startTier < 4; startTier++) {
            for (int i = 1; i < 5 - startTier; i++) {
                LOOM_RECIPES.recipeBuilder(GTOCore.id("loom_" + material.getName() + "_wires_" + i + "_" + startTier))
                        .inputItems(WIRE_DOUBLING_ORDER[startTier], material, 1 << i)
                        .circuitMeta(1 << i)
                        .outputItems(WIRE_DOUBLING_ORDER[startTier + i], material, 1)
                        .EUt(7)
                        .duration(mass * i)
                        .save();
            }
        }

        if (property.getVoltage() < 33) {
            COMPRESSOR_RECIPES.recipeBuilder(GTOCore.id(material.getName() + "_wires"))
                    .inputItems(WIRE_DOUBLING_ORDER[0], material, 2)
                    .outputItems(WIRE_DOUBLING_ORDER[1], material, 1)
                    .EUt(30)
                    .duration(mass)
                    .save();
        }

        for (int i = 1; i < 5; i++) {
            UNPACKER_RECIPES.recipeBuilder(GTOCore.id("pack_" + material.getName() + "_wires_" + i + "_single"))
                    .inputItems(WIRE_DOUBLING_ORDER[i], material, 1)
                    .outputItems(WIRE_DOUBLING_ORDER[0], material, 1 << i)
                    .duration(mass * i)
                    .save();
        }
    }

    private static void processCableStripping(TagPrefix prefix, Material material, WireProperties property, Consumer<FinishedRecipe> provider) {
        if (!prefix.shouldGenerateRecipes(material)) {
            return;
        }
        Material rubber = GTMaterials.Rubber;
        int voltageTier = GTUtil.getTierByVoltage(property.getVoltage());
        if (voltageTier > GTValues.UV) {
            rubber = GTMaterials.StyreneButadieneRubber;
        } else if (voltageTier > GTValues.EV) {
            rubber = GTMaterials.SiliconeRubber;
        }
        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("strip_" + material.getName() + "_" + prefix.name.toLowerCase()))
                .inputItems(prefix, material)
                .outputItems(cableToWireMap.get(prefix), material)
                .outputItems(TagPrefix.plate, rubber,
                        (int) (prefix.secondaryMaterials().get(0).amount() / GTValues.M))
                .duration(100).EUt(GTValues.VA[GTValues.ULV])
                .save();
    }
}
