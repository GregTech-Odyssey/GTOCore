package com.gtocore.mixin.extrabotany;

import net.minecraft.world.level.Level;

import io.github.lounode.eventwrapper.event.level.LevelEventWrapper;
import io.github.lounode.eventwrapper.eventbus.api.SubscribeEventWrapper;
import io.github.lounode.extrabotany.common.impl.WindImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

// todo 更新后删除
@Mixin(WindImpl.EventHandler.class)
public class LevelWindMixin {

    /**
     * @author
     * @reason
     */
    @SubscribeEventWrapper
    @Overwrite(remap = false)
    public static void onLevelLoad(LevelEventWrapper.Load event) {}

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public static void onLevelUnLoad(LevelEventWrapper.Unload event) {}

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public static void onLevelTick(Level level) {}
}
