package com.gto.gtocore.client;

import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.data.lang.LangHandler;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public final class Tooltips {

    public static final Map<String, LangHandler.ENCN> LANG = GTCEu.isDataGen() ? new HashMap<>() : null;

    public static final ImmutableMap<Item, String[]> TOOL_TIPS_KEY_MAP;
    public static final ImmutableMap<Item, LangHandler.ENCNS> TOOL_TIPS_MAP;

    static {
        ImmutableMap.Builder<Item, String[]> toolTipsKey = ImmutableMap.builder();
        toolTipsKey.put(GTBlocks.CASING_TEMPERED_GLASS.asItem(), new String[] { "tooltip.avaritia.tier", String.valueOf(2) });
        TOOL_TIPS_KEY_MAP = toolTipsKey.build();

        ImmutableMap.Builder<Item, LangHandler.ENCNS> toolTipsBuilder = ImmutableMap.builder();
        toolTipsBuilder.put(GTItems.VACUUM_TUBE.get(), new LangHandler.ENCNS(new String[] { "Right-click the handheld rough vacuum tube to obtain vacuum supply from a machine with vacuum level greater than 0" }, new String[] { "手持粗真空管潜行右击真空等级大于0的真空提供机器获取" }));
        toolTipsBuilder.put(GTOBlocks.URUIUM_COIL_BLOCK.asItem(), new LangHandler.ENCNS(new String[] { "Can provide 32000K furnace temperature for the hyper-dimensional plasma furnace", "Only this coil can be used in stellar furnace mode" }, new String[] { "可为超维度等离子锻炉提供32000K炉温", "恒星锻炉模式仅可使用该线圈" }));
        toolTipsBuilder.put(GTOBlocks.QUANTUM_GLASS.asItem(), new LangHandler.ENCNS(new String[] { "Dense but Transparent", "§bGlass & Elegance" }, new String[] { "致密但透明", "§b玻璃&优雅" }));

        TOOL_TIPS_MAP = toolTipsBuilder.build();

        if (LANG != null) {
            add(GTOItems.CREATE_ULTIMATE_BATTERY, "§7Can generate energy out of thin air", "§7能凭空产生能量");
            add(GTOItems.SUPRACHRONAL_MAINFRAME_COMPLEX, "§7Can generate computing power out of thin air", "§7能凭空产生算力");
            add(GTOItems.HYPER_STABLE_SELF_HEALING_ADHESIVE, "§7Selective complete adhesion, effective even when torn or damaged", "§7选择性完全粘合，即使在撕裂或损坏时也有效");
            add(GTOItems.BLACK_BODY_NAQUADRIA_SUPERSOLID, "§7Flows like a liquid, does not reflect any electromagnetic waves, perfectly absorbs and transmits", "§7如液体般流动，不反射任何电磁波，完美地将其吸收与传递");
            add(GTOItems.HUI_CIRCUIT_1, "§793015-Floating Point Operations/Second", "§793015-T浮点运算/秒");
            add(GTOItems.HUI_CIRCUIT_2, "§776M Processing Unit", "§776M处理单元");
            add(GTOItems.HUI_CIRCUIT_3, "§7Invalid RSA Algorithm", "§7无效RSA算法");
            add(GTOItems.HUI_CIRCUIT_4, "§7The 56th Mersenne Prime", "§7第56梅森素数");
            add(GTOItems.HUI_CIRCUIT_5, "§7Paradox", "§7佯谬");
            add(GTOItems.BIOWARE_PRINTED_CIRCUIT_BOARD, "§7Biologically mutated circuit board", "§7生物基因突变的电路基板");
            add(GTOItems.OPTICAL_PRINTED_CIRCUIT_BOARD, "§7Optically injected circuit board", "§7光学注入的电路基板");
            add(GTOItems.EXOTIC_PRINTED_CIRCUIT_BOARD, "§7Quantum circuit board", "§7量子电路基板");
            add(GTOItems.COSMIC_PRINTED_CIRCUIT_BOARD, "§7Circuit board carrying the universe", "§7承载宇宙的电路基板");
            add(GTOItems.SUPRACAUSAL_PRINTED_CIRCUIT_BOARD, "§7Ultimate circuit board", "§7最终的电路基板");
            add(GTOItems.SUPRACAUSAL_MAINFRAME, "§7Precise Forecast", "§7未卜先知");
            add(GTOItems.SUPRACAUSAL_COMPUTER, "§7Utilizes the advantage of wormholes", "§7利用虫洞的优势");
            add(GTOItems.SUPRACAUSAL_ASSEMBLY, "§7A massive singularity", "§7巨量的奇点");
            add(GTOItems.SUPRACAUSAL_PROCESSOR, "§7The power of black holes", "§7黑洞之力");
            add(GTOItems.COSMIC_ASSEMBLY, "§7Gently rotating in a grasp", "§7于握揽微微转动");
            add(GTOItems.COSMIC_COMPUTER, "§7Density approaching singularity", "§7密度趋近于奇点的小东西");
            add(GTOItems.COSMIC_MAINFRAME, "§7The power of the universe, intimidating through the ages!", "§7寰宇之力，震慑古今！");
            add(GTOItems.COSMIC_PROCESSOR, "§7Holding the stars", "§7手握星辰");
            add(GTOItems.EXOTIC_ASSEMBLY, "§7Quantum random walk", "§7量子随机游走");
            add(GTOItems.EXOTIC_COMPUTER, "§7Controlling everything with spin", "§7以自旋控制一切");
            add(GTOItems.EXOTIC_MAINFRAME, "§7Circuits from the future", "§7来自未来的电路");
            add(GTOItems.EXOTIC_PROCESSOR, "§7Super magnetic semiconductor circuit", "§7超级磁性半导体电路");
            add(GTOItems.OPTICAL_ASSEMBLY, "§7The power of lasers!", "§7激光之力！");
            add(GTOItems.OPTICAL_COMPUTER, "§7In the blink of an eye", "§7就在眨眼之间");
            add(GTOItems.OPTICAL_MAINFRAME, "§7Can it be even faster?", "§7还能更快吗？");
            add(GTOItems.OPTICAL_PROCESSOR, "§7Light-speed computation", "§7光速计算");
            add(GTOItems.BIOWARE_ASSEMBLY, "§7Seems to hear whispers", "§7似乎能听到窃窃私语");
            add(GTOItems.BIOWARE_COMPUTER, "§7Covered in slime between metals", "§7金属之间布满了黏菌");
            add(GTOItems.BIOWARE_MAINFRAME, "§7Network of microbial consciousness", "§7菌群意识网络");
            add(GTOItems.BIOWARE_PROCESSOR, "§7Viscous organic slurry adheres to the surface", "§7粘稠的有机浆液附着于表面");

            add(GTOItems.DIAMOND_CRYSTAL_CIRCUIT, "§7Crystal Circuit - Logic", "§7晶体电路-逻辑");
            add(GTOItems.RUBY_CRYSTAL_CIRCUIT, "§7Crystal Circuit - Control", "§7晶体电路-控制");
            add(GTOItems.EMERALD_CRYSTAL_CIRCUIT, "§7Crystal Circuit - Storage", "§7晶体电路-存储");
            add(GTOItems.SAPPHIRE_CRYSTAL_CIRCUIT, "§7Crystal Circuit - Conversion", "§7晶体电路-转换");

            add(GTOBlocks.NAQUADRIA_CHARGE, "Can be activated by Quantum Star", "可由量子之星激活");
            add(GTOBlocks.LEPTONIC_CHARGE, "Can be activated by Gravity Star", "可由重力之星激活");
            add(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE, "Can be activated by Unstable Star", "可由易变之星激活");
        }
    }

    private static void add(ItemLike itemLike, String cn, String en) {
        LANG.put(itemLike.asItem().getDefaultInstance().getDescriptionId() + ".tooltip", new LangHandler.ENCN(cn, en));
    }
}
