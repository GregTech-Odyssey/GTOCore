package com.gtocore.common.machine.multiblock.electric.gcym;

import com.gtolib.api.machine.feature.multiblock.IFluidRendererMachine;
import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluid;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.RequireRerender;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public final class LargeChemicalBathMachine extends GCYMMultiblockMachine implements IFluidRendererMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(LargeChemicalBathMachine.class, GCYMMultiblockMachine.MANAGED_FIELD_HOLDER);
    @DescSynced
    @RequireRerender
    private final Set<BlockPos> fluidBlockOffsets = new ObjectOpenHashSet<>();
    @DescSynced
    private Fluid cachedFluid;

    public LargeChemicalBathMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    protected boolean beforeWorking(@Nullable Recipe recipe) {
        cachedFluid = IFluidRendererMachine.getFluid(recipe);
        return super.beforeWorking(recipe);
    }

    @Override
    @NotNull
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        saveOffsets();
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        fluidBlockOffsets.clear();
    }

    private void saveOffsets() {
        Direction up = RelativeDirection.UP.getRelative(getFrontFacing(), getUpwardsFacing(), isFlipped());
        Direction back = getFrontFacing().getOpposite();
        Direction clockWise;
        Direction counterClockWise;
        if (up == Direction.UP || up == Direction.DOWN) {
            clockWise = getFrontFacing().getClockWise();
            counterClockWise = getFrontFacing().getCounterClockWise();
        } else {
            clockWise = Direction.UP;
            counterClockWise = Direction.DOWN;
        }
        BlockPos pos = getPos();
        BlockPos center = pos.relative(up);
        for (int i = 0; i < 5; i++) {
            center = center.relative(back);
            fluidBlockOffsets.add(center.subtract(pos));
            fluidBlockOffsets.add(center.relative(clockWise).subtract(pos));
            fluidBlockOffsets.add(center.relative(counterClockWise).subtract(pos));
        }
    }

    @Override
    public Set<BlockPos> getFluidBlockOffsets() {
        return this.fluidBlockOffsets;
    }

    @Override
    public Fluid getCachedFluid() {
        return this.cachedFluid;
    }
}
