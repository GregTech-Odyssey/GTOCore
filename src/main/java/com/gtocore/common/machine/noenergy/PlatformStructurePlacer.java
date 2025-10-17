package com.gtocore.common.machine.noenergy;

import com.gtocore.common.data.GTOMachines;

import com.gregtechceu.gtceu.utils.TaskHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.FullChunkStatus;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.lighting.LightEngine;

import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import it.unimi.dsi.fastutil.chars.Char2ReferenceOpenHashMap;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.IntConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.gtocore.common.machine.noenergy.PlatformCreators.loadMappingFromJson;

final class PlatformStructurePlacer {

    private final ServerLevel serverLevel;
    private final BlockIterator blockIterator;
    private final int perTick;
    private final boolean breakBlocks;
    private final boolean skipAir;
    private final boolean updateLight;
    private final IntConsumer onBatch;
    private final Runnable onFinished;

    private final ISubscription subscription;

    private PlatformStructurePlacer(ServerLevel serverLevel,
                                    BlockIterator blockIterator,
                                    int perTick,
                                    boolean breakBlocks,
                                    boolean skipAir,
                                    boolean updateLight,
                                    IntConsumer onBatch,
                                    Runnable onFinished) {
        this.serverLevel = serverLevel;
        this.blockIterator = blockIterator;
        this.perTick = perTick;
        this.breakBlocks = breakBlocks;
        this.skipAir = skipAir;
        this.updateLight = updateLight;
        this.onBatch = onBatch;
        this.onFinished = onFinished;

        this.subscription = TaskHandler.enqueueServerTick(serverLevel, this::placeBatch, this::onComplete, 0);
    }

    /**
     * 每 tick 放置一批方块
     */
    private void placeBatch() {
        int processedThisTick = 0;

        while (blockIterator.hasNext() && processedThisTick < perTick) {
            BlockIterator.Entry entry = blockIterator.next();
            BlockState state = entry.state();

            if (skipAir && state.isAir()) {
                continue;
            }

            BlockPos pos = entry.pos();
            int y = pos.getY();
            if (serverLevel.isOutsideBuildHeight(y)) {
                continue;
            }

            LevelChunk chunk = serverLevel.getChunkAt(pos);
            LevelChunkSection section = chunk.getSection(chunk.getSectionIndex(y));

            int x = pos.getX() & 15;
            int z = pos.getZ() & 15;
            int localY = y & 15;

            BlockState oldState = section.getBlockState(x, localY, z);

            if (oldState.getBlock().equals(GTOMachines.INDUSTRIAL_PLATFORM_DEPLOYMENT_TOOLS.getBlock())) {
                continue;
            }

            if (state.isAir() && oldState.isAir()) {
                continue;
            }

            if (!breakBlocks && !oldState.isAir()) {
                processedThisTick++;
                continue;
            }

            if (oldState.is(Blocks.BEDROCK)) {
                processedThisTick++;
                continue;
            }

            section.setBlockState(x, localY, z, state);

            chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.MOTION_BLOCKING).update(x, y, z, state);
            chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES).update(x, y, z, state);
            chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR).update(x, y, z, state);
            chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE).update(x, y, z, state);

            if (updateLight && LightEngine.hasDifferentLightProperties(serverLevel, pos, oldState, state)) {
                serverLevel.getChunkSource().getLightEngine().checkBlock(pos);
            }

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

            var fullStatus = chunk.getFullStatus();
            if (fullStatus.isOrAfter(FullChunkStatus.BLOCK_TICKING)) {
                serverLevel.getChunkSource().blockChanged(pos);
            }

            chunk.setUnsaved(true);
            processedThisTick++;
        }

        if (onBatch != null) {
            onBatch.accept(blockIterator.getProgressPercentage());
        }

        if (!blockIterator.hasNext()) {
            subscription.unsubscribe();
        }
    }

    private void onComplete() {
        if (onFinished != null) {
            onFinished.run();
        }
    }

    /**
     * 方块迭代器（修复镜像方向错误，支持独立旋转/镜像）
     */
    private static class BlockIterator implements Iterator<BlockIterator.Entry> {

        private final BufferedReader reader;
        private final BlockPos startPos;
        private final Char2ReferenceOpenHashMap<BlockState> blockMapping;
        private final Pattern aislePattern = Pattern.compile("\\.aisle\\(([^)]+)\\)");
        private final Pattern stringPattern = Pattern.compile("\"([^\"]+)\"");

        private String[] currentAisle;
        private int y = 0;
        private int x = 0;
        private int z = 0;

        private final int totalAisles;
        private int processedAisles = 0;

        private final boolean zMirror;
        private final boolean xMirror;
        private final int rotation;
        private final int sizeX, sizeZ;
        private final int offsetX, offsetY, offsetZ;

        BlockIterator(InputStream input, BlockPos startPos, Char2ReferenceOpenHashMap<BlockState> blockMapping,
                      String resourcePath, boolean zMirror, boolean xMirror, int rotation) throws IOException {
            this.reader = new BufferedReader(new InputStreamReader(input));
            this.startPos = startPos;
            this.blockMapping = blockMapping;
            this.totalAisles = countTotalAisles(resourcePath);
            this.zMirror = zMirror;
            this.xMirror = xMirror;
            this.rotation = rotation;

            // 读取完整结构尺寸（包含 sizeY）
            int sx = 0, sy = 0, sz = 0;
            try (InputStream sizeInput = PlatformStructurePlacer.class.getClassLoader().getResourceAsStream(resourcePath)) {
                if (sizeInput != null) {
                    BufferedReader sizeReader = new BufferedReader(new InputStreamReader(sizeInput));
                    String line;
                    while ((line = sizeReader.readLine()) != null) {
                        line = line.trim();
                        if (line.startsWith(".size(")) {
                            Pattern sizePattern = Pattern.compile("\\.size\\((\\d+), (\\d+), (\\d+)\\)");
                            Matcher m = sizePattern.matcher(line);
                            if (m.find()) {
                                sx = Integer.parseInt(m.group(1));
                                sy = Integer.parseInt(m.group(2));
                                sz = Integer.parseInt(m.group(3));
                                break;
                            }
                        }
                    }
                    sizeReader.close();
                }
            }
            this.sizeX = sx;
            this.sizeZ = sz;

            // 预计算最小局部坐标和偏移量（包含 Y 轴）
            int minLx = Integer.MAX_VALUE, minLy = Integer.MAX_VALUE, minLz = Integer.MAX_VALUE;
            for (int iz = 0; iz < sz; iz++) {
                for (int iy = 0; iy < sy; iy++) {
                    for (int ix = 0; ix < sx; ix++) {
                        int[] transformed = transformCoords(ix, iy, iz, sx, sz, rotation, xMirror, zMirror);
                        minLx = Math.min(minLx, transformed[0]);
                        minLy = Math.min(minLy, transformed[1]);
                        minLz = Math.min(minLz, transformed[2]);
                    }
                }
            }
            this.offsetX = -minLx;
            this.offsetY = -minLy;
            this.offsetZ = -minLz;

            readNextAisle();
        }

        /**
         * 坐标变换（XZ 平面内的镜像和绕 Y 轴旋转，与 BlockState 变换顺序一致）
         */
        private static int[] transformCoords(int lx, int ly, int lz, int sx, int sz, int rotation, boolean xMirror, boolean zMirror) {
            int rx = lx, rz = lz;

            // 先旋转（绕 Y 轴顺时针）
            switch (rotation) {
                case 90 -> {
                    int t = rx;
                    rx = sz - 1 - rz;
                    rz = t;
                }
                case 180 -> {
                    rx = sx - 1 - rx;
                    rz = sz - 1 - rz;
                }
                case 270 -> {
                    int t = rx;
                    rx = rz;
                    rz = sx - 1 - t;
                }
            }

            // 再镜像（X/Z 分别处理，与 Mirror 枚举对应）
            if (xMirror) rx = sx - 1 - rx;
            if (zMirror) rz = sz - 1 - rz;

            return new int[] { rx, ly, rz };
        }

        /**
         * 统计文件中总 aisle 数
         */
        private static int countTotalAisles(String resourcePath) throws IOException {
            try (InputStream countInput = PlatformStructurePlacer.class.getClassLoader().getResourceAsStream(resourcePath)) {
                BufferedReader countReader;
                if (countInput != null) {
                    countReader = new BufferedReader(new InputStreamReader(countInput));
                } else {
                    throw new FileNotFoundException("Structure file not found: " + resourcePath);
                }

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
         * 读取下一个 .aisle(...)
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
                            processedAisles++;
                            return;
                        }
                    }
                }
            }
            currentAisle = null;
        }

        int getProgressPercentage() {
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

            // 1. 坐标变换（先旋转，再镜像）
            int[] transformed = transformCoords(x, y, z, sizeX, sizeZ, rotation, xMirror, zMirror);
            int lx = transformed[0] + offsetX;
            int ly = transformed[1] + offsetY;
            int lz = transformed[2] + offsetZ;

            // 2. BlockState 变换（与坐标变换顺序一致：先旋转，再镜像）
            Rotation rotationEnum = switch (rotation) {
                case 90 -> Rotation.CLOCKWISE_90;
                case 180 -> Rotation.CLOCKWISE_180;
                case 270 -> Rotation.COUNTERCLOCKWISE_90;
                default -> Rotation.NONE;
            };
            state = state.rotate(rotationEnum);

            // 镜像处理
            if (xMirror && zMirror) {
                state = state.mirror(Mirror.LEFT_RIGHT);
                state = state.mirror(Mirror.FRONT_BACK);
            } else if (xMirror) {
                state = state.mirror(Mirror.FRONT_BACK);
            } else if (zMirror) {
                state = state.mirror(Mirror.LEFT_RIGHT);
            }

            // 计算最终世界坐标
            BlockPos pos = new BlockPos(
                    startPos.getX() + lx,
                    startPos.getY() + ly,
                    startPos.getZ() + lz);

            Entry entry = new Entry(pos, state);
            x++;
            return entry;
        }

        public record Entry(BlockPos pos, BlockState state) {}
    }

    /**
     * 外部调用入口（支持旋转和镜像）
     */
    static void placeStructureAsync(Level level,
                                    BlockPos startPos,
                                    PlatformBlockType.PlatformBlockStructure structure,
                                    int perTick,
                                    boolean breakBlocks,
                                    boolean skipAir,
                                    boolean updateLight,
                                    boolean zMirror,
                                    boolean xMirror,
                                    int rotation,
                                    IntConsumer onBatch,
                                    Runnable onFinished) throws IOException {
        String resourcePath = "assets/" + structure.resource().getNamespace() + "/" + structure.resource().getPath();
        try (InputStream input = PlatformStructurePlacer.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new FileNotFoundException("Structure file not found: " + structure.resource());
            }

            // 由于未知问题，这里交换了xz顺序，使其可以正常运行
            BlockIterator iterator = new BlockIterator(input, startPos, loadMappingFromJson(structure.blockMapping()), resourcePath, xMirror, zMirror, rotation);
            if (level instanceof ServerLevel serverLevel) new PlatformStructurePlacer(serverLevel, iterator, perTick, breakBlocks, skipAir, updateLight, onBatch, onFinished);
            else throw new IllegalArgumentException("Structure placement can only be done on ServerLevel");
        }
    }
}
