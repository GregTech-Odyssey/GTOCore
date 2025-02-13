package com.gto.gtocore.common.machine.multiblock.electric.smelter;

import com.gto.gtocore.api.machine.multiblock.CoilMultiblockMachine;
import com.gto.gtocore.init.GTORecipeTypes;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class DimensionallyTranscendentPlasmaForgeMachine extends CoilMultiblockMachine {

    public DimensionallyTranscendentPlasmaForgeMachine(IMachineBlockEntity holder) {
        super(holder, false, false);
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        if (recipe == null) return false;
        if (gto$getTemperature() == 273) {
            if (getRecipeType() != GTORecipeTypes.STELLAR_FORGE_RECIPES) {
                return false;
            } else if (recipe.data.getInt("ebf_temp") > 32000) {
                return false;
            }
        } else if (recipe.data.getInt("ebf_temp") > gto$getTemperature()) {
            return false;
        }
        return super.beforeWorking(recipe);
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        textList.add(Component.translatable("gtceu.multiblock.blast_furnace.max_temperature", Component.literal(FormattingUtil.formatNumbers((gto$getTemperature() == 273 ? 32000 : gto$getTemperature())) + "K").withStyle(ChatFormatting.BLUE)));
        if (getRecipeType() == GTORecipeTypes.STELLAR_FORGE_RECIPES && gto$getTemperature() != 273) {
            textList.add(Component.translatable("gtocore.machine.dimensionally_transcendent_plasma_forge.coil").withStyle(ChatFormatting.RED));
        }
    }
}
