package com.gto.gtocore.common.machine.multiblock.electric.voidseries;

import com.gto.gtocore.api.data.GTODimensions;
import com.gto.gtocore.api.data.chemical.GTOChemicalHelper;
import com.gto.gtocore.api.machine.multiblock.StorageMultiblockMachine;
import com.gto.gtocore.api.machine.trait.CustomRecipeLogic;
import com.gto.gtocore.api.recipe.FastSizedIngredient;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.api.recipe.RecipeRunnerHelper;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOOres;
import com.gto.gtocore.common.data.GTORecipeModifiers;
import com.gto.gtocore.common.item.DimensionDataItem;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gregtechceu.gtceu.common.data.GTMaterials.DrillingFluid;
import static net.minecraft.network.chat.Component.translatable;

public final class VoidMinerMachine extends StorageMultiblockMachine {

    private static final GTRecipe RECIPE = GTORecipeBuilder.ofRaw()
            .inputFluids(DrillingFluid.getFluid(1000))
            .EUt(VA[GTValues.EV])
            .duration(20)
            .buildRawRecipe();

    private ResourceLocation dim;

    public VoidMinerMachine(IMachineBlockEntity holder) {
        super(holder, 1, i -> i.is(GTOItems.DIMENSION_DATA.get()) && i.hasTag());
    }

    @Override
    protected void onMachineChanged() {
        dim = null;
        if (isEmpty()) return;
        dim = DimensionDataItem.getDimension(getStorageStack());
    }

    private GTRecipe getRecipe() {
        if (dim == null) return null;
        if (!isEmpty()) {
            if (RecipeRunnerHelper.matchRecipeInput(this, RECIPE)) {
                GTRecipe recipe = RECIPE.copy();
                recipe.tickInputs.put(EURecipeCapability.CAP, List.of(new Content((long) GTValues.VA[getTier()], ChanceLogic.getMaxChancedValue(), ChanceLogic.getMaxChancedValue(), 0)));
                recipe.outputs.put(ItemRecipeCapability.CAP, List.of(content(), content(), content(), content()));
                return GTORecipeModifiers.accurateParallel(this, recipe, 64);
            }
        }
        return null;
    }

    private Content content() {
        return new Content(FastSizedIngredient.create(new ItemStack(GTOChemicalHelper.getItem(TagPrefix.rawOre, GTOOres.selectMaterial(dim)), (int) Math.pow(getTier() - 3, Math.random() + 1))), ChanceLogic.getMaxChancedValue(), ChanceLogic.getMaxChancedValue(), 0);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        onMachineChanged();
    }

    @Override
    public void addDisplayText(@NotNull List<Component> textList) {
        super.addDisplayText(textList);
        if (dim != null && isFormed() && !getStorageStack().isEmpty()) {
            textList.add(translatable("gtceu.multiblock.ore_rig.drilled_ores_list"));
            GTOOres.ALL_ORES.get(GTODimensions.getDimensionKey(dim)).forEach((mat, i) -> textList.add(mat.getLocalizedName().append("x" + i)));
        }
    }

    @Override
    protected @NotNull RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }
}
