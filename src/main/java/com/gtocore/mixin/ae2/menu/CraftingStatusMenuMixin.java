package com.gtocore.mixin.ae2.menu;

import com.gtocore.integration.jech.PinYinUtils;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import appeng.menu.me.crafting.CraftingStatusMenu;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CraftingStatusMenu.class)
public class CraftingStatusMenuMixin {

    @OnlyIn(Dist.CLIENT)
    @Redirect(method = "filterEntry", at = @At(value = "INVOKE", target = "Ljava/lang/String;contains(Ljava/lang/CharSequence;)Z"), remap = false)
    private boolean onReturnToMainMenu(String instance, CharSequence s) {
        return PinYinUtils.match(instance, s);
    }
}
