package com.gtocore.mixin.ae2.blockentity;

import com.gtolib.api.blockentity.IDirectionCacheBlockEntity;

import com.gregtechceu.gtceu.utils.cache.BlockEntityDirectionCache;
import com.gregtechceu.gtceu.utils.collection.O2OOpenCacheHashMap;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import appeng.blockentity.AEBaseBlockEntity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(AEBaseBlockEntity.class)
public class AEBaseBlockEntityMixin extends BlockEntity implements IDirectionCacheBlockEntity {

    @Shadow(remap = false)
    @Mutable
    @Final
    private static Map<BlockEntityType<?>, Item> REPRESENTATIVE_ITEMS;
    @Unique
    private BlockEntityDirectionCache gtolib$directionCache;

    public AEBaseBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void gtolib$init(CallbackInfo ci) {
        REPRESENTATIVE_ITEMS = new O2OOpenCacheHashMap<>();
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void gtolib$init(BlockEntityType blockEntityType, BlockPos pos, BlockState blockState, CallbackInfo ci) {
        gtolib$directionCache = BlockEntityDirectionCache.create();
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    protected Item getItemFromBlockEntity() {
        return REPRESENTATIVE_ITEMS.getOrDefault(getType(), Items.BARRIER);
    }

    @Override
    public BlockEntityDirectionCache gtolib$getDirectionCache() {
        return gtolib$directionCache;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        gtolib$directionCache.clearCache();
    }

    @Override
    public void clearRemoved() {
        gtolib$directionCache.clearCache();
        super.clearRemoved();
    }
}
