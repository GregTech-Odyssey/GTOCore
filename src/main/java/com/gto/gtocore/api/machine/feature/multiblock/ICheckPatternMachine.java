package com.gto.gtocore.api.machine.feature.multiblock;

import com.gto.gtocore.api.gui.GTOGuiTextures;

import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton;
import com.gregtechceu.gtceu.api.machine.MetaMachine;

import net.minecraft.network.chat.Component;

import java.util.List;

public interface ICheckPatternMachine {

    default void gTOCore$setTime(int time) {}

    default int gTOCore$getTime() {
        return 0;
    }

    default boolean gtocore$hasButton() {
        return false;
    }

    static void attachConfigurators(ConfiguratorPanel configuratorPanel, MetaMachine machine) {
        if (machine instanceof ICheckPatternMachine checkPatternMachine) {
            configuratorPanel.attachConfigurators(new IFancyConfiguratorButton.Toggle(
                    GTOGuiTextures.STRUCTURE_CHECK.getSubTexture(0, 0, 1, 0.5),
                    GTOGuiTextures.STRUCTURE_CHECK.getSubTexture(0, 0.5, 1, 0.5),
                    () -> checkPatternMachine.gTOCore$getTime() < 1, (clickData, pressed) -> {
                        if (checkPatternMachine.gTOCore$getTime() > 0) checkPatternMachine.gTOCore$setTime(0);
                    })
                    .setTooltipsSupplier(pressed -> List.of(Component.translatable("gtocore.machine.structure_check"))));
        }
    }
}
