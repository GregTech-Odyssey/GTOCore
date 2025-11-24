package com.gtocore.common.machine.noenergy.PlatformDeployment;

import com.gtocore.common.data.GTOBlocks;

import com.gtolib.GTOCore;
import com.gtolib.utils.RegistriesUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.unimi.dsi.fastutil.chars.Char2ReferenceLinkedOpenHashMap;
import it.unimi.dsi.fastutil.chars.Char2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.chars.CharOpenHashSet;
import it.unimi.dsi.fastutil.objects.Reference2CharLinkedOpenHashMap;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

class PlatformCreators {

    // 非法字符集合
    private static final CharOpenHashSet ILLEGAL_CHARS;

    static {
        ILLEGAL_CHARS = new CharOpenHashSet();

        for (char c : new char[] {
                '.', '(', ')', ',', '/', '\\', '"', '\'', '`'
        }) {
            ILLEGAL_CHARS.add(c);
        }

        for (int i = 0; i <= Character.MAX_VALUE; i++) {
            char c = (char) i;
            if (Character.isISOControl(c)) {
                ILLEGAL_CHARS.add(c);
            }
        }

        for (char c : new char[] {
                '\u200B', '\u200C', '\u200D', '\u200E', '\u200F', '\u2028', '\u2029', '\u2060',
                '\u2061', '\u2062', '\u2063', '\u2064', '\uFFF9', '\uFFFA', '\uFFFB', '\uFEFF',
                '\u00A0', '\u2002', '\u2003', '\u2009', '\u200A', '\u00AD', '\u1680', '\u180E',
                '\u3000', '\u202F', '\u205F'
        }) {
            ILLEGAL_CHARS.add(c);
        }
    }

    private static volatile boolean isExporting = false;

    // BLOCK_MAP 特殊方块处理
    private static final Map<Block, BiConsumer<StringBuilder, Character>> BLOCK_MAP;

    static {
        BLOCK_MAP = ImmutableMap.<Block, BiConsumer<StringBuilder, Character>>builder()
                .put(Blocks.OAK_LOG, (b, c) -> b.append("controller(blocks(definition.get())))"))
                .put(Blocks.DIRT, (b, c) -> b.append("heatingCoils())"))
                .put(Blocks.WHITE_WOOL, (b, c) -> b.append("air())"))
                .put(Blocks.GLASS, (b, c) -> b.append("GTOPredicates.glass())"))
                .put(Blocks.GLOWSTONE, (b, c) -> b.append("GTOPredicates.light())"))
                .put(GTOBlocks.ABS_WHITE_CASING.get(), (b, c) -> b.append("GTOPredicates.absBlocks())"))
                .put(Blocks.FURNACE, (b, c) -> b.append("abilities(MUFFLER))"))
                .build();
    }

    /**
     * 异步导出结构（支持 XZ 平面镜像和绕 Y 轴旋转）
     */
    private static void exportStructureAsync(ServerLevel level, BlockPos pos1, BlockPos pos2,
                                             boolean xMirror, boolean zMirror, int rotation,
                                             Block chamberBlock, boolean laserMode) {
        if (isExporting) return;
        isExporting = true;
        CompletableFuture.runAsync(() -> {
            try {
                exportStructure(level, pos1, pos2, xMirror, zMirror, rotation, chamberBlock, laserMode);
            } finally {
                isExporting = false;
            }
        });
    }

    /**
     * 平台创建函数（异步）
     */
    static void PlatformCreationAsync(ServerLevel level, BlockPos startPos, BlockPos endPos,
                                      boolean xMirror, boolean zMirror, int rotation,
                                      Block chamberBlock, boolean laserMode) {
        exportStructureAsync(level, startPos, endPos, xMirror, zMirror, rotation, chamberBlock, laserMode);
    }

    /**
     * 导出结构和映射文件到 logs/platform/ 目录
     */
    private static void exportStructure(ServerLevel level, BlockPos pos1, BlockPos pos2,
                                        boolean xMirror, boolean zMirror, int rotation,
                                        Block chamberBlock, boolean laserMode) {
        Path outputDir = Paths.get("logs", "platform");
        try {
            Files.createDirectories(outputDir);
        } catch (IOException e) {
            GTOCore.LOGGER.error("Failed to create output directory", e);
            return;
        }

        GTOCore.LOGGER.info("Start exporting the structure");

        String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS").format(new Date());
        String structureFile = outputDir.resolve(timestamp).toString();
        String mappingFile = outputDir.resolve(timestamp + ".json").toString();

        int minX = Math.min(pos1.getX(), pos2.getX());
        int minY = Math.min(pos1.getY(), pos2.getY());
        int minZ = Math.min(pos1.getZ(), pos2.getZ());
        int maxX = Math.max(pos1.getX(), pos2.getX());
        int maxY = Math.max(pos1.getY(), pos2.getY());
        int maxZ = Math.max(pos1.getZ(), pos2.getZ());

        int dx = maxX - minX + 1;
        int dy = maxY - minY + 1;
        int dz = maxZ - minZ + 1;

        boolean swapXZ = (rotation == 90 || rotation == 270);
        if (swapXZ) {
            int temp = dx;
            dx = dz;
            dz = temp;
        }

        // 原始 BlockState -> Character 映射（不去重）
        Reference2CharLinkedOpenHashMap<BlockState> stateToChar = new Reference2CharLinkedOpenHashMap<>();
        char nextChar = getNextValidChar('A');
        BlockState air = Blocks.AIR.defaultBlockState();
        stateToChar.put(air, ' ');

        // 第一次遍历：收集旋转后的映射 & 统计方块数量
        for (int y = minY; y <= maxY; y++) {
            for (int z = minZ; z <= maxZ; z++) {
                for (int x = minX; x <= maxX; x++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    LevelChunk chunk = level.getChunkAt(pos);
                    int sectionIndex = chunk.getSectionIndex(y);
                    LevelChunkSection section = chunk.getSection(sectionIndex);

                    BlockState originalState = section.getBlockState(x & 15, y & 15, z & 15);
                    BlockState transformedState = transformBlockState(originalState, rotation, xMirror, zMirror);

                    if (!stateToChar.containsKey(transformedState)) {
                        stateToChar.put(transformedState, nextChar);
                        nextChar = getNextValidChar((char) (nextChar + 1));
                    }
                }
            }
        }

        // 写 structureFile
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(structureFile), StandardCharsets.UTF_8)) {
            writer.write(".size(" + dx + ", " + dy + ", " + dz + ")");
            writer.newLine();

            for (int outZ = 0; outZ < dz; outZ++) {
                List<String> ySlices = new ArrayList<>();
                for (int outY = 0; outY < dy; outY++) {
                    StringBuilder xChars = new StringBuilder();
                    for (int outX = 0; outX < dx; outX++) {
                        int rx = outX, rz = outZ;
                        switch (rotation) {
                            case 90 -> {
                                int t = rx;
                                rx = dz - 1 - rz;
                                rz = t;
                            }
                            case 180 -> {
                                rx = dx - 1 - rx;
                                rz = dz - 1 - rz;
                            }
                            case 270 -> {
                                int t = rx;
                                rx = rz;
                                rz = dx - 1 - t;
                            }
                        }
                        if (xMirror) rx = dx - 1 - rx;
                        if (zMirror) rz = dz - 1 - rz;

                        int worldX = minX + rx;
                        int worldY = minY + outY;
                        int worldZ = minZ + rz;

                        BlockPos pos = new BlockPos(worldX, worldY, worldZ);
                        LevelChunk chunk = level.getChunkAt(pos);
                        int sectionIndex = chunk.getSectionIndex(worldY);
                        LevelChunkSection section = chunk.getSection(sectionIndex);
                        BlockState originalState = section.getBlockState(worldX & 15, worldY & 15, worldZ & 15);

                        BlockState transformedState = transformBlockState(originalState, rotation, xMirror, zMirror);

                        xChars.append(stateToChar.getOrDefault(transformedState, ' '));
                    }
                    ySlices.add("\"" + xChars + "\"");
                }
                writer.write(".aisle(" + String.join(", ", ySlices) + ")");
                writer.newLine();
            }
        } catch (IOException e) {
            GTOCore.LOGGER.error("Failed to write structure file", e);
        }

        // 写 mappingFile
        Char2ReferenceLinkedOpenHashMap<BlockState> charToState = new Char2ReferenceLinkedOpenHashMap<>();
        stateToChar.reference2CharEntrySet().fastForEach(e -> charToState.put(e.getCharValue(), e.getKey()));
        saveMappingToJson(charToState, mappingFile);

        GTOCore.LOGGER.info("Exported files:");
        GTOCore.LOGGER.info(" - Structure: {}", structureFile);
        GTOCore.LOGGER.info(" - Mapping: {}", mappingFile);
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
            state = state.mirror(Mirror.LEFT_RIGHT);
            state = state.mirror(Mirror.FRONT_BACK);
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
        while (ILLEGAL_CHARS.contains(ch)) {
            ch++;
        }
        return ch;
    }

    /**
     * 保存映射到 JSON 文件
     */
    private static void saveMappingToJson(Map<Character, BlockState> mapping, String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (path.getParent() != null) Files.createDirectories(path.getParent());

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(BlockState.class, new BlockStateTypeAdapter())
                    .setPrettyPrinting()
                    .create();

            try (FileWriter writer = new FileWriter(path.toFile(), StandardCharsets.UTF_8)) {
                gson.toJson(mapping, writer);
            }
        } catch (IOException e) {
            GTOCore.LOGGER.error("Failed to save mapping to JSON file", e);
        }
    }

    /**
     * 从数据包加载映射
     */
    static Char2ReferenceOpenHashMap<BlockState> loadMappingFromJson(ResourceLocation resLoc) {
        try {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(PlatformBlockType.class.getClassLoader()
                    .getResourceAsStream("assets/" + resLoc.toString().replace(":", "/"))), StandardCharsets.UTF_8))) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(BlockState.class, new BlockStateTypeAdapter())
                        .create();
                Type type = new TypeToken<Char2ReferenceOpenHashMap<BlockState>>() {}.getType();
                return gson.fromJson(reader, type);
            } catch (Exception e) {
                GTOCore.LOGGER.error("Resource not found: {}", resLoc);
            }
        } catch (Exception e) {
            GTOCore.LOGGER.error("Failed to load mapping from resource", e);
        }
        return new Char2ReferenceOpenHashMap<>();
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
            for (Property<?> prop : src.getProperties()) {
                props.addProperty(prop.getName(), getPropertyValue(src, prop));
            }
            obj.add("properties", props);
            return obj;
        }

        @Override
        public BlockState deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            Block block = RegistriesUtils.getBlock(obj.get("id").getAsString());

            BlockState state = block.defaultBlockState();
            JsonObject props = obj.getAsJsonObject("properties");

            for (Map.Entry<String, JsonElement> entry : props.entrySet()) {
                Property<?> prop = getPropertyByName(block, entry.getKey());
                if (prop != null) {
                    String valueStr = entry.getValue().getAsString();
                    Optional<?> value = prop.getValue(valueStr);
                    if (value.isPresent()) {
                        state = setPropertyValue(state, prop, (Comparable<?>) value.get());
                    }
                }
            }
            return state;
        }

        @SuppressWarnings("unchecked")
        private static <T extends Comparable<T>> BlockState setPropertyValue(BlockState state, Property<?> prop, Comparable<?> value) {
            return state.setValue((Property<T>) prop, (T) value);
        }

        private static String getPropertyValue(BlockState state, Property<?> prop) {
            return getPropertyValueRaw(state, prop).toString();
        }

        @SuppressWarnings("unchecked")
        private static <T extends Comparable<T>> T getPropertyValueRaw(BlockState state, Property<?> prop) {
            return state.getValue((Property<T>) prop);
        }

        private static Property<?> getPropertyByName(Block block, String name) {
            for (Property<?> prop : block.getStateDefinition().getProperties()) {
                if (prop.getName().equals(name)) {
                    return prop;
                }
            }
            return null;
        }
    }
}
