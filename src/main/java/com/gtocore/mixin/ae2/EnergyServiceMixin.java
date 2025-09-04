package com.gtocore.mixin.ae2;

import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.networking.energy.IAEPowerStorage;
import appeng.me.energy.GridEnergyStorage;
import appeng.me.service.EnergyService;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.SortedSet;

@Mixin(EnergyService.class)
public class EnergyServiceMixin {

    @Shadow(remap = false)
    @Final
    private SortedSet<IAEPowerStorage> providers;

    @Shadow(remap = false)
    private boolean ongoingExtractOperation;

    @Shadow(remap = false)
    @Final
    private GridEnergyStorage localStorage;

    @Shadow(remap = false)
    private double globalAvailablePower;

    @Shadow(remap = false)
    private double tickDrainPerTick;

    @Shadow(remap = false)
    @Final
    private SortedSet<IAEPowerStorage> requesters;

    @Shadow(remap = false)
    private boolean ongoingInjectOperation;

    @Shadow(remap = false)
    private double tickInjectionPerTick;

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public double injectProviderPower(double amountToInject, Actionable mode) {
        if (amountToInject <= 0) return amountToInject;

        final double originalAmount = amountToInject;
        final Iterator<IAEPowerStorage> requesterIterator = this.requesters.iterator();

        ongoingInjectOperation = true;
        try {
            while (amountToInject > 0 && requesterIterator.hasNext()) {
                final IAEPowerStorage requesterNode = requesterIterator.next();
                amountToInject = requesterNode.injectAEPower(amountToInject, mode);

                if (amountToInject > 0 && mode == Actionable.MODULATE) {
                    requesterIterator.remove();
                }
            }
        } finally {
            ongoingInjectOperation = false;
        }

        final double overflow = Math.max(0.0, amountToInject);

        if (mode == Actionable.MODULATE) {
            this.tickInjectionPerTick += originalAmount - overflow;
        }

        return overflow;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public double extractProviderPower(double amountToExtract, Actionable mode) {
        if (amountToExtract <= 0) return 0;

        double extractedPower = 0;
        final Iterator<IAEPowerStorage> providerIterator = this.providers.iterator();

        ongoingExtractOperation = true;
        try {
            while (extractedPower < amountToExtract && providerIterator.hasNext()) {
                final IAEPowerStorage providerNode = providerIterator.next();

                final double requiredAmount = amountToExtract - extractedPower;
                final double extractedFromNode = providerNode.extractAEPower(requiredAmount, mode, PowerMultiplier.ONE);
                extractedPower += extractedFromNode;

                if (extractedFromNode < requiredAmount && mode == Actionable.MODULATE) {
                    providerIterator.remove();
                }
            }
        } finally {
            ongoingExtractOperation = false;
        }

        final double result = Math.min(extractedPower, amountToExtract);

        if (mode == Actionable.MODULATE) {
            if (extractedPower > amountToExtract) {
                this.localStorage.injectAEPower(extractedPower - amountToExtract, Actionable.MODULATE);
            }

            this.globalAvailablePower -= result;
            this.tickDrainPerTick += result;
        }

        return result;
    }
}
