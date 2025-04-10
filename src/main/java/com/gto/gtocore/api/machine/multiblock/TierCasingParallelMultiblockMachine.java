package com.gto.gtocore.api.machine.multiblock;

import com.gto.gtocore.api.machine.feature.multiblock.ITierCasingMachine;
import com.gto.gtocore.api.machine.trait.TierCasingTrait;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import it.unimi.dsi.fastutil.objects.Object2IntMap;

import java.util.function.Function;
import java.util.function.ToIntFunction;

public final class TierCasingParallelMultiblockMachine extends CustomParallelMultiblockMachine implements ITierCasingMachine {

    public static Function<IMachineBlockEntity, TierCasingParallelMultiblockMachine> createParallel(ToIntFunction<TierCasingParallelMultiblockMachine> parallel, boolean defaultParallel, String... tierTypes) {
        return holder -> new TierCasingParallelMultiblockMachine(holder, defaultParallel, parallel, tierTypes);
    }

    private final TierCasingTrait tierCasingTrait;

    private TierCasingParallelMultiblockMachine(IMachineBlockEntity holder, boolean defaultParallel, ToIntFunction<TierCasingParallelMultiblockMachine> parallel, String... tierTypes) {
        super(holder, defaultParallel, machine -> parallel.applyAsInt((TierCasingParallelMultiblockMachine) machine));
        tierCasingTrait = new TierCasingTrait(this, tierTypes);
    }

    @Override
    public Object2IntMap<String> getCasingTiers() {
        return tierCasingTrait.getCasingTiers();
    }
}
