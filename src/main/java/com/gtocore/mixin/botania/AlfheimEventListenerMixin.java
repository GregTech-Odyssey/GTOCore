package com.gtocore.mixin.botania;

import com.gtocore.integration.mythic_botany.AlfheimHelper;

import com.gtolib.api.data.Dimension;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import mythicbotany.alfheim.Alfheim;
import mythicbotany.alfheim.teleporter.AlfheimPortalHandler;
import mythicbotany.alfheim.teleporter.AlfheimTeleporter;
import mythicbotany.config.MythicConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import vazkii.botania.api.recipe.ElvenPortalUpdateEvent;
import vazkii.botania.common.block.block_entity.AlfheimPortalBlockEntity;

import java.util.ArrayList;
import java.util.List;

@Mixin(mythicbotany.EventListener.class)
public abstract class AlfheimEventListenerMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    @SubscribeEvent
    public void alfPortalUpdate(ElvenPortalUpdateEvent event) {
        BlockEntity portal = event.getPortalTile();
        if (!event.isOpen() || portal.getLevel() == null || portal.getLevel().isClientSide) {
            return;
        }
        Level level = portal.getLevel();
        Dimension dimension = Dimension.from(level);
        if (dimension == Dimension.ALFHEIM) {
            if (portal instanceof AlfheimPortalBlockEntity alfPortal) {
                alfPortal.consumeMana(new ArrayList<>(), 0, true);
            }
            return;
        }
        if (!AlfheimPortalHandler.shouldCheck(level)) {
            return;
        }
        List<Player> playersInPortal = level.getEntitiesOfClass(Player.class, event.getAabb());
        if (playersInPortal.isEmpty()) {
            return;
        }
        BlockPos portalPos = portal.getBlockPos();
        for (Player player : playersInPortal) {
            if (player instanceof ServerPlayer serverPlayer && AlfheimHelper.gtocore$canPlayerUsePortal(serverPlayer)) {
                if (AlfheimPortalHandler.setInPortal(serverPlayer.level(), serverPlayer)) {
                    if (dimension == Dimension.OVERWORLD) {
                        if (!AlfheimTeleporter.teleportToAlfheim(serverPlayer, portalPos)) {
                            serverPlayer.sendSystemMessage(Component.translatable("message.mythicbotany.alfheim_not_loaded"));
                        }
                    } else {
                        serverPlayer.sendSystemMessage(Component.translatable("message.mythicbotany.alfheim_overworld_only"));
                    }

                }
            }
        }
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player.tickCount % 20 == 1 && player instanceof ServerPlayer s && Alfheim.DIMENSION == player.level().dimension() && !player.isCreative()) {
            if (MythicConfig.lockAlfheim && !AlfheimHelper.gtocore$canPlayerUsePortal(s))
                player.kill();
        }
    }
}
