package com.gtocore.mixin.ae2.stacks;

import com.gtolib.GTOCore;
import com.gtolib.IFluid;
import com.gtolib.api.ae2.stacks.IAEFluidKey;
import com.gtolib.api.misc.IMapValueCache;
import com.gtolib.utils.UniqueObject;

import com.gregtechceu.gtceu.api.recipe.lookup.IntIngredientMap;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import appeng.api.stacks.AEFluidKey;
import appeng.util.Platform;
import com.llamalad7.mixinextras.sugar.Local;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(AEFluidKey.class)
public class AEFluidKeyMixin implements IAEFluidKey {

    @Shadow(remap = false)
    @Final
    private Fluid fluid;

    @Mutable
    @Shadow(remap = false)
    @Final
    private @Nullable CompoundTag tag;

    @Unique
    private UniqueObject<CompoundTag> gtocore$tag;

    private FluidStack gtocore$stack;

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/Objects;hash([Ljava/lang/Object;)I"), remap = false)
    private int init(Object[] values) {
        gtocore$tag = UniqueObject.of(tag);
        tag = null;
        return 31 * values[0].hashCode() + gtocore$tag.hashCode();
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean matches(FluidStack variant) {
        return !variant.isEmpty() && fluid.isSame(variant.getFluid()) && Objects.equals(gtocore$tag.o, variant.getTag());
    }

    @Redirect(method = "equals", at = @At(value = "INVOKE", target = "Ljava/util/Objects;equals(Ljava/lang/Object;Ljava/lang/Object;)Z"), remap = false)
    private boolean redirectEquals(Object o1, Object o2, @Local(argsOnly = true) Object o) {
        var t = (AEFluidKeyMixin) o;
        return gtocore$tag == t.gtocore$tag;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    protected Component computeDisplayName() {
        return Platform.getFluidDisplayName(fluid, gtocore$tag.o);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public FluidStack toStack(int amount) {
        return new FluidStack(fluid, amount, gtocore$tag.o);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public @Nullable CompoundTag getTag() {
        return gtocore$tag.o;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public @Nullable CompoundTag copyTag() {
        return gtocore$tag.o;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean hasTag() {
        return gtocore$tag.o != null;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void writeToPacket(FriendlyByteBuf data) {
        data.writeVarInt(BuiltInRegistries.FLUID.getId(fluid));
        data.writeNbt(gtocore$tag.o);
    }

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
    public static AEFluidKey of(FluidStack fluidVariant) {
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

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public CompoundTag toTag() {
        CompoundTag result = new CompoundTag();
        result.putString("id", ((IFluid) fluid).gtolib$getIdString());
        if (gtocore$tag.o != null) {
            result.put("tag", gtocore$tag.o.copy());
        }
        return result;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public String toString() {
        String idString = ((IFluid) fluid).gtolib$getIdString();
        return gtocore$tag.o == null ? idString : idString + " (+tag)";
    }

    @Override
    public FluidStack gtolib$getReadOnlyStack() {
        if (gtocore$stack == null) {
            gtocore$stack = toStack(1);
        } else if (gtocore$stack.isEmpty()) {
            gtocore$stack = null;
            GTOCore.LOGGER.error("Something destroyed the read-only fluidStack of {}", this);
            return gtolib$getReadOnlyStack();
        }
        return gtocore$stack;
    }

    @Override
    public void gtolib$setReadOnlyStack(FluidStack stack) {
        gtocore$stack = stack;
    }

    @Override
    public void gtolib$convert(long amount, IntIngredientMap map) {
        map.add(((IFluid) fluid).gtolib$getMapFluid(), amount);
    }
}
