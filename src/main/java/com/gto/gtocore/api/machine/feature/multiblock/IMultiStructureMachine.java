package com.gto.gtocore.api.machine.feature.multiblock;

import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.MultiblockWorldSavedData;

import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;

import java.util.List;

public interface IMultiStructureMachine {

    List<BlockPattern> getMultiPattern();

    default void updateCheck() {
        if (this instanceof WorkableMultiblockMachine machine && machine.isFormed() && machine.getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().tell(new TickTask(1, () -> {
                if (machine.checkPatternWithLock()) {
                    machine.onStructureFormed();
                    if (machine.getRecipeLogic().getLastRecipe() != null) {
                        machine.getRecipeLogic().markLastRecipeDirty();
                    }
                } else {
                    machine.onStructureInvalid();
                    MultiblockWorldSavedData multiblockWorldSavedData = MultiblockWorldSavedData.getOrCreate(serverLevel);
                    multiblockWorldSavedData.removeMapping(machine.getMultiblockState());
                    multiblockWorldSavedData.addAsyncLogic(machine);
                }
            }));
        }
    }
}
