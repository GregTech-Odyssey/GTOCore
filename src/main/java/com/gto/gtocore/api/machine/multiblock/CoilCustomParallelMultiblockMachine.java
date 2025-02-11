package com.gto.gtocore.api.machine.multiblock;

import com.gto.gtocore.api.machine.feature.multiblock.ICoilMachine;
import com.gto.gtocore.api.machine.trait.CoilTrait;

import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import java.util.function.Function;

public class CoilCustomParallelMultiblockMachine extends CustomParallelMultiblockMachine implements ICoilMachine {

    public static Function<IMachineBlockEntity, CoilCustomParallelMultiblockMachine> createParallelCoil(Function<CoilCustomParallelMultiblockMachine, Integer> parallel, boolean defaultParallel, boolean ebf, boolean check) {
        return holder -> new CoilCustomParallelMultiblockMachine(holder, defaultParallel, ebf, check, parallel);
    }

    private final CoilTrait coilTrait;

    protected CoilCustomParallelMultiblockMachine(IMachineBlockEntity holder, boolean defaultParallel, boolean ebf, boolean check, Function<CoilCustomParallelMultiblockMachine, Integer> parallel) {
        super(holder, defaultParallel, machine -> parallel.apply((CoilCustomParallelMultiblockMachine) machine));
        coilTrait = new CoilTrait(this, ebf, check);
    }

    @Override
    public int gto$getTemperature() {
        return coilTrait.getTemperature();
    }

    @Override
    public int getCoilTier() {
        return coilTrait.getCoilTier();
    }

    @Override
    public ICoilType getCoilType() {
        return coilTrait.getCoilType();
    }
}
