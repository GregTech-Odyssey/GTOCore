package com.gtocore.common.machine.multiblock.part;

import com.gtocore.api.gui.configurators.MultiMachineModeFancyConfigurator;
import com.gtocore.common.data.GTORecipeTypes;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.machine.trait.ExtendedRecipeHandlerList;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.trait.CircuitHandler;
import com.gregtechceu.gtceu.api.machine.trait.IRecipeHandlerTrait;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.machine.multiblock.part.DualHatchPartMachine;

import net.minecraft.world.item.ItemStack;

import com.hepdd.gtmthings.api.machine.IProgrammableMachine;
import com.hepdd.gtmthings.common.item.VirtualItemProviderBehavior;
import com.hepdd.gtmthings.data.CustomItems;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

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
    public @NotNull RecipeHandlerList getHandlerList() {
        if (recipeHandlerList == null) {
            List<IRecipeHandler<?>> handlers = new ArrayList<>();
            for (var trait : traits) {
                if (trait instanceof IRecipeHandlerTrait<?> rht && rht.isAvailable() && rht.getHandlerIO() == IO.IN) {
                    handlers.add(rht);
                }
            }
            recipeHandlerList = new ProgrammableRHL(IO.IN, this);
            recipeHandlerList.addHandlers(handlers);
        }
        return recipeHandlerList;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (recipeType == GTORecipeTypes.DUMMY_RECIPES) {
            recipeType = GTORecipeTypes.HATCH_COMBINED;
        }
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

    private static class ProgrammableRHL extends ExtendedRecipeHandlerList {

        private final ProgrammableHatchPartMachine part;

        private ProgrammableRHL(IO handlerIO, ProgrammableHatchPartMachine part) {
            super(handlerIO, part);
            this.part = part;
        }

        @Override
        public ExtendedRecipeHandlerList wrapper() {
            return new ProgrammableRHL(IO.IN, part);
        }

        @Override
        public boolean findRecipe(IRecipeCapabilityHolder holder, GTRecipeType recipeType, Predicate<GTRecipe> canHandle) {
            final var type = part.recipeType;
            if (type != GTORecipeTypes.HATCH_COMBINED && type != recipeType && holder instanceof IRecipeLogicMachine machine && !machine.disabledCombined()) {
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
    }
}
