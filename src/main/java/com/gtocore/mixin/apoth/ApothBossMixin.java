package com.gtocore.mixin.apoth;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ServerLevelAccessor;

import dev.shadowsoffire.apotheosis.adventure.boss.ApothBoss;
import dev.shadowsoffire.apotheosis.adventure.loot.LootRarity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ApothBoss.class)
public abstract class ApothBossMixin {

    /**
     * Apotheosis returns the mount instead of the boss when a boss has one, and only the mount gets
     * positioned - the boss keeps its default (0, 0, 0) position and is lost when the caller spawns the
     * pair, leaving a bare mount behind (e.g. the stray boss riding a spider). Drop the mount and hand
     * back the boss itself.
     */
    @Inject(
             method = "createBoss(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;FLdev/shadowsoffire/apotheosis/adventure/loot/LootRarity;)Lnet/minecraft/world/entity/Mob;",
             at = @At("RETURN"),
             cancellable = true,
             remap = false)
    private void gtocore$dropBossMount(ServerLevelAccessor world, BlockPos pos, RandomSource random, float luck, @Nullable LootRarity rarity, CallbackInfoReturnable<Mob> cir) {
        Mob result = cir.getReturnValue();
        if (result == null || result.getPassengers().isEmpty()) return;
        if (result.getPersistentData().getBoolean("apoth.boss")) return;
        Entity rider = result.getFirstPassenger();
        if (!(rider instanceof Mob boss) || !boss.getPersistentData().getBoolean("apoth.boss")) return;
        boss.stopRiding();
        result.discard();
        boss.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, random.nextFloat() * 360.0F, 0.0F);
        cir.setReturnValue(boss);
    }
}
