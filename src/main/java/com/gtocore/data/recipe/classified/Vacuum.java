package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOFluids;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraftforge.fluids.FluidStack;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.Oxygen;
import static com.gtocore.common.data.GTORecipeTypes.VACUUM_RECIPES;

final class Vacuum {

    public static void init() {
        VACUUM_RECIPES.recipeBuilder("frozen_pearl")
                .inputItems(TagPrefix.gem, GTMaterials.EnderPearl)
                .outputItems("torchmaster:frozen_pearl")
                .EUt(120)
                .duration(600)
                .save();

        VACUUM_RECIPES.recipeBuilder("metastable_oganesson")
                .inputFluids(GTOMaterials.HotOganesson.getFluid(1000))
                .inputFluids(new FluidStack(GTOFluids.GELID_CRYOTHEUM.get(), 144))
                .outputItems(TagPrefix.dustSmall, GTOMaterials.Enderium, 2)
                .outputFluids(GTOMaterials.MetastableOganesson.getFluid(144))
                .EUt(491520)
                .duration(280)
                .save();

        VACUUM_RECIPES.recipeBuilder("fullerene_polymer_matrix_fine_tubing")
                .inputItems(GTOItems.FULLERENE_POLYMER_MATRIX_SOFT_TUBING.asItem())
                .outputItems(GTOItems.FULLERENE_POLYMER_MATRIX_FINE_TUBING.asItem())
                .EUt(500)
                .duration(240)
                .save();

        VACUUM_RECIPES.recipeBuilder("cold_ice_casing")
                .inputItems(GTBlocks.CASING_ALUMINIUM_FROSTPROOF.asItem())
                .inputFluids(GTMaterials.Ice.getFluid(10000))
                .inputFluids(GTMaterials.VanadiumGallium.getFluid(576))
                .outputItems(GTOBlocks.COLD_ICE_CASING.asItem())
                .EUt(1920)
                .duration(200)
                .save();

        VACUUM_RECIPES.recipeBuilder("fuming_nitric_acid")
                .inputFluids(GTOMaterials.FumingNitricAcid.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.CrystallineNitricAcid, 5)
                .EUt(120)
                .duration(180)
                .save();

        VACUUM_RECIPES.recipeBuilder("liquid_hydrogen")
                .inputFluids(GTOMaterials.HighPressureHydrogen.getFluid(1000))
                .outputFluids(GTOMaterials.LiquidHydrogen.getFluid(1000))
                .EUt(7680)
                .duration(240)
                .save();

        VACUUM_RECIPES.recipeBuilder("draconium_ingot")
                .inputItems(TagPrefix.ingotHot, GTOMaterials.Draconium)
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 1000))
                .outputItems(TagPrefix.ingot, GTOMaterials.Draconium)
                .outputFluids(GTMaterials.Helium.getFluid(500))
                .EUt(125829120)
                .duration(100)
                .save();

        VACUUM_RECIPES.builder("liquid_oxygen")
                .inputFluids(GTOMaterials.HighPressureOxygen.getFluid(1000))
                .outputFluids(Oxygen.getFluid(FluidStorageKeys.LIQUID, 1000))
                .duration(240).EUt(VA[EV]).save();

        VACUUM_RECIPES.builder("liquid_nitrogen")
                .inputFluids(GTOMaterials.HighPressureNitrogen.getFluid(1000))
                .outputFluids(GTOMaterials.LiquidNitrogen.getFluid(1000))
                .duration(320).EUt(VA[EV]).save();

        VACUUM_RECIPES.builder("liquid_ammonia")
                .inputFluids(GTMaterials.Ammonia.getFluid(8000))
                .outputFluids(GTOMaterials.LiquidAmmonia.getFluid(1000))
                .duration(200).EUt(VA[HV]).save();

        VACUUM_RECIPES.builder("stainless_steel_ingot")
                .outputItems(TagPrefix.ingot, GTMaterials.StainlessSteel)
                .inputFluids(GTMaterials.StainlessSteel.getFluid(FluidStorageKeys.MOLTEN, 144))
                .EUt(120)
                .duration(120)
                .save();

        VACUUM_RECIPES.builder("manganese_phosphide_ingot")
                .outputItems(TagPrefix.ingot, GTMaterials.ManganesePhosphide)
                .inputFluids(GTMaterials.ManganesePhosphide.getFluid(FluidStorageKeys.MOLTEN, 144))
                .EUt(120)
                .duration(80)
                .save();

        VACUUM_RECIPES.builder("space_coolant_cell_10k")
                .inputItems(GTItems.FLUID_CELL_LARGE_TUNGSTEN_STEEL.asStack())
                .outputItems(GTOItems.SPACE_COOLANT_CELL_10K.asItem())
                .inputFluids(GTOMaterials.CoolantLiquid, 1000)
                .EUt(7680)
                .duration(400)
                .save();
    }
}
