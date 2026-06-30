package com.gtocore.common.machine.trait;

import com.gtocore.common.machine.multiblock.part.ae.AbstractRecipeInternalSlot;
import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachine;

import com.gtolib.api.recipe.lookup.IIngredientConvertible;

import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableRecipeHandlerTrait;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.handler.IFilteredHandler;
import com.gregtechceu.gtceu.api.recipe.handler.IO;
import com.gregtechceu.gtceu.api.recipe.handler.IRecipeHandler;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.ItemIngredient;
import com.gregtechceu.gtceu.utils.function.ObjLongPredicate;

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
            slotHandlers.add(PatternBufferRHL.of(slot, buffer));
        }
    }

    public abstract static class AbstractRHL<S extends AbstractRecipeInternalSlot> extends RecipeHandlerUnit {

        protected final S slot;

        protected AbstractRHL(S slot, IMultiPart part, IRecipeHandler... handlers) {
            super(IO.IN, part, handlers);
            this.isDistinct = true;
            this.slot = slot;
            this.priority = IFilteredHandler.HIGH;
        }

        protected abstract @Nullable GTRecipeDefinition getCachedRecipe();

        protected abstract void clearCachedRecipe();

        protected abstract @Nullable GTRecipeType getEffectiveRecipeType(GTRecipeType recipeType);

        protected abstract void onRecipeHandled(GTRecipe recipe);

        @Override
        public abstract RecipeHandlerUnit wrapper(Collection<IRecipeHandler> handlers);

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

    static class PatternSlotRHL extends AbstractRHL<MEPatternBufferPartMachine.InternalSlot> {

        PatternSlotRHL(MEPatternBufferPartMachine.InternalSlot slot, IMultiPart part, IRecipeHandler... handlers) {
            super(slot, part, handlers);
        }

        private PatternSlotRHL(IRecipeHandler handler, MEPatternBufferPartMachine.InternalSlot slot, MEPatternBufferPartMachine buffer) {
            super(slot, buffer, handler, slot.circuitInventory, slot.shareInventory, slot.shareTank, buffer.circuitInventorySimulated, buffer.shareInventory, buffer.shareTank);
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

        @Override
        public RecipeHandlerUnit wrapper(Collection<IRecipeHandler> handlers) {
            return new PatternSlotRHL(slot, null, handlers.toArray(new IRecipeHandler[0]));
        }
    }

    final static class PatternBufferRHL extends PatternSlotRHL {

        final SlotRecipeHandler recipeHandler;

        private static PatternBufferRHL of(MEPatternBufferPartMachine.InternalSlot slot, MEPatternBufferPartMachine buffer) {
            return new PatternBufferRHL(new SlotRecipeHandler(buffer, slot), slot, buffer);
        }

        private PatternBufferRHL(SlotRecipeHandler handler, MEPatternBufferPartMachine.InternalSlot slot, MEPatternBufferPartMachine buffer) {
            super(handler, slot, buffer);
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
        public IO getHandlerIO() {
            return IO.IN;
        }

        @Override
        public boolean forEachItems(ObjLongPredicate<ItemStack> function) {
            for (var it = slot.itemInventory.iterator(); it.hasNext();) {
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
            slot.itemInventory.fastForEach((k, v) -> {
                if (v < 1) return;
                function.accept(k.getReadOnlyStack(), v);
            });
        }

        @Override
        public boolean forEachFluids(ObjLongPredicate<FluidStack> function) {
            for (var it = slot.fluidInventory.iterator(); it.hasNext();) {
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
            slot.fluidInventory.fastForEach((k, v) -> {
                if (v < 1) return;
                function.accept(k.getReadOnlyStack(), v);
            });
        }

        @Override
        public IntLongMap getSearchMap(@NotNull GTRecipeType type) {
            if (slot.isContentsChanged()) {
                slot.ingredientMap.clear();
                slot.fluidInventory.fastForEach((k, v) -> {
                    if (v < 1) return;
                    ((IIngredientConvertible) (Object) k).gtolib$convert(v, slot.ingredientMap);
                });
                slot.itemInventory.fastForEach((k, v) -> {
                    if (v < 1) return;
                    ((IIngredientConvertible) (Object) k).gtolib$convert(v, slot.ingredientMap);
                });
            }
            return slot.ingredientMap;
        }

        @Override
        public boolean handleRecipeItem(IO io, GTRecipe recipe, List<Content<ItemIngredient>> items, boolean simulate) {
            return slot.handleItemInternal(items, simulate);
        }

        @Override
        public boolean handleRecipeFluid(IO io, GTRecipe recipe, List<Content<FluidIngredient>> fluids, boolean simulate) {
            return slot.handleFluidInternal(fluids, simulate);
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
