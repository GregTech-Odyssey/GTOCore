package com.gtocore.common.machine.multiblock.part;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.handler.IO;

import net.minecraft.MethodsReturnNonnullByDefault;

import com.gto.datasynclib.annotations.SaveToDisk;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import lombok.Getter;

import javax.annotation.ParametersAreNonnullByDefault;

@Getter
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class BlockBusPartMachine extends MultiblockPartMachine implements IMachineLife {

    @SaveToDisk
    private final NotifiableItemStackHandler inventory;

    public BlockBusPartMachine(MetaMachineBlockEntity holder) {
        super(holder);
        inventory = new NotifiableItemStackHandler(this, 100, IO.NONE, IO.BOTH);
    }

    @Override
    public void onMachineRemoved() {
        clearInventory(inventory.storage);
    }

    @Override
    public Widget createUIWidget() {
        int rowSize = 10;
        var group = new WidgetGroup(0, 0, 18 * rowSize + 16, 18 * rowSize + 16);
        var container = new WidgetGroup(4, 4, 18 * rowSize + 8, 18 * rowSize + 8);
        int index = 0;
        for (int y = 0; y < rowSize; y++) {
            for (int x = 0; x < rowSize; x++) {
                container.addWidget(new SlotWidget(inventory.storage, index, 4 + x * 18, 4 + y * 18, true, true).setBackgroundTexture(GuiTextures.SLOT).setIngredientIO(IngredientIO.INPUT));
                index++;
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
}
