package com.gtocore.common.machine.multiblock.electric.space.spacestaion.recipe;

import com.gtocore.api.machine.part.ILargeSpaceStationMachine;
import com.gtocore.common.machine.multiblock.electric.space.spacestaion.RecipeExtension;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.data.GTODimensions;
import com.gtolib.api.machine.trait.IEnhancedRecipeLogic;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.modifier.ParallelLogic;
import com.gtolib.utils.holder.ObjectHolder;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.item.capability.ElectricItem;
import com.gregtechceu.gtceu.api.recipe.content.Content;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@DataGeneratorScanned
public class SpaceDroneDock extends RecipeExtension {

    public SpaceDroneDock(MetaMachineBlockEntity metaMachineBlockEntity) {
        super(metaMachineBlockEntity, ILargeSpaceStationMachine.twoWayPositionFunction(41));
    }

    @Override
    @Nullable
    public Recipe fullModifyRecipe(@NotNull Recipe recipe) {
        long maxParallel;
        AtomicBoolean hasInput = new AtomicBoolean(false);
        for (var c : recipe.conditions) {
            if (!c.check(recipe, getRecipeLogic())) {
                if (getRecipeLogic() instanceof IEnhancedRecipeLogic enhancedRecipeLogic) {
                    enhancedRecipeLogic.gtolib$setIdleReason(c.getTooltips());
                }
                return null;
            }
        }
        ObjectHolder<BigInteger> costEU = new ObjectHolder<>(null);
        ObjectHolder<ItemStack> outputHolder = new ObjectHolder<>(null);
        ObjectHolder<ItemStack> inputHolder = new ObjectHolder<>(null);
        Ingredient chargeable = (Ingredient) recipe.inputs.get(ItemRecipeCapability.CAP).getFirst().content;
        fastForEachInputItems((stack, amount) -> {
            if (hasInput.get()) return;
            ItemStack output = stack.copyWithCount(1);
            ItemStack input = stack.copyWithCount(1);
            if (GTCapabilityHelper.getElectricItem(output) instanceof ElectricItem electricItem && chargeable.test(output)) {
                var change = BigInteger.valueOf(electricItem.getCharge());
                if (change.compareTo(BigInteger.ZERO) > 0) {
                    costEU.value = change;
                    electricItem.setCharge(0);
                    inputHolder.value = input;
                    outputHolder.value = output;
                    hasInput.set(true);
                }
            }
        });
        if (!hasInput.get() || costEU.value == null || costEU.value.compareTo(BigInteger.ZERO) <= 0) {
            ((IEnhancedRecipeLogic) getRecipeLogic()).gtolib$setIdleReason(Component.translatable(DRONE_NO_ENERGY));
            return null;
        }

        List<Content> newInput = new ArrayList<>(recipe.inputs.get(ItemRecipeCapability.CAP));
        // ObjectList<Content> newOutput = new ArrayList<>(recipe.outputs.get(ItemRecipeCapability.CAP));
        newInput.removeFirst();
        recipe.inputs.put(ItemRecipeCapability.CAP, newInput);
        // recipe.outputs.put(ItemRecipeCapability.CAP, newOutput);

        maxParallel = Math.max(1, costEU.value.divide(BigInteger.valueOf(600_000)).longValue());
        // "0.1 + 6.384 / (1.632 + (消耗的电量(单位：GEU))) ^ 4"
        double base = (1.632 + costEU.value.doubleValue() / 1_000_000_000);
        base = base * base;
        recipe.duration = (int) (recipe.duration * (0.1 + 6.384 / base / base));
        recipe = ParallelLogic.accurateParallel(this, recipe, maxParallel);

        inputItem(inputHolder.value);
        outputItem(outputHolder.value);

        return recipe;
    }

    @Override
    public void customText(@NotNull List<Component> list) {
        super.customText(list);
        if (getLevel() == null) return;
        var galaxy = GTODimensions.getGalaxy(getLevel().dimension().location());
        if (galaxy == null) {
            list.add(Component.translatable(NOT_IN_SPACETIME_DOMAIN));
            return;
        }
        list.add(Component.translatable(CURRENT_GALAXY, Component.translatable("gtolib.galaxy.name." + galaxy.name())));
    }

    @RegisterLanguage(cn = "当前空间站所在星系：%s", en = "Current Space Station Galaxy: %s")
    public static final String CURRENT_GALAXY = "gtocore.machine.space_drone_dock.current_galaxy";
    @RegisterLanguage(cn = "当前空间站不在时空域中！", en = "The current space station is not in the spacetime domain!")
    public static final String NOT_IN_SPACETIME_DOMAIN = "gtocore.machine.space_drone_dock.not_in_spacetime_domain";
    @RegisterLanguage(cn = "无人机内没有电，无法出发！", en = "The drone has no power and cannot set off!")
    public static final String DRONE_NO_ENERGY = "gtocore.machine.space_drone_dock.drone_no_energy";
}
