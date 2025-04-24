package com.gto.gtocore.common.item.playerskill;

import com.gregtechceu.gtceu.api.item.component.IInteractionItem;
import com.gto.gtocore.common.data.GTOEffects;
import com.gto.gtocore.utils.ColorUtils;
import com.gto.gtocore.utils.StringUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;

public class MysteriousBoostPotionBehaviour implements IInteractionItem {
    public final int tier;

    public MysteriousBoostPotionBehaviour(int tier) {
        this.tier = tier;
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack itemStack, UseOnContext context) {
        return IInteractionItem.super.onItemUseFirst(itemStack, context);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return IInteractionItem.super.useOn(context);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Item item, Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide()) {
            ItemStack itemInHand = player.getItemInHand(usedHand);
            player.addEffect(new MobEffectInstance(GTOEffects.MYSTERIOUS_BOOST_EFFECT.get(), 20 * 60 * (5+tier) , tier));
            itemInHand.shrink(1);
            player.sendSystemMessage(Component.literal(StringUtils.full_color(Component.translatable("gtocore.player_exp_status.mysterious_boost_potion.success").getString())));
        }
        return IInteractionItem.super.use(item, level, player, usedHand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        return IInteractionItem.super.finishUsingItem(stack, level, livingEntity);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return IInteractionItem.super.getUseAnimation(stack);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return IInteractionItem.super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        return IInteractionItem.super.interactLivingEntity(stack, player, interactionTarget, usedHand);
    }

    @Override
    public boolean sneakBypassUse(ItemStack stack, LevelReader level, BlockPos pos, Player player) {
        return IInteractionItem.super.sneakBypassUse(stack, level, pos, player);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return IInteractionItem.super.onEntitySwing(stack, entity);
    }

    @Override
    public void onAttached(Item item) {
        IInteractionItem.super.onAttached(item);
    }
}
