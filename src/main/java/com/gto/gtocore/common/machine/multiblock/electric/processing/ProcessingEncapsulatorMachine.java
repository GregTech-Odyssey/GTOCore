package com.gto.gtocore.common.machine.multiblock.electric.processing;

import com.gto.gtocore.api.machine.multiblock.CustomParallelMultiblockMachine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import net.minecraft.MethodsReturnNonnullByDefault;

import org.jetbrains.annotations.NotNull;

import java.util.function.ToIntFunction;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class ProcessingEncapsulatorMachine extends CustomParallelMultiblockMachine {

    public ProcessingEncapsulatorMachine(IMachineBlockEntity holder, boolean defaultParallel, @NotNull ToIntFunction<CustomParallelMultiblockMachine> parallel) {
        super(holder, defaultParallel, parallel);
    }
}
