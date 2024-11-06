package com.gto.gtocore.mixin.gtm.gui;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.common.item.ProspectorScannerBehavior;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.gui.factory.HeldItemUIFactory;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;

import net.minecraft.world.entity.player.Player;

import dev.ftb.mods.ftbchunks.api.FTBChunksAPI;
import dev.ftb.mods.ftbchunks.api.client.waypoint.Waypoint;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author EasterFG on 2024/9/28
 */
@Mixin(ProspectorScannerBehavior.class)
public class ProspectorScannerBehaviorMixin {

    @Inject(method = "createUI", at = @At("RETURN"), remap = false)
    public void createUIHook(HeldItemUIFactory.HeldItemHolder holder, Player entityPlayer, CallbackInfoReturnable<ModularUI> cir) {
        cir.getReturnValue().widget(new ButtonWidget(-20, 26, 18, 18, data -> gTLCore$removeAllPoint(entityPlayer))
                .setButtonTexture(GuiTextures.BUTTON, GuiTextures.CLOSE_ICON.copy().scale(0.8f)));
    }

    @Unique
    public void gTLCore$removeAllPoint(Player player) {
        if (!LDLib.isModLoaded("ftbchunks")) return;
        // 只会移除玩家所在维度的标点
        var mgr = FTBChunksAPI.clientApi().getWaypointManager(player.level().dimension())
                .orElse(null);
        if (mgr == null) {
            return;
        }

        List<Waypoint> tempPoint = new ArrayList<>();
        mgr.getAllWaypoints().stream()
                .filter(way -> way.getName().contains("prospect_point"))
                .forEach(tempPoint::add);
        tempPoint.forEach(mgr::removeWaypoint);
    }
}
