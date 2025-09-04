package com.gtocore.api.misc;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

/**
 * 简单的近战攻击 Goal，不依赖 ATTACK_DAMAGE 属性，直接以常量伤害值攻击当前目标。
 */
public class AnimalsRevengeAttackGoal extends Goal {

    private final PathfinderMob mob;
    private final double speed;
    private final double attackReach;
    private final int attackCooldown;
    private final float damage;

    private int ticksUntilNextAttack;

    public AnimalsRevengeAttackGoal(PathfinderMob mob, double speed, double attackReach, int attackCooldownTicks, float damage) {
        this.mob = mob;
        this.speed = speed;
        this.attackReach = attackReach;
        this.attackCooldown = Math.max(5, attackCooldownTicks);
        this.damage = Math.max(0.5F, damage);
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = mob.getTarget();
        return target != null && target.isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = mob.getTarget();
        return target != null && target.isAlive();
    }

    @Override
    public void stop() {
        mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        LivingEntity target = mob.getTarget();
        if (target == null) return;

        mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
        mob.getNavigation().moveTo(target, speed);

        double distance = mob.distanceToSqr(target);
        double reach = Math.pow(attackReach + (double) mob.getBbWidth() * 0.5 + (double) target.getBbWidth() * 0.5, 2);

        if (ticksUntilNextAttack > 0) ticksUntilNextAttack--;

        if (distance <= reach && ticksUntilNextAttack <= 0) {
            performAttack(target);
            ticksUntilNextAttack = attackCooldown;
        }
    }

    private void performAttack(LivingEntity target) {
        Entity self = this.mob;
        if (!(self.level() instanceof ServerLevel serverLevel)) return;
        this.mob.swing(this.mob.getUsedItemHand());
        target.hurt(serverLevel.damageSources().mobAttack(this.mob), damage);
    }
}
