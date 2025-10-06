package com.gtocore.client;

import com.gtocore.client.screen.MessageListScreen;

import com.gtolib.GTOCore;

import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.mojang.brigadier.CommandDispatcher;
import org.embeddedt.modernfix.spark.SparkLaunchProfiler;

@OnlyIn(Dist.CLIENT)
public final class GTOClientCommands {

    public static void init(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(GTOCore.MOD_ID + "c")
                .then(Commands.literal("spark").then(Commands.literal("start").executes(ctx -> {
                    SparkLaunchProfiler.start("all");
                    return 1;
                })).then(Commands.literal("stop").executes(ctx -> {
                    SparkLaunchProfiler.stop("all");
                    return 1;
                })))
                .then(Commands.literal("multiblock").then(
                        Commands.literal("on").executes((ctx) -> {
                            ClientCache.machineNotFormedHighlight = true;
                            return 1;
                        })).then(
                                Commands.literal("off").executes((ctx) -> {
                                    ClientCache.machineNotFormedHighlight = false;
                                    return 1;
                                })))
                .then(Commands.literal("message").executes((ctx) -> {
                    Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new MessageListScreen()));
                    return 1;
                })));
    }
}
