package com.gtocore.mixin.corgilib;

import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import corgitaco.corgilib.forge.client.CorgiLibForgeClientEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(CorgiLibForgeClientEvents.class)
public final class CorgiLibForgeClientEventsMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    @SubscribeEvent
    public static void screenInit(ScreenEvent.Init.Post event) {}

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {}

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    @SubscribeEvent
    public static void registerClientCommands(RegisterClientCommandsEvent event) {}
}
