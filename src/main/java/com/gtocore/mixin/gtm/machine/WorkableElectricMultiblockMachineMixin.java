package com.gtocore.mixin.gtm.machine;

import com.gtolib.api.gui.OverclockConfigurator;
import com.gtolib.api.machine.feature.IElectricMachine;
import com.gtolib.api.machine.feature.IOverclockConfigMachine;
import com.gtolib.api.machine.feature.IPowerAmplifierMachine;
import com.gtolib.api.machine.feature.IUpgradeMachine;
import com.gtolib.api.machine.feature.multiblock.ICheckPatternMachine;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IControllable;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfigurator;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.misc.EnergyContainerList;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.machines.GTMultiMachines;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(WorkableElectricMultiblockMachine.class)
public abstract class WorkableElectricMultiblockMachineMixin extends WorkableMultiblockMachine implements IFancyUIMachine, IOverclockConfigMachine, IUpgradeMachine, IPowerAmplifierMachine, IElectricMachine {

    @Unique
    private double gtolib$powerAmplifier;
    @Unique
    private boolean gtolib$hasPowerAmplifier;
    @Unique
    private double gtolib$speed;
    @Unique
    private double gtolib$energy;
    @Unique
    private int gtolib$ocLimit;

    @Shadow(remap = false)
    protected EnergyContainerList energyContainer;

    @Shadow(remap = false)
    public abstract boolean isGenerator();

    @Shadow(remap = false)
    protected boolean batchEnabled;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void init(MetaMachineBlockEntity holder, Object[] args, CallbackInfo ci) {
        gtolib$powerAmplifier = 1;
        gtolib$speed = 1;
        gtolib$energy = 1;
        gtolib$ocLimit = 20;
    }

    protected WorkableElectricMultiblockMachineMixin(MetaMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    public GTRecipe fullModifyRecipe(GTRecipe recipe) {
        return doModifyRecipe(recipe);
    }

    @Override
    public boolean hasCheckButton() {
        return true;
    }

    @Override
    public void gtolib$setOCLimit(int number) {
        gtolib$ocLimit = number;
    }

    @Override
    public int gtolib$getOCLimit() {
        return gtolib$ocLimit;
    }

    @Override
    public void saveCustomPersistedData(@NotNull CompoundTag tag, boolean forDrop) {
        super.saveCustomPersistedData(tag, forDrop);
        if (gtolib$canUpgraded()) {
            tag.putDouble("speed", gtolib$speed);
            tag.putDouble("energy", gtolib$energy);
        }
        if (isGenerator()) return;
        tag.putInt("ocLimit", gtolib$ocLimit);
    }

    @Override
    public void loadCustomPersistedData(@NotNull CompoundTag tag) {
        super.loadCustomPersistedData(tag);
        if (gtolib$canUpgraded()) {
            double speed = tag.getDouble("speed");
            if (speed != 0) {
                gtolib$speed = speed;
            }
            double energy = tag.getDouble("energy");
            if (energy != 0) {
                gtolib$energy = energy;
            }
        }
        if (isGenerator()) return;
        gtolib$ocLimit = tag.getInt("ocLimit");
    }

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        if (self() instanceof IControllable controllable) {
            configuratorPanel.attachConfigurators(new IFancyConfiguratorButton.Toggle(GuiTextures.BUTTON_POWER.getSubTexture(0, 0, 1, 0.5), GuiTextures.BUTTON_POWER.getSubTexture(0, 0.5, 1, 0.5), controllable::isWorkingEnabled, (clickData, pressed) -> controllable.setWorkingEnabled(pressed)).setTooltipsSupplier(pressed -> List.of(Component.translatable(pressed ? "behaviour.soft_hammer.enabled" : "behaviour.soft_hammer.disabled"))));
        }
        if (!isGenerator()) {
            configuratorPanel.attachConfigurators(new OverclockConfigurator(this));
            configuratorPanel.attachConfigurators(new IFancyConfiguratorButton.Toggle(
                    GuiTextures.BUTTON_BATCH.getSubTexture(0, 0, 1, 0.5),
                    GuiTextures.BUTTON_BATCH.getSubTexture(0, 0.5, 1, 0.5),
                    this::isBatchEnabled, (cd, p) -> batchEnabled = p)
                    .setTooltipsSupplier(p -> List.of(Component.translatable("gtceu.machine.batch_" + (p ? "enabled" : "disabled")))));
        }
        for (Direction direction : Direction.values()) {
            if (self().getCoverContainer().hasCover(direction)) {
                IFancyConfigurator configurator = self().getCoverContainer().getCoverAtSide(direction).getConfigurator();
                if (configurator != null) configuratorPanel.attachConfigurators(configurator);
            }
        }
        ICheckPatternMachine.attachConfigurators(configuratorPanel, self());
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void addDisplayText(List<Component> textList) {
        MachineUtils.addMachineText(textList, this, t -> {});
        for (IMultiPart part : getParts()) {
            part.addMultiText(textList);
        }
    }

    @Override
    @NotNull
    public IEnergyContainer gtolib$getEnergyContainer() {
        return energyContainer;
    }

    @Override
    public void gtolib$setSpeed(double speed) {
        this.gtolib$speed = speed;
    }

    @Override
    public void gtolib$setEnergy(double energy) {
        this.gtolib$energy = energy;
    }

    @Override
    public double gtolib$getSpeed() {
        return gtolib$speed;
    }

    @Override
    public double gtolib$getEnergy() {
        return gtolib$energy;
    }

    @Override
    public boolean gtolib$canUpgraded() {
        return getDefinition() == GTMultiMachines.ELECTRIC_BLAST_FURNACE;
    }

    @Override
    public double gtolib$getPowerAmplifier() {
        return gtolib$powerAmplifier;
    }

    @Override
    public void gtolib$setPowerAmplifier(double powerAmplifier) {
        this.gtolib$powerAmplifier = powerAmplifier;
    }

    @Override
    public boolean gtolib$noPowerAmplifier() {
        return !gtolib$hasPowerAmplifier;
    }

    @Override
    public void gtolib$setHasPowerAmplifier(boolean hasPowerAmplifier) {
        this.gtolib$hasPowerAmplifier = hasPowerAmplifier;
    }
}
