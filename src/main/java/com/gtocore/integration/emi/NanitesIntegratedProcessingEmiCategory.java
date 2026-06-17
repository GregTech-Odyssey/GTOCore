package com.gtocore.integration.emi;

import com.gtocore.common.data.machines.MultiBlockC;

import com.gtolib.GTOCore;

import net.minecraft.network.chat.Component;

import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;

public final class NanitesIntegratedProcessingEmiCategory extends EmiRecipeCategory {

    public static final NanitesIntegratedProcessingEmiCategory ORE_EXTRACTION_MODULE = new NanitesIntegratedProcessingEmiCategory(
            "ore_extraction_module",
            EmiStack.of(MultiBlockC.ORE_EXTRACTION_MODULE.asStack()),
            Component.translatable(MultiBlockC.ORE_EXTRACTION_MODULE.getDescriptionId()));
    public static final NanitesIntegratedProcessingEmiCategory BIOENGINEERING_MODULE = new NanitesIntegratedProcessingEmiCategory(
            "bioengineering_module",
            EmiStack.of(MultiBlockC.BIOENGINEERING_MODULE.asStack()),
            Component.translatable(MultiBlockC.BIOENGINEERING_MODULE.getDescriptionId()));
    public static final NanitesIntegratedProcessingEmiCategory POLYMER_TWISTING_MODULE = new NanitesIntegratedProcessingEmiCategory(
            "polymer_twisting_module",
            EmiStack.of(MultiBlockC.POLYMER_TWISTING_MODULE.asStack()),
            Component.translatable(MultiBlockC.POLYMER_TWISTING_MODULE.getDescriptionId()));

    private final Component name;

    private NanitesIntegratedProcessingEmiCategory(String path, EmiStack icon, Component moduleName) {
        super(GTOCore.id("nanites_integrated_processing_center/" + path), icon);
        this.name = Component.empty()
                .append(moduleName)
                .append(" - ")
                .append(Component.translatable(MultiBlockC.NANITES_INTEGRATED_PROCESSING_CENTER.getDescriptionId()));
    }

    public static EmiRecipeCategory getCategory(int module) {
        return switch (module) {
            case 1 -> ORE_EXTRACTION_MODULE;
            case 2 -> BIOENGINEERING_MODULE;
            case 3 -> POLYMER_TWISTING_MODULE;
            default -> null;
        };
    }

    public static void registerWorkstations(EmiRegistry registry) {
        var controller = EmiStack.of(MultiBlockC.NANITES_INTEGRATED_PROCESSING_CENTER.asStack());
        registry.addWorkstation(ORE_EXTRACTION_MODULE, controller);
        registry.addWorkstation(ORE_EXTRACTION_MODULE, EmiStack.of(MultiBlockC.ORE_EXTRACTION_MODULE.asStack()));
        registry.addWorkstation(BIOENGINEERING_MODULE, controller);
        registry.addWorkstation(BIOENGINEERING_MODULE, EmiStack.of(MultiBlockC.BIOENGINEERING_MODULE.asStack()));
        registry.addWorkstation(POLYMER_TWISTING_MODULE, controller);
        registry.addWorkstation(POLYMER_TWISTING_MODULE, EmiStack.of(MultiBlockC.POLYMER_TWISTING_MODULE.asStack()));
    }

    @Override
    public Component getName() {
        return name;
    }
}
