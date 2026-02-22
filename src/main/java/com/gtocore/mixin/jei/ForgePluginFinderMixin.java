package com.gtocore.mixin.jei;

import com.gtolib.api.GTOApi;

import mezz.jei.api.IModPlugin;
import mezz.jei.forge.startup.ForgePluginFinder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(ForgePluginFinder.class)
public abstract class ForgePluginFinderMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static List<IModPlugin> getModPlugins() {
        Map<Object, IModPlugin> plugins = new HashMap<>();
        GTOApi.JEI_PLUGIN_EVENT.call(plugin -> plugins.put(plugin.getClass(), plugin));
        return new ArrayList<>(plugins.values());
    }
}
