package com.gto.gtocore.mixin.adastra;

import earth.terrarium.adastra.common.entities.vehicles.Rocket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Rocket.class)
public class RocketMixin {

    @ModifyConstant(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;Learth/terrarium/adastra/common/entities/vehicles/Rocket$RocketProperties;)V", remap = false, constant = @Constant(longValue = 3000L))
    private long modifyContainer(long constant) {
        return 16000L;
    }

    @ModifyConstant(method = "consumeFuel", remap = false, constant = @Constant(longValue = 1000L))
    private long modifyConsume1(long constant) {
        return 16000L;
    }

    @ModifyConstant(method = "consumeFuel", remap = false, constant = @Constant(longValue = 3000L))
    private long modifyConsume2(long constant) {
        return 16000L;
    }
}
