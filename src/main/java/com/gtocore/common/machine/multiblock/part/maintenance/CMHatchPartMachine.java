package com.gtocore.common.machine.multiblock.part.maintenance;

import com.gtolib.api.machine.GTOCleanroomType;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.ICleanroomReceiver;
import com.gregtechceu.gtceu.api.machine.feature.ICleanroomProvider;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IWorkableMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.api.machine.multiblock.DummyCleanroom;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.machine.multiblock.part.AutoMaintenanceHatchPartMachine;

import net.minecraft.MethodsReturnNonnullByDefault;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class CMHatchPartMachine extends AutoMaintenanceHatchPartMachine {

    private static final Set<CleanroomType> CLEANROOM = new ObjectOpenHashSet<>(2, 0.9F);
    private static final Set<CleanroomType> STERILE_CLEANROOM = new ObjectOpenHashSet<>(3, 0.9F);
    private static final Set<CleanroomType> LAW_CLEANROOM = new ObjectOpenHashSet<>(4, 0.9F);

    static {
        CLEANROOM.add(CleanroomType.CLEANROOM);
        STERILE_CLEANROOM.addAll(CLEANROOM);
        STERILE_CLEANROOM.add(CleanroomType.STERILE_CLEANROOM);
        LAW_CLEANROOM.addAll(STERILE_CLEANROOM);
        LAW_CLEANROOM.add(GTOCleanroomType.LAW_CLEANROOM);
    }

    public static final ICleanroomProvider DUMMY_CLEANROOM = DummyCleanroom.createForTypes(CLEANROOM);
    public static final ICleanroomProvider STERILE_DUMMY_CLEANROOM = DummyCleanroom.createForTypes(STERILE_CLEANROOM);
    public static final ICleanroomProvider LAW_DUMMY_CLEANROOM = DummyCleanroom.createForTypes(LAW_CLEANROOM);

    private final ICleanroomProvider cleanroomTypes;

    public CMHatchPartMachine(MetaMachineBlockEntity metaTileEntityId,
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

    @Override
    public GTRecipe modifyRecipe(GTRecipe recipe) {
        return recipe;
    }

    @Override
    public boolean afterWorking(IWorkableMultiController controller) {
        return true;
    }

    public static ImmutableSet<CleanroomType> getCleanroomTypes(ICleanroomProvider p) {
        return ImmutableSet.copyOf(p.getTypes());
    }
}
