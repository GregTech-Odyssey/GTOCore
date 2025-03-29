package com.gto.gtocore.mixin.avaritia;

import com.gto.gtocore.api.entity.IEnhancedPlayer;

import net.minecraft.world.entity.player.Player;

import com.llamalad7.mixinextras.sugar.Local;
import committee.nova.mods.avaritia.init.handler.AbilityHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbilityHandler.class)
public abstract class AbilityHandlerMixin {

    @ModifyArg(method = "updateAbilities", at = @At(value = "INVOKE", target = "Lcommittee/nova/mods/avaritia/init/handler/AbilityHandler;handleChestStateChange(Lnet/minecraft/world/entity/player/Player;Ljava/lang/String;Z)V"), index = 2, remap = false)
    private static boolean modify(boolean hasChest, @Local Player player) {
        if (!hasChest && player instanceof IEnhancedPlayer enhancedPlayer) {
            return enhancedPlayer.gTOCore$canFly();
        }
        return true;
    }

    @Inject(method = "handleChestStateChange", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Lists;newArrayList(Ljava/lang/Iterable;)Ljava/util/ArrayList;"), remap = false, cancellable = true)
    private static void change(Player player, String key, boolean hasChest, CallbackInfo ci) {
        if (!hasChest && player instanceof IEnhancedPlayer enhancedPlayer && enhancedPlayer.gTOCore$canFly()) {
            ci.cancel();
        }
    }
}
