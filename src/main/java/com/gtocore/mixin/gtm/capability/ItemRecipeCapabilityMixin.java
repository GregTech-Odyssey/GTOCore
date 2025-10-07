package com.gtocore.mixin.gtm.capability;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.recipe.condition.ResearchCondition;

import com.gtolib.api.item.ItemHandlerModifiable;
import com.gtolib.api.recipe.ContentBuilder;
import com.gtolib.api.recipe.ingredient.CircuitIngredient;
import com.gtolib.api.recipe.ingredient.FastSizedIngredient;
import com.gtolib.utils.ItemUtils;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.item.TagPrefixItem;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.content.IContentSerializer;
import com.gregtechceu.gtceu.api.recipe.ui.GTRecipeTypeUI;
import com.gregtechceu.gtceu.integration.xei.entry.item.ItemEntryList;
import com.gregtechceu.gtceu.integration.xei.entry.item.ItemStackList;
import com.gregtechceu.gtceu.integration.xei.entry.item.ItemTagList;
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
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemRecipeCapability.class)
public abstract class ItemRecipeCapabilityMixin extends RecipeCapability<Ingredient> {

    protected ItemRecipeCapabilityMixin(String name, int color, boolean doRenderSlot, int sortIndex, IContentSerializer<Ingredient> serializer) {
        super(name, color, doRenderSlot, sortIndex, serializer);
    }

    @Shadow(remap = false)
    @Nullable
    private static ItemEntryList tryMapInner(Ingredient inner, int amount) {
        return null;
    }

    @Shadow(remap = false)
    private static @Nullable ItemTagList tryMapTag(Ingredient ingredient, int amount) {
        return null;
    }

    /**
     * @author .
     * @reason 换成FastSizedIngredient
     */
    @Overwrite(remap = false)
    public Ingredient copyInner(Ingredient content) {
        return FastSizedIngredient.copy(content);
    }

    /**
     * @author .
     * @reason 换成FastSizedIngredient
     */
    @Overwrite(remap = false)
    public Ingredient copyWithModifier(Ingredient content, ContentModifier modifier) {
        if (content instanceof FastSizedIngredient sizedIngredient) {
            return FastSizedIngredient.create(sizedIngredient, modifier.apply(sizedIngredient.getAmount()));
        }
        return FastSizedIngredient.create(content, modifier.apply(1L));
    }

    /**
     * @author .
     * @reason 换成FastSizedIngredient
     */
    @Overwrite(remap = false)
    private static ItemEntryList mapItem(final Ingredient ingredient) {
        if (ingredient instanceof FastSizedIngredient sizedIngredient) {
            final int amount = sizedIngredient.getSaturatedAmount();
            var mapped = tryMapInner(sizedIngredient.getInner(), amount);
            if (mapped != null) return mapped;
        } else {
            var tagList = tryMapTag(ingredient, 1);
            if (tagList != null) return tagList;
        }
        return new ItemStackList(ingredient.getItems());
    }

    /**
     * @author .
     * @reason 换成FastSizedIngredient
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
                        slot.setHandlerSlot(new ItemHandlerModifiable(condition.data.dataStack()), 0);
                        slot.setIngredientIO(IngredientIO.CATALYST);
                        slot.setCanTakeItems(false);
                        slot.setCanPutItems(false);
                    }
                }
            }
            if (content != null) {
                float chance = (float) recipeType.getChanceFunction().getBoostedChance(content, recipeTier, chanceTier) / ContentBuilder.maxChance;
                if (io == IO.IN && ItemUtils.getFirstSized(ItemRecipeCapability.CAP.of(content.getContent())).getItem() instanceof TagPrefixItem item && item.tagPrefix == GTOTagPrefix.CATALYST) {
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
                    int amount;
                    if (this.of(content.content) instanceof FastSizedIngredient sizedIngredient) {
                        amount = sizedIngredient.getSaturatedAmount();
                    } else {
                        amount = this.of(content.content).getItems()[0].getCount();
                    }
                    tooltips.add(Component.translatable("gui.tooltips.ae2.Amount", amount).withStyle(ChatFormatting.GRAY));
                });
                if (io == IO.IN && (content.chance == 0 || this.of(content.content) instanceof CircuitIngredient)) {
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
