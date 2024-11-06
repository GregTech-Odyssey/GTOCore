package com.gto.gtocore.common.machine.trait;

import com.gto.gtocore.common.data.GTORecipeModifiers;
import com.gto.gtocore.common.machine.multiblock.electric.INFFluidDrillMachine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid.BedrockFluidVeinSavedData;
import com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid.FluidVeinWorldEntry;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import com.lowdragmc.lowdraglib.side.fluid.FluidHelper;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;

import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.material.Fluid;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public class INFFluidDrillLogic extends RecipeLogic {

    public static final int MAX_PROGRESS = 20;

    @Nullable
    private Fluid veinFluid;
    private int parallel = 1;

    public INFFluidDrillLogic(INFFluidDrillMachine machine) {
        super(machine);
    }

    @Override
    public INFFluidDrillMachine getMachine() {
        return (INFFluidDrillMachine) super.getMachine();
    }

    @Override
    public void findAndHandleRecipe() {
        if (getMachine().getLevel() instanceof ServerLevel serverLevel) {
            lastRecipe = null;
            var data = BedrockFluidVeinSavedData.getOrCreate(serverLevel);
            if (veinFluid == null) {
                this.veinFluid = data.getFluidInChunk(getChunkX(), getChunkZ());
                if (this.veinFluid == null) {
                    if (subscription != null) {
                        subscription.unsubscribe();
                        subscription = null;
                    }
                    return;
                }
            }
            var match = getFluidDrillRecipe();
            if (match != null) {
                if (match.matchRecipe(this.machine).isSuccess() && match.matchTickRecipe(this.machine).isSuccess()) {
                    setupRecipe(match);
                }
            }
        }
    }

    @Nullable
    private GTRecipe getFluidDrillRecipe() {
        if (getMachine().getLevel() instanceof ServerLevel serverLevel && veinFluid != null) {
            var data = BedrockFluidVeinSavedData.getOrCreate(serverLevel);
            var recipe = GTRecipeBuilder.ofRaw()
                    .outputFluids(FluidStack.create(veinFluid, getFluidToProduce(data.getFluidVeinWorldEntry(getChunkX(), getChunkZ()))))
                    .duration(MAX_PROGRESS)
                    .EUt((long) (GTValues.VA[getMachine().getEnergyTier()] * Math.pow(parallel, 1.2)))
                    .buildRawRecipe();
            if (recipe.matchRecipe(getMachine()).isSuccess() && recipe.matchTickRecipe(getMachine()).isSuccess()) {
                return recipe;
            }
        }
        return null;
    }

    public long getFluidToProduce() {
        if (getMachine().getLevel() instanceof ServerLevel serverLevel && veinFluid != null) {
            var data = BedrockFluidVeinSavedData.getOrCreate(serverLevel);
            return getFluidToProduce(data.getFluidVeinWorldEntry(getChunkX(), getChunkZ()));
        }
        return 0;
    }

    private long getFluidToProduce(FluidVeinWorldEntry entry) {
        var definition = entry.getDefinition();
        if (definition != null) {
            int depletedYield = definition.getDepletedYield();
            int regularYield = entry.getFluidYield();
            int remainingOperations = entry.getOperationsRemaining();

            int produced = Math.max(depletedYield,
                    regularYield * remainingOperations / BedrockFluidVeinSavedData.MAXIMUM_VEIN_OPERATIONS);
            produced *= getMachine().getBasis();

            // Overclocks produce 50% more fluid
            if (isOverclocked()) {
                produced = produced * 3 / 2;
            }
            if (produced > 1000) {
                parallel = GTORecipeModifiers.getHatchParallel(getMachine());
                produced *= parallel;
            }
            return produced * FluidHelper.getBucket() / 1000;
        }
        return 0;
    }

    @Override
    public void onRecipeFinish() {
        machine.afterWorking();
        if (lastRecipe != null) {
            lastRecipe.postWorking(this.machine);
            lastRecipe.handleRecipeIO(IO.OUT, this.machine, this.chanceCaches);
        }
        // try it again
        var match = getFluidDrillRecipe();
        if (match != null) {
            if (match.matchRecipe(this.machine).isSuccess() && match.matchTickRecipe(this.machine).isSuccess()) {
                setupRecipe(match);
                return;
            }
        }
        setStatus(Status.IDLE);
        progress = 0;
        duration = 0;
    }

    protected boolean isOverclocked() {
        return getMachine().getEnergyTier() > getMachine().getTier();
    }

    private int getChunkX() {
        return SectionPos.blockToSectionCoord(getMachine().getPos().getX());
    }

    private int getChunkZ() {
        return SectionPos.blockToSectionCoord(getMachine().getPos().getZ());
    }
}
