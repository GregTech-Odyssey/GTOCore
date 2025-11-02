package com.gtocore.client;

import com.gtolib.api.network.NetworkPack;
import com.gtolib.utils.GTOUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import java.util.function.BiConsumer;

public final class Message {

    public static BiConsumer<Player, FriendlyByteBuf> PICK_CRAFT_TOAST_READ = GTOUtils.NOOP_BI_CONSUMER;

    public static final NetworkPack PICK_CRAFT_TOAST = NetworkPack.registerS2C(11, (p, b) -> PICK_CRAFT_TOAST_READ.accept(p, b));
}
