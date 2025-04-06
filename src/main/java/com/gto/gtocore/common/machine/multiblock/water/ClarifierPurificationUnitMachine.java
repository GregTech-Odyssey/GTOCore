package com.gto.gtocore.common.machine.multiblock.water;

import com.gto.gtocore.api.machine.feature.multiblock.IFluidRendererMachine;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.api.recipe.RecipeRunnerHelper;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.utils.MachineUtils;
import com.gto.gtocore.utils.NumberUtils;

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
import net.minecraftforge.fluids.FluidStack;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.annotation.RequireRerender;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lombok.Getter;

import java.util.List;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class ClarifierPurificationUnitMachine extends WaterPurificationUnitMachine implements IFluidRendererMachine {

    private static final Fluid AIR = GTMaterials.Air.getFluid();

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            ClarifierPurificationUnitMachine.class, WaterPurificationUnitMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Persisted
    private int count;

    @Getter
    @DescSynced
    @RequireRerender
    private final Set<BlockPos> fluidBlockOffsets = new ObjectOpenHashSet<>();

    public ClarifierPurificationUnitMachine(IMachineBlockEntity holder, Object... args) {
        super(holder);
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
    public void customText(List<Component> textList) {
        super.customText(textList);
        if (count > 100) {
            textList.add(Component.translatable("gtceu.top.maintenance_broken").withStyle(ChatFormatting.YELLOW));
        }
    }

    @Override
    long before() {
        eut = 0;
        if (count > 100) {
            if (MachineUtils.inputFluid(this, AIR, count * 10000) && MachineUtils.inputFluid(this, Fluids.WATER, (200 + GTValues.RNG.nextInt(100)) * 1000) && MachineUtils.outputItem(this, GTOItems.SCRAP.asStack(count / 20))) {
                count = 0;
            } else {
                return 0;
            }
        }
        int inputCount = Math.min(getParallel(), MachineUtils.getFluidAmount(this, Fluids.WATER)[0]);
        int outputCount = inputCount * 9 / 10;
        GTORecipeBuilder builder = GTORecipeBuilder.ofRaw();
        builder.duration(WaterPurificationPlantMachine.DURATION).inputFluids(new FluidStack(Fluids.WATER, inputCount));
        if (GTValues.RNG.nextInt(100) <= getChance(outputCount / 10)) {
            builder.outputFluids(new FluidStack(WaterPurificationPlantMachine.GradePurifiedWater1, outputCount));
        } else {
            builder.outputFluids(new FluidStack(Fluids.WATER, outputCount));
        }
        recipe = builder.buildRawRecipe();
        if (RecipeRunnerHelper.matchRecipe(this, recipe)) {
            count += NumberUtils.chanceOccurrences(Math.min(10000, inputCount / 1000), 3, 800);
            eut = inputCount / 2;
        }
        return eut;
    }

    private int getChance(int count) {
        if (MachineUtils.inputFluid(this, WaterPurificationPlantMachine.GradePurifiedWater4, count / 16)) {
            return 100;
        } else if (MachineUtils.inputFluid(this, WaterPurificationPlantMachine.GradePurifiedWater3, count / 4)) {
            return 95;
        } else if (MachineUtils.inputFluid(this, WaterPurificationPlantMachine.GradePurifiedWater2, count / 2)) {
            return 90;
        } else if (MachineUtils.inputFluid(this, WaterPurificationPlantMachine.GradePurifiedWater1, count)) {
            return 85;
        }
        return 70;
    }
}
