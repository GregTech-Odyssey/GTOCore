package com.gtocore.mixin.botania;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.ManaBlockType;
import vazkii.botania.api.mana.ManaNetworkAction;
import vazkii.botania.api.mana.ManaPool;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntity;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import vazkii.botania.common.block.mana.ManaPoolBlock;
import vazkii.botania.common.handler.ManaNetworkHandler;

@Mixin(ManaPoolBlockEntity.class)
public abstract class ManaPoolBlockEntityMixin extends BotaniaBlockEntity implements ManaPool {

    @Shadow(remap = false)
    private int manaCap;

    protected ManaPoolBlockEntityMixin(BlockPos pos, BlockState state) {
        super(BotaniaBlockEntities.POOL, pos, state);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    private void initManaCapAndNetwork() {
        if (this.manaCap == -1) {
            ManaPoolBlock.Variant variant = ((ManaPoolBlock) getBlockState().getBlock()).variant;
            if (variant == ManaPoolBlock.Variant.DILUTED) {
                manaCap = 10000;
            } else if (variant == ManaPoolBlock.Variant.FABULOUS) {
                manaCap = 1000000000;
            } else {
                manaCap = 1000000;
            }
        }
        if (!ManaNetworkHandler.instance.isPoolIn(level, this) && !isRemoved()) {
            BotaniaAPI.instance().getManaNetworkInstance().fireManaNetworkEvent(this, ManaBlockType.POOL, ManaNetworkAction.ADD);
        }
    }
}
