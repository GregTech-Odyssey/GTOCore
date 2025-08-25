package com.gtocore.data;

import com.gtocore.common.CommonProxy;
import com.gtocore.common.data.GTOLoots;
import com.gtocore.common.data.GTOOres;
import com.gtocore.common.data.GTORecipeTypes;
import com.gtocore.data.recipe.*;
import com.gtocore.data.recipe.classified.$ClassifiedRecipe;
import com.gtocore.data.recipe.generated.*;
import com.gtocore.data.recipe.gtm.chemistry.ChemistryRecipes;
import com.gtocore.data.recipe.gtm.configurable.RecipeAddition;
import com.gtocore.data.recipe.gtm.misc.*;
import com.gtocore.data.recipe.misc.*;
import com.gtocore.data.recipe.mod.AE2;
import com.gtocore.data.recipe.mod.FunctionalStorage;
import com.gtocore.data.recipe.mod.ImmersiveAircraft;
import com.gtocore.data.recipe.mod.MeteoriteRecipe;
import com.gtocore.data.recipe.processing.*;
import com.gtocore.data.recipe.research.*;
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
import com.gregtechceu.gtceu.data.pack.GTDynamicDataPack;
import com.gregtechceu.gtceu.data.recipe.GTCraftingComponents;
import com.gregtechceu.gtceu.data.recipe.MaterialInfoLoader;
import com.gregtechceu.gtceu.data.recipe.misc.RecyclingRecipes;
import com.gregtechceu.gtceu.data.recipe.misc.StoneMachineRecipes;
import com.gregtechceu.gtceu.data.recipe.misc.WoodMachineRecipes;
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
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;

import java.util.Collections;
import java.util.function.Consumer;

import static com.gtocore.common.data.GTORecipes.EMI_RECIPES;
import static com.gtocore.common.data.GTORecipes.EMI_RECIPE_WIDGETS;

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
        Consumer<FinishedRecipe> consumer = GTDynamicDataPack::addRecipe;

        BlastProperty.GasTier.LOW.setFluid(() -> FastFluidIngredient.of(GTMaterials.Nitrogen.getFluid(1000)));
        BlastProperty.GasTier.MID.setFluid(() -> FastFluidIngredient.of(GTMaterials.Helium.getFluid(100)));
        BlastProperty.GasTier.HIGH.setFluid(() -> FastFluidIngredient.of(GTMaterials.Argon.getFluid(100)));
        BlastProperty.GasTier.HIGHER.setFluid(() -> FastFluidIngredient.of(GTMaterials.Neon.getFluid(100)));
        BlastProperty.GasTier.HIGHEST.setFluid(() -> FastFluidIngredient.of(GTMaterials.Krypton.getFluid(100)));

        ComponentRecipes.init(consumer);

        WoodMachineRecipes.init(consumer);
        StoneMachineRecipes.init(consumer);

        CustomToolRecipes.init(consumer);
        ChemistryRecipes.init(consumer);
        MetaTileEntityMachineRecipeLoader.init(consumer);
        MiscRecipeLoader.init(consumer);
        VanillaStandardRecipes.init(consumer);
        CraftingRecipeLoader.init(consumer);
        FusionLoader.init();
        MachineRecipeLoader.init(consumer);
        AssemblerRecipeLoader.init();
        BatteryRecipes.init(consumer);
        DecorationRecipes.init(consumer);

        CircuitRecipes.init(consumer);
        MetaTileEntityLoader.init(consumer);

        GCYMRecipes.init(consumer);
        RecipeAddition.init(consumer);

        ForEachMaterial.init(consumer);

        // GTO
        GTMTRecipe.init(consumer);
        GCYRecipes.init(consumer);
        MachineRecipe.init(consumer);
        MiscRecipe.init(consumer);
        OrganRecipes.INSTANCE.init(consumer);
        BotaniaRecipes.init(consumer);
        ArsNouveauRecipes.init(consumer);
        ManaRecipes.init(consumer);
        FuelRecipe.init();
        BrineRecipes.init();
        NaquadahProcess.init();
        PlatGroupMetals.init();
        ElementCopying.init();
        StoneDustProcess.init();
        Lanthanidetreatment.init();
        NewResearchSystem.init();
        RadiationHatchRecipes.init();
        PetrochemRecipes.init();
        GlassRecipe.init();
        DyeRecipes.init();
        WoodRecipes.init();
        AE2.init(consumer);
        ImmersiveAircraft.init(consumer);
        FunctionalStorage.init(consumer);
        $ClassifiedRecipe.init(consumer);
        Temporary.init();
        if (GTCEu.isDev()) {
            ScanningRecipes.init();
            AnalyzeData.init();
            AnalyzeRecipes.init();
            DataGenerateRecipe.init();
        }
        if (GTCEu.isDev() || GTOCore.isSimple()) {
            SimpleModeRecipe.init();
        }

        GenerateDisassembly.DISASSEMBLY_RECORD.clear();
        GenerateDisassembly.DISASSEMBLY_BLACKLIST.clear();
        RecyclingRecipes.init(consumer);
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
                EMI_RECIPE_WIDGETS = new Reference2ReferenceOpenHashMap<>();
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
                EMI_RECIPE_WIDGETS = null;
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
            e.printStackTrace();
            CommonProxy.setException(e);
        }
    }
}
