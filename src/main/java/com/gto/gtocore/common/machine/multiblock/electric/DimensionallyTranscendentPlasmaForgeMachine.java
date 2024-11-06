package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.CoilWorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DimensionallyTranscendentPlasmaForgeMachine extends CoilWorkableElectricMultiblockMachine {

    public DimensionallyTranscendentPlasmaForgeMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        if (recipe == null) return false;
        if (getCoilType().getCoilTemperature() == 273) {
            if (getRecipeType() != GTORecipeTypes.STELLAR_FORGE_RECIPES) {
                return false;
            } else if (recipe.data.getInt("ebf_temp") > 32000) {
                return false;
            }
        } else if (recipe.data.getInt("ebf_temp") > getCoilType().getCoilTemperature()) {
            return false;
        }
        return super.beforeWorking(recipe);
    }

    @Override
    public void addDisplayText(@NotNull List<Component> textList) {
        super.addDisplayText(textList);
        if (this.isFormed()) {
            int temp = getCoilType().getCoilTemperature();
            textList.add(Component.translatable("gtceu.multiblock.blast_furnace.max_temperature", Component.literal(FormattingUtil.formatNumbers((temp == 273 ? 32000 : temp)) + "K").withStyle(ChatFormatting.BLUE)));
            if (getRecipeType() == GTORecipeTypes.STELLAR_FORGE_RECIPES && temp != 273) {
                textList.add(Component.translatable("gtocore.machine.dimensionally_transcendent_plasma_forge.coil").withStyle(ChatFormatting.RED));
            }
        }
    }
}
