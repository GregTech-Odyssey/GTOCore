package com.gtocore.client;

import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOItems;

import com.gtolib.api.lang.CNEN;
import com.gtolib.api.lang.CNENS;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.utils.collection.O2OOpenCacheHashMap;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import com.google.common.collect.ImmutableMap;
import committee.nova.mods.avaritia.init.registry.ModItems;

import java.util.Map;

public final class Tooltips {

    public static final Map<String, CNEN> LANG = GTCEu.isDataGen() ? new O2OOpenCacheHashMap<>() : null;

    public static final ImmutableMap<Item, String[]> TOOL_TIPS_KEY_MAP;
    public static final ImmutableMap<Item, CNENS> TOOL_TIPS_MAP;

    static {
        ImmutableMap.Builder<Item, String[]> toolTipsKey = ImmutableMap.builder();
        toolTipsKey.put(GTBlocks.CASING_TEMPERED_GLASS.asItem(), new String[] { "tooltip.avaritia.tier", String.valueOf(2) });
        TOOL_TIPS_KEY_MAP = toolTipsKey.build();

        ImmutableMap.Builder<Item, CNENS> toolTipsBuilder = ImmutableMap.builder();
        toolTipsBuilder.put(GTItems.VACUUM_TUBE.get(), new CNENS(new String[] { "手持粗真空管潜行右击真空等级大于0的真空提供机器获取" }, new String[] { "Right-click the handheld rough vacuum tube to obtain vacuum supply from a machine with vacuum level greater than 0" }));
        toolTipsBuilder.put(GTOBlocks.URUIUM_COIL_BLOCK.asItem(), new CNENS(new String[] { "可为超维度等离子锻炉提供32000K炉温", "恒星锻炉模式仅可使用该线圈" }, new String[] { "Can provide 32000K furnace temperature for the hyper-dimensional plasma furnace", "Only this coil can be used in stellar furnace mode" }));
        toolTipsBuilder.put(GTOBlocks.QUANTUM_GLASS.asItem(), new CNENS(new String[] { "致密但透明", "§b玻璃&优雅" }, new String[] { "Dense but Transparent", "§bGlass & Elegance" }));
        toolTipsBuilder.put(ModItems.neutron_pile.get(), new CNENS(new String[] { "§8§o千万纳米蜂群的损耗换来的却只有一点点..." }, new String[] { "§8§oThe loss of millions of nanites only gets a little..." }));
        toolTipsBuilder.put(GTItems.TURBINE_ROTOR.asItem(), new CNENS(new String[] { "§c已弃用", "§7请清理更换相关涡轮转子生产设备" }, new String[] { "§cDeprecated", "§7Please clean up and replace the relevant turbine rotor production equipment" }));

        TOOL_TIPS_MAP = toolTipsBuilder.build();

        if (LANG != null) {
            add(GTOItems.CREATE_ULTIMATE_BATTERY, "§7能凭空产生能量", "§7Can generate energy out of thin air");
            add(GTOItems.SUPRACHRONAL_MAINFRAME_COMPLEX, "§7能凭空产生算力", "§7Can generate computing power out of thin air");
            add(GTOItems.HYPER_STABLE_SELF_HEALING_ADHESIVE, "§7选择性完全粘合，即使在撕裂或损坏时也有效", "§7Selective complete adhesion, effective even when torn or damaged");
            add(GTOItems.BLACK_BODY_NAQUADRIA_SUPERSOLID, "§7如液体般流动，不反射任何电磁波，完美地将其吸收与传递", "§7Flows like a liquid, does not reflect any electromagnetic waves, perfectly absorbs and transmits");
            add(GTOItems.HUI_CIRCUIT_1, "§793015-T浮点运算/秒", "§793015-Floating Point Operations/Second");
            add(GTOItems.HUI_CIRCUIT_2, "§776M处理单元", "§776M Processing Unit");
            add(GTOItems.HUI_CIRCUIT_3, "§7无效RSA算法", "§7Invalid RSA Algorithm");
            add(GTOItems.HUI_CIRCUIT_4, "§7第56梅森素数", "§7The 56th Mersenne Prime");
            add(GTOItems.HUI_CIRCUIT_5, "§7佯谬", "§7Paradox");
            add(GTOItems.BIOWARE_PRINTED_CIRCUIT_BOARD, "§7生物基因突变的电路基板", "§7Biologically mutated circuit board");
            add(GTOItems.OPTICAL_PRINTED_CIRCUIT_BOARD, "§7光学注入的电路基板", "§7Optically injected circuit board");
            add(GTOItems.EXOTIC_PRINTED_CIRCUIT_BOARD, "§7量子电路基板", "§7Quantum circuit board");
            add(GTOItems.COSMIC_PRINTED_CIRCUIT_BOARD, "§7承载宇宙的电路基板", "§7Circuit board carrying the universe");
            add(GTOItems.SUPRACAUSAL_PRINTED_CIRCUIT_BOARD, "§7最终的电路基板", "§7Ultimate circuit board");
            add(GTOItems.SUPRACAUSAL_MAINFRAME, "§7未卜先知", "§7Precise Forecast");
            add(GTOItems.SUPRACAUSAL_COMPUTER, "§7利用虫洞的优势", "§7Utilizes the advantage of wormholes");
            add(GTOItems.SUPRACAUSAL_ASSEMBLY, "§7巨量的奇点", "§7A massive singularity");
            add(GTOItems.SUPRACAUSAL_PROCESSOR, "§7黑洞之力", "§7The power of black holes");
            add(GTOItems.COSMIC_ASSEMBLY, "§7于握揽微微转动", "§7Gently rotating in a grasp");
            add(GTOItems.COSMIC_COMPUTER, "§7密度趋近于奇点的小东西", "§7Density approaching singularity");
            add(GTOItems.COSMIC_MAINFRAME, "§7寰宇之力，震慑古今！", "§7The power of the universe, intimidating through the ages!");
            add(GTOItems.COSMIC_PROCESSOR, "§7手握星辰", "§7Holding the stars");
            add(GTOItems.EXOTIC_ASSEMBLY, "§7量子随机游走", "§7Quantum random walk");
            add(GTOItems.EXOTIC_COMPUTER, "§7以自旋控制一切", "§7Controlling everything with spin");
            add(GTOItems.EXOTIC_MAINFRAME, "§7来自未来的电路", "§7Circuits from the future");
            add(GTOItems.EXOTIC_PROCESSOR, "§7超级磁性半导体电路", "§7Super magnetic semiconductor circuit");
            add(GTOItems.OPTICAL_ASSEMBLY, "§7激光之力！", "§7The power of lasers!");
            add(GTOItems.OPTICAL_COMPUTER, "§7就在眨眼之间", "§7In the blink of an eye");
            add(GTOItems.OPTICAL_MAINFRAME, "§7还能更快吗？", "§7Can it be even faster?");
            add(GTOItems.OPTICAL_PROCESSOR, "§7光速计算", "§7Light-speed computation");
            add(GTOItems.BIOWARE_ASSEMBLY, "§7似乎能听到窃窃私语", "§7Seems to hear whispers");
            add(GTOItems.BIOWARE_COMPUTER, "§7金属之间布满了黏菌", "§7Covered in slime between metals");
            add(GTOItems.BIOWARE_MAINFRAME, "§7菌群意识网络", "§7Network of microbial consciousness");
            add(GTOItems.BIOWARE_PROCESSOR, "§7粘稠的有机浆液附着于表面", "§7Viscous organic slurry adheres to the surface");

            add(GTOItems.DIAMOND_CRYSTAL_CIRCUIT, "§7晶体电路-逻辑", "§7Crystal Circuit - Logic");
            add(GTOItems.RUBY_CRYSTAL_CIRCUIT, "§7晶体电路-控制", "§7Crystal Circuit - Control");
            add(GTOItems.EMERALD_CRYSTAL_CIRCUIT, "§7晶体电路-存储", "§7Crystal Circuit - Storage");
            add(GTOItems.SAPPHIRE_CRYSTAL_CIRCUIT, "§7晶体电路-转换", "§7Crystal Circuit - Conversion");

            add(GTOBlocks.NAQUADRIA_CHARGE, "可由量子之星激活", "Can be activated by Quantum Star");
            add(GTOBlocks.LEPTONIC_CHARGE, "可由重力之星激活", "Can be activated by Gravity Star");
            add(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE, "可由易变之星激活", "Can be activated by Unstable Star");
        }
    }

    private static void add(ItemLike itemLike, String cn, String en) {
        LANG.put(itemLike.asItem().getDefaultInstance().getDescriptionId() + ".tooltip", new CNEN(cn, en));
    }
}
