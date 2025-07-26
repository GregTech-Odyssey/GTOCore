package com.gtocore.common.machine.monitor;

import com.gtocore.api.gui.DisplayComponentGroup;

import com.gregtechceu.gtceu.api.gui.widget.LongInputWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public abstract class AbstractInfoProviderMonitor extends BasicMonitor implements IInformationProvider {

    @Persisted
    @DescSynced
    private long priority = 0;

    @Persisted
    @DescSynced
    private ResourceLocation[] displayOrderCache = new ResourceLocation[0];
    private static final ManagedFieldHolder BASIC_MONITOR_HOLDER = new ManagedFieldHolder(AbstractInfoProviderMonitor.class, MetaMachine.MANAGED_FIELD_HOLDER);

    protected ManagedFieldHolder getManagedFieldHolder(Class<? extends BasicMonitor> clazz) {
        return new ManagedFieldHolder(clazz, BASIC_MONITOR_HOLDER);
    }

    public AbstractInfoProviderMonitor(IMachineBlockEntity holder) {
        super(holder);
        Class<? extends BasicMonitor> clazz = this.getClass();
        MANAGED_FIELD_HOLDER_MAP.computeIfAbsent(clazz, this::getManagedFieldHolder);
        this.subscribeServerTick(() -> {
            if (this.getOffsetTimer() % 10 == 0) {
                this.syncInfoFromServer();
                this.getSyncStorage().markAllDirty();
                this.getHolder().syncNow(false);
            }
        });
    }

    private static final Map<Class<? extends BasicMonitor>, ManagedFieldHolder> MANAGED_FIELD_HOLDER_MAP = new ConcurrentHashMap<>();

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        var holder = MANAGED_FIELD_HOLDER_MAP.get(getClass());
        if (holder == null) {
            throw new IllegalStateException("ManagedFieldHolder not found for " + getClass().getName());
        }
        return holder;
    }

    @Override
    public long getPriority() {
        return priority;
    }

    @Override
    public void setPriority(long priority) {
        this.priority = priority;
        if (getLevel() != null && !getLevel().isClientSide()) {
            this.getHolder().syncNow(false);
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
        LongInputWidget input = new LongInputWidget(Position.of(50, 144),
                this::getPriority, this::setPriority);
        input.setMax((long) Integer.MAX_VALUE).setMin((long) Integer.MIN_VALUE);
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

    public void setDisplayOrderCache(List<ResourceLocation> displayOrderCache) {
        if (getLevel() != null && !getLevel().isClientSide()) {
            this.displayOrderCache = displayOrderCache.toArray(new ResourceLocation[0]);
        }
    }
}
