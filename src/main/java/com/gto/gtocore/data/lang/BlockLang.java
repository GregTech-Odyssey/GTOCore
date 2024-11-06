package com.gto.gtocore.data.lang;

import com.gregtechceu.gtceu.api.GTValues;

import net.minecraftforge.common.data.LanguageProvider;

public class BlockLang {

    public static void init(LanguageProvider provider) {
        for (int tier = GTValues.EV; tier <= GTValues.MAX; tier++) {
            provider.add("block.gtceu." + GTValues.VN[tier].toLowerCase() + "_buffer", "%s Buffer %s".formatted(GTValues.VLVH[tier], GTValues.VLVT[tier]));
        }

        for (int tier = GTValues.LV; tier <= GTValues.IV; tier++) {
            provider.add("block.gtceu." + GTValues.VN[tier].toLowerCase() + "_dual_input_hatch", "%s Dual Input Hatch".formatted(GTValues.VNF[tier]));
        }

        for (int tier = GTValues.LV; tier <= GTValues.IV; tier++) {
            provider.add("block.gtceu." + GTValues.VN[tier].toLowerCase() + "_dual_output_hatch", "%s Dual Output Hatch".formatted(GTValues.VNF[tier]));
        }

        for (int tier = GTValues.UHV; tier <= GTValues.MAX; tier++) {
            provider.add("block.gtceu." + GTValues.VN[tier].toLowerCase() + "_parallel_hatch", GTValues.VNF[tier] + " Parallel Control Hatch");
        }

        provider.add("block.gtceu.max_256a_laser_source_hatch", "256A §c§lMAX§r Laser Source Hatch");
        provider.add("block.gtceu.max_256a_laser_target_hatch", "256A §c§lMAX§r Laser Target Hatch");
        provider.add("block.gtceu.max_1024a_laser_source_hatch", "1024A §c§lMAX§r Laser Source Hatch");
        provider.add("block.gtceu.max_1024a_laser_target_hatch", "1024A §c§lMAX§r Laser Target Hatch");
        provider.add("block.gtceu.max_4096a_laser_source_hatch", "4096A §c§lMAX§r Laser Source Hatch");
        provider.add("block.gtceu.max_4096a_laser_target_hatch", "4096A §c§lMAX§r Laser Target Hatch");

        provider.add("block.gtocore.primitive_void_ore", "Primitive Void Ore");
    }
}
