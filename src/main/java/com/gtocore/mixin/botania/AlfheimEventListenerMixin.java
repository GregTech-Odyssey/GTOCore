package com.gtocore.mixin.botania;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;
import mythicbotany.MythicPlayerData;
import mythicbotany.alfheim.Alfheim;
import mythicbotany.alfheim.teleporter.AlfheimPortalHandler;
import mythicbotany.alfheim.teleporter.AlfheimTeleporter;
import mythicbotany.config.MythicConfig;
import mythicbotany.register.ModBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import vazkii.botania.api.recipe.ElvenPortalUpdateEvent;
import vazkii.botania.common.block.block_entity.AlfheimPortalBlockEntity;
import vazkii.botania.common.item.BotaniaItems;

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
        if (event.isOpen() && portal.getLevel() != null && !portal.getLevel().isClientSide) {
            if (Alfheim.DIMENSION.equals(portal.getLevel().dimension())) {
                if (portal instanceof AlfheimPortalBlockEntity alfPortal) {
                    alfPortal.consumeMana(new ArrayList<>(), 0, true);
                }
            }

            if (Level.OVERWORLD.equals(portal.getLevel().dimension()) && AlfheimPortalHandler.shouldCheck(portal.getLevel())) {
                List<Player> playersInPortal = portal.getLevel().getEntitiesOfClass(Player.class, event.getAabb());
                for (Player player : playersInPortal) {
                    if (player instanceof ServerPlayer serverPlayer && MythicPlayerData.getData(serverPlayer).getBoolean("KvasirKnowledge") && gtocore$additionalChecks(serverPlayer)) {
                        if (AlfheimPortalHandler.setInPortal(portal.getLevel(), serverPlayer)) {
                            if (!AlfheimTeleporter.teleportToAlfheim(serverPlayer, portal.getBlockPos())) {
                                serverPlayer.sendSystemMessage(Component.translatable("message.mythicbotany.alfheim_not_loaded"));
                            }
                        }
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
        if (player.tickCount % 20 == 1 && !player.level().isClientSide && Alfheim.DIMENSION == player.level().dimension() && !player.isCreative()) {
            if (MythicConfig.lockAlfheim && !MythicPlayerData.getData(player).getBoolean("KvasirKnowledge") && !MythicPlayerData.getData(player).getBoolean("enterAlfheim"))
                player.kill();
        }
    }

    @Unique
    private boolean gtocore$additionalChecks(ServerPlayer player) {
        Item[] REQUIRED_ITEMS = {
                BotaniaItems.kingKey,
                BotaniaItems.flugelEye,
                BotaniaItems.infiniteFruit,
                BotaniaItems.thorRing,
                BotaniaItems.odinRing,
                BotaniaItems.lokiRing,
                ModBlocks.mjoellnir.asItem(),
                ExtraBotanyItems.excalibur,
                ExtraBotanyItems.failnaught,
                ExtraBotanyItems.rheinHammer,
                ExtraBotanyItems.achillesShield,
                ExtraBotanyItems.voidArchives };
        for (Item requiredItem : REQUIRED_ITEMS) {
            boolean itemExists = gtocore$checkItemInIterable(requiredItem, player.getInventory().items) || gtocore$checkItemInIterable(requiredItem, List.of(player.getOffhandItem())) || gtocore$checkItemInIterable(requiredItem, gtocore$getCuriosItemStacks(player));
            if (!itemExists) return false;
        }
        if (!MythicPlayerData.getData(player).getBoolean("enterAlfheim")) {
            MythicPlayerData.getData(player).putBoolean("enterAlfheim", true);
        }
        return true;
    }

    @Unique
    private Iterable<ItemStack> gtocore$getCuriosItemStacks(Player player) {
        List<ItemStack> curiosItems = new ArrayList<>();
        LazyOptional<ICuriosItemHandler> curiosHandlerOpt = CuriosApi.getCuriosInventory(player);
        if (curiosHandlerOpt.isPresent()) {
            for (ICurioStacksHandler slotHandler : curiosHandlerOpt.resolve().get().getCurios().values()) {
                for (int slotIndex = 0; slotIndex < slotHandler.getSlots(); slotIndex++) {
                    ItemStack stack = slotHandler.getStacks().getStackInSlot(slotIndex);
                    if (!stack.isEmpty()) {
                        curiosItems.add(stack);
                    }
                }
            }
        }
        return curiosItems;
    }

    @Unique
    private boolean gtocore$checkItemInIterable(Item targetItem, Iterable<ItemStack> iterable) {
        for (ItemStack stack : iterable) {
            if (!stack.isEmpty() && stack.getItem() == targetItem) return true;
        }
        return false;
    }
}
