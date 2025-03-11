package com.gto.gtocore.integration.emi;

import com.gto.gtocore.integration.chisel.ChiselRecipe;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.recipe.category.GTRecipeCategory;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.common.data.GTFluids;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.fluid.potion.PotionFluid;
import com.gregtechceu.gtceu.common.fluid.potion.PotionFluidHelper;
import com.gregtechceu.gtceu.integration.emi.circuit.GTProgrammedCircuitCategory;
import com.gregtechceu.gtceu.integration.emi.multipage.MultiblockInfoEmiCategory;
import com.gregtechceu.gtceu.integration.emi.orevein.GTBedrockFluidEmiCategory;
import com.gregtechceu.gtceu.integration.emi.orevein.GTOreVeinEmiCategory;
import com.gregtechceu.gtceu.integration.emi.recipe.GTEmiRecipeHandler;
import com.gregtechceu.gtceu.integration.emi.recipe.GTRecipeEMICategory;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.fluids.FluidStack;

import appeng.menu.me.items.PatternEncodingTermMenu;
import com.lowdragmc.lowdraglib.gui.modular.ModularUIContainer;
import de.mari_023.ae2wtlib.wet.WETMenu;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiStack;

public final class GTEMIPlugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        if (GTCEu.isProd()) ChiselRecipe.register(registry);
        registry.addCategory(MultiblockInfoEmiCategory.CATEGORY);
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
        registry.addCategory(GTProgrammedCircuitCategory.CATEGORY);

        GTRecipeEMICategory.registerDisplays(registry);
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
        for (Potion potion : BuiltInRegistries.POTION) {
            FluidStack stack = PotionFluidHelper.getFluidFromPotion(potion, PotionFluidHelper.BOTTLE_AMOUNT);
            registry.addEmiStack(EmiStack.of(stack.getFluid(), stack.getTag()));
        }
    }
}
