package com.gtocore.common.forge

import com.gtocore.api.misc.AnimalsRevengeAttackGoal
import com.gtocore.config.GTOConfig

import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.event.level.LevelEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

import com.gregtechceu.gtceu.api.machine.TickableSubscription
import com.gregtechceu.gtceu.utils.GTUtil
import com.gregtechceu.gtceu.utils.TaskHandler
import com.gtolib.api.annotation.DataGeneratorScanned
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import kotlinx.coroutines.*

import java.util.*
import kotlin.math.max

@DataGeneratorScanned
object AnimalsRevengeEvent {
    // 实体 -> 掉落物
    private val entityLootCache = Object2ObjectOpenHashMap<EntityType<*>, ObjectOpenHashSet<Item>>()

    // 实体 -> 派生食物
    private val derivedFoodCache = Object2ObjectOpenHashMap<EntityType<*>, ObjectOpenHashSet<Item>>()
    private val stackCache = Object2ObjectOpenHashMap<Item, ItemStack>()

    private var lootCacheBuilt: Boolean = false
    private var lootBuildJob: Deferred<Unit>? = null

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private data class EatState(val eatenItem: Item, var maxRemainingSeen: Int, var halfTriggered: Boolean = false)
    private val eatStates = Object2ObjectOpenHashMap<UUID, EatState>()

    @SubscribeEvent
    @JvmStatic
    fun onLevelLoad(event: LevelEvent.Load) {
        val level = event.level
        if (level !is ServerLevel) return
        lootCacheBuilt = false
        lootBuildJob?.cancel()
        lootBuildJob = null

        entityLootCache.clear()
        derivedFoodCache.clear()
        eatStates.clear()
        stackCache.clear()
    }

    @SubscribeEvent
    @JvmStatic
    fun onLivingTick(event: LivingEvent.LivingTickEvent) {
        val player = event.entity as? ServerPlayer ?: return
        val serverLevel = player.level() as? ServerLevel ?: return

        if (!player.isUsingItem) {
            eatStates.remove(player.uuid)
            return
        }

        val using = player.useItem
        if (using.isEmpty || !using.isEdible) return

        val remaining = player.useItemRemainingTicks
        if (remaining <= 0) return

        val st = eatStates[player.uuid]?.takeIf { it.eatenItem == using.item }
            ?: EatState(using.item, remaining).also { eatStates[player.uuid] = it }

        if (remaining > st.maxRemainingSeen) st.maxRemainingSeen = remaining

        if (!st.halfTriggered && remaining <= (st.maxRemainingSeen / 2)) {
            st.halfTriggered = true
            triggerCannibalismEffect(serverLevel, player, using)
        }
    }

    @SubscribeEvent
    @JvmStatic
    fun onFinishEat(event: LivingEntityUseItemEvent.Finish) {
        val player = event.entity as? ServerPlayer ?: return
        eatStates.remove(player.uuid)
    }

    private fun triggerCannibalismEffect(serverLevel: ServerLevel, player: ServerPlayer, eaten: ItemStack) {
        if (!ensureLootCache(serverLevel)) return

        val radius = max(1, GTOConfig.INSTANCE.cannibalismRadius)
        val damage = max(0.0f, GTOConfig.INSTANCE.cannibalismDamage)
        if (damage <= 0.0f) return

        val center: Vec3 = player.position().add(0.0, 0.1, 0.0)
        val area = AABB(center, center).inflate(radius.toDouble(), 1.0, radius.toDouble())
        val entities = serverLevel.getEntitiesOfClass(LivingEntity::class.java, area)
        if (entities.isEmpty()) return

        val preTicks = 5
        for (mob in entities) {
            if (mob === player) continue
            val type = mob.type
            val baseDrops = entityLootCache[type] ?: continue
            if (baseDrops.isEmpty()) continue

            if (!isFoodFromEntity(type, eaten.item, serverLevel)) continue

            val tick = intArrayOf(0)
            val holder = arrayOfNulls<TickableSubscription>(1)
            holder[0] = TaskHandler.enqueueServerTick(serverLevel, {
                tick[0]++
                if (tick[0] >= preTicks) {
                    if (mob.isAlive) {
                        val hurt = mob.hurt(serverLevel.damageSources().generic(), damage)
                        if (hurt) makeAnimalAggressive(mob, player)
                    }
                    holder[0]?.unsubscribe()
                }
            }, GTUtil.NOOP, 1)
        }
    }

    private fun ensureLootCache(serverLevel: ServerLevel): Boolean {
        if (lootCacheBuilt) return true
        val job = lootBuildJob
        if (job == null || job.isCancelled) {
            lootBuildJob = scope.async { buildEntityLootCacheIncremental(serverLevel) }
        }
        return false
    }

    private fun makeAnimalAggressive(entity: LivingEntity, target: ServerPlayer) {
        val mob = entity as? Mob ?: return
        if (mob.type.category == MobCategory.MONSTER) return
        val pm = mob as? PathfinderMob ?: return
        val tag = mob.persistentData
        if (!tag.getBoolean("gtocore_temp_aggressive")) {
            tag.putBoolean("gtocore_temp_aggressive", true)
            pm.goalSelector.addGoal(
                1,
                AnimalsRevengeAttackGoal(pm, 1.2, 1.6, 20, max(1.0f, GTOConfig.INSTANCE.cannibalismDamage)),
            )
            pm.targetSelector.addGoal(1, NearestAttackableTargetGoal(pm, ServerPlayer::class.java, true))
        }
        mob.target = target
        mob.setLastHurtByPlayer(target)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun buildEntityLootCacheIncremental(level: ServerLevel) {
        if (lootCacheBuilt) return
        val types = BuiltInRegistries.ENTITY_TYPE.toList().iterator()

        return suspendCancellableCoroutine { cont ->
            val subHolder = arrayOfNulls<TickableSubscription>(1)
            subHolder[0] = TaskHandler.enqueueServerTick(level, {
                var processed = 0
                try {
                    while (processed < 12 && types.hasNext()) {
                        val type = types.next()
                        try {
                            val entity = type.create(level)
                            if (entity is LivingEntity) {
                                val drops = sampleEntityLootItems(entity, level)
                                if (!drops.isEmpty()) entityLootCache[type] = drops
                            }
                            entity?.discard()
                        } catch (_: Throwable) {
                        }
                        processed++
                    }

                    if (!types.hasNext()) {
                        subHolder[0]?.unsubscribe()
                        lootCacheBuilt = true
                        if (cont.isActive) cont.resume(Unit) {}
                    }
                } catch (_: Throwable) {
                    subHolder[0]?.unsubscribe()
                    lootCacheBuilt = true
                    if (cont.isActive) cont.resume(Unit) {}
                }
            }, GTUtil.NOOP, 1)

            cont.invokeOnCancellation { subHolder[0]?.unsubscribe() }
        }
    }

    private fun sampleEntityLootItems(entity: LivingEntity, level: ServerLevel): ObjectOpenHashSet<Item> {
        val tableId: ResourceLocation = entity.type.defaultLootTable
        val table: LootTable = level.server.lootData.getLootTable(tableId)
        if (table == LootTable.EMPTY) return ObjectOpenHashSet()

        val result = ObjectOpenHashSet<Item>()
        val params = LootParams.Builder(level)
            .withParameter(LootContextParams.THIS_ENTITY, entity)
            .withParameter(LootContextParams.ORIGIN, entity.position())
            .withParameter(LootContextParams.DAMAGE_SOURCE, level.damageSources().generic())
            .create(LootContextParamSets.ENTITY)

        repeat(32) {
            for (stack in table.getRandomItems(params)) {
                if (!stack.isEmpty) result.add(stack.item)
            }
        }
        return result
    }

    // ========= 缓存 + API=========

    @JvmStatic
    fun isFoodFromEntity(type: EntityType<*>, item: Item, level: ServerLevel): Boolean {
        ensureDerivedForType(type, level)
        val derived = derivedFoodCache[type] ?: return false
        return derived.contains(item)
    }

    private fun ensureLootForType(type: EntityType<*>, level: ServerLevel) {
        if (entityLootCache.containsKey(type)) return
        if (lootCacheBuilt) return
        try {
            val entity = type.create(level)
            if (entity is LivingEntity) {
                val drops = sampleEntityLootItems(entity, level)
                if (!drops.isEmpty()) entityLootCache[type] = drops
            }
            entity?.discard()
        } catch (_: Throwable) {
            // ignore
        }
    }

    private fun ensureDerivedForType(type: EntityType<*>, level: ServerLevel) {
        if (derivedFoodCache.containsKey(type)) return
        ensureLootForType(type, level)
        val base = entityLootCache[type] ?: ObjectOpenHashSet()

        val derived = ObjectOpenHashSet<Item>()
        if (!base.isEmpty()) {
            // 先放入基础掉落
            derived.addAll(base)
            // 烹饪派生
            fillCookedOutputs(level.recipeManager, level.registryAccess(), base, derived)
            // 合成派生（允许使用由基础烹饪得到的物品）
            val cookedFromBase = ObjectOpenHashSet<Item>(derived)
            cookedFromBase.removeAll(base)
            fillCraftingOutputs(level.recipeManager, level.registryAccess(), base, cookedFromBase, derived)
        }
        derivedFoodCache[type] = derived
    }

    private fun fillCookedOutputs(manager: RecipeManager, access: RegistryAccess, base: ObjectOpenHashSet<Item>, derived: ObjectOpenHashSet<Item>) {
        val cookingTypes = arrayOf(RecipeType.SMELTING, RecipeType.SMOKING, RecipeType.CAMPFIRE_COOKING)
        for (type in cookingTypes) {
            for (r in manager.getAllRecipesFor(type)) {
                val usesBase = r.ingredients.any { ing -> ingredientMatchesAny(ing, base) }
                if (usesBase) {
                    val out = r.getResultItem(access)
                    if (!out.isEmpty) derived.add(out.item)
                }
            }
        }
    }
    private fun fillCraftingOutputs(manager: RecipeManager, access: RegistryAccess, base: ObjectOpenHashSet<Item>, cookedFromBase: ObjectOpenHashSet<Item>, derived: ObjectOpenHashSet<Item>) {
        for (recipe in manager.getAllRecipesFor(RecipeType.CRAFTING)) {
            val out = recipe.getResultItem(access)
            if (out.isEmpty) continue
            var hit = false
            for (ing in recipe.ingredients) {
                if (ingredientMatchesAny(ing, base) || ingredientMatchesAny(ing, cookedFromBase)) {
                    hit = true
                    break
                }
            }
            if (hit) derived.add(out.item)
        }
    }
    private fun ingredientMatchesAny(ing: net.minecraft.world.item.crafting.Ingredient, items: ObjectOpenHashSet<Item>): Boolean {
        if (items.isEmpty()) return false
        for (item in items) {
            val stack = stackCache.computeIfAbsent(item) { ItemStack(item) }
            if (ing.test(stack)) return true
        }
        return false
    }
}
