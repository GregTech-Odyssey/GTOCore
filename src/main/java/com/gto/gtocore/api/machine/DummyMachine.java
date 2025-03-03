package com.gto.gtocore.api.machine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import lombok.Setter;

public final class DummyMachine extends MetaMachine {

    @Setter
    public String id = "";
    public int temp;
    public int manat;
    public long eut;
    public int duration;
    public int circuit;
    public final GTRecipeType recipeType;
    public final CustomItemStackHandler importItems;
    public final CustomItemStackHandler exportItems;
    public final NotifiableFluidTank importFluids;
    public final NotifiableFluidTank exportFluids;

    private DummyMachine(IMachineBlockEntity holder, GTRecipeType type) {
        super(holder);
        recipeType = type;
        importItems = createImportItemHandler();
        exportItems = createExportItemHandler();
        importFluids = createImportFluidHandler();
        exportFluids = createExportFluidHandler();
    }

    public void sett(String s) {
        temp = Integer.parseInt(s);
    }

    public void setMANAt(String s) {
        manat = Integer.parseInt(s);
    }

    public void setEUt(String s) {
        eut = Long.parseLong(s);
    }

    public void setDuration(String s) {
        duration = Integer.parseInt(s);
    }

    public void setCircuit(String s) {
        circuit = Integer.parseInt(s);
    }

    private CustomItemStackHandler createImportItemHandler() {
        return new CustomItemStackHandler(recipeType.getMaxInputs(ItemRecipeCapability.CAP));
    }

    private CustomItemStackHandler createExportItemHandler() {
        return new CustomItemStackHandler(recipeType.getMaxOutputs(ItemRecipeCapability.CAP));
    }

    private NotifiableFluidTank createImportFluidHandler() {
        return new NotifiableFluidTank(this, recipeType.getMaxInputs(FluidRecipeCapability.CAP), Integer.MAX_VALUE, IO.BOTH);
    }

    private NotifiableFluidTank createExportFluidHandler() {
        return new NotifiableFluidTank(this, recipeType.getMaxOutputs(FluidRecipeCapability.CAP), Integer.MAX_VALUE, IO.BOTH);
    }

    public static DummyMachine createDummyMachine(MetaMachine metaMachine) {
        return createDummyMachine(metaMachine.holder.getBlockEntityType(), metaMachine.getPos(), metaMachine.getBlockState(), ((IRecipeLogicMachine) metaMachine).getRecipeType());
    }

    public static DummyMachine createDummyMachine(BlockEntityType<?> type, BlockPos pos, BlockState blockState, GTRecipeType recipeType) {
        return new DummyMachine(MetaMachineBlockEntity.createBlockEntity(type, pos, blockState), recipeType);
    }
}
