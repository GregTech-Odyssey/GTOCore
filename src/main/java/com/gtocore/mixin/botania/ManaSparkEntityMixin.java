package com.gtocore.mixin.botania;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.common.entity.ManaSparkEntity;
import vazkii.botania.common.entity.SparkBaseEntity;

@Mixin(ManaSparkEntity.class)
public abstract class ManaSparkEntityMixin extends SparkBaseEntity {

    @Shadow(remap = false)
    private boolean firstTick;

    // 修#1417的补丁，后续要是botania自己修了就把这个删了就行，下面同理
    @Shadow(remap = false)
    public abstract ManaReceiver getAttachedManaReceiver();

    public ManaSparkEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    // 修#1417的补丁
    @Invoker(value = "dropAndKill", remap = false)
    protected abstract void gtocore$dropAndKill();

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lvazkii/botania/common/entity/ManaSparkEntity;getAttachedTile()Lvazkii/botania/api/mana/spark/SparkAttachable;", remap = false), cancellable = true)
    private void tick(CallbackInfo ci) {
        if (this.firstTick || tickCount % 20 == 0) return;
        ci.cancel();
    }

    // 修#1417的补丁
    @Inject(method = "updateTransfers", at = @At("HEAD"), remap = false, cancellable = true)
    private void gtocore$dropBrokenSparkBeforeTransferUpdate(CallbackInfo ci) {
        if (getAttachedManaReceiver() != null) return;
        gtocore$dropAndKill();
        ci.cancel();
    }

    // 修#1417的补丁
    @Inject(method = "tick", at = @At(value = "INVOKE_ASSIGN", target = "Lvazkii/botania/common/entity/ManaSparkEntity;getAttachedManaReceiver()Lvazkii/botania/api/mana/ManaReceiver;", remap = false), cancellable = true)
    private void gtocore$dropSparkWhenReceiverMissing(CallbackInfo ci) {
        if (getAttachedManaReceiver() != null) return;
        gtocore$dropAndKill();
        ci.cancel();
    }

    @ModifyConstant(method = "tick", constant = @Constant(intValue = 1000))
    private int tick_rate(int constant) {
        return 20000;
    }
}
