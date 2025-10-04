package com.gtocore.mixin.ae2;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import appeng.api.parts.IPart;
import appeng.blockentity.networking.CableBusBlockEntity;
import appeng.items.tools.quartz.QuartzCuttingKnifeItem;
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
                BlockEntity tile = level.getBlockEntity(pos);
                Component blockNameComponent = null;

                if (tile instanceof CableBusBlockEntity cable) {
                    Vec3 hitVec = context.getClickLocation();
                    Vec3 hitInBlock = new Vec3(hitVec.x - pos.getX(), hitVec.y - pos.getY(), hitVec.z - pos.getZ());
                    IPart part = cable.getCableBus().selectPartLocal(hitInBlock).part;
                    if (part != null) {
                        blockNameComponent = part.getPartItem().asItem().getName(part.getPartItem().asItem().getDefaultInstance());
                    }
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

    @Unique
    @OnlyIn(Dist.CLIENT)
    private void gtocore$setClipboard(Component localizedName) {
        Minecraft.getInstance().keyboardHandler.setClipboard(localizedName.getString());
    }
}
