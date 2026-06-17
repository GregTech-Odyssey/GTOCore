package com.gtocore.common.machine.multiblock.part;

import com.gtolib.api.machine.trait.NotifiableCatalystHandler;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.machine.multiblock.part.WorkableTieredIOPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.handler.IO;

import net.minecraft.MethodsReturnNonnullByDefault;

import com.gto.datasynclib.annotations.SaveToDisk;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.jei.IngredientIO;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class CatalystHatchPartMachine extends WorkableTieredIOPartMachine {

    @SaveToDisk
    private final NotifiableItemStackHandler inventory;

    public CatalystHatchPartMachine(MetaMachineBlockEntity holder, int tier) {
        super(holder, tier, IO.IN);
        this.inventory = new NotifiableCatalystHandler(this, tier == 2 ? 4 : 36, true);
    }

    @Override
    public void onPaintingColorChanged(int color) {
        getHandlerUnit().setColor(color, true);
    }

    @Override
    public Widget createUIWidget() {
        int rowSize = tier == 2 ? 2 : 6;
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
}
