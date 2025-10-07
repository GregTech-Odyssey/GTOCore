package com.gtocore.mixin.avaritia;

import committee.nova.mods.avaritia.common.item.singularity.SingularityItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Timer;

// todo 模组更后都删除
@Mixin(SingularityItem.class)
public class SingularityItemMixin {

    @Shadow(remap = false)
    @Final
    private static Timer singularityIconTimer;

    static {
        singularityIconTimer.cancel();
    }
}
