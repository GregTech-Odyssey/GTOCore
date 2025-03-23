package com.gto.gtocore.mixin.mc;

import com.gto.gtocore.api.fluid.IFluid;
import com.gto.gtocore.utils.RLUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Fluid.class)
public class FluidMixin implements IFluid {

    @Unique
    private ResourceLocation gtocore$id;

    @Override
    public @NotNull ResourceLocation gtocore$getIdLocation() {
        if (gtocore$id == null) {
            gtocore$id = ForgeRegistries.FLUIDS.getKey((Fluid) (Object) this);
            if (gtocore$id == null) gtocore$id = RLUtils.mc("water");
        }
        return gtocore$id;
    }
}
