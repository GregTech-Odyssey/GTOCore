package com.gtocore.common.machine.multiblock.electric;

import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.machine.multiblock.part.SensorPartMachine;

import com.gtolib.api.machine.feature.multiblock.IParallelMachine;
import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.modifier.ParallelLogic;
import com.gtolib.utils.FunctionContainer;
import com.gtolib.utils.MachineUtils;
import com.gtolib.utils.MathUtil;
import com.gtolib.utils.explosion.SphereExplosion;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class FissionReactorMachine extends ElectricMultiblockMachine implements IExplosionMachine, IParallelMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            FissionReactorMachine.class, WorkableMultiblockMachine.MANAGED_FIELD_HOLDER);

    private static final Fluid DistilledWater = GTMaterials.DistilledWater.getFluid();
    private static final Fluid Steam = GTMaterials.Steam.getFluid();
    private static final Fluid SodiumPotassium = GTMaterials.SodiumPotassium.getFluid();
    private static final Fluid HotSodiumPotassium = GTOMaterials.HotSodiumPotassium.getFluid();
    private static final Fluid SupercriticalSodiumPotassium = GTOMaterials.SupercriticalSodiumPotassium.getFluid();

    @Persisted
    private int heat = 298;
    @Persisted
    private int damaged;
    @Persisted
    private int parallel;
    @Persisted
    private int recipeHeat;
    private int fuel, cooler, heatAdjacent = 1, coolerAdjacent;

    private final ConditionalSubscriptionHandler HeatSubs;

    private SensorPartMachine sensorMachine;

    public FissionReactorMachine(MetaMachineBlockEntity holder) {
        super(holder);
        HeatSubs = new ConditionalSubscriptionHandler(this, this::HeatUpdate, () -> isFormed() || heat > 298);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void onPartScan(IMultiPart part) {
        super.onPartScan(part);
        if (sensorMachine == null && part instanceof SensorPartMachine sensorPartMachine) {
            sensorMachine = sensorPartMachine;
        }
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        fuel = 0;
        cooler = 0;
        heatAdjacent = 1;
        coolerAdjacent = 0;
        FunctionContainer<int[], ?> container = getMultiblockState().getMatchContext().get("fissionComponent");
        if (container != null) {
            fuel = container.getValue()[0];
            cooler = container.getValue()[1];
            heatAdjacent = container.getValue()[2] / 2 + 1;
            coolerAdjacent = container.getValue()[3] / 2;
        }
        HeatSubs.initialize(getLevel());
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        fuel = 0;
        cooler = 0;
        sensorMachine = null;
    }

    @Override
    public void onRecipeFinish() {
        parallel = 0;
        recipeHeat = 0;
    }

    @Override
    public void doExplosion(BlockPos pos, float explosionPower) {
        var machine = self();
        var level = machine.getLevel();
        if (level != null) {
            level.removeBlock(machine.getPos(), false);
            SphereExplosion.explosion(pos, level, (int) Math.sqrt(explosionPower), false, true);
        }
    }

    private void HeatUpdate() {
        if (getOffsetTimer() % 20 == 0) {
            HeatSubs.updateSubscription();
            if (getRecipeLogic().isWorking()) {
                boolean isCooler = false;
                int required = recipeHeat * parallel * heat / 1500;
                if (required > 0) {
                    long[] a = getFluidAmount(DistilledWater, SodiumPotassium);
                    int capacity = (int) Math.min(Math.max(a[0] / 800, a[1] / 20), (cooler - (coolerAdjacent / 3L)) << 3);
                    if (capacity - required >= 0) {
                        if (inputFluid(DistilledWater, capacity)) {
                            isCooler = outputFluid(Steam, (int) (capacity * 800 * (heat > 373 ? 160 : 160 / Math.pow(1.4, 373 - heat))));
                        } else if (inputFluid(SodiumPotassium, capacity)) {
                            if (heat > 825) {
                                isCooler = outputFluid(SupercriticalSodiumPotassium, capacity * 20L);
                            } else isCooler = outputFluid(HotSodiumPotassium, capacity * 20L);
                        }
                    }
                    if (isCooler) {
                        int progress = getProgress() + 20 * capacity / required;
                        int surplusProgress = progress - getMaxProgress();
                        if (surplusProgress > 0) {
                            if (heat > 298) heat -= surplusProgress / 20;
                        } else {
                            getRecipeLogic().setProgress(progress);
                        }
                    }
                }
                if (!isCooler) heat += recipeHeat * heatAdjacent;
            } else {
                if (heat > 298) {
                    heat--;
                } else if (damaged > 0) {
                    damaged--;
                }
            }
            if (heat > 1500) {
                if (damaged > 99) {
                    doExplosion(MachineUtils.getOffsetPos(4, 4, getFrontFacing(), getPos()), fuel);
                } else {
                    damaged += Math.max(1, heatAdjacent / 6);
                }
            }
            if (sensorMachine != null) {
                sensorMachine.update(heat);
            }
        }
    }

    @Override
    protected @Nullable Recipe getRealRecipe(Recipe recipe) {
        recipe = ParallelLogic.accurateParallel(this, recipe, fuel);
        if (recipe == null) return null;
        parallel = MathUtil.saturatedCast(recipe.parallels);
        recipeHeat = recipe.data.getInt("FRheat");
        return recipe;
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("gtocore.machine.fission_reactor.fuel", fuel, heatAdjacent - 1));
        textList.add(Component.translatable("gtocore.machine.fission_reactor.cooler", cooler, coolerAdjacent));
        textList.add(Component.translatable("gtocore.machine.fission_reactor.heat", heat));
        textList.add(Component.translatable("gtocore.machine.fission_reactor.damaged", damaged).append("%"));
    }

    @Override
    public long getMaxParallel() {
        return fuel;
    }
}
