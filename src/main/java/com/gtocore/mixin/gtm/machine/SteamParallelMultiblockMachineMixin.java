package com.gtocore.mixin.gtm.machine;

import com.gtolib.api.machine.SteamEnergyContainer;
import com.gtolib.api.machine.feature.DummyEnergyMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.SteamHatchPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.steam.SteamParallelMultiblockMachine;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SteamParallelMultiblockMachine.class)
public abstract class SteamParallelMultiblockMachineMixin extends WorkableMultiblockMachine implements DummyEnergyMachine {

    protected SteamParallelMultiblockMachineMixin(MetaMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Shadow(remap = false)
    public abstract double getConversionRate();

    @Unique
    private SteamEnergyContainer gtolib$container;

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void onStructureFormed() {
        super.onStructureFormed();
        for (var part : getParts()) {
            if (part instanceof SteamHatchPartMachine machine) {
                gtolib$container = new SteamEnergyContainer(getConversionRate(), machine.tank);
                return;
            }
        }
    }

    @Override
    public @NotNull IEnergyContainer gtolib$getEnergyContainer() {
        if (gtolib$container == null) return IEnergyContainer.DEFAULT;
        return gtolib$container;
    }
}
