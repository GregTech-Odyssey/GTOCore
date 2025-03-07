package com.gto.gtocore.mixin.apotheosis;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import dev.shadowsoffire.apotheosis.adventure.loot.LootCategory;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(LootCategory.class)
public abstract class LootCategoryMixin {

    @Shadow(remap = false)
    public static LootCategory register(@Nullable LootCategory orderRef, String name, Predicate<ItemStack> validator, EquipmentSlot[] slots) {
        return null;
    }

    @Shadow(remap = false)
    private static EquipmentSlot[] arr(EquipmentSlot... s) {
        return null;
    }

    @Inject(method = "register(Ljava/lang/String;Ljava/util/function/Predicate;[Lnet/minecraft/world/entity/EquipmentSlot;)Ldev/shadowsoffire/apotheosis/adventure/loot/LootCategory;", at = @At("HEAD"), remap = false, cancellable = true)
    private static void register(String name, Predicate<ItemStack> validator, EquipmentSlot[] slots, CallbackInfoReturnable<LootCategory> cir) {
        if (name.equals("heavy_weapon")) {
            cir.setReturnValue(register(null, name, i -> false, arr(EquipmentSlot.MAINHAND)));
        }
    }
}
