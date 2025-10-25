package com.gtocore.mixin.extrabotany;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import io.github.lounode.extrabotany.common.impl.WindImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

// todo 更新后删除
@Mixin(WindImpl.class)
public class WindImplMixin {

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public double getWindLevel(Level level, Vec3 position) {
        return 10;
    }
}
