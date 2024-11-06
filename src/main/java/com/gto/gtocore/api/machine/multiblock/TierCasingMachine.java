package com.gto.gtocore.api.machine.multiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

@Getter
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TierCasingMachine extends WorkableElectricMultiblockMachine {

    private final Map<String, Integer> casingTiers = new HashMap<>();

    public TierCasingMachine(IMachineBlockEntity holder, String... tierTypes) {
        super(holder);
        for (String type : tierTypes) {
            casingTiers.put(type, 0);
        }
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        casingTiers.replaceAll((t, v) -> getMultiblockState().getMatchContext().get(t));
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        casingTiers.replaceAll((t, v) -> 0);
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        if (recipe != null && !recipe.data.contains("no_inspection")) {
            for (String type : casingTiers.keySet()) {
                if (recipe.data.contains(type) && recipe.data.getInt(type) > casingTiers.get(type)) {
                    return false;
                }
            }
        }
        return super.beforeWorking(recipe);
    }

    @Override
    public void addDisplayText(@NotNull List<Component> textList) {
        super.addDisplayText(textList);
        if (!this.isFormed) return;
        for (String type : casingTiers.keySet()) {
            textList.add(Component.translatable("gtocore.tier." + type, casingTiers.get(type)));
        }
    }
}
