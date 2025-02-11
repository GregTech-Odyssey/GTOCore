package com.gto.gtocore.api.machine.feature.multiblock;

import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.sound.SoundEntry;

public interface IEnhancedMultiblockMachine {

    default void onRecipeFinish() {}

    default void onContentChanges() {}

    default SoundEntry getSound() {
        return null;
    }

    default void onPartScan(IMultiPart part) {}
}
