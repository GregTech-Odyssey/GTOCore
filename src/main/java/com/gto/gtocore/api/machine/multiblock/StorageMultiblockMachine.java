package com.gto.gtocore.api.machine.multiblock;

import com.gto.gtocore.api.item.MachineItemStackHandler;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IMachineModifyDrops;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class StorageMultiblockMachine extends ElectricMultiblockMachine implements IMachineModifyDrops {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            StorageMultiblockMachine.class, ElectricMultiblockMachine.MANAGED_FIELD_HOLDER);

    @DescSynced
    @Persisted
    protected final NotifiableItemStackHandler machineStorage;
    private final int limit;

    public StorageMultiblockMachine(IMachineBlockEntity holder, int limit, @Nullable Predicate<ItemStack> filter) {
        super(holder);
        this.limit = limit;
        machineStorage = createMachineStorage(filter);
    }

    protected NotifiableItemStackHandler createMachineStorage(@Nullable Predicate<ItemStack> filter) {
        NotifiableItemStackHandler storage = new NotifiableItemStackHandler(this, 1, IO.NONE, IO.BOTH, slots -> new MachineItemStackHandler(this::getSlotLimit, this::onMachineChanged));
        storage.setFilter(i -> {
            if (filter != null) {
                if (!filter.test(i)) return false;
            }
            return storageFilter(i);
        });
        return storage;
    }

    protected int getSlotLimit() {
        return limit;
    }

    protected boolean storageFilter(ItemStack itemStack) {
        return true;
    }

    protected void onMachineChanged() {}

    @Override
    public void onDrops(List<ItemStack> drops) {
        clearInventory(machineStorage.storage);
    }

    @Override
    public Widget createUIWidget() {
        var widget = super.createUIWidget();
        if (widget instanceof WidgetGroup group) {
            var size = group.getSize();
            group.addWidget(new SlotWidget(machineStorage.storage, 0, size.width - 30, size.height - 30, true, true).setBackground(GuiTextures.SLOT));
        }
        return widget;
    }

    public boolean isEmpty() {
        return machineStorage.isEmpty();
    }

    protected ItemStack getStorageStack() {
        return machineStorage.getStackInSlot(0);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
}
