package com.gtocore.common.machine.multiblock.electric;

import com.gtocore.common.machine.multiblock.part.WirelessEnergyInterfacePartMachine;

import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.recipe.IdleReason;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.api.recipe.RecipeRunner;
import com.gtolib.api.wireless.ExtendWirelessEnergyContainer;
import com.gtolib.utils.MathUtil;
import com.gtolib.utils.holder.ObjectHolder;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.item.capability.ElectricItem;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.energy.IEnergyStorage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;

public final class EnergyInjectorMachine extends ElectricMultiblockMachine {

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

    @Nullable
    private Recipe getRecipe() {
        ExtendWirelessEnergyContainer container = null;
        BigInteger storage = null;
        if (energyInterfacePartMachine != null) {
            container = energyInterfacePartMachine.getWirelessEnergyContainer();
            if (container == null) return null;
            storage = container.getStorage();
            if (storage.signum() < 1) return null;
        }
        ObjectHolder<BigInteger> eu = new ObjectHolder<>(BigInteger.ZERO);
        RecipeBuilder builder = getRecipeBuilder();
        fastForEachInputItems((stack, amount) -> {
            int count = MathUtil.saturatedCast(amount);
            ItemStack output = stack.copyWithCount(count);
            if (GTCapabilityHelper.getElectricItem(output) instanceof ElectricItem electricItem && electricItem.getTier() <= getTier()) {
                var change = BigInteger.valueOf(electricItem.getMaxCharge() - electricItem.getCharge()).multiply(BigInteger.valueOf(count));
                if (change.compareTo(BigInteger.ZERO) > 0) {
                    eu.value = eu.value.add(change);
                    electricItem.setCharge(electricItem.getMaxCharge());
                    builder.outputItems(output);
                    builder.inputItems(stack.getItem(), count);
                }
            } else if (output.getDamageValue() > 0) {
                eu.value = eu.value.add(BigInteger.valueOf((long) output.getDamageValue() << 7));
                output.setDamageValue(0);
                builder.outputItems(output);
                builder.inputItems(stack.getItem(), count);
            } else {
                IEnergyStorage energyStorage = GTCapabilityHelper.getForgeEnergyItem(output);
                if (energyStorage != null) {
                    int change = (energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored()) * count;
                    if (change > 0) {
                        eu.value = eu.value.add(BigInteger.valueOf((long) Math.ceil((double) change / 64)));
                        energyStorage.receiveEnergy(change, false);
                        builder.outputItems(output);
                        builder.inputItems(stack.getItem(), count);
                    }
                }
            }
        });
        if (eu.value.compareTo(BigInteger.ZERO) > 0) {
            Recipe recipe;
            if (container != null) {
                if (storage.compareTo(eu.value) < 0) {
                    setIdleReason(IdleReason.NO_EU);
                    return null;
                }
                container.setStorage(storage.subtract(eu.value));
                recipe = builder.duration(1).buildRawRecipe();
            } else {
                recipe = builder.EUt(getOverclockVoltage()).duration(Math.max(1, eu.value.divide(BigInteger.valueOf(getOverclockVoltage())).intValue())).buildRawRecipe();
                if (!RecipeRunner.matchTickRecipe(this, recipe)) return null;
            }
            return recipe;
        }
        return null;
    }

    @Override
    public RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }
}
