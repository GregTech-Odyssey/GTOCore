package com.gtocore.common.syncdata;

import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeBuilder;

import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import com.lowdragmc.lowdraglib.syncdata.payload.ObjectTypedPayload;

public final class GTORecipePayload extends ObjectTypedPayload<GTRecipe> {

    @Override
    public Tag serializeNBT() {
        return Recipe.of(payload).serializeNBT();
    }

    @Override
    public void deserializeNBT(Tag tag) {
        payload = Recipe.deserializeNBT(tag);
    }

    @Override
    public void writePayload(FriendlyByteBuf buf) {
        var recipe = (Recipe) payload;
        buf.writeResourceLocation(recipe.id);
        if (recipe.root) {
            buf.writeBoolean(true);
        } else {
            buf.writeBoolean(false);
            Recipe.SERIALIZER.toNetwork(buf, recipe);
            buf.writeInt(recipe.ocLevel);
            buf.writeLong(recipe.parallels);
            if (recipe.rootRecipe != null) buf.writeResourceLocation(recipe.rootRecipe.id);
        }
    }

    @Override
    public void readPayload(FriendlyByteBuf buf) {
        var id = buf.readResourceLocation();
        if (buf.readBoolean()) {
            payload = RecipeBuilder.RECIPE_MAP.get(id);
        } else if (buf.isReadable()) {
            var recipe = Recipe.SERIALIZER.fromNetwork(id, buf);
            if (buf.isReadable()) {
                recipe.ocLevel = buf.readInt();
                recipe.setParallels(buf.readLong());
                if (buf.isReadable()) {
                    recipe.rootRecipe = RecipeBuilder.RECIPE_MAP.get(buf.readResourceLocation());
                }
            }
            payload = recipe;
        }
    }
}
