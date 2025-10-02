package com.gtocore.mixin.ae2;

import appeng.items.tools.quartz.QuartzCuttingKnifeItem;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(QuartzCuttingKnifeItem.class)
public class QuartzCuttingKnifeItemMixin {

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true, remap = false)
    private void onUseOnClient(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Player player = context.getPlayer();
        Level level = context.getLevel();

        if (player != null && player.isShiftKeyDown()) {
            if (level.isClientSide) {
                BlockPos pos = context.getClickedPos();
                BlockState state = level.getBlockState(pos);
                if (!state.isAir()) {
                    Component blockNameComponent = state.getBlock().getName();
                    gtocore$setClipboard(blockNameComponent);
                    player.displayClientMessage(blockNameComponent, false);
                }
            }
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }

    @Unique
    @OnlyIn(Dist.CLIENT)
    private void gtocore$setClipboard(Component localizedName) {
        Minecraft.getInstance().keyboardHandler.setClipboard(localizedName.getString());
    }
}