package com.gtocore.common.machine.multiblock.part;

import com.gtocore.api.gui.configurators.MultiMachineModeFancyConfigurator;
import com.gtocore.common.data.GTORecipeTypes;

import com.gtolib.api.annotation.DataGeneratorScanned;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.trait.CircuitHandler;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.machine.multiblock.part.DualHatchPartMachine;
import com.gregtechceu.gtceu.utils.TaskHandler;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

import com.hepdd.gtmthings.api.machine.IProgrammableMachine;
import com.hepdd.gtmthings.common.item.VirtualItemProviderBehavior;
import com.hepdd.gtmthings.data.CustomItems;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@DataGeneratorScanned
public class ProgrammableHatchPartMachine extends DualHatchPartMachine implements IProgrammableMachine {

    @Persisted
    @DescSynced
    private final ArrayList<GTRecipeType> recipeTypes = new ArrayList<>();
    @Persisted
    @DescSynced
    private GTRecipeType recipeType = GTORecipeTypes.HATCH_COMBINED;

    public ProgrammableHatchPartMachine(MetaMachineBlockEntity holder, int tier, IO io, Object... args) {
        super(holder, tier, io, args);
    }

    private void changeMode(GTRecipeType type) {
        this.recipeType = type == null ? GTORecipeTypes.HATCH_COMBINED : type;
        this.getHandlerList().external.recipeType = type;
    }

    @Override
    public void onPaintingColorChanged(int color) {
        super.onPaintingColorChanged(color);
        if (getLevel() instanceof ServerLevel serverLevel) {
            TaskHandler.enqueueServerTask(serverLevel, () -> this.getHandlerList().external.recipeType = recipeType == GTORecipeTypes.HATCH_COMBINED ? null : recipeType, 1);
        }
    }

    @Override
    protected @NotNull NotifiableItemStackHandler createInventory(Object @NotNull... args) {
        return new NotifiableItemStackHandler(this, getInventorySize(), io).setFilter(itemStack -> !(itemStack.hasTag() && itemStack.is(CustomItems.VIRTUAL_ITEM_PROVIDER.get())));
    }

    @Override
    protected @NotNull NotifiableItemStackHandler createCircuitItemHandler(Object... args) {
        if (args.length > 0 && args[0] instanceof IO io && io == IO.IN) {
            return new ProgrammableCircuitHandler(this);
        } else {
            return NotifiableItemStackHandler.empty(this);
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (recipeType == GTORecipeTypes.DUMMY_RECIPES) {
            recipeType = GTORecipeTypes.HATCH_COMBINED;
        }
        this.getHandlerList().external.recipeType = recipeType == GTORecipeTypes.HATCH_COMBINED ? null : recipeType;
    }

    @Override
    public void attachSideTabs(TabsWidget sideTabs) {
        super.attachSideTabs(sideTabs);
        sideTabs.attachSubTab(new MultiMachineModeFancyConfigurator(recipeTypes, recipeType, this::changeMode));
    }

    @Override
    public void addedToController(@NotNull IMultiController controller) {
        super.addedToController(controller);
        this.recipeTypes.clear();
        this.recipeTypes.addAll(MultiMachineModeFancyConfigurator.extractRecipeTypes(this.getControllers()));
    }

    @Override
    public void removedFromController(@NotNull IMultiController controller) {
        super.removedFromController(controller);
        this.recipeTypes.clear();
        this.recipeTypes.addAll(MultiMachineModeFancyConfigurator.extractRecipeTypes(this.getControllers()));
    }

    @Override
    public boolean isProgrammable() {
        return true;
    }

    @Override
    public void setProgrammable(boolean programmable) {}

    public static class ProgrammableCircuitHandler extends CircuitHandler {

        public ProgrammableCircuitHandler(MetaMachine machine) {
            super(machine, IO.IN, s -> new ProgrammableHandler(machine));
        }

        private static class ProgrammableHandler extends ItemStackHandler {

            private final IProgrammableMachine machine;

            private ProgrammableHandler(Object machine) {
                super(1);
                this.machine = machine instanceof IProgrammableMachine programmableMachine ? programmableMachine : null;
            }

            @NotNull
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                if (machine.isProgrammable() && stack.is(CustomItems.VIRTUAL_ITEM_PROVIDER.get())) {
                    setStackInSlot(slot, VirtualItemProviderBehavior.getVirtualItem(stack));
                    return ItemStack.EMPTY;
                }
                return stack;
            }
        }
    }
}
