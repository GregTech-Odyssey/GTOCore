package com.gtocore.mixin.ftbq;

import com.gtocore.integration.ftbquests.EMIRecipeModHelper;

import net.minecraft.server.level.ServerPlayer;

import com.llamalad7.mixinextras.sugar.Local;
import dev.ftb.mods.ftbquests.net.ToggleEditingModeMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ToggleEditingModeMessage.class)
public class ToggleEditingModeMessageMixin {

    @ModifyArg(method = "handle", at = @At(value = "INVOKE", target = "Ldev/ftb/mods/ftbquests/quest/TeamData;setCanEdit(Lnet/minecraft/world/entity/player/Player;Z)Z"), index = 1, remap = false)
    private boolean setCanEdit(boolean newCanEdit, @Local ServerPlayer player) {
        return newCanEdit && EMIRecipeModHelper.canEdit();
    }
}
