package com.gtocore.api.accelerator.particle;

import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.extension.RecipeExtension;
import com.gregtechceu.gtceu.api.recipe.handler.IO;
import com.gregtechceu.gtceu.api.recipe.handler.IRecipeHandlerHolder;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;

import com.fast.recipesearch.IntLongMap;
import com.gto.datasynclib.DataSyncCodec;
import com.gto.datasynclib.datasream.codec.DataCodec;
import com.gto.datasynclib.datasream.codec.DataDecoder;
import com.gto.datasynclib.datasream.codec.DataEncoder;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class ParticleRecipeExtension extends RecipeExtension<List<ParticleBeam>> {

    public static final ParticleRecipeExtension INSTANCE = new ParticleRecipeExtension();

    private ParticleRecipeExtension() {
        super("particle", DataSyncCodec.of(DataCodec.of(DataEncoder.collection(ParticleBeam.DATA_CODEC), DataDecoder.list(ParticleBeam.DATA_CODEC))), false);
    }

    @Override
    public boolean handle(IO io, @NotNull IRecipeHandlerHolder holder, @Nullable RecipeHandlerUnit unit, @NotNull GTRecipe recipe, boolean simulate) {
        return false;
    }

    @Override
    public void extractInput(GTRecipeDefinition recipe, IntLongMap map) {
        var list = recipe.data.getData(INSTANCE);
        if (list == null) return;
        list.forEach(p -> map.add(p.getDefinition().ingredientId(), p.getAmount()));
    }

    @Override
    public long getParallel(IRecipeHandlerHolder holder, RecipeHandlerUnit unit, GTRecipe recipe, long parallel) {
        return 0;
    }

    @Override
    public void setParallel(GTRecipe recipe, long parallel) {}

    @Override
    public void addInfo(GTRecipeDefinition recipe, WidgetGroup group, int xOffset, MutableInt yOffset) {}

    @Override
    public int getInfoHeight(GTRecipeDefinition recipe) {
        return 0;
    }
}
