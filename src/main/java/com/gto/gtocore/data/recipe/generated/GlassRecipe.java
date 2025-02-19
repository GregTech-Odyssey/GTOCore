package com.gto.gtocore.data.recipe.generated;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeCategories;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fluids.FluidStack;

import com.tterrag.registrate.util.entry.BlockEntry;

import java.util.function.Consumer;

public final class GlassRecipe {

    public static void init(Consumer<FinishedRecipe> provider) {
        GTRecipeTypes.ALLOY_SMELTER_RECIPES.recipeBuilder(GTOCore.id("borosilicate_glass"))
                .inputItems(TagPrefix.block, GTMaterials.BorosilicateGlass)
                .notConsumable(GTItems.SHAPE_MOLD_BLOCK)
                .outputItems(GTOBlocks.BOROSILICATE_GLASS.asItem())
                .category(GTRecipeCategories.INGOT_MOLDING)
                .EUt(30)
                .duration(200)
                .save(provider);

        addGlass(GTValues.EV, GTMaterials.Titanium, GTOBlocks.TITANIUM_BOROSILICATE_GLASS, provider);
        addGlass(GTValues.IV, GTMaterials.Tungsten, GTOBlocks.TUNGSTEN_BOROSILICATE_GLASS, provider);
        addGlass(GTValues.LuV, GTMaterials.HSSS, GTOBlocks.HSSS_BOROSILICATE_GLASS, provider);
        addGlass(GTValues.ZPM, GTMaterials.Naquadah, GTOBlocks.NAQUADAH_BOROSILICATE_GLASS, provider);
        addGlass(GTValues.UV, GTMaterials.Tritanium, GTOBlocks.TRITANIUM_BOROSILICATE_GLASS, provider);
        addGlass(GTValues.UHV, GTMaterials.Neutronium, GTOBlocks.NEUTRONIUM_BOROSILICATE_GLASS, provider);
        addGlass(GTValues.UEV, GTOMaterials.Enderium, GTOBlocks.ENDERIUM_BOROSILICATE_GLASS, provider);
        addGlass(GTValues.UIV, GTOMaterials.Taranium, GTOBlocks.TARANIUM_BOROSILICATE_GLASS, provider);
        addGlass(GTValues.UXV, GTOMaterials.HeavyQuarkDegenerateMatter, GTOBlocks.QUARKS_BOROSILICATE_GLASS, provider);
        addGlass(GTValues.OpV, GTOMaterials.Draconium, GTOBlocks.DRACONIUM_BOROSILICATE_GLASS, provider);
        addGlass(GTValues.MAX, GTOMaterials.CosmicNeutronium, GTOBlocks.COSMIC_NEUTRONIUM_BOROSILICATE_GLASS, provider);
    }

    private static void addGlass(int tier, Material material, BlockEntry<Block> block, Consumer<FinishedRecipe> provider) {
        FluidStack fluidStack = material.getFluid(1152);
        GTRecipeTypes.FLUID_SOLIDFICATION_RECIPES.recipeBuilder(GTOCore.id("borosilicate_glass_" + material.getName()))
                .inputItems(GTOBlocks.BOROSILICATE_GLASS.asItem())
                .inputFluids(fluidStack)
                .outputItems(block.asItem())
                .EUt(GTValues.VA[tier])
                .duration(200)
                .save(provider);

        GTORecipeTypes.LIQUEFACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("borosilicate_glass_" + material.getName()))
                .inputItems(block.asItem())
                .outputFluids(fluidStack)
                .EUt(GTValues.VA[tier])
                .duration(200)
                .blastFurnaceTemp(Math.max(800, (int) (material.getBlastTemperature() * 0.6)))
                .save(provider);
    }
}
