package com.gtocore.common.machine.multiblock.part.maintenance;

import com.gtolib.api.machine.feature.IGravityPartMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.widget.IntInputWidget;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IWorkableMultiController;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.machine.multiblock.part.AutoMaintenanceHatchPartMachine;

import net.minecraft.util.Mth;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

public final class GravityHatchPartMachine extends AutoMaintenanceHatchPartMachine implements IGravityPartMachine {

    public GravityHatchPartMachine(MetaMachineBlockEntity blockEntity) {
        super(blockEntity);
    }

    @Persisted
    private int currentGravity;

    @Override
    public Widget createUIWidget() {
        WidgetGroup GravityGroup = new WidgetGroup(0, 0, 100, 20);
        GravityGroup.addWidget(new IntInputWidget(this::getCurrentGravity, this::setCurrentGravity).setMin(0).setMax(100));
        return GravityGroup;
    }

    private void setCurrentGravity(int gravity) {
        currentGravity = Mth.clamp(gravity, 0, 100);
    }

    @Override
    public GTRecipe modifyRecipe(GTRecipe recipe) {
        return recipe;
    }

    @Override
    public boolean afterWorking(IWorkableMultiController controller) {
        return true;
    }

    @Override
    public int getCurrentGravity() {
        return this.currentGravity;
    }
}
