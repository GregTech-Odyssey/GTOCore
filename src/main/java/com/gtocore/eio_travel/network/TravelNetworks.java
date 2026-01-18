package com.gtocore.eio_travel.network;

import com.gtocore.eio_travel.ITravelTarget;
import com.gtocore.eio_travel.api.TravelHandler;
import com.gtocore.eio_travel.api.TravelRegistry;
import com.gtocore.eio_travel.api.TravelSavedData;

import com.gtolib.api.network.NetworkPack;
import com.gtolib.utils.ServerUtils;

import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.ServerLifecycleHooks;

import com.google.common.base.Predicates;

import java.util.Optional;

public class TravelNetworks {

    private static final NetworkPack ADD_TRAVEL = NetworkPack.registerS2C(
            "eio_travel:add_travel",
            (player, buf) -> TravelRegistry.deserialize(buf.readNbt()).ifPresent(TravelNetworks::handleAddTravel));

    private static final NetworkPack SYNC_TRAVEL = NetworkPack.registerS2C(
            "eio_travel:sync_travel",
            (player, buf) -> {
                CompoundTag nbt = buf.readNbt();
                TravelSavedData travelData = TravelSavedData.getTravelData(GTUtil.getClientLevel());
                travelData.loadNBT(nbt);
            });

    private static final NetworkPack REMOVE_TRAVEL = NetworkPack.registerS2C(
            "eio_travel:remove_travel",
            (player, buf) -> {
                TravelSavedData travelData = TravelSavedData.getTravelData(GTUtil.getClientLevel());
                travelData.removeTravelTargetAt(GTUtil.getClientLevel(), buf.readBlockPos());
            });

    private static final NetworkPack REQUEST_TRAVEL = NetworkPack.registerC2S(
            "eio_travel:request_travel",
            TravelNetworks::handleRequest);

    public static void onServerTravelAdded(ITravelTarget target) {
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            ADD_TRAVEL.send(b -> b.writeNbt(target.save()), ServerUtils.getServer());
        }
    }

    public static void syncTravelData(CompoundTag nbt, ServerPlayer player) {
        SYNC_TRAVEL.send(b -> b.writeNbt(nbt), player);
    }

    public static void syncTravelData(CompoundTag nbt, ServerLevel level) {
        SYNC_TRAVEL.send(b -> b.writeNbt(nbt), level.getPlayers(Predicates.alwaysTrue()));
    }

    public static void onServerTravelRemoved(BlockPos pos) {
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            REMOVE_TRAVEL.send(b -> b.writeBlockPos(pos), ServerUtils.getServer());
        }
    }

    public static void requestTravel(BlockPos pos) {
        REQUEST_TRAVEL.send(b -> b.writeBlockPos(pos));
    }

    private static void handleAddTravel(ITravelTarget target) {
        TravelSavedData travelData = TravelSavedData.getTravelData(GTUtil.getClientLevel());
        travelData.addTravelTarget(GTUtil.getClientLevel(), target);
    }

    private static void handleRequest(ServerPlayer player, FriendlyByteBuf buf) {
        var pos = buf.readBlockPos();

        if (player == null) {
            return;
        }

        TravelSavedData travelData = TravelSavedData.getTravelData(player.level());
        Optional<ITravelTarget> target = travelData.getTravelTarget(pos);

        // These errors should only ever be triggered if there's some form of desync
        if (!TravelHandler.canBlockTeleport(player)) {
            player.displayClientMessage(Component.nullToEmpty("ERROR: Cannot teleport"), true);
            return;
        }
        if (target.isEmpty()) {
            player.displayClientMessage(Component.nullToEmpty("ERROR: Destination not a valid target"), true);
            return;
        }
        // Eventually change the packet structure to include what teleport method was used so this range can be selected
        // correctly
        int range = Math.max(target.get().getBlock2BlockRange(), target.get().getItem2BlockRange());
        if (pos.distSqr(player.getOnPos()) > range * range) {
            player.displayClientMessage(Component.nullToEmpty("ERROR: Too far"), true);
            return;
        }

        TravelHandler.blockTeleportAround(player.level(), player, target.get().getPos(), false);
    }
}
