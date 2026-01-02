package com.gtocore.common.machine.mana;

import com.gtocore.utils.ManaUnification;

import com.gtolib.api.machine.mana.feature.IWirelessManaContainerHolder;
import com.gtolib.api.wireless.WirelessManaContainer;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.widget.IntInputWidget;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.integration.ae2.machine.feature.IGridConnectedMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.trait.GridNodeHolder;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import appbot.ae2.ManaKey;
import appeng.api.config.Actionable;
import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.IStorageMounts;
import appeng.api.storage.IStorageProvider;
import appeng.api.storage.MEStorage;
import com.hepdd.gtmthings.utils.BigIntegerUtils;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import gripe._90.arseng.me.key.SourceKey;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.UUID;

public final class MEManaInterface extends MetaMachine implements
                                   IGridConnectedMachine,
                                   MEStorage,
                                   IMachineLife,
                                   IWirelessManaContainerHolder,
                                   IStorageProvider,
                                   IFancyUIMachine {

    @Getter
    @Setter
    private WirelessManaContainer wirelessManaContainerCache;
    @Persisted
    private final GridNodeHolder nodeHolder;
    @DescSynced
    @Getter
    @Setter
    private boolean isOnline;
    @Persisted
    @Getter
    private int priority = 0;

    public MEManaInterface(MetaMachineBlockEntity holder) {
        super(holder);
        this.nodeHolder = new GridNodeHolder(this);
        getMainNode().addService(IStorageProvider.class, this);
    }

    @Override
    public IManagedGridNode getMainNode() {
        return nodeHolder.getMainNode();
    }

    @Override
    public Component getDescription() {
        return Component.translatable("ars_nouveau.category.source").append("&").append(Component
                .translatable("ftbquests.task.ftbquests.botania_mana"));
    }

    @Override
    public @Nullable UUID getUUID() {
        return getOwnerUUID();
    }

    @Override
    public boolean isPreferredStorageFor(AEKey what, IActionSource source) {
        return what == ManaKey.KEY || what == SourceKey.KEY;
    }

    @Override
    public long insert(AEKey what, long amount, Actionable mode, IActionSource source) {
        if ((what != ManaKey.KEY && what != SourceKey.KEY) || getWirelessManaContainer() == null) {
            return 0;
        }
        var converted = convert(what, amount);
        if (mode == Actionable.MODULATE) {
            getWirelessManaContainer().setStorage(getWirelessManaContainer().getStorage().add(BigInteger.valueOf(converted)));
        }
        return amount;
    }

    @Override
    public long extract(AEKey what, long amount, Actionable mode, IActionSource source) {
        if ((what != ManaKey.KEY && what != SourceKey.KEY) || getWirelessManaContainer() == null) {
            return 0;
        }
        var converted = convert(what, amount);
        long stored = BigIntegerUtils.getLongValue(getWirelessManaContainer().getStorage());
        long toExtract = Math.min(stored, converted);
        if (mode == Actionable.MODULATE) {
            getWirelessManaContainer().setStorage(getWirelessManaContainer().getStorage().subtract(BigInteger.valueOf(toExtract)));
        }
        return toExtract;
    }

    private long convert(AEKey what, long amount) {
        if (what == ManaKey.KEY) {
            return amount;
        } else if (what == SourceKey.KEY) {
            return ManaUnification.sourceToMana(amount);
        } else {
            return 0;
        }
    }

    @Override
    public void getAvailableStacks(KeyCounter toAdd) {
        if (getWirelessManaContainer() == null) {
            return;
        }
        long stored = BigIntegerUtils.getLongValue(getWirelessManaContainer().getStorage());
        if (stored > 0) {
            toAdd.add(ManaKey.KEY, stored);
        }
        long storedSource = ManaUnification.manaToSource(stored);
        if (storedSource > 0) {
            toAdd.add(SourceKey.KEY, storedSource);
        }
    }

    @Override
    public void onMachinePlaced(@org.jetbrains.annotations.Nullable LivingEntity player, ItemStack stack) {
        if (player != null) {
            setOwnerUUID(player.getUUID());
        }
    }

    @Override
    public void mountInventories(IStorageMounts storageMounts) {
        storageMounts.mount(this);
    }

    @Override
    public Widget createUIWidget() {
        final int before = priority;
        var intWidget = new IntInputWidget(this::getPriority, this::setPriority);
        intWidget.setMax(100000000);
        intWidget.setMin(-100000000);
        intWidget.setValue(before);
        return new WidgetGroup(0, 0, 100, 20)
                .addWidget(intWidget)
                .addWidget(new LabelWidget(24, -16, () -> "gui.ae2.Priority")
                        .setHoverTooltips("gui.ae2.PriorityExtractionHint", "gui.ae2.PriorityInsertionHint"));
    }

    private void setPriority(int integer) {
        if (integer != this.priority) {
            this.priority = integer;
            if (getMainNode() != null) IStorageProvider.requestUpdate(getMainNode());
        }
    }
}
