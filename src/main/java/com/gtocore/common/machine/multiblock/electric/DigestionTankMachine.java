package com.gtocore.common.machine.multiblock.electric;

import com.gtocore.common.machine.multiblock.FluidRenderUtils;

import com.gtolib.api.machine.feature.multiblock.IFluidRendererMachine;
import com.gtolib.api.machine.multiblock.CoilMultiblockMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.material.Fluid;

import com.gto.datasynclib.annotations.SyncToClient;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class DigestionTankMachine extends CoilMultiblockMachine implements IFluidRendererMachine {

    @SyncToClient(notifyUpdate = true, autoUpdate = false)
    private Set<BlockPos> fluidBlockOffsets = FluidRenderUtils.emptyFluidBlockOffsets();
    @SyncToClient
    private Fluid cachedFluid;

    public DigestionTankMachine(MetaMachineBlockEntity holder) {
        super(holder, false, true);
    }

    @Override
    public void beforeWorking(@NotNull RecipeHandlerUnit unit, @NotNull GTRecipe recipe) {
        cachedFluid = IFluidRendererMachine.getFluid(recipe);
        super.beforeWorking(unit, recipe);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        fluidBlockOffsets = FluidRenderUtils.loadFluidBlockOffsets(this);
        FluidRenderUtils.markFluidBlockOffsetsForSync(this);
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        fluidBlockOffsets = FluidRenderUtils.emptyFluidBlockOffsets();
        FluidRenderUtils.markFluidBlockOffsetsForSync(this);
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
