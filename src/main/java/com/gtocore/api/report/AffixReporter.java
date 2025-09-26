package com.gtocore.api.report;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import dev.shadowsoffire.apotheosis.adventure.affix.*;
import dev.shadowsoffire.apotheosis.adventure.loot.LootCategory;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import dev.shadowsoffire.apotheosis.adventure.loot.RarityRegistry;
import dev.shadowsoffire.placebo.reload.DynamicHolder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;

public class AffixReporter {

    private static final Path LOG_DIR = Paths.get("logs", "report");

    public static void getAffixReporter() {
        exportAllAffixesToFile();
        exportCompleteAffixReport();
        exportAllAffixesToArrays();
    }

    /**
     * 导出所有词缀信息到TXT文件
     */
    private static void exportAllAffixesToFile() {
        try {
            // 确保目录存在
            if (!Files.exists(LOG_DIR)) {
                Files.createDirectories(LOG_DIR);
            }

            // 创建带时间戳的文件名
            String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
            Path outputFile = LOG_DIR.resolve("affixes_report_" + timestamp + ".txt");

            // 写入文件
            try (BufferedWriter writer = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

                writer.write("=== 词缀系统报告 ===\n");
                writer.write("生成时间: " + new Date() + "\n\n");

                // 写入所有词缀信息
                writer.write("=== 所有已注册词缀 ===\n");
                Collection<Affix> allAffixes = AffixRegistry.INSTANCE.getValues();
                writer.write("词缀总数: " + allAffixes.size() + "\n\n");

                List<String> affixId = new ArrayList<>();
                List<String> affixType = new ArrayList<>();
                List<String> affixPrefixName = new ArrayList<>();
                List<String> affixSuffixName = new ArrayList<>();

                int i = 0;

                for (Affix affix : allAffixes) {
                    ResourceLocation id = AffixRegistry.INSTANCE.getKey(affix);

                    writer.write("records.add(ApotheosisAffixRecord.create(" +
                            i + ",\"" +
                            id.toString() + "\",\"" +
                            affix.getName(true).getString() + " · " + affix.getName(false).getString() + "\",\"" +
                            affix.getName(true).getString() + " · " + affix.getName(false).getString() + "\"));\n");
                    i++;

                    affixId.add(id.toString());
                    affixType.add(String.valueOf(affix.getType()));
                    String prefixName = affix.getName(true).getString();
                    String suffixName = affix.getName(false).getString();
                    affixPrefixName.add(prefixName);
                    affixSuffixName.add(suffixName);
                }
                writer.write("---\n");

                writer.write("\n\n=== 按数组输出的词缀 ===\n");
                writeStringArrayToFile(writer, "affixId", affixId);
                writeStringArrayToFile(writer, "affixType", affixType);
                writeStringArrayToFile(writer, "affixPrefixName", affixPrefixName);
                writeStringArrayToFile(writer, "affixSuffixName", affixSuffixName);

                // 按类型分类写入词缀
                writer.write("\n\n=== 按类型分类的词缀 ===\n");
                for (AffixType type : AffixType.values()) {
                    Collection<DynamicHolder<Affix>> affixesOfType = AffixHelper.byType(type);

                    if (!affixesOfType.isEmpty()) {
                        writer.write(type + "类型词缀 (" + affixesOfType.size() + "个):\n");
                        for (DynamicHolder<Affix> holder : affixesOfType) {
                            if (holder.isBound()) {
                                writer.write("  - " + holder.getId() + "\n");
                            }
                        }
                        writer.write("---\n");
                    }
                }

                // 添加稀有度信息
                writer.write("\n\n=== 稀有度信息 ===\n");
                for (LootRarity rarity : RarityRegistry.INSTANCE.getValues()) {
                    writer.write("稀有度: " + RarityRegistry.INSTANCE.getKey(rarity) + " (权重: " + rarity.getWeight() + ")\n");
                }

                writer.write("\n报告生成完成。\n");
            }

            System.out.println("词缀报告已保存到: " + outputFile.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("保存词缀报告时出错: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("生成词缀报告时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 导出物品词缀信息到TXT文件
     * 
     * @param stack 要检查的物品堆栈
     */
    private static void exportItemAffixesToFile(ItemStack stack) {
        try {
            // 确保目录存在
            if (!Files.exists(LOG_DIR)) {
                Files.createDirectories(LOG_DIR);
            }

            // 创建带时间戳的文件名
            String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
            String itemName = stack.getDisplayName().getString().replaceAll("[^a-zA-Z0-9.-]", "_");
            Path outputFile = LOG_DIR.resolve("item_affixes_" + itemName + "_" + timestamp + ".txt");

            // 写入文件
            try (BufferedWriter writer = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

                writer.write("=== 物品词缀报告 ===\n");
                writer.write("生成时间: " + new Date() + "\n\n");

                writer.write("物品: " + stack.getDisplayName().getString() + "\n");
                writer.write("物品ID: " + stack.getItem().getDescriptionId() + "\n");

                // 获取物品类别
                LootCategory category = LootCategory.forItem(stack);
                writer.write("物品类别: " + category.getName() + "\n");

                // 获取稀有度
                DynamicHolder<LootRarity> rarity = AffixHelper.getRarity(stack);
                writer.write("稀有度: " + (rarity.isBound() ? rarity.getId() : "未知") + "\n");

                // 获取词缀
                Map<DynamicHolder<? extends Affix>, AffixInstance> affixes = AffixHelper.getAffixes(stack);

                if (affixes.isEmpty()) {
                    writer.write("此物品没有任何词缀!\n");
                } else {
                    writer.write("词缀列表 (" + affixes.size() + "个):\n");
                    for (Map.Entry<DynamicHolder<? extends Affix>, AffixInstance> entry : affixes.entrySet()) {
                        AffixInstance instance = entry.getValue();
                        if (instance.isValid()) {
                            writer.write("  - " + instance.affix().getId() +
                                    " (等级: " + instance.level() + ")\n");
                            writer.write("    类型: " + instance.affix().get().getType() + "\n");
                            writer.write("    描述: " + instance.getDescription().getString() + "\n");
                        } else {
                            writer.write("  - " + instance.affix().getId() + " (无效)\n");
                        }
                    }
                }

                writer.write("\n报告生成完成。\n");
            }

            System.out.println("物品词缀报告已保存到: " + outputFile.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("保存物品词缀报告时出错: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("生成物品词缀报告时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 导出所有词缀信息，包括按类型分类
     */
    private static void exportCompleteAffixReport() {
        try {
            // 确保目录存在
            if (!Files.exists(LOG_DIR)) {
                Files.createDirectories(LOG_DIR);
            }

            // 创建带时间戳的文件名
            String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
            Path outputFile = LOG_DIR.resolve("affix_complete_report_" + timestamp + ".txt");

            // 写入文件
            try (BufferedWriter writer = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

                writer.write("=== 完整词缀系统报告 ===\n");
                writer.write("生成时间: " + new Date() + "\n\n");

                // 1. 所有词缀概览
                writer.write("=== 所有词缀概览 ===\n");
                Collection<Affix> allAffixes = AffixRegistry.INSTANCE.getValues();
                writer.write("词缀总数: " + allAffixes.size() + "\n\n");

                // 获取默认稀有度用于描述生成
                LootRarity defaultRarity = RarityRegistry.getMinRarity().get();

                // 2. 按类型分类的详细词缀信息
                writer.write("\n=== 按类型分类的词缀详情 ===\n");
                for (AffixType type : AffixType.values()) {
                    Collection<DynamicHolder<Affix>> affixesOfType = AffixHelper.byType(type);

                    if (!affixesOfType.isEmpty()) {
                        writer.write("\n" + type + "类型词缀 (" + affixesOfType.size() + "个):\n");
                        writer.write("=".repeat(50) + "\n");

                        for (DynamicHolder<Affix> holder : affixesOfType) {
                            if (holder.isBound()) {
                                Affix affix = holder.get();
                                writer.write("词缀ID: " + holder.getId() + "\n");
                                writer.write("  类型: " + affix.getType() + "\n");

                                // 获取名称（前缀和后缀）
                                String prefixName = affix.getName(true).getString();
                                String suffixName = affix.getName(false).getString();
                                writer.write("  前缀名称: " + prefixName + "\n");
                                writer.write("  后缀名称: " + suffixName + "\n");

                                writer.write("-".repeat(30) + "\n");
                            }
                        }
                    }
                }

                // 3. 稀有度信息
                writer.write("\n\n=== 稀有度信息 ===\n");
                for (LootRarity rarity : RarityRegistry.INSTANCE.getValues()) {
                    writer.write("稀有度: " + RarityRegistry.INSTANCE.getKey(rarity) +
                            " (权重: " + rarity.getWeight() +
                            ", 颜色: " + rarity.getColor() + ")\n");
                }

                writer.write("\n报告生成完成。\n");
            }

            System.out.println("完整词缀报告已保存到: " + outputFile.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("保存完整词缀报告时出错: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("生成完整词缀报告时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String[][] exportAllAffixesToArrays() {
        try {
            // 获取所有已注册词缀
            Collection<Affix> allAffixes = AffixRegistry.INSTANCE.getValues();
            int affixCount = allAffixes.size();

            // 创建四个数组来存储不同类型的词缀信息
            String[] affixIds = new String[affixCount];
            String[] affixTypes = new String[affixCount];
            String[] prefixNames = new String[affixCount];
            String[] suffixNames = new String[affixCount];

            // 填充数组
            int index = 0;
            for (Affix affix : allAffixes) {
                ResourceLocation id = AffixRegistry.INSTANCE.getKey(affix);
                affixIds[index] = id.toString();
                affixTypes[index] = affix.getType().toString();
                prefixNames[index] = affix.getName(true).getString();
                suffixNames[index] = affix.getName(false).getString();
                index++;
            }

            // 将所有数组合并到一个二维数组中返回
            return new String[][] { affixIds, affixTypes, prefixNames, suffixNames };

        } catch (Exception e) {
            System.err.println("提取词缀信息时出错: " + e.getMessage());
            e.printStackTrace();
            return new String[4][0]; // 返回空数组
        }
    }

    /**
     * 简单方法：打印所有词缀到控制台
     */
    public static void printAllAffixes() {
        System.out.println("=== 所有已注册词缀 ===");

        // 获取所有词缀
        Collection<Affix> allAffixes = AffixRegistry.INSTANCE.getValues();

        if (allAffixes.isEmpty()) {
            System.out.println("没有找到任何词缀!");
            return;
        }

        // 获取默认稀有度用于描述生成
        LootRarity defaultRarity = RarityRegistry.getMinRarity().get();

        for (Affix affix : allAffixes) {
            ResourceLocation id = AffixRegistry.INSTANCE.getKey(affix);
            System.out.println("词缀ID: " + id);
            System.out.println("  类型: " + affix.getType());

            try {
                String description = affix.getDescription(ItemStack.EMPTY, defaultRarity, 1.0f).getString();
                System.out.println("  描述: " + description);
            } catch (Exception e) {
                System.out.println("  描述: [获取失败: " + e.getMessage() + "]");
            }

            System.out.println("---");
        }

        System.out.println("总计: " + allAffixes.size() + " 个词缀");
    }

    /**
     * 使用StringJoiner优化字符串数组写入
     */
    private static void writeStringArrayToFile(BufferedWriter writer, String arrayName, List<String> list) throws IOException {
        StringJoiner joiner = new StringJoiner(", ", "String[] " + arrayName + " = {", "};");
        for (String item : list) {
            joiner.add("\"" + item.replace("\\", "\\\\").replace("\"", "\\\"") + "\"");
        }
        writer.write(joiner.toString());
        writer.write("\n");
    }
}
