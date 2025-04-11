package com.gto.gtocore.integration.jade.provider;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.machine.IIWirelessInteractorMachine;
import com.gto.gtocore.api.machine.feature.IAirScrubberInteractor;

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
    protected MetaMachine getCapability(Level level, BlockPos pos, @Nullable Direction side) {
        MetaMachine machine = MetaMachine.getMachine(level, pos);
        if (machine instanceof IIWirelessInteractorMachine<?> || machine instanceof IAirScrubberInteractor) {
            return machine;
        }
        return null;
    }

    @Override
    protected void write(CompoundTag data, MetaMachine capability) {
        if (capability instanceof IIWirelessInteractorMachine<?> interactorMachine) {
            MetaMachine machine = interactorMachine.getNetMachine();
            if (machine != null) {
                data.putString("pos", Component.translatable(machine.getDefinition().getDescriptionId()).append("[").append(machine.getPos().toShortString()).append("]").getString());
            }
            if (interactorMachine instanceof IAirScrubberInteractor airScrubberInteractor) {
                MetaMachine airScrubber = airScrubberInteractor.getAirScrubberMachine();
                if (airScrubber != null) {
                    data.putString("pos_a", Component.translatable(airScrubber.getDefinition().getDescriptionId()).append("[").append(airScrubber.getPos().toShortString()).append("]").getString());
                }
            }
        }
    }

    @Override
    protected void addTooltip(CompoundTag capData, ITooltip tooltip, Player player, BlockAccessor block, BlockEntity blockEntity, IPluginConfig config) {
        String pos = capData.getString("pos");
        if (!pos.isEmpty()) {
            tooltip.add(Component.translatable("gtmthings.machine.wireless_energy_hatch.tooltip.bind", pos));
        }
        pos = capData.getString("pos_a");
        if (!pos.isEmpty()) {
            tooltip.add(Component.translatable("gtmthings.machine.wireless_energy_hatch.tooltip.bind", pos));
        }
    }
}
