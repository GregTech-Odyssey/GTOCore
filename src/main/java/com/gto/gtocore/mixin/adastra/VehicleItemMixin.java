package com.gto.gtocore.mixin.adastra;

import earth.terrarium.adastra.common.items.vehicles.VehicleItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(VehicleItem.class)
public class VehicleItemMixin {

    @ModifyConstant(method = "getFluidContainer(Lnet/minecraft/world/item/ItemStack;)Learth/terrarium/botarium/common/fluid/impl/WrappedItemFluidContainer;", remap = false, constant = @Constant(longValue = 3000L))
    private long modifyContainer(long constant) {
        return 16000L;
    }
}
