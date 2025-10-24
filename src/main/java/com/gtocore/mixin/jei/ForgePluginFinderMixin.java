package com.gtocore.mixin.jei;

import com.gtocore.integration.emi.GTEMIPlugin;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mezz.jei.api.IModPlugin;
import mezz.jei.forge.startup.ForgePluginFinder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;

@Mixin(ForgePluginFinder.class)
public final class ForgePluginFinderMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static List<IModPlugin> getModPlugins() {
        List<IModPlugin> plugins = new ObjectArrayList<>();
        GTEMIPlugin.addJEIPlugin(plugins);
        return plugins;
    }
}
