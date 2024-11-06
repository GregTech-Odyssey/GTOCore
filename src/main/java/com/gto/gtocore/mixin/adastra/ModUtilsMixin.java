package com.gto.gtocore.mixin.adastra;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import earth.terrarium.adastra.api.planets.Planet;
import earth.terrarium.adastra.common.entities.vehicles.Lander;
import earth.terrarium.adastra.common.registry.ModEntityTypes;
import earth.terrarium.adastra.common.utils.ModUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static earth.terrarium.adastra.common.utils.ModUtils.teleportToDimension;

@Mixin(ModUtils.class)
public abstract class ModUtilsMixin {

    @Inject(method = "canTeleportToPlanet", at = @At("HEAD"), remap = false, cancellable = true)
    private static void canTeleportToPlanet(Player player, Planet targetPlanet, CallbackInfoReturnable<Boolean> cir) {
        if (player.removeTag("spaceelevatorst")) {
            player.addTag("canTeleportToPlanet");
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "land", at = @At("HEAD"), remap = false, cancellable = true)
    private static void land(ServerPlayer player, ServerLevel targetLevel, Vec3 pos, CallbackInfo ci) {
        if (player.removeTag("canTeleportToPlanet")) {
            ci.cancel();
            player.moveTo(pos);
            Lander lander = ModEntityTypes.LANDER.get().create(targetLevel);
            if (lander == null) return;
            lander.setPos(pos);
            targetLevel.addFreshEntity(lander);
            teleportToDimension(player, targetLevel).startRiding(lander);
        }
    }
}
