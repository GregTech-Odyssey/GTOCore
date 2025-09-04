package com.gtocore.mixin.gtm.registry;

import com.gregtechceu.gtceu.api.data.worldgen.GTOreDefinition;
import com.gregtechceu.gtceu.common.data.GTOres;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.function.Consumer;

@Mixin(GTOres.class)
public class GTOresMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    private static GTOreDefinition create(String name, Consumer<GTOreDefinition> config) {
        return null;
    }
}
