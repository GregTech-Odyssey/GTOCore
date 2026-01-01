package com.gtocore.mixin.mc.mob;

import com.gtocore.api.entity.ILivingEntity;
import com.gtocore.config.GTOConfig;

import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;

import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Set;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements ILivingEntity {

    @Shadow
    protected abstract void dropCustomDeathLoot(DamageSource damageSource, int looting, boolean hitByPlayer);

    @Shadow
    protected abstract void dropEquipment();

    @Shadow
    public abstract RandomSource getRandom();

    protected LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Redirect(method = "die", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;info(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", remap = false))
    private void gto$fixSpam(Logger instance, String s, Object o1, Object o2) {}

    @Inject(method = "hasEffect", at = @At("HEAD"), cancellable = true)
    @SuppressWarnings({ "ConstantConditions", "resource" })
    private void gto$hasEffectInject(MobEffect effect, CallbackInfoReturnable<Boolean> cir) {
        if (effect == MobEffects.NIGHT_VISION && level().isClientSide() && (Object) this instanceof Player && GTOConfig.INSTANCE.nightVision) {
            cir.setReturnValue(true);
        }
    }

    @Override
    public void gtocore$getAllDeathLoot(DamageSource source, Set<ItemStack> itemStacks, int multiplier, boolean filterNbt) {
        this.captureDrops(new ArrayList<>());
        this.dropCustomDeathLoot(source, ForgeHooks.getLootingLevel(this, source.getEntity(), source), true);
        this.dropEquipment();
        this.captureDrops(null).forEach(e -> {
            if (e != null) {
                var item = e.getItem();
                if (filterNbt && item.hasTag()) {
                    return;
                }
                var count = item.getCount();
                if (count < 1) return;
                item.setCount(count * getRandom().nextInt(multiplier / 2, multiplier));
                if (item.isEmpty()) return;
                itemStacks.add(item);
            }
        });
    }
}
