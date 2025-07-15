package com.gtocore.common.machine.multiblock.electric;

import com.gtolib.api.machine.feature.multiblock.IFluidRendererMachine;
import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.modifier.RecipeModifierFunction;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.content.Content;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.RequireRerender;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public final class DissolvingTankMachine extends ElectricMultiblockMachine implements IFluidRendererMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(DissolvingTankMachine.class, ElectricMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    @NotNull
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @DescSynced
    @RequireRerender
    private final Set<BlockPos> fluidBlockOffsets = new ObjectOpenHashSet<>();
    @DescSynced
    private Fluid cachedFluid;

    public DissolvingTankMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    protected boolean beforeWorking(@Nullable Recipe recipe) {
        cachedFluid = IFluidRendererMachine.getFluid(recipe);
        return super.beforeWorking(recipe);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        if (!fluidBlockOffsets.isEmpty()) return;
        BlockPos pos = MachineUtils.getOffsetPos(2, 1, getFrontFacing(), getPos());
        for (int i = -1; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    fluidBlockOffsets.add(pos.offset(i, j, k).subtract(getPos()));
                }
            }
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        fluidBlockOffsets.clear();
    }

    @Nullable
    @Override
    protected Recipe getRealRecipe(@NotNull Recipe recipe) {
        List<Content> fluidList = recipe.inputs.getOrDefault(FluidRecipeCapability.CAP, null);
        FluidStack fluidStack1 = FluidRecipeCapability.CAP.of(fluidList.get(0).getContent()).getStacks()[0];
        FluidStack fluidStack2 = FluidRecipeCapability.CAP.of(fluidList.get(1).getContent()).getStacks()[0];
        long[] a = getFluidAmount(fluidStack1.getFluid(), fluidStack2.getFluid());
        if (a[1] > 0) {
            recipe = RecipeModifierFunction.overclocking(this, RecipeModifierFunction.hatchParallel(this, recipe));
            if (recipe != null) {
                if ((double) a[0] / a[1] != ((double) fluidStack1.getAmount()) / fluidStack2.getAmount()) {
                    recipe.outputs.clear();
                }
                return recipe;
            }
        }
        return null;
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
