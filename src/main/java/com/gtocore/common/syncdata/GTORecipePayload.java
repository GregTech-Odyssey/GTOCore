package com.gtocore.common.syncdata;

import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.utils.RLUtils;

import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.content.ContentInner;
import com.gregtechceu.gtceu.api.recipe.info.FluidRecipeInfo;
import com.gregtechceu.gtceu.api.recipe.info.ItemRecipeInfo;
import com.gregtechceu.gtceu.api.recipe.info.RecipeInfo;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.ItemIngredient;

import net.minecraft.nbt.*;
import net.minecraft.network.FriendlyByteBuf;

import com.gto.datasynclib.datasream.DataComponentMap;
import com.lowdragmc.lowdraglib.syncdata.payload.ObjectTypedPayload;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class GTORecipePayload extends ObjectTypedPayload<GTRecipe> {

    @Override
    public Tag serializeNBT() {
        return GTRecipe.toNbt(payload);
    }

    @Override
    public void deserializeNBT(Tag tag) {
        if (tag instanceof ByteArrayTag arrayTag) {
            payload = GTRecipe.fromNbt(arrayTag);
        } else if (tag instanceof CompoundTag compoundTag) {
            var definition = compoundTag.get("d") instanceof StringTag stringTag ? RecipeBuilder.get(RLUtils.parse(stringTag.getAsString())) : GTRecipe.EMPTY.definition;
            if (definition == null) definition = GTRecipe.EMPTY.definition;
            var data = new DataComponentMap();
            var duration = compoundTag.getInt("duration");
            var tier = compoundTag.getInt("tier");
            var eu = compoundTag.getLong("eu");
            List<Content<ItemIngredient>> itemInput = compoundTag.get("inputs") instanceof CompoundTag i ? fromNbt(ItemRecipeInfo.INSTANCE, i) : Collections.emptyList();
            List<Content<ItemIngredient>> itemOutput = compoundTag.get("outputs") instanceof CompoundTag i ? fromNbt(ItemRecipeInfo.INSTANCE, i) : Collections.emptyList();
            List<Content<FluidIngredient>> fluidInput = compoundTag.get("inputs") instanceof CompoundTag i ? fromNbt(FluidRecipeInfo.INSTANCE, i) : Collections.emptyList();
            List<Content<FluidIngredient>> fluidOutput = compoundTag.get("outputs") instanceof CompoundTag i ? fromNbt(FluidRecipeInfo.INSTANCE, i) : Collections.emptyList();
            payload = new GTRecipe(definition, itemInput, itemOutput, fluidInput, fluidOutput, data, eu, tier, duration);
        }
    }

    private static <T extends ContentInner> List<Content<T>> fromNbt(RecipeInfo capability, CompoundTag tag) {
        if (tag.tags.get(capability.name) instanceof ListTag listTag) {
            var list = new ArrayList<Content<T>>();
            for (var t : listTag) {
                var content = fromNbtContent(capability, t);
                if (content != null) {
                    list.add((Content<T>) content);
                }
            }
            if (!list.isEmpty()) return list;
        }
        return Collections.emptyList();
    }

    @Nullable
    private static <T extends ContentInner> Content<T> fromNbtContent(RecipeInfo capability, @Nullable Tag tag) {
        if (tag instanceof CompoundTag compoundTag && compoundTag.tags.get("content") instanceof CompoundTag content) {
            var ingredient = capability == ItemRecipeInfo.INSTANCE ? ItemIngredient.fromNbt(content) : FluidIngredient.fromNbt(content);
            if (ingredient instanceof ContentInner inner && !inner.isEmpty()) return new Content(ingredient, getChance(compoundTag), getTierChanceBoost(compoundTag));
        }
        return null;
    }

    private static int getChance(CompoundTag tag) {
        if (tag.tags.get("chance") instanceof IntTag chance) {
            return chance.getAsInt();
        }
        return Content.MAX_CHANCE;
    }

    private static int getTierChanceBoost(CompoundTag tag) {
        if (tag.tags.get("tierChanceBoost") instanceof IntTag tierChanceBoost) {
            return tierChanceBoost.getAsInt();
        }
        return 0;
    }

    @Override
    public void writePayload(FriendlyByteBuf buf) {
        GTRecipe.STREAM_CODEC.encode(buf, payload);
    }

    @Override
    public void readPayload(FriendlyByteBuf buf) {
        if (buf.isReadable()) {
            payload = GTRecipe.STREAM_CODEC.decode(buf);
        }
    }
}
