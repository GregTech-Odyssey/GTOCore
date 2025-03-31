package com.gto.gtocore.integration.emi.oreprocessing;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;

import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;

import java.util.ArrayList;
import java.util.List;

public class OreProcessingEmiCategory extends EmiRecipeCategory {

    public static final OreProcessingEmiCategory CATEGORY = new OreProcessingEmiCategory();

    public OreProcessingEmiCategory() {
        super(GTOCore.id("ore_processing_diagram"), EmiStack.of(Items.RAW_IRON));
    }

    public static void registerDisplays(EmiRegistry registry) {
        for (Material mat : GTCEuAPI.materialManager.getRegisteredMaterials()) {
            if (mat.hasProperty(PropertyKey.ORE) && !mat.hasFlag(MaterialFlags.NO_ORE_PROCESSING_TAB)) {
                registry.addRecipe(
                        new OreProcessingEmiRecipe(mat));
            }
        }
    }

    public static void registerWorkStations(EmiRegistry registry) {
        List<MachineDefinition> registeredMachines = new ArrayList<>();
        GTRecipeType[] validTypes = new GTRecipeType[] {
                GTORecipeTypes.CRUSHER_RECIPES,
                GTRecipeTypes.MACERATOR_RECIPES, GTRecipeTypes.ORE_WASHER_RECIPES,
                GTRecipeTypes.THERMAL_CENTRIFUGE_RECIPES, GTRecipeTypes.CENTRIFUGE_RECIPES,
                GTRecipeTypes.CHEMICAL_BATH_RECIPES, GTRecipeTypes.ELECTROMAGNETIC_SEPARATOR_RECIPES,
                GTRecipeTypes.SIFTER_RECIPES
        };
        for (MachineDefinition machine : GTRegistries.MACHINES) {
            if (machine.getRecipeTypes() != null) {
                for (GTRecipeType type : machine.getRecipeTypes()) {
                    for (GTRecipeType validType : validTypes) {
                        if (type == validType && !registeredMachines.contains(machine)) {
                            registry.addWorkstation(CATEGORY, EmiStack.of(machine.asStack()));
                            registeredMachines.add(machine);
                        }
                    }
                }
            }
        }
    }

    @Override
    public Component getName() {
        return Component.translatable("gtceu.jei.ore_processing_diagram");
    }
}
