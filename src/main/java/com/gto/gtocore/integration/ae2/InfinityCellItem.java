package com.gto.gtocore.integration.ae2;

import com.gto.gtocore.integration.ae2.storage.InfinityCellHandler;
import com.gto.gtocore.integration.ae2.storage.InfinityCellInventory;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import appeng.api.stacks.AEKeyType;
import com.google.common.base.Preconditions;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@Getter
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class InfinityCellItem extends Item {

    private final AEKeyType keyType;

    public InfinityCellItem(AEKeyType keyType) {
        super(new Properties().stacksTo(1).fireResistant());
        this.keyType = keyType;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        Preconditions.checkArgument(stack.getItem() == this);
        InfinityCellInventory handler = InfinityCellHandler.INSTANCE.getCellInventory(stack, null);
        CompoundTag tag = stack.getTag();
        if (tag != null && handler != null && handler.hasUUID()) {
            UUID uuid = handler.getUUID();
            if (uuid == null) return;
            tooltip.add(Component.literal("UUID: ").withStyle(ChatFormatting.GRAY).append(Component.literal(uuid.toString()).withStyle(ChatFormatting.AQUA)));
            tooltip.add(Component.literal("Byte: ").withStyle(ChatFormatting.GRAY).append(Component.literal(handler.getTotalStorage()).withStyle(ChatFormatting.GREEN)));
        }
    }
}
