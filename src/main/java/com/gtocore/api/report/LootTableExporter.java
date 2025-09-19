package com.gtocore.api.report;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gtolib.GTOCore;
import com.gtolib.utils.RegistriesUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.storage.loot.LootTable;
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

import static committee.nova.mods.avaritia.api.client.screen.component.Text.i18n;
import static dev.architectury.utils.GameInstance.getServer;

public class LootTableExporter {

    // 要分析的所有战利品表
    private static final List<String> LOOT_TABLES = Arrays.asList(
            "extrabotany:reward_bags/eins",
            "extrabotany:reward_bags/zwei",
            "extrabotany:reward_bags/drei",
            "extrabotany:reward_bags/vier",
            "extrabotany:reward_bags/nine_and_three_quarters",
            "extrabotany:reward_bags/pandoras_box"
    );

    /**
     * 导出所有战利品表到Markdown文件
     */
    public static void exportAllLootTablesToMarkdown() {
        if (getServer() == null) {
            GTOCore.LOGGER.error("无法获取服务器实例");
            return;
        }

        try {
            // 存储所有战利品表的结果
            Map<String, List<LootItem>> allLootItems = new LinkedHashMap<>();
            Map<String, LootTableStats> allStats = new LinkedHashMap<>();

            // 处理每个战利品表
            for (String tableName : LOOT_TABLES) {
                ResourceLocation lootTableLocation = new ResourceLocation(tableName);

                try {
                    // 使用Minecraft的LootTable系统获取战利品表
                    LootTable lootTable = getServer().getLootData().getLootTable(lootTableLocation);
                    if (lootTable == null) {
                        GTOCore.LOGGER.error("找不到战利品表: " + lootTableLocation);
                        continue;
                    }

                    // 获取战利品表的JSON表示以解析详细信息
                    ResourceManager resourceManager = getServer().getResourceManager();
                    ResourceLocation jsonLocation = new ResourceLocation(
                            lootTableLocation.getNamespace(),
                            "loot_tables/" + lootTableLocation.getPath() + ".json"
                    );

                    Resource resource = resourceManager.getResource(jsonLocation).orElse(null);
                    if (resource == null) {
                        GTOCore.LOGGER.error("找不到战利品表JSON: " + jsonLocation);
                        continue;
                    }

                    // 读取战利品表JSON内容
                    String jsonText;
                    try (InputStream inputStream = resource.open()) {
                        jsonText = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                    }

                    // 解析JSON
                    JsonObject lootTableJson = JsonParser.parseString(jsonText).getAsJsonObject();

                    // 处理战利品表数据
                    List<LootItem> lootItems = processLootTable(lootTableJson);
                    allLootItems.put(tableName, lootItems);

                    // 计算统计信息
                    LootTableStats stats = calculateStats(lootItems);
                    allStats.put(tableName, stats);

                } catch (IOException e) {
                    GTOCore.LOGGER.error("处理战利品表 {} 时出错: {}", tableName, e.getMessage(), e);
                }
            }

            // 生成Markdown文档
            String markdown = generateCompleteMarkdown(allLootItems, allStats);

            // 保存到文件
            Path logDir = Paths.get("logs", "report");
            if (!Files.exists(logDir)) Files.createDirectories(logDir);

            String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
            Path reportPath = logDir.resolve("all_loot_tables_analysis_" + timestamp + ".md");

            try (BufferedWriter writer = Files.newBufferedWriter(reportPath)) {
                writer.write(markdown);
                GTOCore.LOGGER.info("所有战利品表分析已导出到: {}", reportPath.getFileName());
            }

        } catch (IOException e) {
            GTOCore.LOGGER.error("导出战利品表时出错: {}", e.getMessage(), e);
        }
    }

    /**
     * 处理战利品表JSON数据
     */
    private static List<LootItem> processLootTable(JsonObject lootTableJson) {
        List<LootItem> lootItems = new ArrayList<>();

        // 检查是否有pools数组
        if (lootTableJson.has("pools") && lootTableJson.get("pools").isJsonArray()) {
            for (JsonElement poolElement : lootTableJson.getAsJsonArray("pools")) {
                JsonObject pool = poolElement.getAsJsonObject();

                // 获取rolls值（抽取次数）
                double rolls = getAverageValue(pool.get("rolls"));

                // 处理entries
                if (pool.has("entries") && pool.get("entries").isJsonArray()) {
                    for (JsonElement entryElement : pool.getAsJsonArray("entries")) {
                        JsonObject entry = entryElement.getAsJsonObject();

                        // 只处理物品类型的entry
                        if (entry.has("type") && "minecraft:item".equals(entry.get("type").getAsString())) {
                            LootItem lootItem = new LootItem();

                            // 获取物品ID
                            if (entry.has("name")) {
                                lootItem.itemId = entry.get("name").getAsString();
                            }

                            // 获取数量
                            lootItem.count = 1.0; // 默认值
                            if (entry.has("functions")) {
                                for (JsonElement functionElement : entry.getAsJsonArray("functions")) {
                                    JsonObject function = functionElement.getAsJsonObject();
                                    if (function.has("function") &&
                                            "minecraft:set_count".equals(function.get("function").getAsString())) {
                                        if (function.has("count")) {
                                            lootItem.count = getAverageValue(function.get("count"));
                                        }
                                        break; // 只处理第一个set_count函数
                                    }
                                }
                            }

                            // 获取权重
                            lootItem.weight = 1.0; // 默认权重
                            if (entry.has("weight")) {
                                lootItem.weight = entry.get("weight").getAsDouble();
                            }

                            // 计算概率和期望
                            lootItem.rolls = rolls;
                            lootItems.add(lootItem);
                        }
                    }
                }
            }
        }

        return lootItems;
    }

    /**
     * 从JsonElement获取平均值（处理范围和固定值）
     */
    private static double getAverageValue(JsonElement element) {
        if (element.isJsonObject()) {
            JsonObject obj = element.getAsJsonObject();
            if (obj.has("min") && obj.has("max")) {
                return (obj.get("min").getAsDouble() + obj.get("max").getAsDouble()) / 2.0;
            } else if (obj.has("value")) {
                return obj.get("value").getAsDouble();
            }
        } else if (element.isJsonPrimitive()) {
            return element.getAsDouble();
        }
        return 1.0; // 默认值
    }

    /**
     * 计算战利品表的统计信息
     */
    private static LootTableStats calculateStats(List<LootItem> lootItems) {
        LootTableStats stats = new LootTableStats();

        // 计算总权重
        double totalWeight = lootItems.stream().mapToDouble(item -> item.weight).sum();
        stats.totalWeight = totalWeight;

        // 计算每个物品的概率和期望
        for (LootItem item : lootItems) {
            // 概率 = (物品权重 / 总权重) * 抽取次数
            item.probability = (item.weight / totalWeight) * item.rolls;

            // 期望 = 概率 * 数量
            item.expectedValue = item.probability * item.count;

            // 累加总期望
            stats.totalExpectedValue += item.expectedValue;
        }

        // 按概率排序
        lootItems.sort((a, b) -> Double.compare(b.probability, a.probability));

        return stats;
    }

    /**
     * 生成完整的Markdown文档
     */
    private static String generateCompleteMarkdown(Map<String, List<LootItem>> allLootItems,
                                                   Map<String, LootTableStats> allStats) {
        StringBuilder markdown = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#.##%");
        DecimalFormat df2 = new DecimalFormat("#.##");

        // 标题
        markdown.append("# 战利品表分析报告\n\n");
        markdown.append("生成时间: ").append(new Date()).append("\n\n");

        // 生成每个战利品表的详细分析
        for (String tableName : allLootItems.keySet()) {
            List<LootItem> lootItems = allLootItems.get(tableName);
            LootTableStats stats = allStats.get(tableName);

            markdown.append("## ").append(tableName).append("\n\n");

            // 统计信息
            markdown.append("**统计信息:**\n");
            markdown.append("- 物品类型总数: ").append(lootItems.size()).append("\n");
            markdown.append("- 总权重: ").append(df2.format(stats.totalWeight)).append("\n");
            markdown.append("- 每次抽取期望值: ").append(df2.format(stats.totalExpectedValue)).append("\n\n");

            // 表格标题
            markdown.append("| 物品 | 数量 | 权重 | 概率 | 期望 |\n");
            markdown.append("|------|------|------|------|------|\n");

            // 表格内容
            for (LootItem item : lootItems) {
                String itemName;
                try {
                    itemName = RegistriesUtils.getItemStack(item.itemId).getHoverName().getString();
                } catch (Exception e) {
                    itemName = item.itemId;
                }

                markdown.append("| `")
                        .append(itemName)
                        .append("` | ")
                        .append(df2.format(item.count))
                        .append(" | ")
                        .append(df2.format(item.weight))
                        .append(" | ")
                        .append(df.format(item.probability))
                        .append(" | ")
                        .append(df2.format(item.expectedValue))
                        .append(" |\n");
            }

            markdown.append("\n");
        }

        // 生成汇总表格
        markdown.append("## 战利品表汇总比较\n\n");
        markdown.append("| 战利品表 | 物品类型数 | 总权重 | 每次抽取期望值 |\n");
        markdown.append("|----------|-----------|--------|----------------|\n");

        for (String tableName : allStats.keySet()) {
            LootTableStats stats = allStats.get(tableName);
            markdown.append("| `")
                    .append(tableName)
                    .append("` | ")
                    .append(allLootItems.get(tableName).size())
                    .append(" | ")
                    .append(df2.format(stats.totalWeight))
                    .append(" | ")
                    .append(df2.format(stats.totalExpectedValue))
                    .append(" |\n");
        }

        return markdown.toString();
    }

    /**
     * 调用函数：导出所有战利品表分析
     */
    public static void exportAllLootTables() {
        exportAllLootTablesToMarkdown();
    }

    /**
     * 内部类：表示战利品项
     */
    private static class LootItem {
        String itemId;
        double count;
        double weight;
        double rolls;
        double probability;
        double expectedValue;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LootItem lootItem = (LootItem) o;
            return Double.compare(lootItem.count, count) == 0 &&
                    Double.compare(lootItem.weight, weight) == 0 &&
                    Double.compare(lootItem.rolls, rolls) == 0 &&
                    Double.compare(lootItem.probability, probability) == 0 &&
                    Double.compare(lootItem.expectedValue, expectedValue) == 0 &&
                    Objects.equals(itemId, lootItem.itemId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(itemId, count, weight, rolls, probability, expectedValue);
        }
    }

    /**
     * 内部类：表示战利品表统计信息
     */
    private static class LootTableStats {
        double totalWeight;
        double totalExpectedValue;
    }
}