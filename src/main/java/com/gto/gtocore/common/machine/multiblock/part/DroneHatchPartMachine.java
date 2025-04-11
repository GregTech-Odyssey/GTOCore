package com.gto.gtocore.common.machine.multiblock.part;

import com.gto.gtocore.api.misc.Drone;
import com.gto.gtocore.utils.GTOUtils;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IWorkableMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredIOPartMachine;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DroneHatchPartMachine extends TieredIOPartMachine implements IMachineLife {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            DroneHatchPartMachine.class, TieredIOPartMachine.MANAGED_FIELD_HOLDER);

    @Persisted
    private final DroneItemStackHandler inventory;

    @Getter
    private final int size;

    public DroneHatchPartMachine(IMachineBlockEntity holder, int tier) {
        super(holder, tier, IO.IN);
        int sizeRoot = tier - 1;
        size = sizeRoot * sizeRoot;
        inventory = new DroneItemStackHandler(size);
    }

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public boolean onWorking(IWorkableMultiController controller) {
        if (getOffsetTimer() % 20 == 0) {
            for (int i = 0; i < inventory.getSlots(); ++i) {
                Drone drone = getDrone(i);
                if (drone != null) {
                    drone.work();
                }
            }
        }
        return true;
    }

    @Nullable
    public Drone getFirstUsableDrone(BlockPos pos1, BlockPos pos2) {
        for (int i = 0; i < size; i++) {
            Drone drone = getDrone(i);
            if (drone != null && !drone.isWork() && GTOUtils.calculateDistance(pos1, pos2) < drone.getRange()) {
                return drone;
            }
        }
        return null;
    }

    @Nullable
    public Drone getDrone(int slot) {
        return inventory.drones[slot];
    }

    @Override
    public void onMachineRemoved() {
        if (getLevel() == null) return;
        for (int i = 0; i < inventory.getSlots(); ++i) {
            Drone drone = getDrone(i);
            if (drone != null && drone.isWork()) return;
            ItemStack stackInSlot = inventory.getStackInSlot(i);
            if (!stackInSlot.isEmpty()) {
                inventory.setStackInSlot(i, ItemStack.EMPTY);
                Block.popResource(this.getLevel(), this.getPos(), stackInSlot);
            }
        }
        inventory.clean();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!isRemote()) {
            inventory.load();
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        inventory.clean();
    }

    @Override
    public Widget createUIWidget() {
        int rowSize = (int) Math.sqrt(size);
        var group = new WidgetGroup(0, 0, 18 * rowSize + 16, 18 * rowSize + 16);
        var container = new WidgetGroup(4, 4, 18 * rowSize + 8, 18 * rowSize + 8);
        int index = 0;
        for (int y = 0; y < rowSize; y++) {
            for (int x = 0; x < rowSize; x++) {
                container.addWidget(new SlotWidget(inventory, index++, 4 + x * 18, 4 + y * 18, true, io.support(IO.IN))
                        .setBackgroundTexture(GuiTextures.SLOT)
                        .setIngredientIO(this.io == IO.IN ? IngredientIO.INPUT : IngredientIO.OUTPUT));
            }
        }

        container.setBackground(GuiTextures.BACKGROUND_INVERSE);
        group.addWidget(container);

        return group;
    }

    @Override
    public boolean canShared() {
        return false;
    }

    private static final class DroneItemStackHandler extends CustomItemStackHandler {

        private final Drone[] drones;

        private DroneItemStackHandler(int size) {
            super(size);
            drones = new Drone[getSlots()];
        }

        private void load() {
            for (int i = 0; i < getSlots(); ++i) {
                Drone drone = Drone.create(getStackInSlot(i));
                if (drone != null) {
                    drones[i] = drone;
                }
            }
        }

        private void clean() {
            for (int i = 0; i < getSlots(); ++i) {
                drones[i] = null;
            }
        }

        @Override
        public void setStackInSlot(int index, @NotNull ItemStack stack) {
            Drone d = drones[index];
            if (d != null && d.isWork()) return;
            drones[index] = null;
            Drone drone = Drone.create(stack);
            if (drone != null) {
                drones[index] = drone;
                super.setStackInSlot(index, stack);
            }
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            Drone d = drones[slot];
            if (d != null && d.isWork()) return stack;
            drones[slot] = null;
            Drone drone = Drone.create(stack);
            if (drone != null) {
                if (!simulate) {
                    drones[slot] = drone;
                    stacks.set(slot, stack);
                    onContentsChanged(slot);
                }
                return ItemStack.EMPTY;
            }
            return stack;
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            Drone drone = drones[slot];
            if (drone != null && drone.isWork()) return ItemStack.EMPTY;
            drones[slot] = null;
            ItemStack stack = getStackInSlot(slot);
            if (!simulate) {
                stacks.set(slot, ItemStack.EMPTY);
                onContentsChanged(slot);
            } else {
                return stack.copy();
            }
            return stack;
        }
    }
}
