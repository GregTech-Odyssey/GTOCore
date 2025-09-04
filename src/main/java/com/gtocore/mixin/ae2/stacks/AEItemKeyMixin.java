package com.gtocore.mixin.ae2.stacks;

import com.gtolib.IItem;
import com.gtolib.api.ae2.IAEItemKey;
import com.gtolib.utils.RLUtils;

import com.gregtechceu.gtceu.utils.ItemStackHashStrategy;

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
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AEItemKey.class)
public abstract class AEItemKeyMixin implements IAEItemKey {

    @Unique
    private static final Object2ObjectOpenCustomHashMap<ItemStack, AEItemKey> gtolib$CACHE = new Object2ObjectOpenCustomHashMap<>(ItemStackHashStrategy.ITEM_AND_TAG);

    @Unique
    private int gtolib$fuzzySearchMaxValue;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void gtolib$init(CallbackInfo ci) {
        gtolib$fuzzySearchMaxValue = -1;
    }

    @Shadow(remap = false)
    private static AEItemKey of(ItemLike item, @Nullable CompoundTag tag, @Nullable CompoundTag caps) {
        return null;
    }

    @Shadow(remap = false)
    @Nullable
    private static CompoundTag serializeStackCaps(ItemStack stack) {
        return null;
    }

    @Shadow(remap = false)
    private int maxStackSize;

    @Shadow(remap = false)
    public abstract ItemStack getReadOnlyStack();

    @Shadow(remap = false)
    @Final
    private Item item;

    @Shadow(remap = false)
    public abstract @Nullable CompoundTag getTag();

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public int getFuzzySearchMaxValue() {
        if (gtolib$fuzzySearchMaxValue < 0) gtolib$fuzzySearchMaxValue = getReadOnlyStack().getItem().getMaxDamage();
        return gtolib$fuzzySearchMaxValue;
    }

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
            synchronized (gtolib$CACHE) {
                return gtolib$CACHE.computeIfAbsent(stack, k -> {
                    var key = of(item, stack.getTag(), serializeStackCaps(stack));
                    ((IAEItemKey) (Object) key).gtolib$setMaxStackSize(item.getMaxStackSize());
                    return key;
                });
            }
        }
    }

    @Unique
    private static @NotNull AEItemKey gtolib$ofNbt(ItemStack stack) {
        var item = stack.getItem();
        synchronized (gtolib$CACHE) {
            return gtolib$CACHE.computeIfAbsent(stack, k -> {
                var key = of(item, stack.getTag(), serializeStackCaps(stack));
                ((IAEItemKey) (Object) key).gtolib$setMaxStackSize(item.getMaxStackSize());
                return key;
            });
        }
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static AEItemKey of(ItemLike item) {
        return ((IItem) item.asItem()).gtolib$getAEKey();
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static AEItemKey of(ItemLike item, @Nullable CompoundTag tag) {
        if (tag == null || tag.isEmpty()) return ((IItem) item.asItem()).gtolib$getAEKey();
        var stack = new ItemStack(item, 1);
        stack.setTag(tag);
        return gtolib$ofNbt(stack);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static @Nullable AEItemKey fromTag(CompoundTag tag) {
        try {
            var item = BuiltInRegistries.ITEM.getOptional(RLUtils.parse(tag.getString("id"))).orElseThrow(() -> new IllegalArgumentException("Unknown item id."));
            var extraTag = tag.contains("tag") ? tag.getCompound("tag") : null;
            var extraCaps = tag.contains("caps") ? tag.getCompound("caps") : null;
            if ((extraTag == null || extraTag.isEmpty()) && (extraCaps == null || extraCaps.isEmpty())) {
                return ((IItem) item.asItem()).gtolib$getAEKey();
            }
            var stack = new ItemStack(item, 1, extraCaps);
            if (extraTag != null) stack.setTag(extraTag);
            return gtolib$ofNbt(stack);
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
        int i = data.readVarInt();
        var item = Item.byId(i);
        var shareTag = data.readNbt();
        if (shareTag == null || shareTag.isEmpty()) {
            return ((IItem) item.asItem()).gtolib$getAEKey();
        }
        var stack = new ItemStack(item);
        stack.readShareTag(shareTag);
        return gtolib$ofNbt(stack);
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
}
