package com.gtocore.mixin.gtm.machine;

import com.gtolib.api.gui.OverclockConfigurator;
import com.gtolib.api.machine.feature.IElectricMachine;
import com.gtolib.api.machine.feature.IOverclockConfigMachine;
import com.gtolib.api.machine.feature.IPowerAmplifierMachine;
import com.gtolib.api.machine.feature.IUpgradeMachine;
import com.gtolib.api.machine.feature.multiblock.ICheckPatternMachine;
import com.gtolib.api.machine.feature.multiblock.IMultiblockTraitHolder;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDisplayUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.misc.EnergyContainerList;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
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
public abstract class WorkableElectricMultiblockMachineMixin extends WorkableMultiblockMachine implements IDisplayUIMachine, IOverclockConfigMachine, IUpgradeMachine, IPowerAmplifierMachine, IElectricMachine {

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

    @Persisted
    private VoidingMode voidingMode = VoidingMode.VOID_NONE;

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

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        configuratorPanel.attachConfigurators(new IFancyConfiguratorButton.Toggle(GuiTextures.BUTTON_POWER.getSubTexture(0, 0, 1, 0.5), GuiTextures.BUTTON_POWER.getSubTexture(0, 0.5, 1, 0.5), this::isWorkingEnabled, (clickData, pressed) -> this.setWorkingEnabled(pressed)).setTooltipsSupplier(pressed -> List.of(Component.translatable(pressed ? "behaviour.soft_hammer.enabled" : "behaviour.soft_hammer.disabled"))));
        if (!isGenerator()) {
            if (hasOverclockConfig()) configuratorPanel.attachConfigurators(new OverclockConfigurator(this));
            if (!(this instanceof IMultiblockTraitHolder holder && !holder.hasBatchConfig())) configuratorPanel.attachConfigurators(new IFancyConfiguratorButton.Toggle(
                    GuiTextures.BUTTON_BATCH.getSubTexture(0, 0, 1, 0.5),
                    GuiTextures.BUTTON_BATCH.getSubTexture(0, 0.5, 1, 0.5),
                    this::isBatchEnabled, (cd, p) -> batchEnabled = p)
                    .setTooltipsSupplier(p -> List.of(Component.translatable("gtceu.machine.batch_" + (p ? "enabled" : "disabled")))));
        }
        ICheckPatternMachine.attachConfigurators(configuratorPanel, self());
        for (var direction : Direction.values()) {
            if (getCoverContainer().hasCover(direction)) {
                var configurator = getCoverContainer().getCoverAtSide(direction).getConfigurator();
                if (configurator != null)
                    configuratorPanel.attachConfigurators(configurator);
            }
        }
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void addDisplayText(List<Component> textList) {
        MachineUtils.addMachineText(textList, this, t -> textList.add(Component.translatable("gtceu.gui.multiblock_no_voiding.0").append(": ")
                .append(ComponentPanelWidget.withButton(Component.translatable(voidingMode.ordinal() == 0 ? "gtceu.gui.multiblock_no_voiding.1" : voidingMode.getSerializedName()), "voidingMode"))));
        for (IMultiPart part : getParts()) {
            part.addMultiText(textList);
        }
    }

    @Override
    public void handleDisplayClick(String componentData, ClickData clickData) {
        if (!clickData.isRemote && componentData.equals("voidingMode")) {
            if (voidingMode.ordinal() + 1 < VoidingMode.VALUES.length) {
                voidingMode = VoidingMode.VALUES[voidingMode.ordinal() + 1];
            } else voidingMode = VoidingMode.VALUES[0];
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
        return false;
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

    @Override
    public void setVoidingMode(VoidingMode mode) {
        this.voidingMode = mode;
    }

    @Override
    public VoidingMode getVoidingMode() {
        return this.voidingMode;
    }
}
