package com.gtocore.api.report;

import com.gtolib.GTOCore;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class EnchantmentStorage {

    /**
     * 内部数据类，用于存储附魔相关信息
     */
    public record EnchantmentData(int maxLevel, String translationKey) {

        @Override
        public @NotNull String toString() {
            return "EnchantmentData{maxLevel=" + maxLevel + ", translationKey='" + translationKey + "'}";
        }
    }

    /**
     * 从注册表获取所有附魔的ID(字符串格式)、最高等级和翻译键
     *
     * @return 包含附魔ID(字符串)、对应最高等级和翻译键的Map
     */
    public static Map<String, EnchantmentData> getAllEnchantmentsWithData() {
        Map<String, EnchantmentData> enchantmentMap = new HashMap<>();

        // 遍历所有已注册的附魔
        for (Map.Entry<ResourceKey<Enchantment>, Enchantment> entry : BuiltInRegistries.ENCHANTMENT.entrySet()) {
            ResourceKey<Enchantment> key = entry.getKey();
            Enchantment enchantment = entry.getValue();

            // 获取ResourceKey中的ResourceLocation，然后转换为字符串
            ResourceLocation location = key.location();
            String enchantmentId = location.toString();

            // 获取附魔的最大等级
            int maxLevel = enchantment.getMaxLevel();

            // 获取附魔的翻译键
            String translationKey = enchantment.getDescriptionId();

            // 添加到结果Map中
            enchantmentMap.put(enchantmentId, new EnchantmentData(maxLevel, translationKey));
        }

        return enchantmentMap;
    }

    /**
     * 从注册表获取所有附魔的ID(字符串格式)和最高等级
     * (保持向后兼容)
     *
     * @return 包含附魔ID(字符串)和对应最高等级的Map
     */
    public static Map<String, Integer> getAllEnchantmentsWithMaxLevel() {
        Map<String, Integer> resultMap = new HashMap<>();
        Map<String, EnchantmentData> fullData = getAllEnchantmentsWithData();

        for (Map.Entry<String, EnchantmentData> entry : fullData.entrySet()) {
            resultMap.put(entry.getKey(), entry.getValue().maxLevel);
        }

        return resultMap;
    }

    /**
     * 将附魔列表打印为CSV格式并保存到指定目录，同时生成转置的数组格式到txt文件
     */
    public static void getEnchantmentsReport() {
        try {
            Map<String, EnchantmentData> enchantments = getAllEnchantmentsWithData();
            Path logDir = Paths.get("logs", "report");

            // 确保目录存在
            if (!Files.exists(logDir)) {
                Files.createDirectories(logDir);
            }

            // 生成文件名
            String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
            String csvFileName = "enchantments_" + timestamp + ".csv";
            String txtFileName = "enchantments_array_" + timestamp + ".txt";
            Path csvFilePath = logDir.resolve(csvFileName);
            Path txtFilePath = logDir.resolve(txtFileName);

            // 准备转置数据
            List<String> enchantmentIds = new ArrayList<>();
            List<Integer> maxLevels = new ArrayList<>();
            List<String> translationKeys = new ArrayList<>();
            List<String> localizedNames = new ArrayList<>();
            List<String> simplifiedIds = new ArrayList<>();

            // 使用try-with-resources确保资源正确关闭
            try (PrintWriter csvWriter = new PrintWriter(new FileWriter(csvFilePath.toFile()));
                    PrintWriter txtWriter = new PrintWriter(new FileWriter(txtFilePath.toFile()))) {

                // 写入CSV表头
                csvWriter.println("Enchantment ID,Max Level,Translation Key,Localized Name,Simplified ID");

                AtomicInteger i = new AtomicInteger();

                // 按字母顺序排序并写入数据
                enchantments.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .forEach(entry -> {
                            String enchantmentId = entry.getKey();
                            EnchantmentData data = entry.getValue();
                            String localizedName = I18n.get(data.translationKey);
                            String simplifiedId = enchantmentId.substring(enchantmentId.indexOf(':') + 1);

                            // 写入CSV行
                            csvWriter.println(enchantmentId + "," +
                                    data.maxLevel + "," +
                                    data.translationKey + "," +
                                    localizedName + "," +
                                    simplifiedId);

                            txtWriter.println("records.add(EnchantmentRecord.create(" +
                                    i + ",\"" +
                                    enchantmentId + "\"," +
                                    data.maxLevel + ",\"" +
                                    localizedName + "\",\"" +
                                    data.translationKey + "\"));");
                            i.getAndIncrement();

                            // 收集数据用于转置
                            enchantmentIds.add(enchantmentId);
                            maxLevels.add(data.maxLevel); // 直接添加整数
                            translationKeys.add(data.translationKey);
                            localizedNames.add(localizedName);
                            simplifiedIds.add(simplifiedId);
                        });

                // 写入转置的数组到txt文件
                txtWriter.println("// Enchantment IDs");
                writeStringArrayToFile(txtWriter, "enchantmentIds", enchantmentIds);

                txtWriter.println("\n// Max Levels");
                writeIntArrayToFile(txtWriter, "maxLevels", maxLevels);

                txtWriter.println("\n// Translation Keys");
                writeStringArrayToFile(txtWriter, "translationKey", translationKeys);

                txtWriter.println("\n// Localized Names");
                writeStringArrayToFile(txtWriter, "localizedNames", localizedNames);

                txtWriter.println("\n// Simplified IDs");
                writeStringArrayToFile(txtWriter, "simplifiedId", simplifiedIds);

                GTOCore.LOGGER.info("附魔列表已成功导出到: run/logs/report/{}", csvFileName);
                GTOCore.LOGGER.info("附魔数组已成功导出到: run/logs/report/{}", txtFileName);
            }
        } catch (IOException e) {
            GTOCore.LOGGER.error("附魔统计报告失败", e);
        }
    }

    /**
     * 使用StringJoiner优化字符串数组写入
     */
    private static void writeStringArrayToFile(PrintWriter writer, String arrayName, List<String> list) {
        StringJoiner joiner = new StringJoiner(", ", "String[] " + arrayName + " = {", "};");
        for (String item : list) {
            joiner.add("\"" + item.replace("\\", "\\\\").replace("\"", "\\\"") + "\"");
        }
        writer.println(joiner);
    }

    /**
     * 使用StringJoiner优化整数数组写入
     */
    private static void writeIntArrayToFile(PrintWriter writer, String arrayName, List<Integer> list) {
        StringJoiner joiner = new StringJoiner(", ", "int[] " + arrayName + " = {", "};");
        for (Integer item : list) {
            joiner.add(item.toString());
        }
        writer.println(joiner);
    }
}
