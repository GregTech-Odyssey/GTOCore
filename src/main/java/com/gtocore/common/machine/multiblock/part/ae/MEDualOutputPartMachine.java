package com.gtocore.common.machine.multiblock.part.ae;

import com.gtolib.api.machine.trait.InaccessibleInfiniteHandler;
import com.gtolib.api.machine.trait.InaccessibleInfiniteTank;

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
import com.lowdragmc.lowdraglib.utils.Position;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MEDualOutputPartMachine extends MEPartMachine {

    @Persisted
    private final KeyStorage internalBuffer;
    @Persisted
    private final KeyStorage internalTankBuffer;
    private final InaccessibleInfiniteHandler handler;
    private final InaccessibleInfiniteTank tank;

    public MEDualOutputPartMachine(MetaMachineBlockEntity holder) {
        super(holder, IO.OUT);
        internalBuffer = new KeyStorage();
        handler = new InaccessibleInfiniteHandler(this, internalBuffer);
        internalTankBuffer = new KeyStorage();
        tank = new InaccessibleInfiniteTank(this, internalTankBuffer);
    }

    @Override
    public void setWorkingEnabled(boolean workingEnabled) {
        super.setWorkingEnabled(workingEnabled);
        handler.updateAutoOutputSubscription();
        tank.updateAutoOutputSubscription();
    }

    @Override
    public void onMainNodeStateChanged(IGridNodeListener.State reason) {
        super.onMainNodeStateChanged(reason);
        handler.updateAutoOutputSubscription();
        tank.updateAutoOutputSubscription();
    }

    @Override
    public void onMachineRemoved() {
        var grid = getMainNode().getGrid();
        if (grid != null) {
            if (!internalBuffer.isEmpty()) {
                for (var entry : internalBuffer) {
                    grid.getStorageService().getInventory().insert(entry.getKey(), entry.getLongValue(),
                            Actionable.MODULATE, getActionSource());
                }
            }
            if (!internalTankBuffer.isEmpty()) {
                for (var entry : internalTankBuffer) {
                    grid.getStorageService().getInventory().insert(entry.getKey(), entry.getLongValue(),
                            Actionable.MODULATE, getActionSource());
                }
            }
        }
    }

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        super.superAttachConfigurators(configuratorPanel);
    }

    @Override
    public Widget createUIWidget() {
        WidgetGroup group = new WidgetGroup(new Position(0, 0));
        group.addWidget(new LabelWidget(0, 0, () -> this.isOnline() ? "gtceu.gui.me_network.online" : "gtceu.gui.me_network.offline"));
        group.addWidget(new AEListGridWidget.Item(5, 20, 3, this.internalBuffer));
        group.addWidget(new AEListGridWidget.Fluid(5, 80, 3, this.internalTankBuffer));
        return group;
    }
}
