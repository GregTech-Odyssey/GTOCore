package com.gtocore.data.recipe.generated;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.data.recipe.generated.DecompositionRecipeHandler;
import com.gregtechceu.gtceu.data.recipe.generated.PolarizingRecipeHandler;
import com.gregtechceu.gtceu.data.recipe.generated.ToolRecipeHandler;

public final class ForEachMaterial {

    public static void init() {
        for (Material material : GTCEuAPI.materialManager.getRegisteredMaterials()) {
            if (material.hasFlag(MaterialFlags.NO_UNIFICATION)) {
                continue;
            }

            DecompositionRecipeHandler.run(material);
            if (!material.hasProperty(PropertyKey.DUST)) continue;
            ToolRecipeHandler.run(material);
            PolarizingRecipeHandler.run(material);
            GTOMaterialRecipeHandler.run(material);
            GTOOreRecipeHandler.run(material);
            GTOPartsRecipeHandler.run(material);
            GTOPipeRecipeHandler.run(material);
            GTORecyclingRecipeHandler.run(material);
            GTOWireCombiningHandler.run(material);
            GTOWireRecipeHandler.run(material);
            GTODisposableToolHandler.run(material);
        }
    }
}
