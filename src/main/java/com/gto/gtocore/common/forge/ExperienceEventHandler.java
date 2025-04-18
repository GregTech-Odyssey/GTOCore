package com.gto.gtocore.common.forge;

import com.gto.gtocore.api.playerskill.SkillData;
import com.gto.gtocore.api.playerskill.command.Administration;
import com.gto.gtocore.api.playerskill.data.ExperienceSystemManager;
import com.gto.gtocore.api.playerskill.data.PlayerData;
import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;
import com.gto.gtocore.api.playerskill.utils.UtilsData;
import com.gto.gtocore.utils.ItemUtils;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ExperienceEventHandler {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        Administration.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (event.phase == TickEvent.Phase.END && player.tickCount % SkillData.GainExperience.GAP_TICK == 0) {
            if (ExperienceSystemManager.INSTANCE != null && ExperienceSystemManager.INSTANCE.isEnabled() && player.level() instanceof ServerLevel) {
                PlayerData playerData = ExperienceSystemManager.INSTANCE.getPlayerData(player.getUUID());
                for (SkillData.SkillType type : SkillData.SkillType.values()) {
                    BasicExperienceLevel level = type.getExperienceLevel(playerData);
                    Integer point = SkillData.GainExperience.EXPERIENCE_RATES.get(type);
                    UtilsData.addExperienceAndSendMessage(player, level, point);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerEatFood(LivingEntityUseItemEvent.Finish event) {
        if (ExperienceSystemManager.INSTANCE.isEnabled() && event.getEntity() instanceof Player player && player.level() instanceof ServerLevel) {
            ItemStack item = event.getItem();
            if (isMeat(item)) {
                PlayerData playerData = ExperienceSystemManager.INSTANCE.getPlayerData(player.getUUID());
                UtilsData.addExperienceAndSendMessage(player, playerData.getAttackExperienceLevel(), SkillData.ExperienceIncome.EAT_MEAT);
            }
        }
    }

    private static boolean isMeat(ItemStack item) {
        if (!item.isEdible()) return false;
        String id = ItemUtils.getId(item);
        for (String keyword : SkillData.MEAT_KEYWORDS) {
            if (id.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}
