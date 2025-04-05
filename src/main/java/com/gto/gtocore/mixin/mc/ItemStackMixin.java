package com.gto.gtocore.mixin.mc;

import com.gto.gtocore.utils.ItemUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.CapabilityProvider;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin extends CapabilityProvider<ItemStack> {

    protected ItemStackMixin(Class<ItemStack> baseClass) {
        super(baseClass);
    }

    @Shadow
    public abstract Item getItem();

    @Shadow
    private int count;

    @Shadow
    @Nullable
    private CompoundTag tag;

    @Inject(method = "save", at = @At("HEAD"), cancellable = true)
    private void save(CompoundTag compoundTag, CallbackInfoReturnable<CompoundTag> cir) {
        compoundTag.putString("id", ItemUtils.getId(getItem()));
        compoundTag.putByte("Count", (byte) Math.min(64, this.count));
        if (this.tag != null) {
            compoundTag.put("tag", this.tag.copy());
        }

        CompoundTag cnbt = serializeCaps();
        if (cnbt != null && !cnbt.isEmpty()) {
            compoundTag.put("ForgeCaps", cnbt);
        }
        cir.setReturnValue(compoundTag);
    }
}
