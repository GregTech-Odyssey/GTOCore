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
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class DigestionTankMachine extends CoilMultiblockMachine implements IFluidRendererMachine {

    @Getter
    @SyncToClient(notifyUpdate = true, autoUpdate = false)
    private final Set<BlockPos> fluidBlockOffsets = FluidRenderUtils.emptyFluidBlockOffsets();
    @Getter
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
        FluidRenderUtils.loadFluidBlockOffsets(this);
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        FluidRenderUtils.clearFluidBlockOffsets(this);
    }
}
