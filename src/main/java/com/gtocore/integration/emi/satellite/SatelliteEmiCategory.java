package com.gtocore.integration.emi.satellite;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.machines.MultiBlockG;
import com.gtocore.common.machine.multiblock.electric.space.SatelliteControlCenterMachine;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.common.data.GTDimensionMarkers;

import net.minecraft.network.chat.Component;

import com.google.common.collect.ImmutableMap;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;

import java.util.List;

import static com.gregtechceu.gtceu.common.data.GTRecipeCapabilities.FLUID;
import static com.gregtechceu.gtceu.common.data.GTRecipeCapabilities.ITEM;

public class SatelliteEmiCategory extends EmiRecipeCategory {

    static final EmiRecipeCategory CATEGORY = new SatelliteEmiCategory();

    private SatelliteEmiCategory() {
        super(GTOCore.id("satellite"), EmiStack.of(GTOItems.PLANET_DATA_CHIP));
    }

    @Override
    public Component getName() {
        return Component.translatable("gtocore.satellite_control_center.emi.launch_satellite");
    }

    public static void register(EmiRegistry registry) {
        registry.addCategory(CATEGORY);
        registry.addWorkstation(CATEGORY, EmiStack.of(MultiBlockG.SATELLITE_CONTROL_CENTER.asStack()));
        for (var entry : SatelliteControlCenterMachine.getPlanets()) {
            var inputs = ImmutableMap.<RecipeCapability<?>, List<Content>>builder();
            var outputs = ImmutableMap.<RecipeCapability<?>, List<Content>>builder();
            if (SatelliteControlCenterMachine.getRocket(entry.getTier()) == null) {
                continue; // Skip if no rocket is defined for this tier
            }
            var dimMarker = GTRegistries.DIMENSION_MARKERS.getOrDefault(entry.getLocation(), GTDimensionMarkers.OVERWORLD);
            inputs.put(ITEM, List.of(
                    consume(ITEM.of(GTOItems.PLANET_DATA_CHIP)),
                    consume(ITEM.of(GTOItems.PLANET_SCAN_SATELLITE)),
                    consume(ITEM.of(SatelliteControlCenterMachine.getRocket(entry.getTier())))));
            inputs.put(FLUID, List.of(consume(FLUID.of(SatelliteControlCenterMachine.getFuel(entry.getTier())))));
            outputs.put(ITEM, List.of(consume(ITEM.of(dimMarker.getIcon()))));
            registry.addRecipe(SatelliteEmiRecipe.fromInputOutput(GTOCore.id("gtocoresatellite").withSuffix("/launch_satellite/" + entry.getKey()), inputs.build(), outputs.build()));
        }
    }

    private static Content consume(Object ing) {
        return new Content(ing, 10000, 10000, 0);
    }
}
