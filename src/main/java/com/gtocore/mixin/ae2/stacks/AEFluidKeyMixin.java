package com.gtocore.mixin.ae2.stacks;

import com.gtolib.IFluid;
import com.gtolib.api.ae2.stacks.IAEFluidKey;
import com.gtolib.api.misc.IMapValueCache;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import appeng.api.stacks.AEFluidKey;
import com.fast.recipesearch.IntLongMap;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AEFluidKey.class)
public class AEFluidKeyMixin implements IAEFluidKey {

    @Shadow(remap = false)
    @Final
    private Fluid fluid;

    @Shadow(remap = false)
    private @Nullable FluidStack readOnlyStack;

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static AEFluidKey of(Fluid fluid, @Nullable CompoundTag tag) {
        if (tag == null || tag.isEmpty()) {
            return ((IFluid) fluid).gtolib$getAEKey();
        }
        return IMapValueCache.FLUID_KEY_CACHE.get(new FluidStack(fluid, 1, tag));
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static AEFluidKey of(Fluid fluid) {
        return ((IFluid) fluid).gtolib$getAEKey();
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static @Nullable AEFluidKey of(FluidStack fluidVariant) {
        if (fluidVariant.isEmpty()) {
            return null;
        }
        var fluid = fluidVariant.getFluid();
        var tag = fluidVariant.getTag();
        if (tag == null || tag.isEmpty()) {
            return ((IFluid) fluid).gtolib$getAEKey();
        }
        return IMapValueCache.FLUID_KEY_CACHE.get(fluidVariant);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static AEFluidKey fromPacket(FriendlyByteBuf data) {
        var fluid = BuiltInRegistries.FLUID.byId(data.readVarInt());
        var tag = data.readNbt();
        if (tag == null || tag.isEmpty()) {
            return ((IFluid) fluid).gtolib$getAEKey();
        }
        return IMapValueCache.FLUID_KEY_CACHE.get(new FluidStack(fluid, 1, tag));
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public ResourceLocation getId() {
        return ((IFluid) fluid).gtolib$getIdLocation();
    }

    @Override
    public void gtolib$setReadOnlyStack(FluidStack stack) {
        readOnlyStack = stack;
    }

    @Override
    public void gtolib$convert(long amount, IntLongMap map) {
        map.add(((IFluid) fluid).gtolib$getMapFluid(), amount);
    }
}
