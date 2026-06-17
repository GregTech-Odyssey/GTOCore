package com.gtocore.data.transaction.manager;

import com.gregtechceu.gtceu.api.transfer.fluid.ICustomFluidStackHandler;
import com.gregtechceu.gtceu.api.transfer.item.ICustomItemStackHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * @param level     位置信息
 * @param inputItem 输入输出存储
 * @param uuid      玩家信息
 */
public record TradeData(@Nullable Level level, BlockPos pos, ICustomItemStackHandler inputItem,
                        ICustomItemStackHandler outputItem, ICustomFluidStackHandler inputFluid,
                        ICustomFluidStackHandler outputFluid, UUID uuid, List<UUID> sharedUUIDs, UUID teamUUID) {

}
