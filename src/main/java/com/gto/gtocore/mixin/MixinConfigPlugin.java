package com.gto.gtocore.mixin;

import com.gregtechceu.gtceu.GTCEu;

import com.lowdragmc.lowdraglib.core.mixins.MixinPluginShared;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinConfigPlugin implements IMixinConfigPlugin {

    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.contains("com.gto.gtocore.mixin.emi")) {
            return MixinPluginShared.isClassFound("dev.emi.emi.api.EmiPlugin");
        } else if (mixinClassName.contains("com.gto.gtocore.mixin.jei")) {
            return MixinPluginShared.isClassFound("mezz.jei.api.IModPlugin");
        } else if (mixinClassName.equals("com.gto.gtocore.mixin.mc.OreConfigurationMixin")) {
            return GTCEu.isProd();
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
