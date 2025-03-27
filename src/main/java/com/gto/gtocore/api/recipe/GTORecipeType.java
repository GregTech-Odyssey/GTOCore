package com.gto.gtocore.api.recipe;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.mixin.gtm.api.recipe.GTRecipeTypeAccessor;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.*;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.gui.SteamTexture;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.chance.boost.ChanceBoostFunction;
import com.gregtechceu.gtceu.api.recipe.ui.GTRecipeTypeUI;
import com.gregtechceu.gtceu.api.sound.SoundEntry;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.ItemLike;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class GTORecipeType extends GTRecipeType {

    Map<ResourceLocation, Boolean> filter;

    public GTORecipeType(ResourceLocation registryName, String group, RecipeType<?>... proxyRecipes) {
        super(registryName, group, proxyRecipes);
        setRecipeBuilder(new GTORecipeBuilder(registryName, this));
    }

    @Nullable
    public Iterator<GTRecipe> searchRecipe(IRecipeCapabilityHolder holder, boolean tick) {
        if (!holder.hasProxies()) return null;
        return new SearchRecipeIterator(holder, this, tick);
    }

    @Override
    public Iterator<GTRecipe> searchRecipe(IRecipeCapabilityHolder holder) {
        return searchRecipe(holder, true);
    }

    @Override
    public GTRecipe toGTrecipe(ResourceLocation id, Recipe<?> recipe) {
        var builder = recipeBuilder(id);
        for (var ingredient : recipe.getIngredients()) {
            builder.inputItems(ingredient);
        }
        builder.outputItems(recipe.getResultItem(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY)));
        if (recipe instanceof SmeltingRecipe smeltingRecipe) {
            builder.duration(smeltingRecipe.getCookingTime() / 4);
            builder.EUt(GTValues.VA[GTValues.LV]);
        }
        return builder.buildRawRecipe();
    }

    @Override
    public GTORecipeBuilder recipeBuilder(ResourceLocation id, Object... append) {
        GTORecipeBuilder builder;
        if (append.length > 0) {
            builder = getRecipeBuilder().copy(new ResourceLocation(id.getNamespace(), id.getPath() + Arrays.stream(append).map(Object::toString).map(FormattingUtil::toLowerCaseUnder).reduce("", (a, b) -> a + "_" + b)));
        } else {
            builder = getRecipeBuilder().copy(id);
        }
        if (filter != null && filter.containsKey(id)) {
            builder.deleted = true;
            filter.put(id, true);
        }
        return builder;
    }

    @Override
    public GTORecipeBuilder recipeBuilder(String id, Object... append) {
        return recipeBuilder(GTCEu.id(id), append);
    }

    @Override
    public GTORecipeBuilder recipeBuilder(MaterialEntry entry, Object... append) {
        return recipeBuilder(GTCEu.id(entry.tagPrefix() + (entry.material().isNull() ? "" : "_" + entry.material().getName())), append);
    }

    @Override
    public GTORecipeBuilder recipeBuilder(Supplier<? extends ItemLike> item, Object... append) {
        return recipeBuilder(item.get(), append);
    }

    @Override
    public GTORecipeBuilder recipeBuilder(ItemLike itemLike, Object... append) {
        return recipeBuilder(new ResourceLocation(itemLike.asItem().getDescriptionId()), append);
    }

    @Override
    public GTORecipeBuilder copyFrom(GTRecipeBuilder builder) {
        return getRecipeBuilder().copyFrom(builder);
    }

    @Override
    public GTORecipeType onRecipeBuild(BiConsumer<GTRecipeBuilder, Consumer<FinishedRecipe>> onBuild) {
        getRecipeBuilder().onSave(onBuild);
        return this;
    }

    @Override
    public GTORecipeType setMaxIOSize(int maxInputs, int maxOutputs, int maxFluidInputs, int maxFluidOutputs) {
        return setMaxSize(IO.IN, ItemRecipeCapability.CAP, maxInputs).setMaxSize(IO.IN, FluidRecipeCapability.CAP, maxFluidInputs).setMaxSize(IO.OUT, ItemRecipeCapability.CAP, maxOutputs).setMaxSize(IO.OUT, FluidRecipeCapability.CAP, maxFluidOutputs);
    }

    @Override
    public GTORecipeType setEUIO(IO io) {
        if (io.support(IO.IN)) {
            setMaxSize(IO.IN, EURecipeCapability.CAP, 1);
        }
        if (io.support(IO.OUT)) {
            setMaxSize(IO.OUT, EURecipeCapability.CAP, 1);
        }
        return this;
    }

    @Override
    public GTORecipeType setMaxSize(IO io, RecipeCapability<?> cap, int max) {
        if (io == IO.IN || io == IO.BOTH) {
            maxInputs.put(cap, max);
        }
        if (io == IO.OUT || io == IO.BOTH) {
            maxOutputs.put(cap, max);
        }
        return this;
    }

    @Override
    public GTORecipeType setSlotOverlay(boolean isOutput, boolean isFluid, IGuiTexture slotOverlay) {
        return (GTORecipeType) super.setSlotOverlay(isOutput, isFluid, slotOverlay);
    }

    @Override
    public GTORecipeType setSlotOverlay(boolean isOutput, boolean isFluid, boolean isLast, IGuiTexture slotOverlay) {
        return (GTORecipeType) super.setSlotOverlay(isOutput, isFluid, isLast, slotOverlay);
    }

    @Override
    public GTORecipeType setProgressBar(ResourceTexture progressBar, ProgressTexture.FillDirection moveType) {
        return (GTORecipeType) super.setProgressBar(progressBar, moveType);
    }

    @Override
    public GTORecipeType setSteamProgressBar(SteamTexture progressBar, ProgressTexture.FillDirection moveType) {
        return (GTORecipeType) super.setSteamProgressBar(progressBar, moveType);
    }

    @Override
    public GTORecipeType setUiBuilder(BiConsumer<GTRecipe, WidgetGroup> uiBuilder) {
        return (GTORecipeType) super.setUiBuilder(uiBuilder);
    }

    @Override
    public GTORecipeType setMaxTooltips(int maxTooltips) {
        return (GTORecipeType) super.setMaxTooltips(maxTooltips);
    }

    @Override
    public GTORecipeType setXEIVisible(boolean XEIVisible) {
        return (GTORecipeType) super.setXEIVisible(XEIVisible);
    }

    @Override
    public GTORecipeType addDataInfo(Function<CompoundTag, String> dataInfo) {
        this.dataInfos.add(dataInfo);
        return this;
    }

    @Override
    public GTORecipeType addCustomRecipeLogic(GTRecipeType.ICustomRecipeLogic recipeLogic) {
        return (GTORecipeType) super.addCustomRecipeLogic(recipeLogic);
    }

    @Override
    public GTORecipeType prepareBuilder(Consumer<GTRecipeBuilder> onPrepare) {
        return (GTORecipeType) super.prepareBuilder(onPrepare);
    }

    @Override
    public GTORecipeType setRecipeBuilder(final GTRecipeBuilder recipeBuilder) {
        return (GTORecipeType) super.setRecipeBuilder(recipeBuilder);
    }

    @Override
    public GTORecipeType setChanceFunction(final ChanceBoostFunction chanceFunction) {
        return (GTORecipeType) super.setChanceFunction(chanceFunction);
    }

    @Override
    public GTORecipeType setRecipeUI(final GTRecipeTypeUI recipeUI) {
        return (GTORecipeType) super.setRecipeUI(recipeUI);
    }

    @Override
    public GTORecipeType setSmallRecipeMap(final GTRecipeType smallRecipeMap) {
        return (GTORecipeType) super.setSmallRecipeMap(smallRecipeMap);
    }

    @Override
    public GTORecipeType getSmallRecipeMap() {
        return (GTORecipeType) super.getSmallRecipeMap();
    }

    @Override
    public GTORecipeType setIconSupplier(@Nullable final Supplier<ItemStack> iconSupplier) {
        return (GTORecipeType) super.setIconSupplier(iconSupplier);
    }

    @Override
    public GTORecipeType setSound(@Nullable final SoundEntry sound) {
        this.sound = sound;
        return this;
    }

    @Override
    public GTORecipeType setFuelRecipeType(final boolean isFuelRecipeType) {
        this.isFuelRecipeType = isFuelRecipeType;
        return this;
    }

    @Override
    public GTORecipeType setScanner(final boolean isScanner) {
        this.isScanner = isScanner;
        return this;
    }

    @Override
    public GTORecipeType setHasResearchSlot(final boolean hasResearchSlot) {
        this.hasResearchSlot = hasResearchSlot;
        return this;
    }

    @Override
    public GTORecipeType setOffsetVoltageText(final boolean offsetVoltageText) {
        return (GTORecipeType) super.setOffsetVoltageText(offsetVoltageText);
    }

    @Override
    public GTORecipeType setVoltageTextOffset(final int voltageTextOffset) {
        return (GTORecipeType) super.setVoltageTextOffset(voltageTextOffset);
    }

    public GTORecipeBuilder builder(String id, Object... append) {
        return recipeBuilder(GTOCore.id(id), append);
    }

    public void addFilter(String id) {
        filter.put(GTCEu.id(id), false);
    }

    private GTORecipeBuilder getRecipeBuilder() {
        return (GTORecipeBuilder) ((GTRecipeTypeAccessor) this).getRecipeBuilder();
    }
}
