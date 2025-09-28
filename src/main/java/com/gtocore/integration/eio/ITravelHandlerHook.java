package com.gtocore.integration.eio;

import com.gtocore.common.machine.multiblock.part.ae.MEPatternPartMachineKt;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import appeng.helpers.patternprovider.PatternProviderLogicHost;
import com.enderio.api.travel.ITravelTarget;
import com.enderio.base.common.handler.TravelHandler;
import com.enderio.base.common.network.SyncTravelDataPacket;
import com.enderio.base.common.travel.TravelSavedData;
import com.enderio.core.common.network.CoreNetwork;
import com.enderio.machines.common.travel.AnchorTravelTarget;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface ITravelHandlerHook {

    static Optional<Double> gto$isTeleportPositionAndSurroundingClear(BlockGetter level, BlockPos target) {
        var result = TravelHandler.isTeleportPositionClear(level, target);
        if (result.isPresent()) return result;
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos offsetPos = target.relative(dir).below();
            result = TravelHandler.isTeleportPositionClear(level, offsetPos);
            if (result.isPresent()) {
                return result;
            }
        }
        return Optional.empty();
    }

    static void removeAndReadd(@NotNull Level level, PatternProviderLogicHost host) {
        Optional<ITravelTarget> travelTarget = TravelSavedData.getTravelData(level).getTravelTarget(host.getBlockEntity().getBlockPos());
        if (travelTarget.isPresent() && travelTarget.get() instanceof AnchorTravelTarget anchorTravelTarget) {
            TravelSavedData.getTravelData(level).removeTravelTargetAt(level, anchorTravelTarget.getPos());
        }
        AnchorTravelTarget anchorTravelTarget = new PatternTravelTarget(host);
        TravelSavedData.getTravelData(level).addTravelTarget(level, anchorTravelTarget);
        requireResync(level);
    }

    static void removeAndReadd(@NotNull Level level, MEPatternPartMachineKt<?> host) {
        Optional<ITravelTarget> travelTarget = TravelSavedData.getTravelData(level).getTravelTarget(host.getHolder().getBlockPos());
        if (travelTarget.isPresent() && travelTarget.get() instanceof AnchorTravelTarget anchorTravelTarget) {
            TravelSavedData.getTravelData(level).removeTravelTargetAt(level, anchorTravelTarget.getPos());
        }
        AnchorTravelTarget anchorTravelTarget = new PatternTravelTarget(host);
        TravelSavedData.getTravelData(level).addTravelTarget(level, anchorTravelTarget);
        requireResync(level);
    }

    static void requireResync(@NotNull Level level) {
        EventHandler.syncTask = () -> {
            if (level instanceof ServerLevel) {
                CoreNetwork.sendToDimension(level.dimension(), new SyncTravelDataPacket(TravelSavedData.getTravelData(level).save(new CompoundTag())));
            }
            EventHandler.syncTask = null;
        };
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
    class EventHandler {

        @Nullable
        public static Runnable syncTask = null;

        @SubscribeEvent
        public static void onServerTick(net.minecraftforge.event.TickEvent.ServerTickEvent event) {
            if (event.phase == net.minecraftforge.event.TickEvent.Phase.END && syncTask != null) {
                syncTask.run();
            }
        }
    }
}
