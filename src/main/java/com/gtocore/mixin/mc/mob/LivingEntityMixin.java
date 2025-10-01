package com.gtocore.mixin.mc.mob;

import com.gtocore.config.GTOConfig;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Redirect(method = "die", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;info(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", remap = false))
    private void gto$fixSpam(Logger instance, String s, Object o1, Object o2) {}

    @Inject(method = "hasEffect", at = @At("HEAD"), cancellable = true)
    private void gto$hasEffectInject(MobEffect effect, CallbackInfoReturnable<Boolean> cir) {
        if (effect == MobEffects.NIGHT_VISION && level().isClientSide() && (Object) this instanceof Player && GTOConfig.INSTANCE.nightVision) {
            cir.setReturnValue(true);
        }
    }
}
