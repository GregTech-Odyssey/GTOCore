package com.gtocore.data.recipe.ae2;

import com.gtocore.common.data.GTOItems;
import com.gtocore.integration.ae.wtlib.WFTMenu;
import com.gtocore.integration.ae.wtlib.WRTMenu;

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
    private final static ResourceLocation wrtId = GTOCore.id(WFTMenu.ID);
    private final static ResourceLocation wftId = GTOCore.id(WRTMenu.ID);

    public static void init() {
        // Upgrade
        GTRecipes.RECIPE_MAP.put(wtId, new Upgrade(Ingredient.of(GTOItems.WIRELESS_ME2IN1), Wireless.ID, wtId));
        GTRecipes.RECIPE_MAP.put(wrtId, new Upgrade(Ingredient.of(GTOItems.WIRELESS_WRT), WRTMenu.ID, wrtId));
        GTRecipes.RECIPE_MAP.put(wftId, new Upgrade(Ingredient.of(GTOItems.WIRELESS_WFT), WFTMenu.ID, wftId));

        // Combine
        combineXWith(wtId, "crafting", AEItems.WIRELESS_CRAFTING_TERMINAL);
        combineXWith(wtId, "pattern_encoding", AE2wtlib.PATTERN_ENCODING_TERMINAL);
        combineXWith(wrtId, "crafting", AEItems.WIRELESS_CRAFTING_TERMINAL);
        combineXWith(wrtId, "pattern_encoding", AE2wtlib.PATTERN_ENCODING_TERMINAL);
        combineXWith(wftId, "crafting", AEItems.WIRELESS_CRAFTING_TERMINAL);
        combineXWith(wftId, "pattern_encoding", AE2wtlib.PATTERN_ENCODING_TERMINAL);
    }

    private static void combineXWith(ResourceLocation wtId, String terminalName, ItemLike terminalItem) {
        var id = wtId.withSuffix("_combined_with_" + terminalName);
        GTRecipes.RECIPE_MAP.put(id, new Combine(Ingredient.of(GTOItems.WIRELESS_ME2IN1), Ingredient.of(terminalItem), Wireless.ID, terminalName, id));
    }
}
