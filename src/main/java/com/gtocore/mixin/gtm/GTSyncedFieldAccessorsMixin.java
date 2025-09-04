package com.gtocore.mixin.gtm;

import com.gtocore.common.syncdata.GTORecipePayload;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.GTSyncedFieldAccessors;
import com.gregtechceu.gtceu.syncdata.FluidStackPayload;
import com.gregtechceu.gtceu.syncdata.MaterialPayload;
import com.gregtechceu.gtceu.syncdata.VirtualTankAccessor;

import net.minecraftforge.fluids.FluidStack;

import com.lowdragmc.lowdraglib.syncdata.payload.FriendlyBufPayload;
import com.lowdragmc.lowdraglib.syncdata.payload.NbtTagPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import static com.lowdragmc.lowdraglib.syncdata.TypedPayloadRegistries.register;
import static com.lowdragmc.lowdraglib.syncdata.TypedPayloadRegistries.registerSimple;

@Mixin(GTSyncedFieldAccessors.class)
public final class GTSyncedFieldAccessorsMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static void init() {
        register(FriendlyBufPayload.class, FriendlyBufPayload::new, GTSyncedFieldAccessors.GT_RECIPE_TYPE_ACCESSOR, 1000);
        register(NbtTagPayload.class, NbtTagPayload::new, VirtualTankAccessor.INSTANCE, 2);
        registerSimple(MaterialPayload.class, MaterialPayload::new, Material.class, 1);
        registerSimple(GTORecipePayload.class, GTORecipePayload::new, GTRecipe.class, 100);
        registerSimple(FluidStackPayload.class, FluidStackPayload::new, FluidStack.class, -1);
    }
}
