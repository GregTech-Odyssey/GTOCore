package com.gtocore.integration.emi.space;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.machines.MultiBlockG;
import com.gtocore.common.machine.multiblock.electric.space.SatelliteControlCenterMachine;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.common.data.GTDimensionMarkers;

import net.minecraft.network.chat.Component;

import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;

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
            if (SatelliteControlCenterMachine.getRocket(entry.getTier()) == null) {
                continue; // Skip if no rocket is defined for this tier
            }
            var dimMarker = GTRegistries.DIMENSION_MARKERS.getOrDefault(entry.getLocation(), GTDimensionMarkers.OVERWORLD);
            registry.addRecipe(SatelliteEmiRecipe.fromInputOutput(GTOCore.id("satellite/launch_satellite/" + entry.getKey()), b -> b.inputItems(SatelliteControlCenterMachine.getRocket(entry.getTier()))
                    .inputFluids(SatelliteControlCenterMachine.getFuel(entry.getTier()))
                    .outputItems(dimMarker.getIcon())));
        }
    }
}
