package com.gtocore.mixin.ae2.storage;

import com.gtolib.api.machine.feature.multiblock.IParallelMachine;

import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import org.spongepowered.asm.mixin.*;

import java.util.Set;

@Mixin(targets = "appeng.me.cells.CreativeCellInventory")
public abstract class CreativeCellInventoryMixin {

    @Mutable
    @Final
    @Shadow(remap = false)
    private Set<AEKey> configured;

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public void getAvailableStacks(KeyCounter out) {
        for (AEKey key : this.configured) {
            out.add(key, IParallelMachine.MAX_PARALLEL);
        }
    }
}
