package com.gto.gtocore.common.data;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.saved.DysonSphereSavaedData;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import com.mojang.brigadier.CommandDispatcher;
import dev.latvian.mods.kubejs.command.KubeJSCommands;

public interface GTOCommands {

    static void init(CommandDispatcher<CommandSourceStack> dispatcher) {
        KubeJSCommands.register(dispatcher);
        dispatcher.register(Commands.literal(GTOCore.MOD_ID)
                .then(Commands.literal("dyson")
                        .then(Commands.literal("info")
                                .executes(ctx -> {
                                    DysonSphereSavaedData.INSTANCE.getDysonLaunchData().forEach((g, p) -> ctx.getSource().sendSuccess(
                                            () -> Component.literal("\nGalaxy: ").append(g)
                                                    .append("\nCount: " + p)
                                                    .append("\nDamage: " + DysonSphereSavaedData.INSTANCE.getDysonDamageData().getOrDefault(g, 0))
                                                    .append("\nIn use: " + DysonSphereSavaedData.INSTANCE.getDysonUse().getOrDefault(g, false)),
                                            true));
                                    return 1;
                                }))
                        .then(Commands.literal("clean").requires(source -> source.hasPermission(2))
                                .executes(ctx -> {
                                    DysonSphereSavaedData.INSTANCE.getDysonLaunchData().clear();
                                    DysonSphereSavaedData.INSTANCE.getDysonDamageData().clear();
                                    DysonSphereSavaedData.INSTANCE.getDysonUse().clear();
                                    DysonSphereSavaedData.INSTANCE.setDirty();
                                    return 1;
                                }))));
    }
}
