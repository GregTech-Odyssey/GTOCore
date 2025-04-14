package com.gto.gtocore.api.playerskill.api;

import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;

public class TintableModelUtils {
    public static <T extends Item> void createTintableModel(DataGenContext<Item, T> ctx, RegistrateItemModelProvider prov, String... texturePaths) {
        createTintableModel(ctx, "item/generated", prov, texturePaths);
    }

    public static <T extends Item> void createTintableModel(DataGenContext<Item, T> ctx, String parentModel, RegistrateItemModelProvider prov, String... texturePaths) {
        ItemModelBuilder builder = prov.getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile(parentModel));
        for (int i = 0; i < texturePaths.length; i++) builder.texture("layer" + i, prov.modLoc(texturePaths[i]));
        JsonObject json = builder.toJson();
        JsonArray tints = new JsonArray();
        for (int i = 0; i < texturePaths.length; i++) tints.add(i);
        json.add("tintindexes", tints);
    }
}