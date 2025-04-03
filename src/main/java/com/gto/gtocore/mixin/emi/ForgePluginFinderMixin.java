package com.gto.gtocore.mixin.emi;

import com.gto.gtocore.integration.jei.GTJEIPlugin;

import mezz.jei.api.IModPlugin;
import mezz.jei.forge.startup.ForgePluginFinder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.ArrayList;
import java.util.List;

@Mixin(ForgePluginFinder.class)
public final class ForgePluginFinderMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static List<IModPlugin> getModPlugins() {
        List<IModPlugin> plugins = new ArrayList<>();
        GTJEIPlugin.addJEIPlugin(plugins);
        return plugins;
    }
}
