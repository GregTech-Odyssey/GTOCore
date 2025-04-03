package com.gto.gtocore.integration.jei;

import com.gto.gtocore.GTOCore;

import com.gregtechceu.gtceu.GTCEu;

import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.p3pp3rf1y.sophisticatedbackpacks.client.gui.BackpackScreen;
import net.p3pp3rf1y.sophisticatedbackpacks.client.gui.BackpackSettingsScreen;
import net.p3pp3rf1y.sophisticatedbackpacks.common.gui.BackpackContainer;
import net.p3pp3rf1y.sophisticatedcore.client.gui.SettingsScreen;
import net.p3pp3rf1y.sophisticatedcore.compat.jei.CraftingContainerRecipeTransferHandlerBase;
import net.p3pp3rf1y.sophisticatedcore.compat.jei.SettingsGhostIngredientHandler;
import net.p3pp3rf1y.sophisticatedcore.compat.jei.StorageGhostIngredientHandler;

import appeng.integration.modules.jei.JEIPlugin;
import com.enderio.base.common.integrations.jei.EnderIOJEI;
import com.enderio.machines.common.integrations.jei.MachinesJEI;
import dev.emi.emi.jemi.JemiPlugin;
import dev.shadowsoffire.apotheosis.ench.compat.EnchJEIPlugin;
import dev.shadowsoffire.apotheosis.potion.compat.PotionJEIPlugin;
import dev.shadowsoffire.apotheosis.village.compat.VillageJEIPlugin;
import jeresources.jei.JEIConfig;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.helpers.IStackHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.library.plugins.jei.JeiInternalPlugin;
import mythicbotany.jei.MythicJei;
import org.jetbrains.annotations.NotNull;
import umpaz.farmersrespite.integration.jei.JEIFRPlugin;

import java.util.ArrayList;
import java.util.List;

public final class GTJEIPlugin implements IModPlugin {

    public static void addJEIPlugin(List<IModPlugin> list) {
        list.add(new GTJEIPlugin());
        list.add(new mezz.jei.library.plugins.vanilla.VanillaPlugin());
        list.add(new JeiInternalPlugin());
        list.add(new JEIPlugin());
        list.add(new EnderIOJEI());
        list.add(new MachinesJEI());
        list.add(new JemiPlugin());
        list.add(new EnchJEIPlugin());
        list.add(new PotionJEIPlugin());
        list.add(new VillageJEIPlugin());
        list.add(new JEIConfig());
        list.add(new MythicJei());
        list.add(new JEIFRPlugin());
        list.add(new de.mari_023.ae2wtlib.reijei.JEIPlugin());
        list.add(new vectorwing.farmersdelight.integration.jei.JEIPlugin());
    }

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return GTOCore.id("jei_plugin");
    }

    @Override
    public void registerGuiHandlers(@NotNull IGuiHandlerRegistration registration) {
        if (GTCEu.isDev()) return;
        registration.addGuiContainerHandler(BackpackScreen.class, new BackpackScreenIGuiContainerHandler());
        registration.addGuiContainerHandler(BackpackSettingsScreen.class, new BackpackSettingsScreenIGuiContainerHandler());
        registration.addGhostIngredientHandler(BackpackScreen.class, new StorageGhostIngredientHandler<>());
        registration.addGhostIngredientHandler(SettingsScreen.class, new SettingsGhostIngredientHandler<>());
    }

    @Override
    public void registerRecipeTransferHandlers(@NotNull IRecipeTransferRegistration registration) {
        if (GTCEu.isDev()) return;
        IRecipeTransferHandlerHelper handlerHelper = registration.getTransferHelper();
        IStackHelper stackHelper = registration.getJeiHelpers().getStackHelper();
        registration.addRecipeTransferHandler(new BackpackContainerCraftingRecipeCraftingContainerRecipeTransferHandlerBase(handlerHelper, stackHelper), RecipeTypes.CRAFTING);
        registration.addRecipeTransferHandler(new BackpackContainerSmithingRecipeCraftingContainerRecipeTransferHandlerBase(handlerHelper, stackHelper), RecipeTypes.SMITHING);
    }

    private static class BackpackScreenIGuiContainerHandler implements IGuiContainerHandler<BackpackScreen> {

        public @NotNull List<Rect2i> getGuiExtraAreas(BackpackScreen gui) {
            List<Rect2i> ret = new ArrayList<>();
            gui.getUpgradeSlotsRectangle().ifPresent(ret::add);
            ret.addAll(gui.getUpgradeSettingsControl().getTabRectangles());
            gui.getSortButtonsRectangle().ifPresent(ret::add);
            return ret;
        }
    }

    private static class BackpackSettingsScreenIGuiContainerHandler implements IGuiContainerHandler<BackpackSettingsScreen> {

        @Override
        public @NotNull List<Rect2i> getGuiExtraAreas(BackpackSettingsScreen gui) {
            return new ArrayList<>(gui.getSettingsTabControl().getTabRectangles());
        }
    }

    private static class BackpackContainerCraftingRecipeCraftingContainerRecipeTransferHandlerBase extends CraftingContainerRecipeTransferHandlerBase<BackpackContainer, CraftingRecipe> {

        private BackpackContainerCraftingRecipeCraftingContainerRecipeTransferHandlerBase(IRecipeTransferHandlerHelper handlerHelper, IStackHelper stackHelper) {
            super(handlerHelper, stackHelper);
        }

        @Override
        public @NotNull Class<BackpackContainer> getContainerClass() {
            return BackpackContainer.class;
        }

        @Override
        public @NotNull RecipeType<CraftingRecipe> getRecipeType() {
            return RecipeTypes.CRAFTING;
        }
    }

    private static class BackpackContainerSmithingRecipeCraftingContainerRecipeTransferHandlerBase extends CraftingContainerRecipeTransferHandlerBase<BackpackContainer, SmithingRecipe> {

        private BackpackContainerSmithingRecipeCraftingContainerRecipeTransferHandlerBase(IRecipeTransferHandlerHelper handlerHelper, IStackHelper stackHelper) {
            super(handlerHelper, stackHelper);
        }

        @Override
        public @NotNull Class<BackpackContainer> getContainerClass() {
            return BackpackContainer.class;
        }

        @Override
        public @NotNull RecipeType<SmithingRecipe> getRecipeType() {
            return RecipeTypes.SMITHING;
        }
    }
}
