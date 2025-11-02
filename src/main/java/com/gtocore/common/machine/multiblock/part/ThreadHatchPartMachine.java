package com.gtocore.common.machine.multiblock.part;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.capability.IExtendWirelessEnergyContainerHolder;
import com.gtolib.api.machine.part.AmountConfigurationHatchPartMachine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton;

import net.minecraft.network.chat.Component;

import com.hepdd.gtmthings.api.misc.WirelessEnergyContainer;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

@Getter
@DataGeneratorScanned
public final class ThreadHatchPartMachine extends AmountConfigurationHatchPartMachine implements IExtendWirelessEnergyContainerHolder {

    @RegisterLanguage(cn = "并行重复配方[%s]", en = "Parallel repeated recipes [%s]")
    private static final String REPEATED_RECIPES = "gtocore.machine.repeated_recipes";

    @RegisterLanguage(cn = "线程压缩[%s]", en = "Compress Thread [%s]")
    private static final String COMPRESS_THREAD = "gtocore.machine.compress_thread";

    @Persisted
    private boolean repeatedRecipes = true;
    @Persisted
    private boolean compressThread = true;

    @Setter
    private WirelessEnergyContainer wirelessEnergyContainerCache;

    public ThreadHatchPartMachine(MetaMachineBlockEntity holder, int tier) {
        super(holder, tier, 1, 1L << (tier - GTValues.LuV));
    }

    public int getCurrentThread() {
        return (int) getCurrent();
    }

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        configuratorPanel.attachConfigurators(new IFancyConfiguratorButton.Toggle(GuiTextures.BUTTON_WORKING_ENABLE.getSubTexture(0, 0.5, 1, 0.5), GuiTextures.BUTTON_WORKING_ENABLE.getSubTexture(0, 0, 1, 0.5), () -> repeatedRecipes, (clickData, pressed) -> repeatedRecipes = pressed).setTooltipsSupplier(pressed -> List.of(Component.translatable(REPEATED_RECIPES, Component.translatable(pressed ? "gtocore.machine.on" : "gtocore.machine.off")))));
        configuratorPanel.attachConfigurators(new IFancyConfiguratorButton.Toggle(GuiTextures.BUTTON_CHUNK_MODE.getSubTexture(0, 0.5, 1, 0.5), GuiTextures.BUTTON_WORKING_ENABLE.getSubTexture(0, 0, 1, 0.5), () -> compressThread, (clickData, pressed) -> compressThread = pressed).setTooltipsSupplier(pressed -> List.of(Component.translatable(COMPRESS_THREAD, Component.translatable(pressed ? "gtocore.machine.on" : "gtocore.machine.off")))));
    }

    @Override
    public @Nullable UUID getUUID() {
        return getOwnerUUID();
    }
}
