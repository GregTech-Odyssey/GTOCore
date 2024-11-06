package com.gto.gtocore.api.data.chemical.material.info;

import com.gto.gtocore.client.renderer.item.StereoscopicItemRenderer;

import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.gregtechceu.gtceu.api.item.component.ICustomRenderer;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class GTOMaterialIconSet extends MaterialIconSet {

    private final ICustomRenderer customRenderer;

    public GTOMaterialIconSet(@NotNull String name,
                              @Nullable MaterialIconSet parentIconset,
                              boolean isRootIconset,
                              ICustomRenderer customRenderer) {
        super(name, parentIconset, isRootIconset);
        this.customRenderer = customRenderer;
    }

    public static final GTOMaterialIconSet CUSTOM_TRANSCENDENT_MENTAL = new GTOMaterialIconSet(
            "transcendent_mental",
            MaterialIconSet.METALLIC,
            false,
            () -> StereoscopicItemRenderer.INSTANCE);

    public static final MaterialIconSet LIMPID = new MaterialIconSet("limpid", DULL);
}
