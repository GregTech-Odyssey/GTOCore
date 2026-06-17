package com.gtocore.integration.jade.provider;

import com.gtocore.common.data.GTORecipeTypes;

import com.gtolib.api.machine.feature.multiblock.ICrossRecipeMachine;
import com.gtolib.api.recipe.ContentBuilder;
import com.gtolib.utils.FluidUtils;
import com.gtolib.utils.GTOUtils;
import com.gtolib.utils.ItemUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.ItemIngredient;
import com.gregtechceu.gtceu.integration.jade.GTElementHelper;
import com.gregtechceu.gtceu.integration.jade.provider.CapabilityBlockProvider;

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

import com.gto.fastcollection.O2LOpenCustomCacheHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenCustomHashMap;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.fluid.JadeFluidObject;
import snownee.jade.api.ui.IElementHelper;

import java.util.ArrayList;
import java.util.List;

public final class RecipeOutputProvider extends CapabilityBlockProvider<RecipeLogic> {

    public RecipeOutputProvider() {
        super(GTCEu.id("recipe_output_info"));
    }

    @Override
    protected @Nullable RecipeLogic getCapability(Level level, BlockPos pos, BlockEntity blockEntity, @Nullable Direction side) {
        return GTCapabilityHelper.getRecipeLogic(blockEntity);
    }

    @Override
    protected void write(CompoundTag data, RecipeLogic recipeLogic) {
        if (recipeLogic.isWorking()) {
            data.putBoolean("Working", recipeLogic.isWorking());
            if (recipeLogic.machine.getRecipeType() == GTORecipeTypes.RANDOM_ORE_RECIPES) return;
            Object2LongOpenCustomHashMap<Content<ItemIngredient>> items = new O2LOpenCustomCacheHashMap<>(ContentBuilder.HASH_STRATEGY);
            Object2LongOpenCustomHashMap<Content<FluidIngredient>> fluids = new O2LOpenCustomCacheHashMap<>(ContentBuilder.HASH_STRATEGY);
            if (recipeLogic.machine instanceof ICrossRecipeMachine recipeMachine && !recipeMachine.getThreads().isEmpty()) {
                recipeMachine.getThreads().forEach(t -> {
                    var recipe = t.getRecipe();
                    for (var content : recipe.itemOutputs) {
                        items.addTo(content, content.amount);
                    }
                    for (var content : recipe.fluidOutputs) {
                        fluids.addTo(content, content.amount);
                    }
                });
            } else {
                var recipe = recipeLogic.getLastRecipe();
                if (recipe == null) return;
                for (var content : recipe.itemOutputs) {
                    items.addTo(content, content.amount);
                }

                for (var content : recipe.fluidOutputs) {
                    fluids.addTo(content, content.amount);
                }
            }
            if (!items.isEmpty()) {
                ListTag itemTags = new ListTag();
                items.object2LongEntrySet().forEach(entry -> {
                    var nbt = new CompoundTag();
                    var ingredient = entry.getKey().inner;
                    var stack = ingredient.getInnerItemStack();
                    if (stack.isEmpty()) return;
                    nbt.putInt("c", entry.getKey().chance);
                    nbt.putString("id", ItemUtils.getId(stack));
                    nbt.putLong("a", entry.getLongValue());
                    if (stack.hasTag()) {
                        nbt.put("tag", stack.getTag());
                    }
                    itemTags.add(nbt);
                });
                data.put("OutputItems", itemTags);
            }
            if (!fluids.isEmpty()) {
                ListTag fluidTags = new ListTag();
                fluids.object2LongEntrySet().forEach(entry -> {
                    var nbt = new CompoundTag();
                    var ingredient = entry.getKey().inner;
                    var fluid = ingredient.getFluid();
                    if (fluid == null) return;
                    nbt.putInt("c", entry.getKey().chance);
                    nbt.putString("FluidName", FluidUtils.getId(fluid));
                    nbt.putLong("a", entry.getLongValue());
                    if (ingredient.nbt != null) {
                        nbt.put("tag", ingredient.nbt);
                    }
                    fluidTags.add(nbt);
                });
                data.put("OutputFluids", fluidTags);
            }
        }
    }

    @Override
    protected void addTooltip(CompoundTag capData, ITooltip tooltip, Player player, BlockAccessor block, BlockEntity blockEntity, IPluginConfig config) {
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
                int chance = tag.getInt("c");
                boolean estimated = chance < ContentBuilder.maxChance;
                long count = tag.getLong("a");
                if (estimated) {
                    count = Math.max(1, count * chance / ContentBuilder.maxChance);
                }
                iTooltip.add(helper.smallItem(stack));
                Component text = Component.literal(" ")
                        .append((estimated ? "~" : "") + count)
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
                int chance = tag.getInt("c");
                boolean estimated = chance < ContentBuilder.maxChance;
                long count = tag.getLong("a");
                if (estimated) {
                    count = Math.max(1, count * chance / ContentBuilder.maxChance);
                }
                iTooltip.add(GTElementHelper.smallFluid(getFluid(stack)));
                Component text = Component.literal(" ")
                        .append((estimated ? "~" : "") + FluidUtils.getUnicodeMillibuckets(count))
                        .append(" ")
                        .append(getFluidName(stack))
                        .withStyle(ChatFormatting.WHITE);
                iTooltip.append(text);

            }
        }
    }

    private static Component getItemName(ItemStack stack) {
        return stack.getDisplayName().copy().withStyle(ChatFormatting.WHITE);
    }

    private static Component getFluidName(FluidStack stack) {
        return ComponentUtils.wrapInSquareBrackets(stack.getDisplayName()).withStyle(ChatFormatting.WHITE);
    }

    private static JadeFluidObject getFluid(FluidStack stack) {
        return JadeFluidObject.of(stack.getFluid(), stack.getAmount());
    }
}
