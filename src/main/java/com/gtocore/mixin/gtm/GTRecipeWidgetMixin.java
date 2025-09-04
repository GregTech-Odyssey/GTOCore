package com.gtocore.mixin.gtm;

import com.gtolib.api.recipe.modifier.RecipeModifierFunction;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.common.recipe.condition.DimensionCondition;
import com.gregtechceu.gtceu.integration.xei.widgets.GTRecipeWidget;

import net.minecraft.nbt.CompoundTag;

import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.ProgressWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import org.apache.commons.lang3.mutable.MutableInt;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

@Mixin(GTRecipeWidget.class)
public abstract class GTRecipeWidgetMixin extends WidgetGroup {

    @Shadow(remap = false)
    protected abstract void setTier(int tier);

    @Shadow(remap = false)
    private int tier;

    @Shadow(remap = false)
    protected abstract void setTierToMin();

    @Shadow(remap = false)
    protected abstract void setRecipeTextWidget(OverclockingLogic logic);

    @Shadow(remap = false)
    public abstract void collectStorage(Table<IO, RecipeCapability<?>, Object> extraTable, Table<IO, RecipeCapability<?>, List<Content>> extraContents, GTRecipe recipe);

    @Shadow(remap = false)
    @Final
    private GTRecipe recipe;

    @Shadow(remap = false)
    public abstract void addSlots(Table<IO, RecipeCapability<?>, List<Content>> contentTable, WidgetGroup group, GTRecipe recipe);

    @Shadow(remap = false)
    @Final
    public static String RECIPE_CONTENT_GROUP_ID;

    @Shadow(remap = false)
    @Final
    public static Pattern RECIPE_CONTENT_GROUP_ID_REGEX;

    @Shadow(remap = false)
    private int yOffset;

    @Shadow(remap = false)
    @Final
    private int xOffset;

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void setRecipeOC(int button, boolean isShiftClick) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            setTier(tier + 1);
        } else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            setTier(tier - 1);
        } else if (button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
            setTierToMin();
        }
        setRecipeTextWidget(new RecipeModifierFunction.Overclocking(isShiftClick));
        setRecipeWidget();
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    private void setRecipeWidget() {
        setClientSideWidget();

        var storages = Tables.newCustomTable(new EnumMap<>(IO.class), LinkedHashMap<RecipeCapability<?>, Object>::new);
        var contents = Tables.newCustomTable(new EnumMap<>(IO.class), LinkedHashMap<RecipeCapability<?>, List<Content>>::new);
        collectStorage(storages, contents, recipe);

        WidgetGroup group = recipe.recipeType.getRecipeUI().createUITemplate(ProgressWidget.JEIProgress, storages,
                recipe.data.copy(), recipe.conditions);
        addSlots(contents, group, recipe);

        var size = group.getSize();

        group.setId(RECIPE_CONTENT_GROUP_ID);
        getWidgetsById(RECIPE_CONTENT_GROUP_ID_REGEX).forEach(this::removeWidget);

        addWidget(group);

        var EUt = recipe.getInputEUt();
        if (EUt == 0) {
            EUt = recipe.getOutputEUt();
        }
        int yOffset = 5 + size.height;
        this.yOffset = yOffset;
        yOffset += EUt > 0 ? 20 : 0;
        if (recipe.data.getBoolean("duration_is_total_cwu")) {
            yOffset -= 10;
        }

        MutableInt yOff = new MutableInt(yOffset);
        for (var capability : recipe.inputs.entrySet()) {
            capability.getKey().addXEIInfo(this, xOffset, recipe, capability.getValue(), false, true, yOff);
        }
        for (var capability : recipe.tickInputs.entrySet()) {
            capability.getKey().addXEIInfo(this, xOffset, recipe, capability.getValue(), true, true, yOff);
        }
        for (var capability : recipe.outputs.entrySet()) {
            capability.getKey().addXEIInfo(this, xOffset, recipe, capability.getValue(), false, false, yOff);
        }
        for (var capability : recipe.tickOutputs.entrySet()) {
            capability.getKey().addXEIInfo(this, xOffset, recipe, capability.getValue(), true, false, yOff);
        }

        for (RecipeCondition condition : recipe.conditions) {
            if (condition.getTooltips() == null) continue;
            if (condition instanceof DimensionCondition dimCondition) {
                addWidget(dimCondition.setupDimensionMarkers(recipe.recipeType.getRecipeUI().getJEISize().width - xOffset - 44, recipe.recipeType.getRecipeUI().getJEISize().height - 32).setBackgroundTexture(IGuiTexture.EMPTY));
            } else addWidget(new LabelWidget(3 - xOffset, yOff.addAndGet(10), condition.getTooltips().getString()));
        }
        for (Function<CompoundTag, String> dataInfo : recipe.recipeType.getDataInfos()) {
            addWidget(new LabelWidget(3 - xOffset, yOff.addAndGet(10), dataInfo.apply(recipe.data)));
        }
        recipe.recipeType.getRecipeUI().appendJEIUI(recipe, this);
    }
}
