package com.gtocore.common.cover;

import com.gtolib.api.capability.IWirelessChargerInteraction;
import com.gtolib.api.machine.impl.WirelessChargerMachine;

import com.gregtechceu.gtceu.api.capability.ICoverable;
import com.gregtechceu.gtceu.api.cover.CoverBehavior;
import com.gregtechceu.gtceu.api.cover.CoverDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandlerModifiable;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class WirelessChargerCover extends CoverBehavior implements IWirelessChargerInteraction {

    private WirelessChargerMachine wirelessChargerMachine;

    private MetaMachine machine;

    private TickableSubscription subscription;

    private IItemHandlerModifiable handlerModifiable;

    public WirelessChargerCover(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide) {
        super(definition, coverHolder, attachedSide);
    }

    @Override
    public boolean canAttach() {
        if (super.canAttach()) {
            machine = MetaMachine.getMachine(coverHolder.holder());
            if (machine != null) {
                for (var direction : Direction.values()) {
                    if (machine.getCoverContainer().getCoverAtSide(direction) instanceof WirelessChargerCover) return false;
                }
                handlerModifiable = machine.getItemHandlerCap(attachedSide, false);
                return handlerModifiable != null;
            }
        }
        return false;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (coverHolder.getLevel() instanceof ServerLevel) {
            subscription = coverHolder.subscribeServerTick(subscription, this::update, 20);
        }
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        unsubscribe();
    }

    private void unsubscribe() {
        if (subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }
    }

    private void update() {
        if (handlerModifiable == null) {
            if (machine == null) {
                machine = MetaMachine.getMachine(coverHolder.holder());
            }
            if (machine == null) {
                unsubscribe();
                return;
            } else {
                handlerModifiable = machine.getItemHandlerCap(attachedSide, false);
                if (handlerModifiable == null) {
                    return;
                }
            }
        }
        var slots = handlerModifiable.getSlots();
        for (int i = 0; i < slots; i++) {
            var stack = handlerModifiable.getStackInSlot(i);
            if (!stack.isEmpty()) {
                IWirelessChargerInteraction.charge(getNetMachine(), stack);
            }
        }
    }

    @Override
    public BlockPos getPos() {
        return coverHolder.getPos();
    }

    @Override
    public @Nullable Level getLevel() {
        return coverHolder.getLevel();
    }

    @Override
    public WirelessChargerMachine getNetMachineCache() {
        return wirelessChargerMachine;
    }

    @Override
    public void setNetMachineCache(WirelessChargerMachine cache) {
        wirelessChargerMachine = cache;
    }

    @Override
    public @Nullable UUID getUUID() {
        return machine == null ? null : machine.getOwnerUUID();
    }
}
