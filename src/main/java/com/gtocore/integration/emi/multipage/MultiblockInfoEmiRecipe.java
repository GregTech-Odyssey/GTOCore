package com.gtocore.integration.emi.multipage;

import com.gtocore.client.gui.PatternPreview;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.item.OrderItem;

import com.gtolib.GTOCore;
import com.gtolib.api.machine.MultiblockDefinition;
import com.gtolib.utils.FileUtils;
import com.gtolib.utils.ItemUtils;
import com.gtolib.utils.RLUtils;
import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.pattern.predicates.SimplePredicate;
import com.gregtechceu.gtceu.common.data.machines.GTMultiMachines;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import com.lowdragmc.lowdraglib.emi.ModularEmiRecipe;
import com.lowdragmc.lowdraglib.emi.ModularForegroundRenderWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.jei.ModularWrapper;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.ListEmiIngredient;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;

public final class MultiblockInfoEmiRecipe extends ModularEmiRecipe<Widget> {

    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(GTCEu.id("multiblock_info"), EmiStack.of(GTMultiMachines.ELECTRIC_BLAST_FURNACE.asItem())) {

        @Override
        public Component getName() {
            return Component.translatable("gtceu.jei.multiblock_info");
        }
    };

    private static final Widget MULTIBLOCK = new Widget(0, 0, 160, 160);

    public final MultiblockMachineDefinition definition;
    public int i;
    public PatternPreview.MBPattern[] patterns = null;

    public MultiblockInfoEmiRecipe(MultiblockMachineDefinition definition) {
        super(() -> MULTIBLOCK);
        this.definition = definition;
        widget = () -> PatternPreview.getPatternWidget(this, definition);
        Consumer<Collection<Item>> action = p -> inputs.add(new ListEmiIngredient(p.stream().map(EmiStack::of).toList(), 1));
        var file = new File(GTOCore.getFile(), "cache/multiblock/" + definition.getName() + "_parts");
        if (file.exists() && file.canRead()) {
            FileUtils.loadFromFile(file, FileUtils.Deserialize.list(FileUtils.Deserialize.list(dis -> RegistriesUtils.getItem(RLUtils.SERIALIZER.deserialize(dis))))).forEach(action);
        } else {
            var pattern = definition.getPatternFactory().get();
            if (pattern != null && pattern.predicates != null) {
                Collection<Collection<Item>> parts = new ReferenceOpenHashSet<>();
                for (var predicate : pattern.predicates) {
                    ArrayList<SimplePredicate> predicates = new ArrayList<>(predicate.common);
                    predicates.addAll(predicate.limited);
                    for (SimplePredicate simplePredicate : predicates) {
                        if (simplePredicate == null || simplePredicate.candidates == null) continue;
                        Set<Item> items = new ReferenceOpenHashSet<>();
                        for (var itemStack : simplePredicate.getCandidates()) {
                            var item = itemStack.getItem();
                            if (item == Items.AIR) continue;
                            items.add(item);
                        }
                        if (items.size() > 1) parts.add(items);
                    }
                }
                FileUtils.saveToFile(parts, file, FileUtils.Serialize.collection(FileUtils.Serialize.collection((dos, obj) -> RLUtils.SERIALIZER.serialize(dos, ItemUtils.getIdLocation(obj)))));
                parts.forEach(action);
            }
        }
        MultiblockDefinition.of(definition).getPatterns()[0].parts().forEach(i -> super.inputs.add(EmiStack.of(i)));
    }

    public List<EmiIngredient> getInputs(int i) {
        if (patterns != null && i >= 0 && patterns.length > i) {
            return patterns[i].parts.stream().map(EmiStack::of).map(s -> (EmiIngredient) s).toList();
        } else {
            return super.getInputs();
        }
    }

    @Override
    public List<EmiIngredient> getInputs() {
        if (i > 0) {
            return getInputs(i);
        }
        return super.getInputs();
    }

    @Override
    public List<EmiStack> getOutputs() {
        if (definition != null) {
            var stack = definition.asStack();
            if (i > 0) stack.setHoverName(Component.empty()
                    .append(stack.getDisplayName()).append(" ")
                    .append(Component.translatable("gtocore.shape", i)));
            return List.of(EmiStack.of(OrderItem.setTarget(GTOItems.ORDER.asStack(), stack)));
        }
        return super.getOutputs();
    }

    @Override
    public List<Widget> getFlatWidgetCollection(Widget widgetIn) {
        return Collections.emptyList();
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return CATEGORY;
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
        widgets.add(new SlotWidget(EmiStack.of(OrderItem.setTarget(GTOItems.ORDER.asStack(), definition.asStack())), 1000, 1000).recipeContext(this));
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
