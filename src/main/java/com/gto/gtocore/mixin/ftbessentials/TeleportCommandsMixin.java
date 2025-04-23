package com.gto.gtocore.mixin.ftbessentials;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.phys.AABB;

import dev.ftb.mods.ftbessentials.command.TeleportCommands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(TeleportCommands.class)
public class TeleportCommandsMixin {

    @Inject(method = "back", at = @At("HEAD"), remap = false, cancellable = true)
    private static void back(ServerPlayer player, CallbackInfoReturnable<Integer> cir) {
        var vec3 = player.position();
        List<Monster> list = player.level().getEntitiesOfClass(Monster.class, new AABB(vec3.x() - 8.0D, vec3.y() - 5.0D, vec3.z() - 8.0D, vec3.x() + 8.0D, vec3.y() + 5.0D, vec3.z() + 8.0D), (p_9062_) -> true);
        if (!list.isEmpty()) {
            player.displayClientMessage(Component.translatable("gtocore.teleport.not_safe"), true);
            cir.setReturnValue(0);
        }
    }
}
