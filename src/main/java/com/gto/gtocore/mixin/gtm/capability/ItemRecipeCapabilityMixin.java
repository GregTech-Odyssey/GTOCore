package com.gto.gtocore.mixin.gtm.capability;

import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.api.machine.trait.IEnhancedRecipeLogic;
import com.gto.gtocore.api.machine.trait.IPatternBufferRecipeHandler;
import com.gto.gtocore.api.recipe.FastSizedIngredient;
import com.gto.gtocore.utils.ItemUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.item.TagPrefixItem;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.ingredient.IntCircuitIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.IntProviderIngredient;
import com.gregtechceu.gtceu.api.recipe.lookup.*;
import com.gregtechceu.gtceu.common.valueprovider.AddedFloat;
import com.gregtechceu.gtceu.common.valueprovider.CastedFloat;
import com.gregtechceu.gtceu.common.valueprovider.FlooredInt;
import com.gregtechceu.gtceu.common.valueprovider.MultipliedFloat;
import com.gregtechceu.gtceu.core.mixins.IngredientAccessor;
import com.gregtechceu.gtceu.core.mixins.TagValueAccessor;
import com.gregtechceu.gtceu.integration.xei.entry.item.ItemEntryList;
import com.gregtechceu.gtceu.integration.xei.entry.item.ItemStackList;
import com.gregtechceu.gtceu.integration.xei.entry.item.ItemTagList;
import com.gregtechceu.gtceu.utils.IngredientEquality;

import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IntersectionIngredient;
import net.minecraftforge.common.crafting.PartialNBTIngredient;
import net.minecraftforge.common.crafting.StrictNBTIngredient;

import com.llamalad7.mixinextras.sugar.Local;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import it.unimi.dsi.fastutil.objects.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;
import java.util.List;

@Slf4j
@Mixin(ItemRecipeCapability.class)
public abstract class ItemRecipeCapabilityMixin {

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

            Object2LongOpenCustomHashMap<ItemStack> map = recipeLogic.gtocore$getParallelItemMap();
            Object2LongOpenHashMap<ItemStack> ingredientStacks = recipeLogic.gtocore$getItemIngredientStacks();

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
                    ingredientStacks.putAll(itemMap);
                } else {
                    for (Object2LongMap.Entry<ItemStack> obj : itemMap.object2LongEntrySet()) {
                        map.computeLong(obj.getKey(), (k, v) -> v == null ? obj.getLongValue() : v + obj.getLongValue());
                    }
                }
                if (!patternBuffer) itemMap.clear();
            }
            ingredientStacks.putAll(map);

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

    @Redirect(method = "applyWidgetInfo", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/api/gui/widget/SlotWidget;setXEIChance(F)Lcom/gregtechceu/gtceu/api/gui/widget/SlotWidget;"), remap = false)
    private SlotWidget setXEIChance(SlotWidget instance, float XEIChance, @Local(argsOnly = true) IO io, @Local(argsOnly = true) Content content) {
        if (io == IO.IN) {
            Ingredient ingredient = ItemRecipeCapability.CAP.of(content.getContent());
            if (ingredient instanceof FastSizedIngredient sizedIngredient && ItemUtils.getFirstSized(sizedIngredient).getItem() instanceof TagPrefixItem item && item.tagPrefix == GTOTagPrefix.CATALYST) {
                instance.setIngredientIO(IngredientIO.CATALYST);
                return instance.setXEIChance(0);
            }
        }
        return instance.setXEIChance(XEIChance);
    }
}
