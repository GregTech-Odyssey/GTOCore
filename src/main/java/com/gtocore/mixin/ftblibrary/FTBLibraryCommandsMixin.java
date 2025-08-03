package com.gtocore.mixin.ftblibrary;

import com.gregtechceu.gtceu.GTCEu;

import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import com.mojang.brigadier.CommandDispatcher;
import dev.ftb.mods.ftblibrary.FTBLibraryCommands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FTBLibraryCommands.class)
public class FTBLibraryCommandsMixin {

    @Inject(method = "registerCommands", at = @At("HEAD"), remap = false, cancellable = true)
    private static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection type, CallbackInfo ci) {
        if (!GTCEu.isDev()) ci.cancel();
    }
}
