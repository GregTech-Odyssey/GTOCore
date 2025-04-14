package com.gto.gtocore.api.playerskill.api.dataGeneration;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraft.data.DataGenerator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DataGeneration {
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        String modId = "yourmodid";

        if (event.includeClient()) {
            generateItemModels(packOutput, modId);
        }
    }

    private static void generateItemModels(PackOutput packOutput, String modId) {
        generateTintableItemModel(packOutput, modId, "skill_gem", "item/generated",
                new String[] {"item/skill_gem_base", "item/skill_gem_border", "item/skill_gem_overlay"});

    }


    public static void generateTintableItemModel(PackOutput packOutput, String modId, String itemName,
                                                 String parentModel, String[] texturePaths) {
        // 方法实现（与之前提供的相同）
        JsonObject modelJson = new JsonObject();

        modelJson.addProperty("parent", parentModel != null ? parentModel : "item/generated");

        JsonObject texturesJson = new JsonObject();
        for (int i = 0; i < texturePaths.length; i++) {
            texturesJson.addProperty("layer" + i, modId + ":" + texturePaths[i]);
        }
        modelJson.add("textures", texturesJson);

        JsonArray tintindexesJson = new JsonArray();
        for (int i = 0; i < texturePaths.length; i++) {
            tintindexesJson.add(i);
        }
        modelJson.add("tintindexes", tintindexesJson);

        try {
            Path outputPath = packOutput.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                    .resolve(modId)
                    .resolve("models/item")
                    .resolve(itemName + ".json");

            Files.createDirectories(outputPath.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(outputPath)) {
                new GsonBuilder().setPrettyPrinting().create().toJson(modelJson, writer);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save model: " + e.getMessage());
        }
    }
}