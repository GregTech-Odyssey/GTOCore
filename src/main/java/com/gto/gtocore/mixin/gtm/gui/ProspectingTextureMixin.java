package com.gto.gtocore.mixin.gtm.gui;

import com.gto.gtocore.client.gui.IProspectingTextureMixin;

import com.gregtechceu.gtceu.api.gui.texture.ProspectingTexture;

import com.lowdragmc.lowdraglib.LDLib;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import dev.ftb.mods.ftbchunks.api.FTBChunksAPI;
import dev.ftb.mods.ftbchunks.net.TeleportFromMapPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author EasterFG on 2024/9/16
 */
@Mixin(ProspectingTexture.class)
public abstract class ProspectingTextureMixin extends AbstractTexture implements IProspectingTextureMixin {

    @Shadow(remap = false)
    @Final
    private int playerXGui;

    @Shadow(remap = false)
    @Final
    private int playerYGui;

    @Unique
    @Override
    public void gTLCore$addWayPoint(Player player, double mouseX, double mouseY) {
        if (!LDLib.isModLoaded("ftbchunks")) {
            return;
        }

        var mgr = FTBChunksAPI.clientApi().getWaypointManager(player.level().dimension()).orElse(null);
        if (mgr == null) return;

        BlockPos pos = this.gTLCore$getBlockPos(player, mouseX, mouseY);
        AtomicInteger index = new AtomicInteger();
        mgr.getAllWaypoints().forEach(waypoint -> {
            if (waypoint.getName().startsWith("prospect_point")) {
                int i = Integer.parseInt(waypoint.getName().replace("prospect_point", ""));
                index.set(Math.max(i, index.get()));
            }
        });
        mgr.addWaypointAt(pos, "prospect_point" + index.incrementAndGet()).setColor(200);
    }

    @Unique
    @Override
    public void gTLCore$teleportWayPoint(Player player, double mouseX, double mouseY) {
        if (!LDLib.isModLoaded("ftbchunks")) return;
        Level level = player.level();
        BlockPos pos = gTLCore$getBlockPos(player, mouseX, mouseY);
        new TeleportFromMapPacket(pos, true, level.dimension()).sendToServer();
    }

    @Unique
    private BlockPos gTLCore$getBlockPos(Player player, double mouseX, double mouseY) {
        double x, z;
        if (playerXGui % 16 > 7 || playerXGui % 16 == 0) {
            x = mouseX - (playerXGui - 1);
        } else {
            x = mouseX - playerXGui;
        }
        if (playerYGui % 16 > 7 || playerYGui % 16 == 0) {
            z = mouseY - (playerYGui - 1);
        } else {
            z = mouseY - playerYGui;
        }
        return new BlockPos((int) (player.position().x + x), 0, (int) (player.position().z + z));
    }
}
