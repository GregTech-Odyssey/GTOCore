package com.gtocore.common.machine.mana.multiblock;

import com.gtolib.api.gui.OverclockConfigurator;
import com.gtolib.api.machine.feature.IOverclockConfigMachine;
import com.gtolib.api.machine.mana.feature.IManaMultiblock;
import com.gtolib.api.machine.mana.trait.ManaTrait;
import com.gtolib.api.machine.multiblock.NoEnergyMultiblockMachine;
import com.gtolib.api.misc.ManaContainerList;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton;

import net.minecraft.network.chat.Component;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ManaMultiblockMachine extends NoEnergyMultiblockMachine implements IManaMultiblock, IOverclockConfigMachine {

    @Persisted
    private boolean batchEnabled;
    @Persisted
    private int ocLimit = 20;
    private final ManaTrait manaTrait;

    public ManaMultiblockMachine(MetaMachineBlockEntity holder) {
        super(holder);
        this.manaTrait = new ManaTrait(this);
    }

    @Override
    public void attachConfigurators(@NotNull ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        if (!isGeneratorMana()) {
            configuratorPanel.attachConfigurators(new OverclockConfigurator(this));
            configuratorPanel.attachConfigurators(new IFancyConfiguratorButton.Toggle(
                    GuiTextures.BUTTON_BATCH.getSubTexture(0, 0, 1, 0.5),
                    GuiTextures.BUTTON_BATCH.getSubTexture(0, 0.5, 1, 0.5),
                    this::isBatchEnabled, (cd, p) -> batchEnabled = p)
                    .setTooltipsSupplier(p -> List.of(Component.translatable("gtceu.machine.batch_" + (p ? "enabled" : "disabled")))));
        }
    }

    @Override
    @NotNull
    public ManaContainerList getManaContainer() {
        return manaTrait.getManaContainers();
    }

    @Override
    public boolean isGeneratorMana() {
        return false;
    }

    @Override
    public void gtolib$setOCLimit(int number) {
        ocLimit = number;
    }

    @Override
    public int gtolib$getOCLimit() {
        return ocLimit;
    }

    @Override
    public boolean isBatchEnabled() {
        return this.batchEnabled;
    }
}
