package com.gtocore.client.gui;

import com.gtocore.integration.emi.multipage.MultiblockInfoEmiRecipe;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import com.lowdragmc.lowdraglib.jei.ModularWrapper;
import dev.emi.emi.runtime.EmiHistory;
import dev.emi.emi.screen.RecipeScreen;
import org.jetbrains.annotations.NotNull;

public final class MultiblockPreviewScreen extends ModularWrapper<PatternPreview> {

    private static final int MIN_WIDTH = 220;
    private static final int MIN_HEIGHT = 160;

    private final Screen parent;
    private final MultiblockInfoEmiRecipe recipe;
    private boolean cleanedUp;

    public MultiblockPreviewScreen(Screen parent, MultiblockInfoEmiRecipe recipe) {
        super(createPreview(parent, recipe));
        this.parent = parent;
        this.recipe = recipe;
        setShouldRenderTooltips(true);
    }

    private static PatternPreview createPreview(Screen parent, MultiblockInfoEmiRecipe recipe) {
        Minecraft minecraft = Minecraft.getInstance();
        int width = Math.max(MIN_WIDTH, minecraft.getWindow().getGuiScaledWidth());
        int height = Math.max(MIN_HEIGHT, minecraft.getWindow().getGuiScaledHeight());
        return PatternPreview.getFullscreenPatternWidget(
                recipe, width, height,
                () -> minecraft.setScreen(parent));
    }

    static Screen prepareParent(Screen screen) {
        if (screen instanceof MultiblockPreviewScreen previewScreen) {
            return previewScreen.parent;
        }
        if (screen instanceof RecipeScreen recipeScreen &&
                recipeScreen.old instanceof MultiblockPreviewScreen previewScreen) {
            AbstractContainerScreen<?> backingScreen = previewScreen.getBackingContainerScreen();
            if (backingScreen != null) {
                recipeScreen.old = backingScreen;
                EmiHistory.clear();
            }
        }
        return screen;
    }

    private AbstractContainerScreen<?> getBackingContainerScreen() {
        if (parent instanceof RecipeScreen recipeScreen) {
            return recipeScreen.old;
        }
        if (parent instanceof AbstractContainerScreen<?> containerScreen) {
            return containerScreen;
        }
        return null;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.fill(0, 0, width, height, 0xE0101010);
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void resize(@NotNull Minecraft minecraft, int width, int height) {
        this.minecraft = minecraft;
        this.font = minecraft.font;
        this.width = width;
        this.height = height;

        int previewWidth = Math.max(MIN_WIDTH, width);
        int previewHeight = Math.max(MIN_HEIGHT, height);
        getWidget().resizeFullscreen(previewWidth, previewHeight);
        modularUI.setSize(previewWidth, previewHeight);
        modularUI.updateScreenSize(width, height);
    }

    @Override
    public void onClose() {
        cleanupOverlay();
        minecraft.setScreen(parent);
    }

    @Override
    public void removed() {
        cleanupOverlay();
        super.removed();
    }

    private void cleanupOverlay() {
        if (cleanedUp) return;
        cleanedUp = true;
        getWidget().restoreOverlayBlocks();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
