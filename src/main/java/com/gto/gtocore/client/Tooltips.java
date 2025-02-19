package com.gto.gtocore.client;

import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.data.lang.LangHandler;
import com.gto.gtocore.utils.StringUtils;

import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.Item;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.tterrag.registrate.util.entry.RegistryEntry;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;

public final class Tooltips {

    public static final ImmutableMap<Item, String[]> TOOL_TIPS_KEY_MAP;
    public static final ImmutableMap<Item, LangHandler.ENCNS> TOOL_TIPS_MAP;
    public static final ImmutableMap<Item, Supplier<String>> FLICKER_TOOL_TIPS_MAP;
    public static final ImmutableSet<Item> suprachronalCircuitSet;
    public static final ImmutableSet<Item> magnetoresonaticcircuitSet;
    public static final ImmutableSet<Item> universalCircuitSet;

    static {
        ImmutableMap.Builder<Item, String[]> toolTipsKey = ImmutableMap.builder();
        toolTipsKey.put(GTBlocks.CASING_TEMPERED_GLASS.asItem(), new String[] { "tooltip.avaritia.tier", String.valueOf(2) });
        TOOL_TIPS_KEY_MAP = toolTipsKey.build();

        ImmutableSet.Builder<Item> suprachronalCircuitBuilder = ImmutableSet.builder();
        suprachronalCircuitBuilder.addAll(Arrays.stream(GTOItems.SUPRACHRONAL_CIRCUIT).map(RegistryEntry::get).toList());
        suprachronalCircuitSet = suprachronalCircuitBuilder.build();

        ImmutableSet.Builder<Item> magnetoresonaticcircuitBuilder = ImmutableSet.builder();
        magnetoresonaticcircuitBuilder.addAll(Arrays.stream(GTOItems.MAGNETO_RESONATIC_CIRCUIT).filter(Objects::nonNull).map(RegistryEntry::get).toList());
        magnetoresonaticcircuitSet = magnetoresonaticcircuitBuilder.build();

        ImmutableSet.Builder<Item> universalCircuitBuilder = ImmutableSet.builder();
        universalCircuitBuilder.addAll(Arrays.stream(GTOItems.UNIVERSAL_CIRCUIT).filter(Objects::nonNull).map(RegistryEntry::get).toList());
        universalCircuitSet = universalCircuitBuilder.build();

        ImmutableMap.Builder<Item, LangHandler.ENCNS> toolTipsBuilder = ImmutableMap.builder();
        toolTipsBuilder.put(GTOItems.CREATE_ULTIMATE_BATTERY.get(), new LangHandler.ENCNS(new String[] { "§7Can generate energy out of thin air" }, new String[] { "§7能凭空产生能量" }));
        toolTipsBuilder.put(GTOItems.SUPRACHRONAL_MAINFRAME_COMPLEX.get(), new LangHandler.ENCNS(new String[] { "§7Can generate computing power out of thin air" }, new String[] { "§7能凭空产生算力" }));
        toolTipsBuilder.put(GTItems.VACUUM_TUBE.get(), new LangHandler.ENCNS(new String[] { "Right-click the handheld rough vacuum tube to obtain vacuum supply from a machine with vacuum level greater than 0" }, new String[] { "手持粗真空管右击真空等级大于0的真空提供机器获取" }));
        toolTipsBuilder.put(GTOItems.WARPED_ENDER_PEARL.get(), new LangHandler.ENCNS(new String[] { "Sneak and right-click to set a personal teleportation point, right-click to teleport to the point" }, new String[] { "潜行右键可设置个人传送点，右键传送到传送点" }));
        toolTipsBuilder.put(GTOBlocks.NAQUADRIA_CHARGE.asItem(), new LangHandler.ENCNS(new String[] { "Can be activated by Quantum Star" }, new String[] { "可由量子之星激活" }));
        toolTipsBuilder.put(GTOBlocks.LEPTONIC_CHARGE.asItem(), new LangHandler.ENCNS(new String[] { "Can be activated by Gravity Star" }, new String[] { "可由重力之星激活" }));
        toolTipsBuilder.put(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem(), new LangHandler.ENCNS(new String[] { "Can be activated by Unstable Star" }, new String[] { "可由易变之星激活" }));
        toolTipsBuilder.put(GTOBlocks.URUIUM_COIL_BLOCK.asItem(), new LangHandler.ENCNS(new String[] { "Can provide 32000K furnace temperature for the hyper-dimensional plasma furnace", "Only this coil can be used in stellar furnace mode" }, new String[] { "可为超维度等离子锻炉提供32000K炉温", "恒星锻炉模式仅可使用该线圈" }));
        toolTipsBuilder.put(GTOItems.HYPER_STABLE_SELF_HEALING_ADHESIVE.get(), new LangHandler.ENCNS(new String[] { "§7Selective complete adhesion, effective even when torn or damaged" }, new String[] { "§7选择性完全粘合，即使在撕裂或损坏时也有效" }));
        toolTipsBuilder.put(GTOItems.BLACK_BODY_NAQUADRIA_SUPERSOLID.get(), new LangHandler.ENCNS(new String[] { "§7Flows like a liquid, does not reflect any electromagnetic waves, perfectly absorbs and transmits" }, new String[] { "§7如液体般流动，不反射任何电磁波，完美地将其吸收与传递" }));
        toolTipsBuilder.put(GTOItems.HUI_CIRCUIT_1.get(), new LangHandler.ENCNS(new String[] { "§793015-Floating Point Operations/Second" }, new String[] { "§793015-T浮点运算/秒" }));
        toolTipsBuilder.put(GTOItems.HUI_CIRCUIT_2.get(), new LangHandler.ENCNS(new String[] { "§776M Processing Unit" }, new String[] { "§776M处理单元" }));
        toolTipsBuilder.put(GTOItems.HUI_CIRCUIT_3.get(), new LangHandler.ENCNS(new String[] { "§7Invalid RSA Algorithm" }, new String[] { "§7无效RSA算法" }));
        toolTipsBuilder.put(GTOItems.HUI_CIRCUIT_4.get(), new LangHandler.ENCNS(new String[] { "§7The 56th Mersenne Prime" }, new String[] { "§7第56梅森素数" }));
        toolTipsBuilder.put(GTOItems.HUI_CIRCUIT_5.get(), new LangHandler.ENCNS(new String[] { "§7Paradox" }, new String[] { "§7佯谬" }));
        toolTipsBuilder.put(GTOItems.BIOWARE_PRINTED_CIRCUIT_BOARD.get(), new LangHandler.ENCNS(new String[] { "§7Biologically mutated circuit board" }, new String[] { "§7生物基因突变的电路基板" }));
        toolTipsBuilder.put(GTOItems.OPTICAL_PRINTED_CIRCUIT_BOARD.get(), new LangHandler.ENCNS(new String[] { "§7Optically injected circuit board" }, new String[] { "§7光学注入的电路基板" }));
        toolTipsBuilder.put(GTOItems.EXOTIC_PRINTED_CIRCUIT_BOARD.get(), new LangHandler.ENCNS(new String[] { "§7Quantum circuit board" }, new String[] { "§7量子电路基板" }));
        toolTipsBuilder.put(GTOItems.COSMIC_PRINTED_CIRCUIT_BOARD.get(), new LangHandler.ENCNS(new String[] { "§7Circuit board carrying the universe" }, new String[] { "§7承载宇宙的电路基板" }));
        toolTipsBuilder.put(GTOItems.SUPRACAUSAL_PRINTED_CIRCUIT_BOARD.get(), new LangHandler.ENCNS(new String[] { "§7Ultimate circuit board" }, new String[] { "§7最终的电路基板" }));
        toolTipsBuilder.put(GTOItems.SUPRACAUSAL_MAINFRAME.get(), new LangHandler.ENCNS(new String[] { "§7Precise Forecast" }, new String[] { "§7未卜先知" }));
        toolTipsBuilder.put(GTOItems.SUPRACAUSAL_COMPUTER.get(), new LangHandler.ENCNS(new String[] { "§7Utilizes the advantage of wormholes" }, new String[] { "§7利用虫洞的优势" }));
        toolTipsBuilder.put(GTOItems.SUPRACAUSAL_ASSEMBLY.get(), new LangHandler.ENCNS(new String[] { "§7A massive singularity" }, new String[] { "§7巨量的奇点" }));
        toolTipsBuilder.put(GTOItems.SUPRACAUSAL_PROCESSOR.get(), new LangHandler.ENCNS(new String[] { "§7The power of black holes" }, new String[] { "§7黑洞之力" }));
        toolTipsBuilder.put(GTOItems.COSMIC_ASSEMBLY.get(), new LangHandler.ENCNS(new String[] { "§7Gently rotating in a grasp" }, new String[] { "§7于握揽微微转动" }));
        toolTipsBuilder.put(GTOItems.COSMIC_COMPUTER.get(), new LangHandler.ENCNS(new String[] { "§7Density approaching singularity" }, new String[] { "§7密度趋近于奇点的小东西" }));
        toolTipsBuilder.put(GTOItems.COSMIC_MAINFRAME.get(), new LangHandler.ENCNS(new String[] { "§7The power of the universe, intimidating through the ages!" }, new String[] { "§7寰宇之力，震慑古今！" }));
        toolTipsBuilder.put(GTOItems.COSMIC_PROCESSOR.get(), new LangHandler.ENCNS(new String[] { "§7Holding the stars" }, new String[] { "§7手握星辰" }));
        toolTipsBuilder.put(GTOItems.EXOTIC_ASSEMBLY.get(), new LangHandler.ENCNS(new String[] { "§7Quantum random walk" }, new String[] { "§7量子随机游走" }));
        toolTipsBuilder.put(GTOItems.EXOTIC_COMPUTER.get(), new LangHandler.ENCNS(new String[] { "§7Controlling everything with spin" }, new String[] { "§7以自旋控制一切" }));
        toolTipsBuilder.put(GTOItems.EXOTIC_MAINFRAME.get(), new LangHandler.ENCNS(new String[] { "§7Circuits from the future" }, new String[] { "§7来自未来的电路" }));
        toolTipsBuilder.put(GTOItems.EXOTIC_PROCESSOR.get(), new LangHandler.ENCNS(new String[] { "§7Super magnetic semiconductor circuit" }, new String[] { "§7超级磁性半导体电路" }));
        toolTipsBuilder.put(GTOItems.OPTICAL_ASSEMBLY.get(), new LangHandler.ENCNS(new String[] { "§7The power of lasers!" }, new String[] { "§7激光之力！" }));
        toolTipsBuilder.put(GTOItems.OPTICAL_COMPUTER.get(), new LangHandler.ENCNS(new String[] { "§7In the blink of an eye" }, new String[] { "§7就在眨眼之间" }));
        toolTipsBuilder.put(GTOItems.OPTICAL_MAINFRAME.get(), new LangHandler.ENCNS(new String[] { "§7Can it be even faster?" }, new String[] { "§7还能更快吗？" }));
        toolTipsBuilder.put(GTOItems.OPTICAL_PROCESSOR.get(), new LangHandler.ENCNS(new String[] { "§7Light-speed computation" }, new String[] { "§7光速计算" }));
        toolTipsBuilder.put(GTOItems.BIOWARE_ASSEMBLY.get(), new LangHandler.ENCNS(new String[] { "§7Seems to hear whispers" }, new String[] { "§7似乎能听到窃窃私语" }));
        toolTipsBuilder.put(GTOItems.BIOWARE_COMPUTER.get(), new LangHandler.ENCNS(new String[] { "§7Covered in slime between metals" }, new String[] { "§7金属之间布满了黏菌" }));
        toolTipsBuilder.put(GTOItems.BIOWARE_MAINFRAME.get(), new LangHandler.ENCNS(new String[] { "§7Network of microbial consciousness" }, new String[] { "§7菌群意识网络" }));
        toolTipsBuilder.put(GTOItems.BIOWARE_PROCESSOR.get(), new LangHandler.ENCNS(new String[] { "§7Viscous organic slurry adheres to the surface" }, new String[] { "§7粘稠的有机浆液附着于表面" }));
        toolTipsBuilder.put(GTOBlocks.QUANTUM_GLASS.asItem(), new LangHandler.ENCNS(new String[] { "Dense but Transparent", "§bGlass & Elegance" }, new String[] { "致密但透明", "§b玻璃&优雅" }));

        TOOL_TIPS_MAP = toolTipsBuilder.build();

        ImmutableMap.Builder<Item, Supplier<String>> flickerToolTipsBuilder = ImmutableMap.builder();
        flickerToolTipsBuilder.put(GTOItems.CREATE_ULTIMATE_BATTERY.get(), () -> "§2" + I18n.get("tooltip.avaritia.tier", "-" + StringUtils.white_blue(I18n.get("gtocore.tooltip.unknown"))));
        flickerToolTipsBuilder.put(GTOItems.SUPRACHRONAL_MAINFRAME_COMPLEX.get(), () -> "§2" + I18n.get("tooltip.avaritia.tier", "-" + StringUtils.white_blue(I18n.get("gtocore.tooltip.unknown"))));
        flickerToolTipsBuilder.put(GTOItems.SUPRACAUSAL_MAINFRAME.get(), () -> StringUtils.full_color(I18n.get("gtocore.tooltip.item.tier_circuit", "MAX")));
        flickerToolTipsBuilder.put(GTOItems.SUPRACAUSAL_COMPUTER.get(), () -> StringUtils.full_color(I18n.get("gtocore.tooltip.item.tier_circuit", "OpV")));
        flickerToolTipsBuilder.put(GTOItems.SUPRACAUSAL_ASSEMBLY.get(), () -> StringUtils.full_color(I18n.get("gtocore.tooltip.item.tier_circuit", "UXV")));
        flickerToolTipsBuilder.put(GTOItems.SUPRACAUSAL_PROCESSOR.get(), () -> StringUtils.full_color(I18n.get("gtocore.tooltip.item.tier_circuit", "UIV")));
        flickerToolTipsBuilder.put(GTOItems.COSMIC_ASSEMBLY.get(), () -> StringUtils.dark_purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "UIV")));
        flickerToolTipsBuilder.put(GTOItems.COSMIC_COMPUTER.get(), () -> StringUtils.dark_purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "UXV")));
        flickerToolTipsBuilder.put(GTOItems.COSMIC_MAINFRAME.get(), () -> StringUtils.dark_purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "OpV")));
        flickerToolTipsBuilder.put(GTOItems.COSMIC_PROCESSOR.get(), () -> StringUtils.dark_purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "UEV")));
        flickerToolTipsBuilder.put(GTOItems.EXOTIC_ASSEMBLY.get(), () -> StringUtils.purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "UEV")));
        flickerToolTipsBuilder.put(GTOItems.EXOTIC_COMPUTER.get(), () -> StringUtils.purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "UIV")));
        flickerToolTipsBuilder.put(GTOItems.EXOTIC_MAINFRAME.get(), () -> StringUtils.purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "UXV")));
        flickerToolTipsBuilder.put(GTOItems.EXOTIC_PROCESSOR.get(), () -> StringUtils.purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "UHV")));
        flickerToolTipsBuilder.put(GTOItems.OPTICAL_ASSEMBLY.get(), () -> StringUtils.golden(I18n.get("gtocore.tooltip.item.tier_circuit", "UHV")));
        flickerToolTipsBuilder.put(GTOItems.OPTICAL_COMPUTER.get(), () -> StringUtils.golden(I18n.get("gtocore.tooltip.item.tier_circuit", "UEV")));
        flickerToolTipsBuilder.put(GTOItems.OPTICAL_MAINFRAME.get(), () -> StringUtils.golden(I18n.get("gtocore.tooltip.item.tier_circuit", "UIV")));
        flickerToolTipsBuilder.put(GTOItems.OPTICAL_PROCESSOR.get(), () -> StringUtils.golden(I18n.get("gtocore.tooltip.item.tier_circuit", "UV")));
        flickerToolTipsBuilder.put(GTOItems.BIOWARE_ASSEMBLY.get(), () -> StringUtils.dark_green(I18n.get("gtocore.tooltip.item.tier_circuit", "UV")));
        flickerToolTipsBuilder.put(GTOItems.BIOWARE_COMPUTER.get(), () -> StringUtils.dark_green(I18n.get("gtocore.tooltip.item.tier_circuit", "UHV")));
        flickerToolTipsBuilder.put(GTOItems.BIOWARE_MAINFRAME.get(), () -> StringUtils.dark_green(I18n.get("gtocore.tooltip.item.tier_circuit", "UEV")));
        flickerToolTipsBuilder.put(GTOItems.BIOWARE_PROCESSOR.get(), () -> StringUtils.dark_green(I18n.get("gtocore.tooltip.item.tier_circuit", "ZPM")));

        FLICKER_TOOL_TIPS_MAP = flickerToolTipsBuilder.build();
    }
}
