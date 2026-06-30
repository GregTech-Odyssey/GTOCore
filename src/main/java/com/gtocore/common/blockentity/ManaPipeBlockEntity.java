package com.gtocore.common.blockentity;

import com.gtocore.common.pipe.mana.*;

import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.blockentity.PipeBlockEntity;
import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.api.mana.ManaCollector;
import vazkii.botania.api.mana.ManaPool;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.api.mana.spark.SparkAttachable;

import java.lang.ref.WeakReference;

public final class ManaPipeBlockEntity extends PipeBlockEntity<ManaPipeType, ManaPipeProperties> implements ManaCollector {

    private WeakReference<ManaPipeNet> currentPipeNet = new WeakReference<>(null);

    public ManaPipeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == BotaniaForgeCapabilities.MANA_RECEIVER) {
            if ((side == null || isConnected(side)) && getManaPipeNet() != null) {
                return BotaniaForgeCapabilities.MANA_RECEIVER.orEmpty(cap, LazyOptional.of(() -> this));
            }
            return LazyOptional.empty();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void clearRemoved() {
        super.clearRemoved();
        if (blockedSide != null && isBlocked(blockedSide)) {
            updateTransferTick(true, this::autoTransfer);
        }
    }

    @Override
    protected void blockedChanged(boolean isBlocked) {
        updateTransferTick(isBlocked && blockedSide != null, this::autoTransfer);
    }

    @Override
    protected void updateNetworkConnection(@NotNull Direction side, boolean connected) {
        super.updateNetworkConnection(side, connected);
        this.updateTransferTick(this.blockedSide != null && this.isBlocked(this.blockedSide), this::autoTransfer);
    }

    @Override
    public void onNeighborChanged() {
        super.onNeighborChanged();
        updateTransferTick(blockedSide != null && isBlocked(blockedSide), this::autoTransfer);
    }

    private void autoTransfer() {
        if (getManaPipeNet() == null) return;
        boolean hasHandler = false;
        autoTransfer = true;
        for (var facing : GTUtil.DIRECTIONS) {
            if (facing != blockedSide && isConnected(facing)) {
                var be = getNeighborBlockEntity(facing);
                if (be == null || be instanceof PipeBlockEntity<?, ?>) continue;
                if (GTCapabilityHelper.getBlockEntityCapability(BotaniaForgeCapabilities.MANA_RECEIVER, be, facing.getOpposite()) instanceof ManaPool pool) {
                    hasHandler = true;
                    var self = this;
                    var manaInPool = pool.getCurrentMana();
                    if (manaInPool > 0 && !self.isFull()) {
                        int manaMissing = self.getMaxMana() - self.getCurrentMana();
                        int manaToRemove = Math.min(manaInPool, manaMissing);
                        pool.receiveMana(-manaToRemove);
                        self.receiveMana(manaToRemove);
                    }
                }
            }
        }
        autoTransfer = false;
        if (!hasHandler) {
            transferSubs.unsubscribe();
            transferSubs = null;
        }
    }

    private ManaPipeNet getManaPipeNet() {
        if (level == null || level.isClientSide) return null;
        var currentPipeNet = this.currentPipeNet.get();
        if (currentPipeNet != null && currentPipeNet.isValid() && currentPipeNet.containsNode(longPos)) return currentPipeNet;
        var worldNet = (LevelManaPipeNet) getPipeBlock().getWorldPipeNet((ServerLevel) getLevel());
        currentPipeNet = worldNet.getNetFromPos(getPipePos(), longPos);
        if (currentPipeNet != null) {
            this.currentPipeNet = new WeakReference<>(currentPipeNet);
        }
        return currentPipeNet;
    }

    @Override
    public void onClientDisplayTick() {}

    @Override
    public float getManaYieldMultiplier(ManaBurst burst) {
        return 1;
    }

    @Override
    public Level getManaReceiverLevel() {
        return level;
    }

    @Override
    public BlockPos getManaReceiverPos() {
        return worldPosition;
    }

    @Override
    public int getCurrentMana() {
        var net = getManaPipeNet();
        if (net == null) return 0;
        long mana = 0;
        for (var path : net.getNetData(longPos, worldPosition)) {
            if (this.autoTransfer && path.getTargetPipe() == this && this.blockedSide != path.getTargetFacing()) continue;
            var handler = path.getHandler(level);
            if (handler == null) continue;
            mana += handler.getCurrentMana();
        }
        return MathUtil.saturatedCast(mana);
    }

    @Override
    public boolean isFull() {
        var net = getManaPipeNet();
        if (net == null) return true;
        for (var path : net.getNetData(longPos, worldPosition)) {
            if (this.autoTransfer && path.getTargetPipe() == this && this.blockedSide != path.getTargetFacing()) continue;
            var handler = path.getHandler(level);
            if (handler == null) continue;
            if (!handler.isFull()) return false;
        }
        return true;
    }

    @Override
    public void receiveMana(int mana) {
        if (mana > 0) {
            var net = getManaPipeNet();
            if (net == null) return;
            for (var path : net.getNetData(longPos, worldPosition)) {
                if (this.autoTransfer && path.getTargetPipe() == this && this.blockedSide != path.getTargetFacing()) continue;
                var handler = path.getHandler(level);
                if (handler == null || handler.isFull() || !handler.canReceiveManaFromBursts()) continue;
                var canReceive = Math.min(mana, getMaxMana(handler) - handler.getCurrentMana());
                if (canReceive > 0) {
                    handler.receiveMana(canReceive);
                    mana -= canReceive;
                    if (mana <= 0) break;
                }
            }
        }
    }

    @Override
    public boolean canReceiveManaFromBursts() {
        var net = getManaPipeNet();
        if (net == null) return false;
        for (var path : net.getNetData(longPos, worldPosition)) {
            if (this.autoTransfer && path.getTargetPipe() == this && this.blockedSide != path.getTargetFacing()) continue;
            var handler = path.getHandler(level);
            if (handler == null) continue;
            if (handler.canReceiveManaFromBursts()) return true;
        }
        return false;
    }

    @Override
    public int getMaxMana() {
        var net = getManaPipeNet();
        if (net == null) return 0;
        long mana = 0;
        for (var path : net.getNetData(longPos, worldPosition)) {
            if (this.autoTransfer && path.getTargetPipe() == this && this.blockedSide != path.getTargetFacing()) continue;
            mana += getMaxMana(path.getHandler(level));
        }
        return MathUtil.saturatedCast(mana);
    }

    private static int getMaxMana(@Nullable ManaReceiver receiver) {
        if (receiver instanceof ManaCollector collector) return collector.getMaxMana();
        if (receiver instanceof ManaPool pool) return pool.getMaxMana();
        if (receiver instanceof SparkAttachable attachable) return attachable.getAvailableSpaceForMana();
        return receiver == null || receiver.isFull() ? 0 : 1000;
    }
}
