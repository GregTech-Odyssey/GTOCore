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

import java.io.*;
import java.util.*;
import java.util.function.Consumer;

/**
 * 高性能结构生成器（流式读取版本）
 * - 流式读取结构文件，内存占用极低
 * - 高度图每次放置立即更新
 * - 光照更新由 updateLight 参数控制：
 * - true: 每次放置方块立即更新光照（两步法）
 * - false: 不更新光照
 * - 生成结束后不做光照更新
 */
public final class PlatformStructurePlacer {

    private final ServerLevel level;
    private final BlockIterator blockIterator;
    private final int totalBlocks;
    private final int perTick;
    private final boolean breakBlocks;
    private final boolean updateLight; // 是否每次放置方块更新光照
    private final Consumer<Integer> onBatch;
    private final Runnable onFinished;

    private int placedCount = 0;
    private ISubscription subscription;

    private PlatformStructurePlacer(ServerLevel level,
                                    BlockIterator blockIterator,
                                    int totalBlocks,
                                    int perTick,
                                    boolean breakBlocks,
                                    boolean updateLight,
                                    Consumer<Integer> onBatch,
                                    Runnable onFinished) {
        this.level = level;
        this.blockIterator = blockIterator;
        this.totalBlocks = totalBlocks;
        this.perTick = perTick;
        this.breakBlocks = breakBlocks;
        this.updateLight = updateLight;
        this.onBatch = onBatch;
        this.onFinished = onFinished;

        this.subscription = TaskHandler.enqueueServerTick(level, this::placeBatch, this::onComplete, 0)::unsubscribe;
    }

    /**
     * 外部调用入口
     */
    public static void placeStructureAsync(ServerLevel level,
                                           BlockPos startPos,
                                           String resourcePath,
                                           Map<Character, BlockState> blockMapping,
                                           int perTick,
                                           boolean breakBlocks,
                                           boolean updateLight,
                                           Consumer<Integer> onBatch,
                                           Runnable onFinished) throws IOException {
        try (InputStream input = PlatformStructurePlacer.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (input == null) throw new FileNotFoundException("Structure file not found: " + resourcePath);

            int totalBlocks = countTotalBlocks(input, blockMapping);
            input.reset();

            BlockIterator iterator = new BlockIterator(input, startPos, blockMapping);

            new PlatformStructurePlacer(level, iterator, totalBlocks, perTick, breakBlocks, updateLight, onBatch, onFinished);
        }
    }

    /**
     * 简化调用
     */
    public static void placeStructureAsync(ServerLevel level,
                                           BlockPos startPos,
                                           String resourcePath,
                                           Map<Character, BlockState> blockMapping) throws IOException {
        placeStructureAsync(level, startPos, resourcePath, blockMapping, 50000, true, false, null, null);
    }

    /**
     * 预计算总方块数
     */
    private static int countTotalBlocks(InputStream input, Map<Character, BlockState> blockMapping) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            int count = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                String[] aisle = line.split(",");
                for (String row : aisle) {
                    for (char c : row.toCharArray()) {
                        if (blockMapping.containsKey(c)) count++;
                    }
                }
            }
            return count;
        }
    }

    /**
     * 每 tick 放置一批方块
     */
    private void placeBatch() {
        if (totalBlocks == 0) {
            subscription.unsubscribe();
            return;
        }

        int end = Math.min(placedCount + perTick, totalBlocks);

        while (placedCount < end && blockIterator.hasNext()) {
            BlockIterator.Entry entry = blockIterator.next();
            BlockPos pos = entry.pos();
            BlockState state = entry.state();

            int y = pos.getY();
            if (level.isOutsideBuildHeight(y)) {
                placedCount++;
                continue;
            }

            LevelChunk chunk = level.getChunkAt(pos);
            LevelChunkSection section = chunk.getSection(chunk.getSectionIndex(y));

            int x = pos.getX() & 15;
            int z = pos.getZ() & 15;
            int localY = y & 15;

            BlockState oldState = section.getBlockState(x, localY, z);

            // 判断是否替换
            if (!breakBlocks && !oldState.isAir()) {
                placedCount++;
                continue;
            }

            // 基岩保护
            if (!level.getGameRules().getBoolean(net.minecraft.world.level.GameRules.RULE_MOBGRIEFING) &&
                    oldState.is(Blocks.BEDROCK)) {
                placedCount++;
                continue;
            }

            // 设置新方块
            section.setBlockState(x, localY, z, state);

            // 更新高度图
            for (Heightmap.Types type : Heightmap.Types.values()) {
                chunk.getOrCreateHeightmapUnprimed(type).update(x, y, z, state);
            }

            // 如果开启光照更新，且光照属性有变化
            if (updateLight && LightEngine.hasDifferentLightProperties(level, pos, oldState, state)) {
                // 更新天空光照源
                chunk.getSkyLightSources().update(level, pos.getX(), pos.getY(), pos.getZ());
                // 通知光照引擎检查该方块
                level.getChunkSource().getLightEngine().checkBlock(pos);
            }

            // BlockEntity 处理
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
            placedCount++;
        }

        // 进度回调
        int progress = totalBlocks > 0 ? (int) ((double) placedCount / totalBlocks * 100) : 0;
        if (onBatch != null) {
            onBatch.accept(progress);
        }

        // 完成后取消订阅
        if (placedCount >= totalBlocks) {
            subscription.unsubscribe();
        }
    }

    /**
     * 全部生成完成后调用
     */
    private void onComplete() {
        if (onFinished != null) {
            onFinished.run();
        }
    }

    /**
     * 方块迭代器（流式读取）
     */
    private static class BlockIterator implements Iterator<BlockIterator.Entry> {

        private final BufferedReader reader;
        private final BlockPos startPos;
        private final Map<Character, BlockState> blockMapping;
        private String[] currentAisle;
        private String currentRow;
        private int z = 0, y = 0, x = 0;

        BlockIterator(InputStream input, BlockPos startPos, Map<Character, BlockState> blockMapping) throws IOException {
            this.reader = new BufferedReader(new InputStreamReader(input));
            this.startPos = startPos;
            this.blockMapping = blockMapping;
            readNextAisle();
        }

        private void readNextAisle() throws IOException {
            String line = reader.readLine();
            currentAisle = line == null ? null : line.split(",");
            y = 0;
            if (currentAisle != null) {
                currentRow = currentAisle[y];
                x = 0;
            }
        }

        @Override
        public boolean hasNext() {
            try {
                while (true) {
                    if (currentAisle == null) return false;

                    while (y < currentAisle.length) {
                        currentRow = currentAisle[y];
                        while (x < currentRow.length()) {
                            char c = currentRow.charAt(x);
                            if (blockMapping.containsKey(c)) return true;
                            x++;
                        }
                        x = 0;
                        y++;
                    }

                    readNextAisle();
                    z++;
                }
            } catch (IOException e) {
                return false;
            }
        }

        @Override
        public Entry next() {
            if (!hasNext()) throw new NoSuchElementException();
            char c = currentRow.charAt(x);
            BlockState state = blockMapping.get(c);
            BlockPos pos = new BlockPos(
                    startPos.getX() + x,
                    startPos.getY() + y,
                    startPos.getZ() + z);
            Entry entry = new Entry(pos, state);
            x++;
            return entry;
        }

        /**
         * Java 17 record 作为数据载体
         */
        public record Entry(BlockPos pos, BlockState state) {}
    }
}
