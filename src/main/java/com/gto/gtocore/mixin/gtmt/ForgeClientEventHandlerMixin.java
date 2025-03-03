package com.gto.gtocore.mixin.gtmt;

import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import com.hepdd.gtmthings.client.ForgeClientEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ForgeClientEventHandler.class)
public final class ForgeClientEventHandlerMixin {

    /**
     * @author .
     * @reason .
     */
    @SubscribeEvent
    @Overwrite(remap = false)
    public static void onRenderWorldLast(RenderLevelStageEvent event) {}
}
