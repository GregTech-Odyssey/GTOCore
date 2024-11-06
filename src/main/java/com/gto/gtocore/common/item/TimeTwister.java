package com.gto.gtocore.common.item;

import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.item.component.IInteractionItem;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IOverclockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

import com.hepdd.gtmthings.api.misc.WirelessEnergyManager;

import java.util.Objects;

public class TimeTwister implements IInteractionItem {

    public static final TimeTwister INSTANCE = new TimeTwister();

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        RecipeLogic recipeLogic = GTCapabilityHelper.getRecipeLogic(context.getLevel(), context.getClickedPos(), null);
        if (recipeLogic != null && recipeLogic.isWorking()) {
            MetaMachine machine = recipeLogic.getMachine();
            if (machine instanceof IOverclockMachine overclockMachine) {
                Player player = Objects.requireNonNull(context.getPlayer());
                int reducedDuration = (int) ((recipeLogic.getDuration() - recipeLogic.getProgress()) * 0.5);
                long eu = 8 * reducedDuration * overclockMachine.getOverclockVoltage();
                if (eu > 0 && WirelessEnergyManager.addEUToGlobalEnergyMap(player.getUUID(), -eu, machine)) {
                    recipeLogic.setProgress(recipeLogic.getProgress() + reducedDuration);
                    player.displayClientMessage(Component.literal("消耗了 " + FormattingUtil.formatNumbers(eu) + " EU，使机器运行时间减少了 " + reducedDuration + " tick"), true);
                }
            }
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }
}
