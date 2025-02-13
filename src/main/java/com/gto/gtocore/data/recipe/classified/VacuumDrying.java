package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.init.GTOMaterials;
import com.gto.gtocore.init.GTORecipeTypes;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

interface VacuumDrying {

    static void init(Consumer<FinishedRecipe> provider) {
        GTORecipeTypes.VACUUM_DRYING_RECIPES.recipeBuilder(GTOCore.id("grossular_front_pro"))
                .inputFluids(GTOMaterials.GrossularFront.getFluid(4000))
                .outputItems(TagPrefix.dust, GTMaterials.Calcium, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Calcium, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Aluminium, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Aluminium, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Tungsten, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Thallium, 16)
                .outputFluids(GTOMaterials.RedMud.getFluid(200))
                .outputFluids(GTMaterials.Water.getFluid(2000))
                .EUt(30720)
                .duration(2400)
                .blastFurnaceTemp(5500)
                .save(provider);

        GTORecipeTypes.VACUUM_DRYING_RECIPES.recipeBuilder(GTOCore.id("sphalerite_front_pro"))
                .inputFluids(GTOMaterials.SphaleriteFront.getFluid(4000))
                .outputItems(TagPrefix.dust, GTMaterials.Zinc, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Zinc, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Iron, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Iron, 32)
                .outputItems(TagPrefix.dust, GTMaterials.Indium, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Gallium, 64)
                .outputFluids(GTOMaterials.RedMud.getFluid(200))
                .outputFluids(GTMaterials.Water.getFluid(2000))
                .EUt(30720)
                .duration(2400)
                .blastFurnaceTemp(5500)
                .save(provider);

        GTORecipeTypes.VACUUM_DRYING_RECIPES.recipeBuilder(GTOCore.id("chalcopyrite_front_pro"))
                .inputFluids(GTOMaterials.ChalcopyriteFront.getFluid(4000))
                .outputItems(TagPrefix.dust, GTMaterials.Copper, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Copper, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Iron, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Iron, 48)
                .outputItems(TagPrefix.dust, GTMaterials.Cadmium, 48)
                .outputItems(TagPrefix.dust, GTMaterials.Indium, 32)
                .outputFluids(GTOMaterials.RedMud.getFluid(200))
                .outputFluids(GTMaterials.Water.getFluid(2000))
                .EUt(7680)
                .duration(2400)
                .blastFurnaceTemp(4500)
                .save(provider);

        GTORecipeTypes.VACUUM_DRYING_RECIPES.recipeBuilder(GTOCore.id("nickel_front_pro"))
                .inputFluids(GTOMaterials.NickelFront.getFluid(4000))
                .outputItems(TagPrefix.dust, GTMaterials.Nickel, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Nickel, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Cobalt, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Cobalt, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Iron, 32)
                .outputItems(TagPrefix.dust, GTMaterials.Rhodium, 32)
                .outputFluids(GTOMaterials.RedMud.getFluid(200))
                .outputFluids(GTMaterials.Water.getFluid(2000))
                .EUt(7680)
                .duration(2400)
                .blastFurnaceTemp(4500)
                .save(provider);

        GTORecipeTypes.VACUUM_DRYING_RECIPES.recipeBuilder(GTOCore.id("pyrope_front_pro"))
                .inputFluids(GTOMaterials.PyropeFront.getFluid(4000))
                .outputItems(TagPrefix.dust, GTMaterials.Magnesium, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Magnesium, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Aluminium, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Manganese, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Boron, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Silicon, 48)
                .outputFluids(GTOMaterials.RedMud.getFluid(200))
                .outputFluids(GTMaterials.Water.getFluid(2000))
                .EUt(1920)
                .duration(2400)
                .blastFurnaceTemp(3500)
                .save(provider);

        GTORecipeTypes.VACUUM_DRYING_RECIPES.recipeBuilder(GTOCore.id("platinum_front_pro"))
                .inputFluids(GTOMaterials.PlatinumFront.getFluid(4000))
                .outputItems(TagPrefix.dust, GTMaterials.Platinum, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Nickel, 48)
                .outputItems(TagPrefix.dust, GTMaterials.Iridium, 32)
                .outputItems(TagPrefix.dust, GTMaterials.Osmium, 32)
                .outputItems(TagPrefix.dust, GTMaterials.Palladium, 32)
                .outputItems(TagPrefix.dust, GTMaterials.Cobalt, 32)
                .outputFluids(GTOMaterials.RedMud.getFluid(200))
                .outputFluids(GTMaterials.Water.getFluid(2000))
                .EUt(30720)
                .duration(2400)
                .blastFurnaceTemp(5500)
                .save(provider);

        GTORecipeTypes.VACUUM_DRYING_RECIPES.recipeBuilder(GTOCore.id("redstone_front_pro"))
                .inputFluids(GTOMaterials.RedstoneFront.getFluid(4000))
                .outputItems(TagPrefix.dust, GTMaterials.Redstone, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Redstone, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Manganese, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Manganese, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Yttrium, 32)
                .outputItems(TagPrefix.dust, GTMaterials.Ytterbium, 16)
                .outputFluids(GTOMaterials.RedMud.getFluid(200))
                .outputFluids(GTMaterials.Water.getFluid(2000))
                .EUt(7680)
                .duration(2400)
                .blastFurnaceTemp(4500)
                .save(provider);

        GTORecipeTypes.VACUUM_DRYING_RECIPES.recipeBuilder(GTOCore.id("almandine_front_pro"))
                .inputFluids(GTOMaterials.AlmandineFront.getFluid(4000))
                .outputItems(TagPrefix.dust, GTMaterials.Aluminium, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Aluminium, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Manganese, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Manganese, 24)
                .outputItems(TagPrefix.dust, GTMaterials.Yttrium, 24)
                .outputItems(TagPrefix.dust, GTMaterials.Ytterbium, 16)
                .outputFluids(GTOMaterials.RedMud.getFluid(200))
                .outputFluids(GTMaterials.Water.getFluid(2000))
                .EUt(7680)
                .duration(2400)
                .blastFurnaceTemp(5500)
                .save(provider);

        GTORecipeTypes.VACUUM_DRYING_RECIPES.recipeBuilder(GTOCore.id("monazite_front_pro"))
                .inputFluids(GTOMaterials.MonaziteFront.getFluid(4000))
                .outputItems(TagPrefix.dust, GTMaterials.Erbium, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Neodymium, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Thorium, 48)
                .outputItems(TagPrefix.dust, GTMaterials.Lanthanum, 32)
                .outputItems(TagPrefix.dust, GTMaterials.Lutetium, 16)
                .outputItems(TagPrefix.dust, GTMaterials.Europium, 8)
                .outputFluids(GTOMaterials.RedMud.getFluid(200))
                .outputFluids(GTMaterials.Water.getFluid(2000))
                .EUt(122880)
                .duration(2400)
                .blastFurnaceTemp(5500)
                .save(provider);

        GTORecipeTypes.VACUUM_DRYING_RECIPES.recipeBuilder(GTOCore.id("spessartine_front_pro"))
                .inputFluids(GTOMaterials.SpessartineFront.getFluid(4000))
                .outputItems(TagPrefix.dust, GTMaterials.Manganese, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Manganese, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Aluminium, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Aluminium, 32)
                .outputItems(TagPrefix.dust, GTMaterials.Palladium, 32)
                .outputItems(TagPrefix.dust, GTMaterials.Strontium, 16)
                .outputFluids(GTOMaterials.RedMud.getFluid(200))
                .outputFluids(GTMaterials.Water.getFluid(2000))
                .EUt(30720)
                .duration(2400)
                .blastFurnaceTemp(5500)
                .save(provider);

        GTORecipeTypes.VACUUM_DRYING_RECIPES.recipeBuilder(GTOCore.id("pentlandite_front_pro"))
                .inputFluids(GTOMaterials.PentlanditeFront.getFluid(4000))
                .outputItems(TagPrefix.dust, GTMaterials.Iron, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Iron, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Nickel, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Nickel, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Bismuth, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Ruthenium, 48)
                .outputFluids(GTOMaterials.RedMud.getFluid(200))
                .outputFluids(GTMaterials.Water.getFluid(2000))
                .EUt(30720)
                .duration(2400)
                .blastFurnaceTemp(5500)
                .save(provider);

        GTORecipeTypes.VACUUM_DRYING_RECIPES.recipeBuilder(GTOCore.id("enriched_naquadah_front_pro"))
                .inputFluids(GTOMaterials.EnrichedNaquadahFront.getFluid(4000))
                .outputItems(TagPrefix.dust, GTMaterials.NaquadahEnriched, 64)
                .outputItems(TagPrefix.dust, GTMaterials.NaquadahEnriched, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Naquadah, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Naquadah, 32)
                .outputItems(TagPrefix.dust, GTMaterials.Naquadria, 64)
                .outputItems(TagPrefix.dust, GTMaterials.Trinium, 32)
                .outputFluids(GTOMaterials.RedMud.getFluid(200))
                .outputFluids(GTMaterials.Water.getFluid(2000))
                .EUt(122880)
                .duration(2400)
                .blastFurnaceTemp(9500)
                .save(provider);
    }
}
