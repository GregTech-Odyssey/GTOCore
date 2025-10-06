package com.gtocore.integration.eio;

import com.gtocore.api.travel.TravelMode;
import com.gtocore.common.machine.multiblock.part.ae.MEPatternPartMachineKt;
import com.gtocore.config.GTOConfig;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import appeng.helpers.patternprovider.PatternProviderLogicHost;
import com.enderio.api.travel.ITravelTarget;
import com.enderio.base.common.handler.TravelHandler;
import com.enderio.base.common.network.SyncTravelDataPacket;
import com.enderio.base.common.travel.TravelSavedData;
import com.enderio.core.common.network.CoreNetwork;
import com.enderio.machines.common.travel.AnchorTravelTarget;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

public interface ITravelHandlerHook {

    String MODE_TAG = "TravelMode";
    String FILTER_BLOCK_TAG = "FilterBlock";

    static TravelMode getTravelMode(Player player) {
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();

        CompoundTag tag = mainHand.hasTag() ? mainHand.getTag() :
                offHand.hasTag() ? offHand.getTag() : null;

        if (tag != null && tag.contains(MODE_TAG)) {
            return TravelMode.fromString(tag.getString(MODE_TAG));
        }
        return TravelMode.ALL;
    }

    static Stream<ITravelTarget> filterTargets(Player player, Stream<ITravelTarget> targets) {
        TravelMode mode = getTravelMode(player);

        return switch (mode) {
            case ONE_PER_CHUNK -> filterOnePerChunk(targets);
            case FILTER_BY_BLOCK -> filterByBlock(player, targets);
            default -> targets;
        };
    }

    static Stream<ITravelTarget> filterOnePerChunk(Stream<ITravelTarget> targets) {
        Map<ChunkPos, ITravelTarget> chunkMap = new HashMap<>();

        targets.forEach(target -> {
            BlockPos pos = target.getPos();
            ChunkPos chunkPos = new ChunkPos(pos);

            chunkMap.merge(chunkPos, target, (existing, newTarget) -> {
                double distExisting = Math.abs(existing.getPos().getY() - pos.getY());
                double distNew = Math.abs(newTarget.getPos().getY() - pos.getY());
                return distNew < distExisting ? newTarget : existing;
            });
        });

        return chunkMap.values().stream();
    }

    static Stream<ITravelTarget> filterByBlock(Player player, Stream<ITravelTarget> targets) {
        ItemStack stack = player.getMainHandItem().isEmpty() ?
                player.getOffhandItem() : player.getMainHandItem();

        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(FILTER_BLOCK_TAG) || tag.getString(FILTER_BLOCK_TAG).isEmpty()) {
            return targets;
        }

        String filterBlock = tag.getString(FILTER_BLOCK_TAG);
        Level level = player.level();

        List<ITravelTarget> targetList = targets.toList();

        Set<String> existingBlockTypes = new HashSet<>();
        for (ITravelTarget target : targetList) {
            BlockPos pos = target.getPos();
            BlockState blockState = level.getBlockState(pos);
            String blockId = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(blockState.getBlock())).toString();
            existingBlockTypes.add(blockId);
        }

        if (!existingBlockTypes.contains(filterBlock)) {
            return Stream.empty();
        }

        return targetList.stream().filter(target -> {
            BlockPos pos = target.getPos();
            BlockState blockState = level.getBlockState(pos);
            String blockId = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(blockState.getBlock())).toString();
            return blockId.equals(filterBlock);
        });
    }

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
        if (!GTOConfig.INSTANCE.staffOfTravellingPatternNodes) return;
        AnchorTravelTarget anchorTravelTarget = new PatternTravelTarget(host);
        TravelSavedData.getTravelData(level).addTravelTarget(level, anchorTravelTarget);
        requireResync(level);
    }

    static void removeAndReadd(@NotNull Level level, MEPatternPartMachineKt<?> host) {
        Optional<ITravelTarget> travelTarget = TravelSavedData.getTravelData(level).getTravelTarget(host.getHolder().getBlockPos());
        if (travelTarget.isPresent() && travelTarget.get() instanceof AnchorTravelTarget anchorTravelTarget) {
            TravelSavedData.getTravelData(level).removeTravelTargetAt(level, anchorTravelTarget.getPos());
        }
        if (!GTOConfig.INSTANCE.staffOfTravellingPatternNodes) return;
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
