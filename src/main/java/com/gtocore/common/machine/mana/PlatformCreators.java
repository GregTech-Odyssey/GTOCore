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
     * 异步导出结构（防止重复执行）
     */
    public static void exportStructureAsync(Level level, BlockPos pos1, BlockPos pos2) {
        if (isExporting) {
            return;
        }
        isExporting = true;

        CompletableFuture.runAsync(() -> {
            try {
                exportStructure(level, pos1, pos2);
            } finally {
                isExporting = false;
            }
        });
    }

    /**
     * 导出结构和映射文件到 logs/platform/ 目录
     */
    public static void exportStructure(Level level, BlockPos pos1, BlockPos pos2) {
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

        Map<BlockState, Character> stateToChar = new LinkedHashMap<>();
        Character nextChar = getNextValidChar('A');
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        // 强制加入空气映射
        BlockState air = Blocks.AIR.defaultBlockState();
        stateToChar.put(air, ' ');

        // 第一次遍历：收集所有方块映射
        for (int y = minY; y <= maxY; y++) {
            for (int z = minZ; z <= maxZ; z++) {
                for (int x = minX; x <= maxX; x++) {
                    mutablePos.set(x, y, z);
                    BlockState state = level.getBlockState(mutablePos);
                    if (!stateToChar.containsKey(state)) {
                        stateToChar.put(state, nextChar);
                        nextChar = getNextValidChar((char) (nextChar + 1));
                    }
                }
            }
        }

        // 第二次遍历：写入结构文件（GT 风格 .aisle）
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(structureFile))) {
            writer.newLine();
            for (int z = minZ; z <= maxZ; z++) {
                List<String> ySlices = new ArrayList<>();
                for (int y = minY; y <= maxY; y++) {
                    StringBuilder xChars = new StringBuilder();
                    for (int x = minX; x <= maxX; x++) {
                        mutablePos.set(x, y, z);
                        BlockState state = level.getBlockState(mutablePos);
                        xChars.append(stateToChar.getOrDefault(state, ' '));
                    }
                    ySlices.add("\"" + xChars + "\"");
                }
                writer.write(".aisle(" + String.join(", ", ySlices) + ")");
                writer.newLine();
            }
        } catch (IOException e) {
            GTOCore.LOGGER.error("Failed to get structure");
        }

        Map<Character, BlockState> charToState = new LinkedHashMap<>();
        stateToChar.forEach((state, ch) -> charToState.put(ch, state));

        GTOCore.LOGGER.info("Mapping size before save: {}", charToState.size());

        saveMappingToJson(charToState, mappingFile);

        GTOCore.LOGGER.info("The structure and mapping files have been exported to:");
        GTOCore.LOGGER.info(" - Structure File: {}", structureFile);
        GTOCore.LOGGER.info(" - Mapping File: {}", mappingFile);
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
            GTOCore.LOGGER.error("Failed to save mapping to JSON file");
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
            GTOCore.LOGGER.error("Failed to load map from data pack");
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

    /**
     * 平台创建函数（异步）
     */
    public static void PlatformCreationAsync(Level level, BlockPos startPos, BlockPos endPos) {
        exportStructureAsync(level, startPos, endPos);
    }
}
