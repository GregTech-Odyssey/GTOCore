package com.gto.gtocore.api.machine.multiblock;

import com.gto.gtocore.api.gui.ParallelConfigurator;
import com.gto.gtocore.api.machine.feature.multiblock.IParallelMachine;
import com.gto.gtocore.api.machine.trait.CustomParallelTrait;

import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class NoEnergyCustomParallelMultiblockMachine extends NoEnergyMultiblockMachine implements IParallelMachine {

    public static Function<IMachineBlockEntity, NoEnergyCustomParallelMultiblockMachine> createParallel(Function<NoEnergyCustomParallelMultiblockMachine, Integer> parallel, boolean defaultParallel) {
        return holder -> new NoEnergyCustomParallelMultiblockMachine(holder, defaultParallel, parallel);
    }

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            NoEnergyCustomParallelMultiblockMachine.class, NoEnergyMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Persisted
    private final CustomParallelTrait customParallelTrait;

    protected NoEnergyCustomParallelMultiblockMachine(IMachineBlockEntity holder, boolean defaultParallel, @NotNull Function<NoEnergyCustomParallelMultiblockMachine, Integer> parallel) {
        super(holder);
        customParallelTrait = new CustomParallelTrait(this, defaultParallel, machine -> parallel.apply((NoEnergyCustomParallelMultiblockMachine) machine));
    }

    @Override
    public void attachConfigurators(@NotNull ConfiguratorPanel configuratorPanel) {
        super.attachConfigurators(configuratorPanel);
        configuratorPanel.attachConfigurators(new ParallelConfigurator(this));
    }

    @Override
    public int getMaxParallel() {
        return customParallelTrait.getMaxParallel();
    }

    @Override
    public int getParallel() {
        return customParallelTrait.getParallel();
    }

    @Override
    public void setParallel(int number) {
        customParallelTrait.setParallel(number);
    }
}
