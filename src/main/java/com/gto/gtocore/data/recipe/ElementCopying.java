package com.gto.gtocore.data.recipe;

import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.Element;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTElements;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static com.gto.gtocore.common.data.GTORecipeTypes.ELEMENT_COPYING_RECIPES;

public class ElementCopying {

    private final static List<Material> fes = List.of(
            GTMaterials.Hydrogen,
            GTMaterials.Deuterium,
            GTMaterials.Tritium,
            GTMaterials.Helium,
            GTMaterials.Helium3,
            GTMaterials.Nitrogen,
            GTMaterials.Oxygen,
            GTMaterials.Fluorine,
            GTMaterials.Neon,
            GTMaterials.Chlorine,
            GTMaterials.Argon,
            GTMaterials.Bromine,
            GTMaterials.Krypton,
            GTMaterials.Xenon,
            GTMaterials.Mercury,
            GTMaterials.Radon);

    private final static List<Material> ies = List.of(
            GTMaterials.Actinium,
            GTMaterials.Aluminium,
            GTMaterials.Americium,
            GTMaterials.Antimony,
            GTMaterials.Arsenic,
            GTMaterials.Astatine,
            GTMaterials.Barium,
            GTMaterials.Berkelium,
            GTMaterials.Beryllium,
            GTMaterials.Bismuth,
            GTMaterials.Bohrium,
            GTMaterials.Boron,
            GTMaterials.Caesium,
            GTMaterials.Calcium,
            GTMaterials.Californium,
            GTMaterials.Carbon,
            GTMaterials.Cadmium,
            GTMaterials.Cerium,
            GTMaterials.Chromium,
            GTMaterials.Cobalt,
            GTMaterials.Copernicium,
            GTMaterials.Copper,
            GTMaterials.Curium,
            GTMaterials.Darmstadtium,
            GTMaterials.Dubnium,
            GTMaterials.Dysprosium,
            GTMaterials.Einsteinium,
            GTMaterials.Erbium,
            GTMaterials.Europium,
            GTMaterials.Fermium,
            GTMaterials.Flerovium,
            GTMaterials.Francium,
            GTMaterials.Gadolinium,
            GTMaterials.Gallium,
            GTMaterials.Germanium,
            GTMaterials.Gold,
            GTMaterials.Hafnium,
            GTMaterials.Hassium,
            GTMaterials.Holmium,
            GTMaterials.Indium,
            GTMaterials.Iodine,
            GTMaterials.Iridium,
            GTMaterials.Iron,
            GTMaterials.Lanthanum,
            GTMaterials.Lawrencium,
            GTMaterials.Lead,
            GTMaterials.Lithium,
            GTMaterials.Livermorium,
            GTMaterials.Lutetium,
            GTMaterials.Magnesium,
            GTMaterials.Mendelevium,
            GTMaterials.Manganese,
            GTMaterials.Meitnerium,
            GTMaterials.Molybdenum,
            GTMaterials.Moscovium,
            GTMaterials.Neodymium,
            GTMaterials.Neptunium,
            GTMaterials.Nickel,
            GTMaterials.Nihonium,
            GTMaterials.Niobium,
            GTMaterials.Nobelium,
            GTMaterials.Oganesson,
            GTMaterials.Osmium,
            GTMaterials.Palladium,
            GTMaterials.Phosphorus,
            GTMaterials.Polonium,
            GTMaterials.Platinum,
            GTMaterials.Plutonium239,
            GTMaterials.Plutonium241,
            GTMaterials.Potassium,
            GTMaterials.Praseodymium,
            GTMaterials.Promethium,
            GTMaterials.Protactinium,
            GTMaterials.Radium,
            GTMaterials.Rhenium,
            GTMaterials.Rhodium,
            GTMaterials.Roentgenium,
            GTMaterials.Rubidium,
            GTMaterials.Ruthenium,
            GTMaterials.Rutherfordium,
            GTMaterials.Samarium,
            GTMaterials.Scandium,
            GTMaterials.Seaborgium,
            GTMaterials.Selenium,
            GTMaterials.Silicon,
            GTMaterials.Silver,
            GTMaterials.Sodium,
            GTMaterials.Strontium,
            GTMaterials.Sulfur,
            GTMaterials.Tantalum,
            GTMaterials.Technetium,
            GTMaterials.Tellurium,
            GTMaterials.Tennessine,
            GTMaterials.Terbium,
            GTMaterials.Thorium,
            GTMaterials.Thallium,
            GTMaterials.Thulium,
            GTMaterials.Tin,
            GTMaterials.Titanium,
            GTOMaterials.Titanium50,
            GTMaterials.Tungsten,
            GTMaterials.Uranium238,
            GTMaterials.Uranium235,
            GTMaterials.Vanadium,
            GTMaterials.Ytterbium,
            GTMaterials.Yttrium,
            GTMaterials.Zinc,
            GTMaterials.Zirconium);

    public static void init(Consumer<FinishedRecipe> provider) {
        for (Material e : fes) {
            Element element = Objects.requireNonNullElse(e.getElement(), GTElements.Nt);
            long mass = element.mass();
            ELEMENT_COPYING_RECIPES.recipeBuilder(e.getName())
                    .notConsumableFluid(e.getFluid(1000))
                    .inputFluids(GTMaterials.UUMatter.getFluid(mass))
                    .outputFluids(e.getFluid(1000))
                    .duration((int) mass)
                    .EUt(GTValues.V[GTValues.UXV])
                    .save(provider);
        }

        for (Material e : ies) {
            Element element = Objects.requireNonNullElse(e.getElement(), GTElements.Nt);
            long mass = element.mass();
            ELEMENT_COPYING_RECIPES.recipeBuilder(e.getName())
                    .notConsumable(TagPrefix.dust, e)
                    .inputFluids(GTMaterials.UUMatter.getFluid(mass))
                    .outputItems(TagPrefix.dust, e)
                    .duration((int) mass)
                    .EUt(GTValues.V[GTValues.UXV])
                    .save(provider);
        }
    }
}
