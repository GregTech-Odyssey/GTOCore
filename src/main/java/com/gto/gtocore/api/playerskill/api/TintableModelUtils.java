package com.gto.gtocore.api.playerskill.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;


public class TintableModelUtils {

    public static <T extends Item> void createTintableModel(
            DataGenContext<Item, T> ctx,
            RegistrateItemModelProvider prov,
            String... texturePaths) {
        createTintableModel(ctx, "item/generated",prov , texturePaths);
    }
    public static <T extends Item> void createTintableModel(
            DataGenContext<Item, T> ctx,
            String parentModel,
            RegistrateItemModelProvider prov,
            String... texturePaths) {
        ItemModelBuilder modelBuilder = prov.getBuilder(ctx.getName())
                .parent(new ModelFile.UncheckedModelFile(parentModel));
        for (int i = 0; i < texturePaths.length; i++) {
            modelBuilder.texture("layer" + i, prov.modLoc(texturePaths[i]));
        }
        addTintIndexesToModel(modelBuilder, texturePaths.length);
    }
    private static void addTintIndexesToModel(ItemModelBuilder modelBuilder, int layerCount) {
        JsonObject json = modelBuilder.toJson();
        JsonArray tintindexesJson = new JsonArray();
        for (int i = 0; i < layerCount; i++) {
            tintindexesJson.add(i);
        }
        json.add("tintindexes", tintindexesJson);
    }
}