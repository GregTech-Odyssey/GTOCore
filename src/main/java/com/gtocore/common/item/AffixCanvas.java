package com.gtocore.common.item;

import com.gtocore.common.data.GTOItems;

import com.gregtechceu.gtceu.api.item.ComponentItem;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

public class AffixCanvas extends ComponentItem {

    public static final String AFFIX_LIST_KEY = "affix_list";
    private static final String AFFIX_ID_KEY = "id";

    /**
     * A single affix id entry in the legacy-compatible affix canvas NBT list.
     */
    public record AffixEntry(ResourceLocation id) {

        public static final Codec<AffixEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf(AFFIX_ID_KEY).forGetter(AffixEntry::id))
                .apply(instance, AffixEntry::new));
    }

    /**
     * Codec-backed representation of an affix canvas tag.
     *
     * <p>
     * The field name intentionally remains {@code affix_list} so existing stacks keep their
     * current NBT shape: {@code {affix_list:[{id:"namespace:path"}]}}.
     * </p>
     */
    public record AffixData(List<AffixEntry> affixList) {

        public static final Codec<AffixData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                AffixEntry.CODEC.listOf().fieldOf(AFFIX_LIST_KEY).forGetter(AffixData::affixList))
                .apply(instance, AffixData::new));

        private static AffixData of(Collection<ResourceLocation> affixes) {
            return new AffixData(affixes.stream().map(AffixEntry::new).toList());
        }
    }

    public AffixCanvas(Properties properties) {
        super(properties);
    }

    /**
     * Reads valid affix ids from an affix canvas stack.
     *
     * @param stack stack to inspect
     * @return valid affix resource ids in stored order
     */
    public static List<ResourceLocation> readAffixes(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(AFFIX_LIST_KEY, Tag.TAG_LIST)) return List.of();

        ListTag affixList = tag.getList(AFFIX_LIST_KEY, Tag.TAG_COMPOUND);
        if (affixList.isEmpty()) return List.of();

        List<ResourceLocation> affixes = new ArrayList<>(affixList.size());
        for (int i = 0; i < affixList.size(); i++) {
            AffixEntry.CODEC.parse(NbtOps.INSTANCE, affixList.getCompound(i))
                    .result()
                    .map(AffixEntry::id)
                    .ifPresent(affixes::add);
        }
        return affixes;
    }

    /**
     * Checks whether a stack has at least one valid affix entry.
     *
     * @param stack stack to inspect
     * @return {@code true} when the canvas stores usable affix data
     */
    public static boolean hasAffixes(ItemStack stack) {
        return !readAffixes(stack).isEmpty();
    }

    /**
     * Stores affix ids on an item stack using the legacy-compatible {@code affix_list} shape.
     *
     * @param stack   target stack
     * @param affixes affix ids to store
     */
    public static void putAffixes(ItemStack stack, Collection<ResourceLocation> affixes) {
        if (affixes.isEmpty()) {
            stack.removeTagKey(AFFIX_LIST_KEY);
            return;
        }

        AffixData.CODEC.encodeStart(NbtOps.INSTANCE, AffixData.of(affixes))
                .result()
                .filter(CompoundTag.class::isInstance)
                .map(CompoundTag.class::cast)
                .ifPresent(tag -> stack.getOrCreateTag().put(AFFIX_LIST_KEY, tag.getList(AFFIX_LIST_KEY, Tag.TAG_COMPOUND)));
    }

    /**
     * Creates an affix canvas stack containing the provided affix ids.
     *
     * @param affixes affix ids to store
     * @return new affix canvas stack
     */
    public static ItemStack createWithAffixes(Collection<ResourceLocation> affixes) {
        ItemStack stack = new ItemStack(GTOItems.AFFIX_CANVAS);
        putAffixes(stack, affixes);
        return stack;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemstack, @Nullable Level world, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        for (ResourceLocation affixId : readAffixes(itemstack)) {
            String translationId = affixId.toString();
            Component nameComponent = Component.translatable("affix." + translationId);
            Component suffixComponent = Component.translatable("affix." + translationId + ".suffix");
            Component combined = nameComponent.copy().append(" · ").append(suffixComponent).withStyle(ChatFormatting.GRAY);
            list.add(combined);
        }
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return hasAffixes(stack);
    }

    @Override
    public @NotNull Rarity getRarity(@NotNull ItemStack stack) {
        return Rarity.UNCOMMON;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return hasAffixes(stack) ? 1 : 64;
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
