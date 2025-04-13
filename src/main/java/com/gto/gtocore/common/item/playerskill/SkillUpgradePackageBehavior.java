package com.gto.gtocore.common.item.playerskill;

import com.gto.gtocore.api.playerskill.SkillData.SkillType;
import com.gto.gtocore.api.playerskill.data.ExperienceSystemManager;
import com.gto.gtocore.api.playerskill.data.PlayerData;
import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;

import com.gregtechceu.gtceu.api.item.component.IInteractionItem;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SkillUpgradePackageBehavior implements IInteractionItem {

    int tier;
    SkillType skillType;

    public SkillUpgradePackageBehavior(int tier, SkillType skillType) {
        this.tier = tier;
        this.skillType = skillType;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Item item, Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide()) {
            ItemStack itemInHand = player.getItemInHand(usedHand);
            PlayerData playerData = ExperienceSystemManager.INSTANCE.getPlayerData(player.getUUID());
            BasicExperienceLevel expLevel = skillType.getExperienceLevel(playerData);

        }

        return IInteractionItem.super.use(item, level, player, usedHand);
    }
}
