package com.gtocore.integration.emi;

import appeng.client.gui.me.common.RepoSlot;
import appeng.integration.modules.jeirei.CraftingHelper;
import appeng.menu.me.items.CraftingTermMenu;
import appeng.menu.slot.CraftingMatrixSlot;
import appeng.menu.slot.CraftingTermSlot;
import appeng.menu.slot.RestrictedInputSlot;
import com.google.common.collect.Lists;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import dev.emi.emi.api.recipe.EmiPlayerInventory;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.recipe.handler.EmiCraftContext;
import dev.emi.emi.api.recipe.handler.EmiRecipeHandler;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.platform.EmiClient;
import dev.emi.emi.screen.RecipeScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CraftingTerminalRecipeHandler<T extends CraftingTermMenu> implements EmiRecipeHandler<T> {

    public List<Slot> getInputSources(CraftingTermMenu handler) {
        return handler.slots;
    }

    @Override
    public EmiPlayerInventory getInventory(AbstractContainerScreen<T> screen) {
        return new EmiPlayerInventory(getInputSources(screen.getMenu()).stream().map(Slot::getItem).map(EmiStack::of).toList());
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.getCategory() == VanillaEmiRecipeCategories.CRAFTING && recipe.supportsRecipeTree();
    }

    @Override
    public boolean canCraft(EmiRecipe recipe, EmiCraftContext<T> context) {
        return true;
    }

    @Override
    public boolean craft(EmiRecipe recipe, EmiCraftContext<T> context) {
        CraftingHelper.performTransfer(context.getScreenHandler(), recipe.getBackingRecipe(), false);
        if (Minecraft.getInstance().screen instanceof RecipeScreen e) {
            e.onClose();
        }
        return true;
    }
}
