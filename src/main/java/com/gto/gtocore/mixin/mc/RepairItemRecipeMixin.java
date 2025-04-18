package com.gto.gtocore.mixin.mc;

import com.gregtechceu.gtceu.api.item.IGTTool;

import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RepairItemRecipe;
import net.minecraft.world.level.Level;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = RepairItemRecipe.class, priority = 0)
public class RepairItemRecipeMixin {

    @Inject(method = "matches(Lnet/minecraft/world/inventory/CraftingContainer;Lnet/minecraft/world/level/Level;)Z", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"), cancellable = true)
    public void matches(CraftingContainer inv, Level level, CallbackInfoReturnable<Boolean> cir, @Local ItemStack itemstack) {
        if (itemstack.getItem() instanceof IGTTool) cir.setReturnValue(false);
    }
}
