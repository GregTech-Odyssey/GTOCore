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
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.trait.IRecipeHandlerTrait;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableRecipeHandlerTrait;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.utils.function.ObjectLongConsumer;
import com.gregtechceu.gtceu.utils.function.ObjectLongPredicate;

import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.fast.recipesearch.IntLongMap;
import it.unimi.dsi.fastutil.objects.Reference2LongOpenHashMap;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Getter
public final class InternalSlotRecipeHandler {

    private final List<RecipeHandlerList> slotHandlers;

    public InternalSlotRecipeHandler(MEPatternBufferPartMachine buffer, MEPatternBufferPartMachine.InternalSlot[] slots) {
        this.slotHandlers = new ArrayList<>(slots.length);
        for (MEPatternBufferPartMachine.InternalSlot slot : slots) {
            slotHandlers.add(new SlotRHL(buffer, slot));
        }
    }

    private static class WrapperRHL extends AbstractRHL {

        private WrapperRHL(AbstractRHL rhl) {
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

    static abstract class AbstractRHL extends ExtendedRecipeHandlerList {

        final MEPatternBufferPartMachine.InternalSlot slot;

        AbstractRHL(MEPatternBufferPartMachine.InternalSlot slot, IMultiPart part) {
            super(IO.IN, part);
            this.slot = slot;
            priority = 1000;
        }

        @Override
        public ExtendedRecipeHandlerList wrapper() {
            return new WrapperRHL(this);
        }

        @Override
        public boolean findRecipe(IRecipeCapabilityHolder holder, GTRecipeType recipeType, Predicate<GTRecipe> canHandle) {
            if (slot.isEmpty() || !(holder instanceof IRecipeLogicMachine machine)) return false;
            if (slot.recipe != null) {
                if (RecipeType.available(slot.recipe.recipeType, machine.disabledCombined() ? new GTRecipeType[] { machine.getRecipeType() } : machine.getRecipeTypes())) {
                    holder.setCurrentHandlerList(this);
                    return canHandle.test(slot.recipe);
                } else {
                    slot.setRecipe(null);
                }
            }
            final var type = slot.machine.recipeType;
            if (type != GTORecipeTypes.HATCH_COMBINED && type != recipeType && !machine.disabledCombined()) {
                if (GTRecipeType.available(type, machine.getRecipeTypes())) {
                    recipeType = type;
                } else {
                    return false;
                }
            }
            var map = this.getIngredientMap(recipeType);
            if (map.isEmpty()) return false;
            holder.setCurrentHandlerList(this);
            return recipeType.db.find(map, canHandle);
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
                        if (ingredient.testItem(e.getKey().getItem())) {
                            available += e.getLongValue();
                            if (available >= needed) break;
                        }
                    }
                    if (available < needed) {
                        if (ingredientStacks == null) ingredientStacks = getItemMap(parallelCache);
                        for (var iter = ingredientStacks.reference2LongEntrySet().fastIterator(); iter.hasNext();) {
                            var inventoryEntry = iter.next();
                            if (ingredient.testItem(inventoryEntry.getKey())) {
                                available += inventoryEntry.getLongValue();
                                if (available >= needed) break;
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

        @Override
        public boolean handleRecipeContent(IO io, Recipe recipe, RecipeCapabilityMap<List<Object>> contents, boolean simulate) {
            if (slot.isEmpty() || (slot.recipe != null && slot.recipe != recipe.rootRecipe)) return false;
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
                if (contents.fluid == null) {
                    slot.setRecipe(recipe.rootRecipe);
                    return true;
                } else {
                    List left = contents.fluid;
                    for (var handler : getCapability(FluidRecipeCapability.CAP)) {
                        left = handler.handleRecipe(IO.IN, recipe, left, simulate);
                        if (left == null) {
                            slot.setRecipe(recipe.rootRecipe);
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }

    static final class SlotRHL extends AbstractRHL {

        final IRecipeHandlerTrait<Ingredient> itemRecipeHandler;
        final IRecipeHandlerTrait<FluidIngredient> fluidRecipeHandler;

        private SlotRHL(MEPatternBufferPartMachine buffer, MEPatternBufferPartMachine.InternalSlot slot) {
            super(slot, buffer);
            itemRecipeHandler = new SlotItemRecipeHandler(buffer, slot);
            fluidRecipeHandler = new SlotFluidRecipeHandler(buffer, slot);
            addHandlers(itemRecipeHandler, fluidRecipeHandler, slot.circuitInventory, slot.shareInventory, slot.shareTank, buffer.circuitInventorySimulated, buffer.shareInventory, buffer.shareTank);
        }
    }

    private static final class SlotItemRecipeHandler extends NonstandardSlotRecipeHandler<Ingredient> {

        private SlotItemRecipeHandler(MEPatternBufferPartMachine buffer, MEPatternBufferPartMachine.InternalSlot slot) {
            super(buffer, slot);
            slot.setOnContentsChanged(this::notifyListeners);
        }

        @Override
        public List<Ingredient> handleRecipe(IO io, GTRecipe recipe, List left, boolean simulate) {
            if (slot.itemInventory.isEmpty()) return left;
            return handleRecipeInner(io, recipe, new ArrayList(left), simulate);
        }

        @Override
        public List<Ingredient> handleRecipeInner(IO io, GTRecipe recipe, List<Ingredient> left, boolean simulate) {
            return slot.handleItemInternal(left, simulate);
        }

        @Override
        public RecipeCapability<Ingredient> getCapability() {
            return ItemRecipeCapability.CAP;
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
        public IntLongMap getIngredientMap(@NotNull GTRecipeType type) {
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
    }

    private static final class SlotFluidRecipeHandler extends NonstandardSlotRecipeHandler<FluidIngredient> {

        private SlotFluidRecipeHandler(MEPatternBufferPartMachine buffer, MEPatternBufferPartMachine.InternalSlot slot) {
            super(buffer, slot);
            slot.setOnContentsChanged(this::notifyListeners);
        }

        @Override
        public List<FluidIngredient> handleRecipe(IO io, GTRecipe recipe, List left, boolean simulate) {
            if (slot.fluidInventory.isEmpty()) return left;
            return handleRecipeInner(io, recipe, new ArrayList(left), simulate);
        }

        @Override
        public List<FluidIngredient> handleRecipeInner(IO io, GTRecipe recipe, List<FluidIngredient> left, boolean simulate) {
            return slot.handleFluidInternal(left, simulate);
        }

        @Override
        public RecipeCapability<FluidIngredient> getCapability() {
            return FluidRecipeCapability.CAP;
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
                if (function.test(e.getKey().getReadOnlyStack(), a)) return true;
            }
            return false;
        }

        @Override
        public void fastForEachFluids(ObjectLongConsumer<FluidStack> function) {
            slot.fluidInventory.reference2LongEntrySet().fastForEach(e -> {
                var a = e.getLongValue();
                if (a < 1) return;
                function.accept(e.getKey().getReadOnlyStack(), a);
            });
        }

        @Override
        public IntLongMap getIngredientMap(@NotNull GTRecipeType type) {
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
    }

    private abstract static class NonstandardSlotRecipeHandler<ING> extends NotifiableRecipeHandlerTrait<ING> implements NonStandardHandler {

        final MEPatternBufferPartMachine.InternalSlot slot;

        private NonstandardSlotRecipeHandler(MEPatternBufferPartMachine buffer, MEPatternBufferPartMachine.InternalSlot slot) {
            super(buffer);
            this.slot = slot;
            slot.setOnContentsChanged(this::notifyListeners);
        }

        @Override
        public boolean hasCapability(@Nullable Direction side) {
            return false;
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
        public IO getHandlerIO() {
            return IO.IN;
        }

        @Override
        public boolean isRecipeOnly() {
            return true;
        }
    }
}
