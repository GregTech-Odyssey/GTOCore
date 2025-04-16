package com.gto.gtocore.data.recipe.generated;

import com.gto.gtocore.utils.ItemUtils;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.common.data.GTMaterialItems;

import net.minecraft.world.item.Item;
import net.minecraftforge.fluids.FluidStack;

import java.util.Map;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gto.gtocore.common.data.GTOItems.*;
import static com.gto.gtocore.common.data.GTORecipeTypes.THREE_DIMENSIONAL_PRINTER_RECIPES;

class GTODisposableToolHandler {

    private record DisposableTool(GTToolType tool, Item item, int consume) {}

    private final static Map<Item, DisposableTool> toolToMoldMap = Map.of(
            DISPOSABLE_FILE.get(), new DisposableTool(GTToolType.FILE, DISPOSABLE_FILE_MOLD.get(), 4),
            DISPOSABLE_WRENCH.get(), new DisposableTool(GTToolType.WRENCH, DISPOSABLE_WRENCH_MOLD.get(), 8),
            DISPOSABLE_CROWBAR.get(), new DisposableTool(GTToolType.CROWBAR, DISPOSABLE_CROWBAR_MOLD.get(), 3),
            DISPOSABLE_WIRE_CUTTER.get(), new DisposableTool(GTToolType.WIRE_CUTTER, DISPOSABLE_WIRE_CUTTER_MOLD.get(), 9),
            DISPOSABLE_HAMMER.get(), new DisposableTool(GTToolType.HARD_HAMMER, DISPOSABLE_HAMMER_MOLD.get(), 6),
            DISPOSABLE_MALLET.get(), new DisposableTool(GTToolType.SOFT_MALLET, DISPOSABLE_MALLET_MOLD.get(), 6),
            DISPOSABLE_SCREWDRIVER.get(), new DisposableTool(GTToolType.SCREWDRIVER, DISPOSABLE_SCREWDRIVER_MOLD.get(), 4),
            DISPOSABLE_SAW.get(), new DisposableTool(GTToolType.SAW, DISPOSABLE_SAW_MOLD.get(), 4));

    static void run(Material material) {
        if (!material.hasProperty(PropertyKey.TOOL)) {
            return;
        }

        if (!material.hasFluid()) {
            return;
        }

        int durability = material.getProperty(PropertyKey.TOOL).getDurability();
        FluidStack fluidStack = material.getFluid(L / 2);

        for (Map.Entry<Item, DisposableTool> entry : toolToMoldMap.entrySet()) {
            if (GTMaterialItems.TOOL_ITEMS.get(material, entry.getValue().tool) == null) continue;
            THREE_DIMENSIONAL_PRINTER_RECIPES.builder(material.getName() + "_to_" + ItemUtils.getIdLocation(entry.getKey()).getPath())
                    .notConsumable(entry.getKey())
                    .inputFluids(fluidStack)
                    .outputItems(entry.getKey(), durability / entry.getValue().consume)
                    .EUt(30)
                    .duration((int) material.getMass()).EUt(VA[ULV])
                    .save();
        }
    }
}
