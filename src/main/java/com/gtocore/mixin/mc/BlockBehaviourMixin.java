package com.gtocore.mixin.mc;

import com.gtocore.common.data.GTOItems;

import com.gtolib.api.item.tool.GTOToolType;
import com.gtolib.mixin.ReferenceAccessor;

import com.gregtechceu.gtceu.api.item.IGTTool;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockBehaviour.class, priority = 0)
public class BlockBehaviourMixin {

    @Inject(method = "getDestroyProgress", at = @At("HEAD"), cancellable = true)
    private void getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        float f = state.getDestroySpeed(level, pos);
        ItemStack stack = player.getMainHandItem();
        if (f == -1.0f) {
            float p = 0.0F;
            Item item = stack.getItem();
            if (item instanceof IGTTool gtTool && gtTool.getToolType() == GTOToolType.VAJRA_IV) {
                p = 0.5F;
            } else if (state.is(Blocks.BEDROCK) && item == GTOItems.BEDROCK_DESTROYER.get()) p = 0.1F;
            cir.setReturnValue(p);
            return;
        }
        var block = state.getBlock();
        if (((ReferenceAccessor) block.builtInRegistryHolder()).getTags().isEmpty()) return;
        float i = stack.isCorrectToolForDrops(state) ? 1 : (block.defaultDestroyTime() > 1 && block != Blocks.BEACON) ? 0.001F : 0.5F;
        cir.setReturnValue(i * player.getDigSpeed(state, pos) / f / 30);
    }
}
