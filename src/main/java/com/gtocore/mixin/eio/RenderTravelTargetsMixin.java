package com.gtocore.mixin.eio;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.enderio.base.client.renderer.travel.RenderTravelTargets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = RenderTravelTargets.class, remap = false)
@OnlyIn(Dist.CLIENT)
public class RenderTravelTargetsMixin {

    @Redirect(method = "renderLevel", at = @At(value = "INVOKE", target = "Lcom/enderio/base/common/handler/TravelHandler;isTeleportPositionClear(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Ljava/util/Optional;", remap = false), remap = false)
    private static java.util.Optional<Double> gto$redirectIsTeleportPositionClear(net.minecraft.world.level.BlockGetter level, net.minecraft.core.BlockPos target) {
        return com.gtocore.integration.eio.ITravelHandlerHook.gto$isTeleportPositionAndSurroundingClear(level, target);
    }
}
