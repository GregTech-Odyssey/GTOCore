package com.gtocore.mixin.jei;

import com.gtocore.config.GTOConfig;
import com.gtocore.integration.emi.GTEMIPlugin;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.forge.startup.ForgePluginFinder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ForgePluginFinder.class)
public abstract class ForgePluginFinderMixin {

    @Shadow(remap = false)
    private static <T> List<T> getInstances(Class<?> annotationClass, Class<T> instanceClass) {
        throw new IllegalStateException("This should never be called");
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static List<IModPlugin> getModPlugins() {
        ObjectOpenCustomHashSet<IModPlugin> plugins = new ObjectOpenCustomHashSet<>(new Hash.Strategy<>() {

            @Override
            public int hashCode(IModPlugin iModPlugin) {
                return iModPlugin.getClass().hashCode();
            }

            @Override
            public boolean equals(IModPlugin iModPlugin, IModPlugin k1) {
                if (iModPlugin == null || k1 == null) return iModPlugin == k1;
                return iModPlugin.getClass() == k1.getClass();
            }
        });
        GTEMIPlugin.addJEIPlugin(plugins);
        if (GTOConfig.INSTANCE.misc.enableEmiJeiExternalPlugins) plugins.addAll(getInstances(JeiPlugin.class, IModPlugin.class));
        return Lists.newArrayList(plugins);
    }
}
