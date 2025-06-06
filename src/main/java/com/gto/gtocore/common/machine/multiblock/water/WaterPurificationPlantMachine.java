package com.gto.gtocore.common.machine.multiblock.water;

import com.gto.gtocore.api.machine.multiblock.ElectricMultiblockMachine;
import com.gto.gtocore.api.machine.trait.CustomRecipeLogic;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;

import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class WaterPurificationPlantMachine extends ElectricMultiblockMachine {

    public static int time;

    public static BlockPos pos;

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            WaterPurificationPlantMachine.class, ElectricMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    static final int DURATION = 2400;

    static final Fluid GradePurifiedWater1 = GTOMaterials.FilteredSater.getFluid();
    static final Fluid GradePurifiedWater2 = GTOMaterials.OzoneWater.getFluid();
    static final Fluid GradePurifiedWater3 = GTOMaterials.FlocculentWater.getFluid();
    static final Fluid GradePurifiedWater4 = GTOMaterials.PHNeutralWater.getFluid();
    static final Fluid GradePurifiedWater5 = GTOMaterials.ExtremeTemperatureWater.getFluid();
    static final Fluid GradePurifiedWater6 = GTOMaterials.ElectricEquilibriumWater.getFluid();
    static final Fluid GradePurifiedWater7 = GTOMaterials.DegassedWater.getFluid();
    static final Fluid GradePurifiedWater8 = GTOMaterials.BaryonicPerfectionWater.getFluid();

    @Getter
    @Persisted
    private final Set<BlockPos> poss = new ObjectOpenHashSet<>();

    private final Map<WaterPurificationUnitMachine, Boolean> waterPurificationUnitMachineMap = new Object2ObjectOpenHashMap<>();

    public WaterPurificationPlantMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    public boolean addWaterPurificationMachine(BlockPos pos, Level level) {
        MetaMachine machine = getMachine(level, pos);
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
    public void onStructureInvalid() {
        for (Map.Entry<WaterPurificationUnitMachine, Boolean> entry : waterPurificationUnitMachineMap.entrySet()) {
            if (entry.getValue()) {
                entry.getKey().getRecipeLogic().resetRecipeLogic();
            }
        }
        super.onStructureInvalid();
    }

    @Override
    public void onWaiting() {
        for (Map.Entry<WaterPurificationUnitMachine, Boolean> entry : waterPurificationUnitMachineMap.entrySet()) {
            if (entry.getValue()) {
                entry.getKey().getRecipeLogic().setWaiting(null);
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
    public boolean beforeWorking(@Nullable GTRecipe r) {
        for (Map.Entry<WaterPurificationUnitMachine, Boolean> entry : waterPurificationUnitMachineMap.entrySet()) {
            if (entry.getValue()) {
                entry.getKey().getRecipeLogic().resetRecipeLogic();
                entry.getKey().getRecipeLogic().setupRecipe(entry.getKey().recipe);
            }
        }
        return super.beforeWorking(r);
    }

    @Override
    public int getOutputSignal(@Nullable Direction side) {
        return 15 * DURATION / getRecipeLogic().getProgress();
    }

    @Override
    public void addDisplayText(@NotNull List<Component> textList) {
        super.addDisplayText(textList);
        if (!isFormed()) return;
        textList.add(ComponentPanelWidget.withButton(Component.translatable("gui.enderio.range.show"), "show"));
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
    public void handleDisplayClick(String componentData, ClickData clickData) {
        if (clickData.isRemote && "show".equals(componentData)) {
            time = 200;
            pos = getPos();
        }
    }

    @Override
    protected RecipeLogic createRecipeLogic(Object... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }

    @Override
    public CustomRecipeLogic getRecipeLogic() {
        return (CustomRecipeLogic) super.getRecipeLogic();
    }

    @Nullable
    private GTRecipe getRecipe() {
        if (!hasProxies()) return null;
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
        if (eut > 0) {
            GTRecipe recipe = GTORecipeBuilder.ofRaw().duration(DURATION).EUt(eut).buildRawRecipe();
            if (recipe.matchRecipeContents(IO.IN, this, recipe.tickInputs, true).isSuccess()) {
                return recipe;
            }
        }
        return null;
    }
}
