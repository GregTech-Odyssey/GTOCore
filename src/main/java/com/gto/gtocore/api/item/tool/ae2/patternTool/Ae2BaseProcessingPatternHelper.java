package com.gto.gtocore.api.item.tool.ae2.patternTool;

import com.gto.gtocore.GTOCore;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKeyType;
import appeng.api.stacks.GenericStack;
import appeng.crafting.pattern.AEProcessingPattern;
import appeng.crafting.pattern.EncodedPatternItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ae2BaseProcessingPatternHelper {

    // 乘或除 输入解码后样板，输出编码后样板
    public static ItemStack multiplyScale(int scale, boolean div, AEProcessingPattern patternDetail, long maxItemStack, long maxFluidStack) {
        var input = patternDetail.getSparseInputs();
        var output = patternDetail.getOutputs();
        if (checkModify(input, scale, div, maxItemStack, maxFluidStack) && checkModify(output, scale, div, 10000000L, 1000000L)) {
            var mulInput = new GenericStack[input.length];
            var mulOutput = new GenericStack[output.length];
            modifyStacks(input, mulInput, scale, div);
            modifyStacks(output, mulOutput, scale, div);
            return PatternDetailsHelper.encodeProcessingPattern(mulInput, mulOutput);
        }
        GTOCore.LOGGER.info("内部错误：无法整除 或 乘数过大");
        return null;
    }

    // 从样板物品解码样板
    public static AEProcessingPattern decodeToAEProcessingPattern(ItemStack patternStack, ServerPlayer serverPlayer) {
        if (patternStack.getItem() instanceof EncodedPatternItem patternItem_1) {
            if (patternItem_1.decode(patternStack, serverPlayer.level(), false) instanceof AEProcessingPattern processStack) {
                return processStack;
            } else {
                GTOCore.LOGGER.info("Ae2BaseProcessingPattern requires a EncodedPatternItem 意外之内的输入：非处理样板");
            }
        } else {
            GTOCore.LOGGER.info("Ae2BaseProcessingPattern requires a EncodedPatternItem 意外之内的输入：非AE样板");
        }
        return null;
    }

    // 转换到list方便操作
    public static List<GenericStack> transGenericStackArrayToList(GenericStack[] genericStackArray) {
        return new ArrayList<>(Arrays.asList(genericStackArray));
    }

    // 转换回[]，构造AE2 API参数
    public static GenericStack[] transGenericStackListToArray(List<GenericStack> genericStackList) {
        GenericStack[] genericStackArrayList = new GenericStack[genericStackList.size()];
        return genericStackList.toArray(genericStackArrayList);
    }

    // 返回排除黑名单物品后的列表
    public static List<GenericStack> GenericStackListBlackFilter(List<GenericStack> inputGenericStacks, List<Item> matchItemList) {
        return inputGenericStacks.stream().filter(
                genericStack -> !itemMatch(genericStack, matchItemList)).toList();
    }

    private static boolean itemMatch(GenericStack genericStack, List<Item> matchItemList) {
        for (Item item : matchItemList) {
            if (genericStack.what().equals(AEItemKey.of(item))) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkModify(GenericStack[] stacks, int scale, boolean div, long maxItemStack, long maxFluidStack) {
        if (div) {
            for (var stack : stacks) {
                if (stack != null) {
                    if (stack.amount() % scale != 0) {
                        return false;
                    }
                }
            }
        } else {
            for (var stack : stacks) {
                if (stack != null) {
                    if (stack.what().getType().equals(AEKeyType.fluids())) {
                        long upper = maxFluidStack * stack.what().getAmountPerUnit();
                        if (stack.amount() * scale > upper) {
                            return false;
                        }
                    }
                    if (stack.what().getType().equals(AEKeyType.items())) {
                        long upper = maxItemStack * stack.what().getAmountPerUnit();
                        if (stack.amount() * scale > upper) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private static void modifyStacks(GenericStack[] stacks, GenericStack[] des, int scale, boolean div) {
        for (int i = 0; i < stacks.length; i++) {
            if (stacks[i] != null) {
                long amt = div ? stacks[i].amount() / scale : stacks[i].amount() * scale;
                des[i] = new GenericStack(stacks[i].what(), amt);
            }
        }
    }
}
