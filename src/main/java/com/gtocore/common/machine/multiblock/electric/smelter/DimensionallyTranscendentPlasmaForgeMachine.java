package com.gtocore.common.machine.multiblock.electric.smelter;

import com.gtocore.common.block.CoilType;
import com.gtocore.common.data.GTORecipeTypes;

import com.gtolib.api.machine.multiblock.CoilMultiblockMachine;
import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class DimensionallyTranscendentPlasmaForgeMachine extends CoilMultiblockMachine {

    public DimensionallyTranscendentPlasmaForgeMachine(MetaMachineBlockEntity holder) {
        super(holder, false, false);
    }

    @Override
    public int getTemperature() {
        return getCoilType() == CoilType.URUIUM ? 32000 : super.getTemperature();
    }

    @Override
    protected boolean beforeWorking(@Nullable Recipe recipe) {
        if (recipe == null) return false;
        if (getRecipeType() == GTORecipeTypes.STELLAR_FORGE_RECIPES) {
            if (getCoilType() != CoilType.URUIUM) {
                return false;
            }
            if (recipe.data.getInt("ebf_temp") > 32000) {
                return false;
            }
        } else if (recipe.data.getInt("ebf_temp") > getTemperature()) {
            return false;
        }
        return super.beforeWorking(recipe);
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        textList.add(Component.translatable("gtceu.multiblock.blast_furnace.max_temperature", Component.literal(FormattingUtil.formatNumbers(getTemperature()) + "K").withStyle(ChatFormatting.BLUE)));
        if (getRecipeType() == GTORecipeTypes.STELLAR_FORGE_RECIPES && getCoilType() != CoilType.URUIUM) {
            textList.add(Component.translatable("gtocore.machine.dimensionally_transcendent_plasma_forge.coil").withStyle(ChatFormatting.RED));
        }
    }
}
