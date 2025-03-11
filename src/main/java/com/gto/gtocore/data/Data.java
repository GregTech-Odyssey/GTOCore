package com.gto.gtocore.data;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.machine.IMultiblockMachineDefinition;
import com.gto.gtocore.common.data.GTOLoots;
import com.gto.gtocore.common.data.GTORecipeTypes;
import com.gto.gtocore.common.data.GTORecipes;
import com.gto.gtocore.config.GTOConfig;
import com.gto.gtocore.data.recipe.*;
import com.gto.gtocore.data.recipe.classified.ClassifiedRecipe;
import com.gto.gtocore.data.recipe.generated.*;
import com.gto.gtocore.data.recipe.generated.ComponentRecipes;
import com.gto.gtocore.data.recipe.processing.*;
import com.gto.gtocore.integration.emi.GTEMIRecipe;
import com.gto.gtocore.integration.emi.multipage.MultiblockInfoEmiRecipe;
import com.gto.gtocore.utils.RegistriesUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.category.GTRecipeCategory;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.data.pack.GTDynamicDataPack;
import com.gregtechceu.gtceu.data.recipe.MaterialInfoLoader;
import com.gregtechceu.gtceu.data.recipe.configurable.RecipeAddition;
import com.gregtechceu.gtceu.data.recipe.misc.*;
import com.gregtechceu.gtceu.data.recipe.serialized.chemistry.ChemistryRecipes;
import com.gregtechceu.gtceu.integration.emi.recipe.GTRecipeEMICategory;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.level.block.Block;

import com.google.common.collect.ImmutableSet;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.config.EmiConfig;
import dev.emi.emi.config.SidebarSide;
import dev.emi.emi.recipe.special.EmiRepairItemRecipe;
import dev.shadowsoffire.placebo.loot.LootSystem;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.Set;
import java.util.function.Consumer;

import static com.gto.gtocore.common.data.GTORecipes.*;

public interface Data {

    static void init() {
        long time = System.currentTimeMillis();
        ChemicalHelper.reinitializeUnification();
        MaterialInfoLoader.init();
        GTOMaterialInfoLoader.init();
        GTORecipes.initFilter();
        Consumer<FinishedRecipe> consumer = GTDynamicDataPack::addRecipe;

        CustomToolRecipes.init(consumer);
        ChemistryRecipes.init(consumer);
        MetaTileEntityMachineRecipeLoader.init(consumer);
        MiscRecipeLoader.init(consumer);
        VanillaStandardRecipes.init(consumer);
        WoodMachineRecipes.init(consumer);
        StoneMachineRecipes.init(consumer);
        CraftingRecipeLoader.init(consumer);
        FusionLoader.init(consumer);
        MachineRecipeLoader.init(consumer);
        AssemblerRecipeLoader.init(consumer);
        AssemblyLineLoader.init(consumer);
        BatteryRecipes.init(consumer);
        DecorationRecipes.init(consumer);

        CircuitRecipes.init(consumer);
        MetaTileEntityLoader.init(consumer);

        GCYMRecipes.init(consumer);
        RecipeAddition.init(consumer);
        GT_FILTER_RECIPES = null;
        SHAPED_FILTER_RECIPES = null;
        SHAPELESS_FILTER_RECIPES = null;

        ForEachMaterial.init(consumer);

        // GTO
        GTMTRecipe.init(consumer);
        FuelRecipe.init(consumer);
        NaquadahProcess.init(consumer);
        PlatGroupMetals.init(consumer);
        GCYRecipes.init(consumer);
        MachineRecipe.init(consumer);
        ComponentRecipes.init(consumer);
        MiscRecipe.init(consumer);
        ElementCopying.init(consumer);
        StoneDustProcess.init(consumer);
        Lanthanidetreatment.init(consumer);
        NewResearchSystem.init(consumer);
        RadiationHatchRecipes.init(consumer);
        RecipeOverwrite.init(consumer);
        PetrochemRecipes.init(consumer);
        GlassRecipe.init(consumer);
        DyeRecipes.init(consumer);
        WoodRecipes.init(consumer);
        ClassifiedRecipe.init(consumer);
        GenerateDisassembly.DISASSEMBLY_RECORD.clear();
        GenerateDisassembly.DISASSEMBLY_BLACKLIST.clear();
        RecyclingRecipes.init(consumer);
        ChemicalHelper.ITEM_MATERIAL_INFO.clear();
        LootSystem.defaultBlockTable(RegistriesUtils.getBlock("farmersrespite:kettle"));
        GTOLoots.BLOCKS.forEach(b -> LootSystem.defaultBlockTable((Block) b));
        GTOLoots.BLOCKS = null;
        GTOCore.LOGGER.info("Data loading took {}ms", System.currentTimeMillis() - time);
    }

    class PreInitialization implements Runnable {

        @Override
        public void run() {
            init();
            if (!GTOConfig.INSTANCE.disableMultiBlockPage) {
                IMultiblockMachineDefinition.init();
            }
            if (GTCEu.Mods.isEMILoaded()) {
                long time = System.currentTimeMillis();
                EmiConfig.logUntranslatedTags = false;
                EmiConfig.workstationLocation = SidebarSide.LEFT;
                EmiRepairItemRecipe.TOOLS.clear();
                GT_RECIPE_MAP.values().forEach(recipe -> recipe.recipeCategory.addRecipe(recipe));
                EMI_RECIPE_WIDGETS = new Object2ObjectOpenHashMap<>();
                ImmutableSet.Builder<EmiRecipe> recipes = ImmutableSet.builder();
                for (GTRecipeCategory category : GTRegistries.RECIPE_CATEGORIES) {
                    if (!category.shouldRegisterDisplays()) continue;
                    var type = category.getRecipeType();
                    if (category == type.getCategory()) type.buildRepresentativeRecipes();
                    EmiRecipeCategory emiCategory = GTRecipeEMICategory.CATEGORIES.apply(category);
                    type.getRecipesInCategory(category).stream().map(recipe -> new GTEMIRecipe(recipe, emiCategory)).forEach(recipes::add);
                }
                if (!GTOConfig.INSTANCE.disableMultiBlockPage) {
                    for (MachineDefinition machine : GTRegistries.MACHINES.values()) {
                        if (machine instanceof MultiblockMachineDefinition definition && definition.isRenderXEIPreview()) {
                            recipes.add(new MultiblockInfoEmiRecipe(definition));
                        }
                    }
                }
                EMI_RECIPE_WIDGETS = null;
                EMI_RECIPES = recipes.build();
                clearCategoryMap();
                GTOCore.LOGGER.info("Pre initialization EMI GTRecipe took {}ms", System.currentTimeMillis() - time);
            }
        }

        private static void clearCategoryMap() {
            if (GTOConfig.INSTANCE.recipeCheck) return;
            for (GTRecipeType type : GTRegistries.RECIPE_TYPES) {
                if (type == GTORecipeTypes.FURNACE_RECIPES) {
                    type.getCategoryMap().putIfAbsent(GTRecipeTypes.FURNACE_RECIPES.getCategory(), Set.of());
                } else {
                    type.getCategoryMap().replaceAll((k, v) -> Set.of());
                }
            }
        }
    }
}
