package com.gtocore.common.machine.trait;

import com.gtocore.common.machine.multiblock.electric.voidseries.AdvancedInfiniteDrillMachine;

import com.gtolib.api.machine.impl.DrillingControlCenterMachine;
import com.gtolib.api.machine.trait.IFluidDrillLogic;
import com.gtolib.api.recipe.RecipeBuilder;

import com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid.BedrockFluidVeinSavedData;
import com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid.FluidVeinWorldEntry;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;

import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.material.Fluid;

import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class AdvancedInfiniteDrillLogic extends RecipeLogic implements IFluidDrillLogic {

    private static final int MAX_PROGRESS = 20;
    private final Reference2IntOpenHashMap<Fluid> veinFluids = new Reference2IntOpenHashMap<>();
    @Setter
    @Getter
    private int range;
    private DrillingControlCenterMachine cache;

    public AdvancedInfiniteDrillLogic(IRecipeLogicMachine machine, int range) {
        super(machine);
        this.range = range;
    }

    @Override
    public AdvancedInfiniteDrillMachine getMachine() {
        return (AdvancedInfiniteDrillMachine) super.getMachine();
    }

    @Override
    public boolean findAndHandleRecipe() {
        if (getMachine().getLevel() instanceof ServerLevel serverLevel) {
            lastRecipe = null;
            var data = BedrockFluidVeinSavedData.getOrCreate(serverLevel);
            if (veinFluids.isEmpty()) {
                getGridFluid(data);
                if (veinFluids.isEmpty()) {
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
        if (getMachine().isEmpty() || !getMachine().canRunnable()) return null;
        if (!veinFluids.isEmpty()) {
            var builder = RecipeBuilder.ofRaw().duration(MAX_PROGRESS).EUt(20000);
            veinFluids.reference2IntEntrySet().fastForEach(e -> builder.outputFluids(e.getKey(), e.getIntValue()));
            var recipe = builder.buildRawRecipe();
            recipe.modifier(getParallel() * efficiency(getMachine().getRate() * 500), true);
            recipe = RecipeModifier.overclocking(getMachine(), RecipeHandlerUnit.NO_DATA, recipe);
            return recipe;
        }
        return null;
    }

    private long getParallel() {
        AdvancedInfiniteDrillMachine drill = getMachine();
        var currentHeat = drill.getCurrentHeat();
        var heat = drill.getRate();
        var efficiency = efficiency(currentHeat);
        var produced = (long) efficiency * heat;
        var machine = getNetMachine();
        if (machine != null) produced = (long) (produced * machine.getMultiplier());
        return produced;
    }

    /**
     * 温度倍率计算
     *
     * @param heat 当前温度
     * @return 倍率
     */
    private static int efficiency(int heat) {
        if (heat < 6000) {
            return 2;
        } else if (heat < 8000) {
            return 4;
        } else {
            return 8;
        }
    }

    private static int getFluidToProduce(FluidVeinWorldEntry entry) {
        var definition = entry.getDefinition();
        if (definition != null) {
            int depletedYield = definition.getDepletedYield();
            int regularYield = entry.getFluidYield();
            int remainingOperations = entry.getOperationsRemaining();
            return Math.max(depletedYield, regularYield * remainingOperations / BedrockFluidVeinSavedData.MAXIMUM_VEIN_OPERATIONS);
        }
        return 0;
    }

    private void getGridFluid(BedrockFluidVeinSavedData data) {
        int x = getChunkX();
        int z = getChunkZ();
        int mid = range / 2;
        for (int i = -mid; i <= mid; i++) {
            for (int j = -mid; j <= mid; j++) {
                var fluid = data.getFluidInChunk(x + i, z + j);
                if (fluid != null) {
                    int produced = getFluidToProduce(data.getFluidVeinWorldEntry(x + i, z + j));
                    if (produced > 0) {
                        int value = veinFluids.getOrDefault(fluid, 0) + produced * 10;
                        veinFluids.put(fluid, value);
                    }
                }
            }
        }
    }

    @NotNull
    public Reference2IntOpenHashMap<Fluid> getVeinFluids() {
        return veinFluids;
    }

    @Override
    public boolean onRecipeFinish() {
        machine.afterWorking();
        if (lastRecipe != null) {
            machine.handleRecipeOutput(lastRecipe);
        }
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
}
