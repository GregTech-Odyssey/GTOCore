package com.gtocore.mixin.ae2;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import appeng.api.integrations.igtooltip.TooltipContext;
import appeng.api.parts.IPartHost;
import appeng.integration.modules.igtooltip.parts.PartHostTooltips;
import appeng.items.tools.quartz.QuartzCuttingKnifeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(QuartzCuttingKnifeItem.class)
public class QuartzCuttingKnifeItemMixin {

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void onUseOnClient(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Player player = context.getPlayer();
        Level level = context.getLevel();

        if (player != null && player.isShiftKeyDown()) {
            if (level.isClientSide) {
                BlockPos pos = context.getClickedPos();
                BlockState state = level.getBlockState(pos);
                BlockEntity tile = level.getBlockEntity(pos);
                Component blockNameComponent = null;
                if (tile instanceof IPartHost partHost) {
                    blockNameComponent = PartHostTooltips.getName(partHost, new TooltipContext(
                            null,
                            context.getClickLocation(),
                            player));
                }
                if (blockNameComponent == null && !state.isAir()) {
                    blockNameComponent = state.getBlock().getName();
                }
                if (blockNameComponent != null) {
                    gtocore$setClipboard(blockNameComponent);
                    player.displayClientMessage(blockNameComponent, false);
                }
            }
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }

    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        if (action == ClickAction.SECONDARY && slot.hasItem()) {
            if (player.level().isClientSide()) {
                gtocore$setClipboard(slot.getItem().getHoverName());
                player.displayClientMessage(slot.getItem().getDisplayName(), false);
            }
            return true;
        }
        return false;
    }

    @Unique
    @OnlyIn(Dist.CLIENT)
    private static void gtocore$setClipboard(Component localizedName) {
        Minecraft.getInstance().keyboardHandler.setClipboard(localizedName.getString());
    }
}
