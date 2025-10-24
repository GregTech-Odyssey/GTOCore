package com.gtocore.api.report;

import com.gtocore.api.data.material.GTOMaterialFlags;
import com.gtocore.client.forge.GTOComponentHandler;

import com.gtolib.GTOCore;
import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterEnumLang;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.FluidPipeProperties;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.ToolProperty;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

@DataGeneratorScanned
public class MaterialReport {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void generateReport() {
        StringBuilder report = new StringBuilder();
        StringBuilder report_json = new StringBuilder();
        StringBuilder report_table = new StringBuilder();

        // 表头
        report_table.append("中文名,英文名,符号,主颜色,次颜色,图标集,基础类型,质量,流体管道吞吐量,物品管道优先级,物品管道传输速率,是否为导线材料,导线最大安培数,导线电压,导线每米损耗,是否为工具材料,工具挖掘速度,工具耐久度,工具挖掘等级,工具附魔性,是否为无尽耐久工具,工具耐久度倍率,是否为转子材料,转子耐久度,转子功率,转子效率,爆炸温度,是否有害物质\n");

        for (Material material : GTCEuAPI.materialManager.getRegisteredMaterials()) {
            Entry entry = Entry.fromMaterial(material);

            // 生成Markdown报告
            report.append("## ").append(entry.cnName).append(" (").append(entry.enName).append(")\n");
            report.append("- 符号: ").append(entry.symbol).append("\n");
            report.append("- 主颜色: ").append(entry.hexColor1).append("\n");
            report.append("- 次颜色: ").append(entry.hexColor2).append("\n");
            report.append("- 图标集: ").append(entry.iconSetName).append("\n");
            report.append("- 基础类型: ").append(entry.baseType.cn).append(" / ").append(entry.baseType.en).append("\n");
            report.append("- 质量: ").append(entry.mass).append("\n");
            // 添加其他属性...

            report.append("\n");

            // 生成gson报告
            report_json.append(GSON.toJson(entry)).append("\n");
            // 生成CSV表格报告
            String cnNameInCsv = entry.cnName.contains(",") ? "\"" + entry.cnName + "\"" : entry.cnName;
            String enNameInCsv = entry.enName.contains(",") ? "\"" + entry.enName + "\"" : entry.enName;
            report_table.append(String.format("%s,%s,%s,%s,%s,%s,%s,%d,",
                    cnNameInCsv, enNameInCsv, entry.symbol, entry.hexColor1, entry.hexColor2,
                    entry.iconSetName, entry.baseType.en, entry.mass));
            report_table.append(entry.hasFluidPipe ? entry.fluidPipeThroughput : "").append(",");
            report_table.append(entry.hasItemPipe ? entry.itemPipePriority : "").append(",");
            report_table.append(entry.hasItemPipe ? entry.itemPipeTransferRate : "").append(",");
            report_table.append(entry.isWireMaterial ? "是" : "否").append(",");
            report_table.append(entry.isWireMaterial ? entry.wireMaxAmperes : "").append(",");
            report_table.append(entry.isWireMaterial ? entry.wireVoltage : "").append(",");
            report_table.append(entry.isWireMaterial ? entry.wireLossPerMeter : "").append(",");
            report_table.append(entry.isToolMaterial ? "是" : "否").append(",");
            report_table.append(entry.isToolMaterial ? entry.harvestSpeedTool : "").append(",");
            report_table.append(entry.isToolMaterial ? entry.durabilityTool : "").append(",");
            report_table.append(entry.isToolMaterial ? entry.harvestLevelTool : "").append(",");
            report_table.append(entry.isToolMaterial ? entry.enchantabilityTool : "").append(",");
            report_table.append(entry.isToolMaterial ? (entry.isUnbreakableTool ? "是" : "否") : "").append(",");
            report_table.append(entry.isToolMaterial ? entry.durabilityMultiplierTool : "").append(",");
            report_table.append(entry.isRotorMaterial ? "是" : "否").append(",");
            report_table.append(entry.isRotorMaterial ? entry.durabilityRotor : "").append(",");
            report_table.append(entry.isRotorMaterial ? entry.powerRotor : "").append(",");
            report_table.append(entry.isRotorMaterial ? entry.efficiencyRotor : "").append(",");
            report_table.append(entry.blastTemperature > 0 ? entry.blastTemperature : "").append(",");
            report_table.append(entry.isHazard ? "是" : "否").append("\n");
        }
        // 写入文件
        writeReportsToFiles(report, report_json, report_table);
    }

    // 将报告写入文件
    private static void writeReportsToFiles(
                                            StringBuilder report, StringBuilder json, StringBuilder report_table) {
        try {
            Path logDir = Paths.get("logs", "report");
            if (!Files.exists(logDir)) Files.createDirectories(logDir);

            String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
            Path reportPath = logDir.resolve("ore_report_" + timestamp + ".md");
            Path reportPath_arrays = logDir.resolve("mat_report_arrays_" + timestamp + ".json");
            Path reportPath_table = logDir.resolve("mat_report_table_" + timestamp + ".csv");

            try (BufferedWriter writer = Files.newBufferedWriter(reportPath)) {
                writer.write(report.toString());
            }

            try (BufferedWriter writer = Files.newBufferedWriter(reportPath_arrays)) {
                writer.write(json.toString());
            }

            try (BufferedWriter writer = Files.newBufferedWriter(reportPath_table, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                writer.write('\uFEFF'); // BOM for UTF-8
                writer.write(report_table.toString());
            }

        } catch (Exception e) {
            GTOCore.LOGGER.error("写入报告文件时发生错误", e);
        }
    }

    // Gson Serializable Entry class
    public static class Entry {

        public String cnName;
        public String enName;
        public String symbol;
        public String hexColor1;
        public String hexColor2;
        public String iconSetName;
        public BaseType baseType;
        public long mass;

        // fluid pipe properties
        public boolean hasFluidPipe;
        public int fluidPipeThroughput;

        // item pipe properties
        public boolean hasItemPipe;
        public int itemPipePriority;
        public float itemPipeTransferRate;

        // wire properties
        public boolean isWireMaterial;
        public int wireMaxAmperes;
        public long wireVoltage;
        public int wireLossPerMeter;

        // tool properties
        public boolean isToolMaterial;
        public float harvestSpeedTool;
        public int durabilityTool;
        public int harvestLevelTool;
        public int enchantabilityTool;
        public boolean isUnbreakableTool;
        public int durabilityMultiplierTool;

        // rotor properties
        public boolean isRotorMaterial;
        public int durabilityRotor;
        public int powerRotor;
        public int efficiencyRotor;

        // blast properties
        public int blastTemperature;

        // hazard properties
        public boolean isHazard;

        public static Entry fromMaterial(Material material) {
            Entry entry = new Entry();
            entry.cnName = material.getLocalizedName().getString();
            entry.enName = GTOComponentHandler.INSTANCE.getEnglishLanguage().getOrDefault(material.getUnlocalizedName(), material.getUnlocalizedName());
            entry.symbol = material.getChemicalFormula();
            entry.hexColor1 = getHexColorString(material.getMaterialRGB());
            entry.hexColor2 = getHexColorString(material.getMaterialSecondaryRGB());
            entry.iconSetName = material.getMaterialIconSet().name;
            entry.baseType = BaseType.inferFromMaterial(material);
            entry.mass = material.getMass();

            entry.hasFluidPipe = material.hasProperty(PropertyKey.FLUID_PIPE);
            if (entry.hasFluidPipe) {
                FluidPipeProperties materialFluidPipeProperties = material.getProperty(PropertyKey.FLUID_PIPE);
                entry.fluidPipeThroughput = materialFluidPipeProperties.getThroughput();
            }
            entry.hasItemPipe = material.hasProperty(PropertyKey.ITEM_PIPE);
            if (entry.hasItemPipe) {
                entry.itemPipePriority = material.getProperty(PropertyKey.ITEM_PIPE).getPriority();
                entry.itemPipeTransferRate = material.getProperty(PropertyKey.ITEM_PIPE).getTransferRate();
            }
            entry.isWireMaterial = material.hasProperty(PropertyKey.WIRE);
            if (entry.isWireMaterial) {
                entry.wireMaxAmperes = material.getProperty(PropertyKey.WIRE).getAmperage();
                entry.wireVoltage = material.getProperty(PropertyKey.WIRE).getVoltage();
                entry.wireLossPerMeter = material.getProperty(PropertyKey.WIRE).getLossPerBlock();
            }
            entry.isToolMaterial = material.hasProperty(PropertyKey.TOOL);
            if (entry.isToolMaterial) {
                ToolProperty toolProperty = material.getProperty(PropertyKey.TOOL);
                entry.harvestSpeedTool = toolProperty.getHarvestSpeed();
                entry.durabilityTool = toolProperty.getDurability();
                entry.harvestLevelTool = toolProperty.getHarvestLevel();
                entry.enchantabilityTool = toolProperty.getEnchantability();
                entry.isUnbreakableTool = toolProperty.isUnbreakable();
                entry.durabilityMultiplierTool = toolProperty.getDurabilityMultiplier();
            }
            entry.isRotorMaterial = material.hasProperty(PropertyKey.ROTOR);
            if (entry.isRotorMaterial) {
                entry.durabilityRotor = material.getProperty(PropertyKey.ROTOR).getDurability();
                entry.powerRotor = material.getProperty(PropertyKey.ROTOR).getPower();
                entry.efficiencyRotor = material.getProperty(PropertyKey.ROTOR).getEfficiency();
            }
            entry.blastTemperature = material.hasProperty(PropertyKey.BLAST) ? material.getProperty(PropertyKey.BLAST).getBlastTemperature() :
                    material.hasProperty(PropertyKey.ALLOY_BLAST) ? material.getProperty(PropertyKey.ALLOY_BLAST).getTemperature() : 0;
            entry.isHazard = material.hasProperty(PropertyKey.HAZARD);
            return entry;
        }
    }

    protected static String getHexColorString(int color) {
        String var10000 = Integer.toHexString(color);
        boolean alpha = (color >> 24 & 255) != 0;
        int var10001 = alpha ? 8 : 6;
        return "#" + StringUtils.leftPad(var10000, var10001, '0');
    }

    @RegisterEnumLang(keyPrefix = "gtocore.material.base_type")
    public enum BaseType {

        METAL("金属", "Metal"),
        GEM("宝石", "Gem"),
        CHEMICAL("化学品", "Chemical"),
        POLYMER("高分子材料", "Polymer"),
        COMPOSITE("复合材料", "Composite"),
        CERAMIC("陶瓷", "Ceramic"),
        WOOD("木材", "Wood"),
        MAGIC("魔法材料", "Magic"),
        OTHER("其他", "Other");

        @RegisterEnumLang.CnValue("name")
        final String cn;
        @RegisterEnumLang.EnValue("name")
        final String en;

        BaseType(String cn, String en) {
            this.cn = cn;
            this.en = en;
        }

        static BaseType inferFromMaterial(Material material) {
            if (material.hasFlag(GTOMaterialFlags.COMPOSITE_MATERIAL))
                return COMPOSITE;
            if (material.hasFlag(GTOMaterialFlags.GENERATE_CERAMIC))
                return CERAMIC;
            if (material.hasFlag(MaterialFlags.MAGICAL))
                return MAGIC;
            if (material.hasProperty(PropertyKey.GEM))
                return GEM;
            if (material.hasProperty(PropertyKey.WOOD))
                return WOOD;
            if (material.hasProperty(PropertyKey.POLYMER))
                return POLYMER;
            if (material.hasProperty(PropertyKey.BLAST) ||
                    material.hasProperty(PropertyKey.ALLOY_BLAST) ||
                    material.getMaterialIconSet() == MaterialIconSet.METALLIC)
                return METAL;
            return CHEMICAL;
        }
    }
}
