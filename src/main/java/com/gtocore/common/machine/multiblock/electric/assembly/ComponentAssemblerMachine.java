package com.gtocore.common.machine.multiblock.electric.assembly;

import com.gtolib.api.machine.feature.multiblock.ITierCasingMachine;
import com.gtolib.api.machine.multiblock.CrossRecipeMultiblockMachine;
import com.gtolib.api.machine.trait.TierCasingTrait;
import com.gtolib.api.recipe.IdleReason;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import it.unimi.dsi.fastutil.objects.Object2IntMap;

import static com.gtolib.api.GTOValues.COMPONENT_ASSEMBLY_CASING_TIER;

public final class ComponentAssemblerMachine extends CrossRecipeMultiblockMachine implements ITierCasingMachine {

    private int maxCasingTier = GTValues.IV;
    private final TierCasingTrait tierCasingTrait;

    public ComponentAssemblerMachine(MetaMachineBlockEntity holder) {
        super(holder, false, true, MachineUtils::getHatchParallel);
        this.tierCasingTrait = new TierCasingTrait(this, COMPONENT_ASSEMBLY_CASING_TIER);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        if (getSubFormedAmount() > 0) maxCasingTier = GTValues.UV;
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        maxCasingTier = GTValues.IV;
    }

    @Override
    public Recipe getRealRecipe(Recipe recipe) {
        if (recipe.data.getInt(COMPONENT_ASSEMBLY_CASING_TIER) > maxCasingTier) {
            setIdleReason(IdleReason.VOLTAGE_TIER_NOT_SATISFIES);
            return null;
        }
        return super.getRealRecipe(recipe);
    }

    @Override
    public Object2IntMap<String> getCasingTiers() {
        return tierCasingTrait.getCasingTiers();
    }
}
