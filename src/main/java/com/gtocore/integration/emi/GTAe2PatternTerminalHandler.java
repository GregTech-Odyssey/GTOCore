package com.gtocore.integration.emi;

import com.gtocore.integration.emi.multipage.MultiblockInfoEmiRecipe;

import com.gtolib.api.ae2.IPatterEncodingTermMenu;
import com.gtolib.api.recipe.RecipeBuilder;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;

import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.integration.modules.jeirei.EncodingHelper;
import appeng.integration.modules.jeirei.TransferHelper;
import appeng.menu.me.common.GridInventoryEntry;
import appeng.menu.me.items.PatternEncodingTermMenu;
import com.hepdd.ae2emicraftingforge.client.helper.mapper.EmiStackHelper;
import dev.emi.emi.api.recipe.EmiPlayerInventory;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.EmiCraftContext;
import dev.emi.emi.api.recipe.handler.EmiRecipeHandler;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.Widget;
import dev.emi.emi.recipe.EmiCookingRecipe;
import dev.emi.emi.recipe.EmiStonecuttingRecipe;
import dev.emi.emi.screen.RecipeScreen;
import vazkii.botania.client.integration.emi.BotaniaEmiRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.hepdd.ae2emicraftingforge.client.helper.rendering.Rendering.getInnerBounds;
import static com.hepdd.ae2emicraftingforge.client.helper.rendering.Rendering.isInputSlot;

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

    private Set<AEKey> getCraftableKeys(T menu) {
        return menu.getClientRepo() != null ? menu.getClientRepo().getAllEntries()
                .stream()
                .filter(GridInventoryEntry::isCraftable)
                .map(GridInventoryEntry::getWhat)
                .collect(Collectors.toSet()) : Set.of();
    }

    @Override
    public List<ClientTooltipComponent> getTooltip(EmiRecipe recipe, EmiCraftContext<T> context) {
        Set<AEKey> craftableKeys = getCraftableKeys(context.getScreenHandler());
        var anyCraftable = recipe.getInputs().stream()
                .anyMatch(ing -> isCraftable(craftableKeys, ing));
        var gatheredTooltip = anyCraftable ? TransferHelper.createEncodingTooltip(true) : new ArrayList<Component>();
        gatheredTooltip.addAll(getCatalystTooltip());
        return gatheredTooltip.stream()
                .map(Component::getVisualOrderText)
                .map(ClientTooltipComponent::create)
                .toList();
    }

    @Override
    public void render(EmiRecipe recipe, EmiCraftContext<T> context, List<Widget> widgets, GuiGraphics draw) {
        for (var widget : widgets) {
            if (widget instanceof SlotWidget slot && isInputSlot(slot)) {
                if (isCraftable(getCraftableKeys(context.getScreenHandler()), slot.getStack())) {
                    renderSlotOverlay(draw, slot);
                }
            }
        }
    }

    private static void renderSlotOverlay(GuiGraphics guiGraphics, SlotWidget slot) {
        var poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(0, 0, 400);
        var bounds = getInnerBounds(slot);
        guiGraphics.fill(bounds.x(),
                bounds.y(),
                bounds.right(),
                bounds.bottom(),
                TransferHelper.BLUE_SLOT_HIGHLIGHT_COLOR);
        poseStack.popPose();
    }

    private static List<Component> getCatalystTooltip() {
        return List.of(
                Component.translatable("gtocore.ae.appeng.me2in1.emi.catalyst").withStyle(ChatFormatting.AQUA),
                Component.translatable("gtocore.ae.appeng.me2in1.emi.catalyst.fill").withStyle(ChatFormatting.GREEN),
                Component.translatable("gtocore.ae.appeng.me2in1.emi.catalyst.virtual").withStyle(ChatFormatting.DARK_GREEN));
    }

    private static boolean isCraftable(Set<AEKey> craftableKeys, EmiIngredient ingredient) {
        return ingredient.getEmiStacks().stream().anyMatch(emiIngredient -> {
            var stack = EmiStackHelper.toGenericStack(emiIngredient);
            return stack != null && craftableKeys.contains(stack.what());
        });
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
            if (recipe instanceof GTEMIRecipe gtemiRecipe && RecipeBuilder.RECIPE_MAP.containsKey(gtemiRecipe.getId())) {
                ((IPatterEncodingTermMenu) menu).gtolib$addRecipe(gtemiRecipe.getId().toString());
                ((IPatterEncodingTermMenu) menu).gtolib$addRecipeType(gtemiRecipe.getRecipeType().registryName.getPath());
            } else {
                ((IPatterEncodingTermMenu) menu).gtolib$addRecipe("");
                ((IPatterEncodingTermMenu) menu).gtolib$addRecipeType("");
            }
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
