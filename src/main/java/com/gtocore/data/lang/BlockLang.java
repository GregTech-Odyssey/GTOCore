package com.gtocore.data.lang;

import com.gtolib.api.GTOValues;
import com.gtolib.api.data.Dimension;

import com.gregtechceu.gtceu.api.GTValues;

import static com.gtocore.data.lang.LangHandler.addCNEN;

final class BlockLang {

    static void init() {
        for (int tier = GTValues.LV; tier <= GTValues.IV; tier++) {
            addCNEN("block.gtceu." + GTValues.VN[tier].toLowerCase() + "_dual_input_hatch", "%s输入总成".formatted(GTOValues.VNFR[tier]), "%s Dual Input Hatch".formatted(GTValues.VNF[tier]));
            addCNEN("block.gtceu." + GTValues.VN[tier].toLowerCase() + "_dual_output_hatch", "%s输出总成".formatted(GTOValues.VNFR[tier]), "%s Dual Output Hatch".formatted(GTValues.VNF[tier]));
        }

        for (int tier = GTValues.IV; tier <= GTValues.MAX; tier++) {
            addCNEN("block.gtceu." + GTValues.VN[tier].toLowerCase() + "_parallel_hatch", GTOValues.VNFR[tier] + "并行控制仓", GTValues.VNF[tier] + " Parallel Control Hatch");
        }

        addCNEN("block.gtceu." + GTValues.VN[GTValues.EV].toLowerCase() + "_parallel_hatch", GTOValues.VNFR[GTValues.EV] + "并行控制仓", GTValues.VNF[GTValues.EV] + " Parallel Control Hatch");

        for (Dimension dim : new Dimension[] {
                Dimension.CERES, Dimension.IO, Dimension.GANYMEDE, Dimension.BARNARDA_C, Dimension.ENCELADUS, Dimension.TITAN, Dimension.PLUTO
        }) {
            var cnName = dim.getCn();
            var cnSuffix = cnName.endsWith("星") ? "仪" : "星球仪";
            addCNEN("block.ad_astra." + dim.name().toLowerCase() + "_globe", dim.getCn() + cnSuffix, dim.getEn() + " Globe");
        }
    }
}
