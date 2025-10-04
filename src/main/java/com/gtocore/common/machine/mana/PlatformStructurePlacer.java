package com.gtocore.common.machine.mana;

import com.gregtechceu.gtceu.utils.TaskHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.FullChunkStatus;
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

    private final ServerLevel serverLevel;
    private final BlockIterator blockIterator;
    private final int perTick;
    private final boolean breakBlocks;
    private final boolean updateLight;
    private final Consumer<Integer> onBatch;
    private final Runnable onFinished;

    private ISubscription subscription;

    private PlatformStructurePlacer(ServerLevel serverLevel,
                                    BlockIterator blockIterator,
                                    int perTick,
                                    boolean breakBlocks,
                                    boolean updateLight,
                                    Consumer<Integer> onBatch,
                                    Runnable onFinished) {
        this.serverLevel = serverLevel;
        this.blockIterator = blockIterator;
        this.perTick = perTick;
        this.breakBlocks = breakBlocks;
        this.updateLight = updateLight;
        this.onBatch = onBatch;
        this.onFinished = onFinished;

        this.subscription = TaskHandler.enqueueServerTick(serverLevel, this::placeBatch, this::onComplete, 0)::unsubscribe;
    }

    /**
     * 外部调用入口（直接接收 PlatformBlockStructure 对象）
     */
    public static void placeStructureAsync(Level level,
                                           BlockPos startPos,
                                           PlatformBlockType.PlatformBlockStructure structure,
                                           int perTick,
                                           boolean breakBlocks,
                                           boolean updateLight,
                                           Consumer<Integer> onBatch,
                                           Runnable onFinished) throws IOException {
        try (InputStream input = PlatformStructurePlacer.class.getClassLoader().getResourceAsStream(structure.getResourcePath())) {
            if (input == null) {
                throw new FileNotFoundException("Structure file not found: " + structure.getResourcePath());
            }

            BlockIterator iterator = new BlockIterator(input, startPos, structure.getBlockMapping(), structure.getResourcePath());
            if (level instanceof ServerLevel serverLevel) new PlatformStructurePlacer(serverLevel, iterator, perTick, breakBlocks, updateLight, onBatch, onFinished);
            else throw new IllegalArgumentException("Structure placement can only be done on ServerLevel");
        }
    }

    /**
     * 每 tick 放置一批方块
     */
    private void placeBatch() {
        int processedThisTick = 0;

        while (blockIterator.hasNext() && processedThisTick < perTick) {
            BlockIterator.Entry entry = blockIterator.next();
            BlockPos pos = entry.pos();
            BlockState state = entry.state();

            int y = pos.getY();
            if (serverLevel.isOutsideBuildHeight(y)) {
                processedThisTick++;
                continue;
            }

            LevelChunk chunk = serverLevel.getChunkAt(pos);
            LevelChunkSection section = chunk.getSection(chunk.getSectionIndex(y));

            int x = pos.getX() & 15;
            int z = pos.getZ() & 15;
            int localY = y & 15;

            BlockState oldState = section.getBlockState(x, localY, z);

            // 判断是否替换
            if (!breakBlocks && !oldState.isAir()) {
                processedThisTick++;
                continue;
            }

            // 基岩保护
            if (oldState.is(Blocks.BEDROCK)) {
                processedThisTick++;
                continue;
            }

            // 设置新方块
            section.setBlockState(x, localY, z, state);

            // 更新高度图
            chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.MOTION_BLOCKING).update(x, y, z, state);
            chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES).update(x, y, z, state);
            chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR).update(x, y, z, state);
            chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE).update(x, y, z, state);

            // 如果开启光照更新，且光照属性有变化
            if (updateLight && LightEngine.hasDifferentLightProperties(serverLevel, pos, oldState, state)) {
                serverLevel.getChunkSource().getLightEngine().checkBlock(pos);
            }

            // BlockEntity 处理
            oldState.onRemove(serverLevel, pos, state, false);
            if (oldState.hasBlockEntity()) {
                serverLevel.removeBlockEntity(pos);
            }
            if (state.hasBlockEntity() && state.getBlock() instanceof net.minecraft.world.level.block.EntityBlock entityBlock) {
                BlockEntity be = entityBlock.newBlockEntity(pos, state);
                if (be != null) {
                    serverLevel.setBlockEntity(be);
                    chunk.setBlockEntity(be);
                }
            }

            // 通知附近玩家方块更新
            var fullStatus = chunk.getFullStatus();
            if (fullStatus.isOrAfter(FullChunkStatus.BLOCK_TICKING)) {
                serverLevel.getChunkSource().blockChanged(pos);
            }

            chunk.setUnsaved(true);
            processedThisTick++;
        }

        // 进度回调（基于 aisle 数量）
        if (onBatch != null) {
            onBatch.accept(blockIterator.getProgressPercentage());
        }

        // 如果迭代器结束，自动停止任务
        if (!blockIterator.hasNext()) {
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

        private final int totalAisles;      // 文件中总 aisle 数
        private int processedAisles = 0;    // 已处理 aisle 数

        BlockIterator(InputStream input, BlockPos startPos, Map<Character, BlockState> blockMapping, String resourcePath) throws IOException {
            this.reader = new BufferedReader(new InputStreamReader(input));
            this.startPos = startPos;
            this.blockMapping = blockMapping;
            this.totalAisles = countTotalAisles(resourcePath);
            readNextAisle();
        }

        /**
         * 统计文件中总 aisle 数
         */
        private int countTotalAisles(String resourcePath) throws IOException {
            try (InputStream countInput = PlatformStructurePlacer.class.getClassLoader().getResourceAsStream(resourcePath)) {

                BufferedReader countReader;
                if (countInput != null) countReader = new BufferedReader(new InputStreamReader(countInput));
                else throw new FileNotFoundException("Structure file not found: " + resourcePath);

                int count = 0;
                String line;
                while ((line = countReader.readLine()) != null) {
                    if (line.trim().startsWith(".aisle(")) {
                        count++;
                    }
                }
                return count;
            }
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
                            processedAisles++; // 处理完一个 aisle
                            return;
                        }
                    }
                }
            }
            currentAisle = null; // 文件结束
        }

        /**
         * 获取基于 aisle 的进度百分比
         */
        public int getProgressPercentage() {
            if (totalAisles == 0) return 0;
            return Math.min(100, (int) (((double) processedAisles / totalAisles) * 100));
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
