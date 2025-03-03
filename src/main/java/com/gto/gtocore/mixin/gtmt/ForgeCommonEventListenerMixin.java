package com.gto.gtocore.mixin.gtmt;

import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import com.hepdd.gtmthings.forge.ForgeCommonEventListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ForgeCommonEventListener.class)
public final class ForgeCommonEventListenerMixin {

    /**
     * @author .
     * @reason .
     */
    @SubscribeEvent
    @Overwrite(remap = false)
    public static void serverSetup(LevelEvent.Load event) {}
}
