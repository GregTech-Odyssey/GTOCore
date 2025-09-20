package com.gtocore.common.machine.multiblock.part.ae;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.config.PowerUnits;
import appeng.api.networking.energy.IAEPowerStorage;
import appeng.api.networking.events.GridPowerStorageStateChanged;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.integration.ae2.machine.feature.IGridConnectedMachine;
import com.gtocore.common.machine.multiblock.noenergy.MEEnergySubstationMachine;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

import static java.lang.Math.min;

public class MEEnergyAccessPartMachine extends MEPartMachine implements IMachineLife, IGridConnectedMachine, IAEPowerStorage {

    public MEEnergyAccessPartMachine(MetaMachineBlockEntity holder) {
        super(holder,IO.NONE);
        this.getMainNode().addService(IAEPowerStorage.class, this);
    }

    @Override
    public void setOnline(boolean isOnline) {
        super.setOnline(isOnline);
    }

    private double EU2AE(long eu){
        return PowerUnits.FE.convertTo(PowerUnits.AE, eu)* ConfigHolder.INSTANCE.compat.energy.euToFeRatio;
    }
    private long AE2EU(double ae){
        return (long) Math.ceil(PowerUnits.AE.convertTo(PowerUnits.FE, ae)/ConfigHolder.INSTANCE.compat.energy.euToFeRatio);
    }
    private MEEnergySubstationMachine getController(){
        var ctrl=getControllers().isEmpty()?null:getControllers().first();
        return ctrl instanceof MEEnergySubstationMachine m?m:null;
    }
    private void postEnergyEvent(){
        if(this.getMainNode().getGrid()!=null){
            this.getMainNode().getGrid().postEvent(new GridPowerStorageStateChanged(this, GridPowerStorageStateChanged.PowerEventType.PROVIDE_POWER));
        }
    }

    @Override
    public void addedToController(@NotNull IMultiController controller) {
        super.addedToController(controller);
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
        if(getController()==null)return 0;
        if(getController().getWirelessEnergyContainer()==null)return 0;
        return EU2AE(getController().getWirelessEnergyContainer().getRate());
    }

    @Override
    public double getAECurrentPower() {
        if(getController()==null)return 0;
        if(getController().getWirelessEnergyContainer()==null)return 0;
        return EU2AE(getController().getWirelessEnergyContainer().getStorage()
                .min(BigInteger.valueOf(getController().getWirelessEnergyContainer().getRate())).longValue());
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


    public double extractAEPower(double amt, Actionable mode) {
        if(getController()==null)return 0;
        if(getController().getWirelessEnergyContainer()==null)return 0;
        double can_extract=min(getAECurrentPower(),amt);
        if(!mode.isSimulate()){
            getController().getWirelessEnergyContainer().removeEnergy(AE2EU(can_extract),getController());
        }
        return can_extract;
    }

    @Override
    public @NotNull MetaMachine self() {
        return this;
    }

    @Override
    public Widget createUIWidget() {
        WidgetGroup group = new WidgetGroup(0, 0, 170, 65);
        group.addWidget(new LabelWidget(5, 0, () -> this.getOnlineField() ? "gtceu.gui.me_network.online" : "gtceu.gui.me_network.offline"));
        return group;
    }


}
