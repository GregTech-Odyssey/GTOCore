package com.gtocore.common.machine.multiblock.water;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.machine.multiblock.FluidRenderUtils;

import com.gtolib.api.machine.feature.multiblock.IFluidRendererMachine;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.utils.NumberUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import com.gto.datasynclib.annotations.SaveToDisk;
import com.gto.datasynclib.annotations.SyncToClient;
import lombok.Getter;

import java.util.List;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class ClarifierPurificationUnitMachine extends WaterPurificationUnitMachine implements IFluidRendererMachine {

    private static final Fluid AIR = GTMaterials.Air.getFluid();

    @SaveToDisk
    private int count;
    @Getter
    @SyncToClient(notifyUpdate = true, autoUpdate = false)
    private final Set<BlockPos> fluidBlockOffsets = FluidRenderUtils.emptyFluidBlockOffsets();
    @Getter
    @SyncToClient
    private Fluid cachedFluid;

    public ClarifierPurificationUnitMachine(MetaMachineBlockEntity holder) {
        super(holder, 1);
    }

    @Override
    public void beforeWorking(RecipeHandlerUnit unit, GTRecipe recipe) {
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

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (count > 100) {
            textList.add(Component.translatable("gtceu.top.maintenance_broken").withStyle(ChatFormatting.YELLOW));
        }
    }

    @Override
    long prepareRecipe(RecipeHandlerUnit unit) {
        eut = 0;
        if (count > 100) {
            if (!simulateOutputItem(GTOItems.SCRAP.asItem(), count / 20)) return 0;
            if (inputFluid(AIR, count * 10000L) && inputFluid(Fluids.WATER, (200L + GTValues.RNG.nextInt(100)) * 1000) && outputItem(GTOItems.SCRAP.asItem(), count / 20)) {
                count = 0;
            } else {
                return 0;
            }
        }
        long inputCount = Math.min(parallel(), unit.getFluidAmount(true, Fluids.WATER)[0]);
        if (inputCount > 0) {
            long outputCount = inputCount * 9 / 10;
            RecipeBuilder builder = getRecipeBuilder();
            builder.duration(WaterPurificationPlantMachine.DURATION).inputFluids(Fluids.WATER, inputCount);
            if (GTValues.RNG.nextInt(100) <= getChance(outputCount / 10)) {
                builder.outputFluids(WaterPurificationPlantMachine.GradePurifiedWater1, outputCount);
            } else {
                builder.outputFluids(Fluids.WATER, outputCount);
            }
            recipe = builder.buildRawRecipe();
            if (matchRecipe(unit, recipe)) {
                count += NumberUtils.chanceOccurrences((int) Math.min(10000, inputCount / 1000), 3, 800);
                calculateVoltage(inputCount);
            }
        }
        return eut;
    }

    private int getChance(long count) {
        if (inputFluid(WaterPurificationPlantMachine.GradePurifiedWater4, count / 16)) {
            return 100;
        } else if (inputFluid(WaterPurificationPlantMachine.GradePurifiedWater3, count / 4)) {
            return 95;
        } else if (inputFluid(WaterPurificationPlantMachine.GradePurifiedWater2, count / 2)) {
            return 90;
        } else if (inputFluid(WaterPurificationPlantMachine.GradePurifiedWater1, count)) {
            return 85;
        }
        return 70;
    }
}
