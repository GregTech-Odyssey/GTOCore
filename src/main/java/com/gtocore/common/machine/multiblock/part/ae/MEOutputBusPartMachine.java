package com.gtocore.common.machine.multiblock.part.ae;

import com.gtolib.api.machine.trait.InaccessibleInfiniteHandler;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.integration.ae2.gui.widget.list.AEListGridWidget;
import com.gregtechceu.gtceu.integration.ae2.utils.KeyStorage;

import net.minecraft.MethodsReturnNonnullByDefault;

import appeng.api.config.Actionable;
import appeng.api.networking.IGridNodeListener;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public final class MEOutputBusPartMachine extends MEPartMachine {

    @Persisted
    private final KeyStorage internalBuffer;
    private final InaccessibleInfiniteHandler handler;

    public MEOutputBusPartMachine(MetaMachineBlockEntity holder) {
        super(holder, IO.OUT);
        internalBuffer = new KeyStorage();
        handler = new InaccessibleInfiniteHandler(this, internalBuffer);
    }

    @Override
    public void setWorkingEnabled(boolean workingEnabled) {
        super.setWorkingEnabled(workingEnabled);
        handler.updateAutoOutputSubscription();
    }

    @Override
    public void onMainNodeStateChanged(IGridNodeListener.State reason) {
        super.onMainNodeStateChanged(reason);
        handler.updateAutoOutputSubscription();
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
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        super.superAttachConfigurators(configuratorPanel);
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
