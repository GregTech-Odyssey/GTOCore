package com.gto.gtocore.common.forge;

import com.gto.gtocore.api.playerskill.SkillRegistry;
import com.gto.gtocore.api.playerskill.SkillValues;
import com.gto.gtocore.api.playerskill.command.Administration;
import com.gto.gtocore.api.playerskill.data.ExperienceSystemManager;
import com.gto.gtocore.api.playerskill.data.PlayerData;
import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;
import com.gto.gtocore.api.playerskill.utils.UtilsAttribute;
import com.gto.gtocore.api.playerskill.utils.UtilsData;
import com.gto.gtocore.utils.ItemUtils;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * 经验系统事件处理器
 * 负责处理与玩家经验、技能相关的各种事件
 */
public class ExperienceEventHandler {

    /**
     * 注册命令事件处理
     * 当服务器启动并注册命令时，将技能管理相关命令注册到命令调度器
     * 
     * @param event 命令注册事件
     */
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        Administration.register(event.getDispatcher());
    }

    /**
     * 玩家Tick事件处理
     * 定期为玩家增加各种技能经验值
     * 
     * @param event 玩家Tick事件
     */
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (event.phase == TickEvent.Phase.END && player.tickCount % SkillValues.GainExperience.GAP_TICK == 0) {
            if (ExperienceSystemManager.INSTANCE != null && ExperienceSystemManager.INSTANCE.isEnabled() && player.level() instanceof ServerLevel) {
                PlayerData playerData = ExperienceSystemManager.INSTANCE.getPlayerData(player.getUUID());
                SkillRegistry.getAll().forEach(skill -> {
                    BasicExperienceLevel level = skill.getExperienceLevel(playerData);
                    long point = SkillValues.GainExperience.EXPERIENCE_RATES.get(skill);
                    UtilsData.addExperienceAndSendMessage(player, level, point);
                });
            }
        }
    }

    // @SubscribeEvent
    // public static void onPlayerEatFood(LivingEntityUseItemEvent.Finish event) {
    // if (ExperienceSystemManager.INSTANCE.isEnabled() && event.getEntity() instanceof Player player && player.level()
    // instanceof ServerLevel) {
    // ItemStack item = event.getItem();
    // if (isMeat(item)) {
    // PlayerData playerData = ExperienceSystemManager.INSTANCE.getPlayerData(player.getUUID());
    // UtilsData.addExperienceAndSendMessage(player, playerData.getStrengthExperienceLevel(),
    // SkillValues.ExperienceIncome.EAT_MEAT);
    // UtilsData.addExperienceAndSendMessage(player, playerData.getLifeIntensityExperienceLevel(),
    // SkillValues.ExperienceIncome.EAT_MEAT);
    // }
    // }
    // }

    /**
     * 玩家加入世界事件处理
     * 当玩家加入世界时，刷新并应用玩家属性修饰符
     * 
     * @param event 实体加入世界事件
     */
    @SubscribeEvent
    public static void onPlayerJoin(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Player player && !player.level().isClientSide) {
            UtilsAttribute.freshDelayApplyModifier(player);
        }
    }

    /**
     * 玩家克隆事件处理
     * 当玩家死亡重生并克隆时，刷新并应用玩家属性修饰符
     * 
     * @param event 玩家克隆事件
     */
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide) {
            UtilsAttribute.freshDelayApplyModifier(player);
        }
    }

    /**
     * 玩家重生事件处理
     * 当玩家重生时，刷新并应用玩家属性修饰符
     * 
     * @param event 玩家重生事件
     */
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide) {
            UtilsAttribute.freshDelayApplyModifier(player);
        }
    }

    /**
     * 玩家切换维度事件处理
     * 当玩家在不同维度间切换时，刷新并应用玩家属性修饰符
     * 
     * @param event 玩家切换维度事件
     */
    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide) {
            UtilsAttribute.freshDelayApplyModifier(player);
        }
    }

    /**
     * 判断物品是否为肉类食物
     * 通过物品ID与预定义的肉类关键词进行匹配来判断
     * 
     * @param item 物品堆栈
     * @return 如果是肉类食物返回true，否则返回false
     */
    private static boolean isMeat(ItemStack item) {
        if (!item.isEdible()) return false;
        String id = ItemUtils.getId(item);
        for (String keyword : SkillValues.MEAT_KEYWORDS) {
            if (id.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}
