package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.api.machine.feature.multiblock.IFluidRendererMachine;
import com.gto.gtocore.api.machine.multiblock.ElectricMultiblockMachine;
import com.gto.gtocore.common.data.GTORecipeModifiers;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;

import net.minecraft.core.BlockPos;
import net.minecraftforge.fluids.FluidStack;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.RequireRerender;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

@Getter
public final class DissolvingTankMachine extends ElectricMultiblockMachine implements IFluidRendererMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            DissolvingTankMachine.class, ElectricMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @DescSynced
    @RequireRerender
    private final Set<BlockPos> fluidBlockOffsets = new ObjectOpenHashSet<>();

    public DissolvingTankMachine(IMachineBlockEntity holder) {
        super(holder);
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
    protected GTRecipe getRealRecipe(@NotNull GTRecipe recipe) {
        List<Content> fluidList = recipe.inputs.getOrDefault(FluidRecipeCapability.CAP, null);
        FluidStack fluidStack1 = FluidRecipeCapability.CAP.of(fluidList.get(0).getContent()).getStacks()[0];
        FluidStack fluidStack2 = FluidRecipeCapability.CAP.of(fluidList.get(1).getContent()).getStacks()[0];
        int[] a = getFluidAmount(fluidStack1.getFluid(), fluidStack2.getFluid());
        if (a[1] > 0) {
            recipe = GTORecipeModifiers.overclocking(this, GTORecipeModifiers.hatchParallel(this, recipe));
            if (recipe != null) {
                if ((double) a[0] / a[1] != ((double) fluidStack1.getAmount()) / fluidStack2.getAmount()) {
                    recipe.outputs.clear();
                }
                return recipe;
            }
        }
        return null;
    }
}
