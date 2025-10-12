package com.gtocore.common.machine.monitor;

import com.gtocore.api.gui.DisplayComponentGroup;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.widget.LongInputWidget;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;

import java.util.List;
import java.util.stream.Stream;

public abstract class AbstractInfoProviderMonitor extends BasicMonitor implements IInformationProvider {

    @Persisted
    @DescSynced
    private long priority = 0;

    @Persisted
    @DescSynced
    private ResourceLocation[] displayOrderCache = new ResourceLocation[0];

    private TickableSubscription tickableSubscription;

    AbstractInfoProviderMonitor(MetaMachineBlockEntity holder) {
        super(holder);
        Class<? extends BasicMonitor> clazz = this.getClass();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        tickableSubscription = this.subscribeServerTick(tickableSubscription, () -> {
            if (this.getOffsetTimer() % 10 == 0) {
                this.syncInfoFromServer();
                this.getSyncStorage().markAllDirty();
                this.requestSync();
            }
        });
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (tickableSubscription != null) {
            tickableSubscription.unsubscribe();
            tickableSubscription = null;
        }
    }

    @Override
    public long getPriority() {
        return priority;
    }

    @Override
    public void setPriority(long priority) {
        this.priority = priority;
        if (getLevel() != null && !getLevel().isClientSide()) {
            this.requestSync();
        }
    }

    @Override
    public List<ResourceLocation> getSortedRLs() {
        if (displayOrderCache == null || displayOrderCache.length == 0) {
            return getAvailableRLs();
        }
        return Stream.of(displayOrderCache).filter(rl -> getAvailableRLs().contains(rl)).toList();
    }

    @Override
    public Widget createUIWidget() {
        final var initialPriority = this.getPriority();
        LongInputWidget input = new LongInputWidget(Position.of(50, 144),
                this::getPriority, this::setPriority);
        input.setMax((long) Integer.MAX_VALUE).setMin((long) Integer.MIN_VALUE).setValue(initialPriority);
        input.setHoverTooltips(Component.translatable("gtocore.machine.monitor.priority"));
        var panel = new ComponentPanelWidget(
                input.getPositionX() + input.getSizeWidth() / 2,
                input.getPositionY() - 15,
                List.of(Component.translatable("gtocore.machine.monitor.priority").withStyle(ChatFormatting.BLACK)))
                .setCenter(true)
                .setClientSideWidget();
        var scrollAreaWrapper = new DisplayComponentGroup(
                this.getAvailableRLs(),
                this.getSortedRLs(),
                this::setDisplayOrderCache,
                new Position(16, 16),
                new Size(168, 108));
        return (new WidgetGroup(0, 0, 200, 160)).addWidget(panel).addWidget(input).addWidget(scrollAreaWrapper);
    }

    private void setDisplayOrderCache(List<ResourceLocation> displayOrderCache) {
        if (getLevel() != null && !getLevel().isClientSide()) {
            this.displayOrderCache = displayOrderCache.toArray(new ResourceLocation[0]);
        }
    }
}
