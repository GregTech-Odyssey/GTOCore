package com.gtocore.mixin.ae2.storage;

import com.gtolib.api.machine.feature.multiblock.IParallelMachine;

import com.gregtechceu.gtceu.utils.collection.OpenCacheHashSet;

import net.minecraft.world.item.ItemStack;

import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(targets = "appeng.me.cells.CreativeCellInventory")
public abstract class CreativeCellInventoryMixin {

    @Mutable
    @Final
    @Shadow(remap = false)
    private Set<AEKey> configured;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void init(ItemStack o, CallbackInfo ci) {
        configured = new OpenCacheHashSet<>();
    }

    @Inject(at = @At("HEAD"), method = "getAvailableStacks", remap = false, cancellable = true)
    private void getAvailableStacks(KeyCounter out, CallbackInfo ci) {
        for (AEKey key : this.configured) {
            out.add(key, IParallelMachine.MAX_PARALLEL);
        }
        ci.cancel();
    }
}
