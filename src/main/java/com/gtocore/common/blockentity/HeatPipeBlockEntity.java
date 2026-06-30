package com.gtocore.common.blockentity;

import com.gtocore.common.pipe.heat.*;

import com.gtolib.api.capability.IHeatContainer;

import com.gregtechceu.gtceu.api.blockentity.PipeBlockEntity;
import com.gregtechceu.gtceu.api.capability.GTCapability;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.EnumMap;

public final class HeatPipeBlockEntity extends PipeBlockEntity<HeatPipeType, HeatPipeProperties> {

    @Getter
    private final EnumMap<Direction, HeatNetHandler> handlers = new EnumMap<>(Direction.class);
    private WeakReference<HeatPipeNet> currentPipeNet = new WeakReference<>(null);
    private HeatNetHandler defaultHandler;

    public HeatPipeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public boolean canHaveBlockedFaces() {
        return false;
    }

    @Override
    public @Nullable <T> Object getGTCapability(@NotNull Class<T> cap, @Nullable Direction side) {
        if (cap == IHeatContainer.class) {
            if ((side == null || isConnected(side)) && !level.isClientSide && checkNetwork()) {
                return handlers.getOrDefault(side, defaultHandler);
            }
            return GTCapability.EMPTY;
        }
        return null;
    }

    private void initHandlers() {
        var net = getHeatPipeNet();
        if (net == null) return;
        for (Direction facing : GTUtil.DIRECTIONS) {
            handlers.put(facing, new HeatNetHandler(net, this, facing));
        }
        defaultHandler = new HeatNetHandler(net, this, null);
    }

    private boolean checkNetwork() {
        if (handlers.isEmpty()) initHandlers();
        if (defaultHandler != null) {
            var current = getHeatPipeNet();
            if (defaultHandler.getNet() != current) {
                defaultHandler.updateNetwork(current);
                for (var handler : handlers.values()) {
                    handler.updateNetwork(current);
                }
            }
        }
        return this.currentPipeNet.get() != null;
    }

    private HeatPipeNet getHeatPipeNet() {
        if (level == null || level.isClientSide) return null;
        var currentPipeNet = this.currentPipeNet.get();
        if (currentPipeNet != null && currentPipeNet.isValid() && currentPipeNet.containsNode(getPipeLongPos())) return currentPipeNet;
        var worldNet = (LevelHeatPipeNet) getPipeBlock().getWorldPipeNet((ServerLevel) getLevel());
        currentPipeNet = worldNet.getNetFromPos(getPipePos(), getPipeLongPos());
        if (currentPipeNet != null) {
            this.currentPipeNet = new WeakReference<>(currentPipeNet);
        }
        return currentPipeNet;
    }
}
