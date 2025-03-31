package com.gto.gtocore.integration.emi.oreprocessing;

import com.gto.gtocore.GTOCore;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;

import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;

public class OreProcessingEmiCategory extends EmiRecipeCategory {

    public static final OreProcessingEmiCategory CATEGORY = new OreProcessingEmiCategory();

    public OreProcessingEmiCategory() {
        super(GTOCore.id("ore_processing_diagram"), EmiStack.of(Items.RAW_IRON));
    }

    public static void registerDisplays(EmiRegistry registry) {
        for (Material mat : GTCEuAPI.materialManager.getRegisteredMaterials()) {
            if (mat.hasProperty(PropertyKey.ORE) && !mat.hasFlag(MaterialFlags.NO_ORE_PROCESSING_TAB)) {
                registry.addRecipe(
                        new OreProcessingEmiRecipe(mat));
            }
        }
    }

    @Override
    public Component getName() {
        return Component.translatable("gtceu.jei.ore_processing_diagram");
    }
}
