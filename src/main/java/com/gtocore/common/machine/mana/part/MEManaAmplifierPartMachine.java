package com.gtocore.common.machine.mana.part;

import com.gtocore.utils.ManaUnification;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.machine.mana.ManaAmplifierPartMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.ButtonConfigurator;
import com.gregtechceu.gtceu.integration.ae2.machine.feature.IGridConnectedMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.trait.GridNodeHolder;

import net.minecraft.network.chat.Component;

import appbot.ae2.ManaKey;
import appeng.api.config.Actionable;
import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.security.IActionSource;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.SwitchWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import gripe._90.arseng.me.key.SourceKey;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@DataGeneratorScanned
public final class MEManaAmplifierPartMachine extends ManaAmplifierPartMachine implements IGridConnectedMachine {

    @RegisterLanguage(cn = "从ME网络拉取魔力", en = "Pull Mana from ME Network")
    public static final String LANG_USE_SOURCE = "gtceu.machine.mana_amplifier.use_source";
    @RegisterLanguage(cn = "从ME网络拉取魔源", en = "Pull Source from ME Network")
    public static final String LANG_USE_MANA = "gtceu.machine.mana_amplifier.use_mana";
    @Persisted
    private final GridNodeHolder nodeHolder;
    @DescSynced
    @Getter
    @Setter
    private boolean isOnline;
    private final ConditionalSubscriptionHandler updateSubs;

    private boolean useMana = true;
    private boolean useSource = true;

    public MEManaAmplifierPartMachine(MetaMachineBlockEntity holder) {
        super(holder);
        this.nodeHolder = new GridNodeHolder(this);
        this.updateSubs = new ConditionalSubscriptionHandler(this, this::updateTick, 20, this::isWorkingEnabled);
    }

    @Override
    public IManagedGridNode getMainNode() {
        return nodeHolder.getMainNode();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        updateSubs.initialize(getLevel());
    }

    @Override
    public Widget createUIWidget() {
        WidgetGroup superWidget = (WidgetGroup) super.createUIWidget();
        return superWidget.addWidget(
                new LabelWidget(4, 26, () -> LANG_USE_MANA)).addWidget(
                        new SwitchWidget(82, 22, 16, 16, (cd, result) -> useMana = result)
                                .setPressed(useMana)
                                .setBaseTexture(GuiTextures.BUTTON,
                                        GuiTextures.PROGRESS_BAR_SOLAR_STEAM.get(true)
                                                .copy()
                                                .getSubTexture(0.0F, 0.0F, 1.0F, (double) 0.5F).scale(0.8F))
                                .setPressedTexture(GuiTextures.BUTTON,
                                        GuiTextures.PROGRESS_BAR_SOLAR_STEAM.get(true)
                                                .copy()
                                                .getSubTexture(0.0F, 0.5F, 1.0F, (double) 0.5F).scale(0.8F)))
                .addWidget(
                        new LabelWidget(4, 44, () -> LANG_USE_SOURCE))
                .addWidget(
                        new SwitchWidget(82, 40, 16, 16, (cd, result) -> useSource = result)
                                .setPressed(useSource)
                                .setBaseTexture(GuiTextures.BUTTON,
                                        GuiTextures.PROGRESS_BAR_SOLAR_STEAM.get(true)
                                                .copy()
                                                .getSubTexture(0.0F, 0.0F, 1.0F, (double) 0.5F).scale(0.8F))
                                .setPressedTexture(GuiTextures.BUTTON,
                                        GuiTextures.PROGRESS_BAR_SOLAR_STEAM.get(true)
                                                .copy()
                                                .getSubTexture(0.0F, 0.5F, 1.0F, (double) 0.5F).scale(0.8F)));
    }

    private void updateTick() {
        this.updateSubs.updateSubscription();
        if (getActionableNode() != null && getActionableNode().isActive()) {
            var meStorage = getActionableNode().getGrid().getStorageService().getInventory();
            long canInsert = manaContainer.getMaxMana() - manaContainer.getCurrentMana();
            if (canInsert > 0 && useMana) {
                long canExtract = meStorage.extract(ManaKey.KEY, canInsert, Actionable.SIMULATE, IActionSource.ofMachine(this));
                if (canExtract > 0) {
                    long extracted = meStorage.extract(ManaKey.KEY, canExtract, Actionable.MODULATE, IActionSource.ofMachine(this));
                    manaContainer.addMana(extracted, 1, false);
                }
            }
            canInsert = ManaUnification.manaToSource(manaContainer.getMaxMana() - manaContainer.getCurrentMana());
            if (canInsert > 0 && useSource) {
                long canExtract = meStorage.extract(SourceKey.KEY, canInsert, Actionable.SIMULATE, IActionSource.ofMachine(this));
                if (canExtract > 0) {
                    long extracted = meStorage.extract(SourceKey.KEY, canExtract, Actionable.MODULATE, IActionSource.ofMachine(this));
                    manaContainer.addMana(ManaUnification.sourceToMana(extracted), 1, false);
                }
            }
        }
    }

    @Override
    public void setWorkingEnabled(boolean isWorkingAllowed) {
        super.setWorkingEnabled(isWorkingAllowed);
        updateSubs.updateSubscription();
    }

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        configuratorPanel.attachConfigurators(new ButtonConfigurator(new GuiTextureGroup(GuiTextures.BUTTON, GuiTextures.REFUND_OVERLAY), this::refundAll).setTooltips(List.of(Component.translatable("gui.gtceu.refund_all.desc"))));
    }

    private void refundAll(ClickData clickData) {
        if (clickData.isRemote) return;
        setWorkingEnabled(false);
        if (getActionableNode() != null && getActionableNode().isActive()) {
            var meStorage = getActionableNode().getGrid().getStorageService().getInventory();
            long currentMana = manaContainer.getCurrentMana();
            if (currentMana > 0) {
                manaContainer.removeMana(
                        meStorage.insert(
                                ManaKey.KEY,
                                currentMana,
                                Actionable.MODULATE,
                                IActionSource.ofMachine(this)),
                        1, false);
            }
        }
    }
}
