package com.gto.gtocore.common.machine.multiblock.electric.bioengineering;

import com.gto.gtocore.api.machine.multiblock.TierCasingMultiblockMachine;
import com.gto.gtocore.common.machine.multiblock.part.RadiationHatchPartMachine;

import com.gregtechceu.gtceu.api.block.IFilterType;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.gto.gtocore.api.GTOValues.GLASS_TIER;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class IncubatorMachine extends TierCasingMultiblockMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            IncubatorMachine.class, WorkableMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Persisted
    private int recipeRadioactivity;

    private int cleanroomTier = 1;

    private final Set<RadiationHatchPartMachine> radiationHatchPartMachines = new ObjectOpenHashSet<>(5);

    public IncubatorMachine(IMachineBlockEntity holder) {
        super(holder, GLASS_TIER);
    }

    @Override
    public void onPartScan(IMultiPart part) {
        super.onPartScan(part);
        if (part instanceof RadiationHatchPartMachine radiationHatchPartMachine) {
            radiationHatchPartMachines.add(radiationHatchPartMachine);
        }
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
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
        radiationHatchPartMachines.clear();
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("tooltip.avaritia.tier", cleanroomTier));
        textList.add(Component.translatable("gtocore.recipe.radioactivity", getRecipeRadioactivity()));
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        if (recipe == null) return false;
        if (recipe.data.contains("filter_casing") && recipe.data.getInt("filter_casing") > cleanroomTier) {
            return false;
        }
        if (recipe.data.contains("radioactivity")) {
            recipeRadioactivity = recipe.data.getInt("radioactivity");
            if (outside()) {
                return false;
            }
        }
        return super.beforeWorking(recipe);
    }

    @Override
    public boolean onWorking() {
        if (!super.onWorking()) return false;
        if (recipeRadioactivity != 0 && getOffsetTimer() % 10 == 0) {
            return !outside();
        }
        return true;
    }

    @Override
    public void afterWorking() {
        recipeRadioactivity = 0;
        super.afterWorking();
    }

    private int getRecipeRadioactivity() {
        int radioactivity = 0;
        for (RadiationHatchPartMachine partMachine : radiationHatchPartMachines) {
            radioactivity += partMachine.getRadioactivity();
        }
        return radioactivity;
    }

    private boolean outside() {
        int radioactivity = getRecipeRadioactivity();
        return radioactivity > recipeRadioactivity + 5 || radioactivity < recipeRadioactivity - 5;
    }
}
