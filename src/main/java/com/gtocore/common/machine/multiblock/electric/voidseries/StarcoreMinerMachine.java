package com.gtocore.common.machine.multiblock.electric.voidseries;

import com.gtocore.common.data.GTOOres;
import com.gtocore.data.IdleReason;

import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.recipe.RecipeBuilder;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.handler.ICustomRecipeLogicHolder;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;

import java.util.Objects;
import java.util.Set;

public final class StarcoreMinerMachine extends ElectricMultiblockMachine implements ICustomRecipeLogicHolder {

    private Set<Material> materials;

    public StarcoreMinerMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    private Set<Material> getMaterials() {
        if (materials == null) {
            var ores = GTOOres.ALL_ORES.get(Objects.requireNonNull(getLevel()).dimension());
            if (ores == null || ores.isEmpty()) return Set.of();
            materials = ores.keySet();
        }
        return materials;
    }

    @Override
    public GTRecipeDefinition createCustomRecipe(RecipeHandlerUnit unit) {
        RecipeBuilder builder = getRecipeBuilder().duration(20).EUt(GTValues.VA[GTValues.MAX]);
        for (Material material : getMaterials()) {
            builder.outputItems(TagPrefix.ore, material, 65536);
        }
        if (builder.getItemOutputs().isEmpty()) {
            setIdleReason(IdleReason.NO_ORES);
            return null;
        }
        return builder.build();
    }
}
