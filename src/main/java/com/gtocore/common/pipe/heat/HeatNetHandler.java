package com.gtocore.common.pipe.heat;

import com.gtocore.common.blockentity.HeatPipeBlockEntity;

import com.gtolib.api.capability.IHeatContainer;

import net.minecraft.core.Direction;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class HeatNetHandler implements IHeatContainer {

    private final HeatPipeBlockEntity pipe;
    private final Direction facing;

    @Getter
    private HeatPipeNet net;

    private boolean transfer;

    public HeatNetHandler(HeatPipeNet net, @NotNull HeatPipeBlockEntity pipe, @Nullable Direction facing) {
        this.net = net;
        this.pipe = pipe;
        this.facing = facing;
    }

    public void updateNetwork(HeatPipeNet net) {
        this.net = net;
    }

    @Override
    public long acceptHeatFromNetwork(Object sender, Direction side, long heat, double temperature, double heatCapacity, double baseTransferRate, int rateMultiplier) {
        if (transfer || net == null) return 0;
        if (side == null) {
            if (facing == null) return 0;
            side = facing;
        }
        long heatUsed = 0;
        var pos = pipe.getPipePos();
        for (var path : net.getNetData(pipe.getPipeLongPos(), pos)) {
            long add = heat - heatUsed;
            if (add <= 0) break;
            if (side == path.getTargetFacing() && pos.equals(path.getTargetPipePos())) continue;
            var handler = path.getHandler(getNet().getLevel());
            if (handler == null) continue;
            var facing = path.getTargetFacing().getOpposite();
            transfer = true;
            long accept = handler.acceptHeatFromNetwork(sender, facing, add, temperature, heatCapacity, baseTransferRate, rateMultiplier);
            transfer = false;
            if (accept == 0) continue;
            heatUsed += accept;
        }
        return heatUsed;
    }

    @Override
    public long getMaxTemperature() {
        return 0;
    }

    @Override
    public double getTemperature() {
        return 0;
    }

    @Override
    public double getHeatCapacity() {
        return 0;
    }

    @Override
    public double getBaseTransferRate() {
        return 0;
    }

    @Override
    public double getCooldownRate() {
        return 0;
    }

    @Override
    public double getAmbientTemperature() {
        return 0;
    }

    @Override
    public long getCurrentHeat() {
        return 0;
    }

    @Override
    public void setCurrentHeat(long heat) {}

    @Override
    public long getMaxHeat() {
        return 0;
    }

    @Override
    public boolean heatIO(Direction side) {
        return false;
    }

    @Override
    public long addHeat(long amount, int rateMultiplier, boolean simulate) {
        return 0;
    }

    @Override
    public long removeHeat(long amount, int rateMultiplier, boolean simulate) {
        return 0;
    }

    @Override
    public long addHeatUnrestricted(long amount, boolean simulate) {
        return 0;
    }

    @Override
    public long removeHeatUnrestricted(long amount, boolean simulate) {
        return 0;
    }

    @Override
    public double transferHeatToAdjacent(int rateMultiplier) {
        return 0;
    }

    @Override
    public int getSignal() {
        return 0;
    }
}
