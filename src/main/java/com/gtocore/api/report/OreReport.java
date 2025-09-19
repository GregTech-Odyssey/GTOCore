package com.gtocore.api.report;

import com.gtocore.common.data.GTOMaterials;

import com.gtolib.GTOCore;
import com.gtolib.api.data.Dimension;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.worldgen.GTOreDefinition;
import com.gregtechceu.gtceu.api.data.worldgen.bedrockore.BedrockOreDefinition;
import com.gregtechceu.gtceu.api.data.worldgen.bedrockore.WeightedMaterial;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class OreReport {

    // 矿石数据内部类
    private static class OreData {

        double totalOreWeight;
        int totalVeinWeight;
        int combinedWeight;
        float density;

        OreData(double oreWeight, int veinWeight, int combined, float density) {
            this.totalOreWeight = oreWeight;
            this.totalVeinWeight = veinWeight;
            this.combinedWeight = combined;
            this.density = density;
        }
    }

    // 维度矿石信息内部类
    private static class DimensionOreInfo {

        final Dimension dimension;
        int totalVeinWeight = 0;
        int totalCombinedWeight = 0;
        Map<Material, OreData> ores = new HashMap<>();

        DimensionOreInfo(Dimension dimension) {
            this.dimension = dimension;
        }
    }

    // 生成矿石报告的主要方法
    public static void generateOreReport() {
        try {
            // 获取材料字段映射
            Map<Material, String> materialFieldMap = getMaterialStringMap();

            // 定义所有维度
            Dimension[] ALL_DIM = { Dimension.OVERWORLD, Dimension.ANCIENT_WORLD, Dimension.THE_NETHER,
                    Dimension.MOON, Dimension.MARS, Dimension.VENUS, Dimension.MERCURY,
                    Dimension.GLACIO, Dimension.TITAN, Dimension.PLUTO, Dimension.IO,
                    Dimension.GANYMEDE, Dimension.ENCELADUS, Dimension.CERES,
                    Dimension.BARNARDA_C, Dimension.OTHERSIDE };

            // 计算每个矿脉的均衡权重
            Map<ResourceLocation, Map<Material, Double>> normalizedOreWeights = calculateNormalizedWeights();

            // 处理所有矿脉定义
            Map<ResourceLocation, DimensionOreInfo> dimensionOreInfoMap = processOreDefinitions(ALL_DIM, normalizedOreWeights);

            // 生成并输出报告
            generateReports(dimensionOreInfoMap, materialFieldMap);

            GTOCore.LOGGER.info("矿石统计报告已生成");
        } catch (Exception e) {
            GTOCore.LOGGER.error("生成矿石统计报告失败", e);
        }
    }

    // 计算每个矿脉的均衡权重
    private static Map<ResourceLocation, Map<Material, Double>> calculateNormalizedWeights() {
        Map<ResourceLocation, Map<Material, Double>> normalizedOreWeights = new HashMap<>();

        for (Map.Entry<ResourceLocation, BedrockOreDefinition> entry : GTRegistries.BEDROCK_ORE_DEFINITIONS.entries()) {
            ResourceLocation veinId = entry.getKey();
            BedrockOreDefinition bedrockOre = entry.getValue();

            int totalOreWeightInVein = bedrockOre.materials().stream()
                    .mapToInt(WeightedMaterial::weight)
                    .sum();

            Map<Material, Double> weights = new HashMap<>();
            for (WeightedMaterial wm : bedrockOre.materials()) {
                double normalizedWeight = (double) wm.weight() / totalOreWeightInVein * 100.0;
                weights.put(wm.material(), normalizedWeight);
            }
            normalizedOreWeights.put(veinId, weights);
        }

        return normalizedOreWeights;
    }

    // 处理所有矿脉定义
    private static Map<ResourceLocation, DimensionOreInfo> processOreDefinitions(
                                                                                 Dimension[] ALL_DIM, Map<ResourceLocation, Map<Material, Double>> normalizedOreWeights) {
        Map<ResourceLocation, DimensionOreInfo> dimensionOreInfoMap = new HashMap<>();

        for (Map.Entry<ResourceLocation, BedrockOreDefinition> entry : GTRegistries.BEDROCK_ORE_DEFINITIONS.entries()) {
            ResourceLocation veinId = entry.getKey();
            BedrockOreDefinition bedrockOre = entry.getValue();
            int veinWeight = bedrockOre.weight();

            Map<Material, Double> normalizedWeights = normalizedOreWeights.get(veinId);

            float density;
            GTOreDefinition oreDefinition = GTRegistries.ORE_VEINS.get(veinId);
            if (oreDefinition != null) {
                density = oreDefinition.density();
            } else {
                density = 1.0f;
            }

            for (ResourceKey<Level> dimensionKey : bedrockOre.dimensionFilter()) {
                ResourceLocation dimensionId = dimensionKey.location();

                Dimension dim = Arrays.stream(ALL_DIM)
                        .filter(d -> d.getLocation().equals(dimensionId))
                        .findFirst()
                        .orElse(null);

                DimensionOreInfo oreInfo = dimensionOreInfoMap.computeIfAbsent(
                        dimensionId, k -> new DimensionOreInfo(dim));

                oreInfo.totalVeinWeight += veinWeight;

                for (WeightedMaterial wm : bedrockOre.materials()) {
                    Material material = wm.material();
                    double oreWeight = normalizedWeights.getOrDefault(material, 0.0);
                    int combinedWeight = (int) (oreWeight * veinWeight * density);
                    oreInfo.totalCombinedWeight += combinedWeight;

                    oreInfo.ores.compute(material, (k, v) -> {
                        if (v == null) {
                            return new OreData(oreWeight, veinWeight, combinedWeight, density);
                        }
                        // 更新现有矿石数据
                        v.totalOreWeight += oreWeight;
                        v.totalVeinWeight += veinWeight;
                        v.combinedWeight += combinedWeight;
                        return v;
                    });
                }
            }
        }

        return dimensionOreInfoMap;
    }

    // 生成并输出报告
    private static void generateReports(
                                        Map<ResourceLocation, DimensionOreInfo> dimensionOreInfoMap,
                                        Map<Material, String> materialFieldMap) {
        try {
            StringBuilder report = new StringBuilder("\n# ***维度矿石统计报告***");
            StringBuilder report_arrays = new StringBuilder("***维度矿石统计输出(数组形式)***");
            StringBuilder report_table = new StringBuilder("***维度矿石统计输出(表格形式)***");
            StringBuilder report_meteorite_recipe = new StringBuilder("***陨星配方***");
            StringBuilder report_meteorite_recipea = new StringBuilder("\n\n//Material组").append("\nMaterial[][] materials = new Material[][] {");
            StringBuilder report_meteorite_recipeb = new StringBuilder("\n\n//int组").append("\nint[][] material_weights = new int[][] {");

            Dimension[] ALL_DIM = { Dimension.OVERWORLD, Dimension.ANCIENT_WORLD,
                    Dimension.THE_NETHER, Dimension.MOON, Dimension.MARS, Dimension.VENUS,
                    Dimension.MERCURY, Dimension.CERES, Dimension.IO, Dimension.GANYMEDE,
                    Dimension.ENCELADUS, Dimension.TITAN, Dimension.PLUTO, Dimension.GLACIO,
                    Dimension.BARNARDA_C, Dimension.OTHERSIDE };

            // 遍历ALL_DIM数组，按其定义的顺序处理每个维度
            for (Dimension dim : ALL_DIM) {
                ResourceLocation dimId = dim.getLocation();
                DimensionOreInfo info = dimensionOreInfoMap.get(dimId);
                if (info == null) {
                    report.append("\n\n## **维度**: ").append(dim.getEn()).append(" (无矿石数据)");
                    continue;
                }

                // 以下逻辑与原代码一致，使用当前维度的数据生成报告
                String dimName = String.format("%s (%s)", dim.getEn(), dim.getCn());
                // 生成Markdown报告
                generateMarkdownReport(report, dimId, info, dim, dimName, materialFieldMap);
                // 生成数组形式报告
                generateArrayReport(report_arrays, dimId, info, dimName, materialFieldMap);
                // 生成表格形式报告
                generateTableReport(report_table, dimId, info, dimName, materialFieldMap);
                // 生成陨星配方
                generateMeteoriteRecipe(report_meteorite_recipea, report_meteorite_recipeb, dimId, info, dimName, materialFieldMap);
            }

            report.append("\n\n****报告结束****");
            report_meteorite_recipe.append(report_meteorite_recipea).append("\n};\n\n");
            report_meteorite_recipe.append(report_meteorite_recipeb).append("\n};\n\n");

            writeReportsToFiles(report, report_arrays, report_table, report_meteorite_recipe);

        } catch (Exception e) {
            GTOCore.LOGGER.error("生成报告时发生错误", e);
        }
    }

    // 生成Markdown报告
    private static void generateMarkdownReport(
                                               StringBuilder report, ResourceLocation dimId, DimensionOreInfo info,
                                               Dimension dim, String dimName, Map<Material, String> materialFieldMap) {
        report.append("\n\n## **维度**: ").append(dimName).append("  ");
        report.append("\n**ID**: ").append(dimId).append("  ");

        if (dim != null) {
            report.append("\n**层级**: ").append(dim.getTier()).append("  ");
            report.append("\n**星系**: ").append(dim.getGalaxy()).append("  ");
            report.append("\n**轨道**: ").append(dim.getOrbit()).append("  ");
        }
        report.append("\n**总矿脉权重**: ").append(info.totalVeinWeight).append("  ");
        report.append("\n**总联合权重**: ").append(info.totalCombinedWeight).append("  ");

        report.append("\n\n### **矿石列表**:  ");
        report.append("\n|序号|矿石名称|  |矿石权重|矿脉权重|矿脉密度|联合权重|");
        report.append("\n|-----|----------|--------------------|------|------|------|------|");

        AtomicInteger index = new AtomicInteger(1);
        info.ores.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue().combinedWeight, e1.getValue().combinedWeight))
                .forEach(e -> {
                    Material material = e.getKey();
                    OreData data = e.getValue();
                    int currentIndex = index.getAndIncrement();
                    String fieldName = materialFieldMap.getOrDefault(material, material.getName());
                    report.append(String.format(
                            "\n| %d | %s | %s | %s | %d | %s | %d |",
                            currentIndex,
                            I18n.get(material.getUnlocalizedName()),
                            fieldName,
                            String.format("%.1f", data.totalOreWeight),
                            data.totalVeinWeight,
                            String.format("%.0f%%", data.density * 100),
                            data.combinedWeight));
                });
    }

    // 生成数组形式报告
    private static void generateArrayReport(
                                            StringBuilder report_arrays, ResourceLocation dimId,
                                            DimensionOreInfo info, String dimName,
                                            Map<Material, String> materialFieldMap) {
        // 为当前维度创建矿石组合权重映射
        Object2IntMap<Material> oreMap = new Object2IntOpenHashMap<>();
        info.ores.forEach((material, oreData) -> oreMap.put(material, oreData.combinedWeight));

        report_arrays.append("\n\n维度: ").append(dimName);
        report_arrays.append("\nID: ").append(dimId);
        List<String> materialNames = new ArrayList<>();
        List<Integer> weights = new ArrayList<>();
        List<String> localizationKeys = new ArrayList<>();

        for (var entry2 : oreMap.object2IntEntrySet()) {
            Material material = entry2.getKey();
            materialNames.add(materialFieldMap.getOrDefault(material, material.getName()));
            weights.add(entry2.getIntValue());
            localizationKeys.add(I18n.get(material.getUnlocalizedName()));
        }

        String materialNamesStr = materialNames.stream().collect(Collectors.joining(", ", "new Material[] { ", " }"));
        String weightsStr = weights.stream().map(Object::toString).collect(Collectors.joining(", ", "new int[] { ", " }"));
        String localizationKeysStr = localizationKeys.stream().collect(Collectors.joining(", ", "{ ", " }"));

        report_arrays.append("\n材料名称数组: \n").append(materialNamesStr);
        report_arrays.append("\n权重数组: \n").append(weightsStr);
        report_arrays.append("\n本地化键数组: \n").append(localizationKeysStr);
    }

    // 生成表格形式报告
    private static void generateTableReport(
                                            StringBuilder report_table, ResourceLocation dimId,
                                            DimensionOreInfo info, String dimName,
                                            Map<Material, String> materialFieldMap) {
        // 为当前维度创建矿石组合权重映射
        Object2IntMap<Material> oreMap = new Object2IntOpenHashMap<>();
        info.ores.forEach((material, oreData) -> oreMap.put(material, oreData.combinedWeight));

        report_table.append("\n\n维度: ").append(dimName);
        report_table.append("\nID: ").append(dimId);
        report_table.append("\n材料名称,权重,本地化键\n");

        for (var entry3 : oreMap.object2IntEntrySet()) {
            Material material = entry3.getKey();
            String fieldName = materialFieldMap.getOrDefault(material, material.getName());
            report_table.append(String.format("%s,%d,%s\n",
                    fieldName,
                    entry3.getIntValue(),
                    I18n.get(material.getUnlocalizedName())));
        }
    }

    // 生成陨星配方参数
    private static void generateMeteoriteRecipe(
                                                StringBuilder report_meteorite_recipea, StringBuilder report_meteorite_recipeb, ResourceLocation dimId,
                                                DimensionOreInfo info, String dimName,
                                                Map<Material, String> materialFieldMap) {
        // 为当前维度创建矿石组合权重映射
        Object2IntMap<Material> oreMap = new Object2IntOpenHashMap<>();
        info.ores.forEach((material, oreData) -> oreMap.put(material, oreData.combinedWeight));

        // 计算权重中位数
        List<Integer> weightValues = new ArrayList<>(oreMap.values());
        Collections.sort(weightValues);
        int medianWeight = weightValues.get(weightValues.size() / 2);

        // 筛选出权重低于中位数的矿石（即权重偏低的一半矿石）
        Object2IntMap<Material> lowWeightOres = new Object2IntOpenHashMap<>();
        for (Object2IntMap.Entry<Material> entry : oreMap.object2IntEntrySet()) {
            if (entry.getIntValue() < medianWeight) {
                lowWeightOres.put(entry.getKey(), entry.getIntValue());
            }
        }

        // 准备材料名称和权重数组
        List<String> allMaterialNames = new ArrayList<>();
        List<Integer> allWeights = new ArrayList<>();
        List<String> lowWeightMaterialNames = new ArrayList<>();
        List<Integer> lowWeights = new ArrayList<>();

        for (Object2IntMap.Entry<Material> entry : oreMap.object2IntEntrySet()) {
            Material material = entry.getKey();
            String fieldName = materialFieldMap.getOrDefault(material, material.getName());
            allMaterialNames.add(fieldName);
            allWeights.add(entry.getIntValue());
        }

        for (Object2IntMap.Entry<Material> entry : lowWeightOres.object2IntEntrySet()) {
            Material material = entry.getKey();
            String fieldName = materialFieldMap.getOrDefault(material, material.getName());
            lowWeightMaterialNames.add(fieldName);
            lowWeights.add(entry.getIntValue());
        }

        // 生成数组代码
        report_meteorite_recipea.append("\n// 维度: ").append(dimName).append("--ID: ").append(dimId);
        report_meteorite_recipeb.append("\n// 维度: ").append(dimName).append("--ID: ").append(dimId);

        // 所有矿石的材料数组和权重数组
        report_meteorite_recipea.append("\nnew Material[] { ").append(String.join(", ", allMaterialNames)).append(" },");
        report_meteorite_recipeb.append("\nnew int[] { ").append(allWeights.stream().map(Object::toString).collect(Collectors.joining(", "))).append(" },");

        // 低权重矿石的材料数组和权重数组
        report_meteorite_recipea.append("\nnew Material[] { ").append(String.join(", ", lowWeightMaterialNames)).append(" },");
        report_meteorite_recipeb.append("\nnew int[] { ").append(lowWeights.stream().map(Object::toString).collect(Collectors.joining(", "))).append(" },");
    }

    // 将报告写入文件
    private static void writeReportsToFiles(
                                            StringBuilder report, StringBuilder report_arrays, StringBuilder report_table, StringBuilder report_meteorite_recipe) {
        try {
            Path logDir = Paths.get("logs", "report");
            if (!Files.exists(logDir)) Files.createDirectories(logDir);

            String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
            Path reportPath = logDir.resolve("ore_report_" + timestamp + ".md");
            Path reportPath_arrays = logDir.resolve("ore_report_arrays_" + timestamp + ".txt");
            Path reportPath_table = logDir.resolve("ore_report_table_" + timestamp + ".csv");
            Path reportPath_meteorite_recipe = logDir.resolve("ore_report_meteorite_recipe_" + timestamp + ".txt");

            try (BufferedWriter writer = Files.newBufferedWriter(reportPath)) {
                writer.write(report.toString());
            }

            try (BufferedWriter writer = Files.newBufferedWriter(reportPath_arrays)) {
                writer.write(report_arrays.toString());
            }

            try (BufferedWriter writer = Files.newBufferedWriter(reportPath_table, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                writer.write('\uFEFF'); // BOM for UTF-8
                writer.write(report_table.toString());
            }

            try (BufferedWriter writer = Files.newBufferedWriter(reportPath_meteorite_recipe)) {
                writer.write(report_meteorite_recipe.toString());
            }

        } catch (Exception e) {
            GTOCore.LOGGER.error("写入报告文件时发生错误", e);
        }
    }

    // 获取材料字段映射
    private static Map<Material, String> getMaterialStringMap() {
        Map<Material, String> materialFieldMap = new HashMap<>();

        // 填充GTOMaterials类的静态字段
        for (java.lang.reflect.Field field : GTOMaterials.class.getDeclaredFields()) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                if (Material.class.isAssignableFrom(field.getType())) {
                    try {
                        Material material = (Material) field.get(null);
                        if (material != null) {
                            materialFieldMap.put(material, "GTOMaterials." + field.getName());
                        }
                    } catch (IllegalAccessException e) {
                        // 忽略无法访问的字段
                    }
                }
            }
        }

        // 填充GTMaterials类的静态字段
        for (java.lang.reflect.Field field : GTMaterials.class.getDeclaredFields()) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                if (Material.class.isAssignableFrom(field.getType())) {
                    try {
                        Material material = (Material) field.get(null);
                        if (material != null && !materialFieldMap.containsKey(material)) {
                            materialFieldMap.put(material, "GTMaterials." + field.getName());
                        }
                    } catch (IllegalAccessException e) {
                        // 忽略无法访问的字段
                    }
                }
            }
        }
        return materialFieldMap;
    }
}
