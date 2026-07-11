package com.gtocore.common.machine.multiblock.part;

import com.gtocore.api.gui.configurators.MultiMachineModeFancyConfigurator;
import com.gtocore.common.data.GTORecipeTypes;

import com.gtolib.api.annotation.DataGeneratorScanned;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.FancyTankConfigurator;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.trait.CircuitHandler;
import com.gregtechceu.gtceu.api.machine.trait.IRecipeHandlerTrait;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.handler.IO;
import com.gregtechceu.gtceu.api.recipe.handler.IRecipeHandler;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.transfer.fluid.CustomFluidTank;
import com.gregtechceu.gtceu.common.machine.multiblock.part.DualHatchPartMachine;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import appeng.api.config.Actionable;
import appeng.api.stacks.AEItemKey;

import com.gto.datasynclib.annotations.SaveToDisk;
import com.gto.datasynclib.annotations.SyncToClient;
import com.hepdd.gtmthings.api.machine.IProgrammableMachine;
import com.hepdd.gtmthings.common.item.VirtualFluidProviderBehavior;
import com.hepdd.gtmthings.common.item.VirtualItemProviderBehavior;
import com.hepdd.gtmthings.data.CustomItems;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiPredicate;

@DataGeneratorScanned
public final class ProgrammableHatchPartMachine extends DualHatchPartMachine implements IProgrammableMachine {

    @SaveToDisk
    @SyncToClient
    private final ArrayList<GTRecipeType> recipeTypes = new ArrayList<>();
    @SaveToDisk
    @SyncToClient
    private GTRecipeType recipeType = null;

    @SaveToDisk
    private final ProgrammableFluidHandler fluidTank = new ProgrammableFluidHandler(this);

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
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        configuratorPanel.attachConfigurators(new FancyTankConfigurator(fluidTank.getStorages(), Component.translatable("gui.gtceu.share_tank.title")));
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

    public @Nullable GTRecipeType getRecipeType() {
        return recipeType;
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

    private static final class ProgrammableFluidHandler extends NotifiableFluidTank {

        public ProgrammableFluidHandler(MetaMachine machine) {
            super(machine, Collections.singletonList(new FluidTank()), IO.IN, IO.IN);
        }

        @Override
        public boolean isNotConsumable() {
            return true;
        }

        @Override
        public @NotNull FluidStack getFluidInTank(int tank) {
            return FluidStack.EMPTY;
        }

        @Override
        public boolean handleRecipeFluid(IO io, GTRecipe recipe, List<Content<FluidIngredient>> fluids, boolean simulate) {
            if (simulate && io == IO.IN) {
                var stored = this.storages[0].getFluid();
                if (!stored.isEmpty()) {
                    var it = fluids.iterator();
                    while (it.hasNext()) {
                        var content = it.next();
                        if (content.chance == 0 && content.inner.test(stored)) {
                            it.remove();
                            break;
                        }
                    }
                }
            }
            return fluids.isEmpty();
        }

        private static final class FluidTank extends CustomFluidTank {

            private FluidTank() {
                super(1000);
            }

            @Override
            public int fill(FluidStack resource, FluidAction action) {
                return 0;
            }

            @Override
            public FluidStack drain(FluidStack resource, FluidAction action) {
                setFluid(FluidStack.EMPTY);
                return FluidStack.EMPTY;
            }

            @Override
            public FluidStack drain(int maxDrain, FluidAction action) {
                setFluid(FluidStack.EMPTY);
                return FluidStack.EMPTY;
            }
        }
    }

    public static class ProgrammableCircuitHandler extends CircuitHandler {

        public ProgrammableCircuitHandler(MetaMachine machine) {
            super(machine, IO.IN, s -> new ProgrammableHandler(machine));
        }

        private static class ProgrammableHandler extends ItemStackHandler {

            private final IProgrammableMachine machine;
            private final ProgrammableFluidHandler fluidTank;

            private ProgrammableHandler(Object machine) {
                super(1);
                this.machine = (IProgrammableMachine) machine;
                if (machine instanceof ProgrammableHatchPartMachine partMachine) {
                    this.fluidTank = partMachine.fluidTank;
                } else {
                    this.fluidTank = null;
                }
            }

            @Override
            public int insertExternal(AEItemKey itemKey, int amount, Actionable mode) {
                if (machine.isProgrammable() && itemKey.hasTag()) {
                    if (itemKey.item == CustomItems.VIRTUAL_ITEM_PROVIDER.get()) {
                        if (mode == Actionable.MODULATE) {
                            setStackInSlot(0, VirtualItemProviderBehavior.getVirtualItem(itemKey.getReadOnlyStack()));
                        }
                        return amount;
                    } else if (fluidTank != null && itemKey.item == CustomItems.VIRTUAL_FLUID_PROVIDER.get()) {
                        if (mode == Actionable.MODULATE) {
                            fluidTank.setFluidInTank(0, VirtualFluidProviderBehavior.getVirtualFluid(itemKey.getReadOnlyStack()));
                        }
                        return amount;
                    }
                }
                return 0;
            }

            @NotNull
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                return stack;
            }

            @Override
            public int insert(int slot, @NotNull ItemStack stack, int count, boolean simulate) {
                return 0;
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
