package com.gtocore.common.machine.multiblock.part;

import com.gtolib.GTOCore;
import com.gtolib.api.machine.feature.multiblock.IParallelMachine;
import com.gtolib.api.machine.part.AmountConfigurationHatchPartMachine;
import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IParallelHatch;

import it.unimi.dsi.fastutil.ints.Int2LongFunction;

public final class ParallelHatchPartMachine extends AmountConfigurationHatchPartMachine implements IParallelHatch {

    public static final Int2LongFunction PARALLEL_FUNCTION = tier -> 1L << ((tier - 3 + (GTOCore.isEasy() ? 1 : 0)) << 1);

    public ParallelHatchPartMachine(MetaMachineBlockEntity holder, int tier) {
        super(holder, tier < 0 ? GTValues.MAX : tier, 1, tier < 0 ? IParallelMachine.MAX_PARALLEL : PARALLEL_FUNCTION.apply(tier));
    }

    @Override
    public int getCurrentParallel() {
        return MathUtil.saturatedCast(getCurrent());
    }

    public long getCurrentParallelLong() {
        return getCurrent();
    }
}
