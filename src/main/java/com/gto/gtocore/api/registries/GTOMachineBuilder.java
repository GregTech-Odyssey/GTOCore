package com.gto.gtocore.api.registries;

import com.gto.gtocore.common.data.GTORecipeModifiers;
import com.gto.gtocore.data.lang.LangHandler;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.block.IMachineBlock;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.gui.editor.EditableMachineUI;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;
import com.gregtechceu.gtceu.api.registry.registrate.MachineBuilder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import com.tterrag.registrate.Registrate;
import org.apache.commons.lang3.function.TriFunction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class GTOMachineBuilder extends MachineBuilder<MachineDefinition> {

    public static final Map<String, LangHandler.ENCN> TOOLTIPS_MAP = GTCEu.isDataGen() ? new HashMap<>() : null;

    private int tooltipsIndex;

    GTOMachineBuilder(Registrate registrate, String name, Function<ResourceLocation, MachineDefinition> definition, Function<IMachineBlockEntity, MetaMachine> machine, BiFunction<BlockBehaviour.Properties, MachineDefinition, IMachineBlock> blockFactory, BiFunction<IMachineBlock, Item.Properties, MetaMachineItem> itemFactory, TriFunction<BlockEntityType<?>, BlockPos, BlockState, IMachineBlockEntity> blockEntityFactory) {
        super(registrate, name, definition, machine, blockFactory, itemFactory, blockEntityFactory);
    }

    @Override
    public GTOMachineBuilder langValue(String langValue) {
        return (GTOMachineBuilder) super.langValue(langValue);
    }

    @Override
    public GTOMachineBuilder tier(int tier) {
        return (GTOMachineBuilder) super.tier(tier);
    }

    @Override
    public GTOMachineBuilder editableUI(@Nullable EditableMachineUI editableUI) {
        return (GTOMachineBuilder) super.editableUI(editableUI);
    }

    @Override
    public GTOMachineBuilder noRecipeModifier() {
        return (GTOMachineBuilder) super.noRecipeModifier();
    }

    @Override
    public GTOMachineBuilder alwaysTryModifyRecipe(boolean alwaysTryModifyRecipe) {
        return (GTOMachineBuilder) super.alwaysTryModifyRecipe(alwaysTryModifyRecipe);
    }

    @Override
    public GTOMachineBuilder recipeModifier(RecipeModifier recipeModifier) {
        alwaysTryModifyRecipe(true);
        return (GTOMachineBuilder) super.recipeModifier(recipeModifier);
    }

    @Override
    public GTOMachineBuilder recipeModifiers(RecipeModifier... recipeModifiers) {
        alwaysTryModifyRecipe(true);
        return (GTOMachineBuilder) super.recipeModifiers(recipeModifiers);
    }

    @Override
    public GTOMachineBuilder recipeType(GTRecipeType type) {
        return (GTOMachineBuilder) super.recipeType(type);
    }

    @Override
    public GTOMachineBuilder abilities(PartAbility... abilities) {
        return (GTOMachineBuilder) super.abilities(abilities);
    }

    @Override
    public GTOMachineBuilder tooltips(Component... components) {
        return (GTOMachineBuilder) super.tooltips(components);
    }

    @Override
    public GTOMachineBuilder renderer(@Nullable Supplier<IRenderer> renderer) {
        return (GTOMachineBuilder) super.renderer(renderer);
    }

    @Override
    public GTOMachineBuilder tieredHullRenderer(ResourceLocation model) {
        return (GTOMachineBuilder) super.tieredHullRenderer(model);
    }

    @Override
    public GTOMachineBuilder overlayTieredHullRenderer(String name) {
        return (GTOMachineBuilder) super.overlayTieredHullRenderer(name);
    }

    @Override
    public GTOMachineBuilder workableTieredHullRenderer(ResourceLocation workableModel) {
        return (GTOMachineBuilder) super.workableTieredHullRenderer(workableModel);
    }

    @Override
    public GTOMachineBuilder tooltipBuilder(BiConsumer<ItemStack, List<Component>> tooltipBuilder) {
        return (GTOMachineBuilder) super.tooltipBuilder(tooltipBuilder);
    }

    public GTOMachineBuilder nonYAxisRotation() {
        return (GTOMachineBuilder) rotationState(RotationState.NON_Y_AXIS);
    }

    public GTOMachineBuilder allRotation() {
        return (GTOMachineBuilder) rotationState(RotationState.ALL);
    }

    public GTOMachineBuilder noneRotation() {
        return (GTOMachineBuilder) rotationState(RotationState.NONE);
    }

    public GTOMachineBuilder tooltipsKey(String key, Object... args) {
        return (GTOMachineBuilder) tooltips(Component.translatable(key, args));
    }

    public GTOMachineBuilder tooltipsText(String en, String cn, Object... args) {
        String key = "gtocore.machine." + name + ".tooltip." + tooltipsIndex;
        if (TOOLTIPS_MAP != null) TOOLTIPS_MAP.put(key, new LangHandler.ENCN(en, cn));
        tooltipsKey(key, args);
        tooltipsIndex++;
        return this;
    }

    public GTOMachineBuilder overclock() {
        return (GTOMachineBuilder) recipeModifier(GTORecipeModifiers.OVERCLOCKING, false);
    }

    public GTOMachineBuilder perfectOverclock() {
        return (GTOMachineBuilder) recipeModifier(GTORecipeModifiers.PERFECT_OVERCLOCKING, false);
    }
}
