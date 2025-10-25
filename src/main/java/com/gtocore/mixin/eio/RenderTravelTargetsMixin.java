package com.gtocore.mixin.eio;

import com.gtocore.integration.eio.ITravelHandlerHook;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.enderio.api.travel.ITravelTarget;
import com.enderio.base.client.renderer.travel.RenderTravelTargets;
import com.enderio.base.common.travel.TravelSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;

@Mixin(value = RenderTravelTargets.class, remap = false)
@OnlyIn(Dist.CLIENT)
public class RenderTravelTargetsMixin {

    @Redirect(method = "renderLevel", at = @At(value = "INVOKE", target = "Lcom/enderio/base/common/handler/TravelHandler;isTeleportPositionClear(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Ljava/util/Optional;"))
    private static java.util.Optional<Double> gto$redirectIsTeleportPositionClear(net.minecraft.world.level.BlockGetter level, net.minecraft.core.BlockPos target) {
        return ITravelHandlerHook.gto$isTeleportPositionAndSurroundingClear(level, target);
    }

    @Redirect(method = "renderLevel", at = @At(value = "INVOKE", target = "Lcom/enderio/base/common/travel/TravelSavedData;getTravelTargets()Ljava/util/Collection;"))
    private static Collection<ITravelTarget> gto$filterRenderTargets(TravelSavedData instance) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            return ITravelHandlerHook.filterTargets(player, instance.getTravelTargets().stream()).toList();
        }
        return instance.getTravelTargets();
    }
}
