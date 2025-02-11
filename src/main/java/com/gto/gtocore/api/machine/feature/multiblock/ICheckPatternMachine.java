package com.gto.gtocore.api.machine.feature.multiblock;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton;
import com.gregtechceu.gtceu.api.machine.MetaMachine;

import net.minecraft.network.chat.Component;

import java.util.List;

public interface ICheckPatternMachine {

    void gTOCore$cleanTime();

    int gTOCore$getTime();

    static void attachConfigurators(ConfiguratorPanel configuratorPanel, MetaMachine machine) {
        if (machine instanceof ICheckPatternMachine checkPatternMachine) {
            configuratorPanel.attachConfigurators(new IFancyConfiguratorButton.Toggle(GuiTextures.BUTTON_CHUNK_MODE.getSubTexture(0, 0, 1, 0.5), GuiTextures.BUTTON_CHUNK_MODE.getSubTexture(0, 0.5, 1, 0.5), () -> checkPatternMachine.gTOCore$getTime() < 1, (clickData, pressed) -> { if (checkPatternMachine.gTOCore$getTime() > 0) checkPatternMachine.gTOCore$cleanTime(); }).setTooltipsSupplier(pressed -> List.of(Component.translatable("gtocore.machine.structure_check"))));
        }
    }
}
