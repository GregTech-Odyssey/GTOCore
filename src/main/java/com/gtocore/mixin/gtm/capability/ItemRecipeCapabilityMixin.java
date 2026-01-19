package com.gtocore.mixin.gtm.capability;

import com.gtocore.api.data.tag.GTOTagPrefix;

import com.gtolib.api.item.ItemHandlerModifiable;
import com.gtolib.api.recipe.ContentBuilder;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.item.TagPrefixItem;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.content.IContentSerializer;
import com.gregtechceu.gtceu.api.recipe.ui.GTRecipeTypeUI;
import com.gregtechceu.gtceu.common.recipe.condition.ResearchCondition;
import com.gregtechceu.gtceu.integration.xei.widgets.GTRecipeWidget;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.items.IItemHandlerModifiable;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ItemRecipeCapability.class)
public abstract class ItemRecipeCapabilityMixin extends RecipeCapability<Ingredient> {

    protected ItemRecipeCapabilityMixin(String name, int color, boolean doRenderSlot, int sortIndex, IContentSerializer<Ingredient> serializer) {
        super(name, color, doRenderSlot, sortIndex, serializer);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void applyWidgetInfo(@NotNull Widget widget, int index, boolean isXEI, IO io, GTRecipeTypeUI.@UnknownNullability("null when storage == null") RecipeHolder recipeHolder, @NotNull GTRecipeType recipeType, @UnknownNullability("null when content == null") GTRecipe recipe, @Nullable Content content, @Nullable Object storage, int recipeTier, int chanceTier) {
        if (widget instanceof SlotWidget slot) {
            if (storage instanceof IItemHandlerModifiable items) {
                if (index >= 0 && index < items.getSlots()) {
                    slot.setHandlerSlot(items, index);
                    slot.setIngredientIO(io == IO.IN ? IngredientIO.INPUT : IngredientIO.OUTPUT);
                    slot.setCanTakeItems(!isXEI);
                    slot.setCanPutItems(!isXEI && io.support(IO.IN));
                }
                if (isXEI && recipeType.isHasResearchSlot() && index == items.getSlots()) {
                    ResearchCondition condition = recipeHolder.conditions().stream().filter(ResearchCondition.class::isInstance).findAny().map(ResearchCondition.class::cast).orElse(null);
                    if (condition != null) {
                        slot.setHandlerSlot(new ItemHandlerModifiable(condition.dataStack), 0);
                        slot.setIngredientIO(IngredientIO.CATALYST);
                        slot.setCanTakeItems(false);
                        slot.setCanPutItems(false);
                    }
                }
            }
            if (content != null) {
                float chance = (float) recipeType.getChanceFunction().getBoostedChance(content, recipeTier, chanceTier) / ContentBuilder.maxChance;
                if (io == IO.IN && ItemRecipeCapability.CAP.of(content).getInnerItemStack().getItem() instanceof TagPrefixItem item && item.tagPrefix == GTOTagPrefix.CATALYST) {
                    slot.setIngredientIO(IngredientIO.CATALYST);
                    slot.setXEIChance(0);
                } else {
                    slot.setXEIChance(chance);
                }
                slot.setOnAddedTooltips((w, tooltips) -> {
                    GTRecipeWidget.setConsumedChance(content, recipe.getChanceLogicForCapability(this, io, isTickSlot(index, io, recipe)), tooltips, recipeTier, chanceTier, recipeType.getChanceFunction());
                    if (isTickSlot(index, io, recipe)) {
                        tooltips.add(Component.translatable("gtceu.gui.content.per_tick"));
                    }
                    tooltips.add(Component.translatable("gui.tooltips.ae2.Amount", ItemRecipeCapability.CAP.of(content).amount).withStyle(ChatFormatting.GRAY));
                });
                if (io == IO.IN && content.chance == 0) {
                    slot.setIngredientIO(IngredientIO.CATALYST);
                }
            }
        }
    }

    @Override
    public boolean isTickSlot(int index, IO io, GTRecipe recipe) {
        return false;
    }
}
