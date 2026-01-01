package com.gtocore.data.recipe.misc;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.data.machines.SpaceMultiblock;
import com.gtocore.common.item.OrderItem;

import com.gtolib.GTOCore;
import com.gtolib.api.data.Dimension;
import com.gtolib.utils.RLUtils;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTDimensionMarkers;
import com.gregtechceu.gtceu.common.data.GTRecipes;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

import earth.terrarium.adastra.common.recipes.SpaceStationRecipe;
import earth.terrarium.adastra.common.recipes.base.IngredientHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.gtocore.common.data.GTORecipeTypes.SPACE_STATION_CONSTRUCTION_RECIPES;

public class SpaceStationRecipes {

    public static void init() {
        // Space Station
        SpaceStationBuilder.builder("earth_orbit_space_station")
                .dimension(Dimension.OVERWORLD)
                .inputItems(SpaceMultiblock.SPACE_STATION.asItem())
                .inputItems(GTBlocks.CASING_STAINLESS_CLEAN.asItem(), 64)
                .inputItems(GTOBlocks.ALUMINUM_ALLOY_8090_SKIN_MECHANICAL_BLOCK.asItem(), 64)
                .inputItems(GTBlocks.CASING_TITANIUM_STABLE.asItem(), 32)
                .inputItems(GTOBlocks.ALUMINUM_ALLOY_7050_SUPPORT_MECHANICAL_BLOCK.asItem(), 16)
                .inputItems(GTOBlocks.PRESSURE_CONTAINMENT_CASING.asItem(), 16)
                .inputItems(GTBlocks.FILTER_CASING.asItem(), 16)
                .inputItems(GTOTagPrefix.frameGt, GTOMaterials.StainlessSteelGC4, 16)
                .inputItems(GTOBlocks.SPACECRAFT_SEALING_MECHANICAL_BLOCK.asItem(), 16)
                .build();
        for (Dimension dimension : Dimension.values()) {
            if (dimension == Dimension.OVERWORLD || !dimension.isWithinGalaxy()) {
                continue;
            }
            SpaceStationBuilder.builder("orbit_space_station_" + dimension.name().toLowerCase())
                    .dimension(dimension)
                    .inputItems(SpaceMultiblock.SPACE_STATION.asItem())
                    .inputItems(GTBlocks.CASING_STAINLESS_CLEAN.asItem(), 344)
                    .inputItems(GTOBlocks.ALUMINUM_ALLOY_8090_SKIN_MECHANICAL_BLOCK.asItem(), 148)
                    .inputItems(GTOBlocks.TITANIUM_ALLOY_PROTECTIVE_MECHANICAL_BLOCK.asItem(), 128)
                    .inputItems(GTOBlocks.ALUMINUM_ALLOY_7050_SUPPORT_MECHANICAL_BLOCK.asItem(), 28)
                    .inputItems(GTOBlocks.PRESSURE_CONTAINMENT_CASING.asItem(), 38)
                    .inputItems(GTBlocks.FILTER_CASING.asItem(), 16)
                    .inputItems(GTOTagPrefix.frameGt, GTOMaterials.StainlessSteelGC4, 46)
                    .inputItems(GTOBlocks.SPACECRAFT_SEALING_MECHANICAL_BLOCK.asItem(), 117)
                    .inputItems(GTOBlocks.ALUMINUM_ALLOY_2090_SKIN_MECHANICAL_BLOCK.asItem(), 32)
                    .build();
        }
    }

    public static void initJsonFilter(Set<ResourceLocation> filters) {
        filters.add(RLUtils.ad("space_station/earth_orbit_space_station"));
        filters.add(RLUtils.ad("space_station/moon_orbit_space_station"));
        filters.add(RLUtils.ad("space_station/mars_orbit_space_station"));
        filters.add(RLUtils.ad("space_station/venus_orbit_space_station"));
        filters.add(RLUtils.ad("space_station/mercury_orbit_space_station"));
    }

    private static class SpaceStationBuilder {

        private final List<IngredientHolder> ingredients = new ArrayList<>();
        private ResourceKey<Level> orbit;
        private Dimension dimension;
        private final ResourceLocation id;
        private ResourceLocation structure = GTOCore.id("space_station");

        private SpaceStationBuilder(String id) {
            this.id = GTOCore.id("adastra_space_station_" + id);
        }

        public SpaceStationBuilder structure(ResourceLocation structure) {
            this.structure = structure;
            return this;
        }

        static SpaceStationBuilder builder(String id) {
            return new SpaceStationBuilder(id);
        }

        SpaceStationBuilder dimension(Dimension dimension) {
            this.orbit = ResourceKey.create(Registries.DIMENSION, dimension.getOrbit());
            this.dimension = dimension;
            return this;
        }

        public SpaceStationBuilder inputItems(TagKey<Item> tag, int count) {
            this.ingredients.add(IngredientHolder.of(Ingredient.of(tag), count));
            return this;
        }

        SpaceStationBuilder inputItems(TagPrefix tagPrefix, Material material, int count) {
            return inputItems(ChemicalHelper.get(tagPrefix, material).getItem(), count);
        }

        SpaceStationBuilder inputItems(ItemLike item, int count) {
            this.ingredients.add(IngredientHolder.of(Ingredient.of(item), count));
            return this;
        }

        public SpaceStationBuilder inputItems(TagKey<Item> tag) {
            this.ingredients.add(IngredientHolder.of(Ingredient.of(tag)));
            return this;
        }

        SpaceStationBuilder inputItems(ItemLike item) {
            this.ingredients.add(IngredientHolder.of(Ingredient.of(item)));
            return this;
        }

        void build() {
            GTRecipes.RECIPE_MAP.put(id, new SpaceStationRecipe(id, ingredients, orbit, structure));
            var recipe = SPACE_STATION_CONSTRUCTION_RECIPES.builder(id.getPath()).dimension(dimension.getLocation());
            ingredients.forEach(i -> recipe.inputItems(i.ingredient(), i.count()));
            recipe.outputItems(OrderItem.setTarget(GTOItems.ORDER.asStack(), GTRegistries.DIMENSION_MARKERS.getOrDefault(dimension.getLocation(), GTDimensionMarkers.OVERWORLD).getIcon()));
            recipe.save();
        }
    }
}
