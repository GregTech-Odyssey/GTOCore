package com.gtocore.mixin.ae2;

import appeng.api.networking.IGridNode;
import appeng.me.Grid;
import com.google.common.collect.SetMultimap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Grid.class)
public interface GridAccessor {

    @Accessor(value = "machines", remap = false)
    SetMultimap<Class<?>, IGridNode> getMachines();
}
