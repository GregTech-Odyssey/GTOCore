package com.gto.gtocore.api.machine.multiblock;

import com.gto.gtocore.api.machine.feature.multiblock.ITierCasingMachine;
import com.gto.gtocore.api.machine.trait.TierCasingTrait;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import net.minecraft.MethodsReturnNonnullByDefault;

import it.unimi.dsi.fastutil.objects.Object2IntMap;

import java.util.function.Function;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TierCasingMultiblockMachine extends ElectricMultiblockMachine implements ITierCasingMachine {

    public static Function<IMachineBlockEntity, TierCasingMultiblockMachine> createMachine(String... tierTypes) {
        return holder -> new TierCasingMultiblockMachine(holder, tierTypes);
    }

    private final TierCasingTrait tierCasingTrait;

    protected TierCasingMultiblockMachine(IMachineBlockEntity holder, String... tierTypes) {
        super(holder);
        tierCasingTrait = new TierCasingTrait(this, tierTypes);
    }

    @Override
    public Object2IntMap<String> getCasingTiers() {
        return tierCasingTrait.getCasingTiers();
    }
}
