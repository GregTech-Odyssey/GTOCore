package com.gtocore.common.machine.multiblock.electric;

import com.gtocore.common.machine.multiblock.part.ae.MECraftPatternPartMachine;

import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.api.recipe.ingredient.FastSizedIngredient;
import com.gtolib.utils.ItemUtils;
import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;

import net.minecraft.world.item.ItemStack;

import it.unimi.dsi.fastutil.objects.Object2LongOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SuperMolecularAssemblerMachine extends ElectricMultiblockMachine {

    private final List<MECraftPatternPartMachine> partMachines = new ObjectArrayList<>();

    public SuperMolecularAssemblerMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public void onPartScan(@NotNull IMultiPart part) {
        super.onPartScan(part);
        if (part instanceof MECraftPatternPartMachine machine) {
            partMachines.add(machine);
            machine.setOnContentsChanged(getRecipeLogic()::updateTickSubscription);
        }
    }

    @Override
    public void onStructureFormed() {
        partMachines.clear();
        super.onStructureFormed();
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        partMachines.clear();
    }

    private Recipe getRecipe() {
        long maxEUt = getOverclockVoltage();
        if (maxEUt == 0) return null;
        Object2LongOpenCustomHashMap<ItemStack> map = new Object2LongOpenCustomHashMap<>(ItemUtils.HASH_STRATEGY);
        for (var machine : partMachines) {
            for (var inventory : machine.getInternalInventory()) {
                if (inventory.getAmount() > 0) {
                    map.addTo(inventory.getOutput(), inventory.getAmount());
                    inventory.setAmount(0);
                }
            }
        }
        if (map.isEmpty()) return null;
        long totalEu = map.values().longStream().sum();
        double d = (double) totalEu / maxEUt;
        int limit = gtolib$getOCLimit();
        RecipeBuilder builder = getRecipeBuilder().EUt(Math.max(1, d >= limit ? maxEUt : (long) (maxEUt * d / limit))).duration((int) Math.max(d, limit));
        for (var entry : map.object2LongEntrySet()) {
            var item = entry.getKey();
            item.setCount(MathUtil.saturatedCast(entry.getLongValue()));
            builder.output(ItemRecipeCapability.CAP, FastSizedIngredient.create(entry.getKey(), entry.getLongValue()));
        }
        return builder.buildRawRecipe();
    }

    @Override
    public RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }
}
