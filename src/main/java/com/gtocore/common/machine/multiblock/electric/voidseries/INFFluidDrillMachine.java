package com.gtocore.common.machine.multiblock.electric.voidseries;

import com.gtocore.common.machine.trait.INFFluidDrillLogic;

import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.level.material.Fluid;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class INFFluidDrillMachine extends ElectricMultiblockMachine {

    private final int t;
    private final int basis;

    public INFFluidDrillMachine(MetaMachineBlockEntity holder, int tier, int basis) {
        super(holder);
        this.t = tier;
        this.basis = basis;
    }

    @Override
    public RecipeLogic createRecipeLogic(Object... args) {
        return new INFFluidDrillLogic(this);
    }

    @Override
    public INFFluidDrillLogic getRecipeLogic() {
        return (INFFluidDrillLogic) super.getRecipeLogic();
    }

    public int getEnergyTier() {
        return Math.min(t + 1, tier);
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        if (isFormed()) {
            int energyContainer = getEnergyTier();
            long maxVoltage = GTValues.V[energyContainer];
            String voltageName = GTValues.VNF[energyContainer];
            textList.add(Component.translatable("gtceu.multiblock.max_energy_per_tick", maxVoltage, voltageName));
            if (getRecipeLogic().getVeinFluid() != null) {
                // Fluid name
                Fluid drilledFluid = getRecipeLogic().getVeinFluid();
                Component fluidInfo = drilledFluid.getFluidType().getDescription().copy().withStyle(ChatFormatting.GREEN);
                textList.add(Component.translatable("gtceu.multiblock.fluid_rig.drilled_fluid", fluidInfo).withStyle(ChatFormatting.GRAY));
                // Fluid amount
                Component amountInfo = Component.literal(FormattingUtil.formatNumbers(getRecipeLogic().getFluidToProduce() * 20L / INFFluidDrillLogic.MAX_PROGRESS) + " mB/s").withStyle(ChatFormatting.BLUE);
                textList.add(Component.translatable("gtceu.multiblock.fluid_rig.fluid_amount", amountInfo).withStyle(ChatFormatting.GRAY));
            } else {
                Component noFluid = Component.translatable("gtceu.multiblock.fluid_rig.no_fluid_in_area").withStyle(ChatFormatting.RED);
                textList.add(Component.translatable("gtceu.multiblock.fluid_rig.drilled_fluid", noFluid).withStyle(ChatFormatting.GRAY));
            }
        } else {
            Component tooltip = Component.translatable("gtceu.multiblock.invalid_structure.tooltip").withStyle(ChatFormatting.GRAY);
            textList.add(Component.translatable("gtceu.multiblock.invalid_structure").withStyle(Style.EMPTY.withColor(ChatFormatting.RED).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, tooltip))));
        }
    }

    @Override
    public int getTier() {
        return this.t;
    }

    public int getBasis() {
        return this.basis;
    }
}
