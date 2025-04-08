package com.gto.gtocore.api.machine.trait;

import com.gto.gtocore.api.machine.feature.multiblock.IMultiblockTraitHolder;
import com.gto.gtocore.api.machine.feature.multiblock.ITierCasingMachine;

import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.network.chat.Component;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public class TierCasingTrait extends MultiblockTrait {

    private final Object2IntMap<String> casingTiers = new Object2IntOpenHashMap<>(2);

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
        for (Object2IntMap.Entry<String> entry : casingTiers.object2IntEntrySet()) {
            String type = entry.getKey();
            if (recipe.data.contains(type)) {
                if (recipe.data.getInt(type) > entry.getIntValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        for (Object2IntMap.Entry<String> entry : casingTiers.object2IntEntrySet()) {
            textList.add(Component.translatable(getTierTranslationKey(entry.getKey()), entry.getIntValue()));
        }
    }

    public static String getTierTranslationKey(String type) {
        return "gtocore.tier." + type;
    }
}
