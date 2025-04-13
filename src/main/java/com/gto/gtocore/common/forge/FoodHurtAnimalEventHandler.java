package com.gto.gtocore.common.forge;

import com.gto.gtocore.config.GTOConfig;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class FoodHurtAnimalEventHandler {

    private static ImmutableMap<Item, Set<Class<? extends LivingEntity>>> foodToEntityClass;

    private static void initialize() {
        Map<String, Class<? extends LivingEntity>> foodEntityMapping = new Object2ObjectOpenHashMap<>();
        foodEntityMapping.put("pork", Pig.class);
        foodEntityMapping.put("ham", Pig.class);
        foodEntityMapping.put("beef", Cow.class);
        foodEntityMapping.put("steak", Cow.class);
        foodEntityMapping.put("chicken", Chicken.class);
        foodEntityMapping.put("mutton", Sheep.class);
        foodEntityMapping.put("rabbit", Rabbit.class);
        foodEntityMapping.put("cod", Cod.class);
        foodEntityMapping.put("salmon", Salmon.class);
        foodEntityMapping.put("fish", TropicalFish.class);
        foodEntityMapping.put("rotten_flesh", Zombie.class);

        Map<Item, Set<Class<? extends LivingEntity>>> builder = new Object2ObjectOpenHashMap<>();
        ForgeRegistries.ITEMS.getEntries().forEach(entry -> {
            Item item = entry.getValue();
            if (!item.isEdible()) return;

            String itemId = entry.getKey().location().toString();

            for (Map.Entry<String, Class<? extends LivingEntity>> mapping : foodEntityMapping.entrySet()) {
                if (itemId.contains(mapping.getKey())) {
                    builder.computeIfAbsent(item, k -> new ObjectOpenHashSet<>()).add(mapping.getValue());
                    break;
                }
            }
        });
        foodToEntityClass = ImmutableMap.copyOf(builder);
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        if (foodToEntityClass == null) {
            Thread thread = new Thread(FoodHurtAnimalEventHandler::initialize);
            thread.setDaemon(true);
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        }
    }

    @SubscribeEvent
    public static void onFoodConsume(LivingEntityUseItemEvent event) {
        if (GTOConfig.INSTANCE.enableAnimalsAreAfraidToEatTheirMeat && foodToEntityClass != null) {
            if (event.getEntity() instanceof Player player && Objects.equals(10, event.getDuration()) && !player.level().isClientSide()) {
                int distance = GTOConfig.INSTANCE.enableAnimalsAreAfraidToEatTheirMeatRange;
                for (var classe : foodToEntityClass.getOrDefault(event.getItem().getItem(), Set.of())) {
                    hurtAnimalsNearPlayer(player, classe, distance);
                }
            }
        }
    }

    private static void hurtAnimalsNearPlayer(Player player, Class<? extends LivingEntity> entityClass, float distance) {
        Level level = player.level();
        List<? extends LivingEntity> entitiesOfClass = level.getEntitiesOfClass(entityClass, player.getBoundingBox().inflate(distance));
        entitiesOfClass.forEach(entity -> {
            entity.hurt(player.damageSources().playerAttack(player), Math.max(entity.getMaxHealth() / 40, 0.25F));
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        ParticleTypes.ANGRY_VILLAGER,
                        entity.getX(),
                        entity.getY() + entity.getBbHeight() * 0.75, // 在实体头部上方
                        entity.getZ(),
                        5,  // 粒子数量
                        0.3, // X方向扩散
                        0.2, // Y方向扩散
                        0.3, // Z方向扩散
                        0.02 // 粒子速度
                );
            }
        });
    }
}
