package com.gto.gtocore.api.data;

import com.gto.gtocore.data.TagsHandler;

import com.gregtechceu.gtceu.api.data.worldgen.IWorldGenLayer;
import com.gregtechceu.gtceu.api.data.worldgen.WorldGeneratorUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static com.gto.gtocore.api.data.GTODimensions.ALL_LAYER_DIMENSION;

public final class GTOWorldGenLayers implements IWorldGenLayer {

    public static final GTOWorldGenLayers ALL_LAYER = new GTOWorldGenLayers("all_layer", new TagMatchTest(TagsHandler.ALL_LAYER_STONE), ALL_LAYER_DIMENSION.keySet());

    private final String id;
    private final RuleTest target;
    private final Set<ResourceLocation> levels;

    private GTOWorldGenLayers(String id, RuleTest target, Set<ResourceLocation> levels) {
        this.id = id;
        this.target = target;
        this.levels = levels;
        WorldGeneratorUtils.WORLD_GEN_LAYERS.put(id, this);
    }

    @Override
    public Set<ResourceLocation> getLevels() {
        return levels;
    }

    @Override
    public RuleTest getTarget() {
        return target;
    }

    @Override
    public boolean isApplicableForLevel(ResourceLocation level) {
        return levels.contains(level);
    }

    @Override
    public @NotNull String getSerializedName() {
        return id;
    }

    public static void init() {}
}
