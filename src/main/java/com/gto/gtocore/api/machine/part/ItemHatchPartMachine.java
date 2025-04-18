package com.gto.gtocore.api.machine.part;

import com.gto.gtocore.api.item.MachineItemStackHandler;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IMachineModifyDrops;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;

import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemHatchPartMachine extends MultiblockPartMachine implements IMachineModifyDrops {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            ItemHatchPartMachine.class, MultiblockPartMachine.MANAGED_FIELD_HOLDER);

    @Getter
    @DescSynced
    @Persisted
    protected NotifiableItemStackHandler inventory;
    private final int limit;

    public ItemHatchPartMachine(IMachineBlockEntity holder, int limit, @Nullable Predicate<ItemStack> filter) {
        super(holder);
        this.limit = limit;
        inventory = createMachineStorage(filter);
    }

    private NotifiableItemStackHandler createMachineStorage(@Nullable Predicate<ItemStack> filter) {
        NotifiableItemStackHandler storage = new NotifiableItemStackHandler(this, 1, IO.NONE, IO.BOTH, slots -> new MachineItemStackHandler(this::getSlotLimit));
        storage.setFilter(i -> {
            if (filter != null) {
                if (!filter.test(i)) return false;
            }
            return storageFilter(i);
        });
        storage.addChangedListener(this::onMachineChanged);
        return storage;
    }

    private int getSlotLimit() {
        return limit;
    }

    protected boolean storageFilter(ItemStack itemStack) {
        return true;
    }

    private void onMachineChanged() {}

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    public static Widget createSLOTWidget(NotifiableItemStackHandler inventory) {
        var group = new WidgetGroup(0, 0, 18 + 16, 18 + 16);
        var container = new WidgetGroup(4, 4, 18 + 8, 18 + 8);
        container.addWidget(new SlotWidget(inventory.storage, 0, 4, 4, true, true)
                .setBackground(GuiTextures.SLOT));
        group.addWidget(container);
        return group;
    }

    @Override
    public Widget createUIWidget() {
        return createSLOTWidget(getInventory());
    }

    @Override
    public void onDrops(List<ItemStack> list) {
        clearInventory(getInventory().storage);
    }

    @Override
    public boolean canShared() {
        return false;
    }
}
