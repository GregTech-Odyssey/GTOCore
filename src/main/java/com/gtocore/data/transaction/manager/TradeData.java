package com.gtocore.data.transaction.manager;

import com.gregtechceu.gtceu.api.transfer.fluid.IFluidHandlerModifiable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandlerModifiable;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * @param level     位置信息
 * @param inputItem 输入输出存储
 * @param uuid      玩家信息
 */
public record TradeData(@Nullable Level level, BlockPos pos, IItemHandlerModifiable inputItem,
                        IItemHandlerModifiable outputItem, IFluidHandlerModifiable inputFluid,
                        IFluidHandlerModifiable outputFluid, UUID uuid, List<UUID> sharedUUIDs, UUID teamUUID) {

}
