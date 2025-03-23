package com.gto.gtocore.mixin.mc;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EnderMan.class, priority = 0)
public class EnderManMixin extends Monster {

    protected EnderManMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "teleport()Z", at = @At("HEAD"), cancellable = true)
    protected void teleport(CallbackInfoReturnable<Boolean> cir) {
        if (isNoAi()) cir.setReturnValue(false);
    }
}
