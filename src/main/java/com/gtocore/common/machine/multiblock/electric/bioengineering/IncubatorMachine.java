package com.gtocore.common.machine.multiblock.electric.bioengineering;

import com.gtocore.common.machine.trait.RadioactivityTrait;

import com.gtolib.api.machine.multiblock.TierCasingMultiblockMachine;
import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.block.IFilterType;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import org.jetbrains.annotations.Nullable;

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
        super(holder, GLASS_TIER);
        radioactivityTrait = new RadioactivityTrait(this);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        tier = Math.min(getCasingTier(GLASS_TIER), tier);
        IFilterType filterType = getMultiblockState().getMatchContext().get("FilterType");
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
        textList.add(Component.translatable("tooltip.avaritia.tier", cleanroomTier));
    }

    @Override
    protected boolean beforeWorking(@Nullable Recipe recipe) {
        if (recipe == null) return false;
        if (recipe.data.contains("filter_casing") && recipe.data.getInt("filter_casing") > cleanroomTier) {
            return false;
        }
        return super.beforeWorking(recipe);
    }
}
