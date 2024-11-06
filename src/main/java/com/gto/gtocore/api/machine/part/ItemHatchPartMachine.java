package com.gto.gtocore.api.machine.part;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IMachineModifyDrops;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;

import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;

import lombok.Getter;

import java.util.List;
import java.util.function.Function;

import javax.annotation.ParametersAreNonnullByDefault;

@Getter
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemHatchPartMachine extends MultiblockPartMachine implements IMachineModifyDrops {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            ItemHatchPartMachine.class, MultiblockPartMachine.MANAGED_FIELD_HOLDER);

    @Persisted
    private final NotifiableItemStackHandler inventory;

    public ItemHatchPartMachine(IMachineBlockEntity holder, int slot, Function<ItemStack, Boolean> filter) {
        super(holder);
        this.inventory = createMachineStorage(slot, filter);
    }

    protected NotifiableItemStackHandler createMachineStorage(int value, Function<ItemStack, Boolean> filter) {
        NotifiableItemStackHandler storage = new NotifiableItemStackHandler(
                this, 1, IO.NONE, IO.BOTH, slots -> new ItemStackTransfer(1) {

                    @Override
                    public int getSlotLimit(int slot) {
                        return value;
                    }
                });
        storage.setFilter(filter);
        return storage;
    }

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
        return createSLOTWidget(inventory);
    }

    @Override
    public void onDrops(List<ItemStack> list) {
        clearInventory(this.inventory.storage);
    }
}
