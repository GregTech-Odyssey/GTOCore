package com.gtocore.integration.jade.provider;

import com.gtolib.GTOCore;
import com.gtolib.api.capability.IIWirelessInteractor;
import com.gtolib.api.machine.feature.IAirScrubberInteractor;
import com.gtolib.api.machine.mana.feature.IManaMachine;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.integration.jade.provider.CapabilityBlockProvider;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import org.jetbrains.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public final class WirelessInteractorMachineProvider extends CapabilityBlockProvider<MetaMachine> {

    public WirelessInteractorMachineProvider() {
        super(GTOCore.id("wireless_interactor_provider"));
    }

    @Nullable
    @Override
    protected MetaMachine getCapability(Level level, BlockPos pos, BlockEntity blockEntity, @Nullable Direction side) {
        MetaMachine machine = MetaMachine.getMachine(blockEntity);
        if (machine instanceof IManaMachine || machine instanceof IIWirelessInteractor<?> || machine instanceof IAirScrubberInteractor) {
            return machine;
        }
        return null;
    }

    @Override
    protected void write(CompoundTag data, MetaMachine capability) {
        if (capability instanceof IManaMachine manaMachine) {
            MetaMachine machine = manaMachine.getManaContainer().getNetMachine();
            if (machine != null) {
                data.putString("pos", Component.Serializer.toJson(Component.translatable(machine.getDefinition().getDescriptionId()).append("[").append(machine.getPos().toShortString()).append("]")));
            }
        } else if (capability instanceof IIWirelessInteractor<?> interactorMachine) {
            MetaMachine machine = (MetaMachine) interactorMachine.getNetMachine();
            if (machine != null) {
                data.putString("pos", Component.Serializer.toJson(Component.translatable(machine.getDefinition().getDescriptionId()).append("[").append(machine.getPos().toShortString()).append("]")));
            }
            if (interactorMachine instanceof IAirScrubberInteractor airScrubberInteractor) {
                MetaMachine airScrubber = airScrubberInteractor.getAirScrubberMachine();
                if (airScrubber != null) {
                    data.putString("pos_a", Component.Serializer.toJson(Component.translatable(airScrubber.getDefinition().getDescriptionId()).append("[").append(airScrubber.getPos().toShortString()).append("]")));
                }
            }
        }
    }

    @Override
    protected void addTooltip(CompoundTag capData, ITooltip tooltip, Player player, BlockAccessor block, BlockEntity blockEntity, IPluginConfig config) {
        String pos = capData.getString("pos");
        if (!pos.isEmpty()) {
            tooltip.add(Component.translatable("gtmthings.machine.wireless_energy_hatch.tooltip.bind", Component.Serializer.fromJson(pos)));
        }
        pos = capData.getString("pos_a");
        if (!pos.isEmpty()) {
            tooltip.add(Component.translatable("gtmthings.machine.wireless_energy_hatch.tooltip.bind", Component.Serializer.fromJson(pos)));
        }
    }
}
