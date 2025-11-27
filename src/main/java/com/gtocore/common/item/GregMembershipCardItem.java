package com.gtocore.common.item;

import com.gtocore.common.data.GTOItems;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GregMembershipCardItem extends Item {

    private static final String TAG_MEMBERSHIP_DATA = "membership_data";
    private static final String TAG_UUID = "uuid";
    private static final String TAG_SHARED = "shared";

    public GregMembershipCardItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    /**
     * 安全地获取或创建 membership_data CompoundTag。
     */
    private static @NotNull CompoundTag getMembershipTag(@NotNull ItemStack stack) {
        return stack.getOrCreateTagElement(TAG_MEMBERSHIP_DATA);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        UUID ownerUuid = getSingleUuid(stack);
        Component ownerNameComponent = Component.translatable("gtocore.gray_membership_card.hover_text.1");
        if (ownerUuid != null && level != null) {
            Player ownerPlayer = level.getPlayerByUUID(ownerUuid);
            ownerNameComponent = (ownerPlayer != null) ? ownerPlayer.getName() :
                    Component.translatable("gtocore.gray_membership_card.hover_text.2");
        }

        List<UUID> sharedUuids = getSharedUuids(stack);
        List<Component> sharedNameComponents = new ArrayList<>();
        if (level != null) {
            for (UUID sharedUuid : sharedUuids) {
                Player sharedPlayer = level.getPlayerByUUID(sharedUuid);
                sharedNameComponents.add((sharedPlayer != null) ? sharedPlayer.getName() :
                        Component.translatable("gtocore.gray_membership_card.hover_text.2"));
            }
        }

        tooltip.add(Component.translatable("gtocore.gray_membership_card.hover_text.3").append(ownerNameComponent));
        if (!sharedNameComponents.isEmpty()) {
            MutableComponent sharedNamesJoined = Component.empty();
            for (int i = 0; i < sharedNameComponents.size(); i++) {
                if (i > 0) {
                    sharedNamesJoined.append(", ");
                }
                sharedNamesJoined.append(sharedNameComponents.get(i));
            }
            tooltip.add(Component.translatable("gtocore.gray_membership_card.hover_text.4").append(sharedNamesJoined));
        }
    }

    /**
     * 根据单个 UUID 创建会员卡物品（使用原生 UUID 存储）。
     */
    public static ItemStack createWithUuid(@NotNull UUID uuid) {
        ItemStack stack = new ItemStack(GTOItems.GREG_MEMBERSHIP_CARD.get());
        CompoundTag membershipTag = getMembershipTag(stack);
        membershipTag.putUUID(TAG_UUID, uuid);
        return stack;
    }

    /**
     * 根据单个 UUID 和 UUID 列表创建会员卡物品（使用原生 UUID 存储）。
     */
    public static ItemStack createWithUuidAndSharedList(@NotNull UUID uuid, @Nullable List<UUID> sharedUuids) {
        ItemStack stack = createWithUuid(uuid);
        CompoundTag membershipTag = getMembershipTag(stack);

        if (sharedUuids == null || sharedUuids.isEmpty()) {
            return stack;
        }

        ListTag sharedListTag = new ListTag();
        for (UUID sharedUuid : sharedUuids) {
            sharedListTag.add(NbtUtils.createUUID(sharedUuid));
        }
        membershipTag.put(TAG_SHARED, sharedListTag);

        return stack;
    }

    /**
     * 检查指定 UUID 是否存在于物品 NBT 中（主人或共享者）。
     */
    public static boolean isUuidPresent(@NotNull ItemStack stack, @NotNull UUID uuidToCheck) {
        CompoundTag membershipTag = getMembershipTag(stack);

        // 检查主人 UUID
        if (membershipTag.hasUUID(TAG_UUID) && membershipTag.getUUID(TAG_UUID).equals(uuidToCheck)) {
            return true;
        }

        // 检查共享者列表
        if (membershipTag.contains(TAG_SHARED, Tag.TAG_LIST)) {
            ListTag sharedListTag = membershipTag.getList(TAG_SHARED, Tag.TAG_INT_ARRAY);
            for (Tag tag : sharedListTag) {
                try {
                    if (NbtUtils.loadUUID(tag).equals(uuidToCheck)) {
                        return true;
                    }
                } catch (ClassCastException | IllegalArgumentException e) {
                    // 忽略无效的标签或UUID
                }
            }
        }

        return false;
    }

    /**
     * 从物品中读取主人 UUID（使用原生方法）。
     */
    @Nullable
    public static UUID getSingleUuid(@NotNull ItemStack stack) {
        CompoundTag membershipTag = getMembershipTag(stack);
        if (membershipTag.hasUUID(TAG_UUID)) {
            try {
                return membershipTag.getUUID(TAG_UUID);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 从物品中读取共享者 UUID 列表（使用原生方法）。
     */
    @NotNull
    public static List<UUID> getSharedUuids(@NotNull ItemStack stack) {
        List<UUID> sharedUuids = new ArrayList<>();
        CompoundTag membershipTag = getMembershipTag(stack);

        if (!membershipTag.contains(TAG_SHARED, Tag.TAG_LIST)) {
            return sharedUuids;
        }

        ListTag sharedListTag = membershipTag.getList(TAG_SHARED, Tag.TAG_INT_ARRAY);
        for (Tag tag : sharedListTag) {
            if (tag instanceof IntArrayTag) {
                try {
                    sharedUuids.add(NbtUtils.loadUUID(tag));
                } catch (IllegalArgumentException ignored) {}
            }
        }

        return sharedUuids;
    }
}
