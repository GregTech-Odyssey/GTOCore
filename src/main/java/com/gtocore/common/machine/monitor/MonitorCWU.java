package com.gtocore.common.machine.monitor;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.capability.IOpticalComputationHatch;
import com.gregtechceu.gtceu.api.capability.IOpticalComputationProvider;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableComputationContainer;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.gto.datasynclib.annotations.SyncToClient;
import org.jetbrains.annotations.Nullable;

import java.text.NumberFormat;
import java.util.List;

public class MonitorCWU extends AbstractInfoProviderMonitor implements IOpticalComputationHatch {

    @SyncToClient
    private long cwtRequestable = 0;
    @SyncToClient
    boolean hasContainer = false;
    private long requestedCWUPerSec;
    @SyncToClient
    private long lastRequestedCWUt;

    protected final NotifiableComputationContainer computationContainer;

    public MonitorCWU(Object o) {
        this((MetaMachineBlockEntity) o);
    }

    public MonitorCWU(MetaMachineBlockEntity holder) {
        super(holder);
        this.computationContainer = new InnerComputationContainer();
    }

    @Override
    public long requestCWU(long cwu, boolean simulate) {
        long requestedCWUt = computationContainer.requestCWU(cwu, simulate);
        if (!simulate) {
            this.requestedCWUPerSec += requestedCWUt;
        }
        return requestedCWUt;
    }

    @Override
    public boolean isTransmitter() {
        return true;
    }

    @Override
    public boolean testCapability(@Nullable Direction side) {
        return false;
    }

    @Override
    public boolean canBridge() {
        return computationContainer.canBridge();
    }

    @Override
    public DisplayComponentList provideInformation() {
        var infoList = super.provideInformation();
        infoList.addIfAbsent(
                DisplayRegistry.COMPUTATION_WORK.id(),
                Component.translatable("gtocore.machine.monitor.cwu.capacity", NumberFormat.getInstance().format(cwtRequestable)).withStyle(ChatFormatting.GREEN).getVisualOrderText());
        infoList.addIfAbsent(
                DisplayRegistry.COMPUTATION_WORK_USED.id(),
                Component.translatable("gtocore.machine.monitor.cwu.used",
                        NumberFormat.getInstance().format(lastRequestedCWUt)).withStyle(ChatFormatting.GRAY).getVisualOrderText());
        return infoList;
    }

    @Override
    public void syncInfoFromServer() {
        cwtRequestable = requestCWU(Long.MAX_VALUE, true);
        hasContainer = true;
        this.lastRequestedCWUt = requestedCWUPerSec / 10;
        this.requestedCWUPerSec = 0;
    }

    @Override
    public List<ResourceLocation> getAvailableRLs() {
        var rls = super.getAvailableRLs();
        rls.add(DisplayRegistry.COMPUTATION_WORK.id());
        rls.add(DisplayRegistry.COMPUTATION_WORK_USED.id());
        return rls;
    }

    private class InnerComputationContainer extends NotifiableComputationContainer {

        InnerComputationContainer() {
            super(MonitorCWU.this, false);
        }

        @Nullable
        @SuppressWarnings("all")
        protected IOpticalComputationProvider getOpticalNetProvider() {
            IOpticalComputationProvider p = null;
            for (Direction direction : Direction.values()) {
                BlockEntity blockEntity = machine.holder.getNeighborBlockEntity(direction);
                if (blockEntity != null) {
                    var cap = GTCapabilityHelper.getComputation(blockEntity, direction.getOpposite());
                    if (cap instanceof IOpticalComputationHatch hatch && hatch.isTransmitter() && hatch != machine) {
                        p = cap;
                        break;
                    } else if (cap != null) {
                        p = cap;
                        break;
                    }
                }
            }
            return p;
        }
    }
}
