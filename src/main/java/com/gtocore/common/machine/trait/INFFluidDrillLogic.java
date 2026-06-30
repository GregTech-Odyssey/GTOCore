package com.gtocore.common.machine.trait;

import com.gtocore.common.machine.multiblock.electric.voidseries.INFFluidDrillMachine;

import com.gtolib.api.machine.impl.DrillingControlCenterMachine;
import com.gtolib.api.machine.trait.IFluidDrillLogic;
import com.gtolib.api.recipe.RecipeBuilder;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid.BedrockFluidVeinSavedData;
import com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid.FluidVeinWorldEntry;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;

import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

public final class INFFluidDrillLogic extends RecipeLogic implements IFluidDrillLogic {

    public static final int MAX_PROGRESS = 20;
    private DrillingControlCenterMachine cache;
    @Nullable
    private Fluid veinFluid;
    private static final int parallel = 1;

    public INFFluidDrillLogic(INFFluidDrillMachine machine) {
        super(machine);
    }

    @Override
    public INFFluidDrillMachine getMachine() {
        return (INFFluidDrillMachine) super.getMachine();
    }

    @Override
    public boolean findAndHandleRecipe() {
        if (getMachine().getLevel() instanceof ServerLevel serverLevel) {
            lastRecipe = null;
            var data = BedrockFluidVeinSavedData.getOrCreate(serverLevel);
            if (veinFluid == null) {
                veinFluid = data.getFluidInChunk(getChunkX(), getChunkZ());
                if (veinFluid == null) {
                    return false;
                }
            }
            var match = getFluidDrillRecipe();
            if (match != null) {
                if (machine.matchRecipeOutput(match) && machine.matchTickRecipe(match)) {
                    return setupRecipe(RecipeHandlerUnit.NO_DATA, match);
                }
            }
        }
        return false;
    }

    @Nullable
    private GTRecipe getFluidDrillRecipe() {
        if (getMachine().getLevel() instanceof ServerLevel serverLevel && veinFluid != null) {
            var data = BedrockFluidVeinSavedData.getOrCreate(serverLevel);
            return RecipeBuilder.ofRaw().outputFluids(new FluidStack(veinFluid, getFluidToProduce(data.getFluidVeinWorldEntry(getChunkX(), getChunkZ())))).duration(MAX_PROGRESS).EUt((long) (GTValues.VA[getMachine().getEnergyTier()] * Math.pow(parallel, 1.2))).buildRawRecipe();
        }
        return null;
    }

    public int getFluidToProduce() {
        if (getMachine().getLevel() instanceof ServerLevel serverLevel && veinFluid != null) {
            var data = BedrockFluidVeinSavedData.getOrCreate(serverLevel);
            return getFluidToProduce(data.getFluidVeinWorldEntry(getChunkX(), getChunkZ()));
        }
        return 0;
    }

    private int getFluidToProduce(FluidVeinWorldEntry entry) {
        var definition = entry.getDefinition();
        if (definition != null) {
            int depletedYield = definition.getDepletedYield();
            int regularYield = entry.getFluidYield();
            int remainingOperations = entry.getOperationsRemaining();
            int produced = Math.max(depletedYield, regularYield * remainingOperations / BedrockFluidVeinSavedData.MAXIMUM_VEIN_OPERATIONS);
            produced *= getMachine().getBasis();
            if (isOverclocked()) {
                produced = produced * 3 / 2;
            }
            DrillingControlCenterMachine machine = getNetMachine();
            if (machine != null) produced = (int) (produced * machine.getMultiplier());
            return produced;
        }
        return 0;
    }

    @Override
    public boolean onRecipeFinish() {
        machine.afterWorking();
        if (lastRecipe != null) machine.handleRecipeOutput(lastRecipe);
        if (this.suspendAfterFinish) {
            this.setStatus(SUSPEND);
            this.suspendAfterFinish = false;
        } else {
            var match = getFluidDrillRecipe();
            if (match != null) {
                if (machine.matchRecipeOutput(match) && machine.matchTickRecipe(match)) {
                    return setupRecipe(RecipeHandlerUnit.NO_DATA, match);
                }
            }
            setStatus(IDLE);
        }
        return false;
    }

    private boolean isOverclocked() {
        return getMachine().getEnergyTier() > getMachine().getTier();
    }

    private int getChunkX() {
        return SectionPos.blockToSectionCoord(getMachine().getPos().getX());
    }

    private int getChunkZ() {
        return SectionPos.blockToSectionCoord(getMachine().getPos().getZ());
    }

    @Override
    public DrillingControlCenterMachine getNetMachineCache() {
        return cache;
    }

    @Override
    public void setNetMachineCache(DrillingControlCenterMachine cache) {
        this.cache = cache;
    }

    @Override
    public void onMachineUnLoad() {
        super.onMachineUnLoad();
        this.cache = null;
    }

    @Nullable
    public Fluid getVeinFluid() {
        return this.veinFluid;
    }
}
