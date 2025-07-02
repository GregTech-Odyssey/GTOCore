package com.gtocore.mixin.mc.mob;

import com.gtolib.GTOCore;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Difficulty;
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
import org.spongepowered.asm.mixin.injection.Redirect;
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
        if (GTOCore.isSimple() || level().getDifficulty().getId() == 0) return;
        boolean isBoss = CommonProxy.isBoss(this);
        if (!isBoss && getRandom().nextBoolean()) return;
        double difficultyValue = (double) level().getDifficulty().getId() / 2;
        AttributeInstance maxHealthInstance = getAttribute(Attributes.MAX_HEALTH);
        if (maxHealthInstance != null) {
            double value = maxHealthInstance.getValue();
            maxHealthInstance.addPermanentModifier(new AttributeModifier(RANDOM_HEALTH_UUID, "addRandomHealth", getRandom().nextInt((int) ((value / (isBoss ? 2 : 10)) * difficultyValue), (int) (value * difficultyValue)), AttributeModifier.Operation.ADDITION));
        }
        AttributeInstance attackDamageInstance = getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackDamageInstance != null) {
            double value = attackDamageInstance.getValue();
            attackDamageInstance.addPermanentModifier(new AttributeModifier(RANDOM_DAMAGE_UUID, "addRandomDamage", getRandom().nextInt((int) ((value / (isBoss ? 2 : 10)) * difficultyValue), (int) (value * difficultyValue)), AttributeModifier.Operation.ADDITION));
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
        if (tickCount % 80 == 8 && getRandom().nextBoolean()) {
            int value = Math.max(1, (int) (Math.log(getMaxHealth() * Math.max(1, level().getDifficulty().getId())) + 0.5));
            heal(value);
        }
    }

    @Inject(method = "checkDespawn", at = @At(value = "HEAD"), cancellable = true)
    private void checkDespawn(CallbackInfo ci) {
        if (tickCount % 20 != 5) ci.cancel();
    }

    @Redirect(method = "checkDespawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getDifficulty()Lnet/minecraft/world/Difficulty;"))
    private Difficulty getDifficulty(Level instance) {
        return Difficulty.HARD;
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return target.canBeSeenAsEnemy();
    }
}
