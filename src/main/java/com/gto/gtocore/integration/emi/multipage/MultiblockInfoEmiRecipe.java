package com.gto.gtocore.integration.emi.multipage;

import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gto.gtocore.api.gui.PatternSlotWidget;
import com.gto.gtocore.client.gui.PatternPreview;
import com.lowdragmc.lowdraglib.emi.ModularEmiRecipe;
import com.lowdragmc.lowdraglib.emi.ModularForegroundRenderWidget;
import com.lowdragmc.lowdraglib.gui.ingredient.IRecipeIngredientSlot;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import com.lowdragmc.lowdraglib.jei.ModularWrapper;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.TankWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class MultiblockInfoEmiRecipe extends ModularEmiRecipe<PatternPreview> {

    private final MultiblockMachineDefinition definition;

    MultiblockInfoEmiRecipe(MultiblockMachineDefinition definition) {
        super(() -> PatternPreview.getPatternWidget(definition));
        this.definition = definition;
    }

    @Override
    public List<Widget> getFlatWidgetCollection(PatternPreview widgetIn) {
        inputs = widgetIn.getScrollableWidgetGroup().widgets.stream().map(p -> ((PatternSlotWidget) p).ingredient).toList();
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
        List<dev.emi.emi.api.widget.Widget> slots = new ArrayList<>();
        for (Widget w : getFlatWidgetCollection(widget)) {
            if (w instanceof IRecipeIngredientSlot slot) {
                if (w.getParent() instanceof DraggableScrollableWidgetGroup draggable && draggable.isUseScissor()) {
                    // don't add the EMI widget at all if we have a draggable group, let the draggable widget handle it instead.
                    continue;
                }
                var io = slot.getIngredientIO();
                if (io != null && io != IngredientIO.RENDER_ONLY) {
                    //noinspection unchecked
                    var ingredients = EmiIngredient.of((List<? extends EmiIngredient>) (List<?>) slot.getXEIIngredients());

                    SlotWidget slotWidget = null;
                    // Clear the LDLib slots & add EMI slots based on them.
                    if (slot instanceof com.lowdragmc.lowdraglib.gui.widget.SlotWidget slotW) {
                        clearSlotWidgetHandler(slotW, 0);
                    } else if (slot instanceof com.lowdragmc.lowdraglib.gui.widget.TankWidget tankW) {
                        clearTankWidgetHandler(tankW);
                        long capacity = Math.max(1, ingredients.getAmount());
                        slotWidget = new TankWidget(ingredients, w.getPosition().x, w.getPosition().y, w.getSize().width, w.getSize().height, capacity);
                    }
                    if (slotWidget == null) {
                        slotWidget = new SlotWidget(ingredients, w.getPosition().x, w.getPosition().y);
                    }

                    slotWidget.customBackground(null, w.getPosition().x, w.getPosition().y, w.getSize().width, w.getSize().height)
                            .drawBack(false);
                    if (io == IngredientIO.CATALYST) {
                        slotWidget.catalyst(true);
                    } else if (io == IngredientIO.OUTPUT) {
                        slotWidget.recipeContext(this);
                    }
                    for (Component component : w.getTooltipTexts()) {
                        slotWidget.appendTooltip(component);
                    }
                    slots.add(slotWidget);
                }
            }
        }
        widgets.add(new CustomModularEmiRecipe(modular, slots));
        slots.forEach(widgets::add);
        widgets.add(new ModularForegroundRenderWidget(modular));
    }

    @Override
    public void addTempWidgets(WidgetHolder widgets) {
        if (TEMP_CACHE != null) {
            TEMP_CACHE.modularUI.triggerCloseListeners();
            TEMP_CACHE = null;
        }

        PatternPreview widget = this.widget.get();
        ModularWrapper<PatternPreview> modular = new ModularWrapper<>(widget);
        modular.setRecipeWidget(0, 0);
        widgets.add(new CustomModularEmiRecipe(modular, new ArrayList<>()));
        TEMP_CACHE = modular;
    }
}
