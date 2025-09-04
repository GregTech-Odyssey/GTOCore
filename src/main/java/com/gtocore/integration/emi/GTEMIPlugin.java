package com.gtocore.integration.emi;

import com.gtocore.integration.chisel.ChiselRecipe;
import com.gtocore.integration.emi.oreprocessing.OreProcessingEmiCategory;
import com.gtocore.integration.emi.satellite.SatelliteEmiCategory;

import com.gtolib.api.ae2.me2in1.Me2in1Menu;
import com.gtolib.api.ae2.me2in1.UtilsMiscs;
import com.gtolib.api.ae2.me2in1.Wireless;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.recipe.category.GTRecipeCategory;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.common.data.GTFluids;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.fluid.potion.PotionFluid;
import com.gregtechceu.gtceu.integration.emi.circuit.GTProgrammedCircuitCategory;
import com.gregtechceu.gtceu.integration.emi.multipage.MultiblockInfoEmiCategory;
import com.gregtechceu.gtceu.integration.emi.orevein.GTBedrockFluidEmiCategory;
import com.gregtechceu.gtceu.integration.emi.orevein.GTOreVeinEmiCategory;
import com.gregtechceu.gtceu.integration.emi.recipe.GTRecipeEMICategory;

import net.minecraft.world.item.alchemy.PotionUtils;
import net.p3pp3rf1y.sophisticatedbackpacks.compat.recipeviewers.emi.BackpackEmiPlugin;

import appeng.menu.me.items.PatternEncodingTermMenu;
import com.arsmeteorites.arsmeteorites.ArsMeteorites;
import com.arsmeteorites.arsmeteorites.emi.MeteoritesEmiPlugin;
import com.enderio.base.common.integrations.jei.EnderIOJEI;
import com.enderio.machines.common.integrations.jei.MachinesJEI;
import com.hepdd.ae2emicraftingforge.Ae2EmiCraftingMod;
import com.hepdd.ae2emicraftingforge.client.Ae2EmiPlugin;
import com.hepdd.ae2emicraftingforge.client.handler.Ae2CraftingHandler;
import com.hepdd.ae2emicraftingforge.client.handler.Ae2PatternTerminalHandler;
import com.hollingsworth.arsnouveau.client.jei.JEIArsNouveauPlugin;
import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.emi.EMIPlugin;
import com.lowdragmc.lowdraglib.gui.modular.ModularUIContainer;
import committee.nova.mods.avaritia.Static;
import committee.nova.mods.avaritia.init.compat.emi.AvaritiaEmiPlugin;
import de.mari_023.ae2wtlib.wct.WCTMenu;
import de.mari_023.ae2wtlib.wet.WETMenu;
import dev.emi.emi.VanillaPlugin;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.jemi.JemiPlugin;
import dev.emi.emi.registry.EmiPluginContainer;
import dev.shadowsoffire.apotheosis.ench.compat.EnchJEIPlugin;
import dev.shadowsoffire.apotheosis.potion.compat.PotionJEIPlugin;
import dev.shadowsoffire.apotheosis.village.compat.VillageJEIPlugin;
import io.github.lounode.extrabotany.api.ExtraBotanyAPI;
import io.github.lounode.extrabotany.client.integration.emi.EmiExtrabotanyPlugin;
import io.github.prismwork.emitrades.EMITradesPlugin;
import jeresources.jei.JEIConfig;
import mezz.jei.api.IModPlugin;
import mezz.jei.library.plugins.jei.JeiInternalPlugin;
import mythicbotany.jei.MythicJei;
import umpaz.farmersrespite.integration.jei.JEIFRPlugin;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.integration.emi.BotaniaEmiPlugin;

import java.util.List;

public final class GTEMIPlugin implements EmiPlugin {

    public static void addJEIPlugin(List<IModPlugin> list) {
        list.add(new mezz.jei.library.plugins.vanilla.VanillaPlugin());
        list.add(new JeiInternalPlugin());
        list.add(new EnderIOJEI());
        list.add(new MachinesJEI());
        list.add(new JemiPlugin());
        list.add(new EnchJEIPlugin());
        list.add(new PotionJEIPlugin());
        list.add(new VillageJEIPlugin());
        list.add(new JEIConfig());
        list.add(new MythicJei());
        list.add(new JEIFRPlugin());
        list.add(new JEIArsNouveauPlugin());
        list.add(new vectorwing.farmersdelight.integration.jei.JEIPlugin());
    }

    public static void addEMIPlugin(List<EmiPluginContainer> list) {
        list.add(new EmiPluginContainer(new VanillaPlugin(), "emi"));
        if (GTCEu.isProd()) {
            list.add(new EmiPluginContainer(new EMITradesPlugin(), "emitrades"));
            list.add(new EmiPluginContainer(new BackpackEmiPlugin(), "backpack"));
        }
        list.add(new EmiPluginContainer(new AvaritiaEmiPlugin(), Static.MOD_ID));
        list.add(new EmiPluginContainer(new BotaniaEmiPlugin(), BotaniaAPI.MODID));
        list.add(new EmiPluginContainer(new EmiExtrabotanyPlugin(), ExtraBotanyAPI.MODID));
        list.add(new EmiPluginContainer(new EMIPlugin(), LDLib.MOD_ID));
        list.add(new EmiPluginContainer(new GTEMIPlugin(), GTCEu.MOD_ID));
        list.add(new EmiPluginContainer(new MeteoritesEmiPlugin(), ArsMeteorites.MOD_ID));
        list.add(new EmiPluginContainer(new Ae2EmiPlugin(), Ae2EmiCraftingMod.MOD_ID));
    }

    @Override
    public void register(EmiRegistry registry) {
        if (GTCEu.isProd()) ChiselRecipe.register(registry);

        registry.addCategory(MultiblockInfoEmiCategory.CATEGORY);
        registry.addCategory(OreProcessingEmiCategory.CATEGORY);
        registry.addCategory(GTOreVeinEmiCategory.CATEGORY);
        registry.addCategory(GTBedrockFluidEmiCategory.CATEGORY);
        for (GTRecipeCategory category : GTRegistries.RECIPE_CATEGORIES) {
            if (GTCEu.isDev() || category.isXEIVisible()) {
                registry.addCategory(GTRecipeEMICategory.CATEGORIES.apply(category));
            }
        }
        registry.addRecipeHandler(ModularUIContainer.MENUTYPE, new GTEmiRecipeHandler());
        registry.addRecipeHandler(Me2in1Menu.TYPE, UtilsMiscs.createEMI2in1());
        registry.addRecipeHandler(Wireless.TYPE, UtilsMiscs.createEMIWireless());
        registry.addRecipeHandler(PatternEncodingTermMenu.TYPE, new GTAe2PatternTerminalHandler<>());
        registry.addRecipeHandler(WETMenu.TYPE, new GTAe2PatternTerminalHandler<>());
        registry.addRecipeHandler(WCTMenu.TYPE, new Ae2CraftingHandler<>(WCTMenu.class));
        registry.addRecipeHandler(WETMenu.TYPE, new Ae2PatternTerminalHandler<>(WETMenu.class));
        registry.addRecipeHandler(PatternEncodingTermMenu.TYPE, new Ae2PatternTerminalHandler<>(PatternEncodingTermMenu.class));
        registry.addCategory(GTProgrammedCircuitCategory.CATEGORY);

        GTRecipeEMICategory.registerDisplays(registry);
        OreProcessingEmiCategory.registerDisplays(registry);
        GTOreVeinEmiCategory.registerDisplays(registry);
        GTBedrockFluidEmiCategory.registerDisplays(registry);
        GTProgrammedCircuitCategory.registerDisplays(registry);

        SatelliteEmiCategory.register(registry);

        GTRecipeEMICategory.registerWorkStations(registry);
        GTOreVeinEmiCategory.registerWorkStations(registry);
        GTBedrockFluidEmiCategory.registerWorkStations(registry);
        registry.setDefaultComparison(GTItems.PROGRAMMED_CIRCUIT.asItem(), Comparison.compareNbt());

        Comparison potionComparison = Comparison.compareData(stack -> PotionUtils.getPotion(stack.getNbt()));
        PotionFluid potionFluid = GTFluids.POTION.get();
        registry.setDefaultComparison(potionFluid.getSource(), potionComparison);
        registry.setDefaultComparison(potionFluid.getFlowing(), potionComparison);
    }
}
