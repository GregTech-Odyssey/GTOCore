package com.gtocore.mixin.ftbq;

import com.gtocore.integration.ftbquests.EMIRecipeModHelper;

import net.minecraft.commands.CommandSourceStack;

import com.mojang.brigadier.CommandDispatcher;
import dev.ftb.mods.ftbquests.command.FTBQuestsCommands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FTBQuestsCommands.class)
public class FTBQuestsCommandsMixin {

    @Inject(method = "register", at = @At("HEAD"), remap = false, cancellable = true)
    private static void register(CommandDispatcher<CommandSourceStack> dispatcher, CallbackInfo ci) {
        if (!EMIRecipeModHelper.canEdit()) ci.cancel();
    }
}
