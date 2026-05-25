package com.gtocore.common.machine.trait;

import com.gtocore.common.machine.multiblock.part.ae.AbstractRecipeInternalSlot;
import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachine;

import com.gtolib.api.ae2.stacks.IAEFluidKey;
import com.gtolib.api.ae2.stacks.IAEItemKey;

import com.gregtechceu.gtceu.api.capability.recipe.*;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableRecipeHandlerTrait;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.handler.*;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.ItemIngredient;
import com.gregtechceu.gtceu.utils.function.ObjLongPredicate;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.fast.recipesearch.IntLongMap;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.ObjLongConsumer;

@Getter
public final class InternalSlotRecipeHandler {

    private final List<RecipeHandlerUnit> slotHandlers;

    public InternalSlotRecipeHandler(MEPatternBufferPartMachine buffer, MEPatternBufferPartMachine.InternalSlot[] slots) {
        this.slotHandlers = new ArrayList<>(slots.length);
        for (MEPatternBufferPartMachine.InternalSlot slot : slots) {
            slotHandlers.add(SlotRHL.of(buffer, slot));
        }
    }

    private static final class WrapperRHL<S extends AbstractRecipeInternalSlot> extends AbstractRHL<S> {

        private WrapperRHL(S slot, Collection<IRecipeHandler> handlers) {
            super(slot, null, handlers.toArray(new IRecipeHandler[0]));
        }
    }

    public abstract static class AbstractRHL<S extends AbstractRecipeInternalSlot> extends RecipeHandlerUnit {

        protected final S slot;

        protected AbstractRHL(S slot, IMultiPart part, IRecipeHandler... handlers) {
            this(slot, part, IFilteredHandler.HIGH, handlers);
        }

        protected AbstractRHL(S slot, IMultiPart part, int priority, IRecipeHandler... handlers) {
            super(IO.IN, part, handlers);
            this.isDistinct = true;
            this.slot = slot;
            this.priority = priority;
        }

        @Override
        public RecipeHandlerUnit wrapper(Collection<IRecipeHandler> handlers) {
            return new WrapperRHL<>(slot, handlers);
        }

        @Override
        public boolean findRecipe(GTRecipeType recipeType, BiPredicate<RecipeHandlerUnit, GTRecipeDefinition> canHandle) {
            if (slot.isEmpty()) return false;
            var cachedRecipe = getCachedRecipe();
            if (cachedRecipe != null) {
                if (canHandle.test(this, cachedRecipe)) {
                    return true;
                } else {
                    clearCachedRecipe();
                }
            }
            recipeType = getEffectiveRecipeType(recipeType);
            if (recipeType == null) return false;
            var map = this.getSearchMap(recipeType);
            if (map.isEmpty()) return false;
            return recipeType.search(this, map, canHandle);
        }

        protected @Nullable GTRecipeDefinition getCachedRecipe() {
            return null;
        }

        protected void clearCachedRecipe() {}

        protected @Nullable GTRecipeType getEffectiveRecipeType(GTRecipeType recipeType) {
            return recipeType;
        }

        protected void onRecipeHandled(GTRecipe recipe) {}

        @Override
        public boolean handleRecipeItem(IO io, GTRecipe recipe, List<Content<ItemIngredient>> items, boolean simulate) {
            if (items.isEmpty()) return true;
            if (io != handlerIO) throw new IllegalStateException("IO is not the same");
            if (slot.isEmpty()) return false;
            for (var handler : itemHandlers) {
                if (!simulate && handler.isNotConsumable()) continue;
                handler.handleRecipeItem(io, recipe, items, simulate);
                if (items.isEmpty()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean handleRecipeFluid(IO io, GTRecipe recipe, List<Content<FluidIngredient>> fluids, boolean simulate) {
            if (fluids.isEmpty()) {
                if (!simulate) onRecipeHandled(recipe);
                return true;
            }
            if (io != handlerIO) throw new IllegalStateException("IO is not the same");
            for (var handler : fluidHandlers) {
                if (!simulate && handler.isNotConsumable()) continue;
                handler.handleRecipeFluid(io, recipe, fluids, simulate);
                if (fluids.isEmpty()) {
                    if (!simulate) onRecipeHandled(recipe);
                    return true;
                }
            }
            return false;
        }
    }

    private abstract static class PatternBufferRHL extends AbstractRHL<MEPatternBufferPartMachine.InternalSlot> {

        private PatternBufferRHL(MEPatternBufferPartMachine.InternalSlot slot, IMultiPart part, IRecipeHandler... handlers) {
            super(slot, part, IFilteredHandler.HIGHEST, handlers);
        }

        @Override
        protected @Nullable GTRecipeDefinition getCachedRecipe() {
            return slot.recipe;
        }

        @Override
        protected void clearCachedRecipe() {
            slot.setRecipe(null);
        }

        @Override
        protected GTRecipeType getEffectiveRecipeType(GTRecipeType recipeType) {
            final var type = slot.machine.recipeType;
            if (type != null && type != recipeType) {
                return type;
            }
            return recipeType;
        }

        @Override
        protected void onRecipeHandled(GTRecipe recipe) {
            slot.setRecipe(recipe.definition);
        }
    }

    static final class SlotRHL extends PatternBufferRHL {

        final SlotRecipeHandler recipeHandler;

        private static SlotRHL of(MEPatternBufferPartMachine buffer, MEPatternBufferPartMachine.InternalSlot slot) {
            return new SlotRHL(new SlotRecipeHandler(buffer, slot), buffer, slot);
        }

        private SlotRHL(SlotRecipeHandler handler, MEPatternBufferPartMachine buffer, MEPatternBufferPartMachine.InternalSlot slot) {
            super(slot, buffer, handler, slot.circuitInventory, slot.shareInventory, slot.shareTank, buffer.circuitInventorySimulated, buffer.shareInventory, buffer.shareTank);
            recipeHandler = handler;
        }
    }

    final static class SlotRecipeHandler extends NotifiableRecipeHandlerTrait {

        final MEPatternBufferPartMachine.InternalSlot slot;

        private SlotRecipeHandler(MEPatternBufferPartMachine buffer, MEPatternBufferPartMachine.InternalSlot slot) {
            super(buffer);
            this.slot = slot;
            slot.setOnContentsChanged(this::notifyListeners);
        }

        @Override
        public boolean hasCapability(@Nullable Direction side) {
            return false;
        }

        @Override
        public IO getHandlerIO() {
            return IO.IN;
        }

        @Override
        public boolean forEachItems(ObjLongPredicate<ItemStack> function) {
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
        public void fastForEachItems(ObjLongConsumer<ItemStack> function) {
            slot.itemInventory.reference2LongEntrySet().fastForEach(e -> {
                var a = e.getLongValue();
                if (a < 1) return;
                function.accept(e.getKey().getReadOnlyStack(), a);
            });
        }

        @Override
        public boolean forEachFluids(ObjLongPredicate<FluidStack> function) {
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
        public void fastForEachFluids(ObjLongConsumer<FluidStack> function) {
            slot.fluidInventory.reference2LongEntrySet().fastForEach(e -> {
                var a = e.getLongValue();
                if (a < 1) return;
                function.accept(e.getKey().getReadOnlyStack(), a);
            });
        }

        @Override
        public IntLongMap getSearchMap(@NotNull GTRecipeType type) {
            slot.ingredientMap.clear();
            slot.fluidInventory.reference2LongEntrySet().fastForEach(e -> {
                var a = e.getLongValue();
                if (a < 1) return;
                ((IAEFluidKey) (Object) e.getKey()).gtolib$convert(a, slot.ingredientMap);
            });
            slot.itemInventory.reference2LongEntrySet().fastForEach(e -> {
                var a = e.getLongValue();
                if (a < 1) return;
                ((IAEItemKey) (Object) e.getKey()).gtolib$convert(a, slot.ingredientMap);
            });
            return slot.ingredientMap;
        }

        @Override
        public void handleRecipeItem(IO io, GTRecipe recipe, List<Content<ItemIngredient>> left, boolean simulate) {
            slot.handleItemInternal(left, simulate);
        }

        @Override
        public void handleRecipeFluid(IO io, GTRecipe recipe, List<Content<FluidIngredient>> left, boolean simulate) {
            slot.handleFluidInternal(left, simulate);
        }

        @Override
        public boolean canHandleItem() {
            return true;
        }

        @Override
        public boolean canHandleFluid() {
            return true;
        }
    }
}
