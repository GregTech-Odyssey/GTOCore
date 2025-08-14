package com.gtocore.common.machine.multiblock.part.ae;

import com.gtolib.api.machine.trait.InaccessibleInfiniteHandler;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.feature.IInteractedMachine;
import com.gregtechceu.gtceu.integration.ae2.gui.widget.list.AEListGridWidget;
import com.gregtechceu.gtceu.integration.ae2.utils.KeyStorage;

import net.minecraft.MethodsReturnNonnullByDefault;

import appeng.api.config.Actionable;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public final class MEOutputBusPartMachine extends MEPartMachine implements IInteractedMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            MEOutputBusPartMachine.class, MEPartMachine.Companion.getMANAGED_FIELD_HOLDER());

    @Persisted
    private final KeyStorage internalBuffer;

    public MEOutputBusPartMachine(MetaMachineBlockEntity holder) {
        super(holder, IO.OUT);
        internalBuffer = new KeyStorage();
        new InaccessibleInfiniteHandler(this, internalBuffer);
    }

    @Override
    public void onMachineRemoved() {
        var grid = getMainNode().getGrid();
        if (grid != null && !internalBuffer.isEmpty()) {
            for (var entry : internalBuffer) {
                grid.getStorageService().getInventory().insert(entry.getKey(), entry.getLongValue(), Actionable.MODULATE, getActionSourceField());
            }
        }
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public Widget createUIWidget() {
        WidgetGroup group = new WidgetGroup(0, 0, 170, 65);
        group.addWidget(new LabelWidget(5, 0, () -> this.getOnlineField() ? "gtceu.gui.me_network.online" : "gtceu.gui.me_network.offline"));
        group.addWidget(new LabelWidget(5, 10, "gtceu.gui.waiting_list"));
        group.addWidget(new AEListGridWidget.Item(5, 20, 3, this.internalBuffer));
        return group;
    }
}
