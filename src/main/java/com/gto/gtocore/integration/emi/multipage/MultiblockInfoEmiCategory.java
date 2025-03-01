package com.gto.gtocore.integration.emi.multipage;

import com.gto.gtocore.config.GTOConfig;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.common.data.machines.GTMultiMachines;

import net.minecraft.network.chat.Component;

import com.google.common.collect.ImmutableSet;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;

public final class MultiblockInfoEmiCategory extends EmiRecipeCategory {

    private static ImmutableSet<MultiblockInfoEmiRecipe> CACHE;

    public static final MultiblockInfoEmiCategory CATEGORY = new MultiblockInfoEmiCategory();

    private MultiblockInfoEmiCategory() {
        super(GTCEu.id("multiblock_info"), EmiStack.of(GTMultiMachines.ELECTRIC_BLAST_FURNACE.getItem()));
    }

    public static void registerDisplays(EmiRegistry registry) {
        if (GTOConfig.INSTANCE.disableMultiBlockPage) return;
        if (CACHE == null) {
            ImmutableSet.Builder<MultiblockInfoEmiRecipe> cacheb = ImmutableSet.builder();
            for (MachineDefinition machine : GTRegistries.MACHINES.values()) {
                if (machine instanceof MultiblockMachineDefinition definition && definition.isRenderXEIPreview()) {
                    MultiblockInfoEmiRecipe recipe = new MultiblockInfoEmiRecipe(definition);
                    cacheb.add(recipe);
                    registry.addRecipe(recipe);
                }
            }
            CACHE = cacheb.build();
        } else {
            CACHE.forEach(registry::addRecipe);
        }
    }

    @Override
    public Component getName() {
        return Component.translatable("gtceu.jei.multiblock_info");
    }
}
