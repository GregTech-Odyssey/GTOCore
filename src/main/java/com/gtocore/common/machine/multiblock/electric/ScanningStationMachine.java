package com.gtocore.common.machine.multiblock.electric;

import com.gtocore.common.machine.multiblock.part.ScanningHolderMachine;

import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockDisplayText;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.IO;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ScanningStationMachine extends ElectricMultiblockMachine {

    private ScanningHolderMachine objectHolder;

    public ScanningStationMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        for (IMultiPart part : getParts()) {
            if (part instanceof ScanningHolderMachine scanningHolder) {
                if (scanningHolder.getFrontFacing() != getFrontFacing().getOpposite()) {
                    onStructureInvalid();
                    return;
                }
                this.objectHolder = scanningHolder;
                // 添加物品流体处理器（包含扫描槽、催化剂槽和数据槽）
                addHandlerList(RecipeHandlerUnit.of(IO.IN, scanningHolder.getAsHandler(), scanningHolder.getCatalystFluidTank()));
            }
        }

        // 必须同时有扫描部件
        if (objectHolder == null) {
            onStructureInvalid();
        }
    }

    @Override
    public boolean checkPattern() {
        boolean isFormed = super.checkPattern();
        if (isFormed && objectHolder != null && objectHolder.getFrontFacing() != getFrontFacing().getOpposite()) {
            onStructureInvalid();
        }
        return isFormed;
    }

    @Override
    public void onStructureInvalid() {
        if (objectHolder != null) {
            objectHolder.setLocked(false);
            objectHolder = null;
        }
        super.onStructureInvalid();
    }

    @Override
    public boolean regressWhenWaiting() {
        return false;
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        MultiblockDisplayText.builder(textList, isFormed())
                .setWorkingStatus(recipeLogic.isWorkingEnabled(), recipeLogic.isActive())
                .setWorkingStatusKeys("gtceu.multiblock.idling", "gtceu.multiblock.work_paused",
                        "gtocore.machine.analysis")
                .addEnergyUsageLine(energyContainer)
                .addEnergyTierLine(tier)
                .addWorkingStatusLine()
                .addProgressLineOnlyPercent(recipeLogic.getProgressPercent());
    }

    @Override
    public boolean matchRecipeOutput(GTRecipe recipe) {
        return true;
    }

    @Override
    public boolean handleRecipeInput(RecipeHandlerUnit unit, GTRecipe recipe) {
        if (super.handleRecipeInput(unit, recipe)) {
            if (objectHolder != null) objectHolder.setLocked(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean handleRecipeOutput(GTRecipe originalRecipe) {
        var lastRecipe = getRecipeLogic().getLastRecipe();
        if (lastRecipe == null) {
            objectHolder.setLocked(false);
            return true;
        }

        var catalyst = lastRecipe.itemInputs;
        if (catalyst.getFirst().inner.getInnerItemStack().getItem() != objectHolder.getCatalystItem(false).getItem()) {
            ItemStack hold = objectHolder.getHeldItem(true);
            objectHolder.setHeldItem(objectHolder.getCatalystItem(true));
            objectHolder.setCatalystItem(hold);
            objectHolder.setLocked(false);
            return true;
        }

        var fluidInputs = lastRecipe.fluidInputs;
        if (!fluidInputs.isEmpty()) {
            var ingredient = fluidInputs.getFirst();
            var requiredFluid = ingredient.inner.getFluid();
            FluidStack currentFluid = objectHolder.getCatalystFluidTank().getFluidInTank(0);
            if (currentFluid.isEmpty() || currentFluid.getFluid() != requiredFluid || currentFluid.getAmount() < ingredient.amount) {
                objectHolder.setLocked(false);
                return true;
            }
        }

        objectHolder.setHeldItem(ItemStack.EMPTY);
        ItemStack outputItem = ItemStack.EMPTY;
        var contents = lastRecipe.itemOutputs;
        if (!contents.isEmpty()) outputItem = contents.getFirst().inner.getInnerItemStack().copy();
        if (!outputItem.isEmpty()) objectHolder.setDataItem(outputItem);

        objectHolder.getCatalystFluidTank().setFluidInTank(0, FluidStack.EMPTY);
        objectHolder.setLocked(false);
        return true;
    }
}
