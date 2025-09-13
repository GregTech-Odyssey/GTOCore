package com.gtocore.common.machine.multiblock.water;

import com.gtocore.common.data.GTOMaterials;

import com.gtolib.api.capability.IIWirelessInteractor;
import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeRunner;
import com.gtolib.utils.ClientUtil;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;
import com.gregtechceu.gtceu.utils.collection.O2OOpenCacheHashMap;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class WaterPurificationPlantMachine extends ElectricMultiblockMachine {

    static final Map<ResourceLocation, Set<WaterPurificationPlantMachine>> NETWORK = new O2OOpenCacheHashMap<>();

    static final int DURATION = 2400;

    static final Fluid GradePurifiedWater1 = GTOMaterials.FilteredSater.getFluid();
    static final Fluid GradePurifiedWater2 = GTOMaterials.OzoneWater.getFluid();
    static final Fluid GradePurifiedWater3 = GTOMaterials.FlocculentWater.getFluid();
    static final Fluid GradePurifiedWater4 = GTOMaterials.PHNeutralWater.getFluid();
    static final Fluid GradePurifiedWater5 = GTOMaterials.ExtremeTemperatureWater.getFluid();
    static final Fluid GradePurifiedWater6 = GTOMaterials.ElectricEquilibriumWater.getFluid();
    static final Fluid GradePurifiedWater7 = GTOMaterials.DegassedWater.getFluid();
    static final Fluid GradePurifiedWater8 = GTOMaterials.BaryonicPerfectionWater.getFluid();

    long availableEu;

    final Object2BooleanOpenHashMap<WaterPurificationUnitMachine> waterPurificationUnitMachineMap = new Object2BooleanOpenHashMap<>();

    public WaterPurificationPlantMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        IIWirelessInteractor.addToNet(NETWORK, this);
    }

    @Override
    public void onUnload() {
        super.onUnload();
        IIWirelessInteractor.removeFromNet(NETWORK, this);
    }

    @Override
    public void onStructureInvalid() {
        IIWirelessInteractor.removeFromNet(NETWORK, this);
        for (ObjectIterator<Object2BooleanMap.Entry<WaterPurificationUnitMachine>> it = waterPurificationUnitMachineMap.object2BooleanEntrySet().fastIterator(); it.hasNext();) {
            var entry = it.next();
            if (entry.getBooleanValue()) {
                entry.getKey().getRecipeLogic().resetRecipeLogic();
                entry.setValue(false);
            }
        }
        super.onStructureInvalid();
    }

    @Override
    public void onRecipeFinish() {
        super.onRecipeFinish();
        for (ObjectIterator<Object2BooleanMap.Entry<WaterPurificationUnitMachine>> it = waterPurificationUnitMachineMap.object2BooleanEntrySet().fastIterator(); it.hasNext();) {
            var entry = it.next();
            if (entry.getBooleanValue() && entry.getKey().getRecipeLogic().getLastRecipe() != null) {
                entry.getKey().getRecipeLogic().onRecipeFinish();
                entry.getKey().onRecipeFinish();
                entry.setValue(false);
            }
        }
    }

    @Override
    public boolean onWorking() {
        for (ObjectIterator<Object2BooleanMap.Entry<WaterPurificationUnitMachine>> it = waterPurificationUnitMachineMap.object2BooleanEntrySet().fastIterator(); it.hasNext();) {
            var entry = it.next();
            if (entry.getBooleanValue()) {
                entry.getKey().onWorking();
                entry.getKey().getRecipeLogic().setProgress(getRecipeLogic().getProgress());
            }
        }
        return super.onWorking();
    }

    @Override
    public void onWaiting() {
        for (ObjectIterator<Object2BooleanMap.Entry<WaterPurificationUnitMachine>> it = waterPurificationUnitMachineMap.object2BooleanEntrySet().fastIterator(); it.hasNext();) {
            var entry = it.next();
            if (entry.getBooleanValue()) {
                entry.getKey().getRecipeLogic().setWaiting(getEnhancedRecipeLogic().gtolib$getIdleReason());
            }
        }
        super.onWaiting();
    }

    @Override
    public void setWorkingEnabled(boolean isWorkingAllowed) {
        for (ObjectIterator<Object2BooleanMap.Entry<WaterPurificationUnitMachine>> it = waterPurificationUnitMachineMap.object2BooleanEntrySet().fastIterator(); it.hasNext();) {
            var entry = it.next();
            var machine = entry.getKey();
            machine.setWorking(isWorkingAllowed);
            entry.setValue(machine.getRecipeLogic().isWorking());
        }
        super.setWorkingEnabled(isWorkingAllowed);
    }

    @Override
    protected boolean beforeWorking(@Nullable Recipe r) {
        for (ObjectIterator<Object2BooleanMap.Entry<WaterPurificationUnitMachine>> it = waterPurificationUnitMachineMap.object2BooleanEntrySet().fastIterator(); it.hasNext();) {
            var entry = it.next();
            if (entry.getBooleanValue()) {
                entry.getKey().getRecipeLogic().resetRecipeLogic();
                entry.getKey().getRecipeLogic().setupRecipe(entry.getKey().recipe);
            }
        }
        return super.beforeWorking(r);
    }

    @Override
    public int getOutputSignal(@Nullable Direction side) {
        if (getRecipeLogic().getProgress() == 0) return 0;
        return 15 * DURATION / getRecipeLogic().getProgress();
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        textList.add(ComponentPanelWidget.withButton(Component.translatable("gui.enderio.range.show"), "show"));
    }

    @Override
    public void addDisplayText(@NotNull List<Component> textList) {
        super.addDisplayText(textList);
        if (!isFormed()) return;
        textList.add(Component.translatable("gtocore.machine.water_purification_plant.bind"));
        for (ObjectIterator<Object2BooleanMap.Entry<WaterPurificationUnitMachine>> it = waterPurificationUnitMachineMap.object2BooleanEntrySet().fastIterator(); it.hasNext();) {
            var entry = it.next();
            MutableComponent component = Component.translatable(entry.getKey().getBlockState().getBlock().getDescriptionId()).append(" ");
            if (entry.getBooleanValue()) {
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
            ClientUtil.highlighting(getPos(), 32);
        }
    }

    @Override
    public RecipeLogic createRecipeLogic(Object... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }

    @Nullable
    private Recipe getRecipe() {
        long eut = 0;
        if (getEnergyContainer().getEnergyStored() < 1000) return null;
        availableEu = getOverclockVoltage();
        for (ObjectIterator<Object2BooleanMap.Entry<WaterPurificationUnitMachine>> it = waterPurificationUnitMachineMap.object2BooleanEntrySet().fastIterator(); it.hasNext();) {
            var entry = it.next();
            var machine = entry.getKey();
            if (machine.isFormed() && !machine.isInValid()) {
                if (machine.getRecipeLogic().isIdle()) {
                    long eu = machine.before();
                    if (eu > 0) {
                        entry.setValue(true);
                        availableEu -= eu;
                        eut += eu;
                    }
                }
            } else {
                it.remove();
            }
        }
        if (eut > 0) {
            Recipe recipe = getRecipeBuilder().duration(DURATION).EUt(eut).buildRawRecipe();
            if (RecipeRunner.matchTickRecipe(this, recipe)) {
                return recipe;
            }
        }
        return null;
    }
}
