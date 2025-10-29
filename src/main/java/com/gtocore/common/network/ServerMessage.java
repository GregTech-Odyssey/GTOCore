package com.gtocore.common.network;

import com.gtocore.client.forge.ForgeClientEvent;
import com.gtocore.integration.ae.hooks.IPushResultsHandler;

import com.gtolib.api.network.NetworkPack;
import com.gtolib.utils.ServerUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import appeng.api.stacks.AEKey;
import appeng.client.gui.me.common.ContentToast;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

// todo 重构
@Deprecated
public final class ServerMessage {

    private static final Int2ObjectOpenHashMap<NetworkPack> PACKS = new Int2ObjectOpenHashMap<>();

    public static void sendData(MinecraftServer server, @Nullable ServerPlayer player, String channel, @Nullable CompoundTag data) {
        send(server, player, channel, buf -> buf.writeNbt(data));
    }

    public static void send(MinecraftServer server, @Nullable ServerPlayer player, String channel, @NotNull Consumer<FriendlyByteBuf> consumer) {
        var pack = PACKS.computeIfAbsent(channel.hashCode(), k -> NetworkPack.registerS2C(k, (p, b) -> handle(channel, p, b)));
        if (player != null) {
            pack.send(consumer, player);
        } else {
            pack.send(consumer, server);
        }
    }

    public static void highlightRegion(ResourceKey<Level> dimension, BlockPos start, BlockPos end, int color, int durationTicks) {
        send(ServerUtils.getServer(), null, "highlightRegion", buf -> {
            buf.writeResourceKey(dimension);
            buf.writeBlockPos(start);
            buf.writeBlockPos(end);
            buf.writeInt(color);
            buf.writeInt(durationTicks);
        });
    }

    public static void stopHighlight(BlockPos start, BlockPos end) {
        send(ServerUtils.getServer(), null, "stopHighlight", buf -> {
            buf.writeBlockPos(start);
            buf.writeBlockPos(end);
        });
    }

    public static void disableDrift(ServerPlayer serverPlayer, boolean drift) {
        send(serverPlayer.server, serverPlayer, "disableDrift", buf -> buf.writeBoolean(drift));
    }

    private static void handle(String channel, @Nullable Player player, FriendlyByteBuf data) {
        if (player == null) return;
        switch (channel) {
            case "craftMenuPushResults" -> {
                if (player.containerMenu.containerId == data.readInt() && player.containerMenu instanceof IPushResultsHandler handler) {
                    handler.gtocore$syncCraftingResults(data);
                }
            }
            case "highlightRegion" -> {
                var dimension = data.readResourceKey(Registries.DIMENSION);
                var start = data.readBlockPos();
                var end = data.readBlockPos();
                var color = data.readInt();
                var durationTicks = data.readInt();
                ForgeClientEvent.highlightRegion(dimension, start, end, color, durationTicks);
            }
            case "stopHighlight" -> {
                var start = data.readBlockPos();
                var end = data.readBlockPos();
                ForgeClientEvent.stopHighlight(start, end);
            }
            case "pickCraftToast" -> {
                AEKey aeKey = AEKey.readKey(data);
                int stateCode = data.readInt();
                Minecraft.getInstance().getToasts().addToast(new ContentToast(aeKey) {

                    @Override
                    protected Component getTitle() {
                        if (stateCode == 0) {
                            return Component.translatable("gtocore.ae.appeng.pick_craft.all_right.title");
                        }
                        return Component.translatable("gtocore.ae.appeng.pick_craft.error.title");
                    }

                    @Override
                    protected void addInfoLines(List<FormattedCharSequence> lines) {
                        var text = stateCode == 0 ?
                                Component.translatable("gtocore.ae.appeng.pick_craft.all_right") :
                                Component.translatable("gtocore.ae.appeng.pick_craft.error." + stateCode);
                        lines.addAll(Minecraft.getInstance().font.split(text, width() - 30 - 5));
                    }
                });
            }
        }
    }
}
