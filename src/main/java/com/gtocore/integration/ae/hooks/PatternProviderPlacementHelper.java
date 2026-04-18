package com.gtocore.integration.ae.hooks;

import com.gtolib.api.blockentity.IDirectionCacheBlockEntity;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

import org.jetbrains.annotations.Nullable;

public final class PatternProviderPlacementHelper {

    private PatternProviderPlacementHelper() {}

    public static void configureAdjacentMachine(@Nullable BlockEntity blockEntity) {
        if (!(blockEntity instanceof IExtendedPatternContainer.IPPPC provider)) {
            return;
        }
        configureAdjacentMachine(provider);
    }

    public static void configureAdjacentMachine(@Nullable IExtendedPatternContainer.IPPPC provider) {
        if (provider == null) {
            return;
        }

        var cache = IDirectionCacheBlockEntity.getBlockEntityDirectionCache(provider.gto$getBlockEntity());
        if (cache == null) {
            return;
        }

        SimpleTieredMachine machine = null;
        Direction providerToMachine = null;

        for (Direction direction : provider.gto$getPushDirection()) {
            var adjacent = cache.getAdjacentBlockEntity(provider.gto$getLevel(), provider.gto$getBlockPos(), direction);
            if (!(adjacent instanceof MetaMachineBlockEntity machineBlockEntity)) {
                continue;
            }
            if (!(machineBlockEntity.getMetaMachine() instanceof SimpleTieredMachine simpleTieredMachine)) {
                continue;
            }
            if (machine != null && machine != simpleTieredMachine) {
                return;
            }
            machine = simpleTieredMachine;
            providerToMachine = direction;
        }

        if (machine == null || providerToMachine == null) {
            return;
        }

        Direction machineOutputSide = providerToMachine.getOpposite();
        machine.setOutputFacingItems(machineOutputSide);
        machine.setOutputFacingFluids(machineOutputSide);
        machine.setAutoOutputItems(true);
        machine.setAutoOutputFluids(true);
        machine.setAllowInputFromOutputSideItems(true);
        machine.setAllowInputFromOutputSideFluids(true);
    }
}
