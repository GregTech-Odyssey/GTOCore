package com.gtocore.data.recipe.builder.research;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import static com.gregtechceu.gtceu.api.GTValues.LuV;
import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gtocore.common.data.GTORecipeTypes.CRYSTAL_SCAN_RECIPES;
import static com.gtocore.common.data.GTORecipeTypes.SCANNER_RECIPES;
import static com.gtocore.data.recipe.builder.research.ExResearchManager.*;

public final class DataCrystalConstruction {

    // 晶片扫描
    public static DataCrystalConstruction buildDataCrystal(Boolean recipe) {
        return new DataCrystalConstruction(recipe);
    }

    private ItemStack itemStack;
    private FluidStack fluidStack;
    private int dataTier;
    private ItemStack dataItem;
    private int dataCrystal;

    private final Boolean recipe;
    private ItemStack catalyst;
    private int duration = 0;
    private long eut = VA[LuV];
    private int cwut = 0;
    private int totalCWU = 0;

    private DataCrystalConstruction(Boolean recipe) {
        this.recipe = recipe;
    }

    public DataCrystalConstruction input(ItemStack itemStack, int dataTier, int dataCrystal) {
        if (fluidStack != null) fluidStack = null;
        this.itemStack = itemStack;
        this.dataTier = dataTier;
        this.dataCrystal = dataCrystal;
        this.dataItem = getDataCrystalItem(dataCrystal);
        return this;
    }

    public DataCrystalConstruction input(FluidStack fluidStack, int dataTier, int dataCrystal) {
        if (itemStack != null) itemStack = null;
        int amount = (fluidStack.getAmount() + 999) / 1000;
        fluidStack.setAmount(amount * 1000);
        this.fluidStack = fluidStack;
        this.dataTier = dataTier;
        this.dataCrystal = dataCrystal;
        this.dataItem = getDataCrystalItem(dataCrystal);
        return this;
    }

    public DataCrystalConstruction catalyst(ItemStack catalyst) {
        this.catalyst = catalyst;
        return this;
    }

    public DataCrystalConstruction duration(int duration) {
        this.duration = duration;
        return this;
    }

    public DataCrystalConstruction EUt(long eut) {
        this.eut = eut;
        return this;
    }

    public DataCrystalConstruction CWUt(int cwut) {
        this.cwut = cwut;
        this.totalCWU = cwut * 800;
        return this;
    }

    public DataCrystalConstruction CWUt(int cwut, int totalCWU) {
        this.cwut = cwut;
        this.totalCWU = totalCWU;
        return this;
    }

    public void save() {
        if (dataItem == null) throw new IllegalStateException("Missing data crystal");
        if (dataCrystal < 1 || dataCrystal > 5) throw new IllegalStateException("DataCrystal Out of index");

        var dataStack = dataItem;
        String recipeId;
        if (itemStack != null) {
            ExResearchManager.writeScanningResearchToNBT(dataStack.getOrCreateTag(), itemStack, dataTier, dataCrystal);
            recipeId = itemStackToString(itemStack);
        } else if (fluidStack != null) {
            ExResearchManager.writeScanningResearchToNBT(dataStack.getOrCreateTag(), fluidStack, dataTier, dataCrystal);
            recipeId = fluidStackToString(fluidStack);
        } else {
            throw new IllegalStateException("The scanned item or fluid is missing");
        }

        if (recipe == null) return;
        if (!recipe) {
            if (itemStack == null) throw new IllegalStateException("The scanned recipe can only use item");
            SCANNER_RECIPES.recipeBuilder(recipeId)
                    .inputItems(EmptyDataCrystalList.get(dataCrystal))
                    .inputItems(itemStack)
                    .outputItems(dataStack)
                    .duration(duration)
                    .EUt(eut)
                    .save();
        } else {
            if (cwut > totalCWU) throw new IllegalStateException("Total CWU cannot be greater than CWU/t!");
            if (catalyst == null) throw new IllegalStateException("Catalyst input required");
            var builder = CRYSTAL_SCAN_RECIPES.recipeBuilder(recipeId)
                    .notConsumable(catalyst)
                    .inputItems(EmptyDataCrystalList.get(dataCrystal));
            if (itemStack != null) builder.inputItems(itemStack);
            else builder.inputFluids(fluidStack);
            builder.outputItems(dataStack)
                    .EUt(eut)
                    .CWUt(cwut)
                    .totalCWU(totalCWU)
                    .save();
        }
    }
}
