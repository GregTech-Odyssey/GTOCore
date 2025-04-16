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

/**
 * 食物伤害动物事件处理器
 * 实现当玩家吃某类动物肉时，附近对应动物会受到惊吓和伤害的功能
 */
public class FoodHurtAnimalEventHandler {

    /**
     * 食物到实体类的映射表
     * 建立食物物品与对应来源生物的关联关系
     */
    private static ImmutableMap<Item, Set<Class<? extends LivingEntity>>> foodToEntityClass;

    /**
     * 初始化食物到实体类的映射关系
     * 扫描所有已注册的食物物品，根据物品ID匹配对应的动物类型
     */
    private static void initialize() {
        // 创建食物关键词到实体类的映射
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

        // 构建物品到实体类集合的映射
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

    /**
     * 服务器启动事件处理
     * 在服务器启动时初始化食物到实体类的映射表
     * 使用低优先级线程执行以避免影响服务器启动性能
     * 
     * @param event 服务器启动事件
     */
    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        if (foodToEntityClass == null) {
            Thread thread = new Thread(FoodHurtAnimalEventHandler::initialize);
            thread.setDaemon(true);
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        }
    }

    /**
     * 食物消耗事件处理
     * 当玩家食用肉类食物时，处理附近相应动物的反应
     * 
     * @param event 实体使用物品事件
     */
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

    /**
     * 处理玩家附近的动物伤害效果
     * 对指定范围内的特定类型动物施加伤害和视觉特效
     * 
     * @param player      玩家实体
     * @param entityClass 要处理的实体类型
     * @param distance    检测范围
     */
    private static void hurtAnimalsNearPlayer(Player player, Class<? extends LivingEntity> entityClass, float distance) {
        Level level = player.level();
        List<? extends LivingEntity> entitiesOfClass = level.getEntitiesOfClass(entityClass, player.getBoundingBox().inflate(distance));
        entitiesOfClass.forEach(entity -> {
            // 对实体造成伤害，数值为最大生命值的1/40或至少0.25
            entity.hurt(player.damageSources().playerAttack(player), Math.max(entity.getMaxHealth() / 40, 0.25F));
            if (level instanceof ServerLevel serverLevel) {
                // 在实体头部生成"生气"粒子效果
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
