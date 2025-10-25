package com.gtocore.data.recipe.generated;

import com.gtocore.common.data.GTORecipeCategories;

import com.gtolib.utils.ItemUtils;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.common.data.GTMaterialItems;

import net.minecraft.world.item.Item;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gtocore.common.data.GTOItems.*;
import static com.gtocore.common.data.GTORecipeTypes.THREE_DIMENSIONAL_PRINTER_RECIPES;

final class GTODisposableToolHandler {

    private record DisposableTool(GTToolType tool, Item mold, Item item, int consume) {}

    private final static List<DisposableTool> toolToMoldMap = List.of(
            new DisposableTool(GTToolType.FILE, DISPOSABLE_FILE_MOLD.get(), DISPOSABLE_FILE.get(), 4),
            new DisposableTool(GTToolType.WRENCH, DISPOSABLE_WRENCH_MOLD.get(), DISPOSABLE_WRENCH.get(), 8),
            new DisposableTool(GTToolType.CROWBAR, DISPOSABLE_CROWBAR_MOLD.get(), DISPOSABLE_CROWBAR.get(), 3),
            new DisposableTool(GTToolType.WIRE_CUTTER, DISPOSABLE_WIRE_CUTTER_MOLD.get(), DISPOSABLE_WIRE_CUTTER.get(), 9),
            new DisposableTool(GTToolType.HARD_HAMMER, DISPOSABLE_HAMMER_MOLD.get(), DISPOSABLE_HAMMER.get(), 6),
            new DisposableTool(GTToolType.SOFT_MALLET, DISPOSABLE_MALLET_MOLD.get(), DISPOSABLE_MALLET.get(), 6),
            new DisposableTool(GTToolType.SCREWDRIVER, DISPOSABLE_SCREWDRIVER_MOLD.get(), DISPOSABLE_SCREWDRIVER.get(), 4),
            new DisposableTool(GTToolType.SAW, DISPOSABLE_SAW_MOLD.get(), DISPOSABLE_SAW.get(), 4));

    public static void run(Material material) {
        if (!material.hasProperty(PropertyKey.TOOL)) {
            return;
        }

        if (!material.hasFluid()) {
            return;
        }

        int durability = material.getProperty(PropertyKey.TOOL).getDurability();
        FluidStack fluidStack = material.getFluid(L / 2);

        for (DisposableTool tool : toolToMoldMap) {
            if (GTMaterialItems.TOOL_ITEMS.get(material, tool.tool) == null) continue;
            THREE_DIMENSIONAL_PRINTER_RECIPES.builder(material.getName() + "_to_" + ItemUtils.getIdLocation(tool.item).getPath())
                    .notConsumable(tool.mold)
                    .inputFluids(fluidStack)
                    .outputItems(tool.item, durability / tool.consume)
                    .EUt(30)
                    .category(GTORecipeCategories.THREE_DIMENSIONAL_PRINTER_RECIPES_DISPOSABLE)
                    .duration((int) material.getMass()).EUt(VA[ULV])
                    .save();
        }
    }
}
