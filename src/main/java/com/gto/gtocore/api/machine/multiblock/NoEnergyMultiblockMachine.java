package com.gto.gtocore.api.machine.multiblock;

import com.gto.gtocore.api.machine.feature.ICheckPatternMachine;
import com.gto.gtocore.api.machine.feature.IEnhancedMultiblockMachine;
import com.gto.gtocore.api.machine.feature.ILockableRecipe;
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

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.widget.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class NoEnergyMultiblockMachine extends WorkableMultiblockMachine implements IFancyUIMachine, IDisplayUIMachine, IEnhancedMultiblockMachine {

    private final Set<MultiblockTrait> multiblockTraits = new LinkedHashSet<>(2);

    public NoEnergyMultiblockMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    protected void addTraits(MultiblockTrait trait) {
        multiblockTraits.add(trait);
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
        recipe = recipe.trimRecipeOutputs(getOutputLimits());
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
    public void onStructureFormed() {
        super.onStructureFormed();
        multiblockTraits.forEach(MultiblockTrait::onStructureFormed);
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        multiblockTraits.forEach(MultiblockTrait::onStructureInvalid);
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        MachineUtils.addMachineText(textList, this, this::customText);
        IDisplayUIMachine.super.addDisplayText(textList);
    }

    protected void customText(@NotNull List<Component> textList) {
        multiblockTraits.forEach(trait -> trait.customText(textList));
    }

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        IFancyUIMachine.super.attachConfigurators(configuratorPanel);
        ILockableRecipe.attachRecipeLockable(configuratorPanel, getRecipeLogic());
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
