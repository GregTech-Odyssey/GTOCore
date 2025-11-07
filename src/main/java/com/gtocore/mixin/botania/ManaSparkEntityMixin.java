package com.gtocore.mixin.botania;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.common.entity.ManaSparkEntity;
import vazkii.botania.common.entity.SparkBaseEntity;

@Mixin(ManaSparkEntity.class)
public abstract class ManaSparkEntityMixin extends SparkBaseEntity {

    @Shadow(remap = false)
    private boolean firstTick;

    public ManaSparkEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lvazkii/botania/common/entity/ManaSparkEntity;getAttachedTile()Lvazkii/botania/api/mana/spark/SparkAttachable;", remap = false), cancellable = true)
    private void tick(CallbackInfo ci) {
        if (this.firstTick || tickCount % 20 == 0) return;
        ci.cancel();
    }

    @ModifyConstant(method = "tick", constant = @Constant(intValue = 1000))
    private int tick_rate(int constant) {
        return 20000;
    }
}
