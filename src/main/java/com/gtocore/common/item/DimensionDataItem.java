package com.gtocore.common.item;

import com.gtolib.api.recipe.lookup.IngredientConverter;
import com.gtolib.api.recipe.lookup.MapIngredient;
import com.gtolib.utils.RLUtils;

import com.gregtechceu.gtceu.api.data.DimensionMarker;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.core.mixins.StrictNBTIngredientAccessor;

import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import com.fast.fastcollection.O2IOpenCacheHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.annotation.Nullable;

public final class DimensionDataItem extends Item {

    private static Item DIMENSION_DATA;

    private static final Object2IntOpenHashMap<String> NBTS = new O2IOpenCacheHashMap<>();

    public static final IngredientConverter<Ingredient> INGREDIENT_CONVERTER = (ingredient, amount, map) -> {
        if (ingredient instanceof StrictNBTIngredientAccessor nbtIngredient) {
            var nbt = nbtIngredient.getStack().getTag();
            if (nbt != null && nbtIngredient.getStack().getItem() == DIMENSION_DATA) {
                if (nbt.tags.get("dim") instanceof StringTag stringTag) {
                    map.add(NBTS.getInt(stringTag.getAsString()), amount);
                    return;
                }
            }
        }
        MapIngredient.INGREDIENT_CONVERTER.convert(ingredient, amount, map);
    };

    public static final IngredientConverter<ItemStack> ITEM_CONVERTER = (stack, amount, map) -> {
        var nbt = stack.getTag();
        if (nbt != null && stack.getItem() == DIMENSION_DATA) {
            if (nbt.tags.get("dim") instanceof StringTag stringTag) {
                map.add(NBTS.getInt(stringTag.getAsString()), amount);
                return;
            }
        }
        MapIngredient.ITEM_CONVERTER.convert(stack, amount, map);
    };

    public DimensionDataItem(Properties properties) {
        super(properties);
        DIMENSION_DATA = this;
    }

    public ItemStack getDimensionData(ResourceLocation resourceLocation) {
        ItemStack stack = getDefaultInstance();
        stack.getOrCreateTag().putString("dim", resourceLocation.toString());
        NBTS.computeIfAbsent(resourceLocation.toString(), k -> MapIngredient.getCount(null));
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
