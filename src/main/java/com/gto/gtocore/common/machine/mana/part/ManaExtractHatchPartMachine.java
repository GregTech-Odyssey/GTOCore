package com.gto.gtocore.common.machine.mana.part;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

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
    protected void tickUpdate() {
        if (getOffsetTimer() % 20 != 0 || isFull()) return;
        ManaReceiver receiver = XplatAbstractions.INSTANCE.findManaReceiver(getLevel(), getPos().relative(getFrontFacing()), null);
        if (receiver instanceof ManaPool pool) {
            int change = getManaContainer().addMana(pool.getCurrentMana(), 20);
            if (change <= 0) return;
            pool.receiveMana(-change);
        }
    }
}
