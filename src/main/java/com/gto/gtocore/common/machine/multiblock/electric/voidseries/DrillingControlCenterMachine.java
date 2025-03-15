package com.gto.gtocore.common.machine.multiblock.electric.voidseries;

import com.gto.gtocore.api.machine.INetMachineInteractor;
import com.gto.gtocore.api.machine.multiblock.ElectricMultiblockMachine;
import com.gto.gtocore.api.machine.trait.CustomRecipeLogic;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.api.recipe.RecipeRunner;
import com.gto.gtocore.client.ClientUtil;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class DrillingControlCenterMachine extends ElectricMultiblockMachine {

    public static final Map<ResourceLocation, Set<DrillingControlCenterMachine>> NETWORK = new Object2ObjectOpenHashMap<>();

    public DrillingControlCenterMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    public double getMultiplier() {
        return Math.pow(1.5, getTier() - GTValues.IV);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        INetMachineInteractor.addToNet(NETWORK, this);
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        INetMachineInteractor.removeFromNet(NETWORK, this);
    }

    @Override
    public void onUnload() {
        super.onUnload();
        INetMachineInteractor.removeFromNet(NETWORK, this);
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        textList.add(ComponentPanelWidget.withButton(Component.translatable("gui.enderio.range.show"), "show"));
    }

    @Override
    public void handleDisplayClick(String componentData, ClickData clickData) {
        if (clickData.isRemote && "show".equals(componentData)) {
            ClientUtil.highlighting(getPos(), 16);
        }
    }

    @Nullable
    private GTRecipe getRecipe() {
        if (!hasProxies() && getTier() < GTValues.IV) return null;
        GTRecipe recipe = GTORecipeBuilder.ofRaw().duration(20).EUt(getOverclockVoltage()).buildRawRecipe();
        if (RecipeRunner.matchTickRecipe(this, recipe)) return recipe;
        return null;
    }

    @Override
    protected @NotNull RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe, true);
    }
}
