package com.gtocore.mixin.farmersdelight;

import com.gtolib.utils.TagUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.registry.ModSounds;

import java.util.Optional;
import java.util.function.Consumer;

import javax.annotation.Nullable;

@Mixin(CuttingBoardBlockEntity.class)
public abstract class CuttingBoardBlockEntityMixin extends SyncedBlockEntity {

    @Unique
    boolean gto$cutResult = false;

    public CuttingBoardBlockEntityMixin(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    @Shadow(remap = false)
    public abstract ItemStack removeItem();

    @Shadow(remap = false)
    public abstract ItemStack getStoredItem();

    @Shadow(remap = false)
    public abstract void playSound(SoundEvent sound, float volume, float pitch);

    @Redirect(method = "processStoredItemUsingTool", at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V", remap = false), remap = false)
    private void redirectIfPresent(Optional<CuttingBoardRecipe> instance, Consumer<CuttingBoardRecipe> action, @Local(argsOnly = true, name = "arg1") ItemStack toolStack, @Local(argsOnly = true, name = "arg2") @Nullable Player player) {
        gto$cutResult = toolStack.is(TagUtils.createForgeTag("tools/knives")) && this.getStoredItem().is(TagUtils.createForgeTag("crops/onion"));
        instance.ifPresent(action);
        if (gto$cutResult && level.getBlockState(getBlockPos().below()).is(Blocks.OBSIDIAN)) {
            if (instance.isEmpty()) {
                this.removeItem();
                if (player != null) {
                    toolStack.hurtAndBreak(1, player, (user) -> user.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                } else if (toolStack.hurt(1, this.level.random, null)) {
                    toolStack.setCount(0);
                }
            }
            this.playSound(ModSounds.BLOCK_CUTTING_BOARD_KNIFE.get(), 0.8F, 1.0F);
            this.playSound(SoundEvents.TRIDENT_RETURN, 1.2F, 1.0F);
            level.setBlockAndUpdate(getBlockPos().below(), Blocks.CRYING_OBSIDIAN.defaultBlockState());
        }
    }

    @Redirect(method = "processStoredItemUsingTool", at = @At(value = "INVOKE", target = "Ljava/util/Optional;isPresent()Z", remap = false), remap = false)
    private boolean redirectIsPresent(Optional<CuttingBoardRecipe> instance) {
        if (gto$cutResult) {
            gto$cutResult = false;
            return true;
        }
        return instance.isPresent();
    }
}
