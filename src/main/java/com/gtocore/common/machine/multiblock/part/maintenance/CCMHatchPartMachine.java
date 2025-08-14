package com.gtocore.common.machine.multiblock.part.maintenance;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.ICleanroomReceiver;
import com.gregtechceu.gtceu.api.machine.feature.ICleanroomProvider;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;

import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class CCMHatchPartMachine extends ACMHatchPartMachine {

    private final ICleanroomProvider cleanroomTypes;

    public CCMHatchPartMachine(MetaMachineBlockEntity metaTileEntityId,
                               ICleanroomProvider cleanroomTypes) {
        super(metaTileEntityId);
        this.cleanroomTypes = cleanroomTypes;
    }

    @Override
    public void addedToController(IMultiController controller) {
        super.addedToController(controller);
        if (controller instanceof ICleanroomReceiver receiver) {
            receiver.setCleanroom(cleanroomTypes);
        }
    }

    @Override
    public void removedFromController(IMultiController controller) {
        super.removedFromController(controller);
        if (controller instanceof ICleanroomReceiver receiver && receiver.getCleanroom() == cleanroomTypes) {
            receiver.setCleanroom(null);
        }
    }
}
