package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.GTODimensions;
import com.gto.gtocore.api.data.chemical.GTOChemicalHelper;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.common.data.*;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.OreProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.List;
import java.util.Map;

import static com.gto.gtocore.common.data.GTORecipeTypes.COSMOS_SIMULATION_RECIPES;

interface CosmosSimulation {

    static void init() {
        COSMOS_SIMULATION_RECIPES.recipeBuilder(GTOCore.id("cosmos_simulation1"))
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

        Map<Integer, Map<Material, Integer>> dustContent = new Object2ObjectOpenHashMap<>();
        Map<Integer, Map<Fluid, Integer>> fluidContent = new Object2ObjectOpenHashMap<>();

        for (Map.Entry<ResourceKey<Level>, Map<Material, Integer>> entry : GTOOres.ALL_ORES.entrySet()) {
            ResourceLocation dimension = entry.getKey().location();
            if (dimension.equals(GTODimensions.THE_NETHER)) continue;
            Integer tier = GTODimensions.ALL_LAYER_DIMENSION.get(dimension);
            if (tier == null || tier == 0) tier = 1;
            if (tier > 9) tier = 9;
            int size = 0;
            Map<Material, Integer> materialMap = new Object2IntOpenHashMap<>();
            Map<Material, Integer> secondaryMap = new Object2IntOpenHashMap<>();
            Map<Fluid, Integer> fluid = new Object2IntOpenHashMap<>();
            GTORecipeBuilder builder = COSMOS_SIMULATION_RECIPES.recipeBuilder(GTOCore.id(dimension.getPath()))
                    .addData("tier", tier)
                    .inputFluids(GTOMaterials.CosmicElement.getFluid(1024000))
                    .notConsumable(GTOItems.DIMENSION_DATA.get().getDimensionData(dimension));

            if (tier > 2) {
                builder.outputFluids(GTOMaterials.RawStarMatter.getFluid(FluidStorageKeys.PLASMA, tier << 16));
            }

            if (tier > 5) {
                builder.outputFluids(GTOMaterials.SpaceTime.getFluid(tier << 4));
            }

            for (Map.Entry<Material, Integer> ore : entry.getValue().entrySet()) {
                Map<Material, Integer> map = getOreMaterial(ore.getKey(), ore.getValue());
                for (Map.Entry<Material, Integer> material : map.entrySet()) {
                    secondaryMap.merge(material.getKey(), (int) (Math.sqrt(material.getValue() << 20)) << 8, (a, b) -> (int) (a + b / 1.5));
                }
                for (Map.Entry<Material, Integer> material : getOreMaterial(map).entrySet()) {
                    materialMap.merge(material.getKey(), (int) (Math.sqrt(material.getValue() << 20)) << 8, (a, b) -> (int) (a + b / 1.5));
                }
            }
            for (FluidStack fluidStack : GTOBedrockFluids.ALL_BEDROCK_FLUID.getOrDefault(entry.getKey(), List.of())) {
                fluid.merge(fluidStack.getFluid(), (int) Math.sqrt(fluidStack.getAmount() << 16) << 8, Integer::sum);
            }
            materialMap.putAll(dustContent.getOrDefault(tier, Map.of()));
            Map<Item, Integer> dust = new Object2IntOpenHashMap<>();
            for (Map.Entry<Material, Integer> material : materialMap.entrySet()) {
                Item item = GTOChemicalHelper.getItem(TagPrefix.dust, material.getKey());
                if (item != Items.AIR) {
                    size++;
                    dust.merge(item, material.getValue(), Integer::sum);
                }
            }
            for (Map.Entry<Material, Integer> material : secondaryMap.entrySet()) {
                if (size > 120) break;
                Item item = GTOChemicalHelper.getItem(TagPrefix.dust, material.getKey());
                if (item != Items.AIR) {
                    size++;
                    dust.merge(item, material.getValue(), (a, b) -> (int) (a + b / 1.75));
                }
            }
            dust.entrySet().stream().sorted(Map.Entry.comparingByValue()).toList().forEach(e -> builder.outputItems(e.getKey(), e.getValue()));
            fluid.putAll(fluidContent.getOrDefault(tier, Map.of()));
            for (Map.Entry<Fluid, Integer> content : fluid.entrySet().stream().sorted(Map.Entry.comparingByValue()).toList()) {
                size++;
                builder.outputFluids(new FluidStack(content.getKey(), content.getValue()));
            }
            builder.duration((int) Math.sqrt(size * tier * 256)).save();
        }
    }

    private static Map<Material, Integer> getOreMaterial(Map<Material, Integer> ore) {
        Map<Material, Integer> map = new Object2IntOpenHashMap<>();
        for (Map.Entry<Material, Integer> entry : ore.entrySet()) {
            List<MaterialStack> components = entry.getKey().getMaterialComponents();
            if (components.isEmpty()) {
                map.merge(entry.getKey(), entry.getValue(), Integer::sum);
            } else {
                for (MaterialStack component : components) {
                    map.merge(component.material(), (int) ((double) components.size() / component.amount() * entry.getValue()), Integer::sum);
                }
            }
        }
        return map;
    }

    private static Map<Material, Integer> getOreMaterial(Material material, int multiplier) {
        OreProperty property = material.getProperty(PropertyKey.ORE);
        if (property == null) return Map.of();
        Map<Material, Integer> ore = new Object2IntOpenHashMap<>();
        ore.put(material, multiplier);
        ore.merge(property.getOreByProduct(0, material), property.getByProductMultiplier() * multiplier / 3, Integer::sum);
        ore.merge(property.getOreByProduct(1, material), property.getByProductMultiplier() * multiplier / 4, Integer::sum);
        ore.merge(property.getOreByProduct(2, material), property.getByProductMultiplier() * multiplier / 5, Integer::sum);
        ore.merge(property.getOreByProduct(3, material), property.getByProductMultiplier() * multiplier / 6, Integer::sum);
        return ore;
    }
}
