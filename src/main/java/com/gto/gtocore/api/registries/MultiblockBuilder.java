package com.gto.gtocore.api.registries;

import com.gto.gtocore.api.recipe.GTORecipeModifier;
import com.gto.gtocore.api.recipe.JointRecipeType;
import com.gto.gtocore.common.data.GTORecipeModifiers;
import com.gto.gtocore.common.machine.multiblock.steam.LargeSteamParallelMultiblockMachine;
import com.gto.gtocore.data.lang.LangHandler;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.block.IMachineBlock;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.registry.registrate.MultiblockMachineBuilder;
import com.gregtechceu.gtceu.client.util.TooltipHelper;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import com.tterrag.registrate.Registrate;
import org.apache.commons.lang3.function.TriFunction;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class MultiblockBuilder extends MultiblockMachineBuilder {

    public static final Map<String, LangHandler.ENCN> TOOLTIPS_MAP = GTCEu.isDataGen() ? new HashMap<>() : null;

    private int tooltipsIndex;
    private final Set<String> recipes = new LinkedHashSet<>();

    MultiblockBuilder(Registrate registrate, String name, Function<IMachineBlockEntity, ? extends MultiblockControllerMachine> metaMachine, BiFunction<BlockBehaviour.Properties, MultiblockMachineDefinition, IMachineBlock> blockFactory, BiFunction<IMachineBlock, Item.Properties, MetaMachineItem> itemFactory, TriFunction<BlockEntityType<?>, BlockPos, BlockState, IMachineBlockEntity> blockEntityFactory) {
        super(registrate, name, metaMachine, blockFactory, itemFactory, blockEntityFactory);
    }

    @Override
    public MultiblockBuilder langValue(String langValue) {
        return (MultiblockBuilder) super.langValue(langValue);
    }

    @Override
    public MultiblockBuilder tier(int tier) {
        return (MultiblockBuilder) super.tier(tier);
    }

    @Override
    public MultiblockBuilder noRecipeModifier() {
        return (MultiblockBuilder) super.noRecipeModifier();
    }

    @Override
    public MultiblockBuilder alwaysTryModifyRecipe(boolean alwaysTryModifyRecipe) {
        return (MultiblockBuilder) super.alwaysTryModifyRecipe(alwaysTryModifyRecipe);
    }

    public MultiblockBuilder recipeModifier(GTORecipeModifier recipeModifier) {
        alwaysTryModifyRecipe(true);
        return (MultiblockBuilder) super.recipeModifier(recipeModifier);
    }

    public MultiblockBuilder recipeModifiers(GTORecipeModifier... recipeModifiers) {
        if (recipeModifiers.length == 1) return recipeModifier(recipeModifiers[0]);
        alwaysTryModifyRecipe(true);
        return (MultiblockBuilder) super.recipeModifiers(recipeModifiers);
    }

    public MultiblockBuilder generator() {
        return (MultiblockBuilder) generator(true);
    }

    public MultiblockBuilder recipe(GTRecipeType... recipeType) {
        if (recipeType[0] instanceof JointRecipeType jointRecipeType) {
            for (GTRecipeType type : jointRecipeType.getTypes()) {
                recipes.add(type.registryName.getNamespace() + "." + type.registryName.getPath());
            }
        } else if (!Objects.equals(recipeType[0].group, "dummy")) recipes.add(recipeType[0].registryName.getNamespace() + "." + recipeType[0].registryName.getPath());
        return (MultiblockBuilder) recipeTypes(recipeType);
    }

    public MultiblockBuilder existingTooltips(String name, int index, Object... args) {
        return tooltipsKey("gtocore.machine." + name + ".tooltip." + index, args);
    }

    public MultiblockBuilder tooltipsKey(String key, Object... args) {
        return (MultiblockBuilder) tooltips(Component.translatable(key, args));
    }

    public MultiblockBuilder tooltipsText(String en, String cn, Object... args) {
        String key = "gtocore.machine." + name + ".tooltip." + tooltipsIndex;
        if (TOOLTIPS_MAP != null) TOOLTIPS_MAP.put(key, new LangHandler.ENCN(en, cn));
        tooltipsKey(key, args);
        tooltipsIndex++;
        return this;
    }

    public MultiblockBuilder block(Supplier<? extends Block> block) {
        if (!recipes.isEmpty()) tooltipsKey("gtceu.machine.available_recipe_map_" + recipes.size() + ".tooltip", recipes.stream().map(Component::translatable).toArray());
        recipes.clear();
        return (MultiblockBuilder) appearanceBlock(block);
    }

    public MultiblockBuilder nonYAxisRotation() {
        return (MultiblockBuilder) rotationState(RotationState.NON_Y_AXIS).allowExtendedFacing(false);
    }

    public MultiblockBuilder allRotation() {
        return (MultiblockBuilder) rotationState(RotationState.ALL);
    }

    public MultiblockBuilder noneRotation() {
        return (MultiblockBuilder) rotationState(RotationState.NONE).allowExtendedFacing(false).allowFlip(false);
    }

    public MultiblockBuilder customTooltipsBuilder(boolean perfectOC, boolean laser, boolean multipleRecipes) {
        return (MultiblockBuilder) tooltipBuilder((stack, components) -> {
            if (laser) components.add(Component.translatable("gtocore.machine.laser.tooltip").withStyle(style -> style.withColor(TooltipHelper.BLINKING_ORANGE.getCurrent())));
            if (multipleRecipes) components.add(Component.translatable("gtocore.machine.multiple_recipes.tooltip").withStyle(style -> style.withColor(TooltipHelper.BLINKING_CYAN.getCurrent())));
            if (perfectOC && !multipleRecipes) components.add(Component.translatable("gtceu.machine.perfect_oc").withStyle(style -> style.withColor(TooltipHelper.BLINKING_RED.getCurrent())));
        });
    }

    public MultiblockBuilder eutMultiplierTooltips(double multiplier) {
        return tooltipsKey("gtocore.machine.eut_multiplier.tooltip", multiplier);
    }

    public MultiblockBuilder durationMultiplierTooltips(double multiplier) {
        return tooltipsKey("gtocore.machine.duration_multiplier.tooltip", multiplier);
    }

    public MultiblockBuilder coilParallelTooltips() {
        return tooltipsKey("gtocore.machine.coil_parallel");
    }

    public MultiblockBuilder parallelizableTooltips() {
        return tooltipsKey("gtceu.multiblock.parallelizable.tooltip");
    }

    public MultiblockBuilder overclock() {
        return (MultiblockBuilder) recipeModifier(GTORecipeModifiers.OVERCLOCKING, false);
    }

    public MultiblockBuilder perfectOverclock() {
        return (MultiblockBuilder) recipeModifier(GTORecipeModifiers.PERFECT_OVERCLOCKING, false);
    }

    public MultiblockBuilder parallelizableOverclock() {
        return recipeModifier(GTORecipeModifiers.PARALLELIZABLE_OVERCLOCK);
    }

    public MultiblockBuilder parallelizablePerfectOverclock() {
        return recipeModifier(GTORecipeModifiers.PARALLELIZABLE_PERFECT_OVERCLOCK);
    }

    public MultiblockBuilder steamOverclock() {
        return steamOverclock("LV");
    }

    public MultiblockBuilder steamOverclock(String v) {
        tooltipsKey("gtocore.machine.steam.tooltip.0");
        tooltipsKey("gtocore.machine.steam.tooltip.1", v);
        tooltipsKey("gtocore.machine.steam.tooltip.2");
        return recipeModifier(LargeSteamParallelMultiblockMachine.recipeModifier(1.5));
    }
}
