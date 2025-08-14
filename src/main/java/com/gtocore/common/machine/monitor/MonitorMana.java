package com.gtocore.common.machine.monitor;

import com.gtolib.api.wireless.WirelessManaContainer;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.PowerSubstationMachine;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;

import java.math.BigInteger;
import java.util.List;

public class MonitorMana extends AbstractInfoProviderMonitor implements ITeamInformationProvider {

    @DescSynced
    private long manaBufferCache = 0L;
    private long manaBufferCache0_5s = 0L;
    @DescSynced
    private long manaBufferCache1s = 0L;

    public MonitorMana(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public void syncInfoFromServer() {
        if (getUUID() != null) {
            manaBufferCache1s = manaBufferCache0_5s;
            manaBufferCache0_5s = manaBufferCache;
            manaBufferCache = WirelessManaContainer.getOrCreateContainer(getUUID()).getStorage().longValue();
        }
    }

    public MonitorMana(Object o) {
        this((MetaMachineBlockEntity) o);
    }

    @Override
    public DisplayComponentList provideInformation() {
        var informationList = ITeamInformationProvider.super.provideInformation();
        if (getUUID() != null) {
            informationList.addIfAbsent(
                    DisplayRegistry.MANA_CURRENT.id(),
                    Component.translatable("gtocore.machine.monitor.mana.current")
                            .withStyle(ChatFormatting.GRAY)
                            .append(Component.literal(String.valueOf(manaBufferCache)).withStyle(ChatFormatting.AQUA))
                            .getVisualOrderText());
            informationList.addIfAbsent(
                    DisplayRegistry.MANA_CURRENT_AS_POOL.id(),
                    Component.translatable("gtocore.machine.monitor.mana.pool.0")
                            .withStyle(ChatFormatting.GRAY)
                            .append(Component.literal(String.format("%.02f", (float) manaBufferCache / ManaPoolBlockEntity.MAX_MANA)).withStyle(ChatFormatting.AQUA))
                            .append(Component.translatable("gtocore.machine.monitor.mana.pool.1").withStyle(ChatFormatting.GRAY))
                            .getVisualOrderText());
            long manaChange = (manaBufferCache - manaBufferCache1s);
            long manaChangeAbs = Math.abs(manaChange);
            if (manaChange >= 0) {
                informationList.addIfAbsent(
                        DisplayRegistry.MANA_CHANGE.id(),
                        Component.translatable("gtocore.machine.monitor.mana.increase",
                                manaChangeAbs).withStyle(ChatFormatting.BLUE)
                                .append(Component.literal(String.valueOf(manaChangeAbs)).withStyle(ChatFormatting.AQUA))
                                .getVisualOrderText());
                informationList.addIfAbsent(
                        DisplayRegistry.MANA_CHANGE_AS_POOL.id(),
                        Component.translatable("gtocore.machine.monitor.mana.pool.0")
                                .withStyle(ChatFormatting.BLUE)
                                .append(Component.literal(String.format("%.02f", (float) manaChangeAbs / ManaPoolBlockEntity.MAX_MANA)).withStyle(ChatFormatting.AQUA))
                                .append(Component.translatable("gtocore.machine.monitor.mana.pool.2").withStyle(ChatFormatting.BLUE))
                                .getVisualOrderText());
            } else {
                informationList.addIfAbsent(
                        DisplayRegistry.MANA_CHANGE.id(),
                        Component.translatable("gtocore.machine.monitor.mana.decrease",
                                manaChangeAbs).withStyle(ChatFormatting.GOLD)
                                .append(Component.literal(String.valueOf(manaChangeAbs)).withStyle(ChatFormatting.AQUA))
                                .getVisualOrderText());
                informationList.addIfAbsent(
                        DisplayRegistry.MANA_CHANGE_AS_POOL.id(),
                        Component.translatable("gtocore.machine.monitor.mana.pool.0")
                                .withStyle(ChatFormatting.BLUE)
                                .append(Component.literal(String.format("%.02f", (float) (manaChangeAbs / ManaPoolBlockEntity.MAX_MANA))).withStyle(ChatFormatting.AQUA))
                                .append(Component.translatable("gtocore.machine.monitor.mana.pool.2").withStyle(ChatFormatting.BLUE))
                                .getVisualOrderText());
                informationList.addIfAbsent(
                        DisplayRegistry.MANA_REMAINING_TIME.id(),
                        Component.translatable("gtceu.multiblock.power_substation.time_to_drain",
                                PowerSubstationMachine.getTimeToFillDrainText(BigInteger.valueOf(manaChangeAbs)))
                                .getVisualOrderText());
            }
            informationList.addIfAbsent(
                    DisplayRegistry.EU_STATUS_BAR.id(),
                    new IDisplayComponent() {

                        @Override
                        public ResourceLocation getId() {
                            return DisplayRegistry.EU_STATUS_BAR.id();
                        }

                        @Override
                        public IDisplayComponent setInformation(Object... information) {
                            return this;
                        }

                        @Override
                        public DisplayType getDisplayType() {
                            return DisplayType.CUSTOM_RENDERER;
                        }

                        @Override
                        public int getVisualWidth() {
                            return 16;
                        }

                        @Override
                        public int getVisualHeight() {
                            return 16;
                        }
                    });
        }
        return informationList;
    }

    @Override
    public List<ResourceLocation> getAvailableRLs() {
        var rls = ITeamInformationProvider.super.getAvailableRLs();
        rls.addAll(List.of(
                DisplayRegistry.MANA_CURRENT.id(),
                DisplayRegistry.MANA_CURRENT_AS_POOL.id(),
                DisplayRegistry.MANA_CHANGE.id(),
                DisplayRegistry.MANA_CHANGE_AS_POOL.id(),
                DisplayRegistry.MANA_REMAINING_TIME.id()));
        return rls;
    }
}
