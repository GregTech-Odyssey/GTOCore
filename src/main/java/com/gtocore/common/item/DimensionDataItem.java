package com.gtocore.common.item;

import com.gtolib.api.recipe.lookup.MapIngredient;
import com.gtolib.utils.RLUtils;

import com.gregtechceu.gtceu.api.data.DimensionMarker;
import com.gregtechceu.gtceu.api.registry.GTRegistries;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.annotation.Nullable;

public final class DimensionDataItem extends Item {

    public DimensionDataItem(Properties properties) {
        super(properties);
    }

    public ItemStack getDimensionData(ResourceLocation resourceLocation) {
        ItemStack stack = getDefaultInstance();
        stack.getOrCreateTag().putString("dim", resourceLocation.toString());
        MapIngredient.add(resourceLocation.toString());
        return stack;
    }

    public static ResourceLocation getDimension(ItemStack stack) {
        return RLUtils.parse(stack.getOrCreateTag().getString("dim"));
    }

    public static DimensionMarker getDimensionMarker(ItemStack stack) {
        ResourceLocation resourceLocation = getDimension(stack);
        return GTRegistries.DIMENSION_MARKERS.getOrDefault(resourceLocation, new DimensionMarker(DimensionMarker.MAX_TIER, () -> Blocks.BARRIER, resourceLocation.toString()));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemstack, @Nullable Level world, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        if (itemstack.hasTag()) {
            list.add(Component.translatable("recipe.condition.dimension.tooltip", getDimensionMarker(itemstack).getIcon().getDisplayName()));
        }
    }
}
