package com.gtocore.common.item;

import com.gregtechceu.gtceu.api.item.component.IAddInformation;
import com.gregtechceu.gtceu.api.item.component.IInteractionItem;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.WorkableTieredMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MachineCoordinateCardBehavior implements IInteractionItem, IAddInformation {

    public MachineCoordinateCardBehavior() {}

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (context.getPlayer() != null) {
            BlockPos blockPos = context.getClickedPos();
            ItemStack card = context.getPlayer().getMainHandItem();
            MetaMachine machine = MetaMachine.getMachine(level, blockPos);
            if (machine instanceof WorkableTieredMachine || machine instanceof IMultiController) {
                CompoundTag tag = card.getOrCreateTag();
                tag.putInt("posX", blockPos.getX());
                tag.putInt("posY", blockPos.getY());
                tag.putInt("posZ", blockPos.getZ());
                tag.putString("name", machine.getDefinition().getBlock().getName().getString());
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Item item, Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide) {
            ItemStack card = player.getItemInHand(usedHand);
            card.setTag(null);
        }
        return IInteractionItem.super.use(item, level, player, usedHand);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("gtocore.tooltip.item.machine_coordinate_card.tooltip.1"));
        CompoundTag tag = itemStack.getTag();
        if (tag != null) {
            list.add(Component.translatable("gtocore.tooltip.item.machine_coordinate_card.tooltip.2",
                    tag.getString("name"), "§5" + tag.getInt("posX"), "§d" + tag.getInt("posY"), "§e" + tag.getInt("posZ")));
        }
    }
}
