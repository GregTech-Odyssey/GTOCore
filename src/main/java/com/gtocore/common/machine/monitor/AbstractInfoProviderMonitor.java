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
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectBooleanPair;

import java.util.List;

public abstract class AbstractInfoProviderMonitor extends BasicMonitor implements IInformationProvider {

    @Persisted
    @DescSynced
    private long priority = 0;

    @Persisted
    @DescSynced
    private ResourceLocation[] displayOrderCache = new ResourceLocation[0];

    @Persisted
    @DescSynced
    private boolean[] displayEnabledCache = new boolean[0];

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
            return getAvailableRLs(); // Default: all available are enabled
        }

        var list = new ObjectArrayList<ResourceLocation>();
        for (int i = 0; i < displayOrderCache.length; i++) {
            // Check if the component is still available and if it is enabled
            if (i < displayEnabledCache.length && displayEnabledCache[i] && getAvailableRLs().contains(displayOrderCache[i])) {
                list.add(displayOrderCache[i]);
            }
        }
        return list;
    }

    private List<ObjectBooleanPair<ResourceLocation>> getComponentStatesForGui() {
        if (displayOrderCache == null || displayOrderCache.length == 0) {
            // If no cache exists, default to all available components being enabled.
            return getAvailableRLs().stream()
                    .map(rl -> ObjectBooleanPair.of(rl, true))
                    .toList();
        }

        var list = new ObjectArrayList<ObjectBooleanPair<ResourceLocation>>();
        for (int i = 0; i < displayOrderCache.length; i++) {
            // Only add components that are still available to the machine
            if (getAvailableRLs().contains(displayOrderCache[i])) {
                boolean isEnabled = i < displayEnabledCache.length && displayEnabledCache[i];
                list.add(ObjectBooleanPair.of(displayOrderCache[i], isEnabled));
            }
        }
        return list;
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
                this.getComponentStatesForGui(),
                this::setComponentStateCache,
                new Position(16, 16),
                new Size(168, 108));
        return (new WidgetGroup(0, 0, 200, 160)).addWidget(panel).addWidget(input).addWidget(scrollAreaWrapper);
    }

    private void setComponentStateCache(List<ObjectBooleanPair<ResourceLocation>> componentStates) {
        if (getLevel() != null && !getLevel().isClientSide()) {
            int size = componentStates.size();
            this.displayOrderCache = new ResourceLocation[size];
            this.displayEnabledCache = new boolean[size];

            for (int i = 0; i < size; i++) {
                ObjectBooleanPair<ResourceLocation> pair = componentStates.get(i);
                this.displayOrderCache[i] = pair.left();
                this.displayEnabledCache[i] = pair.rightBoolean();
            }
        }
    }
}
