package com.gtocore.mixin.ae2.crafting;

import com.gtocore.api.ae2.crafting.OptimizedCraftingCpuLogic;

import com.gtolib.api.ae2.crafting.ICraftingCPUCluster;
import com.gtolib.api.machine.impl.part.CraftingInterfacePartMachine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import appeng.api.networking.IGridNode;
import appeng.blockentity.crafting.CraftingBlockEntity;
import appeng.crafting.execution.CraftingCpuLogic;
import appeng.me.cluster.implementations.CraftingCPUCluster;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingCPUCluster.class)
public abstract class CraftingCPUClusterMixin implements ICraftingCPUCluster {

    @Mutable
    @Shadow(remap = false)
    @Final
    public CraftingCpuLogic craftingLogic;

    @Shadow(remap = false)
    protected abstract CraftingBlockEntity getCore();

    @Unique
    private CraftingInterfacePartMachine gtolib$machine;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void init(BlockPos boundsMin, BlockPos boundsMax, CallbackInfo ci) {
        craftingLogic = new OptimizedCraftingCpuLogic((CraftingCPUCluster) (Object) this);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void markDirty() {
        if (gtolib$machine == null) {
            this.getCore().saveChanges();
        } else {
            gtolib$machine.onChanged();
        }
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public @Nullable IGridNode getNode() {
        if (gtolib$machine == null) {
            CraftingBlockEntity core = getCore();
            return core != null ? core.getActionableNode() : null;
        } else {
            return gtolib$machine.getActionableNode();
        }
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public Level getLevel() {
        if (gtolib$machine == null) {
            return this.getCore().getLevel();
        } else {
            return gtolib$machine.getLevel();
        }
    }

    @Override
    public void setMachine(CraftingInterfacePartMachine machine) {
        this.gtolib$machine = machine;
    }
}
