package com.gto.gtocore.api.item.tool.ae2.patternTool;

import com.gto.gtocore.GTOCore;

import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.stacks.GenericStack;
import appeng.crafting.pattern.AEProcessingPattern;
import com.tterrag.registrate.util.entry.ItemEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Ae2BaseProcessingPattern {

    private ItemStack patternStack; // 样板itemStack
    private final ServerPlayer serverPlayer;
    /*
     * 此处一个对象就是一个样板，因此过滤器决定某些物品/流体会不会在此样板中出现
     */
    private final List<Item> DefaultBlackItem = new ArrayList<>(List.of(
            GTItems.INTEGRATED_CIRCUIT.asItem()));

    public void setDefaultFilter() {
        // 解码，转换为list方便操作
        AEProcessingPattern aeProcessingPattern = Ae2BaseProcessingPatternHelper.decodeToAEProcessingPattern(patternStack, serverPlayer);
        List<GenericStack> inputGenericStacks = Ae2BaseProcessingPatternHelper.transGenericStackArrayToList(aeProcessingPattern.getSparseInputs());
        List<GenericStack> outputGenericStacks = Ae2BaseProcessingPatternHelper.transGenericStackArrayToList(aeProcessingPattern.getSparseOutputs());

        inputGenericStacks = Ae2BaseProcessingPatternHelper.GenericStackListBlackFilter(inputGenericStacks, DefaultBlackItem);
        outputGenericStacks = Ae2BaseProcessingPatternHelper.GenericStackListBlackFilter(outputGenericStacks, DefaultBlackItem);

        // 重新编码，并更新此对象
        patternStack = PatternDetailsHelper.encodeProcessingPattern(
                Ae2BaseProcessingPatternHelper.transGenericStackListToArray(inputGenericStacks),
                Ae2BaseProcessingPatternHelper.transGenericStackListToArray(outputGenericStacks));
    }

    public void useSetScale(int newScale, boolean div, long maxItemStack, long maxFluidStack) {
        try {
            ItemStack oldPatternStack = this.patternStack;
            ItemStack newPatternStack;
            // 乘或除到新的份数
            newPatternStack = Ae2BaseProcessingPatternHelper.multiplyScale(
                    newScale,
                    div,
                    Objects.requireNonNull(Ae2BaseProcessingPatternHelper.decodeToAEProcessingPattern(oldPatternStack, serverPlayer)),
                    maxItemStack,
                    maxFluidStack);
            if (newPatternStack != null && !newPatternStack.isEmpty()) {
                this.patternStack = newPatternStack;
            }
        } catch (Exception e) {
            GTOCore.LOGGER.error(e.getMessage());
        }
    }

    public void setScale(int newScale, boolean div, long maxItemStack, long maxFluidStack) {
        maxFluidStack = Math.min(maxFluidStack, 1000000L);
        maxItemStack = Math.min(maxItemStack, 1000000L);
        useSetScale(newScale, div, maxItemStack, maxFluidStack);
    }

    public void setScale(int newScale, boolean div) {
        long maxItemStack = 9999999L;
        long maxFluidStack = 9999999L;
        useSetScale(newScale, div, maxItemStack, maxFluidStack);
    }

    public void setLore(Component component) {
        this.patternStack = this.patternStack.kjs$withLore(new Component[] { component });
    }

    public ItemStack getPatternItemStack() {
        return this.patternStack;
    }

    public Ae2BaseProcessingPattern(ItemStack patternStack,
                                    ServerPlayer serverPlayer) {
        this.patternStack = patternStack;
        this.serverPlayer = serverPlayer;

        // 不获取返回值，仅检测是否可解码
        Ae2BaseProcessingPatternHelper.decodeToAEProcessingPattern(patternStack, serverPlayer);

        try {
            for (ItemEntry<Item> shapeMold : GTItems.SHAPE_MOLDS) {
                if (shapeMold != null) {
                    this.DefaultBlackItem.add(shapeMold.asItem());
                }
            }
            for (ItemEntry<Item> shapeMold : GTItems.SHAPE_EXTRUDERS) {
                if (shapeMold != null) {
                    this.DefaultBlackItem.add(shapeMold.asItem());
                }
            }
        } catch (Exception e) {
            GTOCore.LOGGER.error(e.getMessage());
        }
    }
}
