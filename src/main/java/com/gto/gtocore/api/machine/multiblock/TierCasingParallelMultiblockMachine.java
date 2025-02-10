package com.gto.gtocore.api.machine.multiblock;

import com.gto.gtocore.api.machine.feature.ITierCasingMachine;
import com.gto.gtocore.api.machine.trait.TierCasingTrait;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import java.util.Map;
import java.util.function.Function;

public class TierCasingParallelMultiblockMachine extends CustomParallelMultiblockMachine implements ITierCasingMachine {

    public static Function<IMachineBlockEntity, TierCasingParallelMultiblockMachine> createParallel(Function<TierCasingParallelMultiblockMachine, Integer> parallel, boolean defaultParallel, String... tierTypes) {
        return holder -> new TierCasingParallelMultiblockMachine(holder, defaultParallel, parallel, tierTypes);
    }

    private final TierCasingTrait tierCasingTrait;

    private TierCasingParallelMultiblockMachine(IMachineBlockEntity holder, boolean defaultParallel, Function<TierCasingParallelMultiblockMachine, Integer> parallel, String... tierTypes) {
        super(holder, defaultParallel, machine -> parallel.apply((TierCasingParallelMultiblockMachine) machine));
        tierCasingTrait = new TierCasingTrait(this, tierTypes);
    }

    @Override
    public Map<String, Integer> getCasingTiers() {
        return tierCasingTrait.getCasingTiers();
    }
}
