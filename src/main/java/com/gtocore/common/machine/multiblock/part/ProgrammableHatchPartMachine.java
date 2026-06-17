package com.gtocore.common.machine.multiblock.part;

import com.gtocore.api.gui.configurators.MultiMachineModeFancyConfigurator;
import com.gtocore.common.data.GTORecipeTypes;

import com.gtolib.api.annotation.DataGeneratorScanned;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.trait.CircuitHandler;
import com.gregtechceu.gtceu.api.machine.trait.IRecipeHandlerTrait;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.handler.IO;
import com.gregtechceu.gtceu.api.recipe.handler.IRecipeHandler;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.common.machine.multiblock.part.DualHatchPartMachine;

import net.minecraft.world.item.ItemStack;

import com.gto.datasynclib.annotations.SaveToDisk;
import com.gto.datasynclib.annotations.SyncToClient;
import com.hepdd.gtmthings.api.machine.IProgrammableMachine;
import com.hepdd.gtmthings.common.item.VirtualItemProviderBehavior;
import com.hepdd.gtmthings.data.CustomItems;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;

@DataGeneratorScanned
public final class ProgrammableHatchPartMachine extends DualHatchPartMachine implements IProgrammableMachine {

    @SaveToDisk
    @SyncToClient
    private final ArrayList<GTRecipeType> recipeTypes = new ArrayList<>();
    @SaveToDisk
    @SyncToClient
    private GTRecipeType recipeType = null;

    public ProgrammableHatchPartMachine(MetaMachineBlockEntity holder, int tier, IO io, Object... args) {
        super(holder, tier, io, args);
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
    public RecipeHandlerUnit getHandlerUnit() {
        var list = getRecipeHandlerUnit();
        if (list == null) {
            List<IRecipeHandler> handlers = new ArrayList<>();
            IO handlerIO = null;
            for (var trait : self().getTraits()) {
                if (trait instanceof IRecipeHandlerTrait rht && rht.isAvailable() && rht.getHandlerIO() != IO.NONE) {
                    if (handlerIO == null) handlerIO = rht.getHandlerIO();
                    handlers.add(rht);
                }
            }

            if (handlers.isEmpty()) {
                list = RecipeHandlerUnit.NO_DATA;
                setRecipeHandlerUnit(list);
            } else {
                list = new ProgrammableRHL(handlerIO, this, handlers);
                setRecipeHandlerUnit(list);
            }
        }
        return list;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (recipeType == GTORecipeTypes.DUMMY_RECIPES || recipeType == GTORecipeTypes.HATCH_COMBINED) {
            recipeType = null;
        }
        MultiMachineModeFancyConfigurator.verify(recipeTypes, recipeType, () -> recipeType = null);
    }

    @Override
    public void attachSideTabs(TabsWidget sideTabs) {
        super.attachSideTabs(sideTabs);
        sideTabs.attachSubTab(new MultiMachineModeFancyConfigurator(recipeTypes, recipeType, this::setRecipeType));
    }

    @Override
    public void addedToController(@NotNull IMultiController controller) {
        super.addedToController(controller);
        this.recipeTypes.clear();
        this.recipeTypes.addAll(MultiMachineModeFancyConfigurator.extractRecipeTypes(this.getController()));
        MultiMachineModeFancyConfigurator.verify(recipeTypes, recipeType, () -> recipeType = null);
    }

    @Override
    public void setAvailableRecipeTypes(@NotNull GTRecipeType[] types) {
        this.recipeTypes.clear();
        this.recipeTypes.addAll(Arrays.asList(types));
        MultiMachineModeFancyConfigurator.verify(recipeTypes, recipeType, () -> recipeType = null);
    }

    @Override
    public void removedFromController(@NotNull IMultiController controller) {
        super.removedFromController(controller);
        this.recipeTypes.clear();
    }

    public void setRecipeType(GTRecipeType type) {
        if (type != recipeType) {
            recipeType = type;
            for (var c : getControllers()) {
                if (c instanceof IRecipeLogicMachine machine) {
                    machine.getRecipeLogic().markLastRecipeDirty();
                    machine.getRecipeLogic().updateTickSubscription();
                }
            }
        }
    }

    @Override
    public boolean swapIO() {
        // Programmable hatches should not be able to swap IO
        return false;
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
                if (machine.isProgrammable() && stack.hasTag() && stack.is(CustomItems.VIRTUAL_ITEM_PROVIDER.get())) {
                    setStackInSlot(slot, VirtualItemProviderBehavior.getVirtualItem(stack));
                    return ItemStack.EMPTY;
                }
                return stack;
            }
        }
    }

    private static class ProgrammableRHL extends RecipeHandlerUnit {

        private final ProgrammableHatchPartMachine part;

        private ProgrammableRHL(IO handlerIO, ProgrammableHatchPartMachine part, Collection<IRecipeHandler> handlers) {
            super(handlerIO, part, handlers.toArray(new IRecipeHandler[0]));
            this.part = part;
            this.priority = 10000;
        }

        @Override
        public RecipeHandlerUnit wrapper(Collection<IRecipeHandler> handlers) {
            return new ProgrammableRHL(IO.IN, part, handlers);
        }

        @Override
        public boolean findRecipe(GTRecipeType recipeType, BiPredicate<RecipeHandlerUnit, GTRecipeDefinition> canHandle) {
            final var type = part.recipeType;
            if (type != null && type != recipeType) {
                recipeType = type;
            }
            var map = this.getSearchMap(recipeType);
            if (map.isEmpty()) return false;
            return recipeType.search(this, map, canHandle);
        }
    }
}
