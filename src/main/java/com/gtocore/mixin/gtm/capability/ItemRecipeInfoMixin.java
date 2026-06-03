package com.gtocore.mixin.gtm.capability;

import com.gtocore.api.data.tag.GTOTagPrefix;

import com.gtolib.api.item.ItemStackHandler;
import com.gtolib.api.recipe.ContentBuilder;

import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.item.TagPrefixItem;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.content.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.handler.IO;
import com.gregtechceu.gtceu.api.recipe.info.ContentRecipeInfo;
import com.gregtechceu.gtceu.api.recipe.info.ItemRecipeInfo;
import com.gregtechceu.gtceu.api.recipe.ingredient.ItemIngredient;
import com.gregtechceu.gtceu.api.recipe.ui.GTRecipeTypeUI;
import com.gregtechceu.gtceu.api.transfer.item.ICustomItemStackHandler;
import com.gregtechceu.gtceu.common.recipe.condition.ResearchCondition;
import com.gregtechceu.gtceu.integration.xei.widgets.GTRecipeWidget;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ItemRecipeInfo.class)
public abstract class ItemRecipeInfoMixin extends ContentRecipeInfo<ItemIngredient> {

    protected ItemRecipeInfoMixin(String name, int color, boolean doRenderSlot, int sortIndex) {
        super(name, color, doRenderSlot, sortIndex);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void applyWidgetInfo(@NotNull Widget widget, int index, boolean isXEI, IO io, GTRecipeTypeUI.@UnknownNullability("null when storage == null") RecipeHolder recipeHolder, @NotNull GTRecipeType recipeType, @UnknownNullability("null when content == null") GTRecipeDefinition recipe, @Nullable Content<ItemIngredient> content, @Nullable Object storage, int recipeTier, int chanceTier) {
        if (widget instanceof SlotWidget slot) {
            if (storage instanceof ICustomItemStackHandler items) {
                if (index >= 0 && index < items.getSlots()) {
                    slot.setHandlerSlot(items, index);
                    slot.setIngredientIO(io == IO.IN ? IngredientIO.INPUT : IngredientIO.OUTPUT);
                    slot.setCanTakeItems(!isXEI);
                    slot.setCanPutItems(!isXEI && io.support(IO.IN));
                }
                if (isXEI && recipeType.isHasResearchSlot() && index == items.getSlots()) {
                    ResearchCondition condition = recipeHolder.conditions().stream().filter(ResearchCondition.class::isInstance).findAny().map(ResearchCondition.class::cast).orElse(null);
                    if (condition != null) {
                        slot.setHandlerSlot(new ItemStackHandler(condition.dataStack), 0);
                        slot.setIngredientIO(IngredientIO.CATALYST);
                        slot.setCanTakeItems(false);
                        slot.setCanPutItems(false);
                    }
                }
            }
            if (content != null) {
                float chance = (float) recipe.chanceFunction.getBoostedChance(content, recipeTier, chanceTier) / ContentBuilder.maxChance;
                if (io == IO.IN && content.inner.getInnerItemStack().getItem() instanceof TagPrefixItem item && item.tagPrefix == GTOTagPrefix.CATALYST) {
                    slot.setIngredientIO(IngredientIO.CATALYST);
                    slot.setXEIChance(0);
                } else {
                    slot.setXEIChance(chance);
                }
                slot.setOnAddedTooltips((w, tooltips) -> {
                    GTRecipeWidget.setConsumedChance(content, ChanceLogic.OR, tooltips, recipeTier, chanceTier, recipe.chanceFunction);
                    tooltips.add(Component.translatable("gui.tooltips.ae2.Amount", content.amount).withStyle(ChatFormatting.GRAY));
                });
                if (io == IO.IN && content.chance == 0) {
                    slot.setIngredientIO(IngredientIO.CATALYST);
                }
            }
        }
    }
}
