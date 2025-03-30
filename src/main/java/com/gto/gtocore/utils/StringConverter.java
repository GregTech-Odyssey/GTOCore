package com.gto.gtocore.utils;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.recipe.FastSizedIngredient;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.core.mixins.IngredientAccessor;
import com.gregtechceu.gtceu.core.mixins.TagValueAccessor;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

public final class StringConverter {

    public static String fromItem(Ingredient ingredient, int re) {
        if (ingredient.isEmpty() || ingredient.getItems().length < 1) return null;
        ItemStack stack = ingredient.getItems()[0];
        int amount = stack.getCount();
        Ingredient inner = ingredient;
        if (ingredient instanceof FastSizedIngredient sizedIngredient) {
            amount = sizedIngredient.getAmount();
            inner = sizedIngredient.getInner();
        }
        for (Ingredient.Value value : ((IngredientAccessor) inner).getValues()) {
            if (value instanceof Ingredient.ItemValue itemValue) {
                for (ItemStack itemStack : itemValue.getItems()) {
                    MaterialEntry entry = ChemicalHelper.getMaterialEntry(itemStack.getItem());
                    if (!entry.isEmpty()) {
                        String material;
                        String tagPrefix;
                        if (StringIndex.MATERIAL_MAP.containsKey(entry.material())) {
                            material = StringIndex.MATERIAL_MAP.get(entry.material());
                        } else {
                            material = "GTOMaterials." + FormattingUtil.lowerUnderscoreToUpperCamel(entry.material().getName());
                        }
                        if (StringIndex.TAGPREFIX_MAP.containsKey(entry.tagPrefix())) {
                            tagPrefix = StringIndex.TAGPREFIX_MAP.get(entry.tagPrefix());
                        } else {
                            tagPrefix = "GTOTagPrefix." + entry.tagPrefix().name().toUpperCase();
                        }
                        if (re == 2) return "new MaterialEntry(" + tagPrefix + ", " + material + (amount > 1 ? ", " + amount : "") + ")";
                        if (re == 1) return tagPrefix + ", " + material + (amount > 1 ? ", " + amount : "");
                        return "ChemicalHelper.get(" + tagPrefix + ", " + material + (amount > 1 ? ", " + amount : "") + ")";
                    }
                }
            } else if (value instanceof Ingredient.TagValue tagValue) {
                TagKey<Item> tag = ((TagValueAccessor) tagValue).getTag();
                if (StringIndex.TAG_MAP.containsKey(tag)) {
                    return StringIndex.TAG_MAP.get(tag) + (amount > 1 ? ", " + amount : "");
                }
                ResourceLocation resourceLocation = tag.location();
                String s = "";
                if (resourceLocation.getNamespace().equals(GTOCore.MOD_ID)) {
                    s = "GTOCore.id(\"" + resourceLocation.getPath() + "\")";
                } else if (resourceLocation.getNamespace().equals(GTCEu.MOD_ID)) {
                    s = "GTCEu.id(\"" + resourceLocation.getPath() + "\")";
                } else if (resourceLocation.getNamespace().equals("forge")) {
                    s = "new ResourceLocation(\"forge\", \"" + resourceLocation.getPath() + "\")";
                } else if (resourceLocation.getNamespace().equals("minecraft")) {
                    s = "new ResourceLocation(\"minecraft\", \"" + resourceLocation.getPath() + "\")";
                }
                return "TagUtil.createTag(" + s + ")";
            }
        }
        if (stack.getItem() instanceof BlockItem blockItem && !ItemUtils.getIdLocation(blockItem.getBlock()).getNamespace().equals("minecraft")) {
            if (StringIndex.BLOCK_LINK_MAP.containsKey(blockItem.getBlock())) {
                return StringIndex.BLOCK_LINK_MAP.get(blockItem.getBlock()) + ".asStack(" + (amount > 1 ? amount : "") + ")";
            } else if (StringIndex.BLOCK_MAP.containsKey(blockItem.getBlock())) {
                return "new ItemStack(" + StringIndex.BLOCK_MAP.get(blockItem.getBlock()) + ".asItem()" + (amount > 1 ? ", " + amount : "") + ")";
            } else if (ItemUtils.getIdLocation(blockItem.getBlock()).getNamespace().equals(GTOCore.MOD_ID)) {
                return "GTOBlocks." + ItemUtils.getIdLocation(blockItem.getBlock()).getPath().toUpperCase() + ".asStack(" + (amount > 1 ? amount : "") + ")";
            }
        } else {
            if (StringIndex.ITEM_LINK_MAP.containsKey(stack.getItem())) {
                return StringIndex.ITEM_LINK_MAP.get(stack.getItem()) + ".asStack(" + (amount > 1 ? amount : "") + ")";
            } else if (StringIndex.ITEM_MAP.containsKey(stack.getItem())) {
                return "new ItemStack(" + StringIndex.ITEM_MAP.get(stack.getItem()) + (amount > 1 ? ", " + amount : "") + ")";
            } else if (ItemUtils.getIdLocation(stack.getItem()).getNamespace().equals(GTOCore.MOD_ID)) {
                return "GTOItems." + ItemUtils.getIdLocation(stack.getItem()).getPath().toUpperCase() + (amount > 1 ? ".asStack(" + amount + ")" : ".asItem()");
            } else if (ItemUtils.getIdLocation(stack.getItem()).getNamespace().equals("minecraft")) {
                return "new ItemStack(Items." + ItemUtils.getIdLocation(stack.getItem()).getPath().toUpperCase() + ".asItem()" + (amount > 1 ? ", " + amount : "") + ")";
            }
        }
        if (re == 1) return "\"" + ItemUtils.getId(ingredient.getItems()[0]) + (amount > 1 ? "\", " + amount : "\"");
        return "RegistriesUtils.getItemStack(\"" + ItemUtils.getId(ingredient.getItems()[0]) + (amount > 1 ? "\", " + amount : "\"") + ")";
    }

    public static String fromFluid(FluidIngredient ingredient, boolean r) {
        if (ingredient.isEmpty() || ingredient.getStacks().length < 1) return null;
        FluidStack stack = ingredient.getStacks()[0];
        ResourceLocation resourceLocation = FluidUtils.getIdLocation(stack.getFluid());
        boolean plasma = false;
        boolean liquid = false;
        boolean molten = false;
        Material material = GTMaterials.get(resourceLocation.toString());
        if (material.isNull() && resourceLocation.toString().contains("_plasma")) {
            material = GTMaterials.get(resourceLocation.toString().replace("_plasma", ""));
            if (!material.isNull()) plasma = true;
        }
        if (material.isNull() && resourceLocation.toString().contains("liquid_")) {
            material = GTMaterials.get(resourceLocation.toString().replace("liquid_", ""));
            if (!material.isNull()) liquid = true;
        }
        if (material.isNull() && resourceLocation.toString().contains("molten_")) {
            material = GTMaterials.get(resourceLocation.toString().replace("molten_", ""));
            if (!material.isNull()) molten = true;
        }
        String m;
        String s;
        if (!material.isNull()) {
            String a = "";
            if (plasma) {
                a = "FluidStorageKeys.PLASMA, ";
            }
            if (liquid) {
                a = "FluidStorageKeys.LIQUID, ";
            }
            if (molten) {
                a = "FluidStorageKeys.MOLTEN, ";
            }
            if (StringIndex.MATERIAL_MAP.containsKey(material)) {
                m = StringIndex.MATERIAL_MAP.get(material);
            } else {
                m = "GTOMaterials." + FormattingUtil.lowerUnderscoreToUpperCamel(material.getName());
            }
            s = (r && a.isEmpty()) ? m + ", " + stack.getAmount() : m + ".getFluid(" + a + stack.getAmount() + ")";
        } else {
            s = "RegistriesUtils.getFluidStack(\"" + resourceLocation + "\", " + stack.getAmount() + ")";
        }
        return s;
    }
}
