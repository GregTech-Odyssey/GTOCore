package com.gto.gtocore.common.network;

import com.gto.gtocore.common.data.GTONet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import org.jetbrains.annotations.Nullable;

public class FromServerMessage extends BaseS2CMessage {

    private final String channel;
    private final CompoundTag data;

    FromServerMessage(String c, @Nullable CompoundTag d) {
        channel = c;
        data = d;
    }

    public FromServerMessage(FriendlyByteBuf buf) {
        channel = buf.readUtf(120);
        data = buf.readNbt();
    }

    @Override
    public MessageType getType() {
        return GTONet.SEND_DATA_FROM_SERVER;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(channel, 120);
        buf.writeNbt(data);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        if (!channel.isEmpty()) {
            ServerMessage.handle(channel, context.getPlayer(), data);
        }
    }
}
