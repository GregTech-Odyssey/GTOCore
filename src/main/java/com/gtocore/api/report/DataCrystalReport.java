package com.gtocore.api.report;

import com.gtolib.GTOCore;
import com.gtolib.utils.RLUtils;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.gtocore.data.recipe.builder.research.ExResearchManager.*;
import static com.gtolib.utils.RegistriesUtils.getItem;

public class DataCrystalReport {

    public static void grtDataCrystalReport() {
        StringBuilder report = new StringBuilder("# ***数据晶片(扫描)-统计***\n  ");
        report.append("|   扫描 ID  | 数据等级 | 介质等级 |    扫描物    |   数量   |\n  ");
        report.append("|-----------|--------|---------|-------------|---------|\n  ");

        for (var entry : scanningMap.int2ObjectEntrySet()) {
            int serial = entry.getIntKey();
            DataCrystal dataCrystal = entry.getValue();
            String scanningId = dataCrystal.data();
            String[] parts = scanningId.split("-", 3);
            String countPart = parts[0];
            int count = Integer.parseInt(countPart.substring(0, countPart.length() - 1));
            String state = countPart.substring(countPart.length() - 1);

            if (state.equals("i")) {
                Item item = getItem(parts[1], parts[2]);
                ItemStack stack = new ItemStack(item, 1);
                String scanningThing = "item  - " + stack.getHoverName().getString();
                report.append(String.format("|0x%08X|%d|%d|%s|%d|\n  ", serial, dataCrystal.tier(), dataCrystal.crystal(), scanningThing, count));
                continue;
            }

            if (state.equals("f")) {
                Fluid fluid = ForgeRegistries.FLUIDS.getValue(RLUtils.fromNamespaceAndPath(parts[1], parts[2]));
                String fluidState;
                if (parts[2].contains("gas")) fluidState = "气态 ";
                else if (parts[2].contains("liquid")) fluidState = "液态 ";
                else if (parts[2].contains("molten")) fluidState = "熔融 ";
                else if (parts[2].contains("plasma")) fluidState = "等离子态 ";
                else fluidState = "";
                String scanningThing = "fluid - " + fluidState + I18n.get(fluid.getFluidType().getDescriptionId());
                report.append(String.format("|0x%08X|%d|%d|%s|%d|\n  ", serial, dataCrystal.tier(), dataCrystal.crystal(), scanningThing, count));
            }
        }

        report.append("\n\n\n\n  ");

        report.append("# ***数据晶片(研究)-统计***\n  ");
        report.append("|   研究 ID  | 数据等级 | 介质等级 |    研究名    |\n  ");
        report.append("|-----------|--------|---------|-------------|\n  ");

        for (var entry : analyzeMap.int2ObjectEntrySet()) {
            int serial = entry.getIntKey();
            DataCrystal dataCrystal = entry.getValue();
            String analyzeId = "gtocore.data." + dataCrystal.data();
            report.append(String.format("|0x%08X|%d|%d|%s|\n  ", serial, dataCrystal.tier(), dataCrystal.crystal(), I18n.get(analyzeId)));
        }

        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
            Path logDir = Paths.get("logs", "report");
            if (!Files.exists(logDir)) Files.createDirectories(logDir);
            Path reportPath1 = logDir.resolve("Data_Crystal_report_" + timestamp + ".md");
            try (BufferedWriter writer = Files.newBufferedWriter(reportPath1)) {
                writer.write(report.toString());
            }
            GTOCore.LOGGER.info("数据晶片(扫描/研究)统计报告已生成:run\\logs\\report\\{}", reportPath1);
        } catch (Exception e) {
            GTOCore.LOGGER.error("生成数据晶片(扫描/研究)统计报告失败", e);
        }
    }
}
