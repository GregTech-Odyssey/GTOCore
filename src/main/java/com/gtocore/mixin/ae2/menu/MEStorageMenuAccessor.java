package com.gtocore.mixin.ae2.menu;

import org.spongepowered.asm.mixin.gen.Invoker;

@org.spongepowered.asm.mixin.Mixin(appeng.menu.me.common.MEStorageMenu.class)
public interface MEStorageMenuAccessor {

    @Invoker(remap = false)
    void callUpdatePowerStatus();
}
