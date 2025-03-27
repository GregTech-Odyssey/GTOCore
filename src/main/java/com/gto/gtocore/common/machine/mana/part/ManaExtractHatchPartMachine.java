package com.gto.gtocore.common.machine.mana.part;

import com.gto.gtocore.api.machine.mana.feature.IManaMachine;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.utils.GTMath;

import vazkii.botania.api.mana.ManaPool;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.xplat.XplatAbstractions;

public final class ManaExtractHatchPartMachine extends ManaHatchPartMachine {

    public ManaExtractHatchPartMachine(IMachineBlockEntity holder, int tier) {
        super(holder, tier, IO.IN, 4);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!isRemote()) {
            tickSubs = subscribeServerTick(tickSubs, this::tickUpdate);
        }
    }

    @Override
    void tickUpdate() {
        if (getOffsetTimer() % 20 != 0 || isFull()) return;
        ManaReceiver receiver = XplatAbstractions.INSTANCE.findManaReceiver(getLevel(), getPos().relative(getFrontFacing()), null);
        if (receiver instanceof ManaPool || receiver instanceof IManaMachine) {
            int change = GTMath.saturatedCast(getManaContainer().addMana(receiver.getCurrentMana(), 20));
            if (change <= 0) return;
            receiver.receiveMana(-change);
        }
    }
}
