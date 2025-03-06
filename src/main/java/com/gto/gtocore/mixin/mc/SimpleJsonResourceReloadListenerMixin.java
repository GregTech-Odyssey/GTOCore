package com.gto.gtocore.mixin.mc;

import com.gto.gtocore.common.data.GTOLoots;
import com.gto.gtocore.common.data.GTORecipes;

import com.gregtechceu.gtceu.data.recipe.configurable.RecipeRemoval;

import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Mixin(value = SimpleJsonResourceReloadListener.class, priority = 0)
public class SimpleJsonResourceReloadListenerMixin {

    @Inject(method = "scanDirectory", at = @At("HEAD"), cancellable = true)
    private static void scanDirectory(ResourceManager resourceManager, String name, Gson gson, Map<ResourceLocation, JsonElement> output, CallbackInfo ci) {
        ci.cancel();
        Set<ResourceLocation> filters = null;
        switch (name) {
            case "advancements":
                return;
            case "recipes": {
                if (GTORecipes.cache) return;
                filters = new ObjectOpenHashSet<>(2048);
                RecipeRemoval.init(filters::add);
                GTORecipes.removal(filters);
                break;
            }
            case "loot_tables": {
                if (GTOLoots.cache) return;
                filters = new ObjectOpenHashSet<>();
                GTOLoots.removal(filters);
                break;
            }
        }
        FileToIdConverter filetoidconverter = FileToIdConverter.json(name);
        Map<ResourceLocation, Resource> resourceMap = filetoidconverter.listMatchingResources(resourceManager);
        for (Map.Entry<ResourceLocation, Resource> entry : resourceMap.entrySet()) {
            ResourceLocation resourcelocation = entry.getKey();
            ResourceLocation resourcelocation1 = filetoidconverter.fileToId(resourcelocation);
            if (filters != null && filters.contains(resourcelocation1)) continue;
            try {
                try (BufferedReader reader = entry.getValue().openAsReader()) {
                    JsonElement jsonelement = GsonHelper.fromJson(gson, reader, JsonElement.class);
                    JsonElement jsonelement1 = output.put(resourcelocation1, jsonelement);
                    if (jsonelement1 == null) continue;
                    throw new IllegalStateException("Duplicate data file ignored with ID " + resourcelocation1);
                }
            } catch (JsonParseException | IOException | IllegalArgumentException ignored) {}
        }
    }
}
