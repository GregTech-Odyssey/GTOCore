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
import java.util.stream.Collectors;

public class LootTableExporter {

    /**
     * 导出指定的战利品表列表到Markdown文件
     *
     * @param lootTables 要分析的战利品表列表
     */
    public static void exportAllLootTables(List<String> lootTables) {
        // 验证输入
        if (lootTables == null || lootTables.isEmpty()) {
            GTOCore.LOGGER.warn("没有指定要导出的战利品表");
            return;
        }
        exportAllLootTablesToMarkdown(lootTables);
    }

    /**
     * 导出所有战利品表到Markdown文件
     */
    private static void exportAllLootTablesToMarkdown(List<String> lootTables) {
        // 使用Forge的方法获取服务器实例，替换Architectury的方法
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            GTOCore.LOGGER.error("无法获取服务器实例 - 可能不在服务端环境或服务器未启动");
            return;
        }

        try {
            // 存储所有战利品表的结果
            Map<String, LootTableAnalysis> allLootTables = new LinkedHashMap<>();

            // 处理每个战利品表
            for (String tableName : lootTables) {
                // 验证战利品表名称格式
                if (!ResourceLocation.isValidResourceLocation(tableName)) {
                    GTOCore.LOGGER.error("无效的战利品表名称格式: {}", tableName);
                    continue;
                }

                ResourceLocation lootTableLocation = new ResourceLocation(tableName);
                LootTableAnalysis analysis = new LootTableAnalysis(tableName);

                try {
                    // 获取战利品表的JSON表示以解析详细信息
                    ResourceManager resourceManager = server.getResourceManager();
                    ResourceLocation jsonLocation = new ResourceLocation(
                            lootTableLocation.getNamespace(),
                            "loot_tables/" + lootTableLocation.getPath() + ".json");

                    // 使用try-catch处理资源获取可能的异常
                    Optional<Resource> resourceOpt = resourceManager.getResource(jsonLocation);
                    if (resourceOpt.isEmpty()) {
                        GTOCore.LOGGER.error("找不到战利品表JSON: {}", jsonLocation);
                        analysis.setError("找不到战利品表JSON资源");
                        allLootTables.put(tableName, analysis);
                        continue;
                    }

                    Resource resource = resourceOpt.get();

                    // 读取战利品表JSON内容
                    String jsonText;
                    try (InputStream inputStream = resource.open()) {
                        jsonText = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                    }

                    // 解析JSON
                    JsonElement jsonElement = JsonParser.parseString(jsonText);
                    if (!jsonElement.isJsonObject()) {
                        GTOCore.LOGGER.error("战利品表JSON不是有效的对象: {}", jsonLocation);
                        analysis.setError("战利品表JSON格式错误");
                        allLootTables.put(tableName, analysis);
                        continue;
                    }

                    JsonObject lootTableJson = jsonElement.getAsJsonObject();
                    analysis.setType(lootTableJson.has("type") ? lootTableJson.get("type").getAsString() : "通用");

                    // 处理全局条件
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

                    // 处理战利品表数据
                    List<LootPool> lootPools = processLootTable(lootTableJson);
                    analysis.setLootPools(lootPools);

                    allLootTables.put(tableName, analysis);

                } catch (IOException e) {
                    String errorMsg = "处理战利品表 " + tableName + " 时出错: " + e.getMessage();
                    GTOCore.LOGGER.error(errorMsg, e);
                    analysis.setError(errorMsg);
                    allLootTables.put(tableName, analysis);
                } catch (Exception e) {
                    // 捕获其他未预料到的异常
                    String errorMsg = "分析战利品表 " + tableName + " 时发生意外错误: " + e.getMessage();
                    GTOCore.LOGGER.error(errorMsg, e);
                    analysis.setError(errorMsg);
                    allLootTables.put(tableName, analysis);
                }
            }

            // 生成Markdown文档
            String markdown = generateCompleteMarkdown(allLootTables);

            // 保存到文件
            Path logDir = Paths.get("logs", "report");
            if (!Files.exists(logDir)) {
                Files.createDirectories(logDir);
            }

            String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
            Path reportPath = logDir.resolve("loottable_analysis_" + timestamp + ".md");

            try (BufferedWriter writer = Files.newBufferedWriter(reportPath)) {
                writer.write(markdown);
                GTOCore.LOGGER.info("战利品表分析已导出到: {}", reportPath.toAbsolutePath());
            }

        } catch (IOException e) {
            GTOCore.LOGGER.error("导出战利品表时出错: {}", e.getMessage(), e);
        }
    }

    /**
     * 处理战利品表JSON数据
     */
    private static List<LootPool> processLootTable(JsonObject lootTableJson) {
        List<LootPool> lootPools = new ArrayList<>();

        // 检查是否有pools数组
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
                lootPool.name = pool.has("name") ? pool.get("name").getAsString() : "未命名";

                // 获取rolls值（抽取次数）
                if (pool.has("rolls")) {
                    lootPool.rolls = getValueInfo(pool.get("rolls"));
                } else {
                    // 设置默认值
                    ValueInfo defaultRolls = new ValueInfo();
                    defaultRolls.average = 1.0;
                    defaultRolls.detail = "1";
                    lootPool.rolls = defaultRolls;
                    GTOCore.LOGGER.debug("奖励池{}未指定rolls，使用默认值1", poolIndex);
                }

                // 获取bonus_rolls值
                if (pool.has("bonus_rolls")) {
                    lootPool.bonusRolls = getValueInfo(pool.get("bonus_rolls"));
                }

                // 处理池条件
                if (pool.has("conditions") && pool.get("conditions").isJsonArray()) {
                    for (JsonElement condElem : pool.getAsJsonArray("conditions")) {
                        if (condElem.isJsonObject()) {
                            lootPool.conditions.add(parseCondition(condElem.getAsJsonObject()));
                        } else {
                            GTOCore.LOGGER.warn("奖励池{}中发现非对象类型的条件", poolIndex);
                        }
                    }
                }

                // 处理entries
                if (pool.has("entries") && pool.get("entries").isJsonArray()) {
                    for (JsonElement entryElement : pool.getAsJsonArray("entries")) {
                        if (entryElement.isJsonObject()) {
                            processLootEntry(entryElement.getAsJsonObject(), lootPool);
                        } else {
                            GTOCore.LOGGER.warn("奖励池{}中发现非对象类型的条目", poolIndex);
                        }
                    }
                }

                // 计算当前奖励池的统计信息
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
     * 处理单个战利品条目
     */
    private static void processLootEntry(JsonObject entry, LootPool lootPool) {
        if (!entry.has("type")) {
            GTOCore.LOGGER.warn("发现没有类型的战利品条目");
            return;
        }

        String entryType = entry.get("type").getAsString();

        // 处理物品类型条目
        if ("minecraft:item".equals(entryType)) {
            processItemEntry(entry, lootPool);
        }
        // 处理标签类型条目
        else if ("minecraft:tag".equals(entryType)) {
            processTagEntry(entry, lootPool);
        }
        // 处理引用其他战利品表的条目
        else if ("minecraft:loot_table".equals(entryType)) {
            processLootTableEntry(entry, lootPool);
        }
        // 处理空条目
        else if ("minecraft:empty".equals(entryType)) {
            processEmptyEntry(entry, lootPool);
        }
        // 处理组条目
        else if ("minecraft:group".equals(entryType)) {
            processGroupEntry(entry, lootPool);
        }
        // 未知类型
        else {
            GTOCore.LOGGER.debug("未处理的战利品条目类型: {}", entryType);
            // 添加未知类型的条目记录
            LootItem lootItem = new LootItem();
            lootItem.type = "未处理类型";
            lootItem.itemId = entryType;
            lootItem.displayName = "未处理: " + entryType;
            lootItem.weight = entry.has("weight") ? entry.get("weight").getAsDouble() : 1.0;
            lootPool.lootItems.add(lootItem);
        }
    }

    /**
     * 处理组条目
     */
    private static void processGroupEntry(JsonObject entry, LootPool lootPool) {
        LootItem lootItem = new LootItem();
        lootItem.type = "组";
        lootItem.itemId = "group";
        lootItem.displayName = "物品组";
        lootItem.weight = entry.has("weight") ? entry.get("weight").getAsDouble() : 1.0;
        lootItem.quality = entry.has("quality") ? entry.get("quality").getAsDouble() : 0.0;

        // 处理条件
        if (entry.has("conditions") && entry.get("conditions").isJsonArray()) {
            for (JsonElement condElem : entry.getAsJsonArray("conditions")) {
                if (condElem.isJsonObject()) {
                    lootItem.conditions.add(parseCondition(condElem.getAsJsonObject()));
                    lootItem.conditionFactor *= 0.5;
                }
            }
        }

        // 处理组内条目
        if (entry.has("children") && entry.get("children").isJsonArray()) {
            int childCount = entry.getAsJsonArray("children").size();
            lootItem.displayName += " (" + childCount + "个子条目)";
        }

        lootPool.lootItems.add(lootItem);
    }

    /**
     * 处理物品类型条目
     */
    private static void processItemEntry(JsonObject entry, LootPool lootPool) {
        LootItem lootItem = new LootItem();
        lootItem.type = "物品";

        // 获取物品ID
        if (entry.has("name")) {
            lootItem.itemId = entry.get("name").getAsString();
            lootItem.displayName = getItemTranslation(lootItem.itemId);
        } else {
            GTOCore.LOGGER.warn("物品条目缺少name属性");
            lootItem.itemId = "unknown";
            lootItem.displayName = "未知物品";
        }

        // 获取权重和质量
        lootItem.weight = entry.has("weight") ? entry.get("weight").getAsDouble() : 1.0;
        lootItem.quality = entry.has("quality") ? entry.get("quality").getAsDouble() : 0.0;

        // 处理条目条件
        if (entry.has("conditions") && entry.get("conditions").isJsonArray()) {
            for (JsonElement condElem : entry.getAsJsonArray("conditions")) {
                if (condElem.isJsonObject()) {
                    lootItem.conditions.add(parseCondition(condElem.getAsJsonObject()));
                    // 简单估算条件对概率的影响（实际情况更复杂）
                    lootItem.conditionFactor *= 0.5; // 假设每个条件使概率减半
                }
            }
        }

        // 处理函数
        if (entry.has("functions") && entry.get("functions").isJsonArray()) {
            for (JsonElement functionElement : entry.getAsJsonArray("functions")) {
                if (functionElement.isJsonObject()) {
                    processFunction(functionElement.getAsJsonObject(), lootItem);
                }
            }
        }

        lootPool.lootItems.add(lootItem);
    }

    /**
     * 处理标签类型条目
     */
    private static void processTagEntry(JsonObject entry, LootPool lootPool) {
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

        // 处理条件
        if (entry.has("conditions") && entry.get("conditions").isJsonArray()) {
            for (JsonElement condElem : entry.getAsJsonArray("conditions")) {
                if (condElem.isJsonObject()) {
                    lootItem.conditions.add(parseCondition(condElem.getAsJsonObject()));
                    lootItem.conditionFactor *= 0.5;
                }
            }
        }

        lootPool.lootItems.add(lootItem);
    }

    /**
     * 处理引用其他战利品表的条目
     */
    private static void processLootTableEntry(JsonObject entry, LootPool lootPool) {
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

        // 处理条件
        if (entry.has("conditions") && entry.get("conditions").isJsonArray()) {
            for (JsonElement condElem : entry.getAsJsonArray("conditions")) {
                if (condElem.isJsonObject()) {
                    lootItem.conditions.add(parseCondition(condElem.getAsJsonObject()));
                    lootItem.conditionFactor *= 0.5;
                }
            }
        }

        lootPool.lootItems.add(lootItem);
    }

    /**
     * 处理空条目
     */
    private static void processEmptyEntry(JsonObject entry, LootPool lootPool) {
        LootItem lootItem = new LootItem();
        lootItem.type = "空";
        lootItem.itemId = "empty";
        lootItem.displayName = "无物品";
        lootItem.weight = entry.has("weight") ? entry.get("weight").getAsDouble() : 1.0;

        lootPool.lootItems.add(lootItem);
    }

    /**
     * 处理函数
     */
    private static void processFunction(JsonObject function, LootItem lootItem) {
        if (!function.has("function")) return;

        String functionType = function.get("function").getAsString();
        lootItem.functions.add(functionType);

        // 处理数量设置函数
        if ("minecraft:set_count".equals(functionType)) {
            if (function.has("count")) {
                ValueInfo countInfo = getValueInfo(function.get("count"));
                lootItem.count = countInfo.average;
                lootItem.countDetail = countInfo.detail;
            }
        }
        // 处理受掠夺附魔影响的数量函数
        else if ("minecraft:looting_enchant".equals(functionType)) {
            lootItem.lootingAffected = true;
            if (function.has("count")) {
                ValueInfo countInfo = getValueInfo(function.get("count"));
                lootItem.lootingCount = countInfo.average;
            }
            if (function.has("limit")) {
                lootItem.lootingLimit = function.get("limit").getAsInt();
            }
        }
        // 处理附魔函数
        else if ("minecraft:enchant_randomly".equals(functionType)) {
            lootItem.enchanted = true;
            if (function.has("enchantments")) {
                lootItem.enchantments = function.getAsJsonArray("enchantments").toString();
            }
        }
        // 处理特定附魔函数
        else if ("minecraft:enchant_with_levels".equals(functionType)) {
            lootItem.enchanted = true;
            lootItem.displayName += " (等级附魔)";
        }
        // 处理熔炉冶炼函数
        else if ("minecraft:furnace_smelt".equals(functionType)) {
            lootItem.smelted = true;
            lootItem.displayName += " (已冶炼)";
        }
        // 处理NBT设置函数
        else if ("minecraft:set_nbt".equals(functionType)) {
            lootItem.hasNbt = true;
            lootItem.displayName += " (带NBT)";
        }
        // 处理物品损坏函数
        else if ("minecraft:set_damage".equals(functionType)) {
            lootItem.hasDamage = true;
            lootItem.displayName += " (有损耗)";
        }
    }

    /**
     * 解析条件
     */
    private static String parseCondition(JsonObject condition) {
        if (!condition.has("condition")) {
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
            case "minecraft:weather":
                sb.append("天气: ").append(condition.get("weather").getAsString());
                break;
            case "minecraft:time_check":
                sb.append("时间检查条件");
                break;
            case "minecraft:entity_properties":
                sb.append("实体属性条件");
                break;
            case "minecraft:block_state_property":
                sb.append("方块状态条件");
                break;
            case "minecraft:inverted":
                if (condition.has("term") && condition.get("term").isJsonObject()) {
                    sb.append("反转: ").append(parseCondition(condition.getAsJsonObject("term")));
                } else {
                    sb.append("反转条件");
                }
                break;
            case "minecraft:alternative":
                sb.append("任一条件满足");
                break;
            case "minecraft:all_of":
                sb.append("所有条件满足");
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
        // 计算总权重
        double totalWeight = lootPool.lootItems.stream()
                .mapToDouble(item -> item.weight)
                .sum();
        lootPool.totalWeight = totalWeight;

        // 计算基础抽取次数（平均值）
        double baseRolls = lootPool.rolls != null ? lootPool.rolls.average : 1.0;
        // 计算额外抽取次数（平均值）
        double bonusRolls = lootPool.bonusRolls != null ? lootPool.bonusRolls.average : 0;
        lootPool.totalRollsAverage = baseRolls + bonusRolls;

        // 计算每个物品的概率和期望
        for (LootItem item : lootPool.lootItems) {
            if (totalWeight <= 0) {
                item.probability = 0;
                item.expectedValue = 0;
                continue;
            }

            // 基础概率 = (物品权重 / 总权重)
            double baseProb = item.weight / totalWeight;

            // 考虑条件因素后的概率
            double conditionalProb = baseProb * item.conditionFactor;

            // 考虑抽取次数的最终概率
            // 使用1 - (1 - p)^n公式计算至少获得一次的概率
            item.probability = 1 - Math.pow(1 - conditionalProb, lootPool.totalRollsAverage);

            // 期望 = 概率 * 数量
            item.expectedValue = item.probability * item.count;

            // 累加总期望
            lootPool.totalExpectedValue += item.expectedValue;
        }

        // 按概率排序
        lootPool.lootItems.sort((a, b) -> Double.compare(b.probability, a.probability));
    }

    /**
     * 从JsonElement获取值信息（处理范围和固定值）
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
                // 处理范围值
                try {
                    double min = obj.get("min").getAsDouble();
                    double max = obj.get("max").getAsDouble();
                    info.average = (min + max) / 2.0;
                    // 处理整数范围的显示
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
                // 处理固定值
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
            // 验证物品ID格式
            if (!ResourceLocation.isValidResourceLocation(itemId)) {
                return "无效ID: " + itemId;
            }

            // 使用RegistriesUtils获取物品
            Item item = RegistriesUtils.getItem(itemId);
            if (item != null) {
                // 获取物品的翻译名称
                String translation = item.getDescription().getString();
                // 确保翻译不是空的
                if (translation == null || translation.isEmpty()) {
                    return itemId;
                }
                return translation + " (`" + itemId + "`)";
            }
        } catch (Exception e) {
            GTOCore.LOGGER.warn("无法获取物品 {} 的翻译: {}", itemId, e.getMessage());
        }

        // 如果无法获取翻译，返回物品ID
        return itemId;
    }

    /**
     * 生成完整的Markdown文档
     */
    private static String generateCompleteMarkdown(Map<String, LootTableAnalysis> allLootTables) {
        StringBuilder markdown = new StringBuilder();
        DecimalFormat percentFormat = new DecimalFormat("#.##%");
        DecimalFormat numberFormat = new DecimalFormat("#.##");

        // 标题
        markdown.append("# Minecraft 战利品表分析报告\n\n");
        markdown.append("生成时间: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("\n\n");
        markdown.append("## 摘要\n");
        markdown.append("- 分析的战利品表总数: ").append(allLootTables.size()).append("\n");

        long errorCount = allLootTables.values().stream().filter(a -> a.getError() != null).count();
        markdown.append("- 分析失败的战利品表: ").append(errorCount).append("\n\n");

        // 生成每个战利品表的详细分析
        for (String tableName : allLootTables.keySet()) {
            LootTableAnalysis analysis = allLootTables.get(tableName);

            markdown.append("## ").append(tableName).append("\n\n");

            // 如果有错误信息
            if (analysis.getError() != null) {
                markdown.append("> **错误**: ").append(analysis.getError()).append("\n\n");
                continue;
            }

            // 战利品表基本信息
            markdown.append("**类型**: ").append(analysis.getType()).append("\n");

            // 全局条件
            if (!analysis.getGlobalConditions().isEmpty()) {
                markdown.append("**全局条件**: \n");
                for (String cond : analysis.getGlobalConditions()) {
                    markdown.append("- ").append(cond).append("\n");
                }
                markdown.append("\n");
            }

            // 遍历每个奖励池
            for (LootPool pool : analysis.getLootPools()) {
                markdown.append("### 奖励池 ").append(pool.poolIndex)
                        .append(pool.name != null ? " (" + pool.name + ")" : "").append("\n\n");

                // 池统计信息
                markdown.append("**池信息:**\n");
                markdown.append("- 抽取次数: ").append(pool.rolls.detail)
                        .append(" (平均: ").append(numberFormat.format(pool.rolls.average)).append(")\n");

                if (pool.bonusRolls != null) {
                    markdown.append("- 额外抽取次数: ").append(pool.bonusRolls.detail)
                            .append(" (平均: ").append(numberFormat.format(pool.bonusRolls.average)).append(")\n");
                }

                markdown.append("- 总平均抽取次数: ").append(numberFormat.format(pool.totalRollsAverage)).append("\n");
                markdown.append("- 物品条目总数: ").append(pool.lootItems.size()).append("\n");
                markdown.append("- 总权重: ").append(numberFormat.format(pool.totalWeight)).append("\n");
                markdown.append("- 总期望值: ").append(numberFormat.format(pool.totalExpectedValue)).append("\n");

                // 池条件
                if (!pool.conditions.isEmpty()) {
                    markdown.append("- 池条件: \n");
                    for (String cond : pool.conditions) {
                        markdown.append("  - ").append(cond).append("\n");
                    }
                }

                markdown.append("\n");

                // 表格标题
                markdown.append("| 类型 | 物品 | 数量 | 权重 | 概率 | 期望 | 特殊属性 |\n");
                markdown.append("|------|------|------|------|------|------|----------|\n");

                // 表格内容
                for (LootItem item : pool.lootItems) {
                    List<String> attributes = new ArrayList<>();
                    if (item.enchanted) attributes.add("已附魔");
                    if (item.smelted) attributes.add("已冶炼");
                    if (item.hasNbt) attributes.add("有NBT");
                    if (item.lootingAffected) attributes.add("受掠夺影响");
                    if (item.hasDamage) attributes.add("有损耗");

                    String attrStr = attributes.isEmpty() ? "-" : String.join(", ", attributes);

                    // 处理可能的空值
                    String displayName = item.displayName != null ? item.displayName : "未知";
                    String countDetail = item.countDetail != null ? item.countDetail : numberFormat.format(item.count);

                    markdown.append("| ")
                            .append(item.type)
                            .append(" | ")
                            .append(displayName)
                            .append(" | ")
                            .append(countDetail)
                            .append(" | ")
                            .append(numberFormat.format(item.weight))
                            .append(" | ")
                            .append(percentFormat.format(item.probability))
                            .append(" | ")
                            .append(numberFormat.format(item.expectedValue))
                            .append(" | ")
                            .append(attrStr)
                            .append(" |\n");
                }

                // 物品条件说明
                List<LootItem> itemsWithConditions = pool.lootItems.stream()
                        .filter(item -> !item.conditions.isEmpty())
                        .collect(Collectors.toList());

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

            // 计算战利品表的总期望值
            double tableTotalExpectedValue = analysis.getLootPools().stream()
                    .mapToDouble(pool -> pool.totalExpectedValue)
                    .sum();

            markdown.append("**战利品表总期望值:** ").append(numberFormat.format(tableTotalExpectedValue)).append("\n\n");
        }

        // 生成汇总表格
        markdown.append("## 战利品表汇总比较\n\n");
        markdown.append("| 战利品表 | 类型 | 奖励池数量 | 总期望值 | 状态 |\n");
        markdown.append("|----------|------|-----------|----------|------|\n");

        for (String tableName : allLootTables.keySet()) {
            LootTableAnalysis analysis = allLootTables.get(tableName);
            String status = analysis.getError() != null ? "错误" : "正常";
            double tableTotalExpectedValue = analysis.getLootPools().stream()
                    .mapToDouble(pool -> pool.totalExpectedValue)
                    .sum();

            markdown.append("| `")
                    .append(tableName)
                    .append("` | ")
                    .append(analysis.getType())
                    .append(" | ")
                    .append(analysis.getLootPools().size())
                    .append(" | ")
                    .append(analysis.getError() != null ? "-" : numberFormat.format(tableTotalExpectedValue))
                    .append(" | ")
                    .append(status)
                    .append(" |\n");
        }

        return markdown.toString();
    }

    /**
     * 内部类：表示值信息（包含平均值和详细描述）
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
     * 内部类：表示战利品项
     */
    private static class LootItem {

        String type;
        String itemId;
        String displayName;
        double count = 1.0;
        String countDetail;
        double weight = 1.0;
        double quality = 0.0;
        double probability = 0.0;
        double expectedValue = 0.0;
        List<String> conditions = new ArrayList<>();
        List<String> functions = new ArrayList<>();
        double conditionFactor = 1.0;
        boolean enchanted = false;
        boolean smelted = false;
        boolean hasNbt = false;
        boolean lootingAffected = false;
        boolean hasDamage = false;
        double lootingCount = 0.0;
        int lootingLimit = Integer.MAX_VALUE;
        String enchantments;
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
        double totalWeight;
        double totalRollsAverage;
        double totalExpectedValue;
    }
}
