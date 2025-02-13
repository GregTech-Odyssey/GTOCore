package com.gto.gtocore.integration.kjs;

import com.gto.gtocore.common.data.GTORecipes;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.integration.kjs.recipe.GTRecipeSchema;
import com.gregtechceu.gtceu.integration.kjs.recipe.components.ExtendedOutputItem;
import com.gregtechceu.gtceu.integration.kjs.recipe.components.GTRecipeComponents;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;

import com.google.common.collect.ImmutableMap;
import dev.latvian.mods.kubejs.BuiltinKubeJSPlugin;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.bindings.event.ServerEvents;
import dev.latvian.mods.kubejs.recipe.RecipesEventJS;
import dev.latvian.mods.kubejs.recipe.schema.RecipeComponentFactoryRegistryEvent;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class GTKubeJSPlugin extends KubeJSPlugin {

    public static boolean cache;

    public static boolean hasKJSGTRecipe;

    public static ImmutableMap<ResourceLocation, Recipe<?>> RECIPES_CACHE;

    public static final Set<Recipe<?>> KJS_RECIPE = new LinkedHashSet<>();

    public static final Set<GTRecipe> KJS_GT_RECIPE = new LinkedHashSet<>();

    private static final BuiltinKubeJSPlugin PLUGIN = new BuiltinKubeJSPlugin();

    @Override
    public void onServerReload() {
        if (!GTCEu.isClientSide()) {
            GTORecipes.init();
        }
    }

    @Override
    public void registerEvents() {
        ServerEvents.GROUP.register();
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        PLUGIN.registerBindings(event);
    }

    @Override
    public void registerRecipeComponents(RecipeComponentFactoryRegistryEvent event) {
        PLUGIN.registerRecipeComponents(event);
    }

    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        PLUGIN.registerRecipeSchemas(event);
        for (var entry : GTRegistries.RECIPE_TYPES.entries()) {
            event.register(entry.getKey(), GTRecipeSchema.SCHEMA);
        }
    }

    @Override
    public void registerTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        PLUGIN.registerTypeWrappers(type, typeWrappers);
        typeWrappers.registerSimple(ExtendedOutputItem.class, ExtendedOutputItem::of);
        typeWrappers.registerSimple(GTRecipeComponents.FluidIngredientJS.class, GTRecipeComponents.FluidIngredientJS::of);
    }

    @Override
    public void injectRuntimeRecipes(RecipesEventJS event, RecipeManager manager, Map<ResourceLocation, Recipe<?>> recipesByName) {
        RecipesEventJS.runInParallel(() -> {
            KJS_RECIPE.clear();
            KJS_RECIPE.addAll(recipesByName.values());
            recipesByName.putAll(RECIPES_CACHE);
            KJS_GT_RECIPE.clear();
            event.addedRecipes.forEach(recipe -> {
                if (recipe instanceof GTRecipeSchema.GTRecipeJS gtRecipe) {
                    GTRecipeBuilder builder = ((GTRecipeType) BuiltInRegistries.RECIPE_TYPE.get(gtRecipe.type.id)).recipeBuilder(gtRecipe.idWithoutType());
                    if (gtRecipe.getValue(GTRecipeSchema.DURATION) != null) {
                        builder.duration = gtRecipe.getValue(GTRecipeSchema.DURATION).intValue();
                    }
                    if (gtRecipe.getValue(GTRecipeSchema.DATA) != null) {
                        builder.data = gtRecipe.getValue(GTRecipeSchema.DATA);
                    }
                    if (gtRecipe.getValue(GTRecipeSchema.CONDITIONS) != null) {
                        builder.conditions.addAll(Arrays.stream(gtRecipe.getValue(GTRecipeSchema.CONDITIONS)).toList());
                    }
                    builder.researchRecipeEntries().addAll(gtRecipe.researchRecipeEntries());
                    if (gtRecipe.getValue(GTRecipeSchema.ALL_INPUTS) != null) {
                        builder.input.putAll(gtRecipe.getValue(GTRecipeSchema.ALL_INPUTS).entrySet().stream().map(entry -> Map.entry(entry.getKey(), Arrays.stream(entry.getValue()).map(content -> entry.getKey().serializer.fromJsonContent(GTRecipeComponents.VALID_CAPS.get(entry.getKey()).getFirst().write(gtRecipe, content))).toList())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
                    }
                    if (gtRecipe.getValue(GTRecipeSchema.ALL_OUTPUTS) != null) {
                        builder.output.putAll(gtRecipe.getValue(GTRecipeSchema.ALL_OUTPUTS).entrySet().stream().map(entry -> Map.entry(entry.getKey(), Arrays.stream(entry.getValue()).map(content -> entry.getKey().serializer.fromJsonContent(GTRecipeComponents.VALID_CAPS.get(entry.getKey()).getSecond().write(gtRecipe, content))).toList())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
                    }
                    if (gtRecipe.getValue(GTRecipeSchema.ALL_TICK_INPUTS) != null) {
                        builder.tickInput.putAll(gtRecipe.getValue(GTRecipeSchema.ALL_TICK_INPUTS).entrySet().stream().map(entry -> Map.entry(entry.getKey(), Arrays.stream(entry.getValue()).map(content -> entry.getKey().serializer.fromJsonContent(GTRecipeComponents.VALID_CAPS.get(entry.getKey()).getFirst().write(gtRecipe, content))).toList())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
                    }
                    if (gtRecipe.getValue(GTRecipeSchema.ALL_TICK_OUTPUTS) != null) {
                        builder.tickOutput.putAll(gtRecipe.getValue(GTRecipeSchema.ALL_TICK_OUTPUTS).entrySet().stream().map(entry -> Map.entry(entry.getKey(), Arrays.stream(entry.getValue()).map(content -> entry.getKey().serializer.fromJsonContent(GTRecipeComponents.VALID_CAPS.get(entry.getKey()).getSecond().write(gtRecipe, content))).toList())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
                    }
                    KJS_GT_RECIPE.add(builder.buildRawRecipe());
                    hasKJSGTRecipe = true;
                }
            });
        });
    }
}
