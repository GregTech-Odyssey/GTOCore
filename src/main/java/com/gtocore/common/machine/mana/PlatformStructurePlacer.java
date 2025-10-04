package com.gtocore.common.machine.mana;

import com.gregtechceu.gtceu.utils.TaskHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.lighting.LightEngine;

import com.lowdragmc.lowdraglib.syncdata.ISubscription;

import java.util.*;
import java.util.function.Consumer;

import static com.gtocore.common.machine.mana.PlatformBlockType.readDepthFromResource;

public final class PlatformStructurePlacer {

    private final ServerLevel level;
    private final List<BlockPos> positions;
    private final List<BlockState> states;
    private final int perTick;
    private final Consumer<Integer> onBatch;
    private final Runnable onFinished;

    private int index = 0;
    private ISubscription subscription;

    private PlatformStructurePlacer(ServerLevel level,
                                    List<BlockPos> positions,
                                    List<BlockState> states,
                                    int perTick,
                                    Consumer<Integer> onBatch,
                                    Runnable onFinished) {
        this.level = level;
        this.positions = positions;
        this.states = states;
        this.perTick = perTick;
        this.onBatch = onBatch;
        this.onFinished = onFinished;

        this.subscription = TaskHandler.enqueueServerTick(level, this::placeBatch, this::onComplete, 0)::unsubscribe;
    }

    /**
     * 从资源文件加载结构并异步放置
     */
    public static void placeStructureAsync(ServerLevel level,
                                           BlockPos startPos,
                                           String resourcePath,
                                           Map<Character, BlockState> blockMapping,
                                           int perTick,
                                           Consumer<Integer> onBatch,
                                           Runnable onFinished) {
        List<String[]> depth = readDepthFromResource(resourcePath);
        List<BlockPos> positions = new ArrayList<>();
        List<BlockState> states = new ArrayList<>();

        for (int z = 0; z < depth.size(); z++) {
            String[] aisle = depth.get(z);
            for (int y = 0; y < aisle.length; y++) {
                String row = aisle[y];
                for (int x = 0; x < row.length(); x++) {
                    char c = row.charAt(x);
                    BlockState state = blockMapping.get(c);
                    if (state != null) {
                        positions.add(new BlockPos(
                                startPos.getX() + x,
                                startPos.getY() + y,
                                startPos.getZ() + z));
                        states.add(state);
                    }
                }
            }
        }

        new PlatformStructurePlacer(level, positions, states, perTick, onBatch, onFinished);
    }

    /**
     * 简化调用（默认每 tick 50000 个方块）
     */
    public static void placeStructureAsync(ServerLevel level,
                                           BlockPos startPos,
                                           String resourcePath,
                                           Map<Character, BlockState> blockMapping) {
        placeStructureAsync(level, startPos, resourcePath, blockMapping, 50000, null, null);
    }

    /**
     * 每 tick 放置一批方块，并立即更新高度图和光照
     */
    private void placeBatch() {
        int total = positions.size();
        if (total == 0) {
            subscription.unsubscribe();
            return;
        }

        int end = Math.min(index + perTick, total);

        for (; index < end; index++) {
            BlockPos pos = positions.get(index);
            BlockState state = states.get(index);

            int y = pos.getY();
            if (level.isOutsideBuildHeight(y)) continue;

            LevelChunk chunk = level.getChunkAt(pos);
            LevelChunkSection section = chunk.getSection(chunk.getSectionIndex(y));

            if (!level.getGameRules().getBoolean(net.minecraft.world.level.GameRules.RULE_MOBGRIEFING) &&
                    chunk.getBlockState(pos).is(Blocks.BEDROCK))
                continue;

            int x = pos.getX() & 15;
            int z = pos.getZ() & 15;
            int localY = y & 15;

            BlockState oldState = section.getBlockState(x, localY, z);
            section.setBlockState(x, localY, z, state);

            for (Heightmap.Types type : Heightmap.Types.values()) {
                chunk.getOrCreateHeightmapUnprimed(type).update(x, y, z, state);
            }

            if (LightEngine.hasDifferentLightProperties(level, pos, oldState, state)) {
                level.getChunkSource().getLightEngine().checkBlock(pos);
            }

            oldState.onRemove(level, pos, state, false);
            if (oldState.hasBlockEntity()) {
                level.removeBlockEntity(pos);
            }
            if (state.hasBlockEntity() && state.getBlock() instanceof net.minecraft.world.level.block.EntityBlock entityBlock) {
                BlockEntity be = entityBlock.newBlockEntity(pos, state);
                if (be != null) {
                    level.setBlockEntity(be);
                    chunk.setBlockEntity(be);
                }
            }

            chunk.setUnsaved(true);
        }

        int progress = (int) ((double) Math.min(end, total) / total * 100);
        if (onBatch != null) {
            onBatch.accept(progress);
        }

        if (index >= total) {
            subscription.unsubscribe();
        }
    }

    private void onComplete() {
        if (onFinished != null) {
            onFinished.run();
        }
    }
}
