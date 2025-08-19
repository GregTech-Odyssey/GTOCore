package com.gtocore.integration.emi;

import com.gtocore.integration.emi.multipage.MultiblockInfoEmiRecipe;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;

import appeng.api.stacks.GenericStack;
import appeng.integration.modules.jeirei.EncodingHelper;
import appeng.menu.me.items.PatternEncodingTermMenu;
import dev.emi.emi.api.recipe.EmiPlayerInventory;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.EmiCraftContext;
import dev.emi.emi.api.recipe.handler.EmiRecipeHandler;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.recipe.EmiCookingRecipe;
import dev.emi.emi.recipe.EmiStonecuttingRecipe;
import dev.emi.emi.screen.RecipeScreen;
import vazkii.botania.client.integration.emi.BotaniaEmiRecipe;

import java.util.List;
import java.util.stream.Stream;

final class GTAe2PatternTerminalHandler<T extends PatternEncodingTermMenu> implements EmiRecipeHandler<T> {

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
    public List<ClientTooltipComponent> getTooltip(EmiRecipe recipe, EmiCraftContext<T> context) {
        return Stream.of(
                Component.translatable("gtocore.ae.appeng.me2in1.emi.catalyst").withStyle(ChatFormatting.GREEN),
                Component.translatable("gtocore.ae.appeng.me2in1.emi.catalyst.virtual").withStyle(ChatFormatting.DARK_GREEN))
                .map(Component::getVisualOrderText)
                .map(ClientTooltipComponent::create)
                .toList();
    }

    @Override
    public boolean canCraft(EmiRecipe recipe, EmiCraftContext<T> context) {
        return true;
    }

    @Override
    public boolean craft(EmiRecipe recipe, EmiCraftContext<T> context) {
        T menu = context.getScreenHandler();
        if (isCrafting(recipe)) {
            EncodingHelper.encodeCraftingRecipe(menu, recipe.getBackingRecipe(), GTEmiEncodingHelper.ofInputs(recipe), i -> true);
        } else {
            EncodingHelper.encodeProcessingRecipe(menu,
                    GTEmiEncodingHelper.ofInputs(recipe),
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

    private static List<GenericStack> ofOutputs(EmiRecipe emiRecipe) {
        return emiRecipe.getOutputs()
                .stream()
                .flatMap(slot -> GTEmiEncodingHelper.intoGenericStack(slot).stream().limit(1))
                .toList();
    }
}
