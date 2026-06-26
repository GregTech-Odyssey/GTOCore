package com.gtocore.mixin.mc;

import com.gtolib.api.player.IEnhancedPlayer;
import com.gtolib.api.player.attribute.PlayerAttributes;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.client.renderer.GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "getNightVisionScale", at = @At("HEAD"), cancellable = true)
    private static void onGetNightVisionScale(LivingEntity livingEntity, float nanoTime, CallbackInfoReturnable<Float> cir) {
        if (livingEntity instanceof Player player) {
            if (nightVision(player)) {
                cir.setReturnValue(1.0f);
            } else {
                cir.setReturnValue(0.0f);
            }
        }
    }

    private static boolean nightVision(Player player) {
        var ep = IEnhancedPlayer.of(player);
        if (ep == null) return false;
        var data = ep.getPlayerData();
        if (data == null) return false;
        var attrs = data.getPlayerAttributes();
        if (attrs == null) return false;
        return attrs.getBooleanCurrent(PlayerAttributes.NIGHT_VISION);
    }
}
