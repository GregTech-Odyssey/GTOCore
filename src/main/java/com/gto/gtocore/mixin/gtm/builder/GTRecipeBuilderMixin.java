package com.gto.gtocore.mixin.gtm.builder;

import com.gto.gtocore.api.data.tag.ITagPrefix;
import com.gto.gtocore.common.data.GTORecipes;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import com.gregtechceu.gtceu.api.recipe.ResearchData;
import com.gregtechceu.gtceu.api.recipe.category.GTRecipeCategory;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.common.recipe.condition.ResearchCondition;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Mixin(GTRecipeBuilder.class)
public abstract class GTRecipeBuilderMixin {

    @Shadow(remap = false)
    public abstract <T> GTRecipeBuilder input(RecipeCapability<T> capability, T obj);

    @Shadow(remap = false)
    public abstract GTRecipe buildRawRecipe();

    @Shadow(remap = false)
    public ResourceLocation id;

    @Shadow(remap = false)
    public GTRecipeType recipeType;

    @Shadow(remap = false)
    @Final
    public Map<RecipeCapability<?>, List<Content>> input;

    @Shadow(remap = false)
    @Final
    public Map<RecipeCapability<?>, List<Content>> output;

    @Shadow(remap = false)
    @Final
    public Map<RecipeCapability<?>, List<Content>> tickInput;

    @Shadow(remap = false)
    @Final
    public Map<RecipeCapability<?>, List<Content>> tickOutput;

    @Shadow(remap = false)
    @Final
    public List<RecipeCondition> conditions;

    @Shadow(remap = false)
    @NotNull
    public CompoundTag data;

    @Shadow(remap = false)
    public int duration;

    @Shadow(remap = false)
    public GTRecipeCategory recipeCategory;

    @Shadow(remap = false)
    public BiConsumer<GTRecipeBuilder, Consumer<FinishedRecipe>> onSave;

    @Shadow(remap = false)
    public abstract GTRecipeBuilder inputItems(ItemStack input);

    @Inject(method = "inputItems(Lcom/gregtechceu/gtceu/api/data/tag/TagPrefix;Lcom/gregtechceu/gtceu/api/data/chemical/material/Material;I)Lcom/gregtechceu/gtceu/data/recipe/builder/GTRecipeBuilder;", at = @At("HEAD"), remap = false, cancellable = true)
    private void inputItem(TagPrefix orePrefix, Material material, int count, CallbackInfoReturnable<GTRecipeBuilder> cir) {
        if (((ITagPrefix) orePrefix).gtocore$isTagInput()) return;
        cir.setReturnValue(inputItems(ChemicalHelper.get(orePrefix, material, count)));
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public GTRecipeBuilder inputFluids(FluidStack input) {
        return input(FluidRecipeCapability.CAP, FluidIngredient.of(input));
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void save(Consumer<FinishedRecipe> consumer) {
        ResourceLocation typeid = new ResourceLocation(id.getNamespace(), recipeType.registryName.getPath() + "/" + id.getPath());
        if (GTORecipes.GT_FILTER_RECIPES != null && GTORecipes.GT_FILTER_RECIPES.contains(typeid)) return;
        if (onSave != null) {
            onSave.accept((GTRecipeBuilder) (Object) this, consumer);
        }
        if (recipeType.isHasResearchSlot()) {
            ResearchCondition condition = this.conditions.stream().filter(ResearchCondition.class::isInstance).findAny().map(ResearchCondition.class::cast).orElse(null);
            if (condition != null) {
                for (ResearchData.ResearchEntry entry : condition.data) {
                    this.recipeType.addDataStickEntry(entry.getResearchId(), buildRawRecipe());
                }
            }
        }
        GTORecipes.GT_RECIPE_MAP.put(typeid.toString(), new GTRecipe(this.recipeType, typeid, this.input, this.output, this.tickInput, this.tickOutput, Map.of(), Map.of(), Map.of(), Map.of(), this.conditions, List.of(), this.data, this.duration, false, this.recipeCategory));
    }
}
