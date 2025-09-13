package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.*;

import com.gtolib.api.data.GTODimensions;
import com.gtolib.api.data.chemical.GTOChemicalHelper;
import com.gtolib.api.recipe.RecipeBuilder;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.OreProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.gtocore.common.data.GTORecipeTypes.COSMOS_SIMULATION_RECIPES;

final class CosmosSimulation {

    public static void init() {
        COSMOS_SIMULATION_RECIPES.recipeBuilder("cosmos_simulation1")
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem(), 64)
                .inputFluids(GTOMaterials.CosmicElement.getFluid(1024000))
                .outputItems(TagPrefix.dust, GTMaterials.Carbon, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Phosphorus, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Sulfur, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Selenium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Iodine, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Boron, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Silicon, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Germanium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Arsenic, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Antimony, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Tellurium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Astatine, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Aluminium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Gallium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Indium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Tin, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Thallium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Lead, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Bismuth, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Polonium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Titanium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Vanadium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Chromium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Manganese, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Iron, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Cobalt, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Nickel, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Copper, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Zinc, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Zirconium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Niobium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Molybdenum, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Technetium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Ruthenium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Rhodium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Palladium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Silver, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Cadmium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Hafnium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Tantalum, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Tungsten, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Rhenium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Osmium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Iridium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Platinum, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Gold, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Beryllium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Magnesium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Calcium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Strontium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Barium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Radium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Yttrium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Lithium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Sodium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Potassium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Rubidium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Caesium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Francium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Scandium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Actinium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Thorium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Protactinium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Uranium238, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Neptunium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Plutonium239, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Americium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Curium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Berkelium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Californium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Einsteinium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Fermium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Mendelevium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Nobelium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Lawrencium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Lanthanum, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Cerium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Praseodymium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Neodymium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Promethium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Samarium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Europium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Gadolinium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Terbium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Dysprosium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Holmium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Erbium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Thulium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Ytterbium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Lutetium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Rutherfordium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Dubnium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Seaborgium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Bohrium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Hassium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Meitnerium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Darmstadtium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Roentgenium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Copernicium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Nihonium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Flerovium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Moscovium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Livermorium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Tennessine, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Oganesson, 2147483)
                .outputItems(TagPrefix.dust, GTOMaterials.Jasper, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Naquadah, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.NaquadahEnriched, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Naquadria, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Duranium, 2147483)
                .outputItems(TagPrefix.dust, GTMaterials.Tritanium, 2147483)
                .outputItems(TagPrefix.dust, GTOMaterials.Mithril, 2147483)
                .outputItems(TagPrefix.dust, GTOMaterials.Orichalcum, 2147483)
                .outputItems(TagPrefix.dust, GTOMaterials.Enderium, 2147483)
                .outputItems(TagPrefix.dust, GTOMaterials.Adamantine, 2147483)
                .outputItems(TagPrefix.dust, GTOMaterials.Vibranium, 2147483)
                .outputItems(TagPrefix.dust, GTOMaterials.Infuscolium, 2147483)
                .outputItems(TagPrefix.dust, GTOMaterials.Taranium, 2147483)
                .outputItems(TagPrefix.dust, GTOMaterials.Draconium, 2147483)
                .outputItems(TagPrefix.dust, GTOMaterials.Starmetal, 2147483)
                .outputFluids(GTOMaterials.SpaceTime.getFluid(256))
                .outputFluids(GTOMaterials.StableBaryonicMatter.getFluid(21474836))
                .outputFluids(GTOMaterials.QuarkGluon.getFluid(FluidStorageKeys.PLASMA, 21474836))
                .outputFluids(GTOMaterials.HeavyQuarkDegenerateMatter.getFluid(FluidStorageKeys.PLASMA, 21474836))
                .outputFluids(GTOMaterials.Neutron.getFluid(214748364))
                .outputFluids(GTOMaterials.HeavyLeptonMixture.getFluid(214748364))
                .outputFluids(GTMaterials.Hydrogen.getFluid(2147483647))
                .outputFluids(GTMaterials.Nitrogen.getFluid(2147483647))
                .outputFluids(GTMaterials.Oxygen.getFluid(2147483647))
                .outputFluids(GTMaterials.Fluorine.getFluid(2147483647))
                .outputFluids(GTMaterials.Chlorine.getFluid(2147483647))
                .outputFluids(GTMaterials.Bromine.getFluid(2147483647))
                .outputFluids(GTMaterials.Helium.getFluid(2147483647))
                .outputFluids(GTMaterials.Neon.getFluid(2147483647))
                .outputFluids(GTMaterials.Argon.getFluid(2147483647))
                .outputFluids(GTMaterials.Krypton.getFluid(2147483647))
                .outputFluids(GTMaterials.Xenon.getFluid(2147483647))
                .outputFluids(GTMaterials.Radon.getFluid(2147483647))
                .outputFluids(GTMaterials.Mercury.getFluid(2147483647))
                .outputFluids(GTMaterials.Deuterium.getFluid(2147483647))
                .outputFluids(GTMaterials.Tritium.getFluid(2147483647))
                .outputFluids(GTMaterials.Helium3.getFluid(2147483647))
                .outputFluids(GTOMaterials.UnknowWater.getFluid(2147483647))
                .outputFluids(GTMaterials.UUMatter.getFluid(2147483647))
                .duration(12000)
                .addData("tier", 10)
                .save();

        Int2ObjectOpenHashMap<Object2IntOpenHashMap<Material>> dustContent = new Int2ObjectOpenHashMap<>();
        Int2ObjectOpenHashMap<Object2IntOpenHashMap<Fluid>> fluidContent = new Int2ObjectOpenHashMap<>();

        for (Map.Entry<ResourceLocation, Object2IntOpenHashMap<Material>> entry : GTOOres.ALL_ORES.entrySet()) {
            ResourceLocation dimension = entry.getKey();
            if (dimension.equals(GTODimensions.THE_NETHER)) continue;
            int tier = GTODimensions.getTier(dimension);
            if (tier == 0) tier = 1;
            if (tier > 9) tier = 9;
            Object2IntOpenHashMap<Material> materialMap = new Object2IntOpenHashMap<>();
            Object2IntOpenHashMap<Fluid> fluid = new Object2IntOpenHashMap<>();
            RecipeBuilder builder = COSMOS_SIMULATION_RECIPES.recipeBuilder(dimension.getPath())
                    .addData("tier", tier)
                    .inputFluids(GTOMaterials.CosmicElement.getFluid(1024000))
                    .notConsumable(GTOItems.DIMENSION_DATA.get().getDimensionData(dimension));

            if (tier > 2) {
                builder.outputFluids(GTOMaterials.RawStarMatter.getFluid(FluidStorageKeys.PLASMA, tier << 16));
            }

            if (tier > 5) {
                builder.outputFluids(GTOMaterials.SpaceTime.getFluid(tier << 4));
            }

            for (Object2IntOpenHashMap.Entry<Material> ore : entry.getValue().object2IntEntrySet()) {
                Object2IntOpenHashMap<Material> map = getOreMaterial(ore.getKey(), ore.getIntValue());
                if (map == null) continue;
                for (Object2IntOpenHashMap.Entry<Material> material : getOreMaterial(map).object2IntEntrySet()) {
                    materialMap.merge(material.getKey(), (int) (Math.sqrt(material.getIntValue() << 20)) << 8, (a, b) -> (int) (a + b / 1.5));
                }
            }
            for (FluidStack fluidStack : GTOBedrockFluids.ALL_BEDROCK_FLUID.getOrDefault(GTODimensions.getDimensionKey(entry.getKey()), Collections.emptyList())) {
                fluid.merge(fluidStack.getFluid(), (int) Math.sqrt(fluidStack.getAmount() << 16) << 8, Integer::sum);
            }
            materialMap.putAll(dustContent.getOrDefault(tier, new Object2IntOpenHashMap<>()));
            Object2IntOpenHashMap<Item> dust = new Object2IntOpenHashMap<>();
            for (Object2IntOpenHashMap.Entry<Material> material : materialMap.object2IntEntrySet()) {
                Item item = GTOChemicalHelper.getItem(TagPrefix.dust, material.getKey());
                if (item != Items.AIR) {
                    dust.mergeInt(item, material.getIntValue(), Integer::sum);
                }
            }
            dust.object2IntEntrySet().stream().sorted(Map.Entry.comparingByValue()).toList().forEach(e -> builder.outputItems(e.getKey(), e.getIntValue()));
            fluid.putAll(fluidContent.getOrDefault(tier, new Object2IntOpenHashMap<>()));
            for (Object2IntOpenHashMap.Entry<Fluid> content : fluid.object2IntEntrySet().stream().sorted(Map.Entry.comparingByValue()).toList()) {
                builder.outputFluids(new FluidStack(content.getKey(), content.getIntValue()));
            }
            builder.duration((int) Math.sqrt(tier * dust.size() << 16)).save();
        }
    }

    private static Object2IntOpenHashMap<Material> getOreMaterial(Object2IntOpenHashMap<Material> ore) {
        Object2IntOpenHashMap<Material> map = new Object2IntOpenHashMap<>();
        for (Object2IntOpenHashMap.Entry<Material> entry : ore.object2IntEntrySet()) {
            List<MaterialStack> components = entry.getKey().getMaterialComponents();
            if (components.isEmpty()) {
                map.mergeInt(entry.getKey(), entry.getIntValue(), Integer::sum);
            } else {
                for (MaterialStack component : components) {
                    map.mergeInt(component.material(), (int) ((double) components.size() / component.amount() * entry.getIntValue()), Integer::sum);
                }
            }
        }
        return map;
    }

    private static Object2IntOpenHashMap<Material> getOreMaterial(Material material, int multiplier) {
        OreProperty property = material.getProperty(PropertyKey.ORE);
        if (property == null) return null;
        Object2IntOpenHashMap<Material> ore = new Object2IntOpenHashMap<>();
        ore.put(material, multiplier);
        ore.merge(property.getOreByProduct(0, material), property.getByProductMultiplier() * multiplier / 3, Integer::sum);
        ore.merge(property.getOreByProduct(1, material), property.getByProductMultiplier() * multiplier / 4, Integer::sum);
        ore.merge(property.getOreByProduct(2, material), property.getByProductMultiplier() * multiplier / 5, Integer::sum);
        ore.merge(property.getOreByProduct(3, material), property.getByProductMultiplier() * multiplier / 6, Integer::sum);
        return ore;
    }
}
