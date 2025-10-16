package com.gtocore.common.machine.multiblock.electric.voidseries;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOOres;
import com.gtocore.common.item.DimensionDataItem;

import com.gtolib.api.machine.multiblock.StorageMultiblockMachine;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.recipe.ContentBuilder;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.api.recipe.RecipeRunner;
import com.gtolib.api.recipe.modifier.ParallelLogic;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gregtechceu.gtceu.common.data.GTMaterials.DrillingFluid;
import static net.minecraft.network.chat.Component.translatable;

public final class VoidMinerMachine extends StorageMultiblockMachine {

    private static final Recipe RECIPE = RecipeBuilder.ofRaw()
            .inputFluids(DrillingFluid.getFluid(1000))
            .EUt(VA[GTValues.EV])
            .duration(20)
            .buildRawRecipe();

    private ResourceLocation dim;

    public VoidMinerMachine(MetaMachineBlockEntity holder) {
        super(holder, 1, i -> i.is(GTOItems.DIMENSION_DATA.get()) && i.hasTag());
    }

    @Override
    public void onMachineChanged() {
        dim = null;
        if (isEmpty()) return;
        dim = DimensionDataItem.getDimension(getStorageStack());
        if (GTOOres.ALL_ORES.containsKey(dim)) {
            getRecipeLogic().updateTickSubscription();
            return;
        }
        dim = null;
    }

    private Recipe getRecipe() {
        if (dim == null) return null;
        if (!isEmpty()) {
            if (RecipeRunner.matchRecipeInput(this, RECIPE)) {
                Recipe recipe = RECIPE.copy();
                recipe.setEut(GTValues.VA[getTier()]);
                recipe.outputs.put(ItemRecipeCapability.CAP, ContentBuilder.createList().items(getItems()).buildList());
                return ParallelLogic.accurateParallel(this, recipe, 64);
            }
        }
        return null;
    }

    private ItemStack[] getItems() {
        ItemStack[] stacks = new ItemStack[4];
        for (int i = 0; i < 4; i++) {
            stacks[i] = new ItemStack(ChemicalHelper.getItem(TagPrefix.rawOre, GTOOres.selectMaterial(dim)), (int) Math.pow(getTier() - 3, GTValues.RNG.nextDouble() + 1));
        }
        return stacks;
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
            GTOOres.ALL_ORES.get(dim).forEach((mat, i) -> textList.add(mat.getLocalizedName().append("x" + i)));
        }
    }

    @Override
    public RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }
}
