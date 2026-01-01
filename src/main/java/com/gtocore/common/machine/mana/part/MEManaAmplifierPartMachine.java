package com.gtocore.common.machine.mana.part;

import com.gtocore.utils.ManaUnification;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.machine.mana.ManaAmplifierPartMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.integration.ae2.machine.feature.IGridConnectedMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.trait.GridNodeHolder;

import appbot.ae2.ManaKey;
import appeng.api.config.Actionable;
import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.security.IActionSource;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.SwitchWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import gripe._90.arseng.me.key.SourceKey;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

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
    private @Nullable TickableSubscription updateSubs;

    private boolean useMana = true;
    private boolean useSource = true;

    public MEManaAmplifierPartMachine(MetaMachineBlockEntity holder) {
        super(holder);
        this.nodeHolder = new GridNodeHolder(this);
    }

    @Override
    public IManagedGridNode getMainNode() {
        return nodeHolder.getMainNode();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        updateSubs = subscribeServerTick(updateSubs, this::updateTick, 20);
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
    public void onUnload() {
        super.onUnload();
        if (updateSubs != null) {
            updateSubs.unsubscribe();
            updateSubs = null;
        }
    }
}
