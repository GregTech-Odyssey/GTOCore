package com.gto.gtocore.utils;

import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public final class GTOResearchManager {

    public static final String RESEARCH_NBT_TAG = "assembly_line_research";
    public static final String RESEARCH_ID_NBT_TAG = "research_id";
    public static final String RESEARCH_ITEM_NBT_TAG = "research_item";
    public static final String RESEARCH_TYPE_NBT_TAG = "research_type";

    @NotNull
    public static ItemStack getDefaultScannerItem() {
        return GTItems.TOOL_DATA_STICK.asStack();
    }

    private GTOResearchManager() {}

    public static void writeResearchToNBT(@NotNull CompoundTag stackCompound, @NotNull String researchId,
                                          @NotNull ItemStack researchItem, GTRecipeType recipeType) {
        CompoundTag compound = new CompoundTag();
        compound.putString(RESEARCH_ID_NBT_TAG, researchId);
        compound.putString(RESEARCH_TYPE_NBT_TAG, recipeType.registryName.toString());
        compound.putString(RESEARCH_ITEM_NBT_TAG, researchItem.toString());
        stackCompound.put(RESEARCH_NBT_TAG, compound);
    }

    public static void createDefaultResearchRecipe(@NotNull GTRecipeBuilder builder,
                                                   Consumer<FinishedRecipe> provider) {
        if (!ConfigHolder.INSTANCE.machines.enableResearch) return;

        for (GTRecipeBuilder.ResearchRecipeEntry entry : builder.researchRecipeEntries()) {
            createDefaultResearchRecipe(builder.recipeType, entry.researchId(), entry.researchStack(),
                    entry.dataStack(), entry.duration(), entry.EUt(), entry.CWUt(), provider);
        }
    }

    public static void createDefaultResearchRecipe(@NotNull GTRecipeType recipeType, @NotNull String researchId,
                                                   @NotNull ItemStack researchItem, @NotNull ItemStack dataItem,
                                                   int duration, int EUt, int CWUt, Consumer<FinishedRecipe> provider) {
        if (!ConfigHolder.INSTANCE.machines.enableResearch) return;

        CompoundTag compound = dataItem.getOrCreateTag();
        writeResearchToNBT(compound, researchId, researchItem, recipeType);

        /*
        下面的配方生成后面要全部改掉
         */

        if (CWUt > 0) {
            GTORecipeTypes.PROFESSIONAL_SCANNER_RECIPES.recipeBuilder(FormattingUtil.toLowerCaseUnderscore(researchId))
                    .inputItems(dataItem.getItem())
                    .inputItems(researchItem)
                    .outputItems(dataItem)
                    .EUt(EUt)
                    .CWUt(CWUt)
                    .totalCWU(duration)
                    .save(provider);
        } else {
            GTRecipeTypes.SCANNER_RECIPES.recipeBuilder(FormattingUtil.toLowerCaseUnderscore(researchId))
                    .inputItems(dataItem.getItem())
                    .inputItems(researchItem)
                    .outputItems(dataItem)
                    .duration(duration)
                    .EUt(EUt)
                    .researchScan(true)
                    .save(provider);
        }
    }
}
