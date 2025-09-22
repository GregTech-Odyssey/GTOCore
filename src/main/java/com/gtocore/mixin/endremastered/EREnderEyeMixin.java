package com.gtocore.mixin.endremastered;

import com.teamremastered.endrem.items.EREnderEye;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EREnderEye.class)
public abstract class EREnderEyeMixin  extends Item {

    public EREnderEyeMixin(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }
}
