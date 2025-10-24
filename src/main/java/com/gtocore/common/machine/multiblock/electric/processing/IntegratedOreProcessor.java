package com.gtocore.common.machine.multiblock.electric.processing;

import com.gtolib.api.machine.multiblock.CrossRecipeMultiblockMachine;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import net.minecraft.network.chat.Component;

import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class IntegratedOreProcessor extends CrossRecipeMultiblockMachine {

    @Persisted
    private boolean repeatedRecipes = true;

    public IntegratedOreProcessor(MetaMachineBlockEntity holder) {
        super(holder, false, true, MachineUtils::getHatchParallel);
    }

    @Override
    public void customText(@NotNull List<Component> list) {
        super.customText(list);
        if (getSubFormedAmount() > 0) {
            list.add(Component.translatable("gtocore.machine.repeated_recipes", ComponentPanelWidget.withButton(repeatedRecipes ? Component.translatable("gtocore.machine.on") : Component.translatable("gtocore.machine.off"), "toggle")));
        }
    }

    @Override
    public void handleDisplayClick(String componentData, ClickData clickData) {
        if (!clickData.isRemote) {
            if (componentData.equals("toggle")) {
                repeatedRecipes = !repeatedRecipes;
            }
        } else {
            super.handleDisplayClick(componentData, clickData);
        }
    }

    @Override
    public int getThread() {
        return getSubFormedAmount() > 0 ? 8 : 1;
    }

    @Override
    public boolean isRepeatedRecipes() {
        return repeatedRecipes;
    }
}
