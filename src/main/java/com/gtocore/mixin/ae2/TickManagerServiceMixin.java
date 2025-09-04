package com.gtocore.mixin.ae2;

import net.minecraft.world.level.Level;

import appeng.api.networking.IGridNode;
import appeng.me.service.TickManagerService;
import appeng.me.service.helpers.TickTracker;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.PriorityQueue;

@Mixin(TickManagerService.class)
public class TickManagerServiceMixin {

    @Mutable
    @Shadow(remap = false)
    @Final
    private Map<IGridNode, TickTracker> alertable;

    @Mutable
    @Shadow(remap = false)
    @Final
    private Map<IGridNode, TickTracker> sleeping;

    @Mutable
    @Shadow(remap = false)
    @Final
    private Map<IGridNode, TickTracker> awake;

    @Mutable
    @Shadow(remap = false)
    @Final
    private Map<Level, PriorityQueue<TickTracker>> upcomingTicks;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void init(CallbackInfo ci) {
        alertable = new Reference2ReferenceOpenHashMap<>();
        sleeping = new Reference2ReferenceOpenHashMap<>();
        awake = new Reference2ReferenceOpenHashMap<>();
        upcomingTicks = new Reference2ReferenceOpenHashMap<>();
    }
}
