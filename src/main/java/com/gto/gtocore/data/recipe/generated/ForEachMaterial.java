package com.gto.gtocore.data.recipe.generated;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.data.recipe.generated.DecompositionRecipeHandler;
import com.gregtechceu.gtceu.data.recipe.generated.PolarizingRecipeHandler;
import com.gregtechceu.gtceu.data.recipe.generated.ToolRecipeHandler;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

public interface ForEachMaterial {

    static void init(Consumer<FinishedRecipe> consumer) {
        for (Material material : GTCEuAPI.materialManager.getRegisteredMaterials()) {
            if (material.hasFlag(MaterialFlags.NO_UNIFICATION)) {
                continue;
            }

            DecompositionRecipeHandler.run(consumer, material);
            if (!material.hasProperty(PropertyKey.DUST)) continue;
            ToolRecipeHandler.run(consumer, material);
            PolarizingRecipeHandler.run(consumer, material);
            GTOMaterialRecipeHandler.run(consumer, material);
            GTOOreRecipeHandler.run(consumer, material);
            GTOPartsRecipeHandler.run(consumer, material);
            GTOPipeRecipeHandler.run(consumer, material);
            GTORecyclingRecipeHandler.run(consumer, material);
            GTOWireCombiningHandler.run(material);
            GTOWireRecipeHandler.run(consumer, material);
            GTODisposableToolHandler.run(material);
        }
    }
}
