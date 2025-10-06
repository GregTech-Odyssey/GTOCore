package com.gtocore.common.machine.mana;

import com.gtolib.GTOCore;
import com.gtolib.utils.RegistriesUtils;
import com.gtolib.utils.StringIndex;
import com.gtolib.utils.StringUtils;

import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class PlatformCreators {

    private static final Set<Character> ILLEGAL_CHARS = new HashSet<>(Arrays.asList('.', '(', ')', ',', '/', '\\'));
    private static volatile boolean isExporting = false;

    /**
     * 异步导出结构（支持 XZ 平面镜像和绕 Y 轴旋转）
     */
    public static void exportStructureAsync(Level level, BlockPos pos1, BlockPos pos2,
                                            boolean xMirror, boolean zMirror, int rotation,
                                            Block chamberBlock, boolean laserMode) {
        if (isExporting) {
            return;
        }
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
     *
     * @param xMirror  X轴对称（左右）
     * @param zMirror  Z轴对称（前后）
     * @param rotation 旋转角度 0/90/180/270（绕Y轴）
     */
    public static void PlatformCreationAsync(Level level, BlockPos startPos, BlockPos endPos,
                                             boolean xMirror, boolean zMirror, int rotation,
                                             Block chamberBlock, boolean laserMode) {
        exportStructureAsync(level, startPos, endPos, xMirror, zMirror, rotation, chamberBlock, laserMode);
    }

    /**
     * 导出结构和映射文件到 logs/platform/ 目录
     */
    public static void exportStructure(Level level, BlockPos pos1, BlockPos pos2,
                                       boolean xMirror, boolean zMirror, int rotation,
                                       Block chamberBlock, boolean laserMode) {
        Path outputDir = Paths.get("logs", "platform");
        try {
            Files.createDirectories(outputDir);
        } catch (IOException e) {
            GTOCore.LOGGER.error("Exporting structure and mapping files failed");
            return;
        }

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

        Map<BlockState, Character> stateToChar = new LinkedHashMap<>();
        Map<ResourceLocation, Integer> blockCount = new HashMap<>();
        Character nextChar = getNextValidChar('A');
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        BlockState air = Blocks.AIR.defaultBlockState();
        stateToChar.put(air, ' ');

        // 第一次遍历：收集旋转后的映射
        for (int y = minY; y <= maxY; y++) {
            for (int z = minZ; z <= maxZ; z++) {
                for (int x = minX; x <= maxX; x++) {
                    mutablePos.set(x, y, z);
                    BlockState originalState = level.getBlockState(mutablePos);
                    BlockState transformedState = transformBlockState(originalState, rotation, xMirror, zMirror);
                    ResourceLocation id = BuiltInRegistries.BLOCK.getKey(transformedState.getBlock());
                    blockCount.put(id, blockCount.getOrDefault(id, 0) + 1);

                    if (!stateToChar.containsKey(transformedState)) {
                        stateToChar.put(transformedState, nextChar);
                        nextChar = getNextValidChar((char) (nextChar + 1));
                    }
                }
            }
        }

        // 第二次遍历：写入结构文件
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(structureFile))) {
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

                        BlockState originalState = level.getBlockState(new BlockPos(worldX, worldY, worldZ));
                        BlockState transformedState = transformBlockState(originalState, rotation, xMirror, zMirror);

                        xChars.append(stateToChar.getOrDefault(transformedState, ' '));
                    }
                    ySlices.add("\"" + xChars + "\"");
                }
                writer.write(".aisle(" + String.join(", ", ySlices) + ")");
                writer.newLine();
            }
        } catch (IOException e) {
            GTOCore.LOGGER.error("Failed to get structure", e);
        }

        // 保存映射文件
        Map<Character, BlockState> charToState = new LinkedHashMap<>();
        stateToChar.forEach((state, ch) -> charToState.put(ch, state));
        saveMappingToJson(charToState, mappingFile);

        // 生成 .block()/.where() 文件
        try (BufferedWriter patternWriter = Files.newBufferedWriter(Paths.get(patternFile))) {
            String chamberId = BuiltInRegistries.BLOCK.getKey(chamberBlock).toString();
            String[] chamberParts = StringUtils.decompose(chamberId);

            patternWriter.write(".block(" + convertBlockToString(chamberBlock, chamberId, chamberParts, true) + ")");
            patternWriter.newLine();
            patternWriter.write(".pattern(definition -> FactoryBlockPattern.start(definition)");
            patternWriter.newLine();

            // 写入 .aisle(...)
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

                        BlockState originalState = level.getBlockState(new BlockPos(worldX, worldY, worldZ));
                        BlockState transformedState = transformBlockState(originalState, rotation, xMirror, zMirror);

                        xChars.append(stateToChar.getOrDefault(transformedState, ' '));
                    }
                    ySlices.add("\"" + xChars + "\"");
                }
                patternWriter.write(".aisle(" + String.join(", ", ySlices) + ")");
                patternWriter.newLine();
            }

            // 写入 .where(...)
            for (Map.Entry<Character, BlockState> entry : charToState.entrySet()) {
                Character ch = entry.getKey();
                BlockState state = entry.getValue();
                if (ch == ' ') continue;

                Block block = state.getBlock();
                if (block == Blocks.COBBLESTONE) {
                    patternWriter.write(".where('" + ch + "', blocks(" + convertBlockToString(chamberBlock, chamberId, chamberParts, false) + ")");
                    if (laserMode) {
                        patternWriter.write(".or(GTOPredicates.autoLaserAbilities(definition.getRecipeTypes()))");
                        patternWriter.newLine();
                        patternWriter.write(".or(abilities(MAINTENANCE).setExactLimit(1)))");
                    } else {
                        patternWriter.write(".or(autoAbilities(definition.getRecipeTypes()))");
                        patternWriter.newLine();
                        patternWriter.write(".or(abilities(MAINTENANCE).setExactLimit(1)))");
                    }
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

            patternWriter.write("Mapping size before save: " + charToState.size());
            patternWriter.write(".size(" + dx + ", " + dy + ", " + dz + ")");
            blockCount.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(e -> {
                        try {
                            patternWriter.write(".extraMaterials(\"" + e.getKey() + "\", " + e.getValue() + ")\n");
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
            patternWriter.newLine();

        } catch (IOException e) {
            GTOCore.LOGGER.error("Failed to save .block()/.where() pattern file", e);
        }

        GTOCore.LOGGER.info("The structure and mapping files have been exported to:");
        GTOCore.LOGGER.info(" - Structure File: {}", structureFile);
        GTOCore.LOGGER.info(" - Mapping File: {}", mappingFile);
        GTOCore.LOGGER.info(" - Pattern File: {}", patternFile);
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
     * 统一的方块状态旋转/镜像处理（与 PlatformStructurePlacer 一致）
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
    private static Character getNextValidChar(char start) {
        Character ch = start;
        while (ILLEGAL_CHARS.contains(ch)) {
            ch++;
        }
        return ch;
    }

    /**
     * 保存映射到 JSON 文件
     */
    public static void saveMappingToJson(Map<Character, BlockState> mapping, String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (path.getParent() != null) Files.createDirectories(path.getParent());

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(BlockState.class, new BlockStateTypeAdapter())
                    .setPrettyPrinting()
                    .create();

            try (FileWriter writer = new FileWriter(path.toFile())) {
                gson.toJson(mapping, writer);
            }
        } catch (IOException e) {
            GTOCore.LOGGER.error("Failed to save mapping to JSON file", e);
        }
    }

    /**
     * 从数据包加载映射
     */
    public static Map<Character, BlockState> loadMappingFromJson(ResourceLocation resLoc) {
        try {
            ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
            Optional<Resource> optionalResource = resourceManager.getResource(resLoc);

            if (optionalResource.isPresent()) {
                Resource resource = optionalResource.get();
                try (Reader reader = new InputStreamReader(resource.open())) {
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(BlockState.class, new BlockStateTypeAdapter())
                            .create();
                    Type type = new TypeToken<Map<Character, BlockState>>() {}.getType();
                    return gson.fromJson(reader, type);
                }
            } else {
                GTOCore.LOGGER.error("Unable to find resource: {}", resLoc);
                return new HashMap<>();
            }
        } catch (Exception e) {
            GTOCore.LOGGER.error("Failed to load map from data pack", e);
            return new HashMap<>();
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
            for (Property<?> prop : src.getProperties()) {
                props.addProperty(prop.getName(), getPropertyValue(src, prop));
            }
            obj.add("properties", props);
            return obj;
        }

        @Override
        public BlockState deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                                                                                                          throws JsonParseException {
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
