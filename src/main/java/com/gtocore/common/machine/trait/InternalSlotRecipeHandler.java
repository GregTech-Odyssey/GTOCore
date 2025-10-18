package com.gtocore.common.machine.trait;

import com.gtocore.common.data.GTORecipeTypes;
import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachine;

import com.gtolib.api.ae2.stacks.IAEFluidKey;
import com.gtolib.api.ae2.stacks.IAEItemKey;
import com.gtolib.api.machine.trait.ExtendedRecipeHandlerList;
import com.gtolib.api.machine.trait.IEnhancedRecipeLogic;
import com.gtolib.api.machine.trait.NonStandardHandler;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeCapabilityMap;
import com.gtolib.api.recipe.RecipeType;
import com.gtolib.api.recipe.ingredient.FastFluidIngredient;
import com.gtolib.api.recipe.ingredient.FastSizedIngredient;
import com.gtolib.api.recipe.modifier.ParallelCache;

import com.gregtechceu.gtceu.api.capability.recipe.*;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.IRecipeHandlerTrait;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableRecipeHandlerTrait;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.lookup.IntIngredientMap;
import com.gregtechceu.gtceu.utils.function.ObjectLongConsumer;
import com.gregtechceu.gtceu.utils.function.ObjectLongPredicate;

import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.Reference2LongOpenHashMap;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public final class InternalSlotRecipeHandler {

    private final List<RecipeHandlerList> slotHandlers;

    public InternalSlotRecipeHandler(MEPatternBufferPartMachine buffer, MEPatternBufferPartMachine.InternalSlot[] slots) {
        this.slotHandlers = new ArrayList<>(slots.length);
        for (MEPatternBufferPartMachine.InternalSlot slot : slots) {
            slotHandlers.add(new SlotRHL(buffer, slot));
        }
    }

    public static class WrapperRHL extends AbstractRHL {

        public WrapperRHL(AbstractRHL rhl) {
            super(rhl.slot, rhl.part);
        }

        private Reference2LongOpenHashMap<Fluid> getFluidMap(ParallelCache parallelCache) {
            var ingredientStacks = parallelCache.getFluidIngredientMap();
            for (var container : getCapability(FluidRecipeCapability.CAP)) {
                if (container.isNotConsumable() || (container instanceof NonStandardHandler nonStandardHandler && nonStandardHandler.isNonStandardHandler())) continue;
                container.fastForEachFluids((a, b) -> ingredientStacks.addTo(a.getFluid(), b));
            }
            return ingredientStacks;
        }

        @Override
        public long getInputFluidParallel(IRecipeLogicMachine holder, List<Content> contents, long parallelAmount) {
            ParallelCache parallelCache = IEnhancedRecipeLogic.of(holder.getRecipeLogic()).gtolib$getParallelCache();
            Reference2LongOpenHashMap<Fluid> ingredientStacks = null;
            for (var content : contents) {
                if (content.chance > 0 && content.content instanceof FastFluidIngredient ingredient) {
                    long needed = ingredient.getAmount();
                    if (needed < 1) continue;
                    long available = 0;
                    for (var it = slot.fluidInventory.reference2LongEntrySet().fastIterator(); it.hasNext();) {
                        var e = it.next();
                        if (ingredient.testFluid(e.getKey().getFluid())) {
                            available = e.getLongValue();
                            break;
                        }
                    }
                    if (available == 0) {
                        if (ingredientStacks == null) ingredientStacks = getFluidMap(parallelCache);
                        for (var it = ingredientStacks.reference2LongEntrySet().fastIterator(); it.hasNext();) {
                            var inventoryEntry = it.next();
                            if (ingredient.testFluid(inventoryEntry.getKey())) {
                                available = inventoryEntry.getLongValue();
                                break;
                            }
                        }
                    }
                    if (available >= needed) {
                        parallelAmount = Math.min(parallelAmount, available / needed);
                    } else {
                        parallelAmount = 0;
                        break;
                    }
                }
            }
            parallelCache.cleanFluidMap();
            return parallelAmount;
        }
    }

    public static abstract class AbstractRHL extends ExtendedRecipeHandlerList {

        public final MEPatternBufferPartMachine.InternalSlot slot;

        AbstractRHL(MEPatternBufferPartMachine.InternalSlot slot, MultiblockPartMachine part) {
            super(IO.IN, part);
            this.slot = slot;
        }

        @Override
        public <T extends GTRecipeType, R extends GTRecipe> Iterator<R> searchRecipe(IRecipeCapabilityHolder holder, T type, Predicate<R> canHandle) {
            if (slot.isEmpty() || !(holder instanceof IRecipeLogicMachine machine)) return Collections.emptyIterator();
            if (slot.recipe != null) {
                if (!RecipeType.available(slot.recipe.recipeType, machine.disabledCombined() ? new GTRecipeType[] { machine.getRecipeType() } : machine.getRecipeTypes())) return Collections.emptyIterator();
                R r = (R) slot.recipe;
                holder.setCurrentHandlerList(this, null);
                if (canHandle.test(r)) {
                    return new Iterator<>() {

                        private boolean hasNext = true;

                        @Override
                        public boolean hasNext() {
                            return hasNext;
                        }

                        @Override
                        public R next() {
                            hasNext = false;
                            return r;
                        }
                    };
                }
                return Collections.emptyIterator();
            } else {
                this.recipeType = slot.machine.recipeType == GTORecipeTypes.HATCH_COMBINED ? null : slot.machine.recipeType;
                return SEARCH.search(holder, type, this, canHandle);
            }
        }

        @Override
        public boolean isDistinct() {
            return true;
        }

        private Reference2LongOpenHashMap<Item> getItemMap(ParallelCache parallelCache) {
            var ingredientStacks = parallelCache.getItemIngredientMap();
            for (var container : getCapability(ItemRecipeCapability.CAP)) {
                if (container.isNotConsumable() || (container instanceof NonStandardHandler handler && handler.isNonStandardHandler())) continue;
                container.fastForEachItems((a, b) -> ingredientStacks.addTo(a.getItem(), b));
            }
            return ingredientStacks;
        }

        @Override
        public long getInputItemParallel(IRecipeLogicMachine holder, List<Content> contents, long parallelAmount) {
            ParallelCache parallelCache = IEnhancedRecipeLogic.of(holder.getRecipeLogic()).gtolib$getParallelCache();
            Reference2LongOpenHashMap<Item> ingredientStacks = null;
            for (var content : contents) {
                if (content.chance > 0 && content.content instanceof FastSizedIngredient ingredient) {
                    long needed = ingredient.getAmount();
                    if (needed < 1) continue;
                    long available = 0;
                    for (var it = slot.itemInventory.reference2LongEntrySet().fastIterator(); it.hasNext();) {
                        var e = it.next();
                        if (FastSizedIngredient.testItem(ingredient, e.getKey().getItem())) {
                            available = e.getLongValue();
                            break;
                        }
                    }
                    if (available == 0) {
                        if (ingredientStacks == null) ingredientStacks = getItemMap(parallelCache);
                        for (var iter = ingredientStacks.reference2LongEntrySet().fastIterator(); iter.hasNext();) {
                            var inventoryEntry = iter.next();
                            if (FastSizedIngredient.testItem(ingredient, inventoryEntry.getKey())) {
                                available = inventoryEntry.getLongValue();
                                break;
                            }
                        }
                    }
                    if (available >= needed) {
                        parallelAmount = Math.min(parallelAmount, available / needed);
                    } else {
                        parallelAmount = 0;
                        break;
                    }
                }
            }
            parallelCache.cleanItemMap();
            return parallelAmount;
        }

        @Override
        public long getInputFluidParallel(IRecipeLogicMachine holder, List<Content> contents, long parallelAmount) {
            for (var content : contents) {
                if (content.chance > 0 && content.content instanceof FastFluidIngredient ingredient) {
                    long needed = ingredient.getAmount();
                    if (needed < 1) continue;
                    long available = 0;
                    for (var it = slot.fluidInventory.reference2LongEntrySet().fastIterator(); it.hasNext();) {
                        var e = it.next();
                        if (ingredient.testFluid(e.getKey().getFluid())) {
                            available = e.getLongValue();
                            break;
                        }
                    }
                    if (available >= needed) {
                        parallelAmount = Math.min(parallelAmount, available / needed);
                    } else {
                        parallelAmount = 0;
                        break;
                    }
                }
            }
            return parallelAmount;
        }

        public boolean handleRecipeContent(GTRecipe recipe, RecipeCapabilityMap<List<Object>> contents, boolean simulate) {
            if (slot.isEmpty() || (slot.recipe != null && !slot.recipe.id.getPath().equals(recipe.id.getPath()))) return false;
            boolean item = contents.item == null;
            if (!item) {
                List left = contents.item;
                for (var handler : getCapability(ItemRecipeCapability.CAP)) {
                    left = handler.handleRecipe(IO.IN, recipe, left, simulate);
                    if (left == null) {
                        item = true;
                        break;
                    }
                }
            }
            if (item) {
                if (contents.fluid != null) {
                    List left = contents.fluid;
                    for (var handler : getCapability(FluidRecipeCapability.CAP)) {
                        left = handler.handleRecipe(IO.IN, recipe, left, simulate);
                        if (left == null) {
                            slot.setRecipe(((Recipe) recipe).rootRecipe);
                            return true;
                        }
                    }
                    return false;
                }
                slot.setRecipe(((Recipe) recipe).rootRecipe);
                return true;
            }
            return false;
        }
    }

    static final class SlotRHL extends AbstractRHL {

        final IRecipeHandlerTrait<Ingredient> itemRecipeHandler;
        final IRecipeHandlerTrait<FluidIngredient> fluidRecipeHandler;

        private SlotRHL(MEPatternBufferPartMachine buffer, MEPatternBufferPartMachine.InternalSlot slot) {
            super(slot, buffer);
            slot.rhl = this;
            itemRecipeHandler = new SlotItemRecipeHandler(buffer, slot);
            fluidRecipeHandler = new SlotFluidRecipeHandler(buffer, slot);
            addHandlers(itemRecipeHandler, fluidRecipeHandler, slot.circuitInventory, slot.shareInventory, slot.shareTank, buffer.circuitInventorySimulated, buffer.shareInventory, buffer.shareTank);
        }
    }

    private static final class SlotItemRecipeHandler extends NotifiableRecipeHandlerTrait<Ingredient> implements NonStandardHandler {

        private final MEPatternBufferPartMachine.InternalSlot slot;

        private SlotItemRecipeHandler(MEPatternBufferPartMachine buffer, MEPatternBufferPartMachine.InternalSlot slot) {
            super(buffer);
            this.slot = slot;
            slot.setOnContentsChanged(this::notifyListeners);
        }

        @Override
        public boolean hasCapability(@Nullable Direction side) {
            return false;
        }

        @Override
        public List<Ingredient> handleRecipe(IO io, GTRecipe recipe, List left, boolean simulate) {
            if (slot.itemInventory.isEmpty()) return left;
            return handleRecipeInner(io, recipe, new ObjectArrayList(left), simulate);
        }

        @Override
        public List<Ingredient> handleRecipeInner(IO io, GTRecipe recipe, List<Ingredient> left, boolean simulate) {
            return slot.handleItemInternal(left, simulate);
        }

        @Override
        public int getSize() {
            return 81;
        }

        @Override
        public boolean isDistinct() {
            return true;
        }

        @Override
        public RecipeCapability<Ingredient> getCapability() {
            return ItemRecipeCapability.CAP;
        }

        @Override
        public IO getHandlerIO() {
            return IO.IN;
        }

        @Override
        public boolean forEachItems(ObjectLongPredicate<ItemStack> function) {
            for (var it = slot.itemInventory.reference2LongEntrySet().fastIterator(); it.hasNext();) {
                var e = it.next();
                var a = e.getLongValue();
                if (a < 1) {
                    it.remove();
                    continue;
                }
                if (function.test(e.getKey().getReadOnlyStack(), a)) return true;
            }
            return false;
        }

        @Override
        public void fastForEachItems(ObjectLongConsumer<ItemStack> function) {
            slot.itemInventory.reference2LongEntrySet().fastForEach(e -> {
                var a = e.getLongValue();
                if (a < 1) return;
                function.accept(e.getKey().getReadOnlyStack(), a);
            });
        }

        @Override
        public IntIngredientMap getIngredientMap() {
            if (slot.itemChanged) {
                slot.itemChanged = false;
                slot.itemIngredientMap.clear();
                slot.itemInventory.reference2LongEntrySet().fastForEach(e -> {
                    var a = e.getLongValue();
                    if (a < 1) return;
                    ((IAEItemKey) (Object) e.getKey()).gtolib$convert(a, slot.itemIngredientMap);
                });
            }
            return slot.itemIngredientMap;
        }

        @Override
        public boolean isRecipeOnly() {
            return true;
        }
    }

    private static final class SlotFluidRecipeHandler extends NotifiableRecipeHandlerTrait<FluidIngredient> implements NonStandardHandler {

        private final MEPatternBufferPartMachine.InternalSlot slot;

        private SlotFluidRecipeHandler(MEPatternBufferPartMachine buffer, MEPatternBufferPartMachine.InternalSlot slot) {
            super(buffer);
            this.slot = slot;
            slot.setOnContentsChanged(this::notifyListeners);
        }

        @Override
        public boolean hasCapability(@Nullable Direction side) {
            return false;
        }

        @Override
        public List<FluidIngredient> handleRecipe(IO io, GTRecipe recipe, List left, boolean simulate) {
            if (slot.fluidInventory.isEmpty()) return left;
            return handleRecipeInner(io, recipe, new ObjectArrayList(left), simulate);
        }

        @Override
        public List<FluidIngredient> handleRecipeInner(IO io, GTRecipe recipe, List<FluidIngredient> left, boolean simulate) {
            return slot.handleFluidInternal(left, simulate);
        }

        @Override
        public int getSize() {
            return 81;
        }

        @Override
        public boolean isDistinct() {
            return true;
        }

        @Override
        public RecipeCapability<FluidIngredient> getCapability() {
            return FluidRecipeCapability.CAP;
        }

        @Override
        public IO getHandlerIO() {
            return IO.IN;
        }

        @Override
        public boolean forEachFluids(ObjectLongPredicate<FluidStack> function) {
            for (var it = slot.fluidInventory.reference2LongEntrySet().fastIterator(); it.hasNext();) {
                var e = it.next();
                var a = e.getLongValue();
                if (a < 1) {
                    it.remove();
                    continue;
                }
                if (function.test(((IAEFluidKey) (Object) e.getKey()).gtolib$getReadOnlyStack(), a)) return true;
            }
            return false;
        }

        @Override
        public void fastForEachFluids(ObjectLongConsumer<FluidStack> function) {
            slot.fluidInventory.reference2LongEntrySet().fastForEach(e -> {
                var a = e.getLongValue();
                if (a < 1) return;
                function.accept(((IAEFluidKey) (Object) e.getKey()).gtolib$getReadOnlyStack(), a);
            });
        }

        @Override
        public IntIngredientMap getIngredientMap() {
            if (slot.fluidChanged) {
                slot.fluidChanged = false;
                slot.fluidIngredientMap.clear();
                slot.fluidInventory.reference2LongEntrySet().fastForEach(e -> {
                    var a = e.getLongValue();
                    if (a < 1) return;
                    ((IAEFluidKey) (Object) e.getKey()).gtolib$convert(a, slot.fluidIngredientMap);
                });
            }
            return slot.fluidIngredientMap;
        }

        @Override
        public boolean isRecipeOnly() {
            return true;
        }
    }

    public List<RecipeHandlerList> getSlotHandlers() {
        return this.slotHandlers;
    }
}
