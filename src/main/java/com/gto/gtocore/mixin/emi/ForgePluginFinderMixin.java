package com.gto.gtocore.mixin.emi;

import appeng.integration.modules.jei.JEIPlugin;
import com.enderio.base.common.integrations.jei.EnderIOJEI;
import com.enderio.machines.common.integrations.jei.MachinesJEI;
import dev.emi.emi.jemi.JemiPlugin;
import dev.shadowsoffire.apotheosis.ench.compat.EnchJEIPlugin;
import dev.shadowsoffire.apotheosis.potion.compat.PotionJEIPlugin;
import dev.shadowsoffire.apotheosis.village.compat.VillageJEIPlugin;
import jeresources.jei.JEIConfig;
import mezz.jei.api.IModPlugin;
import mezz.jei.forge.startup.ForgePluginFinder;
import mezz.jei.library.plugins.jei.JeiInternalPlugin;
import mezz.jei.library.plugins.vanilla.VanillaPlugin;
import mythicbotany.jei.MythicJei;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import umpaz.farmersrespite.integration.jei.JEIFRPlugin;

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
        plugins.add(new VanillaPlugin());
        plugins.add(new JeiInternalPlugin());
        plugins.add(new JEIPlugin());
        plugins.add(new EnderIOJEI());
        plugins.add(new MachinesJEI());
        plugins.add(new JemiPlugin());
        plugins.add(new EnchJEIPlugin());
        plugins.add(new PotionJEIPlugin());
        plugins.add(new VillageJEIPlugin());
        plugins.add(new JEIConfig());
        plugins.add(new MythicJei());
        plugins.add(new vectorwing.farmersdelight.integration.jei.JEIPlugin());
        plugins.add(new JEIFRPlugin());
        return plugins;
    }
}
