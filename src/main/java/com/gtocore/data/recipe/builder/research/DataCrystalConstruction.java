package com.gtocore.data.recipe.builder.research;

import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

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
    private Item dataItem;
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
        this.itemStack = itemStack;
        this.dataTier = dataTier;
        this.dataCrystal = dataCrystal;
        this.dataItem = DataCrystalMap.get(dataCrystal);
        return this;
    }

    public DataCrystalConstruction input(FluidStack fluidStack, int dataTier, int dataCrystal) {
        int amount = fluidStack.getAmount() / 1000 + (fluidStack.getAmount() % 1000 > 1 ? 1 : 0);
        fluidStack.setAmount(amount * 1000);
        this.fluidStack = fluidStack;
        this.dataTier = dataTier;
        this.dataCrystal = dataCrystal;
        this.dataItem = DataCrystalMap.get(dataCrystal);
        ItemStack cell = GTItems.FLUID_CELL.asStack(amount);
        CompoundTag fluidTag = cell.getOrCreateTag();
        CompoundTag fluid = new CompoundTag();
        fluid.putString("FluidName", ForgeRegistries.FLUIDS.getKey(fluidStack.getFluid()).toString());
        fluid.putInt("Amount", 1000);
        fluidTag.put("Fluid", fluid);
        cell.setTag(fluidTag);
        this.itemStack = cell;
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
        this.totalCWU = cwut * 4000;
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

        var dataStack = dataItem.getDefaultInstance();
        if (fluidStack != null) {
            ExResearchManager.writeScanningResearchToNBT(dataStack.getOrCreateTag(), fluidStack, dataTier, dataCrystal);
        } else if (itemStack != null) {
            ExResearchManager.writeScanningResearchToNBT(dataStack.getOrCreateTag(), itemStack, dataTier, dataCrystal);
        } else {
            throw new IllegalStateException("The scanned item or fluid is missing");
        }

        if (recipe == null) return;
        if (!recipe) {
            SCANNER_RECIPES.recipeBuilder(itemStackToString(itemStack))
                    .inputItems(getEmptyCrystal(dataCrystal))
                    .inputItems(itemStack)
                    .outputItems(dataStack)
                    .duration(duration)
                    .EUt(eut)
                    .save();
        } else {
            if (cwut > totalCWU) throw new IllegalStateException("Total CWU cannot be greater than CWU/t!");
            if (catalyst == null) throw new IllegalStateException("Catalyst input required");
            CRYSTAL_SCAN_RECIPES.recipeBuilder(itemStackToString(itemStack))
                    .notConsumable(catalyst)
                    .inputItems(getEmptyCrystal(dataCrystal))
                    .inputItems(itemStack)
                    .outputItems(dataStack)
                    .EUt(eut)
                    .CWUt(cwut)
                    .totalCWU(totalCWU)
                    .save();
        }
    }
}
