package com.gtocore.common.machine.multiblock.electric.gcym;

import com.gtocore.common.data.GTORecipeDataKeys;
import com.gtocore.common.machine.multiblock.electric.DistillationTowerMachine;

import com.gtolib.api.machine.feature.multiblock.ITierCasingMachine;
import com.gtolib.api.machine.trait.TierCasingTrait;
import com.gtolib.api.recipe.TierDataKey;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import it.unimi.dsi.fastutil.objects.Reference2IntMap;

public final class largeLDistillationTowerMachine extends DistillationTowerMachine implements ITierCasingMachine {

    private final TierCasingTrait tierCasingTrait;

    public largeLDistillationTowerMachine(MetaMachineBlockEntity holder) {
        super(holder);
        tierCasingTrait = new TierCasingTrait(this, GTORecipeDataKeys.INTEGRAL_FRAMEWORK_TIER);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        tier = Math.min(getCasingTier(GTORecipeDataKeys.INTEGRAL_FRAMEWORK_TIER), tier);
    }

    @Override
    public boolean gtolib$canUpgraded() {
        return true;
    }

    @Override
    public Reference2IntMap<TierDataKey> getCasingTiers() {
        return tierCasingTrait.getCasingTiers();
    }
}
