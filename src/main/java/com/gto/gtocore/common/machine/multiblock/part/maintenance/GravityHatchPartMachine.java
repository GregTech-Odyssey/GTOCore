package com.gto.gtocore.common.machine.multiblock.part.maintenance;

import com.gto.gtocore.api.machine.part.IGravityPartMachine;

import com.gregtechceu.gtceu.api.gui.widget.IntInputWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.AutoMaintenanceHatchPartMachine;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.util.Mth;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class GravityHatchPartMachine extends AutoMaintenanceHatchPartMachine implements IGravityPartMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            GravityHatchPartMachine.class, MultiblockPartMachine.MANAGED_FIELD_HOLDER);

    public GravityHatchPartMachine(IMachineBlockEntity blockEntity) {
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

    public void setCurrentGravity(int gravity) {
        this.currentGravity = Mth.clamp(gravity, 0, 100);
    }

    @Override
    @NotNull
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
}
