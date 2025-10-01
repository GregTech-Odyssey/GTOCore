package com.gtocore.mixin.buildinggadgets2;

import com.direwolf20.buildinggadgets2.common.containers.customhandler.TemplateManagerHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nonnull;

@Mixin(value = TemplateManagerHandler.class, remap = false)
public abstract class TemplateManagerHandlerMixin {
    @Inject(method = "isItemValid(ILnet/minecraft/world/item/ItemStack;)Z", at = @At("RETURN"), cancellable = true)
    private void addAE2PatternsValidation(int slot, @Nonnull ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            return;
        }
        if (slot == 1) {
            Item encodedPattern = ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath("ae2", "processing_pattern"));
            Item blankPattern = ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath("ae2", "blank_pattern"));
            boolean isAE2Pattern = (encodedPattern != null && stack.is(encodedPattern)) || (blankPattern != null && stack.is(blankPattern));
            if (isAE2Pattern) {
                cir.setReturnValue(true);
            }
        }
    }
}
