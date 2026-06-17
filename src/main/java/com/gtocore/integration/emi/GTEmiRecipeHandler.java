package com.gtocore.integration.emi;

import net.minecraft.world.inventory.Slot;

import com.lowdragmc.lowdraglib.gui.modular.ModularUIContainer;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.EmiCraftContext;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;

import java.util.List;

final class GTEmiRecipeHandler implements StandardRecipeHandler<ModularUIContainer> {

    static final List<CraftAction> canCraftOverrides = new java.util.ArrayList<>();

    @Override
    public List<Slot> getInputSources(ModularUIContainer handler) {
        return handler.getModularUI().getSlotMap().values().stream()
                .filter(e -> e.getIngredientIO() == IngredientIO.INPUT || e.isPlayerContainer || e.isPlayerHotBar)
                .map(SlotWidget::getHandler)
                .toList();
    }

    @Override
    public List<Slot> getCraftingSlots(ModularUIContainer handler) {
        return handler.getModularUI().getSlotMap().values().stream()
                .filter(e -> e.getIngredientIO() == IngredientIO.INPUT)
                .map(SlotWidget::getHandler)
                .toList();
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe instanceof GTEMIRecipe;
    }

    @Override
    public boolean canCraft(EmiRecipe recipe, EmiCraftContext<ModularUIContainer> context) {
        for (CraftAction override : canCraftOverrides) {
            if (override.craft(recipe, context, true)) {
                return true;
            }
        }
        return StandardRecipeHandler.super.canCraft(recipe, context);
    }

    @Override
    public boolean craft(EmiRecipe recipe, EmiCraftContext<ModularUIContainer> context) {
        for (CraftAction override : canCraftOverrides) {
            if (override.craft(recipe, context, true)) {
                return override.craft(recipe, context, false);
            }
        }
        return StandardRecipeHandler.super.craft(recipe, context);
    }
}
