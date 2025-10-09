package com.gtocore.common.machine.multiblock.part;

import com.gtocore.api.gui.configurators.MultiMachineModeFancyConfigurator;

import com.gtolib.api.annotation.DataGeneratorScanned;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.trait.CircuitHandler;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.machine.multiblock.part.DualHatchPartMachine;

import net.minecraft.world.item.ItemStack;

import com.hepdd.gtmthings.api.machine.IProgrammableMachine;
import com.hepdd.gtmthings.common.item.VirtualItemProviderBehavior;
import com.hepdd.gtmthings.data.CustomItems;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@DataGeneratorScanned
public class ProgrammableHatchPartMachine extends DualHatchPartMachine implements IProgrammableMachine{

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            ProgrammableHatchPartMachine.class, DualHatchPartMachine.MANAGED_FIELD_HOLDER);
    @Persisted
    @DescSynced
    private final ArrayList<GTRecipeType> recipeTypes = new ArrayList<>();
    @Persisted
    @DescSynced
    private GTRecipeType mode = GTRecipeTypes.COMBINED_RECIPES;

    public ProgrammableHatchPartMachine(MetaMachineBlockEntity holder, int tier, IO io, Object... args) {
        super(holder, tier, io, args);
    }

    private void changeMode(GTRecipeType recipe) {
        this.mode = recipe;
        this.getHandlerList().setRecipeType(recipe);
        RecipeHandlerList.NOTIFY.accept(this);
    }

    @Override
    @NotNull
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
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
    public void attachConfigurators(@NotNull ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
    }

    @Override
    public void attachSideTabs(TabsWidget sideTabs) {
        super.attachSideTabs(sideTabs);
        sideTabs.attachSubTab(new MultiMachineModeFancyConfigurator(recipeTypes, mode, this::changeMode));
    }

    @Override
    public void addedToController(@NotNull IMultiController controller) {
        super.addedToController(controller);
        this.recipeTypes.clear();
        this.recipeTypes.addAll(MultiMachineModeFancyConfigurator.extractRecipeTypesCombined(this.getControllers()));
    }

    @Override
    public void removedFromController(@NotNull IMultiController controller) {
        super.removedFromController(controller);
        this.recipeTypes.clear();
        this.recipeTypes.addAll(MultiMachineModeFancyConfigurator.extractRecipeTypesCombined(this.getControllers()));
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
