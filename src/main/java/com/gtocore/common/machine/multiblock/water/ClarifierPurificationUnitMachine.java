package com.gtocore.common.machine.multiblock.water;

import com.gtocore.common.data.GTOItems;

import com.gtolib.api.machine.feature.multiblock.IFluidRendererMachine;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.api.recipe.RecipeRunner;
import com.gtolib.utils.MachineUtils;
import com.gtolib.utils.NumberUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.annotation.RequireRerender;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class ClarifierPurificationUnitMachine extends WaterPurificationUnitMachine implements IFluidRendererMachine {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ClarifierPurificationUnitMachine.class);
    private static final Fluid AIR = GTMaterials.Air.getFluid();
    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(ClarifierPurificationUnitMachine.class, WaterPurificationUnitMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Persisted
    private int count;
    @DescSynced
    @RequireRerender
    private final Set<BlockPos> fluidBlockOffsets = new ObjectOpenHashSet<>();
    @DescSynced
    private Fluid cachedFluid;

    public ClarifierPurificationUnitMachine(IMachineBlockEntity holder) {
        super(holder, 1);
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
        BlockPos pos = getPos();
        Direction facing = getFrontFacing();
        for (int i = 0; i < 2; i++) {
            for (int j = 1; j < 7; j++) {
                fluidBlockOffsets.add(MachineUtils.getOffsetPos(j, i, facing, pos).subtract(pos));
            }
            for (int j = -4; j < 5; j++) {
                if (j == 0) continue;
                fluidBlockOffsets.add(MachineUtils.getOffsetPos(1, i, j, facing, pos).subtract(pos));
            }
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        fluidBlockOffsets.clear();
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (count > 100) {
            textList.add(Component.translatable("gtceu.top.maintenance_broken").withStyle(ChatFormatting.YELLOW));
        }
    }

    @Override
    long before() {
        eut = 0;
        if (count > 100) {
            if (inputFluid(AIR, count * 10000L) && inputFluid(Fluids.WATER, (200L + GTValues.RNG.nextInt(100)) * 1000) && outputItem(GTOItems.SCRAP.asStack(count / 20))) {
                count = 0;
            } else {
                return 0;
            }
        }
        long inputCount = Math.min(parallel(), getFluidAmount(Fluids.WATER)[0]);
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
            if (RecipeRunner.matchRecipe(this, recipe)) {
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

    @Override
    public Set<BlockPos> getFluidBlockOffsets() {
        return this.fluidBlockOffsets;
    }

    @Override
    public Fluid getCachedFluid() {
        return this.cachedFluid;
    }
}
