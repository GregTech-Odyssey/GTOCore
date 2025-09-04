package com.gtocore.mixin.gtm.machine;

import com.gtolib.api.capability.IWirelessChargerInteraction;
import com.gtolib.api.machine.feature.IElectricMachine;
import com.gtolib.api.machine.multiblock.WirelessChargerMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredIOPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.common.machine.multiblock.part.EnergyHatchPartMachine;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(EnergyHatchPartMachine.class)
public class EnergyHatchPartMachineMixin extends TieredIOPartMachine implements IWirelessChargerInteraction, IElectricMachine {

    @Shadow(remap = false)
    @Final
    public NotifiableEnergyContainer energyContainer;
    @SuppressWarnings("all")
    private WirelessChargerMachine netMachineCache;
    @Unique
    private TickableSubscription gtolib$tickSubs;

    public EnergyHatchPartMachineMixin(MetaMachineBlockEntity holder, int tier, IO io) {
        super(holder, tier, io);
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
    @NotNull
    public IEnergyContainer gtolib$getEnergyContainer() {
        return energyContainer;
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
