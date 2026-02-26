package com.gtocore.mixin.ae2.search;

import com.gtocore.integration.ae.hooks.IMoreLangCache;

import net.minecraft.network.chat.Component;

import appeng.api.stacks.AEKey;
import com.ref.moremorelang.lang.ComponentTranslator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(value = AEKey.class, remap = false)
public abstract class AEKeyMixin implements IMoreLangCache {

    @Shadow
    public abstract Component getDisplayName();

    @Unique
    private final Map<String, String> gtocore$translationCache = new ConcurrentHashMap<>();

    @Override
    @Unique
    public String gtocore$getTranslatedLower(String langCode) {
        return gtocore$translationCache.computeIfAbsent(langCode, (code) -> {
            Component base = this.getDisplayName();
            return ComponentTranslator.translateComponent(base, code).getString().toLowerCase();
        });
    }
}
