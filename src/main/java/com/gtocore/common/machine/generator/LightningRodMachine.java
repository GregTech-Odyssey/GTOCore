package com.gtocore.common.machine.generator;

import com.gtolib.api.annotation.Scanned;
import com.gtolib.api.annotation.dynamic.DynamicInitialValue;
import com.gtolib.api.annotation.dynamic.DynamicInitialValueTypes;
import com.gtolib.utils.GTOUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.TieredEnergyMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import com.mojang.blaze3d.MethodsReturnNonnullByDefault;

import java.util.Objects;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@Scanned
public final class LightningRodMachine extends TieredEnergyMachine {

    private TickableSubscription energySubs;
    @DynamicInitialValue(typeKey = DynamicInitialValueTypes.KEY_PROBABILITY, key = "lightning_rod.break_probability", easyValue = "0.2D", normalValue = "0.3D", expertValue = "0.4D", cn = "雷击杆破坏概率", cnComment = "雷击杆被雷击后，被破坏的概率为%s。", en = "Lightning Rod Break Probability", enComment = "Probability of the lightning rod being destroyed after being struck by lightning: %s.")
    private static double breakProbability = 1.0;

    public LightningRodMachine(MetaMachineBlockEntity holder, int tier, Object... args) {
        super(holder, tier, args);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!isRemote()) {
            energySubs = subscribeServerTick(energySubs, this::checkEnergy);
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (energySubs != null) {
            energySubs.unsubscribe();
            energySubs = null;
        }
    }

    private void checkEnergy() {
        if (getOffsetTimer() % 10 == 0) {
            BlockState state = Objects.requireNonNull(getLevel()).getBlockState(getPos().offset(0, 1, 0));
            if (state.getBlock() == Blocks.LIGHTNING_ROD && state.getValue(BlockStateProperties.FACING) == Direction.UP && state.getValue(LightningRodBlock.POWERED)) {
                if (energyContainer.getEnergyStored() == getCharge()) {
                    doExplosion(getTier());
                } else {
                    energyContainer.addEnergy(getCharge() / 2 + GTValues.RNG.nextInt());
                }
                if (GTOUtils.probability(breakProbability)) {
                    getLevel().setBlockAndUpdate(getPos().offset(0, 1, 0), Blocks.AIR.defaultBlockState());
                }
            }
        }
    }

    @Override
    protected boolean isEnergyEmitter() {
        return true;
    }

    @Override
    protected long getMaxInputOutputAmperage() {
        return 256L;
    }

    private long getCharge() {
        return 24414 * (1L << (2 * getTier()));
    }

    @Override
    protected NotifiableEnergyContainer createEnergyContainer(Object... args) {
        NotifiableEnergyContainer energyContainer = NotifiableEnergyContainer.emitterContainer(this, getCharge(), GTValues.V[getTier() - 1], getMaxInputOutputAmperage());
        energyContainer.setSideOutputCondition(side -> !hasFrontFacing() || side == getFrontFacing());
        return energyContainer;
    }

    @Override
    public int tintColor(int index) {
        if (index == 2) {
            return GTValues.VC[getTier()];
        }
        return super.tintColor(index);
    }

    public static double getBreakProbability() {
        return LightningRodMachine.breakProbability;
    }
}
