package com.gtocore.mixin.gtm.machine;

import com.gtocore.common.machine.multiblock.part.ProgrammableHatchPartMachine;

import com.gtolib.api.capability.IWirelessChargerInteraction;
import com.gtolib.api.machine.feature.IPowerAmplifierMachine;
import com.gtolib.api.machine.feature.IUpgradeMachine;
import com.gtolib.api.machine.multiblock.WirelessChargerMachine;
import com.gtolib.api.machine.trait.IEnhancedRecipeLogic;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyTooltip;
import com.gregtechceu.gtceu.api.gui.fancy.TooltipsPanel;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.WorkableTieredMachine;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;

import net.minecraft.nbt.CompoundTag;

import com.hepdd.gtmthings.data.CustomItems;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.UUID;

@Mixin(SimpleTieredMachine.class)
public abstract class SimpleTieredMachineMixin extends WorkableTieredMachine implements IUpgradeMachine, IPowerAmplifierMachine, IFancyUIMachine, IWirelessChargerInteraction {

    @Unique
    private double gtolib$speed;
    @Unique
    private double gtolib$energy;
    @Unique
    private double gtolib$powerAmplifier;
    @Unique
    private boolean gtolib$hasPowerAmplifier;
    @SuppressWarnings("all")
    private WirelessChargerMachine netMachineCache;
    @Unique
    private TickableSubscription gtolib$tickSubs;

    protected SimpleTieredMachineMixin(MetaMachineBlockEntity holder, int tier, Int2IntFunction tankScalingFunction, Object... args) {
        super(holder, tier, tankScalingFunction, args);
    }

    @Inject(method = "createCircuitItemHandler", at = @At("HEAD"), remap = false, cancellable = true)
    protected void createCircuitItemHandler(Object[] args, CallbackInfoReturnable<NotifiableItemStackHandler> cir) {
        cir.setReturnValue(new ProgrammableHatchPartMachine.ProgrammableCircuitHandler(this));
    }

    @Override
    protected @NotNull NotifiableItemStackHandler createImportItemHandler(Object @NotNull... args) {
        return new NotifiableItemStackHandler(this, getRecipeType().getMaxInputs(ItemRecipeCapability.CAP), IO.IN).setFilter(i -> !(i.hasTag() && i.is(CustomItems.VIRTUAL_ITEM_PROVIDER.get())));
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void init(CallbackInfo ci) {
        gtolib$speed = 1;
        gtolib$energy = 1;
        gtolib$powerAmplifier = 1;
    }

    @Inject(method = "onLoad", at = @At("TAIL"), remap = false)
    private void onLoad(CallbackInfo ci) {
        if (!isRemote()) {
            gtolib$tickSubs = subscribeServerTick(gtolib$tickSubs, this::charge);
        }
    }

    @Inject(method = "onUnload", at = @At("TAIL"), remap = false)
    private void onUnload(CallbackInfo ci) {
        if (gtolib$tickSubs != null) {
            gtolib$tickSubs.unsubscribe();
            gtolib$tickSubs = null;
        }
        removeNetMachineCache();
    }

    @Override
    public void saveCustomPersistedData(@NotNull CompoundTag tag, boolean forDrop) {
        super.saveCustomPersistedData(tag, forDrop);
        tag.putDouble("speed", gtolib$speed);
        tag.putDouble("energy", gtolib$energy);
    }

    @Override
    public void loadCustomPersistedData(@NotNull CompoundTag tag) {
        super.loadCustomPersistedData(tag);
        double speed = tag.getDouble("speed");
        if (speed != 0) {
            gtolib$speed = speed;
        }
        double energy = tag.getDouble("energy");
        if (energy != 0) {
            gtolib$energy = energy;
        }
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
    public boolean gtolib$canUpgraded() {
        return true;
    }

    @Override
    public void attachTooltips(TooltipsPanel tooltipsPanel) {
        tooltipsPanel.attachTooltips(this);
        getTraits().stream().filter(IFancyTooltip.class::isInstance).map(IFancyTooltip.class::cast).forEach(tooltipsPanel::attachTooltips);
        if (getRecipeLogic() instanceof IEnhancedRecipeLogic enhancedRecipeLogic) {
            tooltipsPanel.attachTooltips(new Basic(() -> GuiTextures.INDICATOR_NO_STEAM.get(true), () -> List.of(enhancedRecipeLogic.gtolib$getIdleReason()), () -> getRecipeLogic().isIdle() && enhancedRecipeLogic.gtolib$getIdleReason() != null, () -> null));
        }
    }

    @Override
    @Nullable
    public UUID getUUID() {
        return getOwnerUUID();
    }

    @SuppressWarnings("all")
    public void setNetMachineCache(final WirelessChargerMachine netMachineCache) {
        this.netMachineCache = netMachineCache;
    }

    @SuppressWarnings("all")
    public WirelessChargerMachine getNetMachineCache() {
        return this.netMachineCache;
    }
}
