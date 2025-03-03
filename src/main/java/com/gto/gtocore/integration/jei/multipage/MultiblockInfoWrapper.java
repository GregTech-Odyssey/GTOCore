package com.gto.gtocore.integration.jei.multipage;

import com.gto.gtocore.client.gui.PatternPreview;

import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;

import com.lowdragmc.lowdraglib.jei.ModularWrapper;

final class MultiblockInfoWrapper extends ModularWrapper<PatternPreview> {

    MultiblockInfoWrapper(MultiblockMachineDefinition definition) {
        super(PatternPreview.getPatternWidget(definition));
    }
}
