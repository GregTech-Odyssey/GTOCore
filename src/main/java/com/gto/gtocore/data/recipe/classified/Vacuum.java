package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.common.data.GTOFluids;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraftforge.fluids.FluidStack;

import static com.gto.gtocore.common.data.GTORecipeTypes.VACUUM_RECIPES;

interface Vacuum {

    static void init() {
        VACUUM_RECIPES.recipeBuilder(GTOCore.id("frozen_pearl"))
                .inputItems(TagPrefix.gem, GTMaterials.EnderPearl)
                .inputFluids(GTMaterials.Ice.getFluid(576))
                .outputItems("torchmaster:frozen_pearl")
                .EUt(120)
                .duration(120)
                .save();

        VACUUM_RECIPES.recipeBuilder(GTOCore.id("metastable_oganesson"))
                .inputFluids(GTOMaterials.HotOganesson.getFluid(1000))
                .inputFluids(new FluidStack(GTOFluids.GELID_CRYOTHEUM.get(), 144))
                .outputItems(TagPrefix.dustSmall, GTOMaterials.Enderium, 2)
                .outputFluids(GTOMaterials.MetastableOganesson.getFluid(144))
                .EUt(491520)
                .duration(280)
                .save();

        VACUUM_RECIPES.recipeBuilder(GTOCore.id("fullerene_polymer_matrix_fine_tubing"))
                .inputItems(GTOItems.FULLERENE_POLYMER_MATRIX_SOFT_TUBING.asStack())
                .outputItems(GTOItems.FULLERENE_POLYMER_MATRIX_FINE_TUBING.asStack())
                .EUt(500)
                .duration(240)
                .save();

        VACUUM_RECIPES.recipeBuilder(GTOCore.id("cold_ice_casing"))
                .inputItems(GTBlocks.CASING_ALUMINIUM_FROSTPROOF.asStack())
                .inputFluids(GTMaterials.Ice.getFluid(10000))
                .inputFluids(GTMaterials.VanadiumGallium.getFluid(576))
                .outputItems(GTOBlocks.COLD_ICE_CASING.asStack())
                .EUt(1920)
                .duration(200)
                .save();

        VACUUM_RECIPES.recipeBuilder(GTOCore.id("fuming_nitric_acid"))
                .inputFluids(GTOMaterials.FumingNitricAcid.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.CrystallineNitricAcid, 5)
                .EUt(120)
                .duration(180)
                .save();

        VACUUM_RECIPES.recipeBuilder(GTOCore.id("liquid_hydrogen"))
                .inputFluids(GTMaterials.Hydrogen.getFluid(1000))
                .outputFluids(GTOMaterials.LiquidHydrogen.getFluid(1000))
                .EUt(7680)
                .duration(240)
                .save();

        VACUUM_RECIPES.recipeBuilder(GTOCore.id("draconium_ingot"))
                .inputItems(TagPrefix.ingotHot, GTOMaterials.Draconium)
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 1000))
                .outputItems(TagPrefix.ingot, GTOMaterials.Draconium)
                .outputFluids(GTMaterials.Helium.getFluid(500))
                .EUt(125829120)
                .duration(100)
                .save();
    }
}
