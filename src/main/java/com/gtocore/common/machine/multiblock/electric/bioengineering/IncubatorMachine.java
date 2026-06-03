package com.gtocore.common.machine.multiblock.electric.bioengineering;

import com.gtocore.common.data.GTORecipeDataKeys;
import com.gtocore.common.machine.trait.RadioactivityTrait;

import com.gtolib.api.machine.multiblock.TierCasingMultiblockMachine;
import com.gtolib.api.recipe.IdleReason;

import com.gregtechceu.gtceu.api.block.IFilterType;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;

import com.gto.datasynclib.annotations.SaveToDisk;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class IncubatorMachine extends TierCasingMultiblockMachine {

    @SaveToDisk
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
    public boolean checkConditions(RecipeHandlerUnit unit, GTRecipeDefinition recipe) {
        if (recipe.data.contains(GTORecipeDataKeys.FILTER_CASING) && recipe.data.getInt(GTORecipeDataKeys.FILTER_CASING) > cleanroomTier) {
            setIdleReason(IdleReason.BLOCK_TIER_NOT_SATISFIES);
            return false;
        }
        return super.checkConditions(unit, recipe);
    }
}
