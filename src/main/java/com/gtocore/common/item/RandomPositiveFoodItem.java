package com.gtocore.common.item;

import com.gregtechceu.gtceu.api.item.ComponentItem;

import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

public final class RandomPositiveFoodItem extends ComponentItem {

    public RandomPositiveFoodItem(Properties properties, int nutrition, float saturation) {
        super(properties.food(new FoodProperties.Builder()
                .nutrition(nutrition)
                .saturationMod(saturation)
                .alwaysEat()
                .build()));
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);
        if (!level.isClientSide && entity instanceof Player player) {
            player.addEffect(randomPositiveEffectInstance(level.getRandom()));
        }
        return result;
    }

    private static @NotNull MobEffectInstance randomPositiveEffectInstance(@NotNull RandomSource rng) {
        MobEffect[] candidates = new MobEffect[] {
                MobEffects.REGENERATION,
                MobEffects.DAMAGE_BOOST,
                MobEffects.MOVEMENT_SPEED,
                MobEffects.DIG_SPEED,
                MobEffects.ABSORPTION,
                MobEffects.JUMP,
                MobEffects.DAMAGE_RESISTANCE,
                MobEffects.FIRE_RESISTANCE,
                MobEffects.NIGHT_VISION,
                MobEffects.WATER_BREATHING,
                MobEffects.HEALTH_BOOST,
                MobEffects.LUCK
        };

        MobEffect effect = candidates[rng.nextInt(candidates.length)];
        int duration = 20 * (10 + rng.nextInt(21)); // 10~30秒
        int amplifier = rng.nextInt(2); // 0~1

        return new MobEffectInstance(effect, duration, amplifier);
    }
}
