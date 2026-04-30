package com.gtocore.mixin.ftbq;

import com.gtocore.integration.ftbquests.PendingItemRewardDelivery;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import dev.architectury.hooks.item.ItemStackHooks;
import dev.ftb.mods.ftbquests.quest.reward.ItemReward;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemReward.class)
public class ItemRewardMixin {

    @Redirect(method = "claim", at = @At(value = "INVOKE", target = "Ldev/architectury/hooks/item/ItemStackHooks;giveItem(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/item/ItemStack;)V"), remap = false)
    private void gtocore$deferRewardUntilRespawn(ServerPlayer player, ItemStack stack) {
        if (PendingItemRewardDelivery.shouldDefer(player)) {
            PendingItemRewardDelivery.defer(player, stack);
        } else {
            ItemStackHooks.giveItem(player, stack);
        }
    }
}
