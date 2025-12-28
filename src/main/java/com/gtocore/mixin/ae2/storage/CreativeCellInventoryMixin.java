package com.gtocore.mixin.ae2.storage;

import com.gtocore.common.item.devtool.CreativeAllFluidCellItem;

import com.gtolib.api.ae2.stacks.TagPrefixKey;
import com.gtolib.api.machine.feature.multiblock.IParallelMachine;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;

import net.minecraft.world.item.ItemStack;

import appeng.api.stacks.AEFluidKey;
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

    @Shadow(remap = false)
    @Final
    private ItemStack stack;

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public void getAvailableStacks(KeyCounter out) {
        if (stack.getItem() instanceof CreativeAllFluidCellItem) {
            GTCEuAPI.materialManager.getRegisteredMaterials()
                    .stream()
                    .filter(m -> m.hasProperty(PropertyKey.FLUID))
                    .map(m -> AEFluidKey.of(m.getFluid()))
                    .forEach(key -> {
                        out.add(key, IParallelMachine.MAX_PARALLEL);
                        configured.add(key);
                    });
            return;
        }
        for (AEKey key : this.configured) {
            if (key instanceof TagPrefixKey) continue;
            out.add(key, IParallelMachine.MAX_PARALLEL);
        }
    }
}
