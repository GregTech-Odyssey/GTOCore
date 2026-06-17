package com.gtocore.mixin.botania;

import com.gtocore.integration.botania.IEntropinnyum;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.common.block.flower.generating.EntropinnyumBlockEntity;

@Mixin(value = EntropinnyumBlockEntity.class, remap = false)
public abstract class EntropinnyumBlockEntityMixin extends GeneratingFlowerBlockEntity implements IEntropinnyum {

    @Unique
    private int gto$maxMana = 6500;
    @Unique
    private static final int gto$maxManaOrigin = 6500;

    public EntropinnyumBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite
    public int getMaxMana() {
        return gto$maxMana;
    }

    @Override
    public void gto$fillExceededMaxMana(int amount) {
        if (amount <= gto$maxManaOrigin) {
            addMana(amount);
            return;
        }
        gto$maxMana = amount;
        addMana(amount);
    }

    @Inject(method = "tickFlower", at = @At("TAIL"))
    private void gto$resetMaxMana(CallbackInfo ci) {
        if (getMana() <= gto$maxMana) {
            gto$maxMana = Math.max(gto$maxManaOrigin, getMana());
        }
    }

    @Override
    public void sync() {
        super.sync();
    }
}
