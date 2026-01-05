package com.gtocore.mixin.botania;

import org.spongepowered.asm.mixin.gen.Accessor;

@org.spongepowered.asm.mixin.Mixin(value = vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity.class, remap = false)
public interface GeneratingFlowerBlockEntityAccessor {

    @Accessor
    int getMana();

    @Accessor
    void setMana(int mana);
}
