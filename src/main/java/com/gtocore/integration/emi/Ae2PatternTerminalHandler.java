package com.gtocore.integration.emi;

import com.gtocore.integration.emi.multipage.MultiblockInfoEmiRecipe;

import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import appeng.integration.modules.jeirei.EncodingHelper;
import appeng.menu.me.items.PatternEncodingTermMenu;
import com.hepdd.gtmthings.api.misc.Hatch;
import com.hepdd.gtmthings.common.item.VirtualItemProviderBehavior;
import com.hepdd.gtmthings.data.CustomItems;
import dev.emi.emi.api.recipe.EmiPlayerInventory;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.EmiCraftContext;
import dev.emi.emi.api.recipe.handler.EmiRecipeHandler;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.recipe.EmiCookingRecipe;
import dev.emi.emi.recipe.EmiStonecuttingRecipe;
import dev.emi.emi.screen.RecipeScreen;
import vazkii.botania.client.integration.emi.BotaniaEmiRecipe;

import java.util.ArrayList;
import java.util.List;

final class Ae2PatternTerminalHandler<T extends PatternEncodingTermMenu> implements EmiRecipeHandler<T> {

    private List<Slot> getInputSources(T handler) {
        return handler.slots;
    }

    @Override
    public EmiPlayerInventory getInventory(AbstractContainerScreen<T> screen) {
        return new EmiPlayerInventory(getInputSources(screen.getMenu()).stream().map(Slot::getItem).map(EmiStack::of).toList());
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe instanceof GTEMIRecipe || recipe instanceof MultiblockInfoEmiRecipe || recipe instanceof EmiCookingRecipe || isCrafting(recipe) || recipe instanceof BotaniaEmiRecipe;
    }

    @Override
    public boolean canCraft(EmiRecipe recipe, EmiCraftContext<T> context) {
        return true;
    }

    @Override
    public boolean craft(EmiRecipe recipe, EmiCraftContext<T> context) {
        T menu = context.getScreenHandler();
        if (isCrafting(recipe)) {
            EncodingHelper.encodeCraftingRecipe(menu, recipe.getBackingRecipe(), ofInputs(recipe), i -> true);
        } else {
            EncodingHelper.encodeProcessingRecipe(menu,
                    ofInputs(recipe),
                    ofOutputs(recipe));
        }
        if (Minecraft.getInstance().screen instanceof RecipeScreen e) {
            e.onClose();
        }
        return true;
    }

    private static boolean isCrafting(EmiRecipe recipe) {
        return recipe instanceof EmiStonecuttingRecipe;
    }

    private static List<List<GenericStack>> ofInputs(EmiRecipe emiRecipe) {
        if (emiRecipe instanceof MultiblockInfoEmiRecipe) {
            return emiRecipe.getInputs()
                    .stream()
                    .filter(Ae2PatternTerminalHandler::isNotHatch)
                    .map(Ae2PatternTerminalHandler::intoGenericStack)
                    .toList();
        }
        var list = new ArrayList<List<GenericStack>>();
        if (GTUtil.isShiftDown() || GTUtil.isCtrlDown()) {
            emiRecipe.getCatalysts()
                    .stream()
                    .map(s -> intoGenericStack(s, GTUtil.isCtrlDown()))
                    .forEach(list::add);
        }
        emiRecipe.getInputs()
                .stream()
                .map(Ae2PatternTerminalHandler::intoGenericStack)
                .forEach(list::add);
        return list;
    }

    private static boolean isNotHatch(EmiIngredient ingredient) {
        if (ingredient instanceof EmiStack stack) {
            return GTUtil.isShiftDown() || !(stack.getKey() instanceof MetaMachineItem meta) || !Hatch.Set.contains(meta.getBlock());
        }
        return false;
    }

    private static List<GenericStack> ofOutputs(EmiRecipe emiRecipe) {
        return emiRecipe.getOutputs()
                .stream()
                .flatMap(slot -> intoGenericStack(slot).stream().limit(1))
                .toList();
    }

    private static List<GenericStack> intoGenericStack(EmiIngredient ingredient) {
        return intoGenericStack(ingredient, false);
    }

    private static List<GenericStack> intoGenericStack(EmiIngredient ingredient, boolean virtual) {
        if (ingredient.isEmpty()) {
            return new ArrayList<>();
        }
        return ingredient.getEmiStacks().stream().map(stack -> fromEmiStack(stack, ingredient.getAmount(), virtual)).toList();
    }

    private static GenericStack fromEmiStack(EmiStack stack, long amount, boolean virtual) {
        if (stack.getKey() instanceof Item) {
            if (virtual) {
                var itemKey = AEItemKey.of(VirtualItemProviderBehavior.setVirtualItem(CustomItems.VIRTUAL_ITEM_PROVIDER.asStack(), stack.getItemStack()));
                itemKey.getTag().putBoolean("marked", true);
                return new GenericStack(itemKey, amount);
            }
            return new GenericStack(AEItemKey.of(stack.getItemStack()), amount);
        } else if (stack.getKey() instanceof Fluid fluid) {
            return new GenericStack(AEFluidKey.of(fluid), amount);
        }
        return new GenericStack(AEItemKey.of(Items.STICK), 0);
    }
}
