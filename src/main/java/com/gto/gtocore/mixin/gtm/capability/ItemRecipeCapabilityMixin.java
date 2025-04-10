package com.gto.gtocore.mixin.gtm.capability;

import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.api.machine.feature.multiblock.IDistinctRecipeHolder;
import com.gto.gtocore.api.machine.trait.IEnhancedRecipeLogic;
import com.gto.gtocore.api.machine.trait.IPatternBufferRecipeHandler;
import com.gto.gtocore.api.recipe.FastSizedIngredient;
import com.gto.gtocore.utils.ItemUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.*;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.item.TagPrefixItem;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.ResearchData;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.content.IContentSerializer;
import com.gregtechceu.gtceu.api.recipe.ingredient.IntCircuitIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.IntProviderIngredient;
import com.gregtechceu.gtceu.api.recipe.lookup.*;
import com.gregtechceu.gtceu.api.recipe.ui.GTRecipeTypeUI;
import com.gregtechceu.gtceu.common.recipe.condition.ResearchCondition;
import com.gregtechceu.gtceu.common.valueprovider.AddedFloat;
import com.gregtechceu.gtceu.common.valueprovider.CastedFloat;
import com.gregtechceu.gtceu.common.valueprovider.FlooredInt;
import com.gregtechceu.gtceu.common.valueprovider.MultipliedFloat;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.core.mixins.IngredientAccessor;
import com.gregtechceu.gtceu.core.mixins.TagValueAccessor;
import com.gregtechceu.gtceu.integration.xei.entry.item.ItemEntryList;
import com.gregtechceu.gtceu.integration.xei.entry.item.ItemStackList;
import com.gregtechceu.gtceu.integration.xei.entry.item.ItemTagList;
import com.gregtechceu.gtceu.integration.xei.handlers.item.CycleItemStackHandler;
import com.gregtechceu.gtceu.integration.xei.widgets.GTRecipeWidget;
import com.gregtechceu.gtceu.utils.IngredientEquality;
import com.gregtechceu.gtceu.utils.ResearchManager;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IntersectionIngredient;
import net.minecraftforge.common.crafting.PartialNBTIngredient;
import net.minecraftforge.common.crafting.StrictNBTIngredient;
import net.minecraftforge.items.IItemHandlerModifiable;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import it.unimi.dsi.fastutil.objects.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
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
    private static ItemEntryList mapIntersection(IntersectionIngredient intersection, int amount) {
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
            return FastSizedIngredient.create(sizedIngredient.getInner(), modifier.apply(sizedIngredient.getAmount()));
        } else if (content instanceof IntProviderIngredient intProviderIngredient) {
            return new IntProviderIngredient(intProviderIngredient.getInner(),
                    new FlooredInt(new AddedFloat(new MultipliedFloat(new CastedFloat(intProviderIngredient.getCountProvider()), ConstantFloat.of((float) modifier.multiplier())), ConstantFloat.of((float) modifier.addition()))));
        }
        return FastSizedIngredient.create(content, modifier.apply(1));
    }

    /**
     * @author .
     * @reason 换成FastSizedIngredient
     */
    @Overwrite(remap = false)
    public List<AbstractMapIngredient> convertToMapIngredient(Object obj) {
        List<AbstractMapIngredient> ingredients = new ObjectArrayList<>(1);
        if (obj instanceof Ingredient ingredient) {

            if (ingredient instanceof StrictNBTIngredient nbt) {
                ingredients.addAll(MapItemStackNBTIngredient.from(nbt));
            } else if (ingredient instanceof PartialNBTIngredient nbt) {
                ingredients.addAll(MapItemStackPartialNBTIngredient.from(nbt));
            } else if (ingredient instanceof FastSizedIngredient sized) {
                if (sized.getInner() instanceof StrictNBTIngredient nbt) {
                    ingredients.addAll(MapItemStackNBTIngredient.from(nbt));
                } else if (sized.getInner() instanceof PartialNBTIngredient nbt) {
                    ingredients.addAll(MapItemStackPartialNBTIngredient.from(nbt));
                } else if (sized.getInner() instanceof IntersectionIngredient intersection) {
                    ingredients.add(new MapIntersectionIngredient(intersection));
                } else {
                    for (Ingredient.Value value : ((IngredientAccessor) sized.getInner()).getValues()) {
                        if (value instanceof Ingredient.TagValue tagValue) {
                            ingredients.add(new MapItemTagIngredient(((TagValueAccessor) tagValue).getTag()));
                        } else {
                            Collection<ItemStack> stacks = value.getItems();
                            for (ItemStack stack : stacks) {
                                ingredients.add(new MapItemStackIngredient(stack, sized.getInner()));
                            }
                        }
                    }
                }
            } else if (ingredient instanceof IntProviderIngredient intProvider) {
                if (intProvider.getInner() instanceof StrictNBTIngredient nbt) {
                    ingredients.addAll(MapItemStackNBTIngredient.from(nbt));
                } else if (intProvider.getInner() instanceof PartialNBTIngredient nbt) {
                    ingredients.addAll(MapItemStackPartialNBTIngredient.from(nbt));
                } else if (intProvider.getInner() instanceof IntersectionIngredient intersection) {
                    ingredients.add(new MapIntersectionIngredient(intersection));
                } else {
                    for (Ingredient.Value value : ((IngredientAccessor) intProvider.getInner()).getValues()) {
                        if (value instanceof Ingredient.TagValue tagValue) {
                            ingredients.add(new MapItemTagIngredient(((TagValueAccessor) tagValue).getTag()));
                        } else {
                            Collection<ItemStack> stacks = value.getItems();
                            for (ItemStack stack : stacks) {
                                ingredients.add(new MapItemStackIngredient(stack, intProvider.getInner()));
                            }
                        }
                    }
                }
            } else if (ingredient instanceof IntersectionIngredient intersection) {
                ingredients.add(new MapIntersectionIngredient(intersection));
            } else {
                for (Ingredient.Value value : ((IngredientAccessor) ingredient).getValues()) {
                    if (value instanceof Ingredient.TagValue tagValue) {
                        ingredients.add(new MapItemTagIngredient(((TagValueAccessor) tagValue).getTag()));
                    } else {
                        Collection<ItemStack> stacks = value.getItems();
                        for (ItemStack stack : stacks) {
                            ingredients.add(new MapItemStackIngredient(stack, ingredient));
                        }
                    }
                }
            }
        } else if (obj instanceof ItemStack stack) {
            ingredients.add(new MapItemStackIngredient(stack));

            stack.getTags().forEach(tag -> ingredients.add(new MapItemTagIngredient(tag)));
            if (stack.hasTag()) {
                ingredients.add(new MapItemStackNBTIngredient(stack, StrictNBTIngredient.of(stack)));
            }
            if (stack.getShareTag() != null) {
                ingredients.add(new MapItemStackPartialNBTIngredient(stack,
                        PartialNBTIngredient.of(stack.getItem(), stack.getShareTag())));
            }
        }
        return ingredients;
    }

    /**
     * @author .
     * @reason 换成FastSizedIngredient
     */
    @Overwrite(remap = false)
    public List<Object> compressIngredients(Collection<Object> ingredients) {
        List<Object> list = new ObjectArrayList<>(ingredients.size());
        for (Object item : ingredients) {
            if (item instanceof Ingredient ingredient) {
                boolean isEqual = false;
                for (Object obj : list) {
                    if (obj instanceof Ingredient ingredient1) {
                        if (IngredientEquality.ingredientEquals(ingredient, ingredient1)) {
                            isEqual = true;
                            break;
                        }
                    } else if (obj instanceof ItemStack stack) {
                        if (ingredient.test(stack)) {
                            isEqual = true;
                            break;
                        }
                    }
                }
                if (isEqual) continue;
                //@formatter:off
                if (ingredient instanceof IntCircuitIngredient) {
                    list.add(0, ingredient);
                } else if (ingredient instanceof FastSizedIngredient sized &&
                        sized.getInner() instanceof IntCircuitIngredient) {
                    list.add(0, ingredient);
                } else if (ingredient instanceof IntProviderIngredient intProvider &&
                        intProvider.getInner() instanceof IntCircuitIngredient) {
                    list.add(0, ingredient);
                } else {
                    list.add(ingredient);
                }
                //@formatter:on
            } else if (item instanceof ItemStack stack) {
                boolean isEqual = false;
                for (Object obj : list) {
                    if (obj instanceof Ingredient ingredient) {
                        if (ingredient.test(stack)) {
                            isEqual = true;
                            break;
                        }
                    } else if (obj instanceof ItemStack stack1) {
                        if (ItemStack.isSameItem(stack, stack1)) {
                            isEqual = true;
                            break;
                        }
                    }
                }
                if (isEqual) continue;
                list.add(stack);
            }
        }
        return list;
    }

    /**
     * @author .
     * @reason 换成FastSizedIngredient
     */
    @Overwrite(remap = false)
    public int getMaxParallelRatio(IRecipeCapabilityHolder holder, GTRecipe recipe, int parallelAmount) {
        if (holder instanceof IRecipeLogicMachine machine && machine.getRecipeLogic() instanceof IEnhancedRecipeLogic recipeLogic) {

            Object2LongOpenCustomHashMap<ItemStack> ingredientStacks = recipeLogic.gtocore$getItemIngredientStacks();
            if (machine instanceof IDistinctRecipeHolder distinctRecipeHolder && distinctRecipeHolder.getCurrentDistinct() != null) {
                var recipeHandlerList = distinctRecipeHolder.getCurrentDistinct().getCapability(ItemRecipeCapability.CAP);
                for (IRecipeHandler<?> container : recipeHandlerList) {
                    if (container instanceof IPatternBufferRecipeHandler handler) {
                        for (var entry : handler.getItemMap().object2LongEntrySet()) {
                            ingredientStacks.computeLong(entry.getKey(), (k, v) -> v == null ? entry.getLongValue() : v + entry.getLongValue());
                        }
                    } else {
                        for (Object object : container.getContents()) {
                            if (object instanceof ItemStack itemStack) {
                                ingredientStacks.computeLong(itemStack, (k, v) -> v == null ? itemStack.getCount() : v + itemStack.getCount());
                            }
                        }
                    }
                }
            } else {
                Object2LongOpenCustomHashMap<ItemStack> map = recipeLogic.gtocore$getParallelItemMap();

                var recipeHandlerList = holder.getCapabilitiesFlat(IO.IN, ItemRecipeCapability.CAP);

                for (IRecipeHandler<?> container : recipeHandlerList) {
                    boolean patternBuffer = container instanceof IPatternBufferRecipeHandler;
                    Object2LongOpenCustomHashMap<ItemStack> itemMap;
                    if (patternBuffer) {
                        itemMap = ((IPatternBufferRecipeHandler) container).getItemMap();
                    } else {
                        itemMap = recipeLogic.gtocore$getItemMap();
                        for (Object object : container.getContents()) {
                            if (object instanceof ItemStack itemStack) {
                                itemMap.computeLong(itemStack, (k, v) -> v == null ? itemStack.getCount() : v + itemStack.getCount());
                            }
                        }
                    }

                    if (container.isDistinct()) {
                        for (var entry : itemMap.object2LongEntrySet()) {
                            ingredientStacks.computeLong(entry.getKey(), (k, v) -> v == null ? entry.getLongValue() : Math.max(v, entry.getLongValue()));
                        }
                    } else {
                        for (Object2LongMap.Entry<ItemStack> obj : itemMap.object2LongEntrySet()) {
                            map.computeLong(obj.getKey(), (k, v) -> v == null ? obj.getLongValue() : v + obj.getLongValue());
                        }
                    }
                    if (!patternBuffer) itemMap.clear();
                }
                for (var entry : map.object2LongEntrySet()) {
                    ingredientStacks.computeLong(entry.getKey(), (k, v) -> v == null ? entry.getLongValue() : Math.max(v, entry.getLongValue()));
                }
            }

            long minMultiplier = Integer.MAX_VALUE - 1;
            Object2IntOpenHashMap<Ingredient> notConsumableMap = recipeLogic.gtocore$getItemNotConsumableMap();
            Object2IntOpenHashMap<Ingredient> countableMap = recipeLogic.gtocore$getItemConsumableMap();
            for (Content content : recipe.getInputContents(ItemRecipeCapability.CAP)) {
                Ingredient recipeIngredient = ItemRecipeCapability.CAP.of(content.content);
                int ingredientCount;
                if (recipeIngredient instanceof FastSizedIngredient sizedIngredient) {
                    ingredientCount = sizedIngredient.getAmount();
                } else if (recipeIngredient instanceof IntProviderIngredient intProviderIngredient) {
                    ingredientCount = intProviderIngredient.getSampledCount(GTValues.RNG);
                } else {
                    ingredientCount = 1;
                }
                if (content.chance == 0) {
                    notConsumableMap.computeIfPresent(recipeIngredient, (k, v) -> v + ingredientCount);
                    notConsumableMap.putIfAbsent(recipeIngredient, ingredientCount);
                } else {
                    countableMap.computeIfPresent(recipeIngredient, (k, v) -> v + ingredientCount);
                    countableMap.putIfAbsent(recipeIngredient, ingredientCount);
                }
            }

            for (Object2IntMap.Entry<Ingredient> recipeInputEntry : notConsumableMap.object2IntEntrySet()) {
                long needed = recipeInputEntry.getIntValue();
                long available = 0;
                for (Object2LongMap.Entry<ItemStack> inventoryEntry : ingredientStacks.object2LongEntrySet()) {
                    if (recipeInputEntry.getKey().test(inventoryEntry.getKey())) {
                        available = inventoryEntry.getLongValue();
                        if (available > needed) {
                            inventoryEntry.setValue(available - needed);
                            needed -= available;
                            break;
                        } else {
                            inventoryEntry.setValue(0);
                            recipeInputEntry.setValue((int) (needed - available));
                            needed -= available;
                        }
                    }
                }
                if (needed >= available) {
                    return 0;
                }
            }

            if (countableMap.isEmpty() && !notConsumableMap.isEmpty()) {
                return parallelAmount;
            }

            for (Object2IntMap.Entry<Ingredient> recipeInputEntry : countableMap.object2IntEntrySet()) {
                long needed = recipeInputEntry.getIntValue();
                long available = 0;
                for (Object2LongMap.Entry<ItemStack> inventoryEntry : ingredientStacks.object2LongEntrySet()) {
                    if (recipeInputEntry.getKey().test(inventoryEntry.getKey())) {
                        available += inventoryEntry.getLongValue();
                        break;
                    }
                }
                if (available >= needed) {
                    long ratio = Math.min(parallelAmount, available / needed);
                    if (ratio < minMultiplier) {
                        minMultiplier = ratio;
                    }
                } else {
                    return 0;
                }
            }
            return (int) minMultiplier;
        }
        return 1;
    }

    /**
     * @author .
     * @reason 换成FastSizedIngredient
     */
    @Overwrite(remap = false)
    private static ItemEntryList mapItem(final Ingredient ingredient) {
        if (ingredient instanceof FastSizedIngredient sizedIngredient) {
            final int amount = sizedIngredient.getAmount();
            var mapped = tryMapInner(sizedIngredient.getInner(), amount);
            if (mapped != null) return mapped;
        } else if (ingredient instanceof IntProviderIngredient intProvider) {
            final int amount = 1;
            var mapped = tryMapInner(intProvider.getInner(), amount);
            if (mapped != null) return mapped;
        } else if (ingredient instanceof IntersectionIngredient intersection) {
            return mapIntersection(intersection, -1);
        } else {
            var tagList = tryMapTag(ingredient, 1);
            if (tagList != null) return tagList;
        }
        ItemStackList stackList = new ItemStackList();
        ItemStack[] items = ingredient.getItems();
        boolean isIntProvider = ingredient instanceof IntProviderIngredient || (ingredient instanceof FastSizedIngredient sized && ItemUtils.getSizedInner(sized) instanceof IntProviderIngredient);
        for (ItemStack item : items) {
            if (isIntProvider) {
                stackList.add(item.copyWithCount(1));
            } else {
                stackList.add(item);
            }
        }

        return stackList;
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
                // 1 over container size.
                // If in a recipe viewer and a research slot can be added, add it.
                if (isXEI && recipeType.isHasResearchSlot() && index == items.getSlots()) {
                    if (ConfigHolder.INSTANCE.machines.enableResearch) {
                        ResearchCondition condition = recipeHolder.conditions().stream()
                                .filter(ResearchCondition.class::isInstance).findAny()
                                .map(ResearchCondition.class::cast).orElse(null);
                        if (condition != null) {
                            List<ItemStack> dataItems = new ArrayList<>();
                            for (ResearchData.ResearchEntry entry : condition.data) {
                                ItemStack dataStick = entry.getDataItem().copy();
                                ResearchManager.writeResearchToNBT(dataStick.getOrCreateTag(), entry.getResearchId(),
                                        recipeType);
                                dataItems.add(dataStick);
                            }
                            CycleItemStackHandler handler = new CycleItemStackHandler(List.of(dataItems));
                            slot.setHandlerSlot(handler, 0);
                            slot.setIngredientIO(IngredientIO.CATALYST);
                            slot.setCanTakeItems(false);
                            slot.setCanPutItems(false);
                        }
                    }
                }
            }
            if (content != null) {
                float chance = (float) recipeType.getChanceFunction().getBoostedChance(content, recipeTier, chanceTier) / content.maxChance;
                if (io == IO.IN && ItemRecipeCapability.CAP.of(content.getContent()) instanceof FastSizedIngredient sizedIngredient && ItemUtils.getFirstSized(sizedIngredient).getItem() instanceof TagPrefixItem item && item.tagPrefix == GTOTagPrefix.CATALYST) {
                    slot.setIngredientIO(IngredientIO.CATALYST);
                    slot.setXEIChance(0);
                } else {
                    slot.setXEIChance(chance);
                }
                slot.setOnAddedTooltips((w, tooltips) -> {
                    GTRecipeWidget.setConsumedChance(content,
                            recipe.getChanceLogicForCapability(this, io, isTickSlot(index, io, recipe)),
                            tooltips, recipeTier, chanceTier, recipeType.getChanceFunction());
                    //@formatter:off
                    if (this.of(content.content) instanceof IntProviderIngredient ingredient) {
                        IntProvider countProvider = ingredient.getCountProvider();
                        tooltips.add(Component.translatable("gtceu.gui.content.count_range",
                                        countProvider.getMinValue(), countProvider.getMaxValue())
                                .withStyle(ChatFormatting.GOLD));
                    } else if (this.of(content.content) instanceof FastSizedIngredient sizedIngredient &&
                            sizedIngredient.getInner() instanceof IntProviderIngredient ingredient) {
                        IntProvider countProvider = ingredient.getCountProvider();
                        tooltips.add(Component.translatable("gtceu.gui.content.count_range",
                                        countProvider.getMinValue(), countProvider.getMaxValue())
                                .withStyle(ChatFormatting.GOLD));
                    }
                    if (isTickSlot(index, io, recipe)) {
                        tooltips.add(Component.translatable("gtceu.gui.content.per_tick"));
                    }
                    int amount;
                    if (this.of(content.content) instanceof FastSizedIngredient sizedIngredient) {
                        amount = sizedIngredient.getAmount();
                    } else {
                        amount= this.of(content.content).getItems()[0].getCount();
                    }
                    tooltips.add(Component.translatable("gui.tooltips.ae2.Amount", amount).withStyle(ChatFormatting.GRAY));
                });
                if (io == IO.IN && (content.chance == 0 || this.of(content.content) instanceof IntCircuitIngredient)) {
                    slot.setIngredientIO(IngredientIO.CATALYST);
                }
            }
        }
    }
}
