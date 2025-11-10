package com.gtocore.data.transaction;

import com.gtolib.utils.WalletUtils;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;

public class TransactionRegistration {

    // 构建 TransactionEntry
    TransactionEntry entry = new TransactionEntry.TransactionEntryBuilder()
            .setRendering(new ItemStackTexture(Blocks.COBBLESTONE.asItem()))
            .setDescriptionSupplier(components -> components.add(Component.literal("这是将草方块替换为圆石的交易").withStyle(ChatFormatting.GREEN)))
            .setUnlockCondition(Component.literal("方块下方为草方块且玩家金币100以上"))
            .setPreTransactionCall((isOnPlayerEntity, playerUUID, world, pos, itemStacks) -> {
                BlockPos belowPos = pos.below();
                BlockState belowState = world.getBlockState(belowPos);
                long goldAmount = WalletUtils.getCurrencyAmount(playerUUID, world, "core");
                return belowState.is(Blocks.GRASS_BLOCK) && goldAmount >= 100;
            })
            .setOnTransactionCall((isOnPlayerEntity, playerUUID, world, pos, itemStacks) -> {
                BlockPos belowPos = pos.below();
                world.setBlock(belowPos, Blocks.COBBLESTONE.defaultBlockState(), 3);
                WalletUtils.subtractCurrency(playerUUID, world, "core", 10);
                WalletUtils.addCurrency(playerUUID, world, "core", 10);
            })
            .build();
}
