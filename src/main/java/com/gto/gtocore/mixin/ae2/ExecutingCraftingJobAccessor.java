package com.gto.gtocore.mixin.ae2;

import appeng.crafting.CraftingLink;
import appeng.crafting.execution.ExecutingCraftingJob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ExecutingCraftingJob.class)
public interface ExecutingCraftingJobAccessor {

    @Accessor(value = "link", remap = false)
    CraftingLink getLink();
}
