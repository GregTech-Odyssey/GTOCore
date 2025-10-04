package com.gtocore.common.machine.mana;

import com.gregtechceu.gtceu.utils.TaskHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PlatformStructurePlacer {

    private final ServerLevel level;
    private final BlockIterator blockIterator;
    private final int totalBlocks;
    private final int perTick;
    private final boolean breakBlocks;
    private final boolean updateLight;
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
    public static void placeStructureAsync(Level level,
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

            if (level instanceof ServerLevel serverLevel) new PlatformStructurePlacer(serverLevel, iterator, totalBlocks, perTick, breakBlocks, updateLight, onBatch, onFinished);
        }
    }

    /**
     * 预计算总方块数（用于进度计算）
     */
    private static int countTotalBlocks(InputStream input, Map<Character, BlockState> blockMapping) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            Pattern aislePattern = Pattern.compile("\\.aisle\\(([^)]+)\\)");
            Pattern stringPattern = Pattern.compile("\"([^\"]+)\"");

            int count = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith(".aisle(")) {
                    Matcher aisleMatcher = aislePattern.matcher(line);
                    if (aisleMatcher.find()) {
                        String content = aisleMatcher.group(1);
                        Matcher stringMatcher = stringPattern.matcher(content);
                        while (stringMatcher.find()) {
                            String row = stringMatcher.group(1);
                            for (char c : row.toCharArray()) {
                                if (blockMapping.containsKey(c)) count++;
                            }
                        }
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
                chunk.getSkyLightSources().update(level, pos.getX(), pos.getY(), pos.getZ());
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
     * 方块迭代器（流式读取 .aisle(...) 格式）
     */
    private static class BlockIterator implements Iterator<BlockIterator.Entry> {

        private final BufferedReader reader;
        private final BlockPos startPos;
        private final Map<Character, BlockState> blockMapping;
        private final Pattern aislePattern = Pattern.compile("\\.aisle\\(([^)]+)\\)");
        private final Pattern stringPattern = Pattern.compile("\"([^\"]+)\"");

        private String[] currentAisle; // 当前 y 层
        private int y = 0;             // 当前行索引（y 层内）
        private int x = 0;             // 当前字符索引（行内 x）
        private int z = 0;             // 当前层索引（深度 z）

        BlockIterator(InputStream input, BlockPos startPos, Map<Character, BlockState> blockMapping) throws IOException {
            this.reader = new BufferedReader(new InputStreamReader(input));
            this.startPos = startPos;
            this.blockMapping = blockMapping;
            readNextAisle();
        }

        /**
         * 读取下一个 .aisle(...) 并解析为 String[]
         */
        private void readNextAisle() throws IOException {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith(".aisle(")) {
                    Matcher aisleMatcher = aislePattern.matcher(line);
                    if (aisleMatcher.find()) {
                        String content = aisleMatcher.group(1);
                        Matcher stringMatcher = stringPattern.matcher(content);
                        List<String> rows = new ArrayList<>();
                        while (stringMatcher.find()) {
                            rows.add(stringMatcher.group(1));
                        }
                        if (!rows.isEmpty()) {
                            currentAisle = rows.toArray(new String[0]);
                            y = 0;
                            x = 0;
                            return;
                        }
                    }
                }
            }
            currentAisle = null; // 文件结束
        }

        @Override
        public boolean hasNext() {
            try {
                while (true) {
                    if (currentAisle == null) return false;

                    if (y < currentAisle.length) {
                        String row = currentAisle[y];
                        while (x < row.length()) {
                            char c = row.charAt(x);
                            if (blockMapping.containsKey(c)) {
                                return true;
                            }
                            x++;
                        }
                        x = 0;
                        y++;
                    } else {
                        readNextAisle();
                        z++;
                    }
                }
            } catch (IOException e) {
                return false;
            }
        }

        @Override
        public Entry next() {
            if (!hasNext()) throw new NoSuchElementException();
            String row = currentAisle[y];
            char c = row.charAt(x);
            BlockState state = blockMapping.get(c);
            BlockPos pos = new BlockPos(
                    startPos.getX() + x,
                    startPos.getY() + y,
                    startPos.getZ() + z);
            Entry entry = new Entry(pos, state);
            x++;
            return entry;
        }

        public record Entry(BlockPos pos, BlockState state) {}
    }
}
