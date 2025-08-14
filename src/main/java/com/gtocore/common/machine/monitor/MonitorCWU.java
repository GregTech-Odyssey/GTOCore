package com.gtocore.common.machine.monitor;

import com.gtolib.api.machine.trait.WirelessComputationContainerTrait;
import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class MonitorCWU extends AbstractInfoProviderMonitor implements ITeamInformationProvider {

    private final WirelessComputationContainerTrait trait;
    @DescSynced
    private long cwtTotal = 0;
    @DescSynced
    private long cwtUnused = 0;
    @DescSynced
    boolean hasContainer = false;

    public MonitorCWU(Object o) {
        this((MetaMachineBlockEntity) o);
    }

    public MonitorCWU(MetaMachineBlockEntity holder) {
        super(holder);
        trait = new WirelessComputationContainerTrait(this, false); // 我是靶仓
    }

    @Override
    @Nullable
    public UUID getUUID() {
        return trait.getUUID();
    }

    @Override
    public DisplayComponentList provideInformation() {
        var infoList = ITeamInformationProvider.super.provideInformation();
        if (hasContainer) {
            long cwtUsed = cwtTotal - cwtUnused;
            infoList.addIfAbsent(
                    DisplayRegistry.COMPUTATION_WORK.id(),
                    Component.translatable("gtocore.machine.monitor.cwu.capacity",
                            cwtUnused, cwtTotal).withStyle(ChatFormatting.GREEN).getVisualOrderText());
            infoList.addIfAbsent(
                    DisplayRegistry.COMPUTATION_WORK_USED.id(),
                    Component.translatable("gtocore.machine.monitor.cwu.used",
                            cwtUsed).withStyle(ChatFormatting.GRAY).getVisualOrderText());
        } else {
            infoList.addIfAbsent(
                    DisplayRegistry.COMPUTATION_WORK_UNAVAILABLE.id(),
                    Component.translatable("gtocore.machine.monitor.cwu.no_container")
                            .withStyle(ChatFormatting.RED).getVisualOrderText());
        }
        return infoList;
    }

    @Override
    public void syncInfoFromServer() {
        var container = trait.getWirelessComputationContainer();
        if (container != null) {
            cwtTotal = MathUtil.saturatedCast(container.getStorage());
            cwtUnused = container.getCache();
            hasContainer = true;
        } else {
            cwtTotal = 0;
            cwtUnused = 0;
            hasContainer = false;
        }
    }

    @Override
    public List<ResourceLocation> getAvailableRLs() {
        var rls = ITeamInformationProvider.super.getAvailableRLs();
        rls.add(DisplayRegistry.COMPUTATION_WORK.id());
        rls.add(DisplayRegistry.COMPUTATION_WORK_USED.id());
        rls.add(DisplayRegistry.COMPUTATION_WORK_UNAVAILABLE.id());
        return rls;
    }
}
