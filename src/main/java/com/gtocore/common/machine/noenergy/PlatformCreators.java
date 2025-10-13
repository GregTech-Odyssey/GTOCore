package com.gtocore.common.machine.noenergy;

import com.gtocore.common.data.GTOBlocks;

import com.gtolib.GTOCore;
import com.gtolib.utils.RegistriesUtils;
import com.gtolib.utils.StringIndex;
import com.gtolib.utils.StringUtils;

import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
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
import it.unimi.dsi.fastutil.objects.Reference2CharOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;

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
        String patternFile = outputDir.resolve(timestamp + ".txt").toString();

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

        // 统计方块数量（按 Block）
        Reference2IntOpenHashMap<Block> blockCountByBlock = new Reference2IntOpenHashMap<>();
        int totalNonAir = 0;

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

                    Block block = transformedState.getBlock();
                    if (!transformedState.isAir()) {
                        blockCountByBlock.addTo(block, 1);
                        totalNonAir++;
                    }

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

        // 构建 patternFile 专用的按 Block 去重映射
        Reference2CharOpenHashMap<Block> blockToChar = new Reference2CharOpenHashMap<>();
        char currentChar = 'A';
        blockToChar.put(Blocks.AIR, ' ');

        for (BlockState state : stateToChar.keySet()) {
            Block block = state.getBlock();
            if (!blockToChar.containsKey(block)) {
                currentChar = getNextValidChar(currentChar);
                blockToChar.put(block, currentChar);
                currentChar = getNextValidChar((char) (currentChar + 1));
            }
        }

        // 生成 .block()/.where() 文件
        try (BufferedWriter patternWriter = Files.newBufferedWriter(Paths.get(patternFile), StandardCharsets.UTF_8)) {
            String chamberId = BuiltInRegistries.BLOCK.getKey(chamberBlock).toString();
            String[] chamberParts = StringUtils.decompose(chamberId);

            patternWriter.write(".block(" + convertBlockToString(chamberBlock, chamberId, chamberParts, true) + ")");
            patternWriter.newLine();
            patternWriter.write(".pattern(definition -> FactoryBlockPattern.start(definition)");
            patternWriter.newLine();

            // .aisle(...)
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

                        xChars.append(blockToChar.getOrDefault(transformedState.getBlock(), ' '));
                    }
                    ySlices.add("\"" + xChars + "\"");
                }
                patternWriter.write(".aisle(" + String.join(", ", ySlices) + ")");
                patternWriter.newLine();
            }

            // .where(...)
            for (var entry : blockToChar.reference2CharEntrySet()) {
                Block block = entry.getKey();
                Character ch = entry.getCharValue();
                if (block == Blocks.AIR) continue;

                if (BLOCK_MAP.containsKey(block)) {
                    patternWriter.write(".where('" + ch + "', ");
                    StringBuilder sb = new StringBuilder();
                    BLOCK_MAP.get(block).accept(sb, ch);
                    patternWriter.write(sb.toString());
                    patternWriter.newLine();
                    continue;
                }

                if (block == Blocks.COBBLESTONE) {
                    patternWriter.write(".where('" + ch + "', blocks(" + convertBlockToString(chamberBlock, chamberId, chamberParts, false) + ")");
                    if (laserMode) {
                        patternWriter.write(".or(GTOPredicates.autoLaserAbilities(definition.getRecipeTypes()))");
                    } else {
                        patternWriter.write(".or(autoAbilities(definition.getRecipeTypes()))");
                    }
                    patternWriter.newLine();
                    patternWriter.write(".or(abilities(MAINTENANCE).setExactLimit(1)))");
                    patternWriter.newLine();
                    continue;
                }

                String id = BuiltInRegistries.BLOCK.getKey(block).toString();
                String[] parts = StringUtils.decompose(id);
                boolean isGT = Objects.equals(parts[0], "gtceu");
                boolean isGTO = Objects.equals(parts[0], GTOCore.MOD_ID);

                if ((isGT || isGTO) && parts[1].contains("_frame")) {
                    patternWriter.write(".where('" + ch + "', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, " + (isGT ? "GTMaterials." : "GTOMaterials.") + FormattingUtil.lowerUnderscoreToUpperCamel(StringUtils.lastDecompose('_', parts[1])[0]) + ")))");
                    patternWriter.newLine();
                    continue;
                }

                patternWriter.write(".where('" + ch + "', blocks(" + convertBlockToString(block, id, parts, false) + "))");
                patternWriter.newLine();
            }

            patternWriter.write(".build())");
            patternWriter.newLine();
            patternWriter.newLine();

            // .extraMaterials
            blockCountByBlock.reference2IntEntrySet().stream()
                    .sorted(Map.Entry.comparingByKey(Comparator.comparing(BuiltInRegistries.BLOCK::getKey)))
                    .forEach(e -> {
                        try {
                            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(e.getKey());
                            patternWriter.write(".extraMaterials(\"" + id + "\", " + e.getIntValue() + ")");
                            patternWriter.newLine();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    });

            patternWriter.write("// Total non-air blocks: " + totalNonAir);
            patternWriter.newLine();

        } catch (IOException e) {
            GTOCore.LOGGER.error("Failed to save .block()/.where() pattern file", e);
        }

        GTOCore.LOGGER.info("Exported files:");
        GTOCore.LOGGER.info(" - Structure: {}", structureFile);
        GTOCore.LOGGER.info(" - Mapping: {}", mappingFile);
        GTOCore.LOGGER.info(" - Pattern: {}", patternFile);
    }

    // 工具方法：方块转代码字符串
    private static String convertBlockToString(Block b, String id, String[] parts, boolean supplier) {
        if (StringIndex.BLOCK_LINK_MAP.containsKey(b)) {
            return StringIndex.BLOCK_LINK_MAP.get(b) + (supplier ? "" : ".get()");
        }
        if (Objects.equals(parts[0], GTOCore.MOD_ID)) {
            return "Blocks." + parts[1].toUpperCase() + (supplier ? "" : ".get()");
        }
        if (Objects.equals(parts[0], "minecraft")) {
            return (supplier ? "() -> " : "") + "Blocks." + parts[1].toUpperCase();
        }
        return "RegistriesUtils.get" + (supplier ? "Supplier" : "") + "Block(\"" + id + "\")";
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
            ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
            Optional<Resource> optionalResource = resourceManager.getResource(resLoc);

            if (optionalResource.isPresent()) {
                Resource resource = optionalResource.get();
                try (Reader reader = new InputStreamReader(resource.open(), StandardCharsets.UTF_8)) {
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(BlockState.class, new BlockStateTypeAdapter())
                            .create();
                    Type type = new TypeToken<Char2ReferenceOpenHashMap<BlockState>>() {}.getType();
                    return gson.fromJson(reader, type);
                }
            }
            GTOCore.LOGGER.error("Resource not found: {}", resLoc);
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
