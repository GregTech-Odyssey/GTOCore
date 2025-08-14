package com.gtocore.common.machine.multiblock.electric.nano;

import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class NanitesModuleMachine extends ElectricMultiblockMachine {

    NanitesIntegratedMachine nanitesIntegratedMachine;

    final int type;

    public NanitesModuleMachine(MetaMachineBlockEntity holder, int type) {
        super(holder);
        this.type = type;
    }

    @Override
    protected Recipe fullModifyRecipe(@NotNull Recipe recipe) {
        if (nanitesIntegratedMachine == null) return null;
        NanitesIntegratedMachine.trimRecipe(recipe, nanitesIntegratedMachine.chance);
        return super.fullModifyRecipe(recipe);
    }

    @Override
    public boolean beforeWorking(Recipe recipe) {
        if (!effective() || recipe.data.getInt("ebf_temp") > nanitesIntegratedMachine.getTemperature() || recipe.data.getInt("module") != type) return false;
        return super.beforeWorking(recipe);
    }

    private boolean effective() {
        return nanitesIntegratedMachine != null && nanitesIntegratedMachine.isFormed();
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        if (effective()) {
            textList.add(Component.translatable("tooltip.emi.chance.consume", 100 - nanitesIntegratedMachine.chance));
            textList.add(Component.translatable("gtceu.multiblock.blast_furnace.max_temperature", Component.translatable(FormattingUtil.formatNumbers(nanitesIntegratedMachine.getTemperature()) + "K").setStyle(Style.EMPTY.withColor(ChatFormatting.RED))));
        }
    }
}
