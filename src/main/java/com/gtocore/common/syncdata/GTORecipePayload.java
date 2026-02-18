package com.gtocore.common.syncdata;

import com.gtolib.api.recipe.Recipe;

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
        Recipe.SERIALIZER.toNetwork(buf, recipe);
        buf.writeInt(recipe.ocLevel);
        buf.writeLong(recipe.parallels);
    }

    @Override
    public void readPayload(FriendlyByteBuf buf) {
        var id = buf.readResourceLocation();
        if (buf.isReadable()) {
            var recipe = Recipe.SERIALIZER.fromNetwork(buf);
            if (buf.isReadable()) {
                recipe.ocLevel = buf.readInt();
                recipe.setParallels(buf.readLong());
            }
            payload = recipe;
        }
    }
}
