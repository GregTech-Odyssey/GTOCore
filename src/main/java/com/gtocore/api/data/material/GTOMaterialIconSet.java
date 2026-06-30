package com.gtocore.api.data.material;

import com.gtocore.client.model.ShaderItemModelBuilder;
import com.gtocore.client.renderer.item.*;

import com.gtolib.GTOCore;
import com.gtolib.utils.RLUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.item.component.ICustomRenderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.Item;

import com.google.gson.Gson;
import com.gto.registrate.providers.DataGenContext;
import com.gto.registrate.providers.RegistrateItemModelProvider;
import com.gto.registrate.util.nullness.NonNullBiConsumer;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

@Getter
public final class GTOMaterialIconSet extends MaterialIconSet {

    @Nullable
    private final ICustomRenderer customRenderer;

    private final GTOIconSetModelFactory modelProvider;

    private GTOMaterialIconSet(String name, MaterialIconSet parentIconset, boolean isRootIconset, IRenderer customRenderer) {
        this(name, parentIconset, isRootIconset, customRenderer == null ? null : () -> customRenderer);
    }

    private GTOMaterialIconSet(String name, MaterialIconSet parentIconset, boolean isRootIconset, ICustomRenderer customRenderer) {
        super(name, parentIconset, isRootIconset);
        this.customRenderer = customRenderer;
        this.modelProvider = (t, m) -> (NonNullBiConsumer.noop());
    }

    private GTOMaterialIconSet(String name, MaterialIconSet parentIconset, boolean isRootIconset, IRenderer customRenderer, GTOIconSetModelFactory modelProvider) {
        super(name, parentIconset, isRootIconset);
        this.customRenderer = customRenderer == null ? null : () -> customRenderer;
        this.modelProvider = modelProvider;
    }

    public static final GTOMaterialIconSet AMPROSIUM = new GTOMaterialIconSet("amprosium", METALLIC, false, HaloItemRenderer.WHITE_HALO);
    public static final GTOMaterialIconSet TRANSCENDENT = new GTOMaterialIconSet("transcendent", METALLIC, false, () -> StereoscopicItemRenderer.INSTANCE);
    public static final GTOMaterialIconSet QUANTUM_CHROMO_DYNAMICALLY = new GTOMaterialIconSet("quantum_chromo_dynamically", METALLIC, false, HaloItemRenderer.QUANTUM_CHROMO_DYNAMICALLY_HALO);
    public static final GTOMaterialIconSet COSMIC = new GTOMaterialIconSet("cosmic", METALLIC, false, HaloItemRenderer.COSMIC_HALO);
    public static final GTOMaterialIconSet CHAOS = new GTOMaterialIconSet("chaos", METALLIC, false, HaloItemRenderer.CHAOS_HALO);
    public static final GTOMaterialIconSet CHAOS_INFINITY = new GTOMaterialIconSet("chaos_infinity_old", METALLIC, false, HaloItemRenderer.CHAOS_INFINITY_HALO);
    public static final GTOMaterialIconSet NEUTRONIUM = new GTOMaterialIconSet("neutronium", METALLIC, false, HaloItemRenderer.NEUTRONIUM_HALO);
    public static final GTOMaterialIconSet COSMIC_NEUTRONIUM = new GTOMaterialIconSet("cosmic_neutronium", METALLIC, false, HaloItemRenderer.COSMIC_NEUTRONIUM_HALO);
    public static final GTOMaterialIconSet MAGNETOHYDRODYNAMICALLY_CONSTRAINED_STAR_MATTER = new GTOMaterialIconSet("magnetohydrodynamically_constrained_star_matter", null, true, HaloItemRenderer.MAGNETOHYDRODYNAMICALLY_CONSTRAINED_STAR_MATTER_HALO);
    public static final GTOMaterialIconSet INFINITY = new GTOMaterialIconSet("infinity", null, true, HaloItemRenderer.INFINITY_HALO);
    public static final GTOMaterialIconSet ETERNITY = new GTOMaterialIconSet("eternity", null, true, HaloItemRenderer.ETERNITY_HALO);
    public static final GTOMaterialIconSet MAGMATTER = new GTOMaterialIconSet("magmatter", null, true, HaloItemRenderer.MAGMATTER_HALO);
    public static final MaterialIconSet WHITE_DWARF_MATTER = new MaterialIconSet("white_dwarf_mtter", null, true);
    public static final MaterialIconSet BLACK_DWARF_MATTER = new MaterialIconSet("black_dwarf_mtter", null, true);
    public static final MaterialIconSet WROUGHT_IRON = new MaterialIconSet("wrought_iron", METALLIC);
    public static final MaterialIconSet PARTICLE_EMITTER = new MaterialIconSet("particle_emitter", null, true);
    public static final MaterialIconSet LIMPID = new MaterialIconSet("limpid", DULL);
    public static final GTOMaterialIconSet INFINITY_CHAOS = new GTOMaterialIconSet("chaos_infinity", METALLIC, false, SpinTransformRenderer.INSTANCE);
    public static final GTOMaterialIconSet TRANSLUCENT = new GTOMaterialIconSet("translucent", SHINY, false, TranslucentRenderer.INSTANCE);
    public static final GTOMaterialIconSet ASTRAL = new GTOMaterialIconSet("cosmic_translucent", BRIGHT, false, HaloItemRenderer.ASTRIUM);

    public static final MaterialIconSet CRUPTIX = new GTOMaterialIconSet("cruptix", DULL, false, null,
            (t, m) -> (ctx, provider) -> {
                if (!t.doGenerateBlock()) {
                    provider.generated(ctx.lazy(), getTextureLocation(t, DULL, provider))
                            .customLoader(ShaderItemModelBuilder::begin)
                            .shader(GTOCore.id("cruptix"))
                            .param("resolution", 64f, 64f)
                            .end();
                }
            });
    public static final MaterialIconSet ENDERITE = new GTOMaterialIconSet("enderite", DULL, false, null,
            (t, m) -> (ctx, provider) -> {
                if (t.doGenerateBlock()) {
                    provider.withExistingParent(ctx.getName(), GTCEu.id("block/material_sets/" + DULL.name + "/" + t.materialIconType()))
                            .customLoader(ShaderItemModelBuilder::begin)
                            .shader(GTOCore.id("item_resonance_wave"))
                            .param("maxDistance", 32f)
                            .end();
                } else {
                    provider.generated(ctx.lazy(), getTextureLocation(t, METALLIC, provider))
                            .customLoader(ShaderItemModelBuilder::begin)
                            .shader(GTOCore.id("item_resonance_wave"))
                            .param("maxDistance", 8f)
                            .end();
                }
            });

    private static ResourceLocation[] getTextureLocation(TagPrefix tagPrefix, MaterialIconSet iconSet, RegistrateItemModelProvider provider) {
        ResourceLocation location = GTCEu.id("item/material_sets/" + iconSet.name + "/" + tagPrefix.materialIconType());
        ResourceLocation[] foundLocations = new ResourceLocation[0];
        while (!provider.existingFileHelper.exists(location, PackType.CLIENT_RESOURCES, ".json", "models") && !iconSet.isRootIconset) {
            iconSet = iconSet.parentIconset;
            location = GTCEu.id("item/material_sets/" + iconSet.name + "/" + tagPrefix.materialIconType());
        }
        try {
            var model = provider.existingFileHelper.getResource(location, PackType.CLIENT_RESOURCES, ".json", "models");
            var modelStream = model.openAsReader();
            var content = gson.fromJson(modelStream, Object.class);
            var json = gson.toJsonTree(content).getAsJsonObject();
            var textures = json.getAsJsonObject("textures");
            if (textures != null) {
                foundLocations = textures.entrySet().stream()
                        .filter(ref -> ref.getKey().startsWith("layer"))
                        .filter(ref -> !ref.getValue().getAsString().startsWith("#"))
                        .map(ref -> RLUtils.parse(ref.getValue().getAsString()))
                        .toArray(ResourceLocation[]::new);
            }
            modelStream.close();
        } catch (IOException ignored) {}
        if (foundLocations.length > 0) {
            return foundLocations;
        }
        return new ResourceLocation[] { location };
    }

    public interface GTOIconSetModelFactory {

        NonNullBiConsumer<DataGenContext<Item, Item>, RegistrateItemModelProvider> create(TagPrefix tagPrefix, Material material);
    }

    private static final Gson gson = new Gson();
}
