package com.gtocore.common.machine.mana;

import com.gtolib.GTOCore;
import com.gtolib.utils.RegistriesUtils;

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
                                            boolean xMirror, boolean zMirror, int rotation) {
        if (isExporting) {
            return;
        }
        isExporting = true;

        CompletableFuture.runAsync(() -> {
            try {
                exportStructure(level, pos1, pos2, xMirror, zMirror, rotation);
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
                                             boolean xMirror, boolean zMirror, int rotation) {
        exportStructureAsync(level, startPos, endPos, xMirror, zMirror, rotation);
    }

    /**
     * 导出结构和映射文件到 logs/platform/ 目录
     */
    public static void exportStructure(Level level, BlockPos pos1, BlockPos pos2,
                                       boolean xMirror, boolean zMirror, int rotation) {
        Path outputDir = Paths.get("logs", "platform");
        try {
            Files.createDirectories(outputDir);
        } catch (IOException e) {
            GTOCore.LOGGER.error("Exporting structure and mapping files failed");
            return;
        }

        // 生成唯一文件名（时间戳 + UUID）
        String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS").format(new Date());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String structureFile = outputDir.resolve(timestamp + "-" + uuid + ".txt").toString();
        String mappingFile = outputDir.resolve(timestamp + "-" + uuid + ".json").toString();

        int minX = Math.min(pos1.getX(), pos2.getX());
        int minY = Math.min(pos1.getY(), pos2.getY());
        int minZ = Math.min(pos1.getZ(), pos2.getZ());
        int maxX = Math.max(pos1.getX(), pos2.getX());
        int maxY = Math.max(pos1.getY(), pos2.getY());
        int maxZ = Math.max(pos1.getZ(), pos2.getZ());

        int dx = maxX - minX + 1;
        int dy = maxY - minY + 1;
        int dz = maxZ - minZ + 1;

        // 根据旋转调整尺寸
        boolean swapXZ = (rotation == 90 || rotation == 270);
        if (swapXZ) {
            int temp = dx;
            dx = dz;
            dz = temp;
        }

        // 旋转和镜像枚举
        Rotation rotationEnum = switch (rotation) {
            case 90 -> Rotation.CLOCKWISE_90;
            case 180 -> Rotation.CLOCKWISE_180;
            case 270 -> Rotation.COUNTERCLOCKWISE_90;
            default -> Rotation.NONE;
        };

        Mirror mirrorEnum = Mirror.NONE;
        Mirror mirrorEnum2 = Mirror.NONE;
        if (xMirror && zMirror) {
            mirrorEnum2 = Mirror.LEFT_RIGHT;
            mirrorEnum = Mirror.FRONT_BACK;
        } else if (xMirror) mirrorEnum = Mirror.FRONT_BACK;
        else if (zMirror) mirrorEnum = Mirror.LEFT_RIGHT;

        Map<BlockState, Character> stateToChar = new LinkedHashMap<>();
        Map<ResourceLocation, Integer> blockCount = new HashMap<>();
        Character nextChar = getNextValidChar('A');
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        BlockState air = Blocks.AIR.defaultBlockState();
        stateToChar.put(air, ' ');

        // 第一次遍历：收集旋转后的映射 + 计数
        for (int y = minY; y <= maxY; y++) {
            for (int z = minZ; z <= maxZ; z++) {
                for (int x = minX; x <= maxX; x++) {
                    mutablePos.set(x, y, z);
                    BlockState originalState = level.getBlockState(mutablePos);

                    // 应用旋转和镜像
                    BlockState transformedState = originalState.rotate(rotationEnum).mirror(mirrorEnum).mirror(mirrorEnum2);

                    ResourceLocation id = BuiltInRegistries.BLOCK.getKey(transformedState.getBlock());
                    blockCount.put(id, blockCount.getOrDefault(id, 0) + 1);

                    if (!stateToChar.containsKey(transformedState)) {
                        stateToChar.put(transformedState, nextChar);
                        nextChar = getNextValidChar((char) (nextChar + 1));
                    }
                }
            }
        }

        // 第二次遍历：写入结构文件（用旋转后的坐标和状态）
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(structureFile))) {
            writer.write(".size(" + dx + ", " + dy + ", " + dz + ")");
            writer.newLine();

            for (int outZ = 0; outZ < dz; outZ++) {
                List<String> ySlices = new ArrayList<>();
                for (int outY = 0; outY < dy; outY++) {
                    StringBuilder xChars = new StringBuilder();
                    for (int outX = 0; outX < dx; outX++) {

                        // 旋转/镜像坐标变换（与放置器完全一致）
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
                        BlockState transformedState = originalState.rotate(rotationEnum).mirror(mirrorEnum);

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

        // 保存映射（旋转后的状态）
        Map<Character, BlockState> charToState = new LinkedHashMap<>();
        stateToChar.forEach((state, ch) -> charToState.put(ch, state));
        saveMappingToJson(charToState, mappingFile);

        GTOCore.LOGGER.info("The structure and mapping files have been exported to:");
        GTOCore.LOGGER.info(" - Structure File: {}", structureFile);
        GTOCore.LOGGER.info(" - Mapping File: {}", mappingFile);
        GTOCore.LOGGER.info("Mapping size before save: {}", charToState.size());
        // 输出 .size 和 .extraMaterials
        List<String> defParts = new ArrayList<>();
        defParts.add(".size(" + dx + ", " + dy + ", " + dz + ")\n");
        blockCount.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e -> defParts.add(".extraMaterials(\"" + e.getKey() + "\", " + e.getValue() + ")\n"));
        GTOCore.LOGGER.info("[{}]", String.join(", ", defParts));
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
