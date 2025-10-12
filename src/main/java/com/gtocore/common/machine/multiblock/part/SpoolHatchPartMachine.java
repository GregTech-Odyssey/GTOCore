package com.gtocore.common.machine.multiblock.part;

import com.gtocore.common.data.GTOItems;

import com.gtolib.api.machine.part.ItemHatchPartMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IInteractedMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IWorkableMultiController;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.Item;

import com.google.common.collect.ImmutableMap;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class SpoolHatchPartMachine extends ItemHatchPartMachine implements IInteractedMachine {

    public static final Map<Item, Integer> SPOOL;

    static {
        ImmutableMap.Builder<Item, Integer> spoolBuilder = ImmutableMap.builder();
        spoolBuilder.put(GTOItems.SPOOLS_MICRO.get(), 1);
        spoolBuilder.put(GTOItems.SPOOLS_SMALL.get(), 2);
        spoolBuilder.put(GTOItems.SPOOLS_MEDIUM.get(), 3);
        spoolBuilder.put(GTOItems.SPOOLS_LARGE.get(), 4);
        spoolBuilder.put(GTOItems.SPOOLS_JUMBO.get(), 5);
        SPOOL = spoolBuilder.build();
    }

    @Persisted
    private boolean isWorking;

    public SpoolHatchPartMachine(MetaMachineBlockEntity holder) {
        super(holder, 64, i -> SPOOL.containsKey(i.getItem()));
    }

    @Override
    public boolean beforeWorking(IWorkableMultiController controller) {
        isWorking = true;
        return super.beforeWorking(controller);
    }

    @Override
    public boolean afterWorking(IWorkableMultiController controller) {
        isWorking = false;
        return super.afterWorking(controller);
    }

    @Override
    public void onMachineRemoved() {
        if (!isWorking) {
            super.onMachineRemoved();
        }
    }
}
