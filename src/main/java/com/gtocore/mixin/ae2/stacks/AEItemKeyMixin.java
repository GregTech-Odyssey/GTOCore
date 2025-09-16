package com.gtocore.mixin.ae2.stacks;

import com.gtolib.IItem;
import com.gtolib.api.ae2.stacks.IAEItemKey;
import com.gtolib.api.misc.IMapValueCache;
import com.gtolib.utils.RLUtils;

import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import appeng.api.stacks.AEItemKey;
import appeng.core.AELog;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AEItemKey.class)
public abstract class AEItemKeyMixin implements IAEItemKey {

    @Shadow(remap = false)
    private int maxStackSize;

    @Shadow(remap = false)
    public abstract ItemStack getReadOnlyStack();

    @Shadow(remap = false)
    @Final
    private Item item;

    @Shadow(remap = false)
    public abstract @Nullable CompoundTag getTag();

    @Shadow(remap = false)
    private @Nullable ItemStack readOnlyStack;

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public ResourceLocation getId() {
        return ((IItem) item).gtolib$getIdLocation();
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static @Nullable AEItemKey of(ItemStack stack) {
        var item = stack.getItem();
        if (item == Items.AIR) return null;
        var tag = stack.getTag();
        if (tag == null || tag.isEmpty()) {
            return ((IItem) item.asItem()).gtolib$getAEKey();
        } else {
            return IMapValueCache.ITEM_KEY_CACHE.get(stack);
        }
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static AEItemKey of(ItemLike itemLike) {
        var item = itemLike.asItem();
        return ((IItem) item).gtolib$getAEKey();
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static AEItemKey of(ItemLike itemLike, @Nullable CompoundTag tag) {
        var item = itemLike.asItem();
        if (tag == null || tag.isEmpty()) return ((IItem) item).gtolib$getAEKey();
        var stack = new ItemStack(item, 1);
        stack.setTag(tag);
        return IMapValueCache.ITEM_KEY_CACHE.get(stack);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static @Nullable AEItemKey fromTag(CompoundTag tag) {
        try {
            var item = BuiltInRegistries.ITEM.getOptional(RLUtils.parse(tag.getString("id"))).orElseThrow(() -> new IllegalArgumentException("Unknown item id."));
            if (item == Items.AIR) return null;
            var extraTag = tag.contains("tag") ? tag.getCompound("tag") : null;
            var extraCaps = tag.contains("caps") ? tag.getCompound("caps") : null;
            if ((extraTag == null || extraTag.isEmpty()) && (extraCaps == null || extraCaps.isEmpty())) {
                return ((IItem) item).gtolib$getAEKey();
            }
            var stack = new ItemStack(item, 1, extraCaps);
            if (extraTag != null) stack.setTag(extraTag);
            return IMapValueCache.ITEM_KEY_CACHE.get(stack);
        } catch (Exception e) {
            AELog.debug("Tried to load an invalid item key from NBT: %s", tag, e);
            return null;
        }
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void writeToPacket(FriendlyByteBuf data) {
        data.writeVarInt(Item.getId(item));
        CompoundTag compoundTag = null;
        if (item.canBeDepleted() || item.shouldOverrideMultiplayerNbt()) {
            compoundTag = item.getShareTag(getReadOnlyStack());
        }
        data.writeNbt(compoundTag);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static AEItemKey fromPacket(FriendlyByteBuf data) {
        var item = Item.byId(data.readVarInt());
        var shareTag = data.readNbt();
        if (shareTag == null || shareTag.isEmpty()) {
            return ((IItem) item).gtolib$getAEKey();
        }
        var stack = new ItemStack(item);
        stack.readShareTag(shareTag);
        return IMapValueCache.ITEM_KEY_CACHE.get(stack);
    }

    @Redirect(method = "toTag", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/DefaultedRegistry;getKey(Ljava/lang/Object;)Lnet/minecraft/resources/ResourceLocation;", remap = true), remap = false)
    private <T> ResourceLocation gtolib$getKey(DefaultedRegistry instance, T t) {
        return ((IItem) t).gtolib$getIdLocation();
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public String toString() {
        String idString = ((IItem) item).gtolib$getIdString();
        return getTag() == null ? idString : idString + " (+tag)";
    }

    @Override
    public void gtolib$setMaxStackSize(int max) {
        maxStackSize = max;
    }

    @Override
    public void gtolib$setReadOnlyStack(ItemStack stack) {
        readOnlyStack = stack;
    }
}
