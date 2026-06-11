package com.gtocore.common.blockentity;

import com.gtocore.common.pipe.mana.*;

import com.gregtechceu.gtceu.api.blockentity.PipeBlockEntity;
import com.gregtechceu.gtceu.utils.GTUtil;
import com.gregtechceu.gtceu.utils.LazyOptionalUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
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

import java.lang.ref.WeakReference;

public final class ManaPipeBlockEntity extends PipeBlockEntity<ManaPipeType, ManaPipeProperties> implements ManaCollector {

    private WeakReference<ManaPipeNet> currentPipeNet = new WeakReference<>(null);

    public ManaPipeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public void setConnection(@NotNull Direction side, boolean connected, boolean fromNeighbor) {
        if (!getLevel().isClientSide && connected && !fromNeighbor) {
            if (getNumConnections() >= 2) return;
            BlockEntity tile = getLevel().getBlockEntity(getPipePos().relative(side));
            if (tile instanceof PipeBlockEntity<?, ?> pipeTile && pipeTile.getPipeType().getClass() == this.getPipeType().getClass()) {
                if (pipeTile.getNumConnections() >= 2) return;
            }
        }
        super.setConnection(side, connected, fromNeighbor);
    }

    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == BotaniaForgeCapabilities.MANA_RECEIVER) {
            if ((side == null || isConnected(side)) && !level.isClientSide && getManaPipeNet() != null) {
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
    public void onNeighborChanged() {
        super.onNeighborChanged();
        updateTransferTick(blockedSide != null && isBlocked(blockedSide), this::autoTransfer);
    }

    private void autoTransfer() {
        if (getManaReceiver() == null) return;
        boolean hasHandler = false;
        autoTransfer = true;
        for (var facing : GTUtil.DIRECTIONS) {
            if (facing != blockedSide && isConnected(facing)) {
                var be = getNeighborBlockEntity(facing);
                if (be == null || be instanceof PipeBlockEntity<?, ?>) continue;
                if (LazyOptionalUtil.get(be.getCapability(BotaniaForgeCapabilities.MANA_RECEIVER, facing.getOpposite())) instanceof ManaPool pool) {
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

    @Nullable
    private ManaReceiver getManaReceiver() {
        var net = getManaPipeNet();
        if (net == null || remove) return null;
        var inv = net.getNetData(longPos, worldPosition);
        if (inv == null) return null;
        return inv.getHandler(level);
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
        var handler = getManaReceiver();
        if (handler == null) return 0;
        return handler.getCurrentMana();
    }

    @Override
    public boolean isFull() {
        var handler = getManaReceiver();
        if (handler == null) return true;
        return handler.isFull();
    }

    @Override
    public void receiveMana(int mana) {
        var handler = getManaReceiver();
        if (handler == null) return;
        handler.receiveMana(mana);
    }

    @Override
    public boolean canReceiveManaFromBursts() {
        var handler = getManaReceiver();
        if (handler == null) return false;
        return handler.canReceiveManaFromBursts();
    }

    @Override
    public int getMaxMana() {
        return getMaxMana(getManaReceiver());
    }

    private static int getMaxMana(@Nullable ManaReceiver receiver) {
        if (receiver instanceof ManaCollector collector) return collector.getMaxMana();
        if (receiver instanceof ManaPool pool) return pool.getMaxMana();
        return receiver == null ? 0 : 1000000;
    }
}
