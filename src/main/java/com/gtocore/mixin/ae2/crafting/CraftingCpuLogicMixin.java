package com.gtocore.mixin.ae2.crafting;

import appeng.api.stacks.AEKey;
import appeng.crafting.execution.CraftingCpuLogic;
import appeng.crafting.inv.ListCraftingInventory;
import appeng.me.cluster.implementations.CraftingCPUCluster;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;
import java.util.function.Consumer;

@Mixin(CraftingCpuLogic.class)
public abstract class CraftingCpuLogicMixin {

    @Mutable
    @Shadow(remap = false)
    @Final
    private ListCraftingInventory inventory;

    @Mutable
    @Shadow(remap = false)
    @Final
    private Set<Consumer<AEKey>> listeners;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    public void gtolib$init(CraftingCPUCluster cluster, CallbackInfo ci) {
        inventory = null;
        listeners = null;
    }
}
