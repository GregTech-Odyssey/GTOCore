package com.gtocore.mixin.ae2.menu;

import appeng.api.util.IConfigManager;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@org.spongepowered.asm.mixin.Mixin(appeng.menu.me.common.MEStorageMenu.class)
public interface MEStorageMenuAccessor {

    @Accessor(remap = false)
    IConfigManager getClientCM();

    @Invoker(remap = false)
    void callUpdatePowerStatus();
}
