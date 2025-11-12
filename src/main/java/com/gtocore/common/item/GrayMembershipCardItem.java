package com.gtocore.common.item;

import com.gtocore.common.data.GTOItems;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

public class GrayMembershipCardItem extends Item {

    public static final String MEMBERSHIP_CARD = "membership_card";
    public static final String UUID_TAG = "uuid";
    public static final String SHARED_TAG = "shared";

    public GrayMembershipCardItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        // 1. 获取主人 UUID 和名称
        UUID ownerUuid = getSingleUuid(stack);
        String ownerName = Component.translatable("gtocore.gray_membership_card.hover_text.1").getString();
        if (ownerUuid != null && level != null) {
            Player ownerPlayer = level.getPlayerByUUID(ownerUuid);
            ownerName = (ownerPlayer != null) ? ownerPlayer.getName().getString() :
                    Component.translatable("gtocore.gray_membership_card.hover_text.2").getString();
        }

        // 2. 获取共享者 UUID 列表和名称列表
        List<UUID> sharedUuids = getSharedUuids(stack);
        List<String> sharedNames = new ArrayList<>();
        if (level != null) {
            for (UUID sharedUuid : sharedUuids) {
                Player sharedPlayer = level.getPlayerByUUID(sharedUuid);
                sharedNames.add((sharedPlayer != null) ? sharedPlayer.getName().getString() :
                        Component.translatable("gtocore.gray_membership_card.hover_text.2").getString());
            }
        }

        // 3. 添加到 Tooltip
        tooltip.add(Component.translatable("gtocore.gray_membership_card.hover_text.3")
                .append(Component.literal(ownerName)));
        if (!sharedNames.isEmpty()) {
            tooltip.add(Component.translatable("gtocore.gray_membership_card.hover_text.4")
                    .append(Component.literal(String.join(", ", sharedNames))));
        }
    }

    /**
     * 方法1：根据单个 UUID 创建会员卡物品
     */
    public static ItemStack createWithUuid(@NotNull UUID uuid) {
        // 直接使用你的物品注册实例
        ItemStack stack = GTOItems.GRAY_MEMBERSHIP_CARD.asStack();

        CompoundTag rootTag = stack.getOrCreateTag();

        // 手动实现 getOrCreateCompound 的逻辑
        CompoundTag membershipTag;
        if (rootTag.contains(MEMBERSHIP_CARD, Tag.TAG_COMPOUND)) {
            membershipTag = rootTag.getCompound(MEMBERSHIP_CARD);
        } else {
            membershipTag = new CompoundTag();
            rootTag.put(MEMBERSHIP_CARD, membershipTag);
        }

        membershipTag.putString(UUID_TAG, uuid.toString());

        // 注意：在1.20.1中，修改了CompoundTag后，需要重新设置回ItemStack
        stack.setTag(rootTag);

        return stack;
    }

    /**
     * 方法2：根据单个 UUID 和 UUID 列表创建会员卡物品
     */
    public static ItemStack createWithUuidAndSharedList(@NotNull UUID uuid, @Nullable List<UUID> sharedUuids) {
        ItemStack stack = createWithUuid(uuid);
        CompoundTag rootTag = stack.getTag();
        if (rootTag == null) {
            rootTag = new CompoundTag();
        }

        // 手动获取或创建主标签
        CompoundTag membershipTag = rootTag.getCompound(MEMBERSHIP_CARD);

        ListTag sharedListTag = new ListTag();
        if (sharedUuids != null && !sharedUuids.isEmpty()) {
            for (UUID sharedUuid : sharedUuids) {
                sharedListTag.add(StringTag.valueOf(sharedUuid.toString()));
            }
        }

        membershipTag.put(SHARED_TAG, sharedListTag);
        rootTag.put(MEMBERSHIP_CARD, membershipTag);
        stack.setTag(rootTag);

        return stack;
    }

    /**
     * 方法3：检查指定 UUID 是否存在于物品 NBT 中
     */
    public static boolean isUuidPresent(@NotNull ItemStack stack, @NotNull UUID uuidToCheck) {
        if (stack.isEmpty() || !(stack.getItem() instanceof GrayMembershipCardItem) || !stack.hasTag()) {
            return false;
        }

        CompoundTag rootTag = stack.getTag();
        assert rootTag != null;

        if (!rootTag.contains(MEMBERSHIP_CARD, Tag.TAG_COMPOUND)) {
            return false;
        }

        CompoundTag membershipTag = rootTag.getCompound(MEMBERSHIP_CARD);
        String targetUuidStr = uuidToCheck.toString();

        // 检查单个 UUID
        if (membershipTag.contains(UUID_TAG, Tag.TAG_STRING) && membershipTag.getString(UUID_TAG).equals(targetUuidStr)) {
            return true;
        }

        // 检查共享列表
        if (membershipTag.contains(SHARED_TAG, Tag.TAG_LIST)) {
            ListTag sharedListTag = membershipTag.getList(SHARED_TAG, Tag.TAG_STRING);
            for (Tag tag : sharedListTag) {
                if (tag instanceof StringTag && tag.getAsString().equals(targetUuidStr)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 辅助方法：从物品中读取单个 UUID
     */
    @Nullable
    public static UUID getSingleUuid(@NotNull ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof GrayMembershipCardItem) || !stack.hasTag()) {
            return null;
        }

        CompoundTag rootTag = stack.getTag();
        assert rootTag != null;
        if (!rootTag.contains(MEMBERSHIP_CARD, Tag.TAG_COMPOUND)) {
            return null;
        }

        CompoundTag membershipTag = rootTag.getCompound(MEMBERSHIP_CARD);
        if (!membershipTag.contains(UUID_TAG, Tag.TAG_STRING)) {
            return null;
        }

        try {
            return UUID.fromString(membershipTag.getString(UUID_TAG));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * 辅助方法：从物品中读取共享 UUID 列表
     */
    @NotNull
    public static List<UUID> getSharedUuids(@NotNull ItemStack stack) {
        List<UUID> sharedUuids = new ArrayList<>();

        if (stack.isEmpty() || !(stack.getItem() instanceof GrayMembershipCardItem) || !stack.hasTag()) {
            return sharedUuids;
        }

        CompoundTag rootTag = stack.getTag();
        assert rootTag != null;
        if (!rootTag.contains(MEMBERSHIP_CARD, Tag.TAG_COMPOUND)) {
            return sharedUuids;
        }

        CompoundTag membershipTag = rootTag.getCompound(MEMBERSHIP_CARD);
        if (!membershipTag.contains(SHARED_TAG, Tag.TAG_LIST)) {
            return sharedUuids;
        }

        ListTag sharedListTag = membershipTag.getList(SHARED_TAG, Tag.TAG_STRING);
        for (Tag tag : sharedListTag) {
            if (tag instanceof StringTag) {
                try {
                    sharedUuids.add(UUID.fromString(((StringTag) tag).getAsString()));
                } catch (IllegalArgumentException e) {
                    // 忽略无效的UUID
                }
            }
        }

        return sharedUuids;
    }
}
