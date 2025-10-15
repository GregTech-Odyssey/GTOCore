package com.gtocore.api.machine;

import appeng.api.implementations.blockentities.ICrankable;
import appeng.capabilities.Capabilities;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.capability.IEnergyInfoProvider;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.api.machine.trait.MachineTrait;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gtolib.GTOCore;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CrankableMachine extends SimpleTieredMachine implements IEnergyInfoProvider {

    public CrankableMachine(MetaMachineBlockEntity holder,int tier, Int2IntFunction tankScalingFunction, Object... args) {
        super(holder,tier, tankScalingFunction, args);
    }
    protected long chargeTicks=0;

    @Override
    public @NotNull List<MachineTrait> getTraits() {
        return super.getTraits().stream()
                .filter(machineTrait->!(machineTrait instanceof IEnergyContainer)).toList();
    }

    @Override
    protected void updateBatterySubscription() {
        if (chargeTicks>0) {
            batterySubs = subscribeServerTick(batterySubs, this::chargeBattery);
        } else if (batterySubs != null) {
            batterySubs.unsubscribe();
            batterySubs = null;
        }
    }

    @Override
    protected void chargeBattery() {
        if(chargeTicks-->0){
            var voltage=energyContainer.getInputVoltage();
            energyContainer.acceptEnergyFromNetwork(this,Direction.UP,voltage,voltage);
        }else{
            updateBatterySubscription();
        }
    }

    @Nullable
    public ICrankable getCrankable(Direction direction) {
        if (direction == Direction.UP) {
            return new Crankable();
        }
        return null;
    }
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, Direction facing) {
        if (Capabilities.CRANKABLE.equals(capability)) {
            var crankable = getCrankable(facing);
            if (crankable == null) {
                return LazyOptional.empty();
            }
            return Capabilities.CRANKABLE.orEmpty(
                    capability,
                    LazyOptional.of(() -> crankable));
        }

        return super.getCapability(capability, facing);
    }

    @Override
    public EnergyInfo getEnergyInfo() {
        return energyContainer.getEnergyInfo();
    }

    @Override
    public long getInputPerSec() {
        return energyContainer.getInputPerSec();
    }

    @Override
    public long getOutputPerSec() {
        return energyContainer.getOutputPerSec();
    }

    @Override
    public boolean supportsBigIntEnergyValues() {
        return false;
    }


    class Crankable implements ICrankable {
        private final NotifiableEnergyContainer container=CrankableMachine.this.energyContainer;
        @Override
        public boolean canTurn() {
            if(container.getEnergyStored()==container.getEnergyCapacity() && GTOCore.isExpert()){
                CrankableMachine.this.doExplosion(tier+1);
                return false;
            }
            return true;
        }

        @Override
        public void applyTurn() {
            CrankableMachine.this.chargeTicks=Math.max(40,CrankableMachine.this.chargeTicks);
            updateBatterySubscription();
        }
    }
}
