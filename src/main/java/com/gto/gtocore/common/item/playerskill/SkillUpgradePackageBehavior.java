package com.gto.gtocore.common.item.playerskill;

import com.gto.gtocore.api.GTOValues;
import com.gto.gtocore.api.playerskill.SkillData;
import com.gto.gtocore.api.playerskill.SkillData.SkillType;
import com.gto.gtocore.api.playerskill.data.ExperienceSystemManager;
import com.gto.gtocore.api.playerskill.data.PlayerData;
import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;
import com.gto.gtocore.api.playerskill.utils.UtilsData;

import com.gregtechceu.gtceu.api.item.component.IInteractionItem;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SkillUpgradePackageBehavior implements IInteractionItem {

    public SkillUpgradePackageBehavior(int tier, SkillType skillType) {
        this.tier = tier;
        this.skillType = skillType;
    }

    private final int tier;
    private final SkillType skillType;

    /*
     * 1.尽可以对当前生命强度及以下使用
     * 2.可增加对应Tier经验10%
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Item item, Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide()) {
            ItemStack itemInHand = player.getItemInHand(usedHand);
            PlayerData playerData = ExperienceSystemManager.INSTANCE.getPlayerData(player.getUUID());
            BasicExperienceLevel targetExpLevel = skillType.getExperienceLevel(playerData);
            if (skillType != SkillType.LIFE_INTENSITY && targetExpLevel.getLevel() == targetExpLevel.getMaxLevel()) {
                player.sendSystemMessage(Component.translatable("gtocore.player_exp_status.upgrade_institution"));
                return IInteractionItem.super.use(item, level, player, usedHand);
            } // 防止套用实时经验包计算机制来非法保留经验
            int targetSkillVoltage = targetExpLevel.getVoltage();
            int tierGap = tier - targetSkillVoltage;
            if (tierGap < 0) {
                player.sendSystemMessage(Component.translatable("gtocore.player_exp_status.sup.error",
                        GTOValues.VNFR[targetSkillVoltage],
                        targetExpLevel.getName(),
                        GTOValues.VNFR[targetSkillVoltage],
                        targetExpLevel.getName()));
                return IInteractionItem.super.use(item, level, player, usedHand);
            } // 只能使用同等级和以上的经验包
            int experienceForNextLevel = targetExpLevel.getExperienceForNextLevel();
            UtilsData.addExperienceAndSendMessage(player, targetExpLevel, SkillData.Formula.upgradePackageBonus.apply(tierGap, experienceForNextLevel));
            itemInHand.grow(-1);
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS,
                    0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
            player.getCooldowns().addCooldown(item, 1); // 1tick冷却防止bug
        }
        return IInteractionItem.super.use(item, level, player, usedHand);
    }
}
