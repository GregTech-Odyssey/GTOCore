package com.gto.gtocore.integration.jade.provider;

import com.gto.gtocore.api.machine.multiblock.CrossRecipeMultiblockMachine;
import com.gto.gtocore.api.machine.multiblock.ElectricMultiblockMachine;
import com.gto.gtocore.common.data.GTORecipeTypes;
import com.gto.gtocore.utils.FluidUtils;
import com.gto.gtocore.utils.GTOUtils;
import com.gto.gtocore.utils.ItemUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.integration.jade.GTElementHelper;
import com.gregtechceu.gtceu.integration.jade.provider.CapabilityBlockProvider;
import com.gregtechceu.gtceu.utils.FluidStackHashStrategy;
import com.gregtechceu.gtceu.utils.ItemStackHashStrategy;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;

import it.unimi.dsi.fastutil.objects.Object2LongOpenCustomHashMap;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.fluid.JadeFluidObject;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.util.FluidTextHelper;

import java.util.*;

public final class RecipeOutputProvider extends CapabilityBlockProvider<RecipeLogic> {

    public RecipeOutputProvider() {
        super(GTCEu.id("recipe_output_info"));
    }

    @Override
    protected @Nullable RecipeLogic getCapability(Level level, BlockPos pos, @Nullable Direction side) {
        return GTCapabilityHelper.getRecipeLogic(level, pos, side);
    }

    @Override
    protected void write(CompoundTag data, RecipeLogic recipeLogic) {
        if (recipeLogic.isWorking()) {
            data.putBoolean("Working", recipeLogic.isWorking());
            if (recipeLogic.getMachine() instanceof ElectricMultiblockMachine machine) {
                if (machine.getRecipeType() == GTORecipeTypes.RANDOM_ORE_RECIPES) return;
                Set<GTRecipe> recipes;
                if (machine instanceof CrossRecipeMultiblockMachine crossRecipeMultiblockMachine) {
                    if (crossRecipeMultiblockMachine.isJadeInfo()) {
                        recipes = crossRecipeMultiblockMachine.getLastRecipes();
                    } else {
                        return;
                    }
                } else {
                    var last = recipeLogic.getLastRecipe();
                    if (last == null) return;
                    recipes = Collections.singleton(last);
                }
                Map<ItemStack, Long> items = new Object2LongOpenCustomHashMap<>(ItemStackHashStrategy.comparingAllButCount());
                Map<FluidStack, Long> fluids = new Object2LongOpenCustomHashMap<>(FluidStackHashStrategy.comparingAllButAmount());
                for (GTRecipe recipe : recipes) {
                    for (ItemStack stack : RecipeHelper.getOutputItems(recipe)) {
                        if (stack != null && !stack.isEmpty()) {
                            items.compute(stack, (key, value) -> {
                                if (value == null) return (long) key.getCount();
                                return value + key.getCount();
                            });
                        }
                    }
                    if (!items.isEmpty()) {
                        ListTag itemTags = new ListTag();
                        items.forEach((stack, value) -> {
                            var nbt = new CompoundTag();
                            nbt.putString("id", ItemUtils.getId(stack));
                            nbt.putLong("Count", value);
                            if (stack.getTag() != null) {
                                nbt.put("tag", stack.getTag().copy());
                            }
                            itemTags.add(nbt);
                        });
                        data.put("OutputItems", itemTags);
                    }

                    for (FluidStack stack : RecipeHelper.getOutputFluids(recipe)) {
                        if (stack != null && !stack.isEmpty()) {
                            fluids.compute(stack, (key, value) -> {
                                if (value == null) return (long) key.getAmount();
                                return value + key.getAmount();
                            });
                        }
                    }
                    if (!fluids.isEmpty()) {
                        ListTag fluidTags = new ListTag();
                        fluids.forEach((stack, value) -> {
                            var nbt = new CompoundTag();
                            nbt.putString("FluidName", FluidUtils.getId(stack.getFluid()));
                            nbt.putLong("Amount", value);
                            if (stack.getTag() != null) {
                                nbt.put("tag", stack.getTag().copy());
                            }
                            fluidTags.add(nbt);
                        });
                        data.put("OutputFluids", fluidTags);
                    }
                }
            }
        }
    }

    @Override
    protected void addTooltip(CompoundTag capData, ITooltip tooltip, Player player, BlockAccessor block,
                              BlockEntity blockEntity, IPluginConfig config) {
        if (capData.getBoolean("Working")) {
            List<CompoundTag> outputItems = new ArrayList<>();
            if (capData.contains("OutputItems", Tag.TAG_LIST)) {
                ListTag itemTags = capData.getList("OutputItems", Tag.TAG_COMPOUND);
                if (!itemTags.isEmpty()) {
                    for (Tag tag : itemTags) {
                        if (tag instanceof CompoundTag tCompoundTag) {
                            outputItems.add(tCompoundTag);
                        }
                    }
                }
            }
            List<CompoundTag> outputFluids = new ArrayList<>();
            if (capData.contains("OutputFluids", Tag.TAG_LIST)) {
                ListTag fluidTags = capData.getList("OutputFluids", Tag.TAG_COMPOUND);
                for (Tag tag : fluidTags) {
                    if (tag instanceof CompoundTag tCompoundTag) {
                        outputFluids.add(tCompoundTag);
                    }
                }
            }
            if (!outputItems.isEmpty() || !outputFluids.isEmpty()) {
                tooltip.add(Component.translatable("gtceu.top.recipe_output"));
            }
            addItemTooltips(tooltip, outputItems);
            addFluidTooltips(tooltip, outputFluids);
        }
    }

    private static void addItemTooltips(ITooltip iTooltip, List<CompoundTag> outputItems) {
        IElementHelper helper = iTooltip.getElementHelper();
        for (CompoundTag tag : outputItems) {
            if (tag != null && !tag.isEmpty()) {
                ItemStack stack = GTOUtils.loadItemStack(tag);
                long count = tag.getLong("Count");
                iTooltip.add(helper.smallItem(stack));
                Component text = Component.literal(" ")
                        .append(String.valueOf(count))
                        .append("× ")
                        .append(getItemName(stack))
                        .withStyle(ChatFormatting.WHITE);
                iTooltip.append(text);
            }
        }
    }

    private static void addFluidTooltips(ITooltip iTooltip, List<CompoundTag> outputFluids) {
        for (CompoundTag tag : outputFluids) {
            if (tag != null && !tag.isEmpty()) {
                FluidStack stack = GTOUtils.loadFluidStack(tag);
                iTooltip.add(GTElementHelper.smallFluid(getFluid(stack)));
                Component text = Component.literal(" ")
                        .append(FluidTextHelper.getUnicodeMillibuckets(tag.getLong("Amount"), true))
                        .append(" ")
                        .append(getFluidName(stack))
                        .withStyle(ChatFormatting.WHITE);
                iTooltip.append(text);

            }
        }
    }

    private static Component getItemName(ItemStack stack) {
        return ComponentUtils.wrapInSquareBrackets(stack.getItem().getDescription()).withStyle(ChatFormatting.WHITE);
    }

    private static Component getFluidName(FluidStack stack) {
        return ComponentUtils.wrapInSquareBrackets(stack.getDisplayName()).withStyle(ChatFormatting.WHITE);
    }

    private static JadeFluidObject getFluid(FluidStack stack) {
        return JadeFluidObject.of(stack.getFluid(), stack.getAmount());
    }
}
