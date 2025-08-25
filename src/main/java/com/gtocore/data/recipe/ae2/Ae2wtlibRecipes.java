package com.gtocore.data.recipe.ae2;

import com.gtocore.common.data.GTOItems;

import com.gtolib.GTOCore;
import com.gtolib.ae2.me2in1.Wireless;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import appeng.core.definitions.AEItems;
import com.google.gson.JsonObject;
import de.mari_023.ae2wtlib.AE2wtlib;
import de.mari_023.ae2wtlib.wut.recipe.Combine;
import de.mari_023.ae2wtlib.wut.recipe.Upgrade;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

final class Ae2wtlibRecipes {

    private final static ResourceLocation wtId = GTOCore.id("me2in1");

    static void init(Consumer<FinishedRecipe> provider) {
        // Upgrade
        provider.accept(new FinishedRecipe() {

            private final Upgrade recipe = new Upgrade(Ingredient.of(GTOItems.WIRELESS_ME2IN1), Wireless.ID, wtId);

            @Override
            public void serializeRecipeData(@NotNull JsonObject jsonObject) {
                jsonObject.add("terminal", recipe.getTerminal().toJson());
                jsonObject.addProperty("terminalName", recipe.getTerminalName());
            }

            @Override
            public @NotNull ResourceLocation getId() {
                return wtId;
            }

            @Override
            public @NotNull RecipeSerializer<?> getType() {
                return Upgrade.serializer;
            }

            @Override
            public @Nullable JsonObject serializeAdvancement() {
                return null;
            }

            @Override
            public @Nullable ResourceLocation getAdvancementId() {
                return null;
            }
        });

        // Combine
        combineMe2in1With(provider, "crafting", AEItems.WIRELESS_CRAFTING_TERMINAL);
        combineMe2in1With(provider, "pattern_encoding", AE2wtlib.PATTERN_ENCODING_TERMINAL);
    }

    private static void combineMe2in1With(Consumer<FinishedRecipe> recipeAdder, String terminalName, ItemLike terminalItem) {
        recipeAdder.accept(new FinishedRecipe() {

            private final Combine recipe = new Combine(
                    Ingredient.of(GTOItems.WIRELESS_ME2IN1), Ingredient.of(terminalItem),
                    Wireless.ID, "pattern_encoding", wtId.withSuffix("_combined_with_" + terminalName));

            @Override
            public void serializeRecipeData(@NotNull JsonObject jsonObject) {
                jsonObject.add("terminalA", recipe.getTerminalA().toJson());
                jsonObject.add("terminalB", recipe.getTerminalB().toJson());
                jsonObject.addProperty("terminalAName", recipe.getTerminalAName());
                jsonObject.addProperty("terminalBName", recipe.getTerminalBName());
            }

            @Override
            public @NotNull ResourceLocation getId() {
                return wtId.withSuffix("_combined_with_" + terminalName);
            }

            @Override
            public @NotNull RecipeSerializer<?> getType() {
                return Combine.serializer;
            }

            @Override
            public @Nullable JsonObject serializeAdvancement() {
                return null;
            }

            @Override
            public @Nullable ResourceLocation getAdvancementId() {
                return null;
            }
        });
    }
}
