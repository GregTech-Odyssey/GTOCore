package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.api.machine.multiblock.ElectricMultiblockMachine;
import com.gto.gtocore.api.machine.trait.CustomRecipeLogic;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.common.machine.multiblock.part.ae.MECraftPatternPartMachine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.utils.GTMath;
import com.gregtechceu.gtceu.utils.ItemStackHashStrategy;

import net.minecraft.world.item.ItemStack;

import it.unimi.dsi.fastutil.objects.Object2LongOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SuperMolecularAssemblerMachine extends ElectricMultiblockMachine {

    private final List<MECraftPatternPartMachine> partMachines = new ObjectArrayList<>();

    public SuperMolecularAssemblerMachine(IMachineBlockEntity holder) {
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

    public void onStructureInvalid() {
        super.onStructureInvalid();
        partMachines.clear();
    }

    private GTRecipe getRecipe() {
        Object2LongOpenCustomHashMap<ItemStack> map = new Object2LongOpenCustomHashMap<>(ItemStackHashStrategy.comparingAllButCount());
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
        long maxEUt = getOverclockVoltage();
        double d = (double) totalEu / maxEUt;
        GTORecipeBuilder builder = GTORecipeBuilder.ofRaw().EUt(d >= 5 ? maxEUt : Math.max(1, (long) (maxEUt * d / 5))).duration((int) Math.max(d, 5));
        for (var entry : map.object2LongEntrySet()) {
            var item = entry.getKey();
            item.setCount(GTMath.saturatedCast(entry.getLongValue()));
            builder.outputItems(item);
        }
        return builder.buildRawRecipe();
    }

    @Override
    protected @NotNull RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }
}
