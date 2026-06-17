package com.gtocore.common.machine.multiblock.electric;

import com.gtolib.api.machine.impl.part.WirelessEnergyInterfacePartMachine;
import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.recipe.IdleReason;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.api.wireless.ExtendWirelessEnergyContainer;
import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.item.capability.ElectricItem;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.handler.ICustomRecipeLogicHolder;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.energy.IEnergyStorage;

import com.gto.datasynclib.util.holder.ObjHolder;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

public final class EnergyInjectorMachine extends ElectricMultiblockMachine implements ICustomRecipeLogicHolder {

    private WirelessEnergyInterfacePartMachine energyInterfacePartMachine;

    public EnergyInjectorMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public boolean hasOverclockConfig() {
        return false;
    }

    @Override
    public boolean hasBatchConfig() {
        return false;
    }

    @Override
    public void onPartScan(@NotNull IMultiPart part) {
        super.onPartScan(part);
        if (energyInterfacePartMachine == null && part instanceof WirelessEnergyInterfacePartMachine hatchPartMachine) {
            energyInterfacePartMachine = hatchPartMachine;
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        energyInterfacePartMachine = null;
    }

    @Override
    public boolean alwaysSearchRecipe() {
        return true;
    }

    @Override
    public GTRecipeDefinition createCustomRecipe(RecipeHandlerUnit unit) {
        ExtendWirelessEnergyContainer container = null;
        BigInteger storage = null;
        if (energyInterfacePartMachine != null) {
            container = energyInterfacePartMachine.getWirelessEnergyContainer();
            if (container == null) return null;
            storage = container.getStorage();
            if (storage.signum() < 1) return null;
        }
        ObjHolder<BigInteger> eu = new ObjHolder<>(BigInteger.ZERO);
        RecipeBuilder builder = getRecipeBuilder();
        unit.fastForEachItems(true, (stack, amount) -> {
            int count = MathUtil.saturatedCast(amount);
            ItemStack output = stack.copyWithCount(count);
            boolean processed = false;

            if (GTCapabilityHelper.getElectricItem(output) instanceof ElectricItem electricItem && electricItem.getTier() <= getTier()) {
                long chargeNeeded = electricItem.getMaxCharge() - electricItem.getCharge();
                if (chargeNeeded > 0) {
                    // 需要充电
                    var change = BigInteger.valueOf(chargeNeeded).multiply(BigInteger.valueOf(count));
                    eu.value = eu.value.add(change);
                    electricItem.setCharge(electricItem.getMaxCharge());
                    processed = true;
                }
            }

            if (!processed && output.getDamageValue() > 0) {
                eu.value = eu.value.add(BigInteger.valueOf((long) output.getDamageValue() << 7).multiply(BigInteger.valueOf(count)));
                output.setDamageValue(0);
                processed = true;
            }

            if (!processed) {
                IEnergyStorage energyStorage = GTCapabilityHelper.getForgeEnergyItem(output);
                if (energyStorage != null) {
                    int change = (energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored()) * count;
                    if (change > 0) {
                        eu.value = eu.value.add(BigInteger.valueOf((long) Math.ceil((double) change / 64)));
                        energyStorage.receiveEnergy(change, false);
                        processed = true;
                    }
                }
            }

            if (processed) {
                builder.outputItems(output);
                builder.inputItems(stack.getItem(), count);
            }
        });
        if (eu.value.compareTo(BigInteger.ZERO) > 0) {

            if (container != null) {
                if (storage.compareTo(eu.value) < 0) {
                    setIdleReason(IdleReason.NO_EU);
                    return null;
                }
                container.setStorage(storage.subtract(eu.value));
                return builder.duration(1).build();
            } else {
                var voltage = getOverclockVoltage();
                if (voltage <= 0) {
                    setIdleReason(IdleReason.NO_EU);
                    return null;
                }
                return builder.EUt(voltage).duration(Math.max(1, eu.value.divide(BigInteger.valueOf(voltage)).intValue())).build();
            }
        }
        return null;
    }
}
