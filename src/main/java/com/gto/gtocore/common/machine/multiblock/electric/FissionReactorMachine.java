package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.api.machine.multiblock.IParallelMachine;
import com.gto.gtocore.api.pattern.util.IValueContainer;
import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.machine.multiblock.part.SensorPartMachine;
import com.gto.gtocore.utils.MachineUtil;

import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;

import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import com.mojang.datafixers.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FissionReactorMachine extends WorkableElectricMultiblockMachine implements IExplosionMachine, IParallelMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            FissionReactorMachine.class, WorkableMultiblockMachine.MANAGED_FIELD_HOLDER);

    private static final Fluid DistilledWater = GTMaterials.DistilledWater.getFluid();
    private static final Fluid Steam = GTMaterials.Steam.getFluid();
    private static final Fluid SodiumPotassium = GTMaterials.SodiumPotassium.getFluid();
    private static final Fluid HotSodiumPotassium = GTOMaterials.HotSodiumPotassium.getFluid();
    private static final Fluid SupercriticalSodiumPotassium = GTOMaterials.SupercriticalSodiumPotassium.getFluid();

    @Persisted
    private BlockPos centrePos = null;
    @Persisted
    private int heat = 298;
    @Persisted
    private int damaged = 0;
    @Persisted
    private int parallel = 0;
    @Persisted
    private int recipeHeat = 0;
    private int fuel = 0, cooler = 0, heatAdjacent = 1, coolerAdjacent = 0;

    private final ConditionalSubscriptionHandler HeatSubs;

    private SensorPartMachine sensorMachine;

    public FissionReactorMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
        this.HeatSubs = new ConditionalSubscriptionHandler(this, this::HeatUpdate, this::isFormed);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    public static int adjacent(Level level, BlockPos pos, Block block) {
        int a = 0;
        BlockPos[] coordinates = new BlockPos[] { pos.offset(1, 0, 0),
                pos.offset(-1, 0, 0),
                pos.offset(0, 1, 0),
                pos.offset(0, -1, 0),
                pos.offset(0, 0, 1),
                pos.offset(0, 0, -1) };
        for (BlockPos blockPos : coordinates) {
            if (level.getBlockState(blockPos).getBlock() == block) {
                a++;
            }
        }
        return a;
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        Level level = getLevel();
        int heatA = 0;
        int coolerA = 0;
        final BlockPos blockPos = MachineUtil.getOffsetPos(4, 0, getFrontFacing(), getPos());
        centrePos = blockPos.offset(0, 4, 0);
        for (int i = -3; i < 4; i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = -3; k < 4; k++) {
                    BlockPos assemblyPos = blockPos.offset(i, j, k);
                    if (level != null && level.getBlockState(assemblyPos).getBlock() == GTOBlocks.FISSION_FUEL_ASSEMBLY.get()) {
                        heatA += adjacent(level, assemblyPos, GTOBlocks.FISSION_FUEL_ASSEMBLY.get());
                    }
                    if (level != null && level.getBlockState(assemblyPos).getBlock() == GTOBlocks.COOLER.get()) {
                        coolerA += adjacent(level, assemblyPos, GTOBlocks.COOLER.get());
                    }
                }
            }
        }
        heatAdjacent = (heatA / 2) + 1;
        coolerAdjacent = coolerA / 2;
        IValueContainer<?> FuelContainer = getMultiblockState().getMatchContext()
                .getOrCreate("FuelAssemblyValue", IValueContainer::noop);
        if (FuelContainer.getValue() instanceof Integer Fuel) {
            this.fuel = Fuel;
        }
        IValueContainer<?> CoolerContainer = getMultiblockState().getMatchContext()
                .getOrCreate("CoolerValue", IValueContainer::noop);
        if (CoolerContainer.getValue() instanceof Integer Cooler) {
            this.cooler = Cooler;
        }
        for (IMultiPart part : getParts()) {
            if (part instanceof SensorPartMachine sensorPartMachine) {
                sensorMachine = sensorPartMachine;
            }
        }
        HeatSubs.initialize(level);
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        fuel = 0;
        cooler = 0;
        sensorMachine = null;
    }

    @Override
    public void afterWorking() {
        parallel = 0;
        recipeHeat = 0;
        super.afterWorking();
    }

    @Override
    public void doExplosion(BlockPos pos, float explosionPower) {
        var machine = this.self();
        var level = machine.getLevel();
        if (level != null) {
            level.removeBlock(machine.getPos(), false);
            level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    explosionPower, Level.ExplosionInteraction.BLOCK);
        }
    }

    private void HeatUpdate() {
        if (getOffsetTimer() % 20 == 0) {
            if (getRecipeLogic().isWorking()) {
                boolean isCooler = false;
                int required = recipeHeat * parallel * heat / 1500;
                long[] a = MachineUtil.getFluidAmount(this, DistilledWater, SodiumPotassium);
                long capacity = Math.min(Math.max(a[0] / 800, a[1] / 20), (cooler - (coolerAdjacent / 3L)) * 8);
                if (capacity - required >= 0) {
                    if (MachineUtil.inputFluid(this, FluidStack.create(DistilledWater, a[0]))) {
                        isCooler = MachineUtil.outputFluid(this, FluidStack.create(Steam, (long) (capacity * 800 * (heat > 373 ? 160 : 160 / Math.pow(1.4, 373 - heat)))));
                    } else if (MachineUtil.inputFluid(this, FluidStack.create(SodiumPotassium, a[1]))) {
                        if (heat > 825) {
                            isCooler = MachineUtil.outputFluid(this, FluidStack.create(SupercriticalSodiumPotassium, capacity * 20));
                        } else isCooler = MachineUtil.outputFluid(this, FluidStack.create(HotSodiumPotassium, capacity * 20));
                    }
                }
                if (isCooler) {
                    int progress = (int) (getProgress() + 20 * capacity / required);
                    int surplusProgress = progress - getMaxProgress();
                    if (surplusProgress > 0) {
                        if (heat > 298) heat -= surplusProgress / 20;
                    } else {
                        getRecipeLogic().setProgress(progress);
                    }
                } else {
                    heat += recipeHeat * heatAdjacent;
                }
            } else {
                if (heat > 298) {
                    heat--;
                } else if (damaged > 0) {
                    damaged--;
                }
            }
            if (heat > 1500) {
                if (damaged > 99) {
                    doExplosion(centrePos, fuel);
                } else {
                    damaged += Math.max(1, heatAdjacent / 6);
                }
            }
            if (sensorMachine != null) {
                sensorMachine.update(heat);
            }
        }
    }

    @Nullable
    public static GTRecipe recipeModifier(MetaMachine machine, @NotNull GTRecipe recipe) {
        if (machine instanceof FissionReactorMachine fissionReactorMachine) {
            Pair<GTRecipe, Integer> result = GTRecipeModifiers.accurateParallel(machine, recipe,
                    fissionReactorMachine.fuel, false);
            GTRecipe recipe1 = result.getFirst();
            fissionReactorMachine.parallel = result.getSecond();
            fissionReactorMachine.recipeHeat = recipe1.data.getInt("FRheat");
            return recipe1;
        }
        return null;
    }

    @Override
    public void onWaiting() {
        super.onWaiting();
        getRecipeLogic().resetRecipeLogic();
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (isFormed()) {
            textList.add(Component.translatable("gtocore.machine.fission_reactor.fuel", fuel, heatAdjacent - 1));
            textList.add(Component.translatable("gtocore.machine.fission_reactor.cooler", cooler, coolerAdjacent));
            textList.add(Component.translatable("gtocore.machine.fission_reactor.heat", heat));
            textList.add(Component.translatable("gtocore.machine.fission_reactor.damaged", damaged).append("%"));
        }
    }

    @Override
    public int getParallel() {
        return fuel;
    }
}
