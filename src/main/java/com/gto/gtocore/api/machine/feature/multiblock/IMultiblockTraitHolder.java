package com.gto.gtocore.api.machine.feature.multiblock;

import com.gto.gtocore.api.machine.trait.MultiblockTrait;

import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IMultiblockTraitHolder {

    List<MultiblockTrait> getMultiblockTraits();

    default void customText(@NotNull List<Component> textList) {
        getMultiblockTraits().forEach(trait -> trait.customText(textList));
    }
}
