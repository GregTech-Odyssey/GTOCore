package com.gtocore.common.machine.trait;

import com.gtocore.common.data.GTORecipeDataKeys;
import com.gtocore.common.machine.multiblock.part.RadiationHatchPartMachine;
import com.gtocore.data.IdleReason;

import com.gtolib.api.machine.feature.multiblock.IMultiblockTraitHolder;
import com.gtolib.api.machine.trait.MultiblockTrait;

import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;

import net.minecraft.network.chat.Component;

import com.gto.datasynclib.annotations.SaveToDisk;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class RadioactivityTrait extends MultiblockTrait {

    @SaveToDisk
    private int recipeRadioactivity;

    private final Set<RadiationHatchPartMachine> radiationHatchPartMachines = new ReferenceOpenHashSet<>();

    public RadioactivityTrait(IMultiblockTraitHolder machine) {
        super(machine);
    }

    @Override
    public void onPartScan(IMultiPart part) {
        if (part instanceof RadiationHatchPartMachine radiationHatchPartMachine) {
            radiationHatchPartMachines.add(radiationHatchPartMachine);
        }
    }

    @Override
    public void onStructureInvalid() {
        radiationHatchPartMachines.clear();
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("gtocore.recipe.radioactivity", getRecipeRadioactivity()));
    }

    @Override
    public GTRecipe modifyRecipe(@NotNull RecipeHandlerUnit unit, @NotNull GTRecipe recipe) {
        recipeRadioactivity = recipe.data.getInt(GTORecipeDataKeys.RADIOACTIVITY);
        if (recipeRadioactivity > 0 && outside()) {
            IdleReason.RADIATION.setReason(machine);
            return null;
        }
        return recipe;
    }

    @Override
    public void afterWorking() {
        recipeRadioactivity = 0;
        super.afterWorking();
    }

    protected int getRecipeRadioactivity() {
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
