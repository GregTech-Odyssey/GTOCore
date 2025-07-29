package com.gtocore.integration.emi.multipage;

import com.gtocore.client.gui.PatternPreview;

import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.pattern.predicates.SimplePredicate;
import com.gregtechceu.gtceu.integration.emi.multipage.MultiblockInfoEmiCategory;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import com.lowdragmc.lowdraglib.emi.ModularEmiRecipe;
import com.lowdragmc.lowdraglib.emi.ModularForegroundRenderWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.jei.ModularWrapper;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.WidgetHolder;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class MultiblockInfoEmiRecipe extends ModularEmiRecipe<Widget> {

    private static final Widget MULTIBLOCK = new Widget(0, 0, 160, 160);

    private final MultiblockMachineDefinition definition;

    public MultiblockInfoEmiRecipe(MultiblockMachineDefinition definition) {
        super(() -> MULTIBLOCK);
        this.definition = definition;
        widget = () -> PatternPreview.getPatternWidget(definition);
        this.inputs = getParts(definition.getPatternFactory().get());
    }

    private List<EmiIngredient> getParts(BlockPattern pattern) {
        HashSet<TraceabilityPredicate> predicateMap = new HashSet<>();

        for (var layer : pattern.blockMatches) {
            for (var aisle : layer) {
                predicateMap.addAll(Arrays.asList(aisle));
            }
        }

        List<List<ItemStack>> parts = new ArrayList<>();
        for (var predicate : predicateMap) {
            if (predicate == null) continue;
            List<SimplePredicate> predicates = new ArrayList<>();
            predicates.addAll(predicate.common);
            predicates.addAll(predicate.limited);
            predicates.removeIf(p -> p == null || p.candidates == null);
            for (SimplePredicate simplePredicate : predicates) {
                List<ItemStack> itemStacks = simplePredicate.getCandidates();
                if (!itemStacks.isEmpty()) {
                    parts.add(simplePredicate.getCandidates());
                }
            }
        }

        return parts.stream().map(p -> EmiIngredient.of(Ingredient.of(p.stream()))).toList();
    }

    @Override
    public List<Widget> getFlatWidgetCollection(Widget widgetIn) {
        return Collections.emptyList();
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
        widgets.add(new CustomModularEmiRecipe(modular, Collections.emptyList()));
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
        widgets.add(new CustomModularEmiRecipe(modular, Collections.emptyList()));
        TEMP_CACHE = modular;
    }
}
