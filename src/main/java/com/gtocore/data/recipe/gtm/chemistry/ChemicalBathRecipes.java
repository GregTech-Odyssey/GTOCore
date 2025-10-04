package com.gtocore.data.recipe.gtm.chemistry;

import com.gregtechceu.gtceu.common.data.GTBlocks;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import static com.gregtechceu.gtceu.api.GTValues.ULV;
import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.dust;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.CHEMICAL_BATH_RECIPES;

final class ChemicalBathRecipes {

    public static void init() {
        CHEMICAL_BATH_RECIPES.recipeBuilder("paper_from_wood_dust")
                .inputItems(dust, Wood)
                .inputFluids(Water.getFluid(100))
                .outputItems(Items.PAPER)
                .duration(200).EUt(4).save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("paper_from_paper_dust")
                .inputItems(dust, Paper)
                .inputFluids(Water.getFluid(100))
                .outputItems(Items.PAPER)
                .duration(100).EUt(4).save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("paper_from_sugar_cane")
                .inputItems(Items.SUGAR_CANE)
                .inputFluids(Water.getFluid(100))
                .outputItems(Items.PAPER)
                .duration(100).EUt(VA[ULV]).save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("paper_from_wood_dust_distilled")
                .inputItems(dust, Wood)
                .inputFluids(DistilledWater.getFluid(100))
                .outputItems(Items.PAPER)
                .duration(200).EUt(4).save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("paper_from_paper_dust_distilled")
                .inputItems(dust, Paper)
                .inputFluids(DistilledWater.getFluid(100))
                .outputItems(Items.PAPER)
                .duration(100).EUt(4).save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("paper_from_sugar_cane_distilled")
                .inputItems(Items.SUGAR_CANE)
                .inputFluids(DistilledWater.getFluid(100))
                .outputItems(Items.PAPER)
                .duration(100).EUt(VA[ULV]).save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("treated_planks")
                .inputItems(ItemTags.PLANKS)
                .inputFluids(Creosote.getFluid(100))
                .outputItems(GTBlocks.TREATED_WOOD_PLANK.asItem())
                .duration(100).EUt(VA[ULV]).save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("light_to_dark_concrete")
                .inputItems(GTBlocks.LIGHT_CONCRETE.asItem())
                .inputFluids(Water.getFluid(100))
                .outputItems(GTBlocks.DARK_CONCRETE.asItem())
                .duration(100).EUt(VA[ULV]).save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("tungstic_acid_from_scheelite")
                .inputItems(dust, Scheelite, 6)
                .inputFluids(HydrochloricAcid.getFluid(2000))
                .outputItems(dust, TungsticAcid, 7)
                .outputItems(dust, CalciumChloride, 3)
                .duration(210).EUt(960).save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("tungstic_acid_from_tungstate")
                .inputItems(dust, Tungstate, 7)
                .inputFluids(HydrochloricAcid.getFluid(2000))
                .outputItems(dust, TungsticAcid, 7)
                .outputItems(dust, LithiumChloride, 4)
                .duration(210).EUt(960).save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("vanilla_glow_ink_sac")
                .inputItems(Items.INK_SAC)
                .inputFluids(Glowstone.getFluid(144))
                .outputItems(Items.GLOW_INK_SAC)
                .duration(300).EUt(VA[ULV])
                .save();
    }
}
