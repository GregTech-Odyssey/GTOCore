package com.gto.gtocore.common.machine.multiblock.water;

import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class WaterPurificationPlantMachine extends WorkableElectricMultiblockMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            WaterPurificationPlantMachine.class, WorkableElectricMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    static final int DURATION = 2400;

    static final Fluid GradePurifiedWater1 = GTOMaterials.GradePurifiedWater1.getFluid();
    static final Fluid GradePurifiedWater2 = GTOMaterials.GradePurifiedWater2.getFluid();
    static final Fluid GradePurifiedWater3 = GTOMaterials.GradePurifiedWater3.getFluid();
    static final Fluid GradePurifiedWater4 = GTOMaterials.GradePurifiedWater4.getFluid();
    static final Fluid GradePurifiedWater5 = GTOMaterials.GradePurifiedWater5.getFluid();
    static final Fluid GradePurifiedWater6 = GTOMaterials.GradePurifiedWater6.getFluid();
    static final Fluid GradePurifiedWater7 = GTOMaterials.GradePurifiedWater7.getFluid();
    static final Fluid GradePurifiedWater8 = GTOMaterials.GradePurifiedWater8.getFluid();

    @Getter
    @Persisted
    private final Set<BlockPos> poss = new HashSet<>();

    private final Map<WaterPurificationUnitMachine, Boolean> waterPurificationUnitMachineMap = new HashMap<>();

    public WaterPurificationPlantMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    private void run() {
        for (Map.Entry<WaterPurificationUnitMachine, Boolean> entry : waterPurificationUnitMachineMap.entrySet()) {
            if (entry.getValue()) {
                entry.getKey().getRecipeLogic().resetRecipeLogic();
                entry.getKey().getRecipeLogic().setupRecipe(entry.getKey().recipe);
            }
        }
    }

    public boolean addWaterPurificationMachine(BlockPos pos, Level level) {
        MetaMachine machine = MetaMachine.getMachine(level, pos);
        if (machine instanceof WaterPurificationUnitMachine waterPurificationMachine && !waterPurificationUnitMachineMap.containsKey(waterPurificationMachine) && waterPurificationMachine.isFormed()) {
            waterPurificationUnitMachineMap.put(waterPurificationMachine, waterPurificationMachine.getRecipeLogic().isWorking());
            return true;
        }
        return false;
    }

    private void update(@Nullable Level level) {
        if (level == null) return;
        waterPurificationUnitMachineMap.clear();
        poss.removeIf(pos -> !addWaterPurificationMachine(pos, level));
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        update(getLevel());
    }

    @Override
    public void onWaiting() {
        for (Map.Entry<WaterPurificationUnitMachine, Boolean> entry : waterPurificationUnitMachineMap.entrySet()) {
            if (entry.getValue()) {
                entry.getKey().getRecipeLogic().resetRecipeLogic();
            }
        }
        super.onWaiting();
    }

    @Override
    public void setWorkingEnabled(boolean isWorkingAllowed) {
        for (Map.Entry<WaterPurificationUnitMachine, Boolean> entry : waterPurificationUnitMachineMap.entrySet()) {
            entry.getKey().setWorking(isWorkingAllowed);
            waterPurificationUnitMachineMap.put(entry.getKey(), isWorkingAllowed);
        }
        super.setWorkingEnabled(isWorkingAllowed);
    }

    @Override
    public int getOutputSignal(@Nullable Direction side) {
        return 15 * DURATION / getRecipeLogic().getProgress();
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        textList.add(Component.translatable("gtocore.machine.water_purification_plant.bind"));
        for (Map.Entry<WaterPurificationUnitMachine, Boolean> entry : waterPurificationUnitMachineMap.entrySet()) {
            MutableComponent component = Component.translatable(entry.getKey().getBlockState().getBlock().getDescriptionId()).append(" ");
            if (entry.getValue()) {
                component.append(Component.translatable("gtceu.multiblock.running").append("\n").append(Component.translatable("gtceu.multiblock.energy_consumption", FormattingUtil.formatNumbers(entry.getKey().eut), Component.literal(GTValues.VNF[GTUtil.getTierByVoltage(entry.getKey().eut)]))));
            } else {
                component.append(Component.translatable("gtceu.multiblock.idling"));
            }
            textList.add(component);
        }
    }

    @Override
    protected RecipeLogic createRecipeLogic(Object... args) {
        return new WaterPurificationPlantRecipesLogic(this);
    }

    @Override
    public WaterPurificationPlantRecipesLogic getRecipeLogic() {
        return (WaterPurificationPlantRecipesLogic) super.getRecipeLogic();
    }

    public class WaterPurificationPlantRecipesLogic extends RecipeLogic {

        public WaterPurificationPlantRecipesLogic(WaterPurificationPlantMachine machine) {
            super(machine);
        }

        @Override
        public WaterPurificationPlantMachine getMachine() {
            return (WaterPurificationPlantMachine) super.getMachine();
        }

        @Override
        public void findAndHandleRecipe() {
            lastRecipe = null;
            GTRecipe match = getRecipe();
            if (match != null) {
                if (match.matchTickRecipe(this.machine).isSuccess()) {
                    setupRecipe(match);
                    run();
                }
            }
        }

        @Nullable
        private GTRecipe getRecipe() {
            if (!machine.hasProxies()) return null;
            update(getLevel());
            if (waterPurificationUnitMachineMap.isEmpty()) return null;
            long eut = 0;
            for (WaterPurificationUnitMachine machine : waterPurificationUnitMachineMap.keySet()) {
                if (machine.isFormed() && machine.getRecipeLogic().isIdle()) {
                    long eu = machine.before();
                    if (eu > 0) {
                        waterPurificationUnitMachineMap.put(machine, true);
                        eut += eu;
                    }
                }
            }
            GTRecipeBuilder builder = GTRecipeBuilder.ofRaw().duration(DURATION);
            if (eut > 0) return builder.EUt(eut).buildRawRecipe();
            return null;
        }

        @Override
        public void onRecipeFinish() {
            machine.afterWorking();
            GTRecipe match = getRecipe();
            if (match != null) {
                if (match.matchTickRecipe(this.machine).isSuccess()) {
                    setupRecipe(match);
                    run();
                    return;
                }
            }
            setStatus(Status.IDLE);
            progress = 0;
            duration = 0;
        }
    }
}
