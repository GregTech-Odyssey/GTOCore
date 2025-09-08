package com.gtocore.common.item;

import com.gregtechceu.gtceu.api.item.ComponentItem;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.annotation.Nullable;

public class AffixCanvas extends ComponentItem {

    public AffixCanvas(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemstack, @Nullable Level world, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        CompoundTag tag = itemstack.getTag();
        if (tag != null && tag.contains("affix_list", Tag.TAG_LIST)) {
            ListTag affixes = tag.getList("affix_list", Tag.TAG_COMPOUND);
            int count = affixes.size();

            for (int i = 0; i < count; i++) {
                CompoundTag affix = affixes.getCompound(i);
                if (affix.contains("id", Tag.TAG_STRING)) {
                    String affixId = affix.getString("id");
                    Component nameComponent = Component.translatable("affix." + affixId);
                    Component suffixComponent = Component.translatable("affix." + affixId + ".suffix");
                    Component combined = nameComponent.copy().append(" · ").append(suffixComponent).withStyle(ChatFormatting.GRAY);
                    list.add(combined);
                }
            }
        }
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains("affix_list", Tag.TAG_LIST);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("affix_list", Tag.TAG_LIST)) {
            return 1;
        }
        return 64;
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }
}
