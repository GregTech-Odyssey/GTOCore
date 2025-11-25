package com.gtocore.data.recipe.ae2;

import com.gtocore.common.data.GTOItems;

import com.gtolib.GTOCore;
import com.gtolib.api.ae2.me2in1.Wireless;

import com.gregtechceu.gtceu.common.data.GTRecipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import appeng.core.definitions.AEItems;
import de.mari_023.ae2wtlib.AE2wtlib;
import de.mari_023.ae2wtlib.wut.recipe.Combine;
import de.mari_023.ae2wtlib.wut.recipe.Upgrade;

public final class Ae2wtlibRecipes {

    private final static ResourceLocation wtId = GTOCore.id("me2in1");

    public static void init() {
        // Upgrade
        GTRecipes.RECIPE_MAP.put(wtId, new Upgrade(Ingredient.of(GTOItems.WIRELESS_ME2IN1), Wireless.ID, wtId));

        // Combine
        combineMe2in1With("crafting", AEItems.WIRELESS_CRAFTING_TERMINAL);
        combineMe2in1With("pattern_encoding", AE2wtlib.PATTERN_ENCODING_TERMINAL);
    }

    private static void combineMe2in1With(String terminalName, ItemLike terminalItem) {
        var id = wtId.withSuffix("_combined_with_" + terminalName);
        GTRecipes.RECIPE_MAP.put(id, new Combine(Ingredient.of(GTOItems.WIRELESS_ME2IN1), Ingredient.of(terminalItem), Wireless.ID, terminalName, id));
    }
}
