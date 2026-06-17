package com.gtocore.common.machine.multiblock.part.ae;

import com.gtocore.common.data.GTORecipeDataKeys;

import com.gtolib.api.machine.multiblock.TierCasingMultiblockMachine;
import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.recipe.handler.IO;
import com.gregtechceu.gtceu.config.ConfigHolder;

import net.minecraft.nbt.CompoundTag;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.config.PowerUnits;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.energy.IAEPowerStorage;
import appeng.api.networking.events.GridPowerStorageStateChanged;
import appeng.me.service.EnergyService;

import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import org.jetbrains.annotations.NotNull;

import static java.lang.Math.min;

public class MEEnergyAccessPartMachine extends MEPartMachine implements IAEPowerStorage {

    private double ratio = ConfigHolder.INSTANCE.compat.energy.euToFeRatio;
    private TierCasingMultiblockMachine controller = null;

    public MEEnergyAccessPartMachine(MetaMachineBlockEntity holder) {
        super(holder, IO.NONE);
        this.getMainNode().addService(IAEPowerStorage.class, this);
    }

    @Override
    public void setOnline(boolean isOnline) {
        super.setOnline(isOnline);
        postEnergyEvent();
    }

    private double EU2AE(long eu) {
        return PowerUnits.FE.convertTo(PowerUnits.AE, eu) * ratio;
    }

    private long AE2EU(double ae) {
        return MathUtil.saturatedCast(PowerUnits.AE.convertTo(PowerUnits.FE, ae) / ratio);
    }

    @Override
    public void setWorkingEnabled(boolean workingEnabled) {
        super.setWorkingEnabled(workingEnabled);
        if (workingEnabled) postEnergyEvent();
    }

    private void postEnergyEvent() {
        if (controller == null) {
            return;
        }
        updateRatio();
        if (this.getMainNode().getGrid() != null) {
            this.getMainNode().getGrid().postEvent(new GridPowerStorageStateChanged(this, GridPowerStorageStateChanged.PowerEventType.PROVIDE_POWER));
        }
    }

    private void updateRatio() {
        this.ratio = ConfigHolder.INSTANCE.compat.energy.euToFeRatio;
        if (controller != null) {
            this.ratio *= 1 + 0.3 * controller.getCasingTier(GTORecipeDataKeys.GLASS_TIER);
            this.ratio *= controller.getSubFormedAmount() + 1;
        }
    }

    private void refreshEnergyService(Runnable change) {
        IGridNode node = this.getMainNode().getNode();
        IGrid grid = this.getMainNode().getGrid();
        if (node != null && grid != null && grid.getEnergyService() instanceof EnergyService energyService) {
            CompoundTag savedData = new CompoundTag();
            energyService.saveNodeData(node, savedData);
            energyService.removeNode(node);
            change.run();
            updateRatio();
            energyService.addNode(node, savedData);
        } else {
            change.run();
            updateRatio();
        }
    }

    @Override
    public void removedFromController(@NotNull IMultiController controller) {
        super.removedFromController(controller);
        refreshEnergyService(() -> this.controller = null);
    }

    @Override
    public void addedToController(@NotNull IMultiController controller) {
        super.addedToController(controller);
        TierCasingMultiblockMachine newController = (TierCasingMultiblockMachine) controller;
        refreshEnergyService(() -> this.controller = newController);
        postEnergyEvent();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        postEnergyEvent();
    }

    @Override
    public double injectAEPower(double amt, Actionable mode) {
        return amt;
    }

    @Override
    public double getAEMaxPower() {
        if (controller == null) {
            return 0;
        }
        return EU2AE(controller.getEnergyContainer().getEnergyCapacity());
    }

    @Override
    public double getAECurrentPower() {
        if (controller == null) {
            return 0;
        }
        if (!this.workingEnabled) return 0;
        return EU2AE(controller.getEnergyContainer().getEnergyStored());
    }

    @Override
    public boolean isAEPublicPowerStorage() {
        return true;
    }

    @Override
    public AccessRestriction getPowerFlow() {
        return AccessRestriction.READ;
    }

    @Override
    public double extractAEPower(double amt, Actionable mode, PowerMultiplier multiplier) {
        return multiplier.divide(this.extractAEPower(multiplier.multiply(amt), mode));
    }

    private double extractAEPower(double amt, Actionable mode) {
        if (controller == null) {
            return 0;
        }
        if (!this.workingEnabled) return 0;
        double can_extract = min(getAECurrentPower(), amt);
        if (!mode.isSimulate()) {
            controller.getEnergyContainer().changeEnergy(-AE2EU(can_extract));
        }
        return can_extract;
    }

    @Override
    public Widget createUIWidget() {
        WidgetGroup group = new WidgetGroup(0, 0, 170, 65);
        group.addWidget(new LabelWidget(5, 0, () -> this.getOnlineField() ? "gtceu.gui.me_network.online" : "gtceu.gui.me_network.offline"));
        return group;
    }
}
