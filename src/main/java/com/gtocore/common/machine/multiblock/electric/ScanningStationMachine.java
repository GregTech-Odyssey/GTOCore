package com.gtocore.common.machine.multiblock.electric;

import com.gtocore.common.machine.multiblock.part.ScanningHolderMachine;

import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeRunner;
import com.gtolib.utils.ItemUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockDisplayText;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
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
                addHandlerList(RecipeHandlerList.of(IO.IN, scanningHolder.getAsHandler(), scanningHolder.getCatalystFluidTank()));
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
    public boolean matchRecipe(Recipe recipe) {
        return RecipeRunner.matchRecipeInput(this, recipe);
    }

    @Override
    public boolean handleRecipeIO(Recipe originalRecipe, IO io) {
        if (io == IO.IN) {
            objectHolder.setLocked(true);
            return true;
        }

        var lastRecipe = getRecipeLogic().getLastRecipe();
        if (lastRecipe == null) {
            objectHolder.setLocked(false);
            return true;
        }

        var catalyst = lastRecipe.getInputContents(ItemRecipeCapability.CAP);
        if (ItemUtils.getFirstSized(ItemRecipeCapability.CAP.of(catalyst.getFirst().content)).getItem() != objectHolder.getCatalystItem(false).getItem()) {
            ItemStack hold = objectHolder.getHeldItem(true);
            objectHolder.setHeldItem(objectHolder.getCatalystItem(true));
            objectHolder.setCatalystItem(hold);
            objectHolder.setLocked(false);
            return true;
        }

        var fluidInputs = lastRecipe.getInputContents(FluidRecipeCapability.CAP);
        if (!fluidInputs.isEmpty()) {
            FluidStack requiredFluid = FluidRecipeCapability.CAP.of(fluidInputs.getFirst().content).getStacks()[0];
            FluidStack currentFluid = objectHolder.getCatalystFluidTank().getFluidInTank(0);
            if (currentFluid.isEmpty() || !requiredFluid.isFluidEqual(currentFluid) || currentFluid.getAmount() < requiredFluid.getAmount()) {
                objectHolder.setLocked(false);
                return true;
            }
        }

        objectHolder.setHeldItem(ItemStack.EMPTY);
        ItemStack outputItem = ItemStack.EMPTY;
        var contents = lastRecipe.getOutputContents(ItemRecipeCapability.CAP);
        if (!contents.isEmpty()) outputItem = ItemUtils.getFirstSized((Ingredient) contents.getFirst().content).copy();
        if (!outputItem.isEmpty()) objectHolder.setDataItem(outputItem);

        objectHolder.getCatalystFluidTank().setFluidInTank(0, FluidStack.EMPTY);
        objectHolder.setLocked(false);
        return true;
    }
}
