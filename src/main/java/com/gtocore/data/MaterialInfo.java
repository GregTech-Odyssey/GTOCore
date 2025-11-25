package com.gtocore.data;

import com.gregtechceu.gtceu.api.data.chemical.material.stack.ItemMaterialInfo;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.data.recipe.GTCraftingComponents;

import static com.gregtechceu.gtceu.api.GTValues.M;
import static com.gregtechceu.gtceu.api.data.chemical.material.ItemMaterialData.registerMaterialInfo;

final class MaterialInfo {

    static void init() {
        for (int tier = 0; tier < 15; tier++) {
            registerMaterialInfo(GTMachines.HULL[tier].get(), new ItemMaterialInfo(
                    new MaterialStack(((MaterialEntry) GTCraftingComponents.PLATE.get(tier)).material(), M << 3),
                    new MaterialStack(((MaterialEntry) GTCraftingComponents.CABLE.get(tier)).material(), M << 1)));
        }
    }
}
