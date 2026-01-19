package com.gtocore.common.machine.monitor;

import com.gregtechceu.gtceu.api.blockentity.ITickSubscription;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.hepdd.gtmthings.api.capability.IBindable;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BasicMonitor extends MetaMachine implements IBindable, IMachineLife, IMonitor {

    private TickableSubscription tickSubscription;

    BasicMonitor(MetaMachineBlockEntity holder) {
        super(holder);
    }

    public BasicMonitor(Object o) {
        this((MetaMachineBlockEntity) o);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() != null && !getLevel().isClientSide) {
            Manager.addBlock(this);
            tickSubscription = subscribeClientTick(tickSubscription, this::clientTick, 10);
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (getLevel() != null && !getLevel().isClientSide) {
            Manager.removeBlock(this);
        }
        tickSubscription = ITickSubscription.unsubscribe(tickSubscription);
    }

    @OnlyIn(Dist.CLIENT)
    protected void clientTick() {
        Manager.updateAllNetworkDisplayMachines(getLevel());
    }

    @Override
    public @Nullable UUID getUUID() {
        return getOwnerUUID();
    }

    @Override
    public void onPaintingColorChanged(int color) {
        super.onPaintingColorChanged(color);
        if (getLevel() != null && !getLevel().isClientSide) {
            Manager.removeBlock(getBlockState(), getPos(), getLevel());
            Manager.addBlock(getBlockState(), getPos(), getLevel(), color);
        }
    }
}
