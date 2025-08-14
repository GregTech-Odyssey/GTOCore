package com.gtocore.common.machine.mana.part;

import com.gtolib.api.machine.mana.feature.IManaMachine;
import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import com.hollingsworth.arsnouveau.api.util.SourceUtil;
import com.hollingsworth.arsnouveau.common.block.tile.SourceJarTile;
import vazkii.botania.api.mana.ManaPool;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.xplat.XplatAbstractions;

public final class ManaExtractHatchPartMachine extends ManaHatchPartMachine {

    public ManaExtractHatchPartMachine(MetaMachineBlockEntity holder, int tier) {
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
        if (getOffsetTimer() % 20 != 0) return;
        BlockPos frontPos = getPos().relative(getFrontFacing());
        Level level = getLevel();
        if (!isFull()) {
            ManaReceiver receiver = XplatAbstractions.INSTANCE.findManaReceiver(level, frontPos, null);
            if (receiver instanceof ManaPool || receiver instanceof IManaMachine) {
                int change = MathUtil.saturatedCast(getManaContainer().addMana(receiver.getCurrentMana(), 20, false));
                if (change <= 0) return;
                receiver.receiveMana(-change);
            }

            if (level != null && level.getBlockEntity(frontPos) instanceof SourceJarTile jarTile) {
                int sourceAmount = jarTile.getSource();
                int change = Math.toIntExact(getManaContainer().addMana(sourceAmount / 4, 20, false) * 4);
                if (change <= 0) return;
                SourceUtil.takeSource(frontPos, level, 0, change);
            }
        }
    }
}
