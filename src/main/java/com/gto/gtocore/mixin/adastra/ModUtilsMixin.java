package com.gto.gtocore.mixin.adastra;

import com.gto.gtocore.api.data.GTODimensions;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import earth.terrarium.adastra.api.planets.Planet;
import earth.terrarium.adastra.common.config.AdAstraConfig;
import earth.terrarium.adastra.common.entities.vehicles.Lander;
import earth.terrarium.adastra.common.entities.vehicles.Rocket;
import earth.terrarium.adastra.common.menus.PlanetsMenu;
import earth.terrarium.adastra.common.registry.ModEntityTypes;
import earth.terrarium.adastra.common.utils.ModUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static earth.terrarium.adastra.common.utils.ModUtils.teleportToDimension;

@Mixin(ModUtils.class)
public final class ModUtilsMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static boolean canTeleportToPlanet(Player player, Planet targetPlanet) {
        if (player.removeTag("spaceelevatorst")) {
            player.addTag("canTeleportToPlanet");
            return true;
        }
        if (!(player.containerMenu instanceof PlanetsMenu)) {
            return false;
        } else if (!player.isCreative() && !player.isSpectator()) {
            String[] planets = AdAstraConfig.disabledPlanets.split(",");

            for (String planet : planets) {
                if (planet.equals(targetPlanet.dimension().location().toString())) {
                    return false;
                }
            }

            Entity var8 = player.getVehicle();
            if (var8 instanceof Rocket rocket) {
                if (rocket.getY() < (double) AdAstraConfig.atmosphereLeave) {
                    return false;
                } else {
                    ResourceLocation resourceLocation = targetPlanet.dimension().location();
                    int tier = GTODimensions.PLANET_DISTANCES.containsKey(resourceLocation) ? GTODimensions.calculateDistance(rocket.level().dimension().location(), resourceLocation) : targetPlanet.tier();
                    return tier <= rocket.tier();
                }
            } else {
                return false;
            }
        } else {
            return true;
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
