package com.gtocore.common.machine.multiblock.part;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.CircuitHandler;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.common.machine.multiblock.part.DualHatchPartMachine;

import net.minecraft.world.item.ItemStack;

import com.hepdd.gtmthings.api.machine.IProgrammableMachine;
import com.hepdd.gtmthings.common.item.VirtualItemProviderBehavior;
import com.hepdd.gtmthings.data.CustomItems;
import org.jetbrains.annotations.NotNull;

public class ProgrammableHatchPartMachine extends DualHatchPartMachine {

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

    public static class ProgrammableCircuitHandler extends CircuitHandler {

        public ProgrammableCircuitHandler(MetaMachine machine) {
            super(machine, IO.IN, s -> new ProgrammableHandler(s, machine));
        }

        private static class ProgrammableHandler extends ItemStackHandler {

            private final Object machine;

            private ProgrammableHandler(int size, Object machine) {
                super(size);
                this.machine = machine;
            }

            @NotNull
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                if (stack.is(CustomItems.VIRTUAL_ITEM_PROVIDER.get())) {
                    boolean allow = true;
                    if (machine instanceof IProgrammableMachine programmableMachine) {
                        allow = programmableMachine.isProgrammable();
                    }
                    if (allow) {
                        setStackInSlot(slot, VirtualItemProviderBehavior.getVirtualItem(stack));
                        return ItemStack.EMPTY;
                    }
                }
                return stack;
            }
        }
    }
}
