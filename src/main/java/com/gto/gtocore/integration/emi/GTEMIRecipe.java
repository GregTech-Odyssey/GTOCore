package com.gto.gtocore.integration.emi;

import com.gto.gtocore.common.data.GTORecipes;
import com.gto.gtocore.mixin.mc.IngredientTagValueAccessor;
import com.gto.gtocore.utils.ItemUtils;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;
import com.gregtechceu.gtceu.core.mixins.IngredientAccessor;
import com.gregtechceu.gtceu.integration.xei.widgets.GTRecipeWidget;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.EmptyHandler;

import com.lowdragmc.lowdraglib.emi.ModularEmiRecipe;
import com.lowdragmc.lowdraglib.emi.ModularForegroundRenderWidget;
import com.lowdragmc.lowdraglib.emi.ModularWrapperWidget;
import com.lowdragmc.lowdraglib.gui.ingredient.IRecipeIngredientSlot;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import com.lowdragmc.lowdraglib.jei.ModularWrapper;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.TagEmiIngredient;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.TankWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class GTEMIRecipe extends ModularEmiRecipe<Widget> {

    private final EmiRecipeCategory category;
    private final GTRecipe recipe;

    public GTEMIRecipe(GTRecipe recipe, EmiRecipeCategory category) {
        super(() -> GTORecipes.EMI_RECIPE_WIDGETS.computeIfAbsent(recipe.recipeType, type -> new Widget(getXOffset(recipe), 0, type.getRecipeUI().getJEISize().width, type.getRecipeUI().getJEISize().height)));
        this.recipe = recipe;
        this.category = category;
        inputs = null;
        widget = () -> new GTRecipeWidget(recipe);
    }

    private static int getXOffset(GTRecipe recipe) {
        if (recipe.recipeType.getRecipeUI().getOriginalWidth() != recipe.recipeType.getRecipeUI().getJEISize().width) {
            return (recipe.recipeType.getRecipeUI().getJEISize().width -
                    recipe.recipeType.getRecipeUI().getOriginalWidth()) / 2;
        }
        return 0;
    }

    private static EmiIngredient getEmiIngredient(Ingredient ingredient, boolean input) {
        Ingredient inner = ingredient;
        boolean sized;
        if (ingredient instanceof SizedIngredient sizedIngredient) {
            inner = ItemUtils.getSizedInner(sizedIngredient);
            sized = true;
        } else {
            sized = false;
        }
        ItemStack[] itemStacks = inner.getItems();
        if (itemStacks.length == 0) return EmiStack.EMPTY;
        for (Ingredient.Value value : ((IngredientAccessor) inner).getValues()) {
            if (input && value instanceof Ingredient.TagValue tagValue) {
                TagKey<Item> tagKey = ((IngredientTagValueAccessor) tagValue).getTag();
                return new TagEmiIngredient(tagKey, sized ? ((SizedIngredient) ingredient).getAmount() : itemStacks[0].getCount());
            } else {
                return EmiStack.of(sized ? itemStacks[0].copyWithCount(((SizedIngredient) ingredient).getAmount()) : itemStacks[0]);
            }
        }
        return EmiStack.EMPTY;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        if (inputs == null) {
            inputs = new ArrayList<>();
            recipe.inputs.forEach((k, v) -> {
                if (k instanceof ItemRecipeCapability) {
                    v.forEach(c -> {
                        if (c.getContent() instanceof Ingredient ingredient) {
                            float chance = (float) c.chance / ChanceLogic.getMaxChancedValue();
                            EmiIngredient emiIngredient = getEmiIngredient(ingredient, true).setChance(chance);
                            if (chance > 0) {
                                inputs.add(emiIngredient);
                            } else {
                                catalysts.add(emiIngredient);
                            }
                        }
                    });
                } else if (k instanceof FluidRecipeCapability) {
                    v.forEach(c -> {
                        if (c.getContent() instanceof FluidIngredient ingredient) {
                            FluidStack[] stacks = ingredient.getStacks();
                            if (stacks.length != 0) {
                                float chance = (float) c.chance / ChanceLogic.getMaxChancedValue();
                                EmiIngredient emiIngredient = EmiStack.of(stacks[0].getFluid(), stacks[0].getTag(), stacks[0].getAmount()).setChance(chance);
                                if (chance > 0) {
                                    inputs.add(emiIngredient);
                                } else {
                                    catalysts.add(emiIngredient);
                                }
                            }
                        }
                    });
                }
            });
            recipe.outputs.forEach((k, v) -> {
                if (k instanceof ItemRecipeCapability) {
                    v.forEach(c -> {
                        if (c.getContent() instanceof Ingredient ingredient) {
                            float chance = (float) c.chance / ChanceLogic.getMaxChancedValue();
                            outputs.add((EmiStack) getEmiIngredient(ingredient, false).setChance(chance));
                        }
                    });
                } else if (k instanceof FluidRecipeCapability) {
                    v.forEach(c -> {
                        if (c.getContent() instanceof FluidIngredient ingredient) {
                            float chance = (float) c.chance / ChanceLogic.getMaxChancedValue();
                            FluidStack[] stacks = ingredient.getStacks();
                            if (stacks.length != 0) {
                                outputs.add(EmiStack.of(stacks[0].getFluid(), stacks[0].getTag(), stacks[0].getAmount()).setChance(chance));
                            }
                        }
                    });
                }
            });
        }
        return inputs;
    }

    @Override
    public List<Widget> getFlatWidgetCollection(Widget widget) {
        return List.of();
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return category;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return recipe.getId();
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        var widget = this.widget.get();
        var modular = new ModularWrapper<>(widget);
        modular.setRecipeWidget(0, 0);

        synchronized (CACHE_OPENED) {
            CACHE_OPENED.add(modular);
        }
        List<Widget> widgetList = new ArrayList<>();
        if (widget instanceof WidgetGroup group) {
            for (Widget w : group.widgets) {
                widgetList.add(w);
                if (w instanceof WidgetGroup group1) {
                    widgetList.addAll(group1.getContainedWidgets(true));
                }
            }
        } else {
            widgetList.add(widget);
        }
        List<dev.emi.emi.api.widget.Widget> slots = new ArrayList<>();
        for (com.lowdragmc.lowdraglib.gui.widget.Widget w : widgetList) {
            if (w instanceof IRecipeIngredientSlot slot) {
                if (w.getParent() instanceof DraggableScrollableWidgetGroup draggable && draggable.isUseScissor()) {
                    continue;
                }
                var io = slot.getIngredientIO();
                if (io != null && io != IngredientIO.RENDER_ONLY) {
                    // noinspection unchecked
                    var ingredients = EmiIngredient
                            .of((List<? extends EmiIngredient>) (List<?>) slot.getXEIIngredients());

                    SlotWidget slotWidget = null;
                    // Clear the LDLib slots & add EMI slots based on them.
                    if (slot instanceof com.gregtechceu.gtceu.api.gui.widget.SlotWidget slotW) {
                        slotW.setHandlerSlot((IItemHandlerModifiable) EmptyHandler.INSTANCE, 0);
                        slotW.setDrawHoverOverlay(false).setDrawHoverTips(false);
                    } else if (slot instanceof com.gregtechceu.gtceu.api.gui.widget.TankWidget tankW) {
                        tankW.setFluidTank(EmptyFluidHandler.INSTANCE);
                        tankW.setDrawHoverOverlay(false).setDrawHoverTips(false);
                        long capacity = Math.max(1, ingredients.getAmount());
                        slotWidget = new TankWidget(ingredients, w.getPosition().x, w.getPosition().y,
                                w.getSize().width, w.getSize().height, capacity);
                    }
                    if (slotWidget == null) {
                        slotWidget = new SlotWidget(ingredients, w.getPosition().x, w.getPosition().y);
                    }

                    slotWidget
                            .customBackground(null, w.getPosition().x, w.getPosition().y, w.getSize().width,
                                    w.getSize().height)
                            .drawBack(false);
                    if (io == IngredientIO.CATALYST) {
                        slotWidget.catalyst(true);
                    } else if (io == IngredientIO.OUTPUT) {
                        slotWidget.recipeContext(this);
                    }
                    for (Component component : w.getTooltipTexts()) {
                        slotWidget.appendTooltip(component);
                    }
                    slots.add(slotWidget);
                }
            }
        }
        widgets.add(new ModularWrapperWidget(modular, slots));
        slots.forEach(widgets::add);
        widgets.add(new ModularForegroundRenderWidget(modular));
    }
}
