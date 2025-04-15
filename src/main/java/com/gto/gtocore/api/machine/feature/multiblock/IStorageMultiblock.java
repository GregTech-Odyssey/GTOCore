package com.gto.gtocore.api.machine.feature.multiblock;

import com.gto.gtocore.api.item.MachineItemStackHandler;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineModifyDrops;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;

import net.minecraft.world.item.ItemStack;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;

import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

public interface IStorageMultiblock extends IMachineModifyDrops {

    NotifiableItemStackHandler getMachineStorage();

    default NotifiableItemStackHandler createMachineStorage(@Nullable Predicate<ItemStack> filter) {
        NotifiableItemStackHandler storage = new NotifiableItemStackHandler((MetaMachine) this, 1, IO.NONE, IO.BOTH, slots -> new MachineItemStackHandler(this::getSlotLimit, this::onMachineChanged));
        storage.setFilter(i -> {
            if (filter != null) {
                if (!filter.test(i)) return false;
            }
            return storageFilter(i);
        });
        return storage;
    }

    default int getSlotLimit() {
        return 64;
    }

    default boolean storageFilter(ItemStack itemStack) {
        return true;
    }

    default void onMachineChanged() {}

    default boolean isEmpty() {
        return getMachineStorage().isEmpty();
    }

    default ItemStack getStorageStack() {
        return getMachineStorage().getStackInSlot(0);
    }

    default Widget createUIWidget(Widget widget) {
        if (widget instanceof WidgetGroup group) {
            var size = group.getSize();
            group.addWidget(new SlotWidget(getMachineStorage().storage, 0, size.width - 30, size.height - 30, true, true).setBackground(GuiTextures.SLOT));
        }
        return widget;
    }

    @Override
    default void onDrops(List<ItemStack> drops) {
        ((MetaMachine) this).clearInventory(getMachineStorage().storage);
    }
}
