package com.gtocore.common.machine.mana.part;

import com.gtocore.common.machine.mana.multiblock.PulseMachineMaintenancePedestal;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredPartMachine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.mana.ManaReceiver;

import static com.gregtechceu.gtceu.api.GTValues.LV;

public class PulseMachineMaintenanceCore extends TieredPartMachine implements ManaReceiver {

    @Setter
    @Getter
    @Nullable
    private PulseMachineMaintenancePedestal pedestal;

    public PulseMachineMaintenanceCore(MetaMachineBlockEntity holder) {
        super(holder, LV);
    }

    @Override
    public Level getManaReceiverLevel() {
        return getLevel();
    }

    @Override
    public BlockPos getManaReceiverPos() {
        return getPos();
    }

    @Override
    public int getCurrentMana() {
        return 0;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public void receiveMana(int mana) {
        if (mana >= 240 && pedestal != null) {
            pedestal.resolveRandomProblem();
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (pedestal != null) {
            pedestal = null;
        }
    }

    @Override
    public @Nullable <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == BotaniaForgeCapabilities.MANA_RECEIVER) {
            return LazyOptional.of(() -> this).cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public boolean canReceiveManaFromBursts() {
        return true;
    }
}
