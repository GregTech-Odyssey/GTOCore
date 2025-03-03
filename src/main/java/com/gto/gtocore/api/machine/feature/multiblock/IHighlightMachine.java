package com.gto.gtocore.api.machine.feature.multiblock;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import com.glodblock.github.extendedae.client.render.EAEHighlightHandler;

import java.util.List;
import java.util.Set;

public interface IHighlightMachine {

    Set<BlockPos> getHighlightPos();

    default void attachHighlightConfigurators(ConfiguratorPanel configuratorPanel) {
        configuratorPanel.attachConfigurators(new IFancyConfiguratorButton.Toggle(
                GuiTextures.LIGHT_ON, GuiTextures.LIGHT_ON, () -> false,
                (clickData, pressed) -> {
                    if (clickData.isRemote && ((MultiblockControllerMachine) this).isFormed() && ((MetaMachine) this).getLevel() != null) getHighlightPos().forEach(p -> EAEHighlightHandler.highlight(p, ((MetaMachine) this).getLevel().dimension(), System.currentTimeMillis() + 15000));
                })
                .setTooltipsSupplier(pressed -> List.of(Component.translatable("gtocore.machine.highlight_module"))));
    }
}
