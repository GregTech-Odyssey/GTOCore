package com.gtocore.data.transaction.manager;

import com.gregtechceu.gtceu.api.transfer.fluid.IFluidHandlerModifiable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandlerModifiable;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Getter
public class TradeData {

    /** 位置信息 */
    @Nullable
    private final Level level;
    private final BlockPos pos;

    /** 输入输出存储 */
    private final IItemHandlerModifiable inputItem;
    private final IItemHandlerModifiable outputItem;
    private final IFluidHandlerModifiable inputFluid;
    private final IFluidHandlerModifiable outputFluid;

    /** 玩家信息 */
    private final UUID uuid;
    private final UUID teamUUID;

    public TradeData(@Nullable Level level,
                     BlockPos pos,
                     IItemHandlerModifiable inputItem,
                     IItemHandlerModifiable outputItem,
                     IFluidHandlerModifiable inputFluid,
                     IFluidHandlerModifiable outputFluid,
                     UUID uuid,
                     UUID teamUUID) {
        this.level = level;
        this.pos = pos;
        this.inputItem = inputItem;
        this.outputItem = outputItem;
        this.inputFluid = inputFluid;
        this.outputFluid = outputFluid;
        this.uuid = uuid;
        this.teamUUID = teamUUID;
    }
}
