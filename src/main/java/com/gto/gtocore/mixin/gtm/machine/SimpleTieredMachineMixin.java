package com.gto.gtocore.mixin.gtm.machine;

import com.gto.gtocore.api.machine.feature.IPowerAmplifierMachine;
import com.gto.gtocore.api.machine.feature.IUpgradeMachine;
import com.gto.gtocore.api.machine.trait.IEnhancedRecipeLogic;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyTooltip;
import com.gregtechceu.gtceu.api.gui.fancy.TooltipsPanel;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.api.machine.WorkableTieredMachine;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;

import net.minecraft.nbt.CompoundTag;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SimpleTieredMachine.class)
public class SimpleTieredMachineMixin extends WorkableTieredMachine implements IUpgradeMachine, IPowerAmplifierMachine, IFancyUIMachine {

    @Unique
    private double gtocore$speed = 1;

    @Unique
    private double gtocore$energy = 1;

    @Unique
    private double gtocore$powerAmplifier = 1;

    @Unique
    private boolean gtocore$hasPowerAmplifier;

    public SimpleTieredMachineMixin(IMachineBlockEntity holder, int tier, Int2IntFunction tankScalingFunction, Object... args) {
        super(holder, tier, tankScalingFunction, args);
    }

    @Override
    public boolean alwaysTryModifyRecipe() {
        return false;
    }

    @Inject(method = "attachConfigurators", at = @At(value = "TAIL"), remap = false)
    private void attachConfigurators(ConfiguratorPanel configuratorPanel, CallbackInfo ci) {
        IEnhancedRecipeLogic.attachRecipeLockable(configuratorPanel, getRecipeLogic());
    }

    @Override
    public void saveCustomPersistedData(@NotNull CompoundTag tag, boolean forDrop) {
        super.saveCustomPersistedData(tag, forDrop);
        tag.putDouble("speed", gtocore$speed);
        tag.putDouble("energy", gtocore$energy);
    }

    @Override
    public void loadCustomPersistedData(@NotNull CompoundTag tag) {
        super.loadCustomPersistedData(tag);
        double speed = tag.getDouble("speed");
        if (speed != 0) {
            gtocore$speed = speed;
        }
        double energy = tag.getDouble("energy");
        if (energy != 0) {
            gtocore$energy = energy;
        }
    }

    @Override
    public void gtocore$setSpeed(double speed) {
        this.gtocore$speed = speed;
        getRecipeLogic().markLastRecipeDirty();
    }

    @Override
    public void gtocore$setEnergy(double energy) {
        this.gtocore$energy = energy;
        getRecipeLogic().markLastRecipeDirty();
    }

    @Override
    public double gtocore$getSpeed() {
        return gtocore$speed;
    }

    @Override
    public double gtocore$getEnergy() {
        return gtocore$energy;
    }

    @Override
    public double gtocore$getPowerAmplifier() {
        return gtocore$powerAmplifier;
    }

    @Override
    public void gtocore$setPowerAmplifier(double powerAmplifier) {
        this.gtocore$powerAmplifier = powerAmplifier;
    }

    @Override
    public boolean gtocore$noPowerAmplifier() {
        return !gtocore$hasPowerAmplifier;
    }

    @Override
    public void gtocore$setHasPowerAmplifier(boolean hasPowerAmplifier) {
        this.gtocore$hasPowerAmplifier = hasPowerAmplifier;
    }

    @Override
    public void attachTooltips(TooltipsPanel tooltipsPanel) {
        tooltipsPanel.attachTooltips(this);
        getTraits().stream().filter(IFancyTooltip.class::isInstance).map(IFancyTooltip.class::cast).forEach(tooltipsPanel::attachTooltips);
        if (getRecipeLogic() instanceof IEnhancedRecipeLogic enhancedRecipeLogic) {
            tooltipsPanel.attachTooltips(new IFancyTooltip.Basic(() -> GuiTextures.INDICATOR_NO_STEAM.get(true), () -> List.of(enhancedRecipeLogic.gTOCore$getIdleReason()), () -> getRecipeLogic().isIdle() && enhancedRecipeLogic.gTOCore$getIdleReason() != null, () -> null));
        }
    }
}
