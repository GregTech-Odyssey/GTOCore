package com.gtocore.mixin.jumbofurnace;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

import commoble.jumbofurnace.jumbo_furnace.JumboFurnaceCoreBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixins for JumboFurnaceCoreBlockEntity.
 */
@Mixin(JumboFurnaceCoreBlockEntity.class)
public abstract class JumboFurnaceCoreBlockEntityMixin {

    @Shadow(remap = false)
    private boolean needsRecipeUpdate;

    /**
     * Fix #1550: smelting an item with a crafting-remaining item (e.g. the gold knife, whose
     * remainder is itself) in the Jumbo Furnace left the input un-consumed and duplicated it.
     *
     * craft() calls recipe.getRemainingItems(wrapper) once per simultaneous recipe, and that method
     * scans the SHARED items-being-smelted handler. So every smelted input that has a crafting
     * remainder gets refunded N times (N = parallel recipe count): the gold knife is never consumed
     * (N=1) and is multiplied (N>1, parallel with other ores) - exactly the report.
     *
     * Vanilla furnaces never refund crafting-remainder items on smelting inputs. Returning no
     * remainders here makes the Jumbo Furnace consume inputs identically to a vanilla furnace.
     * (Fuel-slot container return is handled separately in consumeFuel() and is unaffected.)
     */
    @Redirect(
              method = "craft",
              at = @At(
                       value = "INVOKE",
                       target = "Lnet/minecraft/world/item/crafting/Recipe;getRemainingItems(Lnet/minecraft/world/Container;)Lnet/minecraft/core/NonNullList;",
                       remap = true),
              remap = false)
    private NonNullList<ItemStack> gtocore$noSmeltingInputRefund(Recipe<?> recipe, Container inv) {
        return NonNullList.create();
    }

    /**
     * Fix: when the output inventory changes, re-evaluate recipes so the machine can resume
     * after output space becomes available.
     *
     * Without this, if output was full when updateRecipes() ran, matchAndClaimInputs() returns
     * false for all recipes, cachedRecipes becomes empty, hasRecipes becomes false, and the
     * machine stops. Clearing output only triggers needsOutputUpdate, not needsRecipeUpdate,
     * so updateRecipes() is never called again — the machine stays stuck forever.
     */
    @Inject(method = "onOutputInventoryChanged", at = @At("HEAD"), remap = false)
    private void gtocore$revalidateRecipesOnOutputChange(CallbackInfo ci) {
        needsRecipeUpdate = true;
    }
}
