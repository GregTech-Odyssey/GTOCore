package com.gtocore.common.machine.trait;

import com.gtocore.common.machine.multiblock.part.ae.MEPatternBufferPartMachine;

import com.gtolib.api.machine.trait.ExtendedRecipeHandlerList;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeCapabilityMap;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.IRecipeHandlerTrait;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableRecipeHandlerTrait;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import it.unimi.dsi.fastutil.objects.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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
            rhl.rhl = this;
        }
    }

    public static abstract class AbstractRHL extends ExtendedRecipeHandlerList {

        public RecipeHandlerList rhl = this;
        public final MEPatternBufferPartMachine.InternalSlot slot;

        AbstractRHL(MEPatternBufferPartMachine.InternalSlot slot, MultiblockPartMachine part) {
            super(IO.IN, part);
            this.slot = slot;
        }

        @Override
        public boolean isDistinct() {
            return true;
        }

        public boolean handleRecipeContent(GTRecipe recipe, RecipeCapabilityMap<List<Object>> contents, boolean simulate) {
            if (slot.isEmpty()) return false;
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

    private static final class SlotItemRecipeHandler extends NotifiableRecipeHandlerTrait<Ingredient> {

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
        public double getTotalContentAmount() {
            double a = 0;
            for (ObjectIterator<Object2LongMap.Entry<ItemStack>> it = slot.itemInventory.object2LongEntrySet().fastIterator(); it.hasNext();) {
                a += it.next().getLongValue();
            }
            return a;
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
            return slot.itemInventory.isEmpty() ? null : slot.itemInventory;
        }

        @Override
        public boolean isRecipeOnly() {
            return true;
        }
    }

    private static final class SlotFluidRecipeHandler extends NotifiableRecipeHandlerTrait<FluidIngredient> {

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
        public double getTotalContentAmount() {
            double a = 0;
            for (ObjectIterator<Object2LongMap.Entry<FluidStack>> it = slot.fluidInventory.object2LongEntrySet().fastIterator(); it.hasNext();) {
                a += it.next().getLongValue();
            }
            return a;
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
        public Object2LongOpenHashMap<FluidStack> getFluidMap() {
            return slot.fluidInventory.isEmpty() ? null : slot.fluidInventory;
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
