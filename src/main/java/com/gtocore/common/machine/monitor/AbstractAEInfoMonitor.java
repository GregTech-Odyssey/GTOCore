package com.gtocore.common.machine.monitor;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.feature.IGridConnectedMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.trait.GridNodeHolder;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import appeng.api.networking.IManagedGridNode;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.List;

public abstract class AbstractAEInfoMonitor extends AbstractInfoProviderMonitor implements IGridConnectedMachine {

    private static final ManagedFieldHolder MONITOR_HOLDER = new ManagedFieldHolder(AbstractAEInfoMonitor.class, MetaMachine.MANAGED_FIELD_HOLDER);

    @DescSynced
    @NotNull
    protected State state = State.NO_GRID;

    protected int lastUpdateTime = 0;

    @Persisted
    protected final GridNodeHolder nodeHolder;

    @DescSynced
    protected boolean isOnline;

    public AbstractAEInfoMonitor(IMachineBlockEntity holder) {
        super(holder);
        this.nodeHolder = new GridNodeHolder(this);
    }

    protected ManagedFieldHolder getManagedFieldHolder(Class<? extends BasicMonitor> clazz) {
        return new ManagedFieldHolder(clazz, MONITOR_HOLDER);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        getMainNode().setExposedOnSides(EnumSet.allOf(Direction.class));
    }

    @Override
    public IManagedGridNode getMainNode() {
        return nodeHolder.getMainNode();
    }

    @Override
    public void setOnline(final boolean isOnline) {
        this.isOnline = isOnline;
    }

    @Override
    public boolean isOnline() {
        return this.isOnline;
    }

    @Override
    public DisplayComponentList provideInformation() {
        var infoList = super.provideInformation();

        switch (state) {
            case NO_GRID -> infoList.addIfAbsent(
                    DisplayRegistry.AE_ERROR.id(),
                    Component.translatable("gtocore.machine.monitor.ae.status.no_grid").withStyle(ChatFormatting.RED).getVisualOrderText());
            case NO_CONFIG -> infoList.addIfAbsent(
                    DisplayRegistry.AE_ERROR.id(),
                    Component.translatable("gtocore.machine.monitor.ae.status.no_config").withStyle(ChatFormatting.RED).getVisualOrderText());
        }
        return infoList;
    }

    @Override
    public List<ResourceLocation> getAvailableRLs() {
        var rls = super.getAvailableRLs();
        rls.add(DisplayRegistry.AE_ERROR.id());
        return rls;
    }

    /**
     * 因还需判断是否为NO_CONFIG状态，
     * {@link State}的变更必须在继承类中实现！
     */
    @Override
    public abstract void syncInfoFromServer();

    protected enum State {
        NO_GRID,
        NO_CONFIG,
        NORMAL,
    }
}
