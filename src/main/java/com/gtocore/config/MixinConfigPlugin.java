package com.gtocore.config;

import com.gtocore.integration.Mods;

import com.gtolib.api.misc.AbstractMixinConfigPlugin;

public final class MixinConfigPlugin extends AbstractMixinConfigPlugin {

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.equals("com.gtocore.mixin.ae2.search.RepoSearchMixin")) {
            return Mods.LANG.isLoaded();
        }
        if (mixinClassName.equals("com.gtocore.mixin.ae2.search.AEKeyMixin")) {
            return Mods.LANG.isLoaded();
        }
        return true;
    }
}
