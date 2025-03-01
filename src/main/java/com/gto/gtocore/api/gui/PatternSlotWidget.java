package com.gto.gtocore.api.gui;

import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.items.IItemHandlerModifiable;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.config.EmiConfig;
import dev.emi.emi.runtime.EmiFavorites;
import dev.emi.emi.screen.EmiScreenManager;

import java.util.List;

public final class PatternSlotWidget extends SlotWidget {

    public final EmiIngredient ingredient;

    public PatternSlotWidget(IItemHandlerModifiable itemHandler, int slotIndex, int xPosition, int yPosition) {
        super(itemHandler, slotIndex, xPosition, yPosition, false, false);
        setClientSideWidget();
        setBackgroundTexture(ColorPattern.T_GRAY.rectTexture());
        ingredient = EmiIngredient.of(Ingredient.of(itemHandler.getStackInSlot(0)));
    }

    @Override
    public Object getXEIIngredientOverMouse(double mouseX, double mouseY) {
        if (isMouseOverElement(mouseX, mouseY)) {
            return ingredient;
        }
        return null;
    }

    @Override
    public List<Object> getXEIIngredients() {
        return List.of(ingredient);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean value = isMouseOverElement(mouseX, mouseY);
        if (value) {
            if (button == 0) {
                EmiApi.displayRecipes(ingredient);
            } else if (button == 1) {
                EmiApi.displayUses(ingredient);
            }
        }
        return value;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!isMouseOverElement(EmiScreenManager.lastMouseX, EmiScreenManager.lastMouseY)) {
            return false;
        }
        if (EmiConfig.favorite.matchesKey(keyCode, scanCode)) {
            EmiFavorites.addFavorite(ingredient);
        } else if (EmiConfig.viewUses.matchesKey(keyCode, scanCode)) {
            EmiApi.displayUses(ingredient);
        } else if (EmiConfig.viewRecipes.matchesKey(keyCode, scanCode)) {
            EmiApi.displayRecipes(ingredient);
        }
        return true;
    }
}
