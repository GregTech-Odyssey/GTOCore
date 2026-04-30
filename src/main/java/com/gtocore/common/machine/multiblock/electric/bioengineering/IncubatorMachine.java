package com.gtocore.common.machine.multiblock.electric.bioengineering;

import com.gtocore.common.data.GTORecipeDataKeys;
import com.gtocore.common.machine.trait.RadioactivityTrait;

import com.gtolib.api.machine.multiblock.TierCasingMultiblockMachine;
import com.gtolib.api.recipe.IdleReason;
import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.block.IFilterType;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.pattern.Predicates;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.gtolib.api.GTOValues.GLASS_TIER;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class IncubatorMachine extends TierCasingMultiblockMachine {

    @Persisted
    private final RadioactivityTrait radioactivityTrait;

    private int cleanroomTier = 1;

    public IncubatorMachine(MetaMachineBlockEntity holder) {
        super(holder, GTORecipeDataKeys.GLASS_TIER);
        radioactivityTrait = new RadioactivityTrait(this);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        tier = Math.min(getCasingTier(GTORecipeDataKeys.GLASS_TIER), tier);
        IFilterType filterType = getMultiblockState().getMatchContext().get(Predicates.DataKey.FILTER_TYPE);
        if (filterType != null) {
            switch (filterType.getCleanroomType().getName()) {
                case "cleanroom":
                    cleanroomTier = 1;
                    break;
                case "sterile_cleanroom":
                    cleanroomTier = 2;
                    break;
                case "law_cleanroom":
                    cleanroomTier = 3;
            }
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        cleanroomTier = 1;
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("ars_nouveau.tier", cleanroomTier));
    }

    @Override
    protected boolean beforeWorking(Recipe recipe) {
        if (recipe.data.contains(GTORecipeDataKeys.FILTER_CASING) && recipe.data.getInt(GTORecipeDataKeys.FILTER_CASING) > cleanroomTier) {
            setIdleReason(IdleReason.BLOCK_TIER_NOT_SATISFIES);
            return false;
        }
        return super.beforeWorking(recipe);
    }
}
