package com.gto.gtocore.mixin.mc;

import com.gto.gtocore.config.GTOConfig;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import snownee.jade.util.CommonProxy;

import java.util.UUID;

@Mixin(value = Mob.class, priority = 0)
public abstract class MobMixin extends LivingEntity {

    @Unique
    private static final UUID RANDOM_HEALTH_UUID = UUID.fromString("f3bbe254-3008-48cf-9774-f69d1e81d16b");

    @Unique
    private static final UUID RANDOM_DAMAGE_UUID = UUID.fromString("88b9a4ec-7cb6-4533-bed3-69ee0e823fd3");

    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        if (GTOConfig.getDifficulty() == 1) return;
        boolean isBoss = CommonProxy.isBoss(this);
        if (!isBoss && getRandom().nextBoolean()) return;
        int difficultyValue = Math.max(1, level().getDifficulty().getId());
        AttributeInstance maxHealthInstance = getAttribute(Attributes.MAX_HEALTH);
        if (maxHealthInstance != null) {
            int value = (int) Math.ceil(maxHealthInstance.getValue());
            maxHealthInstance.addPermanentModifier(new AttributeModifier(RANDOM_HEALTH_UUID, "addRandomHealth", getRandom().nextInt((value / (isBoss ? 2 : 10)) << difficultyValue, value << difficultyValue), AttributeModifier.Operation.ADDITION));
        }
        AttributeInstance attackDamageInstance = getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackDamageInstance != null) {
            int value = (int) Math.ceil(attackDamageInstance.getValue()) << 1;
            attackDamageInstance.addPermanentModifier(new AttributeModifier(RANDOM_DAMAGE_UUID, "addRandomDamage", getRandom().nextInt((value / (isBoss ? 2 : 10)) * difficultyValue, value * difficultyValue), AttributeModifier.Operation.ADDITION));
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At(value = "HEAD"))
    private void readAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        AttributeInstance maxHealthInstance = getAttribute(Attributes.MAX_HEALTH);
        if (maxHealthInstance != null) {
            maxHealthInstance.removeModifier(RANDOM_HEALTH_UUID);
        }
        AttributeInstance attackDamageInstance = getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackDamageInstance != null) {
            attackDamageInstance.removeModifier(RANDOM_DAMAGE_UUID);
        }
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void tick(CallbackInfo ci) {
        if (tickCount % 80 == 0 && getRandom().nextBoolean()) {
            int value = Math.max(1, (int) Math.ceil(Math.log(getMaxHealth() * Math.max(1, level().getDifficulty().getId()))));
            heal(value);
        }
    }
}
