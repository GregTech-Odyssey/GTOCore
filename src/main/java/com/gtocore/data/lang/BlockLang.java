package com.gtocore.data.lang;

import com.gtolib.api.GTOValues;
import com.gtolib.api.data.Dimension;

import com.gregtechceu.gtceu.api.GTValues;

import static com.gtocore.data.lang.LangHandler.addCNEN;

final class BlockLang {

    public static void init() {
        for (int tier = GTValues.EV; tier <= GTValues.MAX; tier++) {
            addCNEN("block.gtceu." + GTValues.VN[tier].toLowerCase() + "_buffer", "%s缓存器 %s".formatted(GTOValues.VLVHCN[tier], GTValues.VLVT[tier]), "%s Buffer %s".formatted(GTValues.VLVH[tier], GTValues.VLVT[tier]));
        }

        for (int tier = GTValues.LV; tier <= GTValues.IV; tier++) {
            addCNEN("block.gtceu." + GTValues.VN[tier].toLowerCase() + "_dual_input_hatch", "%s输入总成".formatted(GTOValues.VNFR[tier]), "%s Dual Input Hatch".formatted(GTValues.VNF[tier]));
            addCNEN("block.gtceu." + GTValues.VN[tier].toLowerCase() + "_dual_output_hatch", "%s输出总成".formatted(GTOValues.VNFR[tier]), "%s Dual Output Hatch".formatted(GTValues.VNF[tier]));
        }

        for (int tier = GTValues.UHV; tier <= GTValues.MAX; tier++) {
            addCNEN("block.gtceu." + GTValues.VN[tier].toLowerCase() + "_parallel_hatch", GTOValues.VNFR[tier] + "并行控制仓", GTValues.VNF[tier] + " Parallel Control Hatch");
        }

        addCNEN("block.gtceu." + GTValues.VN[GTValues.EV].toLowerCase() + "_parallel_hatch", GTOValues.VNFR[GTValues.EV] + "并行控制仓", GTValues.VNF[GTValues.EV] + " Parallel Control Hatch");

        addCNEN("block.gtceu.max_256a_laser_source_hatch", "256§e安§r§c§lMAX§r激光源仓", "256A §c§lMAX§r Laser Source Hatch");
        addCNEN("block.gtceu.max_256a_laser_target_hatch", "256§e安§r§c§lMAX§r激光靶仓", "256A §c§lMAX§r Laser Target Hatch");
        addCNEN("block.gtceu.max_1024a_laser_source_hatch", "1024§e安§r§c§lMAX§r激光源仓", "1024A §c§lMAX§r Laser Source Hatch");
        addCNEN("block.gtceu.max_1024a_laser_target_hatch", "1024§e安§r§c§lMAX§r激光靶仓", "1024A §c§lMAX§r Laser Target Hatch");
        addCNEN("block.gtceu.max_4096a_laser_source_hatch", "4096§e安§r§c§lMAX§r激光源仓", "4096A §c§lMAX§r Laser Source Hatch");
        addCNEN("block.gtceu.max_4096a_laser_target_hatch", "4096§e安§r§c§lMAX§r激光靶仓", "4096A §c§lMAX§r Laser Target Hatch");

        addCNEN("block.gtceu.control_hatch", "机器控制仓", "Machine Control Hatch");

        for (Dimension dim : new Dimension[] {
                Dimension.CERES, Dimension.IO, Dimension.GANYMEDE, Dimension.BARNARDA_C, Dimension.ENCELADUS, Dimension.TITAN, Dimension.PLUTO
        }) {
            var cnName = dim.getCn();
            var cnSuffix = cnName.endsWith("星") ? "仪" : "星球仪";
            addCNEN("block.ad_astra." + dim.name().toLowerCase() + "_globe", dim.getCn() + cnSuffix, dim.getEn() + " Globe");
        }
    }
}
