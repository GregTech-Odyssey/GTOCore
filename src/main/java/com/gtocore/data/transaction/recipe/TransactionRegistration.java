package com.gtocore.data.transaction.recipe;

import com.gtocore.data.transaction.common.TradingStationMachine;
import com.gtocore.data.transaction.recipe.entry.TransactionEntry;

import com.gtolib.GTOCore;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;

import java.util.List;

/**
 * 交易实例注册示例：展示如何使用TransactionEntry构建具体交易
 */
public class TransactionRegistration {

    public static TransactionEntry trade = new TransactionEntry.Builder()
            .texture(new ItemStackTexture(Items.BREAD)) // 图标用面包
            .description(List.of(Component.literal("10个木头 → 1个面包")))
            .unlockCondition("无解锁条件")
            .inputItem(new ItemStack(Items.OAK_WOOD, 10)) // 输入10个木头
            .outputItem(new ItemStack(Items.BREAD, 1)) // 输出1个面包
            .preCheck((context, entry) -> {
                // 额外检查：玩家背包至少有10个木头（实际需结合context实现）
                return true;
            })
            .onExecute((context, entry) -> {
                // 执行逻辑：扣减木头，添加面包（实际需操作玩家背包）
            })
            .build();

    // 创建交易条目实例
    public static TransactionEntry createJungleBeaconTrade() {
        return new TransactionEntry.Builder()
                // 设置UI显示：信标图标+描述
                .texture(new ItemStackTexture(Blocks.BEACON.asItem()))
                .description(List.of(
                        Component.literal("丛林祭坛仪式"),
                        Component.literal("消耗：10个绿宝石"),
                        Component.literal("效果：在祭坛位置生成信标")))
                // 解锁条件文本
                .unlockCondition("需要在丛林中，且祭坛周围有草方块")

                // 输入资源（交易消耗）
                .inputItem(new ItemStack(Items.EMERALD, 10)) // 10个绿宝石（假设"emerald"是绿宝石货币ID）

                // 前置检查：3x3区域有草方块 + 丛林群系
                .preCheck(TransactionRegistration::checkJungleAndGrass)

                // 执行逻辑：在坐标生成信标
                .onExecute(TransactionRegistration::spawnBeaconAtPos)

                .build();
    }

    // 前置检查逻辑：3x3区域有草方块且在丛林群系
    private static boolean checkJungleAndGrass(TradingStationMachine machine, TransactionEntry entry) {
        if (!(machine.getLevel() instanceof ServerLevel serverLevel)) return false;

        GTOCore.LOGGER.info("run checkJungleAndGrass");
        BlockPos centerPos = machine.getPos();

        // 1. 检查是否在丛林群系（包括所有丛林变种，通过标签判断更灵活）
        boolean isJungleBiome = serverLevel.getBiome(centerPos).is(BiomeTags.IS_JUNGLE);
        if (!isJungleBiome) {
            // 可以通过context向玩家发送提示（实际需结合玩家对象实现）
            return false;
        }

        // 2. 检查3x3区域内是否至少有1个草方块（中心坐标向xyz各扩展1格）
        boolean hasGrassIn3x3 = false;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    BlockPos checkPos = centerPos.offset(dx, dy, dz);
                    BlockState state = serverLevel.getBlockState(checkPos);
                    if (state.is(Blocks.GRASS_BLOCK)) {
                        hasGrassIn3x3 = true;
                        break; // 找到草方块就退出循环
                    }
                }
                if (hasGrassIn3x3) break;
            }
            if (hasGrassIn3x3) break;
        }

        return hasGrassIn3x3; // 同时满足群系和草方块条件才返回true
    }

    // 执行逻辑：在交易坐标生成信标
    private static void spawnBeaconAtPos(TradingStationMachine machine, TransactionEntry entry) {
        ServerLevel world = (ServerLevel) machine.getLevel();
        BlockPos pos = machine.getPos();

        GTOCore.LOGGER.info("run spawnBeaconAtPos");

        // 在目标位置放置信标（替换原有方块）
        world.setBlock(pos, Blocks.BEACON.defaultBlockState(), 3); // 3=更新标志（同步客户端+触发方块更新）

        // 可选：添加粒子效果或音效增强体验
        world.levelEvent(2001, pos, Block.getId(Blocks.BEACON.defaultBlockState())); // 方块放置粒子
    }
}
