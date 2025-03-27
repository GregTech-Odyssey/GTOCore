package com.gto.gtocore.mixin.gtm.capability;

import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.utils.ItemUtils;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.item.TagPrefixItem;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;

import net.minecraft.world.item.crafting.Ingredient;

import com.llamalad7.mixinextras.sugar.Local;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemRecipeCapability.class)
public class ItemRecipeCapabilityMixin {

    @Redirect(method = "applyWidgetInfo", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/api/gui/widget/SlotWidget;setXEIChance(F)Lcom/gregtechceu/gtceu/api/gui/widget/SlotWidget;"), remap = false)
    private SlotWidget setXEIChance(SlotWidget instance, float XEIChance, @Local(argsOnly = true) IO io, @Local(argsOnly = true) Content content) {
        if (io == IO.IN) {
            Ingredient ingredient = ItemRecipeCapability.CAP.of(content.getContent());
            if (ingredient instanceof SizedIngredient sizedIngredient && ItemUtils.getFirstSized(sizedIngredient).getItem() instanceof TagPrefixItem item && item.tagPrefix == GTOTagPrefix.CATALYST) {
                instance.setIngredientIO(IngredientIO.CATALYST);
                return instance.setXEIChance(0);
            }
        }
        return instance.setXEIChance(XEIChance);
    }
}
