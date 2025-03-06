package com.gto.gtocore.common.network;

import com.gto.gtocore.common.data.GTONet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import org.jetbrains.annotations.Nullable;

public class FromClientMessage extends BaseC2SMessage {

    private final String channel;
    private final CompoundTag data;

    FromClientMessage(String c, @Nullable CompoundTag d) {
        channel = c;
        data = d;
    }

    public FromClientMessage(FriendlyByteBuf buf) {
        channel = buf.readUtf(120);
        data = buf.readNbt();
    }

    @Override
    public MessageType getType() {
        return GTONet.SEND_DATA_FROM_CLIENT;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(channel, 120);
        buf.writeNbt(data);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        if (!channel.isEmpty() && context.getPlayer() instanceof ServerPlayer serverPlayer) {
            ClientMessage.handle(channel, serverPlayer, data);
        }
    }
}
