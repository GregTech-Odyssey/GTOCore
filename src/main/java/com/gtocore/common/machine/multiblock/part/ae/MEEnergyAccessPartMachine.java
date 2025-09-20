package com.gtocore.common.machine.multiblock.part.ae;

import com.gtolib.api.machine.multiblock.TierCasingMultiblockMachine;
import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.config.ConfigHolder;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.config.PowerUnits;
import appeng.api.networking.energy.IAEPowerStorage;
import appeng.api.networking.events.GridPowerStorageStateChanged;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import org.jetbrains.annotations.NotNull;

import static com.gtolib.api.GTOValues.GLASS_TIER;
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
        this.ratio = ConfigHolder.INSTANCE.compat.energy.euToFeRatio;
        this.ratio *= 1 + 0.3 * controller.getCasingTier(GLASS_TIER);
        if (this.getMainNode().getGrid() != null) {
            this.getMainNode().getGrid().postEvent(new GridPowerStorageStateChanged(this, GridPowerStorageStateChanged.PowerEventType.PROVIDE_POWER));
        }
    }

    @Override
    public void removedFromController(@NotNull IMultiController controller) {
        super.removedFromController(controller);
        this.controller = null;
    }

    @Override
    public void addedToController(@NotNull IMultiController controller) {
        super.addedToController(controller);
        this.controller = (TierCasingMultiblockMachine) controller;
        postEnergyEvent();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        postEnergyEvent();
    }

    @Override
    public double injectAEPower(double amt, Actionable mode) {
        return 0;
    }

    @Override
    public double getAEMaxPower() {
        return Long.MAX_VALUE;
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
        return AccessRestriction.READ_WRITE;
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
