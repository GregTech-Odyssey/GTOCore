package com.gtocore.mixin.gtm.registry;

import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.registry.GTRegistry;

import net.minecraft.resources.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(GTRegistry.String.class)
public abstract class GTRegistryStringMixin<V> extends GTRegistry<String, V> {

    protected GTRegistryStringMixin(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    public <T extends V> T register(java.lang.String key, T value) {
        if (value instanceof Material || value instanceof RecipeCapability) registry.put(key, value);
        return value;
    }
}
