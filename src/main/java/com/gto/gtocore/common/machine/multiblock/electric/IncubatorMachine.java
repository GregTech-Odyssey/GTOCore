package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.common.machine.multiblock.part.RadiationHatchPartMachine;

import com.gregtechceu.gtceu.api.block.IFilterType;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class IncubatorMachine extends WorkableElectricMultiblockMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            IncubatorMachine.class, WorkableMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Persisted
    private int recipeRadioactivity = 0;

    private int cleanroomTier = 1;

    private Set<RadiationHatchPartMachine> radiationHatchPartMachines;

    public IncubatorMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
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
        for (IMultiPart part : getParts()) {
            if (part instanceof RadiationHatchPartMachine radiationHatchPartMachine) {
                radiationHatchPartMachines = Objects.requireNonNullElseGet(radiationHatchPartMachines, HashSet::new);
                radiationHatchPartMachines.add(radiationHatchPartMachine);
            }
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        cleanroomTier = 1;
        radiationHatchPartMachines = null;
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
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
        boolean value = super.onWorking();
        if (recipeRadioactivity != 0 && getOffsetTimer() % 10 == 0) {
            if (outside()) {
                return false;
            }
        }
        return value;
    }

    @Override
    public void afterWorking() {
        recipeRadioactivity = 0;
        super.afterWorking();
    }

    private int getRecipeRadioactivity() {
        int radioactivity = 0;
        if (radiationHatchPartMachines != null)
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
