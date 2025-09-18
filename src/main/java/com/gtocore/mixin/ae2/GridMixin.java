package com.gtocore.mixin.ae2;

import com.gtocore.integration.jade.provider.AEGridProvider;

import com.gtolib.api.ae2.IExpandedGrid;

import appeng.api.networking.IGridNode;
import appeng.hooks.ticking.TickHandler;
import appeng.me.Grid;
import com.google.common.collect.SetMultimap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Grid.class)
public abstract class GridMixin implements IExpandedGrid {

    @Unique
    private long gtocore$averageTickTime;

    @Unique
    private long gtocore$totaTickCount;
    @Unique
    private long gtocore$startTime;
    @Unique
    private boolean gtocore$observe;

    @Shadow(remap = false)
    @Final
    private SetMultimap<Class<?>, IGridNode> machines;

    @Override
    public SetMultimap<Class<?>, IGridNode> getMachines() {
        return machines;
    }

    @Override
    public long getLatency() {
        return gtocore$averageTickTime;
    }

    @Override
    public void observe() {
        gtocore$observe = true;
    }

    @Inject(method = "onServerStartTick", at = @At(value = "INVOKE", target = "Lappeng/me/helpers/GridServiceContainer;serverStartTickServices()[Lappeng/api/networking/IGridServiceProvider;"), remap = false)
    private void onServerStartTick(CallbackInfo ci) {
        if (gtocore$observe || AEGridProvider.OBSERVE) {
            gtocore$startTime = System.nanoTime();
        }
    }

    @Inject(method = "onServerStartTick", at = @At("TAIL"), remap = false)
    private void onServerStartTick2(CallbackInfo ci) {
        if (gtocore$observe || AEGridProvider.OBSERVE) {
            gtocore$totaTickCount += System.nanoTime() - gtocore$startTime;
        }
    }

    @Inject(method = "onServerEndTick", at = @At(value = "INVOKE", target = "Lappeng/me/helpers/GridServiceContainer;serverEndTickServices()[Lappeng/api/networking/IGridServiceProvider;"), remap = false)
    private void onServerEndTick(CallbackInfo ci) {
        if (gtocore$observe || AEGridProvider.OBSERVE) {
            gtocore$startTime = System.nanoTime();
        }
    }

    @Inject(method = "onServerEndTick", at = @At("TAIL"), remap = false)
    private void onServerEndTick2(CallbackInfo ci) {
        if (gtocore$observe || AEGridProvider.OBSERVE) {
            gtocore$totaTickCount += System.nanoTime() - gtocore$startTime;
            if ((TickHandler.instance().getCurrentTick() & 127) == 0) {
                this.gtocore$observe = false;
                gtocore$averageTickTime = gtocore$totaTickCount / 128000;
                gtocore$totaTickCount = 0;
            }
            if (AEGridProvider.OBSERVE) IExpandedGrid.PERFORMANCE_MAP.put(this, gtocore$averageTickTime);
        }
    }

    @Inject(method = "onLevelStartTick", at = @At(value = "INVOKE", target = "Lappeng/me/helpers/GridServiceContainer;levelStartTickServices()[Lappeng/api/networking/IGridServiceProvider;"), remap = false)
    private void onLevelStartTick(CallbackInfo ci) {
        if (gtocore$observe || AEGridProvider.OBSERVE) {
            gtocore$startTime = System.nanoTime();
        }
    }

    @Inject(method = "onLevelStartTick", at = @At("TAIL"), remap = false)
    private void onLevelStartTick2(CallbackInfo ci) {
        if (gtocore$observe || AEGridProvider.OBSERVE) {
            gtocore$totaTickCount += System.nanoTime() - gtocore$startTime;
        }
    }
}
