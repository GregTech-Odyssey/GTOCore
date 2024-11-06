package com.gto.gtocore.client.gui;

import net.minecraft.world.entity.player.Player;

import org.spongepowered.asm.mixin.Unique;

/**
 * @author EasterFG on 2024/9/16
 */
public interface IProspectingTextureMixin {

    void gTLCore$addWayPoint(Player player, double mouseX, double mouseY);

    @Unique
    void gTLCore$teleportWayPoint(Player player, double mouseX, double mouseY);
}
