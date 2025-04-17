package com.gto.gtocore.mixin.gtm.machine;

import com.gto.gtocore.api.machine.feature.IUpgradeMachine;
import com.gto.gtocore.api.machine.trait.IEnhancedRecipeLogic;

import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.api.machine.WorkableTieredMachine;

import net.minecraft.nbt.CompoundTag;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SimpleTieredMachine.class)
public class SimpleTieredMachineMixin extends WorkableTieredMachine implements IUpgradeMachine {

    @Unique
    private double gtocore$speed;

    @Unique
    private double gtocore$energy;

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
        gtocore$speed = tag.getDouble("speed");
        gtocore$energy = tag.getDouble("energy");
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
}
