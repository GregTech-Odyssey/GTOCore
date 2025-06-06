package com.gto.gtocore.data.recipe.pack;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

final class Packer {

    static void init(Consumer<FinishedRecipe> provider) {
        GTRecipeTypes.PACKER_RECIPES.recipeBuilder(GTOCore.id("zero_point_module"))
                .inputItems(GTOItems.ZERO_POINT_MODULE_FRAGMENTS.asStack(64))
                .outputItems(GTItems.ZERO_POINT_MODULE.asStack())
                .EUt(120)
                .duration(2000)
                .save(provider);

        GTRecipeTypes.PACKER_RECIPES.recipeBuilder(GTOCore.id("scrap_box"))
                .inputItems(GTOItems.SCRAP.asStack(9))
                .outputItems(GTOItems.SCRAP_BOX.asStack())
                .EUt(12)
                .duration(200)
                .save(provider);

        GTRecipeTypes.PACKER_RECIPES.recipeBuilder(GTOCore.id("tiny_degenerate_rhenium_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.DegenerateRhenium)
                .outputItems(TagPrefix.dustTiny, GTOMaterials.DegenerateRhenium, 9)
                .EUt(1920)
                .duration(20)
                .save(provider);

        GTRecipeTypes.PACKER_RECIPES.recipeBuilder(GTOCore.id("universal_circuit_max"))
                .inputItems(CustomTags.MAX_CIRCUITS)
                .outputItems(GTOItems.UNIVERSAL_CIRCUIT[GTValues.MAX].asStack())
                .EUt(7)
                .duration(32768)
                .save(provider);

        GTRecipeTypes.PACKER_RECIPES.recipeBuilder(GTOCore.id("universal_circuit_luv"))
                .inputItems(CustomTags.LuV_CIRCUITS)
                .outputItems(GTOItems.UNIVERSAL_CIRCUIT[GTValues.LuV].asStack())
                .EUt(7)
                .duration(128)
                .save(provider);

        GTRecipeTypes.PACKER_RECIPES.recipeBuilder(GTOCore.id("universal_circuit_opv"))
                .inputItems(CustomTags.OpV_CIRCUITS)
                .outputItems(GTOItems.UNIVERSAL_CIRCUIT[GTValues.OpV].asStack())
                .EUt(7)
                .duration(16384)
                .save(provider);

        GTRecipeTypes.PACKER_RECIPES.recipeBuilder(GTOCore.id("universal_circuit_uxv"))
                .inputItems(CustomTags.UXV_CIRCUITS)
                .outputItems(GTOItems.UNIVERSAL_CIRCUIT[GTValues.UXV].asStack())
                .EUt(7)
                .duration(8192)
                .save(provider);

        GTRecipeTypes.PACKER_RECIPES.recipeBuilder(GTOCore.id("universal_circuit_ulv"))
                .inputItems(CustomTags.ULV_CIRCUITS)
                .outputItems(GTOItems.UNIVERSAL_CIRCUIT[GTValues.ULV].asStack())
                .EUt(7)
                .duration(2)
                .save(provider);

        GTRecipeTypes.PACKER_RECIPES.recipeBuilder(GTOCore.id("universal_circuit_uev"))
                .inputItems(CustomTags.UEV_CIRCUITS)
                .outputItems(GTOItems.UNIVERSAL_CIRCUIT[GTValues.UEV].asStack())
                .EUt(7)
                .duration(2048)
                .save(provider);

        GTRecipeTypes.PACKER_RECIPES.recipeBuilder(GTOCore.id("universal_circuit_uhv"))
                .inputItems(CustomTags.UHV_CIRCUITS)
                .outputItems(GTOItems.UNIVERSAL_CIRCUIT[GTValues.UHV].asStack())
                .EUt(7)
                .duration(1024)
                .save(provider);

        GTRecipeTypes.PACKER_RECIPES.recipeBuilder(GTOCore.id("universal_circuit_uiv"))
                .inputItems(CustomTags.UIV_CIRCUITS)
                .outputItems(GTOItems.UNIVERSAL_CIRCUIT[GTValues.UIV].asStack())
                .EUt(7)
                .duration(4096)
                .save(provider);

        GTRecipeTypes.PACKER_RECIPES.recipeBuilder(GTOCore.id("universal_circuit_mv"))
                .inputItems(CustomTags.MV_CIRCUITS)
                .outputItems(GTOItems.UNIVERSAL_CIRCUIT[GTValues.MV].asStack())
                .EUt(7)
                .duration(8)
                .save(provider);

        GTRecipeTypes.PACKER_RECIPES.recipeBuilder(GTOCore.id("universal_circuit_lv"))
                .inputItems(CustomTags.LV_CIRCUITS)
                .outputItems(GTOItems.UNIVERSAL_CIRCUIT[GTValues.LV].asStack())
                .EUt(7)
                .duration(4)
                .save(provider);

        GTRecipeTypes.PACKER_RECIPES.recipeBuilder(GTOCore.id("universal_circuit_uv"))
                .inputItems(CustomTags.UV_CIRCUITS)
                .outputItems(GTOItems.UNIVERSAL_CIRCUIT[GTValues.UV].asStack())
                .EUt(7)
                .duration(512)
                .save(provider);

        GTRecipeTypes.PACKER_RECIPES.recipeBuilder(GTOCore.id("universal_circuit_hv"))
                .inputItems(CustomTags.HV_CIRCUITS)
                .outputItems(GTOItems.UNIVERSAL_CIRCUIT[GTValues.HV].asStack())
                .EUt(7)
                .duration(16)
                .save(provider);

        GTRecipeTypes.PACKER_RECIPES.recipeBuilder(GTOCore.id("universal_circuit_iv"))
                .inputItems(CustomTags.IV_CIRCUITS)
                .outputItems(GTOItems.UNIVERSAL_CIRCUIT[GTValues.IV].asStack())
                .EUt(7)
                .duration(64)
                .save(provider);

        GTRecipeTypes.PACKER_RECIPES.recipeBuilder(GTOCore.id("universal_circuit_ev"))
                .inputItems(CustomTags.EV_CIRCUITS)
                .outputItems(GTOItems.UNIVERSAL_CIRCUIT[GTValues.EV].asStack())
                .EUt(7)
                .duration(32)
                .save(provider);

        GTRecipeTypes.PACKER_RECIPES.recipeBuilder(GTOCore.id("universal_circuit_zpm"))
                .inputItems(CustomTags.ZPM_CIRCUITS)
                .outputItems(GTOItems.UNIVERSAL_CIRCUIT[GTValues.ZPM].asStack())
                .EUt(7)
                .duration(256)
                .save(provider);

        GTRecipeTypes.PACKER_RECIPES.recipeBuilder(GTOCore.id("magmatter_dust"))
                .inputItems(GTOTagPrefix.nanites, GTOMaterials.TranscendentMetal)
                .inputItems(TagPrefix.dustSmall, GTOMaterials.Magmatter, 4)
                .outputItems(GTOTagPrefix.contaminableNanites, GTOMaterials.TranscendentMetal)
                .outputItems(TagPrefix.dust, GTOMaterials.Magmatter)
                .EUt(30)
                .duration(20)
                .save(provider);
    }
}
