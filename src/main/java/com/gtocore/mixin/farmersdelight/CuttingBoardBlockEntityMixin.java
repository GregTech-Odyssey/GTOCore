package com.gtocore.mixin.farmersdelight;

import com.gtolib.utils.TagUtils;

import com.gregtechceu.gtceu.api.GTValues;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;

import java.util.Optional;
import java.util.function.Consumer;

import javax.annotation.Nullable;

@Mixin(CuttingBoardBlockEntity.class)
public abstract class CuttingBoardBlockEntityMixin extends SyncedBlockEntity {

    public CuttingBoardBlockEntityMixin(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    @Shadow(remap = false)
    public abstract ItemStack getStoredItem();

    @Shadow(remap = false)
    public abstract void playSound(SoundEvent sound, float volume, float pitch);

    @Redirect(method = "processStoredItemUsingTool", at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V", remap = false), remap = false)
    private void redirectIfPresent(Optional<CuttingBoardRecipe> instance, Consumer<CuttingBoardRecipe> action, @Local(argsOnly = true, name = "arg2") @Nullable Player player) {
        if (!(level instanceof ServerLevel)) return;
        var cutResult = this.getStoredItem().is(TagUtils.createForgeItemTag("crops/onion")) && instance.isPresent();
        instance.ifPresent(action);

        if (cutResult) {
            this.playSound(SoundEvents.TRIDENT_RETURN, 1.0F, 1.0F);
            if (GTValues.RNG.nextFloat() < 0.05 && level.getBlockState(getBlockPos().below()).is(Blocks.OBSIDIAN)) {
                level.setBlockAndUpdate(getBlockPos().below(), Blocks.CRYING_OBSIDIAN.defaultBlockState());
                LightningBolt lb = EntityType.LIGHTNING_BOLT.create(level);
                if (lb != null) {
                    lb.setPos(getBlockPos().getCenter());
                    lb.setVisualOnly(true);
                    level.addFreshEntity(lb);
                }
            }
        }
    }
}
