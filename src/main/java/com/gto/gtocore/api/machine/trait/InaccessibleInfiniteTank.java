package com.gto.gtocore.api.machine.trait;

import com.gto.gtocore.api.machine.IMEHatchPart;
import com.gto.gtocore.integration.ae2.KeyMap;
import com.gto.gtocore.utils.FluidUtils;

import com.gregtechceu.gtceu.api.capability.IControllable;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.transfer.fluid.CustomFluidTank;
import com.gregtechceu.gtceu.integration.ae2.machine.feature.IGridConnectedMachine;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import appeng.api.networking.IGrid;
import appeng.api.stacks.AEFluidKey;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public final class InaccessibleInfiniteTank extends NotifiableFluidTank {

    private final FluidStorageDelegate delegate;

    private TickableSubscription updateSubs;

    public InaccessibleInfiniteTank(MetaMachine holder, KeyMap internalBuffer) {
        super(holder, List.of(new FluidStorageDelegate(internalBuffer)), IO.OUT, IO.NONE);
        internalBuffer.setOnContentsChanged(null);
        delegate = (FluidStorageDelegate) getStorages()[0];
        allowSameFluids = true;
    }

    @Override
    public void onMachineLoad() {
        super.onMachineLoad();
        updateSubs = getMachine().subscribeServerTick(updateSubs, this::updateTick);
    }

    @Override
    public void onMachineUnLoad() {
        super.onMachineUnLoad();
        if (updateSubs != null) {
            updateSubs.unsubscribe();
            updateSubs = null;
        }
    }

    private void updateTick() {
        if (machine instanceof IControllable controllable && !controllable.isWorkingEnabled()) return;
        if (machine instanceof IGridConnectedMachine connectedMachine && connectedMachine.isOnline() && connectedMachine.shouldSyncME() && connectedMachine.updateMEStatus()) {
            IGrid grid = connectedMachine.getMainNode().getGrid();
            synchronized (delegate.internalBuffer.storage) {
                if (grid != null && !delegate.internalBuffer.isEmpty()) {
                    delegate.internalBuffer.insertInventory(grid.getStorageService().getInventory(), ((IMEHatchPart) machine).gtocore$getActionSource());
                }
            }
        }
    }

    @Override
    public List<FluidIngredient> handleRecipeInner(IO io, GTRecipe recipe, List<FluidIngredient> left, boolean simulate) {
        if (!simulate && io == IO.OUT) {
            for (FluidIngredient ingredient : left) {
                if (ingredient.isEmpty()) continue;
                Fluid fluid = FluidUtils.getFirst(ingredient);
                if (fluid != null) {
                    delegate.fill(fluid, ingredient.getAmount(), ingredient.getNbt());
                }
            }
            return null;
        }
        return null;
    }

    @Override
    public List<FluidIngredient> handleRecipe(IO io, GTRecipe recipe, List<?> left, boolean simulate) {
        return handleRecipeInner(io, recipe, (List<FluidIngredient>) left, simulate);
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public @NotNull List<Object> getContents() {
        return Collections.emptyList();
    }

    @Override
    public double getTotalContentAmount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        return FluidStack.EMPTY;
    }

    @Override
    public void setFluidInTank(int tank, @NotNull FluidStack fluidStack) {}

    @Override
    public int getTankCapacity(int tank) {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return true;
    }

    private static class FluidStorageDelegate extends CustomFluidTank {

        private final KeyMap internalBuffer;

        private FluidStorageDelegate(KeyMap internalBuffer) {
            super(0);
            this.internalBuffer = internalBuffer;
        }

        private void fill(Fluid fluid, int amount, CompoundTag tag) {
            var key = AEFluidKey.of(fluid, tag);
            synchronized (internalBuffer.storage) {
                long oldValue = internalBuffer.storage.getOrDefault(key, 0);
                long changeValue = Math.min(Long.MAX_VALUE - oldValue, amount);
                if (changeValue > 0) {
                    internalBuffer.storage.put(key, oldValue + changeValue);
                }
            }
        }

        @Override
        public int getCapacity() {
            return Integer.MAX_VALUE;
        }

        @Override
        public void setFluid(FluidStack fluid) {}

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            return 0;
        }

        @Override
        public boolean supportsFill(int tank) {
            return false;
        }

        @Override
        public boolean supportsDrain(int tank) {
            return false;
        }
    }
}
