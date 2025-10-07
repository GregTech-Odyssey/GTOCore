package com.gtocore.common.machine.multiblock.electric.smelter;

import com.gtocore.common.block.CoilType;
import com.gtocore.common.data.GTORecipeTypes;

import com.gtolib.api.machine.multiblock.CoilCrossRecipeMultiblockMachine;
import com.gtolib.api.recipe.IdleReason;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class DimensionallyTranscendentPlasmaForgeMachine extends CoilCrossRecipeMultiblockMachine {

    public DimensionallyTranscendentPlasmaForgeMachine(MetaMachineBlockEntity holder) {
        super(holder, false, true, false, false, MachineUtils::getHatchParallelLong);
    }

    @Override
    public int getTemperature() {
        return getCoilType() == CoilType.URUIUM ? 32000 : super.getTemperature();
    }

    @Override
    public Recipe getRealRecipe(@NotNull Recipe recipe) {
        if (getRecipeType() == GTORecipeTypes.STELLAR_FORGE_RECIPES) {
            if (getCoilType() != CoilType.URUIUM) {
                getEnhancedRecipeLogic().gtolib$setIdleReason(Component.translatable("gtocore.machine.dimensionally_transcendent_plasma_forge.coil"));
                return null;
            }
        } else if (recipe.data.getInt("ebf_temp") > getTemperature()) {
            setIdleReason(IdleReason.INSUFFICIENT_TEMPERATURE);
            return null;
        }
        return super.getRealRecipe(recipe);
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        textList.add(Component.translatable("gtceu.multiblock.blast_furnace.max_temperature", Component.literal(FormattingUtil.formatNumbers(getTemperature()) + "K").withStyle(ChatFormatting.BLUE)));
        if (getRecipeType() == GTORecipeTypes.STELLAR_FORGE_RECIPES && getCoilType() != CoilType.URUIUM) {
            textList.add(Component.translatable("gtocore.machine.dimensionally_transcendent_plasma_forge.coil").withStyle(ChatFormatting.RED));
        }
    }
}
