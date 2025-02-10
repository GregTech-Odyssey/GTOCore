package com.gto.gtocore.common.machine.multiblock.electric.assembly;

import com.gto.gtocore.api.machine.multiblock.CrossRecipeMultiblockMachine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class SuprachronalAssemblyLineModuleMachine extends CrossRecipeMultiblockMachine {

    SuprachronalAssemblyLineMachine assemblyLineMachine;

    public SuprachronalAssemblyLineModuleMachine(IMachineBlockEntity holder) {
        super(holder, false, false, m -> ((SuprachronalAssemblyLineModuleMachine) m).parallel());
    }

    private int parallel() {
        if (!effective()) return 1;
        return assemblyLineMachine.getMaxParallel();
    }

    private boolean effective() {
        return assemblyLineMachine != null && assemblyLineMachine.isFormed();
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        textList.add(effective() ? Component.translatable("gtocore.machine.module.have") : Component.translatable("gtocore.machine.module.null"));
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        if (effective()) return super.beforeWorking(recipe);
        return false;
    }
}
