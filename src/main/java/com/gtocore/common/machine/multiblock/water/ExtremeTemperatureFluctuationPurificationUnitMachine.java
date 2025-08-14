package com.gtocore.common.machine.multiblock.water;

import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.machine.multiblock.part.SensorPartMachine;

import com.gtolib.api.recipe.RecipeRunner;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class ExtremeTemperatureFluctuationPurificationUnitMachine extends WaterPurificationUnitMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            ExtremeTemperatureFluctuationPurificationUnitMachine.class, WaterPurificationUnitMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private static final Fluid STEAM = GTOMaterials.SupercriticalSteam.getFluid();
    private static final Fluid HELIUM = GTMaterials.Helium.getFluid();
    private static final Fluid HELIUM_LIQUID = GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID);
    private static final Fluid HELIUM_PLASMA = GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA);

    @Persisted
    private int heat = 298;

    @Persisted
    private int chance = 1;

    @Persisted
    private long inputCount;

    @Persisted
    private boolean cycle;

    private SensorPartMachine sensorMachine;

    public ExtremeTemperatureFluctuationPurificationUnitMachine(MetaMachineBlockEntity holder) {
        super(holder, 16);
    }

    @Override
    public void onPartScan(IMultiPart part) {
        super.onPartScan(part);
        if (sensorMachine == null && part instanceof SensorPartMachine sensorPartMachine) {
            sensorMachine = sensorPartMachine;
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        sensorMachine = null;
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        if (getRecipeLogic().isWorking()) {
            textList.add(Component.translatable("gtceu.multiblock.fusion_reactor.heat", heat));
            textList.add(Component.translatable("gui.enderio.sag_mill_chance", chance));
        }
    }

    @Override
    public boolean onWorking() {
        if (!super.onWorking()) return false;
        if (getOffsetTimer() % 20 == 0) {
            long[] a = getFluidAmount(HELIUM_LIQUID, HELIUM_PLASMA);
            int helium_liquid = (int) Math.min(100, a[0]);
            if (inputFluid(HELIUM_LIQUID, helium_liquid)) {
                heat = Math.max(4, heat - (int) (helium_liquid * (4 + Math.random() * 2)));
                outputFluid(HELIUM, helium_liquid);
            }
            int helium_plasma = (int) Math.min(10, a[1]);
            if (inputFluid(HELIUM_PLASMA, helium_plasma)) {
                heat += (int) (helium_plasma * (80 + Math.random() * 40));
                outputFluid(HELIUM, helium_plasma);
            }
            if (heat > 12500) {
                heat = 298;
                outputFluid(STEAM, inputCount * 9);
                return false;
            } else if (heat > 10000) {
                cycle = true;
            }
            if (cycle && heat < 10) {
                cycle = false;
                chance += 33;
            }
            if (sensorMachine != null) {
                sensorMachine.update(heat);
            }
        }
        return true;
    }

    @Override
    public void onRecipeFinish() {
        super.onRecipeFinish();
        if (Math.random() * 100 <= chance) outputFluid(WaterPurificationPlantMachine.GradePurifiedWater5, inputCount * 9 / 10);
    }

    @Override
    long before() {
        eut = 0;
        heat = 298;
        chance = 1;
        cycle = false;
        inputCount = Math.min(parallel(), getFluidAmount(WaterPurificationPlantMachine.GradePurifiedWater4)[0]);
        if (inputCount > 0) {
            recipe = getRecipeBuilder().duration(WaterPurificationPlantMachine.DURATION).inputFluids(WaterPurificationPlantMachine.GradePurifiedWater4, inputCount).buildRawRecipe();
            if (RecipeRunner.matchRecipe(this, recipe)) {
                calculateVoltage(inputCount);
            }
        }
        return eut;
    }
}
