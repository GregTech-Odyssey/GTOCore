package com.gto.gtocore.integration.emi.multipage;

import com.gto.gtocore.api.machine.IMultiblockMachineDefinition;
import com.gto.gtocore.client.gui.PatternPreview;

import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

import com.lowdragmc.lowdraglib.emi.ModularEmiRecipe;
import com.lowdragmc.lowdraglib.emi.ModularForegroundRenderWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.jei.ModularWrapper;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.WidgetHolder;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class MultiblockInfoEmiRecipe extends ModularEmiRecipe<Widget> {

    private static final Widget MULTIBLOCK = new Widget(0, 0, 160, 160);

    private final MultiblockMachineDefinition definition;

    MultiblockInfoEmiRecipe(MultiblockMachineDefinition definition) {
        super(() -> MULTIBLOCK);
        this.definition = definition;
        inputs = null;
        widget = () -> PatternPreview.getPatternWidget(definition);
    }

    @Override
    public List<EmiIngredient> getInputs() {
        if (inputs == null) {
            inputs = new ArrayList<>();
            ((IMultiblockMachineDefinition) definition).gtocore$getPatterns()[0].parts().forEach(i -> inputs.add(EmiIngredient.of(Ingredient.of(i))));
        }
        return inputs;
    }

    @Override
    public List<Widget> getFlatWidgetCollection(Widget widgetIn) {
        return List.of();
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return MultiblockInfoEmiCategory.CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return definition.getId();
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        var widget = this.widget.get();
        var modular = new ModularWrapper<>(widget);
        modular.setRecipeWidget(0, 0);

        synchronized (CACHE_OPENED) {
            CACHE_OPENED.add(modular);
        }
        widgets.add(new CustomModularEmiRecipe(modular, List.of()));
        widgets.add(new ModularForegroundRenderWidget(modular));
    }

    @Override
    public void addTempWidgets(WidgetHolder widgets) {
        if (TEMP_CACHE != null) {
            TEMP_CACHE.modularUI.triggerCloseListeners();
            TEMP_CACHE = null;
        }

        PatternPreview widget = (PatternPreview) this.widget.get();
        ModularWrapper<PatternPreview> modular = new ModularWrapper<>(widget);
        modular.setRecipeWidget(0, 0);
        widgets.add(new CustomModularEmiRecipe(modular, List.of()));
        TEMP_CACHE = modular;
    }
}
