package com.gtocore.data.recipe.generated;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.Element;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTElements;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Objects;

import static com.gtocore.common.data.GTORecipeTypes.ELEMENT_COPYING_RECIPES;
import static com.gtocore.common.data.GTORecipeTypes.SCANNER_RECIPES;

public final class ElementCopying {

    public static void init() {
        List<Material> fes = List.of(
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

        List<Material> ies = List.of(
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

        for (Material e : fes) {
            Element element = Objects.requireNonNullElse(e.getElement(), GTElements.Nt);
            int mass = (int) element.mass();
            Fluid fluid = e.getFluid();
            ItemStack data = GTOItems.DATA_DISC.get().getDisc(fluid);
            FluidStack stack = new FluidStack(fluid, 1000);
            SCANNER_RECIPES.recipeBuilder(e.getName())
                    .inputItems(GTOItems.DATA_DISC)
                    .inputFluids(stack)
                    .outputItems(data)
                    .EUt(480)
                    .duration(mass << 8)
                    .save();

            ELEMENT_COPYING_RECIPES.recipeBuilder(e.getName())
                    .notConsumable(data)
                    .inputFluids(GTMaterials.UUMatter.getFluid(mass))
                    .outputFluids(stack)
                    .duration(mass)
                    .EUt(GTValues.V[GTValues.UXV])
                    .save();
        }

        for (Material e : ies) {
            Element element = Objects.requireNonNullElse(e.getElement(), GTElements.Nt);
            int mass = (int) element.mass();
            ItemStack stack = ChemicalHelper.get(TagPrefix.dust, e);
            ItemStack data = GTOItems.DATA_DISC.get().getDisc(stack);

            SCANNER_RECIPES.recipeBuilder(e.getName())
                    .inputItems(GTOItems.DATA_DISC)
                    .inputItems(stack)
                    .outputItems(data)
                    .EUt(480)
                    .duration(mass << 8)
                    .save();

            ELEMENT_COPYING_RECIPES.recipeBuilder(e.getName())
                    .notConsumable(data)
                    .inputFluids(GTMaterials.UUMatter.getFluid(mass))
                    .outputItems(stack)
                    .duration(mass)
                    .EUt(GTValues.V[GTValues.UXV])
                    .save();
        }
    }
}
