package com.gtocore.data.transaction;

import com.gtolib.utils.WalletUtils;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;

import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;

/**
 * 交易实例注册示例：展示如何使用TransactionEntry构建具体交易
 */
public class TransactionRegistration {

    // 构建一个"草方块换圆石"的交易实例（修复原示例中的逻辑错误）
    TransactionEntry grassToCobblestone = new TransactionEntry.TransactionEntryBuilder()
            // 显示图标：圆石物品纹理
            .rendering(new ItemStackTexture(Blocks.COBBLESTONE.asItem()))
            // 交易描述：绿色文本说明
            .descriptionSupplier(components -> components.add(
                    Component.literal("消耗100金币，将下方草方块转换为圆石").withStyle(ChatFormatting.GREEN)))
            // 解锁条件说明
            .unlockCondition("需满足：下方是草方块，且玩家金币≥100")
            // 交易前检查：验证条件
            .preCheck(context -> {
                BlockPos belowPos = context.pos().below(); // 从上下文获取位置
                // 检查下方是否为草方块
                boolean isGrassBelow = context.world().getBlockState(belowPos).is(Blocks.GRASS_BLOCK);
                // 检查金币是否足够（使用工具类获取）
                long gold = WalletUtils.getCurrencyAmount(context.playerUUID(), context.world(), "core");
                return isGrassBelow && gold >= 100;
            })
            // 交易执行：替换方块并扣金币
            .onExecute(context -> {
                BlockPos belowPos = context.pos().below();
                // 将下方方块替换为圆石
                context.world().setBlock(belowPos, Blocks.COBBLESTONE.defaultBlockState(), 3);
                // 扣除100金币（修复原示例中"扣10加10"的逻辑错误）
                WalletUtils.subtractCurrency(context.playerUUID(), context.world(), "core", 100);
            })
            .build();
}
