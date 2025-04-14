package com.gto.gtocore.api.playerskill.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TintableItemModelProvider implements DataProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final PackOutput packOutput;
    private final String modId;
    private final Map<String, ModelData> models = new HashMap<>();

    public TintableItemModelProvider(PackOutput packOutput, String modId) {
        this.packOutput = packOutput;
        this.modId = modId;
    }

    /**
     * 添加一个多层纹理物品模型，自动设置tintindexes
     */
    public TintableItemModelProvider addMultiLayerItem(String itemName, String... texturePaths) {
        return addMultiLayerItem(itemName, "item/generated", texturePaths);
    }

    /**
     * 添加一个多层纹理物品模型，自定义父模型，自动设置tintindexes
     */
    public TintableItemModelProvider addMultiLayerItem(String itemName, String parentModel, String... texturePaths) {
        models.put(itemName, new ModelData(parentModel, texturePaths, true));
        return this;
    }

    /**
     * 添加一个多层纹理物品模型，可选是否设置tintindexes
     */
    public TintableItemModelProvider addMultiLayerItem(String itemName, String parentModel, boolean tintAll, String... texturePaths) {
        models.put(itemName, new ModelData(parentModel, texturePaths, tintAll));
        return this;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        Path outputFolder = packOutput.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                .resolve(modId)
                .resolve("models/item");

        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (Map.Entry<String, ModelData> entry : models.entrySet()) {
            String itemName = entry.getKey();
            ModelData data = entry.getValue();

            JsonObject modelJson = new JsonObject();
            modelJson.addProperty("parent", data.parentModel);

            JsonObject texturesJson = new JsonObject();
            for (int i = 0; i < data.texturePaths.length; i++) {
                texturesJson.addProperty("layer" + i, modId + ":" + data.texturePaths[i]);
            }
            modelJson.add("textures", texturesJson);

            if (data.tintAll) {
                JsonArray tintindexesJson = new JsonArray();
                for (int i = 0; i < data.texturePaths.length; i++) {
                    tintindexesJson.add(i);
                }
                modelJson.add("tintindexes", tintindexesJson);
            }

            Path outputPath = outputFolder.resolve(itemName + ".json");
            futures.add(DataProvider.saveStable(cache, modelJson, outputPath));
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    @Override
    public String getName() {
        return modId + " tintable item models";
    }

    private static class ModelData {
        final String parentModel;
        final String[] texturePaths;
        final boolean tintAll;

        ModelData(String parentModel, String[] texturePaths, boolean tintAll) {
            this.parentModel = parentModel;
            this.texturePaths = texturePaths;
            this.tintAll = tintAll;
        }
    }
}