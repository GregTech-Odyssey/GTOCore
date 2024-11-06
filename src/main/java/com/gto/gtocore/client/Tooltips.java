package com.gto.gtocore.client;

import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.utils.StringUtil;

import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.Item;

import com.tterrag.registrate.util.entry.RegistryEntry;

import java.util.*;
import java.util.function.Supplier;

public class Tooltips {

    public static final Map<Item, String[]> TOOL_TIPS_MAP = new HashMap<>();
    public static final Map<Item, Supplier<String>> FLICKER_TOOL_TIPS_MAP = new HashMap<>();
    public static final Set<Item> suprachronalCircuitSet = new HashSet<>();
    public static final Set<Item> magnetoresonaticcircuitSet = new HashSet<>();

    static {
        suprachronalCircuitSet.addAll(Arrays.stream(GTOItems.SUPRACHRONAL_CIRCUIT).map(RegistryEntry::get).toList());
        magnetoresonaticcircuitSet.addAll(Arrays.stream(GTOItems.MAGNETO_RESONATIC_CIRCUIT).filter(Objects::nonNull).map(RegistryEntry::get).toList());
        TOOL_TIPS_MAP.put(GTOItems.CREATE_ULTIMATE_BATTERY.get(), new String[] { "§7Can generate energy out of thin air" });
        TOOL_TIPS_MAP.put(GTOItems.SUPRACHRONAL_MAINFRAME_COMPLEX.get(), new String[] { "§7Can generate computing power out of thin air" });
        TOOL_TIPS_MAP.put(GTItems.VACUUM_TUBE.get(), new String[] { "Right-click the handheld rough vacuum tube to obtain vacuum supply from a machine with vacuum level greater than 0" });
        TOOL_TIPS_MAP.put(GTOItems.WARPED_ENDER_PEAL.get(), new String[] { "Sneak and right-click to set a personal teleportation point, right-click to teleport to the point" });
        TOOL_TIPS_MAP.put(GTOBlocks.NAQUADRIA_CHARGE.asItem(), new String[] { "Can be activated by Quantum Star" });
        TOOL_TIPS_MAP.put(GTOBlocks.LEPTONIC_CHARGE.asItem(), new String[] { "Can be activated by Gravity Star" });
        TOOL_TIPS_MAP.put(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem(), new String[] { "Can be activated by Unstable Star" });
        TOOL_TIPS_MAP.put(GTOBlocks.COIL_URUIUM.asItem(), new String[] { "Can provide 32000K furnace temperature for the hyper-dimensional plasma furnace", "Only this coil can be used in stellar furnace mode" });
        TOOL_TIPS_MAP.put(GTOBlocks.ESSENCE_BLOCK.asItem(), new String[] { "Place bone block in the transformation chamber to obtain" });
        TOOL_TIPS_MAP.put(GTOBlocks.DRACONIUM_BLOCK_CHARGED.asItem(), new String[] { "Place obsidian infused with dragon power in the transformation chamber to obtain" });
        TOOL_TIPS_MAP.put(GTOItems.HYPER_STABLE_SELF_HEALING_ADHESIVE.get(), new String[] { "§7Selective complete adhesion, effective even when torn or damaged" });
        TOOL_TIPS_MAP.put(GTOItems.BLACK_BODY_NAQUADRIA_SUPERSOLID.get(), new String[] { "§7Flows like a liquid, does not reflect any electromagnetic waves, perfectly absorbs and transmits" });
        TOOL_TIPS_MAP.put(GTOItems.HUI_CIRCUIT_1.get(), new String[] { "§793015-Floating Point Operations/Second" });
        TOOL_TIPS_MAP.put(GTOItems.HUI_CIRCUIT_2.get(), new String[] { "§776M Processing Unit" });
        TOOL_TIPS_MAP.put(GTOItems.HUI_CIRCUIT_3.get(), new String[] { "§7Invalid RSA Algorithm" });
        TOOL_TIPS_MAP.put(GTOItems.HUI_CIRCUIT_4.get(), new String[] { "§7The 56th Mersenne Prime" });
        TOOL_TIPS_MAP.put(GTOItems.HUI_CIRCUIT_5.get(), new String[] { "§7Paradox" });
        TOOL_TIPS_MAP.put(GTOItems.BIOWARE_PRINTED_CIRCUIT_BOARD.get(), new String[] { "§7Biologically mutated circuit board" });
        TOOL_TIPS_MAP.put(GTOItems.OPTICAL_PRINTED_CIRCUIT_BOARD.get(), new String[] { "§7Optically injected circuit board" });
        TOOL_TIPS_MAP.put(GTOItems.EXOTIC_PRINTED_CIRCUIT_BOARD.get(), new String[] { "§7Quantum circuit board" });
        TOOL_TIPS_MAP.put(GTOItems.COSMIC_PRINTED_CIRCUIT_BOARD.get(), new String[] { "§7Circuit board carrying the universe" });
        TOOL_TIPS_MAP.put(GTOItems.SUPRACAUSAL_PRINTED_CIRCUIT_BOARD.get(), new String[] { "§7Ultimate circuit board" });
        TOOL_TIPS_MAP.put(GTOItems.SUPRACAUSAL_MAINFRAME.get(), new String[] { "§7Seer" });
        TOOL_TIPS_MAP.put(GTOItems.SUPRACAUSAL_COMPUTER.get(), new String[] { "§7Utilizes the advantage of wormholes" });
        TOOL_TIPS_MAP.put(GTOItems.SUPRACAUSAL_ASSEMBLY.get(), new String[] { "§7A massive singularity" });
        TOOL_TIPS_MAP.put(GTOItems.SUPRACAUSAL_PROCESSOR.get(), new String[] { "§7The power of black holes" });
        TOOL_TIPS_MAP.put(GTOItems.COSMIC_ASSEMBLY.get(), new String[] { "§7Gently rotating in a grasp" });
        TOOL_TIPS_MAP.put(GTOItems.COSMIC_COMPUTER.get(), new String[] { "§7Density approaching singularity" });
        TOOL_TIPS_MAP.put(GTOItems.COSMIC_MAINFRAME.get(), new String[] { "§7The power of the universe, intimidating through the ages!" });
        TOOL_TIPS_MAP.put(GTOItems.COSMIC_PROCESSOR.get(), new String[] { "§7Holding the stars" });
        TOOL_TIPS_MAP.put(GTOItems.EXOTIC_ASSEMBLY.get(), new String[] { "§7Quantum random walk" });
        TOOL_TIPS_MAP.put(GTOItems.EXOTIC_COMPUTER.get(), new String[] { "§7Controlling everything with spin" });
        TOOL_TIPS_MAP.put(GTOItems.EXOTIC_MAINFRAME.get(), new String[] { "§7Circuits from the future" });
        TOOL_TIPS_MAP.put(GTOItems.EXOTIC_PROCESSOR.get(), new String[] { "§7Super magnetic semiconductor circuit" });
        TOOL_TIPS_MAP.put(GTOItems.OPTICAL_ASSEMBLY.get(), new String[] { "§7The power of lasers!" });
        TOOL_TIPS_MAP.put(GTOItems.OPTICAL_COMPUTER.get(), new String[] { "§7In the blink of an eye" });
        TOOL_TIPS_MAP.put(GTOItems.OPTICAL_MAINFRAME.get(), new String[] { "§7Can it be even faster?" });
        TOOL_TIPS_MAP.put(GTOItems.OPTICAL_PROCESSOR.get(), new String[] { "§7Light-speed computation" });
        TOOL_TIPS_MAP.put(GTOItems.BIOWARE_ASSEMBLY.get(), new String[] { "§7Seems to hear whispers" });
        TOOL_TIPS_MAP.put(GTOItems.BIOWARE_COMPUTER.get(), new String[] { "§7Covered in slime between metals" });
        TOOL_TIPS_MAP.put(GTOItems.BIOWARE_MAINFRAME.get(), new String[] { "§7Network of microbial consciousness" });
        TOOL_TIPS_MAP.put(GTOItems.BIOWARE_PROCESSOR.get(), new String[] { "§7Viscous organic slurry adheres to the surface" });
        TOOL_TIPS_MAP.put(GTOBlocks.QUANTUM_GLASS.asItem(), new String[] { "Dense but Transparent", "§bGlass & Elegance" });

        FLICKER_TOOL_TIPS_MAP.put(GTOItems.CREATE_ULTIMATE_BATTERY.get(), () -> "§2" + I18n.get("tooltip.avaritia.tier", "-" + StringUtil.white_blue(I18n.get("gtocore.tooltip.unknown"))));
        FLICKER_TOOL_TIPS_MAP.put(GTOItems.SUPRACHRONAL_MAINFRAME_COMPLEX.get(), () -> "§2" + I18n.get("tooltip.avaritia.tier", "-" + StringUtil.white_blue(I18n.get("gtocore.tooltip.unknown"))));
        FLICKER_TOOL_TIPS_MAP.put(GTOItems.SUPRACAUSAL_MAINFRAME.get(), () -> StringUtil.full_color(I18n.get("gtocore.tooltip.item.tier_circuit", "MAX")));
        FLICKER_TOOL_TIPS_MAP.put(GTOItems.SUPRACAUSAL_COMPUTER.get(), () -> StringUtil.full_color(I18n.get("gtocore.tooltip.item.tier_circuit", "OpV")));
        FLICKER_TOOL_TIPS_MAP.put(GTOItems.SUPRACAUSAL_ASSEMBLY.get(), () -> StringUtil.full_color(I18n.get("gtocore.tooltip.item.tier_circuit", "UXV")));
        FLICKER_TOOL_TIPS_MAP.put(GTOItems.SUPRACAUSAL_PROCESSOR.get(), () -> StringUtil.full_color(I18n.get("gtocore.tooltip.item.tier_circuit", "UIV")));
        FLICKER_TOOL_TIPS_MAP.put(GTOItems.COSMIC_ASSEMBLY.get(), () -> StringUtil.dark_purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "UIV")));
        FLICKER_TOOL_TIPS_MAP.put(GTOItems.COSMIC_COMPUTER.get(), () -> StringUtil.dark_purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "UXV")));
        FLICKER_TOOL_TIPS_MAP.put(GTOItems.COSMIC_MAINFRAME.get(), () -> StringUtil.dark_purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "OpV")));
        FLICKER_TOOL_TIPS_MAP.put(GTOItems.COSMIC_PROCESSOR.get(), () -> StringUtil.dark_purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "UEV")));
        FLICKER_TOOL_TIPS_MAP.put(GTOItems.EXOTIC_ASSEMBLY.get(), () -> StringUtil.purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "UEV")));
        FLICKER_TOOL_TIPS_MAP.put(GTOItems.EXOTIC_COMPUTER.get(), () -> StringUtil.purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "UIV")));
        FLICKER_TOOL_TIPS_MAP.put(GTOItems.EXOTIC_MAINFRAME.get(), () -> StringUtil.purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "UXV")));
        FLICKER_TOOL_TIPS_MAP.put(GTOItems.EXOTIC_PROCESSOR.get(), () -> StringUtil.purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "UHV")));
        FLICKER_TOOL_TIPS_MAP.put(GTOItems.OPTICAL_ASSEMBLY.get(), () -> StringUtil.golden(I18n.get("gtocore.tooltip.item.tier_circuit", "UHV")));
        FLICKER_TOOL_TIPS_MAP.put(GTOItems.OPTICAL_COMPUTER.get(), () -> StringUtil.golden(I18n.get("gtocore.tooltip.item.tier_circuit", "UEV")));
        FLICKER_TOOL_TIPS_MAP.put(GTOItems.OPTICAL_MAINFRAME.get(), () -> StringUtil.golden(I18n.get("gtocore.tooltip.item.tier_circuit", "UIV")));
        FLICKER_TOOL_TIPS_MAP.put(GTOItems.OPTICAL_PROCESSOR.get(), () -> StringUtil.golden(I18n.get("gtocore.tooltip.item.tier_circuit", "UV")));
        FLICKER_TOOL_TIPS_MAP.put(GTOItems.BIOWARE_ASSEMBLY.get(), () -> StringUtil.dark_green(I18n.get("gtocore.tooltip.item.tier_circuit", "UV")));
        FLICKER_TOOL_TIPS_MAP.put(GTOItems.BIOWARE_COMPUTER.get(), () -> StringUtil.dark_green(I18n.get("gtocore.tooltip.item.tier_circuit", "UHV")));
        FLICKER_TOOL_TIPS_MAP.put(GTOItems.BIOWARE_MAINFRAME.get(), () -> StringUtil.dark_green(I18n.get("gtocore.tooltip.item.tier_circuit", "UEV")));
        FLICKER_TOOL_TIPS_MAP.put(GTOItems.BIOWARE_PROCESSOR.get(), () -> StringUtil.dark_green(I18n.get("gtocore.tooltip.item.tier_circuit", "ZPM")));
    }
}
