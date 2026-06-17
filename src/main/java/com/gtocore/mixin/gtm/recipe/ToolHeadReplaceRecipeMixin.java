package com.gtocore.mixin.gtm.recipe;

import com.gtolib.api.item.tool.GTOToolType;

import com.gregtechceu.gtceu.api.item.IGTTool;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.common.recipe.ToolHeadReplaceRecipe;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ToolHeadReplaceRecipe.class, remap = false)
public final class ToolHeadReplaceRecipeMixin {

    @Inject(method = "matches", at = @At("HEAD"), cancellable = true)
    private void matches(CraftingContainer inv, Level level, CallbackInfoReturnable<Boolean> cir) {
        if (hasVajra(inv)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "assemble", at = @At("HEAD"), cancellable = true)
    private void assemble(CraftingContainer inv, RegistryAccess registryAccess, CallbackInfoReturnable<ItemStack> cir) {
        if (hasVajra(inv)) {
            cir.setReturnValue(ItemStack.EMPTY);
        }
    }

    private static boolean hasVajra(CraftingContainer inv) {
        for (int i = 0; i < inv.getContainerSize(); i++) {
            if (inv.getItem(i).getItem() instanceof IGTTool tool && isVajra(tool.getToolType())) {
                return true;
            }
        }
        return false;
    }

    private static boolean isVajra(GTToolType toolType) {
        return toolType == GTOToolType.VAJRA_HV || toolType == GTOToolType.VAJRA_EV ||
                toolType == GTOToolType.VAJRA_IV;
    }
}
