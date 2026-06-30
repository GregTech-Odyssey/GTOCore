package com.gtocore.client;

import com.gtocore.client.screen.MessageListScreen;
import com.gtocore.utils.NotificationUtils;

import com.gtolib.GTOCore;

import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import appeng.core.localization.GuiText;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
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
                .then(Commands.literal("notify")
                        .then(Commands.literal("test").executes(ctx -> sendNotification(ctx, "GTO Notification Test", "Runtime notification test", NotificationUtils.Type.INFO, NotificationUtils.DEFAULT_ICON)))
                        .then(Commands.literal("ae2").executes(ctx -> sendNotification(ctx, GuiText.ToastCraftingJobFinishedTitle.text().getString(), "Test AE2 Crafting Job", NotificationUtils.Type.INFO, "assets/ae2/textures/block/controller.png")))
                        .then(notifyTypeCommand("none", NotificationUtils.Type.NONE))
                        .then(notifyTypeCommand("info", NotificationUtils.Type.INFO))
                        .then(notifyTypeCommand("warning", NotificationUtils.Type.WARNING))
                        .then(notifyTypeCommand("error", NotificationUtils.Type.ERROR)))
                .then(Commands.literal("message").executes((ctx) -> {
                    Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new MessageListScreen()));
                    return 1;
                })));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> notifyTypeCommand(String name, NotificationUtils.Type type) {
        return Commands.literal(name)
                .then(Commands.argument("title", StringArgumentType.string())
                        .then(Commands.argument("text", StringArgumentType.greedyString())
                                .executes(ctx -> sendNotification(ctx, StringArgumentType.getString(ctx, "title"), StringArgumentType.getString(ctx, "text"), type, NotificationUtils.DEFAULT_ICON))));
    }

    private static int sendNotification(CommandContext<CommandSourceStack> ctx, String title, String text, NotificationUtils.Type type, String iconResource) {
        boolean sent = NotificationUtils.notify(title, null, text, type, iconResource);
        if (sent) {
            ctx.getSource().sendSuccess(() -> Component.literal("Notification sent."), false);
            return 1;
        }
        ctx.getSource().sendFailure(Component.literal("Notification failed."));
        return 0;
    }
}
