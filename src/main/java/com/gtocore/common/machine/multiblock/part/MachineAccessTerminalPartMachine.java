package com.gtocore.common.machine.multiblock.part;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredIOPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;

public final class MachineAccessTerminalPartMachine extends TieredIOPartMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(MachineAccessTerminalPartMachine.class, TieredIOPartMachine.MANAGED_FIELD_HOLDER);
    @Persisted
    private final NotifiableItemStackHandler inventory;

    public MachineAccessTerminalPartMachine(MetaMachineBlockEntity holder) {
        super(holder, 10, IO.IN);
        this.inventory = new NotifiableItemStackHandler(this, 64, IO.NONE, IO.NONE, CustomItemStackHandler::new);
        inventory.setFilter(i -> i.getItem() instanceof MetaMachineItem);
    }

    @Override
    @NotNull
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public Widget createUIWidget() {
        int rowSize = 8;
        var group = new WidgetGroup(0, 0, 18 * rowSize + 16, 18 * rowSize + 16);
        var container = new WidgetGroup(4, 4, 18 * rowSize + 8, 18 * rowSize + 8);
        int index = 0;
        for (int y = 0; y < rowSize; y++) {
            for (int x = 0; x < rowSize; x++) {
                container.addWidget(new SlotWidget(inventory.storage, index++, 4 + x * 18, 4 + y * 18, true, io.support(IO.IN)).setBackgroundTexture(GuiTextures.SLOT).setIngredientIO(this.io == IO.IN ? IngredientIO.INPUT : IngredientIO.OUTPUT));
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

    public NotifiableItemStackHandler getInventory() {
        return this.inventory;
    }
}
