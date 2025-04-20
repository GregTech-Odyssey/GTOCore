package com.gto.gtocore.api.machine.multiblock;

import com.gto.gtocore.api.machine.feature.multiblock.ICheckPatternMachine;
import com.gto.gtocore.api.machine.feature.multiblock.IEnhancedMultiblockMachine;
import com.gto.gtocore.api.machine.feature.multiblock.IMultiblockTraitHolder;
import com.gto.gtocore.api.machine.trait.IEnhancedRecipeLogic;
import com.gto.gtocore.api.machine.trait.MultiblockTrait;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider;
import com.gregtechceu.gtceu.api.gui.fancy.TooltipsPanel;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDisplayUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.widget.*;
import lombok.Getter;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.ParametersAreNonnullByDefault;

@Getter
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class NoEnergyMultiblockMachine extends WorkableMultiblockMachine implements IFancyUIMachine, IDisplayUIMachine, IEnhancedMultiblockMachine, IMultiblockTraitHolder, ICheckPatternMachine {

    private final List<MultiblockTrait> multiblockTraits = new ArrayList<>(2);

    public NoEnergyMultiblockMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public boolean gtocore$hasButton() {
        return true;
    }

    @Override
    public boolean alwaysTryModifyRecipe() {
        if (getDefinition().isAlwaysTryModifyRecipe()) return true;
        for (MultiblockTrait trait : multiblockTraits) {
            if (trait.alwaysTryModifyRecipe()) return true;
        }
        return false;
    }

    @Override
    @Nullable
    public GTRecipe fullModifyRecipe(@NotNull GTRecipe recipe) {
        recipe = RecipeHelper.trimRecipeOutputs(recipe, getOutputLimits());
        for (MultiblockTrait trait : multiblockTraits) {
            recipe = trait.modifyRecipe(recipe);
            if (recipe == null) return null;
        }
        return doModifyRecipe(recipe);
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        if (recipe == null) return false;
        for (MultiblockTrait trait : multiblockTraits) {
            if (trait.beforeWorking(recipe)) return false;
        }
        return super.beforeWorking(recipe);
    }

    @Override
    public boolean onWorking() {
        for (MultiblockTrait trait : multiblockTraits) {
            if (trait.onWorking()) return false;
        }
        return super.onWorking();
    }

    @Override
    public void afterWorking() {
        for (MultiblockTrait trait : multiblockTraits) {
            trait.afterWorking();
        }
        super.afterWorking();
    }

    @Override
    @MustBeInvokedByOverriders
    public void onPartScan(IMultiPart part) {
        for (MultiblockTrait trait : multiblockTraits) {
            trait.onPartScan(part);
        }
    }

    @Override
    @MustBeInvokedByOverriders
    public void onStructureFormed() {
        super.onStructureFormed();
        multiblockTraits.forEach(MultiblockTrait::onStructureFormed);
    }

    @Override
    @MustBeInvokedByOverriders
    public void onStructureInvalid() {
        super.onStructureInvalid();
        multiblockTraits.forEach(MultiblockTrait::onStructureInvalid);
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        MachineUtils.addMachineText(textList, this, this::customText);
        IDisplayUIMachine.super.addDisplayText(textList);
    }

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        IFancyUIMachine.super.attachConfigurators(configuratorPanel);
        IEnhancedRecipeLogic.attachRecipeLockable(configuratorPanel, getRecipeLogic());
        ICheckPatternMachine.attachConfigurators(configuratorPanel, this);
    }

    @Override
    public Widget createUIWidget() {
        var group = new WidgetGroup(0, 0, 182 + 8, 117 + 8);
        group.addWidget(new DraggableScrollableWidgetGroup(4, 4, 182, 117).setBackground(getScreenTexture())
                .addWidget(new LabelWidget(4, 5, self().getBlockState().getBlock().getDescriptionId()))
                .addWidget(new ComponentPanelWidget(4, 17, this::addDisplayText)
                        .textSupplier(Objects.requireNonNull(getLevel()).isClientSide ? null : this::addDisplayText)
                        .setMaxWidthLimit(200)
                        .clickHandler(this::handleDisplayClick)));
        group.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return group;
    }

    @Override
    public ModularUI createUI(Player entityPlayer) {
        return new ModularUI(198, 208, this, entityPlayer)
                .widget(new FancyMachineUIWidget(this, 198, 208));
    }

    @Override
    public List<IFancyUIProvider> getSubTabs() {
        return getParts().stream()
                .filter(Objects::nonNull)
                .map(IFancyUIProvider.class::cast)
                .toList();
    }

    @Override
    public void attachTooltips(TooltipsPanel tooltipsPanel) {
        for (IMultiPart part : getParts()) {
            part.attachFancyTooltipsToController(this, tooltipsPanel);
        }
    }
}
