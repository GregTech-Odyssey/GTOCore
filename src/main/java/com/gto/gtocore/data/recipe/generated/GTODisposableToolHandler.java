package com.gto.gtocore.data.recipe.generated;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;

import com.tterrag.registrate.util.entry.ItemEntry;

import java.util.Map;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gto.gtocore.common.data.GTOItems.*;
import static com.gto.gtocore.common.data.GTORecipeTypes.FLUID_SOLIDFICATION_RECIPES;

public class GTODisposableToolHandler {

    static void run(Material material) {
        if (!material.hasProperty(PropertyKey.TOOL)) {
            return;
        }

        if (!material.hasFluid()) {
            return;
        }

        int durability = material.getProperty(PropertyKey.TOOL).getDurability();

        Map<ItemEntry, ItemEntry> toolToMoldMap = Map.of(
                DISPOSABLE_FILE, DISPOSABLE_FILE_MOLD,
                DISPOSABLE_WRENCH, DISPOSABLE_WRENCH_MOLD,
                DISPOSABLE_CROWBAR, DISPOSABLE_CROWBAR_MOLD,
                DISPOSABLE_WIRE_CUTTER, DISPOSABLE_WIRE_CUTTER_MOLD,
                DISPOSABLE_HAMMER, DISPOSABLE_HAMMER_MOLD,
                DISPOSABLE_MALLET, DISPOSABLE_MALLET_MOLD,
                DISPOSABLE_SCREWDRIVER, DISPOSABLE_SCREWDRIVER_MOLD,
                DISPOSABLE_SAW, DISPOSABLE_SAW_MOLD);

        Map<ItemEntry, Integer> toolToConsumeMap = Map.of(
                DISPOSABLE_FILE, 4,
                DISPOSABLE_WRENCH, 8,
                DISPOSABLE_CROWBAR, 3,
                DISPOSABLE_WIRE_CUTTER, 9,
                DISPOSABLE_HAMMER, 6,
                DISPOSABLE_MALLET, 6,
                DISPOSABLE_SCREWDRIVER, 4,
                DISPOSABLE_SAW, 4);

        for (ItemEntry disposableTool : toolToMoldMap.keySet()) {
            FLUID_SOLIDFICATION_RECIPES.recipeBuilder(material.getName() + "_to_${disposableTool.getName()}")
                    .notConsumable(toolToMoldMap.get(disposableTool))
                    .inputFluids(material.getFluid(L / 2))
                    .outputItems(disposableTool, durability / toolToConsumeMap.get(disposableTool))
                    .duration((int) material.getMass()).EUt(VA[ULV])
                    .save();
        }
    }
}
