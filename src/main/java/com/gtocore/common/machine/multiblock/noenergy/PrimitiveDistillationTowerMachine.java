package com.gtocore.common.machine.multiblock.noenergy;

import com.gtocore.api.machine.part.IHeatContainerPart;
import com.gtocore.api.pattern.GTOPredicates;

import com.gtolib.api.machine.multiblock.NoEnergyMultiblockMachine;
import com.gtolib.api.recipe.IdleReason;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.machine.feature.IDummyEnergyMachine;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDistillationTower;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fluids.capability.IFluidHandler;

import com.lowdragmc.lowdraglib.gui.widget.*;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class PrimitiveDistillationTowerMachine extends NoEnergyMultiblockMachine implements IExplosionMachine, IDummyEnergyMachine, IDistillationTower {

    private static final DummyContainer CONTAINER = new DummyContainer(120);

    @NotNull
    @Getter
    private final List<IFluidHandler> fluidOutputs = new ArrayList<>();

    private IHeatContainerPart heatMachineA;
    private IHeatContainerPart heatMachineB;

    public PrimitiveDistillationTowerMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    private boolean shouldTick() {
        return isFormed && heatMachineA != null && heatMachineB != null;
    }

    @Nullable
    @Override
    protected GTRecipe getRealRecipe(RecipeHandlerUnit unit, GTRecipe recipe) {
        if (heatMachineA != null && heatMachineB != null) {
            var a = heatMachineA.getHeatContainer().getTemperature();
            var b = heatMachineB.getHeatContainer().getTemperature();
            if (b < 400) return null;
            recipe.duration = (int) (recipe.duration * getDurationMultiplier(a, b));
            return recipe;
        }
        setIdleReason(IdleReason.INSUFFICIENT_TEMPERATURE);
        return null;
    }

    @Override
    public boolean handleTickRecipe(GTRecipe recipe) {
        if (heatMachineA == null || heatMachineB == null) return false;
        var a = heatMachineA.getHeatContainer().getTemperature();
        var b = heatMachineB.getHeatContainer().getTemperature();
        if (heatMachineB.getHeatContainer().removeHeatUnrestricted(1, false) == 1) {
            if (getOffsetTimer() % 2 == 0 && a < b - 100) heatMachineA.getHeatContainer().addHeatUnrestricted(1, false);
            return true;
        } else {
            return false;
        }
    }

    private double getDurationMultiplier(double temperatureA, double temperatureB) {
        return temperatureB > 400 ? Math.sqrt(Math.max(1, temperatureA - 350D)) * 800 / (temperatureB * Math.clamp(getRecipeLogic().getTotalContinuousRunningTime() / 1000, 1, 2)) : 1;
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        if (heatMachineA == null || heatMachineB == null) return;
        var a = heatMachineA.getHeatContainer().getTemperature();
        var b = heatMachineB.getHeatContainer().getTemperature();
        textList.add(Component.translatable("gtocore.machine.current_temperature", a + " | " + b));
        textList.add(Component.translatable("gtocore.machine.total_time", getRecipeLogic().getTotalContinuousRunningTime()));
        textList.add(Component.translatable("gtocore.machine.duration_multiplier.tooltip", FormattingUtil.formatNumbers(getDurationMultiplier(a, b))));
    }

    @Override
    public void onPartScan(IMultiPart part) {
        super.onPartScan(part);
        if (part instanceof IHeatContainerPart heatContainerPart) {
            if (getMultiblockState().getMatchContext().getOrDefault(GTOPredicates.DataKeys.A, Collections.emptySet()).contains(heatContainerPart.self().getPos())) {
                this.heatMachineA = heatContainerPart;
            } else {
                this.heatMachineB = heatContainerPart;
            }
        }
    }

    @Override
    public Comparator<IMultiPart> getPartSorter() {
        return Comparator.comparingInt(p -> p.self().getPos().getY());
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        addOutputs();
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        heatMachineA = null;
        heatMachineB = null;
        fluidOutputs.clear();
    }

    @Override
    public IEnergyContainer getEnergyContainer() {
        return CONTAINER;
    }

    @Override
    public boolean jade() {
        return false;
    }

    @Override
    public int getYOffset() {
        return 1;
    }
}
