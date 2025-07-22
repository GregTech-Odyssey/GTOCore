package com.gtocore.data.recipe.classified;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import static com.gtocore.common.data.GTORecipeTypes.PACKER_RECIPES;
import static com.gtocore.common.data.GTORecipeTypes.UNPACKER_RECIPES;

final class Packer {

    public static void init() {
        PACKER_RECIPES.recipeBuilder("zero_point_module")
                .inputItems(GTOItems.ZERO_POINT_MODULE_FRAGMENTS.asStack(64))
                .outputItems(GTItems.ZERO_POINT_MODULE.asItem())
                .EUt(120)
                .duration(2000)
                .save();

        PACKER_RECIPES.recipeBuilder("scrap_box")
                .inputItems(GTOItems.SCRAP.asStack(9))
                .outputItems(GTOItems.SCRAP_BOX.asItem())
                .EUt(12)
                .duration(200)
                .save();

        UNPACKER_RECIPES.recipeBuilder("tiny_degenerate_rhenium_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.DegenerateRhenium)
                .outputItems(TagPrefix.dustTiny, GTOMaterials.DegenerateRhenium, 9)
                .EUt(1920)
                .duration(20)
                .save();

        PACKER_RECIPES.recipeBuilder("universal_circuit_max")
                .inputItems(CustomTags.MAX_CIRCUITS)
                .outputItems(GTOItems.UNIVERSAL_CIRCUIT[GTValues.MAX].get())
                .EUt(7)
                .duration(32768)
                .save();

        PACKER_RECIPES.recipeBuilder("universal_circuit_luv")
                .inputItems(CustomTags.LuV_CIRCUITS)
                .outputItems(GTOItems.UNIVERSAL_CIRCUIT[GTValues.LuV].get())
                .EUt(7)
                .duration(128)
                .save();

        PACKER_RECIPES.recipeBuilder("universal_circuit_opv")
                .inputItems(CustomTags.OpV_CIRCUITS)
                .outputItems(GTOItems.UNIVERSAL_CIRCUIT[GTValues.OpV].get())
                .EUt(7)
                .duration(16384)
                .save();

        PACKER_RECIPES.recipeBuilder("universal_circuit_uxv")
                .inputItems(CustomTags.UXV_CIRCUITS)
                .outputItems(GTOItems.UNIVERSAL_CIRCUIT[GTValues.UXV].get())
                .EUt(7)
                .duration(8192)
                .save();

        PACKER_RECIPES.recipeBuilder("universal_circuit_ulv")
                .inputItems(CustomTags.ULV_CIRCUITS)
                .outputItems(GTOItems.UNIVERSAL_CIRCUIT[GTValues.ULV].get())
                .EUt(7)
                .duration(2)
                .save();

        PACKER_RECIPES.recipeBuilder("universal_circuit_uev")
                .inputItems(CustomTags.UEV_CIRCUITS)
                .outputItems(GTOItems.UNIVERSAL_CIRCUIT[GTValues.UEV].get())
                .EUt(7)
                .duration(2048)
                .save();

        PACKER_RECIPES.recipeBuilder("universal_circuit_uhv")
                .inputItems(CustomTags.UHV_CIRCUITS)
                .outputItems(GTOItems.UNIVERSAL_CIRCUIT[GTValues.UHV].get())
                .EUt(7)
                .duration(1024)
                .save();

        PACKER_RECIPES.recipeBuilder("universal_circuit_uiv")
                .inputItems(CustomTags.UIV_CIRCUITS)
                .outputItems(GTOItems.UNIVERSAL_CIRCUIT[GTValues.UIV].get())
                .EUt(7)
                .duration(4096)
                .save();

        PACKER_RECIPES.recipeBuilder("universal_circuit_mv")
                .inputItems(CustomTags.MV_CIRCUITS)
                .outputItems(GTOItems.UNIVERSAL_CIRCUIT[GTValues.MV].get())
                .EUt(7)
                .duration(8)
                .save();

        PACKER_RECIPES.recipeBuilder("universal_circuit_lv")
                .inputItems(CustomTags.LV_CIRCUITS)
                .outputItems(GTOItems.UNIVERSAL_CIRCUIT[GTValues.LV].get())
                .EUt(7)
                .duration(4)
                .save();

        PACKER_RECIPES.recipeBuilder("universal_circuit_uv")
                .inputItems(CustomTags.UV_CIRCUITS)
                .outputItems(GTOItems.UNIVERSAL_CIRCUIT[GTValues.UV].get())
                .EUt(7)
                .duration(512)
                .save();

        PACKER_RECIPES.recipeBuilder("universal_circuit_hv")
                .inputItems(CustomTags.HV_CIRCUITS)
                .outputItems(GTOItems.UNIVERSAL_CIRCUIT[GTValues.HV].get())
                .EUt(7)
                .duration(16)
                .save();

        PACKER_RECIPES.recipeBuilder("universal_circuit_iv")
                .inputItems(CustomTags.IV_CIRCUITS)
                .outputItems(GTOItems.UNIVERSAL_CIRCUIT[GTValues.IV].get())
                .EUt(7)
                .duration(64)
                .save();

        PACKER_RECIPES.recipeBuilder("universal_circuit_ev")
                .inputItems(CustomTags.EV_CIRCUITS)
                .outputItems(GTOItems.UNIVERSAL_CIRCUIT[GTValues.EV].get())
                .EUt(7)
                .duration(32)
                .save();

        PACKER_RECIPES.recipeBuilder("universal_circuit_zpm")
                .inputItems(CustomTags.ZPM_CIRCUITS)
                .outputItems(GTOItems.UNIVERSAL_CIRCUIT[GTValues.ZPM].get())
                .EUt(7)
                .duration(256)
                .save();

        PACKER_RECIPES.recipeBuilder("magmatter_dust")
                .inputItems(GTOTagPrefix.NANITES, GTOMaterials.TranscendentMetal)
                .inputItems(TagPrefix.dustSmall, GTOMaterials.Magmatter, 4)
                .outputItems(GTOTagPrefix.CONTAMINABLE_NANITES, GTOMaterials.TranscendentMetal)
                .outputItems(TagPrefix.dust, GTOMaterials.Magmatter)
                .EUt(30)
                .duration(20)
                .save();

        PACKER_RECIPES.recipeBuilder("carrot_crate")
                .inputItems(new ItemStack(Items.CARROT.asItem(), 9))
                .outputItems(RegistriesUtils.getItemStack("farmersdelight:carrot_crate"))
                .EUt(12)
                .duration(10)
                .save();

        PACKER_RECIPES.recipeBuilder("potato_crate")
                .inputItems(new ItemStack(Items.POTATO.asItem(), 9))
                .outputItems(RegistriesUtils.getItemStack("farmersdelight:potato_crate"))
                .EUt(12)
                .duration(10)
                .save();

        PACKER_RECIPES.recipeBuilder("beetroot_crate")
                .inputItems(new ItemStack(Items.BEETROOT.asItem(), 9))
                .outputItems(RegistriesUtils.getItemStack("farmersdelight:beetroot_crate"))
                .EUt(12)
                .duration(10)
                .save();

        PACKER_RECIPES.recipeBuilder("cabbage_crate")
                .inputItems(RegistriesUtils.getItemStack("farmersdelight:cabbage", 9))
                .outputItems(RegistriesUtils.getItemStack("farmersdelight:cabbage_crate"))
                .EUt(12)
                .duration(10)
                .save();

        PACKER_RECIPES.recipeBuilder("tomato_crate")
                .inputItems(RegistriesUtils.getItemStack("farmersdelight:tomato", 9))
                .outputItems(RegistriesUtils.getItemStack("farmersdelight:tomato_crate"))
                .EUt(12)
                .duration(10)
                .save();

        PACKER_RECIPES.recipeBuilder("onion_crate")
                .inputItems(RegistriesUtils.getItemStack("farmersdelight:onion", 9))
                .outputItems(RegistriesUtils.getItemStack("farmersdelight:onion_crate"))
                .EUt(12)
                .duration(10)
                .save();

        PACKER_RECIPES.recipeBuilder("rice_bale")
                .inputItems(RegistriesUtils.getItemStack("farmersdelight:rice_panicle", 9))
                .outputItems(RegistriesUtils.getItemStack("farmersdelight:rice_bale"))
                .EUt(12)
                .duration(10)
                .save();

        PACKER_RECIPES.recipeBuilder("rice_bag")
                .inputItems(RegistriesUtils.getItemStack("farmersdelight:rice", 9))
                .outputItems(RegistriesUtils.getItemStack("farmersdelight:rice_bag"))
                .EUt(12)
                .duration(10)
                .save();

        PACKER_RECIPES.recipeBuilder("straw_bale")
                .inputItems(RegistriesUtils.getItemStack("farmersdelight:straw", 9))
                .outputItems(RegistriesUtils.getItemStack("farmersdelight:straw_bale"))
                .EUt(12)
                .duration(10)
                .save();

        PACKER_RECIPES.recipeBuilder("green_tea_leaves_sack")
                .inputItems(RegistriesUtils.getItemStack("farmersrespite:green_tea_leaves", 9))
                .outputItems(RegistriesUtils.getItemStack("farmersrespite:green_tea_leaves_sack"))
                .EUt(12)
                .duration(10)
                .save();

        PACKER_RECIPES.recipeBuilder("yellow_tea_leaves_sack")
                .inputItems(RegistriesUtils.getItemStack("farmersrespite:yellow_tea_leaves", 9))
                .outputItems(RegistriesUtils.getItemStack("farmersrespite:yellow_tea_leaves_sack"))
                .EUt(12)
                .duration(10)
                .save();

        PACKER_RECIPES.recipeBuilder("black_tea_leaves_sack")
                .inputItems(RegistriesUtils.getItemStack("farmersrespite:black_tea_leaves", 9))
                .outputItems(RegistriesUtils.getItemStack("farmersrespite:black_tea_leaves_sack"))
                .EUt(12)
                .duration(10)
                .save();

        PACKER_RECIPES.recipeBuilder("coffee_beans_sack")
                .inputItems(RegistriesUtils.getItemStack("farmersrespite:coffee_beans", 9))
                .outputItems(RegistriesUtils.getItemStack("farmersrespite:coffee_beans_sack"))
                .EUt(12)
                .duration(10)
                .save();
    }
}
