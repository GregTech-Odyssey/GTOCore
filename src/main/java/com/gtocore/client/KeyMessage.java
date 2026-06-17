package com.gtocore.client;

import com.gtolib.api.network.NetworkPack;

import com.gregtechceu.gtceu.api.item.IGTTool;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class KeyMessage {

    public static void init() {}

    public static final NetworkPack NETWORK_PACK = NetworkPack.registerC2S("keyPressC2S", (p, b) -> pressAction(p, b.readVarInt()));

    private static void pressAction(ServerPlayer player, int type) {
        Level level = player.level();
        if (!level.hasChunkAt(player.blockPosition())) {
            return;
        }
        if (type == 2) {
            upgradeToolSpeed(player);
        }
    }

    private static void upgradeToolSpeed(Player player) {
        ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (itemStack.getItem() instanceof IGTTool gtTool && gtTool.getToolType().name.contains("_vajra")) {
            if (player.isShiftKeyDown()) {
                itemStack.getOrCreateTag().putBoolean("MinersFervor", !itemStack.getOrCreateTag().getBoolean("MinersFervor"));
                player.displayClientMessage(Component.translatable(itemStack.getOrCreateTag().getBoolean("MinersFervor") ?
                        "gui.active" : "gui.inactive",
                        Component.translatable("enchantment.apotheosis.miners_fervor")), true);
                return;
            }
            float speed = itemStack.getOrCreateTag().getFloat("ToolSpeed");
            float newSpeed = adjustToolSpeed(speed, 4, (int) gtTool.getMaterialToolSpeed(itemStack));
            itemStack.getOrCreateTag().putFloat("ToolSpeed", newSpeed);
            player.displayClientMessage(Component.translatable("jade.horseStat.speed", newSpeed), true);
        }
    }

    private static float adjustToolSpeed(float speed, int fallback, int max) {
        if (speed > 0.0F) {
            if (speed * 2 < max) {
                return speed * 2;
            } else if (speed < max) {
                return max;
            }
        }
        return fallback;
    }
}
