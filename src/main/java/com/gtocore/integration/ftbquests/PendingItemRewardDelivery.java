package com.gtocore.integration.ftbquests;

import com.gtolib.GTOCore;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import dev.architectury.hooks.item.ItemStackHooks;

public final class PendingItemRewardDelivery {

    private static final String PENDING_REWARDS_TAG = "gtocore.ftbquests_pending_item_rewards";

    private PendingItemRewardDelivery() {}

    public static boolean shouldDefer(ServerPlayer player) {
        return !player.isAlive() || player.isRemoved() || player.getHealth() <= 0.0F;
    }

    public static void defer(ServerPlayer player, ItemStack stack) {
        if (stack.isEmpty()) return;

        CompoundTag persistedData = getPersistedData(player);
        ListTag pendingRewards = persistedData.contains(PENDING_REWARDS_TAG, Tag.TAG_LIST) ?
                persistedData.getList(PENDING_REWARDS_TAG, Tag.TAG_COMPOUND) :
                new ListTag();
        pendingRewards.add(stack.save(new CompoundTag()));
        persistedData.put(PENDING_REWARDS_TAG, pendingRewards);
    }

    public static void deliver(ServerPlayer player) {
        CompoundTag persistedData = getPersistedData(player);
        if (!persistedData.contains(PENDING_REWARDS_TAG, Tag.TAG_LIST)) return;

        ListTag pendingRewards = persistedData.getList(PENDING_REWARDS_TAG, Tag.TAG_COMPOUND).copy();
        persistedData.remove(PENDING_REWARDS_TAG);

        for (int i = 0; i < pendingRewards.size(); i++) {
            ItemStack stack = ItemStack.of(pendingRewards.getCompound(i));
            if (!stack.isEmpty()) {
                ItemStackHooks.giveItem(player, stack);
            }
        }
    }

    private static CompoundTag getPersistedData(Player player) {
        CompoundTag persistentData = player.getPersistentData();
        CompoundTag persistedData = persistentData.getCompound(Player.PERSISTED_NBT_TAG);
        persistentData.put(Player.PERSISTED_NBT_TAG, persistedData);
        return persistedData;
    }

    @Mod.EventBusSubscriber(modid = GTOCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static final class Events {

        private Events() {}

        @SubscribeEvent
        public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
            if (event.getEntity() instanceof ServerPlayer player) {
                player.server.execute(() -> {
                    if (!shouldDefer(player)) {
                        deliver(player);
                    }
                });
            }
        }
    }
}
