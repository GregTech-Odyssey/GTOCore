package com.gtocore.integration.emi;

import com.gtocore.integration.chisel.ChiselRecipe;
import com.gtocore.integration.emi.oreprocessing.OreProcessingEmiCategory;

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

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.menu.me.items.CraftingTermMenu;
import appeng.menu.me.items.PatternEncodingTermMenu;
import com.arsmeteorites.arsmeteorites.ArsMeteorites;
import com.arsmeteorites.arsmeteorites.emi.MeteoritesEmiPlugin;
import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.emi.EMIPlugin;
import com.lowdragmc.lowdraglib.gui.modular.ModularUIContainer;
import committee.nova.mods.avaritia.Static;
import committee.nova.mods.avaritia.init.compat.emi.AvaritiaEmiPlugin;
import de.mari_023.ae2wtlib.wet.WETMenu;
import dev.emi.emi.VanillaPlugin;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.*;
import dev.emi.emi.registry.EmiPluginContainer;
import io.github.prismwork.emitrades.EMITradesPlugin;
import tfar.craftingstation.CraftingStation;
import tfar.craftingstation.recipeviewers.emi.EmiViewerPlugin;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.integration.emi.BotaniaEmiPlugin;

import java.util.List;

public final class GTEMIPlugin implements EmiPlugin {

    public static void addEMIPlugin(List<EmiPluginContainer> list) {
        list.add(new EmiPluginContainer(new VanillaPlugin(), "emi"));
        if (GTCEu.isProd()) {
            list.add(new EmiPluginContainer(new EMITradesPlugin(), "emitrades"));
            list.add(new EmiPluginContainer(new BackpackEmiPlugin(), "backpack"));
            list.add(new EmiPluginContainer(new EmiViewerPlugin(), CraftingStation.MODID));
        }
        list.add(new EmiPluginContainer(new AvaritiaEmiPlugin(), Static.MOD_ID));
        list.add(new EmiPluginContainer(new BotaniaEmiPlugin(), BotaniaAPI.MODID));
        list.add(new EmiPluginContainer(new EMIPlugin(), LDLib.MOD_ID));
        list.add(new EmiPluginContainer(new GTEMIPlugin(), GTCEu.MOD_ID));
        list.add(new EmiPluginContainer(new MeteoritesEmiPlugin(), ArsMeteorites.MOD_ID));
    }

    public static EmiIngredient genericStackToEmiIngredient(GenericStack stack) {
        AEKey key = stack.what();
        if (key instanceof AEItemKey itemKey) {
            return new ItemEmiStack(itemKey.getItem(), itemKey.getTag(), stack.amount());
        } else if (key instanceof AEFluidKey fluidKey) {
            return new FluidEmiStack(fluidKey.getFluid(), fluidKey.getTag(), stack.amount());
        }
        return EmiStack.EMPTY;
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
        registry.addRecipeHandler(PatternEncodingTermMenu.TYPE, new Ae2PatternTerminalHandler<>());
        registry.addRecipeHandler(WETMenu.TYPE, new Ae2PatternTerminalHandler<>());
        registry.addRecipeHandler(CraftingTermMenu.TYPE, new CraftingTerminalRecipeHandler<>());
        registry.addCategory(GTProgrammedCircuitCategory.CATEGORY);

        GTRecipeEMICategory.registerDisplays(registry);
        OreProcessingEmiCategory.registerDisplays(registry);
        GTOreVeinEmiCategory.registerDisplays(registry);
        GTBedrockFluidEmiCategory.registerDisplays(registry);
        GTProgrammedCircuitCategory.registerDisplays(registry);
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
