package com.gtocore.mixin.ae2.stacks;

import com.gtolib.IFluid;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

import appeng.api.stacks.AEFluidKey;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AEFluidKey.class)
public class AEFluidKeyMixin {

    @Shadow(remap = false)
    @Final
    private Fluid fluid;

    @Shadow(remap = false)
    @Final
    private @Nullable CompoundTag tag;

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static AEFluidKey of(Fluid fluid, @Nullable CompoundTag tag) {
        if (tag == null || tag.isEmpty()) {
            return ((IFluid) fluid).gtolib$getAEKey();
        }
        return AEFluidKeyInvoker.of(fluid, tag.copy());
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
        return AEFluidKeyInvoker.of(fluid, tag);
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
        if (tag != null) {
            result.put("tag", tag.copy());
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
        return tag == null ? idString : idString + " (+tag)";
    }
}
