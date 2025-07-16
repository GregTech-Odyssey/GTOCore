package com.gtocore.common.machine.multiblock.electric;

import com.gtocore.common.machine.multiblock.part.DataGenerateHolderMachine;

import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeRunner;
import com.gtolib.utils.ItemUtils;

import com.gregtechceu.gtceu.api.capability.IOpticalComputationProvider;
import com.gregtechceu.gtceu.api.capability.IOpticalComputationReceiver;
import com.gregtechceu.gtceu.api.capability.forge.GTCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockDisplayText;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SyntheticDataAssemblyPlantMachine extends ElectricMultiblockMachine implements IOpticalComputationReceiver {

    @Override
    public IOpticalComputationProvider getComputationProvider() {
        return computationProvider;
    }

    private IOpticalComputationProvider computationProvider;
    private DataGenerateHolderMachine objectHolder;

    public SyntheticDataAssemblyPlantMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        for (IMultiPart part : getParts()) {
            if (part instanceof DataGenerateHolderMachine scanningHolder) {
                if (scanningHolder.getFrontFacing() != getFrontFacing().getOpposite()) {
                    onStructureInvalid();
                    return;
                }
                this.objectHolder = scanningHolder;
                // 添加物品处理器（包含扫描槽、催化剂槽和数据槽）
                addHandlerList(RecipeHandlerList.of(IO.IN, scanningHolder.getAsHandler()));
            }

            // 获取计算提供者
            part.self().holder.self()
                    .getCapability(GTCapability.CAPABILITY_COMPUTATION_PROVIDER)
                    .ifPresent(provider -> this.computationProvider = provider);
        }

        // 必须同时有扫描部件和计算提供者
        if (computationProvider == null || objectHolder == null) {
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
        computationProvider = null;
        // 重置扫描部件状态
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
                        "gtocore.machine.assembling")
                .addEnergyUsageLine(energyContainer)
                .addEnergyTierLine(tier)
                .addWorkingStatusLine()
                .addProgressLineOnlyPercent(recipeLogic.getProgressPercent());
    }

    @Override
    public boolean matchRecipe(Recipe recipe) {
        if (objectHolder.getDataItem(false).hasTag()) return false;
        return RecipeRunner.matchRecipeInput(this, recipe);
    }

    @Override
    public boolean handleRecipeIO(Recipe originalRecipe, IO io) {
        if (io == IO.IN) {
            objectHolder.setLocked(true);
            return true;
        }

        if (getRecipeLogic().getLastRecipe() == null) {
            objectHolder.setLocked(false);
            return true;
        }

        ItemStack outputItem = ItemStack.EMPTY;
        var contents = getRecipeLogic().getLastRecipe().getOutputContents(ItemRecipeCapability.CAP);
        if (!contents.isEmpty()) {
            outputItem = ItemUtils.getFirstSized((Ingredient) contents.get(0).content).copy();
        }
        if (!outputItem.isEmpty()) {
            objectHolder.setDataItem(outputItem);
        }

        objectHolder.setLocked(false);
        return true;
    }
}
