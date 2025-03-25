package com.gto.gtocore.common.data;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.saved.DysonSphereSavaedData;
import com.gto.gtocore.utils.ItemUtils;
import com.gto.gtocore.utils.StringConverter;

import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.mojang.brigadier.CommandDispatcher;

public interface GTOCommands {

    static void init(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(GTOCore.MOD_ID)
                .then(Commands.literal("dyson")
                        .then(Commands.literal("info")
                                .executes(ctx -> {
                                    DysonSphereSavaedData.INSTANCE.getDysonLaunchData().forEach((g, p) -> ctx.getSource().sendSuccess(
                                            () -> Component.literal("\nGalaxy: ").append(g)
                                                    .append("\nCount: " + p)
                                                    .append("\nDamage: " + DysonSphereSavaedData.INSTANCE.getDysonDamageData().getOrDefault(g, 0))
                                                    .append("\nIn use: " + DysonSphereSavaedData.INSTANCE.getDysonUse().getOrDefault(g, false)),
                                            false));
                                    return 1;
                                }))
                        .then(Commands.literal("clean").requires(source -> source.hasPermission(2))
                                .executes(ctx -> {
                                    DysonSphereSavaedData.INSTANCE.getDysonLaunchData().clear();
                                    DysonSphereSavaedData.INSTANCE.getDysonDamageData().clear();
                                    DysonSphereSavaedData.INSTANCE.getDysonUse().clear();
                                    DysonSphereSavaedData.INSTANCE.setDirty();
                                    return 1;
                                })))
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
            String f = StringConverter.fromFluid(FluidIngredient.of(new FluidStack(fluid, 1000)));
            if (f != null) {
                player.sendSystemMessage(copy(Component.literal(f).withStyle(ChatFormatting.AQUA)));
            }
            player.sendSystemMessage(copy(Component.literal(fluid.builtInRegistryHolder().key().location().toString()).withStyle(ChatFormatting.GREEN)));
        }
    }
}
