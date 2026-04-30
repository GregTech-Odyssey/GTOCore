package com.gtocore.mixin.ae2.blockentity;

import com.gtocore.integration.ae.hooks.PatternProviderPlacementHelper;

import com.gtolib.api.blockentity.IDirectionCacheBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import appeng.block.AEBaseBlock;
import appeng.block.AEBaseEntityBlock;
import appeng.block.crafting.PatternProviderBlock;
import appeng.blockentity.AEBaseBlockEntity;

import com.glodblock.github.extendedae.common.blocks.BlockExPatternProvider;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AEBaseEntityBlock.class)
public abstract class AEBaseEntityBlockMixin<T extends AEBaseBlockEntity> extends AEBaseBlock {

    @Shadow(remap = false)
    public abstract @Nullable T getBlockEntity(BlockGetter level, BlockPos pos);

    protected AEBaseEntityBlockMixin(Properties props) {
        super(props);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide()) {
            var te = getBlockEntity(level, pos);
            if (te != null) {
                IDirectionCacheBlockEntity.getBlockEntityDirectionCache(te).clearCache();
            }
        }
    }

    @Inject(method = "setPlacedBy", at = @At("TAIL"), remap = false)
    private void gtocore$configureAdjacentMachine(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack, CallbackInfo ci) {
        if (level.isClientSide() || !(placer instanceof Player player) || !player.isShiftKeyDown()) {
            return;
        }
        if (!((Object) this instanceof PatternProviderBlock) && !((Object) this instanceof BlockExPatternProvider)) {
            return;
        }
        PatternProviderPlacementHelper.configureAdjacentMachine(getBlockEntity(level, pos));
    }
}
