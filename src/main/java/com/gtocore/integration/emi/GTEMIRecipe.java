package com.gtocore.integration.emi;

import com.gtolib.api.recipe.ContentBuilder;
import com.gtolib.api.recipe.RecipeDefinition;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.ItemIngredient;
import com.gregtechceu.gtceu.integration.xei.widgets.GTRecipeWidget;
import com.gregtechceu.gtceu.utils.ItemStackHashStrategy;
import com.gregtechceu.gtceu.utils.ResearchManager;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
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
import dev.emi.emi.EmiPort;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.ItemEmiStack;
import dev.emi.emi.api.stack.TagEmiIngredient;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.TankWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.IntSupplier;

public final class GTEMIRecipe extends ModularEmiRecipe<Widget> {

    private static final Map<GTRecipeType, Widget> EMI_RECIPE_WIDGETS = new Reference2ReferenceOpenHashMap<>();

    private final EmiRecipeCategory category;
    private final RecipeDefinition recipe;
    public final IntSupplier displayPriority;

    public GTEMIRecipe(RecipeDefinition recipe, EmiRecipeCategory category) {
        super(() -> EMI_RECIPE_WIDGETS.computeIfAbsent(recipe.recipeType, type -> new Widget(getXOffset(recipe), 0, type.getRecipeUI().getJEISize().width, getHeight(recipe))));
        this.recipe = recipe;
        this.category = category;
        displayPriority = () -> recipe.priority;
        inputs = null;
        widget = () -> new GTRecipeWidget(recipe);
    }

    public int getTier() {
        return recipe.tier;
    }

    public GTRecipeType getRecipeType() {
        return recipe.recipeType;
    }

    private static int getXOffset(RecipeDefinition recipe) {
        if (recipe.recipeType.getRecipeUI().getOriginalWidth() != recipe.recipeType.getRecipeUI().getJEISize().width) {
            return (recipe.recipeType.getRecipeUI().getJEISize().width -
                    recipe.recipeType.getRecipeUI().getOriginalWidth()) / 2;
        }
        return 0;
    }

    private static int getHeight(RecipeDefinition recipe) {
        return recipe.recipeType.getRecipeUI().getJEISize().height + recipe.conditions.size() * 10;
    }

    @Override
    public int getDisplayHeight() {
        return getHeight(recipe);
    }

    @SuppressWarnings("all")
    private static EmiIngredient getEmiIngredient(ItemIngredient ingredient, boolean input) {
        Ingredient inner = ingredient.inner;
        ItemStack[] itemStacks = inner.getItems();
        if (itemStacks.length == 0) return EmiStack.EMPTY;
        ItemStack itemStack = itemStacks[0];
        long amount = ingredient.amount;
        for (Ingredient.Value value : inner.values) {
            if (input && value instanceof Ingredient.TagValue tagValue) {
                return new TagEmiIngredient(tagValue.tag, amount);
            } else {
                Item item = itemStack.getItem();
                CompoundTag nbt = itemStack.getTag();
                if (nbt == null || nbt.isEmpty()) {
                    return new ItemEmiStack(item, null, amount);
                }
                var stack = new ItemEmiStack(item, nbt, amount);
                stack.comparison(EmiPort.compareStrict());
                return stack;
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
                        if (c.inner instanceof ItemIngredient ingredient) {
                            float chance = (float) c.chance / ContentBuilder.maxChance;
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
                        if (c.inner instanceof FluidIngredient ingredient) {
                            var fluid = ingredient.getFluid();
                            if (fluid != null) {
                                float chance = (float) c.chance / ContentBuilder.maxChance;
                                EmiIngredient emiIngredient = EmiStack.of(fluid, ingredient.nbt, ingredient.amount).setChance(chance);
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
                        if (c.inner instanceof ItemIngredient ingredient) {
                            float chance = (float) c.chance / ContentBuilder.maxChance;
                            outputs.add((EmiStack) getEmiIngredient(ingredient, false).setChance(chance));
                        }
                    });
                } else if (k instanceof FluidRecipeCapability) {
                    v.forEach(c -> {
                        if (c.inner instanceof FluidIngredient ingredient) {
                            float chance = (float) c.chance / ContentBuilder.maxChance;
                            var fluid = ingredient.getFluid();
                            if (fluid != null) {
                                outputs.add(EmiStack.of(fluid, ingredient.nbt, ingredient.amount).setChance(chance));
                            }
                        }
                    });
                }
            });
            if (recipe.recipeType.isScanner()) {
                ResearchManager.ResearchItem researchData = null;
                for (Content content : recipe.getOutputContents(ItemRecipeCapability.CAP)) {
                    var stack = ItemRecipeCapability.CAP.of(content).getInnerItemStack();
                    if (stack.isEmpty()) continue;
                    researchData = ResearchManager.readResearchId(stack);
                    if (researchData != null) break;
                }
                if (researchData != null) {
                    var possibleRecipes = researchData.recipeType().getDataStickEntry(researchData.researchId());
                    Set<ItemStack> cache = new ObjectOpenCustomHashSet<>(ItemStackHashStrategy.ITEM);
                    if (possibleRecipes != null) {
                        for (var r : possibleRecipes) {
                            var outputs = r.getOutputContents(ItemRecipeCapability.CAP);
                            if (outputs.isEmpty()) continue;
                            var outputContent = outputs.getFirst();
                            var ingredient = ItemRecipeCapability.CAP.of(outputContent);
                            var stack = ingredient.getInnerItemStack();
                            if (stack.isEmpty()) continue;
                            if (!cache.contains(stack)) {
                                cache.add(stack);
                                super.outputs.add((EmiStack) getEmiIngredient(ingredient, false));
                            }
                        }
                    }
                }
            }
        }
        return inputs;
    }

    @Override
    public List<Widget> getFlatWidgetCollection(Widget widget) {
        return Collections.emptyList();
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
