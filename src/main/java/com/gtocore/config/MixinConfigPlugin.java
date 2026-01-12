package com.gtocore.config;

import com.gtolib.api.misc.AbstractMixinConfigPlugin;

public final class MixinConfigPlugin extends AbstractMixinConfigPlugin {

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return !"com.gtocore.mixin.eae.GuiExPatternTerminalMixinWithJech".equals(mixinClassName) && !"com.gtocore.mixin.eae.GuiExPatternTerminalMixinWithoutJech".equals(mixinClassName);
    }
}
