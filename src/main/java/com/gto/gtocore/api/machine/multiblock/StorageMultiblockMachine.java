package com.gto.gtocore.api.machine.multiblock;

import com.gto.gtocore.api.machine.feature.multiblock.IStorageMultiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;

import java.util.function.Predicate;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class StorageMultiblockMachine extends ElectricMultiblockMachine implements IStorageMultiblock {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            StorageMultiblockMachine.class, ElectricMultiblockMachine.MANAGED_FIELD_HOLDER);

    @DescSynced
    @Persisted
    @Getter
    protected final NotifiableItemStackHandler machineStorage;
    private final int limit;

    public StorageMultiblockMachine(IMachineBlockEntity holder, int limit, @Nullable Predicate<ItemStack> filter) {
        super(holder);
        this.limit = limit;
        machineStorage = createMachineStorage(filter);
    }

    @Override
    public int getSlotLimit() {
        return limit;
    }

    @Override
    public Widget createUIWidget() {
        return createUIWidget(super.createUIWidget());
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
}
