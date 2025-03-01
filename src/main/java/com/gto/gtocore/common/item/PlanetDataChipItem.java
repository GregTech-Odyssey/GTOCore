package com.gto.gtocore.common.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

public final class PlanetDataChipItem extends Item {

    public PlanetDataChipItem(Properties properties) {
        super(properties);
    }

    public ItemStack getPlanetDataChip(UUID uuid, ResourceLocation resourceLocation) {
        ItemStack stack = new ItemStack(this);
        CompoundTag tag = stack.getOrCreateTag();
        tag.putUUID("uuid", uuid);
        tag.putString("planet", resourceLocation.toString());
        return stack;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemstack, @Nullable Level world, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        CompoundTag tag = itemstack.getTag();
        if (tag != null) {
            list.add(Component.literal("UUID: ").append(tag.getUUID("uuid").toString()));
            list.add(Component.translatable("gtceu.jei.ore_vein_diagram.dimensions").append(tag.getString("planet")));
        }
    }
}
