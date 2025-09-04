package com.gtocore.mixin.ae2.crafting;

import com.gtocore.api.ae2.crafting.ICraftingCPUCluster;
import com.gtocore.api.ae2.crafting.OptimizedCraftingCpuLogic;

import com.gtolib.api.ae2.machine.CraftingInterfacePartMachine;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import appeng.api.networking.IGridNode;
import appeng.blockentity.crafting.CraftingBlockEntity;
import appeng.blockentity.crafting.CraftingMonitorBlockEntity;
import appeng.crafting.execution.CraftingCpuLogic;
import appeng.me.cluster.implementations.CraftingCPUCluster;
import appeng.me.helpers.MachineSource;
import appeng.util.ConfigManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CraftingCPUCluster.class)
public abstract class CraftingCPUClusterMixin implements ICraftingCPUCluster {

    @Shadow(remap = false)
    private MachineSource machineSrc;

    @Shadow(remap = false)
    @Final
    private List<CraftingBlockEntity> blockEntities;

    @Shadow(remap = false)
    @Final
    private List<CraftingMonitorBlockEntity> status;

    @Shadow(remap = false)
    private long storage;

    @Shadow(remap = false)
    private int accelerator;

    @Mutable
    @Shadow(remap = false)
    @Final
    public CraftingCpuLogic craftingLogic;

    @Shadow(remap = false)
    @Final
    private ConfigManager configManager;

    @Shadow(remap = false)
    private Component myName;

    @Shadow(remap = false)
    private boolean isDestroyed;

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
    void addBlockEntity(CraftingBlockEntity te) {
        if (this.machineSrc == null || te.isCoreBlock()) {
            this.machineSrc = new MachineSource(te);
        }

        te.setCoreBlock(false);
        te.saveChanges();
        this.blockEntities.add(0, te);

        if (te instanceof CraftingMonitorBlockEntity) {
            this.status.add((CraftingMonitorBlockEntity) te);
        }
        if (te.getStorageBytes() > 0) {
            this.storage += te.getStorageBytes();
            accelerator++;
        }
        if (te.getAcceleratorThreads() > 0) {
            this.accelerator += 16;
        }
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
    @SuppressWarnings("all")
    public ConfigManager getConfigManager() {
        return configManager;
    }

    @Override
    @SuppressWarnings("all")
    public void setName(Component name) {
        this.myName = name;
    }

    @Override
    @SuppressWarnings("all")
    public void setDestroyed(boolean destroyed) {
        this.isDestroyed = destroyed;
    }

    @Override
    @SuppressWarnings("all")
    public void setStorage(long storage) {
        this.storage = storage;
    }

    @Override
    @SuppressWarnings("all")
    public void setMachineSrc(MachineSource src) {
        this.machineSrc = src;
    }

    @Override
    @SuppressWarnings("all")
    public void setAccelerator(int accelerator) {
        this.accelerator = accelerator;
    }

    @Override
    @SuppressWarnings("all")
    public void setMachine(CraftingInterfacePartMachine machine) {
        this.gtolib$machine = machine;
    }
}
