package com.gtocore.data.recipe.processing;

import com.gtolib.GTOCore;
import com.gtolib.api.data.chemical.GTOChemicalHelper;
import com.gtolib.api.recipe.builder.DataCrystalConstruction;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gtolib.api.recipe.research.ExResearchManager.ScanningMap;
import static com.gtolib.utils.RegistriesUtils.getItem;

public final class ScanningRecipes {

    public static void init() {
        DataCrystalConstruction.buildDataCrystal(false)
                .inputScanning(new ItemStack(GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Naquadah)), 1, 1)
                .EUt(VA[IV])
                .duration(200)
                .save();

        DataCrystalConstruction.buildDataCrystal(true)
                .inputScanning(ChemicalHelper.get(TagPrefix.dust, GTMaterials.ActivatedCarbon), 3, 2)
                .catalyst(ChemicalHelper.get(TagPrefix.lens, GTMaterials.Amethyst))
                .EUt(VA[ZPM])
                .CWUt(16)
                .save();

        DataCrystalConstruction.buildDataCrystal(true)
                .inputScanning(GTMaterials.Water.getFluid(50050), 3, 2)
                .catalyst(ChemicalHelper.get(TagPrefix.lens, GTMaterials.Amethyst))
                .EUt(VA[ZPM])
                .CWUt(16)
                .save();

        if (false) {
            StringBuilder report = new StringBuilder("# ***数据晶片(扫描)-统计***\n  ");

            report.append("|   扫描 ID  | 数据等级 | 介质等级 |    扫描物    |   数量   |\n  ");
            report.append("|-----------|--------|---------|-------------|---------|\n  ");

            for (var entry : ScanningMap.int2ObjectEntrySet()) {
                int serial = entry.getIntKey();
                String scanningId = entry.getValue();

                String[] parts = scanningId.split("-", 3);
                if (parts.length != 3) {
                    report.append(String.format("|%08X|%d|%d|%s|无法识别|\n  ",
                            serial, (serial >>> 28) & 0x0F, (serial >>> 24) & 0x0F, scanningId));
                    continue;
                }

                String countPart = parts[0];
                int count = Integer.parseInt(countPart.substring(0, countPart.length() - 1));

                // 尝试作为物品解析
                Item item = getItem(parts[1], parts[2]);
                if (item != Items.BARRIER) {
                    ItemStack stack = new ItemStack(item, 1);
                    report.append(String.format("|%08X|%d|%d|item  - %s:%s|%d|\n  ",
                            serial, (serial >>> 28) & 0x0F, (serial >>> 24) & 0x0F, parts[1], parts[2], count));
                    continue;
                }
                // 尝试作为流体解析
                Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(parts[1], parts[2]));
                if (fluid != null) {
                    FluidStack stack = new FluidStack(fluid, 1);
                    report.append(String.format("|%08X|%d|%d|fluid - %s:%s|%d|\n  ",
                            serial, (serial >>> 28) & 0x0F, (serial >>> 24) & 0x0F, parts[1], parts[2], count));
                    continue;
                }
                report.append(String.format("|%08X|%d|%d|%s|%d|\n  ",
                        serial, (serial >>> 28) & 0x0F, (serial >>> 24) & 0x0F, scanningId, count));

            }

            try {
                Path logDir = Paths.get("logs", "report");
                if (!Files.exists(logDir)) Files.createDirectories(logDir);
                Path reportPath = logDir.resolve("scanning_report.md");
                try (BufferedWriter writer = Files.newBufferedWriter(reportPath)) {
                    writer.write(report.toString());
                }
                GTOCore.LOGGER.info("数据晶片(扫描)统计报告已生成: {}", reportPath.toAbsolutePath());
            } catch (Exception e) {
                GTOCore.LOGGER.error("生成数据晶片(扫描)统计报告失败", e);
            }
        }
    }
}
