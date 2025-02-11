package com.gto.gtocore.api.machine.trait;

import com.gto.gtocore.api.machine.feature.multiblock.IMultiblockTraitHolder;
import com.gto.gtocore.api.machine.feature.multiblock.ITierCasingMachine;

import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.network.chat.Component;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

@Getter
public class TierCasingTrait extends MultiblockTrait {

    private final Map<String, Integer> casingTiers = new Object2IntOpenHashMap<>(2);

    public TierCasingTrait(ITierCasingMachine machine, String... tierTypes) {
        super((IMultiblockTraitHolder) machine);
        for (String type : tierTypes) {
            casingTiers.put(type, 0);
        }
    }

    @Override
    public void onStructureFormed() {
        casingTiers.replaceAll((t, v) -> getMachine().getMultiblockState().getMatchContext().get(t));
    }

    @Override
    public void onStructureInvalid() {
        casingTiers.replaceAll((t, v) -> 0);
    }

    @Override
    public boolean beforeWorking(@NotNull GTRecipe recipe) {
        for (Map.Entry<String, Integer> entry : casingTiers.entrySet()) {
            String type = entry.getKey();
            if (recipe.data.contains(type)) {
                if (recipe.data.getInt(type) > entry.getValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        for (Map.Entry<String, Integer> entry : casingTiers.entrySet()) {
            textList.add(Component.translatable(getTierTranslationKey(entry.getKey()), entry.getValue()));
        }
    }

    public static String getTierTranslationKey(String type) {
        return "gtocore.tier." + type;
    }
}
