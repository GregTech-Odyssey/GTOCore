package com.gto.gtocore.api.playerskill.api;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.gto.gtocore.GTOCore;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class UtilsModels {
    public static <T extends Item> void createMultiLayerModel(
            DataGenContext<Item, T> ctx,
            RegistrateItemModelProvider prov,
            String... texturePaths) {

        ItemModelBuilder modelBuilder = prov.getBuilder(ctx.getName())
                .parent(new ModelFile.UncheckedModelFile("item/generated"));

        // 添加所有纹理层
        for (int i = 0; i < texturePaths.length; i++) {
            modelBuilder.texture("layer" + i, texturePaths[i]);
        }
    }
}
