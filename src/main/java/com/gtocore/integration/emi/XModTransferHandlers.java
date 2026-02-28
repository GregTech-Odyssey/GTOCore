package com.gtocore.integration.emi;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import appeng.core.localization.ItemModText;
import appeng.integration.modules.emi.AbstractRecipeHandler;
import appeng.integration.modules.emi.EmiUseCraftingRecipeHandler;
import appeng.util.CraftingRecipeUtil;
import com.glodblock.github.extendedae.api.CraftingMode;
import com.glodblock.github.extendedae.container.ContainerExCraftingTerminal;
import com.glodblock.github.extendedae.network.EPPNetworkHandler;
import com.glodblock.github.extendedae.xmod.jei.transfer.ExCraftingHelper;
import com.glodblock.github.glodium.network.packet.CGenericPacket;
import com.google.common.collect.ImmutableSet;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class XModTransferHandlers {

    public static class ExCraftingTransferHandler<T extends ContainerExCraftingTerminal> extends AbstractRecipeHandler<T> {

        ImmutableSet<EmiRecipeCategory> supportedCategories = ImmutableSet.of(
                VanillaEmiRecipeCategories.CRAFTING,
                VanillaEmiRecipeCategories.STONECUTTING,
                VanillaEmiRecipeCategories.SMITHING,
                VanillaEmiRecipeCategories.ANVIL_REPAIRING);

        public ExCraftingTransferHandler(Class<T> containerClass) {
            super(containerClass);
        }

        @Override
        public boolean supportsRecipe(EmiRecipe recipe) {
            // For actual crafting, we only support normal crafting recipes
            return supportedCategories.contains(recipe.getCategory());
        }

        @Override
        public Result transferRecipe(T container, @Nullable Recipe<?> obj, EmiRecipe emiRecipe, boolean doTransfer) {
            if (obj instanceof Recipe<?> recipe) {
                var type = recipe.getType();
                boolean craftMissing = AbstractContainerScreen.hasControlDown();
                if (type == RecipeType.CRAFTING) {
                    return this.transferCraft(container, recipe, emiRecipe, doTransfer);
                }
                if (type == RecipeType.STONECUTTING) {
                    return this.handlerRecipe(recipe, CraftingMode.STONECUTTER, getGuiSlotToIngredientMapPlain(recipe), container, 1, doTransfer, craftMissing);
                }
                if (type == RecipeType.SMITHING) {
                    return this.handlerRecipe(recipe, CraftingMode.SMITHING, getGuiSlotToIngredientMapPlain(recipe), container, 3, doTransfer, craftMissing);
                }
            }
            return Result.createFailed(ItemModText.INCOMPATIBLE_RECIPE.text());
        }

        private Result transferCraft(T menu, Recipe<?> recipeBase, EmiRecipe emiRecipe, boolean doTransfer) {
            // Recipe displays can be based on anything. Not just Recipe<?>
            Recipe<?> recipe = null;
            if (recipeBase instanceof Recipe<?>) {
                recipe = recipeBase;
            }

            boolean craftingRecipe = isCraftingRecipe(recipe, emiRecipe);
            if (!craftingRecipe) {
                return Result.createNotApplicable();
            }

            if (!fitsIn3x3Grid(recipe, emiRecipe)) {
                return Result.createFailed(ItemModText.RECIPE_TOO_LARGE.text());
            }

            if (recipe == null) {
                if (emiRecipe != null) {
                    recipe = createFakeRecipe(emiRecipe);
                }
            }

            // Find missing ingredient
            var slotToIngredientMap = getGuiSlotToIngredientMap(recipe);
            var missingSlots = menu.findMissingIngredients(slotToIngredientMap);

            if (missingSlots.missingSlots().size() == slotToIngredientMap.size()) {
                // All missing, can't do much...
                return Result.createFailed(ItemModText.NO_ITEMS.text(), missingSlots.missingSlots());
            }

            if (doTransfer) {
                this.switchMode(CraftingMode.CRAFTING);
                // Thank you RS for pioneering this amazing feature! :)
                boolean craftMissing = AbstractContainerScreen.hasControlDown();
                ExCraftingHelper.performTransfer(menu, recipe, 9, craftMissing);
            } else {
                if (missingSlots.anyMissingOrCraftable()) {
                    // Highlight the slots with missing ingredients
                    return new Result.PartiallyCraftable(missingSlots);
                }
            }

            // No error
            return Result.createSuccessful();
        }

        private Recipe<?> createFakeRecipe(EmiRecipe display) {
            var ingredients = NonNullList.withSize(CRAFTING_GRID_WIDTH * CRAFTING_GRID_HEIGHT,
                    Ingredient.EMPTY);

            for (int i = 0; i < Math.min(display.getInputs().size(), ingredients.size()); i++) {
                var ingredient = Ingredient.of(display.getInputs().get(i).getEmiStacks().stream()
                        .map(EmiStack::getItemStack)
                        .filter(is -> !is.isEmpty()));
                ingredients.set(i, ingredient);
            }

            return new ShapedRecipe(ResourceLocation.fromNamespaceAndPath("c", "null"), "", CraftingBookCategory.MISC, CRAFTING_GRID_WIDTH,
                    CRAFTING_GRID_HEIGHT, ingredients, ItemStack.EMPTY);
        }

        private Result handlerRecipe(Recipe<?> recipe, CraftingMode mode, Map<Integer, Ingredient> slotToIngredientMap, T menu, int recipeSize, boolean doTransfer, boolean craftMissing) {
            var missingSlots = menu.findMissingIngredients(slotToIngredientMap);
            if (missingSlots.missingSlots().size() == slotToIngredientMap.size()) {
                return new Result.PartiallyCraftable(missingSlots);
            }
            // Find missing ingredients and highlight the slots which have these
            if (doTransfer) {
                this.switchMode(mode);
                ExCraftingHelper.performTransfer(menu, recipe, recipeSize, craftMissing);
                if (mode == CraftingMode.STONECUTTER) {
                    EPPNetworkHandler.INSTANCE.sendToServer(new CGenericPacket("stonecutter_select", recipe.getId().toString()));
                }
            }
            // No error
            return Result.createSuccessful();
        }

        private void switchMode(CraftingMode mode) {
            EPPNetworkHandler.INSTANCE.sendToServer(new CGenericPacket("set_mode", mode.ordinal()));
        }

        private static Map<Integer, Ingredient> getGuiSlotToIngredientMapPlain(Recipe<?> recipe) {
            var inputs = CraftingRecipeUtil.getIngredients(recipe);
            Map<Integer, Ingredient> ingredientMap = new HashMap<>(inputs.size());
            for (int i = 0; i < inputs.size(); i++) {
                ingredientMap.put(i, inputs.get(i));
            }
            return ingredientMap;
        }

        private static Map<Integer, Ingredient> getGuiSlotToIngredientMap(Recipe<?> recipe) {
            return EmiUseCraftingRecipeHandler.getGuiSlotToIngredientMap(recipe);
        }
    }
}
