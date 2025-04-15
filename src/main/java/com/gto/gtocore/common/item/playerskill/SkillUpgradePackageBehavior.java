package com.gto.gtocore.common.item.playerskill;

import com.gto.gtocore.api.GTOValues;
import com.gto.gtocore.api.playerskill.SkillData.SkillType;
import com.gto.gtocore.api.playerskill.data.ExperienceSystemManager;
import com.gto.gtocore.api.playerskill.data.PlayerData;
import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;

import com.gregtechceu.gtceu.api.item.component.IInteractionItem;

import net.minecraft.network.chat.Component;
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
            int bodyVoltage = playerData.getBodyExperienceLevel().getVoltage();
            int targetSkillVoltage = targetExpLevel.getVoltage();
            int tierGap = tier - targetSkillVoltage;
            if (tierGap < 0) {
                player.sendSystemMessage(Component.translatable("gtocore.player_exp_status.sup.error",
                        GTOValues.VNFR[targetSkillVoltage],
                        targetExpLevel.getName(),
                        GTOValues.VNFR[targetSkillVoltage],
                        targetExpLevel.getName()));
                return IInteractionItem.super.use(item, level, player, usedHand);
            }

        }

        return IInteractionItem.super.use(item, level, player, usedHand);
    }
}
