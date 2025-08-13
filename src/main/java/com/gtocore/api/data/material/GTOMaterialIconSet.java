package com.gtocore.api.data.material;

import com.gtocore.client.renderer.item.HaloItemRenderer;
import com.gtocore.client.renderer.item.StereoscopicItemRenderer;

import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.gregtechceu.gtceu.api.item.component.ICustomRenderer;

import com.lowdragmc.lowdraglib.client.renderer.IRenderer;

public final class GTOMaterialIconSet extends MaterialIconSet {

    private final ICustomRenderer customRenderer;

    private GTOMaterialIconSet(String name, MaterialIconSet parentIconset, boolean isRootIconset, IRenderer customRenderer) {
        this(name, parentIconset, isRootIconset, customRenderer == null ? null : () -> customRenderer);
    }

    private GTOMaterialIconSet(String name, MaterialIconSet parentIconset, boolean isRootIconset, ICustomRenderer customRenderer) {
        super(name, parentIconset, isRootIconset);
        this.customRenderer = customRenderer;
    }

    public static final GTOMaterialIconSet AMPROSIUM = new GTOMaterialIconSet("amprosium", METALLIC, false, HaloItemRenderer.WHITE_HALO);
    public static final GTOMaterialIconSet TRANSCENDENT = new GTOMaterialIconSet("transcendent", METALLIC, false, () -> StereoscopicItemRenderer.INSTANCE);
    public static final GTOMaterialIconSet QUANTUM_CHROMO_DYNAMICALLY = new GTOMaterialIconSet("quantum_chromo_dynamically", METALLIC, false, HaloItemRenderer.QUANTUM_CHROMO_DYNAMICALLY_HALO);
    public static final GTOMaterialIconSet COSMIC = new GTOMaterialIconSet("cosmic", METALLIC, false, HaloItemRenderer.COSMIC_HALO);
    public static final GTOMaterialIconSet CHAOS = new GTOMaterialIconSet("chaos", METALLIC, false, HaloItemRenderer.CHAOS_HALO);
    public static final GTOMaterialIconSet CHAOS_INFINITY = new GTOMaterialIconSet("chaos_infinity", METALLIC, false, HaloItemRenderer.CHAOS_INFINITY_HALO);
    public static final GTOMaterialIconSet NEUTRONIUM = new GTOMaterialIconSet("neutronium", METALLIC, false, HaloItemRenderer.NEUTRONIUM_HALO);
    public static final GTOMaterialIconSet COSMIC_NEUTRONIUM = new GTOMaterialIconSet("cosmic_neutronium", METALLIC, false, HaloItemRenderer.COSMIC_NEUTRONIUM_HALO);
    public static final GTOMaterialIconSet MAGNETOHYDRODYNAMICALLY_CONSTRAINED_STAR_MATTER = new GTOMaterialIconSet("magnetohydrodynamically_constrained_star_matter", null, true, HaloItemRenderer.MAGNETOHYDRODYNAMICALLY_CONSTRAINED_STAR_MATTER_HALO);
    public static final GTOMaterialIconSet INFINITY = new GTOMaterialIconSet("infinity", null, true, HaloItemRenderer.INFINITY_HALO);
    public static final GTOMaterialIconSet ETERNITY = new GTOMaterialIconSet("eternity", null, true, HaloItemRenderer.ETERNITY_HALO);
    public static final GTOMaterialIconSet MAGMATTER = new GTOMaterialIconSet("magmatter", null, true, HaloItemRenderer.MAGMATTER_HALO);
    public static final GTOMaterialIconSet PARTICLE_EMITTER = new GTOMaterialIconSet("particle_emitter", null, true, HaloItemRenderer.RADIOACTIVE);
    public static final MaterialIconSet LIMPID = new MaterialIconSet("limpid", DULL);

    public ICustomRenderer getCustomRenderer() {
        return this.customRenderer;
    }
}
