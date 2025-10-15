package com.gtocore.api.machine;

import com.gtocore.data.IdleReason;

import com.gtolib.api.machine.SimpleNoEnergyMachine;
import com.gtolib.api.machine.feature.DummyEnergyMachine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import appeng.api.implementations.blockentities.ICrankable;
import appeng.capabilities.Capabilities;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CrankableMachine extends SimpleNoEnergyMachine implements DummyEnergyMachine {

    private final DummyContainer energyContainer;

    public CrankableMachine(MetaMachineBlockEntity holder, int tier, Int2IntFunction tankScalingFunction, Object... args) {
        super(holder, tier, tankScalingFunction, args);
        energyContainer = new DummyContainer(GTValues.V[tier]);
    }

    protected long chargeTicks = 0;

    @Override
    protected @NotNull RecipeLogic createRecipeLogic() {
        return new RecipeLogic(this) {

            @Override
            public void handleRecipeWorking() {
                if (chargeTicks-- > 0) {
                    setStatus(RecipeLogic.Status.WORKING);
                    progress++;
                    totalContinuousRunningTime++;
                } else {
                    setWaiting(IdleReason.NO_CRANK.reason());
                }
            }
        };
    }

    @Nullable
    public ICrankable getCrankable(Direction direction) {
        if (direction == Direction.UP) {
            return new Crankable();
        }
        return null;
    }

    @Override
    @NotNull
    public IEnergyContainer gtolib$getEnergyContainer() {
        return energyContainer;
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

    class Crankable implements ICrankable {

        @Override
        public boolean canTurn() {
            return getRecipeLogic().isWorking() || chargeTicks != 40;
        }

        @Override
        public void applyTurn() {
            chargeTicks = Math.max(chargeTicks, 20);
        }
    }
}
