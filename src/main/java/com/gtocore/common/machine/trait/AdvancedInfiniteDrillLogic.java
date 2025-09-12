package com.gtocore.common.machine.trait;

import com.gtocore.common.machine.multiblock.electric.voidseries.AdvancedInfiniteDrillMachine;

import com.gtolib.api.machine.trait.IEnhancedRecipeLogic;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeRunner;

import com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid.BedrockFluidVeinSavedData;
import com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid.FluidVeinWorldEntry;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;

import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class AdvancedInfiniteDrillLogic extends RecipeLogic implements IEnhancedRecipeLogic {

    private static final int MAX_PROGRESS = 20;
    private final Object2IntOpenHashMap<Fluid> veinFluids = new Object2IntOpenHashMap<>();
    private int range;

    public AdvancedInfiniteDrillLogic(IRecipeLogicMachine machine, int range) {
        super(machine);
        this.range = range;
    }

    @Override
    public AdvancedInfiniteDrillMachine getMachine() {
        return (AdvancedInfiniteDrillMachine) super.getMachine();
    }

    @Override
    public void findAndHandleRecipe() {
        if (getMachine().getLevel() instanceof ServerLevel serverLevel) {
            lastRecipe = null;
            var data = BedrockFluidVeinSavedData.getOrCreate(serverLevel);
            if (veinFluids.isEmpty()) {
                getGridFluid(data);
                if (veinFluids.isEmpty()) {
                    if (subscription != null) {
                        subscription.unsubscribe();
                        subscription = null;
                    }
                }
            }
            if (getMachine().isEmpty() || !getMachine().canRunnable()) return;
            var match = getFluidDrillRecipe();
            if (match != null) {
                if (RecipeRunner.matchRecipe(machine, match) && RecipeRunner.matchTickRecipe(machine, match)) {
                    setupRecipe(match);
                }
            }
        }
    }

    @Nullable
    private Recipe getFluidDrillRecipe() {
        if (!veinFluids.isEmpty()) {
            var recipe = gtolib$getRecipeBuilder().duration(MAX_PROGRESS).EUt(20000).outputFluids(veinFluids.object2IntEntrySet().stream().map(entry -> new FluidStack(entry.getKey(), entry.getIntValue())).toArray(FluidStack[]::new)).buildRawRecipe();
            recipe.modifier(new ContentModifier(getParallel(), efficiency(getMachine().getRate() * 500)), true);
            recipe.manat = 0L;
            recipe.cwut = 0L;
            if (RecipeRunner.matchRecipe(machine, recipe) && RecipeRunner.matchTickRecipe(machine, recipe)) {
                return recipe;
            }
        }
        return null;
    }

    private long getParallel() {
        AdvancedInfiniteDrillMachine drill = getMachine();
        var currentHeat = drill.getCurrentHeat();
        var heat = drill.getRate();
        var efficiency = efficiency(currentHeat);
        return (long) efficiency * heat;
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
    public Object2IntOpenHashMap<Fluid> getVeinFluids() {
        return veinFluids;
    }

    @Override
    public void onRecipeFinish() {
        machine.afterWorking();
        if (lastRecipe != null) {
            RecipeRunner.handleRecipeOutput(machine, (Recipe) lastRecipe);
        }
        // try it again
        var match = getFluidDrillRecipe();
        if (match != null) {
            if (RecipeRunner.matchRecipe(machine, match) && RecipeRunner.matchTickRecipe(machine, match)) {
                setupRecipe(match);
                return;
            }
        }
        setStatus(Status.IDLE);
        progress = 0;
        duration = 0;
    }

    private int getChunkX() {
        return SectionPos.blockToSectionCoord(getMachine().getPos().getX());
    }

    private int getChunkZ() {
        return SectionPos.blockToSectionCoord(getMachine().getPos().getZ());
    }

    public void setRange(final int range) {
        this.range = range;
    }

    public int getRange() {
        return this.range;
    }
}
