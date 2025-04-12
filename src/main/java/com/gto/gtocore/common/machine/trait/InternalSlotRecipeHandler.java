package com.gto.gtocore.common.machine.trait;

import com.gto.gtocore.api.machine.trait.IPatternBufferRecipeHandler;
import com.gto.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachine;

import com.gregtechceu.gtceu.api.capability.recipe.*;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableRecipeHandlerTrait;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import it.unimi.dsi.fastutil.objects.Object2LongOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class InternalSlotRecipeHandler {

    private final List<RecipeHandlerList> slotHandlers;

    public InternalSlotRecipeHandler(MEPatternBufferPartMachine buffer, MEPatternBufferPartMachine.InternalSlot[] slots) {
        this.slotHandlers = new ArrayList<>(slots.length);
        for (int i = 0; i < slots.length; i++) {
            var rhl = new SlotRHL(buffer, slots[i], i);
            slotHandlers.add(rhl);
        }
    }

    @Getter
    public static class SlotRHL extends RecipeHandlerList {

        private final SlotItemRecipeHandler itemRecipeHandler;
        private final SlotFluidRecipeHandler fluidRecipeHandler;

        private SlotRHL(MEPatternBufferPartMachine buffer, MEPatternBufferPartMachine.InternalSlot slot, int idx) {
            super(IO.IN);
            itemRecipeHandler = new SlotItemRecipeHandler(buffer, slot, idx);
            fluidRecipeHandler = new SlotFluidRecipeHandler(buffer, slot, idx);
            addHandlers(buffer.getCircuitInventory(), buffer.getShareInventory(), buffer.getShareTank(),
                    itemRecipeHandler, fluidRecipeHandler);
        }

        @Override
        public boolean isDistinct() {
            return true;
        }

        @Override
        public void setDistinct(boolean ignored, boolean notify) {}
    }

    @Getter
    private static class SlotItemRecipeHandler extends NotifiableRecipeHandlerTrait<Ingredient> implements IPatternBufferRecipeHandler {

        private final MEPatternBufferPartMachine.InternalSlot slot;
        private final int priority;

        private SlotItemRecipeHandler(MEPatternBufferPartMachine buffer, MEPatternBufferPartMachine.InternalSlot slot, int index) {
            super(buffer);
            this.slot = slot;
            this.priority = IFilteredHandler.HIGH + index + 1;
            slot.setOnContentsChanged(this::notifyListeners);
        }

        @Override
        public List<Ingredient> handleRecipe(IO io, GTRecipe recipe, List<?> left, boolean simulate) {
            List<Ingredient> list = (List<Ingredient>) left;
            if (io != IO.IN || slot.isItemEmpty()) return list;
            return handleRecipeInner(io, recipe, new ObjectArrayList<>(list), simulate);
        }

        @Override
        public List<Ingredient> handleRecipeInner(IO io, GTRecipe recipe, List<Ingredient> left, boolean simulate) {
            return slot.handleItemInternal(left, simulate);
        }

        @Override
        public @NotNull List<Object> getContents() {
            return (List) slot.getItems();
        }

        @Override
        public double getTotalContentAmount() {
            return slot.getItems().stream().mapToLong(ItemStack::getCount).sum();
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
        public Object2LongOpenCustomHashMap<ItemStack> getItemMap() {
            return slot.getItemInventory();
        }

        @Override
        public Object2LongOpenHashMap<FluidStack> getFluidMap() {
            return slot.getFluidInventory();
        }
    }

    @Getter
    private static class SlotFluidRecipeHandler extends NotifiableRecipeHandlerTrait<FluidIngredient> implements IPatternBufferRecipeHandler {

        private final MEPatternBufferPartMachine.InternalSlot slot;
        private final int priority;

        private SlotFluidRecipeHandler(MEPatternBufferPartMachine buffer, MEPatternBufferPartMachine.InternalSlot slot, int index) {
            super(buffer);
            this.slot = slot;
            this.priority = IFilteredHandler.HIGH + index + 1;
            slot.setOnContentsChanged(this::notifyListeners);
        }

        @Override
        public List<FluidIngredient> handleRecipe(IO io, GTRecipe recipe, List<?> left, boolean simulate) {
            List<FluidIngredient> list = (List<FluidIngredient>) left;
            if (io != IO.IN || slot.isFluidEmpty()) return list;
            return handleRecipeInner(io, recipe, new ObjectArrayList<>(list), simulate);
        }

        @Override
        public List<FluidIngredient> handleRecipeInner(IO io, GTRecipe recipe, List<FluidIngredient> left, boolean simulate) {
            return slot.handleFluidInternal(left, simulate);
        }

        @Override
        public @NotNull List<Object> getContents() {
            return (List) slot.getFluids();
        }

        @Override
        public double getTotalContentAmount() {
            return slot.getFluids().stream().mapToLong(FluidStack::getAmount).sum();
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
        public Object2LongOpenCustomHashMap<ItemStack> getItemMap() {
            return slot.getItemInventory();
        }

        @Override
        public Object2LongOpenHashMap<FluidStack> getFluidMap() {
            return slot.getFluidInventory();
        }
    }
}
