package com.gto.gtocore.integration.emi.oreprocessing;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;

import net.minecraft.resources.ResourceLocation;

import com.lowdragmc.lowdraglib.emi.ModularEmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import org.jetbrains.annotations.Nullable;

public class OreProcessingEmiRecipe extends ModularEmiRecipe<OreByProductWidget> {

    final Material material;

    public OreProcessingEmiRecipe(Material material) {
        super(() -> new OreByProductWidget(material));
        this.material = material;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return OreProcessingEmiCategory.CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return material.getResourceLocation();
    }

    @Override
    public boolean supportsRecipeTree() {
        return false;
    }
}
