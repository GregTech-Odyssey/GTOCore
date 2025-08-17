package com.gtocore.common.data;

import com.gtocore.common.saved.DysonSphereSavaedData;

import com.gtolib.GTOCore;
import com.gtolib.api.data.Dimension;
import com.gtolib.api.misc.PlanetManagement;
import com.gtolib.api.recipe.ingredient.FastFluidIngredient;
import com.gtolib.utils.ItemUtils;
import com.gtolib.utils.RLUtils;
import com.gtolib.utils.StringConverter;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import org.embeddedt.modernfix.spark.SparkLaunchProfiler;

import java.util.Arrays;

public final class GTOCommands {

    public static void init(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(GTOCore.MOD_ID)
                .then(Commands.literal("spark").then(Commands.literal("start").executes(ctx -> {
                    SparkLaunchProfiler.start("all");
                    return 1;
                })).then(Commands.literal("stop").executes(ctx -> {
                    SparkLaunchProfiler.stop("all");
                    return 1;
                })))
                .then(Commands.literal("space")
                        .then(Commands.literal("planet").then(Commands.literal("unlock").requires(source -> source.hasPermission(2)).then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("id", StringArgumentType.greedyString()).suggests((context, builder) -> SharedSuggestionProvider.suggest(Arrays.stream(Dimension.values()).filter(Dimension::isWithinGalaxy).map(Dimension::getLocation).map(ResourceLocation::toString), builder)).executes(ctx -> {
                            ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
                            ResourceLocation id = RLUtils.parse(StringArgumentType.getString(ctx, "id"));
                            PlanetManagement.unlock(player.getUUID(), id);
                            ctx.getSource().sendSuccess(() -> Component.translatable(PlanetManagement.isUnlocked(player, id) ? "gtocore.unlocked" : "gtocore.ununlocked"), true);
                            return 1;
                        })))))
                        .then(Commands.literal("dyson").then(Commands.literal("info").executes(ctx -> {
                            DysonSphereSavaedData.INSTANCE.getDysonLaunchData().forEach((g, p) -> ctx.getSource().sendSuccess(() -> Component.literal("\nGalaxy: ").append(g).append("\nCount: " + p).append("\nDamage: " + DysonSphereSavaedData.INSTANCE.getDysonDamageData().getOrDefault(g, 0)).append("\nIn use: " + DysonSphereSavaedData.INSTANCE.getDysonUse().getOrDefault(g, false)), false));
                            return 1;
                        })).then(Commands.literal("clean").requires(source -> source.hasPermission(2)).executes(ctx -> {
                            DysonSphereSavaedData.INSTANCE.getDysonLaunchData().clear();
                            DysonSphereSavaedData.INSTANCE.getDysonDamageData().clear();
                            DysonSphereSavaedData.INSTANCE.getDysonUse().clear();
                            DysonSphereSavaedData.INSTANCE.setDirty();
                            return 1;
                        }))))
                .then(Commands.literal("hand").executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayer();
                    if (player != null) {
                        hand(player);
                    }
                    return 1;
                })));
    }

    private static Component copy(Component c) {
        return Component.literal("- ")
                .withStyle(ChatFormatting.GRAY)
                .withStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, c.getString())))
                .withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to copy"))))
                .append(c);
    }

    private static void hand(ServerPlayer player) {
        player.sendSystemMessage(Component.literal("Item in hand:"));
        ItemStack stack = player.getMainHandItem();
        String s = StringConverter.fromItem(Ingredient.of(stack), 1);
        if (s != null) {
            player.sendSystemMessage(copy(Component.literal(s).withStyle(ChatFormatting.DARK_BLUE)));
        }
        player.sendSystemMessage(copy(Component.literal(ItemUtils.getId(stack)).withStyle(ChatFormatting.GREEN)));
        if (stack.hasTag()) player.sendSystemMessage(copy(Component.literal(stack.getTag().toString()).withStyle(ChatFormatting.AQUA)));
        for (TagKey<Item> tag : stack.getItemHolder().tags().toList()) {
            player.sendSystemMessage(copy(Component.literal(tag.location().toString()).withStyle((ChatFormatting.YELLOW))));
        }
        if (stack.getItem() instanceof BucketItem bucketItem) {
            player.sendSystemMessage(Component.literal("Held fluid:"));
            Fluid fluid = bucketItem.getFluid();
            String f = StringConverter.fromFluid(FastFluidIngredient.of(1000L, fluid), false);
            if (f != null) {
                player.sendSystemMessage(copy(Component.literal(f).withStyle(ChatFormatting.AQUA)));
            }
            player.sendSystemMessage(copy(Component.literal(fluid.builtInRegistryHolder().key().location().toString()).withStyle(ChatFormatting.GREEN)));
        }
    }
}
