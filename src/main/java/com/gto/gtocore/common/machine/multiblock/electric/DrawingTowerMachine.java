package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.api.machine.multiblock.ElectricMultiblockMachine;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.machine.multiblock.part.SpoolHatchPartMachine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class DrawingTowerMachine extends ElectricMultiblockMachine {

    private SpoolHatchPartMachine spoolHatchPartMachine;

    public DrawingTowerMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    public void onPartScan(IMultiPart part) {
        super.onPartScan(part);
        if (spoolHatchPartMachine == null && part instanceof SpoolHatchPartMachine spoolHatchPart) {
            spoolHatchPartMachine = spoolHatchPart;
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        spoolHatchPartMachine = null;
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        if (spoolHatchPartMachine == null || recipe == null) {
            return false;
        }

        CustomItemStackHandler storage = spoolHatchPartMachine.getInventory().storage;
        ItemStack item = storage.getStackInSlot(0);
        int tier = getItemTier(item);

        // Check if the item is a valid spool and matches the required tier in the recipe
        if (tier == recipe.data.getInt("spool")) {
            // Decrease the item count instead of increasing damage
            if (item.getCount() > 1) {
                item.shrink(1); // Reduce the stack size by one
                storage.setStackInSlot(0, item);
            } else {
                storage.setStackInSlot(0, ItemStack.EMPTY); // Remove the item if only one left
            }
            return super.beforeWorking(recipe);
        }
        return false;
    }

    private int getItemTier(ItemStack item) {
        if (item.isEmpty()) {
            return 0;
        }
        return SpoolHatchPartMachine.SPOOL.getOrDefault(item.getItem(), 0);
    }

    // Static block to initialize SPOOL map with extended tiers
    static {
        // Ensure thread-safe initialization
        Map<Item, Integer> spoolMap = new ConcurrentHashMap<>(SpoolHatchPartMachine.SPOOL);
        spoolMap.put(GTOItems.SPOOLS_MICRO.get(), 1);
        spoolMap.put(GTOItems.SPOOLS_SMALL.get(), 2);
        spoolMap.put(GTOItems.SPOOLS_MEDIUM.get(), 3);
        spoolMap.put(GTOItems.SPOOLS_LARGE.get(), 4);
        spoolMap.put(GTOItems.SPOOLS_JUMBO.get(), 5);
        SpoolHatchPartMachine.SPOOL = spoolMap;
    }
}
