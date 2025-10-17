package com.gtocore.data;

import com.gtocore.common.data.GTOLoots;
import com.gtocore.common.data.GTOOres;
import com.gtocore.common.data.GTORecipeTypes;
import com.gtocore.data.recipe.*;
import com.gtocore.data.recipe.ae2.AE2;
import com.gtocore.data.recipe.ae2.Ae2wtlibRecipes;
import com.gtocore.data.recipe.classified.$ClassifiedRecipe;
import com.gtocore.data.recipe.generated.*;
import com.gtocore.data.recipe.gtm.chemistry.ChemistryRecipes;
import com.gtocore.data.recipe.gtm.configurable.RecipeAddition;
import com.gtocore.data.recipe.gtm.misc.*;
import com.gtocore.data.recipe.magic.ArsNouveauRecipes;
import com.gtocore.data.recipe.magic.BotaniaRecipes;
import com.gtocore.data.recipe.magic.MagicRecipesA;
import com.gtocore.data.recipe.magic.MagicRecipesB;
import com.gtocore.data.recipe.misc.ComponentRecipes;
import com.gtocore.data.recipe.misc.SpaceStationRecipes;
import com.gtocore.data.recipe.mod.FunctionalStorage;
import com.gtocore.data.recipe.mod.ImmersiveAircraft;
import com.gtocore.data.recipe.mod.MeteoriteRecipe;
import com.gtocore.data.recipe.mod.Sophisticated;
import com.gtocore.data.recipe.processing.*;
import com.gtocore.data.recipe.research.AnalyzeRecipes;
import com.gtocore.data.recipe.research.DataGenerateRecipe;
import com.gtocore.data.recipe.research.ScanningRecipes;
import com.gtocore.integration.emi.GTEMIRecipe;
import com.gtocore.integration.emi.multipage.MultiblockInfoEmiRecipe;

import com.gtolib.GTOCore;
import com.gtolib.api.machine.MultiblockDefinition;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.api.recipe.ingredient.FastFluidIngredient;
import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.material.ItemMaterialData;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.category.GTRecipeCategory;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.core.MixinHelpers;
import com.gregtechceu.gtceu.data.recipe.GTCraftingComponents;
import com.gregtechceu.gtceu.data.recipe.MaterialInfoLoader;
import com.gregtechceu.gtceu.data.recipe.misc.RecyclingRecipes;
import com.gregtechceu.gtceu.data.recipe.misc.StoneMachineRecipes;
import com.gregtechceu.gtceu.data.recipe.misc.WoodMachineRecipes;
import com.gregtechceu.gtceu.integration.emi.recipe.GTRecipeEMICategory;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.Block;

import com.google.common.collect.ImmutableSet;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.config.EmiConfig;
import dev.emi.emi.config.SidebarSide;
import dev.emi.emi.recipe.special.EmiRepairItemRecipe;
import dev.shadowsoffire.placebo.loot.LootSystem;
import me.jellysquid.mods.sodium.mixin.core.render.MinecraftAccessor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.Collections;

import static com.gtocore.common.data.GTORecipes.EMI_RECIPES;

public final class Data {

    public static void init() {
        long time = System.currentTimeMillis();
        GTOOres.init();
        MeteoriteRecipe.init();

        ItemMaterialData.reinitializeMaterialData();
        GTCraftingComponents.init();
        MaterialInfoLoader.init();
        MaterialInfo.init();
        RecipeBuilder.initialization();
        RecipeFilter.init();

        BlastProperty.GasTier.LOW.setFluid(() -> FastFluidIngredient.of(GTMaterials.Nitrogen.getFluid(1000)));
        BlastProperty.GasTier.MID.setFluid(() -> FastFluidIngredient.of(GTMaterials.Helium.getFluid(100)));
        BlastProperty.GasTier.HIGH.setFluid(() -> FastFluidIngredient.of(GTMaterials.Argon.getFluid(100)));
        BlastProperty.GasTier.HIGHER.setFluid(() -> FastFluidIngredient.of(GTMaterials.Neon.getFluid(100)));
        BlastProperty.GasTier.HIGHEST.setFluid(() -> FastFluidIngredient.of(GTMaterials.Krypton.getFluid(100)));

        ComponentRecipes.init();

        WoodMachineRecipes.init();
        StoneMachineRecipes.init();

        CustomToolRecipes.init();
        ChemistryRecipes.init();
        MetaTileEntityMachineRecipeLoader.init();
        MiscRecipeLoader.init();
        VanillaStandardRecipes.init();
        CraftingRecipeLoader.init();
        FusionLoader.init();
        MachineRecipeLoader.init();
        AssemblerRecipeLoader.init();
        BatteryRecipes.init();
        DecorationRecipes.init();

        CircuitRecipes.init();
        MetaTileEntityLoader.init();

        GCYMRecipes.init();
        RecipeAddition.init();

        ForEachMaterial.init();

        // GTO
        GTMTRecipe.init();
        GCYRecipes.init();
        MachineRecipe.init();
        MiscRecipe.init();
        SpaceStationRecipes.init();
        OrganRecipes.INSTANCE.init();
        BotaniaRecipes.init();
        ArsNouveauRecipes.init();
        MagicRecipesA.init();
        MagicRecipesB.init();
        FuelRecipe.init();
        BrineRecipes.init();
        NaquadahProcess.init();
        PlatGroupMetals.init();
        CompositeMaterialsProcessing.init();
        ElementCopying.init();
        StoneDustProcess.init();
        Lanthanidetreatment.init();
        NewResearchSystem.init();
        RadiationHatchRecipes.init();
        PetrochemRecipes.init();
        GlassRecipe.init();
        DyeRecipes.init();
        WoodRecipes.init();
        AE2.init();
        Ae2wtlibRecipes.init();
        ImmersiveAircraft.init();
        FunctionalStorage.init();
        Sophisticated.init();
        $ClassifiedRecipe.init();
        Temporary.init();
        if (GTCEu.isDev()) {
            ScanningRecipes.init();
            if (false) {
                AnalyzeRecipes.init();
                DataGenerateRecipe.init();
            }
        }
        if (GTCEu.isDev() || GTOCore.isEasy()) {
            EasyModeRecipe.init();
        }

        GenerateDisassembly.DISASSEMBLY_RECORD.clear();
        GenerateDisassembly.DISASSEMBLY_BLACKLIST.clear();
        RecyclingRecipes.init();
        ItemMaterialData.ITEM_MATERIAL_INFO.clear();
        RecipeBuilder.clean();
        LootSystem.defaultBlockTable(RegistriesUtils.getBlock("farmersrespite:kettle"));
        GTOLoots.BLOCKS.forEach(b -> LootSystem.defaultBlockTable((Block) b));
        GTOLoots.BLOCKS = null;
        GTOLoots.generateGTDynamicLoot();
        MixinHelpers.registryGTDynamicTags();
        GTOCore.LOGGER.info("Data loading took {}ms", System.currentTimeMillis() - time);
    }

    public static void asyncInit() {
        try {
            init();
            RecipeBuilder.RECIPE_MAP.values().forEach(recipe -> recipe.recipeCategory.addRecipe(recipe));
            if (GTCEu.Mods.isEMILoaded()) {
                MultiblockDefinition.init();
                long time = System.currentTimeMillis();
                EmiConfig.logUntranslatedTags = false;
                EmiConfig.workstationLocation = SidebarSide.LEFT;
                EmiRepairItemRecipe.TOOLS.clear();
                ImmutableSet.Builder<EmiRecipe> recipes = ImmutableSet.builder();
                for (GTRecipeCategory category : GTRegistries.RECIPE_CATEGORIES) {
                    if (!category.shouldRegisterDisplays()) continue;
                    var type = category.getRecipeType();
                    if (category == type.getCategory()) type.buildRepresentativeRecipes();
                    EmiRecipeCategory emiCategory = GTRecipeEMICategory.CATEGORIES.apply(category);
                    type.getRecipesInCategory(category).stream().map(recipe -> new GTEMIRecipe((Recipe) recipe, emiCategory)).forEach(recipes::add);
                }
                for (MachineDefinition machine : GTRegistries.MACHINES.values()) {
                    if (machine instanceof MultiblockMachineDefinition definition && definition.isRenderXEIPreview()) {
                        recipes.add(new MultiblockInfoEmiRecipe(definition));
                    }
                }
                EMI_RECIPES = recipes.build();
                for (GTRecipeType type : GTRegistries.RECIPE_TYPES) {
                    if (type == GTORecipeTypes.FURNACE_RECIPES) {
                        type.getCategoryMap().putIfAbsent(GTRecipeTypes.FURNACE_RECIPES.getCategory(), Collections.emptySet());
                    } else {
                        type.getCategoryMap().replaceAll((k, v) -> Collections.emptySet());
                    }
                }
                GTOCore.LOGGER.info("Pre initialization EMI GTRecipe took {}ms", System.currentTimeMillis() - time);
            }
        } catch (Exception e) {
            Configurator.setRootLevel(Level.DEBUG);
            e.printStackTrace();
            Client.interrupt();
        }
    }

    private static class Client {

        private static void interrupt() {
            ((MinecraftAccessor) Minecraft.getInstance()).embeddium$getGameThread().interrupt();
        }
    }
}
