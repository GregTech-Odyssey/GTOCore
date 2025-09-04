package com.gtocore.mixin.ae2.menu;

import appeng.api.util.IConfigManager;
import org.spongepowered.asm.mixin.gen.Accessor;

@org.spongepowered.asm.mixin.Mixin(appeng.menu.me.common.MEStorageMenu.class)
public interface MEStorageMenuAccessor {

    @Accessor(remap = false)
    IConfigManager getClientCM();
}
