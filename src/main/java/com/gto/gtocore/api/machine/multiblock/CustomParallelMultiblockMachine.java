package com.gto.gtocore.api.machine.multiblock;

import com.gto.gtocore.api.gui.ParallelConfigurator;
import com.gto.gtocore.api.machine.feature.IParallelMachine;
import com.gto.gtocore.api.machine.trait.CustomParallelTrait;

import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class CustomParallelMultiblockMachine extends ElectricMultiblockMachine implements IParallelMachine {

    public static Function<IMachineBlockEntity, CustomParallelMultiblockMachine> createParallel(Function<CustomParallelMultiblockMachine, Integer> parallel, boolean defaultParallel) {
        return holder -> new CustomParallelMultiblockMachine(holder, defaultParallel, parallel);
    }

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            CustomParallelMultiblockMachine.class, ElectricMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Persisted
    private final CustomParallelTrait customParallelTrait;

    protected CustomParallelMultiblockMachine(IMachineBlockEntity holder, boolean defaultParallel, @NotNull Function<CustomParallelMultiblockMachine, Integer> parallel) {
        super(holder);
        customParallelTrait = new CustomParallelTrait(this, defaultParallel, machine -> parallel.apply((CustomParallelMultiblockMachine) machine));
    }

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
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
