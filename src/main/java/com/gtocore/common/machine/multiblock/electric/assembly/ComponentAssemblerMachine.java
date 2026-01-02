package com.gtocore.common.machine.multiblock.electric.assembly;

import com.gtolib.api.machine.multiblock.TierCasingMultiblockMachine;
import com.gtolib.api.recipe.IdleReason;
import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import static com.gtolib.api.GTOValues.COMPONENT_ASSEMBLY_CASING_TIER;

public class ComponentAssemblerMachine extends TierCasingMultiblockMachine {

    private int casingTier;

    public ComponentAssemblerMachine(MetaMachineBlockEntity holder) {
        super(holder, COMPONENT_ASSEMBLY_CASING_TIER);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        if (getSubFormedAmount() > 0) {
            casingTier = Math.min(GTValues.UV, getCasingTier(COMPONENT_ASSEMBLY_CASING_TIER));
        } else {
            casingTier = Math.min(GTValues.IV, getCasingTier(COMPONENT_ASSEMBLY_CASING_TIER));
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        casingTier = 0;
    }

    @Override
    public Recipe getRealRecipe(Recipe recipe) {
        if (recipe.data.getInt(COMPONENT_ASSEMBLY_CASING_TIER) > casingTier) {
            setIdleReason(IdleReason.VOLTAGE_TIER_NOT_SATISFIES);
            return null;
        }
        return super.getRealRecipe(recipe);
    }
}
