package com.gto.gtocore.mixin.kubejs;

import com.gto.gtocore.common.network.ServerMessage;

import net.minecraft.nbt.CompoundTag;

import dev.architectury.networking.NetworkManager;
import dev.latvian.mods.kubejs.net.SendDataFromServerMessage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SendDataFromServerMessage.class)
public class SendDataFromServerMessageMixin {

    @Shadow(remap = false)
    @Final
    private String channel;

    @Shadow(remap = false)
    @Final
    private CompoundTag data;

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void handle(NetworkManager.PacketContext context) {
        if (!channel.isEmpty()) {
            ServerMessage.handle(channel, context.getPlayer(), data);
        }
    }
}
