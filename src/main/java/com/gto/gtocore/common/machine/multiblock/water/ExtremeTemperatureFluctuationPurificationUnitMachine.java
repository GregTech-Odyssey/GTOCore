package com.gto.gtocore.common.machine.multiblock.water;

import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.machine.multiblock.part.SensorPartMachine;
import com.gto.gtocore.utils.MachineUtil;

import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ExtremeTemperatureFluctuationPurificationUnitMachine extends WaterPurificationUnitMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
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
    private long chance = 1;

    @Persisted
    private int inputCount;

    @Persisted
    private boolean cycle;

    private SensorPartMachine sensorMachine;

    public ExtremeTemperatureFluctuationPurificationUnitMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        for (IMultiPart part : getParts()) {
            if (part instanceof SensorPartMachine sensorPartMachine) {
                sensorMachine = sensorPartMachine;
            }
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        sensorMachine = null;
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (getRecipeLogic().isWorking()) {
            textList.add(Component.translatable("gtceu.multiblock.fusion_reactor.heat", heat));
            textList.add(Component.translatable("gtceu.gui.content.chance_1", chance));
        }
    }

    @Override
    public boolean onWorking() {
        boolean result = super.onWorking();
        if (getOffsetTimer() % 20 == 0) {
            long[] a = MachineUtil.getFluidAmount(this, HELIUM_LIQUID, HELIUM_PLASMA);
            long helium_liquid = Math.min(100, a[0]);
            if (MachineUtil.inputFluid(this, FluidStack.create(HELIUM_LIQUID, helium_liquid))) {
                heat = Math.max(4, heat - (int) (helium_liquid * (4 + Math.random() * 2)));
                MachineUtil.outputFluid(this, FluidStack.create(HELIUM, helium_liquid));
            }
            long helium_plasma = Math.min(10, a[1]);
            if (MachineUtil.inputFluid(this, FluidStack.create(HELIUM_PLASMA, helium_plasma))) {
                heat += (int) (helium_plasma * (80 + Math.random() * 40));
                MachineUtil.outputFluid(this, FluidStack.create(HELIUM, helium_plasma));
            }
            if (heat > 125000) {
                heat = 298;
                MachineUtil.outputFluid(this, FluidStack.create(STEAM, inputCount * 9L));
                getRecipeLogic().resetRecipeLogic();
            } else if (heat > 100000) {
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
        return result;
    }

    @Override
    public void afterWorking() {
        super.afterWorking();
        if (Math.random() * 100 <= chance) MachineUtil.outputFluid(this, FluidStack.create(WaterPurificationPlantMachine.GradePurifiedWater5, inputCount * 9L / 10));
    }

    @Override
    long before() {
        eut = 0;
        heat = 298;
        chance = 1;
        cycle = false;
        inputCount = (int) Math.min(Integer.MAX_VALUE, MachineUtil.getFluidAmount(this, WaterPurificationPlantMachine.GradePurifiedWater4)[0]);
        recipe = GTRecipeBuilder.ofRaw().duration(WaterPurificationPlantMachine.DURATION).inputFluids(FluidStack.create(WaterPurificationPlantMachine.GradePurifiedWater4, inputCount)).buildRawRecipe();
        if (recipe.matchRecipe(this).isSuccess()) {
            eut = inputCount * 5L;
        }
        return eut;
    }
}
