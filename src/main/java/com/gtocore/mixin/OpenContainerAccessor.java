package com.gtocore.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PlayMessages;

import org.spongepowered.asm.mixin.Overwrite;

import java.util.function.Supplier;

@org.spongepowered.asm.mixin.Mixin(net.minecraftforge.network.PlayMessages.OpenContainer.class)
public class OpenContainerAccessor {

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public static void handle(PlayMessages.OpenContainer msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Exception e = null;
            try {
                MenuScreens.getScreenFactory(msg.getType(), Minecraft.getInstance(), msg.getWindowId(), msg.getName()).ifPresent(f -> {
                    var c = msg.getType().create(msg.getWindowId(), Minecraft.getInstance().player.getInventory(), msg.getAdditionalData());
                    Screen s = ((MenuScreens.ScreenConstructor<AbstractContainerMenu, ?>) f).create(c, Minecraft.getInstance().player.getInventory(), msg.getName());
                    Minecraft.getInstance().player.containerMenu = ((MenuAccess) s).getMenu();
                    Minecraft.getInstance().setScreen(s);
                });
            } catch (Exception ex) {
                e = ex;
                ex.printStackTrace();
            } finally {
                msg.getAdditionalData().release();
            }
            if (e != null) {
                throw new RuntimeException(e);
            }

        });
        ctx.get().setPacketHandled(true);
    }
}
