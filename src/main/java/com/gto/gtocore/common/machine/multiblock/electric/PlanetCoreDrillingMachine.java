package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.api.machine.multiblock.ElectricMultiblockMachine;
import com.gto.gtocore.api.machine.trait.CustomRecipeLogic;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.common.data.GTOOres;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;

public final class PlanetCoreDrillingMachine extends ElectricMultiblockMachine {

    private Set<Material> materials;

    public PlanetCoreDrillingMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Nullable
    private GTRecipe getRecipe() {
        if (hasProxies()) {
            GTORecipeBuilder builder = GTORecipeBuilder.ofRaw().duration(20).EUt(GTValues.VA[GTValues.MAX]);
            for (Material material : getMaterials()) {
                builder.outputItems(TagPrefix.ore, material, 1024);
            }
            GTRecipe recipe = builder.buildRawRecipe();
            if (recipe.matchRecipeContents(IO.IN, this, recipe.tickInputs, true).isSuccess() && recipe.matchRecipeContents(IO.OUT, this, recipe.outputs, false).isSuccess()) return recipe;
        }
        return null;
    }

    private Set<Material> getMaterials() {
        if (materials == null) materials = GTOOres.ALL_ORES.get(Objects.requireNonNull(getLevel()).dimension()).keySet();
        return materials;
    }

    @Override
    protected @NotNull RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }

    @Override
    public @NotNull CustomRecipeLogic getRecipeLogic() {
        return (CustomRecipeLogic) super.getRecipeLogic();
    }
}
