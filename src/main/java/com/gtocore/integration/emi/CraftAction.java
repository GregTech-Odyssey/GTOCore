package com.gtocore.integration.emi;

import com.lowdragmc.lowdraglib.gui.modular.ModularUIContainer;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.EmiCraftContext;

import static com.gtocore.integration.emi.GTEmiRecipeHandler.canCraftOverrides;

@FunctionalInterface
public interface CraftAction {

    boolean craft(EmiRecipe recipe, EmiCraftContext<ModularUIContainer> context, boolean simulate);

    static void registerCanCraftOverride(CraftAction canCraft) {
        canCraftOverrides.add(canCraft);
    }
}
