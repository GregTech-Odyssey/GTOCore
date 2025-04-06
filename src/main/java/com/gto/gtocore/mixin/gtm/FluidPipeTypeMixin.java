package com.gto.gtocore.mixin.gtm;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.client.model.PipeModel;
import com.gregtechceu.gtceu.common.pipelike.fluidpipe.FluidPipeType;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FluidPipeType.class)
public class FluidPipeTypeMixin {

    @Shadow(remap = false)
    @Final
    public float thickness;

    @Shadow(remap = false)
    @Final
    public String name;

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public PipeModel createPipeModel(Material material) {
        if (material == GTOMaterials.SpaceTime) {
            return new PipeModel(thickness, () -> GTOCore.id("block/material_sets/spacetime/pipe_side"),
                    () -> GTOCore.id("block/material_sets/spacetime/pipe_%s_in".formatted(name)), null, null);
        }
        if (material.hasProperty(PropertyKey.WOOD)) {
            return new PipeModel(thickness, () -> GTCEu.id("block/pipe/pipe_side_wood"), () -> GTCEu.id("block/pipe/pipe_%s_in_wood".formatted(name)), null, null);
        }
        return new PipeModel(thickness, () -> GTCEu.id("block/pipe/pipe_side"), () -> GTCEu.id("block/pipe/pipe_%s_in".formatted(name)), null, null);
    }
}
