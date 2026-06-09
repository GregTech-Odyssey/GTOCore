package com.gtocore.eio_travel.client;

import com.gtocore.eio_travel.api.ITravelTarget;
import com.gtocore.eio_travel.api.TravelRegistry;
import com.gtocore.eio_travel.logic.TravelHandler;
import com.gtocore.eio_travel.logic.TravelSavedData;
import com.gtocore.eio_travel.logic.TravelUtils;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class RenderTravelTargets {

    @SubscribeEvent
    public static void renderLevel(RenderLevelStageEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        LocalPlayer player = minecraft.player;
        if (level == null || player == null || event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) {
            return;
        }

        if (!TravelHandler.canTeleport(player)) {
            return;
        }

        boolean itemTeleport = TravelHandler.canItemTeleport(player);
        TravelSavedData data = TravelSavedData.getTravelData(level);
        @Nullable
        ITravelTarget activeTarget = TravelHandler.getTeleportAnchorTarget(player).orElse(null);
        var targets = TravelUtils.filterTargets(player, data.getTravelTargets().stream()).toList();
        PoseStack poseStack = event.getPoseStack();
        Camera mainCamera = minecraft.gameRenderer.getMainCamera();
        Vec3 projectedView = mainCamera.getPosition();
        for (ITravelTarget target : targets) {
            double range = itemTeleport ? target.getItem2BlockRange() : target.getBlock2BlockRange();
            double distanceSquared = target.getPos().distToCenterSqr(player.position());
            if (range * range < distanceSquared || distanceSquared < TravelHandler.MIN_TELEPORTATION_DISTANCE_SQUARED || TravelUtils.gto$isTeleportPositionAndSurroundingClear(level, target.getPos()).isEmpty()) {
                continue;
            }

            poseStack.pushPose();
            poseStack.translate(-projectedView.x, -projectedView.y, -projectedView.z);

            boolean active = activeTarget == target;
            TravelRegistry.getRenderer(target).render(target, event.getLevelRenderer(), poseStack, distanceSquared, active);
            poseStack.popPose();
        }
    }
}
