package com.gto.gtocore.common.machine.multiblock.electric.viod;

import com.gto.gtocore.api.data.GTODimensions;
import com.gto.gtocore.api.machine.multiblock.StorageMultiblockMachine;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOOres;
import com.gto.gtocore.common.data.GTORecipeModifiers;
import com.gto.gtocore.common.item.DimensionDataItem;
import com.gto.gtocore.common.machine.multiblock.noenergy.PrimitiveOreMachine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;

import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import static net.minecraft.network.chat.Component.translatable;

public final class VoidMinerMachine extends StorageMultiblockMachine {

    private Map<Material, Integer> materialIntegerMap;

    public VoidMinerMachine(IMachineBlockEntity holder) {
        super(holder, 1, i -> i.is(GTOItems.DIMENSION_DATA.get()) && i.hasTag());
    }

    @Override
    protected void onMachineChanged() {
        materialIntegerMap = GTOOres.ALL_ORES.get(GTODimensions.getDimensionKey(DimensionDataItem.getDimension(getStorageStack())));
    }

    @Override
    protected GTRecipe getRealRecipe(@NotNull GTRecipe recipe) {
        if (getStorageStack().isEmpty()) {
            recipe.tickInputs.put(EURecipeCapability.CAP, List.of(new Content((long) GTValues.VA[getTier()], ChanceLogic.getMaxChancedValue(), ChanceLogic.getMaxChancedValue(), 0, null, null)));
            recipe.outputs.put(ItemRecipeCapability.CAP, List.of(new Content(SizedIngredient.create(ChemicalHelper.get(TagPrefix.rawOre, PrimitiveOreMachine.selectMaterial(materialIntegerMap)).copyWithCount((int) Math.pow(getTier() - 3, Math.random() + 1))), ChanceLogic.getMaxChancedValue(), ChanceLogic.getMaxChancedValue(), 0, null, null)));
            return GTORecipeModifiers.accurateParallel(this, recipe, 64);
        }
        return recipe;
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        onMachineChanged();
    }

    @Override
    public void addDisplayText(@NotNull List<Component> textList) {
        super.addDisplayText(textList);
        if (materialIntegerMap != null && isFormed() && !getStorageStack().isEmpty()) {
            textList.add(translatable("gtceu.multiblock.ore_rig.drilled_ores_list"));
            materialIntegerMap.forEach((mat, i) -> textList.add(mat.getLocalizedName().append("x" + i)));
        }
    }
}
