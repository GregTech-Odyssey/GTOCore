package com.gtocore.integration.ae;

import appeng.api.config.Actionable;
import appeng.api.networking.IGrid;
import appeng.api.networking.crafting.ICraftingPlan;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.KeyCounter;
import appeng.crafting.inv.ListCraftingInventory;
import org.jetbrains.annotations.NotNull;

public class CraftingCpuHelperExtended {

    public static @NotNull KeyCounter tryExtractInitialItemsIgnoreMissing(ICraftingPlan plan, @NotNull IGrid grid,
                                                                          ListCraftingInventory cpuInventory, IActionSource src) {
        var storage = grid.getStorageService().getInventory();
        KeyCounter missings = new KeyCounter();

        for (var entry : plan.usedItems()) {
            var what = entry.getKey();
            var toExtract = entry.getLongValue();
            var extracted = storage.extract(what, toExtract, Actionable.MODULATE, src);
            cpuInventory.insert(what, extracted, Actionable.MODULATE);

            if (extracted < toExtract) {
                missings.add(what, toExtract - extracted);
            }
        }
        for (var entry : plan.missingItems()) {
            missings.add(entry.getKey(), entry.getLongValue());
        }

        return missings;
    }
}
