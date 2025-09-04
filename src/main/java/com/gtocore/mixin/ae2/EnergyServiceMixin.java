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
    public double injectProviderPower(double amt, Actionable mode) {
        if (amt <= 0) return amt;

        final double originalAmount = amt;

        var it = this.requesters.iterator();

        ongoingInjectOperation = true;
        try {
            while (amt > 0 && it.hasNext()) {
                final IAEPowerStorage node = it.next();
                amt = node.injectAEPower(amt, mode);

                if (amt > 0 && mode == Actionable.MODULATE) {
                    it.remove();
                }
            }
        } finally {
            ongoingInjectOperation = false;
        }

        final double overflow = Math.max(0.0, amt);

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
    public double extractProviderPower(double amt, Actionable mode) {
        if (amt <= 0) amt = 1;

        double extractedPower = 0;

        final Iterator<IAEPowerStorage> it = this.providers.iterator();

        ongoingExtractOperation = true;
        try {
            while (extractedPower < amt && it.hasNext()) {
                final IAEPowerStorage node = it.next();

                final double req = amt - extractedPower;
                final double newPower = node.extractAEPower(req, mode, PowerMultiplier.ONE);
                extractedPower += newPower;

                if (newPower < req && mode == Actionable.MODULATE) {
                    it.remove();
                }
            }
        } finally {
            ongoingExtractOperation = false;
        }

        final double result = Math.min(extractedPower, amt);

        if (mode == Actionable.MODULATE) {
            if (extractedPower > amt) {
                this.localStorage.injectAEPower(extractedPower - amt, Actionable.MODULATE);
            }

            this.globalAvailablePower -= result;
            this.tickDrainPerTick += result;
        }

        return result;
    }
}
