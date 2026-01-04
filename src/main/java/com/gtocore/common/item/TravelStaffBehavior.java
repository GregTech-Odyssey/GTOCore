package com.gtocore.common.item;

import com.gtocore.common.data.translation.GTOItemTooltips;
import com.gtocore.config.GTOConfig;
import com.gtocore.eio_travel.api.TravelHandler;

import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.capability.IElectricItem;
import com.gregtechceu.gtceu.api.item.component.ElectricStats;

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

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.gregtechceu.gtceu.api.GTValues.LV;

public class TravelStaffBehavior extends ElectricStats {

    private static final long EU_COST = 128L;
    private static final long EU_MAX = 768000L;

    protected TravelStaffBehavior(long maxCharge, int tier, boolean chargeable, boolean dischargeable) {
        super(maxCharge, tier, chargeable, dischargeable);
    }

    public static TravelStaffBehavior create() {
        return new TravelStaffBehavior(
                EU_MAX,
                LV,
                true,
                false);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Item self, Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (getActivationStatus(stack).isAir()) {
            if (tryPerformAction(self, level, player, stack)) {
                return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
            }
            return InteractionResultHolder.fail(stack);
        }
        return super.use(self, level, player, usedHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (getActivationStatus(context.getItemInHand()).isBlock()) {
            if (context.getPlayer() != null && tryPerformAction(context.getItemInHand().getItem(), context.getLevel(), context.getPlayer(), context.getItemInHand())) {
                return InteractionResult.sidedSuccess(context.getLevel().isClientSide());
            }

            return InteractionResult.FAIL;
        }

        return super.useOn(context);
    }

    private boolean tryPerformAction(Item self, Level level, Player player, ItemStack stack) {
        boolean isCreative = player.isCreative();
        if (hasResources(stack) || isCreative) {
            if (performAction(self, level, player, stack)) {
                if (!level.isClientSide() && !isCreative) {
                    consumeResources(stack);
                }

                return true;
            }

            return false;
        }

        return false;
    }

    /**
     * Perform your action
     * 
     * @return true if it was a success and you want to consume the resources
     */
    public boolean performAction(Item self, Level level, Player player, ItemStack stack) {
        if (player.isShiftKeyDown()) {
            if (TravelHandler.shortTeleport(level, player)) {
                player.getCooldowns().addCooldown(self, GTOConfig.INSTANCE.travelConfig.travelStaffCooldown);
                return true;
            }
        } else {
            if (TravelHandler.blockTeleport(level, player)) {
                player.getCooldowns().addCooldown(self, GTOConfig.INSTANCE.travelConfig.travelStaffCooldown);
                return true;
            }
        }
        return false;
    }

    @Override
    public void appendTooltips(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents,
                               TooltipFlag isAdvanced) {
        tooltipComponents.addAll(GTOItemTooltips.INSTANCE.getTravelStaff().get());
        super.appendTooltips(stack, level, tooltipComponents, isAdvanced);
    }

    public boolean hasResources(ItemStack stack) {
        IElectricItem electricItem = GTCapabilityHelper.getElectricItem(stack);
        if (electricItem == null) return false;
        return electricItem.getCharge() >= EU_COST;
    }

    public void consumeResources(ItemStack stack) {
        IElectricItem electricItem = GTCapabilityHelper.getElectricItem(stack);
        if (electricItem == null) return;
        electricItem.discharge(
                EU_COST,
                LV,
                true,
                true,
                false);
    }

    protected ActivationStatus getActivationStatus(ItemStack stack) {
        return ActivationStatus.ALL;
    }

    @Getter
    protected enum ActivationStatus {

        BLOCK(true, false),
        AIR(false, true),
        ALL(true, true);

        private final boolean isBlock;
        private final boolean isAir;

        ActivationStatus(boolean isBlock, boolean isAir) {
            this.isBlock = isBlock;
            this.isAir = isAir;
        }
    }
}
