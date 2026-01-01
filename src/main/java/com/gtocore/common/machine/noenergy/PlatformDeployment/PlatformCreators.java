package com.gtocore.common.machine.noenergy.PlatformDeployment;

import com.gtolib.GTOCore;
import com.gtolib.utils.RLUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.LevelChunk;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.unimi.dsi.fastutil.chars.Char2ReferenceLinkedOpenHashMap;
import it.unimi.dsi.fastutil.chars.Char2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.chars.CharOpenHashSet;
import it.unimi.dsi.fastutil.objects.Reference2CharLinkedOpenHashMap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

class PlatformCreators {

    private static final CharOpenHashSet ILLEGAL_CHARS = new CharOpenHashSet();
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");
    private static final long PROGRESS_THRESHOLD = 10_000_000L;
    private static volatile boolean isExporting = false;

    private static final ThreadFactory VIRTUAL_THREAD_FACTORY = Thread.ofVirtual().name("structure-export-", 0).factory();
    private static final java.util.concurrent.ExecutorService EXPORT_EXECUTOR = Executors.newThreadPerTaskExecutor(VIRTUAL_THREAD_FACTORY);

    static {
        for (char c : new char[] { '.', '(', ')', ',', '/', '\\', '"', '\'', '`' }) {
            ILLEGAL_CHARS.add(c);
        }
        for (int i = Character.MIN_VALUE; i <= Character.MAX_VALUE; i++) {
            char c = (char) i;
            if (Character.isISOControl(c)) ILLEGAL_CHARS.add(c);
        }
        for (char c : """
                \u200B\u200C\u200D\u200E\u200F\u2028\u2029\u2060
                \u2061\u2062\u2063\u2064\uFFF9\uFFFA\uFFFB\uFEFF
                \u00A0\u2002\u2003\u2009\u200A\u00AD\u1680\u180E
                \u3000\u202F\u205F""".replace("\n", "").toCharArray()) {
            ILLEGAL_CHARS.add(c);
        }
    }

    /**
     * 异步导出结构（支持 XZ 平面镜像和绕 Y 轴旋转）
     */
    static void PlatformCreationAsync(ServerLevel level, BlockPos startPos, BlockPos endPos,
                                      boolean xMirror, boolean zMirror, int rotation) {
        if (isExporting) return;
        isExporting = true;
        CompletableFuture.runAsync(() -> {
            try {
                exportStructure(level, startPos, endPos, xMirror, zMirror, rotation);
            } catch (Exception e) {
                GTOCore.LOGGER.error("Structure export failed", e);
            } finally {
                isExporting = false;
            }
        }, EXPORT_EXECUTOR);
    }

    /**
     * 平台创建函数（异步）
     */
    private static void exportStructure(ServerLevel level, BlockPos pos1, BlockPos pos2,
                                        boolean xMirror, boolean zMirror, int rotation) {
        // 输出目录创建
        Path outputDir = Paths.get("logs", "platform");
        try {
            Files.createDirectories(outputDir);
        } catch (IOException e) {
            GTOCore.LOGGER.error("Failed to create output directory", e);
            return;
        }

        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        Path structurePath = outputDir.resolve(timestamp);
        Path mappingPath = outputDir.resolve(timestamp + ".json");

        int minX = Math.min(pos1.getX(), pos2.getX());
        int minY = Math.min(pos1.getY(), pos2.getY());
        int minZ = Math.min(pos1.getZ(), pos2.getZ());
        int maxX = Math.max(pos1.getX(), pos2.getX());
        int maxY = Math.max(pos1.getY(), pos2.getY());
        int maxZ = Math.max(pos1.getZ(), pos2.getZ());

        int dx = maxX - minX + 1;
        int dy = maxY - minY + 1;
        int dz = maxZ - minZ + 1;

        boolean swapXZ = rotation == 90 || rotation == 270;
        if (swapXZ) {
            int temp = dx;
            dx = dz;
            dz = temp;
        }

        // 映射表初始化
        Reference2CharLinkedOpenHashMap<BlockState> stateToChar = new Reference2CharLinkedOpenHashMap<>();
        char nextChar = getNextValidChar('A');
        BlockState air = Blocks.AIR.defaultBlockState();
        stateToChar.put(air, ' ');

        // 进度统计
        long totalBlocks = (long) dx * dy * dz;
        long[] progress = { 0 };

        // 复用MutableBlockPos减少对象创建
        MutableBlockPos mutablePos = new MutableBlockPos();

        // Chunk缓存（修复ChunkPos获取逻辑）
        Map<ChunkPos, LevelChunk> chunkCache = new HashMap<>();

        // 结构文件写入
        try (BufferedWriter writer = Files.newBufferedWriter(structurePath, StandardCharsets.UTF_8)) {
            GTOCore.LOGGER.info("Starting structure export");
            writer.write(String.format(".size(%d, %d, %d)", dx, dy, dz));
            writer.newLine();
            writer.flush();

            // 遍历Z层
            for (int outZ = 0; outZ < dz; outZ++) {
                List<String> ySlices = new ArrayList<>(dy); // 预分配容量

                for (int outY = 0; outY < dy; outY++) {
                    StringBuilder xChars = new StringBuilder(dx); // 预分配容量

                    for (int outX = 0; outX < dx; outX++) {
                        // 坐标转换
                        int[] transformed = transformCoords(outX, outZ, dx, dz, rotation, xMirror, zMirror);
                        int rx = transformed[0];
                        int rz = transformed[1];

                        // 世界坐标计算
                        int worldX = minX + rx;
                        int worldY = minY + outY;
                        int worldZ = minZ + rz;

                        // 获取BlockState（修复Chunk缓存逻辑）
                        mutablePos.set(worldX, worldY, worldZ);
                        BlockState originalState = getCachedBlockState(level, mutablePos, chunkCache);

                        // 变换BlockState
                        BlockState transformedState = transformBlockState(originalState, rotation, xMirror, zMirror);

                        // 更新映射表
                        if (!stateToChar.containsKey(transformedState)) {
                            stateToChar.put(transformedState, nextChar);
                            nextChar = getNextValidChar((char) (nextChar + 1));
                        }
                        xChars.append(stateToChar.get(transformedState));

                        // 进度汇报
                        if (++progress[0] % PROGRESS_THRESHOLD == 0 || progress[0] == totalBlocks) {
                            double percent = (double) progress[0] / totalBlocks * 100;
                            GTOCore.LOGGER.info(String.format("Export progress: %d / %d blocks (%.2f%%)",
                                    progress[0], totalBlocks, percent));
                        }
                    }
                    ySlices.add(String.format("\"%s\"", xChars));
                }

                // 写入当前层并Flush
                writer.write(String.format(".aisle(%s)", String.join(", ", ySlices)));
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            GTOCore.LOGGER.error("Failed to write structure file", e);
        }

        // 生成映射文件
        Char2ReferenceLinkedOpenHashMap<BlockState> charToState = new Char2ReferenceLinkedOpenHashMap<>();
        stateToChar.reference2CharEntrySet().fastForEach(e -> charToState.put(e.getCharValue(), e.getKey()));
        saveMappingToJson(charToState, mappingPath);

        // 日志输出
        GTOCore.LOGGER.info("Exported files:");
        GTOCore.LOGGER.info(" - Structure: {}", structurePath);
        GTOCore.LOGGER.info(" - Mapping: {}", mappingPath);
    }

    private static BlockState getCachedBlockState(ServerLevel level, MutableBlockPos pos, Map<ChunkPos, LevelChunk> chunkCache) {
        ChunkPos chunkPos = new ChunkPos(pos);
        LevelChunk chunk = chunkCache.computeIfAbsent(chunkPos, cp -> level.getChunk(cp.x, cp.z));
        return chunk.getBlockState(pos);
    }

    /**
     * 坐标转换提取
     */
    private static int[] transformCoords(int outX, int outZ, int dx, int dz, int rotation, boolean xMirror, boolean zMirror) {
        int rx = outX;
        int rz = outZ;

        // Java 21增强switch
        rx = switch (rotation) {
            case 90 -> {
                int t = rx;
                rx = dz - 1 - rz;
                rz = t;
                yield rx;
            }
            case 180 -> dx - 1 - rx;
            case 270 -> {
                int t = rx;
                rx = rz;
                rz = dx - 1 - t;
                yield rx;
            }
            default -> rx;
        };

        if (xMirror) rx = dx - 1 - rx;
        if (zMirror) rz = dz - 1 - rz;

        return new int[] { rx, rz };
    }

    /**
     * 统一的方块状态旋转/镜像处理
     */
    private static BlockState transformBlockState(BlockState original, int rotation, boolean xMirror, boolean zMirror) {
        Rotation rotationEnum = switch (rotation) {
            case 90 -> Rotation.COUNTERCLOCKWISE_90;
            case 180 -> Rotation.CLOCKWISE_180;
            case 270 -> Rotation.CLOCKWISE_90;
            default -> Rotation.NONE;
        };
        BlockState state = original.rotate(rotationEnum);

        if (xMirror && zMirror) {
            state = state.mirror(Mirror.LEFT_RIGHT).mirror(Mirror.FRONT_BACK);
        } else if (xMirror) {
            state = state.mirror(Mirror.FRONT_BACK);
        } else if (zMirror) {
            state = state.mirror(Mirror.LEFT_RIGHT);
        }
        return state;
    }

    /**
     * 获取下一个可用的字符
     */
    private static char getNextValidChar(char start) {
        char ch = start;
        while (ILLEGAL_CHARS.contains(ch)) ch++;
        return ch;
    }

    /**
     * 保存映射到 JSON 文件
     */
    private static void saveMappingToJson(Map<Character, BlockState> mapping, Path path) {
        try {
            if (path.getParent() != null) Files.createDirectories(path.getParent());

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(BlockState.class, new BlockStateTypeAdapter())
                    .setPrettyPrinting()
                    .create();

            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                gson.toJson(mapping, writer);
            }
        } catch (IOException e) {
            GTOCore.LOGGER.error("Failed to save mapping to {}", path, e);
        }
    }

    /**
     * 从数据包加载映射
     */
    static Char2ReferenceOpenHashMap<BlockState> loadMappingFromJson(ResourceLocation resLoc) {
        String resourcePath = String.format("assets/%s/%s", resLoc.getNamespace(), resLoc.getPath());
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(
                        PlatformCreators.class.getClassLoader().getResourceAsStream(resourcePath)),
                        StandardCharsets.UTF_8))) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(BlockState.class, new BlockStateTypeAdapter())
                    .create();
            Type type = new TypeToken<Char2ReferenceOpenHashMap<BlockState>>() {}.getType();
            return gson.fromJson(reader, type);
        } catch (Exception e) {
            GTOCore.LOGGER.error("Failed to load mapping from {}", resLoc, e);
            return new Char2ReferenceOpenHashMap<>();
        }
    }

    /**
     * BlockState JSON 适配器
     */
    private static class BlockStateTypeAdapter implements JsonSerializer<BlockState>, JsonDeserializer<BlockState> {

        @Override
        public JsonElement serialize(BlockState src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(src.getBlock());
            obj.addProperty("id", id.toString());

            JsonObject props = new JsonObject();
            src.getProperties().forEach(prop -> {
                props.addProperty(prop.getName(), getPropertyValue(src, prop));
            });
            obj.add("properties", props);
            return obj;
        }

        @Override
        public BlockState deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            Block block = BuiltInRegistries.BLOCK.get(RLUtils.parse(obj.get("id").getAsString()));
            if (block == Blocks.AIR) return Blocks.AIR.defaultBlockState();

            final BlockState[] state = { block.defaultBlockState() };
            JsonObject props = obj.getAsJsonObject("properties");

            props.entrySet().forEach(entry -> {
                Property<?> prop = block.getStateDefinition().getProperty(entry.getKey());
                if (prop != null) {
                    Optional<?> valueOpt = prop.getValue(entry.getValue().getAsString());
                    if (valueOpt.isPresent()) {
                        Object value = valueOpt.get();
                        @SuppressWarnings({ "rawtypes", "unchecked" })
                        BlockState newState = state[0].setValue((Property) prop, (Comparable) value);
                        state[0] = newState;
                    }
                }
            });

            return state[0];
        }

        private static <T extends Comparable<T>> String getPropertyValue(BlockState state, Property<T> prop) {
            return state.getValue(prop).toString();
        }
    }
}
