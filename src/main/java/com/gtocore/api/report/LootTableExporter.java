package com.gtocore.api.report;

import com.gtolib.GTOCore;
import com.gtolib.utils.RegistriesUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraftforge.server.ServerLifecycleHooks;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class LootTableExporter {

    private static final ThreadLocal<SimpleDateFormat> DATE_FORMATTER = ThreadLocal.withInitial(
            () -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    private static final ThreadLocal<SimpleDateFormat> TIMESTAMP_FORMATTER = ThreadLocal.withInitial(
            () -> new SimpleDateFormat("yyyyMMdd-HHmmss"));

    private static final DecimalFormat PERCENT_FORMAT = new DecimalFormat("#.##%");
    private static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("#.##");

    /**
     * 导出所有命名空间下的所有战利品表到Markdown文件
     */
    public static void exportAllLootTables() {
        List<String> allLootTables = getAllLootTableLocations();
        exportLootTables(allLootTables);
    }

    /**
     * 导出指定的战利品表列表到Markdown文件
     */
    public static void exportLootTables(List<String> lootTables) {
        if (lootTables == null || lootTables.isEmpty()) {
            GTOCore.LOGGER.warn("没有指定要导出的战利品表或没有找到任何战利品表");
            return;
        }

        long startTime = System.nanoTime();
        exportAllLootTablesToMarkdown(lootTables);
        long endTime = System.nanoTime();

        long durationMs = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        GTOCore.LOGGER.info("战利品表导出完成，共处理 {} 个战利品表，耗时 {} ms", lootTables.size(), durationMs);
    }

    /**
     * 获取所有可用的战利品表位置（所有命名空间）
     */
    public static List<String> getAllLootTableLocations() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            GTOCore.LOGGER.error("无法获取服务器实例 - 可能不在服务端环境或服务器未启动");
            return Collections.emptyList();
        }

        ResourceManager resourceManager = server.getResourceManager();
        Set<ResourceLocation> allResources = resourceManager.listResources("loot_tables",
                path -> path.toString().endsWith(".json")).keySet();

        return allResources.stream()
                .map(location -> {
                    String path = location.getPath();
                    String lootTablePath = path.substring("loot_tables/".length(), path.length() - ".json".length());
                    return location.getNamespace() + ":" + lootTablePath;
                })
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * 导出所有战利品表到Markdown文件
     */
    private static void exportAllLootTablesToMarkdown(List<String> lootTables) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            GTOCore.LOGGER.error("无法获取服务器实例 - 可能不在服务端环境或服务器未启动");
            return;
        }

        try {
            Map<String, LootTableAnalysis> allLootTables = new LinkedHashMap<>();

            Map<String, List<String>> lootTablesByNamespace = lootTables.stream()
                    .collect(Collectors.groupingBy(
                            tableName -> new ResourceLocation(tableName).getNamespace(),
                            TreeMap::new, Collectors.toList()));

            for (Map.Entry<String, List<String>> entry : lootTablesByNamespace.entrySet()) {
                String namespace = entry.getKey();
                List<String> namespaceTables = entry.getValue();

                GTOCore.LOGGER.info("正在处理命名空间 {} 下的 {} 个战利品表", namespace, namespaceTables.size());

                for (String tableName : namespaceTables) {
                    processLootTable(server, tableName, allLootTables);
                }
            }

            String markdown = generateCompleteMarkdown(allLootTables, lootTablesByNamespace);
            Path logDir = Paths.get("logs", "report");
            Files.createDirectories(logDir);

            String timestamp = TIMESTAMP_FORMATTER.get().format(new Date());
            String randomNumber = String.format("%08d", (int) (Math.random() * 100000000));
            Path reportPath = logDir.resolve("loottable_analysis_" + timestamp + "_" + randomNumber + ".md");

            try (BufferedWriter writer = Files.newBufferedWriter(reportPath)) {
                writer.write(markdown);
                GTOCore.LOGGER.info("战利品表分析已导出到: {}", reportPath.toAbsolutePath());
            }

        } catch (IOException e) {
            GTOCore.LOGGER.error("导出战利品表时出错: {}", e.getMessage(), e);
        }
    }

    /**
     * 处理单个战利品表
     */
    private static void processLootTable(MinecraftServer server, String tableName,
                                         Map<String, LootTableAnalysis> allLootTables) {
        if (!ResourceLocation.isValidResourceLocation(tableName)) {
            GTOCore.LOGGER.error("无效的战利品表名称格式: {}", tableName);
            return;
        }

        ResourceLocation lootTableLocation = new ResourceLocation(tableName);
        LootTableAnalysis analysis = new LootTableAnalysis(tableName);

        try {
            ResourceManager resourceManager = server.getResourceManager();
            ResourceLocation jsonLocation = new ResourceLocation(
                    lootTableLocation.getNamespace(),
                    "loot_tables/" + lootTableLocation.getPath() + ".json");

            Optional<Resource> resourceOpt = resourceManager.getResource(jsonLocation);
            if (resourceOpt.isEmpty()) {
                GTOCore.LOGGER.error("找不到战利品表JSON: {}", jsonLocation);
                analysis.setError("找不到战利品表JSON资源");
                allLootTables.put(tableName, analysis);
                return;
            }

            Resource resource = resourceOpt.get();
            String jsonText;
            try (InputStream inputStream = resource.open()) {
                jsonText = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            }

            JsonElement jsonElement = JsonParser.parseString(jsonText);
            if (!jsonElement.isJsonObject()) {
                GTOCore.LOGGER.error("战利品表JSON不是有效的对象: {}", jsonLocation);
                analysis.setError("战利品表JSON格式错误");
                allLootTables.put(tableName, analysis);
            }

            JsonObject lootTableJson = jsonElement.getAsJsonObject();
            analysis.setType(lootTableJson.has("type") ? lootTableJson.get("type").getAsString() : "通用");

            List<String> globalConditions = new ArrayList<>();
            if (lootTableJson.has("conditions") && lootTableJson.get("conditions").isJsonArray()) {
                for (JsonElement condElem : lootTableJson.getAsJsonArray("conditions")) {
                    if (condElem.isJsonObject()) {
                        globalConditions.add(parseCondition(condElem.getAsJsonObject()));
                    } else {
                        GTOCore.LOGGER.warn("战利品表{}中发现非对象类型的条件", tableName);
                    }
                }
            }
            analysis.setGlobalConditions(globalConditions);

            List<LootPool> lootPools = processLootTable(lootTableJson);
            analysis.setLootPools(lootPools);

            allLootTables.put(tableName, analysis);

        } catch (IOException e) {
            String errorMsg = "处理战利品表 " + tableName + " 时出错: " + e.getMessage();
            GTOCore.LOGGER.error(errorMsg, e);
            analysis.setError(errorMsg);
            allLootTables.put(tableName, analysis);
        } catch (Exception e) {
            String errorMsg = "分析战利品表 " + tableName + " 时发生意外错误: " + e.getMessage();
            GTOCore.LOGGER.error(errorMsg, e);
            analysis.setError(errorMsg);
            allLootTables.put(tableName, analysis);
        }
    }

    /**
     * 处理战利品表JSON数据
     */
    private static List<LootPool> processLootTable(JsonObject lootTableJson) {
        List<LootPool> lootPools = new ArrayList<>();

        if (lootTableJson.has("pools") && lootTableJson.get("pools").isJsonArray()) {
            int poolIndex = 1;
            for (JsonElement poolElement : lootTableJson.getAsJsonArray("pools")) {
                if (!poolElement.isJsonObject()) {
                    GTOCore.LOGGER.warn("发现非对象类型的奖励池");
                    continue;
                }

                JsonObject pool = poolElement.getAsJsonObject();
                LootPool lootPool = new LootPool();
                lootPool.poolIndex = poolIndex;
                lootPool.name = pool.has("name") ? pool.get("name").getAsString() : "-";

                // 解析池级别的函数
                if (pool.has("functions") && pool.get("functions").isJsonArray()) {
                    for (JsonElement functionElement : pool.getAsJsonArray("functions")) {
                        if (functionElement.isJsonObject()) {
                            lootPool.functions.add(functionElement.getAsJsonObject());
                        }
                    }
                }

                if (pool.has("rolls")) {
                    lootPool.rolls = getValueInfo(pool.get("rolls"));
                } else {
                    ValueInfo defaultRolls = new ValueInfo();
                    defaultRolls.average = 1.0;
                    defaultRolls.detail = "1";
                    lootPool.rolls = defaultRolls;
                    GTOCore.LOGGER.debug("奖励池{}未指定rolls，使用默认值1", poolIndex);
                }

                if (pool.has("bonus_rolls")) {
                    lootPool.bonusRolls = getValueInfo(pool.get("bonus_rolls"));
                }

                if (pool.has("conditions") && pool.get("conditions").isJsonArray()) {
                    for (JsonElement condElem : pool.getAsJsonArray("conditions")) {
                        if (condElem.isJsonObject()) {
                            lootPool.conditions.add(parseCondition(condElem.getAsJsonObject()));
                        } else {
                            GTOCore.LOGGER.warn("奖励池{}中发现非对象类型的条件", poolIndex);
                        }
                    }
                }

                if (pool.has("entries") && pool.get("entries").isJsonArray()) {
                    for (JsonElement entryElement : pool.getAsJsonArray("entries")) {
                        if (entryElement.isJsonObject()) {
                            processLootEntry(entryElement.getAsJsonObject(), lootPool);
                        } else {
                            GTOCore.LOGGER.warn("奖励池{}中发现非对象类型的条目", poolIndex);
                        }
                    }
                }

                applyPoolFunctionsToItems(lootPool);

                calculatePoolStats(lootPool);
                lootPools.add(lootPool);
                poolIndex++;
            }
        } else {
            GTOCore.LOGGER.debug("战利品表不包含pools数组或pools不是数组类型");
        }

        return lootPools;
    }

    /**
     * 将池级别的函数应用到所有物品条目
     */
    private static void applyPoolFunctionsToItems(LootPool lootPool) {
        if (lootPool.lootItems == null || lootPool.functions == null) {
            return;
        }

        for (LootItem item : lootPool.lootItems) {
            for (JsonObject function : lootPool.functions) {
                processFunction(function, item, true);
            }
        }
    }

    /**
     * 处理单个战利品条目
     */
    private static void processLootEntry(JsonObject entry, LootPool lootPool) {
        if (entry == null) {
            GTOCore.LOGGER.warn("处理空的战利品条目");
            return;
        }

        if (!entry.has("type")) {
            GTOCore.LOGGER.warn("发现没有类型的战利品条目");
            return;
        }

        String entryType = entry.get("type").getAsString();

        if ("minecraft:item".equals(entryType)) {
            processItemEntry(entry, lootPool);
        } else if ("minecraft:tag".equals(entryType)) {
            processTagEntry(entry, lootPool);
        } else if ("minecraft:loot_table".equals(entryType)) {
            processLootTableEntry(entry, lootPool);
        } else if ("minecraft:empty".equals(entryType)) {
            processEmptyEntry(entry, lootPool);
        } else if ("minecraft:group".equals(entryType)) {
            processGroupEntry(entry, lootPool);
        } else {
            GTOCore.LOGGER.debug("未处理的战利品条目类型: {}", entryType);
            LootItem lootItem = new LootItem();
            lootItem.type = "未处理类型";
            lootItem.itemId = entryType;
            lootItem.displayName = "未处理: " + entryType;
            lootItem.weight = entry.has("weight") ? entry.get("weight").getAsDouble() : 1.0;
            lootPool.lootItems.add(lootItem);
        }
    }

    /**
     * 处理物品类型条目
     */
    private static void processItemEntry(JsonObject entry, LootPool lootPool) {
        LootItem lootItem = new LootItem();
        lootItem.type = "物品";

        if (entry.has("name")) {
            lootItem.itemId = entry.get("name").getAsString();
            lootItem.displayName = getItemTranslation(lootItem.itemId);
        } else {
            GTOCore.LOGGER.warn("物品条目缺少name属性");
            lootItem.itemId = "unknown";
            lootItem.displayName = "未知物品";
        }

        lootItem.weight = entry.has("weight") ? entry.get("weight").getAsDouble() : 1.0;
        lootItem.quality = entry.has("quality") ? entry.get("quality").getAsDouble() : 0.0;

        if (entry.has("conditions") && entry.get("conditions").isJsonArray()) {
            for (JsonElement condElem : entry.getAsJsonArray("conditions")) {
                if (condElem.isJsonObject()) {
                    lootItem.conditions.add(parseCondition(condElem.getAsJsonObject()));
                    lootItem.conditionFactor *= 0.7;
                }
            }
        }

        if (entry.has("functions") && entry.get("functions").isJsonArray()) {
            for (JsonElement functionElement : entry.getAsJsonArray("functions")) {
                if (functionElement.isJsonObject()) {
                    processFunction(functionElement.getAsJsonObject(), lootItem, false);
                }
            }
        }

        lootPool.lootItems.add(lootItem);
    }

    /**
     * 处理函数 - 支持更多常用函数类型
     */
    private static void processFunction(JsonObject function, LootItem lootItem, boolean isPoolFunction) {
        if (function == null || lootItem == null || !function.has("function")) {
            return;
        }

        String functionType = function.get("function").getAsString();
        String functionSource = isPoolFunction ? "池级别" : "条目级别";
        lootItem.functions.add(functionSource + ":" + functionType);

        if ("minecraft:set_count".equals(functionType)) {
            boolean isAdd = function.has("add") && function.get("add").getAsBoolean();
            lootItem.countOperation = isAdd ? "添加" : "设置";

            if (function.has("count")) {
                ValueInfo countInfo = getValueInfo(function.get("count"));
                if (isAdd) {
                    lootItem.count += countInfo.average;
                    lootItem.countDetail = (lootItem.countDetail != null ? lootItem.countDetail + "+" : "") + countInfo.detail;
                } else {
                    lootItem.count = countInfo.average;
                    lootItem.countDetail = countInfo.detail;
                }

                lootItem.displayName += (isPoolFunction ? " (池" : " (") + lootItem.countOperation +
                        "数量: " + countInfo.detail + ")";
            }
        } else if ("minecraft:add_count".equals(functionType)) {
            lootItem.countOperation = "添加";
            if (function.has("count")) {
                ValueInfo countInfo = getValueInfo(function.get("count"));
                lootItem.count += countInfo.average;
                lootItem.countDetail = (lootItem.countDetail != null ? lootItem.countDetail + "+" : "") + countInfo.detail;
                lootItem.displayName += (isPoolFunction ? " (池添加数量: " : " (添加数量: ") + countInfo.detail + ")";
            }
        } else if ("minecraft:looting_enchant".equals(functionType)) {
            lootItem.lootingAffected = true;
            if (function.has("count")) {
                ValueInfo countInfo = getValueInfo(function.get("count"));
                lootItem.lootingCount = countInfo.average;
                lootItem.displayName += (isPoolFunction ? " (池掠夺加成: " : " (掠夺加成: ") + countInfo.detail + "每级)";
            }
            if (function.has("limit")) {
                lootItem.lootingLimit = function.get("limit").getAsInt();
                lootItem.displayName += " (上限: " + lootItem.lootingLimit + ")";
            }
        } else if ("minecraft:enchant_randomly".equals(functionType)) {
            lootItem.enchanted = true;
            if (function.has("enchantments")) {
                lootItem.enchantments = function.getAsJsonArray("enchantments").toString()
                        .replace("[", "").replace("]", "").replace("\"", "");
                lootItem.displayName += (isPoolFunction ? " (池随机附魔: " : " (随机附魔: ") + lootItem.enchantments + ")";
            } else {
                lootItem.displayName += (isPoolFunction ? " (池随机附魔)" : " (随机附魔)");
            }
        } else if ("minecraft:enchant_with_levels".equals(functionType)) {
            lootItem.enchanted = true;
            lootItem.enchantWithLevels = true;
            if (function.has("levels")) {
                ValueInfo levelsInfo = getValueInfo(function.get("levels"));
                lootItem.displayName += (isPoolFunction ? " (池等级附魔: " : " (等级附魔: ") + levelsInfo.detail + "级)";
            }
            if (function.has("treasure") && function.get("treasure").getAsBoolean()) {
                lootItem.treasureEnchant = true;
                lootItem.displayName += " (包含宝藏附魔)";
            }
        } else if ("minecraft:potion".equals(functionType)) {
            lootItem.hasPotion = true;
            if (function.has("potion")) {
                lootItem.potionType = function.get("potion").getAsString();
                lootItem.displayName += (isPoolFunction ? " (池药水: " : " (药水: ") + lootItem.potionType + ")";
            } else {
                lootItem.displayName += (isPoolFunction ? " (池随机药水)" : " (随机药水)");
            }
        } else if ("minecraft:smelt".equals(functionType)) {
            lootItem.smelted = true;
            lootItem.displayName += (isPoolFunction ? " (池已冶炼)" : " (已冶炼)");
        } else if ("minecraft:set_damage".equals(functionType)) {
            lootItem.hasDamage = true;
            if (function.has("damage")) {
                ValueInfo damageInfo = getValueInfo(function.get("damage"));
                lootItem.damage = damageInfo.average;
                lootItem.displayName += (isPoolFunction ? " (池损伤: " : " (损伤: ") + damageInfo.detail + ")";
            }
        } else if ("minecraft:set_nbt".equals(functionType)) {
            lootItem.hasNbt = true;
            lootItem.displayName += (isPoolFunction ? " (池有NBT数据)" : " (有NBT数据)");
        } else if ("minecraft:fill_player_head".equals(functionType)) {
            lootItem.hasPlayerHeadData = true;
            lootItem.displayName += (isPoolFunction ? " (池填充玩家头颅数据)" : " (填充玩家头颅数据)");
        } else if ("minecraft:entity装备".equals(functionType)) {
            lootItem.isEntityEquipment = true;
            lootItem.displayName += (isPoolFunction ? " (池实体装备)" : " (实体装备)");
        } else if ("minecraft:copy_name".equals(functionType)) {
            lootItem.copyName = true;
            String source = function.has("source") ? function.get("source").getAsString() : "实体";
            lootItem.displayName += (isPoolFunction ? " (池复制" : " (复制") + source + "名称)";
        } else if ("minecraft:copy_nbt".equals(functionType)) {
            lootItem.copyNbt = true;
            lootItem.displayName += (isPoolFunction ? " (池复制NBT)" : " (复制NBT)");
        } else if ("minecraft:limit_count".equals(functionType)) {
            lootItem.limitCount = true;
            if (function.has("limit")) {
                int limit = function.get("limit").getAsInt();
                lootItem.displayName += (isPoolFunction ? " (池数量上限: " : " (数量上限: ") + limit + ")";
            }
        } else if ("minecraft:set_attributes".equals(functionType)) {
            lootItem.hasAttributes = true;
            lootItem.displayName += (isPoolFunction ? " (池设置属性)" : " (设置属性)");
        } else if ("minecraft:set_book_contents".equals(functionType)) {
            lootItem.hasBookContents = true;
            lootItem.displayName += (isPoolFunction ? " (池设置书本内容)" : " (设置书本内容)");
        } else if ("minecraft:set_lore".equals(functionType)) {
            lootItem.hasLore = true;
            lootItem.displayName += (isPoolFunction ? " (池设置 Lore)" : " (设置 Lore)");
        } else if ("minecraft:set_name".equals(functionType)) {
            lootItem.hasCustomName = true;
            lootItem.displayName += (isPoolFunction ? " (池自定义名称)" : " (自定义名称)");
        } else if ("minecraft:explosion_decay".equals(functionType)) {
            lootItem.explosionDecay = true;
            lootItem.displayName += (isPoolFunction ? " (池爆炸衰减)" : " (爆炸衰减)");
        } else {
            GTOCore.LOGGER.debug("未处理的{}函数类型: {}", functionSource, functionType);
            lootItem.otherFunctions.add(functionSource + ":" + functionType);
        }
    }

    private static void processTagEntry(JsonObject entry, LootPool lootPool) {
        if (entry == null || lootPool == null) {
            GTOCore.LOGGER.warn("处理空的标签条目或奖励池");
            return;
        }

        LootItem lootItem = new LootItem();
        lootItem.type = "标签";

        if (entry.has("name")) {
            lootItem.itemId = entry.get("name").getAsString();
            lootItem.displayName = "标签: " + lootItem.itemId;
        } else {
            GTOCore.LOGGER.warn("标签条目缺少name属性");
            lootItem.itemId = "unknown:tag";
            lootItem.displayName = "未知标签";
        }

        lootItem.weight = entry.has("weight") ? entry.get("weight").getAsDouble() : 1.0;
        lootItem.quality = entry.has("quality") ? entry.get("quality").getAsDouble() : 0.0;

        if (entry.has("conditions") && entry.get("conditions").isJsonArray()) {
            for (JsonElement condElem : entry.getAsJsonArray("conditions")) {
                if (condElem.isJsonObject()) {
                    lootItem.conditions.add(parseCondition(condElem.getAsJsonObject()));
                    lootItem.conditionFactor *= 0.7;
                }
            }
        }

        lootPool.lootItems.add(lootItem);
    }

    private static void processLootTableEntry(JsonObject entry, LootPool lootPool) {
        if (entry == null || lootPool == null) {
            GTOCore.LOGGER.warn("处理空的战利品表引用条目或奖励池");
            return;
        }

        LootItem lootItem = new LootItem();
        lootItem.type = "战利品表引用";

        if (entry.has("name")) {
            lootItem.itemId = entry.get("name").getAsString();
            lootItem.displayName = "引用: " + lootItem.itemId;
        } else {
            GTOCore.LOGGER.warn("战利品表引用条目缺少name属性");
            lootItem.itemId = "unknown:loottable";
            lootItem.displayName = "未知战利品表引用";
        }

        lootItem.weight = entry.has("weight") ? entry.get("weight").getAsDouble() : 1.0;
        lootItem.quality = entry.has("quality") ? entry.get("quality").getAsDouble() : 0.0;

        if (entry.has("conditions") && entry.get("conditions").isJsonArray()) {
            for (JsonElement condElem : entry.getAsJsonArray("conditions")) {
                if (condElem.isJsonObject()) {
                    lootItem.conditions.add(parseCondition(condElem.getAsJsonObject()));
                    lootItem.conditionFactor *= 0.7;
                }
            }
        }

        lootPool.lootItems.add(lootItem);
    }

    private static void processEmptyEntry(JsonObject entry, LootPool lootPool) {
        if (entry == null || lootPool == null) {
            GTOCore.LOGGER.warn("处理空的空条目或奖励池");
            return;
        }

        LootItem lootItem = new LootItem();
        lootItem.type = "空";
        lootItem.itemId = "empty";
        lootItem.displayName = "无物品";
        lootItem.weight = entry.has("weight") ? entry.get("weight").getAsDouble() : 1.0;

        lootPool.lootItems.add(lootItem);
    }

    private static void processGroupEntry(JsonObject entry, LootPool lootPool) {
        if (entry == null || lootPool == null) {
            GTOCore.LOGGER.warn("处理空的组条目或奖励池");
            return;
        }

        LootItem lootItem = new LootItem();
        lootItem.type = "组";
        lootItem.itemId = "group";
        lootItem.displayName = "物品组";
        lootItem.weight = entry.has("weight") ? entry.get("weight").getAsDouble() : 1.0;
        lootItem.quality = entry.has("quality") ? entry.get("quality").getAsDouble() : 0.0;

        if (entry.has("conditions") && entry.get("conditions").isJsonArray()) {
            for (JsonElement condElem : entry.getAsJsonArray("conditions")) {
                if (condElem.isJsonObject()) {
                    lootItem.conditions.add(parseCondition(condElem.getAsJsonObject()));
                    lootItem.conditionFactor *= 0.7;
                }
            }
        }

        if (entry.has("children") && entry.get("children").isJsonArray()) {
            int childCount = entry.getAsJsonArray("children").size();
            lootItem.displayName += " (" + childCount + "个子条目)";
        }

        lootPool.lootItems.add(lootItem);
    }

    /**
     * 解析条件
     */
    private static String parseCondition(JsonObject condition) {
        if (condition == null || !condition.has("condition")) {
            return "未知条件";
        }

        String conditionType = condition.get("condition").getAsString();
        StringBuilder sb = new StringBuilder();

        switch (conditionType) {
            case "minecraft:killed_by_player":
                sb.append("被玩家杀死");
                break;
            case "minecraft:random_chance":
                double chance = condition.get("chance").getAsDouble();
                sb.append(String.format("随机概率 (%.0f%%)", chance * 100));
                break;
            case "minecraft:random_chance_with_looting":
                double baseChance = condition.get("chance").getAsDouble();
                double lootingMultiplier = condition.get("looting_multiplier").getAsDouble();
                sb.append(String.format("随掠夺等级变化的概率 (基础: %.0f%%, 每级掠夺+%.0f%%)",
                        baseChance * 100, lootingMultiplier * 100));
                break;
            case "minecraft:difficulty":
                sb.append("难度: ").append(condition.get("difficulty").getAsString());
                break;
            case "minecraft:tool":
                if (condition.has("predicate") && condition.get("predicate").isJsonObject()) {
                    JsonObject predicate = condition.getAsJsonObject("predicate");
                    if (predicate.has("tag")) {
                        sb.append("需要工具标签: ").append(predicate.get("tag").getAsString());
                    } else {
                        sb.append("需要特定工具");
                    }
                } else {
                    sb.append("需要特定工具");
                }
                break;
            case "minecraft:entity_properties":
                sb.append("实体属性条件");
                break;
            case "minecraft:location_check":
                sb.append("位置检查条件");
                break;
            case "minecraft:weather_check":
                sb.append("天气检查条件");
                break;
            case "minecraft:time_check":
                sb.append("时间检查条件");
                break;
            case "minecraft:dimension":
                sb.append("维度: ").append(condition.get("dimension").getAsString());
                break;
            case "minecraft:match_tool":
                sb.append("工具匹配条件");
                break;
            default:
                sb.append("条件: ").append(conditionType);
        }

        return sb.toString();
    }

    /**
     * 计算奖励池的统计信息
     */
    private static void calculatePoolStats(LootPool lootPool) {
        if (lootPool == null || lootPool.lootItems == null) {
            return;
        }

        double totalWeight = lootPool.lootItems.stream()
                .mapToDouble(item -> item.weight)
                .sum();
        lootPool.totalWeight = totalWeight;

        double baseRolls = lootPool.rolls != null ? lootPool.rolls.average : 1.0;
        double bonusRolls = lootPool.bonusRolls != null ? lootPool.bonusRolls.average : 0;
        lootPool.totalRollsAverage = baseRolls + bonusRolls;

        for (LootItem item : lootPool.lootItems) {
            if (totalWeight <= 0) {
                item.probability = 0;
                item.expectedValue = 0;
                continue;
            }

            double baseProb = item.weight / totalWeight;
            double conditionalProb = baseProb * item.conditionFactor;
            // 确保概率在0-1范围内
            conditionalProb = Math.max(0, Math.min(1, conditionalProb));

            item.probability = 1 - Math.pow(1 - conditionalProb, lootPool.totalRollsAverage);
            // 确保概率在0-1范围内
            item.probability = Math.max(0, Math.min(1, item.probability));

            item.expectedValue = item.probability * item.count;
            lootPool.totalExpectedValue += item.expectedValue;
        }

        // 排序前检查是否为空
        if (!lootPool.lootItems.isEmpty()) {
            lootPool.lootItems.sort((a, b) -> Double.compare(b.probability, a.probability));
        }
    }

    /**
     * 从JsonElement获取值信息
     */
    private static ValueInfo getValueInfo(JsonElement element) {
        ValueInfo info = new ValueInfo();

        if (element == null || element.isJsonNull()) {
            info.average = 1.0;
            info.detail = "1";
            return info;
        }

        if (element.isJsonObject()) {
            JsonObject obj = element.getAsJsonObject();
            if (obj.has("min") && obj.has("max")) {
                try {
                    double min = obj.get("min").getAsDouble();
                    double max = obj.get("max").getAsDouble();
                    // 确保min <= max
                    if (min > max) {
                        double temp = min;
                        min = max;
                        max = temp;
                        GTOCore.LOGGER.warn("发现min > max的情况，已自动交换");
                    }

                    info.average = (min + max) / 2.0;
                    if (min == (long) min && max == (long) max) {
                        info.detail = String.format("%d-%d", (long) min, (long) max);
                    } else {
                        info.detail = String.format("%.1f-%.1f", min, max);
                    }
                } catch (Exception e) {
                    GTOCore.LOGGER.warn("解析范围值时出错: {}", e.getMessage());
                    info.average = 1.0;
                    info.detail = "1";
                }
            } else if (obj.has("value")) {
                try {
                    info.average = obj.get("value").getAsDouble();
                    if (info.average == (long) info.average) {
                        info.detail = String.format("%d", (long) info.average);
                    } else {
                        info.detail = String.valueOf(info.average);
                    }
                } catch (Exception e) {
                    GTOCore.LOGGER.warn("解析固定值时出错: {}", e.getMessage());
                    info.average = 1.0;
                    info.detail = "1";
                }
            } else {
                info.average = 1.0;
                info.detail = "1";
            }
        } else if (element.isJsonPrimitive()) {
            try {
                info.average = element.getAsDouble();
                if (info.average == (long) info.average) {
                    info.detail = String.format("%d", (long) info.average);
                } else {
                    info.detail = String.valueOf(info.average);
                }
            } catch (Exception e) {
                GTOCore.LOGGER.warn("解析原始值时出错: {}", e.getMessage());
                info.average = 1.0;
                info.detail = "1";
            }
        } else {
            info.average = 1.0;
            info.detail = "1";
        }

        return info;
    }

    /**
     * 获取物品的翻译名称
     */
    private static String getItemTranslation(String itemId) {
        try {
            if (itemId == null || !ResourceLocation.isValidResourceLocation(itemId)) {
                return "无效ID: " + (itemId == null ? "null" : itemId);
            }

            Item item = RegistriesUtils.getItem(itemId);
            if (item != null) {
                String translation = item.getDescription().getString();
                if (translation.isEmpty()) {
                    return itemId;
                }
                return translation + " (`" + itemId + "`)";
            }
        } catch (Exception e) {
            GTOCore.LOGGER.warn("无法获取物品 {} 的翻译: {}", itemId, e.getMessage());
        }

        return itemId;
    }

    /**
     * 生成完整的Markdown文档，包含命名空间统计
     */
    private static String generateCompleteMarkdown(Map<String, LootTableAnalysis> allLootTables,
                                                   Map<String, List<String>> lootTablesByNamespace) {
        if (allLootTables == null || allLootTables.isEmpty()) {
            return "# 战利品表分析报告\n\n没有可分析的战利品表数据。";
        }

        StringBuilder markdown = new StringBuilder();

        markdown.append("# Minecraft 战利品表分析报告\n\n");
        markdown.append("生成时间: ").append(DATE_FORMATTER.get().format(new Date())).append("\n\n");
        markdown.append("## 摘要\n");
        markdown.append("- 分析的战利品表总数: ").append(allLootTables.size()).append("\n");
        markdown.append("- 涉及的命名空间数量: ").append(lootTablesByNamespace.size()).append("\n");

        // 按命名空间统计
        markdown.append("\n### 命名空间分布\n");
        for (Map.Entry<String, List<String>> entry : lootTablesByNamespace.entrySet()) {
            markdown.append("- ").append(entry.getKey()).append(": ").append(entry.getValue().size()).append(" 个战利品表\n");
        }

        long errorCount = allLootTables.values().stream().filter(a -> a.getError() != null).count();
        markdown.append("\n- 分析失败的战利品表: ").append(errorCount).append("\n\n");

        // 按命名空间分组展示
        for (Map.Entry<String, List<String>> namespaceEntry : lootTablesByNamespace.entrySet()) {
            String namespace = namespaceEntry.getKey();
            List<String> namespaceTables = namespaceEntry.getValue();

            markdown.append("## 命名空间: ").append(namespace).append("\n");
            markdown.append("**包含战利品表数量:** ").append(namespaceTables.size()).append("\n\n");

            for (String tableName : namespaceTables) {
                LootTableAnalysis analysis = allLootTables.get(tableName);
                if (analysis == null) {
                    continue;
                }

                markdown.append("### 战利品表: ").append(tableName).append("\n\n");

                if (analysis.getError() != null) {
                    markdown.append("> **错误**: ").append(analysis.getError()).append("\n\n");
                    continue;
                }

                markdown.append("**类型**: ").append(analysis.getType()).append("\n");

                if (!analysis.getGlobalConditions().isEmpty()) {
                    markdown.append("**全局条件**: \n");
                    for (String cond : analysis.getGlobalConditions()) {
                        markdown.append("- ").append(cond).append("\n");
                    }
                    markdown.append("\n");
                }

                for (LootPool pool : analysis.getLootPools()) {
                    if (pool == null) {
                        continue;
                    }

                    markdown.append("#### 奖励池 ").append(pool.poolIndex)
                            .append(pool.name != null ? " (" + pool.name + ")" : "").append("\n\n");

                    // 显示池级函数信息
                    if (!pool.functions.isEmpty()) {
                        markdown.append("**池级函数:**\n");
                        for (JsonObject func : pool.functions) {
                            if (func == null || !func.has("function")) {
                                continue;
                            }

                            String funcName = func.get("function").getAsString();
                            markdown.append("- ").append(funcName);
                            if (func.has("count")) {
                                markdown.append(" (数量: ").append(getValueInfo(func.get("count")).detail).append(")");
                            } else if (func.has("levels")) {
                                markdown.append(" (等级: ").append(getValueInfo(func.get("levels")).detail).append(")");
                            } else if (func.has("limit")) {
                                markdown.append(" (上限: ").append(func.get("limit").getAsInt()).append(")");
                            }
                            markdown.append("\n");
                        }
                        markdown.append("\n");
                    }

                    markdown.append("**池信息:**\n");
                    markdown.append("- 抽取次数: ").append(pool.rolls != null ? pool.rolls.detail : "1")
                            .append(" (平均: ").append(pool.rolls != null ? NUMBER_FORMAT.format(pool.rolls.average) : "1").append(")\n");

                    if (pool.bonusRolls != null) {
                        markdown.append("- 额外抽取次数: ").append(pool.bonusRolls.detail)
                                .append(" (平均: ").append(NUMBER_FORMAT.format(pool.bonusRolls.average)).append(")\n");
                    }

                    markdown.append("- 总平均抽取次数: ").append(NUMBER_FORMAT.format(pool.totalRollsAverage)).append("\n");
                    markdown.append("- 物品条目总数: ").append(pool.lootItems.size()).append("\n");
                    markdown.append("- 总权重: ").append(NUMBER_FORMAT.format(pool.totalWeight)).append("\n");
                    markdown.append("- 总期望值: ").append(NUMBER_FORMAT.format(pool.totalExpectedValue)).append("\n");

                    if (!pool.conditions.isEmpty()) {
                        markdown.append("- 池条件: \n");
                        for (String cond : pool.conditions) {
                            markdown.append("  - ").append(cond).append("\n");
                        }
                    }

                    markdown.append("\n");

                    markdown.append("| 类型 | 物品 | 数量 | 权重 | 概率 | 期望 | 特殊属性 |\n");
                    markdown.append("|------|------|------|------|------|------|----------|\n");

                    for (LootItem item : pool.lootItems) {
                        if (item == null) {
                            continue;
                        }

                        List<String> attributes = new ArrayList<>();
                        if (item.enchanted) attributes.add("已附魔");
                        if (item.enchantWithLevels) attributes.add("等级附魔");
                        if (item.treasureEnchant) attributes.add("宝藏附魔");
                        if (item.smelted) attributes.add("已冶炼");
                        if (item.hasNbt) attributes.add("有NBT");
                        if (item.copyNbt) attributes.add("复制NBT");
                        if (item.lootingAffected) attributes.add("受掠夺影响");
                        if (item.hasDamage) attributes.add("有损耗");
                        if (item.hasPotion) attributes.add("有药水效果");
                        if (item.copyName) attributes.add("复制名称");
                        if (item.limitCount) attributes.add("数量有限制");
                        if (item.hasAttributes) attributes.add("有属性");
                        if (item.hasBookContents) attributes.add("有书本内容");
                        if (item.hasLore) attributes.add("有Lore");
                        if (item.hasCustomName) attributes.add("有自定义名称");
                        if (item.explosionDecay) attributes.add("爆炸衰减");
                        if (item.hasPlayerHeadData) attributes.add("玩家头颅数据");
                        if (item.isEntityEquipment) attributes.add("实体装备");
                        if (!item.otherFunctions.isEmpty()) {
                            attributes.add("其他函数: " + String.join(",", item.otherFunctions));
                        }

                        String attrStr = attributes.isEmpty() ? "-" : String.join(", ", attributes);
                        String displayName = item.displayName != null ? item.displayName : "未知";
                        String countDetail = item.countDetail != null ? item.countDetail : NUMBER_FORMAT.format(item.count);
                        String countOp = item.countOperation != null ? item.countOperation + " " : "";

                        markdown.append("| ")
                                .append(item.type)
                                .append(" | ")
                                .append(displayName)
                                .append(" | ")
                                .append(countOp).append(countDetail)
                                .append(" | ")
                                .append(NUMBER_FORMAT.format(item.weight))
                                .append(" | ")
                                .append(PERCENT_FORMAT.format(item.probability))
                                .append(" | ")
                                .append(NUMBER_FORMAT.format(item.expectedValue))
                                .append(" | ")
                                .append(attrStr)
                                .append(" |\n");
                    }

                    List<LootItem> itemsWithConditions = pool.lootItems.stream()
                            .filter(item -> item != null && !item.conditions.isEmpty())
                            .toList();

                    if (!itemsWithConditions.isEmpty()) {
                        markdown.append("\n**物品条件说明:**\n");
                        for (LootItem item : itemsWithConditions) {
                            markdown.append("- ").append(item.displayName).append(":\n");
                            for (String cond : item.conditions) {
                                markdown.append("  - ").append(cond).append("\n");
                            }
                        }
                    }

                    markdown.append("\n");
                }

                double tableTotalExpectedValue = analysis.getLootPools().stream()
                        .filter(Objects::nonNull)
                        .mapToDouble(pool -> pool.totalExpectedValue)
                        .sum();

                markdown.append("**战利品表总期望值:** ").append(NUMBER_FORMAT.format(tableTotalExpectedValue)).append("\n\n");
            }
        }

        markdown.append("## 战利品表汇总比较\n\n");
        markdown.append("| 命名空间 | 战利品表 | 类型 | 奖励池数量 | 总期望值 | 状态 |\n");
        markdown.append("|----------|----------|------|-----------|----------|------|\n");

        for (String tableName : allLootTables.keySet()) {
            LootTableAnalysis analysis = allLootTables.get(tableName);
            if (analysis == null) {
                continue;
            }

            ResourceLocation loc = new ResourceLocation(tableName);
            String namespace = loc.getNamespace();

            String status = analysis.getError() != null ? "错误" : "正常";
            double tableTotalExpectedValue = analysis.getLootPools().stream()
                    .filter(Objects::nonNull)
                    .mapToDouble(pool -> pool.totalExpectedValue)
                    .sum();

            markdown.append("| ")
                    .append(namespace)
                    .append(" | `")
                    .append(loc.getPath())
                    .append("` | ")
                    .append(analysis.getType())
                    .append(" | ")
                    .append(analysis.getLootPools().size())
                    .append(" | ")
                    .append(analysis.getError() != null ? "-" : NUMBER_FORMAT.format(tableTotalExpectedValue))
                    .append(" | ")
                    .append(status)
                    .append(" |\n");
        }

        return markdown.toString();
    }

    /**
     * 内部类：表示值信息
     */
    private static class ValueInfo {

        double average;
        String detail;
    }

    /**
     * 内部类：表示战利品表分析结果
     */
    private static class LootTableAnalysis {

        private final String name;
        private String type;
        private List<String> globalConditions = new ArrayList<>();
        private List<LootPool> lootPools = new ArrayList<>();
        private String error;

        public LootTableAnalysis(String name) {
            this.name = name;
            this.type = "未知";
        }

        // getter和setter方法
        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<String> getGlobalConditions() {
            return globalConditions;
        }

        public void setGlobalConditions(List<String> globalConditions) {
            this.globalConditions = globalConditions;
        }

        public List<LootPool> getLootPools() {
            return lootPools;
        }

        public void setLootPools(List<LootPool> lootPools) {
            this.lootPools = lootPools;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }

    /**
     * 内部类：表示战利品项 - 增加了更多函数相关属性
     */
    private static class LootItem {

        String type;
        String itemId;
        String displayName;
        double count = 1.000; // 默认数量1
        String countDetail;
        String countOperation; // 数量操作："设置"或"添加"
        double weight = 1.000; // 默认权重1
        double quality = 0.000;
        double probability = 0.000;
        double expectedValue = 0.000;
        List<String> conditions = new ArrayList<>();
        List<String> functions = new ArrayList<>(); // 记录所有函数
        List<String> otherFunctions = new ArrayList<>(); // 未分类函数
        double conditionFactor = 1.000;
        boolean enchanted = false;
        boolean enchantWithLevels = false; // 等级附魔
        boolean treasureEnchant = false; // 宝藏附魔
        boolean smelted = false;
        boolean hasNbt = false;
        boolean copyNbt = false; // 复制NBT
        boolean lootingAffected = false;
        boolean hasDamage = false;
        boolean hasPotion = false; // 药水效果
        boolean copyName = false; // 复制名称
        boolean limitCount = false; // 数量限制
        boolean hasAttributes = false; // 属性设置
        boolean hasBookContents = false; // 书本内容
        boolean hasLore = false; // Lore
        boolean hasCustomName = false; // 自定义名称
        boolean explosionDecay = false; // 爆炸衰减
        boolean hasPlayerHeadData = false; // 玩家头颅数据
        boolean isEntityEquipment = false; // 实体装备
        double lootingCount = 0.000;
        int lootingLimit = Integer.MAX_VALUE;
        String enchantments;
        String potionType; // 药水类型
        double damage; // 损伤值
    }

    /**
     * 内部类：表示奖励池
     */
    private static class LootPool {

        int poolIndex;
        String name;
        ValueInfo rolls;
        ValueInfo bonusRolls;
        List<LootItem> lootItems = new ArrayList<>();
        List<String> conditions = new ArrayList<>();
        List<JsonObject> functions = new ArrayList<>();
        double totalWeight;
        double totalRollsAverage;
        double totalExpectedValue;
    }
}
